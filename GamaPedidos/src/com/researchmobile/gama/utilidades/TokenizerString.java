package com.researchmobile.gama.utilidades;

import java.util.StringTokenizer;

public class TokenizerString {
	
		public String[] div(String texto){
		StringTokenizer tokens = new StringTokenizer(texto,"#");
        int nDatos = tokens.countTokens();
        String[] datosRegistro = new String[nDatos];
        int i = 0;
        while (tokens.hasMoreTokens()){
        	String str = tokens.nextToken();
        	datosRegistro[i]=str;
        	i++;
        	}
		return datosRegistro;
		}
	
	/*
	 * Guarda registros en base de datos, Tabla: CLIENTE
	 */
	public String[] guardaCliente(String texto){
		StringTokenizer tokens = new StringTokenizer(texto,"#");
        int nDatos = tokens.countTokens();
        String[] datosRegistro = new String[nDatos];
        int i = 0;
        while (tokens.hasMoreTokens()){
        	String str = tokens.nextToken();
        	datosRegistro[i]=str;
        	i++;
        	}
		return datosRegistro;
		}
	
	
	/*
	 * Guarda registros en base de datos, Tabla: CLIENTE
	 */
	public String[] guardaPedido(String texto){
		StringTokenizer tokens = new StringTokenizer(texto,"#");
        int nDatos = tokens.countTokens();
        String[] datosRegistro = new String[nDatos];
        int i = 0;
        while (tokens.hasMoreTokens()){
        	String str = tokens.nextToken();
        	datosRegistro[i]=str;
        	i++;
        	}
		return datosRegistro;
		}
	
	
	
	public String[] divApp (String texto){
		StringTokenizer tokens = new StringTokenizer(texto);
        int nDatos = tokens.countTokens();
        String[] appName = new String[nDatos];
        int i = 0;
        while (tokens.hasMoreTokens()){
        	String str = tokens.nextToken();
        	appName[i]=str;
        	i++;
        	}
		return appName;
	}
	
	public String activityNameApp (String texto){
		StringTokenizer tokens = new StringTokenizer(texto, ".");
        int nDatos = tokens.countTokens();
        String[] appName = new String[nDatos];
        String name = "";
        int i = 0;
        while (tokens.hasMoreTokens()){
        	String str = tokens.nextToken();
        	appName[i]=str;
        	i++;
        	}
        name = appName[nDatos - 1];
		return name;
	}
}
