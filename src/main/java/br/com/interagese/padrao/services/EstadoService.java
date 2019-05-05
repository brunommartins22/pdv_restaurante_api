/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.padrao.services;

import br.com.interagese.padrao.rest.models.Estado;
import br.com.interagese.padrao.rest.util.PadraoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author adam
 */
@Service
public class EstadoService extends PadraoService<Estado> {

    public EstadoService() {
        super(Estado.class);
    }

    @Override
    public List<Estado> findAll() {
        return em.createQuery("SELECT o from Estado o ORDER BY o.sigla").getResultList();
    }

}
