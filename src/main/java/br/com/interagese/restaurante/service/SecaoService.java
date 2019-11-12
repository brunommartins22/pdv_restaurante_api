/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restaurante.service;

import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.restaurantemodel.models.Secao;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dinael
 */
@Service
public class SecaoService extends PadraoService<Secao>{
    
    @Override
    public String getWhere(String complementoConsulta) {
        if(complementoConsulta == null || complementoConsulta.isEmpty()){
            return complementoConsulta;
        }
        return ("nmSecao like '%" + complementoConsulta+ "%' "); //To change body of generated methods, choose Tools | Templates.
    }
}
