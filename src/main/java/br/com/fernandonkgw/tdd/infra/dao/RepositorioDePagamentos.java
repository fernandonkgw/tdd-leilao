package br.com.fernandonkgw.tdd.infra.dao;

import br.com.fernandonkgw.tdd.dominio.Pagamento;

public interface RepositorioDePagamentos {

	void salvar(Pagamento pagamento);
}
