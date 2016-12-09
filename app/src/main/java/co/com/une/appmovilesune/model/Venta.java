package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;

import android.content.ContentValues;
import android.content.pm.FeatureInfo;
import android.util.Log;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Calendario;

public class Venta implements Serializable {

    private boolean validarAgenda = false;
    private boolean validarTelevisores = false;
    private boolean validarDecosPago = true;
    private boolean validarOfertaScooring = true;
    private int agendableSiebel = 2;

    private String[] documentacion = new String[7];

    private String[] telefonia = new String[19];
    private String[] television = new String[19];
    private String[] internet = new String[15];
    private String[] internet3g = new String[12];
    private String[] internet4g = new String[5];
    private String[] gota = new String[4];

    private boolean controlGota = false;

    ArrayList<ListaDefault> listDocumentacion = new ArrayList<ListaDefault>();
    ArrayList<ItemDecodificador> itemDecodificadors;

    String listAdicionalesTo;
    String listAdicionalesTV;
    String listAdicionalesBa;

    private String id, id_asesoria;
    private String total;
    private String totalPagoAntCargoFijo, totalPagoConexion, totalPagoParcialConexion, descuentoConexion;
    private String totalNuevos;

    private String scooring;

    private String fecha, hora;

    private String otrasPromociones;

    private String empaquetamiento;
    private String empaquetada;
    private String observaciones;
    private String horarioAtencion;
    private String estrato;
    private String tipoServicio;
    private String municipio;
    private String departamento;
    private String municipioSSC;
    private String dependencia;
    private String TipoConstruccion;
    public String medioIngreso;
    private String oferta;
    private String agendaSiebel = null;
    private String idOferta;
    private String mensajeScore = "";
    private String microZona = "";
    private String cobroDomingo = "";

    private ArrayList<ItemDecodificador> decos;

    public String[] getTelefonia() {
        return telefonia;
    }

    public void setTelefonia(String plan, String precio, String linea, String pagoLinea, String migracion,
                             String tipoMigracion, String promocion, String duracion, String precioDescuento, String planFacturacion,
                             String tecnologia, String tipoCotizacion, String identificador, String adicionales,
                             String precioAdicionalesIndividuales, String precioAdicionales, String toTecnologiacr, String PagoAntCargoFijo, String PagoParcialConexion) {
        this.telefonia[0] = plan;
        this.telefonia[1] = precio;
        this.telefonia[2] = linea;
        this.telefonia[3] = pagoLinea;
        this.telefonia[4] = migracion;
        this.telefonia[5] = tipoMigracion;
        this.telefonia[6] = promocion;
        this.telefonia[7] = duracion;
        this.telefonia[8] = precioDescuento;
        this.telefonia[9] = planFacturacion;
        this.telefonia[10] = tecnologia;
        this.telefonia[11] = tipoCotizacion;
        this.telefonia[12] = identificador;
        this.telefonia[13] = adicionales;
        this.telefonia[14] = precioAdicionalesIndividuales;
        this.telefonia[15] = precioAdicionales;
        this.telefonia[16] = toTecnologiacr;
        this.telefonia[17] = PagoAntCargoFijo;
        this.telefonia[18] = PagoParcialConexion;

        System.out.println("this.telefonia[16] " + this.telefonia[16]);
    }

    public String[] getTelevision() {
        return television;
    }

    public void setTelevision(String plan, String precio, String extensiones, String promocion, String duracion,
                              String precioDescuento, String Adicionales, String precioAdicionalesIndividuales, String precioAdicionales,
                              String migracion, String tipoMigracion, String planFacturacion, String decos, String tecnologia,
                              String tipoCotizacion, String identificador, String tvTecnologiacr, String PagoAntCargoFijo, String PagoParcialConexion) {
        this.television[0] = plan;
        this.television[1] = precio;
        this.television[2] = extensiones;
        this.television[3] = promocion;
        this.television[4] = duracion;
        this.television[5] = precioDescuento;
        this.television[6] = Adicionales;
        this.television[7] = precioAdicionalesIndividuales;
        this.television[8] = precioAdicionales;
        this.television[9] = migracion;
        this.television[10] = tipoMigracion;
        this.television[11] = planFacturacion;
        this.television[12] = decos;
        this.television[13] = tecnologia;
        this.television[14] = tipoCotizacion;
        this.television[15] = identificador;
        this.television[16] = tvTecnologiacr;
        this.television[17] = PagoAntCargoFijo;
        this.television[18] = PagoParcialConexion;

        System.out.println("this.television[16] " + this.television[16]);
    }

    public String[] getInternet() {
        return internet;
    }

    public void setInternet(String plan, String precio, String wifi, String migracion, String tipoMigracion,
                            String promocion, String duracion, String precioDescuento, String planFacturacion, String tipoCotizacion,
                            String identificador, String baTecnologiacr, String ipDinamica, String PagoAntCargoFijo, String PagoParcialConexion) {
        this.internet[0] = plan;
        this.internet[1] = precio;
        this.internet[2] = wifi;
        this.internet[3] = migracion;
        this.internet[4] = tipoMigracion;
        this.internet[5] = promocion;
        this.internet[6] = duracion;
        this.internet[7] = precioDescuento;
        this.internet[8] = planFacturacion;
        this.internet[9] = tipoCotizacion;
        this.internet[10] = identificador;
        this.internet[11] = baTecnologiacr;
        this.internet[12] = ipDinamica;
        this.internet[13] = PagoAntCargoFijo;
        this.internet[14] = PagoParcialConexion;

        System.out.println("this.internet[11] " + this.internet[11]);
    }

    public String[] getGota() {
        return gota;
    }

    public void setGota(String plan, String precio, String velocidadInicial, String velocidadFinal) {
        this.gota[0] = plan;
        this.gota[1] = precio;
        this.gota[2] = velocidadInicial;
        this.gota[3] = velocidadFinal;

    }

    public boolean isControlGota() {
        return controlGota;
    }

    public void setControlGota(boolean controlGota) {
        this.controlGota = controlGota;
    }

    public String[] getDocumentacion() {
        return documentacion;
    }

	/*
     * public void setDocumentacion(String datosPersonales, String
	 * mensajesTexto, String eMail, String contrato, String factura, String
	 * tipoContrato, String telemercadeo) { this.documentacion[0] =
	 * datosPersonales; this.documentacion[1] = mensajesTexto;
	 * this.documentacion[2] = eMail; this.documentacion[3] = contrato;
	 * this.documentacion[4] = factura; this.documentacion[5] = tipoContrato;
	 * this.documentacion[6] = telemercadeo; }
	 */

    public void setDocumentacion(String id) {
        this.documentacion[0] = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalNuevos() {
        return totalNuevos;
    }

    public void setTotalNuevos(String totalNuevos) {
        this.totalNuevos = totalNuevos;
    }

    public String getScooring() {
        return scooring;
    }

    public void setScooring(String scooring) {
        this.scooring = scooring;
    }

    public String getOtrasPromociones() {
        return otrasPromociones;
    }

    public void setOtrasPromociones(String otrasPromociones) {
        this.otrasPromociones = otrasPromociones;
    }

    public String getEmpaquetamiento() {
        return empaquetamiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setEmpaquetamiento(String empaquetamiento) {
        this.empaquetamiento = empaquetamiento;
        // System.out.println("empaquetamiento " + empaquetamiento);
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        // System.out.println("observaciones " + observaciones);
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEstrato() {
        return estrato;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getListAdicionalesTo() {
        return listAdicionalesTo;
    }

    public void setListAdicionalesTo(String listAdicionalesTo) {
        this.listAdicionalesTo = listAdicionalesTo;
    }

    public String getListAdicionalesTV() {
        return listAdicionalesTV;
    }

    public void setListAdicionalesTV(String listAdicionalesTV) {
        this.listAdicionalesTV = listAdicionalesTV;
    }

    public ArrayList<ItemDecodificador> getDecos() {
        return decos;
    }

    public void setDecos(ArrayList<ItemDecodificador> decos) {
        this.decos = decos;
    }

    public String getListAdicionalesBa() {
        return listAdicionalesBa;
    }

    public void setListAdicionalesBa(String listAdicionalesBa) {
        this.listAdicionalesBa = listAdicionalesBa;
    }

    public ArrayList<ListaDefault> getListDocumentacion() {
        return listDocumentacion;
    }

    public void setListDocumentacion(ArrayList<ListaDefault> listDocumentacion) {
        this.listDocumentacion = listDocumentacion;
    }

    public boolean isValidarAgenda() {
        return validarAgenda;
    }

    public void setValidarAgenda(boolean validarAgenda) {
        this.validarAgenda = validarAgenda;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public void guardarVenta(String idAsesoria) {
        id_asesoria = idAsesoria;
        setFecha(Calendario.getFecha());
        setHora(Calendario.getHora());
        ContentValues cv = new ContentValues();
        cv.put("id_asesoria", id_asesoria);
        cv.put("Telefonia", telefonia[0]);
        cv.put("NuevoMigracionTO", telefonia[4]);
        cv.put("PlanActualTO", telefonia[5]);
        cv.put("PrecioTelefonia", telefonia[1]);
        cv.put("PromoTO", telefonia[6]);
        cv.put("TiempoPromoTO", telefonia[7]);
        cv.put("PrecioPromoTO", telefonia[8]);
        cv.put("Linea", telefonia[2]);
        cv.put("PagoLinea", telefonia[3]);
        cv.put("Television", television[0]);
        cv.put("PrecioTelevision", television[1]);
        cv.put("PromoTV", "PROMOCION: " + television[3]);
        cv.put("TiempoPromoTV", television[4]);
        cv.put("PrecioPromoTV", television[5]);
        cv.put("AdicionalTV", television[6]);
        cv.put("PreciosIndividualesAdicionalesTV", television[7]);
        cv.put("PrecioAdicionalTV", television[8]);
        cv.put("NuevoMigracionTV", television[9]);
        cv.put("PlanActualTV", television[10]);
        cv.put("Extensiones", television[2]);
        cv.put("Internet", internet[0]);
        cv.put("NuevoMigracionBA", internet[3]);
        cv.put("PlanActualBA", internet[4]);
        cv.put("PrecioInternet", internet[1]);
        cv.put("PromoBA", internet[5]);
        cv.put("TiempoPromoBA", internet[6]);
        cv.put("PrecioPromoBA", internet[7]);
        cv.put("Wifi", internet[2]);
        cv.put("InternetMovil", internet3g[0]);
        cv.put("NuevoMigracioIM", internet3g[7]);
        cv.put("PlanActualIM", internet3g[8]);
        cv.put("PrecioInternetMovil", internet3g[1]);
        cv.put("PromoIM", internet3g[9]);
        cv.put("TiempoPromoIM", internet3g[10]);
        cv.put("PrecioPromoIM", internet3g[11]);
        cv.put("Modem", internet3g[2]);
        cv.put("FinanciacionModem", internet3g[3]);
        cv.put("PorcentajeModem", internet3g[4]);
        cv.put("PrecioModem", internet3g[5]);
        cv.put("EntregaModem", internet3g[6]);
        cv.put("Internet4G", internet4g[0]);
        cv.put("PrecioInternet4G", internet4g[1]);
        cv.put("Promo4G", internet4g[2]);
        cv.put("TiempoPromo4G", internet4g[3]);
        cv.put("PrecioPromo4G", internet4g[4]);
        // cv.put("Documentacion", "Autoriza Datos Personales: "+
        // documentacion[0]+ " - Envio Mensajes Texto: "+ documentacion[1] +
        // " - Envio Mail: "+ documentacion[2] + " - Envio Contrato: "+
        // documentacion[3] + " - Envio Factura: "+ documentacion[4] +
        // " - Tipo Contracto: "+ documentacion[5]+ " - Telemercadeo: " +
        // documentacion[6]);
        cv.put("Documentacion", documentacion[0]);
        cv.put("OtrasPromociones", otrasPromociones);
        cv.put("Total", total);
        cv.put("Fecha", fecha);
        cv.put("Hora", hora);
        cv.put("Empaquetamiento", empaquetamiento);
        cv.put("Observaciones", observaciones);
        cv.put("HorarioAtencion", horarioAtencion);
        cv.put("Scooring", scooring);
        if (MainActivity.basedatos.insertar("ventas", cv)) {
            ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "ventas",
                    new String[]{"id"}, "id_asesoria=?", new String[]{idAsesoria}, null, null, null);
            id = resultado.get(0).get(0);
        } else {
            id = null;
        }
    }

    public void cargarVenta(String idAsesoria) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "ventas", null,
                "id_asesoria=?", new String[]{idAsesoria}, null, null, null);

        if (resultado != null) {
            this.id = resultado.get(0).get(0);
            this.observaciones = resultado.get(0).get(2);
            this.documentacion[0] = resultado.get(0).get(3);
            this.telefonia[0] = resultado.get(0).get(4);
            this.telefonia[1] = resultado.get(0).get(5);
            this.telefonia[2] = resultado.get(0).get(11);
            this.telefonia[3] = resultado.get(0).get(12);
            this.telefonia[4] = resultado.get(0).get(6);
            this.telefonia[5] = resultado.get(0).get(7);
            this.telefonia[6] = resultado.get(0).get(8);
            this.telefonia[7] = resultado.get(0).get(9);
            this.telefonia[8] = resultado.get(0).get(10);
            this.television[0] = resultado.get(0).get(13);
            this.television[1] = resultado.get(0).get(14);
            this.television[2] = resultado.get(0).get(15);
            this.television[3] = resultado.get(0).get(19);
            this.television[4] = resultado.get(0).get(20);
            this.television[5] = resultado.get(0).get(21);
            this.television[6] = resultado.get(0).get(16);
            this.television[7] = resultado.get(0).get(17);
            this.television[8] = resultado.get(0).get(18);
            this.television[9] = resultado.get(0).get(22);
            this.television[10] = resultado.get(0).get(23);
            this.internet[0] = resultado.get(0).get(24);
            this.internet[1] = resultado.get(0).get(25);
            this.internet[2] = resultado.get(0).get(29);
            this.internet[3] = resultado.get(0).get(30);
            this.internet[4] = resultado.get(0).get(31);
            this.internet[5] = resultado.get(0).get(26);
            this.internet[6] = resultado.get(0).get(27);
            this.internet[7] = resultado.get(0).get(28);
            this.internet3g[0] = resultado.get(0).get(32);
            this.internet3g[1] = resultado.get(0).get(33);
            this.internet3g[2] = resultado.get(0).get(39);
            this.internet3g[3] = resultado.get(0).get(40);
            this.internet3g[4] = resultado.get(0).get(41);
            this.internet3g[5] = resultado.get(0).get(42);
            this.internet3g[6] = resultado.get(0).get(43);
            this.internet3g[7] = resultado.get(0).get(34);
            this.internet3g[8] = resultado.get(0).get(35);
            this.internet3g[9] = resultado.get(0).get(36);
            this.internet3g[10] = resultado.get(0).get(37);
            this.internet3g[11] = resultado.get(0).get(38);
            this.internet4g[0] = resultado.get(0).get(44);
            this.internet4g[1] = resultado.get(0).get(45);
            this.internet4g[2] = resultado.get(0).get(46);
            this.internet4g[3] = resultado.get(0).get(47);
            this.internet4g[4] = resultado.get(0).get(48);
            this.otrasPromociones = resultado.get(0).get(49);
            this.total = resultado.get(0).get(50);
            this.empaquetamiento = resultado.get(0).get(51);
            this.fecha = resultado.get(0).get(52);
            this.hora = resultado.get(0).get(53);
            this.horarioAtencion = resultado.get(0).get(54);
            this.scooring = resultado.get(0).get(55);
        }
    }

    public JSONObject consolidarVenta() {
        JSONObject venta = new JSONObject();
        JSONArray adicionalesTO = new JSONArray();
        JSONArray adicionalesTV = new JSONArray();
        ArrayList<JSONObject> objectProductos = new ArrayList<JSONObject>();
        JSONArray arrayProductos = new JSONArray();

        try {

            if (!telefonia[0].equalsIgnoreCase(Utilidades.inicial_guion) && !telefonia[0].equalsIgnoreCase("")) {
                System.out.println("telefonia[16] " + telefonia[16]);
                objectProductos.add(new Utilidades().jsonProductosVenta(telefonia[0], "TO", telefonia[4], telefonia[5],
                        telefonia[1], telefonia[6], telefonia[7], "null", "null", telefonia[2], "null", "null", "null",
                        listAdicionalesTo, telefonia[9], telefonia[10], "N/A", telefonia[11], telefonia[12],
                        telefonia[16], null, null, telefonia[17], telefonia[18]));
            }

            if (!television[0].equalsIgnoreCase(Utilidades.inicial_guion) && !television[0].equalsIgnoreCase("")) {
                System.out.println("television[16] " + television[16]);
                objectProductos.add(new Utilidades().jsonProductosVenta(television[0], "TV", television[9],
                        television[10], television[1], television[3], television[4], television[2], "null", "null",
                        "null", "", "null", listAdicionalesTV, television[11], television[13], television[12],
                        television[14], television[15], television[16], itemDecodificadors, null, television[17], television[18]));
            }

            if (!internet[0].equalsIgnoreCase(Utilidades.inicial_guion) && !internet[0].equalsIgnoreCase("")) {
                System.out.println("internet[11] " + internet[11]);
                objectProductos.add(new Utilidades().jsonProductosVenta(internet[0], "BA", internet[3], internet[4],
                        internet[1], internet[5], internet[6], "null", internet[2], "null", "null", "", "null",
                        listAdicionalesBa, internet[8], "N/A", "N/A", internet[9], internet[10], internet[11], null,
                        internet[12], internet[13], internet[14]));

            }

            for (int i = 0; i < objectProductos.size(); i++) {
                arrayProductos.put(objectProductos.get(i));
                System.out.println("objectProductos.get(" + i + ") " + objectProductos.get(i).toString());
            }

            venta.put("productos", arrayProductos);
            venta.put("total", total);
            venta.put("totalPagoAntCargoFijo", totalPagoAntCargoFijo);
            venta.put("totalPagoConexion", totalPagoConexion);
            venta.put("totalPagoParcialConexion", totalPagoParcialConexion);
            venta.put("descuentoConexion", descuentoConexion);
            venta.put("totalNuevos", totalNuevos);
            venta.put("fecha", fecha + " " + hora);
            venta.put("permanencia", telefonia[3]);

            if (empaquetamiento.equalsIgnoreCase("SI")) {
                empaquetada = "1";
            } else {
                empaquetada = "0";
            }

            venta.put("empaquetada", empaquetada);
            venta.put("empaquetadaNombre", empaquetamiento);
            venta.put("dependencias", dependencia);

            venta.put("observaciones", observaciones.replace(";", ".,"));
            venta.put("horarioAtencion", horarioAtencion);
            venta.put("microZona", microZona);
            venta.put("cobroDomingo", cobroDomingo);
            venta.put("tipoServicio", tipoServicio);
            venta.put("estrato", estrato);
            venta.put("municipio", municipio);
            venta.put("departamento", departamento);

			/*
			 * JSONObject movilidad = new JSONObject();
			 * movilidad.put("producto", "");
			 * 
			 * venta.put("movilidad", movilidad);
			 */
            venta.put("movilidad", "");
            venta.put("municipiossc", municipioSSC);

            if (agendaSiebel != null && !agendaSiebel.equalsIgnoreCase("")) {
                venta.put("agendaSiebel", new JSONObject(agendaSiebel));
                venta.put("idOferta", idOferta);
            } else {
                venta.put("agendaSiebel", "");
                venta.put("idOferta", "");
            }
            venta.put("agendableSiebel", agendableSiebel);
            venta.put("oferta", oferta);

            if (Utilidades.excluirNacional("conceptoConstructor", TipoConstruccion)) {
                venta.put("constructores", 1);
                venta.put("conceptoConstructor", TipoConstruccion);
            } else {
                venta.put("constructores", 0);
            }
            if (medioIngreso != null) {
                venta.put("medioIngreso", medioIngreso);
            } else {
                venta.put("medioIngreso", "n/a");
            }

            // venta.put("scooring", scooring);
        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }
        return venta;
    }

    public void quitarPagosParciales() {
        setTotalPagoParcialConexion("0.0");
        setDescuentoConexion("0.0");
        this.telefonia[18] = "0.0";
        this.television[18] = "0.0";
        this.internet[14] = "0.0";
    }

    public String getMunicipioSSC() {
        return municipioSSC;
    }

    public void setMunicipioSSC(String municipioSSC) {
        this.municipioSSC = municipioSSC;
    }

    public String getTipoConstruccion() {
        return TipoConstruccion;
    }

    public void setTipoConstruccion(String tipoConstruccion) {
        TipoConstruccion = tipoConstruccion;
    }

    public boolean isValidarTelevisores() {
        return validarTelevisores;
    }

    public void setValidarTelevisores(boolean validarTelevisores) {
        this.validarTelevisores = validarTelevisores;
    }

    public boolean isValidarDecosPago() {
        return validarDecosPago;
    }

    public void setValidarDecosPago(boolean validarDecosPago) {
        this.validarDecosPago = validarDecosPago;
    }

    public boolean isValidarOfertaScooring() {
        return validarOfertaScooring;
    }

    public void setValidarOfertaScooring(boolean validarOfertaScooring) {
        this.validarOfertaScooring = validarOfertaScooring;
    }

    public String getMedioIngreso() {
        return medioIngreso;
    }

    public void setMedioIngreso(String medioIngreso) {
        this.medioIngreso = medioIngreso;
    }

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }

    public String getAgendaSiebel() {
        return agendaSiebel;
    }

    public void setAgendaSiebel(String agendaSiebel) {
        this.agendaSiebel = agendaSiebel;
    }

    public String getMicroZona() {
        return microZona;
    }

    public void setMicroZona(String microZona) {
        this.microZona = microZona;
    }

    public String getCobroDomingo() {
        return cobroDomingo;
    }

    public void setCobroDomingo(String cobroDomingo) {
        this.cobroDomingo = cobroDomingo;
    }

    public String getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(String idOferta) {
        this.idOferta = idOferta;
    }

    public int getAgendableSiebel() {
        return agendableSiebel;
    }

    public void setAgendableSiebel(int agendableSiebel) {
        this.agendableSiebel = agendableSiebel;
    }

    public String getMensajeScore() {
        return mensajeScore;
    }

    public void setMensajeScore(String mensajeScore) {
        this.mensajeScore = mensajeScore;
    }

    public ArrayList<ItemDecodificador> getItemDecodificadors() {
        return itemDecodificadors;
    }

    public void setItemDecodificadors(ArrayList<ItemDecodificador> itemDecodificadors) {
        this.itemDecodificadors = itemDecodificadors;
    }

    public String getTotalPagoAntCargoFijo() {
        return totalPagoAntCargoFijo;
    }

    public void setTotalPagoAntCargoFijo(String totalPagoAntCargoFijo) {
        this.totalPagoAntCargoFijo = totalPagoAntCargoFijo;
    }

    public String getTotalPagoConexion() {
        return totalPagoConexion;
    }

    public void setTotalPagoConexion(String totalPagoConexion) {
        this.totalPagoConexion = totalPagoConexion;
    }

    public String getTotalPagoParcialConexion() {
        return totalPagoParcialConexion;
    }

    public void setTotalPagoParcialConexion(String totalPagoParcialConexion) {
        this.totalPagoParcialConexion = totalPagoParcialConexion;
    }

    public String getDescuentoConexion() {
        return descuentoConexion;
    }

    public void setDescuentoConexion(String descuentoConexion) {
        this.descuentoConexion = descuentoConexion;
    }
}
