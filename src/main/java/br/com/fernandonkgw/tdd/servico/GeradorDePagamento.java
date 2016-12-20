package br.com.fernandonkgw.tdd.servico;

import java.util.Calendar;
import java.util.List;

import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.dominio.Pagamento;
import br.com.fernandonkgw.tdd.dominio.Relogio;
import br.com.fernandonkgw.tdd.dominio.RelogioDoSistema;
import br.com.fernandonkgw.tdd.infra.dao.RepositorioDeLeiloes;
import br.com.fernandonkgw.tdd.infra.dao.RepositorioDePagamentos;

public class GeradorDePagamento {

	private final RepositorioDeLeiloes leiloes;
	private final RepositorioDePagamentos pagamentos;
	private final Avaliador avaliador;
	private final Relogio relogio;

	public GeradorDePagamento(RepositorioDeLeiloes leiloes,
			RepositorioDePagamentos pagamentos, Avaliador avaliador,
			Relogio relogio) {
		this.leiloes = leiloes;
		this.pagamentos = pagamentos;
		this.avaliador = avaliador;
		this.relogio = relogio;
	}

	public GeradorDePagamento(RepositorioDeLeiloes leiloes, RepositorioDePagamentos pagamentos, Avaliador avaliador) {
		this(leiloes, pagamentos, avaliador, new RelogioDoSistema());
	}
	
	public void gera() {
		
		List<Leilao> leiloesEncerrados = leiloes.encerrados();
		for (Leilao leilao : leiloesEncerrados) {
			avaliador.avalia(leilao);
			
			Pagamento novoPagamento = new Pagamento(avaliador.getMaiorLance(), primeiroDiaUtil());
			pagamentos.salvar(novoPagamento);
		}
	}

	private Calendar primeiroDiaUtil() {
		Calendar data = relogio.hoje();
		int diaDaSemana = data.get(Calendar.DAY_OF_WEEK);
		
		if (diaDaSemana == Calendar.SATURDAY) data.add(Calendar.DAY_OF_MONTH, 2);
		else if (diaDaSemana == Calendar.SUNDAY) data.add(Calendar.DAY_OF_MONTH, 1);
		
		return data;
	}
}
