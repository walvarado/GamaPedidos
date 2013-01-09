package com.researchmobile.gama.db;

import android.content.ContentValues;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase {
	
	
	// Datos Necesarios para la Base de Datos
	private static final String N_BD = "datamercadisa017";
	private static final int VERSION_BD = 1;
	
	//Declaracion de Variables Helper, contexto y Base de datos
	private BDHelper nHelper;
	private final Context nContexto;
	private SQLiteDatabase nBD;
	
	private static class BDHelper extends SQLiteOpenHelper {

		public BDHelper(Context context){
			super(context, N_BD, null, VERSION_BD);
			// TODO Auto-generated constructor stub
			
		}

		// Metodo de creacion de tablas en la base de datos
		@Override
		public void onCreate(SQLiteDatabase db) {
			// Creacion de tablas
						db.execSQL("CREATE TABLE Cliente (	idCliente text PRIMARY KEY," + 
															"empresa text  NULL,"+
															"contacto text  NULL, "+
															"direccion text NULL,"+
															"telefono text NULL,"+
															"diasCredito integer NULL,"+
															"limite numeric NULL ,"+
															"cliSaldo numeric NULL,"+
															"des1 numeric NULL,"+
															"des2 numeric NULL ,"+
															"des3 numeric NULL,"+
															"saldo numeric  NULL,"+
															"visitado integer NULL,"+
															"nit text NULL);");
						
						db.execSQL("CREATE TABLE Producto (	codigoProducto text PRIMARY KEY," + 
															"codigoAlterno text NULL, "+
															"descripcion text  NULL,"+
															"precioDescuento1 numeric  NULL, "+
															"precioDescuento2 numeric NULL,"+
															"precioDescuento3 numeric NULL,"+
															"precio numeric NULL," +
															"costo numeric NULL," +
															"unidadesCaja numeric NULL," +
															"existencia integer  NULL);");
						
						db.execSQL("CREATE TABLE Vendedor (	codigoVendedor text PRIMARY KEY," + 
															"nombre text NULL, "+
															"direccion text  NULL,"+
															"telefono text  NULL, "+
															"identificacion text NULL,"+
															"comision numeric NULL,"+
															"ruta integer  NULL);");
						
						db.execSQL("CREATE TABLE Pedido   (	codigoPedido INTEGER PRIMARY KEY AUTOINCREMENT," + 
															"codigoCliente text NULL, "+
															"fecha numeric  NULL,"+
															"hora numeric  NULL, "+
															"total numeric NULL,"+
															"efectivo numeric NULL,"+
															"cheque numeric NULL,"+
															"credito numeric NULL,"+
															"sinc integer  NULL,"+
															"fallido integer NULL,"+
															"idmotivo integer NULL,"+
															"motivo text NULL);");
						
						db.execSQL("CREATE TABLE DetallePedido (codigoArticulo text NULL," + 
															"documento text NULL,"+
															"unidades integer  NULL,"+
															"precio numeric NULL,"+
															"sinc integer  NULL,"+
															"descripcion text NULL,"+
															"caja integer NULL,"+
															"unidad integer NULL,"+
															"presentacion text NULL,"+
															"existencia integer NULL,"+
															"bonificacion text NULL,"+
															"subtotal numeric NULL);");
						
						db.execSQL("CREATE TABLE Motivo (id text NULL," + 
															"motivo text NULL);");
						
						db.execSQL("CREATE TABLE EstadoDeCuenta   (	codigoCliente text NULL," + 
															"movDocumento text PRIMARY KEY, "+
															"fecha numeric  NULL,"+
															"total numeric  NULL, "+
															"credito numeric NULL,"+
															"pagado numeric NULL,"+
															"saldo numeric NULL,"+
															"cancelado integer NULL,"+
															"sinc integer  NULL);");
						
						db.execSQL("CREATE TABLE Pago   (	 correlativo INTEGER PRIMARY KEY AUTOINCREMENT," + 
															"movDocumento text NULL, "+
															"noDocumento text  NULL,"+
															"abono numeric  NULL, "+
															"fecha numeric NULL,"+
															"tipoDocumento text NULL,"+
															"sinc integer  NULL);");
						
						
						db.execSQL("CREATE TABLE Usuario (	 usuario text NULL," +
															"clave text NULL," +
															"dia text NULL);");
		 }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			db.execSQL("DROP TABLE IF EXISTS Cliente");
			db.execSQL("DROP TABLE IF EXISTS Producto");
			db.execSQL("DROP TABLE IF EXISTS Vendedor");
			db.execSQL("DROP TABLE IF EXISTS Pedido");
			db.execSQL("DROP TABLE IF EXISTS DetallePedido");
			db.execSQL("DROP TABLE IF EXISTS EstadoDeCuenta");
			db.execSQL("DROP TABLE IF EXISTS Pago");
			db.execSQL("DROP TABLE IF EXISTS Usuario");
			db.execSQL("DROP TABLE IF EXISTS Motivo");
			onCreate(db);
		}
	}
	//CONSULTAS
	public Cursor consultaClientes() {
		Cursor c = nBD.rawQuery(" SELECT * FROM Cliente;", null);
		if(c != null){
		         return c;
		}
		return null;
		}
	
	public Cursor consultaVendedor(){
		Cursor c = nBD.rawQuery(" SELECT * FROM Vendedor;", null);
		if(c != null){
		         return c;
		}
		return null;
		
	}
	
	public Cursor consultaMotivos() {
		Cursor c = nBD.rawQuery(" SELECT * FROM Motivo Order By motivo;", null);
		if(c != null){
		         return c;
		}
		return null;
		}
	
	public Cursor consultaPedidos() {
		System.out.println("pedidos locales");
		Cursor c = nBD.rawQuery(" SELECT * FROM Pedido ;", null);
		
		System.out.println("pedidos locales");
		if(c != null){
			
			System.out.println(c.getCount());
		         return c;
		}
		return null;
		}
	
	public Cursor consultaDetallePedidos(String codigoPedido){
		String[] arg = new String[]{codigoPedido};
		Cursor c = nBD.rawQuery("SELECT * FROM DetallePedido WHERE documento=? ", arg);
		
		if (c != null){
			System.out.println(c.getCount());
			return c;
		}
		return null;
		
	}
	public Cursor consultaPagos(String movDocumento) {
		String[] arg = new String[]{movDocumento};
		Cursor c = nBD.rawQuery(" SELECT movDocumento, noDocumento, fecha, abono, tipoDocumento FROM Pago WHERE movDocumento=? ", arg);
		if(c != null){
			
		         return c;
		}
		return null;
		}
	
	public Cursor consultaSumaAbonos(String movDocumento) {
		String[] arg = new String[]{movDocumento};
		Cursor c = nBD.rawQuery(" SELECT sum(abono) FROM Pago WHERE movDocumento=? ", arg);
		if(c != null){
		         return c;
		}
		return null;
		}
	
	public Cursor consultaEstadoDeCuenta(String cliente){
		
		String[] arg = new String[]{cliente,"AND "};
		Cursor c = nBD.rawQuery(" SELECT* FROM EstadoDeCuenta  WHERE codigoCliente=? ", arg);
		if(c != null){
		         return c;
		}
		return null;
		
	}
	
/*	public String consultaMaxPagos(){
		Cursor c = nBD.rawQuery(" SELECT Count(correlativo) FROM Pagos;", null);
		int i = c.getColumnIndex("Count(correlativo)");
		String max = c.getString(i);
		return max;	
	}*/
	
	public Cursor consultaDetalleCliente(String idCliente) {
		String[] arg = new String[]{idCliente};
		Cursor c = nBD.rawQuery("SELECT * FROM Cliente WHERE Cliente =? ", arg);
		if(c != null){
			     return c;
		}
		return null;
		}
	
	public Cursor consultaUsuario(String usuario, String pass) {
		Cursor c = nBD.rawQuery("SELECT * FROM Usuario;", null);
		
		if(c != null){
			System.out.println("DATABASE, 238, USUARIOS = " + c.getCount());
			     return c;
		}else{
			System.out.println("DATABASE, 241, ERROR BUSCANDO USUARIOS");
			return null;
		}
		
		}
	
	//INSERTS
	//Insertar datos de usuario
	public long insertUsuario(String usuario, String clave, String dia) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("usuario", usuario);
		cv.put("clave", clave);
		cv.put("dia", dia);
		System.out.println(cv);
		return nBD.insert("Usuario", null, cv);	
	}
	
	//Insertar motivos no compra
	public long insertMotivos(String id, String motivo) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("id", id);
		cv.put("motivo", motivo);
		System.out.println(cv);
		return nBD.insert("Motivo", null, cv);	
	}
	
	//borrar los registros de la DB
	public void limpiarDB(){
		nBD.execSQL("DELETE FROM Cliente;");
		nBD.execSQL("DELETE FROM Producto;");
		nBD.execSQL("DELETE FROM Vendedor;");
		nBD.execSQL("DELETE FROM EstadoDeCuenta");
		nBD.execSQL("DELETE FROM Pago");
		nBD.execSQL("DELETE FROM Motivo");
	}
	
	public void limpiarClienteDB(){
		nBD.execSQL("DELETE FROM Cliente;");
	}
	
	public void limpiaPedidoDB(){
		nBD.execSQL("DELETE FROM DetallePedido;");
		nBD.execSQL("DELETE FROM Pedido;");
	}
	
	public void limpiaUsuario(){
		nBD.execSQL("DELETE FROM Usuario;");
	}
	public Cursor consultaProducto() {
		Cursor c = nBD.rawQuery(" SELECT * FROM Producto Order By descripcion;", null);
		if(c != null){
		         return c;
		}
		return null;
	}
	
	public DataBase (Context c){
		nContexto = c;
	}
	
	public DataBase abrir() throws Exception{
		
		nHelper = new BDHelper(nContexto);
		nBD = nHelper.getWritableDatabase();
		return this;
	}

	public void cerrar() {
		// TODO Auto-generated method stub
		nHelper.close();
	}

	/*
	public long crearEntrada(String nom, String tel) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(ID_PERSONA, nom);
		cv.put(ID_TELEFONO, tel);
		return nBD.insert(N_TABLA, null, cv);
		
	}*/
	
	
	//////////////////////								   //////////////////////
	/////////////////////								  ///////////////////////
	///////////////////// INSERCIONES A LA BASE DE DATOS ////////////////////////
	
	public long insertCliente(String  idCliente, String empresa, String contacto, String direccion, String telefono, String diasCredito, String limite, String cliSaldo, String des1, String des2, String des3, String saldo, int visitado, String nit ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("idCliente", idCliente);
		cv.put("empresa", empresa);
		cv.put("contacto", contacto);
		cv.put("direccion", direccion);
		cv.put("telefono", telefono);
		cv.put("diasCredito", diasCredito);
		cv.put("limite", limite);
		cv.put("cliSaldo", cliSaldo);
		cv.put("des1", des1);
		cv.put("des2", des2);
		cv.put("des3", des3);
		cv.put("saldo", saldo);
		cv.put("visitado", visitado);
		cv.put("nit", nit);
		
		return nBD.insert("Cliente", null, cv);	
	}
	
	public long insertProducto(String codigoProducto, String codigoAlterno, String descripcion, String precioDescuento1, String precioDescuento2, String precioDescuento3, String precio, String costo, String unidadesCaja, String existencia  ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("codigoProducto", codigoProducto);
		cv.put("codigoAlterno", codigoAlterno);
		cv.put("descripcion", descripcion);
		cv.put("precioDescuento1", precioDescuento1);
		cv.put("precioDescuento2", precioDescuento2);
		cv.put("precioDescuento3", precioDescuento3);
		cv.put("precio", precio);
		cv.put("costo", costo);
		cv.put("unidadesCaja", unidadesCaja);
		cv.put("existencia", existencia);
		
			
		return nBD.insert("Producto", null, cv);	
	}
	
	public long insertVendedor(String codigoVendedor, String nombre, String direccion, String telefono, String identificacion, String comision, String ruta ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("codigoVendedor", codigoVendedor);
		cv.put("nombre", nombre);
		cv.put("direccion", direccion);
		cv.put("telefono", telefono);
		cv.put("identificacion", identificacion);
		cv.put("comision", comision);
		cv.put("ruta", ruta);
	
		return nBD.insert("Vendedor", null, cv);	
	}
	
	public long insertMotivo(String idmotivo, String motivo) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("id", idmotivo);
		cv.put("motivo", motivo);
		
		return nBD.insert("Motivo", null, cv);	
	}
	
	public long insertPedido(String codigoCliente, String fecha, String hora, String total, String efectivo, 
			String cheque, String credito, String sinc, String fallido, String idmotivo, String motivo) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		//cv.put("codigoPedido", "6");
		cv.put("codigoCliente", codigoCliente);
		cv.put("fecha", fecha);
		cv.put("hora", hora);
		cv.put("total", total);
		cv.put("efectivo", efectivo);
		cv.put("cheque", cheque);
		cv.put("credito", credito);
		cv.put("sinc", sinc);
		cv.put("fallido", fallido);
		cv.put("idmotivo", idmotivo);
		cv.put("motivo", motivo);
		
		
		System.out.println("Encabezado Guardado");
		return nBD.insert("Pedido", null, cv);	
	}
	

	public long insertDetallePedido(String codigoArticulo, String documento, String unidades, String precio, String sinc,
			String descripcion, String caja, String unidad, String presentacion, String existencia, String bonificacion, String subtotal) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("codigoArticulo", codigoArticulo);
		cv.put("documento", documento);
		cv.put("unidades", unidades);
		cv.put("precio", precio);
		cv.put("sinc", sinc);
		cv.put("descripcion", descripcion);
		cv.put("caja", caja);
		cv.put("unidad", unidad);
		cv.put("presentacion", presentacion);
		cv.put("existencia", existencia);
		cv.put("bonificacion", bonificacion);
		cv.put("subtotal", subtotal);
		System.out.println("Detalle pedido Guardado");
		
		return nBD.insert("DetallePedido", null, cv);	
	}
	
	public long insertEstadosDeCuenta(String codigoCliente, String movDocumento, String fecha, String total, String credito, String pagado, String saldo, String cancelado, String sinc  ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("codigoCliente", codigoCliente);
		cv.put("movDocumento", movDocumento);
		cv.put("fecha", fecha);
		cv.put("total", total);
		cv.put("credito", credito);
		cv.put("pagado", pagado);
		cv.put("saldo", saldo);
		cv.put("cancelado", cancelado);
		cv.put("sinc", sinc);
				
		return nBD.insert("EstadoDeCuenta", null, cv);	
	}
	
	public long insertPago(String movDocumento, String noDocumento, String abono, String fecha, String tipoDocumento, String sinc ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		//cv.put("correlativo", correlativo);
		cv.put("movDocumento", movDocumento);
		cv.put("noDocumento", noDocumento);
		cv.put("abono", abono);
		cv.put("fecha", fecha);
		cv.put("tipoDocumento", tipoDocumento);
		cv.put("sinc", sinc);
		
		return nBD.insert("Pago", null, cv);	
	}
	
	
	public long noCompraInsert(String tipodoc, String nodoc, String cliente  ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Cliente", tipodoc);
		cv.put("Motivo", nodoc);
		cv.put("Fecha", cliente);
		
		
		return nBD.insert("NoCompra", null, cv);	
	}
	
	public long pedidoInsert(String pedido, String fecha, String vendedor, String cliente, String nombre, String total, String observaciones, String fechaentrega, String tipocredito, String factura, String serie, String facturado, String tabulado, String bodega  ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Pedido", pedido);
		cv.put("Fecha", fecha);
		cv.put("Vendedor", vendedor);
		cv.put("Cliente", cliente);
		cv.put("Nombre", nombre);
		cv.put("Total", total);
		cv.put("Observaciones", observaciones);
		cv.put("FechaEntrega", fechaentrega);
		cv.put("tipoCredito", tipocredito);
		cv.put("Factura", factura);
		cv.put("Serie", serie);
		cv.put("Facturado", facturado);
		cv.put("Tabulado", tabulado);
		cv.put("Bodega", bodega);
		
		return nBD.insert("Pedido", null, cv);	
	}
	
	public long productoInsert(String producto, String nombre, String unidades, String precioa, String preciob, String precioc, String preciod, String precioe, String proveedor, String categoria, String nombreprov  ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Producto", producto);
		cv.put("Nombre", nombre);
		cv.put("Unidades", unidades);
		cv.put("PrecioA", precioa);
		cv.put("PrecioB", preciob);
		cv.put("PrecioC", precioc);
		cv.put("PrecioD", preciod);
		cv.put("PrecioE", precioe);
		cv.put("Proveedor", proveedor);
		cv.put("Categoria", categoria);
		cv.put("NombreProv", nombreprov);
	
		return nBD.insert("Producto", null, cv);	
	}
	
	public long proveedorInsert(String proveedor, String nombre) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Proveedor", proveedor);
		cv.put("Nombre", nombre);
			
		return nBD.insert("Proveedor", null, cv);	
	}
	
	public long resolucionInsert(String serie, String correlativo, String del, String al, String resolucion, String fecharesolucion, String fechaingreso, String numerototal, String id  ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Serie", serie);
		cv.put("Correlativo", correlativo);
		cv.put("Del", del);
		cv.put("Al", al);
		cv.put("Resolucion", resolucion);
		cv.put("FechaResolucion", fecharesolucion);
		cv.put("FechaIngreso", fechaingreso);
		cv.put("NumeroTotal", numerototal);
		cv.put("ID", id);
		
		return nBD.insert("Resolucion", null, cv);	
	}
	
	
	public long vendedorInsert(String vendedor, String nombre) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Vendedor", vendedor);
		cv.put("Nombre", nombre);
			
		return nBD.insert("Vendedor", null, cv);	
	}
	
	public long ventaInsert(String caja, String unidad, String subtotal, String producto, String mes, String anio ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Caja", caja);
		cv.put("Unidad", unidad);
		cv.put("SubTotal", subtotal);
		cv.put("Producto", producto);
		cv.put("Mes", mes);
		cv.put("Año", anio);
		
		
		return nBD.insert("Venta", null, cv);	
	}
	
	public long visitadoInsert(String caja, String unidad, String subtotal, String producto, String mes, String anio ) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("Cliente", caja);
		cv.put("Semana", unidad);
		cv.put("Dia", subtotal);
		cv.put("Fecha", producto);
		
		return nBD.insert("Visitado", null, cv);	
	}
	
	public Cursor consultaFactura()throws SQLException {
		// TODO Auto-generated method stub
		//String[]columnas = new String[]{"factura", ID_PERSONA,ID_TELEFONO};
		//Cursor c = nBD.query(N_TABLA, columnas, ID_FILA + "=" + lb, null, null, null, null);
		//Cursor c = nBD.rawQuery(" SELECT A.Pedido, B.NombreComercial, B.RazonSocial,B.Direccion, A.Fecha, A.Cliente, B.NomVendedor FROM Pedido A, Cliente B WHERE A.Cliente = B.Cliente ", null);
		Cursor c = nBD.rawQuery("SELECT Pedido, Pedido, Pedido, Pedido, Pedido, Pedido, Pedido FROM Pedido", null);
		if(c != null){
		
			return c;
			
		}
		return null;
	}
	
	//UPDATES
	public void actualizarPedido(String codigoPedido) throws SQLException {
		// TODO Auto-generated method stub
		ContentValues cvEditar = new ContentValues();
		cvEditar.put("sinc", "1");
		nBD.update("Pedido", cvEditar, "codigoPedido" + "=" + codigoPedido, null);
		System.out.println("Pedido actualizado. DataBase, 595");
	}
	
	public void actualizarCliente(String codigoCliente) throws SQLException{
		int visitado = 1;
		System.out.println("DataBase. 601. Actualizar cliente " + codigoCliente);
		ContentValues cvEditar = new ContentValues();
		cvEditar.put("visitado", visitado);
		nBD.update("Cliente", cvEditar, "idCliente=" + codigoCliente, null);
		System.out.println(nBD);
		System.out.println("Cliente actualizado. DataBase, 595");
	}
}
