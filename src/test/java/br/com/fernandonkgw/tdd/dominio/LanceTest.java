package br.com.fernandonkgw.tdd.dominio;

import org.junit.Test;

public class LanceTest {

	@Test(expected=IllegalArgumentException.class)
	public void deveRecusarLanceComValorZero() {
		new Lance(new Usuario("Joao"), 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deveRecusarLanceComValorMenorQueZero() {
		new Lance(new Usuario("Joao"), -5);
	}
	
}
