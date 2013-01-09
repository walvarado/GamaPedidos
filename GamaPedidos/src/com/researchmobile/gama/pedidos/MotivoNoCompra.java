package com.researchmobile.gama.pedidos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.Fecha;
import com.researchmobile.gama.entidades.Motivo;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.entidades.Usuario;
import com.researchmobile.gama.entidades.Vendedor;
import com.researchmobile.gama.utilidades.ConnectState;
import com.researchmobile.gama.ws.PeticionWS;

public class MotivoNoCompra extends Activity implements OnClickListener{
	private Spinner listaMotivoSpinner;
	private Button enviarButton;
	private Button cancelarButton;
	
	private String cliente;
	private Motivo[] motivos;
	private Motivo motivoSeleccionado;
	private Vendedor vendedor;
	private String idMovito;
	private Resultado resultado;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motivonocompra);
        
        Bundle bundle = getIntent().getExtras();
        setCliente((String)bundle.getString("cliente"));
        setResultado((Resultado)bundle.get("resultado"));
        setVendedor((Vendedor)bundle.get("vendedor"));
        setMotivoSeleccionado(new Motivo());
        
        setMotivos(getResultado().getMotivo());
        setListaMotivoSpinner((Spinner)findViewById(R.id.motivonocompra_motivos_spinner));
        
        setEnviarButton((Button)findViewById(R.id.motivonocompra_entrar_button));
        getEnviarButton().setOnClickListener(this);
        setCancelarButton((Button)findViewById(R.id.motivonocompra_cancelar_button));
        getCancelarButton().setOnClickListener(this);
        
        fillData();
        //getListaMotivoSpinner().getSelectedItem()
        
	}
	
	private void fillData() {
		final String[] motivosString = listaMotivos();
		ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(this, R.layout.special_spinner, R.id.special_spinner_motivo, motivosString);
		getListaMotivoSpinner().setAdapter(adaptador);
		tomarMotivo();
	}

	@Override
	public void onClick(View view) {
		if (view == getEnviarButton()){
			enviarMotivo();
		}else if(view == getCancelarButton()){
			cancelarMotivo();
		}
		
	}
	
	private String[] listaMotivos(){
		int tamano = getMotivos().length;
		String[] descripcion = new String[tamano];
		for (int i = 0; i < tamano; i++){
			
			descripcion[i] = getMotivos()[i].getMotivo();
			System.out.println("MOTIVONOCOMPRA, motivos = "+ descripcion[i]);
		}
		return descripcion;
		
	}

	private void cancelarMotivo() {
		LayoutInflater factory = LayoutInflater.from(MotivoNoCompra.this);
		final View textEntryView = factory.inflate(R.layout.tomar_pedido_cancelar_dialog, null);

		final AlertDialog.Builder alert = new AlertDialog.Builder(MotivoNoCompra.this);
		        
		alert.setTitle("CANCELAR");
		alert.setMessage("¿ESTA SEGURO QUE DESEA CANCELAR EL ENVIO DEL MOTIVO NO COMPRA?");
		alert.setView(textEntryView);

		alert.setPositiveButton("  SI  ", new DialogInterface.OnClickListener()
		{
		     public void onClick(DialogInterface dialog, int whichButton)
		     {
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
		


	private void enviarMotivo() {
		
		LayoutInflater factory = LayoutInflater.from(MotivoNoCompra.this);
		final View textEntryView = factory.inflate(R.layout.tomar_pedido_cancelar_dialog, null);

		final AlertDialog.Builder alert = new AlertDialog.Builder(MotivoNoCompra.this);
		        
		alert.setTitle("ENVIAR MOTIVO");
		alert.setMessage("¿CONTINUAR EL ENVIO?");
		alert.setView(textEntryView);

		alert.setPositiveButton("  SI  ", new DialogInterface.OnClickListener()
		{
		     public void onClick(DialogInterface dialog, int whichButton)
		     {
		    	
		 		ejecutarEnvio(); 
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

	protected void ejecutarEnvio() {
		ConnectState connectState = new ConnectState();
		Fecha fecha = new Fecha();
		Usuario usuario = new Usuario();
		if (connectState.isConnectedToInternet(this)){
			PeticionWS peticionWS = new PeticionWS();
			String error = peticionWS.postMotivo(usuario.getUsuario(), usuario.getClave(), getCliente(), fecha.FechaHoy(), fecha.Hora(), getMotivoSeleccionado(), getVendedor().getVendedor());
			
			for (int i = 0; i < getResultado().getCliente().length; i++){
				if (getResultado().getCliente()[i].getCliCodigo().equalsIgnoreCase(getCliente())){
					getResultado().getCliente()[i].setVisitado(1);
				}
			}
			
			guardarDB(fecha, usuario, "1");
		}else{
			for (int i = 0; i < getResultado().getCliente().length; i++){
				if (getResultado().getCliente()[i].getCliCodigo().equalsIgnoreCase(getCliente())){
					getResultado().getCliente()[i].setVisitado(1);
				}
			}
			guardarDB(fecha, usuario, "0");
		}
		
		
		Toast.makeText(getBaseContext(), "SE HA ENVIADO EL MOTIVO", Toast.LENGTH_SHORT).show();
		finish();
		
	}

	private void tomarMotivo() {
		getListaMotivoSpinner().setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							android.view.View v, int position, long id) {
						
						
						String motivoSeleccionado = getListaMotivoSpinner().getSelectedItem().toString();
						String idMotivoSeleccionado = buscarIdMotivo(motivoSeleccionado);
						
						getMotivoSeleccionado().setId(idMotivoSeleccionado);
						getMotivoSeleccionado().setMotivo(motivoSeleccionado);
						
						System.out.println("MOTIVONOCOMPRA, id = " + getMotivoSeleccionado().getId());
						System.out.println("MOTIVONOCOMPRA, motivo = " + getMotivoSeleccionado().getMotivo());

					}

					private String buscarIdMotivo(String motivoSeleccionado) {
						int tamano = getMotivos().length;
						for (int i = 0; i < tamano; i++){
							if (motivoSeleccionado.equalsIgnoreCase(getMotivos()[i].getMotivo())){
								return getMotivos()[i].getId();
							}
						}
						return null;
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// textView.setText("");
					}
				});
		
	}

	private void guardarDB(Fecha fecha, Usuario usuario, String sinc) {
		ConexionDB conexionDBSend = new ConexionDB();
		EncabezadoPedido encabezado = new EncabezadoPedido();
		encabezado.setCodigoCliente(getCliente());
		encabezado.setFecha(fecha.FechaHoy());
		encabezado.setHora(fecha.Hora());
		//encabezado.setSinc(0);
		encabezado.setFallido(1);
		//encabezado.setMotivo(Integer.parseInt(getMotivo().getId()));
		//encabezado.setMotivoNoCompra(getMotivo().getMotivo());
		conexionDBSend.GuardaEncabezadoPedido(encabezado, MotivoNoCompra.this, sinc);
		conexionDBSend.actualizaCliente(this, encabezado.getCodigoCliente());
		
	}

	public Spinner getListaMotivoSpinner() {
		return listaMotivoSpinner;
	}

	public void setListaMotivoSpinner(Spinner listaMotivoSpinner) {
		this.listaMotivoSpinner = listaMotivoSpinner;
	}

	public Button getEnviarButton() {
		return enviarButton;
	}

	public void setEnviarButton(Button enviarButton) {
		this.enviarButton = enviarButton;
	}

	public Button getCancelarButton() {
		return cancelarButton;
	}

	public void setCancelarButton(Button cancelarButton) {
		this.cancelarButton = cancelarButton;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public String getIdMovito() {
		return idMovito;
	}

	public void setIdMovito(String idMovito) {
		this.idMovito = idMovito;
	}

	public Motivo[] getMotivos() {
		return motivos;
	}

	public void setMotivos(Motivo[] motivos) {
		this.motivos = motivos;
	}

	public Motivo getMotivoSeleccionado() {
		return motivoSeleccionado;
	}

	public void setMotivoSeleccionado(Motivo motivoSeleccionado) {
		this.motivoSeleccionado = motivoSeleccionado;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}
	
}
