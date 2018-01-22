/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.util.ArrayList;

import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Obreiro;

/**
 *
 * @author Rafael
 */
public interface iDAOGrau {
    
    public ArrayList<Grau> buscaGraus();
    public Grau getGrauAtual(Obreiro o);
    
}
