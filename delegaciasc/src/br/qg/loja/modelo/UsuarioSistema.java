package br.qg.loja.modelo;

/**
 * 
 * @author Rafael
 *
 */
public class UsuarioSistema {
	private long id;
	private String login;
	private String password;
	private boolean isAdmin;
	private boolean isActive;
		
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	
	
}
