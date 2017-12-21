package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import co.com.une.appmovilesune.MainActivity;

public class Blindaje implements Serializable {

    // Declaracion de atributos de blindaje
    public String ma_id;
    public String ciudad;
    public String direccion;
    public String estrato;
    public String barrio;
    public String comuna;
    public String urbanizacion;
    public String paquete;
    public String plan_de_to;
    public String to_coniva;
    public String plancerrado_coniva;
    public String plantv;
    public String tv_masiptvconiva;
    public String vel_fact;
    public String ba_coniva;
    public String gotica_coniva;
    public String totalconiva_subsidios;
    public String Regalo_Inicial;
    public String Regalo_Inicial_2;
    public String Oferta_1;
    public String Regalo_Oferta_1;
    public String Valor_Oferta_1;
    public String Factura_Oferta_1;
    public String Oferta_2;
    public String Regalo_Oferta_2;
    public String Valor_Oferta_2;
    public String Factura_Oferta_2;

    public boolean agenda;
    public String fechaAgenda;
    public String franjaAgenda;
    public String motivoAgendaOferta;
    public String motivoAgendaRegalo;

    public String migracionred_regaloinicial;
    public String migracionred_oferta1;
    public String migraacionred_oferta2;
    public String tipo_instalacion_oferta1;
    public String tipo_instalacion_oferta2;
    public String cambioequipo_ba;
    public String incluyente_excluyente_regaloinicial1ofer1amg;
    public String incluyente_excluyente_regaloinicial2afer1amg;
    public String incluyente_excluyente_regaloinicial1of2;

    public String nombreCliente;
    public String tipoCedulaCliente;
    public String cedulaCliente;
    public String observaciones;

    public boolean aceptaRegalo;
    public boolean aceptaOferta1;
    public boolean aceptaOferta2;
    public boolean tieneOferta;
    public boolean tieneRegalo;
    public String idIVR;

    public String[] formulario;
    public String[] resumen;

    public ArrayList<String[]> cliente;

    public String unemasCodigo, unemasMensaje;

    // Si se genera una nueva variable en blindaje, se agrega en el constructor
    // y se iguala
    public Blindaje(String ma_id, String ciudad, String direccion, String estrato, String barrio, String comuna,
                    String urbanizacion, String paquete, String plan_de_to, String to_coniva, String plancerrado_coniva,
                    String plantv, String tv_masiptvconiva, String vel_fact, String ba_coniva, String gotica_coniva,
                    String totalconiva_subsidios, String Regalo_Inicial, String Regalo_Inicial_2, String Oferta_1,
                    String Regalo_Oferta_1, String Valor_Oferta_1, String Factura_Oferta_1, String Oferta_2,
                    String Regalo_Oferta_2, String Valor_Oferta_2, String Factura_Oferta_2, boolean agenda,
                    String migracionred_regaloinicial, String migracionred_oferta1, String migraacionred_oferta2,
                    String tipo_instalacion_oferta1, String tipo_instalacion_oferta2, String cambioequipo_ba,
                    String incluyente_excluyente_regaloinicial1ofer1amg, String incluyente_excluyente_regaloinicial2afer1amg,
                    String incluyente_excluyente_regaloinicial1of2, ArrayList<String[]> cliente, boolean tieneOferta,
                    boolean tieneRegalo) {

        this.ma_id = ma_id;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.estrato = estrato;
        this.barrio = barrio;
        this.comuna = comuna;
        this.urbanizacion = urbanizacion;
        this.paquete = paquete;
        this.plan_de_to = plan_de_to;
        this.to_coniva = to_coniva;
        this.plancerrado_coniva = plancerrado_coniva;
        this.plantv = plantv;
        this.tv_masiptvconiva = tv_masiptvconiva;
        this.vel_fact = vel_fact;
        this.ba_coniva = ba_coniva;
        this.gotica_coniva = gotica_coniva;
        this.totalconiva_subsidios = totalconiva_subsidios;
        this.Regalo_Inicial = Regalo_Inicial;
        this.Regalo_Inicial_2 = Regalo_Inicial_2;
        this.Oferta_1 = Oferta_1;
        this.Regalo_Oferta_1 = Regalo_Oferta_1;
        this.Valor_Oferta_1 = Valor_Oferta_1;
        this.Factura_Oferta_1 = Factura_Oferta_1;
        this.Oferta_2 = Oferta_2;
        this.Regalo_Oferta_2 = Regalo_Oferta_2;
        this.Valor_Oferta_2 = Valor_Oferta_2;
        this.Factura_Oferta_2 = Factura_Oferta_2;

        this.agenda = agenda;

        this.migracionred_regaloinicial = migracionred_regaloinicial;
        this.migracionred_oferta1 = migracionred_oferta1;
        this.migraacionred_oferta2 = migraacionred_oferta2;
        this.tipo_instalacion_oferta1 = tipo_instalacion_oferta1;
        this.tipo_instalacion_oferta2 = tipo_instalacion_oferta2;
        this.incluyente_excluyente_regaloinicial1ofer1amg = incluyente_excluyente_regaloinicial1ofer1amg;
        this.incluyente_excluyente_regaloinicial2afer1amg = incluyente_excluyente_regaloinicial2afer1amg;
        this.incluyente_excluyente_regaloinicial1of2 = incluyente_excluyente_regaloinicial1of2;
        this.cambioequipo_ba = cambioequipo_ba;
        this.tieneOferta = tieneOferta;
        this.tieneRegalo = tieneRegalo;

        this.cliente = cliente;

    }

    // Se genera el json de blindasje para agregar en el consolidado, se validan
    // si hay q validarlo
    public JSONObject consolidarBlindaje() {

        JSONObject blindaje = new JSONObject();
        try {
            blindaje.put("ma_id", ma_id);
            blindaje.put("ciudad", ciudad);
            blindaje.put("direccion", direccion);
            blindaje.put("estrato", estrato);
            blindaje.put("barrio", barrio);
            blindaje.put("comuna", comuna);
            blindaje.put("urbanizacion", urbanizacion);
            blindaje.put("paquete", paquete);
            blindaje.put("plan_de_to", plan_de_to);
            blindaje.put("to_coniva", to_coniva);
            blindaje.put("plancerrado_coniva", plancerrado_coniva);
            blindaje.put("plantv", plantv);
            blindaje.put("tv_masiptvconiva", tv_masiptvconiva);
            blindaje.put("vel_fact", vel_fact);
            blindaje.put("ba_coniva", ba_coniva);
            blindaje.put("gotica_coniva", gotica_coniva);
            blindaje.put("totalconiva_subsidios", totalconiva_subsidios);
            blindaje.put("origen", "Venta Movil");
            System.out.println("Fecha Agenda =>" + franjaAgenda);
            if (fechaAgenda != null) {
                blindaje.put("fechaAgenda", fechaAgenda);
                blindaje.put("motivoAgendaOferta", motivoAgendaOferta);
                blindaje.put("motivoAgendaRegalo", motivoAgendaRegalo);
            } else {
                blindaje.put("fechaAgenda", "");
                blindaje.put("motivoAgendaOferta", "");
                blindaje.put("motivoAgendaRegalo", "");
            }

            System.out.println("Franja Agenda =>" + franjaAgenda);
            if (franjaAgenda != null) {
                blindaje.put("jornadaAgenda", franjaAgenda);
            } else {
                blindaje.put("jornadaAgenda", "");
            }

            blindaje.put("codigoAsesor", MainActivity.config.getCodigo_asesor());
            blindaje.put("DocumentoAsesor", MainActivity.config.getDocumento_asesor());
            blindaje.put("nombreAsesor", MainActivity.config.getNombre_asesor());
            blindaje.put("canal", MainActivity.config.getCanal());
            blindaje.put("observaciones", observaciones);

            if (nombreCliente.equals("Otro")) {
                blindaje.put("TipoCliente", 1);
            } else {
                blindaje.put("TipoCliente", 0);
            }

            if (aceptaRegalo) {
                blindaje.put("Regalo_Inicial", Regalo_Inicial + " " + Regalo_Inicial_2);
                blindaje.put("aceptaRegalo", aceptaRegalo);
            } else {
                blindaje.put("Regalo_Inicial", "");
                blindaje.put("aceptaRegalo", "0");
            }

            if (aceptaOferta1) {
                blindaje.put("Oferta", Oferta_1);
                blindaje.put("Regalo_Oferta", Regalo_Oferta_1);
                blindaje.put("Valor_Oferta", Valor_Oferta_1);
                blindaje.put("Factura_Oferta", Factura_Oferta_1);
                blindaje.put("aceptaOferta", aceptaOferta1);
                blindaje.put("ofertaNumero", 1);
            } else if (aceptaOferta2) {
                blindaje.put("Oferta", Oferta_2);
                blindaje.put("Regalo_Oferta", Regalo_Oferta_2);
                blindaje.put("Valor_Oferta", Valor_Oferta_2);
                blindaje.put("Factura_Oferta", Factura_Oferta_2);
                blindaje.put("aceptaOferta", aceptaOferta2);
                blindaje.put("ofertaNumero", 2);
            } else {
                blindaje.put("Oferta", "");
                blindaje.put("Regalo_Oferta", "0");
                blindaje.put("Valor_Oferta", "0");
                blindaje.put("Factura_Oferta", "0");
                blindaje.put("aceptaOferta", "0");
                blindaje.put("ofertaNumero", "0");
            }

            JSONObject um = new JSONObject();
            um.put("codigo", unemasCodigo);
            um.put("mensaje", unemasMensaje);

            blindaje.put("unemas", um);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return blindaje;

    }

}
