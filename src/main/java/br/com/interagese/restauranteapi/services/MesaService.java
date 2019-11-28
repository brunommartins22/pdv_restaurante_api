/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restauranteapi.services;

import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.restaurantemodel.models.Mesa;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bruno Martins
 */
@Service
public class MesaService extends PadraoService<Mesa> {

    @Override
    public List<Mesa> findRange(String complementoConsulta, int apartirDe, int quantidade, String ordernacao) {
        List<Mesa> result = super.findRange(complementoConsulta, apartirDe, quantidade, ordernacao); //To change body of generated methods, choose Tools | Templates.

        for (Mesa mesa : result) {
            mesa.setNumeroMesaDesc("Mesa " + mesa.getNumeroMesa());
            mesa.setQuantidadePessoasDesc(mesa.getQuantidadePessoas() + " pessoa(s)");
        }

        return result;
    }
    
    
    
    

}
