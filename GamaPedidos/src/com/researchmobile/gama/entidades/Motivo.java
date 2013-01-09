package com.researchmobile.gama.entidades;

import java.io.Serializable;

public class Motivo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private  String id;
	private  String motivo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
