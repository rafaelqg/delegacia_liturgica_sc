/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import br.qg.loja.integracao.DAOFactory;
import br.qg.loja.integracao.iDAOAnuidadeObreiro;
import br.qg.loja.integracao.iDAOTipoEspecie;
import br.qg.loja.modelo.Loja;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.PagamentoAnuidadeObreiro;
import br.qg.loja.modelo.TipoEspecie;

/**
 *
 * @author RafaelQG
 */
public class DAOAnuidadeObreiro implements iDAOAnuidadeObreiro {

	
	
	
	
    @Override
    public boolean armazenaPagamentoAnuidade(PagamentoAnuidadeObreiro obj) {
        boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        Connection con = ds.getCon();
        try {
            if (existePagamento(obj.getAno(), obj.getObreiro(), obj.getLoja(), obj.getTipoEspecie(),obj.getGrau())!=null) {
                //update
                String sql = "update obreiro_fluxo_anuidade set obreiro=?,numero_loja_filosofica_atual=?,anoPagamento=?,dt_comprovate_dep_bancario=?,nr_prancha_encaminhamento=?,dt_prancha_encaminhamento=?, diligencia_motivo=?,dt_emissao_diligencia=?, dt_retorno_diligencia=?,numero_romaneio=?,dt_romaneio=?, valor_deposito=?, grau=? where numero=?"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
                PreparedStatement ps = con.prepareStatement(sql); 
                // Susbstitui cada um dos "?" do c�digo SQL pelo valor correspond�nte.
                ps.setInt(1, obj.getObreiro().getNumero());
                ps.setInt(2, obj.getLoja().getNumero());
                ps.setInt(3, obj.getAno());

                if (obj.getDtComprovanteBancario() != null) {
                    ps.setDate(4, new java.sql.Date(obj.getDtComprovanteBancario().getTime()));
                } else {
                    ps.setObject(4, null);
                }
                ps.setInt(5, obj.getNumeroPrancha());
                if (obj.getDtPrancha() != null) {
                    ps.setDate(6, new java.sql.Date(obj.getDtPrancha().getTime()));
                } else {
                    ps.setObject(6, null);
                }
                ps.setString(7, obj.getMotivo());

                if (obj.getDtEmissao() != null) {
                    ps.setDate(8, new java.sql.Date(obj.getDtEmissao().getTime()));
                } else {
                    ps.setObject(8, null);
                }

                if (obj.getDtRetorno() != null) {
                    ps.setDate(9, new java.sql.Date(obj.getDtRetorno().getTime()));
                } else {
                    ps.setObject(9, null);
                }
                ps.setInt(10, obj.getNumeroRomaneiro());
                if (obj.getDtRomaneio() != null) {
                    ps.setDate(11, new java.sql.Date(obj.getDtRomaneio().getTime()));
                } else {
                    ps.setObject(11, null);
                }
                ps.setDouble(12, obj.getValor());
                ps.setInt(13, obj.getGrau());
                ps.setInt(14, obj.getNumero());
                ps.executeUpdate();
                sucess = true;
            } else {

                //insert
                String sql = "insert into obreiro_fluxo_anuidade (obreiro,numero_loja_filosofica_atual,anoPagamento,dt_comprovate_dep_bancario,nr_prancha_encaminhamento,dt_prancha_encaminhamento, diligencia_motivo,dt_emissao_diligencia, dt_retorno_diligencia,numero_romaneio,dt_romaneio,valor_deposito, tipo_especie, grau) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
                PreparedStatement ps = con.prepareStatement(sql);      
                ps.setInt(1, obj.getObreiro().getNumero());
                ps.setInt(2, obj.getLoja().getNumero());
                ps.setInt(3, obj.getAno());

                if (obj.getDtComprovanteBancario() != null) {
                    ps.setDate(4, new java.sql.Date(obj.getDtComprovanteBancario().getTime()));
                } else {
                    ps.setObject(4, null);
                }
                ps.setInt(5, obj.getNumeroPrancha());
                if (obj.getDtPrancha() != null) {
                    ps.setDate(6, new java.sql.Date(obj.getDtPrancha().getTime()));
                } else {
                    ps.setObject(6, null);
                }
                ps.setString(7, obj.getMotivo());

                if (obj.getDtEmissao() != null) {
                    ps.setDate(8, new java.sql.Date(obj.getDtEmissao().getTime()));
                } else {
                    ps.setObject(8, null);
                }

                if (obj.getDtRetorno() != null) {
                    ps.setDate(9, new java.sql.Date(obj.getDtRetorno().getTime()));
                } else {
                    ps.setObject(9, null);
                }
                ps.setInt(10, obj.getNumeroRomaneiro());
                if (obj.getDtRomaneio() != null) {
                    ps.setDate(11, new java.sql.Date(obj.getDtRomaneio().getTime()));
                } else {
                    ps.setObject(11, null);
                }
                ps.setDouble(12, obj.getValor());
                if(obj.getTipoEspecie()==null){
                	ps.setInt(13, 2);//default
                }else{
                	ps.setInt(13, obj.getTipoEspecie().getNumero());
                }
                ps.setInt(14, obj.getGrau());
                ps.executeUpdate();
                sucess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sucess;
    }
    
    private final String SELECT_PAGAMENTO_OBREIRO = "SELECT numero,obreiro,numero_loja_filosofica_atual,anoPagamento,dt_comprovate_dep_bancario,nr_prancha_encaminhamento,dt_prancha_encaminhamento, diligencia_motivo,dt_emissao_diligencia, dt_retorno_diligencia,numero_romaneio,dt_romaneio,valor_deposito, tipo_especie, grau FROM obreiro_fluxo_anuidade";

    @Override
    public ArrayList<PagamentoAnuidadeObreiro> buscaPagamentoAnuidadesPorObreiroLoja(Obreiro obj, Loja lj) {
        ArrayList<PagamentoAnuidadeObreiro>  resultado = new ArrayList<PagamentoAnuidadeObreiro>();
        try {
            DataSourceMySQL con = new DataSourceMySQL();
            String sql = SELECT_PAGAMENTO_OBREIRO + " where obreiro=? and numero_loja_filosofica_atual=? order by obreiro";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, obj.getNumero());
            ps.setInt(2, lj.getNumero());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagamentoAnuidadeObreiro pagamento= new PagamentoAnuidadeObreiro();
                montaObjeto(pagamento, rs);
                resultado.add(pagamento);      
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    
    }

    public PagamentoAnuidadeObreiro existePagamento(int ano, Obreiro obr, Loja lj, TipoEspecie tipoEspecie, int grau) {
    	PagamentoAnuidadeObreiro resultado = null;
        try {
            DataSourceMySQL con = new DataSourceMySQL();
            String sql = SELECT_PAGAMENTO_OBREIRO + " where anoPagamento=? and obreiro=? and numero_loja_filosofica_atual=? and tipo_especie=?";
            if (tipoEspecie.getNumero()!=2){
            	 sql+=" and grau=?";
            }
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, ano);
            ps.setInt(2, obr.getNumero());
            ps.setInt(3, lj.getNumero());
            ps.setInt(4, tipoEspecie.getNumero());
            if (tipoEspecie.getNumero()!=2){
            	ps.setInt(5, grau);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { 
            	resultado= new PagamentoAnuidadeObreiro();
            	montaObjetoPlainObject(resultado, rs);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }
    
    /*
    obreiro,numero_loja_filosofica_atual,anoPagamento,
        dt_comprovante_dep_bancario,nr_prancha_encaminhamento,dt_prancha_encaminhamento,
        diligencia_motivo,dt_emissao_diligencia,
        dt_retorno_diligencia,numero_romaneio,dt_romaneio
         */
    
     private void montaObjeto(PagamentoAnuidadeObreiro obj, ResultSet rs) throws Exception {
         //Aqui recupero todos os campos relevantes desejados.
        obj.setObreiro(DAOFactory.getDAOObreiro().buscaObreiroPorChave(rs.getInt("o.numero")));
        obj.setLoja(DAOFactory.getDaoLoja().buscaLojaPorChave(rs.getInt("numero_loja_filosofica_atual")));
        obj.setAno(rs.getInt("anoPagamento"));
        obj.setNumero(rs.getInt("ofa.numero"));
        obj.setDtComprovanteBancario(rs.getDate("dt_comprovate_dep_bancario") != null ? new java.util.Date(rs.getDate("dt_comprovate_dep_bancario").getTime()) : null);
        obj.setNumeroPrancha(rs.getInt("nr_prancha_encaminhamento"));
        obj.setDtPrancha(rs.getDate("dt_prancha_encaminhamento")!=null ? new java.util.Date(rs.getDate("dt_prancha_encaminhamento").getTime()) : null);
        obj.setMotivo(rs.getString("diligencia_motivo"));
        obj.setDtEmissao(rs.getDate("dt_emissao_diligencia")!=null ? new java.util.Date(rs.getDate("dt_emissao_diligencia").getTime()) : null);
        obj.setDtRetorno(rs.getDate("dt_retorno_diligencia")!=null ? new java.util.Date(rs.getDate("dt_retorno_diligencia").getTime()) : null);
        obj.setNumeroRomaneiro(rs.getInt("numero_romaneio"));
        obj.setDtRomaneio(rs.getDate("dt_romaneio")!=null ? new java.util.Date(rs.getDate("dt_romaneio").getTime()) : null);
        obj.setValor(rs.getDouble("valor_deposito"));
        obj.setGrau(rs.getInt("grau"));
        obj.setTipoEspecie(DAOFactory.getDAOTipoEspecie().getTipoEspecie(rs.getInt("tipo_especie") ));
        
    }
     
     /*
      * Para os métodos que utilizam select sem joins
      */
      private void montaObjetoPlainObject(PagamentoAnuidadeObreiro obj, ResultSet rs) throws Exception {
         //Aqui recupero todos os campos relevantes desejados.
        obj.setObreiro(DAOFactory.getDAOObreiro().buscaObreiroPorChave(rs.getInt("obreiro")));
        obj.setLoja(DAOFactory.getDaoLoja().buscaLojaPorChave(rs.getInt("numero_loja_filosofica_atual")));
        obj.setAno(rs.getInt("anoPagamento"));
        obj.setNumero(rs.getInt("numero"));
        obj.setDtComprovanteBancario(rs.getDate("dt_comprovate_dep_bancario") != null ? new java.util.Date(rs.getDate("dt_comprovate_dep_bancario").getTime()) : null);
        obj.setNumeroPrancha(rs.getInt("nr_prancha_encaminhamento"));
        obj.setDtPrancha(rs.getDate("dt_prancha_encaminhamento")!=null ? new java.util.Date(rs.getDate("dt_prancha_encaminhamento").getTime()) : null);
        obj.setMotivo(rs.getString("diligencia_motivo"));
        obj.setDtEmissao(rs.getDate("dt_emissao_diligencia")!=null ? new java.util.Date(rs.getDate("dt_emissao_diligencia").getTime()) : null);
        obj.setDtRetorno(rs.getDate("dt_retorno_diligencia")!=null ? new java.util.Date(rs.getDate("dt_retorno_diligencia").getTime()) : null);
        obj.setNumeroRomaneiro(rs.getInt("numero_romaneio"));
        obj.setDtRomaneio(rs.getDate("dt_romaneio")!=null ? new java.util.Date(rs.getDate("dt_romaneio").getTime()) : null);
        obj.setValor(rs.getDouble("valor_deposito"));
        obj.setTipoEspecie(DAOFactory.getDAOTipoEspecie().getTipoEspecie(rs.getInt("tipo_especie") ));
        obj.setGrau(rs.getInt("grau"));
    }


    @Override
    public boolean excluiPagamentosPorObreiroLoja(int ano, Obreiro obj, Loja lj, TipoEspecie tipoEspecie) {
        boolean resultado=true;
        try {
            DataSourceMySQL con = new DataSourceMySQL(); 
            String sql ="delete from obreiro_fluxo_anuidade where anoPagamento=? and obreiro=? and numero_loja_filosofica_atual=? and tipo_especie=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, ano);
            ps.setInt(2, obj.getNumero());
            ps.setInt(3, lj.getNumero());
            ps.setInt(4, tipoEspecie.getNumero());
            ps.executeUpdate();
        } catch (Exception e) {
            resultado=false;
            System.err.println(e.getMessage());
        }
        return resultado;
    }
    

  
    @Override
    public ArrayList<PagamentoAnuidadeObreiro> buscaPagamentoAnuidadesPorLoja(Loja lj) {
         ArrayList<PagamentoAnuidadeObreiro>  resultado = new ArrayList<PagamentoAnuidadeObreiro>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            //String sql = "select o.ime, o.nome, max(og.dt_iniciacao_grau), ofa.numero, o.numero, ofa.numero_loja_filosofica_atual, ofa.anoPagamento, ofa.dt_comprovate_dep_bancario, ofa.nr_prancha_encaminhamento, ofa.dt_prancha_encaminhamento, ofa.diligencia_motivo, ofa.dt_emissao_diligencia, ofa.dt_retorno_diligencia, ofa.numero_romaneio, ofa.dt_romaneio from obreiro_grau og join obreiro o on o.ime=og.ime left join obreiro_fluxo_anuidade ofa on o.numero=ofa.obreiro and ofa.numero_loja_filosofica_atual=og.numero_loja_filosofica_atual where og.numero_loja_filosofica_atual=? group by ime order by o.nome";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            String sql="select o.ime, o.nome, og.numero_grau,og.dt_iniciacao_grau, \n" +
            "ofa.numero, o.numero, og.numero_loja_filosofica_atual,\n" +
            "ofa.anoPagamento, ofa.valor_deposito, ofa.dt_comprovate_dep_bancario, ofa.nr_prancha_encaminhamento, \n" +
            "ofa.dt_prancha_encaminhamento, ofa.diligencia_motivo, ofa.dt_emissao_diligencia, ofa.tipo_especie, \n" +
            "ofa.dt_retorno_diligencia, ofa.numero_romaneio, ofa.dt_romaneio, ofa.grau\n" +
            " \n" +
            "from obreiro_grau og \n" +
            "\n" +
            "join \n" +
            "	(\n" +
            "		select ime, max(numero_loja_filosofica_atual) ultLoja\n" +
            "		from obreiro_grau\n" +
            "		group by ime\n" +
            "	) x\n" +
            "	on x.ime = og.ime\n" +
            "	and x.ultLoja = og. numero_loja_filosofica_atual\n" +
            "\n" +
            "join obreiro o \n" +
            "on o.ime=og.ime \n" +
            "\n" +
            "left join obreiro_fluxo_anuidade ofa \n" +
            "on o.numero=ofa.obreiro \n" +
            "and ofa.numero_loja_filosofica_atual=og.numero_loja_filosofica_atual \n" +
            "\n" +
            "where og.numero_loja_filosofica_atual =?\n" +
            "\n" +
            "group by ime,tipo_especie order by o.nome,tipo_especie,ofa.anoPagamento";//
            
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, lj.getNumero());
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                PagamentoAnuidadeObreiro pagamento= new PagamentoAnuidadeObreiro();
                montaObjeto(pagamento, rs);
                resultado.add(pagamento);      
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }   

    @Override
    public ArrayList<PagamentoAnuidadeObreiro> buscaPagamentoPorObreiro(Obreiro obj) {
          ArrayList<PagamentoAnuidadeObreiro>  resultado = new ArrayList<PagamentoAnuidadeObreiro>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_PAGAMENTO_OBREIRO + " where obreiro=? order by anoPagamento,tipo_especie ";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, obj.getNumero());
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                PagamentoAnuidadeObreiro pagamento= new PagamentoAnuidadeObreiro();
                montaObjetoPlainObject(pagamento, rs);
                resultado.add(pagamento);      
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resultado;
    }
    
}
