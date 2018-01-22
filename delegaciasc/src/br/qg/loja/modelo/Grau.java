/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.modelo;

/**
 *
 * @author Rafael
 */
public class Grau {
    private int numero;
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    @Override
    public String toString(){
        return String.valueOf(this.numero);
    }
}
