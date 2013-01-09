package com.researchmobile.gama.entidades;

import android.content.Context;
import android.database.Cursor;

import com.researchmobile.gama.db.ConexionDB;
import com.researchmobile.gama.db.DataBase;

public class PeticionLocal {
	private Resultado resultado;

	public Resultado DatosLocales(Context context) {
		
		setResultado(new Resultado());
		getResultado().setError(0);
		getResultado().setMensaje("Login Exitoso");
		
		DataBase objeto = new DataBase(context);
		try {
			objeto.abrir();
			Cursor c = objeto.consultaClientes();
			Cliente[] cliente = new Cliente[c.getCount()];
			System.out.println(c.getCount());
			int i = 0;
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				cliente[i].setCliCodigo(c.getString(0));
				cliente[i].setCliEmpresa(c.getString(1));
				cliente[i].setCliContacto(c.getString(2));
				cliente[i].setCliDireccion(c.getString(3));
				cliente[i].setCliTelefono(c.getString(4));
				cliente[i].setCliDiasCredito(c.getString(5));
				cliente[i].setCliLimite(c.getString(6));
				cliente[i].setCliSaldo(c.getString(7));
				cliente[i].setCliDescuento1(c.getString(8));
				cliente[i].setCliDescuento2(c.getString(9));
				cliente[i].setCliDescuento3(c.getString(10));
				cliente[i].setSaldo(c.getString(11));
			}
			
			getResultado().setCliente(cliente);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getResultado();
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

}
