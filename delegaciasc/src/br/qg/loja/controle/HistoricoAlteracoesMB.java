package br.qg.loja.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.HistoricoAlteracao;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;

@ManagedBean
@ViewScoped
public class HistoricoAlteracoesMB {

	public ArrayList<HistoricoAlteracao> getListaAlteracoes(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
		ArrayList<HistoricoAlteracao> lista= DAOFactory.getDaoHistoricoAlteracao().buscaTodas();
		/*
		for(HistoricoAlteracao ha:lista){
			String formatado=ha.getDescricao().replaceAll("\n", "<br />");
			ha.setDescricao(formatado);
		}
		*/
		return lista;
	}
	
}
