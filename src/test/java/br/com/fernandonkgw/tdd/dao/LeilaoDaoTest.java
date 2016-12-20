package br.com.fernandonkgw.tdd.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.fernandonkgw.tdd.builder.CriadorDeLeilao;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.dominio.Usuario;

public class LeilaoDaoTest {

	private Session session;
	private UsuarioDao usuarioDao;
	private LeilaoDao leilaoDao;
	private Usuario fernando;

	@Before
	public void antesDeCadaTeste() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		leilaoDao = new LeilaoDao(session);
		fernando = new Usuario("Fernando", "fernando@gmail.com");
		session.getTransaction().begin();
		usuarioDao.salvar(fernando);
	}
	
	@After
	public void depoisDeCadaTeste() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveContarLeiloesNaoEncerrados() {
		
		Leilao ativo = new Leilao("Geladeira", 1500.0, fernando, false);
		
		Leilao encerrado = new Leilao("TV", 700.0, fernando, false);
		encerrado.encerra();
		
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		assertEquals(1L, total);
	}
	
	@Test
	public void deveRetornarZeroCasoNaoHajaNenhumLeilaoNaoEncerrado() {
		
		Leilao leilaoDeGeladeira = new Leilao("Geladeira", 1500.0, fernando, false);
		Leilao leilaoDeXBox = new Leilao("XBox", 500.0, fernando, false);
		
		leilaoDeGeladeira.encerra();
		leilaoDeXBox.encerra();
		
		leilaoDao.salvar(leilaoDeGeladeira);
		leilaoDao.salvar(leilaoDeXBox);
		
		long total = leilaoDao.total();
		
		assertEquals(0L, total);
	}

	@Test
	public void deveRetornarLeiloesdeProdutoNovo() {
		
		Leilao leilaoDeGeladeiraNova = new Leilao("Geladeira", 1500.0, fernando, false);
		Leilao leilaoDeXboxUsado = new Leilao("XBox", 500.0, fernando, true);
		
		leilaoDao.salvar(leilaoDeGeladeiraNova);
		leilaoDao.salvar(leilaoDeXboxUsado);
		
		List<Leilao> novos = leilaoDao.novos();
		Leilao leilaoNovo = novos.get(0);
		
		assertEquals(1L, novos.size());
		assertEquals("Geladeira", leilaoNovo.getDescricao());
		assertFalse(leilaoNovo.isUsado());
	}
	
	@Test
	public void deveRetornarLeiloesAntigos() {
		Leilao leilaoAntigoDeGeladeira = new CriadorDeLeilao().para("Geladeira").naDataAntiga().constroi();
		Leilao leilaoRecenteDeXBox = new CriadorDeLeilao().para("XBox").naData(Calendar.getInstance()).constroi();
		
		leilaoDao.salvar(leilaoAntigoDeGeladeira);
		leilaoDao.salvar(leilaoRecenteDeXBox);
		
		List<Leilao> leiloesAntigos = leilaoDao.antigos();
		Leilao leilaoAntigo = leiloesAntigos.get(0);
		
		assertEquals(1L, leiloesAntigos.size());
		assertEquals("Geladeira", leilaoAntigo.getDescricao());
	}
	
	@Test
	public void naoDeveRetornarLeiloesDeUmaSemana() {
		Leilao leilaoAntigoDeGeladeira = new CriadorDeLeilao().para("Geladeira").emUmaSemanaAtras().constroi();
		
		leilaoDao.salvar(leilaoAntigoDeGeladeira);
		
		List<Leilao> leiloesAntigos = leilaoDao.antigos();
		
		assertEquals(1L, leiloesAntigos.size());
	}
}
