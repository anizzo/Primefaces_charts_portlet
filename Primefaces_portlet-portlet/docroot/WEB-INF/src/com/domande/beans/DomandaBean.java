package com.domande.beans;

import java.io.Serializable;


public class DomandaBean implements Serializable {

	private int idDomanda;
	private String apertura;
	private int valore;
	
	public DomandaBean(int idDomanda, String apertura, int valore) {
		this.idDomanda = idDomanda;
		this.apertura = apertura;
		this.valore = valore;
	}
	
	public int getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(int idDomanda) {
		this.idDomanda = idDomanda;
	}
	public String getApertura() {
		return apertura;
	}
	public void setApertura(String apertura) {
		this.apertura = apertura;
	}
	public int getValore() {
		return valore;
	}
	public void setValore(int valore) {
		this.valore = valore;
	}
	
}
