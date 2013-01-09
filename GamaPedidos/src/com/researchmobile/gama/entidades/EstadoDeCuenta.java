package com.researchmobile.gama.entidades;

import java.io.Serializable;

public class EstadoDeCuenta implements Serializable{
			private static final long serialVersionUID = 1L;
			
		private String codigocliente;
		private String movdocumento;
		private String fecha;
		private String total;
		private String credito;
		private String pagado;
		private String saldo;
		private int cancelado;
		private int sinc;
		
		private Pago[] pago;
		
		public String getCodigocliente() {
			return codigocliente;
		}
		public void setCodigocliente(String codigocliente) {
			this.codigocliente = codigocliente;
		}
		public String getMovdocumento() {
			return movdocumento;
		}
		public void setMovdocumento(String movdocumento) {
			this.movdocumento = movdocumento;
		}
		public String getFecha() {
			return fecha;
		}
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}
		public String getTotal() {
			return total;
		}
		public void setTotal(String total) {
			this.total = total;
		}
		public String getCredito() {
			return credito;
		}
		public void setCredito(String credito) {
			this.credito = credito;
		}
		public String getPagado() {
			return pagado;
		}
		public void setPagado(String pagado) {
			this.pagado = pagado;
		}
		public String getSaldo() {
			return saldo;
		}
		public void setSaldo(String saldo) {
			this.saldo = saldo;
		}
		public void setPago(Pago[] pago) {
			this.pago = pago;
		}
		public Pago[] getPago() {
			return pago;
		}
		public void setSinc(int sinc) {
			this.sinc = sinc;
		}
		public int getSinc() {
			return sinc;
		}
		public void setCancelado(int cancelado) {
			this.cancelado = cancelado;
		}
		public int getCancelado() {
			return cancelado;
		}
		
}
