/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restauranteapi.services;

import br.com.interagese.padrao.rest.util.PadraoService;
import br.com.interagese.restaurantemodel.models.Ambiente;
import br.com.interagese.restaurantemodel.models.Mesa;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bruno Martins
 */
@Service
public class AmbienteService extends PadraoService<Ambiente> {

    @Override
    public Ambiente update(Ambiente obj) throws Exception {

        if (obj.getListMesas().isEmpty()) {
            em.createNativeQuery("Update pdvrestaurante.mesa set status = true where id in (select list_mesas_id from pdvrestaurante.ambiente_list_mesas where ambiente_id = '" + obj.getId() + "')").executeUpdate();
        } else {
            List<Object[]> result = em.createNativeQuery("SELECT * FROM pdvrestaurante.ambiente_list_mesas where ambiente_id = " + obj.getId()).getResultList();
            for (Object[] o : result) {

                Mesa m = obj.getListMesas().stream().filter((mesa) -> {
                    return mesa.getId() == ((Number) o[1]).longValue(); 
                }).findFirst().orElse(null);

                if (m == null) {
                    em.createNativeQuery("Update pdvrestaurante.mesa set status = true where id = " + ((Number) o[1]).longValue()).executeUpdate();
                }
            }
        }

        return super.update(obj); 
    }

}
