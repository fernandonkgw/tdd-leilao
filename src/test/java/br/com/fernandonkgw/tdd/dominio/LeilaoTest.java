package br.com.fernandonkgw.tdd.dominio;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static br.com.fernandonkgw.tdd.matcher.LeilaoMatcher.temUmLance;

import org.junit.Before;
import org.junit.Test;

public class LeilaoTest {

	private Usuario steveJobs;
	private Usuario steveWozniak;
	private Usuario billGates;
	private Leilao leilao;

	@Before
	public void criaUsuarios() {
		this.leilao = new Leilao("Macbook Pro 15");
		this.steveJobs = new Usuario("Steve Jobs");
		this.steveWozniak = new Usuario("Steve Wozniak");
		this.billGates = new Usuario("Bill Gates");
	}
	
	@Test
	public void deveReceberUmLance() {
		assertTrue(leilao.getLances().isEmpty());
		
		Lance lance = new Lance(steveJobs, 3000.0);
		leilao.propoe(lance);
		
		assertThat(leilao.getLances().size(), equalTo(1));
		assertThat(leilao, temUmLance(lance));
	}
	
	@Test
	public void deveReceberVariosLances() {
		leilao.propoe(new Lance(steveJobs, 3000.0));
		leilao.propoe(new Lance(steveWozniak, 4000.0));
		
		assertThat(leilao.getLances().size(), equalTo(2));
		Lance primeiroLance = leilao.primeiroLanceDado();
		assertThat(primeiroLance.getValor(), equalTo(3000.0));
		assertThat(leilao.getLances().get(1).getValor(), equalTo(4000.0));
	}
	
	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
		leilao.propoe(new Lance(steveJobs, 2000.0));
		leilao.propoe(new Lance(steveJobs, 3000.0));
		
		assertThat(leilao.getLances().size(), equalTo(1));
		Lance primeiroLance = leilao.primeiroLanceDado();
		assertThat(primeiroLance.getValor(), equalTo(2000.0));
	}
	
	@Test
	public void naoDeveAceitarMaisQue5LancesDoMesmoUsuario() {
		leilao.propoe(new Lance(steveJobs, 2000.0));
		leilao.propoe(new Lance(billGates, 3000.0));
		leilao.propoe(new Lance(steveJobs, 4000.0));
		leilao.propoe(new Lance(billGates, 5000.0));
		leilao.propoe(new Lance(steveJobs, 6000.0));
		leilao.propoe(new Lance(billGates, 7000.0));
		leilao.propoe(new Lance(steveJobs, 8000.0));
		leilao.propoe(new Lance(billGates, 9000.0));
		leilao.propoe(new Lance(steveJobs, 10000.0));
		leilao.propoe(new Lance(billGates, 11000.0));
		// deve ser ignorado
		leilao.propoe(new Lance(steveJobs, 12000.0));
		
		assertThat(leilao.getLances().size(), equalTo(10));
		Lance ultimoLance = leilao.ultimoLanceDado();
		assertThat(ultimoLance.getValor(), equalTo(11000.0));
	}
	
	@Test
	public void deveDobrarOUltimoLanceDado() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		leilao.propoe(new Lance(steveJobs, 2000.0));
		leilao.propoe(new Lance(billGates, 3000.0));
		leilao.dobraLance(steveJobs);
		
		Lance ultimoLance = leilao.ultimoLanceDado();
		assertThat(ultimoLance.getValor(), equalTo(4000.0));
	}
	
	@Test
	public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		
		leilao.dobraLance(steveJobs);
		
		assertThat(leilao.getLances().size(), equalTo(0));
	}
}
