/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.qg.loja.integracao.iDAOParametro;
import br.qg.loja.modelo.Cargo;

/**
 *
 * @author rafael
 */
public class DAOParametro implements iDAOParametro {

    @Override
    public boolean registraParametro(String description, File f) {
        boolean sucess = false;
        DataSourceMySQL ds = new DataSourceMySQL();
        Connection con = ds.getCon();// Recupera uma conexão ativa ou cria uma nova conexão
        try {
            //update
            String sql = "insert into parametro  (description, file) values(?,?)"; // Define um comando SQL, e os parâmetros são definidos por "?" para posteriormente poderem ser definidos.
            PreparedStatement ps = con.prepareStatement(sql); // O comando SQL é preparado para ter seus parametros definidos. O preparedStatement proporciona a substituição dos "?" pelo valores correspondênte.
            // Susbstitui cada um dos "?" do código SQL pelo valor correspondênte.
            ps.setString(1, description);
            ps.setBinaryStream(2, new FileInputStream(f));
            ps.executeUpdate();
            sucess = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return sucess;
    }

    @Override
    public File buscaParametro(String description) {
        File resultado = new File("logo.bmp");// esta lista será o retorno do método, independentemente de encontrar ou não resultados.
        try {
            FileOutputStream output = new FileOutputStream(resultado);
            DataSourceMySQL con = new DataSourceMySQL(); // instancia um objeto para prover uma conexão com BD
            String sql = "select file from parametro where description=?";// código SQL para consultar os dados do DB, devem buscar todos os atributos (aqui estão sendo trazidos apenas o id e o nome)
            Connection c = con.getCon();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, description);
            ResultSet rs = ps.executeQuery(); // Este comando busca os resultados do bd, e os atribui ao objeto da classe ResultSet
            while (rs.next()) { // percorre cada uma das linhas encontradas no DB
                InputStream input = rs.getBinaryStream("file");
                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0) {
                    output.write(buffer);
                }
                input.close();
            }
            output.close();           
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        return resultado;
    }
}