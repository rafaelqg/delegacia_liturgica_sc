/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.util.ArrayList;
import java.util.Date;

import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.LogAltercaoObreiro;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;

/**
 *
 * @author Rafael
 */
public interface iDAOObreiro {
    public boolean armazenaObreiro(Obreiro obj);
    public ArrayList<Obreiro> buscaObreiros();
    public ArrayList<ArrayList> buscaObreirosPorLoja(Loja l);
    public Obreiro buscaObreiroPorChave(int chave);
    public Obreiro buscaObreiroPorCPF(String cpf);
    public Obreiro buscaObreiroPorIME(int ime);
    public Obreiro buscaObreiroPorNome(String nome);
    public Loja buscaLojaAtual(Obreiro obj);
    public Obreiro autenticar(String ime, String cpf);
    public void logOperacao(long idUsuario, int idOperacao, int idObreiro);
    public ArrayList<LogAltercaoObreiro> buscaLogAlteracoes();
    /**
     * 
     * @param value: O nome ou o código IME do obreiro pesquisado
     * @return : os objetos encontrados que atendem Ã  pesquisa.
     */
    public ArrayList<Obreiro> buscaObreiroBasico(String value);
    public boolean removeObreiro(int chave);
    public boolean removeRelacionamentosObreiroLoja(int ime);
    public boolean removeRelacionamentoObreiroLoja(int ime, int grau);
    public boolean armazenaRelacionamentoLoja(Loja loja, Obreiro obreiro, Grau grau, Date data,Cargo cargo);
    
    public ArrayList<Obreiro> buscaObreirosAniversariantesdoMes();
    public ArrayList<Obreiro> buscaObreirosAniversariantesdoDia();
    /**
     * 
     * @param loja
     * @param obreiro
     * @param grau
     * @return the id of the relationship between loja and obreiro or -1 if nothing is found 
     */
    public int buscaRelacionamentoLoja(Loja loja, Obreiro obreiro, Grau grau);
    
    /**
     * 
     * @param obreiro
     * @return a 3 dimentional array containg  all relationships between Loja and Obreiro per Corpo.
     * The values are returned in following order:  loja, grau, data;
     * 
     */
    public ArrayList<ArrayList<ArrayList>> getRelacionamentoLojas(Obreiro obreiro);
}
