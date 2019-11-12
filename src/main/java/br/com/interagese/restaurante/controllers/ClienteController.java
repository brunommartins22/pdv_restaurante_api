/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restaurante.controllers;

import br.com.interagese.padrao.rest.util.IsServiceDefault;
import br.com.interagese.padrao.rest.util.PadraoController;
import br.com.interagese.restaurante.service.ClienteService;
import br.com.interagese.restaurantemodel.models.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dinael
 */
@RestController
@RequestMapping (path="/api/clientes")
public class ClienteController extends PadraoController <Cliente>{
    
    @IsServiceDefault
    @Autowired
    public ClienteService service;
    
}
