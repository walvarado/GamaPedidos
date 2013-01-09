package com.researchmobile.gama.utilidades;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

public class Listas {
	
	public ArrayList<HashMap<String, String>> ListaClientes (JSONArray jsonArray){
		
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("cliente", "1111");
		map.put("nombreComercial", "nada");
		map.put("razonSocial", "nada");
		mylist.add(map);
		return mylist;
		
		/*
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
        	map.put("cliente", "1111");
        	map.put("nombreComercial", "nada");
        	map.put("razonSocial", "nada");
        	mylist.add(map);
		return mylist;
		*/
	}
	
	public ArrayList<HashMap<String, String>> ListaClientesVisitados (){
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
        	map.put("cliente", "1111");
        	map.put("nombreComercial", "nada");
        	map.put("razonSocial", "nada");
        	mylist.add(map);
		return mylist;
	}
	
	public ArrayList<HashMap<String, String>> ListaPedidos (){
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
        	map.put("cliente", "1111");
        	map.put("nombreComercial", "nada");
        	map.put("razonSocial", "nada");
        	mylist.add(map);
		return mylist;
	}
	
	public ArrayList<HashMap<String, String>> ListaCuentasXCobrar (){
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
        	map.put("cliente", "1111");
        	map.put("nombreComercial", "nada");
        	map.put("razonSocial", "nada");
        	mylist.add(map);
		return mylist;
	}

}
