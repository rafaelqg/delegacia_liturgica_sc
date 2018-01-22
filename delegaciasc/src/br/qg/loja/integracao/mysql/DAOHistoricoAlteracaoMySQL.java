/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOGrau;
import br.qg.loja.integracao.iDAOHistoricoAlteracao;
import br.qg.loja.modelo.HistoricoAlteracao;


/**
 *
 * @author Rafael
 */
public class DAOHistoricoAlteracaoMySQL implements iDAOHistoricoAlteracao {  

	@Override
	public ArrayList<HistoricoAlteracao> buscaTodas() {
		ArrayList<HistoricoAlteracao> lista = new ArrayList<HistoricoAlteracao>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = "select descricao_alteracao,date_time_alteracao,CPF_responsavel from historico_alteracoes order by date_time_alteracao desc";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
            	HistoricoAlteracao obj = new HistoricoAlteracao(); // cria um novo objeto  para cada registro do banco de dados
                obj.setDescricao(rs.getString("descricao_alteracao")); 
                obj.setDate(rs.getTimestamp(("date_time_alteracao")));
                obj.setCpf(rs.getString("CPF_responsavel")); 
                obj.setObreiro(DAOFactory.getDAOObreiro().buscaObreiroPorCPF(rs.getString("CPF_responsavel")));
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
	}

	@Override
	public boolean inserir(HistoricoAlteracao ha) {
		//insert
		boolean result=false;
		try{
			DataSourceMySQL con = new DataSourceMySQL(); 
	        String sql = "insert into historico_alteracoes (descricao_alteracao,CPF_responsavel, date_time_alteracao) values(?,?,?)";     
	        PreparedStatement ps = con.getCon().prepareStatement(sql);
	        ps.setString(1, ha.getDescricao());
	        ps.setString(2, ha.getCpf());
	        ps.setTimestamp(3, new java.sql.Timestamp(ha.getDate().getTime()));
	        ps.executeUpdate(); 
	        result=true;
		}catch(Exception e){
			
		}
        return result;
	}
    
}
