/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restauranteapi.services;


import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.restaurantemodel.models.Endereco;
import br.com.interagese.restaurantemodel.models.Telefone;
import br.com.interagese.restaurantemodel.models.Fornecedor;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dinael
 */
@Service
public class FornecedorService extends PadraoService <Fornecedor> {
    
    @Override
    public List<Fornecedor> findRange(String complementoConsulta, int apartirDe, int quantidade, String ordernacao) {
        List<Fornecedor> result = super.findRange(complementoConsulta, apartirDe, quantidade, ordernacao); //To change body of generated methods, choose Tools | Templates.
        result.forEach((fornecedor) -> {
            if (fornecedor.getPessoa() != null) {
                fornecedor.getPessoa().setNomePessoa(fornecedor.getPessoa().getNomePessoa());
            } else {
                fornecedor.getPessoa().setNomePessoa("");
            }
        });
        return result;
    }
    
    @Override
    public Fornecedor create(Fornecedor obj) throws Exception {
        try {
            setID(obj);

            obj.getAtributoPadrao().setDataAlteracao(new Date());
            obj.getPessoa().setAtributoPadrao(obj.getAtributoPadrao());
            
            for( Endereco end: obj.getPessoa().getListEndereco()) {
                end.setAtributoPadrao(obj.getAtributoPadrao());
            }
            
            for ( Telefone tel: obj.getPessoa().getListTelefone()){
                 tel.setAtributoPadrao(obj.getAtributoPadrao());
            }
            em.persist(obj);

            return obj;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
