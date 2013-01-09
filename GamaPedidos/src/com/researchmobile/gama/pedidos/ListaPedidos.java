package com.researchmobile.gama.pedidos;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.utilidades.ListaTest;

public class ListaPedidos extends Activity{
	private EncabezadoPedido[] pedido;
	private ListaTest listaPedidos;
	private SimpleAdapter simpleAdapter;
	private ListView pedidosListView;
	
	private Resultado resultado;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listapedidos);
        Bundle bundle = getIntent().getExtras();
        setResultado((Resultado)bundle.get("resultado"));
        
        
        setPedidosListView((ListView)findViewById(R.id.lista_pedidos_pedidos_listview));
        setListaPedidos(new ListaTest());
        
        setSimpleAdapter( 
            	new SimpleAdapter(this, 
            					  getListaPedidos().ListaPedidos(this),
            					  R.layout.fila_lista_pedidos,
            					  new String[] {"pedido",
            									"codigo",
            									"total",
            									"hora"}, 
            					  new int[] {R.id.fila_lista_pedidos_pedido,
            								 R.id.fila_lista_pedidos_codigo,
            								 R.id.fila_lista_pedidos_total, 
            								 R.id.fila_lista_pedidos_hora}));
            
        getPedidosListView().setAdapter(getSimpleAdapter());
        getPedidosListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> selected = (HashMap<String, String>) getSimpleAdapter().getItem(position);
				String pedido = (String) selected.get("pedido");
                String codigo = (String) selected.get("codigo");
                String total = (String) selected.get("total");
                if (total.equalsIgnoreCase("0.00")){
                	Toast.makeText(getBaseContext(), "ESTE CLEINTE NO REALIZO COMPRA", Toast.LENGTH_LONG).show();
                }else{
                	VerDialog(pedido, codigo);
                	/*Intent intent = new Intent(ListaPedidos.this, VerDetalle.class);
                    intent.putExtra("resultado", getResultado());
                    intent.putExtra("codigoCliente", codigo);
                    intent.putExtra("total", Float.parseFloat(total));
                    startActivity(intent);
                    */
                }
                
				
			}
		});
        
	}

	protected void VerDialog(String pedido2, String codigo) {
		new AlertDialog.Builder(this)
	      .setTitle("ELIMINAR PEDIDO")
	      .setMessage("Esta seguro que desea eliminar el pedido número " + pedido2 + " del cliente " + codigo + "?")
	      .setPositiveButton("   SI   ", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int whichButton) {
	            	  
	              }
	      })
	      .setNegativeButton("   NO   ", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
            	  
              }
      })
	      .show();
	}

	public EncabezadoPedido[] getPedido() {
		return pedido;
	}

	public void setPedido(EncabezadoPedido[] pedido) {
		this.pedido = pedido;
	}

	public ListaTest getListaPedidos() {
		return listaPedidos;
	}

	public void setListaPedidos(ListaTest listaPedidos) {
		this.listaPedidos = listaPedidos;
	}

	public SimpleAdapter getSimpleAdapter() {
		return simpleAdapter;
	}

	public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
		this.simpleAdapter = simpleAdapter;
	}

	public ListView getPedidosListView() {
		return pedidosListView;
	}

	public void setPedidosListView(ListView pedidosListView) {
		this.pedidosListView = pedidosListView;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}
}
