/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.IOManagement.jtable;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.TipoEspecie;
import visao.JPanelObreiro;

/**
 *
 * @author RafaelQG
 */
public class JTableFormatter {
    public static final String NO_RESULT_FOUND_LABEL="Nenhum resultado encontrado.";
    public void setHeaderBackground(JTable table, String columnTitle, Color background) {
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(background);
        TableColumn column = table.getColumn(columnTitle);
       
        try {
            column.setHeaderRenderer(headerRenderer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void configCellGrau(JTable table, String columnName, ArrayList<Grau> grausCache) {
        TableColumn column = table.getColumn(columnName);
        JComboBox field = new JComboBox();
        for (Grau obj : grausCache) {
            field.addItem(obj);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }

    public void configCellData(JTable table, String columnName) {
        TableColumn column = table.getColumn(columnName);
        try {
            JFormattedTextField field = new JFormattedTextField(new MaskFormatter("##/##/####"));
            field.addFocusListener(new java.awt.event.FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (e.getComponent() != null) {
                        JFormattedTextField field = (JFormattedTextField) e.getComponent();
                        field.setCaretPosition(0);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });


            column.setCellEditor(new DefaultCellEditor(field));
        } catch (ParseException ex) {
            Logger.getLogger(JPanelObreiro.class.getName()).log(Level.SEVERE, null, ex);
        }

        //JTableCustomizer.setEditorForRow(table, column.getModelIndex(), new SimpleDateFormat(DATE_FORMAT));
    }
    
     public void configCellLoja(JTable table, int corpo) {
        TableColumn column = table.getColumnModel().getColumn(0);
        JComboBox field = new JComboBox();
        ArrayList<Loja> lojas = DAOFactory.getDaoLoja().buscaLojasPorCorpo(corpo);
        for (Loja obj : lojas) {
            field.addItem(obj);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }

   public void configCellAno(JTable table, String columnName) {
        TableColumn column = table.getColumn(columnName);
        JComboBox field = new JComboBox();
        field.addItem(null);
        for (int year = new Date().getYear() + 1900; year > 1900; year--) {
            field.addItem(year);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }
   
    public void configTipoEspecie(JTable table, String columnName) {
         TableColumn column = table.getColumn(columnName);
        JComboBox field = new JComboBox();
        ArrayList<TipoEspecie> tipos = DAOFactory.getDAOTipoEspecie().getAllTipoEspecie();
        for (TipoEspecie obj : tipos) {
            field.addItem(obj);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }

   public void configCellCargo(JTable table) {
        TableColumn column = table.getColumnModel().getColumn(3);
        JComboBox field = new JComboBox();
        ArrayList<Cargo> cargos = DAOFactory.getDAOCargo().buscaCargos();
        field.addItem(null);
        for (Cargo obj : cargos) {
            field.addItem(obj);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }
}
