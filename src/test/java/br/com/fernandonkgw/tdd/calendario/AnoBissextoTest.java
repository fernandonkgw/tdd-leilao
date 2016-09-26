package br.com.fernandonkgw.tdd.calendario;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AnoBissextoTest {

	@Test
	public void deveRetornarAnoBissexto() {
		AnoBissexto anoBissexto = new AnoBissexto(2016);
		
		assertTrue(anoBissexto.isAnoBissexto());
	}
	
	@Test
	public void naoDeveRetornarAnoBissexto() {
		AnoBissexto anoBissexto = new AnoBissexto(2015);
		
		assertFalse(anoBissexto.isAnoBissexto());
	}
}
