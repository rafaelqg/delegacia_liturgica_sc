/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.integracao;

import java.io.File;

/**
 *
 * @author rafael
 */
public interface iDAOParametro {
    
    public boolean registraParametro(String description, File f);
    public File buscaParametro(String description);
}
