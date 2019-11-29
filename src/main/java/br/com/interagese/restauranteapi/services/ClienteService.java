/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restauranteapi.services;

import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.restaurantemodel.models.Cliente;
import br.com.interagese.restaurantemodel.models.Endereco;
import br.com.interagese.restaurantemodel.models.Fornecedor;
import br.com.interagese.restaurantemodel.models.Telefone;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dinael
 */

@Service
public class ClienteService extends PadraoService <Cliente>{
    
    @Override
    public Cliente create(Cliente obj) throws Exception {
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
