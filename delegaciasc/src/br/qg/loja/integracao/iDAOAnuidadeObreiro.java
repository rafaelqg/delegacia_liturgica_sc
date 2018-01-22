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
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;
import br.qg.loja.modelo.TipoEspecie;

/**
 *
 * @author Rafael
 */
public interface iDAOAnuidadeObreiro {
    public boolean armazenaPagamentoAnuidade(PagamentoAnuidadeObreiro obj);
    public ArrayList<PagamentoAnuidadeObreiro> buscaPagamentoPorObreiro(Obreiro obj);
    public ArrayList<PagamentoAnuidadeObreiro> buscaPagamentoAnuidadesPorObreiroLoja(Obreiro obj, Loja lj);
    public ArrayList<PagamentoAnuidadeObreiro> buscaPagamentoAnuidadesPorLoja(Loja lj);
    public boolean excluiPagamentosPorObreiroLoja(int ano, Obreiro obj, Loja lj, TipoEspecie tipoEspecie);
    public PagamentoAnuidadeObreiro existePagamento(int ano, Obreiro obr, Loja lj, TipoEspecie tipoEspecie, int grau);
}
