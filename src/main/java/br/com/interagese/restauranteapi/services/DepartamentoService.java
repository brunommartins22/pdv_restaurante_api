/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restauranteapi.services;

import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.restaurantemodel.models.Departamento;
import java.util.List;
import org.springframework.stereotype.Service;



/**
 *
 * @author Dinael
 */
@Service
public class DepartamentoService extends PadraoService<Departamento> {
       
    @Override
    public List<Departamento> findRange(String complementoConsulta, int apartirDe, int quantidade, String ordernacao) {
        List<Departamento> result = super.findRange(complementoConsulta, apartirDe, quantidade, ordernacao); //To change body of generated methods, choose Tools | Templates.
        result.forEach((departamento) -> {
            if (departamento.getSecao() != null) {
                departamento.setNmSecao(departamento.getSecao().getNmSecao());
            } else {
                departamento.setNmSecao("");
            }
        });
        return result;
    }
    
    @Override
    public String getWhere (String complementoConsulta) {
        if(complementoConsulta == null || complementoConsulta.isEmpty()){
            return complementoConsulta;
        }
        return ("nomeDepartamento like '%" + complementoConsulta +"%' ");
    } 
    
}
