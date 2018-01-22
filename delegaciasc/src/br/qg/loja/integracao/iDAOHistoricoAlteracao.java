/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.util.ArrayList;

import br.qg.loja.modelo.HistoricoAlteracao;

/**
 *
 * @author Rafael
 */
public interface iDAOHistoricoAlteracao {
    
    public ArrayList<HistoricoAlteracao> buscaTodas();
    public boolean inserir(HistoricoAlteracao ha);
    
}
