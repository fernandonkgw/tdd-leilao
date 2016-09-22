package br.com.fernandonkgw.tdd.matematica;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatematicaMalucaTest {

	@Test
	public void deveMultiplicarNumerosMaioresQue30() {
		MatematicaMaluca matematicaMaluca = new MatematicaMaluca();
		assertEquals(160, matematicaMaluca.contaMaluca(40));
	}

	@Test
	public void deveMultiplicarNumerosMaioresQue10EMenoresQue30() {
		MatematicaMaluca matematicaMaluca = new MatematicaMaluca();
		assertEquals(60, matematicaMaluca.contaMaluca(20));
	}
	
	@Test
	public void deveMultiplicarNumerosMenoresQue10() {
		MatematicaMaluca matematicaMaluca = new MatematicaMaluca();
		assertEquals(10, matematicaMaluca.contaMaluca(5));
	}
}
