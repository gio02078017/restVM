package co.com.une.appmovilesune.change;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.CrackleExistente;
import co.com.une.appmovilesune.adapters.ItemKeyValue;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.adapters.ListaCotizacion;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.ProductoCotizador;

/**
 * Created by Gospina on 28/10/2016.
 */

public class UtilidadesTarificadorNew {

    public UtilidadesTarificadorNew(){

    }

    public static ArrayList<String> aplicarDescuentos(String promocion,String duracion) {

      ArrayList<String> descuento = new ArrayList<String>();

        if(promocion.equalsIgnoreCase("0") || promocion.equalsIgnoreCase("0.0")){
            descuento.add("Sin Promocion");
            descuento.add("N/A");
        }else{
            descuento.add(promocion + "%");
            if(duracion.equalsIgnoreCase("1")){
                descuento.add(duracion + " Mes ");
            }else{
                descuento.add(duracion + " Meses ");
            }
        }

        return descuento;
    }

    public static boolean validarSegundaTelefonia(Cliente cliente) {

        boolean segundaTO = false;

        if (cliente.getPortafolio() != null) {
            try {
                JSONObject portafolio = new JSONObject(cliente.getPortafolio());
                if (portafolio.has("TO")) {

                    if (portafolio.get("TO").getClass().getSimpleName().equals("JSONArray")) {
                        JSONArray array = portafolio.getJSONArray("TO");
                        // Portafolio.add(new ListaDefault(0, "TO", "Titulo"));
                        for (int i = 0; i < array.length(); i++) {
                            if (array.getJSONObject(i).getString("Estado_Servicio").equalsIgnoreCase("ACTI")
                                    || array.getJSONObject(i).getString("Estado_Servicio").equalsIgnoreCase("SXFP")) {
                                segundaTO = true;
                            }
                        }

                    } else if (portafolio.get("TO").getClass().getSimpleName().equals("JSONObject")) {

                        JSONObject array = portafolio.getJSONObject("TO");

                        if (array.getString("Estado_Servicio").equalsIgnoreCase("ACTI")) {
                            segundaTO = true;
                        }
                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.w("Error", e.getMessage());
                // mensaje = "Problemas procesando el portafolio del cliente";
            }
        }

        return segundaTO;
    }

    public static void imprimirProductosCotizacion(ArrayList<ProductoCotizador> productos){

        System.out.println("************imprimirProductosCotizacion**********Inicio********");
        for (int i = 0; i < productos.size(); i++) {

            System.out.println("************Producto**********Posicion("+i+")********");
            System.out.println("************tipo**********"+productos.get(i).getTipo()+"********");
            System.out.println("************tipoPeticion**********"+productos.get(i).getTipoPeticion()+"********");
            System.out.println("************plan**********"+productos.get(i).getPlan()+"********");
            System.out.println("************planFacturacionInd**********"+productos.get(i).getPlanFacturacionInd()+"********");
            System.out.println("************planFacturacionEmp**********"+productos.get(i).getPlanFacturacionEmp()+"********");
            System.out.println("************cargoBasicoInd**********"+productos.get(i).getCargoBasicoInd()+"********");
            System.out.println("************cargoBasicoEmp**********"+productos.get(i).getCargoBasicoEmp()+"********");
            System.out.println("************cargoBasicoInd**********"+productos.get(i).getCargoBasicoInd()+"********");
            System.out.println("************descuentoCargobasico**********"+productos.get(i).getDescuentoCargobasico()+"********");
            System.out.println("************duracionDescuento**********"+productos.get(i).getDuracionDescuento()+"********");
            System.out.println("************velocidad**********"+productos.get(i).getVelocidad()+"********");
            System.out.println("************pagoParcial**********"+productos.get(i).getPagoParcial()+"********");
            System.out.println("************pagoParcialDescuento**********"+productos.get(i).getPagoParcialDescuento()+"********");
            System.out.println("************totalPagoParcial()**********"+productos.get(i).getTotalPagoParcial()+"********");
        }

        System.out.println("************imprimirProductosCotizacion**********Fin********");
    }

    public static void imprimirDescuentos(ArrayList<ArrayList<String>> descuentos){
         if(descuentos != null){
             for (int i = 0; i <descuentos.size() ; i++) {
                 System.out.println("impresion descuentos tipoProducto "+descuentos.get(i).get(0));
                 System.out.println("impresion descuentos descuento "+descuentos.get(i).get(1));
                 System.out.println("impresion descuentos tiempo "+descuentos.get(i).get(2));
             }
         }
    }

    public static boolean isTrioDuoNuevo(ArrayList<ProductoCotizador> productos){

        boolean trioDuoNuevo = false;

        ArrayList<Boolean> nuevos = new ArrayList<Boolean>();

        for (ProductoCotizador producto: productos) {
            if(producto.getTipoPeticion().equals("N")){
                nuevos.add(true);
            }
        }

        if(nuevos.size() >= 3){
            trioDuoNuevo = true;
        }

        return trioDuoNuevo;
    }

    public static boolean isDuoNuevo(ArrayList<ProductoCotizador> productos){

        boolean duoNuevo = false;

        ArrayList<Boolean> nuevos = new ArrayList<Boolean>();

        for (ProductoCotizador producto: productos) {
            if(producto.getTipoPeticion().equals("N")){
                nuevos.add(true);
            }
        }

        if(nuevos.size() == 2){
            duoNuevo = true;
        }

        return duoNuevo;
    }

    public static boolean isCotizacionConExistentes(ArrayList<ProductoCotizador> productos){

        boolean trioDuoNuevo = false;

        ArrayList<Boolean> nuevos = new ArrayList<Boolean>();

        for (ProductoCotizador producto: productos) {
            Log.d("TipoPeticion",producto.getTipoPeticion());
            if(producto.getTipoPeticion().equals("N") || producto.getTipoPeticion().equals("-")){
                nuevos.add(true);
            }else {
                nuevos.add(false);
            }
        }

        return nuevos.contains(false);
    }

    public static boolean validarEstandarizacion(Cotizacion cotizacion, Cliente cliente, Context contex) {

        boolean validarEstandarizacion = true;
        boolean validarTelefonoServicio = true;


        if (cotizacion.getTipoTo().equalsIgnoreCase("N") && !cotizacion.getTelefonia().equalsIgnoreCase(Utilidades.inicial_guion)) {
            if (Utilidades.visible("portafolioATC", cliente.getCiudad())
			/* && cliente.getPortafolio() == null */) {
                if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
                    // System.out.println("cliente.isConsultaNormalizada() "
                    // + cliente.isConsultaNormalizada());
                    // System.out.println("cliente.isControlNormalizada() "
                    // + cliente.isControlNormalizada());

                    if (!cliente.isControlCerca() && !Utilidades.CoberturaRural(cliente)) {
                        if (!cliente.isControlNormalizada()) {
                            validarEstandarizacion = false;
                            // ja.put(Utilidades.jsonMensajes("Direccion",
                            // "Debe Normalizar la Direcci�n"));
                            Toast.makeText(contex, "Debe Normalizar la Dirección", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cliente.getPortafolio() == null/*
																 * cliente.
																 * isConsultaNormalizada
																 * ()
																 */) {
                                //lanzarSimulador("direccion", "Portafolio");
                                ;
                            }
                        }
                    }

                }

                String telefono = Utilidades.limpiarTelefono(cliente.getTelefono());
                if (!telefono.equals("")) {
                    System.out.println("trlefono " + telefono);
                    if (telefono.length() != 7) {
                        validarTelefonoServicio = false;
                        Toast.makeText(contex, "Telefono De Servicio, Debe Estar Compuesto Por 7 Digitos",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }else if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
                System.out.println("estandarizarDireccion cliente.isControlNormalizada()  interna else "+cliente.isControlNormalizada());
                System.out.println("estandarizarDireccion Utilidades.CoberturaRural(cliente) interna else"+Utilidades.CoberturaRural(cliente));
                if (!cliente.isControlNormalizada() && !Utilidades.CoberturaRural(cliente)) {
                    validarEstandarizacion = false;
                    Toast.makeText(contex, contex.getResources().getString(R.string.normailizardireccion), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
            System.out.println("estandarizarDireccion cliente.isControlNormalizada() externa else "+cliente.isControlNormalizada());
            System.out.println("estandarizarDireccion Utilidades.CoberturaRural(cliente) externa else "+Utilidades.CoberturaRural(cliente));
            if (!cliente.isControlNormalizada() && !Utilidades.CoberturaRural(cliente)) {
                validarEstandarizacion = false;
                Toast.makeText(contex, contex.getResources().getString(R.string.normailizardireccion), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        System.out.println("validarEstandarizacion "+validarEstandarizacion);

        return validarEstandarizacion;

    }

    public static String innerJoinTarifas = "inner join condicionesxtarifas cxt on p.idTarifa = cxt.id_tarifa " +
            "inner join condiciones c on c.id = cxt.id_condicion ";

    public static String queryInternoTarifas(String departamento, String ciudad){
        boolean valido = false;

        String query ="SELECT DISTINCT(c1.id) FROM condiciones c1 " +
                      "INNER JOIN condiciones c2 ON c1.id=c2.id " +
                      "WHERE c1.clave='Departamento' AND (c1.valor = 'N/A' or c1.valor like '%"+departamento+"%') and c1.tipo ='Tarifa'" +
                      "AND c2.clave='Ciudad' AND (c2.valor = 'N/A' or c2.valor like '%"+ciudad+"%') and c2.tipo ='Tarifa' ";

        return query;
    }


    public static String homologarDepartamentoCotizacion(String departamento,String ciudad){

        if(departamento.equalsIgnoreCase("Distrito Capital De Bogota")){
            departamento = ciudad;
        }

        return departamento;
    }

    public static boolean ventaConExistente(Cotizacion cotizacion){
        boolean existentes = false;

        System.out.println("nuevos " + existentes);

        if (!cotizacion.getTelefonia().equalsIgnoreCase(Utilidades.inicial_guion)
                && !cotizacion.getTelefonia().equalsIgnoreCase("")) {

            System.out.println("cotizacion.getTipoCotizacionTo() " + cotizacion.getTipoCotizacionTo());
            if (!cotizacion.getTipoCotizacionTo().equalsIgnoreCase("1")) {
                existentes = true;
            }
        }

        if (!cotizacion.getInternet().equalsIgnoreCase(Utilidades.inicial_guion)
                && !cotizacion.getInternet().equalsIgnoreCase("")) {

            System.out.println("cotizacion.getTipoCotizacionBa() " + cotizacion.getTipoCotizacionBa());

            if (!cotizacion.getTipoCotizacionBa().equalsIgnoreCase("1")) {
                existentes = true;
            }
        }

        if (!cotizacion.getTelevision().equalsIgnoreCase(Utilidades.inicial_guion)
                && !cotizacion.getTelevision().equalsIgnoreCase("")) {

            System.out.println("cotizacion.getTipoCotizacionTv() " + cotizacion.getTipoCotizacionTv());

            if (!cotizacion.getTipoCotizacionTv().equalsIgnoreCase("1")) {
                existentes = true;
            }
        }

        System.out.println("nuevos " + existentes);

        return existentes;
    }

    public static boolean validarVelocidadInternet(String plan, String adicional, Cliente cliente) {

        boolean result = false;

        System.out.println("adicional_ba adicional "+ adicional);
        System.out.println("adicional_ba plan "+ plan);

        String parametros = "departamento like ? and tipo_producto = ? and estrato like ? and Producto = ?";
        String[] datos = new String[]{"%" + cliente.getDepartamento() + "%",  "ba", "%" + cliente.getEstrato() + "%", plan};

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Precios",
                new String[]{"ProductoHomologado", "velocidadGota"},
                parametros, datos,
                null, null, null);

        System.out.println("adicional_ba validarVelocidadInternet "+ respuesta);


        if(respuesta != null){

            int velocidadPlan = Integer.parseInt(respuesta.get(0).get(0).replace("MB",""));
            int velocidadGota = 0;

            if(!respuesta.get(0).get(1).equalsIgnoreCase("") && !respuesta.get(0).get(1).equalsIgnoreCase("N/A")){
                velocidadGota = Integer.parseInt(respuesta.get(0).get(1).replace("MB",""));
            }

            if(velocidadPlan >= obtenerVelocidadMinimaInternet(adicional) || velocidadGota >= obtenerVelocidadMinimaInternet(adicional)){
                result = true;
            } else {
                result = false;
            }

        }

        return result;

    }

    public static int obtenerVelocidadMinimaInternet(String adicional){

        int velocidad = -1;

        System.out.println("adicional_ba obtenerVelocidadMinimaInternet adicional "+ adicional);

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?", new String[]{"velocidadMinimaInternet", adicional}, null,
                null, null);

        System.out.println("adicional_ba obtenerVelocidadMinimaInternet resultado "+resultado);

        if(resultado != null){
            velocidad = Integer.parseInt(resultado.get(0).get(0));
        }

        return velocidad;

    }

    public static String validarHomologadoAdicionalBa(String adicional){

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?", new String[]{"HomologadoAdicionalBa", adicional}, null,
                null, null);

        if(resultado != null){
            adicional = resultado.get(0).get(0);
        }

        return adicional;
    }

    public static boolean televisionExistentePortafolioElite(String portafolio, String documento) {
        boolean televisionExistente = false;

        try {

            if(portafolio != null){
                JSONObject datosPortafolio = new JSONObject(portafolio);
                JSONArray productosInstalados = datosPortafolio.getJSONArray("ListaDatosIdentificador");

                if(productosInstalados != null){
                    for (int i = 0; i < productosInstalados.length(); i++){
                        JSONObject producto = productosInstalados.getJSONObject(i);
                        if(producto.getString("Producto").equalsIgnoreCase("TELEV") && producto.getString("clienteId").equalsIgnoreCase(documento)){
                            televisionExistente = true;
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return televisionExistente;
    }

    public static boolean validarHBOGOPortafolioElite(String portafolio, String documento, String tipoOferta){

        boolean hboGoExistente = false;

        System.out.println("Validar Crackle Sony HBO GO CrackleExistente portafolio "+portafolio);
        System.out.println("Validar Crackle Sony  HBO GO CrackleExistente documento "+documento);
        System.out.println("Validar Crackle Sony  HBO GO CrackleExistente tipoOferta "+tipoOferta);

        try {

            if(portafolio != null){
                JSONObject datosPortafolio = new JSONObject(portafolio);

                if (datosPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONArray")) {
                    JSONArray productosInstalados = datosPortafolio.getJSONArray("ListaDatosIdentificador");
                    System.out.println("Validar Crackle Sony  HBO GO producto array");
                    System.out.println("Validar Crackle Sony  HBO GO producto arrayinfo "+productosInstalados);
                    if (productosInstalados != null) {
                        for (int i = 0; i < productosInstalados.length(); i++) {
                            JSONObject producto = productosInstalados.getJSONObject(i);
                            System.out.println("Validar Crackle Sony  HBO GO producto " + producto);
                            System.out.println("Validar Crackle Sony  HBO GO  Adicionales " + producto.getString("Adicionales"));
                            if (producto.getString("Adicionales").contains("HBO GO") && (producto.getString("clienteId").equalsIgnoreCase(documento) || (tipoOferta.equalsIgnoreCase("DUO") || tipoOferta.equalsIgnoreCase("TRIO")))) {
                                hboGoExistente = true;
                            }
                        }
                    }
                }else if (datosPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONObject")) {
                    //JSONObject productosInstalados = datosPortafolio.JSONObject("ListaDatosIdentificador");
                    JSONObject producto = datosPortafolio.getJSONObject("ListaDatosIdentificador");
                    System.out.println("Validar Crackle Sony  HBO GO producto object");
                    System.out.println("Validar Crackle Sony  HBO GO producto object "+producto);
                    if (producto.getString("Adicionales").contains("HBO GO") && (producto.getString("clienteId").equalsIgnoreCase(documento) || (tipoOferta.equalsIgnoreCase("DUO") || tipoOferta.equalsIgnoreCase("TRIO")))) {
                        hboGoExistente = true;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Validar Crackle Sony  HBO GO  CrackleExistente hboGoExistente "+hboGoExistente);

        return hboGoExistente;

    }

    public static CrackleExistente validarCracklePortafolioElite(String portafolio, String documento, String tipoOferta){

        boolean CrackleExistente = false;
        String tipoProducto = "";
        CrackleExistente crackleExistente = new CrackleExistente();


        try {

            if(portafolio != null){
                JSONObject datosPortafolio = new JSONObject(portafolio);
                if (datosPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONArray")) {
                    JSONArray productosInstalados = datosPortafolio.getJSONArray("ListaDatosIdentificador");

                    System.out.println("Validar Crackle Sony productosInstalados" + productosInstalados);

                    if (productosInstalados != null) {
                        for (int i = 0; i < productosInstalados.length(); i++) {
                            JSONObject producto = productosInstalados.getJSONObject(i);
                            System.out.println("Validar Crackle Sony producto " + producto);
                            System.out.println("Validar Crackle Adicionales " + producto.getString("Adicionales"));
                            if (producto.getString("Adicionales").contains("CRACKLE SONY") && (producto.getString("clienteId").equalsIgnoreCase(documento))) {
                                CrackleExistente = true;
                                tipoProducto = producto.getString("Producto");
                            }
                        }
                    }
                }else if (datosPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONObject")) {
                    JSONObject producto = datosPortafolio.getJSONObject("ListaDatosIdentificador");
                    System.out.println("Validar Crackle Sony  HBO GO producto object");
                    System.out.println("Validar Crackle Sony  HBO GO producto object "+producto);
                    if (producto.getString("Adicionales").contains("CRACKLE SONY") && (producto.getString("clienteId").equalsIgnoreCase(documento))) {
                        CrackleExistente = true;
                        tipoProducto = producto.getString("Producto");
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Validar Crackle Sony CrackleExistente "+CrackleExistente);

        crackleExistente.setExistente(CrackleExistente);
        crackleExistente.setTipoProducto(tipoProducto);

        return crackleExistente;

    }

    public static String cambiarPlan(String plan){
        String cambio = plan;
        boolean control = false;

        ArrayList<ItemKeyValue> verificarPlanes = homologarPlan();
        if(verificarPlanes.size() > 0) {
            for (int i = 0; i < verificarPlanes.size(); i++) {
                if (plan.contains(verificarPlanes.get(i).getKey())) {
                    cambio = cambio.replace(verificarPlanes.get(i).getKey(), verificarPlanes.get(i).getValues());
                    control = true;
                }
            }

        }


            if(!control && plan.contains("_")){
                cambio = plan.replaceAll("_"," ");
            }


        return cambio;
    }

    public static ArrayList<ItemKeyValue> homologarPlan(){
        ArrayList<ItemKeyValue> planes = new ArrayList<ItemKeyValue>();

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave,lst_valor"}, "lst_nombre=?", new String[]{"homologarPlan"},
                null, null, null);

        System.out.println("respuesta " + respuesta);

        if (respuesta != null) {
            for (int i = 0; i < respuesta.size(); i++) {
                    planes.add(new ItemKeyValue(respuesta.get(i).get(0), respuesta.get(i).get(1)));

            }

        }

        return planes;
    }

    public static int homologarTipoTransacion (String tipoTransacion){
        int tipo = -1;
        if(tipoTransacion.equalsIgnoreCase("N")){
            tipo = 1;
        }else if(tipoTransacion.equalsIgnoreCase("C") || tipoTransacion.equalsIgnoreCase("E")){
            tipo = 0;
        }

        return tipo;
    }

    private ArrayList<ProductoCotizador> obtenerPagoParcial(CotizacionCliente cotizacionCliente) {

        ArrayList<ProductoCotizador> productos = cotizacionCliente.getProductoCotizador();

        if(cotizacionCliente.getContadorProductos() > 0){
            ArrayList<ArrayList<String>> resultQueryExterno =  resultQueryExternoPagoParcial(cotizacionCliente);
            if(resultQueryExterno.size() >0){
                ArrayList<ArrayList<String>> resultQueryInterno =  resultQueryInternoPagoParcial(resultQueryExterno);

            }
        }

        return productos;
    }

    public ArrayList<ArrayList<String>> resultQueryExternoPagoParcial(CotizacionCliente cotizacionCliente){
        ArrayList<ArrayList<String>> respuesta = null;

        if(cotizacionCliente.getContadorProductos() > 0){
            ArrayList<ListaCotizacion> listaCotizacion = listaCotizacion (cotizacionCliente.getProductoCotizador());

            if(listaCotizacion.size() > 0) {
                listaCotizacion =  listaCotizacionUnion(listaCotizacion);
                String datos = "";
                String uniones = "";
                String clausulas = "";

                for (int i = 0; i < listaCotizacion.size(); i++) {
                    System.out.println("pintar listaCotizacion tipoTransacion " + listaCotizacion.get(i).getTipoTransacion());
                    System.out.println("pintar listaCotizacion tipoProducto " + listaCotizacion.get(i).getTipoProducto());
                    System.out.println("pintar listaCotizacion Clausula " + listaCotizacion.get(i).getClausula());
                    System.out.println("pintar listaCotizacion Datos " + listaCotizacion.get(i).getDatos());
                    System.out.println("pintar listaCotizacion Union " + listaCotizacion.get(i).getUnion());
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

                System.out.println("pintar datos " + datos);
                System.out.println("pintar uniones " + uniones);
                System.out.println("pintar clausulas " + clausulas);
                String query = "select " + datos + " " + uniones + "\n where " + clausulas + "\n and p1.tipoPaquete = " + cotizacionCliente.getContadorProductos();

                System.out.println("pintar query " + query);

                respuesta = MainActivity.basedatos.consultar2(query);
            }
        }

        return respuesta;
    }

    public ArrayList<ListaCotizacion> listaCotizacion (ArrayList<ProductoCotizador> productos ){
        ArrayList<ListaCotizacion> listaCotizacion = new ArrayList<ListaCotizacion>();
        for (int i = 0; i < productos.size(); i++) {
            if(!productos.get(i).getTipoPeticion().equalsIgnoreCase("-") && !productos.get(i).getPlan().equalsIgnoreCase("-")){
                listaCotizacion.add(new ListaCotizacion(productos.get(i).getTipoPeticion(),productos.get(i).traducirProducto().toUpperCase()));
            }
        }
        return listaCotizacion;
    }

    public ArrayList<ListaCotizacion> listaCotizacionUnion (ArrayList<ListaCotizacion> listaCotizacion){
        for (int i = 0; i < listaCotizacion.size(); i++) {
            System.out.println("pintar listaCotizacion tipoTransacion "+listaCotizacion.get(i).getTipoTransacion());
            System.out.println("pintar listaCotizacion tipoProducto "+listaCotizacion.get(i).getTipoProducto());
            if(i == 0){
                listaCotizacion.get(i).setClausula("p1.producto='"+listaCotizacion.get(i).getTipoProducto()+"' AND p1.tipoTransacion= '"+UtilidadesTarificadorNew.homologarTipoTransacion(listaCotizacion.get(i).getTipoTransacion())+"'");
                listaCotizacion.get(i).setDatos("p1.id");
                listaCotizacion.get(i).setUnion("from pagoParcial p1");
            }else if(i == 1){
                listaCotizacion.get(i).setClausula("and p2.producto='"+listaCotizacion.get(i).getTipoProducto()+"' AND p2.tipoTransacion= '"+UtilidadesTarificadorNew.homologarTipoTransacion(listaCotizacion.get(i).getTipoTransacion())+"'");
                listaCotizacion.get(i).setDatos("p2.id");
                listaCotizacion.get(i).setUnion("INNER JOIN pagoParcial p2 ON p1.idPaquete=p2.idPaquete");
            }else if(i == 2){
                listaCotizacion.get(i).setClausula("and p3.producto='"+listaCotizacion.get(i).getTipoProducto()+"' AND p3.tipoTransacion= '"+UtilidadesTarificadorNew.homologarTipoTransacion(listaCotizacion.get(i).getTipoTransacion())+"'");
                listaCotizacion.get(i).setDatos("p3.id");
                listaCotizacion.get(i).setUnion("INNER JOIN pagoParcial p3 ON p1.idPaquete=p3.idPaquete");
            }
        }
        return listaCotizacion;

    }

    public ArrayList<ArrayList<String>> resultQueryInternoPagoParcial (ArrayList<ArrayList<String>> resultQueryExterno){

        String clausula2 = "id IN (?)";
        String[] valores = new String[]{resultQueryExterno.get(0).get(0)};

        String query = "select producto,valor,descuento from  pagoParcial where id in ("+resultQueryExterno.get(0).get(0)+")";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar2(query);

        System.out.println("pintar respuesta 2 " + respuesta);

        return respuesta;
    }
}
