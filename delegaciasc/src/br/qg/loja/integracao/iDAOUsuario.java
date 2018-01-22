package br.qg.loja.integracao;

import java.util.ArrayList;

import br.qg.loja.modelo.UsuarioSistema;


public interface iDAOUsuario {
	 public ArrayList<UsuarioSistema> buscaUsuarios();
	 public UsuarioSistema autentica(String login, String password);
	 public boolean gravaDados(UsuarioSistema usuario);
	 public UsuarioSistema buscaPorChave(int chave);
}
