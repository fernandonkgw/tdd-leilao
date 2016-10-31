package br.com.fernandonkgw.tdd.servico;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import br.com.fernandonkgw.tdd.builder.CriadorDeLeilao;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.infra.dao.RepositorioDeLeiloes;

public class EncerradorDeLeilaoTest {

	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes() {
		Calendar antiga = new GregorianCalendar(1999, Calendar.FEBRUARY, 20);
		
		Leilao leilao1 = new CriadorDeLeilao().para("TV de Plasma").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
		List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);
		
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(leiloesAntigos);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();
		
		assertThat(encerrador.getTotalEncerrados(), equalTo(2));
		assertTrue(leilao1.isEncerrado());
		assertTrue(leilao2.isEncerrado());
	}
	
	@Test
	public void naoDeveEncerrarLeiloesQueComecaramOntem() {
		Calendar ontem = Calendar.getInstance();
		ontem.add(Calendar.DAY_OF_MONTH, -1);
		
		Leilao leilao1 = new CriadorDeLeilao().para("TV de Plasma").naData(ontem).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(ontem).constroi();
		List<Leilao> leiloesDeOntem = Arrays.asList(leilao1, leilao2);
		
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(leiloesDeOntem);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();
		
		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
		assertFalse(leilao1.isEncerrado());
		assertFalse(leilao2.isEncerrado());
	}
	
	@Test
	public void naoDeveEncerrarCasoNaoHajaNenhum() {
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
		encerrador.encerra();
		
		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
	}
	
	public void deveAtualizarLeiloesEncerrados() {
		
	}
}
