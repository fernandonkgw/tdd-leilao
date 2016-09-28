package br.com.fernandonkgw.tdd.infra.dao;

import java.util.List;

import br.com.fernandonkgw.tdd.dominio.Leilao;

public interface RepositorioDeLeiloes {

	void salva(Leilao leilao);
	
	List<Leilao> encerrados();
	
	List<Leilao> correntes();
	
	void atualiza(Leilao leilao);
}
