/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Rafael
 */
public class DataSourceMySQL {
    
    private final String CONECTION_FAIL_MSG="Não foi possível se conectar com o banco de dados."
            + "\nPara resolver o problema o banco de dados precisa ser inicializado."
            + "\nCaso não saiba como proceder, peça ajuda ao administrador do sistema (rafael.q.g@hotmail.com).";
    
    
    private static Connection con = null;

    public Connection getCon() {
        this.ConectaBD();
        return con;
    }

	private void ConectaBD() {
		String url = "";
		String user = "";
		String passwd = "";
		
			try {
				if (con == null || con.isClosed()) {
					// cria uma nova conexão a primeira vez que o aplicativo a solicita
					Class.forName("com.mysql.jdbc.Driver");
					url = "jdbc:mysql://localhost/sistema_loja";
					user = "root";
					passwd = "root";
					con = DriverManager.getConnection(url, user, passwd);
					System.out.println("Database: Connected in local database");
				}
			} catch (SQLException | ClassNotFoundException e) {
		
				try{
					con = DriverManager.getConnection(url, user, passwd);
					System.out.println("Database: Connected in server database");
				} catch (Exception e1) {
					con = null;
					System.err.println(e1.getMessage());
					JOptionPane.showMessageDialog(null, CONECTION_FAIL_MSG);
					System.exit(1);
				}
		}
	}

    public void DesconectaDB() throws Exception {
        if (con != null) {
           // con.close();
        }
    }
}
