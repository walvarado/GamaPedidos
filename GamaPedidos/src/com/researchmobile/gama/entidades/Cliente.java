package com.researchmobile.gama.entidades;

import java.io.Serializable;

public class Cliente implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cliCodigo;
	private String cliEmpresa;
	private String cliContacto;
	private String cliDireccion;
	private String cliTelefono;
	private String cliDiasCredito;
	private String cliLimite;
	private String cliSaldo;
	private String cliDescuento1;
	private String cliDescuento2;
	private String cliDescuento3;
	private String Saldo;
	private String cliNit;
	private int descuentos;
	private int visitado;
	private EstadoDeCuenta[] estadoCuentas;
	public String getCliCodigo() {
		return cliCodigo;
	}
	public void setCliCodigo(String cliCodigo) {
		this.cliCodigo = cliCodigo;
	}
	public String getCliEmpresa() {
		return cliEmpresa;
	}
	public void setCliEmpresa(String cliEmpresa) {
		this.cliEmpresa = cliEmpresa;
	}
	public String getCliContacto() {
		return cliContacto;
	}
	public void setCliContacto(String cliContacto) {
		this.cliContacto = cliContacto;
	}
	public String getCliDireccion() {
		return cliDireccion;
	}
	public void setCliDireccion(String cliDireccion) {
		this.cliDireccion = cliDireccion;
	}
	public String getCliTelefono() {
		return cliTelefono;
	}
	public void setCliTelefono(String cliTelefono) {
		this.cliTelefono = cliTelefono;
	}
	public String getCliDiasCredito() {
		return cliDiasCredito;
	}
	public void setCliDiasCredito(String cliDiasCredito) {
		this.cliDiasCredito = cliDiasCredito;
	}
	public String getCliLimite() {
		return cliLimite;
	}
	public void setCliLimite(String cliLimite) {
		this.cliLimite = cliLimite;
	}
	public String getCliSaldo() {
		return cliSaldo;
	}
	public void setCliSaldo(String cliSaldo) {
		this.cliSaldo = cliSaldo;
	}
	public String getCliDescuento1() {
		return cliDescuento1;
	}
	public void setCliDescuento1(String cliDescuento1) {
		this.cliDescuento1 = cliDescuento1;
	}
	public String getCliDescuento2() {
		return cliDescuento2;
	}
	public void setCliDescuento2(String cliDescuento2) {
		this.cliDescuento2 = cliDescuento2;
	}
	public String getCliDescuento3() {
		return cliDescuento3;
	}
	public void setCliDescuento3(String cliDescuento3) {
		this.cliDescuento3 = cliDescuento3;
	}
	public String getSaldo() {
		return Saldo;
	}
	public void setSaldo(String saldo) {
		Saldo = saldo;
	}
	public EstadoDeCuenta[] getEstadoCuentas() {
		return estadoCuentas;
	}
	public void setEstadoCuentas(EstadoDeCuenta[] estadoCuentas) {
		this.estadoCuentas = estadoCuentas;
	}
	public int getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(int descuentos) {
		this.descuentos = descuentos;
	}
	public String getCliNit() {
		return cliNit;
	}
	public void setCliNit(String cliNit) {
		this.cliNit = cliNit;
	}
	public int getVisitado() {
		return visitado;
	}
	public void setVisitado(int visitado) {
		this.visitado = visitado;
	}
	
}
