package com.researchmobile.gama.pedidos;

import java.util.HashMap;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.db.DataBase;
import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.DetallePedido;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.Fecha;
import com.researchmobile.gama.entidades.NuevoPedido;
import com.researchmobile.gama.entidades.Producto;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.entidades.Usuario;
import com.researchmobile.gama.utilidades.ConnectState;
import com.researchmobile.gama.utilidades.FormatDecimal;
import com.researchmobile.gama.utilidades.ListaTest;
import com.researchmobile.gama.ws.PeticionWS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VerDetalle extends Activity implements TextWatcher, OnItemClickListener {
	
    private String idCliente;
    private ListaTest listaProductos;
    private ListView listaProductosListView;
    private Resultado resultado;
    private NuevoPedido nuevoPedido;
    private EncabezadoPedido encabezadoPedido;
    private Cliente cliente;
    private DetallePedido[] detallePedido;
    private TextView totalPedidoTextView;
    private EditText buscarEditText;
    private ImageButton borrarImageButton;
    
    private Fecha fecha;
    private FormatDecimal formatDecimal;
    
    private String caja;
    private String unidad;
    private String codigo;
    private String nombre;
    private float totalPedido;
    private String cajaTemp;
    private String unidadTemp;
    private float precioSeleccionado;
    private String codigoPedido;
    
    private String usuario = Usuario.getUsuario();
    private String clave = Usuario.getClave();
    
    private SimpleAdapter simpleAdapter;
    
    private boolean controlMenu;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tomar_pedido);
        
        Bundle bundle = getIntent().getExtras();
        setResultado((Resultado)bundle.get("resultado"));  //Recibe Resultado de Pedido (lista de clientes)
        setIdCliente((String)bundle.get("codigoCliente")); //Recibe Codifo del cliente seleccionado 
        
        setFecha(new Fecha());
        setFormatDecimal(new FormatDecimal());
        
        setCliente(new Cliente());
        DatosCliente(getIdCliente());
        setTotalPedido((Float)bundle.get("total"));
        setTotalPedidoTextView((TextView)findViewById(R.id.tomar_pedido_total_textview));
        getTotalPedidoTextView().setText(getFormatDecimal().ConvierteFloat(getTotalPedido()));
        setEncabezadoPedido(new EncabezadoPedido());
        setNuevoPedido(new NuevoPedido());
        
        setControlMenu(true);
        
        setBorrarImageButton((ImageButton)findViewById(R.id.tomar_pedido_borrar_imagebutton));
        
        setBuscarEditText((EditText)findViewById(R.id.tomar_pedido_buscar_edittext));
        getBuscarEditText().addTextChangedListener(this);
        
        getBorrarImageButton().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getBuscarEditText().setText("");
				
			}
		});
        
        final DataBase objeto = new DataBase(VerDetalle.this);
        
		setListaProductosListView((ListView)findViewById(R.id.tomar_pedido_lista_productos_listview));
		getListaProductosListView().setOnItemClickListener(this);
        setListaProductos(new ListaTest());
        
        IniciaPedido(objeto);
    }
	
	private void DatosCliente(String idCliente2) {
		for (int i = 0; i < getResultado().getCliente().length; i++){
			if (getResultado().getCliente()[i].getCliCodigo().equalsIgnoreCase(idCliente2)){
				setCliente(getResultado().getCliente()[i]);
			}
		}
		
	}

	//Desactivar el Bot�n Back(Atras) del dispositivo
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Preventing default implementation previous to
			// android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * MENU
	 */

	// To change item text dynamically
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(isControlMenu()){
			menu.getItem(0).setEnabled(true);
			menu.getItem(1).setEnabled(false);
		}else{
			menu.getItem(1).setEnabled(true);
			menu.getItem(0).setEnabled(false);
		}
		
		if (getTotalPedido() < 0.1){
			menu.getItem(3).setEnabled(false);
		}else{
			menu.getItem(3).setEnabled(true);
		}
		
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tomar_pedido_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.tomar_pedido_ver_pedido_opcion:
			setControlMenu(false);
			VerPedido();
			return true;
		
		case R.id.tomar_pedido_agregar_opcion:
			setControlMenu(true);
			AgregarArticulo();
			return true;
			
		case R.id.tomar_pedido_cancelar_opcion:
			CancelarPedido();
			return true;
		case R.id.tomar_pedido_enviar_opcion:
			EnviarPedido();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	private void AgregarArticulo() {
		LlenaLista(null, null, null);
		
	}

	private void EnviarPedido() {
		LayoutInflater factory = LayoutInflater.from(VerDetalle.this);
		final View textEntryView = factory.inflate(R.layout.tomar_pedido_cancelar_dialog, null);

		final AlertDialog.Builder alert = new AlertDialog.Builder(VerDetalle.this);
		        
		alert.setTitle("ENVIAR PEDIDO");
		alert.setMessage("�ESTA SEGURO QUE DESEA ENVIAR EL PEDIDO?");
		alert.setView(textEntryView);

		alert.setPositiveButton("  SI  ", new DialogInterface.OnClickListener()
		{
			String error = null;
		     public void onClick(DialogInterface dialog, int whichButton)
		     {
		    	getEncabezadoPedido().setTotal(getTotalPedido());
		 		PeticionWS peticionWs = new PeticionWS();
		 		ConnectState connectState = new ConnectState();
		 		if (connectState.isConnectedToInternet(VerDetalle.this)){
		 			env�osPendientes();
		 			verificaPendientes();
		 			error = peticionWs.postPedido(getNuevoPedido(), getUsuario(), getClave());
		 			if (error.equalsIgnoreCase("0")){
		 				System.out.println("Se enviaron los datos al WS");
			 			guardaPedido("1");
			 			ConexionDB conexionDBSend = new ConexionDB();
			 			conexionDBSend.GuardaEncabezadoPedido(getNuevoPedido().getEncabezado(), VerDetalle.this, "1");
			 			String numeroPedido = conexionDBSend.VerNumeroPedido(VerDetalle.this);
			 			conexionDBSend.GuardaDetallePedido(getNuevoPedido().getDetallePedido(), numeroPedido, VerDetalle.this);
			 			Toast.makeText(getBaseContext(), "El pedido se envio exitosamente", Toast.LENGTH_LONG).show();
		 			}else{
		 				ConexionDB conexionDB = new ConexionDB();
			 			//conexionDB.limpiaPedidoDB(TomarPedido.this);
			 			error = conexionDB.GuardaEncabezadoPedido(getNuevoPedido().getEncabezado(), VerDetalle.this, "0");
			 			String numeroPedido = conexionDB.VerNumeroPedido(VerDetalle.this);
			 			error = conexionDB.GuardaDetallePedido(getNuevoPedido().getDetallePedido(), numeroPedido, VerDetalle.this);
			 			Toast.makeText(getBaseContext(), "El pedido se guardo temporalmente porque no tiene una conexion segura", Toast.LENGTH_LONG).show();
			 			System.out.println("Se guardo en detalle en la BD");
			 			Intent intent = new Intent(VerDetalle.this, Pedido.class);
			 			intent.putExtra("resultado", getResultado());
			 			startActivity(intent);
		 			}
		 			
		 		}else{
		 			ConexionDB conexionDB = new ConexionDB();
		 			//conexionDB.limpiaPedidoDB(TomarPedido.this);
		 			error = conexionDB.GuardaEncabezadoPedido(getNuevoPedido().getEncabezado(), VerDetalle.this, "0");
		 			String numeroPedido = conexionDB.VerNumeroPedido(VerDetalle.this);
		 			error = conexionDB.GuardaDetallePedido(getNuevoPedido().getDetallePedido(), numeroPedido, VerDetalle.this);
		 			Toast.makeText(getBaseContext(), "El pedido se guardo temporalmente porque no tiene una conexion segura", Toast.LENGTH_LONG).show();
		 			System.out.println("Se guardo en detalle en la BD");
		 		}
		 		
		 		if (error.equalsIgnoreCase("0")){
		 			
		 			Intent intent = new Intent(VerDetalle.this, Pedido.class);
		 			intent.putExtra("resultado", getResultado());
		 			startActivity(intent);
		 		} 
		     }
			private void guardaPedido(String string) {
				// TODO Auto-generated method stub
				
			}
			private void env�osPendientes() {
				NuevoPedido pedidoPendiente = new NuevoPedido();
				DataBase dataBase = new DataBase(VerDetalle.this);
				try {
					dataBase.abrir();
					Cursor cursor = dataBase.consultaPedidos();
					dataBase.cerrar();
					int tamano = cursor.getCount();
					
					if (tamano > 0){
						EncabezadoPedido encabezado = new EncabezadoPedido();
						cursor.moveToFirst();
						encabezado.setFecha(cursor.getString(2));System.out.println(tamano);
						encabezado.setHora(cursor.getString(3));
						encabezado.setSinc(Integer.parseInt(cursor.getString(8)));
						encabezado.setTotal(Float.parseFloat(cursor.getString(4)));
						System.out.println("fecha : " + encabezado.getFecha());
						System.out.println("hora : " + encabezado.getHora());
						System.out.println("sinc : " + encabezado.getSinc());
						System.out.println("total : " + encabezado.getTotal());
						while (cursor.moveToNext()) {
							
							encabezado.setFecha(cursor.getString(2));System.out.println(tamano);
							encabezado.setHora(cursor.getString(3));
							encabezado.setSinc(Integer.parseInt(cursor.getString(8)));
							encabezado.setTotal(Float.parseFloat(cursor.getString(4)));
							System.out.println("fecha : " + encabezado.getFecha());
							System.out.println("hora : " + encabezado.getHora());
							System.out.println("sinc : " + encabezado.getSinc());
							System.out.println("total : " + encabezado.getTotal());
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
			}
		});
		 
		alert.setNegativeButton("  NO  ", new DialogInterface.OnClickListener()
		{
		     public void onClick(DialogInterface dialog, int whichButton)
		     {
		          // Canceled.
		     }
		});
		
		alert.show();
	}

	protected void verificaPendientes() {
		ConexionDB conexionDB = new ConexionDB();
		PeticionWS peticionWS = new PeticionWS();
		
		//conexionDB.VerPedidos(this);
		EncabezadoPedido[] pedidoTemp = conexionDB.VerPedidos(this);
		//DetallePedido[] detalleTemp = conexionDB.verDetalle(this);
		
		if (pedidoTemp != null){
			
			int tamanoPedido = pedidoTemp.length;
			System.out.println("pedidos pendientes de envio = " + tamanoPedido);
			//int tamanoDetalle = detalleTemp.length;
			
				for (int i = 0; i < tamanoPedido; i++){
					
					if (pedidoTemp[i].getSinc() == 0){
						NuevoPedido nuevoPedidoTemp = new NuevoPedido();
						nuevoPedidoTemp.setEncabezado(pedidoTemp[i]);
						System.out.println("pedido a enviar = " + nuevoPedidoTemp.getEncabezado().getCodigoPedidoTemp());
						nuevoPedidoTemp.setDetallePedido(conexionDB.BuscaDetallePedido(this, pedidoTemp[i].getCodigoPedidoTemp()));
						System.out.println("revision 1");
						peticionWS.postPedido(nuevoPedidoTemp, getUsuario(), getClave());
						System.out.println("revision 2");
						conexionDB.actualizaEncabezadoPedido(this, pedidoTemp[i].getCodigoPedidoTemp());
						System.out.println("se envio un pedido que estaba guardado");
					}
				}
		}
	}

	private void CancelarPedido() {
		LayoutInflater factory = LayoutInflater.from(VerDetalle.this);
		final View textEntryView = factory.inflate(R.layout.tomar_pedido_cancelar_dialog, null);

		final AlertDialog.Builder alert = new AlertDialog.Builder(VerDetalle.this);
		        
		alert.setTitle("CANCELAR PEDIDO");
		alert.setMessage("�ESTA SEGURO QUE DESEA CANCELAR EL PEDIDO?");
		alert.setView(textEntryView);

		alert.setPositiveButton("  SI  ", new DialogInterface.OnClickListener()
		{
		     public void onClick(DialogInterface dialog, int whichButton)
		     {
		 		setNuevoPedido(null);
				finish(); 
		     }
		});
		 
		alert.setNegativeButton("  NO  ", new DialogInterface.OnClickListener()
		{
		     public void onClick(DialogInterface dialog, int whichButton)
		     {
		          // Canceled.
		     }
		});
		
		alert.show();
	}

	private void VerPedido() {
		LlenaListaDetalle(null, null, null);
	}

	private void IniciaPedido(DataBase objeto) {
		LlenaEncabezado();
		LlenaDetallePedido();
		
		LlenaLista(objeto, cajaTemp, unidadTemp);
		//GeneraPedido();
	}

	private void LlenaDetallePedido() {
		ConexionDB conexion = new ConexionDB();
		DetallePedido[] detalle = null;
		
		detalle = conexion.verDetalle(VerDetalle.this, getNuevoPedido().getEncabezado().getCodigoPedidoTemp());
		//System.out.println("VERDETALLE, datos del detalle = " + detalle[1].getBonificacion());
		getNuevoPedido().setDetallePedido(detalle);
		System.out.println("VERDETALLE, copio los detalles");
	}

	private void LlenaEncabezado() {
		ConexionDB conexion = new ConexionDB();
		EncabezadoPedido[] encabezado = null;
		encabezado = conexion.VerPedidos(VerDetalle.this);
		int tamano = encabezado.length;
		for (int i = 0; i < tamano; i++){
			if(encabezado[i].getCodigoCliente().equalsIgnoreCase(getIdCliente())){
				getNuevoPedido().setEncabezado(encabezado[i]);
			}
		}
		
	}

	private void LlenaLista(DataBase objeto, String caja, String unidad) {
		setSimpleAdapter( 
        	new SimpleAdapter(this, 
        					  getListaProductos().ListaProductosPrueba(getNuevoPedido().getDetallePedido()),	
        					  R.layout.fila_lista_productos,
        					  new String[] {"codigoProducto",
        									"nombreProducto", 
        									"cajas",
											"unidades",
											"valor",
											"presentacion",
											"existencia",
											"bonificacion",
											"total"},
        					  new int[] {R.id.fila_lista_producto_codigo_textview, 
        								 R.id.fila_lista_producto_nombreProducto_textview, 
        								 R.id.fila_lista_producto_cajas_textview,
        								 R.id.fila_lista_producto_unidades_textview,
        								 R.id.fila_lista_producto_precioi_textview,
        								 R.id.fila_lista_producto_presentacion_textview,
        								 R.id.fila_lista_producto_existencia_textview,
        								 R.id.fila_lista_producto_bonificacion_textview,
        								 R.id.fila_lista_producto_valor_textview}));
		
		getListaProductosListView().setAdapter(getSimpleAdapter());
    }
	
	private void LlenaListaDetalle(DataBase objeto, String caja, String unidad) {
		setSimpleAdapter( 
        	new SimpleAdapter(this, 
        					  //getListaClientes().ListaClientes(),
        					  getListaProductos().ListaProductosDetalle(getNuevoPedido().getDetallePedido()),	
        					  R.layout.fila_lista_productos,
        					  new String[] {"codigoProducto",
        									"nombreProducto", 
        									"cajas",
											"unidades",
											"valor",
											"presentacion",
											"existencia",
											"bonificacion",
											"total"},
        					  new int[] {R.id.fila_lista_producto_codigo_textview, 
        								 R.id.fila_lista_producto_nombreProducto_textview, 
        								 R.id.fila_lista_producto_cajas_textview,
        								 R.id.fila_lista_producto_unidades_textview,
        								 R.id.fila_lista_producto_precioi_textview,
        								 R.id.fila_lista_producto_presentacion_textview,
        								 R.id.fila_lista_producto_existencia_textview,
        								 R.id.fila_lista_producto_bonificacion_textview,
        								 R.id.fila_lista_producto_valor_textview}));
		
		getListaProductosListView().setAdapter(getSimpleAdapter());
    }
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public ListaTest getListaProductos() {
		return listaProductos;
	}
	public void setListaProductos(ListaTest listaProductos) {
		this.listaProductos = listaProductos;
	}
	public ListView getListaProductosListView() {
		return listaProductosListView;
	}
	public void setListaProductosListView(ListView listaProductosListView) {
		this.listaProductosListView = listaProductosListView;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	public NuevoPedido getNuevoPedido() {
		return nuevoPedido;
	}

	public void setNuevoPedido(NuevoPedido nuevoPedido) {
		this.nuevoPedido = nuevoPedido;
	}

	public EncabezadoPedido getEncabezadoPedido() {
		return encabezadoPedido;
	}

	public void setEncabezadoPedido(EncabezadoPedido encabezadoPedido) {
		this.encabezadoPedido = encabezadoPedido;
	}

	public DetallePedido[] getDetallePedido() {
		return detallePedido;
	}

	public void setDetallePedido(DetallePedido[] detallePedido) {
		this.detallePedido = detallePedido;
	}

	public TextView getTotalPedidoTextView() {
		return totalPedidoTextView;
	}

	public void setTotalPedidoTextView(TextView totalPedidoTextView) {
		this.totalPedidoTextView = totalPedidoTextView;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		getSimpleAdapter().getFilter().filter(s);
        getSimpleAdapter().notifyDataSetChanged();
	}

	public SimpleAdapter getSimpleAdapter() {
		return simpleAdapter;
	}

	public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
		this.simpleAdapter = simpleAdapter;
	}

	public EditText getBuscarEditText() {
		return buscarEditText;
	}

	public void setBuscarEditText(EditText buscarEditText) {
		this.buscarEditText = buscarEditText;
	}
	
	private void ActualizarLista() {
		LlenaLista(null, null, null);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		@SuppressWarnings("unchecked")
        HashMap<String, String> selected = (HashMap<String, String>) getSimpleAdapter().getItem(position);
        String codigoProducto = (String) selected.get("codigoProducto");
        String cajas = (String) selected.get("cajas");
        String unidades = (String) selected.get("unidades");
        Producto productoTemp = new Producto();
        productoTemp = ProductoSeleccionado(codigoProducto);
        VerDialog(productoTemp, cajas, unidades);
   }

		private Producto ProductoSeleccionado(String codigoProducto) {
		for (int i = 0; i < getResultado().getProducto().length; i++){
			if (getResultado().getProducto()[i].getArtCodigo().equalsIgnoreCase(codigoProducto)){
				return getResultado().getProducto()[i];
			}
		}
		return null;
	}

		private void VerDialog(final Producto producto, String cajas, String unidades) {
			setCodigo(producto.getArtCodigo());
			setCaja(cajas);
			setUnidad(unidades);
			LayoutInflater factory = LayoutInflater.from(VerDetalle.this);            
         
			final View textEntryView = factory.inflate(R.layout.tomar_pedido_dialog, null);
 			final EditText cajaEditText = (EditText) textEntryView.findViewById(R.id.tomarpedido_dialog_caja_edittext);
 			final EditText unidadEditText = (EditText) textEntryView.findViewById(R.id.tomarpedido_dialog_unidad_edittext);
 			final ImageButton eliminarImageButton = (ImageButton) textEntryView.findViewById(R.id.tomar_pedido_dialog_eliminar_imagebutton);
 			final TextView precioTextViewDialog = (TextView) textEntryView.findViewById(R.id.precioTextViewDialog);
 			setPrecioSeleccionado(0);
 			
 			FormatDecimal formatDecimal = new FormatDecimal();
 			//Llenar String[] para el spinner
 			//final String precios[] = new String[getCliente().getDescuentos() + 1];
 			//final float precioAplicado = 0;
 			//int posicionPrecio = 0;
 			setPrecioSeleccionado(producto.getArtPrecio());
 			
 			if (getCliente().getCliDescuento1().equalsIgnoreCase("1")){
 				setPrecioSeleccionado(producto.getArtDescuento1());
 			}else if (getCliente().getCliDescuento2().equalsIgnoreCase("1")){
 				setPrecioSeleccionado(producto.getArtDescuento2());
 			}else if (getCliente().getCliDescuento3().equalsIgnoreCase("1")){
 				setPrecioSeleccionado(producto.getArtDescuento3());
 			}
 	         precioTextViewDialog.setText(formatDecimal.ConvierteFloat(getPrecioSeleccionado()));
 	         
 		 //Quitar los ceros de caja y unidad para mostrar en el dialog
         if (getCaja().equalsIgnoreCase("0")){
        	 cajaEditText.setText("");
         }else{
        	 cajaEditText.setText(getCaja());
         }
         
         if (getUnidad().equalsIgnoreCase("0")){
        	 unidadEditText.setText("");
         }else{
        	 unidadEditText.setText(getUnidad());
         }
         
         
         final AlertDialog.Builder alert = new AlertDialog.Builder(VerDetalle.this);
         
         alert.setTitle(producto.getArtDescripcion());
         alert.setView(textEntryView);
         alert.setPositiveButton("   OK   ", new DialogInterface.OnClickListener()
         {
             public void onClick(DialogInterface dialog, int whichButton)
             {
            	setCajaTemp(cajaEditText.getText().toString());
                setUnidadTemp(unidadEditText.getText().toString());
                int cursor = 0;
             	boolean fin = false;
             	while (fin == false){
             		if (getNuevoPedido().getDetallePedido()[cursor].getCodigoProducto().equalsIgnoreCase(getCodigo())){
             			getNuevoPedido().getDetallePedido()[cursor].setPrecioSeleccionado(getPrecioSeleccionado());
             			ActualizaArticulo(cursor);
             			CalculaTotal();
                     	fin = true;
             		}
             		cursor++;
             	}
             	ActualizarLista();
             	CalculaTotal();
             }

			private void CalculaTotal() {
				int tamano = getNuevoPedido().getDetallePedido().length;
				int caja = 0;
				int unidad = 0;
				float subtotal = 0;
				int unidadesCaja = 0;
				float precio = 0;
				float totalTemp = 0;
				setTotalPedido(0);
				
				for (int i = 0; i < tamano; i++){
					totalTemp = getTotalPedido();
					//precio = getNuevoPedido().getDetallePedido()[i].getPrecio();
					precio = getNuevoPedido().getDetallePedido()[i].getPrecioSeleccionado();
					caja = Integer.parseInt(getNuevoPedido().getDetallePedido()[i].getCaja());
					unidad = Integer.parseInt(getNuevoPedido().getDetallePedido()[i].getUnidad());
					unidadesCaja = Integer.parseInt(getNuevoPedido().getDetallePedido()[i].getUnidadesCaja());
					getNuevoPedido().getDetallePedido()[i].setTotalUnidades((caja * unidadesCaja) + unidad);
					subtotal = precio * getNuevoPedido().getDetallePedido()[i].getTotalUnidades();
					setTotalPedido(subtotal + totalTemp);
					getNuevoPedido().getDetallePedido()[i].setSubTotal(String.valueOf(subtotal));
				}
				getTotalPedidoTextView().setText(getFormatDecimal().ConvierteFloat(getTotalPedido()));
			}

			private void ActualizaArticulo(int cursor) {
				
				if (getCajaTemp().equalsIgnoreCase("")){
     				getNuevoPedido().getDetallePedido()[cursor].setCaja("0");
         		}else{
         			getNuevoPedido().getDetallePedido()[cursor].setCaja(getCajaTemp());
         		}
     			
     			if (getUnidadTemp().equalsIgnoreCase("")){
     				getNuevoPedido().getDetallePedido()[cursor].setUnidad("0");
     			}else{
     				getNuevoPedido().getDetallePedido()[cursor].setUnidad(getUnidadTemp());
     			}
     		}
         });

         alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener()
         {
             public void onClick(DialogInterface dialog, int whichButton)
             {
             // Canceled.
             }
         });
         
         eliminarImageButton.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				cajaEditText.setText("");
 				unidadEditText.setText("");
 				
 			}
 		});
         
         alert.show();
			
		}

		public String getCajaTemp() {
			return cajaTemp;
		}

		public void setCajaTemp(String cajaTemp) {
			this.cajaTemp = cajaTemp;
		}

		public String getUnidadTemp() {
			return unidadTemp;
		}

		public void setUnidadTemp(String unidadTemp) {
			this.unidadTemp = unidadTemp;
		}

		public float getTotalPedido() {
			return totalPedido;
		}

		public void setTotalPedido(float totalPedido) {
			this.totalPedido = totalPedido;
		}

		public ImageButton getBorrarImageButton() {
			return borrarImageButton;
		}

		public void setBorrarImageButton(ImageButton borrarImageButton) {
			this.borrarImageButton = borrarImageButton;
		}

		public boolean isControlMenu() {
			return controlMenu;
		}

		public void setControlMenu(boolean controlMenu) {
			this.controlMenu = controlMenu;
		}

		public Fecha getFecha() {
			return fecha;
		}

		public void setFecha(Fecha fecha) {
			this.fecha = fecha;
		}

		public Cliente getCliente() {
			return cliente;
		}

		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}

		public float getPrecioSeleccionado() {
			return precioSeleccionado;
		}

		public void setPrecioSeleccionado(float precioSeleccionado) {
			this.precioSeleccionado = precioSeleccionado;
		}

		public FormatDecimal getFormatDecimal() {
			return formatDecimal;
		}

		public void setFormatDecimal(FormatDecimal formatDecimal) {
			this.formatDecimal = formatDecimal;
		}

		public String getUsuario() {
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}

		public String getClave() {
			return clave;
		}

		public void setClave(String clave) {
			this.clave = clave;
		}

		public String getCodigoPedido() {
			return codigoPedido;
		}

		public void setCodigoPedido(String codigoPedido) {
			this.codigoPedido = codigoPedido;
		}

}
