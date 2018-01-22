/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.Obreiro;

/**
 *
 * @author Rafael
 */
public class JFramePrincipal extends javax.swing.JFrame {
    private final String SISTEM_TITLE="Supremo Conselho do Brasil";
    private static JPanelObreiro mainForm;  
    private static JFramePrincipal mainScreen;
    private visao.JPanelAnuidadeDoCorpo jPanelAnuidadeDoCorpo1;
    private visao.JPanelAnuidadeDoIrmao jPanelAnuidadeDoIrmao2;
    private visao.JPanelCargosAdministrativos jPanelCargosAdministrativos1;
    private visao.JPanelComendas jPanelComendas1;
    private visao.JPanelMudancaGrau jPanelMudancaGrau1;
    private visao.JPanelConfiguracoes jPanelConfiguracoes;
    private visao.JPanelMudancaOutrosExpedientes jPanelMudancaOutrosExpedientes1;
    private visao.JPanelObreiro jPanelObreiro1;
    private visao.JPanelRelatorioBasico jPanelRelatorioBasico1;
    private visao.JPanelRelatorioObreirosLoja jPanelRelatorioObreirosLoja;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    
	
	
    public static JPanelObreiro getMainForm() {
        return mainForm;
    }

    public static void setMainForm(JPanelObreiro mainForm) {
        JFramePrincipal.mainForm = mainForm;
    }
    
    public static void showForm(int ime){
        Obreiro obj=DAOFactory.getDAOObreiro().buscaObreiroPorIME(ime);
        JFramePrincipal.getMainForm().preencherFormulario(obj);
        mainScreen.jTabbedPane1.setSelectedIndex(0);
    }
    

    /**
     * Creates new form JFramePrincipal
     */
    public JFramePrincipal() {  
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JFramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(JFramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JFramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(JFramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
       insertComponents();
        //sei icon
        //Toolkit.getDefaultToolkit().getImage("resources/supre_conselho.png");
        
        java.net.URL imgURL = getClass().getResource("/resources/supre_conselho_min.png");
        ImageIcon icon =new ImageIcon(imgURL);
        this.setIconImage(icon.getImage()); 
        
        //this.setExtendedState(MAXIMIZED_BOTH);
        setMainForm(this.jPanelObreiro1);
        mainScreen=this;
        //centerWindow();
        this.setExtendedState(MAXIMIZED_BOTH);
        //this.setResizable(false);   
    }
	
	private void insertComponents(){
	 jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelObreiro1 = new visao.JPanelObreiro();
        jPanelRelatorioBasico1 = new visao.JPanelRelatorioBasico();
        jPanelRelatorioObreirosLoja=new visao.JPanelRelatorioObreirosLoja();
        jPanelAnuidadeDoIrmao2 = new visao.JPanelAnuidadeDoIrmao();
        jPanelAnuidadeDoCorpo1 = new visao.JPanelAnuidadeDoCorpo();
        jPanelConfiguracoes = new JPanelConfiguracoes();
        //jPanelComendas1 = new visao.JPanelComendas();
        //jPanelMudancaGrau1 = new visao.JPanelMudancaGrau();
        //jPanelCargosAdministrativos1 = new visao.JPanelCargosAdministrativos();
        //jPanelMudancaOutrosExpedientes1 = new visao.JPanelMudancaOutrosExpedientes();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(SISTEM_TITLE);
        setPreferredSize(new java.awt.Dimension(1136, 800));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jScrollPane1.setViewportView(jPanelObreiro1);

        jTabbedPane1.addTab("Ficha de Obreiro", jScrollPane1);
        jTabbedPane1.addTab("Consulta", jPanelRelatorioBasico1);
        jTabbedPane1.addTab("Consulta por loja", jPanelRelatorioObreirosLoja);
        jTabbedPane1.addTab("Anuidade do Irmão", jPanelAnuidadeDoIrmao2);
        jTabbedPane1.addTab("Anuidade do Corpo", jPanelAnuidadeDoCorpo1);
       // jTabbedPane1.addTab("Comendas", jPanelComendas1);
       // jTabbedPane1.addTab("Mudança de Grau", jPanelMudancaGrau1);
       // jTabbedPane1.addTab("Cargos Administrativos", jPanelCargosAdministrativos1);
       // jTabbedPane1.addTab("Outros Expedientes", jPanelMudancaOutrosExpedientes1);
        jTabbedPane1.addTab("Configurações", jPanelConfiguracoes);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1116, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
	
	}
	

private void centerWindow(){
    Dimension dim= Toolkit.getDefaultToolkit().getScreenSize();
    int wint= this.getSize().width;
    int hint= this.getSize().height;
    int x= (dim.width-wint)/2;
    int y = (dim.height-hint)/2;
    this.setLocation(x, y);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

       
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        
    }//GEN-LAST:event_formKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFramePrincipal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
   
    // End of variables declaration//GEN-END:variables
}

