package br.com.fernandonkgw.tdd.servico;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import br.com.fernandonkgw.tdd.builder.CriadorDeLeilao;
import br.com.fernandonkgw.tdd.dao.RepositorioDeLeiloes;
import br.com.fernandonkgw.tdd.dao.RepositorioDePagamentos;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.dominio.Pagamento;
import br.com.fernandonkgw.tdd.dominio.Relogio;
import br.com.fernandonkgw.tdd.dominio.Usuario;

public class GeradorDePagamentoTest {

	private RepositorioDeLeiloes leilaoDaoMock;
	private RepositorioDePagamentos pagamentoDaoMock;
	private ArgumentCaptor<Pagamento> pagamentoArgument;
	private Leilao leilaoEncerrado;
	
	@Before
	public void setup() {
		leilaoDaoMock = mock(RepositorioDeLeiloes.class);
		pagamentoDaoMock = mock(RepositorioDePagamentos.class);
		pagamentoArgument = ArgumentCaptor.forClass(Pagamento.class);
		leilaoEncerrado = criaLeilaComLancesDe2000e2500();
	}
	
	@Test
	public void deveGerarPagamentoParaUmLeilaoEncerrado() {
		
		when(leilaoDaoMock.encerrados()).thenReturn(Arrays.asList(leilaoEncerrado));
		
		GeradorDePagamento gerador = new GeradorDePagamento(leilaoDaoMock, pagamentoDaoMock, new Avaliador());
		gerador.gera();
		
		verify(pagamentoDaoMock).salvar(pagamentoArgument.capture());
		
		Pagamento pagamentoGerado = pagamentoArgument.getValue();
		assertEquals(2500.0, pagamentoGerado.getValor(), 0.00001);
	}

	private Leilao criaLeilaComLancesDe2000e2500() {
		return new CriadorDeLeilao()
			.para("Playstation")
			.lance(new Usuario("Jose da Silva"), 2000.0)
			.lance(new Usuario("Maria Pereira"), 2500)
			.constroi();
	}

	@Test
	public void deveEmpurrarParaOProximoDiaUtil() {
		
		Relogio relogio = mock(Relogio.class);
		
		Calendar sabado = Calendar.getInstance();
		sabado.set(2012, Calendar.APRIL, 7);
		
		when(relogio.hoje()).thenReturn(sabado);
		when(leilaoDaoMock.encerrados()).thenReturn(Arrays.asList(leilaoEncerrado));
		
		GeradorDePagamento gerador = new GeradorDePagamento(leilaoDaoMock, pagamentoDaoMock, new Avaliador(), relogio);
		gerador.gera();
		
		verify(pagamentoDaoMock).salvar(pagamentoArgument.capture());
		Pagamento pagamentoGerado = pagamentoArgument.getValue();
		
		assertEquals(Calendar.MONDAY, pagamentoGerado.getData().get(Calendar.DAY_OF_WEEK));
	}
	
	@Test
	public void deveEmpurrarParaProximoDiaUtilQuandoExecutadoNoDomingo() {
		
		Relogio relogio = mock(Relogio.class);
		
		Calendar domingo = Calendar.getInstance();
		domingo.set(2012, Calendar.APRIL, 8);
		
		when(relogio.hoje()).thenReturn(domingo);
		when(leilaoDaoMock.encerrados()).thenReturn(Arrays.asList(leilaoEncerrado));
		
		GeradorDePagamento gerador = new GeradorDePagamento(leilaoDaoMock, pagamentoDaoMock, new Avaliador(), relogio);
		gerador.gera();
		
		verify(pagamentoDaoMock).salvar(pagamentoArgument.capture());
		Pagamento pagamentoGerado = pagamentoArgument.getValue();
		
		assertEquals(Calendar.MONDAY, pagamentoGerado.getData().get(Calendar.DAY_OF_WEEK));
	}
}
