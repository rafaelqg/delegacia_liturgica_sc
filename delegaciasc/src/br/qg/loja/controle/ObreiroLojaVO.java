package br.qg.loja.controle;

import java.util.Date;

/**
 * 
 * @author Rafael Queiroz Gonçalves
 * @since: 03/02/2016
 * Present data related to obreiros in lojas.
 */
public class ObreiroLojaVO {
	private int ime;
	private String nome;
	private Date dataIniciacao;
	private int grau;
	
	public int getIme() {
		return ime;
	}
	public void setIme(int ime) {
		this.ime = ime;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataIniciacao() {
		return dataIniciacao;
	}
	public void setDataIniciacao(Date dataIniciacao) {
		this.dataIniciacao = dataIniciacao;
	}
	public int getGrau() {
		return grau;
	}
	public void setGrau(int grau) {
		this.grau = grau;
	}	
}
