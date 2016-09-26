package br.com.fernandonkgw.tdd.calendario;

public class AnoBissexto {

	private int ano;

	public AnoBissexto(int ano) {
		this.ano = ano;
	}
	
	public int getAno() {
		return ano;
	}
	
	public boolean isAnoBissexto() {
		if ((this.ano % 4) == 0 && (this.ano % 100) != 0) {
			return true;
		} else if ((this.ano % 400) == 0) {
			return true;
		} else {
			return false;
		}
	}
}
