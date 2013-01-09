package com.researchmobile.gama.entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	private static String usuario;
	private static String clave;
	
	public static String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public static String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
}
