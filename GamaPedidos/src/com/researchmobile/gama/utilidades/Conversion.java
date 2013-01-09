package com.researchmobile.gama.utilidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.researchmobile.gama.entidades.Cliente;
import com.researchmobile.gama.entidades.DetallePedido;
import com.researchmobile.gama.entidades.EncabezadoPedido;
import com.researchmobile.gama.entidades.Fecha;

public class Conversion {
	private static final int LIMITE_FACTURA = 10;
	
	public String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	
	public static String byteArrayToHexString(byte in[]){
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0){
			return null;
		}
	    String pseudo[] = {"0", "1", "2","3", "4", "5", "6", "7", "8","9", "a", "b", "c", "d", "e", "f"};
	    StringBuffer out_str_buf = new StringBuffer(in.length);
	    while (i < in.length){
	       	ch = (byte) (in[i] & 0xF0); // Strip off high nibble
	       	ch = (byte) (ch >>> 4);     // shift the bits down
	       	ch = (byte) (ch & 0x0F);    // must do this is high order bit is on!
	       	out_str_buf.append(pseudo[ (int) ch]); // convert the nibble to a String Character
	       	ch = (byte) (in[i] & 0x0F); // Strip off low nibble
	       	out_str_buf.append(pseudo[ (int) ch]); // convert the nibble to a String Character
	       	i++;
	    }
		String rslt = new String(out_str_buf);
		return rslt;
    }
	
	public String creaJsonNocompra (String motivo, String clicodigo, String fecha, String hora, String idVendedor){
		String result = "";
		
		result = "&idnoventa=" + motivo + 
		"&clicodigo=" +  clicodigo +
		"&movfecha=" + fecha +
		"&movhora=" + hora +
		"&vendedor=" + idVendedor;
		
		return result;
	}
	
	public String creaJsonPedido (EncabezadoPedido encabezado, DetallePedido[] detPedido, int i, int tamano, int a2){
		
		System.out.println("CONVERSION, 82, Valor de i = " + i);
		System.out.println("CONVERSION, 83, TAmaño pedido = " + detPedido.length);
		JSONObject allData = new JSONObject();
		
		JSONArray encabezadoJsonArray = new JSONArray();
        JSONObject dataEncabezado = new JSONObject();
        //JSONArray encabezado = new JSONArray();
        JSONArray detalle = new JSONArray();
        
        try {
        	float totalFactura = 0;
        	
	        Fecha fecha = new Fecha();
	        
	        for (int j = i; j < a2*LIMITE_FACTURA; j++){
	        		JSONObject dataDetalle= new JSONObject();
		            dataDetalle.put("artcodigo", detPedido[j].getCodigoProducto());
		            dataDetalle.put("movunidades", String.valueOf(detPedido[j].getTotalUnidades()));
		            dataDetalle.put("movprecio", String.valueOf(detPedido[j].getPrecioSeleccionado()));
		            dataDetalle.put("unidadesxfardo", detPedido[j].getUnidadesCaja());
		            dataDetalle.put("movfechaentrega", fecha.semanaSiguiente());
		            totalFactura = totalFactura + Float.parseFloat(detPedido[j].getSubTotal());
		            detalle.put(dataDetalle);
		            System.out.println("CONVERSION, 113, se agrego "+j +" articulo");
		            if (j == tamano-1){
		            j=a2*LIMITE_FACTURA;
		            }
	        }
	        
	        dataEncabezado.put("id", "1");
			dataEncabezado.put("clicodigo", encabezado.getCodigoCliente());
			dataEncabezado.put("movtotal", String.valueOf(totalFactura));
			dataEncabezado.put("movefectivo", encabezado.getEfectivo());
	        dataEncabezado.put("movcheque", encabezado.getCheque());
	        dataEncabezado.put("movcredito", encabezado.getCredito());
	        dataEncabezado.put("movfecha", encabezado.getFecha());
	        dataEncabezado.put("movhora", encabezado.getHora());
	                
	        //encabezado.put(dataEncabezado);
	        System.out.println("CONVERSION, 115, tamañoPedido de factura " + a2 + " = " + detalle.length());
	        encabezadoJsonArray.put(dataEncabezado);
	        allData.put("encabezado", encabezadoJsonArray);
	        allData.put("detalle", detalle);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String result = allData.toString();
		return result;
	}
	
	public String creaJsonEstadoCuenta(Cliente cliente){
		return null;
		
	}

}
