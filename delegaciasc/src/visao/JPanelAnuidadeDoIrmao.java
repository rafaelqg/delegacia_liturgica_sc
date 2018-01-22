/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;
import visao.IOManagement.DateManager;
import visao.IOManagement.jtable.JTableFormatter;
import visao.IOManagement.NumericManager;
import visao.IOManagement.jtable.ZebraRenderer;

/**
 *
 * @author RafaelQG
 */
public class JPanelAnuidadeDoIrmao extends javax.swing.JPanel {

    private final String SUCESS_WRITE_MSG = "Pagamentos registrados com sucesso!";
    private final String VALIDATION_MSG = "Existem campos preenchidos incorretamente!";

    /**
     * Creates new form JPanelAnuidadeDoIrmao
     */
    public JPanelAnuidadeDoIrmao() {
        initComponents();
        initTables();
         this.jTable1.setAutoCreateRowSorter(true);
         this.jTable1.setRowHeight(21);
        JComboBox field = this.jComboBoxLoja;
        ArrayList<Loja> lojas = DAOFactory.getDaoLoja().buscaLojas();
        for (Loja obj : lojas) {
            field.addItem(obj);
        }

        /*
         field = this.jComboBoxIrmao;
         ArrayList<Obreiro> obreiros = DAOFactory.getDAOObreiro().buscaObreiros();
         for (Obreiro obj : obreiros) {
         field.addItem(obj);
         }
         */
    }

    private void initTables() {

        JTableFormatter tableFormatter = new JTableFormatter();
        tableFormatter.configCellData(this.jTable1, "Dt-Compr. Bancário");
        tableFormatter.configCellData(this.jTable1, "Dt-Prancha");
        tableFormatter.configCellData(this.jTable1, "Dt. Emissão Diligência");
        tableFormatter.configCellData(this.jTable1, "Dt. Retorno Diligência");
        tableFormatter.configCellData(this.jTable1, "Dt. Romaneio");
        tableFormatter.configCellAno(this.jTable1, "Ano");

        //set zebra rows
        this.jTable1.setDefaultRenderer(Object.class, new ZebraRenderer());//set zebra rows
       

        Color shadeOfBlue = new Color(164, 211, 238);
        Color shadeOfGreen = new Color(112, 219, 147);
        Color shadeOfYellow = new Color(255, 228, 196);
        Color shadeOfSnow = new Color(224, 238, 224);
        Color shadeOfRed = new Color(255, 192, 203);

        tableFormatter.setHeaderBackground(this.jTable1, "Nome Irmão", shadeOfYellow);
        tableFormatter.setHeaderBackground(this.jTable1, "Ano", shadeOfYellow);
        tableFormatter.setHeaderBackground(this.jTable1, "Valor (R$)", shadeOfSnow);
        tableFormatter.setHeaderBackground(this.jTable1, "Dt-Compr. Bancário", shadeOfSnow);
        tableFormatter.setHeaderBackground(this.jTable1, "Nr. Prancha", shadeOfRed);
        tableFormatter.setHeaderBackground(this.jTable1, "Dt-Prancha", shadeOfRed);

        tableFormatter.setHeaderBackground(this.jTable1, "Nr. Romaneio", shadeOfBlue);
        tableFormatter.setHeaderBackground(this.jTable1, "Dt. Romaneio", shadeOfBlue);
        tableFormatter.setHeaderBackground(this.jTable1, "Dt. Emissão Diligência", shadeOfGreen);
        tableFormatter.setHeaderBackground(this.jTable1, "Dt. Retorno Diligência", shadeOfGreen);
        tableFormatter.setHeaderBackground(this.jTable1, "Motivo Diligência", shadeOfGreen);
    }

    private void limpaTabela() {
        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        /*
        for (int i = 0; i < 100; i++) {
            model.addRow(new Vector());
        }
        */
        this.jTable1.setDefaultRenderer(Object.class, new ZebraRenderer());//set zebra rows 
    }

    private void buscaPagamentoAnuidadeLoja() {
        if (this.getCursor().getType() != Cursor.WAIT_CURSOR) { // avoid more than one click while processing
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                Loja lj = (Loja) this.jComboBoxLoja.getSelectedItem();
                limpaTabela();
                DateManager dm = new DateManager();
                NumericManager nm = new NumericManager();
                DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
                ArrayList<PagamentoAnuidadeObreiro> pagamentos = DAOFactory.getDAOAnuidadeObreiro().buscaPagamentoAnuidadesPorLoja(lj);
                int i = 0;
                double total = 0;
                if(pagamentos.isEmpty()){
                    this.jLabelSearchResult.setText(JTableFormatter.NO_RESULT_FOUND_LABEL);
                }else{
                    this.jLabelSearchResult.setText("Foram encontrados "+ pagamentos.size() + " registros");
                }
                for (PagamentoAnuidadeObreiro pagamento : pagamentos) {
                    int j = 0;
                    model.addRow(new Vector());//adiciona uma por registro
                    model.setValueAt(pagamento.getObreiro(), i, j++);
                    int ano = pagamento.getAno();
                    if (ano != 0) {
                        model.setValueAt(ano, i, j++);
                    } else {
                        model.setValueAt("", i, j++);
                    }
                    model.setValueAt(pagamento.getValor(), i, j++);
                    total += pagamento.getValor();
                    Date dtComprovanteBancario = pagamento.getDtComprovanteBancario();
                    model.setValueAt(dm.setDateField(dtComprovanteBancario), i, j++);
                    int numeroPrancha = pagamento.getNumeroPrancha();
                    if (numeroPrancha > 0) {
                        model.setValueAt(numeroPrancha, i, j++);
                    } else {
                        model.setValueAt("", i, j++);
                    }
                    Date dtPrancha = pagamento.getDtPrancha();
                    model.setValueAt(dm.setDateField(dtPrancha), i, j++);
                    String motivo = pagamento.getMotivo();
                    model.setValueAt(motivo, i, j++);
                    Date dtEmissao = pagamento.getDtEmissao();
                    model.setValueAt(dm.setDateField(dtEmissao), i, j++);
                    Date dtRetorno = pagamento.getDtRetorno();
                    model.setValueAt(dm.setDateField(dtRetorno), i, j++);
                    int numeroRomaneio = pagamento.getNumeroRomaneiro();
                    if (numeroRomaneio > 0) {
                        model.setValueAt(numeroRomaneio, i, j++);
                    } else {
                        model.setValueAt("", i, j++);
                    }
                    Date dataRomaneio = pagamento.getDtPrancha();
                    model.setValueAt(dm.setDateField(dataRomaneio), i, j++);
                    i++;
                }
                this.jTable1.setDefaultRenderer(Object.class, new ZebraRenderer());//set zebra rows
                this.jLabelValorTotal.setText(nm.getCurrencyFormat(total));
            } finally {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    private void salvaRegistros() {
        if (this.getCursor().getType() != Cursor.WAIT_CURSOR) { // avoid more than one click while processing
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                if (validaRegistros()) {

                    DateManager dm = new DateManager();
                    NumericManager nm = new NumericManager();
                    Loja lj = (Loja) this.jComboBoxLoja.getSelectedItem();
                    DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
                    double total = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 0) != null) {
                            int j = 0;
                            Obreiro obr = (Obreiro) model.getValueAt(i, j++);
                            Object obj = null;//this variable is used below as a pointer to facilitate fields validation
                           // DAOFactory.getDAOAnuidadeObreiro().excluiPagamentosPorObreiroLoja(obr, lj); //exclui registros atuais
                            int ano = nm.getIntField(model.getValueAt(i, j++));
                            if (ano > 0) {// Just save a record when the year is filled
                                double valor = nm.getDoubleField(model.getValueAt(i, j++));
                                total += valor;
                                Date dtComprovanteBancario = dm.getDateField(model.getValueAt(i, j++).toString());
                                int numeroPrancha = nm.getIntField(model.getValueAt(i, j++).toString());
                                Date dtPrancha = dm.getDateField(model.getValueAt(i, j++));
                                obj = model.getValueAt(i, j++);
                                String motivo = obj == null ? "" : obj.toString();
                                Date dtEmissao = dm.getDateField(model.getValueAt(i, j++));
                                Date dtRetorno = dm.getDateField(model.getValueAt(i, j++));
                                int numeroRomaneio = nm.getIntField(model.getValueAt(i, j++).toString());
                                Date dataRomaneio = dm.getDateField(model.getValueAt(i, j++));
                                PagamentoAnuidadeObreiro pag = new PagamentoAnuidadeObreiro();
                                pag.setObreiro(obr);
                                pag.setLoja(lj);
                                pag.setAno(ano);
                                pag.setDtComprovanteBancario(dtComprovanteBancario);
                                pag.setDtEmissao(dtEmissao);
                                pag.setDtPrancha(dtPrancha);
                                pag.setMotivo(motivo);
                                pag.setNumeroPrancha(numeroPrancha);
                                pag.setDtRetorno(dtRetorno);
                                pag.setNumeroRomaneiro(numeroRomaneio);
                                pag.setDtRomaneio(dataRomaneio);
                                pag.setValor(valor);
                                DAOFactory.getDAOAnuidadeObreiro().armazenaPagamentoAnuidade(pag);
                            } else {
                               // DAOFactory.getDAOAnuidadeObreiro().excluiPagamentosPorObreiroLoja(obr, lj);//remove 
                            }
                        }
                    }
                    this.jLabelValorTotal.setText(nm.getCurrencyFormat(total));
                    JOptionPane.showMessageDialog(this, this.SUCESS_WRITE_MSG, "", JOptionPane.INFORMATION_MESSAGE);

                }
            } finally {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    private boolean validaRegistros() {
        DateManager dm = new DateManager();
        NumericManager nm = new NumericManager();
        boolean isOK = true;
        String message = "";
        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        for (int i = 0; i < model.getRowCount() && isOK; i++) {
            Object obj = model.getValueAt(i, 1);// 1 is year
            if (obj != null && !obj.toString().equals("")) { // Just start to validate a record if the year is filled
                Obreiro o = (Obreiro) model.getValueAt(i, 0);


                if (model.getValueAt(i, 3) == null || !dm.isValidDate(model.getValueAt(i, 3).toString())) {
                    isOK = false;
                    changeBackgroundColor(this.jTable1, i, 3);
                    message = "O campo data do comprovante está preenchido de forma incorreta para o obreiro \"" + o.getNome() + "\" (linha " + (i + 1) + ")";
                }

                try {
                    double valor = nm.getDoubleField(model.getValueAt(i, 2));
                    if (valor <= 0) {
                        message = "O campo valor está preenchido de forma incorreta para o obreiro \"" + o.getNome() + "\" (linha " + (i + 1) + ")";
                        isOK = false;
                        changeBackgroundColor(this.jTable1, i, 2);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    isOK = false;
                    changeBackgroundColor(this.jTable1, i, 2);
                    message = "O campo valor está preenchido de forma incorreta para o obreiro \"" + o.getNome() + "\" (linha " + (i + 1) + ")";
                }

                /*
                 try {
                 int numeroPrancha = nm.getIntField(model.getValueAt(i, 3).toString());
                 } catch (Exception e) {
                 System.err.println(e.getMessage());
                 isOK = false;
                 changeBackgroundColor(this.jTable1, i, 3);
                 message="O campo número prancha está preenchido de forma incorreta para o obreiro \""+o.getNome()+"\" (linha "+ (i+1)+ ")";
                 }
                 */
                /*
                 if (model.getValueAt(i, 4) == null || !dm.isValidDate(model.getValueAt(i, 4).toString())) {
                 isOK = false;
                 changeBackgroundColor(this.jTable1, i, 4);
                 message="O campo data do prancha está preenchido de forma incorreta para o obreiro \""+o.getNome()+"\" (linha "+ (i+1)+ ")";
                 }
                 */

                /*
                 try {
                 String motivo = model.getValueAt(i, 5).toString();
                 } catch (Exception e) {
                 System.err.println(e.getMessage());
                 isOK = false;
                 changeBackgroundColor(this.jTable1, i, 5);
                 message="O campo motivo está preenchido de forma incorreta para o obreiro \""+o.getNome()+"\" (linha "+ (i+1)+ ")";
                 }
                 */

                /*
                 if (model.getValueAt(i, 6) == null || !dm.isValidDate(model.getValueAt(i, 6).toString())) {
                 isOK = false;
                 changeBackgroundColor(this.jTable1, i, 6);
                 message="O campo data de emissão está preenchido de forma incorreta para o obreiro \""+o.getNome()+"\" (linha "+ (i+1)+ ")";
                 }
                 */
                /*
                 if (model.getValueAt(i, 7) == null || !dm.isValidDate(model.getValueAt(i, 7).toString())) {
                 isOK = false;
                 changeBackgroundColor(this.jTable1, i, 7);
                 message="O campo data de retorno está preenchido de forma incorreta para o obreiro \""+o.getNome()+"\" (linha "+ (i+1)+ ")";
                 }
                 */

                try {
                    int numeroRomaneio = nm.getIntField(model.getValueAt(i, 9).toString());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    isOK = false;
                    changeBackgroundColor(this.jTable1, i, 9);
                    message = "O campo número romaneio está preenchido de forma incorreta para o obreiro \"" + o.getNome() + "\" (linha " + (i + 1) + ")";
                }
                /*
                 if (model.getValueAt(i, 9) == null || !dm.isValidDate(model.getValueAt(i, 9).toString())) {
                 isOK = false;
                 changeBackgroundColor(this.jTable1, i, 9);
                 message="O campo data romaneio está preenchido de forma incorreta para o obreiro \""+o.getNome()+"\" (linha "+ (i+1)+ ")";
                 }
                 */
            }
        }
        if (!isOK) {
            JOptionPane.showMessageDialog(this, message, "", JOptionPane.WARNING_MESSAGE);
        }
        return isOK;
    }

    private void changeBackgroundColor(JTable table, int row, int col) {
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        boolean toggle = false;
        boolean extend = false;
        table.changeSelection(row, col, toggle, extend);
        //first atempt sets bg color for all cells, it is not OK
        // table.setSelectionBackground(Color.RED);

        //second atempt getting no result
        //table.getCellEditor(row,col).getTableCellEditorComponent(table,table.getValueAt(row,col),true,row,col).setBackground(Color.red);
        //table.getCellRenderer(row, col).getTableCellRendererComponent(table,table.getValueAt(row,col),true,true,row,col).setBackground(Color.red);
        //3th atempt getting no result
        //Component c = table.getCellRenderer(row, col).getTableCellRendererComponent(table, table.getValueAt(row, col), true, true, row, col);
        //c.setForeground(Color.red);

        //4th atempt getting no result
        //DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getCellRenderer(row, col).getTableCellRendererComponent(table, table.getValueAt(row, col), true, true, row, col).;
        //renderer.setBorder(new LineBorder(Color.red));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxLoja = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabelValorTotal = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabelSearchResult = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nome Irmão", "Ano", "Valor (R$)", "Dt-Compr. Bancário", "Nr. Prancha", "Dt-Prancha", "Motivo Diligência", "Dt. Emissão Diligência", "Dt. Retorno Diligência", "Nr. Romaneio", "Dt. Romaneio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(19);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(230);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(15);
            jTable1.getColumnModel().getColumn(10).setPreferredWidth(35);
        }

        jLabel1.setText("Selecione a loja desejada:");

        jComboBoxLoja.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxLojaItemStateChanged(evt);
            }
        });
        jComboBoxLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLojaActionPerformed(evt);
            }
        });

        jButton1.setText("Salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Valor total (R$):");

        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabelSearchResult.setText("Resultado da busca");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelValorTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBoxLoja, 0, 551, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelSearchResult)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jComboBoxLoja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabelValorTotal))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSearchResult)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        salvaRegistros();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBoxLojaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxLojaItemStateChanged
    }//GEN-LAST:event_jComboBoxLojaItemStateChanged

    private void jComboBoxLojaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLojaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxLojaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        buscaPagamentoAnuidadeLoja();
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBoxLoja;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelSearchResult;
    private javax.swing.JLabel jLabelValorTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
