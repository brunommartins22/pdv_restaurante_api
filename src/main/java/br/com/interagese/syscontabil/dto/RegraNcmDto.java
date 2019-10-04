/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.syscontabil.dto;

import br.com.interagese.erplibrary.AtributoPadrao;
import br.com.interagese.syscontabil.models.RegraNcm;
import javax.persistence.Embedded;

/**
 *
 * @author Bruno Martins
 */
public class RegraNcmDto {

    private String id;
    private RegraNcm regraNcmLucroPresumido;
    private RegraNcm regraNcmLucroReal;
    private RegraNcm regraNcmSimplesNacional;
    private String situacaoLucroPresumido = "Pendente";
    private String situacaoLucroReal = "Pendente";
    private String situacaoSimplesNacional = "Pendente";
    @Embedded
    private AtributoPadrao atributoPadrao = new AtributoPadrao();
    
    //****************************** get && setts ******************************

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the regraNcmLucroPresumido
     */
    public RegraNcm getRegraNcmLucroPresumido() {
        return regraNcmLucroPresumido;
    }

    /**
     * @param regraNcmLucroPresumido the regraNcmLucroPresumido to set
     */
    public void setRegraNcmLucroPresumido(RegraNcm regraNcmLucroPresumido) {
        this.regraNcmLucroPresumido = regraNcmLucroPresumido;
    }

    /**
     * @return the regraNcmLucroReal
     */
    public RegraNcm getRegraNcmLucroReal() {
        return regraNcmLucroReal;
    }

    /**
     * @param regraNcmLucroReal the regraNcmLucroReal to set
     */
    public void setRegraNcmLucroReal(RegraNcm regraNcmLucroReal) {
        this.regraNcmLucroReal = regraNcmLucroReal;
    }

    /**
     * @return the regraNcmSiplesNacional
     */
    public RegraNcm getRegraNcmSimplesNacional() {
        return regraNcmSimplesNacional;
    }

    /**
     * @param regraNcmSiplesNacional the regraNcmSiplesNacional to set
     */
    public void setRegraNcmSimplesNacional(RegraNcm regraNcmSiplesNacional) {
        this.regraNcmSimplesNacional = regraNcmSiplesNacional;
    }

    /**
     * @return the atributoPadrao
     */
    public AtributoPadrao getAtributoPadrao() {
        return atributoPadrao;
    }

    /**
     * @param atributoPadrao the atributoPadrao to set
     */
    public void setAtributoPadrao(AtributoPadrao atributoPadrao) {
        this.atributoPadrao = atributoPadrao;
    }

    /**
     * @return the situacaoLucroPresumido
     */
    public String getSituacaoLucroPresumido() {
        return situacaoLucroPresumido;
    }

    /**
     * @param situacaoLucroPresumido the situacaoLucroPresumido to set
     */
    public void setSituacaoLucroPresumido(String situacaoLucroPresumido) {
        this.situacaoLucroPresumido = situacaoLucroPresumido;
    }

    /**
     * @return the situacaoLucroReal
     */
    public String getSituacaoLucroReal() {
        return situacaoLucroReal;
    }

    /**
     * @param situacaoLucroReal the situacaoLucroReal to set
     */
    public void setSituacaoLucroReal(String situacaoLucroReal) {
        this.situacaoLucroReal = situacaoLucroReal;
    }

    /**
     * @return the situacaoSimplesNacional
     */
    public String getSituacaoSimplesNacional() {
        return situacaoSimplesNacional;
    }

    /**
     * @param situacaoSimplesNacional the situacaoSimplesNacional to set
     */
    public void setSituacaoSimplesNacional(String situacaoSimplesNacional) {
        this.situacaoSimplesNacional = situacaoSimplesNacional;
    }

    
    
}
