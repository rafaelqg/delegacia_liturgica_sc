package br.qg.loja.controle;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.UsuarioSistema;

@ManagedBean
@SessionScoped
public class UsuarioMB {
	private UsuarioSistema usuario= new UsuarioSistema(); //este � o usu�rio que est� sendo manipulado na tela de administra��o
	
	public UsuarioMB(){
		
	}

	public ArrayList<UsuarioSistema> buscaUsuarios(){
		return DAOFactory.getiDAOUsuario().buscaUsuarios();
	}
	
	private String getMessage(String key){
		FacesContext context = FacesContext.getCurrentInstance();
		Locale locale = context .getViewRoot().getLocale();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ResourceBundle bundle = ResourceBundle.getBundle("resources.language", locale, loader);
		String msg = bundle.getString(key);
		return msg;
	}
	
	public UsuarioSistema getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioSistema usuario) {
		this.usuario = usuario;
	}
	
	private boolean validaDados(){
		boolean result= true;
		if(this.usuario.getLogin().trim().equals("") && this.usuario.getPassword().trim().equals("")){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aten��o", getMessage("LBL_USER_VALIDATION_EMPTY")));
			return false;
		}
		return result;
	}
	
	public void registrar(){
		
		boolean result=DAOFactory.getiDAOUsuario().gravaDados(usuario);
		if(this.validaDados()){
			if(result){
				String message=usuario.getId()<1?"Usu�rio registrado":"Dados do usu�rio atualizados.";
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aten��o", message));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aten��o", "N�o foi poss�vel registrar este usu�rio! Tente outro login."));
			}
		}
		
	}
	
	public String editar(UsuarioSistema obj){
		this.usuario=obj;
        String paginaRetorno = "usuario_form.xhtml";
		return paginaRetorno;
	}
	
	public void desabilitar(UsuarioSistema obj){
		obj.setIsActive(false);
		DAOFactory.getiDAOUsuario().gravaDados(obj);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aten��o", "Usu�rio " + obj.getLogin()+ " desabilitado."));
	}
	
	public void abilitar(UsuarioSistema obj){
		obj.setIsActive(true);
		DAOFactory.getiDAOUsuario().gravaDados(obj);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aten��o", "Usu�rio " + obj.getLogin()+ " desabilitado."));
	}
	
		
}
