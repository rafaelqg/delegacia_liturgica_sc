package br.qg.loja.controle;

import java.util.Date;

import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;

public class ObreiroLoja {
	private  Loja loja;
    private  Grau grau;
    private  Date data;
    private Cargo cargo;
    
	public Loja getLoja() {
		return loja;
	}
	
	public void setLoja(Loja loja) {
		this.loja = loja;
	}
	
	public Grau getGrau() {
		return grau;
	}
	
	public void setGrau(Grau grau) {
		this.grau = grau;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Cargo getCargo() {
		return cargo;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}    
}
