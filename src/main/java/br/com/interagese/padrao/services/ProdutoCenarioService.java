/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.padrao.services;

import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.syscontabil.models.ProdutoCenario;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bruno Martins
 */
@Service
public class ProdutoCenarioService extends PadraoService<ProdutoCenario> {

    public List<ProdutoCenario> loadProdutoCenarioByClienteById(Long idCliente, Long idCenario) {
        String sql = "SELECT o FROM ProdutoCenario o where o.produtoCliente.cliente.id = :cliente and o.cenario.id = :id";

        List<ProdutoCenario> result = em.createQuery(sql).setParameter("cliente", idCliente).setParameter("id", idCenario).getResultList();

        return result;
    }

}
