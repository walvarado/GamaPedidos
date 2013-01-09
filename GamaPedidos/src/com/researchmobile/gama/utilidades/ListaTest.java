package com.researchmobile.gama.utilidades;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.db.DataBase;
import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.DetallePedido;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.EstadoDeCuenta;
import com.researchmobile.gama.entidades.Pago;
import com.researchmobile.gama.entidades.Producto;

public class ListaTest {
	//ids para tabla Cliente
	public static final int CLIENTE_NOMBRE_COMERCIAL = 0;
	public static final int CLIENTE_NIT = 1;
	public static final int CLIENTE_RAZON_SOCIAL = 2;
	public static final int CLIENTE_TELEFONO = 3;
	public static final int CLIENTE_DIRECCION = 4;
	public static final int CLIENTE_VENDEDOR = 5;
	public static final int CLIENTE_LIMITE_CREDITO = 6;
	public static final int CLIENTE_TIPO_PRECIO = 7;
	public static final int CLIENTE_DIA_VISITA = 8;
	public static final int CLIENTE_SECUENCIA = 9;
	public static final int CLIENTE_FRECUENCIA = 10;
	public static final int CLIENTE_SALDO = 11;
	public static final int CLIENTE_CLIENTE = 12;
	public static final int CLIENTE_NOMBRE_VENDEDOR = 13;
	
	//ids para tabla Producto
	public static final int PRODUCTO_PRODUCTO = 0;
	public static final int PRODUCTO_NOMBRE_PRODUCTO = 1;
	public static final int PRODUCTO_UNIDADES = 2;
	public static final int PRODUCTO_PRECIO_A = 3;
	public static final int PRODUCTO_PRECIO_B = 4;
	public static final int PRODUCTO_PRECIO_C = 5;
	public static final int PRODUCTO_PRECIO_D = 6;
	public static final int PRODUCTO_PRECIO_E = 7;
	public static final int PRODUCTO_PROVEEDOR = 8;
	public static final int PRODUCTO_CATEGORIA = 9;
	public static final int PRODUCTO_NOMBRE_PROVEEDOR = 10;
	
	//Crea lista de clientes
	public ArrayList<HashMap<String, String>> ListaClientes (Cliente[] cliente, int estado){
		int visitado = 0;
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i=0; i<cliente.length; i++){
			visitado = cliente[i].getVisitado();
			
			if(visitado == estado){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("codigoCliente", cliente[i].getCliCodigo());
		        map.put("empresa", cliente[i].getCliEmpresa());
		        map.put("contacto", cliente[i].getCliContacto());
		        map.put("direccion", cliente[i].getCliDireccion());
		        map.put("telefono", cliente[i].getCliTelefono());
		        map.put("nit", "C/F");
		        mylist.add(map);
		    }
			
		}
		return mylist;
		
	}
	
	//Crea lista de clientes de la base de datos
	public ArrayList<HashMap<String, String>> CliestesDB (Context context, int estado) throws Exception{
		int visitado = 0;
		
		DataBase dataBase = new DataBase(context);
		dataBase.abrir();
		Cursor cursor = dataBase.consultaClientes();
		dataBase.cerrar();
		cursor.moveToFirst();
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		
		//for (int i=0; i<cursor.getCount(); i++){
			visitado = Integer.parseInt(cursor.getString(0));
			
			if(visitado == estado){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("codigoCliente", cursor.getString(0));
		        map.put("empresa", cursor.getString(0));
		        map.put("contacto", cursor.getString(0));
		        map.put("direccion", cursor.getString(0));
		        map.put("telefono", cursor.getString(0));
		        map.put("nit", "C/F");
		        mylist.add(map);
		        cursor.moveToNext();
		        
			}
			
		//}
		return mylist;
		
	}	
	
	//Crea lista de productos prueba
	public ArrayList<HashMap<String, String>> ListaProductosPrueba (DetallePedido[] detallePedido){
		FormatDecimal formatDecimal = new FormatDecimal();
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		
		//DetallePedido[] detallePedido = nuevoPedido.getDetallePedido();
		int tamanoPedido = detallePedido.length;
		
		
		for (int i = 0; i < tamanoPedido; i++){
			HashMap<String, String> map = new HashMap<String, String>();
        	map.put("codigoProducto", detallePedido[i].getCodigoProducto());
        	map.put("nombreProducto", detallePedido[i].getDescripcion());
        	map.put("cajas", detallePedido[i].getCaja());
        	map.put("unidades", detallePedido[i].getUnidad());
        	map.put("valor", formatDecimal.ConvierteFloat(detallePedido[i].getPrecio()));
        	map.put("presentacion", detallePedido[i].getUnidadesCaja());
        	map.put("existencia", detallePedido[i].getExistencia());
        	map.put("bonificacion", String.valueOf(detallePedido[i].getPrecio1()));
        	map.put("total", formatDecimal.ConvierteFloat(Float.parseFloat(detallePedido[i].getSubTotal())));
        	mylist.add(map);
		}
		return mylist;
	}
	
	//Crea lista de productos prueba
	public ArrayList<HashMap<String, String>> ListaProductosDetalle (DetallePedido[] detallePedido){
		FormatDecimal formatDecimal = new FormatDecimal();
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		
		int tamanoPedido = detallePedido.length;
		
		for (int i = 0; i < tamanoPedido; i++){
			HashMap<String, String> map = new HashMap<String, String>();
			if (detallePedido[i].getUnidad().equalsIgnoreCase("0") && detallePedido[i].getCaja().equalsIgnoreCase("0")){
				
			}else{
				map.put("codigoProducto", detallePedido[i].getCodigoProducto());
	        	map.put("nombreProducto", detallePedido[i].getDescripcion());
	        	map.put("cajas", detallePedido[i].getCaja());
	        	map.put("unidades", detallePedido[i].getUnidad());
	        	map.put("valor", formatDecimal.ConvierteFloat(detallePedido[i].getPrecio()));
	        	map.put("presentacion", detallePedido[i].getUnidadesCaja());
	        	map.put("existencia", detallePedido[i].getExistencia());
	        	map.put("bonificacion", String.valueOf(detallePedido[i].getPrecio1()));
	        	map.put("total", formatDecimal.ConvierteFloat(Float.parseFloat(detallePedido[i].getSubTotal())));
	        	mylist.add(map);
			}
        	
		}
		return mylist;
	}
	
	//Crea lista de estados de cuenta
	//Crea lista de clientes
	public ArrayList<HashMap<String, String>> ListaEstadosDeCuenta (EstadoDeCuenta[] estado){
		FormatDecimal formatDecimal = new FormatDecimal();
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i=0; i<estado.length; i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("movDocumento", estado[i].getMovdocumento());
	        map.put("fecha", estado[i].getFecha());
	        map.put("total", formatDecimal.ConvierteFloat(Float.parseFloat(estado[i].getTotal())));
	        map.put("pagado", formatDecimal.ConvierteFloat(Float.parseFloat(estado[i].getPagado())));
	        map.put("saldo", formatDecimal.ConvierteFloat(Float.parseFloat(estado[i].getSaldo())));
	        mylist.add(map);
		}
		return mylist;
	}
	
public ArrayList<HashMap<String, String>> ListaPagos (Pago[] pago){
		FormatDecimal formatDecimal = new FormatDecimal();
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i=0; i<pago.length; i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("noDocumento", pago[i].getNoDocumento());
			map.put("tipoDocumento", pago[i].getTipoDocumento());
	        map.put("fecha", pago[i].getFecha());
	        map.put("abono", formatDecimal.ConvierteFloat(pago[i].getAbono()));
	        
	        mylist.add(map);
		}
		return mylist;
	}


	//Crear lista de Productos
	public ArrayList<HashMap<String, String>> ListaProductos(Producto[] producto) {
		FormatDecimal formatDecimal = new FormatDecimal();
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < producto.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("nombre", producto[i].getArtDescripcion());
			map.put("precio", formatDecimal.ConvierteFloat(producto[i].getArtPrecio()));
			map.put("existencia", producto[i].getArtExistencia());
			mylist.add(map);
		}
		return mylist;
	}
	
	//Crear lista de Pedidos realizados en el día
	public ArrayList<HashMap<String, String>> ListaPedidos(Context context) {
		ConexionDB conexionDb = new ConexionDB();
		
		EncabezadoPedido pedido[] = conexionDb.VerPedidos(context);
		FormatDecimal formatDecimal = new FormatDecimal();
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < pedido.length; i++) {
			float totalTemp = pedido[i].getTotal();
			if (totalTemp > 0){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("pedido", pedido[i].getCodigoPedidoTemp());
				map.put("codigo", pedido[i].getCodigoCliente());
				map.put("total", formatDecimal.ConvierteFloat(pedido[i].getTotal()));
				map.put("hora", pedido[i].getHora());
				mylist.add(map);
			}
		}
		return mylist;
	}
	
	//Crear lista de Clientes visitados en el día
	public ArrayList<HashMap<String, String>> ListaClientesVisitados(Context context) {
		ConexionDB conexionDb = new ConexionDB();
		
		EncabezadoPedido pedido[] = conexionDb.VerPedidos(context);
		FormatDecimal formatDecimal = new FormatDecimal();
		String estado = "SIN PEDIDO";
		float totalTemp = 0;
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < pedido.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("cliente", pedido[i].getCodigoCliente());
			totalTemp = pedido[i].getTotal();
			if (totalTemp > 0){
				map.put("total", String.valueOf(pedido[i].getTotal()));
			}else{
				map.put("total", estado);
			}
			
			map.put("hora", pedido[i].getHora());
			mylist.add(map);
		}
		return mylist;
	}
}
