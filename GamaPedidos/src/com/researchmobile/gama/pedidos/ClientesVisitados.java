package com.researchmobile.gama.pedidos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.utilidades.ListaTest;

public class ClientesVisitados extends Activity{
	
	private EncabezadoPedido[] clientes;
	private ListaTest listaClientes;
	private SimpleAdapter simpleAdapter;
	private ListView clientesListView;
	private Resultado resultado;
	private int clientesVisitados;
	private int generalClientes;
	private TextView visitadosTextView;
	private TextView generalTextView;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientesvisitados);
        
        Bundle bundle = getIntent().getExtras();
        setClientesVisitados((int)bundle.getInt("visitados"));
        setGeneralClientes((int)bundle.getInt("general"));
        
        setVisitadosTextView((TextView)findViewById(R.id.clientesvisitados_visitados_textview));
        setGeneralTextView((TextView)findViewById(R.id.clientesvisitados_general_textview));
        getVisitadosTextView().setText(String.valueOf(getClientesVisitados()));
        getGeneralTextView().setText(String.valueOf(getGeneralClientes()));
        setClientesListView((ListView)findViewById(R.id.clientes_visitados_listview));
        setListaClientes(new ListaTest());
        setSimpleAdapter( 
            	new SimpleAdapter(this, 
            					  getListaClientes().ListaClientesVisitados(this),
            					  R.layout.fila_lista_clientes_visitados,
            					  new String[] {"cliente",
            									"total",
            									"hora"}, 
            					  new int[] {R.id.clientes_visitados_cliente_textview,
            								 R.id.clientes_visitados_total_textview, 
            								 R.id.clientes_visitados_hora_textview}));
            
        getClientesListView().setAdapter(getSimpleAdapter());
        
        
        
	}
	
	
	public SimpleAdapter getSimpleAdapter() {
		return simpleAdapter;
	}
	public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
		this.simpleAdapter = simpleAdapter;
	}
	
	public Resultado getResultado() {
		return resultado;
	}
	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}
	public ListView getClientesListView() {
		return clientesListView;
	}
	public void setClientesListView(ListView clientesListView) {
		this.clientesListView = clientesListView;
	}


	public EncabezadoPedido[] getClientes() {
		return clientes;
	}


	public void setClientes(EncabezadoPedido[] clientes) {
		this.clientes = clientes;
	}


	public ListaTest getListaClientes() {
		return listaClientes;
	}


	public void setListaClientes(ListaTest listaClientes) {
		this.listaClientes = listaClientes;
	}


	public int getClientesVisitados() {
		return clientesVisitados;
	}


	public void setClientesVisitados(int clientesVisitados) {
		this.clientesVisitados = clientesVisitados;
	}


	public int getGeneralClientes() {
		return generalClientes;
	}


	public void setGeneralClientes(int generalClientes) {
		this.generalClientes = generalClientes;
	}


	public TextView getVisitadosTextView() {
		return visitadosTextView;
	}


	public void setVisitadosTextView(TextView visitadosTextView) {
		this.visitadosTextView = visitadosTextView;
	}


	public TextView getGeneralTextView() {
		return generalTextView;
	}


	public void setGeneralTextView(TextView generalTextView) {
		this.generalTextView = generalTextView;
	}
	
}