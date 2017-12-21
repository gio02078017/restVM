package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Calendario;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Asesoria implements Serializable {

    private String id;
    private String VisitaAtendida;
    private String Observaciones;
    private String FechaInicial;
    private String HoraInicial;
    private String FechaFinal;
    private String HoraFinal;
    private String Coordenadas;
    public Configuracion Configuracion;

    public Cliente cliente;
    public Competencia competencia;
    public Peticion peticion;
    public Obsequio obsequio;
    public Venta venta;
    public Cotizacion cotizacion;
    public CotizacionCliente cotizacionCliente;
    public Scooring scooring;
    public Prospecto prospecto;
    public Blindaje blindaje;
    public UneMas unemas;

    public Localizacion localizacion;
    ArrayList<String[]> localizaciones = new ArrayList<String[]>();

    public Asesoria() {
        this.VisitaAtendida = "";
        this.Observaciones = "";
        this.FechaInicial = Calendario.getFecha();
        this.HoraInicial = Calendario.getHora();
        this.FechaFinal = "";
        this.HoraFinal = "";
    }

    public void guardarAsesoria() {
        ContentValues cv = new ContentValues();
        if (Configuracion.getCodigo() == null) {
            Configuracion.setCodigo("1234");
        }
        cv.put("codigo_asesor", Configuracion.getCodigo());
        cv.put("VisitaAtendida", VisitaAtendida);
        cv.put("Observaciones", Observaciones);
        cv.put("FechaInicial", FechaInicial);
        cv.put("HoraInicial", HoraInicial);
        cv.put("FechaFinal", FechaFinal);
        cv.put("HoraFinal", HoraFinal);
        if (MainActivity.basedatos.insertar("asesorias", cv)) {
            ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "asesorias",
                    new String[]{"id"}, "codigo_asesor=? and FechaInicial=? and HoraInicial=?",
                    new String[]{Configuracion.getCodigo(), FechaInicial, HoraInicial}, null, null, null);
            if (resultado != null) {
                id = resultado.get(0).get(0);
            } else {
                id = null;
            }
        } else {
            id = null;
        }
    }

    public String consolidarAsesoria() {
        FechaFinal = Calendario.getFecha();
        HoraFinal = Calendario.getHora();
        JSONObject jo = new JSONObject();
        int tipoAsesoria = 0;
        try {
            jo.put("atendida", "SI");
            jo.put("observaciones", "NO");
            jo.put("fechaInicial", FechaInicial + " " + HoraInicial);
            // jo.put("HoraInicial", HoraInicial);
            jo.put("fechaFinal", FechaFinal + " " + HoraFinal);
            jo.put("Fisico", "Físico");
            jo.put("Fisica", "Física");
            jo.put("Electronico", "Electrónico");
            // jo.put("HoraFinal", HoraFinal);
            // jo.put("Coordenadas", "");

            if (scooring != null) {
                if (scooring.isValidarCartera()) {
                    String[] carteraUNE = scooring.getCarteraUNE();
                    jo.put("estadoCuentasUne", carteraUNE[1]);
                    jo.put("accionCuentasUne", carteraUNE[2]);
                    jo.put("origenCuentasUne", carteraUNE[3]);
                } else {
                    jo.put("estadoCuentasUne", "N/A");
                    jo.put("accionCuentasUne", "N/A");
                    jo.put("origenCuentasUne", "N/A");
                }
            } else {
                jo.put("estadoCuentasUne", "");
                jo.put("accionCuentasUne", "");
                jo.put("origenCuentasUne", "");
            }

            if (cliente != null) {
                jo.put("cliente", cliente.consolidarCliente());
            } else {
                jo.put("cliente", "");
            }
            /*
			 * if (peticion != null) { jo.put("peticion",
			 * peticion.consolidarPeticion()); } else { jo.put("peticion", "");
			 * }
			 */
            if (competencia != null) {
                jo.put("competencia", competencia.consolidarCompetencia(cliente.getEstrato()));
                tipoAsesoria = 3;
            } else {
                jo.put("competencia", "");
            }
			/*
			 * if (obsequio != null) { jo.put("obsequio", ""); } else {
			 * jo.put("obsequio", ""); }
			 */
            if (venta != null && prospecto == null) {
                venta.setTipoServicio(cliente.getTipoServicio());
                venta.setEstrato(cliente.getEstrato());
                venta.setDepartamento(MainActivity.config.getDepartamento());
                venta.setMunicipio(cliente.getCiudad());
                venta.setMunicipioSSC(cliente.getMunicipioSSC());
                venta.setTipoConstruccion(cliente.getTipoConstruccion());
                System.out.println("venta.consolidarVenta()" + venta.consolidarVenta());

                jo.put("venta", venta.consolidarVenta());
                jo.put("confirmacionid", venta.getDocumentacion()[0]);
                tipoAsesoria = 2;
            } else {
                jo.put("venta", "");
                jo.put("confirmacionid", "");
            }

            if (Configuracion != null) {
                // jo.put("configuracion",
                // Configuracion.consolidarConfiguracion());
                jo.put("asesor", Configuracion.getCodigo());
                jo.put("canal", MainActivity.config.getCanal());
                // jo.put("canal", Configuracion.get);
                jo.put("municipio", Configuracion.getCiudad());
                jo.put("departamento", Configuracion.getDepartamento());
            } else {
                jo.put("configuracion", "");
            }

            if (prospecto != null) {
                jo.put("prospecto", prospecto.consolidarProspecto());
                tipoAsesoria = 1;
            } else {
                jo.put("prospecto", "");
            }

            if (blindaje != null) {
                jo.put("blindaje", blindaje.consolidarBlindaje());
                jo.put("confirmacionid", blindaje.idIVR);
            } else {
                jo.put("blindaje", "");
            }

            localizaciones.clear();
            localizaciones = localizacion.getLocalizaciones();
            if (localizaciones.size() > 0) {
                if (localizacion != null) {
                    jo.put("localizacion",
                            localizacion.consolidarLocalizacion(tipoAsesoria, Configuracion.getCodigo()));
                }
            } else {

                System.out.println("Utilidades " + Utilidades.localizacionDefault(cliente.getCiudad()));

                JSONObject defaultLocalizacion = new JSONObject(Utilidades.localizacionDefault(cliente.getCiudad()));

                localizacion.almacenar(defaultLocalizacion.getString("latitud"),
                        defaultLocalizacion.getString("longitud"), "Default");

                if (localizacion != null) {
                    jo.put("localizacion",
                            localizacion.consolidarLocalizacion(tipoAsesoria, Configuracion.getCodigo()));
                }
            }

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

        // System.out.println(jo.toString());
        return jo.toString();

    }

    public void cargarAsesoria(String id) {
        this.id = id.trim();
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "asesorias", null, "id=?",
                new String[]{this.id}, null, null, null);
        // System.out.println(resultado);

        if (resultado != null) {
            this.VisitaAtendida = resultado.get(0).get(2);
            this.Observaciones = resultado.get(0).get(3);
            this.FechaInicial = resultado.get(0).get(4);
            this.HoraInicial = resultado.get(0).get(5);
            this.FechaFinal = resultado.get(0).get(6);
            this.HoraFinal = resultado.get(0).get(7);
            Configuracion = MainActivity.config;
            cliente = new Cliente();
            cliente.cargarCliente(this.id);
            cliente.getFacturacion().cargarFacturacion(cliente.getId());
            cotizacion = new Cotizacion();
            cotizacion.cargarCotizacion(this.id);
            cotizacion.setEstrato(cliente.getEstrato());
            venta = new Venta();
            venta.cargarVenta(this.id);
            if (venta.getTotal() == null) {
                venta = null;
            }
        } else {
            // System.out.println("No asesorias");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.Coordenadas = coordenadas;
    }

}
