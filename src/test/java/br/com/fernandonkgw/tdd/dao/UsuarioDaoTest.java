package br.com.fernandonkgw.tdd.dao;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.fernandonkgw.tdd.dominio.Usuario;

public class UsuarioDaoTest {

	private Session session;
	private UsuarioDao usuarioDao;

	@Before
	public void antesDeCadaTeste() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		session.getTransaction().begin();
	}
	
	@After
	public void depoisDeCadaTeste() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveEncontrarPeloNomeEEmail() {
		
		Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
		usuarioDao.salvar(novoUsuario);
		
		Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");
		
		assertEquals("Joao da Silva", usuario.getNome());
		assertEquals("joao@dasilva.com.br", usuario.getEmail());
	}

	@Test
	public void deveRetornarNuloSeUsuarioNaoExistir() {
		
		Usuario usuario = usuarioDao.porNomeEEmail("Fernando", "fernando@gmail.com");
		
		assertNull(usuario);
	}
}
