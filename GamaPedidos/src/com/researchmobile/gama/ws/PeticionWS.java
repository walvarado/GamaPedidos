package com.researchmobile.gama.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.db.DataBase;
import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.DetallePedido;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.EstadoDeCuenta;
import com.researchmobile.gama.entidades.Fecha;
import com.researchmobile.gama.entidades.Motivo;
import com.researchmobile.gama.entidades.NuevoPedido;
import com.researchmobile.gama.entidades.Producto;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.entidades.Vendedor;
import com.researchmobile.gama.utilidades.ConnectState;
import com.researchmobile.gama.utilidades.Conversion;

public class PeticionWS {
	private static String WS = "json.php?";
	private static String ACTION_PEDIDO = "pedido";
	private static String ACTION_CATALOGO = "catalogo";
	private static String ACTION_MOTIVO = "noventa";
	private static int LIMITE_FACTURA = 10;
	
	//private static String URL = "ServicesTransferBI/usuario?";
	
	//columnas de tabla Cliente
	public static final int CLIENTE_CODIGO = 0;
	public static final int CLIENTE_EMPRESA = 1;
	public static final int CLIENTE_CONTACTO = 2;
	public static final int CLIENTE_DIRECCION = 3;
	public static final int CLIENTE_TELEFONO = 4;
	public static final int CLIENTE_DIAS_CREDITO = 5;
	public static final int CLIENTE_LIMITE = 6;
	public static final int CLIENTE_CLISALDO = 7;
	public static final int CLIENTE_DESCUENTO1 = 8;
	public static final int CLIENTE_DESCUENTO2 = 9;
	public static final int CLIENTE_DESCUENTO3 = 10;
	public static final int CLIENTE_SALDO = 11;
	public static final int CLIENTE_VISITADO = 12;
	public static final int CLIENTE_NIT = 13;
	
	//columnas de tabla Producto
	public static final int PRODUCTO_CODIGO = 0;
	public static final int PRODUCTO_CODIGO_ALTERNO = 1;
	public static final int PRODUCTO_DESCRIPCION = 2;
	public static final int PRODUCTO_DESCUENTO1 = 3;
	public static final int PRODUCTO_DESCUENTO2 = 4;
	public static final int PRODUCTO_DESCUENTO3 = 5;
	public static final int PRODUCTO_PRECIO = 6;
	public static final int PRODUCTO_COSTO = 7;
	public static final int PRODUCTO_UNIDADES_CAJA = 8;
	public static final int PRODUCTO_EXISTENCIA = 9;
	
	Cliente cliente[] = null;
	private Resultado resultado;
	ConnectState connectState = new ConnectState();
	
	JSONObject jsonObject;
	
	ConexionDB conexionDB;
	

	@SuppressWarnings("null")
	public Resultado LoginWS (String usuario, String clave, Context context){
		setResultado(new Resultado());
		if (connectState.isConnectedToInternet(context)) {
			if (BuscaClientesWS(usuario, clave, context)) {
				return getResultado();
			}else{
				dalosLocales(usuario, clave, context);
				return getResultado();
			}
		}else {
			dalosLocales(usuario, clave, context);
			//System.out.println(getResultado().getCliente()[3].getCliCodigo());
			return getResultado();
		}
	}
	
	private boolean VerificaLoginDB(String usuario, String clave, Context context) {
		final DataBase objeto2 = new DataBase(context);
		try {
			Fecha fecha = new Fecha();
			String hoy = fecha.Dia();
			
			objeto2.abrir();
			Cursor cursor = objeto2.consultaUsuario(usuario, clave);
			objeto2.cerrar();
			System.out.println("tamaño de cursor " + cursor.getCount());
			System.out.println("cursor : " + cursor);
			System.out.println(cursor.getColumnName(0));
			System.out.println(cursor.getColumnName(1));
			System.out.println(cursor.getColumnName(2));
			//System.out.println("algo" + cursor.getString(1));
			
			if (cursor.getCount() != 0){
				cursor.moveToFirst();
				
				String usuarioDB = cursor.getString(0);
				String claveDB = cursor.getString(1);
				String diaDB = cursor.getString(2);
				System.out.println(cursor.getString(2) + " = " + hoy);
				
				if (diaDB.equalsIgnoreCase(hoy)){
					if (usuario.equalsIgnoreCase(usuarioDB) && clave.equalsIgnoreCase(claveDB)){
						System.out.println("LOGIN, PETICIONWS, el usaurio ya esta logueado");
						return true;
					}else{
						return false;
					}
					
				}else{
					objeto2.abrir();
					objeto2.limpiaUsuario();
					objeto2.insertUsuario(usuario, clave, hoy);
					objeto2.limpiaPedidoDB();
					objeto2.cerrar();
					System.out.println("LOGIN, PETICIONWS, Usuario Guradado aaaa -- " + usuario + " " + clave + " " + hoy);
					
					return true;
				}
				
			}else{
				System.out.println("no hay usuario");
				objeto2.abrir();
				objeto2.limpiaUsuario();
				objeto2.insertUsuario(usuario, clave, hoy);
				objeto2.limpiaPedidoDB();
				objeto2.cerrar();
				System.out.println("LOGIN, PETICIONWS, Usuario Guradado bbbbb , " +  usuario + " " + clave + " " + hoy);
				return true;
			}
		}catch (Exception exception){
			System.out.println("Usuario Guradado ccccc");
			objeto2.cerrar();
			return false;
		}
		
	}

	private void dalosLocales(String usuario, String clave, Context context) {
		if (!VerificaLoginDB(usuario, clave, context)){
			getResultado().setError(1);
			getResultado().setMensaje("ERROR DE LOGIN");
		}else{
			try{
				cargarClientesDB(context);
				cargarProductosDB(context);
				cargaMotivosDB(context);
				cargaVendedorDB(context);
			}catch(Exception exception){
				System.out.println("PETICIONWS, 160. POSIBLEMENTE NO HAY DATOS EN LA DB");
			}
			
		}
	}

	private void cargaVendedorDB(Context context) {
		DataBase dataBase = new DataBase(context);
		
		try {
			
			dataBase.abrir();
			System.out.println("PETICIONWS, vendedor");
			Cursor cursor = dataBase.consultaVendedor();
			dataBase.cerrar();
			
			Vendedor vendedor = new Vendedor();
			cursor.moveToFirst();
			vendedor.setVendedor(cursor.getString(0));
			vendedor.setNombre(cursor.getString(1));
			vendedor.setDireccion(cursor.getString(2));
			vendedor.setTelefono(cursor.getString(3));
			vendedor.setIdentificacion(cursor.getString(4));
			vendedor.setComision(cursor.getString(5));
			vendedor.setRuta(cursor.getString(6));
			getResultado().setVendedor(vendedor);
			
		} catch (Exception e) {
			System.out.println("no carga nada");
			System.out.println(e);
			dataBase.cerrar();
			e.printStackTrace();
		}
		
	}

	private void cargaMotivosDB(Context context) {
		DataBase dataBase = new DataBase(context);
		
		try {
			
			dataBase.abrir();
			System.out.println("PETICIONWS, motivos");
			Cursor cursor = dataBase.consultaMotivos();
			
			
			int tamano = cursor.getCount();
			
			Motivo motivos[] = new Motivo[tamano];
			int contador = 0;
			
			cursor.moveToFirst();
			Motivo motivoTemp = new Motivo();
			motivoTemp.setId(cursor.getString(0));
			motivoTemp.setMotivo(cursor.getString(1));
			
			motivos[contador] = motivoTemp;
			contador++;
			
			while (cursor.moveToNext()) {
				Motivo motivoTemp2 = new Motivo();
				motivoTemp2.setId(cursor.getString(0));
				motivoTemp2.setMotivo(cursor.getString(1));
				motivos[contador] = motivoTemp2;
				contador++;
			}
			dataBase.cerrar();
			
			getResultado().setMotivo(motivos);
			
			//setCliente(clientes);
		} catch (Exception e) {
			System.out.println("no carga nada");
			System.out.println(e);
			dataBase.cerrar();
			e.printStackTrace();
		}
		
	}

	private void cargarProductosDB(Context context) {
		DataBase dataBase = new DataBase(context);
		
		try {
			
			dataBase.abrir();
			System.out.println("ooooooo");
			Cursor cursor = dataBase.consultaProducto();
			
			
			int tamano = cursor.getCount();
			Producto productos[] = new Producto[tamano];
			int contador = 0;
			
			cursor.moveToFirst();
			Producto productoTemp = new Producto();
			productoTemp.setArtCodigo(cursor.getString(PRODUCTO_CODIGO));
			productoTemp.setArtCodigoAlterno(cursor.getString(PRODUCTO_CODIGO_ALTERNO));
			productoTemp.setArtDescripcion(cursor.getString(PRODUCTO_DESCRIPCION));
			productoTemp.setArtDescuento1(Float.parseFloat(cursor.getString(PRODUCTO_DESCUENTO1)));
			productoTemp.setArtDescuento2(Float.parseFloat(cursor.getString(PRODUCTO_DESCUENTO2)));
			productoTemp.setArtDescuento3(Float.parseFloat(cursor.getString(PRODUCTO_DESCUENTO3)));
			productoTemp.setArtPrecio(Float.parseFloat(cursor.getString(PRODUCTO_PRECIO)));
			productoTemp.setArtCosto(Float.parseFloat(cursor.getString(PRODUCTO_COSTO)));
			productoTemp.setArtExistencia(cursor.getString(PRODUCTO_EXISTENCIA));
			productoTemp.setArtUnidadesCaja(cursor.getString(PRODUCTO_UNIDADES_CAJA));
			productoTemp.setCaja("0");
			productoTemp.setUnidad("0");
			productos[contador] = productoTemp;
			contador++;
			
			while (cursor.moveToNext()) {
				Producto productoTemp2 = new Producto();
				productoTemp2.setArtCodigo(cursor.getString(PRODUCTO_CODIGO));
				productoTemp2.setArtCodigoAlterno(cursor.getString(PRODUCTO_CODIGO_ALTERNO));
				productoTemp2.setArtDescripcion(cursor.getString(PRODUCTO_DESCRIPCION));
				productoTemp2.setArtDescuento1(Float.parseFloat(cursor.getString(PRODUCTO_DESCUENTO1)));
				productoTemp2.setArtDescuento2(Float.parseFloat(cursor.getString(PRODUCTO_DESCUENTO2)));
				productoTemp2.setArtDescuento3(Float.parseFloat(cursor.getString(PRODUCTO_DESCUENTO3)));
				productoTemp2.setArtPrecio(Float.parseFloat(cursor.getString(PRODUCTO_PRECIO)));
				productoTemp2.setArtCosto(Float.parseFloat(cursor.getString(PRODUCTO_COSTO)));
				productoTemp2.setArtExistencia(cursor.getString(PRODUCTO_EXISTENCIA));
				productoTemp2.setArtUnidadesCaja(cursor.getString(PRODUCTO_UNIDADES_CAJA));
				productoTemp2.setCaja("0");
				productoTemp2.setUnidad("0");
				productos[contador] = productoTemp2;
				contador++;
			}
			dataBase.cerrar();
			
			getResultado().setProducto(productos);
			
			//setCliente(clientes);
		} catch (Exception e) {
			System.out.println("no carga nada");
			System.out.println(e);
			dataBase.cerrar();
			e.printStackTrace();
		}
	}

	public void cargarClientesDB(Context context) {
		DataBase dataBase = new DataBase(context);
		try {
			dataBase.abrir();
			Cursor cursor = dataBase.consultaClientes();
			
			int tamano = cursor.getCount();
			Cliente clientes[] = new Cliente[tamano];
			int contador = 0;

			cursor.moveToFirst();
			Cliente clienteTemp = new Cliente();
			//clienteTemp.setCliCodigo(String.valueOf(contador));
			clienteTemp.setCliCodigo(cursor.getString(CLIENTE_CODIGO));
			clienteTemp.setCliEmpresa(cursor.getString(CLIENTE_EMPRESA));
			clienteTemp.setCliContacto(cursor.getString(CLIENTE_CONTACTO));
			clienteTemp.setCliDireccion(cursor.getString(CLIENTE_DIRECCION));
			
			clienteTemp.setCliTelefono(cursor.getString(CLIENTE_TELEFONO));
			clienteTemp.setCliDiasCredito(cursor.getString(CLIENTE_DIAS_CREDITO));
			clienteTemp.setCliLimite(cursor.getString(CLIENTE_LIMITE));
			clienteTemp.setCliSaldo(cursor.getString(CLIENTE_CLISALDO));
			clienteTemp.setCliDescuento1(cursor.getString(CLIENTE_DESCUENTO1));
			clienteTemp.setCliDescuento2(cursor.getString(CLIENTE_DESCUENTO2));
			clienteTemp.setCliDescuento3(cursor.getString(CLIENTE_DESCUENTO3));
			clienteTemp.setSaldo(cursor.getString(CLIENTE_SALDO));
			clienteTemp.setVisitado(cursor.getInt(CLIENTE_VISITADO));
			clienteTemp.setCliNit(cursor.getString(CLIENTE_NIT));
			clientes[contador] = clienteTemp;
			contador++;
			
			while (cursor.moveToNext()) {
				Cliente clienteTemp2 = new Cliente();
				clienteTemp2.setCliCodigo(cursor.getString(CLIENTE_CODIGO));
				//clienteTemp2.setCliCodigo(String.valueOf(contador));
				clienteTemp2.setCliEmpresa(cursor.getString(CLIENTE_EMPRESA));
				clienteTemp2.setCliContacto(cursor.getString(CLIENTE_CONTACTO));
				clienteTemp2.setCliDireccion(cursor.getString(CLIENTE_DIRECCION));
				clienteTemp2.setCliTelefono(cursor.getString(CLIENTE_TELEFONO));
				clienteTemp2.setCliDiasCredito(cursor.getString(CLIENTE_DIAS_CREDITO));
				clienteTemp2.setCliLimite(cursor.getString(CLIENTE_LIMITE));
				clienteTemp2.setCliSaldo(cursor.getString(CLIENTE_CLISALDO));
				clienteTemp2.setCliDescuento1(cursor.getString(CLIENTE_DESCUENTO1));
				clienteTemp2.setCliDescuento2(cursor.getString(CLIENTE_DESCUENTO2));
				clienteTemp2.setCliDescuento3(cursor.getString(CLIENTE_DESCUENTO3));
				clienteTemp2.setSaldo(cursor.getString(CLIENTE_SALDO));
				clienteTemp2.setVisitado(cursor.getInt(CLIENTE_VISITADO));
				clienteTemp.setCliNit(cursor.getString(CLIENTE_NIT));
				clientes[contador] = clienteTemp2;
				contador++;
			}
			dataBase.cerrar();
			getResultado().setCliente(clientes);
			
			//setCliente(clientes);
		} catch (Exception e) {
			getResultado().setError(1);
			System.out.println("PETICIONWS, 352. NO HAY DATOS");
			e.printStackTrace();
		}
	}
	
	public Cliente[] cargarClientesDB2(Context context) {
		DataBase dataBase = new DataBase(context);
		Cliente clientes[] = null;
		try {
			dataBase.abrir();
			Cursor cursor = dataBase.consultaClientes();
			
			int tamano = cursor.getCount();
			clientes = new Cliente[tamano];
			int contador = 0;

			cursor.moveToFirst();
			Cliente clienteTemp = new Cliente();
			//clienteTemp.setCliCodigo(String.valueOf(contador));
			clienteTemp.setCliCodigo(cursor.getString(CLIENTE_CODIGO));
			clienteTemp.setCliEmpresa(cursor.getString(CLIENTE_EMPRESA));
			clienteTemp.setCliContacto(cursor.getString(CLIENTE_CONTACTO));
			clienteTemp.setCliDireccion(cursor.getString(CLIENTE_DIRECCION));
			
			clienteTemp.setCliTelefono(cursor.getString(CLIENTE_TELEFONO));
			clienteTemp.setCliDiasCredito(cursor.getString(CLIENTE_DIAS_CREDITO));
			clienteTemp.setCliLimite(cursor.getString(CLIENTE_LIMITE));
			clienteTemp.setCliSaldo(cursor.getString(CLIENTE_CLISALDO));
			clienteTemp.setCliDescuento1(cursor.getString(CLIENTE_DESCUENTO1));
			clienteTemp.setCliDescuento2(cursor.getString(CLIENTE_DESCUENTO2));
			clienteTemp.setCliDescuento3(cursor.getString(CLIENTE_DESCUENTO3));
			clienteTemp.setSaldo(cursor.getString(CLIENTE_SALDO));
			clienteTemp.setVisitado(cursor.getInt(CLIENTE_VISITADO));
			clienteTemp.setCliNit(cursor.getString(CLIENTE_NIT));
			clientes[contador] = clienteTemp;
			contador++;
			
			while (cursor.moveToNext()) {
				Cliente clienteTemp2 = new Cliente();
				clienteTemp2.setCliCodigo(cursor.getString(CLIENTE_CODIGO));
				//clienteTemp2.setCliCodigo(String.valueOf(contador));
				clienteTemp2.setCliEmpresa(cursor.getString(CLIENTE_EMPRESA));
				clienteTemp2.setCliContacto(cursor.getString(CLIENTE_CONTACTO));
				clienteTemp2.setCliDireccion(cursor.getString(CLIENTE_DIRECCION));
				clienteTemp2.setCliTelefono(cursor.getString(CLIENTE_TELEFONO));
				clienteTemp2.setCliDiasCredito(cursor.getString(CLIENTE_DIAS_CREDITO));
				clienteTemp2.setCliLimite(cursor.getString(CLIENTE_LIMITE));
				clienteTemp2.setCliSaldo(cursor.getString(CLIENTE_CLISALDO));
				clienteTemp2.setCliDescuento1(cursor.getString(CLIENTE_DESCUENTO1));
				clienteTemp2.setCliDescuento2(cursor.getString(CLIENTE_DESCUENTO2));
				clienteTemp2.setCliDescuento3(cursor.getString(CLIENTE_DESCUENTO3));
				clienteTemp2.setSaldo(cursor.getString(CLIENTE_SALDO));
				clienteTemp2.setVisitado(cursor.getInt(CLIENTE_VISITADO));
				clienteTemp.setCliNit(cursor.getString(CLIENTE_NIT));
				clientes[contador] = clienteTemp2;
				contador++;
			}
			dataBase.cerrar();
			return clientes;
			//getResultado().setCliente(clientes);
			
			//setCliente(clientes);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return clientes;
		
		
	}

	@SuppressWarnings("null")
	private boolean BuscaClientesWS(String usuario, String clave, Context context) {
		Fecha fecha = new Fecha();
		String finalURL = WS + "username=" + usuario + "&password=" + clave + "&action=" + ACTION_CATALOGO + "&semana=" + fecha.Semana();
		System.out.println("LOGIN, PETICIONWS, 354, Todo va bien");
		jsonObject = ConnectWS.getJsonObject(finalURL);
		System.out.println("LOGIN, PETICIONWS, 356, Todo va bien");
		
		try{
			
			if (jsonObject.has("error")){
				System.out.println("PETICIONWS, 368, ");
				ConexionDB conexionLimpiaDB = new ConexionDB();
				System.out.println("PETICIONWS, 368, ");
				conexionLimpiaDB.LimpiaDB(context);
				System.out.println("PETICIONWS, 368, ");
				
				String error = jsonObject.getString("error");
				String mensaje = jsonObject.getString("mensaje");
				System.out.println("PETICIONWS, 368, " + error);
				
				getResultado().setError(Integer.parseInt(error));
				getResultado().setMensaje(mensaje);
				
				if (getResultado().getError() > 0){
					return false;
				}else {
					System.out.println("PETICIONWS, 376, " + error);
				if (VerificaLoginDB(usuario, clave, context)){
					if (jsonObject.has("vendedor")){
						//Copiando Datos del Vendedor
						JSONObject jsonObjectVendedor = jsonObject.getJSONObject("vendedor");
						Vendedor vendedor = new Vendedor();
						vendedor.setVendedor(jsonObjectVendedor.getString("vendedor"));
						vendedor.setNombre((jsonObjectVendedor.getString("nombre")));
						vendedor.setDireccion(jsonObjectVendedor.getString("direccion"));
						vendedor.setTelefono(jsonObjectVendedor.getString("telefono"));
						vendedor.setIdentificacion(jsonObjectVendedor.getString("identificacion"));
						vendedor.setComision(jsonObjectVendedor.getString("comision"));
						vendedor.setRuta(jsonObjectVendedor.getString("ruta"));
						
						getResultado().setVendedor(vendedor);
						//Guarda Datos de Vendedor en la DB
						ConexionDB conexionVendedor = new ConexionDB();
						conexionVendedor.GuardaVendedor(getResultado().getVendedor(), context);
						System.out.println("LOGIN, PETICIONWS, Vendedor Guardado");
					}
					
					if (jsonObject.has("catalogoNoVenta")){
						//Copiando Datos del Vendedor
						JSONArray jsonArrayMotivos = jsonObject.getJSONArray("catalogoNoVenta");
						int tamano = jsonArrayMotivos.length();
						Motivo motivo[] = new Motivo[tamano];
						for (int i = 0; i < tamano; i++){
							Motivo motivoTemp = new Motivo();
							JSONObject jsonMotivo = jsonArrayMotivos.getJSONObject(i);
							motivoTemp.setId(jsonMotivo.getString("IDnoventa"));
							motivoTemp.setMotivo(jsonMotivo.getString("descripnoventa"));
							motivo[i] = motivoTemp;
						}
						getResultado().setMotivo(motivo);
						
						//Guarda Motivos en la DB
						ConexionDB conexionMotivo = new ConexionDB();
						conexionMotivo.GuardaMotivos(getResultado().getMotivo(), context);
						System.out.println("Motivo Guardado");
					}
					
					if (jsonObject.has("catalogoClientes")){
						//Copiando Clientes
						JSONArray jsonArrayClientes = jsonObject.getJSONArray("catalogoClientes");
						int tamano = jsonArrayClientes.length();
						Cliente clientes[] = new Cliente[tamano];
						for (int i = 0; i < jsonArrayClientes.length(); i++){
							Cliente clienteTemp = new Cliente();
							JSONObject account = jsonArrayClientes.getJSONObject(i);
							clienteTemp.setCliCodigo(account.getString("clicodigo"));
		                    clienteTemp.setCliEmpresa(account.getString("cliempresa"));
		                    clienteTemp.setCliContacto(account.getString("clicontacto"));
		                    clienteTemp.setCliDireccion(account.getString("clidireccion"));
		                    clienteTemp.setCliTelefono(account.getString("clitelefono"));
		                    clienteTemp.setCliDiasCredito(account.getString("clidiascredito"));
		                    clienteTemp.setCliLimite(account.getString("clilimite"));
		                    clienteTemp.setCliSaldo(account.getString("clisaldo"));
		                    clienteTemp.setCliDescuento1(account.getString("clides1"));
		                    clienteTemp.setCliDescuento2(account.getString("clides2"));
		                    clienteTemp.setCliDescuento3(account.getString("clides3"));
		                    clienteTemp.setCliNit(account.getString("clinit"));
		                    clienteTemp.setSaldo(account.getString("Saldo"));
		                    //clienteTemp.setVisitado(account.getInt("visitado"));
		                    clienteTemp.setVisitado(0);
		                    
		                    int cuentaDescuentos = 0;
		                    if (account.getString("clides1").equalsIgnoreCase("1")){
		                    	cuentaDescuentos++;
		                    }
		                    if (account.getString("clides2").equalsIgnoreCase("1")){
		                    	cuentaDescuentos++;
		                    }
		                    if (account.getString("clides3").equalsIgnoreCase("1")){
		                    	cuentaDescuentos++;
		                    }
		                    clienteTemp.setDescuentos(cuentaDescuentos);
		                    clientes[i] = clienteTemp;
						}
						getResultado().setCliente(clientes);
						// Guarda en la base de datos los CLIENTES obtenidos
						ConexionDB conexionCliente = new ConexionDB();
						conexionCliente.GuardaClientes(getResultado().getCliente(), context);
						System.out.println("LOGIN, PETICIONWS, Clientes Guardados : " + getResultado().getCliente().length);
					}
					
					if (jsonObject.has("catalogoProductos")){
						JSONArray jsonArrayProductos = jsonObject.getJSONArray("catalogoProductos");
						//Copiando productos
						
						int tamanoProductos = jsonArrayProductos.length();
						if (tamanoProductos > 0){
							Producto productos[] = new Producto[tamanoProductos];
							for (int p = 0; p < tamanoProductos; p++){
								Producto productoTemp = new Producto();
								JSONObject productoJsonObject = jsonArrayProductos.getJSONObject(p);
								productoTemp.setArtCodigo(productoJsonObject.getString("codigo"));
								productoTemp.setArtCodigoAlterno(productoJsonObject.getString("codigoalterno"));
								productoTemp.setArtDescripcion(productoJsonObject.getString("descripcion"));
								productoTemp.setArtDescuento1(Float.parseFloat(productoJsonObject.getString("descuento1")));
								productoTemp.setArtDescuento2(Float.parseFloat(productoJsonObject.getString("descuento2")));
								productoTemp.setArtDescuento3(Float.parseFloat(productoJsonObject.getString("descuento3")));
								productoTemp.setArtPrecio(Float.parseFloat(productoJsonObject.getString("precio")));
								productoTemp.setArtCosto(Float.parseFloat(productoJsonObject.getString("costo")));
								productoTemp.setArtExistencia(productoJsonObject.getString(("existencia")));
								productoTemp.setArtUnidadesCaja(productoJsonObject.getString("unidadescaja"));
								productoTemp.setCaja("0");
								productoTemp.setUnidad("0");
								productos[p] = productoTemp;
							}
							getResultado().setProducto(productos);
							// Guarda en la base de datos los PRODUCTOS obtenidos
							ConexionDB conexionProducto = new ConexionDB();
							conexionProducto.GuardaProductos(getResultado().getProducto(), context);
							System.out.println("Productos Guardados : " + getResultado().getProducto().length);
						}
							
					}
					
					if (jsonObject.has("estadosdecuenta")){
						JSONArray jsonArrayCuentas = jsonObject.getJSONArray("estadosdecuenta");
						
						//copiando Estados de cuenta
						int tamanoCuentas = jsonArrayCuentas.length();
						if (tamanoCuentas > 0){
							EstadoDeCuenta cuentas[] = new EstadoDeCuenta[tamanoCuentas];
							for (int c = 0; c < tamanoCuentas; c++){
								EstadoDeCuenta cuentaTemp = new EstadoDeCuenta();
								JSONObject cuentaJsonObject = jsonArrayCuentas.getJSONObject(c);
								cuentaTemp.setCodigocliente(cuentaJsonObject.getString("clicodigo"));
								cuentaTemp.setCredito(cuentaJsonObject.getString("credito"));
								cuentaTemp.setFecha(cuentaJsonObject.getString("fecha"));
								cuentaTemp.setMovdocumento(cuentaJsonObject.getString("movdocumento"));
								cuentaTemp.setPagado(cuentaJsonObject.getString("pagado"));
								cuentaTemp.setSaldo(cuentaJsonObject.getString("saldo"));
								cuentaTemp.setTotal(cuentaJsonObject.getString("total"));
								cuentas[c] = cuentaTemp;
							}
							getResultado().setEstadoCuenta(cuentas);
							// Guarda en la base de datos los ESTADOS DE CUENTA obtenidos
							//ConexionDB conexionEstadosCuenta = new ConexionDB();
							//conexionEstadosCuenta.GuardaEstadoDeCuenta(getResultado().getEstadoCuenta(), context);
						}
						AsignaCuentas();
					}
					return true;
					}else{
					//PeticionLocal peticionLocal = new PeticionLocal();
					//setResultado(peticionLocal.DatosLocales(context));
					getResultado().setError(1);
					System.out.println("no hay datos");
					return true;
				}
				}
			}else{
				return false;
			}
				
			
		}catch(Exception exception){
			return false;
		}
	}

	private void AsignaCuentas() {
		int tamanoClientes = getResultado().getCliente().length;
		int tamanoCuentas = getResultado().getEstadoCuenta().length;
		int cuentasDeCliente = 0;
		
		for (int cuentaClientes = 0; cuentaClientes < tamanoClientes; cuentaClientes++){
			for (int cuentaCuentas = 0; cuentaCuentas < tamanoCuentas; cuentaCuentas++){
				if (getResultado().getEstadoCuenta()[cuentaCuentas].getCodigocliente().equalsIgnoreCase(getResultado().getCliente()[cuentaClientes].getCliCodigo())){
					cuentasDeCliente++;
				}
			}
			GuardaCuentas(cuentasDeCliente, getResultado().getCliente()[cuentaClientes].getCliCodigo());
			cuentasDeCliente = 0;
		}
	}

	private void GuardaCuentas(int tamano, String codigoCliente) {
		int tamanoCuentas = tamano;
		int tamanoClientes = getResultado().getCliente().length;

		EstadoDeCuenta cuentas[] = new EstadoDeCuenta[tamanoCuentas];
		for (int cuentaCuentas = 0; cuentaCuentas < tamanoCuentas; cuentaCuentas++){
			if (getResultado().getEstadoCuenta()[cuentaCuentas].getCodigocliente().equalsIgnoreCase(codigoCliente)){
				cuentas[cuentaCuentas] = getResultado().getEstadoCuenta()[cuentaCuentas];
			}
		}
		
		for (int cuentaClientes = 0; cuentaClientes < tamanoClientes; cuentaClientes++){
			if (getResultado().getCliente()[cuentaClientes].getCliCodigo().equalsIgnoreCase(codigoCliente)){
				getResultado().getCliente()[cuentaClientes].setEstadoCuentas(cuentas);
			}
		}
	}

	public String postMotivo(String usuario, String password, String codigoCliente, String fecha, String hora, Motivo motivo, String idVendedor){
		System.out.println("PETICIONWS, idmotivo = " + motivo.getId());
		NuevoPedido nuevoPedido = new NuevoPedido();
		EncabezadoPedido encabezado = new EncabezadoPedido();
		Conversion conversion = new Conversion();
		encabezado.setCodigoCliente(codigoCliente);
		encabezado.setFecha(fecha);
		encabezado.setHora(hora);
		encabezado.setFallido(1);
		encabezado.setMotivo(Integer.parseInt(motivo.getId()));
		encabezado.setMotivoNoCompra(motivo.getMotivo());
		encabezado.setCheque("0");
		encabezado.setCredito("0");
		encabezado.setEfectivo("0");
		encabezado.setTotal(0);
		encabezado.setSinc(1);
		nuevoPedido.setEncabezado(encabezado);
		
		String motivoString = conversion.creaJsonNocompra(motivo.getId(), codigoCliente, fecha, hora, idVendedor);
		String finalURL = WS + "username=" + usuario + "&password=" + password + "&action=" + ACTION_MOTIVO + "&json=" + motivoString;
		System.out.println(finalURL);
		ConnectWS connectWS = new ConnectWS();
		String error = "";
		jsonObject = connectWS.enviaMotivo(finalURL);
		/*try {
			error = jsonObject.getString("error");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return error;
	}
	
	@SuppressWarnings("static-access")
	public String postPedido(NuevoPedido pedido, String usuario, String clave){
		
		String error = "1";
		int tamanoDetalle = pedido.getDetallePedido().length;
		int tamano = 0;
		for (int a = 0; a < tamanoDetalle; a++){
			if (Float.parseFloat(pedido.getDetallePedido()[a].getSubTotal()) > 0){
				tamano++;
			}
		}
		int j = 0;
		DetallePedido[] detPedido = new DetallePedido[tamano];
		for (int a = 0; a < tamanoDetalle; a++){
			if (Float.parseFloat(pedido.getDetallePedido()[a].getSubTotal()) > 0){
				DetallePedido detTemp = new DetallePedido();
				detTemp = pedido.getDetallePedido()[a];
				detPedido[j] = detTemp;
				j++;
			}
		}
		
		
		
		System.out.println("PETICIONWS, 612, tamanoDetalle = " + detPedido.length);
		
		int i = 0;
		int facts = 0;
		if (tamano < 11){
			facts = 1;
		}else{
			facts = tamano/LIMITE_FACTURA;
			if (tamano%10 > 0){
				facts++;
			}
		}
		
		System.out.println("PETICIONWS, 632, facts = " + facts);
		
		
		for (int a = 1; a <= facts; a++){
			Conversion conversion = new Conversion();
			String pedidoString = conversion.creaJsonPedido(pedido.getEncabezado(), detPedido, i, tamano, a);
			String finalURL = WS + "username=" + usuario + "&password=" + clave + "&action=" + ACTION_PEDIDO + "&json=" + pedidoString;
			System.out.println("PETICIONWS, 653, Envia pedido " + a + pedidoString);
			ConnectWS connectWs = new ConnectWS();
			
			jsonObject = connectWs.enviarPedido(finalURL);
			if (jsonObject == null){
				return error;
			}
				try {
					error = jsonObject.getString("error");
				} catch (JSONException e) {
					System.out.println(e);
					e.printStackTrace();
					return error;
				}
				i = i+LIMITE_FACTURA;
		}
		return error;
	}

	public Cliente[] getCliente() {
		return cliente;
	}

	public void setCliente(Cliente[] cliente) {
		this.cliente = cliente;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

}
