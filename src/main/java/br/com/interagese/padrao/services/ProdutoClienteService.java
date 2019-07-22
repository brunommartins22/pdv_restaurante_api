/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.padrao.services;

import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.padrao.rest.util.TransformNativeQuery;
import br.com.interagese.syscontabil.domains.DominioRegras;
import br.com.interagese.syscontabil.domains.DominioValidacaoProduto;
import br.com.interagese.syscontabil.models.Cliente;
import br.com.interagese.syscontabil.models.ProdutoCliente;
import br.com.interagese.syscontabil.models.ProdutoGeral;
import br.com.interagese.syscontabil.models.RegraNcm;
import br.com.interagese.syscontabil.models.RegraProduto;
import br.com.interagese.syscontabil.models.RegraRegimeTributario;
import br.com.interagese.syscontabil.models.TributoEstadualHistorico;
import br.com.interagese.syscontabil.models.TributoEstadualPadrao;
import br.com.interagese.syscontabil.models.TributoFederalHistorico;
import br.com.interagese.syscontabil.models.TributoFederalPadrao;
import br.com.interagese.syscontabil.temp.ClienteProdutoTemp;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bruno Martins
 */
@Service
public class ProdutoClienteService extends PadraoService<ProdutoCliente> {

    @Autowired
    private RegraProdutoService regraProdutoService;
    @Autowired
    private RegraRegimeService regraRegimeTributarioService;
    @Autowired
    private RegraNcmService regraNcmService;
    @Autowired
    private ProdutoGeralService produtoGeralService;

    public List<ClienteProdutoTemp> loadProductClient() throws Exception {

        String sql = "SELECT "
                + "  c.id AS CODIGO,"
                + "  c.tipo_regime as REGIME,"
                + "  c.cpf_cnpj AS CPF_CNPJ,"
                + "  c.razao_social AS CLIENTE,"
                + "  (SELECT count(*) from syscontabil.produto_cliente pc WHERE pc.cliente_id = c.id AND pc.status='PENDENTE') AS PENDENTE,"
                + "  (SELECT COUNT(*) FROM syscontabil.produto_cliente PC WHERE PC.cliente_id = C.ID AND pc.status='VALIDADO') AS VALIDADO "
                + "FROM syscontabil.cliente c ";

        List<Object[]> result = em.createNativeQuery(sql).getResultList();

        List<ClienteProdutoTemp> resultClienteProduto = new TransformNativeQuery(ClienteProdutoTemp.class).execute(result);
        resultClienteProduto.forEach((cp) -> {
            cp.setQtdRegistro(cp.getQtdRegistrosPendentes().add(cp.getQtdRegistrosAtualizados()));
        });
        return resultClienteProduto;

    }

    public List<ProdutoCliente> loadProductClientById(BigInteger clienteId) {
        if (clienteId != null) {
            String sql = "select o from ProdutoCliente o where o.clienteId = '" + clienteId + "' order by o.nomeProduto";
            TypedQuery<ProdutoCliente> query = em.createQuery(sql, ProdutoCliente.class);

            return query.getResultList();

        }
        return null;
    }

    public ClienteProdutoTemp loadProductClientRule(Cliente cliente) throws Exception {

        ClienteProdutoTemp temp = new ClienteProdutoTemp();
        temp.setClienteId(new BigInteger(cliente.getId().toString()));
        temp.setRegime(cliente.getTipoRegime().getDescricao());
        temp.setCpfCnpj(cliente.getCpfCnpj());
        temp.setNomeCliente(cliente.getRazaoSocial());
        List<ProdutoCliente> listProductClient = loadProductClientById(new BigInteger(cliente.getId().toString()));

        for (ProdutoCliente produtoCliente : listProductClient) {
            int cont = 1;
            String ncmPadrao = "";
            String cestPadrao = "";
            boolean isProdutoGeral = true;
            TributoFederalPadrao tributoFederalPadrao = null;
            TributoEstadualPadrao tributoEstadualPadrao = null;

            //******************************************************************
            switch (cont) {
                case 1: {
                    if (produtoCliente.getEan() != null) {
                        String sql = "Select o.ncm,o.cest from syscontabil.produto_geral o where o.ean ='" + produtoCliente.getEan() + "'";

                        List<Object[]> lista = em.createNativeQuery(sql).getResultList();

                        if (!lista.isEmpty()) {
                            for (Object[] o : lista) {
                                ncmPadrao = (String) o[0];
                                cestPadrao = (String) o[1];
                            }
                        } else {
                            isProdutoGeral = false;
                        }
                        //************************ Product Rule ****************************

                        RegraProduto regraProduto = regraProdutoService.loadRegraProduto(new BigInteger(cliente.getId().toString()), produtoCliente.getEan());

                        if (regraProduto != null) {
                            tributoFederalPadrao = regraProduto.getTributoFederalPadrao();
                            tributoEstadualPadrao = regraProduto.getTributoEstadualPadrao();
                            produtoCliente.setRegras(DominioRegras.PRODUTO);
                            break;
                        }

                    }
                    cont++;

                }
                case 2: {
                    if (produtoCliente.getNcmCliente() != null) {
                        //************************ Ncm Rule ****************************

                        RegraNcm regraNcm = regraNcmService.loadRegraNcm(produtoCliente.getNcmCliente(), cliente.getTipoRegime().toString());

                        if (regraNcm != null) {
                            tributoFederalPadrao = regraNcm.getTributoFederalPadrao();
                            tributoEstadualPadrao = regraNcm.getTributoEstadualPadrao();
                            produtoCliente.setRegras(DominioRegras.NCM);
                            break;
                        }
                    }
                    cont++;
                }
                case 3: {
                    if (cliente.getTipoRegime() != null) {
                        //************************ Regime Tributário Rule ****************************

                        RegraRegimeTributario regimeTributario = regraRegimeTributarioService.loadRegraRegimeTributario(cliente.getTipoRegime().toString());

                        if (regimeTributario != null) {
                            tributoFederalPadrao = regimeTributario.getTributoFederalPadrao();
                            tributoEstadualPadrao = regimeTributario.getTributoEstadualPadrao();
                            produtoCliente.setRegras(DominioRegras.REGIME);
                        }

                    }
                }
            }

            //*************** insert data in productClient *********************
            produtoCliente.setIsProdutoGeral(isProdutoGeral);
            produtoCliente.setNcmPadrao(ncmPadrao == null ? "" : ncmPadrao);
            produtoCliente.setCestPadrao(cestPadrao == null ? "" : cestPadrao);
            produtoCliente.setTributoFederalPadrao(tributoFederalPadrao == null ? new TributoFederalPadrao() : tributoFederalPadrao);
            produtoCliente.setTributoEstadualPadrao(tributoEstadualPadrao == null ? new TributoEstadualPadrao() : tributoEstadualPadrao);
        }

        //******** insert update list for new result ListProductClient *********
        temp.setResultProdutoCliente(listProductClient);

        return temp;
    }

    public void confirmClientRule(ProdutoCliente produtoCliente) throws Exception {
        if (produtoCliente != null) {

            //***************** validation produto geral ***********************
            if (!produtoCliente.isIsProdutoGeral()) {
                if (produtoCliente.getNcmPadrao() == null || produtoCliente.getNcmPadrao().equals("")) {
                    throw new Exception("Ncm (Sugerido) não informado !!");
                }
                if (produtoCliente.getCestPadrao() == null || produtoCliente.getCestPadrao().equals("")) {
                    throw new Exception("Cest (Sugerido) não informado !!");
                }

                ProdutoGeral geral = new ProdutoGeral();
                geral.setEan(produtoCliente.getEan());
                geral.setNomeProduto(produtoCliente.getNomeProduto());
                geral.setNcm(produtoCliente.getNcmPadrao());
                geral.setCest(produtoCliente.getCestPadrao());

                produtoGeralService.create(geral);

            }

            produtoCliente.setNcmCliente(produtoCliente.getNcmPadrao());
            produtoCliente.setCestCliente(produtoCliente.getCestPadrao());

            if (produtoCliente.getTributoEstadualCliente() != null) {
                if (produtoCliente.getTributoEstadualHistorico() == null) {
                    produtoCliente.setTributoEstadualHistorico(new TributoEstadualHistorico());
                }
                //****************************** ICMS **************************
                //****************************** Tributos de Entrada **********************************
                //****************************** Historical Entry *************************************
                produtoCliente.getTributoEstadualHistorico().setCstIcmsEntradaHistorico(produtoCliente.getTributoEstadualCliente().getCstIcmsEntradaCliente());
                produtoCliente.getTributoEstadualHistorico().setAliquotaIcmsEntradaHistorico(produtoCliente.getTributoEstadualCliente().getAliquotaIcmsEntradaCliente());
                produtoCliente.getTributoEstadualHistorico().setAliquotaIcmsEntradaSTHistorico(produtoCliente.getTributoEstadualCliente().getAliquotaIcmsEntradaSTCliente());
                produtoCliente.getTributoEstadualHistorico().setReducaoBaseCalculoIcmsEntradaHistorico(produtoCliente.getTributoEstadualCliente().getReducaoBaseCalculoIcmsEntradaCliente());
                produtoCliente.getTributoEstadualHistorico().setReducaoBaseCalculoIcmsEntradaSTHistorico(produtoCliente.getTributoEstadualCliente().getReducaoBaseCalculoIcmsEntradaSTCliente());
                //****************************** Update Client Entry **********************************
                produtoCliente.getTributoEstadualCliente().setCstIcmsEntradaCliente(produtoCliente.getTributoEstadualPadrao().getCstIcmsEntradaPadrao());
                produtoCliente.getTributoEstadualCliente().setAliquotaIcmsEntradaCliente(produtoCliente.getTributoEstadualPadrao().getAliquotaIcmsEntradaPadrao());
                produtoCliente.getTributoEstadualCliente().setAliquotaIcmsEntradaSTCliente(produtoCliente.getTributoEstadualPadrao().getAliquotaIcmsEntradaSTPadrao());
                produtoCliente.getTributoEstadualCliente().setReducaoBaseCalculoIcmsEntradaCliente(produtoCliente.getTributoEstadualPadrao().getReducaoBaseCalculoIcmsEntradaPadrao());
                produtoCliente.getTributoEstadualCliente().setReducaoBaseCalculoIcmsEntradaSTCliente(produtoCliente.getTributoEstadualPadrao().getReducaoBaseCalculoIcmsEntradaSTPadrao());
                //****************************** Tributos de Saída ************************************
                //****************************** Historical Exit **************************************
                produtoCliente.getTributoEstadualHistorico().setCstIcmsSaidaHistorico(produtoCliente.getTributoEstadualCliente().getCstIcmsSaidaCliente());
                produtoCliente.getTributoEstadualHistorico().setAliquotaIcmsSaidaHistorico(produtoCliente.getTributoEstadualCliente().getAliquotaIcmsSaidaCliente());
                produtoCliente.getTributoEstadualHistorico().setAliquotaIcmsSaidaSTHistorico(produtoCliente.getTributoEstadualCliente().getAliquotaIcmsSaidaSTCliente());
                produtoCliente.getTributoEstadualHistorico().setReducaoBaseCalculoIcmsSaidaHistorico(produtoCliente.getTributoEstadualCliente().getReducaoBaseCalculoIcmsSaidaCliente());
                produtoCliente.getTributoEstadualHistorico().setReducaoBaseCalculoIcmsSaidaSTHistorico(produtoCliente.getTributoEstadualCliente().getReducaoBaseCalculoIcmsSaidaSTCliente());
                //****************************** Update Client Exit ***********************************
                produtoCliente.getTributoEstadualCliente().setCstIcmsSaidaCliente(produtoCliente.getTributoEstadualPadrao().getCstIcmsSaidaPadrao());
                produtoCliente.getTributoEstadualCliente().setAliquotaIcmsSaidaCliente(produtoCliente.getTributoEstadualPadrao().getAliquotaIcmsSaidaPadrao());
                produtoCliente.getTributoEstadualCliente().setAliquotaIcmsSaidaSTCliente(produtoCliente.getTributoEstadualPadrao().getAliquotaIcmsSaidaSTPadrao());
                produtoCliente.getTributoEstadualCliente().setReducaoBaseCalculoIcmsSaidaCliente(produtoCliente.getTributoEstadualPadrao().getReducaoBaseCalculoIcmsSaidaPadrao());
                produtoCliente.getTributoEstadualCliente().setReducaoBaseCalculoIcmsSaidaSTCliente(produtoCliente.getTributoEstadualPadrao().getReducaoBaseCalculoIcmsSaidaSTPadrao());
            }
            if (produtoCliente.getTributoFederalCliente() != null) {
                if (produtoCliente.getTributoFederalHistorico() == null) {
                    produtoCliente.setTributoFederalHistorico(new TributoFederalHistorico());
                }
                //************************* Pis ********************************
                //****************************** Tributos de Entrada **********************************
                //****************************** Historical Entry *************************************
                produtoCliente.getTributoFederalHistorico().setCstPisEntradaHistorico(produtoCliente.getTributoFederalCliente().getCstPisEntradaCliente());
                produtoCliente.getTributoFederalHistorico().setAliquotaPisEntradaHistorico(produtoCliente.getTributoFederalCliente().getAliquotaPisEntradaCliente());
                //****************************** Update Client Entry **********************************
                produtoCliente.getTributoFederalCliente().setCstPisEntradaCliente(produtoCliente.getTributoFederalPadrao().getCstPisEntradaPadrao());
                produtoCliente.getTributoFederalCliente().setAliquotaPisEntradaCliente(produtoCliente.getTributoFederalPadrao().getAliquotaPisEntradaPadrao());
                //****************************** Tributos de Saída ************************************
                //****************************** Historical Exit **************************************
                produtoCliente.getTributoFederalHistorico().setCstPisSaidaHistorico(produtoCliente.getTributoFederalCliente().getCstPisSaidaCliente());
                produtoCliente.getTributoFederalHistorico().setAliquotaPisSaidaHistorico(produtoCliente.getTributoFederalCliente().getAliquotaPisSaidaCliente());
                //****************************** Update Client Exit ***********************************
                produtoCliente.getTributoFederalCliente().setCstPisSaidaCliente(produtoCliente.getTributoFederalPadrao().getCstPisSaidaPadrao());
                produtoCliente.getTributoFederalCliente().setAliquotaPisSaidaCliente(produtoCliente.getTributoFederalPadrao().getAliquotaPisSaidaPadrao());
                //************************* Cofins *****************************
                //****************************** Tributos de Entrada **********************************
                //****************************** Historical Entry *************************************
                produtoCliente.getTributoFederalHistorico().setCstCofinsEntradaHistorico(produtoCliente.getTributoFederalCliente().getCstCofinsEntradaCliente());
                produtoCliente.getTributoFederalHistorico().setAliquotaCofinsEntradaHistorico(produtoCliente.getTributoFederalCliente().getAliquotaCofinsEntradaCliente());
                //****************************** Update Client Entry **********************************
                produtoCliente.getTributoFederalCliente().setCstCofinsEntradaCliente(produtoCliente.getTributoFederalPadrao().getCstCofinsEntradaPadrao());
                produtoCliente.getTributoFederalCliente().setAliquotaCofinsEntradaCliente(produtoCliente.getTributoFederalPadrao().getAliquotaCofinsEntradaPadrao());
                //****************************** Tributos de Saída ************************************
                //****************************** Historical Exit **************************************
                produtoCliente.getTributoFederalHistorico().setCstCofinsSaidaHistorico(produtoCliente.getTributoFederalCliente().getCstCofinsSaidaCliente());
                produtoCliente.getTributoFederalHistorico().setAliquotaCofinsSaidaHistorico(produtoCliente.getTributoFederalCliente().getAliquotaCofinsSaidaCliente());
                //****************************** Update Client Exit ***********************************
                produtoCliente.getTributoFederalCliente().setCstCofinsSaidaCliente(produtoCliente.getTributoFederalPadrao().getCstCofinsSaidaPadrao());
                produtoCliente.getTributoFederalCliente().setAliquotaCofinsSaidaCliente(produtoCliente.getTributoFederalPadrao().getAliquotaCofinsSaidaPadrao());
                //**************************** Ipi *****************************
                //****************************** Tributos de Entrada **********************************
                //****************************** Historical Entry *************************************
                produtoCliente.getTributoFederalHistorico().setCstIpiEntradaHistorico(produtoCliente.getTributoFederalCliente().getCstIpiEntradaCliente());
                produtoCliente.getTributoFederalHistorico().setAliquotaIpiEntradaHistorico(produtoCliente.getTributoFederalCliente().getAliquotaIpiEntradaCliente());
                //****************************** Update Client Entry **********************************
                produtoCliente.getTributoFederalCliente().setCstIpiEntradaCliente(produtoCliente.getTributoFederalPadrao().getCstIpiEntradaPadrao());
                produtoCliente.getTributoFederalCliente().setAliquotaIpiEntradaCliente(produtoCliente.getTributoFederalPadrao().getAliquotaIpiEntradaPadrao());
                //****************************** Tributos de Saída ************************************
                //****************************** Historical Exit **************************************
                produtoCliente.getTributoFederalHistorico().setCstIpiSaidaHistorico(produtoCliente.getTributoFederalCliente().getCstIpiSaidaCliente());
                produtoCliente.getTributoFederalHistorico().setAliquotaIpiSaidaHistorico(produtoCliente.getTributoFederalCliente().getAliquotaIpiSaidaCliente());
                //****************************** Update Client Exit ***********************************
                produtoCliente.getTributoFederalCliente().setCstIpiSaidaCliente(produtoCliente.getTributoFederalPadrao().getCstIpiSaidaPadrao());
                produtoCliente.getTributoFederalCliente().setAliquotaIpiSaidaCliente(produtoCliente.getTributoFederalPadrao().getAliquotaIpiSaidaPadrao());
            }

            produtoCliente.setStatus(DominioValidacaoProduto.VALIDADO);

        }
    }

    @Override
    public ProdutoCliente update(ProdutoCliente obj) throws Exception {
        confirmClientRule(obj);
        return super.update(obj);
    }

    public boolean generatedRolesByProductClient(ProdutoCliente obj) throws Exception {
        if (obj != null) {
            switch (obj.getRegras()) {
                case PRODUTO: {
                    RegraProduto regraProduto = new RegraProduto();

                    break;
                }
                case NCM: {
                    RegraNcm regraNcm = new RegraNcm();

                    break;
                }
                case REGIME: {
                    RegraRegimeTributario regimeTributario = new RegraRegimeTributario();

                    break;
                }
            }
        }

        return true;
    }

}
