package co.com.une.appmovilesune.change;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.CrackleExistente;
import co.com.une.appmovilesune.adapters.ItemKeyValue;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.adapters.ListaCotizacion;
import co.com.une.appmovilesune.model.AdicionalCotizador;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.PaqueteUNE;
import co.com.une.appmovilesune.model.PortafolioUNE;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Scooring;

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

    public static void imprimirAdicionalesCotizacion(ArrayList<AdicionalCotizador> adicionales){

        System.out.println("************imprimirAdicionalesCotizacion**********Inicio********");
        for (int i = 0; i < adicionales.size(); i++) {
            System.out.println("************Adicional**********Posicion("+i+")********");
            System.out.println("************tipo**********"+adicionales.get(i).getTipoAdicional()+"********");
            System.out.println("************tipoPeticion**********"+adicionales.get(i).getTiposolicitud()+"********");
            System.out.println("************nombrePlan**********"+adicionales.get(i).getNombreAdicional()+"********");
            System.out.println("************precio**********"+adicionales.get(i).getPrecioAdicional()+"********");
            System.out.println("************descuento**********"+adicionales.get(i).getDescuento()+"********");
            System.out.println("************duracionDescuento**********"+adicionales.get(i).getDuracionDescuento()+"********");
        }

        System.out.println("************imprimirAdicionalesCotizacion**********Fin********");
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

    public static boolean validarEstandarizacion(Cliente cliente, Context contex) {

        boolean validarEstandarizacion = true;

        System.out.println("clinte direccion cliente.getDireccionNormalizada() "+cliente.getDireccionNormalizada() );
        System.out.println("clinte direccion cliente.getDireccion() "+cliente.getDireccion() );

        if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
            System.out.println("estandarizarDireccion cliente.isControlNormalizada() externa else "+cliente.isControlNormalizada());
            System.out.println("estandarizarDireccion Utilidades.CoberturaRural(cliente) externa else "+Utilidades.CoberturaRural(cliente));
            System.out.println("estandarizarDireccion cliente.getDireccionNormalizada() "+cliente.getDireccionNormalizada() );
            System.out.println("estandarizarDireccion cliente.getDireccion() "+cliente.getDireccion() );
            if (!cliente.isControlNormalizada() && !Utilidades.CoberturaRural(cliente)) {

                validarEstandarizacion = false;
                Toast.makeText(contex, contex.getResources().getString(R.string.estandarizardireccion2), Toast.LENGTH_SHORT)
                        .show();
            }else if(!cliente.getDireccionNormalizada().equalsIgnoreCase(cliente.getDireccion())){
                validarEstandarizacion = false;
                Toast.makeText(contex, contex.getResources().getString(R.string.normalizardireccionnuevamente
                ), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        System.out.println("validarEstandarizacion "+validarEstandarizacion);

        return validarEstandarizacion;

    }

    public static boolean validarEstandarizacion(Cotizacion cotizacion, Cliente cliente, Context contex) {

        boolean validarEstandarizacion = true;
        boolean validarTelefonoServicio = true;

        System.out.println("clinte direccion cliente.getDireccionNormalizada() "+cliente.getDireccionNormalizada() );
        System.out.println("clinte direccion cliente.getDireccion() "+cliente.getDireccion() );
        //cliente.getDireccionNormalizada().equalsIgnoreCase(cliente.getDireccionNormalizada()
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
                            Toast.makeText(contex,contex.getResources().getString(R.string.estandarizardireccion2), Toast.LENGTH_SHORT).show();
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
                System.out.println("estandarizarDireccion cliente.getDireccionNormalizada() "+cliente.getDireccionNormalizada() );
                System.out.println("estandarizarDireccion cliente.getDireccion() "+cliente.getDireccion() );
                if (!cliente.isControlNormalizada() && !Utilidades.CoberturaRural(cliente)) {
                    validarEstandarizacion = false;
                    Toast.makeText(contex, contex.getResources().getString(R.string.estandarizardireccion2), Toast.LENGTH_SHORT)
                            .show();
                }else if(!cliente.getDireccionNormalizada().equalsIgnoreCase(cliente.getDireccion())){
                    validarEstandarizacion = false;
                    Toast.makeText(contex, contex.getResources().getString(R.string.normalizardireccionnuevamente
                    ), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
            System.out.println("estandarizarDireccion cliente.isControlNormalizada() externa else "+cliente.isControlNormalizada());
            System.out.println("estandarizarDireccion Utilidades.CoberturaRural(cliente) externa else "+Utilidades.CoberturaRural(cliente));
            System.out.println("estandarizarDireccion cliente.getDireccionNormalizada() "+cliente.getDireccionNormalizada() );
            System.out.println("estandarizarDireccion cliente.getDireccion() "+cliente.getDireccion() );
            if (!cliente.isControlNormalizada() && !Utilidades.CoberturaRural(cliente)) {

                validarEstandarizacion = false;
                Toast.makeText(contex, contex.getResources().getString(R.string.estandarizardireccion2), Toast.LENGTH_SHORT)
                        .show();
            }else if(!cliente.getDireccionNormalizada().equalsIgnoreCase(cliente.getDireccion())){
                validarEstandarizacion = false;
                Toast.makeText(contex, contex.getResources().getString(R.string.normalizardireccionnuevamente
                ), Toast.LENGTH_SHORT)
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

    public static boolean ventaConExistente(CotizacionCliente cotizacionCliente){
        boolean existentes = false;

        System.out.println("nuevos " + existentes);

        for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
            System.out.println("ventaConExistente cotizacion.getTipoCotizacion() " + cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNumerico());
            if(!Utilidades.validarVacioProducto(cotizacionCliente.getProductoCotizador().get(i).getPlan())){
                System.out.println("ventaConExistente cotizacion.getTipoCotizacion() " + cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNumerico());

                if (!cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNumerico().equalsIgnoreCase("1")) {
                    existentes = true;
                }
            }
        }

        System.out.println("nuevos " + existentes);

        return existentes;
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

    public static boolean televisionExistentePortafolioElite1(String portafolio, String documento) {
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

    public static boolean televisionExistentePortafolioElitePaquetes(PortafolioUNE portafolioUNE, String documento) {
        boolean televisionExistente = false;

            if(portafolioUNE != null){
                /*JSONObject datosPortafolio = new JSONObject(portafolio);
                JSONArray productosInstalados = datosPortafolio.getJSONArray("ListaDatosIdentificador");

                if(productosInstalados != null){
                    for (int i = 0; i < productosInstalados.length(); i++){
                        JSONObject producto = productosInstalados.getJSONObject(i);
                        if(producto.getString("Producto").equalsIgnoreCase("TELEV") && producto.getString("clienteId").equalsIgnoreCase(documento)){
                            televisionExistente = true;
                        }
                    }
                }*/

                if(portafolioUNE.getPaqueteUNEArrayList() != null && portafolioUNE.getPaqueteUNEArrayList().size() > 0) {
                    for (int i = 0; i <portafolioUNE.getPaqueteUNEArrayList().size(); i++) {
                        PaqueteUNE paqueteUNE = portafolioUNE.getPaqueteUNEArrayList().get(i);
                        paqueteUNE.imprimirProductosPaquete("Validar Crackle Sony");
                        for (int j = 0; j < paqueteUNE.getProductoPortafolioUNEArrayList().size(); j++) {
                            paqueteUNE.getProductoPortafolioUNEArrayList().get(j).imprimir();
                            System.out.println("Validar Crackle Sony producto " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getPlan());
                            System.out.println("Validar Crackle Sony tipoProducto " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getProducto());
                            System.out.println("Validar Crackle Adicionales " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getAdicionales());
                            System.out.println("Validar Crackle documento " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getClienteId());


                            if(paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getProducto().equalsIgnoreCase("TELEV") && paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getClienteId().equalsIgnoreCase(documento)){
                                televisionExistente = true;
                            }
                        }
                    }
                }

                /*if(!CrackleExistente && portafolioUNE.getPaqueteSeleccionado() != null){
                    for (int j = 0; j < portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size(); j++) {
                        System.out.println("Validar Crackle Sony producto " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getPlan());
                        System.out.println("Validar Crackle Sony tipoProducto " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getProducto());
                        System.out.println("Validar Crackle Adicionales " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getAdicionales());
                        if (portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getAdicionales().contains("CRACKLE SONY") && (paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getClienteId().equalsIgnoreCase(documento))) {
                            CrackleExistente = true;
                            tipoProducto = portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getProducto();
                        }
                    }
                }*/
            }

        return televisionExistente;
    }

    public static boolean validarHBOGOPortafolioElite1(String portafolio, String documento, String tipoOferta){

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

    public static boolean validarHBOGOPortafolioElitePaquete(PortafolioUNE portafolioUNE, String documento, String tipoOferta){

        boolean hboGoExistente = false;

        System.out.println("cargarAdicionalesBa Validar Crackle Sony HBO GO CrackleExistente portafolio "+portafolioUNE);
        System.out.println("cargarAdicionalesBa Validar Crackle Sony  HBO GO CrackleExistente documento "+documento);
        System.out.println("cargarAdicionalesBa Validar Crackle Sony  HBO GO CrackleExistente tipoOferta "+tipoOferta);

            if(portafolioUNE != null){
                 if(portafolioUNE.getPaqueteUNEArrayList() != null && portafolioUNE.getPaqueteUNEArrayList().size() > 0) {
                    for (int i = 0; i <portafolioUNE.getPaqueteUNEArrayList().size(); i++) {
                        PaqueteUNE paqueteUNE = portafolioUNE.getPaqueteUNEArrayList().get(i);
                        paqueteUNE.imprimirProductosPaquete("Validar HBO GO");
                        for (int j = 0; j < paqueteUNE.getProductoPortafolioUNEArrayList().size(); j++) {
                            System.out.println("cargarAdicionalesBa Validar HBO GO producto " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getPlan());
                            System.out.println("cargarAdicionalesBa Validar HBO GO Sony tipoProducto " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getProducto());
                            System.out.println("cargarAdicionalesBa Validar HBO GO Adicionales " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getAdicionales());
                            if (paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getAdicionales().contains("HBO GO") && (paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getClienteId().equalsIgnoreCase(documento))) {
                                hboGoExistente = true;
                            }
                        }
                    }
                }
            }


        System.out.println("cargarAdicionalesBa primera validacion HBO GO " + hboGoExistente);

        if(!hboGoExistente && portafolioUNE.getPaqueteSeleccionado() != null){
            for (int j = 0; j < portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size(); j++) {
                System.out.println("cargarAdicionalesBa Validar HBO GO producto " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getPlan());
                System.out.println("cargarAdicionalesBa Validar HBO GO tipoProducto " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getProducto());
                System.out.println("cargarAdicionalesBa Validar HBO GO Adicionales " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getAdicionales());
                if (portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getAdicionales().contains("HBO GO")/* && (portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getClienteId().equalsIgnoreCase(documento))*/) {
                    hboGoExistente = true;
                }
            }
        }

        System.out.println("cargarAdicionalesBa Validar Crackle Sony  HBO GO  CrackleExistente hboGoExistente "+hboGoExistente);

        return hboGoExistente;

    }

    public static CrackleExistente validarCracklePortafolioElite1(String portafolio, String documento, String tipoOferta){

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

    public static CrackleExistente validarCracklePortafolioElitePaquetes(PortafolioUNE portafolioUNE, String documento, String tipoOferta){

        boolean CrackleExistente = false;
        String tipoProducto = "";
        CrackleExistente crackleExistente = new CrackleExistente();

            if(portafolioUNE != null){
                if(portafolioUNE.getPaqueteUNEArrayList() != null && portafolioUNE.getPaqueteUNEArrayList().size() > 0) {
                    for (int i = 0; i <portafolioUNE.getPaqueteUNEArrayList().size(); i++) {
                        PaqueteUNE paqueteUNE = portafolioUNE.getPaqueteUNEArrayList().get(i);
                        for (int j = 0; j < paqueteUNE.getProductoPortafolioUNEArrayList().size(); j++) {
                            System.out.println("cargarAdicionalesBa Validar Crackle Sony producto " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getPlan());
                            System.out.println("cargarAdicionalesBa Validar Crackle Sony tipoProducto " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getProducto());
                            System.out.println("cargarAdicionalesBa Validar Crackle Adicionales " + paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getAdicionales());
                            if (paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getAdicionales().contains("CRACKLE SONY") && (paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getClienteId().equalsIgnoreCase(documento))) {
                                CrackleExistente = true;
                                tipoProducto = paqueteUNE.getProductoPortafolioUNEArrayList().get(j).getProducto();
                            }
                        }
                    }
                }

                System.out.println("cargarAdicionalesBa primera validacion CrackleExistente " + CrackleExistente);

                if(!CrackleExistente && portafolioUNE.getPaqueteSeleccionado() != null){
                    for (int j = 0; j < portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size(); j++) {
                        System.out.println("cargarAdicionalesBaValidar Crackle Sony producto " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getPlan());
                        System.out.println("cargarAdicionalesBaValidar Crackle Sony tipoProducto " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getProducto());
                        System.out.println("cargarAdicionalesBaValidar Crackle Adicionales " + portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getAdicionales());
                        if (portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getAdicionales().contains("CRACKLE SONY")/* && (portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getClienteId().equalsIgnoreCase(documento))*/) {
                            CrackleExistente = true;
                            tipoProducto = portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getProducto();
                        }
                    }
                }

                System.out.println("cargarAdicionalesBa segunda validacion CrackleExistente " + CrackleExistente);

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

    public static void imprimirAdicionales (String [][] adicionales, String ubicacion){

        System.out.println(ubicacion +adicionales);

        if(adicionales!= null && adicionales.length >0 ){
            for (int i = 0; i < adicionales.length ; i++) {
                System.out.println(ubicacion+" adicionales["+i+"][0] "+adicionales[i][0]);
                System.out.println(ubicacion+" adicionales["+i+"][1] "+adicionales[i][1]);
            }
        }
    }

    public static ProductoCotizador traducirProducto(ArrayList<ProductoCotizador> productos, int tipo ){
        ProductoCotizador producto = null;

        for (int i = 0; i < productos.size(); i++) {
            if(productos.get(i).getTipo() == tipo){
                producto = productos.get(i);
            }
        }

        return producto;

    }

    public static boolean validarCotizacionClienteNuevo(Cliente cliente,CotizacionCliente cotizacionCliente){

        boolean cotizacionValida = false;

        if (!Utilidades.excluir("excluirppca", cliente.getCiudad())) {
            if(cotizacionCliente.isClienteNuevo() && cotizacionCliente.getCodigoClienteNuevo().equalsIgnoreCase("00")) {
                if(!UtilidadesTarificadorNew.ventaConExistente(cotizacionCliente)){
                    cotizacionValida = true;
                }
            } else if (cotizacionCliente.isClienteNuevo() && cotizacionCliente.getCodigoClienteNuevo().equalsIgnoreCase("00")) {
                cotizacionValida = true;
            } else {
                cotizacionValida = true;
            }
        } else {
            cotizacionValida = true;
        }


        return cotizacionValida;

    }

    public static boolean validarCarteraUNE(Cliente cliente, Scooring scooring) {

        boolean valid = true;

        if (scooring != null && scooring.isValidarCartera()) {

            if (scooring.getIdCliente() != null && scooring.getIdCliente().equalsIgnoreCase(cliente.getCedula())) {

                if (Utilidades.excluir("habilitarBloqueoScooring", cliente.getCiudad())) {
                    if (scooring.getAccion().equalsIgnoreCase("No Vender")) {

                        valid = false;

                    }
                }

            } else {
                /*Toast.makeText(this, "Documento del cliente vs Documento de Consulta en Estado Cuenta no coincide",
                        Toast.LENGTH_SHORT).show();*/
                //resultCotizador("estado cuenta");
                valid = false;
            }

        }

        return valid;
    }

    public static boolean validarScooring(Cliente cliente,CotizacionCliente cotizacionCliente,Scooring scooring,Context context) {
        boolean valid = true;
        String codigoPA = "00";
        if (scooring != null && !scooring.isValidarCartera()) {
            if (cliente.getScooringune() != null) {
                if (cliente.getScooringune().getDocumentoScooring().equalsIgnoreCase(cliente.getCedula())) {
                    if (cliente.getScooringune().isValidarScooring()) {
                        if (!cliente.getScooringune().isPasaScooring()) {
                            if (productosNuevos(cotizacionCliente)) {
                                if (codigoPA.equalsIgnoreCase("00")) {
                                    if (Utilidades.excluirEstadosPagoAnticipado()
                                            .contains(cliente.getScooringune().getRazonScooring())) {
                                        valid = true;
                                        cliente.setPagoAnticipado("SI");
                                    } else {
                                        valid = false;
                                        cliente.setPagoAnticipado("NO");
                                    }
                                } else {
                                    valid = false;

                                }

                            } else {
                                cliente.setDomiciliacion("NO");
                                cliente.getScooringune().setNoAplicaScooring(true);
                            }
                        } else {
                            if (!productosNuevos(cotizacionCliente)) {
                                cliente.setDomiciliacion("NO");
                                cliente.getScooringune().setNoAplicaScooring(true);
                            }
                        }
                    } else {
                        if (cliente.getScooringune().isPendienteRespuesta()) {
                            if (!productosNuevos(cotizacionCliente)) {
                                cliente.setDomiciliacion("NO");
                                cliente.getScooringune().setNoAplicaScooring(true);
                            }
                        }
                    }
                } else {
                    Utilidades.MensajesToast(
                            context.getResources().getString(R.string.documentoinvalidocuentasune), context);
                    //resultCotizador("estado cuenta");*/
                    valid = false;
                }
            }
        }

        return valid;
    }

    public static boolean validarCotizacionvsPortafolio(Cliente cliente,CotizacionCliente cotizacionCliente,Context context) {
        boolean valid = true;

        int contadorIguales = 0;

        if(cotizacionCliente.getTipoOferta().equalsIgnoreCase("Individual")){
            if(!cliente.getPortafolioUNE().getPaqueteSeleccionado().getIdPaquete().equalsIgnoreCase("0")){
                return false;
            }
        }

        if(cotizacionCliente.getProductoCotizador().size() >= cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size()){
            for (int i = 0; i < cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size(); i++) {
                for (int j = 0; j < cotizacionCliente.getProductoCotizador().size(); j++) {

                    if((cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(i).tipoProducto() == cotizacionCliente.getProductoCotizador().get(j).getTipo()) && cotizacionCliente.getProductoCotizador().get(j).getTipoPeticion().equalsIgnoreCase("C")){
                        contadorIguales++;
                    }
                }

            }

            System.out.println("validarCotizacionvsPortafolio contadorIguales "+contadorIguales);

            if(cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size() == contadorIguales){
                valid = true;
            }else{
                valid = false;
            }
        }else{
            valid = false;
        }

        return valid;
    }

    public static boolean productosNuevos(CotizacionCliente cotizacionCliente) {

        boolean nuevos = false;

        System.out.println("----Carrusel--- nuevos " + nuevos);
        System.out.println("----Carrusel--- otizacionCliente.getProductoCotizador().size(); " + cotizacionCliente.getProductoCotizador().size());

        for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
            if(!Utilidades.validarVacioProducto(cotizacionCliente.getProductoCotizador().get(i).getPlan())){
                System.out.println("----Carrusel--- cotizacion.getTipoCotizacion " + cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNumerico());
                if (cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNumerico().equalsIgnoreCase("1")) {
                    nuevos = true;
                }
            }

        }

        System.out.println("----Carrusel--- nuevos respuesta " + nuevos);

        return nuevos;
    }

    public static String traducirMeses(String dato){
        String meses = dato + " Meses";
        if(dato.equals("1")){
            meses = dato +" Mes";
        }

        return meses;
    }

    public static String traducirPorcentaje(String dato){
        return dato +"%";
    }

    public static String calcularDescuento(double porcentaje, double precio){
        double total = (precio * porcentaje)/100;
        return String.valueOf(total);
    }

    public static boolean validarCarrusel(CotizacionCliente cotizacionCliente,Cliente cliente, Context context) {
        boolean validar = true;

        ArrayList<String> productosLog = new ArrayList<String>();

        if (Utilidades.validarPermiso("carrusel") || cliente.isControlCerca() || Utilidades.CoberturaRural(cliente)) {
            return true;
        }

        System.out.println("----Carrusel--- carrusel inicio validacion");
        System.out.println("----Carrusel--- cliente.isControlCarrusel() "+cliente.isControlCarrusel());

        if (Utilidades.excluir("MunicipiosCarrusel", cliente.getCiudad())) {
            if (cliente.isControlCarrusel()) {
                System.out.println("----Carrusel--- if ");
                System.out.println("----Carrusel--- cliente.getCodigoCarrusel() "+cliente.getCodigoCarrusel());
                if (cliente.getCodigoCarrusel().equals("02") || cliente.getCodigoCarrusel().equals("-1")) {
                } else if (cliente.getDireccion().equalsIgnoreCase(cliente.getDireccionCarrusel())) {
                    if (cliente.getCodigoCarrusel().equals("00") && cliente.getArraycarrusel() != null) {
                        if (cliente.getArraycarrusel().size() > 0) {
                            for (int i = 0; i < cliente.getArraycarrusel().size(); i++) {
                                if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TO")) {
                                    ProductoCotizador productoCotizador = UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(),ProductoCotizador.getTELEFONIA());
                                    if(!Utilidades.validarVacioProducto(productoCotizador.getPlan())){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), context)) {
                                            productosLog.add("TO");
                                        }
                                    }
                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TV")) {
                                    ProductoCotizador productoCotizador = UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(),ProductoCotizador.getTELEVISION());
                                    if(!Utilidades.validarVacioProducto(productoCotizador.getPlan())){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), context)) {
                                            productosLog.add("TV");
                                        }
                                    }

                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("BA")) {
                                    ProductoCotizador productoCotizador = UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(),ProductoCotizador.getINTERNET());
                                    if(!Utilidades.validarVacioProducto(productoCotizador.getPlan())){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), context)) {
                                            productosLog.add("TV");
                                        }
                                    }
                                }
                            }

                            if(productosLog.size() > 0){
                                validar = false;
                                for (String prod: productosLog) {
                                    //Log.i(TAG,"LOG "+prod);
                                }
                            }



                        }
                    } else if (cliente.getCodigoCarrusel().equals("01")) {
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.mensajeRetiros), Toast.LENGTH_SHORT).show();
                    //resultCotizador("carrusel");
                    return  false;
                }
            } else {
                System.out.println("----Carrusel--- else ");
               if (productosNuevos(cotizacionCliente)) {
                   System.out.println("----Carrusel--- cliente.getCodigoCarrusel() "+cliente.getCodigoCarrusel());
                    if (cliente.getCodigoCarrusel() == null) {
                        Toast.makeText(context, context.getResources().getString(R.string.mensajeRetiros), Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                }
            }
        }

        return validar;
    }

    public static CotizacionCliente tipoDeFacturacion_Copia(CotizacionCliente cotizacionCliente, Cliente cliente ) {

        //ArrayList<ProductoCotizador> productos = cotizacionCliente.getProductoCotizador();
        System.out.println("cliente.servicio "+cliente.getServicioElite() );
        System.out.println("cliente smart promo "+cliente.isClienteNuevoSmartPromo());

        if(!Utilidades.validarNullyVacio(cliente.getServicioElite())) {
            try {
                JSONObject facturacion_SmartPromo = new JSONObject(cliente.getServicioElite());

                if(facturacion_SmartPromo.has("resultTipoFacturacionySmartPromoxCiudad") && facturacion_SmartPromo.getJSONObject("resultTipoFacturacionySmartPromoxCiudad").getString("codigoRespuesta").equalsIgnoreCase("00")){
                    if (!UtilidadesTarificadorNew.ventaConExistente(cotizacionCliente)) {
                        System.out.println("venta nueva ");
                        System.out.println("cliente smart promo "+cliente.isClienteNuevoSmartPromo());
                        JSONObject informacion =  facturacion_SmartPromo.getJSONObject("resultTipoFacturacionySmartPromoxCiudad").getJSONObject("datos");
                        for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
                            cotizacionCliente.getProductoCotizador().get(i).setTipoFacturacion(informacion.getString("tipoFacturacion"));
                            cotizacionCliente.getProductoCotizador().get(i).setSmartPromo("NO");
                            cotizacionCliente.getProductoCotizador().get(i).setActivacion("Activacion");
                            cotizacionCliente.getProductoCotizador().get(i).setInicioFacturacion("I");
                            if(informacion.getString("smartpromo").equalsIgnoreCase("ON") && cliente.isClienteNuevoSmartPromo()){
                                cotizacionCliente.getProductoCotizador().get(i).setSmartPromo("SI");
                                cotizacionCliente.getProductoCotizador().get(i).setInicioFacturacion("S");
                            }
                        }

                        cotizacionCliente.setLecturaxProducto("NO");
                        cotizacionCliente.setOfertaTipoFacturacion(informacion.getString("tipoFacturacion"));
                        cotizacionCliente.setSmartPromo("NO");
                        cotizacionCliente.setOfertaInicioFacturacion("I");
                        if(informacion.getString("smartpromo").equalsIgnoreCase("ON") && cliente.isClienteNuevoSmartPromo()){
                            cotizacionCliente.setSmartPromo("SI");
                            cotizacionCliente.setOfertaInicioFacturacion("S");
                        }
                    } else {
                        System.out.println("venta existente ");
                    }
                }


            }catch(JSONException e){
                System.out.println("error "+e.getMessage());
            }
        }


        return cotizacionCliente;
    }


    public static CotizacionCliente tipoDeFacturacion(CotizacionCliente cotizacionCliente, Cliente cliente ) {

        //ArrayList<ProductoCotizador> productos = cotizacionCliente.getProductoCotizador();

        if(cliente.getPortafolioUNE()!= null/* && cliente.getPortafolioUNE().getPaqueteSeleccionado() != null*/) {
                    if (!UtilidadesTarificadorNew.ventaConExistente(cotizacionCliente)) {

                        if(cliente.getPortafolioUNE().getTipoFacturacionCiudad() != null) {
                            for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
                                cotizacionCliente.getProductoCotizador().get(i).setTipoFacturacion(cliente.getPortafolioUNE().getTipoFacturacionCiudad());
                                cotizacionCliente.getProductoCotizador().get(i).setSmartPromo("NO");
                                cotizacionCliente.getProductoCotizador().get(i).setActivacion("Activacion");
                                cotizacionCliente.getProductoCotizador().get(i).setInicioFacturacion("I");
                                cotizacionCliente.getProductoCotizador().get(i).setTipoTransaccion("Nuevo");
                                if (cliente.getPortafolioUNE().getSmartPromoCiudad().equalsIgnoreCase("ON") && cliente.isClienteNuevoSmartPromo()) {
                                    cotizacionCliente.getProductoCotizador().get(i).setSmartPromo("SI");
                                    cotizacionCliente.getProductoCotizador().get(i).setInicioFacturacion("S");
                                }
                            }

                            cotizacionCliente.setLecturaxProducto("NO");
                            cotizacionCliente.setOfertaTipoFacturacion(cliente.getPortafolioUNE().getTipoFacturacionCiudad());
                            cotizacionCliente.setSmartPromo("NO");
                            cotizacionCliente.setOfertaInicioFacturacion("I");
                            if (cliente.getPortafolioUNE().getSmartPromoCiudad().equalsIgnoreCase("ON") && cliente.isClienteNuevoSmartPromo()) {
                                cotizacionCliente.setSmartPromo("SI");
                                cotizacionCliente.setOfertaInicioFacturacion("S");
                            }
                        }
                    } else {
                        //System.out.println("venta existente cliente.getPortafolioUNE() "+cliente.getPortafolioUNE());
                        //System.out.println("venta existente cliente.getPortafolioUNE().getPaqueteSeleccionado() "+cliente.getPortafolioUNE().getPaqueteSeleccionado());

                        if(cliente.getPortafolioUNE() != null && cliente.getPortafolioUNE().getPaqueteSeleccionado() != null && cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList() != null) {
                            for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
                                //cotizacionCliente.getProductoCotizador().get(i).imprimir();
                                if(cotizacionCliente.getProductoCotizador().get(i).getTipoPeticion().equalsIgnoreCase("N")){
                                    cotizacionCliente.getProductoCotizador().get(i).setTipoFacturacion(facturacionProductosNuevos(cliente.getPortafolioUNE()));
                                    cotizacionCliente.getProductoCotizador().get(i).setActivacion("Activacion");
                                    cotizacionCliente.getProductoCotizador().get(i).setTipoTransaccion(cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNombreCompeto());
                                    cotizacionCliente.getProductoCotizador().get(i).setInicioFacturacion(InicioFacturacionCrossVentaNueva(cliente));
                                    cotizacionCliente.getProductoCotizador().get(i).setSmartPromo("NO");
                                    if(InicioFacturacionCrossVentaNueva(cliente).equalsIgnoreCase("S")){
                                        cotizacionCliente.getProductoCotizador().get(i).setSmartPromo("SI");
                                    }
                                }else{
                                    for (int j = 0; j < cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size(); j++) {
                                        //if(cotizacionCliente.getProductoCotizador())

                                        cotizacionCliente.getProductoCotizador().get(i).imprimir("CotizacionCliente tipoDeFacturacion");
                                        cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).imprimir();

                                        if(cotizacionCliente.getProductoCotizador().get(i).getTipo() == cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).tipoProducto()){
                                            cotizacionCliente.getProductoCotizador().get(i).setTipoFacturacion(cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getTipoFactura());
                                            cotizacionCliente.getProductoCotizador().get(i).setSmartPromo(cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getSmartPromo());
                                            cotizacionCliente.getProductoCotizador().get(i).setPlanAnterior(cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(j).getPlanId());
                                            if(cotizacionCliente.getProductoCotizador().get(i).getTipoFacturacion().equalsIgnoreCase("VENCIDA")){
                                                cotizacionCliente.getProductoCotizador().get(i).setActivacion("Activacion");
                                                cotizacionCliente.getProductoCotizador().get(i).setTipoTransaccion(cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNombreCompeto());
                                                cotizacionCliente.getProductoCotizador().get(i).setInicioFacturacion("I");
                                            }
                                        }

                                    }

                                }
                            }
                            //facturacionProductosNuevos(cliente.getPortafolioUNE());
                            cotizacionCliente.setLecturaxProducto("SI");
                        }
                    }
        }


        return cotizacionCliente;
    }

    public static String InicioFacturacionCrossVentaNueva(Cliente cliente){
        boolean smartPromo = false;
        String tipoFacturacion = "I";

        for (int i = 0; i < cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size(); i++) {
            if (cliente.getPortafolioUNE().getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(i).getSmartPromo().equalsIgnoreCase("SI")){
               smartPromo = true;
            }
        }

        if(smartPromo){

            if(cliente.getPortafolioUNE().getSmartPromoCiudad().equalsIgnoreCase("ON")){
                tipoFacturacion = "S";
            }
        }

        return tipoFacturacion;
    }


    public static String facturacionProductosNuevos(PortafolioUNE portafolioUNE){

        ArrayList<String> facturacion = new ArrayList<String>();

        String nuevoTipoFacturacion = null;

        for (int i = 0; i < portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().size(); i++) {
            facturacion.add(portafolioUNE.getPaqueteSeleccionado().getProductoPortafolioUNEArrayList().get(i).getTipoFactura());
        }

        System.out.println("facturacion 0 "+facturacion);

        HashSet<String> hashSet = new HashSet<String>(facturacion);
        facturacion.clear();
        facturacion.addAll(hashSet);

        if(facturacion.size() == 1){
            nuevoTipoFacturacion = facturacion.get(0);
        }else if(facturacion.size() > 1){
            nuevoTipoFacturacion = "ANTICIPADA";
        }

        System.out.println("facturacion 1 "+facturacion);

        return nuevoTipoFacturacion;
    }

    public static String homologarPlanDecosIVR(String deco){
        return "Decodificador " + deco + " (Adicional)";
    }



}
