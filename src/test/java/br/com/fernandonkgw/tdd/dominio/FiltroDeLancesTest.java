package br.com.fernandonkgw.tdd.dominio;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FiltroDeLancesTest {

	@Test
	public void deveSelecionarLancesEntre1000E3000() {
		Usuario joao = new Usuario("Joao");
		
		FiltroDeLances filtro = new FiltroDeLances();
		List<Lance> resultado = filtro.filtra(Arrays.asList(
				new Lance(joao, 2000.0),
				new Lance(joao, 1000.0),
				new Lance(joao, 3000.0),
				new Lance(joao, 800)));
		
		assertEquals(1, resultado.size());
		assertEquals(2000, resultado.get(0).getValor(), 0.00001);
	}

	@Test
	public void deveSelecionarLancesEntre500E700() {
		Usuario joao = new Usuario("Joao");
		
		FiltroDeLances filtro = new FiltroDeLances();
		List<Lance> resultado = filtro.filtra(Arrays.asList(
				new Lance(joao, 600.0),
				new Lance(joao, 500.0),
				new Lance(joao, 700.0),
				new Lance(joao, 800.0)));
		
		assertEquals(1, resultado.size());
		assertEquals(600.0, resultado.get(0).getValor(), 0.00001);
	}
}
