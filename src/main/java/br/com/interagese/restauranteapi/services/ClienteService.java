/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restauranteapi.services;
import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.restaurantemodel.models.Cliente;
import br.com.interagese.restaurantemodel.models.Endereco;
import br.com.interagese.restaurantemodel.models.Telefone;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dinael
 */

@Service
public class ClienteService extends PadraoService <Cliente>{
    
     
    @Override
    public List<Cliente> findRange(String complementoConsulta, int apartirDe, int quantidade, String ordernacao) {
        List<Cliente> result = super.findRange(complementoConsulta, apartirDe, quantidade, ordernacao); //To change body of generated methods, choose Tools | Templates.
        result.forEach((Cliente) -> {
            if (Cliente.getPessoa() != null) {
                Cliente.getPessoa().setNomePessoa(Cliente.getPessoa().getNomePessoa());
            } else {
                Cliente.getPessoa().setNomePessoa("");
            }
        });
        return result;
    }
    
    @Override
    public String getWhere (String complementoConsulta) {
        if(complementoConsulta == null || complementoConsulta.isEmpty()){
            return complementoConsulta;
        }
        return ("pessoa.nomePessoa like '%" + complementoConsulta +"%' ");
    } 
    
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
    
     @Override   
     public Cliente update(Cliente obj) throws Exception {
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

        em.merge(obj);
        return obj;
         } catch (IllegalArgumentException ex) {
            return null;
        }
    }
 
    
}
