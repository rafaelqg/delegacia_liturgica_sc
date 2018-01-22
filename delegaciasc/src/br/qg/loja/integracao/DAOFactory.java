/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import br.qg.loja.integracao.mysql.DAOAnuidadeCorpo;
import br.qg.loja.integracao.mysql.DAOAnuidadeObreiro;
import br.qg.loja.integracao.mysql.DAOCargoMySQL;
import br.qg.loja.integracao.mysql.DAOGrauMySQL;
import br.qg.loja.integracao.mysql.DAOHistoricoAlteracaoMySQL;
import br.qg.loja.integracao.mysql.DAOLojaMySQL;
import br.qg.loja.integracao.mysql.DAOObreiroMySQL;
import br.qg.loja.integracao.mysql.DAOParametro;
import br.qg.loja.integracao.mysql.DAOTipoEspecie;
import br.qg.loja.integracao.mysql.DAOUsuario;
import br.qg.loja.modelo.HistoricoAlteracao;

/**
 * 
 * @author Rafael
 */
public class DAOFactory {

    private static iDAOObreiro daoObreiro = new DAOObreiroMySQL();
    private static iDAOGrau daoGrau = new DAOGrauMySQL();
    private static iDAOLoja daoLoja = new DAOLojaMySQL();
    private static iDAOCargo daoCargo = new DAOCargoMySQL();
    private static iDAOAnuidadeObreiro daoAnuidadeObreiro = new DAOAnuidadeObreiro();
    private static iDAOAnuidadeCorpo daoAnuidadeCorpo = new DAOAnuidadeCorpo();
    private static iDAOTipoEspecie daoTipoEspecie = new DAOTipoEspecie();
    private static iDAOParametro daoParametro= new DAOParametro();
    private static iDAOHistoricoAlteracao daoHistoricoAlteracao= new DAOHistoricoAlteracaoMySQL();
    private static iDAOUsuario daoUsuario= new DAOUsuario();
    
    
    public static iDAOUsuario getiDAOUsuario(){
    	return daoUsuario;
    }
    
    
    public static iDAOHistoricoAlteracao getDaoHistoricoAlteracao() {
		return daoHistoricoAlteracao;
	}

	public static void setDaoHistoricoAlteracao(iDAOHistoricoAlteracao daoHistoricoAlteracao) {
		DAOFactory.daoHistoricoAlteracao = daoHistoricoAlteracao;
	}

	public static iDAOLoja getDaoLoja(){
        return daoLoja;
    }

    public static iDAOObreiro getDAOObreiro() {
        return daoObreiro;
    }

    public static iDAOGrau getDAOGrau() {
        return daoGrau;
    }
    public static iDAOParametro getDAOParametro() {
        return daoParametro;
    }
    public static iDAOCargo getDAOCargo() {
        return daoCargo;
    }

    public static iDAOAnuidadeObreiro getDAOAnuidadeObreiro() {
        return daoAnuidadeObreiro;
    }
    
     public static iDAOAnuidadeCorpo getDAOAnuidadeCorpo() {
        return daoAnuidadeCorpo;
    }
     
     public static iDAOTipoEspecie getDAOTipoEspecie(){
         return daoTipoEspecie;
     }
}