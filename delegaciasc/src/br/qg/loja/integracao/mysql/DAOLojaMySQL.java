/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.qg.loja.integracao.iDAOLoja;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;

/**
 *
 * @author Rafael
 */
public class DAOLojaMySQL implements iDAOLoja {

    private final String SELECT_LOJA="SELECT celular, email, endereco_completo, local, nome, numero, telefone,corpo, data_fundacao FROM loja"; // código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)";
    
    @Override
    public ArrayList<Loja> buscaLojasPorCorpo(int corpo) {
        ArrayList<Loja> resultado = new ArrayList<Loja>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_LOJA+" where corpo=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, corpo);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Loja obj = new Loja(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                resultado.add(obj);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }
    
       private void montaObjeto(Loja obj, ResultSet rs) throws Exception {
        // Aqui recupero todos os campos relevantes desejados.
        obj.setCelular(rs.getString("celular")); //o parâmetro é o nome da coluna no banco de dados
        obj.setCorpo(rs.getInt("corpo"));
        obj.setEmail(rs.getString("email"));
        obj.setEnderecoCompleto(rs.getString("endereco_completo"));
        obj.setLocal(rs.getString("local"));
        obj.setNome(rs.getString("nome"));
        obj.setNumero(rs.getInt("numero"));
        obj.setTelefone(rs.getString("telefone"));
        obj.setDataFundacao(rs.getDate("data_fundacao"));
        
    }

    @Override
    public Loja buscaLojaPorChave(int chave) {
        Loja resultado = null;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_LOJA+" where numero=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, chave);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                resultado = new Loja(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(resultado, rs);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Loja> buscaLojas() {
          ArrayList<Loja> resultado = new ArrayList<Loja>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_LOJA + " order by nome asc";// c�digo SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Loja obj = new Loja(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                resultado.add(obj);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }

	@Override
	public boolean registraLoja(Loja loja) {
		boolean result=false;
		try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            Connection c = con.getCon();
            if(loja.getNumero()<1){
            	String sql = "insert into loja (nome, local, endereco_completo,telefone, celular, email, corpo, data_fundacao) values(?,?,?,?,?,?,?, ?)";
            	PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, loja.getNome());
                ps.setString(2, loja.getLocal());
                ps.setString(3, loja.getEnderecoCompleto());
                ps.setString(4, loja.getTelefone());
                ps.setString(5, loja.getCelular());
                ps.setInt(6, loja.getCorpo());
                if(loja.getDataFundacao()!=null){
                ps.setDate(7, new  java.sql.Date(loja.getDataFundacao().getTime()));
                }else{
                	ps.setObject(7, null);
                }
                ps.executeUpdate();
                result=true;
            }else{
            	String sql = "update loja set nome=?, local=?,endereco_completo=?,telefone=?, celular=?,email=?, corpo=?, data_fundacao=? where numero=?";
            	PreparedStatement ps = c.prepareStatement(sql);
            	  ps.setString(1, loja.getNome());
                  ps.setString(2, loja.getLocal());
                  ps.setString(3, loja.getEnderecoCompleto());
                  ps.setString(4, loja.getTelefone());
                  ps.setString(5, loja.getCelular());
                  ps.setString(6, loja.getEmail());
                  ps.setInt(7, loja.getCorpo());
                  
                  if(loja.getDataFundacao()!=null){
                      ps.setDate(8, new  java.sql.Date(loja.getDataFundacao().getTime()));
                      }else{
                      	ps.setObject(8, null);
                      }
                  
                  ps.setInt(9, loja.getNumero());
                ps.executeUpdate(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
                result=true;
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
	}
    
    
    
}
