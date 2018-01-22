/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.modelo;

/**
 *
 * @author RafaelQG
 */
public class Cargo {
    private int numero;
    private String descricao;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String toString(){
        return this.getDescricao();
    }
}
