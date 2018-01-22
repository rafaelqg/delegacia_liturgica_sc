/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.modelo;

import java.util.Date;

/**
 *
 * @author RafaelQG
 */

public class PagamentoAnuidadeCorpo {
    private int numero; //numero
    private Loja loja; //loja
    private int anoPagamento; //ano_pagamento
    private double valorDeposito; //valor_deposito
    private TipoEspecie tipoEspecie; //tipo_especie
    private Date dtComprovanteBancario;//dt_comprovante_dep_bancario
    private String motivo; //motivo
    private String nrPrancha;//nr_prancha_encaminhamento
    private Date dtPrancha;//dt_prancha_encaminhamento
    private Date dtEmissao;//dt_emissao_diligencia
    private Date dtRetorno;//dt_retorno_diligencia
    private String nrEmissao;//nr_emissao_diligencia
    private Date dtRomaneio; //dt_romaneio
    private String nrRomaneio; //numero_romaneio
    private int numeroGuia; //numero_guia

    public Date getDtRetorno() {
        return dtRetorno;
    }

    public void setDtRetorno(Date dtRetorno) {
        this.dtRetorno = dtRetorno;
    }


    public int getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(int numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public int getAnoPagamento() {
        return anoPagamento;
    }

    public void setAnoPagamento(int anoPagamento) {
        this.anoPagamento = anoPagamento;
    }

    public double getValorDeposito() {
        return valorDeposito;
    }

    public void setValorDeposito(double valorDeposito) {
        this.valorDeposito = valorDeposito;
    }

    public TipoEspecie getTipoEspecie() {
        return tipoEspecie;
    }

    public void setTipoEspecie(TipoEspecie tipoEspecie) {
        this.tipoEspecie = tipoEspecie;
    }

    public Date getDtComprovanteBancario() {
        return dtComprovanteBancario;
    }

    public void setDtComprovanteBancario(Date dtComprovanteBancario) {
        this.dtComprovanteBancario = dtComprovanteBancario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNrPrancha() {
        return nrPrancha;
    }

    public void setNrPrancha(String nrPrancha) {
        this.nrPrancha = nrPrancha;
    }

    public Date getDtPrancha() {
        return dtPrancha;
    }

    public void setDtPrancha(Date dtPrancha) {
        this.dtPrancha = dtPrancha;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getNrEmissao() {
        return nrEmissao;
    }

    public void setNrEmissao(String nrEmissao) {
        this.nrEmissao = nrEmissao;
    }

    public Date getDtRomaneio() {
        return dtRomaneio;
    }

    public void setDtRomaneio(Date dtRomaneio) {
        this.dtRomaneio = dtRomaneio;
    }

    public String getNrRomaneio() {
        return nrRomaneio;
    }

    public void setNrRomaneio(String nrRomaneio) {
        this.nrRomaneio = nrRomaneio;
    }
    
    public boolean equals(PagamentoAnuidadeCorpo pag){
    	boolean resultado=false;
    	if(pag.getAnoPagamento()==this.getAnoPagamento() && pag.getLoja().equals(this.getLoja())){
    		resultado=true;
    	}
    	return resultado;
    }
    
}
