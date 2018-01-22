package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.qg.loja.integracao.iDAOUsuario;
import br.qg.loja.modelo.UsuarioSistema;

public class DAOUsuario implements iDAOUsuario{

	private final String BASE_SELECT= "select id, login, password, is_admin, is_active from usuario";
	private void preencheObjeto(UsuarioSistema obj, ResultSet rs) throws SQLException{
		obj.setId(rs.getInt("id"));
        obj.setLogin(rs.getString("login"));
        obj.setPassword(rs.getString("password"));
        obj.setIsAdmin(rs.getBoolean("is_admin"));
        obj.setIsActive(rs.getBoolean("is_active"));
	}
	
	@Override
	public ArrayList<UsuarioSistema> buscaUsuarios() {
		ArrayList<UsuarioSistema> lista = new ArrayList<UsuarioSistema>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = BASE_SELECT+ " order by login";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
            	UsuarioSistema obj = new UsuarioSistema(); // cria um novo objeto  para cada registro do banco de dados
            	this.preencheObjeto(obj,rs);
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
	}

	@Override
	public UsuarioSistema autentica(String login, String password) {
		UsuarioSistema obj=null;
		try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = BASE_SELECT+ " where login=? and password=md5(?) and is_active=1";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
            	obj = new UsuarioSistema(); // cria um novo objeto  para cada registro do banco de dados
            	this.preencheObjeto(obj,rs);
           }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return obj;
	}

	@Override
	public boolean gravaDados(UsuarioSistema usuario) {
		boolean result=false;
		try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            Connection c = con.getCon();
            if(usuario.getId()<1){
            	String sql = "insert into usuario (login, password, is_admin,is_active) values(?,md5(?),?,1)";
            	PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, usuario.getLogin());
                ps.setString(2, usuario.getPassword());
                ps.setBoolean(3, usuario.getIsAdmin());
                ps.executeUpdate(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
                result=true;
            }else{
            	String sql = "update usuario set login=?, is_admin=?,is_active=? where id=?";
            	PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, usuario.getLogin());
                ps.setBoolean(2, usuario.getIsAdmin());
                ps.setBoolean(3, usuario.getIsActive());
                ps.setLong(4, usuario.getId());
                ps.executeUpdate(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
                result=true;
                
                if(usuario.getPassword().length()!=0 && usuario.getPassword().length()!=32){ //update password just when a new one is inputed
                	sql = "update usuario set password=md5(?) where id=?";
                	ps = c.prepareStatement(sql);
                	ps.setString(1, usuario.getPassword());
                    ps.setLong(2, usuario.getId());
                    ps.executeUpdate(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
                    result=true;        
                }
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
	}

	/**
	 * @return: return null if not object is found
	 */
	@Override
	public UsuarioSistema buscaPorChave(int chave) {
		UsuarioSistema obj=null;
		try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = BASE_SELECT+ " where id=?";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, chave);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
            	obj = new UsuarioSistema(); // cria um novo objeto  para cada registro do banco de dados
            	this.preencheObjeto(obj,rs);
           }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return obj;
	}

}
