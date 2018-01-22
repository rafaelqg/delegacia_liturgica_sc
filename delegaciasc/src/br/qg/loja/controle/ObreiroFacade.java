/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.controle;

import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOObreiro;
import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import visao.IOManagement.DateManager;
import visao.IOManagement.NumericManager;
import visao.printer.pdf.PDFGenerator;

/**
 *
 * @author Rafael
 */
public class ObreiroFacade {

    
    public boolean salvarImagemLogo(File imgFile){
        String description="logo_impressao";
        return DAOFactory.getDAOParametro().registraParametro(description, imgFile);
    }
    
     public File getImagemLogo(){
        return DAOFactory.getDAOParametro().buscaParametro("logo_impressao");
    }
  
     public void impremeObreiro(Obreiro obj, String fileName) {
         PDFGenerator printer = new PDFGenerator();
         String targetFileNameFullPath = fileName;
         DateManager dm = new DateManager();
         NumericManager nm = new NumericManager();
         String HTML = "";
         String CSS = "<style type='text/css'>td{font-family:calibri; font-size:12px;}</style>";
         HTML += CSS;
         HTML += "";
         HTML += "<table style=\"width:100%;\" border=\"0\">";
         HTML += "<tr style=\"background-color:#A8A8A8 ;\"><td colspan=\"4\" style=\"text-align:center\"><b>Dados Cadastrais</b></td></tr>";
         HTML += "<tr><td>Nome completo:</td><td>" + obj.getNome() + "</td><td>CPF:</td><td>" + obj.getCpf() + "</td></tr>";
         HTML += "<tr><td>Nome do pai:</td><td>" + obj.getNomePai() + "</td><td>Nome da m&atilde;e:</td><td>" + obj.getNomeMae() + "</td></tr>";
         HTML += "<tr><td>Data de nascimento:</td><td>" + dm.setDateField(obj.getDtNascimento()) + "</td><td>Local de nascimento:</td><td>" + obj.getLocalNascimento() + "</td></tr>";
         HTML += "<tr><td>RG:</td><td>" + obj.getRg() + "</td><td>E-mail:</td><td>" + obj.getEmail() + "</td></tr>";
         HTML += "<tr><td>Endere&ccedil;o residencial:</td><td colspan=\"3\">" + obj.getEnderecoResidencial() + "</td></tr>";
         HTML += "<tr><td>Endere&ccedil;o comercial:</td><td colspan=\"3\">" + obj.getEnderecoComercial() + "</td></tr>";
         HTML += "<tr><td>Profiss&atilde;o:</td><td>" + obj.getProfissao() + "</td><td>Cargos:</td><td>" + obj.getCargo() + "</td></tr>";
         HTML += "<tr><td>Endere&ccedil;o comercial:</td><td colspan=\"3\">" + obj.getEnderecoComercial() + "</td></tr>";
         HTML += "<tr><td>Rendimento (R$):</td><td colspan=\"3\">" + nm.getCurrencyFormat(obj.getRendimento()) + "</td></tr>";
         HTML += "<tr><td>Outras Informa&ccedil;&otilde;es:</td><td colspan=\"3\">" + obj.getOutrasInformacoes() + "</td></tr>";
         HTML += "<tr><td>Loja simb&oacute;lica atual:</td><td colspan=\"3\">" + obj.getLojaSimbolicaAtual() + "</td></tr>";
         HTML += "<tr><td>Nome da esposa:</td><td>" + obj.getNomeEsposa() + "</td><td>Data de nascimento esposa:</td><td>" + (obj.getDtNascimentoEsposa() == null ? "" : dm.setDateField(obj.getDtNascimentoEsposa())) + "</td></tr>";
         HTML += "<tr><td>CIM:</td><td >" + obj.getCim() + "</td><td>IME:</td><td>" + obj.getIme() + "</td></tr>";
         HTML += "</table>";
         HTML += "<table border=\"0\" style=\"width:100%\">";
         HTML += "<tr style=\"background-color:#A8A8A8 ;\"><td colspan=\"4\" style=\"text-align:center\"><b>Dados Ma&ccedil;&ocirc;nicos</b></td></tr>";
         HTML += "<tr><td>Loja simb&oacute;lica que foi /iniciado-elevado-exaltado</td><td>Data</td><td colspan=\"2\">Grau</td></tr>";
         HTML += "<tr><td>" + obj.getLojaIniciacao() + "</td><td>" + (obj.getDtIniciacao() != null ? dm.setDateField(obj.getDtIniciacao()) : "") + "</td><td colspan=\"2\">" + 1 + "</td></tr>";
         HTML += "<tr><td>" + obj.getLojaElevacao() + "</td><td>" + (obj.getDtElevacao() != null ? dm.setDateField(obj.getDtElevacao()) : "") + "</td><td colspan=\"2\">" + 2 + "</td></tr>";
         HTML += "<tr><td>" + obj.getLojaExaltacao() + "</td><td>" + (obj.getDtExaltacao() != null ? dm.setDateField(obj.getDtExaltacao()) : "") + "</td><td colspan=\"2\">" + 3 + "</td></tr>";
         HTML += "<tr><td colspan=\"4\">&Eacute; mestre instalado: (" + (!obj.isMestreInstalado() ? "X" : " ") + ") N&atilde;o  (" + (obj.isMestreInstalado() ? "X" : " ") + ") Sim</td></tr>";
         ArrayList<ArrayList<ArrayList>> relacionamentoLojas = DAOFactory.getDAOObreiro().getRelacionamentoLojas(obj);
         HTML += "<tr style=\"background-color:#D8D8D8 ;\"><td>Lojas de perfei&ccedil;&atilde;o</td><td>Data</td><td>Grau</td><td>Cargo (s) exerc&iacute;dos</td></tr>";
         HTML += buildHTMLForLojasRelationships(relacionamentoLojas.get(0));
         HTML += "<tr style=\"background-color:#D8D8D8 ;\"><td>Cap&iacute;tulo Roza Cruz</td><td>Data</td><td>Grau</td><td>Cargo (s) exerc&iacute;dos</td></tr>";
         HTML += buildHTMLForLojasRelationships(relacionamentoLojas.get(1));
         HTML += "<tr style=\"background-color:#D8D8D8 ;\"><td>Conselho dos Cavaleiros Kadosh</td><td>Data</td><td>Grau</td><td>Cargo (s) exerc&iacute;dos</td></tr>";
         HTML += buildHTMLForLojasRelationships(relacionamentoLojas.get(2));
         HTML += "<tr style=\"background-color:#D8D8D8 ;\"><td>Consist&oacute;rio de Pr&iacute;ncipeis do Real Segredo</td><td>Data</td><td>Grau</td><td>Cargo (s) exerc&iacute;dos</td></tr>";
         HTML += buildHTMLForLojasRelationships(relacionamentoLojas.get(3));
         HTML += "</table>";
        
         File f=new br.qg.loja.controle.ObreiroFacade().getImagemLogo();
         //java.net.URL imgURL = getClass().getResource("/resources/supre_conselho_min.png");

         String headerCode=this.getHeaderCode();
     
         printer.createPDFBasedonHTMLCode(headerCode + HTML, targetFileNameFullPath);
         f.delete();
         //printer.addHeaderAndFooter(headerCode, "", targetFileNameFullPath,20);
     }
     
     
    public void impremeAniversariantes(String fileName) {
        PDFGenerator printer = new PDFGenerator();
        String targetFileNameFullPath = fileName;
        DateManager dm = new DateManager();
        
        iDAOObreiro dao = DAOFactory.getDAOObreiro();
		List<Obreiro> lista = dao.buscaObreirosAniversariantesdoMes();
        
        String HTML = "";
        String CSS = "<style type='text/css'>td{font-family:calibri; font-size:12px;}</style>";
        HTML += CSS;
        HTML += "<br /><h1 style='font-size:14px;' align=\"center\">Obreiro(s) Aniversariante(s) do Mes</h1><br />";
        HTML += "<table style=\"width:100%;\" border=\"0\" align=\"center\">";
        HTML += "<tr><th>IME</th><th>Nome</th><th>Data de nascimento</th><th>Loja simbolica atual</th><th>Loja filosofica atual</th><th>e-mail</th></tr>";
       
        for(Obreiro obj:lista){
        	HTML += "<tr style='vertical-align: top'><td>"+obj.getIme()+"</td><td>"+obj.getNome()+"</td><td>"+dm.setDateField(obj.getDtNascimento())+"</td><td>"+obj.getLojaSimbolicaAtual()+"</td><td>"+obj.getLojaAtual().getNome()+"</td><td>"+obj.getEmail()+"</td></tr>";
            
        }
        
        HTML += "</table>";
       
        File f=new br.qg.loja.controle.ObreiroFacade().getImagemLogo();
        //java.net.URL imgURL = getClass().getResource("/resources/supre_conselho_min.png");

        String headerCode=this.getHeaderCode();
    
        printer.createPDFBasedonHTMLCode(headerCode + HTML, targetFileNameFullPath);
        System.out.println("Full path:" +targetFileNameFullPath);
        f.delete();
        //printer.addHeaderAndFooter(headerCode, "", targetFileNameFullPath,20);
    }

    
    private String getHeaderCode(){
         File f=new br.qg.loja.controle.ObreiroFacade().getImagemLogo();
        //java.net.URL imgURL = getClass().getResource("/resources/supre_conselho_min.png");
        String imgSrc=f.getName();//imgURL.getPath();
        String headerCode="<table align='center'><tr>";
        headerCode+="<td valign='top'><img src='"+imgSrc+"' alt='logo' /></td><td><p align='center'><b>SUPREMO CONSELHO DO BRASIL DO GRAU 33 <br />";
        headerCode+="PARA O  &nbsp;&nbsp;  R.: .E.: .A.: .A.:</b><br />";
        headerCode+="<span style='size=8px;'>Delegacia de Santa Catarina<br />";
        headerCode+="Av. Prefeito Osmar Cunha, 183, Ed. Ceisa Center - Bloco B - Sala 111 <br />";
        headerCode+="Caixa Postal: 13064 - CEP 88010-320 - Florian&oacute;polis - SC <br />";
        headerCode+="Fone (48) 3224 3350 - supreconsdelsc@ccmail.com.br</span><br /></p>";
        headerCode+="</td></tr></table>";
        return headerCode;
    }
    
    public void impremeObreirosPorLoja(Loja loja, String fileName) {
        PDFGenerator printer = new PDFGenerator();
        String targetFileNameFullPath = fileName;
        DateManager dm = new DateManager();
        NumericManager nm = new NumericManager();

        String HTML = "";
        String CSS = "<style>td{font-family:calibri; font-size:12px;}</style>";
        HTML += CSS;
        HTML += "<br /><b>Loja:</b>" + loja.getNome() + "<br /><br />";
        HTML += "<table style=\"width:100%;\" border=\"1\">";
        
        HTML += "<tr><td>IME</td><td>Obreiro</td><td>Grau</td><td>Data Inicia&ccedil;&atilde;o</td></tr>";

        ArrayList<ArrayList> lista = DAOFactory.getDAOObreiro().buscaObreirosPorLoja(loja);
        for (ArrayList obj : lista) {
            if (obj.get(0) != null) {
                int ime = Integer.valueOf(obj.get(0).toString());
                String nome=obj.get(1).toString();
                Grau g = (Grau) obj.get(2);
                Date dt = (Date) obj.get(3);
                HTML += "<tr><td>" + ime + "</td><td>" + nome + "</td><td>" + g.getNumero() + "</td><td>" + dm.setDateField(dt) + "</td></tr>";
            }
        }


        HTML += "</table>";
        HTML += "<br /><br /><b>Total:</b>" + lista.size() + "<br /><br />";
        String headerCode=this.getHeaderCode();
        printer.createPDFBasedonHTMLCode(headerCode+HTML, targetFileNameFullPath);
    }

    private String buildHTMLForLojasRelationships(ArrayList<ArrayList> dados) {
        String HTML = "";
        for (int i = 0; i < dados.size(); i++) {
            Loja loja = (Loja) dados.get(i).get(0);
            Grau grau = (Grau) dados.get(i).get(1);
            String data = null;
            DateManager dm = new DateManager();
            if (dados.get(i).get(2) != null) {
                data = dm.setDateField((Date) dados.get(i).get(2));
            }
            Cargo cargo = (Cargo) dados.get(i).get(3);
            HTML += "<tr>";
            HTML += "<td>" + loja + "</td>";
            HTML += "<td>" + (data == null ? "" : data) + "</td>";
            HTML += "<td>" + grau + "</td>";
            HTML += "<td>" + (cargo == null ? "" : cargo) + "</td>";
            HTML += "</tr>";
        }
        return HTML;
    }
}
