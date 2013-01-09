package com.researchmobile.gama.entidades;

import java.io.Serializable;

public class Resultado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int error;
	private String mensaje;
	private Cliente[] cliente;
	private Producto[] producto;
	private Motivo[] motivo;
	private EstadoDeCuenta[] estadoCuenta;
	private int semana;
	private String dia;
	private Vendedor vendedor;
	
	
	public Cliente[] getCliente() {
		return cliente;
	}
	public void setCliente(Cliente[] cliente) {
		this.cliente = cliente;
	}
	public int getSemana() {
		return semana;
	}
	public void setSemana(int semana) {
		this.semana = semana;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public Producto[] getProducto() {
		return producto;
	}
	public void setProducto(Producto[] producto) {
		this.producto = producto;
	}
	public Vendedor getVendedor() {
		return vendedor;
	}
	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public EstadoDeCuenta[] getEstadoCuenta() {
		return estadoCuenta;
	}
	public void setEstadoCuenta(EstadoDeCuenta[] estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
	public Motivo[] getMotivo() {
		return motivo;
	}
	public void setMotivo(Motivo[] motivo) {
		this.motivo = motivo;
	}

}
