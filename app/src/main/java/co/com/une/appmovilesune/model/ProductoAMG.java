package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductoAMG implements Serializable {

    public static final String NUEVO = "N";
    public static final String CAMBIO = "C";
    public static final String EXISTENTE = "E";

    public String identificador;
    public String producto;
    public String nombre;
    public String plan;
    public double cargoBasico;
    public String velocidad;
    public ArrayList<String[]> descuentosComerciales;
    public double totalCargoBasico;
    public ArrayList<String[]> iva;
    public ArrayList<ArrayList<AdicionalAMG>> adicionales;
    public ArrayList<ArrayList<AdicionalAMG>> otrosAdicionales;
    public ArrayList<String[]> otrosFinancieros;
    public String acceso;
    public double totalProducto;
    public ArrayList<String[]> independientes;
    public ArrayList<String[]> promociones;
    public ArrayList<String[]> detalles;
    public String planFactura;
    public String nuevoCambioExistente;
    public String descuento;
    public String duracion;
    public String tecnologia;
    public String cliente;
    public String planFacturacion;
    public String estado;
    public String tecnologiacr;

    public ProductoAMG() {

    }

    public ProductoAMG(JSONObject producto) throws JSONException {

        // Log.d("ProductoAMG", producto.toString());
        identificador = producto.getString("identificador");
        this.producto = producto.getString("producto");
        nombre = producto.getString("productoNombre");
        plan = producto.getString("plan");
        cargoBasico = producto.getDouble("cargoBasico");
        velocidad = producto.getString("velocidad");
        cliente = producto.getString("clienteId");

        if (!producto.get("descuentosComerciales").getClass().getSimpleName().equals(JSONArray.class.getSimpleName())) {
            descuentosComerciales = new ArrayList<String[]>();
            for (int i = 0; i < producto.getJSONObject("descuentosComerciales").length(); i++) {
                String[] data = new String[]{
                        producto.getJSONObject("descuentosComerciales").names().get(i).toString(),
                        producto.getJSONObject("descuentosComerciales")
                                .getString(producto.getJSONObject("descuentosComerciales").names().get(i).toString())};
                descuentosComerciales.add(data);
            }
        }

        totalCargoBasico = producto.getDouble("totalCargoBasico");
        if (!producto.get("iva").getClass().getSimpleName().equals(JSONArray.class.getSimpleName())) {
            iva = new ArrayList<String[]>();
            for (int i = 0; i < producto.getJSONObject("iva").length(); i++) {
                String[] data = new String[]{producto.getJSONObject("iva").names().get(i).toString(), producto
                        .getJSONObject("iva").getString(producto.getJSONObject("iva").names().get(i).toString())};
                iva.add(data);
            }
        }

        if (!producto.get("otrosFinancieros").getClass().getSimpleName().equals(JSONArray.class.getSimpleName())) {
            otrosFinancieros = new ArrayList<String[]>();
            for (int i = 0; i < producto.getJSONObject("otrosFinancieros").length(); i++) {
                String[] data = new String[]{producto.getJSONObject("otrosFinancieros").names().get(i).toString(),
                        producto.getJSONObject("otrosFinancieros")
                                .getString(producto.getJSONObject("otrosFinancieros").names().get(i).toString())};
                otrosFinancieros.add(data);
            }
        }

        acceso = producto.getString("acceso");
        totalProducto = producto.getDouble("totalProducto");
        if (!producto.get("mostrarIndependientes").getClass().getSimpleName().equals(JSONArray.class.getSimpleName())) {
            independientes = new ArrayList<String[]>();
            for (int i = 0; i < producto.getJSONObject("mostrarIndependientes").length(); i++) {
                String[] data = new String[]{
                        producto.getJSONObject("mostrarIndependientes").names().get(i).toString(),
                        producto.getJSONObject("mostrarIndependientes")
                                .getString(producto.getJSONObject("mostrarIndependientes").names().get(i).toString())};
                independientes.add(data);
            }
        }

        if (!producto.get("aplicaPromocion").getClass().getSimpleName().equals(JSONArray.class.getSimpleName())) {
            promociones = new ArrayList<String[]>();
            for (int i = 0; i < producto.getJSONObject("aplicaPromocion").length(); i++) {
                String[] data = new String[]{producto.getJSONObject("aplicaPromocion").names().get(i).toString(),
                        producto.getJSONObject("aplicaPromocion")
                                .getString(producto.getJSONObject("aplicaPromocion").names().get(i).toString())};
                promociones.add(data);
            }
        }

        if (!producto.get("detalles").getClass().getSimpleName().equals(JSONArray.class.getSimpleName())) {
            detalles = new ArrayList<String[]>();
            for (int i = 0; i < producto.getJSONObject("detalles").length(); i++) {
                String[] data = new String[]{producto.getJSONObject("detalles").names().get(i).toString(),
                        producto.getJSONObject("detalles")
                                .getString(producto.getJSONObject("detalles").names().get(i).toString())};
                detalles.add(data);
            }
        }
        planFactura = producto.getString("planFactura");
        planFacturacion = producto.getString("planFacturacion");
        adicionales = new ArrayList<ArrayList<AdicionalAMG>>();
        for (int i = 0; i < producto.getJSONArray("adicionales").length(); i++) {
            JSONObject adicionalCat = producto.getJSONArray("adicionales").getJSONObject(i);
            String produnto = adicionalCat.getString("producto");
            ArrayList<AdicionalAMG> dataCat = new ArrayList<AdicionalAMG>();
            for (int j = 0; j < adicionalCat.getJSONArray("groupeddata").length(); j++) {
                JSONObject adicional = adicionalCat.getJSONArray("groupeddata").getJSONObject(j);
                AdicionalAMG data = new AdicionalAMG(adicional, produnto);
                dataCat.add(data);
            }
            adicionales.add(dataCat);
        }

        otrosAdicionales = new ArrayList<ArrayList<AdicionalAMG>>();
        for (int i = 0; i < producto.getJSONArray("otrosAdicionales").length(); i++) {
            JSONObject adicionalCat = producto.getJSONArray("otrosAdicionales").getJSONObject(i);
            String produnto = adicionalCat.getString("producto");
            ArrayList<AdicionalAMG> dataCat = new ArrayList<AdicionalAMG>();
            for (int j = 0; j < adicionalCat.getJSONArray("groupeddata").length(); j++) {
                JSONObject adicional = adicionalCat.getJSONArray("groupeddata").getJSONObject(j);
                AdicionalAMG data = new AdicionalAMG(adicional, produnto);
                dataCat.add(data);
            }
            otrosAdicionales.add(dataCat);
        }

        estado = producto.getString("estadoServicio");

        if (producto.has("tecnologia")) {
            tecnologiacr = producto.getString("tecnologia");
        }

    }

    public double calcularCargoAdicionales() {
        double total = 0;
        if (adicionales != null) {
            for (int i = 0; i < adicionales.size(); i++) {
                for (int j = 0; j < adicionales.get(i).size(); j++) {
                    total = total + adicionales.get(i).get(j).total;
                }
            }
        }

        return total;

    }

    public double calcularCargoFinancieros() {

        double total = 0;
        if (otrosFinancieros != null) {
            for (int i = 0; i < otrosFinancieros.size(); i++) {
                total += Double.valueOf(otrosFinancieros.get(i)[1]);
            }
        }

        if (iva != null) {
            for (int i = 0; i < iva.size(); i++) {
                total += Double.valueOf(iva.get(i)[1]);
            }
        }

        return total;
    }

    public double obtenerIVA() {
        double total = 0;
        if (iva != null) {
            for (int i = 0; i < iva.size(); i++) {
                if (iva.get(i)[0].equalsIgnoreCase("Total")) {
                    total += Double.valueOf(iva.get(i)[1]);
                }
            }
        }
        return total;
    }

    public ProductoAMG clone() {
        ProductoAMG clon = new ProductoAMG();
        clon.identificador = this.identificador;
        clon.producto = this.producto;
        clon.nombre = this.nombre;
        clon.plan = this.plan;
        clon.cargoBasico = this.cargoBasico;
        clon.velocidad = this.velocidad;
        clon.descuentosComerciales = this.descuentosComerciales;
        clon.totalCargoBasico = this.totalCargoBasico;
        clon.iva = this.iva;
        clon.adicionales = this.adicionales;
        clon.otrosAdicionales = this.otrosAdicionales;
        clon.otrosFinancieros = this.otrosFinancieros;
        clon.acceso = this.acceso;
        clon.totalProducto = this.totalProducto;
        clon.independientes = this.independientes;
        clon.promociones = this.promociones;
        clon.detalles = this.detalles;
        clon.planFactura = this.planFactura;
        clon.nuevoCambioExistente = this.nuevoCambioExistente;
        clon.descuento = this.descuento;
        clon.duracion = this.duracion;
        clon.tecnologia = this.tecnologia;
        clon.cliente = this.cliente;
        clon.planFacturacion = this.planFacturacion;

        clon.tecnologiacr = this.tecnologiacr;
        return clon;
    }

}
