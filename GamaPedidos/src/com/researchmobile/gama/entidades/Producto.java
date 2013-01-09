package com.researchmobile.gama.entidades;

import java.io.Serializable;

public class Producto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String artCodigo;
	private String artCodigoAlterno;
	private String artDescripcion;
	private float artDescuento1;
	private float artDescuento2;
	private float artDescuento3;
	private float artPrecio;
	private float artCosto;
	private String artUnidadesCaja;
	private String artExistencia;
	private float precioSeleccionado;
	
	private String caja;
	private String unidad;
	
	public String getArtCodigo() {
		return artCodigo;
	}
	public void setArtCodigo(String artCodigo) {
		this.artCodigo = artCodigo;
	}
	public String getArtCodigoAlterno() {
		return artCodigoAlterno;
	}
	public void setArtCodigoAlterno(String artCodigoAlterno) {
		this.artCodigoAlterno = artCodigoAlterno;
	}
	public String getArtDescripcion() {
		return artDescripcion;
	}
	public void setArtDescripcion(String artDescripcion) {
		this.artDescripcion = artDescripcion;
	}
	public String getCaja() {
		return caja;
	}
	public void setCaja(String caja) {
		this.caja = caja;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getArtExistencia() {
		return artExistencia;
	}
	public void setArtExistencia(String artExistencia) {
		this.artExistencia = artExistencia;
	}
	public String getArtUnidadesCaja() {
		return artUnidadesCaja;
	}
	public void setArtUnidadesCaja(String artUnidadesCaja) {
		this.artUnidadesCaja = artUnidadesCaja;
	}
	public float getPrecioSeleccionado() {
		return precioSeleccionado;
	}
	public void setPrecioSeleccionado(float precioSeleccionado) {
		this.precioSeleccionado = precioSeleccionado;
	}
	public float getArtDescuento1() {
		return artDescuento1;
	}
	public void setArtDescuento1(float artDescuento1) {
		this.artDescuento1 = artDescuento1;
	}
	public float getArtDescuento2() {
		return artDescuento2;
	}
	public void setArtDescuento2(float artDescuento2) {
		this.artDescuento2 = artDescuento2;
	}
	public float getArtDescuento3() {
		return artDescuento3;
	}
	public void setArtDescuento3(float artDescuento3) {
		this.artDescuento3 = artDescuento3;
	}
	public float getArtPrecio() {
		return artPrecio;
	}
	public void setArtPrecio(float artPrecio) {
		this.artPrecio = artPrecio;
	}
	public float getArtCosto() {
		return artCosto;
	}
	public void setArtCosto(float artCosto) {
		this.artCosto = artCosto;
	}

}
