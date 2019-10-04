/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.interagese.syscontabil.models;

import br.com.interagese.erplibrary.AtributoPadrao;
import br.com.interagese.erplibrary.EnderecoPadrao;
import br.com.interagese.rest.domain.DominioTipoPessoa;
import br.com.interagese.syscontabil.domains.DominioRegime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Bruno Martins
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_cliente")
    @SequenceGenerator(name = "gen_cliente", sequenceName = "seq_cliente")
    private Long id;
    @Column(length = 255, nullable = false)
    private String razaoSocial;
    @Column(length = 255)
    private String nomeFantasia;
    @Column(length = 255)
    private String nomeResposavel;
    @Enumerated(EnumType.STRING)
    private DominioRegime tipoRegime;
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private DominioTipoPessoa tipoCliente;
    @Column(length = 14, nullable = false, unique = true)
    private String cpfCnpj;
    @Column(length = 12, nullable = false, unique = true)
    private String rgIe;
    @Column(length = 12, unique = true)
    private String im;
    @Column(length = 12, unique = true)
    private String ieSt;
    @Column(length = 12, unique = true)
    private String suframa;
    @Embedded
    private EnderecoPadrao endereco;
    @Embedded
    private AtributoPadrao atributoPadrao;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Cenario> cenarios;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Cnae> atividades;
    @Column(columnDefinition="Boolean default true")
    private Boolean ativo;
    @Column(length = 20)
    private String numeroContrato;
    
    @Transient
    private String situacao;
    
    public Cliente() {

    }

    //*********************** Eequals && Hashcode ******************************
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.interagese.syscontabil.models.Cliente { id=" + getId() + " ]";
    }

    //*************************** get && setts *********************************
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the razaoSocial
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * @param razaoSocial the razaoSocial to set
     */
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    /**
     * @return the nomeFantasia
     */
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    /**
     * @param nomeFantasia the nomeFantasia to set
     */
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    /**
     * @return the nomeResposavel
     */
    public String getNomeResposavel() {
        return nomeResposavel;
    }

    /**
     * @param nomeResposavel the nomeResposavel to set
     */
    public void setNomeResposavel(String nomeResposavel) {
        this.nomeResposavel = nomeResposavel;
    }

    /**
     * @return the tipoRegime
     */
    public DominioRegime getTipoRegime() {
        return tipoRegime;
    }

    /**
     * @param tipoRegime the tipoRegime to set
     */
    public void setTipoRegime(DominioRegime tipoRegime) {
        this.tipoRegime = tipoRegime;
    }

    /**
     * @return the tipoCliente
     */
    public DominioTipoPessoa getTipoCliente() {
        return tipoCliente;
    }

    /**
     * @param tipoCliente the tipoCliente to set
     */
    public void setTipoCliente(DominioTipoPessoa tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    /**
     * @return the cpfCnpj
     */
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * @param cpfCnpj the cpfCnpj to set
     */
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    /**
     * @return the rgIe
     */
    public String getRgIe() {
        return rgIe;
    }

    /**
     * @param rgIe the rgIe to set
     */
    public void setRgIe(String rgIe) {
        this.rgIe = rgIe;
    }

    /**
     * @return the im
     */
    public String getIm() {
        return im;
    }

    /**
     * @param im the im to set
     */
    public void setIm(String im) {
        this.im = im;
    }

    /**
     * @return the ieSt
     */
    public String getIeSt() {
        return ieSt;
    }

    /**
     * @param ieSt the ieSt to set
     */
    public void setIeSt(String ieSt) {
        this.ieSt = ieSt;
    }

    /**
     * @return the suframa
     */
    public String getSuframa() {
        return suframa;
    }

    /**
     * @param suframa the suframa to set
     */
    public void setSuframa(String suframa) {
        this.suframa = suframa;
    }

    /**
     * @return the endereco
     */
    public EnderecoPadrao getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(EnderecoPadrao endereco) {
        this.endereco = endereco;
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
     * @return the cenarios
     */
    public List<Cenario> getCenarios() {
        if (cenarios == null) {
            cenarios = new ArrayList<>();
        }
        return cenarios;
    }

    /**
     * @param cenarios the cenarios to set
     */
    public void setCenarios(List<Cenario> cenarios) {
        this.cenarios = cenarios;
    }

    public List<Cnae> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Cnae> atividades) {
        this.atividades = atividades;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

}
