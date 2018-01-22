package br.qg.loja.controle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.UsuarioSistema;

@ManagedBean
@SessionScoped
public class LoginAdmin {
	private String login;
	private String password;
	private boolean admin;
	private UsuarioSistema usuario; //este é o usuário que está logado
	//utilized in change password functionality
	private String currentPassword;
	private String newPassword;
	
	public String autenticar(){
		String result="";
		if(this.login.equals("admin") && this.password.equals("sgc12111832") || this.autentica(login, password)){
			result="obreiros.xhtml";
			this.admin=true;
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Atenção", "Credenciais de acesso inválidas"));
		}
		return result;
	}
	
	public boolean alteraSenha(){
		boolean result=false;
		if(DAOFactory.getiDAOUsuario().autentica(this.usuario.getLogin(), this.currentPassword)!=null){
			this.usuario.setPassword(this.newPassword);
			DAOFactory.getiDAOUsuario().gravaDados(this.usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Senha atualizada com sucesso!"));
			result=true;
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Senha atual não confere."));
		}
		return result;
	}
	
	public String logout(){
		this.admin=false;
		this.login="";
		this.password="";
		return "login.xhtml";
	}
	
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
	public boolean getAdmin() {
		return admin;
	}
	public void setAdmin(boolean isAdmin) {
		this.admin = isAdmin;
	}
	
	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}
	
	

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	private boolean autentica(String login, String password){
		this.usuario=DAOFactory.getiDAOUsuario().autentica(login, password);
		return this.usuario==null?false:true;
	}
	
}
