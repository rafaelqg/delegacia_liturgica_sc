package br.qg.loja.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;
import br.qg.loja.modelo.TipoEspecie;

@ManagedBean
@SessionScoped
public class PagamentoIrmaoMB  {
	private int lojaId;
	private List<PagamentoAnuidadeObreiro> pagamentos;
	private int anoAtual=new Date().getYear()+1900;
	private PagamentoAnuidadeObreiro selectedPagamento;
	private List<SelectItem> graus= new ArrayList<SelectItem>();
	
	public ArrayList<SelectItem> getLojas(){
		List<Loja> lojas=DAOFactory.getDaoLoja().buscaLojas();
		ArrayList<SelectItem> items=new ArrayList<SelectItem>();
		 for(Loja l: lojas){
			 items.add(new SelectItem(l.getNumero(),l.getNome()));
			}
		 return items;
	}
	
	private void inicializaPagamentosObreirojosPorLoja(Loja lj){
		pagamentos= DAOFactory.getDAOAnuidadeObreiro().buscaPagamentoAnuidadesPorLoja(lj);
		TipoEspecie te=DAOFactory.getDAOTipoEspecie().getTipoEspecie(2);
		for(PagamentoAnuidadeObreiro pag: pagamentos){
			if(pag.getAno()==0){
				pag.setAno(new Date().getYear()+1900);
			}
			
			//se o obreiro não tiver nenhum pagamento, este obrigatoriamente terá um do tipo 2 (atividade)
			if(pag.getNumero()<=0){
				pag.setTipoEspecie(te);
				DAOFactory.getDAOAnuidadeObreiro().armazenaPagamentoAnuidade(pag);
			}	
		}
	}
	
	/**
	 * Deixa pré carregado os pagamentos da primeira loja da lista
	 */
	public PagamentoIrmaoMB(){
		List<Loja> lojas=DAOFactory.getDaoLoja().buscaLojas();
		this.inicializaPagamentosObreirojosPorLoja(lojas.get(0));
		List<Grau> listaGraus=DAOFactory.getDAOGrau().buscaGraus();
		for (Grau g: listaGraus){
			graus.add(new SelectItem(g.getNumero(), String.valueOf(g.getNumero())));
		}
	}
		
	public void carregaPagamentos(){
		Loja loja= DAOFactory.getDaoLoja().buscaLojaPorChave(lojaId);
		this.inicializaPagamentosObreirojosPorLoja(loja);		
	}
	
	public void onChangeYearAtualizaPagamento(PagamentoAnuidadeObreiro  pagamentoSelecionado){
		int ano=pagamentoSelecionado.getAno();
		Obreiro obr=pagamentoSelecionado.getObreiro();
		Loja lj= pagamentoSelecionado.getLoja();
		TipoEspecie te=pagamentoSelecionado.getTipoEspecie();
		int grau=pagamentoSelecionado.getGrau();
		PagamentoAnuidadeObreiro pag=DAOFactory.getDAOAnuidadeObreiro().existePagamento(ano, obr, lj, te,grau);
		int i=pagamentos.indexOf(pagamentoSelecionado);
		if (pag == null){
			pag= new PagamentoAnuidadeObreiro();
			pag.setObreiro(obr);
			pag.setAno(ano);
			pag.setLoja(lj);
			pag.setTipoEspecie(te);
			if (te.getNumero()!=2){
				pag.setGrau(grau);
			}
		}
		pagamentos.set(i, pag);
	}
	
	public void salvaPagamento(PagamentoAnuidadeObreiro pag){
		String titulo="";
		String mensagem="";
		if(DAOFactory.getDAOAnuidadeObreiro().armazenaPagamentoAnuidade(pag)){
			titulo="Confirmação";
			mensagem="Dados do pagamento registrados com sucesso!";
		}else{
			titulo="Atenção!";
			mensagem="Ocorreu um problema interno no sistema e seus dados não poderam ser atualizados.";
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(titulo,mensagem ));
	}
	
	public void incluirPagamentoAtividade(){
		incluirPagamento(2);
	}
	
	public void incluirPagamentoElevacao(){
		incluirPagamento(3);
	}
	
	public void incluirPagamentoRegularizacao(){
		incluirPagamento(4);
	}
	
	
	private void incluirPagamento(int tipoEspecie){
		//incluir mensagem se já existir pagamento para o tipoEspecie, obreiro, e ano e não criar nova entrada
		int ano=this.selectedPagamento.getAno();
		Obreiro obr=this.selectedPagamento.getObreiro();
		Loja lj= this.selectedPagamento.getLoja();
		TipoEspecie te= DAOFactory.getDAOTipoEspecie().getTipoEspecie(tipoEspecie);
		int grau=this.selectedPagamento.getGrau();
		PagamentoAnuidadeObreiro pag=DAOFactory.getDAOAnuidadeObreiro().existePagamento(ano, obr, lj, te,grau);
		if(pag==null){
			pag= new PagamentoAnuidadeObreiro();
			pag.setAno(ano);
			pag.setLoja(lj);
			pag.setObreiro(obr);
			pag.setTipoEspecie(te);
			if(DAOFactory.getDAOAnuidadeObreiro().armazenaPagamentoAnuidade(pag)){
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Informação","Pagamento inserido."));
				//para atualização da tela
				this.carregaPagamentos();
			}
		}else{
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Atenção","Já existe pagamento para o obreiro -"+obr.getNome() +" - no ano de "+ ano+ ", do tipo espécie - "+te.getNome()));
		}
	
	}
	
    public void excluiPagamento(){
    	int ano=this.selectedPagamento.getAno();
		Obreiro obr=this.selectedPagamento.getObreiro();
		Loja lj= this.selectedPagamento.getLoja();
		TipoEspecie te= this.selectedPagamento.getTipoEspecie();
		
    	if (DAOFactory.getDAOAnuidadeObreiro().excluiPagamentosPorObreiroLoja(ano, obr, lj, te)){
    		this.carregaPagamentos();
    		//Se o tipo for 2 (Atividade obreiro), após a exclusão, cadastrar novamente, com os dados vazios. Sempre é necessário manter um tipo=2 na lista
    		if(te.getNumero()==2){
    			PagamentoAnuidadeObreiro pag= new PagamentoAnuidadeObreiro();
    			pag.setAno(ano);
    			pag.setLoja(lj);
    			pag.setObreiro(obr);
    			pag.setTipoEspecie(te);
    			DAOFactory.getDAOAnuidadeObreiro().armazenaPagamentoAnuidade(pag);
    		}
    		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Atenção","Pagamento excluído com sucesso!")); 		
    	}
		
    }
      
	
	
	public int getAnoAtual() {
		return anoAtual;
	}


	public void setAnoAtual(int anoAtual) {
		this.anoAtual = anoAtual;
	}

	public int getLojaId() {
		return lojaId;
	}

	public void setLojaId(int lojaId) {
		this.lojaId = lojaId;
	}

	public List<PagamentoAnuidadeObreiro> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoAnuidadeObreiro> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public PagamentoAnuidadeObreiro getSelectedPagamento() {
		return selectedPagamento;
	}

	public void setSelectedPagamento(PagamentoAnuidadeObreiro selectedPagamento) {
		this.selectedPagamento = selectedPagamento;
	}

	public List<SelectItem> getGraus() {
		return graus;
	}

	public void setGraus(List<SelectItem> graus) {
		this.graus = graus;
	}
}
