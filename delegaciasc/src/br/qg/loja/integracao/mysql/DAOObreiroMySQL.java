/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOLoja;
import br.qg.loja.integracao.iDAOObreiro;
import br.qg.loja.modelo.Cargo;
import br.qg.loja.modelo.Grau;
import br.qg.loja.modelo.LogAltercaoObreiro;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;

/**
 *
 * @author Rafael
 */
public class DAOObreiroMySQL implements iDAOObreiro {

    @Override
    public boolean armazenaObreiro(Obreiro obj) {
        boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        Connection con = ds.getCon();
        //verifica se ime mudou, e se sim, atualiza tabela de relacionamento com grau_loja
        
        try {
            if (obj.getNumero() != -1) {
                //update
            	//verifica se houve altera��o do IME
            	int imeNovo=obj.getIme();
                int imeAntigo=this.buscaObreiroPorChave(obj.getNumero()).getIme();
            	boolean mudouIME=imeNovo==imeAntigo?false:true;
            	
            	
                String sql = "update obreiro set ime=?,nome=?,nome_mae=?,cpf=?,rg=?,dt_nascimento=?,local_nascimento=?,endereco_residencial=?, endereco_comercial=?,profissao=?,cargo=?,rendimento=?, outras_informacoes=?,nome_esposa=?,dt_nascimento_esposa=?,nome_pai=?,loja_simbolica_atual=?,email=?,cim=?,dt_exaltacao=?, dt_elevacao=?, dt_iniciacao=?,loja_exaltacao=?, loja_elevacao=?, loja_iniciacao=?,mestre_instalado=?  where numero=?"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, obj.getIme());
                ps.setString(2, obj.getNome());
                ps.setString(3, obj.getNomeMae());
                ps.setString(4, obj.getCpf());
                ps.setString(5, obj.getRg());
                if(obj.getDtNascimento()!=null){
                    ps.setDate(6, new java.sql.Date(obj.getDtNascimento().getTime()));
                }else{
                    ps.setObject(6, null);
                }
                ps.setString(7, obj.getLocalNascimento());
                ps.setString(8, obj.getEnderecoResidencial());
                ps.setString(9, obj.getEnderecoComercial());
                ps.setString(10, obj.getProfissao());
                ps.setString(11, obj.getCargo());
                ps.setDouble(12, obj.getRendimento());
                ps.setString(13, obj.getOutrasInformacoes());
                ps.setString(14, obj.getNomeEsposa());
                if(obj.getDtNascimentoEsposa()!=null){
                    ps.setDate(15, new java.sql.Date(obj.getDtNascimentoEsposa().getTime()));
                }else{
                    ps.setObject(15, null);
                }
                ps.setString(16, obj.getNomePai());
                ps.setString(17, obj.getLojaSimbolicaAtual());
                ps.setString(18, obj.getEmail());
                ps.setInt(19, obj.getCim());
                ps.setDate(20, obj.getDtExaltacao() != null ? new java.sql.Date(obj.getDtExaltacao().getTime()) : null);
                ps.setDate(21, obj.getDtElevacao() != null ? new java.sql.Date(obj.getDtElevacao().getTime()) : null);
                ps.setDate(22, obj.getDtIniciacao() != null ? new java.sql.Date(obj.getDtIniciacao().getTime()) : null); 
                ps.setString(23, obj.getLojaExaltacao());
                ps.setString(24, obj.getLojaElevacao());
                ps.setString(25, obj.getLojaIniciacao());
                ps.setBoolean(26, obj.isMestreInstalado());
                ps.setInt(27, obj.getNumero());
                ps.executeUpdate();               
               
                sucess = true;
                
                if (mudouIME){
                	atualizaIMERelacionamentoLoja(imeAntigo,imeNovo);
                }
            } else {
                //insert
                String sql = "insert into obreiro (ime,nome,nome_mae,cpf,rg,dt_nascimento,local_nascimento,endereco_residencial, endereco_comercial,profissao,cargo,rendimento, outras_informacoes,nome_esposa,dt_nascimento_esposa, nome_pai,loja_simbolica_atual,email,cim,dt_exaltacao, dt_elevacao, dt_iniciacao,loja_exaltacao, loja_elevacao, loja_iniciacao,mestre_instalado) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, obj.getIme());
                ps.setString(2, obj.getNome());
                ps.setString(3, obj.getNomeMae());
                ps.setString(4, obj.getCpf());
                ps.setString(5, obj.getRg());
                if(obj.getDtNascimento()!=null){
                    ps.setDate(6, new java.sql.Date(obj.getDtNascimento().getTime()));
                }else{
                    ps.setObject(6, null);
                }
                ps.setString(7, obj.getLocalNascimento());
                ps.setString(8, obj.getEnderecoResidencial());
                ps.setString(9, obj.getEnderecoComercial());
                ps.setString(10, obj.getProfissao());
                ps.setString(11, obj.getCargo());
                ps.setDouble(12, obj.getRendimento());
                ps.setString(13, obj.getOutrasInformacoes());
                ps.setString(14, obj.getNomeEsposa());
                if(obj.getDtNascimentoEsposa()!=null){
                    ps.setDate(15, new java.sql.Date(obj.getDtNascimentoEsposa().getTime()));
                }else{
                     ps.setObject(15, null);
                }
                ps.setString(16, obj.getNomePai());
                ps.setString(17, obj.getLojaSimbolicaAtual());
                ps.setString(18, obj.getEmail());
                ps.setInt(19, obj.getCim());
                ps.setDate(20, obj.getDtIniciacao() != null ? new java.sql.Date(obj.getDtIniciacao().getTime()) : null);
                ps.setDate(21, obj.getDtElevacao() != null ? new java.sql.Date(obj.getDtElevacao().getTime()) : null);
                ps.setDate(22, obj.getDtExaltacao() != null ? new java.sql.Date(obj.getDtExaltacao().getTime()) : null);
                ps.setString(23, obj.getLojaExaltacao());
                ps.setString(24, obj.getLojaElevacao());
                ps.setString(25, obj.getLojaIniciacao());
                ps.setBoolean(26, obj.isMestreInstalado());
                ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
                sucess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucess;
    }

    @Override
    public ArrayList<Obreiro> buscaObreiros() {
        ArrayList<Obreiro> lista = new ArrayList<Obreiro>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " order by nome";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }

    @Override
    public Obreiro buscaObreiroPorChave(int chave) {
        Obreiro resultado = null;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " where numero=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, chave);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                resultado = obj;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }
    private final String SELECT_OBREIRO = "SELECT cargo,cpf,dt_nascimento,dt_nascimento_esposa,endereco_comercial,endereco_residencial,ime,local_nascimento,nome,nome_esposa,nome_mae,numero,outras_informacoes, profissao,rendimento, rg,nome_pai,loja_simbolica_atual,email,cim,dt_exaltacao, dt_elevacao, dt_iniciacao,loja_exaltacao, loja_elevacao, loja_iniciacao, mestre_instalado FROM obreiro"; // código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)";

    private void montaObjeto(Obreiro obj, ResultSet rs) throws Exception {
        // Aqui recupero todos os campos relevantes desejados.
        obj.setCargo(rs.getString("cargo")); //o parâmetro é o nome da coluna no banco de dados
        obj.setCpf(rs.getString("cpf"));
        if(rs.getDate("dt_nascimento")!=null){
            obj.setDtNascimento(new java.util.Date(rs.getDate("dt_nascimento").getTime()));
        }
        if(rs.getDate("dt_nascimento_esposa")!=null){
            obj.setDtNascimentoEsposa(new java.util.Date(rs.getDate("dt_nascimento_esposa").getTime()));
        }
        obj.setEnderecoComercial(rs.getString("endereco_comercial"));
        obj.setEnderecoResidencial(rs.getString("endereco_residencial"));
        obj.setIme(rs.getInt("ime"));
        obj.setLocalNascimento(rs.getString("local_nascimento"));
        obj.setNome(rs.getString("nome"));
        obj.setNomeEsposa(rs.getString("nome_esposa"));
        obj.setNomeMae(rs.getString("nome_mae"));
        obj.setNumero(rs.getInt("numero"));
        obj.setOutrasInformacoes(rs.getString("outras_informacoes"));
        obj.setProfissao(rs.getString("profissao"));
        obj.setRendimento(rs.getDouble("rendimento"));
        obj.setRg(rs.getString("rg"));
        obj.setNomePai(rs.getString("nome_pai"));
        obj.setLojaSimbolicaAtual(rs.getString("loja_simbolica_atual"));
        obj.setEmail(rs.getString("email"));
        obj.setCim(rs.getInt("cim"));
        obj.setDtExaltacao(rs.getDate("dt_exaltacao")!=null?new java.util.Date(rs.getDate("dt_exaltacao").getTime()):null);
        obj.setDtElevacao(rs.getDate("dt_elevacao")!=null?new java.util.Date(rs.getDate("dt_elevacao").getTime()):null);
        obj.setDtIniciacao(rs.getDate("dt_iniciacao")!=null?new java.util.Date(rs.getDate("dt_iniciacao").getTime()):null);
        obj.setLojaExaltacao(rs.getString("loja_exaltacao"));
        obj.setLojaElevacao(rs.getString("loja_elevacao"));
        obj.setLojaIniciacao(rs.getString("loja_iniciacao"));
        obj.setMestreInstalado(rs.getBoolean("mestre_instalado"));
    }

    @Override
    public Obreiro buscaObreiroPorIME(int ime) {
        Obreiro resultado = null;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " where ime=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, ime);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                resultado = obj;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }

    @Override
    public boolean removeObreiro(int chave) {
        boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        try {
            Connection con = ds.getCon();// Recupera uma conexão ativa ou cria uma nova conexão
            //insert
            String sql = "delete from obreiro where numero=?"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
            PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
            // Susbstitui cada um dos "?" do código SQL pelo valor correspondênte.
            ps.setInt(1, chave);
            ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
            sucess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucess;
    }

    @Override
    public ArrayList<Obreiro> buscaObreiroBasico(String value) {
        ArrayList<Obreiro> lista = new ArrayList<Obreiro>();
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " where nome like ? or ime = ? order by nome";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + value + "%");
            int ime = 0;
            try {
                ime = Integer.parseInt(value);
                
            } catch (Exception e2) {
            	ime=-10;
            }
            ps.setInt(2, ime);
            
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean armazenaRelacionamentoLoja(Loja loja, Obreiro obreiro, Grau grau, Date data, Cargo cargo) {
        boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        Connection con = ds.getCon();// Recupera uma conexão ativa ou cria uma nova conexão
        int numero = buscaRelacionamentoLoja(loja, obreiro, grau);
        try {
            if (numero != -1) {
                //update
                String sql = "update obreiro_grau set ime=?, numero_loja_filosofica_atual=?,numero_grau=?,dt_iniciacao_grau=?,codigo_cargo_exercido=? where numero=?"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
                PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
                // Susbstitui cada um dos "?" do código SQL pelo valor correspondênte.
                ps.setInt(1, obreiro.getIme());
                ps.setInt(2, loja.getNumero());
                ps.setInt(3, grau.getNumero());
                if(data!=null){
                    ps.setDate(4, new java.sql.Date(data.getTime()));
                }else{
                    ps.setObject(4, null);
                }
                if(cargo!=null){
                    ps.setInt(5, cargo.getNumero());
                }else{
                    ps.setObject(5, null);
                }
                ps.setInt(6, numero);
                ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
                sucess = true;
            } else {
                //insert
                String sql = "insert into obreiro_grau (ime,numero_loja_filosofica_atual,numero_grau,dt_iniciacao_grau,codigo_cargo_exercido) VALUES(?,?,?,?,?)"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
                PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
                // Susbstitui cada um dos "?" do código SQL pelo valor correspondênte.
                ps.setInt(1, obreiro.getIme());
                ps.setInt(2, loja.getNumero());
                ps.setInt(3, grau.getNumero());
                if(data!=null){
                    ps.setDate(4, new java.sql.Date(data.getTime()));
                }else{
                    ps.setObject(4, null);
                }
                 if(cargo!=null){
                    ps.setInt(5, cargo.getNumero());
                }else{
                    ps.setObject(5, null);
                }
                ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
                sucess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucess;
    }
    
    public boolean atualizaIMERelacionamentoLoja(int imeAntigo, int imeNovo) {
        boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        Connection con = ds.getCon();
        try {
                String sql = "update obreiro_grau set ime=? where ime=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, imeNovo);
                ps.setInt(2, imeAntigo);
                ps.executeUpdate();
                sucess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucess;
    }
    
    private final String SELECT_OBREIRO_LOJA = "SELECT og.numero,ime,numero_loja_filosofica_atual,numero_grau,dt_iniciacao_grau,codigo_cargo_exercido FROM obreiro_grau og"; // código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)";

    @Override
    public int buscaRelacionamentoLoja(Loja loja, Obreiro obreiro, Grau grau) {
        int chave = -1;
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO_LOJA + " where numero_loja_filosofica_atual =? and ime=? and numero_grau=?";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, loja.getNumero());
            ps.setInt(2, obreiro.getIme());
            ps.setInt(3, grau.getNumero());
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                chave = rs.getInt("numero");
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return chave;
    }

    @Override
    public ArrayList<ArrayList<ArrayList>> getRelacionamentoLojas(Obreiro obreiro) {
        ArrayList<ArrayList<ArrayList>> relacionamentos = new ArrayList<ArrayList<ArrayList>>();
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO_LOJA + " join loja l on l.numero=numero_loja_filosofica_atual where ime=? and corpo=? order by numero_grau";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            for (int i = 1; i <= 4; i++) {
                ArrayList<ArrayList> relacionamentoCorpo= new ArrayList<ArrayList>();
                ps.setInt(1, obreiro.getIme());
                ps.setInt(2, i);
                ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
                while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                    ArrayList relacionamento= new ArrayList();
                    int chaveLoja = rs.getInt("numero_loja_filosofica_atual");
                    int chaveGrau = rs.getInt("numero_grau");
                    int chaveCargo= rs.getInt("codigo_cargo_exercido");
                    Grau grau= new Grau();
                    grau.setNumero(chaveGrau);
                    Date data= null;
                    if(rs.getObject("dt_iniciacao_grau")!=null){
                        data= new java.util.Date(rs.getDate("dt_iniciacao_grau").getTime());
                    }
                    Loja loja= DAOFactory.getDaoLoja().buscaLojaPorChave(chaveLoja);
                    Cargo cargo=DAOFactory.getDAOCargo().buscaCargoPorCodigo(chaveCargo);
                    relacionamento.add(loja);
                    relacionamento.add(grau);
                    relacionamento.add(data);
                    relacionamento.add(cargo);
                    relacionamentoCorpo.add(relacionamento);
                }
                relacionamentos.add(relacionamentoCorpo);
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return relacionamentos;
    }

    @Override
    public boolean removeRelacionamentosObreiroLoja(int ime) {
       boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        try {
            Connection con = ds.getCon();// Recupera uma conexão ativa ou cria uma nova conexão
            //insert
            String sql = "delete from obreiro_grau where ime=?"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
            PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
            // Susbstitui cada um dos "?" do código SQL pelo valor correspondênte.
            ps.setInt(1, ime);
            ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
            sucess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucess;
        
    }

    @Override
    public Obreiro buscaObreiroPorNome(String nome) {
        Obreiro resultado = null;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " where nome=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                resultado = obj;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<ArrayList> buscaObreirosPorLoja(Loja loja) {
        ArrayList<ArrayList> relacionamentos = new ArrayList<ArrayList>();
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql="select t.ime, t.numero_grau, t.codigo_cargo_exercido, t.dt_iniciacao_grau , o.nome from obreiro_grau t, obreiro o where   (t.ime = o.ime) and ((numero_grau in (select max(numero_grau) from obreiro_grau tin where tin.ime=t.ime) and numero_loja_filosofica_atual=?))  order by t.ime";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, loja.getNumero());
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                ArrayList dados= new ArrayList();
                int chaveObreiro =rs.getInt("t.ime");
                int chaveGrau = rs.getInt("t.numero_grau");
                int chaveCargo= rs.getInt("t.codigo_cargo_exercido");
                //Obreiro obr=this.buscaObreiroPorIME(chaveObreiro);
                Grau grau= new Grau();
                grau.setNumero(chaveGrau);
                Date data= null;
                if(rs.getObject("t.dt_iniciacao_grau")!=null){
                    data= new java.util.Date(rs.getDate("t.dt_iniciacao_grau").getTime());
                }
                //Cargo cargo=DAOFactory.getDAOCargo().buscaCargoPorCodigo(chaveCargo);
                String nomeObreiro=rs.getString("o.nome");
                dados.add(chaveObreiro);  
                dados.add(nomeObreiro);
                dados.add(grau);
                dados.add(data);
                         
                relacionamentos.add(dados);
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return relacionamentos;
    }

    @Override
    public Obreiro autenticar(String ime, String CPF) {
        int imeInt=Integer.valueOf(ime.trim());
    	Obreiro resultado = null;
    	try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " where ime=? and (cpf=? or cpf='   .   .   -  ' or cpf='')";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, imeInt);
            ps.setString(2, CPF);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                resultado = obj;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }

	@Override
	public boolean removeRelacionamentoObreiroLoja(int ime, int grau) {
		 boolean sucess = false;
	        DataSourceMySQL ds = new DataSourceMySQL();
	        try {
	            Connection con = ds.getCon();// Recupera uma conexão ativa ou cria uma nova conexão
	            //insert
	            String sql = "delete from obreiro_grau where ime=? and numero_grau=?"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
	            PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
	            // Susbstitui cada um dos "?" do código SQL pelo valor correspondênte.
	            ps.setInt(1, ime);
	            ps.setInt(2, grau);
	            ps.executeUpdate(); // este comando envia o código SQL para ser processado pelo servidor de banco de dados
	            sucess = true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return sucess;
	}

	@Override
	public Obreiro buscaObreiroPorCPF(String cpf) {
		 Obreiro resultado = null;// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
	        try {
	            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
	            String sql = SELECT_OBREIRO + " where  cpf=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
	            Connection c = con.getCon();
	            PreparedStatement ps = c.prepareStatement(sql);
	            ps.setString(1, cpf);
	            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
	            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
	                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
	                montaObjeto(obj, rs);
	                resultado = obj;
	            }
	        } catch (Exception e) {
	            System.err.println(e.getMessage());
	        }
	        return resultado;
	}

	@Override
	public ArrayList<Obreiro> buscaObreirosAniversariantesdoMes() {
		ArrayList<Obreiro> lista = new ArrayList<Obreiro>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " where MONTH(dt_nascimento)=MONTH(NOW()) order by nome";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
	}

	@Override
	public ArrayList<Obreiro> buscaObreirosAniversariantesdoDia() {
		ArrayList<Obreiro> lista = new ArrayList<Obreiro>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_OBREIRO + " WHERE (MONTH(dt_nascimento) = MONTH(NOW()) AND DAY(dt_nascimento) = DAY(NOW())) OR (MONTH(dt_nascimento) = 2 AND DAY(dt_nascimento) = 29 AND MONTH(NOW()) = 3 AND DAY(NOW()) = 1 AND (YEAR(NOW()) % 4 = 0) AND ((YEAR(NOW()) % 100 != 0) OR (YEAR(NOW()) % 400 = 0))) order by nome";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                Obreiro obj = new Obreiro(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
	}

	@Override
	public Loja buscaLojaAtual(Obreiro obj) {
		Loja l = new Loja();
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql="select numero_loja_filosofica_atual, ime, numero_grau, dt_iniciacao_grau from obreiro_grau where ime=?  order by dt_iniciacao_grau desc limit 1";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, obj.getIme());
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                int chaveLoja =rs.getInt("numero_loja_filosofica_atual");
                iDAOLoja daoLoja=DAOFactory.getDaoLoja();
                l =daoLoja.buscaLojaPorChave(chaveLoja);
            }
           
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return l;
	}
	
	public void logOperacao(long idUsuario, int idOperacao, int idObreiro){
		try{
			DataSourceMySQL con = new DataSourceMySQL();    
	        String sql = "insert into log_operacoes_obreiro (id_usuario, id_operacao, id_obreiro, timestamp) VALUES(?,?,?, now())";
	        Connection c = con.getCon();
	        PreparedStatement ps = c.prepareStatement(sql);
	        ps.setLong(1, idUsuario);
	        ps.setInt(2, idOperacao);
	        ps.setInt(3, idObreiro);
	        ps.executeUpdate();
		 } catch (Exception e) {
	            System.err.println(e.getMessage());
		 }
	}

	@Override
	public ArrayList<LogAltercaoObreiro> buscaLogAlteracoes() {
		  ArrayList<LogAltercaoObreiro> lista = new ArrayList<LogAltercaoObreiro>();
	        try {
	            DataSourceMySQL con = new DataSourceMySQL();
	            String sql = "SELECT u.login, ob.nome, date(log.timestamp) FROM log_operacoes_obreiro log inner join usuario u on log.id_usuario=u.id inner join obreiro ob on ob.numero=log.id_obreiro group by date(log.timestamp),log.id_usuario, log.id_obreiro order by date(log.timestamp) desc";
	            Connection c = con.getCon();
	            PreparedStatement ps = c.prepareStatement(sql);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	            	LogAltercaoObreiro obj = new LogAltercaoObreiro(); 
	            	obj.setNomeUsuario(rs.getString(1));
	            	obj.setNomeObreiro(rs.getString(2));
	            	obj.setDate(rs.getString(3));
	                lista.add(obj); 
	            }
	        } catch (Exception e) {
	            System.err.println(e.getMessage());
	        }
	        return lista;
	}



}