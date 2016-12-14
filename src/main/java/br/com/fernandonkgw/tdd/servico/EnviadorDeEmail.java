package br.com.fernandonkgw.tdd.servico;

import br.com.fernandonkgw.tdd.dominio.Leilao;

public interface EnviadorDeEmail {
	
	void envia(Leilao leilao);
}
