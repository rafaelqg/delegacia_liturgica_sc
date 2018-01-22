package br.qg.loja.controle;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOLoja;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.UsuarioSistema;

@ManagedBean
@SessionScoped
public class LojaMB {
	private int selectLojaId=0;
	
	private Loja loja= new Loja();
	private List<SelectItem> lista= new ArrayList<SelectItem>();//Utilized for combobox options
	private List<ObreiroLojaVO> listaTabela = new ArrayList<ObreiroLojaVO>();
		
	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}
	
public void registrar(){
		DAOFactory.getDaoLoja().registraLoja(this.loja);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Atenção", "Dados da loja atualizados!"));
}

public String editar(Loja obj){
	this.loja=obj;
    String paginaRetorno = "loja_form.xhtml";
	return paginaRetorno;
}
		

	public List<SelectItem>  getLojas() {
		iDAOLoja dao = DAOFactory.getDaoLoja();
		List<Loja> lojas = dao.buscaLojas();
		List<SelectItem> items=new ArrayList<SelectItem>();
		for(Loja l: lojas){
			items.add(new SelectItem(l.getNumero(),l.getNome()));
		}
		return items;
	}
	
	public List<Loja>  listaLojas() {
		iDAOLoja dao = DAOFactory.getDaoLoja();
		return dao.buscaLojas();

	}
	
	public void buscaObreirosPorLoja(){
		int id= this.selectLojaId;
		Loja loja= DAOFactory.getDaoLoja().buscaLojaPorChave(id);
		 ArrayList<ArrayList> listaAL = DAOFactory.getDAOObreiro().buscaObreirosPorLoja(loja);
		 listaTabela = new ArrayList<ObreiroLojaVO>();   
		 for (ArrayList obj : listaAL) {
	            if (obj.get(0) != null) {
	                int ime = Integer.valueOf(obj.get(0).toString());
	                String nome=obj.get(1).toString();
	                Grau g = (Grau) obj.get(2);
	                Date dt = (Date) obj.get(3);
	                ObreiroLojaVO ol= new ObreiroLojaVO();
	                ol.setIme(ime);
	                ol.setGrau(g.getNumero());
	                ol.setDataIniciacao(dt);
	                ol.setNome(nome);
	                listaTabela.add(ol);
	            }
	        }
	}

	public void imprimirObreirosPorLoja() {
		int id= this.selectLojaId;
		Loja loja= DAOFactory.getDaoLoja().buscaLojaPorChave(id);
		String fileName = loja.getNome() + ".pdf";
		new ObreiroFacade().impremeObreirosPorLoja(loja, fileName);
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


	

	public List<SelectItem> getLista() {
		return lista;
	}

	public void setLista(List<SelectItem> lista) {
		this.lista = lista;
	}
	
	public int getSelectLojaId() {
		return selectLojaId;
	}

	public void setSelectLojaId(int selectLojaId) {
		this.selectLojaId = selectLojaId;
	}

	public List<ObreiroLojaVO> getListaTabela() {
		return listaTabela;
	}

	public void setListaTabela(List<ObreiroLojaVO> listaTabela) {
		this.listaTabela = listaTabela;
	}

	


}
