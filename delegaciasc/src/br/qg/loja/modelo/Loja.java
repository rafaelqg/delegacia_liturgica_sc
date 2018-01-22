/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Rafael
 */
public class Loja {
    private int numero;
    private String nome;
    private String local;
    private String enderecoCompleto;
    private String telefone;
    private String celular;
    private String email;
    private Date dataFundacao;
    private int corpo;

    
    @Override
    public String toString(){
        return this.getNome();
    }
          
    public Date getDataFundacao() {
		return dataFundacao;
	}

	public void setDataFundacao(Date dataFundacao) {
		this.dataFundacao = dataFundacao;
	}



	public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCorpo() {
        return corpo;
    }

    public void setCorpo(int corpo) {
        this.corpo = corpo;
    }
    
    @Override
    public int hashCode() {
       return this.numero;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
       Loja other = (Loja) obj;
        if(this.numero==other.getNumero()){
        	return true;
        }
        return false;
    }
    
        
}
