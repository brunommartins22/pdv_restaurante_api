/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.restaurante.controllers;

import br.com.interagese.padrao.rest.util.IsServiceDefault;
import br.com.interagese.padrao.rest.util.PadraoController;
import br.com.interagese.restaurante.service.FornecedorService;
import br.com.interagese.restaurantemodel.models.Fornecedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dinael
 */
@RestController
@RequestMapping (path="/api/fornecedores")
public class FornecedorController extends PadraoController <Fornecedor> {
 
    @IsServiceDefault
    @Autowired
    public FornecedorService service;
    
    
}
