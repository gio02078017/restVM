package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.google.analytics.tracking.android.Log;
import com.itextpdf.text.log.SysoCounter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ListaIpDinamica;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesDecos;
import co.com.une.appmovilesune.interfaces.Observer;

public class AmigoCuentas extends AsyncTask<ArrayList<Object>, Integer, ArrayList<Object>> implements Serializable {

    public final static String OFERTA_SUPER = "super";
    public final static String OFERTA_BASICA = "basica";
    public final static String OFERTA_RETENCION = "retencion";
    public final static String OFERTA_DIGITAL = "digital";
    public final static String OFERTA_PROMO12 = "PROMO12";

    public String portafolio;
    public String ofertaSuper, ofertaBasica, ofertaRetencion, ofertaDigital, ofertaDigitalConfigurable, ofertaPromo12;
    public ArrayList<ProductoAMG> productosPortafolio;
    public ArrayList<ArrayList<ProductoAMG>> productosOfertaSuper;
    public ArrayList<ArrayList<ProductoAMG>> productosOfertaBasica;
    public ArrayList<ArrayList<ProductoAMG>> productosOfertaRetencion;
    public ArrayList<ArrayList<ProductoAMG>> productosOfertaDigital;
    public ArrayList<ArrayList<ProductoAMG>> productosOfertaDigitalConfig;
    public ArrayList<ArrayList<ProductoAMG>> productosOfertaPromo12;

    public ArrayList<ProductoAMG> productosBasica;
    public ArrayList<ProductoAMG> productosSuper;
    public ArrayList<ProductoAMG> productosRetencion;
    public ArrayList<ProductoAMG> productosDigital;
    public ArrayList<ProductoAMG> productosDigitalConfig;
    public ArrayList<ProductoAMG> productosPromo12;
    public ArrayList<ItemDecodificador> decodificadores;
    public ArrayList<ListaIpDinamica> ipDinamica;

    public ArrayList<String[]> direcciones;
    public Cliente cliente;
    private String accion, lugar = "";
    private Context context;
    ProgressDialog pd;

    public String tipo, modulo;

    public ProductoAMG toExistente, tvExistente, baExistente;

    public boolean suspendido = false;

    public String municipio = "";

    public AmigoCuentas() {
        productosBasica = new ArrayList<ProductoAMG>();
        productosSuper = new ArrayList<ProductoAMG>();
        productosRetencion = new ArrayList<ProductoAMG>();
        productosDigital = new ArrayList<ProductoAMG>();
        productosDigitalConfig = new ArrayList<ProductoAMG>();
        productosPromo12 = new ArrayList<ProductoAMG>();
    }

    @Override
    protected void onPreExecute() {
        System.out.println("onPreExecute");
        accion = "";
        if (lugar.equals("Manual")) {
            /*
			 * pd = ProgressDialog.show(context, "Consultando",
			 * "Obteniendo Informacion...");
			 */
        } else {
            MainActivity.btnAmigoCuentas.setVisible();
        }
    }

    @Override
    protected ArrayList<Object> doInBackground(ArrayList<Object>... params) {
        // TODO Auto-generated method stub
        System.out.println("doInBackground");
        // System.out.println("parametros "+params[0]);
        ArrayList<Object> respuesta = new ArrayList<Object>();
        try {

            ArrayList<String> intro = (ArrayList<String>) params[0].get(0);
            String valor = intro.get(0);
            String tipo = intro.get(1);
            String municipio = intro.get(2);

            System.out.println("observer " + params[0].get(1));
            respuesta.add(params[0].get(1));

            JSONObject data = new JSONObject();
            data.put("valor", valor);
            data.put("tipo", tipo);
            data.put("municipio", municipio);

            // System.out.println("data "+data.toString());

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"parametros", data.toString()});
            JSONObject jo = new JSONObject(MainActivity.conexion.ejecutarSoap("getAmigoCuentas", parametros));

            String res = jo.get("data").toString();
            // System.out.println("Configuracion => "+data);
            if (Configuracion.validarIntegridad(res, jo.get("crc").toString())) {
                res = new String(Base64.decode(res));

                // System.out.println("res" + res);

                jo = new JSONObject(res);
                this.tipo = jo.getString("tipo");
                this.modulo = jo.getString("modulo");
                if (this.tipo.equals("Oferta")) {
                    portafolio = jo.getJSONObject("portafolio").toString();
                    organizarEstructuraPortafolio();

                    jo = jo.getJSONObject("data");
                    if (modulo.equals("normal")) {
                        ofertaSuper = jo.getJSONArray("superplay").toString();
                        organizarEstructuraSuper();
                        ofertaBasica = jo.getJSONArray("basica").toString();
                        organizarEstructuraBasica();
                    } else if (modulo.equals("digital")) {
                        ofertaDigital = jo.getJSONArray("Ultra").toString();
                        organizarEstructuraDigital();
                        ofertaPromo12 = jo.getJSONArray("PROMO12").toString();
                        organizarEstructuraPromo12();
                        ofertaDigitalConfigurable = jo.getJSONArray("DigitalConfig").toString();
                        organizarEstructuraDigitalConfig();
                    }

                    ofertaRetencion = jo.getJSONArray("retencion").toString();
                    organizarEstructuraRetencion();

                } else if (this.tipo.equals("Selector")) {
                    direcciones = new ArrayList<String[]>();
                    for (int i = 0; i < jo.getJSONArray("data").length(); i++) {
                        JSONObject direccion = jo.getJSONArray("data").getJSONObject(i);
                        String[] direccionData = new String[]{direccion.get("CH").toString(),
                                direccion.get("DIR").toString()};
                        direcciones.add(direccionData);
                    }
                } else {
                    respuesta.add(false);
                    return respuesta;
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            respuesta.add(false);
            return respuesta;
        }

        if (productosPortafolio != null) {
            if (!productosPortafolio.isEmpty()) {
                respuesta.add(true);
            } else {
                respuesta.add(false);
            }
        } else {
            respuesta.add(false);
        }

        return respuesta;
    }

    @Override
    protected void onPostExecute(ArrayList<Object> result) {
        boolean resultado = false;
        Observer ob = null;
        ob = (Observer) result.get(0);
        resultado = (Boolean) result.get(1);

        MainActivity.btnAmigoCuentas.setInvisible();
        if (resultado) {
            MainActivity.btnAmigoCuentas.setOK();
        } else {
            MainActivity.btnAmigoCuentas.setWRONG();
        }

        if (ob != null) {
            System.out.println("Observador AMC -> " + ob);
            ob.update(tipo);
        }

    }

    public void setManual() {
        this.lugar = "Manual";
        // this.context = context;
    }

    public void organizarEstructuraSuper() {

        try {
            productosOfertaSuper = new ArrayList<ArrayList<ProductoAMG>>();
            JSONArray productos = new JSONArray(this.ofertaSuper);
            System.out.println("this.ofertaSuper " + this.ofertaSuper);
            for (int i = 0; i < productos.length(); i++) {
                ArrayList<ProductoAMG> prods = new ArrayList<ProductoAMG>();
                for (int j = 0; j < productos.getJSONObject(i).getJSONArray("groupeddata").length(); j++) {
                    ProductoAMG prod = new ProductoAMG(
                            productos.getJSONObject(i).getJSONArray("groupeddata").getJSONObject(j));
                    prods.add(prod);
                }
                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TO")) {
                    if (toExistente != null) {
                        ProductoAMG productoEx = toExistente;
                        productoEx.plan = "Telefonia Existente";
                        prods.add(productoEx);
                    }
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TV")) {
                    if (tvExistente != null) {
                        ProductoAMG productoEx = tvExistente;
                        if (productoEx.tecnologia.equalsIgnoreCase("HFC")) {
                            productoEx.plan = "HFC Existente";
                        } else {
                            productoEx.plan = "IPTV Existente";
                        }

                        prods.add(productoEx);
                    }
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("BA")) {
                    if (baExistente != null) {
                        ProductoAMG productoEx = baExistente;
                        productoEx.plan = "Internet Existente";
                        prods.add(productoEx);
                    }
                }

                productosOfertaSuper.add(prods);
            }
            // Log.d("organizarEstructura",productos.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void organizarEstructuraBasica() {

        try {
            productosOfertaBasica = new ArrayList<ArrayList<ProductoAMG>>();
            JSONArray productos = new JSONArray(this.ofertaBasica);
            System.out.println("this.ofertaBasica " + this.ofertaBasica);
            for (int i = 0; i < productos.length(); i++) {
                ArrayList<ProductoAMG> prods = new ArrayList<ProductoAMG>();
                for (int j = 0; j < productos.getJSONObject(i).getJSONArray("groupeddata").length(); j++) {
                    ProductoAMG prod = new ProductoAMG(
                            productos.getJSONObject(i).getJSONArray("groupeddata").getJSONObject(j));
                    prods.add(prod);
                }
                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TO")) {
                    if (toExistente != null) {
                        System.out.println("toExistente.plan => " + toExistente.plan);
                        ProductoAMG productoEx = toExistente;
                        productoEx.plan = "Telefonia Existente";
                        System.out.println("toExistente.plan => " + toExistente.plan);
                        System.out.println("productoEx.plan => " + productoEx.plan);
                        prods.add(productoEx);
                    }
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TV")) {
                    if (tvExistente != null) {
                        ProductoAMG productoEx = tvExistente;
                        if (productoEx.tecnologia.equalsIgnoreCase("HFC")) {
                            productoEx.plan = "HFC Existente";
                        } else {
                            productoEx.plan = "IPTV Existente";
                        }

                        prods.add(productoEx);
                    }
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("BA")) {
                    if (baExistente != null) {
                        ProductoAMG productoEx = baExistente;
                        productoEx.plan = "Internet Existente";
                        prods.add(productoEx);
                    }
                }
                productosOfertaBasica.add(prods);
            }
            // Log.d("organizarEstructura",productos.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void organizarEstructuraRetencion() {

        try {
            productosOfertaRetencion = new ArrayList<ArrayList<ProductoAMG>>();
            JSONArray productos = new JSONArray(this.ofertaRetencion);
            System.out.println("this.ofertaRetencion " + this.ofertaRetencion);

            for (int i = 0; i < productos.length(); i++) {
                ArrayList<ProductoAMG> prods = new ArrayList<ProductoAMG>();

                for (int j = 0; j < productos.getJSONObject(i).getJSONArray("groupeddata").length(); j++) {
                    ProductoAMG prod = new ProductoAMG(
                            productos.getJSONObject(i).getJSONArray("groupeddata").getJSONObject(j));
                    prods.add(prod);
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TO")) {
                    if (toExistente != null) {
                        ProductoAMG productoEx = toExistente;
                        productoEx.plan = "Telefonia Existente";
                        prods.add(productoEx);
                    }
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TV")) {
                    if (tvExistente != null) {
                        ProductoAMG productoEx = tvExistente;
                        if (productoEx.tecnologia.equalsIgnoreCase("HFC")) {
                            productoEx.plan = "HFC Existente";
                        } else {
                            productoEx.plan = "IPTV Existente";
                        }

                        prods.add(productoEx);
                    }
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("BA")) {
                    if (baExistente != null) {
                        ProductoAMG productoEx = baExistente;
                        productoEx.plan = "Internet Existente";
                        prods.add(productoEx);
                    }
                }

                productosOfertaRetencion.add(prods);
            }
            // Log.d("organizarEstructura",productos.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void organizarEstructuraDigital() {

        try {
            productosOfertaDigital = new ArrayList<ArrayList<ProductoAMG>>();
            JSONArray productos = new JSONArray(this.ofertaDigital);
            System.out.println("this.ofertaDigital " + this.ofertaDigital);

            for (int i = 0; i < productos.length(); i++) {
                ArrayList<ProductoAMG> prods = new ArrayList<ProductoAMG>();

                for (int j = 0; j < productos.getJSONObject(i).getJSONArray("groupeddata").length(); j++) {
                    ProductoAMG prod = new ProductoAMG(
                            productos.getJSONObject(i).getJSONArray("groupeddata").getJSONObject(j));
                    prods.add(prod);
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TO")) {
                    if (toExistente != null) {
                        ProductoAMG productoEx = toExistente;
                        productoEx.plan = "Telefonia Existente";
                        prods.add(productoEx);
                    }
                }

                System.out.println("productos.getJSONObject(i)" + productos.getJSONObject(i));

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TV")) {
                    if (tvExistente != null) {
                        ProductoAMG productoEx = tvExistente;
                        if (productoEx.tecnologia.equalsIgnoreCase("HFC")) {
                            productoEx.plan = "HFC Existente";
                        } else {
                            productoEx.plan = "IPTV Existente";
                        }

                        prods.add(productoEx);
                    }
                }

                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("BA")) {
                    if (baExistente != null) {
                        ProductoAMG productoEx = baExistente;
                        productoEx.plan = "Internet Existente";
                        prods.add(productoEx);
                    }
                }

                productosOfertaDigital.add(prods);
            }
            // Log.d("organizarEstructura",productos.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void organizarEstructuraDigitalConfig() {
        try {
            productosOfertaDigitalConfig = new ArrayList<ArrayList<ProductoAMG>>();
            JSONArray productos = new JSONArray(this.ofertaDigitalConfigurable);
            System.out.println("this.ofertaDigital " + this.ofertaDigitalConfigurable);
            for (int i = 0; i < productos.length(); i++) {
                ArrayList<ProductoAMG> prods = new ArrayList<ProductoAMG>();
                for (int j = 0; j < productos.getJSONObject(i).getJSONArray("groupeddata").length(); j++) {
                    ProductoAMG prod = new ProductoAMG(
                            productos.getJSONObject(i).getJSONArray("groupeddata").getJSONObject(j));
                    prods.add(prod);
                }
                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TO")) {
                    if (toExistente != null) {
                        ProductoAMG productoEx = toExistente;
                        productoEx.plan = "Telefonia Existente";
                        prods.add(productoEx);
                    }
                }
                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("TV")) {
                    if (tvExistente != null) {
                        ProductoAMG productoEx = tvExistente;
                        if (productoEx.tecnologia.equalsIgnoreCase("HFC")) {
                            productoEx.plan = "HFC Existente";
                        } else {
                            productoEx.plan = "IPTV Existente";
                        }
                        prods.add(productoEx);
                    }
                }
                if (productos.getJSONObject(i).getString("producto").equalsIgnoreCase("BA")) {
                    if (baExistente != null) {
                        ProductoAMG productoEx = baExistente;
                        productoEx.plan = "Internet Existente";
                        prods.add(productoEx);
                    }
                }
                productosOfertaDigitalConfig.add(prods);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void organizarEstructuraPromo12() {
        try {
            productosOfertaPromo12 = new ArrayList<ArrayList<ProductoAMG>>();
            JSONArray productos = new JSONArray(this.ofertaPromo12);
            System.out.println("this.ofertaPromo12 " + this.ofertaPromo12);
            for (int i = 0; i < productos.length(); i++) {
                ArrayList<ProductoAMG> prods = new ArrayList<ProductoAMG>();
                for (int j = 0; j < productos.getJSONObject(i).getJSONArray("groupeddata").length(); j++) {
                    ProductoAMG prod = new ProductoAMG(
                            productos.getJSONObject(i).getJSONArray("groupeddata").getJSONObject(j));
                    prods.add(prod);
                }
				/*
				 * if(productos.getJSONObject(i).getString("producto").
				 * equalsIgnoreCase("TO")){ if(toExistente != null){ ProductoAMG
				 * productoEx = toExistente; productoEx.plan =
				 * "Telefonia Existente"; prods.add(productoEx); } }
				 * if(productos.getJSONObject(i).getString("producto").
				 * equalsIgnoreCase("TV")){ if(tvExistente != null){ ProductoAMG
				 * productoEx = tvExistente;
				 * if(productoEx.tecnologia.equalsIgnoreCase("HFC")){
				 * productoEx.plan = "HFC Existente"; }else{ productoEx.plan =
				 * "IPTV Existente"; } prods.add(productoEx); } }
				 * if(productos.getJSONObject(i).getString("producto").
				 * equalsIgnoreCase("BA")){ if(baExistente != null){ ProductoAMG
				 * productoEx = baExistente; productoEx.plan =
				 * "Internet Existente"; prods.add(productoEx); } }
				 */
                productosOfertaPromo12.add(prods);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void organizarEstructuraPortafolio() {

        try {
            productosPortafolio = new ArrayList<ProductoAMG>();
            JSONObject portafolio = new JSONObject(this.portafolio);
            JSONArray productos = portafolio.getJSONArray("productosFactura");
            JSONObject tecnologia = portafolio.getJSONObject("infoCliente").getJSONObject("tecnologia");
            // Log.d("organizarEstructura",productos.toString());

            municipio = portafolio.getJSONObject("infoCliente").getString("municipio");

            System.out.println("portafolio.getJSONObject('infoCliente)" + portafolio.getJSONObject("infoCliente"));

            decodificadores = UtilidadesDecos
                    .decodificadoresExistentes(portafolio.getJSONObject("infoCliente").toString());

            ipDinamica = Utilidades.direccionamientoIP(portafolio.getJSONObject("infoCliente").toString());

            System.out.println("ipDinamica " + ipDinamica);

            System.out.println("decodificadores " + decodificadores);

            for (int i = 0; i < productos.length(); i++) {
                JSONObject producto = productos.getJSONObject(i);

                ProductoAMG productoAmc = new ProductoAMG(producto);

                System.out.println("productoAmc " + productoAmc);

                if (productoAmc.estado.equalsIgnoreCase("SXFP")) {
                    this.suspendido = true;
                }

                if (productoAmc.producto.equalsIgnoreCase("TO")) {
                    toExistente = productoAmc.clone();

                    productosPortafolio.add(productoAmc);
                }

                if (productoAmc.producto.equalsIgnoreCase("TV") || productoAmc.producto.contains("TELEV")) {
                    if (tecnologia.has("HFC")) {
                        if (tecnologia.getString("HFC").equals("1")) {
                            productoAmc.tecnologia = "HFC";
                        } else {
                            productoAmc.tecnologia = "REDCO";
                        }
                    }
                    tvExistente = productoAmc.clone();

                    productosPortafolio.add(productoAmc);
                }

                if (productoAmc.producto.equalsIgnoreCase("BA")) {
                    baExistente = productoAmc.clone();
                    productosPortafolio.add(productoAmc);

                }

            }
            System.out.println("asds");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void agrearProducto(ProductoAMG producto, String oferta) {

        validarProductoOferta(producto.producto, oferta);
        indicarNuevoCambioExistente(producto);
        if (oferta.equals(OFERTA_BASICA)) {
            productosBasica.add(producto);
            System.out.println("Producto agregado a la oferta " + oferta);
        } else if (oferta.equals(OFERTA_SUPER)) {
            productosSuper.add(producto);
            System.out.println("Producto agregado a la oferta " + oferta);
        } else if (oferta.equals(OFERTA_RETENCION)) {
            productosRetencion.add(producto);
            System.out.println("Producto agregado a la oferta " + oferta);
        }

    }

    private void validarProductoOferta(String producto, String oferta) {
        if (oferta.equals(OFERTA_BASICA)) {
            for (int i = 0; i < productosBasica.size(); i++) {
                if (productosBasica.get(i).producto.equals(producto)) {
                    productosBasica.remove(i);
                }
            }
        } else if (oferta.equals(OFERTA_SUPER)) {
            for (int i = 0; i < productosSuper.size(); i++) {
                if (productosSuper.get(i).producto.equals(producto)) {
                    productosSuper.remove(i);
                }
            }
        } else if (oferta.equals(OFERTA_RETENCION)) {
            for (int i = 0; i < productosRetencion.size(); i++) {
                if (productosRetencion.get(i).producto.equals(producto)) {
                    productosRetencion.remove(i);
                }
            }
        }

    }

    private void indicarNuevoCambioExistente(ProductoAMG producto) {
        ProductoAMG auxiliar = null;
        for (int i = 0; i < productosPortafolio.size(); i++) {
            if (productosPortafolio.get(i).producto.equals(producto.producto)) {
                auxiliar = productosPortafolio.get(i);
            }
        }

        if (auxiliar == null) {
            producto.nuevoCambioExistente = producto.NUEVO;
        } else {
            if (auxiliar.plan.equals(producto.plan) || producto.plan.contains("Existente")) {
                producto.nuevoCambioExistente = producto.EXISTENTE;
            } else {
                producto.nuevoCambioExistente = producto.CAMBIO;
            }
        }
    }

    public void mostrarDescuentos(String oferta) {
        String planTo = "-", telefonia = "-", planTv = "-", television = "-", planBa = "-", internet = "-";
        if (oferta.equals(OFERTA_BASICA)) {
            for (int i = 0; i < productosBasica.size(); i++) {
                if (productosBasica.get(i).producto.equals("TO")) {
                    planTo = productosBasica.get(i).nuevoCambioExistente;
                    telefonia = productosBasica.get(i).plan;
                } else if (productosBasica.get(i).producto.equals("TV")) {
                    planTv = productosBasica.get(i).nuevoCambioExistente;
                    television = productosBasica.get(i).plan;
                } else if (productosBasica.get(i).producto.equals("BA")) {
                    planBa = productosBasica.get(i).nuevoCambioExistente;
                    internet = productosBasica.get(i).plan;
                } else if (productosBasica.get(i).producto.equals("ADICHD")) {
                    // Tarificador.itemPromocionesAdicionales
                }
            }
            ArrayList<ArrayList<String>> descuentos = Tarificador.Descuentos(planTo, telefonia, planTv, television,
                    planBa, internet);

            if (descuentos != null) {
                for (int i = 0; i < descuentos.size(); i++) {
                    for (int j = 0; j < productosBasica.size(); j++) {
                        if (productosBasica.get(j).producto.equalsIgnoreCase(descuentos.get(i).get(0))) {
                            productosBasica.get(j).descuento = descuentos.get(i).get(1);
                            productosBasica.get(j).duracion = descuentos.get(i).get(2);
                        }
                    }
                }
            }
        } else if (oferta.equals(OFERTA_SUPER)) {
            for (int i = 0; i < productosSuper.size(); i++) {
                if (productosSuper.get(i).producto.equals("TO")) {
                    planTo = productosSuper.get(i).nuevoCambioExistente;
                    telefonia = productosSuper.get(i).plan;
                } else if (productosSuper.get(i).producto.equals("TV")) {
                    planTv = productosSuper.get(i).nuevoCambioExistente;
                    television = productosSuper.get(i).plan;
                } else if (productosSuper.get(i).producto.equals("BA")) {
                    planBa = productosSuper.get(i).nuevoCambioExistente;
                    internet = productosSuper.get(i).plan;
                }
            }

            ArrayList<ArrayList<String>> descuentos = Tarificador.Descuentos(planTo, telefonia, planTv, television,
                    planBa, internet);
            if (descuentos != null) {
                for (int i = 0; i < descuentos.size(); i++) {
                    for (int j = 0; j < productosSuper.size(); j++) {
                        if (productosSuper.get(j).producto.equalsIgnoreCase(descuentos.get(i).get(0))) {
                            productosSuper.get(j).descuento = descuentos.get(i).get(1);
                            productosSuper.get(j).duracion = descuentos.get(i).get(2);
                        }
                    }
                }
            }
        } else if (oferta.equals(OFERTA_RETENCION)) {
            for (int i = 0; i < productosRetencion.size(); i++) {
                if (productosRetencion.get(i).producto.equals("TO")) {
                    planTo = productosRetencion.get(i).nuevoCambioExistente;
                    telefonia = productosRetencion.get(i).plan;
                } else if (productosRetencion.get(i).producto.equals("TV")) {
                    planTv = productosRetencion.get(i).nuevoCambioExistente;
                    television = productosRetencion.get(i).plan;
                } else if (productosRetencion.get(i).producto.equals("BA")) {
                    planBa = productosRetencion.get(i).nuevoCambioExistente;
                    internet = productosRetencion.get(i).plan;
                }
            }

            ArrayList<ArrayList<String>> descuentos = Tarificador.Descuentos(planTo, telefonia, planTv, television,
                    planBa, internet);
            if (descuentos != null) {
                for (int i = 0; i < descuentos.size(); i++) {
                    for (int j = 0; j < productosRetencion.size(); j++) {
                        if (productosRetencion.get(j).producto.equalsIgnoreCase(descuentos.get(i).get(0))) {
                            productosRetencion.get(j).descuento = descuentos.get(i).get(1);
                            productosRetencion.get(j).duracion = descuentos.get(i).get(2);
                        }
                    }
                }
            }
        }
    }

    private void consultarProductosTV() {

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?",
                new String[]{"ofertaDigital", "productos"}, null, null, null);

        if (respuesta != null) {
            ArrayList<ArrayList<String>> productos = MainActivity.basedatos.consultar(false, "precios",
                    new String[]{"Producto", "individual", "individual_iva - individual", "empaquetado",
                            "empaquetado_iva - empaquetado"},
                    "Producto=? and estrato like ?", new String[]{"ofertaDigital", "%" + cliente.getEstrato() + "%"},
                    null, null, null);
        }

    }
}
