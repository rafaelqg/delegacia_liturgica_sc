/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.util.ArrayList;

import br.qg.loja.modelo.Cargo;

/**
 *
 * @author RafaelQG
 */
public interface iDAOCargo {
    public ArrayList<Cargo> buscaCargos();
    public Cargo buscaCargoPorCodigo(int codigo);
    
}
