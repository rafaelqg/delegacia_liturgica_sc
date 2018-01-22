package br.qg.loja.controle;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOObreiro;
import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;

@ManagedBean
@ViewScoped
public class ObreiroGrauMB {
	private List<SelectItem> cargos;
	private List<SelectItem> lojas;
	private List<SelectItem> graus;
	
	private int selectedLojaId;
	private int selectedCargoId;
	private int selectedGrauId;
	private Date data;
	
	/**
	 * 
	 * @param corpo: varia de: 1 - perfeicao; 2 - vera cruz; 3-kadosh ; 4- real segredo.
	 */
	public boolean updateOptions(int corpo){
		ArrayList<Cargo> listaCargos=DAOFactory.getDAOCargo().buscaCargos();
		ArrayList<Loja> listaLojas=DAOFactory.getDaoLoja().buscaLojasPorCorpo(corpo);
		ArrayList<Grau> listaGraus=null;
		
		this.cargos=new ArrayList<SelectItem>();
		this.cargos.add(new SelectItem(null, ""));//Opção vazia para permitir seleção
		for(Cargo c: listaCargos){
			cargos.add(new SelectItem(c.getNumero(),c.getDescricao()));
		}
		
		this.lojas=new ArrayList<SelectItem>();
		for(Loja l: listaLojas){
			lojas.add(new SelectItem(l.getNumero(),l.getNome()));
		}
		
		switch (corpo){
			case 1:
				listaGraus = getGrausRange(4, 14);
				break;
			case 2:
				listaGraus= getGrausRange(15, 18);
				break;
			case 3:
				listaGraus = getGrausRange(19, 30);
				break;
			case 4:
				listaGraus = getGrausRange(31, 33);
				break;
		}
		
		this.graus=new ArrayList<SelectItem>();
		for(Grau g: listaGraus){
			graus.add(new SelectItem(g.getNumero(),String.valueOf(g.getNumero())));
		}
		this.data=null;
		this.selectedCargoId=0;
		this.selectedGrauId=0;
		this.selectedLojaId=0;
		return true;
	}
	
	public void setSelectedValues(ObreiroLoja ol, int corpo){
		this.updateOptions(corpo);
		if(ol.getLoja()!=null){
			this.selectedLojaId=ol.getLoja().getNumero();
		}
		if(ol.getGrau()!=null){
			this.selectedGrauId=ol.getGrau().getNumero();
		}
		this.data=ol.getData();

		if(ol.getCargo()!=null){
			this.selectedCargoId=ol.getCargo().getNumero();
		}
	}
	
	  private ArrayList<Grau> getGrausRange(int floor, int roof) {
	        ArrayList<Grau> graus = new ArrayList<Grau>();
	        for (int i = floor; i <= roof; i++) {
	            Grau g = new Grau();
	            g.setNumero(i);
	            graus.add(g);
	        }
	        return graus;
	    }
	
	
	
	public List<SelectItem> getCargos() {
		return cargos;
	}
	
	public void saveRelacionamento(){
		//busca loja
		Loja l=DAOFactory.getDaoLoja().buscaLojaPorChave(this.selectedLojaId);
		//busca grau
		Grau g=new Grau();
		g.setNumero(this.selectedGrauId);
		//busca cargo
		Cargo c= DAOFactory.getDAOCargo().buscaCargoPorCodigo(this.selectedCargoId);
		//busca obreiro
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		ObreiroMB obreiroMB =(ObreiroMB)sessionMap.get("obreiroMB");
		Obreiro o=obreiroMB.getObreiro();
		//insere relacionamento
		iDAOObreiro dao= DAOFactory.getDAOObreiro();
		boolean result=dao.armazenaRelacionamentoLoja(l, o, g, data, c);
		obreiroMB.carregaRelacionamentos();
		String mensagem="";
		String titulo="";
		if(result){
			titulo="Confirmação:";
			mensagem="Dados do obreiro atualizados com sucesso!";
		}else{
			titulo="Atenção!";
			mensagem="Ocorreu um problema interno no sistema e seus dados não poderam ser atualizados.";
		}
		//menssagem de retorno
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(titulo,mensagem ));
	}
	
	public void excluiRelacionamento(int ime, int grau){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		ObreiroMB obreiroMB =(ObreiroMB)sessionMap.get("obreiroMB");
		iDAOObreiro dao= DAOFactory.getDAOObreiro();
		String mensagem="";
		String titulo="";
		if (dao.removeRelacionamentoObreiroLoja(ime, grau)){
			titulo="Confirmação:";
			mensagem="Relacionamento excluído!";
			obreiroMB.carregaRelacionamentos();
		}else{
			titulo="Atenção!";
			mensagem="Ocorreu um problema interno no sistema e seus dados não poderam ser atualizados.";
		}
		//menssagem de retorno
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(titulo,mensagem ));	
	}
	
	public void sair(){
		
	}
	
	

	public void setCargos(ArrayList<SelectItem> cargos) {
		this.cargos = cargos;
	}

	public List<SelectItem> getLojas() {
		return lojas;
	}

	public void setLojas(ArrayList<SelectItem> lojas) {
		this.lojas = lojas;
	}

	public List<SelectItem> getGraus() {
		return graus;
	}

	public void setGraus(ArrayList<SelectItem> graus) {
		this.graus = graus;
	}

	public int getSelectedLojaId() {
		return selectedLojaId;
	}

	public void setSelectedLojaId(int selectedLojaId) {
		this.selectedLojaId = selectedLojaId;
	}

	public int getSelectedCargoId() {
		return selectedCargoId;
	}

	public void setSelectedCargoId(int selectedCargoId) {
		this.selectedCargoId = selectedCargoId;
	}

	public int getSelectedGrauId() {
		return selectedGrauId;
	}

	public void setSelectedGrauId(int selectedGrauId) {
		this.selectedGrauId = selectedGrauId;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
}
