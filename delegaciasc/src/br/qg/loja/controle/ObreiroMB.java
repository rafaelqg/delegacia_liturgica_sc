package br.qg.loja.controle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.qg.loja.integracao.*;
import br.qg.loja.modelo.*;

@ManagedBean
@SessionScoped
public class ObreiroMB {
	private List<Obreiro> lista= new ArrayList<Obreiro>();
	private String criterio; 
	private Obreiro obreiro;
	private ArrayList<PagamentoAnuidadeObreiro> listaPagamentoAnuidades;
	private ArrayList<ObreiroLoja> lojaPerfeicao;
	private ArrayList<ObreiroLoja> cavaleirosKadosh;
	private ArrayList<ObreiroLoja> rosaCruz;
	private ArrayList<ObreiroLoja> consistorioRealSegredo;
	private ArrayList<LogAltercaoObreiro> logAlteracaoObreiro;
	
	public List<Obreiro> getObreiros() {
		iDAOObreiro dao = DAOFactory.getDAOObreiro();
		lista = dao.buscaObreiros();
		return lista;
	}
	
		
	public ArrayList<LogAltercaoObreiro> getLogAlteracaoObreiro() {
		iDAOObreiro dao = DAOFactory.getDAOObreiro();
		this.logAlteracaoObreiro = dao.buscaLogAlteracoes();
		return this.logAlteracaoObreiro;
	}


	public List<Obreiro> getObreirosAniversariantesMes() {
		iDAOObreiro dao = DAOFactory.getDAOObreiro();
		lista = dao.buscaObreirosAniversariantesdoMes();
		return lista;
	}
	
	public List<Obreiro> getObreirosAniversariantesDia() {
		iDAOObreiro dao = DAOFactory.getDAOObreiro();
		lista = dao.buscaObreirosAniversariantesdoDia();
		return lista;
	}
	
	public List<Obreiro> getObreirosFiltro() {
		iDAOObreiro dao = DAOFactory.getDAOObreiro();
		lista = dao.buscaObreiroBasico(this.criterio);
		return lista;
	}
	
	public void imprimirAniversariantesdoMes() {
		String fileName = "aniversariantes.pdf";
		new ObreiroFacade().impremeAniversariantes(fileName);
		System.out.println("printed file: " + fileName);
		File file = new File(fileName);
		// present file to user
		try {
			// Get the FacesContext
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	         
	        // Get HTTP response
	        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	         
	        // Set response headers
	        response.reset();   // Reset the response in the first place
	        response.setHeader("Content-Type", "application/pdf");  // Set only the content type
	         
	        // Open response output stream
	        OutputStream responseOutputStream = response.getOutputStream();
	         
	        // Read PDF contents
	        
	        InputStream pdfInputStream = new FileInputStream(file);
	         
	        // Read PDF contents and write them to the output
	        byte[] bytesBuffer = new byte[2048];
	        int bytesRead;
	        while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
	            responseOutputStream.write(bytesBuffer, 0, bytesRead);
	        }
	         
	        // Make sure that everything is out
	        responseOutputStream.flush();
	          
	        // Close both streams
	        pdfInputStream.close();
	        responseOutputStream.close();
	         
	        // JSF doc:
	        // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated
	        // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
	        // as soon as the current phase is completed.
	        facesContext.responseComplete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	

	public void imprimirObreiro(Obreiro obreiro) {
		String fileName = obreiro.getIme() + ".pdf";
		new ObreiroFacade().impremeObreiro(obreiro, fileName);
		System.out.println("printed file: " + fileName);
		File file = new File(fileName);
		// present file to user
		try {
			// Get the FacesContext
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	         
	        // Get HTTP response
	        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	         
	        // Set response headers
	        response.reset();   // Reset the response in the first place
	        response.setHeader("Content-Type", "application/pdf");  // Set only the content type
	         
	        // Open response output stream
	        OutputStream responseOutputStream = response.getOutputStream();
	         
	        // Read PDF contents
	        
	        InputStream pdfInputStream = new FileInputStream(file);
	         
	        // Read PDF contents and write them to the output
	        byte[] bytesBuffer = new byte[2048];
	        int bytesRead;
	        while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
	            responseOutputStream.write(bytesBuffer, 0, bytesRead);
	        }
	         
	        // Make sure that everything is out
	        responseOutputStream.flush();
	          
	        // Close both streams
	        pdfInputStream.close();
	        responseOutputStream.close();
	         
	        // JSF doc:
	        // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated
	        // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
	        // as soon as the current phase is completed.
	        facesContext.responseComplete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String editar(Obreiro obj){
		this.obreiro=obj;
		carregaRelacionamentos();
        String paginaRetorno ="";
		paginaRetorno="obreiro_form.xhtml";
		return paginaRetorno;
	}
	
	public void carregaRelacionamentos(){
		this.lojaPerfeicao= new ArrayList<ObreiroLoja>();
		this.rosaCruz= new ArrayList<ObreiroLoja>();
		this.cavaleirosKadosh= new ArrayList<ObreiroLoja>();
		this.consistorioRealSegredo= new ArrayList<ObreiroLoja>();
		
		ArrayList<ArrayList<ArrayList>> relacionamentoLojas = DAOFactory.getDAOObreiro().getRelacionamentoLojas(this.obreiro);
		carregaTabelaObraObreiro(this.lojaPerfeicao, relacionamentoLojas.get(0));
        carregaTabelaObraObreiro(this.rosaCruz, relacionamentoLojas.get(1));
        carregaTabelaObraObreiro(this.cavaleirosKadosh, relacionamentoLojas.get(2));
        carregaTabelaObraObreiro(this.consistorioRealSegredo, relacionamentoLojas.get(3));
        this.listaPagamentoAnuidades=  DAOFactory.getDAOAnuidadeObreiro().buscaPagamentoPorObreiro(this.obreiro);
	}
	
	public String novo(){
		this.obreiro=new Obreiro();
		this.lojaPerfeicao= new ArrayList<ObreiroLoja>();
		this.rosaCruz= new ArrayList<ObreiroLoja>();
		this.cavaleirosKadosh= new ArrayList<ObreiroLoja>();
		this.consistorioRealSegredo= new ArrayList<ObreiroLoja>();
        String paginaRetorno ="";
		paginaRetorno="obreiro_form.xhtml";
		return paginaRetorno;
	}
	
	
	
	 private void carregaTabelaObraObreiro(ArrayList<ObreiroLoja> listaOL, ArrayList<ArrayList> dados) {
		    
		 	//listaOL= new ArrayList<ObreiroLoja>();
	        for (int i = 0; i < dados.size(); i++) {
	        	ObreiroLoja obj= new ObreiroLoja();
	            Loja loja = (Loja) dados.get(i).get(0);
	            Grau grau = (Grau) dados.get(i).get(1);
	            Date data=null;
	            if(dados.get(i).get(2)!=null){
	            	data= (Date) dados.get(i).get(2);
	            }
	            Cargo cargo = (Cargo) dados.get(i).get(3);
	            obj.setLoja(loja);
	            obj.setGrau(grau);
	            obj.setData(data);
	            obj.setCargo(cargo);
	            listaOL.add(obj);
	        }
	        
	    }
	
	
	public String sair(){
		this.obreiro=null;
		String paginaRetorno="obreiros.xhtml";
		return paginaRetorno;
	}
	
	public List<Obreiro> getLista() {
		return lista;
	}

	public void setLista(List<Obreiro> lista) {
		this.lista = lista;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}
	
	public Obreiro getObreiro() {
		return obreiro;
	}

	public void setObreiro(Obreiro obreiro) {
		this.obreiro = obreiro;
	}
	
	public String atualizar(){
		if(validaFormulario()){
			iDAOObreiro dao= DAOFactory.getDAOObreiro();
			int operacao= obreiro.getNumero() >= 0 ? 1 : 2;//1 - update, 2 - insert
			boolean result=dao.armazenaObreiro(this.obreiro);
			HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	       
			if (session!=null){
		        LoginAdmin internal = (LoginAdmin) session.getAttribute("loginAdmin");
		        UsuarioSistema user=internal.getUsuario();
		        dao.logOperacao(user.getId() , operacao, this.obreiro.getNumero());
	        }
			
			String mensagem="";
			String titulo="";
			if(result){
				titulo="Confirmação:";
				mensagem="Dados do obreiro atualizados com sucesso!";
			}else{
				titulo="Atenção!";
				mensagem="Ocorreu um problema interno no sistema e seus dados não poderam ser atualizados.";
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(titulo,mensagem ));
			}
		return "obreiros.xhtml";		
	}

	public ArrayList<PagamentoAnuidadeObreiro> getListaPagamentoAnuidades() {
		return listaPagamentoAnuidades;
	}

	public void setListaPagamentoAnuidades(ArrayList<PagamentoAnuidadeObreiro> listaPagamentoAnuidades) {
		this.listaPagamentoAnuidades = listaPagamentoAnuidades;
	}

	public ArrayList<ObreiroLoja> getLojaPerfeicao() {
		return lojaPerfeicao;
	}

	public void setLojaPerfeicao(ArrayList<ObreiroLoja> lojaPerfeicao) {
		this.lojaPerfeicao = lojaPerfeicao;
	}

	public ArrayList<ObreiroLoja> getCavaleirosKadosh() {
		return cavaleirosKadosh;
	}

	public void setCavaleirosKadosh(ArrayList<ObreiroLoja> cavaleirosKadosh) {
		this.cavaleirosKadosh = cavaleirosKadosh;
	}

	public ArrayList<ObreiroLoja> getRosaCruz() {
		return rosaCruz;
	}

	public void setRosaCruz(ArrayList<ObreiroLoja> rosaCruz) {
		this.rosaCruz = rosaCruz;
	}

	public ArrayList<ObreiroLoja> getConsistorioRealSegredo() {
		return consistorioRealSegredo;
	}

	public void setConsistorioRealSegredo(ArrayList<ObreiroLoja> consistorioRealSegredo) {
		this.consistorioRealSegredo = consistorioRealSegredo;
	}
	
	
	   private boolean validaFormulario() {
	        boolean isValid = true;
	        ArrayList<String> mensagens=new ArrayList<String>();
	        if (this.obreiro.getNome().trim().equals("")) {
	        	mensagens.add("O campo nome é de preenchimento obrigatório.");
	            isValid = false;
	        }else if (!validaIME()) {
	        	mensagens.add("O valor IME informado deve ser um valor numérico e único na base de dados.");		           
	            isValid = false;
	        } else if (!validaNome()) {
	        	mensagens.add("O nome informado já existe na base dados.");
	            isValid = false;
	        }
	        String mensagem="";
	        for(String m : mensagens){
	        	mensagem+=m+"\n\n";
	        }
	        if(mensagens.size()>0){
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Atenção",mensagem ));
	        }
	        return isValid;
	    }

	    private boolean validaIME() {
	        boolean ok = true;
	        String ime = String.valueOf(this.obreiro.getIme());
	        if (!ime.equals("")) {
	            try {
	                Integer.parseInt(ime);
	                Obreiro obj = DAOFactory.getDAOObreiro().buscaObreiroPorIME(Integer.parseInt(ime));
	                if (obj != null && obj.getNumero() != this.obreiro.getNumero()) {
	                    ok = false;
	                }
	            }catch(Exception e){
	            	System.err.println(e.getMessage());
	            }
	        }
	        return ok;
	    }

	    	    
	    private boolean validaNome() {
	        boolean ok = true;
	        String nome = this.obreiro.getNome();
	        if (!nome.equals("")) {
	            Obreiro obj = DAOFactory.getDAOObreiro().buscaObreiroPorNome(nome);
	            if (obj != null && obj.getNumero() != this.obreiro.getNumero()) {
	                ok = false;
	            }
	        }
	        return ok;
	    }

	
	
}