package br.com.fernandonkgw.tdd.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.fernandonkgw.tdd.dominio.Lance;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.dominio.Usuario;

public class CriadorDeLeilao {

	
	private String descricao;
	private Calendar data;
	private List<Lance> lances;
	private boolean encerrado;

	public CriadorDeLeilao() {
		this.data = Calendar.getInstance();
		lances = new ArrayList<>();
	}
	
	public CriadorDeLeilao para(String descricao) {
		this.descricao = descricao;
		return this;
	}

	public CriadorDeLeilao naData(Calendar data) {
		this.data = data;
		return this;
	}
	
	public CriadorDeLeilao lance(Usuario usuario, double valor) {
		lances.add(new Lance(usuario, valor));
		
		return this;
	}

	public CriadorDeLeilao encerrado() {
		this.encerrado = true;
		return this;
	}
	
	public Leilao constroi() {
		Leilao leilao = new Leilao(descricao, data);
		for (Lance lanceDado : lances) leilao.propoe(lanceDado);
		if(encerrado) leilao.encerra();
		
		return leilao;
	}

	public CriadorDeLeilao naDataAntiga() {
		Calendar antiga = Calendar.getInstance();
		antiga.add(Calendar.DAY_OF_MONTH, -10);
		this.data = antiga;
		return this;
	}

	public CriadorDeLeilao emUmaSemanaAtras() {
		Calendar umaSemanaAtras = Calendar.getInstance();
		umaSemanaAtras.add(Calendar.DAY_OF_MONTH, -7);
		this.data = umaSemanaAtras;
		return this;
	}

}
