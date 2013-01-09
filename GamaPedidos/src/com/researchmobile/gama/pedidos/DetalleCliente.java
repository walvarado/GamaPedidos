package com.researchmobile.gama.pedidos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.researchmobile.gama.db.DataBase;
import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.utilidades.ListaTest;

public class DetalleCliente extends Activity{
	
	private Cliente cliente;
	private ListaTest listaTest;
	private String codigoCliente;
	private TextView codigoClienteTextView;
	private TextView empresaTextView;
	private TextView contactoTextView;
	private TextView direccionTextView;
	private TextView telefonoTextView;
	private TextView diasCreditoTextView;
	private TextView limiteTextView;
	private TextView saldoTextView;
	private TextView descuento1TextView;
	private TextView descuento2TextView;
	private TextView descuento3TextView;
	private TextView saldo2TextView;
	
	private Resultado resultado;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_cliente);
        
        setResultado(new Resultado());
        Bundle bundle = getIntent().getExtras();
        setResultado((Resultado)bundle.get("resultado"));  //Resibe Resultado de Pedido (lista de clientes)
        setCodigoCliente((String)this.getIntent().getSerializableExtra("codigoCliente"));
        setListaTest(new ListaTest());
        
        setCodigoClienteTextView((TextView)findViewById(R.id.detalle_cliente_codigo_textview));
        setEmpresaTextView((TextView)findViewById(R.id.detalle_cliente_empresa_textview));
        setContactoTextView((TextView)findViewById(R.id.detalle_cliente_contacto_textview));
        setDireccionTextView((TextView)findViewById(R.id.detalle_cliente_direccion_textview));
        setTelefonoTextView((TextView)findViewById(R.id.detalle_cliente_telefono_textview));
        setDiasCreditoTextView((TextView)findViewById(R.id.detalle_cliente_dias_credito_textview));
        setLimiteTextView((TextView)findViewById(R.id.detalle_cliente_limite_textview));
        setSaldoTextView((TextView)findViewById(R.id.detalle_cliente_saldo_textview));
        setDescuento1TextView((TextView)findViewById(R.id.detalle_cliente_descuento_textview));
        
        ClienteSeleccionado();
        
        getCodigoClienteTextView().setText(getCliente().getCliCodigo());
        getEmpresaTextView().setText(getCliente().getCliEmpresa());
        getContactoTextView().setText(getCliente().getCliContacto());
        getDireccionTextView().setText(getCliente().getCliDireccion());
        getTelefonoTextView().setText(getCliente().getCliTelefono());
        getDiasCreditoTextView().setText(getCliente().getCliDiasCredito());
        getLimiteTextView().setText(getCliente().getCliLimite());
        getSaldoTextView().setText(getCliente().getCliSaldo());
        getDescuento1TextView().setText(getCliente().getCliDescuento1());
        
        final DataBase objeto = new DataBase(DetalleCliente.this);
        ClienteSeleccionado();
    }
	
	private void ClienteSeleccionado() {
		int tamano = getResultado().getCliente().length;
		for (int i = 0; i < tamano; i++){
			if (getResultado().getCliente()[i].getCliCodigo().equalsIgnoreCase(getCodigoCliente())){
				setCliente(getResultado().getCliente()[i]);
			}
		}
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
		inflater.inflate(R.menu.detalle_cliente_menu, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.detalle_cliente_menu_tomar_pedido_opcion:
			TomarPedido();
			return true;
		case R.id.detalle_cliente_menu_motivo_nocompra_opcion:
			//Toast.makeText(getBaseContext(), "En proceso de desarrollo", Toast.LENGTH_LONG).show();
			MotivoNoCompra();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void MotivoNoCompra() {
		Intent intent = new Intent(DetalleCliente.this, MotivoNoCompra.class);
		System.out.println("DETALLECLIENTE, cliente = " + getCliente().getCliCodigo());
		System.out.println("DETALLECLIENTE, motivos = " + getResultado().getMotivo().length);
		System.out.println("DETALLECLIENTE, vendedor = " + getResultado().getVendedor().getVendedor());
		
		intent.putExtra("cliente", getCliente().getCliCodigo());
		intent.putExtra("resultado", getResultado());
		intent.putExtra("vendedor", getResultado().getVendedor());
		startActivity(intent);
		
	}

	private void CuentasPorCobrar() {
		
		int tamano = getCliente().getEstadoCuentas().length;
		//Verificar si el cliente tiene cuentas pendientes
		if (tamano > 0){
			Intent intent = new Intent(DetalleCliente.this, EstadosCuenta.class);
			intent.putExtra("cliente", getCliente());
			startActivity(intent);
		}else{
			Toast.makeText(getBaseContext(), "No hay cuentas pendientes", Toast.LENGTH_LONG).show();
		}
	}

	private void TomarPedido() {
		Intent tomarPedidoIntent = new Intent(DetalleCliente.this, TomarPedido.class);
		tomarPedidoIntent.putExtra("resultado", getResultado());
		tomarPedidoIntent.putExtra("codigoCliente", getCodigoCliente());
		startActivity(tomarPedidoIntent);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TextView getTelefonoTextView() {
		return telefonoTextView;
	}

	public void setTelefonoTextView(TextView telefonoTextView) {
		this.telefonoTextView = telefonoTextView;
	}

	public TextView getDireccionTextView() {
		return direccionTextView;
	}

	public void setDireccionTextView(TextView direccionTextView) {
		this.direccionTextView = direccionTextView;
	}

	public ListaTest getListaTest() {
		return listaTest;
	}

	public void setListaTest(ListaTest listaTest) {
		this.listaTest = listaTest;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	public TextView getCodigoClienteTextView() {
		return codigoClienteTextView;
	}

	public void setCodigoClienteTextView(TextView codigoClienteTextView) {
		this.codigoClienteTextView = codigoClienteTextView;
	}

	public TextView getEmpresaTextView() {
		return empresaTextView;
	}

	public void setEmpresaTextView(TextView empresaTextView) {
		this.empresaTextView = empresaTextView;
	}

	public TextView getContactoTextView() {
		return contactoTextView;
	}

	public void setContactoTextView(TextView contactoTextView) {
		this.contactoTextView = contactoTextView;
	}

	public TextView getDiasCreditoTextView() {
		return diasCreditoTextView;
	}

	public void setDiasCreditoTextView(TextView diasCreditoTextView) {
		this.diasCreditoTextView = diasCreditoTextView;
	}

	public TextView getLimiteTextView() {
		return limiteTextView;
	}

	public void setLimiteTextView(TextView limiteTextView) {
		this.limiteTextView = limiteTextView;
	}

	public TextView getSaldoTextView() {
		return saldoTextView;
	}

	public void setSaldoTextView(TextView saldoTextView) {
		this.saldoTextView = saldoTextView;
	}

	public TextView getDescuento1TextView() {
		return descuento1TextView;
	}

	public void setDescuento1TextView(TextView descuento1TextView) {
		this.descuento1TextView = descuento1TextView;
	}

	public TextView getDescuento2TextView() {
		return descuento2TextView;
	}

	public void setDescuento2TextView(TextView descuento2TextView) {
		this.descuento2TextView = descuento2TextView;
	}

	public TextView getDescuento3TextView() {
		return descuento3TextView;
	}

	public void setDescuento3TextView(TextView descuento3TextView) {
		this.descuento3TextView = descuento3TextView;
	}

	public TextView getSaldo2TextView() {
		return saldo2TextView;
	}

	public void setSaldo2TextView(TextView saldo2TextView) {
		this.saldo2TextView = saldo2TextView;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

}
