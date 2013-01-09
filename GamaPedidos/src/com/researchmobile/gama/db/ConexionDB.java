package com.researchmobile.gama.db;

import android.content.Context;
import android.database.Cursor;

import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.DetallePedido;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.EstadoDeCuenta;
import com.researchmobile.gama.entidades.Motivo;
import com.researchmobile.gama.entidades.Pago;
import com.researchmobile.gama.entidades.Producto;
import com.researchmobile.gama.entidades.Vendedor;
import com.researchmobile.gama.utilidades.FormatDecimal;

public class ConexionDB {
	
	public void LimpiaDB (Context context){
		DataBase objeto = new DataBase(context);
		try {
			objeto.abrir();
			objeto.limpiarDB();
			objeto.cerrar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void LimpiaClienteDB (Context context){
		DataBase objeto = new DataBase(context);
		try {
			objeto.abrir();
			objeto.limpiarDB();
			objeto.cerrar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void limpiaPedidoDB (Context context){
		DataBase objeto = new DataBase(context);
		try{
			objeto.abrir();
			objeto.limpiaPedidoDB();
			objeto.cerrar();
		}catch(Exception exception){
			
		}
	}
	
	public void GuardaProductos(Producto[] producto, Context context){
		final DataBase objeto = new DataBase(context);
		final Producto[] productos = producto.clone();		
		try {
			objeto.abrir();
			for (int i = 0; i < productos.length; i++){
				
				objeto.insertProducto(productos[i].getArtCodigo(),
									  productos[i].getArtCodigoAlterno(), 
									  productos[i].getArtDescripcion(),
									  String.valueOf(productos[i].getArtDescuento1()), 
									  String.valueOf(productos[i].getArtDescuento2()), 
									  String.valueOf(productos[i].getArtDescuento3()), 
									  String.valueOf(productos[i].getArtPrecio()),
									  String.valueOf(productos[i].getArtCosto()),
									  productos[i].getArtUnidadesCaja(),
									  productos[i].getArtExistencia()
									  );
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}
		objeto.cerrar();
	}
	
	public void GuardaClientes(Cliente[] cliente, Context context){
		final DataBase objeto = new DataBase(context);
		final Cliente[] clientes = cliente.clone();
		try{
			objeto.abrir();
			for (int i = 0; i < clientes.length; i++){
						objeto.insertCliente(clientes[i].getCliCodigo(), 
											 clientes[i].getCliEmpresa(),
											 clientes[i].getCliContacto(), 
											 clientes[i].getCliDireccion(), 
											 clientes[i].getCliTelefono(), 
											 clientes[i].getCliDiasCredito(), 
											 clientes[i].getCliLimite(), 
											 clientes[i].getCliSaldo(), 
											 clientes[i].getCliDescuento1(), 
											 clientes[i].getCliDescuento2(), 
											 clientes[i].getCliDescuento3(), 
											 clientes[i].getSaldo(),
											 clientes[i].getVisitado(),
											 clientes[i].getCliNit());
				
			}
			objeto.cerrar();
		}catch(Exception exception){
			
		}
	}

	public void GuardaVendedor(Vendedor vendedor, Context context) {
		final DataBase objeto = new DataBase(context);
		final Vendedor vendedores = vendedor;
		
		try {
			objeto.abrir();
				objeto.insertVendedor(vendedores.getVendedor(),
									  vendedores.getNombre(), 
									  vendedores.getDireccion(),
									  vendedores.getTelefono(),
									  vendedores.getIdentificacion(),
									  vendedores.getComision(), 
									  vendedores.getRuta());
			objeto.cerrar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void GuardaMotivos(Motivo[] motivo, Context context){
		final DataBase objeto = new DataBase(context);
		final Motivo[] motivos = motivo.clone();
		
		try{
			objeto.abrir();
			for (int i = 0; i < motivos.length; i++){
				objeto.insertMotivos(motivo[i].getId(), motivo[i].getMotivo());
			}
			objeto.cerrar();
			
		}catch(Exception exception){
			
		}
	}

	public void GuardaEstadoDeCuenta(EstadoDeCuenta[] estado, Context context){
		final DataBase objeto = new DataBase(context);
		final EstadoDeCuenta[] estados = estado.clone();
		
			try {
				objeto.abrir();
				for (int i=0; i < estados.length; i++){
					
					objeto.insertEstadosDeCuenta(estados[i].getCodigocliente(),
												 estados[i].getMovdocumento(), 
												 estados[i].getFecha(), 
												 estados[i].getTotal(), 
												 estados[i].getCredito(), 
												 estados[i].getPagado(), 
												 estados[i].getSaldo(),
												 String.valueOf(estados[i].getCancelado()),
												 String.valueOf(estados[i].getSinc())
												 );
				}
				objeto.cerrar();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void GuardaPago(Pago[] pago, Context context){
		final DataBase objeto = new DataBase(context);
		final Pago[] pagos = pago.clone();
		FormatDecimal formatDecimal = new FormatDecimal();
		
			try {
				objeto.abrir();
				for (int i=0; i < pagos.length; i++){
					
					//objeto.insertPago(correlativo, movDocumento, noDocumento, abono, fecha, tipoDocumento)
					objeto.insertPago(
												 pagos[i].getMovDocumento(), 
												 pagos[i].getNoDocumento(), 
												 formatDecimal.ConvierteFloat(pagos[i].getAbono()), 
												 pagos[i].getFecha(), 
												 pagos[i].getTipoDocumento(),
												 "0");
				}
				objeto.cerrar();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public String GuardarMotivo(Context context, String usuario, String password, String fecha, String hora, Motivo motivo, String idCliente){
		final DataBase objeto = new DataBase(context);
		String cheque = "0";
		String efectivo = "0";
		String credito = "0";
		String sinc = "0";
		try {
			objeto.abrir();
			objeto.insertPedido(idCliente, 
					            fecha, 
					            hora, 
					            "0", 
					            efectivo, 
					            cheque, 
					            credito, 
					            sinc, 
					            "1", 
					            motivo.getId(), 
					            motivo.getMotivo());
			
		}catch(Exception exception){
			
		}
		
		
		return "1";
	}

	public String GuardaEncabezadoPedido(EncabezadoPedido encabezado, Context context, String sinc){
		final DataBase objeto = new DataBase(context);
		
		try {
			objeto.abrir();
			objeto.insertPedido(encabezado.getCodigoCliente(), 	
								encabezado.getFecha(), 
								encabezado.getHora(), 
								String.valueOf(encabezado.getTotal()), 
								encabezado.getEfectivo(), 
								encabezado.getCheque(), 
								encabezado.getCredito(),
								sinc, 
								"0", 
								"0", 
								"0"
								);
			
			objeto.cerrar();
			return "0";
			
		} catch (Exception e) {
			System.out.println("Error guardando encabezado");
			return "1";
		}
	}
	
	public String VerNumeroPedido(Context context){
		final DataBase objeto = new DataBase(context);
		Cursor cursor = null;
		String result = "";
		try {
			objeto.abrir();
			cursor = objeto.consultaPedidos();
			cursor.moveToLast();
			
			result = cursor.getString(0);
			System.out.println("buscando numero de pedido, -- " + result);
			objeto.cerrar();
			return result;
			
		} catch (Exception e) {
			System.out.println("Error guardando encabezado");
			return result;
		}
	}
	
	public DetallePedido[] BuscaDetallePedido(Context context, String codigoPedido){
		final DataBase objeto = new DataBase(context);
		Cursor cursor = null;
		DetallePedido[] detalle = null;
		
		try {
			objeto.abrir();
			cursor = objeto.consultaDetallePedidos(codigoPedido);
			objeto.cerrar();
			int tamano = cursor.getCount();
			System.out.println("tamaño de cursor de detalle = " + tamano);
			//System.out.println("artículo agregado = " + cursor.getString(columnIndex));
			detalle = new DetallePedido[tamano];
			cursor.moveToFirst();
			for (int i = 0; i < tamano; i++){
				DetallePedido detalleTemp = new DetallePedido();
				detalleTemp.setCodigoPedido(cursor.getString(1));
				detalleTemp.setCodigoProducto(cursor.getString(0));
				detalleTemp.setPrecioSeleccionado(Float.parseFloat(cursor.getString(3)));
				detalleTemp.setTotalUnidades(Integer.parseInt(cursor.getString(2)));
				detalleTemp.setSubTotal(cursor.getString(11));
				detalle[i] = detalleTemp;
				System.out.println("artículo agregado = " + detalleTemp.getCodigoPedido());
				cursor.moveToNext();
			}
			return detalle;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void actualizaCliente(Context context, String codigoCliente){
		final DataBase objeto = new DataBase(context);
		try {
			objeto.abrir();
			objeto.actualizarCliente(codigoCliente);
			objeto.cerrar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actualizaEncabezadoPedido(Context context, String codigoPedido){
		final DataBase objeto = new DataBase(context);
		try {
			objeto.abrir();
			objeto.actualizarPedido(codigoPedido);
			objeto.cerrar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public EncabezadoPedido[] VerPedidos(Context context){
		final DataBase objeto = new DataBase(context);
		Cursor cursor = null;
		EncabezadoPedido[] pedidos = null;
		
		try {
			objeto.abrir();
			cursor = objeto.consultaPedidos();
			objeto.cerrar();
			
			int tamano = cursor.getCount();
			
			if (tamano > 0){
				pedidos = new EncabezadoPedido[tamano];
				cursor.moveToFirst();
				
				for (int i = 0; i < tamano; i++){
					EncabezadoPedido pedidoTemp = new EncabezadoPedido();
					pedidoTemp.setCodigoPedidoTemp(cursor.getString(0));
					pedidoTemp.setCodigoCliente(cursor.getString(1));
					pedidoTemp.setTotal(Float.parseFloat(cursor.getString(4)));
					pedidoTemp.setHora(cursor.getString(3));
					pedidoTemp.setSinc(Integer.parseInt(cursor.getString(8)));
					pedidos[i] = pedidoTemp;
					cursor.moveToNext();
				}
				return pedidos;
			}else{
				return null;
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pedidos;
	}
	
	public DetallePedido[] verDetalle(Context context, String pedido){
		final DataBase objeto = new DataBase(context);
		Cursor cursor = null;
		DetallePedido[] detalle = null;
		
		try {
			objeto.abrir();
			cursor = objeto.consultaDetallePedidos(pedido);
			objeto.cerrar();
			System.out.println("CONEXIONDB, artículos = " + cursor.getCount());
			int tamano = cursor.getCount();
			detalle = new DetallePedido[tamano];
			cursor.moveToFirst();
			
			for (int i = 0; i < tamano; i++){
				DetallePedido detalleTemp = new DetallePedido();
				//detalleTemp.set
				detalleTemp.setCodigoProducto(cursor.getString(0));System.out.println("CONEXIONDB, ver codigo artículo");
				detalleTemp.setDescripcion(cursor.getString(5));
				detalleTemp.setCaja(cursor.getString(6));
				detalleTemp.setUnidad(cursor.getString(7));
				detalleTemp.setPrecioSeleccionado(Float.parseFloat(cursor.getString(3)));System.out.println("CONEXIONDB, ver precio artículo");
				detalleTemp.setUnidadesCaja(cursor.getString(2));
				detalleTemp.setExistencia(cursor.getString(9));
				detalleTemp.setTotalUnidades(Integer.parseInt(cursor.getString(2)));System.out.println("CONEXIONDB, ver total unidades artículo");
				detalleTemp.setCodigoPedido(cursor.getString(1));System.out.println("CONEXIONDB, ver codigo pgedido artículo");
				detalleTemp.setBonificacion(cursor.getString(10));
				detalleTemp.setSubTotal(cursor.getString(11));
				detalle[i] = detalleTemp;
				cursor.moveToNext();
			}
			return detalle;
		}catch (Exception e){
			
		}
		return detalle;
		
	}
	
	@SuppressWarnings("unused")
	public String GuardaDetallePedido(DetallePedido[] detalle, String numeroPedido, Context context){
		final DataBase objeto = new DataBase(context);
		final DetallePedido[] detalles = detalle.clone();
		
		try {
			objeto.abrir();
			for (int i =0; i < detalles.length; i++){
				if (detalles[i].getTotalUnidades() > 0){	
				objeto.insertDetallePedido(detalles[i].getCodigoProducto(),
										   numeroPedido, 
										   String.valueOf(detalles[i].getTotalUnidades()), 
										   String.valueOf(detalles[i].getPrecio()),
										   "0", 
										   detalles[i].getDescripcion(), 
										   detalles[i].getCaja(),
										   detalles[i].getUnidad(), 
										   detalles[i].getPresentacion(),
										   detalles[i].getExistencia(),
										   detalles[i].getBonificacion(),
										   detalles[i].getSubTotal());
				}
			}
			objeto.cerrar();
			return "0";
			
		
		} catch (Exception e) {
			System.out.println("Error guardando detalle pedido");
			return "1";
		}
	}
}

