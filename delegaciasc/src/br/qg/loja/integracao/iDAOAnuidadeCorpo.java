/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.util.ArrayList;
import java.util.Date;

import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.PagamentoAnuidadeCorpo;

/**
 *
 * @author Rafael
 */
public interface iDAOAnuidadeCorpo {
    public boolean armazenaPagamentoAnuidadeCorpo(PagamentoAnuidadeCorpo obj);
    public ArrayList<PagamentoAnuidadeCorpo> buscaPagamentoPorCorpo(Loja obj);
    public PagamentoAnuidadeCorpo buscaPagamentoPorCorpoAno(Loja obj, int ano);
    public boolean excluiPagamentoCorpo(PagamentoAnuidadeCorpo obj);
}
