package br.com.fernandonkgw.tdd.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.fernandonkgw.tdd.builder.LeilaoBuilder;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.dominio.Usuario;

public class LeilaoDaoTest {

	private Session session;
	private UsuarioDao usuarioDao;
	private LeilaoDao leilaoDao;
	private Usuario fernando;
	private Leilao geladeiraNovaDoFernandoValor1500;
	private Leilao xboxNovoDoFernandoValor500;

	@Before
	public void antesDeCadaTeste() {
		session = new CriadorDeSessao().getSession();
		usuarioDao = new UsuarioDao(session);
		leilaoDao = new LeilaoDao(session);
		fernando = new Usuario("Fernando", "fernando@gmail.com");
		session.getTransaction().begin();
		usuarioDao.salvar(fernando);
		criaLeiloes();
	}
	
	private void criaLeiloes() {
		geladeiraNovaDoFernandoValor1500 = new LeilaoBuilder()
			.para("Geladeira")
			.novo()
			.dono(fernando)
			.comValorInicial(1500.0)
			.build();
		xboxNovoDoFernandoValor500 = new LeilaoBuilder()
			.para("XBox")
			.novo()
			.dono(fernando)
			.comValorInicial(500.0)
			.build();
	}
	
	@After
	public void depoisDeCadaTeste() {
		session.getTransaction().rollback();
		session.close();
	}
	
	@Test
	public void deveContarLeiloesNaoEncerrados() {
		
		xboxNovoDoFernandoValor500.encerra();
		
		leilaoDao.salvar(geladeiraNovaDoFernandoValor1500);
		leilaoDao.salvar(xboxNovoDoFernandoValor500);
		
		long total = leilaoDao.total();
		
		assertEquals(1L, total);
	}
	
	@Test
	public void deveRetornarZeroCasoNaoHajaNenhumLeilaoNaoEncerrado() {
		
		
		geladeiraNovaDoFernandoValor1500.encerra();
		xboxNovoDoFernandoValor500.encerra();
		
		leilaoDao.salvar(geladeiraNovaDoFernandoValor1500);
		leilaoDao.salvar(xboxNovoDoFernandoValor500);
		
		long total = leilaoDao.total();
		
		assertEquals(0L, total);
	}

	@Test
	public void deveRetornarLeiloesdeProdutoNovo() {
		
		Leilao leilaoDeXboxUsado = new Leilao("XBox", 500.0, fernando, true);
		
		leilaoDao.salvar(geladeiraNovaDoFernandoValor1500);
		leilaoDao.salvar(leilaoDeXboxUsado);
		
		List<Leilao> novos = leilaoDao.novos();
		Leilao leilaoNovo = novos.get(0);
		
		assertEquals(1L, novos.size());
		assertEquals("Geladeira", leilaoNovo.getDescricao());
		assertFalse(leilaoNovo.isUsado());
	}
	
	@Test
	public void deveRetornarLeiloesAntigos() {
		
		Leilao leilaoAntigoDeGeladeira = new LeilaoBuilder().para("Geladeira").dono(fernando).naDataAntiga().build();
		Leilao leilaoRecenteDeXBox = new LeilaoBuilder().para("XBox").build();
		
		leilaoDao.salvar(leilaoAntigoDeGeladeira);
		leilaoDao.salvar(leilaoRecenteDeXBox);
		
		List<Leilao> leiloesAntigos = leilaoDao.antigos();
		Leilao leilaoAntigo = leiloesAntigos.get(0);
		
		assertEquals(1L, leiloesAntigos.size());
		assertEquals("Geladeira", leilaoAntigo.getDescricao());
	}
	
	@Test
	public void naoDeveRetornarLeiloesDeUmaSemana() {
		Leilao leilaoAntigoDeGeladeira = new LeilaoBuilder().para("Geladeira").emUmaSemanaAtras().build();
		
		leilaoDao.salvar(leilaoAntigoDeGeladeira);
		
		List<Leilao> leiloesAntigos = leilaoDao.antigos();
		
		assertEquals(0, leiloesAntigos.size());
	}
}
