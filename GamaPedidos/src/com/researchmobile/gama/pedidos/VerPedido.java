package com.researchmobile.gama.pedidos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.researchmobile.gama.entidades.DetallePedido;
import com.researchmobile.gama.entidades.NuevoPedido;

public class VerPedido extends Activity{
	
	private NuevoPedido pedido;
	private DetallePedido[] detallePedido;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_pedido);
        Bundle bundle = getIntent().getExtras();
        setPedido((NuevoPedido)bundle.get("pedido"));
        
        llenaDetalle();
        //Toast.makeText(getBaseContext(), String.valueOf(getDetallePedido().length), Toast.LENGTH_LONG).show();
	}

	private void llenaDetalle() {
		int tamano = getPedido().getDetallePedido().length;
		int tamanoPedido = 0;
		DetallePedido[] detalle = new DetallePedido[tamano];
		for (int i = 0; i<tamano;i++){
			if (getPedido().getDetallePedido()[i].getCaja().equalsIgnoreCase("0") &&
				getPedido().getDetallePedido()[i].getUnidad().equalsIgnoreCase("0")	){
			
			}else{
				detalle[i].setCaja(getPedido().getDetallePedido()[i].getCaja());
				detalle[i].setCodigoProducto(getPedido().getDetallePedido()[i].getCodigoProducto());
				detalle[i].setDescripcion(getPedido().getDetallePedido()[i].getDescripcion());
				detalle[i].setPrecio(getPedido().getDetallePedido()[i].getPrecio());
				detalle[i].setPrecio1(getPedido().getDetallePedido()[i].getPrecio1());
				detalle[i].setPrecio2(getPedido().getDetallePedido()[i].getPrecio2());
				detalle[i].setPrecio3(getPedido().getDetallePedido()[i].getPrecio3());
				detalle[i].setUnidad(getPedido().getDetallePedido()[i].getUnidad());
				tamanoPedido++;
				
			}
		}
		setDetallePedido(detalle);
		
	}

	public NuevoPedido getPedido() {
		return pedido;
	}

	public void setPedido(NuevoPedido pedido) {
		this.pedido = pedido;
	}

	public DetallePedido[] getDetallePedido() {
		return detallePedido;
	}

	public void setDetallePedido(DetallePedido[] detallePedido) {
		this.detallePedido = detallePedido;
	}

}
