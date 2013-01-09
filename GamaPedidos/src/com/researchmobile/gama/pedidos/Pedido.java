package com.researchmobile.gama.pedidos;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.Fecha;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.utilidades.ListaTest;
import com.researchmobile.gama.utilidades.Listas;

public class Pedido extends Activity implements TextWatcher, OnItemClickListener {
	
	private static int VISITADO = 1;
	private static int NO_VISITADO = 0;
	
	private ListView clientesListView;
	private TextView semanaTextView;
	private TextView diaTextView;
	private Listas listaClientes;
	private ListaTest listaClientesTest;
	private Cliente cliente[];
	private Resultado resultado;
	private SimpleAdapter simpleAdapter;
	private ImageButton borrarImageButton;
	private int estado;
	
	private EditText buscarEditText;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido);
        
        
        setBuscarEditText((EditText)findViewById(R.id.pedido_buscar_editText));
        getBuscarEditText().addTextChangedListener(this);
        
        setEstado(NO_VISITADO);
        
        Bundle bundle = getIntent().getExtras();
        setResultado((Resultado)bundle.get("resultado"));
        
        Fecha fecha = new Fecha();
        getResultado().setSemana(fecha.Semana());
        getResultado().setDia(fecha.Dia());
        
        setListaClientes(new Listas());
        setListaClientesTest(new ListaTest());
        
        
        setBorrarImageButton((ImageButton)findViewById(R.id.pedido_borrar_imagebutton));
        getBorrarImageButton().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LimpiarBusqueda();
				
			}
		});
        
        setSemanaTextView((TextView)findViewById(R.id.pedido_semana_textview));
        setDiaTextView((TextView)findViewById(R.id.pedido_dia_textview));
        
        getSemanaTextView().setText("SEMANA " + String.valueOf(getResultado().getSemana()));
        getDiaTextView().setText(getResultado().getDia());
        setClientesListView((ListView)findViewById(R.id.pedido_lista_clientes_listView));
        
        fillData();
        
        
    }
	
	private void fillData() {
		System.out.println("PEDIDO, estado = " + getEstado());
		setSimpleAdapter( 
	        	new SimpleAdapter(this, 
	        					  //getListaClientes().ListaClientes(),
	        					  getListaClientesTest().ListaClientes(getResultado().getCliente(), getEstado()),	
	        					  R.layout.fila_lista_clientes,
	        					  new String[] {"codigoCliente",
	        									"empresa",
	        									"contacto",
	        									"direccion",
	        									"telefono",
	        									"nit"}, 
	        					  new int[] {R.id.fila_lista_clientes_codigo_cliente_textview,
	        								 R.id.fila_lista_clientes_empresa_textview, 
	        								 R.id.fila_lista_clientes_contacto_textview,
	        								 R.id.fila_lista_clientes_codigo_direccion_textview,
	        								 R.id.fila_lista_clientes_telefono_textview,
	        								 R.id.fila_lista_clientes_nit_textview}));
	        
	        getClientesListView().setAdapter(getSimpleAdapter());
	        
	        //Al hacer click a un registro 
	        getClientesListView().setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	               
	            	@SuppressWarnings("unchecked")
	               HashMap<String, String> selected = (HashMap<String, String>) getSimpleAdapter().getItem(position);
	               String idCliente = (String) selected.get("codigoCliente");
	               
	               Intent intent = new Intent(Pedido.this, DetalleCliente.class);
	               intent.putExtra("codigoCliente", idCliente);
	               intent.putExtra("resultado", getResultado());
	               startActivity(intent);
	          }
		 });
		
	}

	//Inabilitar el boton back <--
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	      if (keyCode == KeyEvent.KEYCODE_BACK) {
	            // Preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR     
	            return true;
	      }
	      
	      return super.onKeyDown(keyCode, event);
	    }

	
	/**
	 * MENU
	 */

	// To change item text dynamically
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pedido_menu, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.pedido_menu_clientesvisitados_opcion:
			ClientesVisitadosActivity();

			return true;
		case R.id.pedido_menu_listapedidos_opcion:
			ListaPedidosActivity();
			return true;
			
		case R.id.pedido_menu_salir_opcion:
			Salir();
			return true;
			
		case R.id.pedido_menu_sincronizar_opcion:
			Sincronizar();
			//Productos();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void Sincronizar() {
		
		      new AlertDialog.Builder(Pedido.this)
		      .setTitle("SINCRONIZAR DATOS")
		      .setMessage("Esta Seguro que desa sincronizar los datos, Esto borrará todo lo trabajado en este día.")
		      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface dialog, int whichButton) {
		            	  Toast.makeText(getBaseContext(), "Sincronizando", Toast.LENGTH_SHORT).show();
		              }
		      })
		      .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getBaseContext(), "Proceso de Sincronizacion Cancelado", Toast.LENGTH_SHORT).show();
					
				}
			})
		      .show();
	}
	
	private void Productos() {
		//Toast.makeText(getBaseContext(), "En proceso de Desarrollo", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(Pedido.this, Existencia.class);
		//intent.putExtra("productos", getResultado().getProducto());
		intent.putExtra("resultado", getResultado());
		startActivity(intent);
	}

	private void LimpiarBusqueda() {
		getBuscarEditText().setText("");
		
	}

	private void Salir() {
		Intent intent = new Intent(Pedido.this, Login.class);
		startActivity(intent);
		
	}

	private void ListaPedidosActivity() {
		ConexionDB conexionDb = new ConexionDB();
		EncabezadoPedido pedido[] = conexionDb.VerPedidos(this);
		if (pedido == null){
			Toast.makeText(getBaseContext(), "NO SE HAN REALIZADO PEDIDOS", Toast.LENGTH_SHORT).show();
		}else{
			Intent listaPedidosIntent = new Intent(Pedido.this, ListaPedidos.class);
			listaPedidosIntent.putExtra("resultado", getResultado());
			
			startActivity(listaPedidosIntent);
		}
		
		//Toast.makeText(getBaseContext(), "En proceso de desarrollo", Toast.LENGTH_LONG).show();
		
	}

	private void ClientesVisitadosActivity() {
		
		if (getEstado()==VISITADO){
			setEstado(NO_VISITADO);
		}else{
			setEstado(VISITADO);
		}
		
		fillData();
		
		/*
		ConexionDB conexionDb = new ConexionDB();
		EncabezadoPedido pedido[] = conexionDb.VerPedidos(this);
		if (pedido == null){
			Toast.makeText(getBaseContext(), "NO HAY CLIENTES VISITADOS DE HOY", Toast.LENGTH_SHORT).show();
		}else{
			Intent clientesVisitadosIntent = new Intent(Pedido.this, ClientesVisitados.class);
			clientesVisitadosIntent.putExtra("visitados", pedido.length);
			clientesVisitadosIntent.putExtra("general", getResultado().getCliente().length);
			startActivity(clientesVisitadosIntent);
			//Toast.makeText(getBaseContext(), "En proceso de desarrollo", Toast.LENGTH_LONG).show();
		}
		*/
		
	}

	public Listas getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(Listas listaClientes) {
		this.listaClientes = listaClientes;
	}

	public ListView getClientesListView() {
		return clientesListView;
	}

	public void setClientesListView(ListView clientesListView) {
		this.clientesListView = clientesListView;
	}

	public TextView getSemanaTextView() {
		return semanaTextView;
	}

	public void setSemanaTextView(TextView semanaTextView) {
		this.semanaTextView = semanaTextView;
	}

	public TextView getDiaTextView() {
		return diaTextView;
	}

	public void setDiaTextView(TextView diaTextView) {
		this.diaTextView = diaTextView;
	}

	public ListaTest getListaClientesTest() {
		return listaClientesTest;
	}

	public void setListaClientesTest(ListaTest listaClientesTest) {
		this.listaClientesTest = listaClientesTest;
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

	public EditText getBuscarEditText() {
		return buscarEditText;
	}

	public void setBuscarEditText(EditText buscarEditText) {
		this.buscarEditText = buscarEditText;
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	public SimpleAdapter getSimpleAdapter() {
		return simpleAdapter;
	}

	public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
		this.simpleAdapter = simpleAdapter;
	}

	public ImageButton getBorrarImageButton() {
		return borrarImageButton;
	}

	public void setBorrarImageButton(ImageButton borrarImageButton) {
		this.borrarImageButton = borrarImageButton;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

}
