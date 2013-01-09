package com.researchmobile.gama.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;

public class ConnectWS {
	//private static final String finalURL = "http://megasoftech.com/megainfo/";
	private static final String finalURL = "http://200.6.222.110:8080/megainfo/";
	
	public static JSONArray getJsonArray(String url) { //Clase que obtienen el objeto JSON
		
		JSONArray jsonArray = null;
		try{
			URL urlConnect = new URL(finalURL + url);    //Cadena de Conexión URL
	        HttpURLConnection urlConnection = (HttpURLConnection)urlConnect.openConnection(); //Haciendo la conexión 
	        InputStream inputStream = urlConnection.getInputStream();   //Obteniendo inputStream
	        String responseInputStream = convertStreamToString(inputStream);    //Convirtiendo el inputStream a String
	        jsonArray = new JSONArray(responseInputStream);   //asignando el Objeto JSON a response
	        
	    }catch (Exception exception){
			
		}
		return jsonArray;
	}
	
	public static JSONObject getJsonObject(String url) { //Clase que obtienen el objeto JSON
		
		JSONObject jsonObject = null;
		
		try{
			//InputStream inputStream;
			
			URL urlConnect = new URL(finalURL + url);    //Cadena de Conexión URL
			System.out.println("LOGIN, CONNECTWS, 47 " + urlConnect);
	        HttpURLConnection urlConnection = (HttpURLConnection)urlConnect.openConnection(); //Haciendo la conexión
	        System.out.println("LOGIN, CONNECTWS, 49, Se conecto correctamente");
	        InputStream inputStream = urlConnection.getInputStream();   //Obteniendo inputStream
	        String responseInputStream = convertStreamToString(inputStream);    //Convirtiendo el inputStream a String
	        
			jsonObject = new JSONObject(responseInputStream);   //asignando el Objeto JSON a response
			urlConnection.disconnect();
			 
		}catch (Exception exception){
			return null;
		}
		return jsonObject;
	}
	
	public static JSONObject enviarPedido(String url) { //Clase que obtienen el objeto JSON
		
		JSONObject jsonObject = null;
		
		try{
			
			URL urlCon = new URL("http", "200.6.222.110", 8080, "/megainfo/" + url); 
			//URL urlConnect = new URL(finalURLQuiz + url);    //Cadena de Conexión URL
			HttpURLConnection urlConnection = (HttpURLConnection)urlCon.openConnection(); //Haciendo la conexión
			System.out.println(urlCon);
			System.out.println(urlConnection.getResponseCode());
			InputStream inputStream = urlConnection.getInputStream();   //Obteniendo inputStream
			String responseInputStream = convertStreamToString(inputStream);    //Convirtiendo el inputStream a String
	        jsonObject = new JSONObject(responseInputStream);   //asignando el Objeto JSON a response
			
	    }catch (Exception exception){
	    	System.out.println(exception);
	    	return null;
		}
	    return jsonObject;
}
	
	@SuppressWarnings("unused")
	public static JSONObject enviaPedido(String url){
		JSONObject jsonObject = null;
		try{
			URL urlConnect = new URL(finalURL + url);    //Cadena de Conexión URL
	        HttpURLConnection urlConnection = (HttpURLConnection)urlConnect.openConnection(); //Haciendo la conexión
	        System.out.println(urlConnect);
	        InputStream inputStream = urlConnection.getInputStream();   //Obteniendo inputStream
	        String responseInputStream = convertStreamToString(inputStream);    //Convirtiendo el inputStream a String
	        jsonObject = new JSONObject(responseInputStream);   //asignando el Objeto JSON a response
	        
	    }catch (Exception exception){
			
		}
		return jsonObject;
		
	}
	
	public static JSONObject enviaMotivo(String url){
		JSONObject jsonObject = null;
		try{
			URL urlConnect = new URL(finalURL + url);    //Cadena de Conexión URL
	        HttpURLConnection urlConnection = (HttpURLConnection)urlConnect.openConnection(); //Haciendo la conexión 
	        InputStream inputStream = urlConnection.getInputStream();   //Obteniendo inputStream
	        System.out.println(urlConnect);
	        String responseInputStream = convertStreamToString(inputStream);    //Convirtiendo el inputStream a String
	        jsonObject = new JSONObject(responseInputStream);   //asignando el Objeto JSON a response
	        
	    }catch (Exception exception){
			System.out.println("CONNECTWS, el envio del motivo fallo");
		}
		return jsonObject;
	}
	private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder(); 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
