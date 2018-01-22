/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import br.qg.loja.controle.ObreiroFacade;
import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOObreiro;
import br.qg.loja.integracao.mysql.DAOObreiroMySQL;
import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;
import visao.IOManagement.DateManager;
import visao.IOManagement.NumericManager;
import visao.printer.pdf.PDFGenerator;
import visao.printer.pdf.PDFViewer;

/**
 *
 * @author Rafael
 */
public class JPanelObreiro extends javax.swing.JPanel {

    private final String FORM_TITLE = "Ficha de Obreiro";
    private final String SUCESS_WRITE_MSG = "Obreiro registrado com sucesso!";
    private final String HINT_DATE_FIELD = "formato dd/mm/aaaa (exemplo: 05/08/1930)";
    private final String HINT_DOUBLE_FIELD = "formato ##.###,## (exemplo: 11.687,87)";
    private final String EXIT_SYSTEM = "Você deseja encerrar o sistema?";
    private final String DATE_FORMAT = "dd/MM/yyyy";
    private final String DATE_FORMAT_VALIDATION_MSG = "A data informada está incorreta. Observe:";
    private final String NEW_RECORD_CONFIRM_ACTION = "Caso haja informações atualizadas no formulário que ainda não tenham sido salvas, estas serão perdidas. Deseja continuar?";
    private final String IME_EMPTY = "O campo IME deve ser informado.";
    private final String IME_VALID = "O campo IME deve ser informado com um valor numérico.";
    private final String IME_EXISTS = "O valor do IME informado já foi registrado para outro obreiro.";
    private final String NAME_EXISTS = "O nome do obreiro já está registrado no sistema.";
    private final String NOME_EMPTY = "O nome do obreiro dever ser informado.";
    private final String GRAU_EMPTY = "O campo grau não deve ficar em branco.";
    private int editedId = -1;//;-1 means the form will create a new record, anything else, it will update the existing want.

    /**
     * Creates new form JPanelObreiro
     */
    public JPanelObreiro() {   
        initComponents(); 
        initTables();
        //change the panel state to "New Record"
        this.jToggleButtonNewRecord.setSelected(true);
        this.jToggleButtonNewRecord.setEnabled(false);
        this.setBackground(Color.WHITE);
        this.jRadioButtonMestreInstaladoNao.setBackground(Color.WHITE);
        this.jRadioButtonMestreInstaladoSim.setBackground(Color.WHITE);
        //set field masks
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

    private void initTables() {
        configCellGrau(this.jTableLojaSimbolica, "Grau", getGrausRange(1, 3));
        configCellGrau(this.jTableDadosKadosh, "Grau", getGrausRange(19, 30));
        configCellGrau(this.jTableDadosRealSegredo, "Grau", getGrausRange(31, 33));
        configCellGrau(this.jTableLojaPerfeicao, "Grau", getGrausRange(4, 14));
        configCellGrau(this.jTableRozaCruz, "Grau", getGrausRange(15, 18));

        configCellData(this.jTableLojaSimbolica, "Data");
        configCellData(this.jTableLojaPerfeicao, "Data");
        configCellData(this.jTableDadosKadosh, "Data / Iniciação");
        configCellData(this.jTableDadosRealSegredo, "Data / Iniciação");
        configCellData(this.jTableRozaCruz, "Data / Iniciação");

        configCellLoja(this.jTableLojaPerfeicao, 1);
        configCellLoja(this.jTableRozaCruz, 2);
        configCellLoja(this.jTableDadosKadosh, 3);
        configCellLoja(this.jTableDadosRealSegredo, 4);

        configCellCargo(this.jTableLojaPerfeicao);
        configCellCargo(this.jTableRozaCruz);
        configCellCargo(this.jTableDadosKadosh);
        configCellCargo(this.jTableDadosRealSegredo);

        this.jTableLojaPerfeicao.setRowHeight(21);
        this.jTableRozaCruz.setRowHeight(21);
        this.jTableDadosKadosh.setRowHeight(21);
        this.jTableDadosRealSegredo.setRowHeight(21);
        this.jTableLojaSimbolica.setRowHeight(21);
        this.jTableTempoGrau.setRowHeight(21);
        this.jTableAnuidades.setRowHeight(21);
    }

    private void configCellLoja(JTable table, int corpo) {
        TableColumn column = table.getColumnModel().getColumn(0);
        JComboBox field = new JComboBox();
        //field.setEditable(true);
        ArrayList<Loja> lojas = DAOFactory.getDaoLoja().buscaLojasPorCorpo(corpo);
        for (Loja obj : lojas) {
            field.addItem(obj);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }

    private void configCellCargo(JTable table) {
        TableColumn column = table.getColumnModel().getColumn(3);
        JComboBox field = new JComboBox();
        ArrayList<Cargo> cargos = DAOFactory.getDAOCargo().buscaCargos();
        field.addItem(null);
        for (Cargo obj : cargos) {
            field.addItem(obj);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }

    private void configCellGrau(JTable table, String columnName, ArrayList<Grau> grausCache) {
        TableColumn column = table.getColumn(columnName);
        JComboBox field = new JComboBox();
        for (Grau obj : grausCache) {
            field.addItem(obj);
        }
        column.setCellEditor(new DefaultCellEditor(field));
    }

    private void configCellData(JTable table, String columnName) {
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

    private Obreiro preencheObreiro() {
        Obreiro obj = new Obreiro();
        obj.setCargo(this.jTextFieldCargo.getText());
        obj.setCpf(this.jTextFieldCPF.getText());
        obj.setEmail(this.jTextFieldEmail.getText());
        obj.setEnderecoComercial(this.jTextAreaEndComercial.getText());
        obj.setEnderecoResidencial(this.jTextAreaEndResidencial.getText());
        obj.setLocalNascimento(this.jTextFieldLocal.getText());
        obj.setNomeEsposa(this.jTextFieldNomeEsposa.getText());
        obj.setCargo(this.jTextFieldCargo.getText());
        obj.setNomeMae(this.jTextFieldNomeMae.getText());
        obj.setNomePai(this.jTextFieldNomePai.getText());
        obj.setLojaSimbolicaAtual(this.jTextFieldLojaSimbolicaAtual.getText());
        obj.setDtNascimento(getDateField(this.jTextFieldDtNascimento.getText()));
        obj.setDtNascimentoEsposa(getDateField(this.jTextFieldDtNascimentoEsposa.getText()));
        obj.setNome(this.jTextFieldNomeCompleto.getText());
        obj.setIme(getIntField(this.jTextFieldIME.getText()));
        obj.setRg(this.jTextFieldRG.getText());
        obj.setProfissao(this.jTextFieldProfissao.getText());
        obj.setOutrasInformacoes(this.jTextAreaOutrasInformacoes.getText());
        obj.setRendimento(getDoubleField(this.jTextFieldRendimento.getText()));
        obj.setCim(getIntField(this.jTextFieldCIM.getText()));

        Object value = this.jTableLojaSimbolica.getValueAt(0, 1);
        obj.setLojaIniciacao(value == null ? null : (String) value);
        value = this.jTableLojaSimbolica.getValueAt(1, 1);
        obj.setLojaElevacao(value == null ? null : (String) value);
        value = this.jTableLojaSimbolica.getValueAt(2, 1);
        obj.setLojaExaltacao(value == null ? null : (String) value);

        value = this.jTableLojaSimbolica.getValueAt(0, 2);
        obj.setDtIniciacao(value == null ? null : getDateField(value.toString()));
        value = this.jTableLojaSimbolica.getValueAt(1, 2);
        obj.setDtElevacao(value == null ? null : getDateField(value.toString()));
        value = this.jTableLojaSimbolica.getValueAt(2, 2);
        obj.setDtExaltacao(value == null ? null : getDateField(value.toString()));

        if (this.jRadioButtonMestreInstaladoSim.isSelected()) {
            obj.setMestreInstalado(true);
        } else {
            obj.setMestreInstalado(false);
        }

        if (this.editedId != -1) {
            obj.setNumero(this.editedId);
        }
        return obj;
    }

    private void carregaLojaObreiro(Obreiro obreiro) {
        ArrayList<ArrayList<ArrayList>> relacionamentoLojas = DAOFactory.getDAOObreiro().getRelacionamentoLojas(obreiro);
        carregaTabelaObraObreiro(this.jTableLojaPerfeicao, relacionamentoLojas.get(0));
        carregaTabelaObraObreiro(this.jTableRozaCruz, relacionamentoLojas.get(1));
        carregaTabelaObraObreiro(this.jTableDadosKadosh, relacionamentoLojas.get(2));
        carregaTabelaObraObreiro(this.jTableDadosRealSegredo, relacionamentoLojas.get(3));
        carregaTabelaPagamentosAnuidadesObreiro(obreiro);
    }
    
    private void carregaTabelaPagamentosAnuidadesObreiro(Obreiro obreiro){
        ArrayList<PagamentoAnuidadeObreiro> lista= DAOFactory.getDAOAnuidadeObreiro().buscaPagamentoPorObreiro(obreiro);
        DefaultTableModel model = (DefaultTableModel) this.jTableAnuidades.getModel();
        NumericManager  nm= new NumericManager();
        DateManager dm= new DateManager();
        for (int i = 0; i < lista.size(); i++) {
            model.addRow(new Vector());
            model.setValueAt(lista.get(i).getAno(), i, 0);
            model.setValueAt(lista.get(i).getLoja().getNome(), i, 1);
            model.setValueAt(nm.getCurrencyFormat(lista.get(i).getValor()), i, 2);
            model.setValueAt(dm.setDateField(lista.get(i).getDtComprovanteBancario()), i, 3);
        }
    }

    private void carregaTabelaObraObreiro(JTable table, ArrayList<ArrayList> dados) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < dados.size(); i++) {
            Loja loja = (Loja) dados.get(i).get(0);
            Grau grau = (Grau) dados.get(i).get(1);
            String data = null;
            if (dados.get(i).get(2) != null) {
                data = setDateField((Date) dados.get(i).get(2));
            }
            Cargo cargo = (Cargo) dados.get(i).get(3);
            model.setValueAt(loja, i, 0);
            model.setValueAt(grau, i, 1);
            model.setValueAt(data, i, 2);
            model.setValueAt(cargo, i, 3);
        }
    }

    private void limpaTabelaObraObreiro(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(null, i, 0);
            model.setValueAt(null, i, 1);
            model.setValueAt(null, i, 2);
            model.setValueAt(null, i, 3);
        }
    }

    private void salvaObreiroLoja(Obreiro obreiro) {
        DAOFactory.getDAOObreiro().removeRelacionamentosObreiroLoja(obreiro.getIme());
        salvaLojasObreiros(this.jTableDadosKadosh, obreiro);
        salvaLojasObreiros(this.jTableDadosRealSegredo, obreiro);
        salvaLojasObreiros(this.jTableLojaPerfeicao, obreiro);
        salvaLojasObreiros(this.jTableRozaCruz, obreiro);
    }

    private void limpaTabelasObreiroLoja() {
        limpaTabelaObraObreiro(this.jTableDadosKadosh);
        limpaTabelaObraObreiro(this.jTableDadosRealSegredo);
        limpaTabelaObraObreiro(this.jTableLojaPerfeicao);
        limpaTabelaObraObreiro(this.jTableRozaCruz);
    }

    private void salvaLojasObreiros(JTable table, Obreiro obreiro) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0) != null) {
                Loja loja = (Loja) model.getValueAt(i, 0);
                Grau grau = (Grau) model.getValueAt(i, 1);
                Object dataTxt = model.getValueAt(i, 2);
                Date data = null;
                if (dataTxt != null) {
                    if (this.isValidDate(dataTxt.toString())) {
                        data = getDateField(dataTxt.toString());
                    } else {
                        String msg = DATE_FORMAT_VALIDATION_MSG;
                        msg = msg + "\n" + "Grau: " + grau.getNumero() + "  |  " + "Data: " + dataTxt;
                        JOptionPane.showMessageDialog(this, msg, "", JOptionPane.WARNING_MESSAGE);
                    }
                }

                Object cargoObj = model.getValueAt(i, 3);
                Cargo cargo = null;
                if (cargoObj != null) {
                    cargo = (Cargo) cargoObj;
                }
                DAOFactory.getDAOObreiro().armazenaRelacionamentoLoja(loja, obreiro, grau, data, cargo);
            }
        }
    }

    private boolean validaDadosLojasObreiros(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0) != null) {
                Object dataTxt = model.getValueAt(i, 2);

                Grau grau = (Grau) model.getValueAt(i, 1);
                if (grau == null) {
                    JOptionPane.showMessageDialog(this, GRAU_EMPTY, "", JOptionPane.WARNING_MESSAGE);
                    setFocusJTableCell(table, i, 1);
                    return false;
                }


                if (dataTxt != null && !dataTxt.equals("")) {
                    //fix issue with empty formated issue 
                    if (dataTxt.equals("  /  /    ")) { //It is the empty field
                        model.setValueAt(null, i, 2);
                        dataTxt = null;
                    } else {
                        if (!this.isValidDate(dataTxt.toString())) {
                            String msg = DATE_FORMAT_VALIDATION_MSG;
                            msg = msg + "\n" + "Grau: " + grau.getNumero() + "  |  " + "Data: " + dataTxt;
                            JOptionPane.showMessageDialog(this, msg, "", JOptionPane.WARNING_MESSAGE);
                            setFocusJTableCell(table, i, 2);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean validaDatesGrausSimbolicos() {
        Object[] dates = new Object[3];
        dates[0] = this.jTableLojaSimbolica.getValueAt(0, 2);
        dates[1] = this.jTableLojaSimbolica.getValueAt(1, 2);
        dates[2] = this.jTableLojaSimbolica.getValueAt(2, 2);
        byte notValidIndex = -1;
        for (byte i = 0; i < dates.length; i++) {
            if (dates[i] != null && !dates[i].toString().equals("")) {
                //fix issue with empty formated issue 
                String dataTxt=dates[i].toString();
                if (dataTxt.equals("  /  /    ")) { //It is the empty field
                    this.jTableLojaSimbolica.setValueAt(null,i, 2);
                    dataTxt = null;
                } else {
                    if (!this.isValidDate(dates[i].toString())) {
                        notValidIndex = i;
                    }
                }
            }
        }
        
        if (notValidIndex != -1) {
            String msg = DATE_FORMAT_VALIDATION_MSG;
            msg = msg + "\n" + "Grau: " + (notValidIndex + 1) + " |  " + "Data: " + dates[notValidIndex];
            JOptionPane.showMessageDialog(this, msg, "", JOptionPane.WARNING_MESSAGE);
            setFocusJTableCell(this.jTableLojaSimbolica, notValidIndex, 2);
        }
        return notValidIndex == -1;
    }
    
    private boolean validaDateField(JFormattedTextField field) {
        String dt = field.getText();
        boolean result=true;
        if (dt != null && !dt.equals("")) {
            //fix issue with empty formated issue 
            if (dt.equals("  /  /    ")) { //It is the empty field
               field.setText("");
            } else {
                if (!this.isValidDate(dt)) {
                    result= false;
                }
            }
        }
        return result;
    }

    private void setFocusJTableCell(JTable table, int row, int column) {
        table.setCellSelectionEnabled(true);
        table.changeSelection(row, column, false, false);
        table.requestFocus();
    }

    private boolean validaObreiroLoja() {
        boolean t1 = validaDadosLojasObreiros(this.jTableDadosKadosh);
        if (!t1) {
            return false;
        }

        boolean t2 = validaDadosLojasObreiros(this.jTableDadosRealSegredo);
        if (!t2) {
            return false;
        }
        boolean t3 = validaDadosLojasObreiros(this.jTableLojaPerfeicao);
        if (!t3) {
            return false;
        }
        boolean t4 = validaDadosLojasObreiros(this.jTableRozaCruz);
        if (!t4) {
            return false;
        }
        return true;
    }

    public void preencherFormulario(Obreiro obj) {
        limpaFormulario();
        this.jTextFieldCargo.setText(obj.getCargo());
        this.jTextFieldCPF.setText(obj.getCpf());
        this.jTextFieldEmail.setText(obj.getEmail());
        this.jTextAreaEndComercial.setText(obj.getEnderecoComercial());
        this.jTextAreaEndResidencial.setText(obj.getEnderecoResidencial());
        this.jTextFieldLocal.setText(obj.getLocalNascimento());
        this.jTextFieldNomeEsposa.setText(obj.getNomeEsposa());
        this.jTextFieldCargo.setText(obj.getCargo());
        this.jTextFieldNomeMae.setText(obj.getNomeMae());
        this.jTextFieldNomePai.setText(obj.getNomePai());
        this.jTextFieldLojaSimbolicaAtual.setText(obj.getLojaSimbolicaAtual());
        this.jTextFieldDtNascimento.setValue(setDateField(obj.getDtNascimento()));
        this.jTextFieldDtNascimentoEsposa.setValue(setDateField(obj.getDtNascimentoEsposa()));
        this.jTextFieldNomeCompleto.setText(obj.getNome());
        this.jTextFieldIME.setText(String.valueOf(obj.getIme()));
        this.jTextFieldRG.setText(obj.getRg());
        this.jTextFieldProfissao.setText(obj.getProfissao());
        this.jTextAreaOutrasInformacoes.setText(obj.getOutrasInformacoes());
        this.jTextFieldRendimento.setText(String.valueOf(obj.getRendimento()));
        this.jTextFieldCIM.setText(String.valueOf(obj.getCim()));
        this.editedId = obj.getNumero();

        this.jTableLojaSimbolica.setValueAt(obj.getLojaIniciacao(), 0, 1);
        this.jTableLojaSimbolica.setValueAt(obj.getLojaElevacao(), 1, 1);
        this.jTableLojaSimbolica.setValueAt(obj.getLojaExaltacao(), 2, 1);
        this.jTableLojaSimbolica.setValueAt(setDateField(obj.getDtIniciacao()), 0, 2);
        this.jTableLojaSimbolica.setValueAt(setDateField(obj.getDtElevacao()), 1, 2);
        this.jTableLojaSimbolica.setValueAt(setDateField(obj.getDtExaltacao()), 2, 2);
        if (obj.isMestreInstalado()) {
            this.jRadioButtonMestreInstaladoSim.setSelected(true);
        } else {
            this.jRadioButtonMestreInstaladoNao.setSelected(true);
        }
        carregaLojaObreiro(obj);
        //visual stuffs
        this.setBackground(null);
        this.jRadioButtonMestreInstaladoNao.setBackground(null);
        this.jRadioButtonMestreInstaladoSim.setBackground(null);
        this.jToggleButtonNewRecord.setEnabled(true);
        this.jToggleButtonNewRecord.setSelected(false);
    }

    public void limpaFormulario() {
        this.jTextFieldCargo.setText("");
        this.jTextFieldCPF.setText("");
        this.jTextFieldEmail.setText("");
        this.jTextAreaEndComercial.setText("");
        this.jTextAreaEndResidencial.setText("");
        this.jTextFieldLocal.setText("");
        this.jTextFieldNomeEsposa.setText("");
        this.jTextFieldCargo.setText("");
        this.jTextFieldNomeMae.setText("");
        this.jTextFieldNomePai.setText("");
        this.jTextFieldLojaSimbolicaAtual.setText("");
        this.jTextFieldDtNascimento.setValue(null);
        this.jTextFieldDtNascimentoEsposa.setValue(null);
        this.jTextFieldNomeCompleto.setText("");
        this.jTextFieldIME.setText("");
        this.jTextFieldRG.setText("");
        this.jTextFieldProfissao.setText("");
        this.jTextAreaOutrasInformacoes.setText("");
        this.jTextFieldRendimento.setText("");
        this.jTextFieldCIM.setText("");
        this.editedId = -1;
        this.jRadioButtonMestreInstaladoNao.setSelected(true);
        this.jTableLojaSimbolica.setValueAt(null, 0, 1);
        this.jTableLojaSimbolica.setValueAt(null, 1, 1);
        this.jTableLojaSimbolica.setValueAt(null, 2, 1);
        this.jTableLojaSimbolica.setValueAt(null, 0, 2);
        this.jTableLojaSimbolica.setValueAt(null, 1, 2);
        this.jTableLojaSimbolica.setValueAt(null, 2, 2);
        limpaTabelasObreiroLoja();
    }

    private double getCurrencyField(String text) {
        while (text.contains(".")) {
            text = text.replace(".", "");
        }
        text = text.replace(",", ".");
        double result = 0;
        try {
            result = Double.parseDouble(text);
        } catch (Exception e) {
        }
        return result;
    }

    private int getIntField(String text) {
        int result = -1;
        try {
            result = Integer.parseInt(text);
        } catch (Exception e) {
        }
        return result;
    }

    private double getDoubleField(String text) {
        double result = 0;
        try {
            result = Double.parseDouble(text);
        } catch (Exception e) {
        }
        return result;
    }

    private Date getDateField(String text) {
        Date result = null;
        try {
            String parts[] = text.split("/");
            if (parts.length == 3) {
                int day = Integer.parseInt(cleanLeftZero(parts[0]));
                int month = Integer.parseInt(cleanLeftZero(parts[1]));
                int year = Integer.parseInt(parts[2]);
                result = new Date(year - 1900, month - 1, day);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    private String cleanLeftZero(String number) {
        String result = number;
        if (number.charAt(0) == '0') {
            result = String.valueOf(number.charAt(1));
        }
        return result;
    }

    /**
     * Inform if the date is valid or not, considering not just the format but
     * confirms the existing of informed date.
     *
     * @param dateTxt: a date in format dd/MM/yyyy
     * @return true if it is a valid date.
     */
    public boolean isValidDate(String dateTxt) {
        boolean isValid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(dateTxt);
            isValid = sdf.format(date).equals(dateTxt); //após o parse, caso a data esteja errada, ela é aproximada para data mais próxima, logo difere do texto de entrada, e consta-se que a data é inválida
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            isValid = false;
        }
        return isValid;
    }

    private String setDateField(Date dt) {
        String result = null;
        if (dt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            result = sdf.format(dt);
        }
        return result;
    }

    private void gravaObreiro() {
        Obreiro obj = preencheObreiro();
        DAOFactory.getDAOObreiro().armazenaObreiro(obj);
        salvaObreiroLoja(obj);
        JOptionPane.showMessageDialog(this, this.SUCESS_WRITE_MSG, "", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupEMestre = new javax.swing.ButtonGroup();
        jLabelIME = new javax.swing.JLabel();
        jTextFieldIME = new javax.swing.JTextField();
        jLabelNomeCompleto = new javax.swing.JLabel();
        jTextFieldNomeCompleto = new javax.swing.JTextField();
        jLabelNomePai = new javax.swing.JLabel();
        jTextFieldNomePai = new javax.swing.JTextField();
        jLabelNomeMae = new javax.swing.JLabel();
        jTextFieldNomeMae = new javax.swing.JTextField();
        jLabelCPF = new javax.swing.JLabel();
        jLabelDtNascimento = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldLocal = new javax.swing.JTextField();
        jLabelRG = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelProfissao = new javax.swing.JLabel();
        jTextFieldProfissao = new javax.swing.JTextField();
        jLabelCargo = new javax.swing.JLabel();
        jLabelOutrasInformacoes = new javax.swing.JLabel();
        jTextFieldNomeEsposa = new javax.swing.JTextField();
        jLabelRendimento1 = new javax.swing.JLabel();
        jLabelDataNascimentoEsposa = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaOutrasInformacoes = new javax.swing.JTextArea();
        jLabelLojaSimbolicaAtual2 = new javax.swing.JLabel();
        jLabelNomeEsposa1 = new javax.swing.JLabel();
        jTextFieldLojaSimbolicaAtual = new javax.swing.JTextField();
        jTextFieldCargo = new javax.swing.JTextField();
        jLabelCIM = new javax.swing.JLabel();
        jTextFieldCIM = new javax.swing.JTextField();
        jLabelEndResidencial = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaEndResidencial = new javax.swing.JTextArea();
        jLabelEndComercial = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaEndComercial = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jRadioButtonMestreInstaladoNao = new javax.swing.JRadioButton();
        jRadioButtonMestreInstaladoSim = new javax.swing.JRadioButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableLojaSimbolica = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableLojaPerfeicao = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableRozaCruz = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableDadosKadosh = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableDadosRealSegredo = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableTempoGrau = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableAnuidades = new javax.swing.JTable();
        jToggleButtonNewRecord = new javax.swing.JToggleButton();
        jTextFieldCPF = new javax.swing.JFormattedTextField();
        jTextFieldRendimento = new javax.swing.JFormattedTextField();
        jTextFieldEmail = new javax.swing.JFormattedTextField();
        jTextFieldRG = new javax.swing.JTextField();
        jTextFieldDtNascimento = new javax.swing.JFormattedTextField();
        jTextFieldDtNascimentoEsposa = new javax.swing.JFormattedTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(884, 700));
        setLayout(new java.awt.GridBagLayout());

        jLabelIME.setText("IME:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelIME, gridBagConstraints);

        jTextFieldIME.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldIMEFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 67;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldIME, gridBagConstraints);

        jLabelNomeCompleto.setText("Nome completo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelNomeCompleto, gridBagConstraints);

        jTextFieldNomeCompleto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldNomeCompletoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldNomeCompleto, gridBagConstraints);

        jLabelNomePai.setText("Nome pai:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelNomePai, gridBagConstraints);

        jTextFieldNomePai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomePaiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldNomePai, gridBagConstraints);

        jLabelNomeMae.setText("Nome mãe:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelNomeMae, gridBagConstraints);

        jTextFieldNomeMae.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomeMaeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldNomeMae, gridBagConstraints);

        jLabelCPF.setText("CPF:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelCPF, gridBagConstraints);

        jLabelDtNascimento.setText("Data de nascimento:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelDtNascimento, gridBagConstraints);

        jLabel1.setText("Local Nascimento:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabel1, gridBagConstraints);

        jTextFieldLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLocalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldLocal, gridBagConstraints);

        jLabelRG.setText("RG:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelRG, gridBagConstraints);

        jLabelEmail.setText("e-mail:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelEmail, gridBagConstraints);

        jLabelProfissao.setText("Profissão:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelProfissao, gridBagConstraints);

        jTextFieldProfissao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProfissaoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldProfissao, gridBagConstraints);

        jLabelCargo.setText("Cargo(s):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelCargo, gridBagConstraints);

        jLabelOutrasInformacoes.setText("Outras informações:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelOutrasInformacoes, gridBagConstraints);

        jTextFieldNomeEsposa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomeEsposaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 222;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldNomeEsposa, gridBagConstraints);

        jLabelRendimento1.setText("Rendimentos (R$):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelRendimento1, gridBagConstraints);

        jLabelDataNascimentoEsposa.setLabelFor(jLabelDataNascimentoEsposa);
        jLabelDataNascimentoEsposa.setText("Data de nascimento da esposa:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelDataNascimentoEsposa, gridBagConstraints);

        jTextAreaOutrasInformacoes.setColumns(20);
        jTextAreaOutrasInformacoes.setRows(5);
        jTextAreaOutrasInformacoes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAreaOutrasInformacoesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextAreaOutrasInformacoes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane1, gridBagConstraints);

        jLabelLojaSimbolicaAtual2.setText("Loja simbólica atual:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelLojaSimbolicaAtual2, gridBagConstraints);

        jLabelNomeEsposa1.setText("Nome da esposa:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelNomeEsposa1, gridBagConstraints);

        jTextFieldLojaSimbolicaAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLojaSimbolicaAtualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 222;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldLojaSimbolicaAtual, gridBagConstraints);

        jTextFieldCargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCargoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldCargo, gridBagConstraints);

        jLabelCIM.setText("CIM:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelCIM, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 222;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldCIM, gridBagConstraints);

        jLabelEndResidencial.setText("Endereço residencial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelEndResidencial, gridBagConstraints);

        jTextAreaEndResidencial.setColumns(20);
        jTextAreaEndResidencial.setRows(5);
        jTextAreaEndResidencial.setPreferredSize(new java.awt.Dimension(120, 94));
        jTextAreaEndResidencial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAreaEndResidencialKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTextAreaEndResidencial);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.ipady = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane3, gridBagConstraints);

        jLabelEndComercial.setText("Endereço Comercial:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabelEndComercial, gridBagConstraints);

        jTextAreaEndComercial.setColumns(20);
        jTextAreaEndComercial.setRows(5);
        jTextAreaEndComercial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAreaEndComercialKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextAreaEndComercial);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.ipady = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jScrollPane2, gridBagConstraints);

        jButton1.setText("Gravar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jButton1, gridBagConstraints);

        jLabel2.setText("É mestre instalado:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jLabel2, gridBagConstraints);

        buttonGroupEMestre.add(jRadioButtonMestreInstaladoNao);
        jRadioButtonMestreInstaladoNao.setSelected(true);
        jRadioButtonMestreInstaladoNao.setText("Não");
        jRadioButtonMestreInstaladoNao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMestreInstaladoNaoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 23;
        add(jRadioButtonMestreInstaladoNao, gridBagConstraints);

        buttonGroupEMestre.add(jRadioButtonMestreInstaladoSim);
        jRadioButtonMestreInstaladoSim.setText("Sim");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jRadioButtonMestreInstaladoSim, gridBagConstraints);

        jTableLojaSimbolica.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", null, null},
                {"2", null, null},
                {"3", null, null}
            },
            new String [] {
                "Grau", "Loja simbólica que foi iniciado/elevado/exaltado", "Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLojaSimbolica.setPreferredSize(new java.awt.Dimension(210, 540));
        jTableLojaSimbolica.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTableLojaSimbolica);
        jTableLojaSimbolica.getColumnModel().getColumn(0).setMinWidth(20);
        jTableLojaSimbolica.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTableLojaSimbolica.getColumnModel().getColumn(1).setMinWidth(150);
        jTableLojaSimbolica.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTableLojaSimbolica.getColumnModel().getColumn(2).setMinWidth(30);
        jTableLojaSimbolica.getColumnModel().getColumn(2).setPreferredWidth(30);

        jTabbedPane1.addTab("Loja Simbólica", jScrollPane4);

        jTableLojaPerfeicao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Loja de perfeição", "Grau", "Data", "Cargo(s) Exercído(s)"
            }
        ));
        jTableLojaPerfeicao.setPreferredSize(new java.awt.Dimension(210, 540));
        jTableLojaPerfeicao.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(jTableLojaPerfeicao);
        jTableLojaPerfeicao.getColumnModel().getColumn(0).setMinWidth(120);
        jTableLojaPerfeicao.getColumnModel().getColumn(0).setPreferredWidth(120);
        jTableLojaPerfeicao.getColumnModel().getColumn(1).setMinWidth(30);
        jTableLojaPerfeicao.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTableLojaPerfeicao.getColumnModel().getColumn(2).setMinWidth(30);
        jTableLojaPerfeicao.getColumnModel().getColumn(2).setPreferredWidth(30);

        jTabbedPane1.addTab("Loja de Perfeição", jScrollPane5);

        jTableRozaCruz.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Capítulo Rosa Cruz", "Grau", "Data / Iniciação", "Cargo(s) Exercído(s)"
            }
        ));
        jTableRozaCruz.setPreferredSize(new java.awt.Dimension(210, 540));
        jTableRozaCruz.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(jTableRozaCruz);
        jTableRozaCruz.getColumnModel().getColumn(0).setMinWidth(120);
        jTableRozaCruz.getColumnModel().getColumn(0).setPreferredWidth(120);
        jTableRozaCruz.getColumnModel().getColumn(1).setMinWidth(30);
        jTableRozaCruz.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTableRozaCruz.getColumnModel().getColumn(2).setMinWidth(30);
        jTableRozaCruz.getColumnModel().getColumn(2).setPreferredWidth(30);

        jTabbedPane1.addTab("Capítulo Rosa Cruz", jScrollPane6);

        jTableDadosKadosh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Conselho de Cavaleiros Kadosch", "Grau", "Data / Iniciação", "Cargo(s) Exercído(s)"
            }
        ));
        jTableDadosKadosh.setPreferredSize(new java.awt.Dimension(210, 540));
        jTableDadosKadosh.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(jTableDadosKadosh);
        jTableDadosKadosh.getColumnModel().getColumn(0).setMinWidth(120);
        jTableDadosKadosh.getColumnModel().getColumn(0).setPreferredWidth(120);
        jTableDadosKadosh.getColumnModel().getColumn(1).setMinWidth(30);
        jTableDadosKadosh.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTableDadosKadosh.getColumnModel().getColumn(2).setMinWidth(30);
        jTableDadosKadosh.getColumnModel().getColumn(2).setPreferredWidth(30);

        jTabbedPane1.addTab("Conselho de Cavaleiros Kadosch", jScrollPane7);

        jTableDadosRealSegredo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Consistório de Príncipes do Real Segredo", "Grau", "Data / Iniciação", "Cargo(s) Exercído(s)"
            }
        ));
        jTableDadosRealSegredo.setPreferredSize(new java.awt.Dimension(210, 540));
        jTableDadosRealSegredo.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(jTableDadosRealSegredo);
        jTableDadosRealSegredo.getColumnModel().getColumn(0).setMinWidth(120);
        jTableDadosRealSegredo.getColumnModel().getColumn(0).setPreferredWidth(120);
        jTableDadosRealSegredo.getColumnModel().getColumn(1).setMinWidth(30);
        jTableDadosRealSegredo.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTableDadosRealSegredo.getColumnModel().getColumn(2).setMinWidth(30);
        jTableDadosRealSegredo.getColumnModel().getColumn(2).setPreferredWidth(30);

        jTabbedPane1.addTab("Consistório de Príncipes do Real Segredo", jScrollPane8);

        jTableTempoGrau.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", null, null},
                {"2", null, null},
                {"3", null, null},
                {"4", null, null},
                {"5", null, null},
                {"6", null, null},
                {"7", null, null},
                {"8", null, null},
                {"9", null, null},
                {"10", null, null},
                {"11", null, null},
                {"12", null, null},
                {"13", null, null},
                {"14", null, null},
                {"15", null, null},
                {"16", null, null},
                {"17", null, null},
                {"18", null, null},
                {"19", null, null},
                {"20", null, null},
                {"21", null, null},
                {"22", null, null},
                {"23", null, null},
                {"24", null, null},
                {"25", null, null},
                {"26", null, null},
                {"27", null, null},
                {"28", null, null},
                {"29", null, null},
                {"30", null, null},
                {"31", null, null},
                {"32", null, null},
                {"33", null, null}
            },
            new String [] {
                "Grau", "Tempo", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableTempoGrau.setPreferredSize(new java.awt.Dimension(210, 540));
        jTableTempoGrau.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(jTableTempoGrau);
        jTableTempoGrau.getColumnModel().getColumn(0).setMinWidth(120);
        jTableTempoGrau.getColumnModel().getColumn(0).setPreferredWidth(120);
        jTableTempoGrau.getColumnModel().getColumn(1).setMinWidth(30);
        jTableTempoGrau.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTableTempoGrau.getColumnModel().getColumn(2).setMinWidth(30);
        jTableTempoGrau.getColumnModel().getColumn(2).setPreferredWidth(30);

        jTabbedPane1.addTab("Tempo de cada grau", jScrollPane9);

        jTableAnuidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ano", "Loja simbólica", "Valor (R$)", "Data do comprovante bancário"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAnuidades.setPreferredSize(new java.awt.Dimension(210, 540));
        jTableAnuidades.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(jTableAnuidades);
        jTableAnuidades.getColumnModel().getColumn(0).setMinWidth(20);
        jTableAnuidades.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTableAnuidades.getColumnModel().getColumn(1).setMinWidth(150);
        jTableAnuidades.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTableAnuidades.getColumnModel().getColumn(2).setMinWidth(30);
        jTableAnuidades.getColumnModel().getColumn(2).setPreferredWidth(30);

        jTabbedPane1.addTab("Anuidades", jScrollPane10);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 55;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTabbedPane1, gridBagConstraints);

        jToggleButtonNewRecord.setText("Novo Registro");
        jToggleButtonNewRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonNewRecordActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jToggleButtonNewRecord, gridBagConstraints);

        try {
            jTextFieldCPF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldCPF, gridBagConstraints);

        jTextFieldRendimento.setFormatterFactory(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldRendimento, gridBagConstraints);

        jTextFieldEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmailActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldEmail, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldRG, gridBagConstraints);

        try {
            jTextFieldDtNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldDtNascimento, gridBagConstraints);

        try {
            jTextFieldDtNascimentoEsposa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTextFieldDtNascimentoEsposa, gridBagConstraints);

        jButton2.setText("X");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jButton2, gridBagConstraints);

        jButton3.setText("X");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jButton3, gridBagConstraints);

        jButton4.setBackground(new java.awt.Color(204, 204, 204));
        jButton4.setText("Imprimir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        add(jButton4, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNomePaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomePaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNomePaiActionPerformed

    private void jTextFieldNomeMaeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomeMaeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNomeMaeActionPerformed

    private void jTextFieldLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLocalActionPerformed

    private void jTextFieldProfissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProfissaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProfissaoActionPerformed

    private void jTextFieldNomeEsposaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomeEsposaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNomeEsposaActionPerformed

    private void jTextFieldLojaSimbolicaAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLojaSimbolicaAtualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLojaSimbolicaAtualActionPerformed

    private void jTextFieldCargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCargoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (this.getCursor().getType() != Cursor.WAIT_CURSOR) { // avoid more than one click while processing
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try {
                if (validaFormulario()) {
                    gravaObreiro();
                }
            } finally {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private boolean validaFormulario() {
        boolean isValid = true;
        if (this.jTextFieldNomeCompleto.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, NOME_EMPTY);
            isValid = false;
        } else if(!validaIMENumerico()){
            JOptionPane.showMessageDialog(this, IME_VALID);
            isValid=false;
        } else if (this.jTextFieldIME.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, IME_EMPTY);
            isValid = false;
        } else if (!validaIME()) {
            JOptionPane.showMessageDialog(this, IME_EXISTS);
            isValid = false;
        } else if (!validaNome()) {
            JOptionPane.showMessageDialog(this, NAME_EXISTS);
            isValid = false;
        } else if (!validaObreiroLoja()) {
            isValid = false;
        } else if (!validaDatesGrausSimbolicos()) {
            isValid = false;
        } else if(!validaDateField(this.jTextFieldDtNascimento)){
             String msg = DATE_FORMAT_VALIDATION_MSG + "\n" + "Data de nascimento: " + this.jTextFieldDtNascimento.getText();;
             JOptionPane.showMessageDialog(this, msg, "", JOptionPane.WARNING_MESSAGE);          
             isValid = false;
        }else if(!validaDateField(this.jTextFieldDtNascimentoEsposa)){
             String msg = DATE_FORMAT_VALIDATION_MSG + "\n" + "Data de nascimento esposa: " + this.jTextFieldDtNascimentoEsposa.getText();;
             JOptionPane.showMessageDialog(this, msg, "", JOptionPane.WARNING_MESSAGE);          
             isValid = false;
        }
        return isValid;
    }

    private boolean validaIME() {
        boolean ok = true;
        String ime = this.jTextFieldIME.getText().trim();
        if (!ime.equals("")) {
            try {
                Integer.parseInt(ime);
                Obreiro obj = DAOFactory.getDAOObreiro().buscaObreiroPorIME(Integer.parseInt(ime));
                if (obj != null && (obj.getNumero() != this.editedId || this.editedId == -1)) {
                    ok = false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, IME_VALID);
            }

        }
        return ok;
    }

        private boolean validaIMENumerico() {
        boolean ok = true;
        String ime = this.jTextFieldIME.getText().trim();
        if (!ime.equals("")) {
            try {
                Integer.parseInt(ime);
            } catch (NumberFormatException e) {
                ok=false;
            }

        }
        return ok;
    }
    
    
    private boolean validaNome() {
        boolean ok = true;
        String nome = this.jTextFieldNomeCompleto.getText().trim();
        if (!nome.equals("")) {
            Obreiro obj = DAOFactory.getDAOObreiro().buscaObreiroPorNome(nome);
            if (obj != null && (obj.getNumero() != this.editedId || this.editedId == -1)) {
                ok = false;
            }
        }
        return ok;
    }

    private void jRadioButtonMestreInstaladoNaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMestreInstaladoNaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonMestreInstaladoNaoActionPerformed

    private void jToggleButtonNewRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonNewRecordActionPerformed
        int answer = 0;
        if (this.editedId != -1) {
            answer = JOptionPane.showConfirmDialog(this, NEW_RECORD_CONFIRM_ACTION, "", JOptionPane.YES_NO_OPTION);
        }
        if (this.jToggleButtonNewRecord.isSelected() && answer == 0) {
            this.setBackground(Color.WHITE);
            this.jRadioButtonMestreInstaladoNao.setBackground(Color.WHITE);
            this.jRadioButtonMestreInstaladoSim.setBackground(Color.WHITE);
            limpaFormulario();
            this.jToggleButtonNewRecord.setEnabled(false);
            this.editedId = -1;
        }
    }//GEN-LAST:event_jToggleButtonNewRecordActionPerformed

    private void jTextFieldEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEmailActionPerformed

    private void jTextAreaEndResidencialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaEndResidencialKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
            if (evt.getModifiers() > 0) {
                jTextAreaEndResidencial.transferFocusBackward();
            } else {
                jTextAreaEndResidencial.transferFocus();
            }
            evt.consume();
        }
    }//GEN-LAST:event_jTextAreaEndResidencialKeyPressed

    private void jTextAreaEndComercialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaEndComercialKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
            if (evt.getModifiers() > 0) {
                jTextAreaEndComercial.transferFocusBackward();
            } else {
                jTextAreaEndComercial.transferFocus();
            }
            evt.consume();
        }
    }//GEN-LAST:event_jTextAreaEndComercialKeyPressed

    private void jTextAreaOutrasInformacoesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaOutrasInformacoesKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
            if (evt.getModifiers() > 0) {
                jTextAreaOutrasInformacoes.transferFocusBackward();
            } else {
                jTextAreaOutrasInformacoes.transferFocus();
            }
            evt.consume();
        }
    }//GEN-LAST:event_jTextAreaOutrasInformacoesKeyPressed

    private void jTextFieldNomeCompletoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldNomeCompletoFocusLost
        if (!validaNome()) {
            JOptionPane.showMessageDialog(this, NAME_EXISTS);
        }
    }//GEN-LAST:event_jTextFieldNomeCompletoFocusLost

    private void jTextFieldIMEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldIMEFocusLost
        if (!validaIME()) {
            JOptionPane.showMessageDialog(this, IME_EXISTS);
        }
    }//GEN-LAST:event_jTextFieldIMEFocusLost

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.jTextFieldDtNascimento.setValue(null);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.jTextFieldDtNascimentoEsposa.setValue(null);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try{
            int ime=Integer.valueOf(this.jTextFieldIME.getText());
            String targetFileNameFullPath="ficha_ime_"+ ime+".pdf";
            Obreiro obj= DAOFactory.getDAOObreiro().buscaObreiroPorIME(ime);
            if(obj!=null){
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                try{
                    new ObreiroFacade().impremeObreiro(obj, targetFileNameFullPath);
                    File f = new File(targetFileNameFullPath);
                    PDFViewer.show(f);
                }finally{ 
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
                           
            
            
        }catch(NumberFormatException e){
            System.err.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupEMestre;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelCIM;
    private javax.swing.JLabel jLabelCPF;
    private javax.swing.JLabel jLabelCargo;
    private javax.swing.JLabel jLabelDataNascimentoEsposa;
    private javax.swing.JLabel jLabelDtNascimento;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelEndComercial;
    private javax.swing.JLabel jLabelEndResidencial;
    private javax.swing.JLabel jLabelIME;
    private javax.swing.JLabel jLabelLojaSimbolicaAtual2;
    private javax.swing.JLabel jLabelNomeCompleto;
    private javax.swing.JLabel jLabelNomeEsposa1;
    private javax.swing.JLabel jLabelNomeMae;
    private javax.swing.JLabel jLabelNomePai;
    private javax.swing.JLabel jLabelOutrasInformacoes;
    private javax.swing.JLabel jLabelProfissao;
    private javax.swing.JLabel jLabelRG;
    private javax.swing.JLabel jLabelRendimento1;
    private javax.swing.JRadioButton jRadioButtonMestreInstaladoNao;
    private javax.swing.JRadioButton jRadioButtonMestreInstaladoSim;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableAnuidades;
    private javax.swing.JTable jTableDadosKadosh;
    private javax.swing.JTable jTableDadosRealSegredo;
    private javax.swing.JTable jTableLojaPerfeicao;
    private javax.swing.JTable jTableLojaSimbolica;
    private javax.swing.JTable jTableRozaCruz;
    private javax.swing.JTable jTableTempoGrau;
    private javax.swing.JTextArea jTextAreaEndComercial;
    private javax.swing.JTextArea jTextAreaEndResidencial;
    private javax.swing.JTextArea jTextAreaOutrasInformacoes;
    private javax.swing.JTextField jTextFieldCIM;
    private javax.swing.JFormattedTextField jTextFieldCPF;
    private javax.swing.JTextField jTextFieldCargo;
    private javax.swing.JFormattedTextField jTextFieldDtNascimento;
    private javax.swing.JFormattedTextField jTextFieldDtNascimentoEsposa;
    private javax.swing.JFormattedTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldIME;
    private javax.swing.JTextField jTextFieldLocal;
    private javax.swing.JTextField jTextFieldLojaSimbolicaAtual;
    private javax.swing.JTextField jTextFieldNomeCompleto;
    private javax.swing.JTextField jTextFieldNomeEsposa;
    private javax.swing.JTextField jTextFieldNomeMae;
    private javax.swing.JTextField jTextFieldNomePai;
    private javax.swing.JTextField jTextFieldProfissao;
    private javax.swing.JTextField jTextFieldRG;
    private javax.swing.JFormattedTextField jTextFieldRendimento;
    private javax.swing.JToggleButton jToggleButtonNewRecord;
    // End of variables declaration//GEN-END:variables
}
