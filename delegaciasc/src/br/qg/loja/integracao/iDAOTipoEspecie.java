/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.util.ArrayList;
import java.util.Hashtable;

import br.qg.loja.modelo.TipoEspecie;

/**
 *
 * @author RafaelQG
 */
public interface iDAOTipoEspecie {
    
    
    public TipoEspecie getTipoEspecie(int numero);
    public ArrayList<TipoEspecie> getAllTipoEspecie();
    public Hashtable<Integer, TipoEspecie> getAllTipoEspecieHash();
}
