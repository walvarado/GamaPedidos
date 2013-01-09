package com.researchmobile.gama.pedidos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.researchmobile.gama.db.DataBase;
import com.researchmobile.gama.utilidades.TokenizerString;

public class Sincronizar extends Activity {
	private Button sincronizarButton;
	private TokenizerString tokenizerString;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sincronizar);
        setTokenizerString(new TokenizerString());
        
        final DataBase objeto = new DataBase(this);
        
        Button borrarDatos = (Button)findViewById(R.id.sincronizar_borrar_registro_button);
        borrarDatos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					objeto.abrir();
					objeto.limpiarDB();
					objeto.cerrar();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
        
        setSincronizarButton((Button)findViewById(R.id.sincronizar_button));
        getSincronizarButton().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					objeto.abrir();
					
					File fCliente=new File("/sdcard/dbfacture/db.txt");
					BufferedReader entrada;
					String[] filaDividida = null;
					
					entrada = new BufferedReader( new FileReader( fCliente ));
						while(entrada.ready()){
							filaDividida = getTokenizerString().guardaCliente(entrada.readLine());
							if (filaDividida[0].equalsIgnoreCase("cliente")){
								/*objeto.clienteInsert(filaDividida[1], 
										 filaDividida[2], 
										 filaDividida[3], 
										 filaDividida[4], 
										 filaDividida[5], 
										 filaDividida[6], 
										 filaDividida[7], 
										 filaDividida[8], 
										 filaDividida[9], 
										 filaDividida[10], 
										 filaDividida[11], 
										 filaDividida[12], 
										 filaDividida[13],
										 filaDividida[14],
										 filaDividida[14]);*/
							}else if (filaDividida[0].equalsIgnoreCase("producto")){
								objeto.productoInsert(filaDividida[1], 
										 filaDividida[2], 
										 filaDividida[3], 
										 filaDividida[4], 
										 filaDividida[5], 
										 filaDividida[6], 
										 filaDividida[7], 
										 filaDividida[8], 
										 filaDividida[9], 
										 filaDividida[10], 
										 filaDividida[11]);
							}else if (filaDividida[0].equalsIgnoreCase("existencia")){
								/*objeto.existenciaInsert(filaDividida[1], 
														filaDividida[2]);
							*/
							}else if (filaDividida[0].equalsIgnoreCase("vendedor")){
								objeto.vendedorInsert(filaDividida[1], 
													  filaDividida[2]);
							}
						}
					objeto.cerrar();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
			}
		});
        
	}

	public Button getSincronizarButton() {
		return sincronizarButton;
	}

	public void setSincronizarButton(Button sincronizarButton) {
		this.sincronizarButton = sincronizarButton;
	}

	public TokenizerString getTokenizerString() {
		return tokenizerString;
	}

	public void setTokenizerString(TokenizerString tokenizerString) {
		this.tokenizerString = tokenizerString;
	}

}
