package br.qg.loja.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.PagamentoAnuidadeCorpo;
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;

@ManagedBean
@SessionScoped
public class PagamentoLojaMB {
	private List<PagamentoAnuidadeCorpo> pagamentos;
	private int anoAtual=new Date().getYear()+1900;
	
	
	public PagamentoLojaMB(){
		this.pagamentos=getPagamentoLojas();
	}
	
	private ArrayList<PagamentoAnuidadeCorpo> getPagamentoLojas(){
		List<Loja> lojas=DAOFactory.getDaoLoja().buscaLojas();
		ArrayList<PagamentoAnuidadeCorpo> pagamentos = new ArrayList<PagamentoAnuidadeCorpo>();
		 for(Loja l: lojas){
			 PagamentoAnuidadeCorpo pag= DAOFactory.getDAOAnuidadeCorpo().buscaPagamentoPorCorpoAno(l, anoAtual);
			 if(pag == null){
				 pag = new PagamentoAnuidadeCorpo();
				 pag.setLoja(l);
				 pag.setAnoPagamento(anoAtual);
			 }
			 pagamentos.add(pag);
		 }
		 return pagamentos;
	}
			
	public void onChangeYearAtualizaPagamento(PagamentoAnuidadeCorpo  pagamentoSelecionado){
		int ano=pagamentoSelecionado.getAnoPagamento();
		Loja lj= pagamentoSelecionado.getLoja();
		PagamentoAnuidadeCorpo pag=DAOFactory.getDAOAnuidadeCorpo().buscaPagamentoPorCorpoAno(lj, ano);
		int i=pagamentos.indexOf(pagamentoSelecionado);
		if (pag == null){
			System.out.println("Não achou");
			pag= new PagamentoAnuidadeCorpo();
			pag.setAnoPagamento(ano);
			pag.setLoja(lj);
		}
		pagamentos.set(i, pag);
	}
	
	public void salvaPagamento(PagamentoAnuidadeCorpo pag){
		String titulo="";
		String mensagem="";
		if(DAOFactory.getDAOAnuidadeCorpo().armazenaPagamentoAnuidadeCorpo(pag)){
			titulo="Confirmação";
			mensagem="Dados do pagamento registrados com sucesso!";
		}else{
			titulo="Atenção!";
			mensagem="Ocorreu um problema interno no sistema e seus dados não poderam ser atualizados.";
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(titulo,mensagem ));
	}
	
	public int getAnoAtual() {
		return anoAtual;
	}


	public void setAnoAtual(int anoAtual) {
		this.anoAtual = anoAtual;
	}


	public List<PagamentoAnuidadeCorpo> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoAnuidadeCorpo> pagamentos) {
		this.pagamentos = pagamentos;
	}

}
