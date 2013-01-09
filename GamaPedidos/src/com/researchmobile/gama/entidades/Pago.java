package com.researchmobile.gama.entidades;

import java.io.Serializable;

public class Pago implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	private String movDocumento;
	private String noDocumento;
	private float abono;
	private String fecha;
	private String tipoDocumento;
	private int sinc;
	
	
	public String getNoDocumento() {
		return noDocumento;
	}
	public void setNoDocumento(String noDocumento) {
		this.noDocumento = noDocumento;
	}
	public float getAbono() {
		return abono;
	}
	public void setAbono(float abono) {
		this.abono = abono;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setMovDocumento(String movDocumento) {
		this.movDocumento = movDocumento;
	}
	public String getMovDocumento() {
		return movDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setSinc(int sinc) {
		this.sinc = sinc;
	}
	public int getSinc() {
		return sinc;
	}
	
}
