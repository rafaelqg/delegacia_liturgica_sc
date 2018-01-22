/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import br.qg.loja.integracao.iDAOTipoEspecie;
import br.qg.loja.modelo.Obreiro;
import br.qg.loja.modelo.TipoEspecie;

/**
 *
 * @author RafaelQG
 */
public class DAOTipoEspecie implements iDAOTipoEspecie {

    private final String SELECT_TIPO_ESPECIE = "select numero, nome from tipo_especie";
    
    @Override
    public TipoEspecie getTipoEspecie(int numero) {
        TipoEspecie obj=null;// esta lista ser� o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_TIPO_ESPECIE + " where numero=?";
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                 obj = new TipoEspecie(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println("Tipo esp�cie - getTipoEspecie: "+ e.getMessage());
        }
        return obj;
    }

    @Override
    public ArrayList<TipoEspecie> getAllTipoEspecie() {
         ArrayList<TipoEspecie> lista = new ArrayList<TipoEspecie>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = SELECT_TIPO_ESPECIE;
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                TipoEspecie obj = new TipoEspecie(); // cria um novo objeto  para cada registro do banco de dados
                montaObjeto(obj, rs);
                lista.add(obj); // inclui o registro do banco de dados, em forma de objeto, em uma lista
            }
            con.DesconectaDB();
        } catch (Exception e) {
            System.err.println("Tipo esp�cie - getAll: "+e.getMessage());
        }
        return lista;
    }
    
    public Hashtable<Integer, TipoEspecie> getAllTipoEspecieHash() {
    Hashtable<Integer, TipoEspecie> hashTable = new Hashtable<Integer, TipoEspecie>();// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
       try {
           DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
           String sql = SELECT_TIPO_ESPECIE;
           Connection c = con.getCon();
           PreparedStatement ps = c.prepareStatement(sql);
           ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
           while (rs.next()) { // percorre cada uma das linhas encontradas no DB
               TipoEspecie obj = new TipoEspecie(); // cria um novo objeto  para cada registro do banco de dados
               montaObjeto(obj, rs);
               hashTable.put(new Integer(obj.getNumero()), obj);  // inclui o registro do banco de dados, em forma de objeto, em uma lista
           }
           con.DesconectaDB();
       } catch (Exception e) {
           System.err.println("Tipo esp�cie - getAllTipoEspecieHash: "+e.getMessage());
       }
       
       return hashTable;
   }
    
    private void  montaObjeto(TipoEspecie obj, ResultSet rs) throws Exception {
        // Aqui recupero todos os campos relevantes desejados.
        obj.setNumero(rs.getInt("numero")); //o parâmetro é o nome da coluna no banco de dados
        obj.setNome(rs.getString("nome"));
    }
    
}
