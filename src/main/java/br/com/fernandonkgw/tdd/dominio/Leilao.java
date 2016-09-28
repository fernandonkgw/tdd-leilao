package br.com.fernandonkgw.tdd.dominio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Leilao {

	private String descricao;
	private Calendar data;
	private List<Lance> lances;
	private boolean encerrado;
	private int id;

	public Leilao(String descricao) {
		this.descricao = descricao;
		this.lances = new ArrayList<Lance>();
	}

	public Leilao(String descricao, Calendar data) {
		this.descricao = descricao;
		this.data = data;
		this.lances = new ArrayList<Lance>();
	}

	public void propoe(Lance lance) {

		if (lances.isEmpty() || podeDarLance(lance.getUsuario())) {
			lances.add(lance);
		}
	}

	private boolean podeDarLance(Usuario usuario) {
		return !ultimoLanceDado().getUsuario().equals(usuario)
				&& qtdeDeLancesDo(usuario) < 5;
	}

	private int qtdeDeLancesDo(Usuario usuario) {
		int total = 0;
		for (Lance l : lances) {
			if (l.getUsuario().equals(usuario)) {
				total++;
			}
		}
		return total;
	}

	public Lance primeiroLanceDado() {
		return lances.get(0);
	}

	public Lance ultimoLanceDado() {
		return lances.get(lances.size() - 1);
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public void dobraLance(Usuario usuario) {
		Lance ultimoLance = ultimoLanceDo(usuario);
		if (ultimoLance != null) {
			propoe(new Lance(usuario, ultimoLance.getValor() * 2));
		}
	}

	private Lance ultimoLanceDo(Usuario usuario) {
		Lance ultimo = null;
		for (Lance lance : this.lances) {
			if (lance.getUsuario().equals(usuario))
				ultimo = lance;
		}
		return ultimo;
	}

	public boolean isEncerrado() {
		return encerrado;
	}

	public void encerra() {
		this.encerrado = true;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEncerrado(boolean encerrado) {
		this.encerrado = encerrado;
	}

}
