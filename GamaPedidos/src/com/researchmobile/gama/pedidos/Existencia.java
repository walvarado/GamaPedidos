package com.researchmobile.gama.pedidos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.researchmobile.gama.entidades.Producto;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.utilidades.ListaTest;

public class Existencia extends Activity{
	private Producto[] producto;
	private ListaTest listaProductos;
	private SimpleAdapter simpleAdapter;
	private ListView existenciaListView;
	
	private Resultado resultado;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existencia);
        
        Bundle bundle = getIntent().getExtras();
        //setProducto((Producto[])bundle.get("productos"));
        setResultado((Resultado)bundle.get("resultado"));
        setExistenciaListView((ListView)findViewById(R.id.existencia_listview));
        setListaProductos(new ListaTest());
        
        setSimpleAdapter( 
            	new SimpleAdapter(this, 
            					  getListaProductos().ListaProductos(getResultado().getProducto()),
            					  R.layout.fila_lista_existencia,
            					  new String[] {"nombre",
            									"precio",
            									"existencia"}, 
            					  new int[] {R.id.fila_lista_existencia_nombre_textview,
            								 R.id.fila_lista_existencia_precio_textview, 
            								 R.id.fila_lista_existencia_existencia_textview}));
            
        getExistenciaListView().setAdapter(getSimpleAdapter());
        
        
	}

	public ListaTest getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(ListaTest listaProductos) {
		this.listaProductos = listaProductos;
	}

	public SimpleAdapter getSimpleAdapter() {
		return simpleAdapter;
	}

	public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
		this.simpleAdapter = simpleAdapter;
	}

	public Producto[] getProducto() {
		return producto;
	}

	public void setProducto(Producto[] producto) {
		this.producto = producto;
	}

	public ListView getExistenciaListView() {
		return existenciaListView;
	}

	public void setExistenciaListView(ListView existenciaListView) {
		this.existenciaListView = existenciaListView;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}
}
