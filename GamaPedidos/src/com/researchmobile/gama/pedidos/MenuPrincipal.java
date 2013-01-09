package com.researchmobile.gama.pedidos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.researchmobile.gama.db.DataBase;

public class MenuPrincipal extends Activity {
	
	Animation traslacion;
    Animation rotacion;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuprincipal);
        
        DataBase objeto = new DataBase(MenuPrincipal.this);
        try {
			objeto.abrir();
			//objeto.limpiarDetallePedidoTemp();
			objeto.cerrar();
			
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "no esta vacío detallePedidoTemp", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
        
        ImageView iconResearchMobileImageView = (ImageView)findViewById(R.id.menuprincipal_logo_imageview);
        ImageView nameResearchMobileImageView = (ImageView)findViewById(R.id.menuprincipal_researchmobile_imageview);
        
        rotacion = AnimationUtils.loadAnimation(this, R.anim.rotacion);
        traslacion = AnimationUtils.loadAnimation(this, R.anim.traslacion);
        
        iconResearchMobileImageView.setAnimation(rotacion);
        nameResearchMobileImageView.setAnimation(traslacion);
        
        
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
		inflater.inflate(R.menu.menuprincipal_menu, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuprincipal_menu_pedido_opcion:
			PedidoActivity();

			return true;
		case R.id.menuprincipal_menu_verpedidos_opcion:
			VerPedidosActivity();

			return true;
		case R.id.menuprincipal_menu_existencia_opcion:
			ExistenciaActivity();

			return true;
		case R.id.menuprincipal_menu_precios_opcion:
			PreciosActivity();

			return true;
		case R.id.menuprincipal_menu_cuentasxcobrar_opcion:
			CuentasXCobrarActivity();

			return true;
		case R.id.menuprincipal_menu_ventas_opcion:
			VentasActivity();

			return true;
		case R.id.menuprincipal_menu_configurar_opcion:
			ConfigurarActivity();

			return true;
		case R.id.menuprincipal_menu_sincronizar_opcion:
			SincronizarActivity();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void SincronizarActivity() {
		Intent sincronizarIntent = new Intent(MenuPrincipal.this, Sincronizar.class);
		startActivity(sincronizarIntent);
		
	}

	private void ConfigurarActivity() {
		// TODO Auto-generated method stub
		
	}

	private void VentasActivity() {
		// TODO Auto-generated method stub
		
	}

	private void CuentasXCobrarActivity() {
		// TODO Auto-generated method stub
		
	}

	private void PreciosActivity() {
		// TODO Auto-generated method stub
		
	}

	private void ExistenciaActivity() {
		// TODO Auto-generated method stub
		
	}

	private void VerPedidosActivity() {
		// TODO Auto-generated method stub
		
	}

	private void PedidoActivity() {
		Intent pedidoIntent = new Intent(MenuPrincipal.this, Pedido.class);
		startActivity(pedidoIntent);
		
	}

}