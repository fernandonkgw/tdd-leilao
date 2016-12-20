package br.com.fernandonkgw.tdd.servico;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import br.com.fernandonkgw.tdd.builder.CriadorDeLeilao;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.infra.dao.RepositorioDeLeiloes;
import br.com.fernandonkgw.tdd.infra.email.EnviadorDeEmail;

public class EncerradorDeLeilaoTest {

	private Calendar antiga;
	private Calendar ontem;
	private Leilao leilaoAntigoDeTvDePlasma;
	private Leilao leilaoAntigoDeGeladeira;
	private Leilao leilaoDeOntemDeTvDePlasma;
	private Leilao leilaoDeOntemDeGeladeira;
	private RepositorioDeLeiloes daoFalso;
	private EnviadorDeEmail carteiroFalso;
	private EncerradorDeLeilao encerrador;
	
	@Before
	public void criaDatasELeiloes() {
		antiga = new GregorianCalendar(1999, Calendar.FEBRUARY, 20);
		ontem = Calendar.getInstance();
		ontem.add(Calendar.DAY_OF_MONTH, -1);
		leilaoAntigoDeTvDePlasma = new CriadorDeLeilao().para("TV de Plasma").naData(antiga).constroi();
		leilaoAntigoDeGeladeira = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
		leilaoDeOntemDeTvDePlasma = new CriadorDeLeilao().para("TV de Plasma").naData(ontem).constroi();
		leilaoDeOntemDeGeladeira = new CriadorDeLeilao().para("Geladeira").naData(ontem).constroi();
		daoFalso = mock(RepositorioDeLeiloes.class);
		carteiroFalso = mock(EnviadorDeEmail.class);
		encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
	}
	
	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes() {
		
		List<Leilao> leiloesAntigos = Arrays.asList(leilaoAntigoDeTvDePlasma, leilaoAntigoDeGeladeira);
		
		when(daoFalso.correntes()).thenReturn(leiloesAntigos);
		
		encerrador.encerra();
		
		assertThat(encerrador.getTotalEncerrados(), equalTo(2));
		assertTrue(leilaoAntigoDeTvDePlasma.isEncerrado());
		assertTrue(leilaoAntigoDeGeladeira.isEncerrado());
	}
	
	@Test
	public void naoDeveEncerrarLeiloesQueComecaramOntem() {
		
		List<Leilao> leiloesDeOntem = Arrays.asList(leilaoDeOntemDeTvDePlasma, leilaoDeOntemDeGeladeira);
		
		when(daoFalso.correntes()).thenReturn(leiloesDeOntem);
		
		encerrador.encerra();
		
		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
		assertFalse(leilaoDeOntemDeTvDePlasma.isEncerrado());
		assertFalse(leilaoDeOntemDeGeladeira.isEncerrado());
	}
	
	@Test
	public void naoDeveEncerrarCasoNaoHajaNenhum() {
		when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());
		
		encerrador.encerra();
		
		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
	}
	
	@Test
	public void deveAtualizarLeiloesEncerrados() {
		
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilaoAntigoDeTvDePlasma));
		
		encerrador.encerra();
		
		// passamos os mocks que serão verificados
		InOrder inOrder = inOrder(daoFalso, carteiroFalso);
		
		// verifica se o método atualiza foi invocado
		// a primeira invocação
		inOrder.verify(daoFalso, times(1)).atualiza(leilaoAntigoDeTvDePlasma);
		
		
		inOrder.verify(carteiroFalso, times(1)).envia(leilaoAntigoDeTvDePlasma);
	}
	
	@Test
	public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {
		
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilaoDeOntemDeTvDePlasma, leilaoDeOntemDeGeladeira));
		
		encerrador.encerra();
		
		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
		assertFalse(leilaoDeOntemDeTvDePlasma.isEncerrado());
		assertFalse(leilaoDeOntemDeGeladeira.isEncerrado());
		
		verify(daoFalso, never()).atualiza(leilaoDeOntemDeTvDePlasma);
		verify(daoFalso, never()).atualiza(leilaoDeOntemDeGeladeira);
	}
	
	@Test
	public void deveContinuarExecucaoMesmoQuandoDaoFalha() {
		
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilaoAntigoDeTvDePlasma, leilaoAntigoDeGeladeira));
		
		doThrow(new RuntimeException()).when(daoFalso).atualiza(leilaoAntigoDeTvDePlasma);
		
		encerrador.encerra();
		
		verify(daoFalso).atualiza(leilaoAntigoDeGeladeira);
		verify(carteiroFalso).envia(leilaoAntigoDeGeladeira);
	}
	
	@Test
	public void deveContinuarExecucaoMesmoQuandoEnviadorDeEmailFalha() {
		
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilaoAntigoDeTvDePlasma, leilaoAntigoDeGeladeira));
		
		doThrow(new RuntimeException()).when(carteiroFalso).envia(leilaoAntigoDeTvDePlasma);
		
		encerrador.encerra();
		
		verify(daoFalso).atualiza(leilaoAntigoDeGeladeira);
		verify(carteiroFalso).envia(leilaoAntigoDeGeladeira);
	}
	
	@Test
	public void naoDeveEnviarEmailQuandoDaoLancaExcecaoParaTodosLeiloes() {
		
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilaoAntigoDeTvDePlasma, leilaoAntigoDeGeladeira));
		
		doThrow(new RuntimeException()).when(daoFalso).atualiza(any(Leilao.class));
		
		encerrador.encerra();
		
		verify(carteiroFalso, never()).envia(leilaoAntigoDeTvDePlasma);
		verify(carteiroFalso, never()).envia(leilaoAntigoDeGeladeira);
	}
}
