package br.qg.loja.modelo;

import java.util.Date;

public class HistoricoAlteracao {
	private Date date;
	private String descricao;
	private String cpf;
	private Obreiro obreiro;
	
			
	public Obreiro getObreiro() {
		return obreiro;
	}

	public void setObreiro(Obreiro obreiro) {
		this.obreiro = obreiro;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}
