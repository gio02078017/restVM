package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.model.Decodificadores;

public class Cotizacion implements Serializable {

    private String id, idAsesoria;

    private String tipoTo;
    private String telefonia;
    private String toInd, toEmp;
    private String toDInd, toDEmp;
    private String toPagoAntCargoFijo;
    private String toPagoParcialConexion;
    private String promoTo;
    private String tiempoPromoTo;
    private String planFacturacionTo_I;
    private String planFacturacionTo_P;
    private String tipoCotizacionTo;
    private boolean segundaTelefonia;
    public String toIdent;
    public String toPlanAnt;
    public String toTecnologiacr;

    private String tipoTv;
    private String television;
    private String tvInd, tvEmp;
    private String tvPagoAntCargoFijo;
    private String tvPagoParcialConexion;
    private String tvDInd, tvDEmp;
    private String promoTv;
    private String tiempoPromoTv;
    private String planFacturacionTv_I;
    private String planFacturacionTv_P;
    private String tipoCotizacionTv;
    public String tvIdent;
    public String tvPlanAnt;
    public String tvTecnologiacr;

    private String[][] adicionales;
    private String adInd, adEmp, totalAdicionales;

    private String[][] adicionalesTo;
    private String adIndTo, adEmpTo, totalAdicionalesTo;

    private String[][] adicionalesBa;
    private String adIndBa, adEmpBa, totalAdicionalesBa;

    private String tipoBa;
    private String internet;
    private String baInd, baEmp;
    private String baDInd, baDEmp;
    private String baPagoAntCargoFijo;
    private String baPagoParcialConexion;
    private String promoBa;
    private String tiempoPromoBa;
    private String planFacturacionBa_I;
    private String planFacturacionBa_P;
    private String tipoCotizacionBa;
    public String baIdent;
    public String baPlanAnt;
    public String baTecnologiacr;
    public String Ipdinamica;

    private String internet3g;
    private String internet3gInd, internet3gEmp;
    private String internet3gDInd, internet3gDEmp;
    private String promo3g;

    private String internet4g;
    private String internet4gInd, internet4gEmp;
    private String internet4gDInd, internet4gDEmp;
    private String promo4g;

    private String totalInd, totalEmp;

    private String totalPagoAntCargoFijo, totalPagoConexion, totalPagoParcialConexion, descuentoConexion;

    private String totalIndDescuento, totalEmpDescuento;

    private String contadorProductos;

    private String estrato;

    private boolean validoIVR;
    private boolean aplicarAd;
    private boolean promoTo_12_100;

    private boolean controlGota;
    private int precioGota;
    private int precioGotaSinIva;
    private String velocidadInicial;
    private String velocidadFinal;
    private String nombreGota;

    private String oferta;

    public String medioIngreso;

    private boolean cambiarCotizacion = false;

    private ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales;
    private ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionalesInternet;

    private ArrayList<ItemDecodificador> itemDecodificadores;
    private Decodificadores objectDecodificador;

    private boolean controlDescuentosAd = false;
    private double preciosConDescuentoAd = 0.0;
    private double precioDescuentosAd = 0.0;

    private ArrayList<ItemDecodificador> decodificadores;
    private double totalDecos = 0.0;

    private boolean HFCDigital = false;

    private String tipoOferta;
    private String ofertaCotizacion;

    private boolean retiroCrackleBa = false;
    private boolean retiroHBOGO= false;

    public Cotizacion() {
        this.telefonia = "";
        this.toInd = "";
        this.toEmp = "";
        this.toDInd = "";
        this.toDEmp = "";
        this.promoTo = "";
        this.television = "";
        this.tvInd = "";
        this.tvEmp = "";
        this.tvDInd = "";
        this.tvDEmp = "";
        this.promoTv = "";
        this.adicionales = null;
        this.adicionalesTo = null;
        this.adicionalesBa = null;
        this.adInd = "";
        this.adEmp = "";
        this.internet = "";
        this.baInd = "";
        this.baEmp = "";
        this.baDInd = "";
        this.baDEmp = "";
        this.promoBa = "";
        this.internet3g = "";
        this.internet3gInd = "";
        this.internet3gEmp = "";
        this.internet3gDInd = "";
        this.internet3gDEmp = "";
        this.promo3g = "";
        this.internet4g = "";
        this.internet4gInd = "";
        this.internet4gEmp = "";
        this.internet4gDInd = "";
        this.internet4gDEmp = "";
        this.promo4g = "";
        this.totalInd = "";
        this.totalEmp = "";
        this.totalIndDescuento = "";
        this.totalEmpDescuento = "";
        this.contadorProductos = "";
        this.estrato = "";
        this.toIdent = "";
        this.tvIdent = "";
        this.baIdent = "";
        this.toPlanAnt = "";
        this.tvPlanAnt = "";
        this.baPlanAnt = "";
        this.aplicarAd = false;
        this.controlGota = false;
        this.precioGota = 0;
        this.precioGotaSinIva = 0;
        this.velocidadInicial = "";
        this.velocidadFinal = "";
        this.nombreGota = "";

    }

    public Cotizacion(String telefonia, String toInd, String toEmp, String toDInd, String toDEmp, String promoTo,
                      String television, String tvInd, String tvEmp, String tvDInd, String tvDEmp, String promoTv,
                      String[][] adicionales, String adInd, String adEmp, boolean aplicarAd, String internet, String baInd,
                      String baEmp, String baDInd, String baDEmp, String promoBa, String internet3g, String internet3gInd,
                      String internet3gEmp, String internet3gDInd, String internet3gDEmp, String promo3g, String internet4g,
                      String internet4gInd, String internet4gEmp, String internet4gDInd, String internet4gDEmp, String promo4g,
                      String totalInd, String totalEmp, String contadorProductos, String estrato) {
        super();
        this.telefonia = telefonia;
        this.toInd = toInd;
        this.toEmp = toEmp;
        this.toDInd = toDInd;
        this.toDEmp = toDEmp;
        this.promoTo = promoTo;
        this.television = television;
        this.tvInd = tvInd;
        this.tvEmp = tvEmp;
        this.tvDInd = tvDInd;
        this.tvDEmp = tvDEmp;
        this.promoTv = promoTv;
        this.adicionales = adicionales;
        this.adInd = adInd;
        this.adEmp = adEmp;
        this.internet = internet;
        this.baInd = baInd;
        this.baEmp = baEmp;
        this.baDInd = baDInd;
        this.baDEmp = baDEmp;
        this.promoBa = promoBa;
        this.internet3g = internet3g;
        this.internet3gInd = internet3gInd;
        this.internet3gEmp = internet3gEmp;
        this.internet3gDInd = internet3gDInd;
        this.internet3gDEmp = internet3gDEmp;
        this.promo3g = promo3g;
        this.internet4g = internet4g;
        this.internet4gInd = internet4gInd;
        this.internet4gEmp = internet4gEmp;
        this.internet4gDInd = internet4gDInd;
        this.internet4gDEmp = internet4gDEmp;
        this.promo4g = promo4g;
        this.totalInd = totalInd;
        this.totalEmp = totalEmp;
        this.contadorProductos = contadorProductos;
        this.estrato = estrato;
        this.validoIVR = false;
        this.aplicarAd = aplicarAd;
    }

    public void actualizarCotizacion(String id_asesoria) {
        idAsesoria = id_asesoria;
        ContentValues cv = new ContentValues();
        cv.put("id_asesoria", idAsesoria);
        cv.put("telefonia", telefonia);
        cv.put("toInd", toInd);
        cv.put("toEmp", toEmp);
        cv.put("toDInd", toDInd);
        cv.put("toDEmp", toDEmp);
        cv.put("promoTo", promoTo);
        cv.put("television", television);
        cv.put("tvInd", tvInd);
        cv.put("tvEmp", tvEmp);
        cv.put("tvDInd", tvDInd);
        cv.put("tvDEmp", tvDEmp);
        cv.put("promoTv", promoTv);
        if (adicionales.length > 0) {
            String adic = "";
            String adicPre = "";
            for (int i = 0; i < adicionales.length; i++) {
                adic = adic + adicionales[i][0] + ",";
                adicPre = adicPre + adicionales[i][1] + ",";
            }
            adic = adic.substring(adic.length() - 1);
            adicPre = adicPre.substring(adicPre.length() - 1);
            cv.put("adicionales", adic);
            cv.put("precioAdicionales", adicPre);
        } else {
            cv.put("adicionales", "");
            cv.put("precioAdicionales", "");
        }
        cv.put("adInd", adInd);
        cv.put("adEmp", adEmp);
        cv.put("internet", internet);
        cv.put("baInd", baInd);
        cv.put("baEmp", baEmp);
        cv.put("baDInd", baDInd);
        cv.put("baDEmp", baDEmp);
        cv.put("promoBa", promoBa);
        cv.put("internet3g", internet3g);
        cv.put("internet3gInd", internet3gInd);
        cv.put("internet3gEmp", internet3gEmp);
        cv.put("internet3gDInd", internet3gDInd);
        cv.put("internet3gDEmp", internet3gDEmp);
        cv.put("promo3g", promo3g);
        cv.put("internet4g", internet4g);
        cv.put("internet4gInd", internet4gInd);
        cv.put("internet4gEmp", internet4gEmp);
        cv.put("internet4gDInd", internet4gDInd);
        cv.put("internet4gDEmp", internet4gDEmp);
        cv.put("promo4g", promo4g);
        cv.put("totalInd", totalInd);
        cv.put("totalEmp", totalEmp);
        cv.put("contadorProductos", contadorProductos);
        cv.put("estrato", estrato);
        cv.put("validoIVR", validoIVR);

        if (id == null) {
            guardarCotizacion(id_asesoria);
        } else if (MainActivity.basedatos.actualizar("cotizacion", cv, "id=?", new String[]{id})) {
            // System.out.println("cotizacion Actualizada");
        }
    }

    public void guardarCotizacion(String id_asesoria) {
        idAsesoria = id_asesoria;
        ContentValues cv = new ContentValues();
        cv.put("id_asesoria", idAsesoria);
        cv.put("telefonia", telefonia);
        cv.put("toInd", toInd);
        cv.put("toEmp", toEmp);
        cv.put("toDInd", toDInd);
        cv.put("toDEmp", toDEmp);
        cv.put("promoTo", promoTo);
        cv.put("television", television);
        cv.put("tvInd", tvInd);
        cv.put("tvEmp", tvEmp);
        cv.put("tvDInd", tvDInd);
        cv.put("tvDEmp", tvDEmp);
        cv.put("promoTv", promoTv);
        if (adicionales.length > 0) {
            String adic = "";
            String adicPre = "";
            for (int i = 0; i < adicionales.length; i++) {
                adic = adic + adicionales[i][0] + ",";
                adicPre = adicPre + adicionales[i][1] + ",";
            }
            adic = adic.substring(adic.length() - 1);
            adicPre = adicPre.substring(adicPre.length() - 1);
            cv.put("adicionales", adic);
            cv.put("precioAdicionales", adicPre);
        } else {
            cv.put("adicionales", "");
            cv.put("precioAdicionales", "");
        }
        cv.put("adInd", adInd);
        cv.put("adEmp", adEmp);
        cv.put("internet", internet);
        cv.put("baInd", baInd);
        cv.put("baEmp", baEmp);
        cv.put("baDInd", baDInd);
        cv.put("baDEmp", baDEmp);
        cv.put("promoBa", promoBa);
        cv.put("internet3g", internet3g);
        cv.put("internet3gInd", internet3gInd);
        cv.put("internet3gEmp", internet3gEmp);
        cv.put("internet3gDInd", internet3gDInd);
        cv.put("internet3gDEmp", internet3gDEmp);
        cv.put("promo3g", promo3g);
        cv.put("internet4g", internet4g);
        cv.put("internet4gInd", internet4gInd);
        cv.put("internet4gEmp", internet4gEmp);
        cv.put("internet4gDInd", internet4gDInd);
        cv.put("internet4gDEmp", internet4gDEmp);
        cv.put("promo4g", promo4g);
        cv.put("totalInd", totalInd);
        cv.put("totalEmp", totalEmp);
        cv.put("contadorProductos", contadorProductos);
        cv.put("estrato", estrato);
        cv.put("validoIVR", validoIVR);

        if (MainActivity.basedatos.insertar("cotizacion", cv)) {
            ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "cotizacion",
                    new String[]{"id"}, "id_asesoria=?", new String[]{idAsesoria}, null, null, null);
            id = resultado.get(0).get(0);
        } else {
            id = null;
        }
    }

    public void cargarCotizacion(String id_asesoria) {
        idAsesoria = id_asesoria;

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "cotizacion", null,
                "id_asesoria=?", new String[]{idAsesoria}, null, null, null);
        // System.out.println("Cotizacion 196 => " + resultado);
        if (resultado != null) {
            this.id = resultado.get(0).get(0);
            this.telefonia = resultado.get(0).get(2);
            this.toInd = resultado.get(0).get(3);
            this.toEmp = resultado.get(0).get(4);
            this.toDInd = resultado.get(0).get(5);
            this.toDEmp = resultado.get(0).get(6);
            this.promoTo = resultado.get(0).get(7);
            this.television = resultado.get(0).get(8);
            this.tvInd = resultado.get(0).get(9);
            this.tvEmp = resultado.get(0).get(10);
            this.tvDInd = resultado.get(0).get(11);
            this.tvDEmp = resultado.get(0).get(12);
            this.promoTv = resultado.get(0).get(13);
            String[] adic = resultado.get(0).get(14).split(",");
            String[] preAdic = resultado.get(0).get(15).split(",");

            String[][] adicPre = new String[adic.length][2];

            for (int i = 0; i < adic.length; i++) {
                adicPre[i][0] = adic[i];
                adicPre[i][1] = preAdic[i];
            }

            this.adicionales = adicPre;

            this.adInd = resultado.get(0).get(16);
            this.adEmp = resultado.get(0).get(17);
            this.internet = resultado.get(0).get(18);
            this.baInd = resultado.get(0).get(19);
            this.baEmp = resultado.get(0).get(20);
            this.baDInd = resultado.get(0).get(21);
            this.baDEmp = resultado.get(0).get(22);
            this.promoBa = resultado.get(0).get(23);
            this.internet3g = resultado.get(0).get(24);
            this.internet3gInd = resultado.get(0).get(25);
            this.internet3gEmp = resultado.get(0).get(26);
            this.internet3gDInd = resultado.get(0).get(27);
            this.internet3gDEmp = resultado.get(0).get(28);
            this.promo3g = resultado.get(0).get(29);
            this.internet4g = resultado.get(0).get(30);
            this.internet4gInd = resultado.get(0).get(31);
            this.internet4gEmp = resultado.get(0).get(32);
            this.internet4gDInd = resultado.get(0).get(33);
            this.internet4gDEmp = resultado.get(0).get(34);
            this.promo4g = resultado.get(0).get(35);
            this.totalInd = resultado.get(0).get(36);
            this.totalEmp = resultado.get(0).get(37);
            this.contadorProductos = resultado.get(0).get(38);
            this.estrato = resultado.get(0).get(39);
        }
    }

    public void Telefonia(String tipoTo, String telefonia, String toInd, String toEmp, String toDInd, String toDEmp,
                          String promoTo, String tiempoPromoTo, String toIdent, String toPlanAnt, String toTecnologiacr) {
        this.tipoTo = tipoTo;
        this.telefonia = telefonia;
        this.toInd = toInd;
        this.toEmp = toEmp;
        this.toDInd = toDInd;
        this.toDEmp = toDEmp;
        this.promoTo = promoTo;
        this.tiempoPromoTo = tiempoPromoTo;
        this.toIdent = toIdent;
        this.toPlanAnt = toPlanAnt;
        this.toTecnologiacr = toTecnologiacr;

    }

    public void TelefoniaCargos(String toPagoAntCargoFijo, String toPagoParcialConexion) {
        this.toPagoAntCargoFijo = toPagoAntCargoFijo;
        this.toPagoParcialConexion = toPagoParcialConexion;
    }

    public void Telefonia(String tipoTo, String telefonia, String toInd, String toEmp) {
        this.tipoTo = tipoTo;
        this.telefonia = telefonia;
        this.toInd = toInd;
        this.toEmp = toEmp;
    }

    public void Television(String tipoTv, String television, String tvInd, String tvEmp, String tvDInd, String tvDEmp,
                           String promoTv, String tiempoPromoTv, String tvIdent, String tvPlanAnt, String tvTecnologiacr) {
        this.tipoTv = tipoTv;
        this.television = television;
        this.tvInd = tvInd;
        this.tvEmp = tvEmp;
        this.tvDInd = tvDInd;
        this.tvDEmp = tvDEmp;
        this.promoTv = promoTv;
        this.tiempoPromoTv = tiempoPromoTv;
        this.tvIdent = tvIdent;
        this.tvPlanAnt = tvPlanAnt;
        this.tvTecnologiacr = tvTecnologiacr;

    }

    public void TelevisionCargos(String tvPagoAntCargoFijo, String tvPagoParcialConexion) {
        this.tvPagoAntCargoFijo = tvPagoAntCargoFijo;
        this.tvPagoParcialConexion = tvPagoParcialConexion;
    }

    public void Television(String tipoTv, String television, String tvInd, String tvEmp) {
        this.tipoTv = tipoTv;
        this.television = television;
        this.tvInd = tvInd;
        this.tvEmp = tvEmp;
    }

    public void Adicionales(String[][] adicionales, String adInd, String adEmp) {
        this.adicionales = adicionales;
        this.adInd = adInd;
        this.adEmp = adEmp;
    }

    public void AdicionalesTO(String[][] adicionales, String adInd, String adEmp) {
        this.adicionalesTo = adicionales;
        this.adIndTo = adInd;
        this.adEmpTo = adEmp;
    }

    public void AdicionalesBA(String[][] adicionales, String adInd, String adEmp) {
        this.adicionalesBa = adicionales;
        this.adIndBa = adInd;
        this.adEmpBa = adEmp;
    }

    public void Internet(String tipoBa, String internet, String baInd, String baEmp, String baDInd, String baDEmp,
                         String promoBa, String tiempoPromoBa, String baIdent, String baPlanAnt, String baTecnologiacr) {
        this.tipoBa = tipoBa;
        this.internet = internet;
        this.baInd = baInd;
        this.baEmp = baEmp;
        this.baDInd = baDInd;
        this.baDEmp = baDEmp;
        this.promoBa = promoBa;
        this.tiempoPromoBa = tiempoPromoBa;
        this.baIdent = baIdent;
        this.baPlanAnt = baPlanAnt;
        this.baTecnologiacr = baTecnologiacr;

    }

    public void InternetCargos(String baPagoAntCargoFijo, String baPagoParcialConexion) {
        this.baPagoAntCargoFijo = baPagoAntCargoFijo;
        this.baPagoParcialConexion = baPagoParcialConexion;
    }

    public void Internet(String tipoBa, String internet, String baInd, String baEmp) {
        this.tipoBa = tipoBa;
        this.internet = internet;
        this.baInd = baInd;
        this.baEmp = baEmp;
    }

    public void Internet_3G(String internet3g, String internet3gInd, String internet3gEmp, String internet3gDInd,
                            String internet3gDEmp, String promo3g) {
        this.internet3g = internet3g;
        this.internet3gInd = internet3gInd;
        this.internet3gEmp = internet3gEmp;
        this.internet3gDInd = internet3gDInd;
        this.internet3gDEmp = internet3gDEmp;
        this.promo3g = promo3g;
    }

    public void Internet_4G(String internet4g, String internet4gInd, String internet4gEmp, String internet4gDInd,
                            String internet4gDEmp, String promo4g) {
        this.internet4g = internet4g;
        this.internet4gInd = internet4gInd;
        this.internet4gEmp = internet4gEmp;
        this.internet4gDInd = internet4gDInd;
        this.internet4gDEmp = internet4gDEmp;
        this.promo4g = promo4g;
    }

    public void Otros(String totalInd, String totalEmp, String contadorProductos, String estrato) {
        this.totalInd = totalInd;
        this.totalEmp = totalEmp;
        this.contadorProductos = contadorProductos;
        this.estrato = estrato;
    }

    public void setGota(boolean controlGota, int precioGota, int precioGotaSinIva, String velocidadInicial,
                        String velocidadFinal, String nombreGota) {
        this.controlGota = controlGota;
        this.precioGota = precioGota;
        this.precioGotaSinIva = precioGotaSinIva;
        this.velocidadInicial = velocidadInicial;
        this.velocidadFinal = velocidadFinal;
        this.nombreGota = nombreGota;
    }

    public String getTipoTo() {
        return tipoTo;
    }

    public void setTipoTo(String tipoTo) {
        this.tipoTo = tipoTo;
    }

    public String getTelefonia() {
        return telefonia;
    }

    public void setTelefonia(String telefonia) {
        this.telefonia = telefonia;
    }

    public String getToInd() {
        return toInd;
    }

    public void setToInd(String toInd) {
        this.toInd = toInd;
    }

    public String getToEmp() {
        return toEmp;
    }

    public void setToEmp(String toEmp) {
        this.toEmp = toEmp;
    }

    public String getToDInd() {
        return toDInd;
    }

    public void setToDInd(String toDInd) {
        this.toDInd = toDInd;
    }

    public String getToDEmp() {
        return toDEmp;
    }

    public void setToDEmp(String toDEmp) {
        this.toDEmp = toDEmp;
    }

    public String getPromoTo() {
        return promoTo;
    }

    public void setPromoTo(String promoTo) {
        this.promoTo = promoTo;
    }

    public String getTiempoPromoTo() {
        return tiempoPromoTo;
    }

    public void setTiempoPromoTo(String tiempoPromoTo) {
        this.tiempoPromoTo = tiempoPromoTo;
    }

    public String getTipoTv() {
        return tipoTv;
    }

    public void setTipoTv(String tipoTv) {
        this.tipoTv = tipoTv;
    }

    public String getTelevision() {
        return television;
    }

    public void setTelevision(String television) {
        this.television = television;
    }

    public String getTvInd() {
        return tvInd;
    }

    public void setTvInd(String tvInd) {
        this.tvInd = tvInd;
    }

    public String getTvEmp() {
        return tvEmp;
    }

    public void setTvEmp(String tvEmp) {
        this.tvEmp = tvEmp;
    }

    public String getTvDInd() {
        return tvDInd;
    }

    public void setTvDInd(String tvDInd) {
        this.tvDInd = tvDInd;
    }

    public String getTvDEmp() {
        return tvDEmp;
    }

    public void setTvDEmp(String tvDEmp) {
        this.tvDEmp = tvDEmp;
    }

    public String getPromoTv() {
        return promoTv;
    }

    public void setPromoTv(String promoTv) {
        this.promoTv = promoTv;
    }

    public String getTiempoPromoTv() {
        return tiempoPromoTv;
    }

    public void setTiempoPromoTv(String tiempoPromoTv) {
        this.tiempoPromoTv = tiempoPromoTv;
    }

    public String[][] getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(String[][] adicionales) {
        this.adicionales = adicionales;
    }

    public String getAdInd() {
        return adInd;
    }

    public void setAdInd(String adInd) {
        this.adInd = adInd;
    }

    public String getAdEmp() {
        return adEmp;
    }

    public void setAdEmp(String adEmp) {
        this.adEmp = adEmp;
    }

    public void setTotalAdicionales(String total) {
        totalAdicionales = total;
    }

    public String getTotalAdicionales() {
        return totalAdicionales;
    }

    public String getTipoBa() {
        return tipoBa;
    }

    public void setTipoBa(String tipoBa) {
        this.tipoBa = tipoBa;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getBaInd() {
        return baInd;
    }

    public void setBaInd(String baInd) {
        this.baInd = baInd;
    }

    public String getBaEmp() {
        return baEmp;
    }

    public void setBaEmp(String baEmp) {
        this.baEmp = baEmp;
    }

    public String getBaDInd() {
        return baDInd;
    }

    public void setBaDInd(String baDInd) {
        this.baDInd = baDInd;
    }

    public String getBaDEmp() {
        return baDEmp;
    }

    public void setBaDEmp(String baDEmp) {
        this.baDEmp = baDEmp;
    }

    public String getPromoBa() {
        return promoBa;
    }

    public void setPromoBa(String promoBa) {
        this.promoBa = promoBa;
    }

    public String getTiempoPromoBa() {
        return tiempoPromoBa;
    }

    public void setTiempoPromoBa(String tiempoPromoBa) {
        this.tiempoPromoBa = tiempoPromoBa;
    }

    public String getIpdinamica() {
        return Ipdinamica;
    }

    public void setIpdinamica(String ipdinamica) {
        Ipdinamica = ipdinamica;
    }

    public String getInternet3g() {
        return internet3g;
    }

    public void setInternet3g(String internet3g) {
        this.internet3g = internet3g;
    }

    public String getInternet3gInd() {
        return internet3gInd;
    }

    public void setInternet3gInd(String internet3gInd) {
        this.internet3gInd = internet3gInd;
    }

    public String getInternet3gEmp() {
        return internet3gEmp;
    }

    public void setInternet3gEmp(String internet3gEmp) {
        this.internet3gEmp = internet3gEmp;
    }

    public String getInternet3gDInd() {
        return internet3gDInd;
    }

    public void setInternet3gDInd(String internet3gDInd) {
        this.internet3gDInd = internet3gDInd;
    }

    public String getInternet3gDEmp() {
        return internet3gDEmp;
    }

    public void setInternet3gDEmp(String internet3gDEmp) {
        this.internet3gDEmp = internet3gDEmp;
    }

    public String getPromo3g() {
        return promo3g;
    }

    public void setPromo3g(String promo3g) {
        this.promo3g = promo3g;
    }

    public String getInternet4g() {
        return internet4g;
    }

    public void setInternet4g(String internet4g) {
        this.internet4g = internet4g;
    }

    public String getInternet4gInd() {
        return internet4gInd;
    }

    public void setInternet4gInd(String internet4gInd) {
        this.internet4gInd = internet4gInd;
    }

    public String getInternet4gEmp() {
        return internet4gEmp;
    }

    public void setInternet4gEmp(String internet4gEmp) {
        this.internet4gEmp = internet4gEmp;
    }

    public String getInternet4gDInd() {
        return internet4gDInd;
    }

    public void setInternet4gDInd(String internet4gDInd) {
        this.internet4gDInd = internet4gDInd;
    }

    public String getInternet4gDEmp() {
        return internet4gDEmp;
    }

    public void setInternet4gDEmp(String internet4gDEmp) {
        this.internet4gDEmp = internet4gDEmp;
    }

    public String getPromo4g() {
        return promo4g;
    }

    public void setPromo4g(String promo4g) {
        this.promo4g = promo4g;
    }

    public String getTotalInd() {
        return totalInd;
    }

    public void setTotalInd(String totalInd) {
        this.totalInd = totalInd;
    }

    public String getTotalEmp() {
        return totalEmp;
    }

    public void setTotalEmp(String totalEmp) {
        this.totalEmp = totalEmp;
    }

    public String getTotalIndDescuento() {
        return totalIndDescuento;
    }

    public void setTotalIndDescuento(String totalIndDescuento) {
        this.totalIndDescuento = totalIndDescuento;
    }

    public String getTotalEmpDescuento() {
        return totalEmpDescuento;
    }

    public void setTotalEmpDescuento(String totalEmpDescuento) {
        this.totalEmpDescuento = totalEmpDescuento;
    }

    public String getContadorProductos() {
        return contadorProductos;
    }

    public void setContadorProductos(String contadorProductos) {
        this.contadorProductos = contadorProductos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isValidoIVR() {
        return validoIVR;
    }

    public void setValidoIVR(boolean validoIVR) {
        this.validoIVR = validoIVR;
    }

    public String getEstrato() {
        return estrato;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public ArrayList<ItemPromocionesAdicionales> getItemPromocionesAdicionales() {
        return itemPromocionesAdicionales;
    }

    public void setItemPromocionesAdicionales(ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales) {
        this.itemPromocionesAdicionales = itemPromocionesAdicionales;
    }

    public ArrayList<ItemPromocionesAdicionales> getItemPromocionesAdicionalesInternet() {
        return itemPromocionesAdicionalesInternet;
    }

    public void setItemPromocionesAdicionalesInternet(ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionalesInternet) {
        this.itemPromocionesAdicionalesInternet = itemPromocionesAdicionalesInternet;
    }

    public ArrayList<ItemDecodificador> getItemDecodificadores() {
        return itemDecodificadores;
    }

    public void setItemDecodificadores(ArrayList<ItemDecodificador> itemDecodificadores) {
        this.itemDecodificadores = itemDecodificadores;
    }

    public ArrayList<ItemDecodificador> getDecodificadores() {
        return decodificadores;
    }

    public void setDecodificadores(ArrayList<ItemDecodificador> decodificadores) {
        this.decodificadores = decodificadores;
    }

    public double getTotalDecos() {
        return totalDecos;
    }

    public void setTotalDecos(double totalDecos) {
        this.totalDecos = totalDecos;
    }

    public String getIdAsesoria() {
        return idAsesoria;
    }

    public void setIdAsesoria(String idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public boolean isAplicarAd() {
        return aplicarAd;
    }

    public void setAplicarAd(boolean aplicarAd) {
        this.aplicarAd = aplicarAd;
    }

    public String getPlanFacturacionTo_I() {
        return planFacturacionTo_I;
    }

    public void setPlanFacturacionTo_I(String planFacturacionTo_I) {
        this.planFacturacionTo_I = planFacturacionTo_I;
    }

    public String getPlanFacturacionTo_P() {
        return planFacturacionTo_P;
    }

    public void setPlanFacturacionTo_P(String planFacturacionTo_P) {
        this.planFacturacionTo_P = planFacturacionTo_P;
    }

    public String getPlanFacturacionTv_I() {
        return planFacturacionTv_I;
    }

    public void setPlanFacturacionTv_I(String planFacturacionTv_I) {
        this.planFacturacionTv_I = planFacturacionTv_I;
    }

    public String getPlanFacturacionTv_P() {
        return planFacturacionTv_P;
    }

    public void setPlanFacturacionTv_P(String planFacturacionTv_P) {
        this.planFacturacionTv_P = planFacturacionTv_P;
    }

    public String getPlanFacturacionBa_I() {
        return planFacturacionBa_I;
    }

    public void setPlanFacturacionBa_I(String planFacturacionBa_I) {
        this.planFacturacionBa_I = planFacturacionBa_I;
    }

    public String getPlanFacturacionBa_P() {
        return planFacturacionBa_P;
    }

    public void setPlanFacturacionBa_P(String planFacturacionBa_P) {
        this.planFacturacionBa_P = planFacturacionBa_P;
    }

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }

    public String getTipoCotizacionTo() {
        return tipoCotizacionTo;
    }

    public void setTipoCotizacionTo(String tipoCotizacionTo) {
        this.tipoCotizacionTo = tipoCotizacionTo;
    }

    public String getTipoCotizacionTv() {
        return tipoCotizacionTv;
    }

    public void setTipoCotizacionTv(String tipoCotizacionTv) {
        this.tipoCotizacionTv = tipoCotizacionTv;
    }

    public String getTipoCotizacionBa() {
        return tipoCotizacionBa;
    }

    public void setTipoCotizacionBa(String tipoCotizacionBa) {
        this.tipoCotizacionBa = tipoCotizacionBa;
    }

    public boolean isSegundaTelefonia() {
        return segundaTelefonia;
    }

    public void setSegundaTelefonia(boolean segundaTelefonia) {
        this.segundaTelefonia = segundaTelefonia;
    }

    public String getMedioIngreso() {
        return medioIngreso;
    }

    public void setMedioIngreso(String medioIngreso) {
        this.medioIngreso = medioIngreso;
    }

    public String[][] getAdicionalesTo() {
        return adicionalesTo;
    }

    public void setAdicionalesTo(String[][] adicionalesTo) {
        this.adicionalesTo = adicionalesTo;
    }

    public String getAdIndTo() {
        return adIndTo;
    }

    public void setAdIndTo(String adIndTo) {
        this.adIndTo = adIndTo;
    }

    public String getAdEmpTo() {
        return adEmpTo;
    }

    public void setAdEmpTo(String adEmpTo) {
        this.adEmpTo = adEmpTo;
    }

    public String getTotalAdicionalesTo() {
        return totalAdicionalesTo;
    }

    public void setTotalAdicionalesTo(String totalAdicionalesTo) {
        this.totalAdicionalesTo = totalAdicionalesTo;
    }

    public String[][] getAdicionalesBa() {
        return adicionalesBa;
    }

    public void setAdicionalesBa(String[][] adicionalesBa) {
        this.adicionalesBa = adicionalesBa;
    }

    public String getAdIndBa() {
        return adIndBa;
    }

    public void setAdIndBa(String adIndBa) {
        this.adIndBa = adIndBa;
    }

    public String getAdEmpBa() {
        return adEmpBa;
    }

    public void setAdEmpBa(String adEmpBa) {
        this.adEmpBa = adEmpBa;
    }

    public String getTotalAdicionalesBa() {
        return totalAdicionalesBa;
    }

    public void setTotalAdicionalesBa(String totalAdicionalesBa) {
        this.totalAdicionalesBa = totalAdicionalesBa;
    }

    public boolean isPromoTo_12_100() {
        return promoTo_12_100;
    }

    public void setPromoTo_12_100(boolean promoTo_12_100) {
        this.promoTo_12_100 = promoTo_12_100;
    }

    public boolean isControlGota() {
        return controlGota;
    }

    public void setControlGota(boolean controlGota) {
        this.controlGota = controlGota;
    }

    public int getPrecioGota() {
        return precioGota;
    }

    public void setPrecioGota(int precioGota) {
        this.precioGota = precioGota;
    }


    public int getPrecioGotaSinIva() {
        return precioGotaSinIva;
    }

    public void setPrecioGotaSinIva(int precioGotaSinIva) {
        this.precioGotaSinIva = precioGotaSinIva;
    }

    public String getVelocidadInicial() {
        return velocidadInicial;
    }

    public void setVelocidadInicial(String velocidadInicial) {
        this.velocidadInicial = velocidadInicial;
    }

    public String getVelocidadFinal() {
        return velocidadFinal;
    }

    public void setVelocidadFinal(String velocidadFinal) {
        this.velocidadFinal = velocidadFinal;
    }

    public String getNombreGota() {
        return nombreGota;
    }

    public void setNombreGota(String nombreGota) {
        this.nombreGota = nombreGota;
    }

    public void setDescuentosAd(boolean controlDescuentosAd, double preciosConDescuentoAd, double precioDescuentosAd) {
        this.controlDescuentosAd = controlDescuentosAd;
        this.preciosConDescuentoAd = preciosConDescuentoAd;
        this.precioDescuentosAd = precioDescuentosAd;
    }

    public boolean isCambiarCotizacion() {
        return cambiarCotizacion;
    }

    public void setCambiarCotizacion(boolean cambiarCotizacion) {
        this.cambiarCotizacion = cambiarCotizacion;
    }

    public boolean isHFCDigital() {
        return HFCDigital;
    }

    public void setHFCDigital(boolean hFCDigital) {
        HFCDigital = hFCDigital;
    }

    public String getTipoOferta() {
        return tipoOferta;
    }

    public void setTipoOferta(String tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public String getOfertaCotizacion() {
        return ofertaCotizacion;
    }

    public void setOfertaCotizacion(String ofertaCotizacion) {
        this.ofertaCotizacion = ofertaCotizacion;
    }

    public String getToPagoAntCargoFijo() {
        return toPagoAntCargoFijo;
    }

    public void setToPagoAntCargoFijo(String toPagoAntCargoFijo) {
        this.toPagoAntCargoFijo = toPagoAntCargoFijo;
    }

    public String getToPagoParcialConexion() {
        return toPagoParcialConexion;
    }

    public void setToPagoParcialConexion(String toPagoParcialConexion) {
        this.toPagoParcialConexion = toPagoParcialConexion;
    }

    public String getTvPagoAntCargoFijo() {
        return tvPagoAntCargoFijo;
    }

    public void setTvPagoAntCargoFijo(String tvPagoAntCargoFijo) {
        this.tvPagoAntCargoFijo = tvPagoAntCargoFijo;
    }

    public String getTvPagoParcialConexion() {
        return tvPagoParcialConexion;
    }

    public void setTvPagoParcialConexion(String tvPagoParcialConexion) {
        this.tvPagoParcialConexion = tvPagoParcialConexion;
    }

    public String getBaPagoAntCargoFijo() {
        return baPagoAntCargoFijo;
    }

    public void setBaPagoAntCargoFijo(String baPagoAntCargoFijo) {
        this.baPagoAntCargoFijo = baPagoAntCargoFijo;
    }

    public String getBaPagoParcialConexion() {
        return baPagoParcialConexion;
    }

    public void setBaPagoParcialConexion(String baPagoParcialConexion) {
        this.baPagoParcialConexion = baPagoParcialConexion;
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

    public Decodificadores getObjectDecodificador() {
        return objectDecodificador;
    }

    public void setObjectDecodificador(Decodificadores objectDecodificador) {
        this.objectDecodificador = objectDecodificador;
    }

    public boolean isRetiroCrackleBa() {
        return retiroCrackleBa;
    }

    public void setRetiroCrackleBa(boolean retiroCrackleBa) {
        this.retiroCrackleBa = retiroCrackleBa;
    }

    public boolean isRetiroHBOGO() {
        return retiroHBOGO;
    }

    public void setRetiroHBOGO(boolean retiroHBOGO) {
        this.retiroHBOGO = retiroHBOGO;
    }
}