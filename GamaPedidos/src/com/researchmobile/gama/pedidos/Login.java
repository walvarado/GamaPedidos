package com.researchmobile.gama.pedidos;
//LINEA 228 ejecucion del login
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.db.DataBase;
import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.Fecha;
import com.researchmobile.gama.entidades.NuevoPedido;
import com.researchmobile.gama.entidades.Resultado;
import com.researchmobile.gama.entidades.Usuario;
import com.researchmobile.gama.ws.PeticionWS;

public class Login extends Activity implements OnClickListener{
	//Estados
	public static final int ESTADO_EXITOSO = 1;
	
	//Mensajes
	public static final int MSJ_CAMPOS_INCOMPLETOS = 1;
	
	private EditText usuarioEditText;
	private EditText contraseniaEditText;
	private Button entrarButton;
	
	private PeticionWS peticionWS;
	private Resultado resultado;
	private Cliente cliente[];
	
	private static AsyncTask<String, Void, Resultado> loadRouteTask;
	private static ProgressDialog progressDialog;
	
	private String username;
	private String  password;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        setResultado(new Resultado());
        setUsuarioEditText((EditText)findViewById(R.id.login_usuario_edittext));
        setContraseniaEditText((EditText)findViewById(R.id.login_contrasenia_edittext));
        setEntrarButton((Button)findViewById(R.id.login_entrar_button));
        
        getUsuarioEditText().setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
                {
                    getContraseniaEditText().requestFocus();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                	//
                    return true;
                }
                return false;
            }
        });
        
        getContraseniaEditText().setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
                {
                	IniciarLogin();
                	return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                	//
                    return true;
                }
                return false;
            }
        });
        //getCancelarButton().setOnClickListener(this);
        
        getEntrarButton().setOnClickListener(this);
    }
    
    //Inhabilitar boton "back"
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	
              // Preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
        	//android.os.Process.killProcess(android.os.Process.myPid());
        	moveTaskToBack(true);
              return true;
        }
        
        return super.onKeyDown(keyCode, event);
      }

    @Override
	public void onClick(View view) {
		if (view == getEntrarButton()){
			System.out.println("LOGIN, 120, se presiono boton enter");
			IniciarLogin();
		}
		
	}

	private void IniciarLogin() {
		
		//if (gpsReference()){
			peticionWS = new PeticionWS();
			
			setUsername(getUsuarioEditText().getText().toString());
			setPassword(getContraseniaEditText().getText().toString());
			
			if (Requerimientos(getUsername(), getPassword())) {
				setLoadRouteTask(new LoadRouteTask());
				getLoadRouteTask().execute(getUsername(), getPassword());
			}else{
				Mensaje(MSJ_CAMPOS_INCOMPLETOS);
			} 
		//}else{
			//System.out.println("LOGIN, 141, Gps No esta activado");
			//VerDialogGps();
		//}
	}

	private void Mensaje(int mensaje) {
		switch(mensaje){
		case MSJ_CAMPOS_INCOMPLETOS:
			Toast.makeText(getBaseContext(), "DEBE LLENAR TODOS LOS CAMPOS REQUERIDOS", Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	private boolean gpsReference() {
		System.out.println("LOGIN, 154, verificando gps");
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		if(!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER ))
		{
			System.out.println("LOGIN, 158, No esta activado");
		    return false;
		}else{
			System.out.println("LOGIN, 158, Si esta activado");
			return true;
		}
	}

	private void VerDialogGps() {
		
		      new AlertDialog.Builder(Login.this)
		      
		      .setTitle("GPS DESACTIVADO!!!")
		      .setMessage("Debe activar el GPS")
		      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface dialog, int whichButton) {
		            	Intent myIntent = new Intent( Settings.ACTION_SECURITY_SETTINGS );
		      		    startActivity(myIntent);
		              }
		      })
		      .show();
	}
	private boolean Requerimientos(String username, String password) {
		if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")){
			return false;
		}
		return true;
	}
	
	private AsyncTask<String, Void, Resultado> getLoadRouteTask() {
		return loadRouteTask;
	}

	private void setLoadRouteTask(AsyncTask<String, Void, Resultado> loadRouteTask_) {
		loadRouteTask = loadRouteTask_;
	}
	
	/*
	* Class to load the Visits Route
	*/
	private class LoadRouteTask extends AsyncTask<String, Void, Resultado> {                      
	    

	protected void onPreExecute() {
	progressDialog = ProgressDialog.show(Login.this, "VERIFICANDO DATOS","ESPERE UN MOMENTO");
	progressDialog.setCancelable(false);
	                  
	super.onPreExecute();
	}
	            
	/*
	* RouteResult es una clase propia, puede cambiar y el valor dependera de nosotros
	*/

	protected Resultado doInBackground(String... params) {
	    Resultado resultado = null;
	                  
	    try {
	    		resultado = VerificacionLogin(getResultado());
			}
	        catch(Exception exception) {
	        	Toast.makeText(getBaseContext(), "PROBLEMAS DE CONEXION", Toast.LENGTH_LONG).show();
	        	System.out.println(exception);
	        }
	        return resultado;
	}

	private Resultado VerificacionLogin(Resultado resultado) {
		setResultado((peticionWS.LoginWS(getUsername(), getPassword(),Login.this)));
		return getResultado();
	}

	protected void onPostExecute(Resultado resultado) {
	    if (progressDialog != null && progressDialog.isShowing()) {
	        progressDialog.dismiss();
	        progressDialog = null;
	    }
	    
	    CargarClientes();
	    super.onPostExecute(resultado);
	}

	private void CargarClientes() {
		if (getResultado().getError() > 0){
			getUsuarioEditText().setText("");
			getContraseniaEditText().setText("");
			Toast.makeText(getBaseContext(), "EL LOGIN NO ES CORRECTO O EL SERVIDOR NO ESTA DISPONIBLE", Toast.LENGTH_LONG).show();
		}else{
			Usuario usuario = new Usuario();
			usuario.setUsuario(getUsername());
			usuario.setClave(getPassword());
			VerificaLoginDB(getUsername(), getPassword(), Login.this);
			Intent LoginIntent = new Intent(Login.this,	Pedido.class);
			LoginIntent.putExtra("resultado", getResultado());
			startActivity(LoginIntent);
		}
	}
	}
	
	@SuppressWarnings("unused")
	private void VerificaLoginDB(String usuario, String clave, Context context) {
		final DataBase objeto2 = new DataBase(context);
		try {
			Fecha fecha = new Fecha();
			String hoy = fecha.Dia();
			
			objeto2.abrir();
			Cursor cursor = objeto2.consultaUsuario(usuario, clave);
			objeto2.cerrar();
			if (cursor.getCount() != 0){
				cursor.moveToFirst();
				
				String usuarioDB = cursor.getString(0);
				String claveDB = cursor.getString(1);
				String diaDB = cursor.getString(2);
				
				if (diaDB.equalsIgnoreCase(hoy)){
						System.out.println("LOGIN, Es hoy");	
					}else{
						System.out.println("LOGIN, no es hoy");
						verificaPendientes();
					}
				}
		}catch (Exception exception){
			System.out.println("Usuario Guradado ccccc");
			
		}
		
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
						peticionWS.postPedido(nuevoPedidoTemp, getUsername(), getPassword());
						System.out.println("revision 2");
						conexionDB.actualizaEncabezadoPedido(this, pedidoTemp[i].getCodigoPedidoTemp());
						System.out.println("se envio un pedido que estaba guardado");
					}
				}
				conexionDB.limpiaPedidoDB(this);
		}else{
			System.out.println("LOGIN, no hay pedidos pendientes");
		}
	}

	public EditText getUsuarioEditText() {
		return usuarioEditText;
	}

	public void setUsuarioEditText(EditText usuarioEditText) {
		this.usuarioEditText = usuarioEditText;
	}

	public EditText getContraseniaEditText() {
		return contraseniaEditText;
	}

	public void setContraseniaEditText(EditText contraseniaEditText) {
		this.contraseniaEditText = contraseniaEditText;
	}

	public Button getEntrarButton() {
		return entrarButton;
	}

	public void setEntrarButton(Button entrarButton) {
		this.entrarButton = entrarButton;
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
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
