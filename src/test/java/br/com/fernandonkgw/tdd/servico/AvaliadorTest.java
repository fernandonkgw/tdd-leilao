package br.com.fernandonkgw.tdd.servico;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.fernandonkgw.tdd.builder.LeilaoBuilder;
import br.com.fernandonkgw.tdd.dominio.Lance;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.dominio.Usuario;

public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario maria;

	@Before
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
		
		this.joao = new Usuario("João");
		this.jose = new Usuario("José");
		this.maria = new Usuario("Maria");
	}
	
	@Test
	public void deveEntenderLancesEmOrdemCrescente() {
		
		// parte 1: cenario
		Leilao leilao = new LeilaoBuilder().para("Playstation 3 Novo")
				.lance(joao, 250.0)
				.lance(jose, 300.0)
				.lance(maria, 400.0)
				.build();
		
		// parte 2: acao, 
		leiloeiro.avalia(leilao);
		
		// parte 3: validacao
		double maiorEsperado = 400.0;
		double menorEsperado = 250.0;
		
		assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
		assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));
	}
	
	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		
		// cenario: 1 Lance
		Leilao leilao = new LeilaoBuilder().para("Playstation 3 Novo")
				.lance(joao, 1000.0)
				.build();
		
		// parte 2: acao, 
		leiloeiro.avalia(leilao);
		
		// parte 3: validacao
		double maiorEsperado = 1000.0;
		double menorEsperado = 1000.0;
		
		assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
		assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));
	}
	
	@Test
	public void deveEncontrarTresMaioresLances() {
		
		Leilao leilao =  new LeilaoBuilder().para("Playstation 3 Novo")
				.lance(joao, 100.0)
				.lance(maria, 200)
				.lance(joao, 300)
				.lance(maria, 400.0)
				.build();
		
		leiloeiro.avalia(leilao);
		
		List<Lance> maiores = leiloeiro.getTresMaiores();
		assertThat(maiores.size(), equalTo(3));
		assertThat(maiores, hasItems(
				new Lance(maria, 400.0),
				new Lance(joao, 300.0),
				new Lance(maria, 200.0)
				));
	}
	
	@Test
	public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
		Leilao leilao = new LeilaoBuilder().para("Playstation 3 Novo")
				.lance(joao, 200.0)
				.lance(maria, 250.0)
				.lance(joao, 340.0)
				.lance(maria, 100.0)
				.lance(joao, 500)
				.build();
		
		leiloeiro.avalia(leilao);
		
		double maiorEsperado = 500.0;
		double menorEsperado = 100.0;
		assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
		assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));
	}
	
	@Test
	public void deveEntenderLeilaoComLancesEmOrdemDecrescente() {
		
		Leilao leilao = new LeilaoBuilder().para("Playstation 3 Novo")
				.lance(joao, 400.0)
				.lance(maria, 300.0)
				.lance(joao, 200.0)
				.lance(maria, 100)
				.build();
		
		leiloeiro.avalia(leilao);
		
		double maiorEsperado = 400.0;
		double menorEsperado = 100.0;
		assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
		assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));
	}
	
	@Test
	public void deveCalcularMedia() {
		
		// cenario: 3 lances em ordem crescentes
		Leilao leilao = new LeilaoBuilder().para("Playstation 3 Novo")
				.lance(maria, 300.0)
				.lance(joao, 400.0)
				.lance(jose, 500.0)
				.build();
		
		// executa acao
		leiloeiro.avalia(leilao);
		
		// comparando a saida com esperado
		assertThat(leiloeiro.getMedia(), equalTo(400.0));
	}

	@Test(expected=RuntimeException.class)
	public void naoDeveAvaliarLeilaoSemLanceDado() {
		Leilao leilao = new LeilaoBuilder().para("Playstation 3").build();
		
		leiloeiro.avalia(leilao);
	}
}
