package br.qg.loja.controle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.qg.loja.integracao.*;
import br.qg.loja.modelo.*;

@ManagedBean
@SessionScoped
public class Login {
	private String cpf;
	private String ime;
	private Obreiro obreiro=null;
		
	
	public Obreiro getObreiro() {
		return obreiro;
	}
	public void setObreiro(Obreiro obreiro) {
		this.obreiro = obreiro;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	
	
	public String atualizarDadosPessoais(){
		iDAOObreiro dao= DAOFactory.getDAOObreiro();
		
		
		String alteracoes=this.verificaAlteracoes(this.obreiro,dao.buscaObreiroPorIME(this.obreiro.getIme()));
		boolean result=dao.armazenaObreiro(this.obreiro);
		if(!alteracoes.equals("")){
			HistoricoAlteracao ha=new HistoricoAlteracao();
			ha.setDate(new Date());
			ha.setCpf(this.obreiro.getCpf());
			ha.setDescricao(alteracoes);
			DAOFactory.getDaoHistoricoAlteracao().inserir(ha);
		}
		String mensagem="";
		String titulo="";
		if(result){
			titulo="Confirmação:";
			mensagem="Seus dados foram atualizados com sucesso!";
		}else{
			titulo="Atenção!";
			mensagem="ocorreu um problema interno no sistema e seus dados não poderam ser atualizados.";
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(titulo,mensagem ));
		return "";
	}
	
	public String verificaAlteracoes(Obreiro newObj, Obreiro oldObj){
		String alteracoes="";
		if(!newObj.getEmail().equals(oldObj.getEmail())){
			alteracoes+="E-mail:\n";
			alteracoes+="Antigo: "+oldObj.getEmail()+"\n";
			alteracoes+="Novo: "+newObj.getEmail()+"\n\n\n";
		}
		if(!newObj.getCpf().equals(oldObj.getCpf())){
			alteracoes+="CPF:\n";
			alteracoes+="Antigo: "+oldObj.getCpf()+"\n";
			alteracoes+="Novo: "+newObj.getCpf()+"\n\n\n";
		}
		try{
			if(!newObj.getDtNascimento().equals(oldObj.getDtNascimento())){
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				alteracoes+="Data de nascimento:\n";
				alteracoes+="Antiga: "+formatter.format(oldObj.getDtNascimento()) +"\n";
				alteracoes+="Nova: "+formatter.format(newObj.getDtNascimento()) + "\n\n\n";
			}
		}catch(NullPointerException e){
			alteracoes+="Data de nascimento.";
		}
		
		if(!newObj.getProfissao().equals(oldObj.getProfissao())){
			alteracoes+="Profissão:\n";
			alteracoes+="Antiga: "+oldObj.getProfissao() +"\n";
			alteracoes+="Nova: "+newObj.getProfissao() + "\n\n\n";
		}
		
		if(!newObj.getCargo().equals(oldObj.getCargo())){
			alteracoes+="Cargo:\n";
			alteracoes+="Antigo: "+oldObj.getCargo() +"\n";
			alteracoes+="Novo: "+newObj.getCargo() + "\n\n\n";
		}
		
		if(!newObj.getEnderecoResidencial().equals(oldObj.getEnderecoResidencial())){
			alteracoes+="Endereço Residencial:\n";
			alteracoes+="Antigo: "+oldObj.getEnderecoResidencial() +"\n";
			alteracoes+="Novo: "+newObj.getEnderecoComercial() + "\n\n\n";
		}
		
		if(!newObj.getEnderecoComercial().equals(oldObj.getEnderecoComercial())){
			alteracoes+="Endereço Comercial:\n";
			alteracoes+="Antigo: "+oldObj.getEnderecoComercial() +"\n";
			alteracoes+="Novo: "+newObj.getEnderecoComercial() + "\n\n\n";
		}
		
		if(!newObj.getNomeEsposa().equals(oldObj.getNomeEsposa())){
			alteracoes+="Nome esposa:\n";
			alteracoes+="Antigo: "+oldObj.getNomeEsposa()+"\n";
			alteracoes+="Novo: "+newObj.getNomeEsposa() + "\n\n\n";
		}
		try{
			if(!newObj.getDtNascimentoEsposa().equals(oldObj.getDtNascimentoEsposa())){
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				alteracoes+="Data de nascimento da esposa:\n";
				alteracoes+="Antiga: "+formatter.format(oldObj.getDtNascimentoEsposa()) +"\n";
				alteracoes+="Nova: "+formatter.format(newObj.getDtNascimentoEsposa()) + "\n\n\n";
			}
		}catch(NullPointerException e){
			alteracoes+="Data de nascimento da esposa.";
		}
		
		return alteracoes;
	}
	
	
	public String autenticar(){
		iDAOObreiro dao= DAOFactory.getDAOObreiro();
		Obreiro obr= dao.autenticar(this.ime, this.cpf);
		
		
		String paginaRetorno ="";
		if (obr==null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Atenção", "Não foi possível encontrar o registro correspondente ao IME - "+ this.ime + ", e CPF - " + this.cpf+". Caso seus dados estejam corretos, entre em contato com: supreconsdelsc@ccmail.com.br"));
		}else{
			/*
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("user", obr);
			//SomeObject yourVariable = (SomeObject) sessionMap.get("somekey");
			*/
			this.obreiro=obr;
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Acesso autorizado"));
			paginaRetorno="meus_dados.xhtml?faces-redirect=true";
		}
		return paginaRetorno;
	}
	
	public String sair(){
		this.obreiro=null;
		String paginaRetorno="index.xhtml?faces-redirect=true";
		return paginaRetorno;
	}
}
