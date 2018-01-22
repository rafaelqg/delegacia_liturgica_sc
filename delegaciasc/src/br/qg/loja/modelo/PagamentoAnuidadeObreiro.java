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
public class PagamentoAnuidadeObreiro {
   private Obreiro obreiro;
   private Loja loja;
   private int ano;
   private Date dtComprovanteBancario;
   private int numeroPrancha;
   private Date dtPrancha;
   private String motivo;
   private Date dtEmissao;
   private Date dtRetorno;
   private Date dtRomaneio;
   private int numeroRomaneiro;
   private int numero;
   private Date dataPagamento;
   private double valor;
   private TipoEspecie tipoEspecie;
   private int grau;

   
   public PagamentoAnuidadeObreiro(){
	   this.tipoEspecie=new TipoEspecie();
	   this.tipoEspecie.setNumero(2);
	   this.tipoEspecie.setNome("Atividade obreiro");
   }
   
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
   
    public Date getDtRomaneio() {
        return dtRomaneio;
    }

    public void setDtRomaneio(Date dtRomaneio) {
        this.dtRomaneio = dtRomaneio;
    }

    public int getNumeroRomaneiro() {
        return numeroRomaneiro;
    }

    public void setNumeroRomaneiro(int numeroRomaneiro) {
        this.numeroRomaneiro = numeroRomaneiro;
    }
   

    public Obreiro getObreiro() {
        return obreiro;
    }

    public void setObreiro(Obreiro obreiro) {
        this.obreiro = obreiro;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

   
    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Date getDtComprovanteBancario() {
        return dtComprovanteBancario;
    }

    public void setDtComprovanteBancario(Date dtComprovanteBancario) {
        this.dtComprovanteBancario = dtComprovanteBancario;
    }

    public int getNumeroPrancha() {
        return numeroPrancha;
    }

    public void setNumeroPrancha(int numeroPrancha) {
        this.numeroPrancha = numeroPrancha;
    }

    public Date getDtPrancha() {
        return dtPrancha;
    }

    public void setDtPrancha(Date dtPrancha) {
        this.dtPrancha = dtPrancha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public Date getDtRetorno() {
        return dtRetorno;
    }

    public void setDtRetorno(Date dtRetorno) {
        this.dtRetorno = dtRetorno;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
    
    
    public TipoEspecie getTipoEspecie() {
		return tipoEspecie;
	}

	public void setTipoEspecie(TipoEspecie tipoEspecie) {
		this.tipoEspecie = tipoEspecie;
	}

	public boolean equals(PagamentoAnuidadeObreiro pag){
    	boolean retorno=false;
    	if (this.getObreiro().getIme()==pag.getObreiro().getIme() && this.getLoja()==pag.getLoja()){
    		retorno=true;
    	}
    	return retorno;
    }

	public int getGrau() {
		return grau;
	}

	public void setGrau(int grau) {
		this.grau = grau;
	}
	
 
}