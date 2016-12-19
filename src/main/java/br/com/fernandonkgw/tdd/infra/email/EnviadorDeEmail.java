package br.com.fernandonkgw.tdd.infra.email;

import br.com.fernandonkgw.tdd.dominio.Leilao;

public interface EnviadorDeEmail {
	
	void envia(Leilao leilao);
}
