/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOAnuidadeCorpo;
import br.qg.loja.integracao.iDAOAnuidadeObreiro;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.PagamentoAnuidadeCorpo;
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;
import br.qg.loja.modelo.TipoEspecie;

/**
 *
 * @author RafaelQG
 */
public class DAOAnuidadeCorpo implements iDAOAnuidadeCorpo{
    private final String SELECT_PAGAMENTO_CORPO= "SELECT numero,loja,ano_pagamento,dt_comprovante_dep_bancario, nr_prancha_encaminhamento, dt_prancha_encaminhamento, diligencia_motivo, dt_emissao_diligencia, dt_retorno_diligencia, numero_romaneio, dt_romaneio, valor_deposito, tipo_especie, numero_guia FROM loja_dados_financeiros"; // código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)";

    private boolean existePagamento(int numero) {
        boolean resultado = false;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_PAGAMENTO_CORPO + " where numero=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            if (rs.next()) {
                resultado = true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }
    
    /*
    obreiro,numero_loja_filosofica_atual,anoPagamento,
        dt_comprovante_dep_bancario,nr_prancha_encaminhamento,dt_prancha_encaminhamento,
        diligencia_motivo,dt_emissao_diligencia,
        dt_retorno_diligencia,numero_romaneio,dt_romaneio
         */
    
     private void montaObjeto(PagamentoAnuidadeCorpo obj, ResultSet rs) throws Exception {
         //Aqui recupero todos os campos relevantes desejados.
         obj.setNumero(rs.getInt("numero"));
         obj.setLoja(DAOFactory.getDaoLoja().buscaLojaPorChave(rs.getInt("loja")));
         obj.setAnoPagamento(rs.getInt("ano_pagamento"));
         obj.setDtComprovanteBancario(rs.getDate("dt_comprovante_dep_bancario") != null ? new java.util.Date(rs.getDate("dt_comprovante_dep_bancario").getTime()) : null);
         obj.setNrPrancha(String.valueOf(rs.getInt("nr_prancha_encaminhamento")));
         obj.setDtPrancha(rs.getDate("dt_prancha_encaminhamento") != null ? new java.util.Date(rs.getDate("dt_prancha_encaminhamento").getTime()) : null);
         obj.setDtEmissao(rs.getDate("dt_emissao_diligencia") != null ? new java.util.Date(rs.getDate("dt_emissao_diligencia").getTime()) : null);
         obj.setDtRetorno(rs.getDate("dt_retorno_diligencia") != null ? new java.util.Date(rs.getDate("dt_retorno_diligencia").getTime()) : null);
         obj.setDtRomaneio(rs.getDate("dt_romaneio") != null ? new java.util.Date(rs.getDate("dt_romaneio").getTime()) : null);
         obj.setMotivo(rs.getString("diligencia_motivo"));
         obj.setNrRomaneio(String.valueOf(rs.getInt("numero_romaneio")));
         obj.setValorDeposito(rs.getDouble("valor_deposito"));
         obj.setTipoEspecie(DAOFactory.getDAOTipoEspecie().getTipoEspecie(rs.getInt("tipo_especie")));
         obj.setNumeroGuia(rs.getInt("numero_guia"));
     }
    
    private void prepareObjectToSQLStatement(PreparedStatement ps, PagamentoAnuidadeCorpo obj) throws SQLException{
         // Susbstitui cada um dos "?" do c�digo SQL pelo valor correspondente.
        ps.setInt(1, obj.getLoja().getNumero());
        ps.setInt(2, obj.getAnoPagamento());
        if(obj.getDtComprovanteBancario()!=null){
            ps.setDate(3, new java.sql.Date(obj.getDtComprovanteBancario().getTime()));
        }else{
            ps.setObject(3, null);
        }
        ps.setDouble(4, obj.getValorDeposito());
        if(obj.getTipoEspecie()!=null){
        	ps.setInt(5, obj.getTipoEspecie().getNumero());
        }else{
        	ps.setObject(5, null);
        }
        ps.setString(6, obj.getMotivo());
        try{
           if (obj.getNrPrancha()!=null && !obj.getNrPrancha().equals("")){
                ps.setInt(7, Integer.valueOf(obj.getNrPrancha()));
           }else{
                ps.setObject(7, null);
           }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        if(obj.getDtPrancha()!=null){
            ps.setDate(8, new java.sql.Date(obj.getDtPrancha().getTime()));
        }else{
            ps.setObject(8, null);    
        }
        if(obj.getDtEmissao()!=null){
            ps.setDate(9, new java.sql.Date(obj.getDtEmissao().getTime()));
        }else{
            ps.setObject(9, null);
        }
        if(obj.getDtRetorno()!=null){
             ps.setDate(10, new java.sql.Date(obj.getDtRetorno().getTime()));
        }else{
            ps.setObject(10, null); 
        }
      // ps.setString(11, obj.getNrEmissao());
        if(obj.getDtRomaneio()!=null){
            ps.setDate(11, new java.sql.Date(obj.getDtRomaneio().getTime()));
        }else{
            ps.setObject(11, null);
        }
        if(!obj.getNrRomaneio().equals("")){
        ps.setString(12, obj.getNrRomaneio());
        }else{
        	ps.setObject(12, null);
        }

        ps.setInt(13, obj.getNumeroGuia());
        
    }
    
    @Override
    public boolean armazenaPagamentoAnuidadeCorpo(PagamentoAnuidadeCorpo obj) {
        boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        Connection con = ds.getCon();// Recupera uma conexão ativa ou cria uma nova conexão
        try {
            if (existePagamento(obj.getNumero())) {
                //update
                String sql = "update loja_dados_financeiros set loja=?,ano_pagamento=?,dt_comprovante_dep_bancario=?,valor_deposito=?,tipo_especie=?, diligencia_motivo=?,nr_prancha_encaminhamento=?,dt_prancha_encaminhamento=?,dt_emissao_diligencia=?,dt_retorno_diligencia=?,dt_romaneio=?,numero_romaneio=?,numero_guia=? where numero=?"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
                PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
                prepareObjectToSQLStatement(ps,obj);    
                ps.setInt(14, obj.getNumero());        
                ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
                sucess = true;
            } else {
               //insert
                String sql = "insert into loja_dados_financeiros (loja,ano_pagamento,dt_comprovante_dep_bancario, valor_deposito,tipo_especie, diligencia_motivo,nr_prancha_encaminhamento,dt_prancha_encaminhamento, dt_emissao_diligencia,dt_retorno_diligencia,dt_romaneio,numero_romaneio,numero_guia) values(?,?,?,?,?,?,?,?,?,?,?,?,?)"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.    
                PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
                prepareObjectToSQLStatement(ps,obj);
                ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
                sucess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucess;
    }

    @Override
    public ArrayList<PagamentoAnuidadeCorpo> buscaPagamentoPorCorpo(Loja obj) {
         ArrayList<PagamentoAnuidadeCorpo> resultado = new ArrayList<PagamentoAnuidadeCorpo>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_PAGAMENTO_CORPO + " where loja=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, obj.getNumero());
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                PagamentoAnuidadeCorpo pagamento= new PagamentoAnuidadeCorpo();
                montaObjeto(pagamento, rs);
                resultado.add(pagamento);      
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }

    @Override
    public boolean excluiPagamentoCorpo(PagamentoAnuidadeCorpo obj) {
        boolean resultado=true;
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql ="delete from loja_dados_financeiros where numero=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, obj.getNumero());
            ps.executeUpdate();
        } catch (Exception e) {
            resultado=false;
            System.err.println(e.getMessage());
        }
        return resultado;
    }

	@Override
	public PagamentoAnuidadeCorpo buscaPagamentoPorCorpoAno(Loja obj, int ano) {
		PagamentoAnuidadeCorpo resultado = null;
        try {
            DataSourceMySQL con = new DataSourceMySQL(); 
            String sql = SELECT_PAGAMENTO_CORPO + " where loja=? and ano_pagamento=?";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, obj.getNumero());
            ps.setInt(2, ano);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagamentoAnuidadeCorpo pagamento= new PagamentoAnuidadeCorpo();
                montaObjeto(pagamento, rs);
                resultado=pagamento;  
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
	}


    
}
