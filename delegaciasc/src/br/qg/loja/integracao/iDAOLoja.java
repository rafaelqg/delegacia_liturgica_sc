/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.util.ArrayList;

import br.qg.loja.modelo.Loja;

/**
 *
 * @author Rafael
 */
public interface iDAOLoja {
    
    public ArrayList<Loja> buscaLojasPorCorpo(int corpo);
    public ArrayList<Loja> buscaLojas();
    public Loja buscaLojaPorChave(int chave);
    public boolean registraLoja(Loja loja);
}
