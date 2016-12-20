package br.com.fernandonkgw.tdd.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.fernandonkgw.tdd.dominio.Lance;
import br.com.fernandonkgw.tdd.dominio.Leilao;
import br.com.fernandonkgw.tdd.dominio.Usuario;

public class LeilaoBuilder {

	
	private String descricao;
	private Calendar data;
	private List<Lance> lances;
	private boolean encerrado;
	private boolean usado;
	private Double valorInicial;
	private  Usuario dono;

	public LeilaoBuilder() {
		this.data = Calendar.getInstance();
		lances = new ArrayList<>();
	}
	
	public LeilaoBuilder para(String descricao) {
		this.descricao = descricao;
		return this;
	}

	public LeilaoBuilder naData(Calendar data) {
		this.data = data;
		return this;
	}
	
	public LeilaoBuilder lance(Usuario usuario, double valor) {
		lances.add(new Lance(usuario, valor));
		
		return this;
	}

	public LeilaoBuilder encerrado() {
		this.encerrado = true;
		return this;
	}
	
	public Leilao build() {
		Leilao leilao = new Leilao(descricao, valorInicial, dono, usado, data);
		for (Lance lanceDado : lances) leilao.propoe(lanceDado);
		if(encerrado) leilao.encerra();
		
		return leilao;
	}

	public LeilaoBuilder naDataAntiga() {
		Calendar antiga = Calendar.getInstance();
		antiga.add(Calendar.DAY_OF_MONTH, -10);
		this.data = antiga;
		return this;
	}

	public LeilaoBuilder emUmaSemanaAtras() {
		Calendar umaSemanaAtras = Calendar.getInstance();
		umaSemanaAtras.add(Calendar.DAY_OF_MONTH, -7);
		this.data = umaSemanaAtras;
		return this;
	}

	public LeilaoBuilder novo() {
		this.usado = false;
		return this;
	}
	
	public LeilaoBuilder comValorInicial(Double valorInicial) {
		this.valorInicial = valorInicial;
		return this;
	}
	
	public LeilaoBuilder dono(Usuario dono) {
		this.dono = dono;
		return this;
	}
}
