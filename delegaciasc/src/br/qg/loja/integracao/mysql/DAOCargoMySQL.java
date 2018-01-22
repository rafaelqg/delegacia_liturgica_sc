/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.qg.loja.integracao.iDAOCargo;
import br.qg.loja.modelo.Cargo;

/**
 *
 * @author RafaelQG
 */
public class DAOCargoMySQL implements iDAOCargo {

    @Override
    public ArrayList<Cargo> buscaCargos() {
        ArrayList<Cargo> resultado = new ArrayList<Cargo>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = "select numero, nome from cargo_loja";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Cargo obj = new Cargo(); // cria um novo objeto  para cada registro do banco de dados
                obj.setDescricao(rs.getString(2));
                obj.setNumero(rs.getInt(1));
                resultado.add(obj);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }

    @Override
    public Cargo buscaCargoPorCodigo(int codigo) {
        Cargo resultado = null;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = "select numero, nome from cargo_loja where numero=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                resultado= new Cargo(); // cria um novo objeto  para cada registro do banco de dados
                resultado.setDescricao(rs.getString(2));
                resultado.setNumero(rs.getInt(1));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }
    
}
