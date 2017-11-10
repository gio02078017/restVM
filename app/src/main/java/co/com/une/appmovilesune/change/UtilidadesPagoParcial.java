package co.com.une.appmovilesune.change;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ListaCotizacion;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.ProductoCotizador;

/**
 * Created by Gospina on 28/10/2016.
 */

public class UtilidadesPagoParcial {

    public UtilidadesPagoParcial(){

    }

    public static ArrayList<ProductoCotizador> obtenerPagoParcial(CotizacionCliente cotizacionCliente) {

        ArrayList<ProductoCotizador> productos = cotizacionCliente.getProductoCotizador();

        if(cotizacionCliente.getContadorProductos() > 0){
            ArrayList<ArrayList<String>> resultQueryExterno =  resultQueryExternoPagoParcial(cotizacionCliente);
            if(resultQueryExterno != null && resultQueryExterno.size() >0){
                ArrayList<ArrayList<String>> resultQueryInterno =  resultQueryInternoPagoParcial(resultQueryExterno);
                if(resultQueryInterno != null &&  resultQueryInterno.size() > 0){
                    productos = consolidarProductosConPagoParcial (resultQueryInterno, productos);
                }

            }
        }

        return productos;
    }

    public static ArrayList<ArrayList<String>> resultQueryExternoPagoParcial(CotizacionCliente cotizacionCliente){
        ArrayList<ArrayList<String>> respuesta = null;

        if(cotizacionCliente.getContadorProductos() > 0){
            ArrayList<ListaCotizacion> listaCotizacion = listaCotizacion (cotizacionCliente.getProductoCotizador());

            if(listaCotizacion.size() > 0) {
                listaCotizacion =  listaCotizacionUnion(listaCotizacion);
                String datos = "";
                String uniones = "";
                String clausulas = "";

                for (int i = 0; i < listaCotizacion.size(); i++) {
                     if (i == 0) {
                        datos = datos + listaCotizacion.get(i).getDatos();
                        uniones = uniones + listaCotizacion.get(i).getUnion();
                        clausulas = clausulas + listaCotizacion.get(i).getClausula();
                    } else {
                        datos = datos + "||','||" + listaCotizacion.get(i).getDatos();
                        uniones = uniones + " \n " + listaCotizacion.get(i).getUnion();
                        clausulas = clausulas + " \n " + listaCotizacion.get(i).getClausula();
                    }

                }

                String query = "select " + datos + " " + uniones + "\n where " + clausulas + "\n and p1.tipoPaquete = " + cotizacionCliente.getContadorProductos();

                 respuesta = MainActivity.basedatos.consultar2(query);
            }
        }

        return respuesta;
    }

    public static ArrayList<ListaCotizacion> listaCotizacion (ArrayList<ProductoCotizador> productos ){
        ArrayList<ListaCotizacion> listaCotizacion = new ArrayList<ListaCotizacion>();
        for (int i = 0; i < productos.size(); i++) {
            if(!productos.get(i).getTipoPeticion().equalsIgnoreCase("-") && !productos.get(i).getPlan().equalsIgnoreCase("-")){
                listaCotizacion.add(new ListaCotizacion(productos.get(i).getTipoPeticion(),productos.get(i).traducirProducto().toUpperCase()));
            }
        }
        return listaCotizacion;
    }

    public static ArrayList<ListaCotizacion> listaCotizacionUnion (ArrayList<ListaCotizacion> listaCotizacion){
        for (int i = 0; i < listaCotizacion.size(); i++) {
            if(i == 0){
                listaCotizacion.get(i).setClausula("p1.producto='"+listaCotizacion.get(i).getTipoProducto()+"' AND p1.tipoTransacion= '"+ UtilidadesTarificadorNew.homologarTipoTransacion(listaCotizacion.get(i).getTipoTransacion())+"'");
                listaCotizacion.get(i).setDatos("p1.id");
                listaCotizacion.get(i).setUnion("from pagoParcial p1");
            }else if(i == 1){
                listaCotizacion.get(i).setClausula("and p2.producto='"+listaCotizacion.get(i).getTipoProducto()+"' AND p2.tipoTransacion= '"+ UtilidadesTarificadorNew.homologarTipoTransacion(listaCotizacion.get(i).getTipoTransacion())+"'");
                listaCotizacion.get(i).setDatos("p2.id");
                listaCotizacion.get(i).setUnion("INNER JOIN pagoParcial p2 ON p1.idPaquete=p2.idPaquete");
            }else if(i == 2){
                listaCotizacion.get(i).setClausula("and p3.producto='"+listaCotizacion.get(i).getTipoProducto()+"' AND p3.tipoTransacion= '"+ UtilidadesTarificadorNew.homologarTipoTransacion(listaCotizacion.get(i).getTipoTransacion())+"'");
                listaCotizacion.get(i).setDatos("p3.id");
                listaCotizacion.get(i).setUnion("INNER JOIN pagoParcial p3 ON p1.idPaquete=p3.idPaquete");
            }
        }
        return listaCotizacion;

    }

    public static ArrayList<ArrayList<String>> resultQueryInternoPagoParcial (ArrayList<ArrayList<String>> resultQueryExterno){

        String clausula2 = "id IN (?)";
        String[] valores = new String[]{resultQueryExterno.get(0).get(0)};

        String query = "select producto,valor,descuento from  pagoParcial where id in ("+resultQueryExterno.get(0).get(0)+")";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar2(query);

        return respuesta;
    }

    public static ArrayList<ProductoCotizador> consolidarProductosConPagoParcial (ArrayList<ArrayList<String>> resultQueryInterno, ArrayList<ProductoCotizador> productos){
        for (int i = 0; i < resultQueryInterno.size(); i++) {
            for (int j = 0; j < productos.size(); j++) {
                if (!productos.get(j).getTipoPeticion().equalsIgnoreCase("-") && !productos.get(j).getPlan().equalsIgnoreCase("-")) {
                    if (productos.get(j).traducirProducto().toUpperCase().equalsIgnoreCase(resultQueryInterno.get(i).get(0))) {
                        productos.get(j).setPagoParcial(Utilidades.convertirDouble(resultQueryInterno.get(i).get(1), "respuesta2.get(i).get(1)"));
                        productos.get(j).setPagoParcialDescuento(Utilidades.convertirDouble(resultQueryInterno.get(i).get(2), "respuesta2.get(i).get(2)"));
                        if (productos.get(j).getPagoParcialDescuento() > 0.0 && productos.get(j).getPagoParcial() > 0.0) {
                            productos.get(j).setTotalPagoParcial(productos.get(j).calcularDescuento());
                        } else {
                            productos.get(j).setTotalPagoParcial(productos.get(j).getPagoParcial());
                        }
                    }
                }
            }
        }
        return productos;
    }

    public static String obtenerCadenaValorConexion(ArrayList<ProductoCotizador> productos) {

        ArrayList<String> productosNuevos = new ArrayList<String>();
        String cadenaConexion = "";

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getTipoPeticion().equals("N")) {
                if (!productosNuevos.contains(productos.get(i).traducirProducto().toUpperCase())) {
                    productosNuevos.add(productos.get(i).traducirProducto().toUpperCase());
                }
            }        }

        for (String prod : productosNuevos) {
            cadenaConexion += prod + "-";
        }

        if (!cadenaConexion.equals("")) {
            cadenaConexion = cadenaConexion.substring(0, cadenaConexion.length() - 1);
        }

        return cadenaConexion;

    }

    public static double obtenerValorConexion(String cadena) {

        double valorConexion = 0;

        String clausula = "productos=?";
        String[] valores = new String[]{cadena};

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "valorconexion", new String[]{"valor"}, clausula,
                valores, null, null, null);

          if (respuesta != null) {
            valorConexion = Double.parseDouble(respuesta.get(0).get(0));
        }

        return valorConexion;

    }

    public static void guardarPagoParcial(JSONArray arrayPagoParcial) {

        MainActivity.basedatos.eliminar("pagoParcial", null, null);
        try {
            for (int i = 0; i < arrayPagoParcial.length(); i++) {
                ContentValues cv = new ContentValues();
                JSONObject pagoParcial = arrayPagoParcial.getJSONObject(i);
                cv.put("producto", pagoParcial.getString("producto"));
                cv.put("valor", pagoParcial.getString("valor"));
                cv.put("descuento", pagoParcial.getString("descuento"));
                cv.put("tipoPaquete", pagoParcial.getString("tipoPaquete"));
                cv.put("idPaquete", pagoParcial.getString("idPaquete"));
                cv.put("idPagoParcialPaquete", pagoParcial.getString("idPagoParcialPaquete"));
                cv.put("tipoTransacion", pagoParcial.getString("tipoTransacion"));

                MainActivity.basedatos.insertar("pagoParcial", cv);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void guardarValorConexion(JSONArray valoresConexion) {

        MainActivity.basedatos.eliminar("valorconexion", null, null);
        try {
            for (int i = 0; i < valoresConexion.length(); i++) {
                ContentValues cv = new ContentValues();
                JSONObject valorConexion = valoresConexion.getJSONObject(i);
                cv.put("productos", valorConexion.getString("productos"));
                cv.put("valor", valorConexion.getString("valor"));
                MainActivity.basedatos.insertar("valorconexion", cv);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
