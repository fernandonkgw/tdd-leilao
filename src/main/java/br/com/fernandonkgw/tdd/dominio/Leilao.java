package br.com.fernandonkgw.tdd.dominio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;



@Entity
public class Leilao {

	private String descricao;
	private Calendar dataAbertura;
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="leilao")
	private List<Lance> lances = new ArrayList<>();
	private boolean encerrado;
	@Id @GeneratedValue
	private int id;
	@ManyToOne
	private Usuario dono;
	private Double valorInicial;
	private boolean usado;

	public Leilao() {
		this.dataAbertura = Calendar.getInstance();
	}
	
	public Leilao(String descricao, Double valorInicial, Usuario dono, boolean usado) {
		this();
		this.descricao = descricao;
		this.valorInicial = valorInicial;
		this.dono = dono;
		this.usado = usado;
	}	

	public Leilao(String descricao) {
		this.descricao = descricao;
	}

	public Leilao(String descricao, Calendar data) {
		this.descricao = descricao;
		this.dataAbertura = data;
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

	public Calendar getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Calendar dataAbertura) {
		this.dataAbertura = dataAbertura;
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

	public Usuario getDono() {
		return dono;
	}

	public void setDono(Usuario dono) {
		this.dono = dono;
	}

	public Double getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(Double valorInicial) {
		this.valorInicial = valorInicial;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

}
