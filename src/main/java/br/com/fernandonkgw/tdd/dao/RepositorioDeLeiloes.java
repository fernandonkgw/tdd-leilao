package br.com.fernandonkgw.tdd.dao;

import java.util.List;

import br.com.fernandonkgw.tdd.dominio.Leilao;

public interface RepositorioDeLeiloes {

	void salvar(Leilao leilao);
	
	List<Leilao> encerrados();
	
	List<Leilao> correntes();
	
	void atualiza(Leilao leilao);
}
