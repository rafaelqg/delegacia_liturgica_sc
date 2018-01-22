/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.qg.loja.integracao.iDAOGrau;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Obreiro;

/**
 *
 * @author Rafael
 */
public class DAOGrauMySQL implements iDAOGrau{

    @Override
    public ArrayList<Grau> buscaGraus() {
        ArrayList<Grau> lista = new ArrayList<Grau>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = "select numero from grau order by numero";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Grau obj = new Grau(); // cria um novo objeto  para cada registro do banco de dados
                obj.setNumero(rs.getInt("numero"));
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }

    @Override
    public Grau getGrauAtual(Obreiro o) {  
        Grau g= null;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = "SELECT max(numero_grau) FROM obreiro_grau where ime=?;";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, o.getIme());
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                g = new Grau(); // cria um novo objeto  para cada registro do banco de dados
                g.setNumero(rs.getInt("max(numero_grau)"));
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return g;
    }
    
}
