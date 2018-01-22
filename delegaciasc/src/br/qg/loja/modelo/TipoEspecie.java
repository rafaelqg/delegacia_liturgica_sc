/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.modelo;

/**
 *
 * @author RafaelQG
 */
public class TipoEspecie {
    private int numero;
    private String nome;

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
    
    @Override
    public String toString(){
        return this.nome;
    }
    
    @Override
    public int hashCode() {
    	return this.numero;
    }
    
}
