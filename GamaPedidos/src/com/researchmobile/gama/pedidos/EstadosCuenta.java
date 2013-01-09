package com.researchmobile.gama.pedidos;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.db.DataBase;
import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.EstadoDeCuenta;
import com.researchmobile.gama.entidades.Fecha;
import com.researchmobile.gama.entidades.Pago;
import com.researchmobile.gama.utilidades.FormatDecimal;
import com.researchmobile.gama.utilidades.ListaTest;

public class EstadosCuenta extends Activity implements TextWatcher, OnItemClickListener{
	
 private ListView cuentasListView;
 private ListView pagosListView;
 //private TextView pruebaTextView;
 private SimpleAdapter simpleAdapter;
 private SimpleAdapter pagosSimpleAdapter;
 private ListaTest listaEstadosDeCuentaTest; // para guardar el ArrayList
 private ListaTest listaPagosTest;
 private ListView estadosDeCuentaListView;
 private EstadoDeCuenta[] cuentas;
 private Pago[] pago;
 private Cliente cliente;
 private TextView saldo;
 private TextView saldoDetalle;
 private TextView documentoDetalle;
 public String saldoDetalleGlobal;
 public String movDocumentoGlobal;
 
 public void onCreate(Bundle savedInstanceState){
	 super.onCreate(savedInstanceState);
     setContentView(R.layout.estados_de_cuenta);
	 
	 Bundle bundle = getIntent().getExtras();
	 setCliente((Cliente)bundle.get("cliente"));
	 
	 //System.out.println(getCliente().getEstadoCuentas()[1].getMovdocumento());
	 //Toast.makeText(getBaseContext(), String.valueOf(getCliente().getEstadoCuentas().length), Toast.LENGTH_LONG).show();
	 //setSaldo((TextView)findViewById(R.id.estados_de_cuenta_dialog_total_text));
	 setSaldo((TextView)findViewById(R.id.estados_de_cuenta_saldo_textview));
	 setDocumentoDetalle((TextView)findViewById(R.id.estados_de_cuenta_detalle_documento_textview));
	 setSaldoDetalle((TextView)findViewById(R.id.estados_de_cuenta_detalle_saldo_textview));
	// int tamano = getCliente().getEstadoCuentas().length;
	 String saldoTotalcli = calculaSaldoTotal();
	 getSaldo().setText(saldoTotalcli);
	
	// Pago pagos[] = new Pago[1];
	 
	
	 //getCliente().getEstadoCuentas()[0].getPago()[0].setMovDocumento("ENV000000000226");
	 //getCliente().getEstadoCuentas()[0].getPago()[0].setAbono(100);
	 actualizaCuentas();
	 muestraLista();
	// Id	@+id/estados_de_cuenta_detalle_listView
	
 }
 
 private void actualizaCuentas() {
	// TODO Auto-generated method stub
	 
 final DataBase objeto = new DataBase(EstadosCuenta.this);
	 
	 try {
		objeto.abrir();		 
		
		 int tamanoCuentas = getCliente().getEstadoCuentas().length;
		for(int i=0; i<tamanoCuentas; i++)
		{
			 Cursor c = objeto.consultaSumaAbonos(getCliente().getEstadoCuentas()[i].getMovdocumento());
				 
				c.moveToFirst();
				Float suma = Float.parseFloat(c.getString(0));
				getCliente().getEstadoCuentas()[i].setPagado(Float.toString(suma));
				//Float saldo = Float.parseFloat(getCliente().getEstadoCuentas()[i].getSaldo());
				Float credit = Float.parseFloat(getCliente().getEstadoCuentas()[i].getCredito());
				//getCliente().getEstadoCuentas()[i].
				getCliente().getEstadoCuentas()[i].setSaldo(Float.toString(credit-suma));
				                                
		}
		
			
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		Toast.makeText(getBaseContext(), "NO SE HAN REALIZADO PAGOS \n EN ALGUNAS CUENTAS", Toast.LENGTH_LONG).show();
	}
	 
	 
	 objeto.cerrar();
}

public void muestraLista(){
	 
	 setCuentasListView((ListView)findViewById(R.id.pedido_lista_estados_cuenta_listView));
	 setListaEstadosDeCuentaTest(new ListaTest());
	 
	 
		setSimpleAdapter( 
	         	new SimpleAdapter(this, 
	         					  //getListaClientes().ListaClientes(),
	         					  getListaEstadosDeCuentaTest().ListaEstadosDeCuenta(getCliente().getEstadoCuentas()),	
	         					  R.layout.fila_lista_estados_de_cuenta,
	         					  new String[] {"movDocumento",
	         									"fecha",
	         									"total",
	         									"pagado",
	         									"saldo"}, 
	         					  new int[] {R.id.fila_lista_estados_de_cuenta_mov_documento,
	         								 R.id.fila_lista_estados_de_cuenta_fecha,
	         								 R.id.fila_lista_estados_de_cuenta_total,
	         								 R.id.fila_lista_estados_de_cuenta_pagado,
	         								 R.id.fila_lista_estados_de_cuenta_saldo}));
	         
	         getCuentasListView().setAdapter(getSimpleAdapter());
	         
	         getCuentasListView().setOnItemClickListener(new OnItemClickListener() {
	             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                
	             	@SuppressWarnings("unchecked")
	                HashMap<String, String> selected = (HashMap<String, String>) getSimpleAdapter().getItem(position);
	                String movDocumento = (String) selected.get("movDocumento");
	                String saldo = (String) selected.get("saldo");
	                String total = (String) selected.get("total");
	               //VerDialog(saldo, movDocumento);
	                movDocumentoGlobal = movDocumento;
	                saldoDetalleGlobal = saldo;
	                getDocumentoDetalle().setText(movDocumento);
	                getSaldoDetalle().setText(saldo);
	                cargaPagos(movDocumento);
	                muestraPagos(saldo, movDocumento);
	             
	           }
	 	 });
 }
 
 private void muestraPagos(final String saldo, final String movDocumento){
		
		setPagosListView((ListView)findViewById(R.id.estados_de_cuenta_detalle_listView));
		 setListaPagosTest(new ListaTest());
		 
		 
			setPagosSimpleAdapter( 
		         	new SimpleAdapter(this, 
		         					  //getListaClientes().ListaClientes(),
		         					  getListaPagosTest().ListaPagos(getPago()),	
		         					  R.layout.fila_lista_detalle_estados_de_cuenta,
		         					  new String[] {"noDocumento",
		         									"fecha",
		         									"abono",
		         									"tipoDocumento"}, 
		         					  new int[] {R.id.fila_lista_detalle_estados_de_cuenta_no_documento,
		         								 R.id.fila_lista_detalle_estados_de_cuenta_fecha,
		         								 R.id.fila_lista_detalle_estados_de_cuenta_abono,
		         								 R.id.fila_lista_detalle_estados_de_cuenta_tipo_documento}));
		         
		         getPagosListView().setAdapter(getPagosSimpleAdapter());
		         
		
	} 
 
 public void cargaPagos(String movDocumento){
	 final DataBase objeto = new DataBase(EstadosCuenta.this);
	 
	 try {
		objeto.abrir();
		
		//objeto.limpiarDB();
		// objeto.insertPago(movDocumento, "1", "2.00", "10/5/2012", "1");
		// objeto.insertPago(movDocumento, "2", "5.00", "11/5/2012", "1");

		 
		 Cursor c = objeto.consultaPagos(movDocumento);
		 	//int iFila = c.getColumnIndex("mov");
			int tamanoPagos = c.getCount();
			Pago pagos[] = new Pago[tamanoPagos];
			 
			 int contador = 0;
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Pago pagoTemp = new Pago();
				//resultado = resultado + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + "SABER";
				 pagoTemp.setMovDocumento(c.getString(0));
				 pagoTemp.setNoDocumento(c.getString(1));
				 pagoTemp.setFecha(c.getString(2));
				 pagoTemp.setAbono(Float.parseFloat(c.getString(3).toString()));
				 pagoTemp.setTipoDocumento(c.getString(4));
				 
				 pagos[contador] = pagoTemp;
				 contador++;
			}
			setPago(pagos);
		
			
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		Toast.makeText(getBaseContext(), "NO SE HAN REALIZADO ABONOS", Toast.LENGTH_LONG).show();
	}
	 
	 
	 objeto.cerrar();
 }

 
 /**
	 * MENU
	 */

	// To change item text dynamically
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.getItem(0).setEnabled(false);
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.estado_de_cuenta_menu, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.estado_de_cuenta_menu_guardar_pago:
			Guardar();
			return true;
		case R.id.estado_de_cuenta_menu_abono:
			VerDialog(saldoDetalleGlobal, movDocumentoGlobal);
			
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
private void Guardar() {
		// TODO Auto-generated method stub	
	
	ConexionDB conexionCliente = new ConexionDB();
	conexionCliente.GuardaEstadoDeCuenta(getCliente().getEstadoCuentas(), EstadosCuenta.this); // Guarda en la base de datos los ESTADOS DE CUENTA nuevos
	Toast.makeText(getBaseContext(), "Se guardaron los datos en la BD \n por error de conexion", Toast.LENGTH_LONG).show();
	}





private void VerDialog(final String saldo, final String movDocumento) {
	
	
	LayoutInflater factory = LayoutInflater.from(EstadosCuenta.this);            
 
	final View textEntryView = factory.inflate(R.layout.estados_cuenta_dialog, null);
		final EditText abonoEditText = (EditText) textEntryView.findViewById(R.id.estados_de_cuenta_dialog_abono);
		final EditText documentoEditText = (EditText) textEntryView.findViewById(R.id.estados_de_cuenta_dialog_documento);
		final TextView saldoTextView = (TextView) textEntryView.findViewById(R.id.estados_de_cuenta_dialog_saldo_textview);
		
		//String saldoTotal  = calculaSaldoTotal();
		saldoTextView.setText(saldo);
		abonoEditText.setText("0");
 
 AlertDialog.Builder alert = new AlertDialog.Builder(EstadosCuenta.this);

 alert.setTitle("NUEVO MOVIMIENTO");
 alert.setMessage("INGRESE LOS DATOS\n DEL ABONO ");
 alert.setView(textEntryView);
 alert.setPositiveButton("AGREGAR", new DialogInterface.OnClickListener()
 {
     public void onClick(DialogInterface dialog, int whichButton)
     {
    	 try
    	 {
    	 float limiteCredito;
    	 limiteCredito = Float.parseFloat(getCliente().getCliLimite());
    	 
    	 float saldoActual = Float.parseFloat(saldo) ;
    	 float nuevoCredito = limiteCredito - saldoActual;
    	 String credito = Float.toString(nuevoCredito);
    	 String abonoString = abonoEditText.getText().toString();
    	 //Toast.makeText(getBaseContext(), abonoString, Toast.LENGTH_LONG).show();
    	 float abono = Float.parseFloat(abonoString);
    	 
    	 
    	 float nuevoSaldo =  saldoActual - abono ;
    	 
    	 int posicion = 0;
    	 int cursor = 0;

    	 boolean fin = false;
       	 while (fin == false){
       		 				if (getCliente().getEstadoCuentas()[cursor].getMovdocumento().equalsIgnoreCase(movDocumento)){
       		 				posicion = cursor;
       		 				fin = true;
       		 					}
      		cursor = cursor + 1;
      	                      }
    	 Fecha fecha = new Fecha();
    	 String fechaHoy = fecha.FechaHoy();
    	 getCliente().getEstadoCuentas()[posicion].setCodigocliente(getCliente().getCliCodigo());
    	// getCliente().getEstadoCuentas()[posicion].setCredito(credito);
    	// getCliente().getEstadoCuentas()[posicion].setTotal(saldo);
    	 //getCliente().getEstadoCuentas()[posicion].setMovdocumento(documentoEditText.getText().toString());
    	 //getCliente().getEstadoCuentas()[posicion].setFecha(fechaHoy);
    	 float totalAbonos = abono + Float.parseFloat(getCliente().getEstadoCuentas()[posicion].getPagado());
    	 getCliente().getEstadoCuentas()[posicion].setPagado(Float.toString(totalAbonos));
    	 getCliente().getEstadoCuentas()[posicion].setSaldo(Float.toString(nuevoSaldo));
    	 String saldoTotal  = calculaSaldoTotal();
    	 getSaldo().setText(saldoTotal);
    	 
    	 final DataBase objeto = new DataBase(EstadosCuenta.this);
    	 objeto.abrir();
    	 objeto.insertPago(movDocumento,documentoEditText.getText().toString() ,abonoEditText.getText().toString() , fechaHoy, "1","0");
    	 objeto.cerrar();
    	 
    	 
    	 muestraLista();
    	 cargaPagos(movDocumento);
    	 muestraPagos(saldo, movDocumento);
    	
    	 }
      catch (Exception e) {
			// TODO Auto-generated catch block
    	  Toast.makeText(getBaseContext(), "LO SENTIMOS NO SE PUDO \nAGREGAR EL MOVIMIENTO", Toast.LENGTH_LONG).show();
		}
    	
    	 
    	
    	
     }

	

	 }
 						);

 alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener()
 {
     public void onClick(DialogInterface dialog, int whichButton)
     {
     // Canceled.
     }
 });

 alert.show();
	
}


private String calculaSaldoTotal() {
	// TODO Auto-generated method stub
	FormatDecimal formatDecimal = new FormatDecimal();
	int tamano = getCliente().getEstadoCuentas().length;
	float saldoTemp = 0;
	float saldos = 0;
	for (int i = 0; i < tamano; i++){
	saldoTemp = Float.parseFloat(getCliente().getEstadoCuentas()[i].getSaldo());
	saldos = saldos + saldoTemp;
	}
	String total = formatDecimal.ConvierteFloat(saldos);
	return total;
}

public SimpleAdapter getSimpleAdapter() {
	return simpleAdapter;
}

public void setSimpleAdapter(SimpleAdapter simpleAdapter) {
	this.simpleAdapter = simpleAdapter;
}

public ListaTest getListaEstadosDeCuentaTest() {
	return listaEstadosDeCuentaTest;
}

public void setListaEstadosDeCuentaTest(ListaTest listaEstadosDeCuentaTest) {
	this.listaEstadosDeCuentaTest = listaEstadosDeCuentaTest;
}

public ListView getEstadosDeCuentaListView() {
	return estadosDeCuentaListView;
}

public void setEstadosDeCuentaListView(ListView estadosDeCuentaListView) {
	this.estadosDeCuentaListView = estadosDeCuentaListView;
}

public EstadoDeCuenta[] getCuentas() {
	return cuentas;
}

public void setCuentas(EstadoDeCuenta[] cuentas) {
	this.cuentas = cuentas;
}

public ListView getCuentasListView() {
	return cuentasListView;
}

public void setCuentasListView(ListView cuentasListView) {
	this.cuentasListView = cuentasListView;
}

public Cliente getCliente() {
	return cliente;
}

public void setCliente(Cliente cliente) {
	this.cliente = cliente;
}

public TextView getSaldo() {
	return saldo;
}

public void setSaldo(TextView saldo) {
	this.saldo = saldo;
}

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void afterTextChanged(Editable arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	// TODO Auto-generated method stub
	
}

@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
	
}

public void setPagosListView(ListView pagosListView) {
	this.pagosListView = pagosListView;
}

public ListView getPagosListView() {
	return pagosListView;
}

public void setPagosSimpleAdapter(SimpleAdapter pagosSimpleAdapter) {
	this.pagosSimpleAdapter = pagosSimpleAdapter;
}

public SimpleAdapter getPagosSimpleAdapter() {
	return pagosSimpleAdapter;
}

public void setListaPagosTest(ListaTest listaPagosTest) {
	this.listaPagosTest = listaPagosTest;
}

public ListaTest getListaPagosTest() {
	return listaPagosTest;
}

public void setPago(Pago[] pago) {
	this.pago = pago;
}

public Pago[] getPago() {
	return pago;
}

public void setSaldoDetalle(TextView saldoDetalle) {
	this.saldoDetalle = saldoDetalle;
}

public TextView getSaldoDetalle() {
	return saldoDetalle;
}

public void setDocumentoDetalle(TextView documentoDetalle) {
	this.documentoDetalle = documentoDetalle;
}

public TextView getDocumentoDetalle() {
	return documentoDetalle;
}





 
}
