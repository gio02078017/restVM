package co.com.une.appmovilesune.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.kobjects.util.Util;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemDependencias;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ItemTarificador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.complements.Calendario;

public class TarificadorNew {

    private String departamento, ciudad, estrato;

    private String telefonia, television, internet;
    private String planTo, planTv, planBa;

    private double TO_I = 0, TO_I_IVA = 0, TO_P = 0, TO_P_IVA = 0, TO_I_D = 0, TO_I_IVA_D = 0, TO_P_D = 0,
            TO_P_IVA_D = 0, TO_PP = 0;
    private double BA_I = 0, BA_I_IVA = 0, BA_P = 0, BA_P_IVA = 0, BA_I_D = 0, BA_I_IVA_D = 0, BA_P_D = 0,
            BA_P_IVA_D = 0, BA_PP = 0;
    private double TV_I = 0, TV_I_IVA = 0, TV_P = 0, TV_P_IVA = 0, TV_I_D = 0, TV_I_IVA_D = 0, TV_P_D = 0,
            TV_P_IVA_D = 0;

    private double valorGotaSinIva = 0, valorGota = 0;

    private double TT_I = 0, TT_I_IVA = 0, TT_P = 0, TT_P_IVA = 0, TT_I_TP_IVA = 0, TT_P_TP_IVA = 0;

    private String dependenciaTO, dependenciaTV, dependenciaBA;
    private int cantidadTO, cantidadTV, cantidadBA;
    private int contadorFija = 0;
    private boolean dependencia = false;
    private boolean control = true;
    private boolean controlGota = false;
    private boolean exception = true;
    private String Oferta = "";

    private String velocidadInicial = "", velocidadFinal = "";

    private String planFacturacionTO_I, planFacturacionTV_I, planFacturacionBA_I;
    private String planFacturacionTO_P, planFacturacionTV_P, planFacturacionBA_P;

    private double totalDecodificadores;

    private ArrayList<ItemDependencias> itemDependencias = new ArrayList<ItemDependencias>();

    public static ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();

    private double TT_P_TP_IVA_Sin_Demanda = 0, TT_I_TP_IVA_Sin_Demanda = 0;

    private double precio_ad = 0;
    private String[][] Descripcion_Ad;
    private double Precio_Ad_Tv_IVA = 0;
    private int Contador = 0;
    private int Contador_productos = 0;
    private Context context;
    private boolean nuevo;
    private String ciudadCliente;

    private String jsonDatos;

    private String oferta = "";


    public TarificadorNew() {

    }

    public void
    Consulta_Tarifaz(String planTo, String telefonia, String planTv, String television, String planBa, String internet, String[][] descripcion_Ad, double precio_ad, String estrato, boolean nuevo,
                     Cliente cliente, String jsonDatos, Context context, double totalDecodificadores, String oferta) {

        this.estrato = estrato;
        this.telefonia = telefonia;
        this.television = television;
        this.internet = internet;

        this.planTo = planTo;
        this.planTv = planTv;
        this.planBa = planBa;

        this.nuevo = nuevo;
        this.ciudadCliente = cliente.getCiudad();
        this.jsonDatos = jsonDatos;

        this.precio_ad = precio_ad;
        this.Descripcion_Ad = descripcion_Ad;
        this.context = context;
        dependencia = false;
        exception = true;
        control = false;
        controlGota = false;
        velocidadInicial = "";
        velocidadFinal = "";
        itemDependencias.clear();

        this.totalDecodificadores = totalDecodificadores;

        this.oferta = oferta;

        // System.out.println("desTelefonia " + desTelefonia);

        System.out.println("Consulta_Tarifaz telefonia " + telefonia + " plan " + planTo);
        System.out.println("Consulta_Tarifaz television " + television + " plan " + planTv);
        System.out.println("Consulta_Tarifaz internet " + internet + " plan " + planTv);
        System.out.println("Consulta_Tarifaz json " + jsonDatos);
        System.out.println("Consulta_Tarifaz  oferta " + oferta);

        limpiar();

        Tarifas_Telefonia(telefonia);
        Tarifas_Television(television);
        Tarifas_Internet(internet);
        Total();
    }

    // metodo encargado de la cotizacion de Telefonia
    public void Tarifas_Telefonia(String Telefonia) {
        if (Telefonia != null) {
            if (!Telefonia.equalsIgnoreCase(Utilidades.inicial) && !Telefonia.equalsIgnoreCase(Utilidades.inicial_guion)) {
                Consulta(Telefonia, Utilidades.tipo_producto_to);
            } else {
                telefonia = "-";
            }
        } else {
            telefonia = "-";
        }

    }

    // metodo encargado de la cotizacion de Television
    public void Tarifas_Television(String Television) {
        if (Television != null) {
            if (!Television.equalsIgnoreCase(Utilidades.inicial) && !Television.equalsIgnoreCase(Utilidades.inicial_guion)) {
                Consulta(Television, Utilidades.tipo_producto_tv);
            } else {
                television = "-";
            }
        } else {
            television = "-";
        }

    }

    // metodo encargado de la cotizacion de Internet
    public void Tarifas_Internet(String Internet) {
        if (Internet != null) {
            if (!Internet.equalsIgnoreCase(Utilidades.inicial) && !Internet.equalsIgnoreCase(Utilidades.inicial_guion)) {
                Consulta(Internet, Utilidades.tipo_producto_ba);
            } else {
                internet = "-";
            }
        } else {
            internet = "-";
        }

    }

    // metodo que consulta las tarifas en la DB
    public void Consulta(String producto, String tipo_producto) {
        int Individual = 0, Individual_Iva = 0, Empaquetado = 0, Empaquetado_Iva = 0;
        int Contador_Paquete = 0;
        String paquete = "";

        Contador_Paquete = Contador_Productos();
        if (Contador_Paquete == 1 || Utilidades.validarNacionalValor("ofertaIndividual", oferta)) {
            paquete = "Ind";
        } else if (Contador_Paquete == 2) {
            paquete = "Duo";
        } else if (Contador_Paquete > 2) {
            paquete = "Trio";
        }

        String depto = "";
        if (MainActivity.config.getDepartamento().equalsIgnoreCase("Distrito Capital de Bogota")) {
            depto = ciudadCliente;
        } else {
            depto = MainActivity.config.getDepartamento();
        }

        try {
            if (producto.contains("Existente")) {
                Precios("0", "0", "0", "0", "", "0", tipo_producto, "", "", "", "", "", "");
            } else {

               /* ArrayList<String> precios = MainActivity.basedatos.consultar(false, "Precios",
                        new String[]{"individual", "individual_iva", "empaquetado", "empaquetado_iva", "Oferta",
                                "cantidadproductos", "homoPrimeraLinea", "homoSegundaLinea", "valorGota",
                                "valorGotaIva", "ProductoHomologado", "velocidadGota"},
                        "departamento=? and tipo_producto=? and Producto=? and estrato like ? and tipo_paquete Like ?",
                        new String[]{depto, tipo_producto, producto, "%" + estrato + "%", "%" +  + "%"}, null,
                        null, null).get(0);*/

                String datos = "individual, individual_iva,empaquetado,empaquetado_iva,Oferta,cantidadproductos,homoPrimeraLinea," +
                        "homoSegundaLinea,valorGota,valorGotaIva,ProductoHomologado,velocidadGota";

                String query = "select " + datos + " from Precios p " +
                        UtilidadesTarificadorNew.innerJoinTarifas +
                        " where cxt.id_condicion in (" + UtilidadesTarificadorNew.queryInternoTarifas(depto, ciudadCliente) + ")" +
                        " and estrato like '%" + estrato + "%' and Tipo_Producto = '" + tipo_producto + "' and producto ='" + producto + "' and tipo_paquete like '%" + paquete + "%'";

                System.out.println("query consulta precios " + query);

                ArrayList<String> precios = MainActivity.basedatos.consultar2(query).get(0);


                System.out.println("consultaNT precios " + precios);
                System.out.println("consultaNT tipoProducto " + tipo_producto);
                System.out.println("consultaNT producto " + producto);

                Precios(precios.get(0), precios.get(1), precios.get(2), precios.get(3), precios.get(4), precios.get(5),
                        tipo_producto, precios.get(6), precios.get(7), precios.get(8), precios.get(9), precios.get(10),
                        precios.get(11));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Error ", "Muesta por aca " + e.getMessage());
            limpiar();
            dependencia = true;
            exception = false;
            // System.out.println("tipo producto " + tipo_producto);
        }

    }

    public int ContadoOfertas(int fija) {
        int oferta = 0;
        int tamano = Descripcion_Ad.length;

        for (int i = 0; i < tamano; i++) {
            String adicional = Descripcion_Ad[i][0];
            System.out.println("adicional " + adicional);
            String Oferta = Utilidades.OfertaProductos(adicional, "adic");
            System.out.println("Oferta " + Oferta);
            if (!Oferta.equalsIgnoreCase("")) {
                oferta++;
                itemDependencias.add(new ItemDependencias(Oferta, fija, "ADIC"));
            }

        }

        return oferta;
    }

    // metodo encargado de contar las cantidad de productos seleccionados
    public int Contador_Productos() {
        int Contador = 0;
        Contador_productos = 0;

        if (!telefonia.equalsIgnoreCase(Utilidades.inicial) && !telefonia.equalsIgnoreCase(Utilidades.inicial_guion)) {
            Contador_productos++;
        }

        if (!television.equalsIgnoreCase(Utilidades.inicial)
                && !television.equalsIgnoreCase(Utilidades.inicial_guion)) {
            Contador_productos++;
        }


        if (!internet.equalsIgnoreCase(Utilidades.inicial) && !internet.equalsIgnoreCase(Utilidades.inicial_guion)) {
            Contador_productos++;
        }


        Contador = Contador_productos;

        return Contador;
    }

    // metodo encargado almacenar las tarifas de acuerdo al producto
    // seleccionado
    public void Precios(String individual, String individual_Iva, String empaquetado, String empaquetado_Iva,
                        String dependencias, String CantidadProductos, String Tipo, String homoPrimeraLinea,
                        String homoSegundaLinea, String dataGotaSinIva, String dataGota, String inicialGota, String finalGota) {

        int Individual = 0, Individual_Iva = 0, Empaquetado = 0, Empaquetado_Iva = 0, Cantidad = 0;

        System.out.println("dependencias Precios " + dependencias);
        System.out.println("dataGota " + dataGota);

        try {
            Individual = Integer.parseInt(individual);
            Individual_Iva = Integer.parseInt(individual_Iva);
            Empaquetado = Integer.parseInt(empaquetado);
            Empaquetado_Iva = Integer.parseInt(empaquetado_Iva);
            Cantidad = Integer.parseInt(CantidadProductos);
        } catch (NumberFormatException e) {

            Log.w("Error NumberFormatException", "Mensaje " + e.getMessage());
        }

        if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_to)) {
            TO_I = Individual;
            TO_I_IVA = Individual_Iva;
            TO_P = Empaquetado;
            TO_P_IVA = Empaquetado_Iva;
            dependenciaTO = dependencias;
            planFacturacionTO_P = homoPrimeraLinea;
            planFacturacionTO_I = homoSegundaLinea;

            if (!dependenciaTO.equalsIgnoreCase("")) {
                dependencia = true;
            }

            cantidadTO = Cantidad;
            itemDependencias.add(new ItemDependencias(dependencias, Cantidad, "TO"));
            contadorFija++;
        }

        if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_tv)) {
            TV_I = Individual;
            TV_I_IVA = Individual_Iva;
            TV_P = Empaquetado;
            TV_P_IVA = Empaquetado_Iva;
            dependenciaTV = dependencias;
            planFacturacionTV_P = homoPrimeraLinea;
            planFacturacionTV_I = homoSegundaLinea;
            if (!dependenciaTV.equalsIgnoreCase("")) {
                dependencia = true;
            }

            cantidadTV = Cantidad;
            itemDependencias.add(new ItemDependencias(dependencias, Cantidad, "TV"));
            contadorFija++;
        }

        if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_ba)) {
            BA_I = Individual;
            BA_I_IVA = Individual_Iva;
            BA_P = Empaquetado;
            BA_P_IVA = Empaquetado_Iva;
            dependenciaBA = dependencias;
            planFacturacionBA_P = homoPrimeraLinea;
            planFacturacionBA_I = homoSegundaLinea;
            if (!dependenciaBA.equalsIgnoreCase("")) {
                dependencia = true;
            }
            cantidadBA = Cantidad;
            itemDependencias.add(new ItemDependencias(dependencias, Cantidad, "BA"));
            contadorFija++;

            if (!dataGota.equalsIgnoreCase("") && !dataGota.equalsIgnoreCase("N/A")
                    && !dataGota.equalsIgnoreCase("null")) {
                controlGota = true;
                valorGotaSinIva = Integer.parseInt(dataGotaSinIva);
                valorGota = Integer.parseInt(dataGota);
                velocidadInicial = inicialGota;
                velocidadFinal = finalGota;
                System.out.println("GOTA" + valorGota);
                System.out.println("GOTA velocidadInicial" + velocidadInicial);
                System.out.println("GOTA velocidadFinal" + velocidadFinal);

            }
        }
    }

    public void Descuentos(String producto, String descuento, double individual, double individual_iva,
                           double empaquetado, double empaquetado_iva, String estrato, String tipo_producto) {

        double Individual = 0, Individual_Iva = 0, Empaquetado = 0, Empaquetado_Iva = 0;

        if (descuento.equalsIgnoreCase("100%")) {

            setAsignar_Descuentos(0, 0, 0, 0, tipo_producto, descuento);

        } else if (descuento.contains("%")) {

            double porcentajedescuento = 0;

            try {
                porcentajedescuento = Double.parseDouble(descuento.replaceAll("%", ""));
            } catch (NumberFormatException e) {
                Log.w("Error NumberFormatException", "Mensaje " + e.getMessage());
                porcentajedescuento = 0;
            }

            if (porcentajedescuento != 0) {
                porcentajedescuento = (100 - porcentajedescuento) / 100;

                Individual = (int) Math.floor(individual * (porcentajedescuento));
                Individual_Iva = (int) Math.floor(individual_iva * (porcentajedescuento));
                Empaquetado = (int) Math.floor(empaquetado * (porcentajedescuento));
                Empaquetado_Iva = (int) Math.floor(empaquetado_iva * (porcentajedescuento));
                setAsignar_Descuentos(Individual, Individual_Iva, Empaquetado, Empaquetado_Iva, tipo_producto,
                        descuento);

            } else {
                setAsignar_Descuentos(Individual, Individual_Iva, Empaquetado, Empaquetado_Iva, tipo_producto,
                        descuento);

            }

        } else {

            String control = descuento;

            try {

                ArrayList<String> descuentos = MainActivity.basedatos.consultar(false, "Descuentos",
                        new String[]{"Individual", "Individual_Iva", "Empaquetado", "Empaquetado_Iva"},
                        "Tipo_Producto=? and Estrato Like ? and Producto Like ? and Descuentos=? and Ciudad Like ? ",
                        new String[]{tipo_producto, "%" + estrato + "%", "%" + producto + "%", descuento,
                                "%" + ciudadCliente + "%"},
                        null, null, null).get(0);

                individual = Integer.parseInt(descuentos.get(0));
                individual_iva = Integer.parseInt(descuentos.get(1));
                empaquetado = Integer.parseInt(descuentos.get(2));
                empaquetado_iva = Integer.parseInt(descuentos.get(3));

            } catch (NumberFormatException e) {

                Log.w("Error NumberFormatException", "Mensaje " + e.getMessage());
                control = "N/A";
            } catch (Exception e) {

                Log.w("Error NumberFormatException", "Mensaje " + e.getMessage());
                control = "N/A";
            }

            setAsignar_Descuentos(individual, individual_iva, empaquetado, empaquetado_iva, tipo_producto, control);

        }
    }

    public void setAsignar_Descuentos(double individual, double individual_iva, double empaquetado,
                                      double empaquetado_iva, String tipo_producto, String control) {

        if (tipo_producto.equalsIgnoreCase(Utilidades.tipo_producto_to_d)) {
            TO_I_D = individual;
            TO_I_IVA_D = individual_iva;
            TO_P_D = empaquetado;
            TO_P_IVA_D = empaquetado_iva;

        } else if (tipo_producto.equalsIgnoreCase(Utilidades.tipo_producto_tv_d)) {
            TV_I_D = individual;
            TV_I_IVA_D = individual_iva;
            TV_P_D = empaquetado;
            TV_P_IVA_D = empaquetado_iva;
        } else if (tipo_producto.equalsIgnoreCase(Utilidades.tipo_producto_ba_d)) {
            BA_I_D = individual;
            BA_I_IVA_D = individual_iva;
            BA_P_D = empaquetado;
            BA_P_IVA_D = empaquetado_iva;
        }
    }

    public static ArrayList<ArrayList<String>> Descuentos(String planTo, String telefonia, String planTv,
                                                          String television, String planBa, String internet) {
        int count = 0;
        String to = "";
        String ba = "";
        String tv = "";
        String query = "";
        ArrayList<ArrayList<String>> promociones = null;

        if (!planTo.equalsIgnoreCase(Utilidades.inicial_plan) && !telefonia.equalsIgnoreCase(Utilidades.inicial)) {
            String planNumericoTo = Utilidades.planNumerico(planTo);
            // System.out.println("planNumericoTo " + planNumericoTo);
            if (planNumericoTo.equalsIgnoreCase("3")) {
                to = "(r2.tipo_producto = 'to' AND r2.nuevo = '0')";
            } else {
                to = "(r2.tipo_producto = 'to' AND (r2.plan LIKE '%" + telefonia
                        + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoTo + "')";
            }
            count++;
        }

        if (planTv != null && television != null) {
            if (!planTv.equalsIgnoreCase(Utilidades.inicial_plan) && !television.equalsIgnoreCase(Utilidades.inicial)) {
                String planNumericoTv = Utilidades.planNumerico(planTv);
                // System.out.println("planNumericoTv " + planNumericoTv);
                if (count == 0) {
                    if (planNumericoTv.equalsIgnoreCase("3")) {
                        tv = "(r2.tipo_producto = 'tv' AND r2.nuevo = '0')";
                    } else {
                        tv = "(r2.tipo_producto = 'tv' AND ( r2.plan LIKE '%" + television
                                + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoTv + "')";
                    }
                } else {
                    if (planNumericoTv.equalsIgnoreCase("3")) {
                        tv = "OR (r2.tipo_producto = 'tv' AND r2.nuevo = '0')";
                    } else {
                        tv = " OR (r2.tipo_producto = 'tv' AND (r2.plan LIKE '%" + television
                                + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoTv + "')";
                    }
                }
                count++;
            }
        }

        if (planBa != null && internet != null && !planBa.equalsIgnoreCase(Utilidades.inicial_plan)
                && !internet.equalsIgnoreCase(Utilidades.inicial)) {
            String planNumericoBa = Utilidades.planNumerico(planBa);
            // System.out.println("planNumericoBa " + planNumericoBa);
            if (count == 0) {
                if (planNumericoBa.equalsIgnoreCase("3")) {
                    ba = "(r2.tipo_producto = 'ba' AND r2.nuevo = '0')";
                } else {
                    ba = "(r2.tipo_producto = 'ba' AND (r2.plan LIKE '%" + internet
                            + "%' OR r2.plan = '')AND r2.nuevo = '" + planNumericoBa + "')";
                }
            } else {
                if (planNumericoBa.equalsIgnoreCase("3")) {
                    ba = "OR (r2.tipo_producto = 'ba' AND r2.nuevo = '0')";
                } else {
                    ba = "OR (r2.tipo_producto = 'ba' AND (r2.plan LIKE '%" + internet
                            + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoBa + "')";
                }
            }
            count++;
        }

        if (count > 0) {
            query = "SELECT r1.tipo_producto,r1.descuento,r1.meses FROM reglas r1 "
                    + "JOIN (SELECT r2.regla as regla,COUNT(1) AS conteo,r2.cantidad  " + "FROM reglas r2  WHERE (" + to
                    + " " + tv + " " + ba
                    + ") GROUP BY r2.regla   HAVING conteo = r2.cantidad  ORDER BY 2 DESC LIMIT 1) "
                    + "AS tbl ON tbl.regla = r1.regla  WHERE r1.cantidad='" + count + "'";
            System.out.println("query descuentos " + query);
            promociones = MainActivity.basedatos.consultarDescuentos(query);

        }

        System.out.println("query promociones " + query);

        System.out.println("query promociones " + promociones);

        return promociones;

    }

    public static ArrayList<ArrayList<String>> Descuentos2(String planTo, String telefonia, String planTv,
                                                           String television, String planBa, String internet, String jsonDatos) {
        int count = 0;
        String to = "";
        String ba = "";
        String tv = "";
        String query = "";

        ArrayList<ArrayList<String>> promociones2 = null;

        System.out.println("Descuentos2 planTo " + planTo);
        System.out.println("Descuentos2 telefonia " + telefonia);
        System.out.println("Descuentos2 planTv " + planTv);
        System.out.println("Descuentos2 television " + television);
        System.out.println("Descuentos2 planTv " + planBa);
        System.out.println("Descuentos2 television " + internet);
        System.out.println("Descuentos2 jsonDatos " + jsonDatos);


        if (planTv != null && television != null) {
            if (!planTo.equalsIgnoreCase(Utilidades.inicial_plan) && !planTo.equalsIgnoreCase(Utilidades.inicial_guion) && !telefonia.equalsIgnoreCase(Utilidades.inicial) && !telefonia.equalsIgnoreCase(Utilidades.inicial_guion)) {
                String planNumericoTo = Utilidades.planNumerico(planTo);
                // System.out.println("planNumericoTo " + planNumericoTo);
                if (planNumericoTo.equalsIgnoreCase("3")) {
                    to = "(r2.tipo_producto = 'to' AND r2.nuevo = '0')";
                } else {
                    to = "(r2.tipo_producto = 'to' AND (r2.plan LIKE '%" + telefonia
                            + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoTo + "')";
                }
                count++;
            }
        }

        if (planTv != null && television != null) {
            if (!planTv.equalsIgnoreCase(Utilidades.inicial_plan) && !planTv.equalsIgnoreCase(Utilidades.inicial_guion) && !television.equalsIgnoreCase(Utilidades.inicial) && !television.equalsIgnoreCase(Utilidades.inicial_guion)) {
                String planNumericoTv = Utilidades.planNumerico(planTv);
                // System.out.println("planNumericoTv " + planNumericoTv);
                if (count == 0) {
                    if (planNumericoTv.equalsIgnoreCase("3")) {
                        tv = "(r2.tipo_producto = 'tv' AND r2.nuevo = '0')";
                    } else {
                        tv = "(r2.tipo_producto = 'tv' AND ( r2.plan LIKE '%" + television
                                + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoTv + "')";
                    }
                } else {
                    if (planNumericoTv.equalsIgnoreCase("3")) {
                        tv = "OR (r2.tipo_producto = 'tv' AND r2.nuevo = '0')";
                    } else {
                        tv = " OR (r2.tipo_producto = 'tv' AND (r2.plan LIKE '%" + television
                                + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoTv + "')";
                    }
                }
                count++;
            }
        }

        if (planBa != null && internet != null && !planBa.equalsIgnoreCase(Utilidades.inicial_plan)
                && !planBa.equalsIgnoreCase(Utilidades.inicial_guion) && !internet.equalsIgnoreCase(Utilidades.inicial)
                && !internet.equalsIgnoreCase(Utilidades.inicial_guion)) {
            String planNumericoBa = Utilidades.planNumerico(planBa);
            // System.out.println("planNumericoBa " + planNumericoBa);
            if (count == 0) {
                if (planNumericoBa.equalsIgnoreCase("3")) {
                    ba = "(r2.tipo_producto = 'ba' AND r2.nuevo = '0')";
                } else {
                    ba = "(r2.tipo_producto = 'ba' AND (r2.plan LIKE '%" + internet
                            + "%' OR r2.plan = '')AND r2.nuevo = '" + planNumericoBa + "')";
                }
            } else {
                if (planNumericoBa.equalsIgnoreCase("3")) {
                    ba = "OR (r2.tipo_producto = 'ba' AND r2.nuevo = '0')";
                } else {
                    ba = "OR (r2.tipo_producto = 'ba' AND (r2.plan LIKE '%" + internet
                            + "%' OR r2.plan = '') AND r2.nuevo = '" + planNumericoBa + "')";
                }
            }
            count++;
        }

        if (count > 0) {
            query = "SELECT DISTINCT (r1.regla)  FROM reglas r1 "
                    + "JOIN (SELECT r2.regla as regla,COUNT(*) AS conteo,r2.cantidad  " + "FROM reglas r2  WHERE (" + to
                    + " " + tv + " " + ba + ") GROUP BY r2.regla   HAVING conteo = r2.cantidad) "
                    + "AS tbl ON tbl.regla = r1.regla  WHERE r1.cantidad='" + count + "'";
            System.out.println("query descuentos 2 " + query);

            query = Utilidades.queryExternoPromociones(query, jsonDatos);

            System.out.println("query descuentos 2 completo" + query);
            promociones2 = MainActivity.basedatos.consultarDescuentos(query);

        }

        System.out.println("query promociones 2 " + query);

        System.out.println("query ppromociones2 arraylist " + promociones2);

        return promociones2;

    }

    public static ArrayList<ArrayList<String>> consultarDescuentos(String[][] adicionales, String ciudad, boolean nuevo,
                                                                   int contadorFija) {

        ArrayList<ArrayList<String>> promociones = null;
        int isNuevo = 0;

        String depto = "";
        if (MainActivity.config.getDepartamento().equalsIgnoreCase("Distrito Capital de Bogota")) {
            depto = ciudad;
        } else {
            depto = MainActivity.config.getDepartamento();
        }

        String adicionalesCadena = "";
        int cantidad = adicionales.length;
        for (int i = 0; i < cantidad; i++) {
            if (i == 0 && cantidad > 1) {
                adicionalesCadena += "'" + adicionales[i][0] + "',";
            } else if (i == (adicionales.length - 1)) {
                adicionalesCadena += "'" + adicionales[i][0] + "'";
            } else {
                adicionalesCadena += "'" + adicionales[i][0] + "',";
            }
        }

        if (nuevo) {
            isNuevo = 1;
        } else {
            isNuevo = 0;
        }

        String query = "SELECT  adicional, SUM(Descuento) AS promocion,SUM(Meses) AS meses "
                + "FROM( SELECT adicional,tarifa, tarifaiva,'' AS Descuento,'' AS Meses "
                + "FROM Adicionales WHERE (departamento LIKE '" + depto + "' OR departamento = ''"
                + "OR departamento IS NULL) AND tipoProducto = 'tv' " + "AND adicional IN (" + adicionalesCadena + ") "
                + "GROUP BY adicional " + "UNION " + "SELECT Plan,'','',Descuento,Meses FROM reglas "
                + "WHERE Tipo_Producto = 'adic_tv' " + "AND Plan IN (" + adicionalesCadena + ") "
                + "AND (Cantidad LIKE '%" + contadorFija + "%' OR Cantidad IS NULL OR Cantidad ='') " + "AND (Nuevo = '"
                + isNuevo + "' OR Nuevo = '' OR Nuevo IS NULL) " + "GROUP BY Plan) AS tbl GROUP BY adicional";
        System.out.println("query adicionales " + query);
        promociones = MainActivity.basedatos.consultarDescuentos(query);

        return promociones;

    }

    public static ArrayList<ArrayList<String>> consultarDescuentos2(String[][] adicionales, String ciudad,
                                                                    boolean nuevo, int contadorFija, String jsonDatos) {

        ArrayList<ArrayList<String>> promociones = null;
        int isNuevo = 0;

        String adicionalesCadena = "";
        int cantidad = adicionales.length;
        for (int i = 0; i < cantidad; i++) {
            if (i == 0 && cantidad > 1) {
                adicionalesCadena += "'" + adicionales[i][0] + "',";
            } else if (i == (adicionales.length - 1)) {
                adicionalesCadena += "'" + adicionales[i][0] + "'";
            } else {
                adicionalesCadena += "'" + adicionales[i][0] + "',";
            }
        }

        if (nuevo) {
            isNuevo = 1;
        } else {
            isNuevo = 0;
        }

        String query = "SELECT DISTINCT (regla) FROM reglas " + "WHERE Tipo_Producto = 'adic_tv' " + "   AND Plan IN ("
                + adicionalesCadena + ")" + "   AND (Cantidad LIKE '%3%' OR Cantidad IS NULL OR Cantidad ='')"
                + "   AND (Nuevo = '1' OR Nuevo = '' OR Nuevo IS NULL)";

        query = Utilidades.queryExternoPromocionesAdicionales(query, jsonDatos);

        System.out.println("query Adicionales 2" + query);

        promociones = MainActivity.basedatos.consultarDescuentos(query);

        System.out.println("promociones 2 Adicionales " + promociones);

        return promociones;

    }

    // metodo encargado de obtener los totales de la cotizacion
    public void Total() {

        int cantidad = 0;

        String controlDependencia = "";
        itemPromocionesAdicionales.clear();

        System.out.println("exception " + exception);

        if (exception) {
            control = true;
        }

        System.out.println("control " + control);

        System.out.println("Dependencias " + dependencia);

        if (dependencia) {

            System.out.println("hola itemDependencias.sise() " + itemDependencias.size());

            if (itemDependencias.size() > 0) {
                contadorFija += ContadoOfertas(itemDependencias.get(0).getCantidad());
            }

            System.out.println("contadorFija " + contadorFija + " itemDependencias.size() " + itemDependencias.size());

            if (itemDependencias.size() == contadorFija) {
                for (int i = 0; i < itemDependencias.size(); i++) {

                    if (itemDependencias.get(i).getCantidad() != contadorFija) {
                        control = false;
                        System.out.println("Salida por cantidad");
                        break;
                    }

                    if (i == 0) {
                        controlDependencia = itemDependencias.get(i).getDependencia();
                    } else {
                        if (!controlDependencia.equalsIgnoreCase(itemDependencias.get(i).getDependencia())) {
                            control = false;
                            System.out.println("Dependencia");
                            break;
                        }
                    }
                }

                if (Utilidades.validarNacionalValor("ofertaIndividual", oferta)) {
                    control = true;
                }
            } else {
                if (Utilidades.validarNacionalValor("ofertaIndividual", oferta)) {
                    control = true;
                } else {
                    control = false;
                    System.out.println("Salida por comparacion");
                }

            }
        }

        System.out.println("control " + control + "dependencias " + dependencia);

        if (control && dependencia) {
            Oferta = controlDependencia;
        }

        if (control) {

            if (controlGota) {
                precio_ad = precio_ad + valorGota + totalDecodificadores;
                Precio_Ad_Tv_IVA = precio_ad + totalDecodificadores;
            } else {
                Precio_Ad_Tv_IVA = precio_ad + totalDecodificadores;
            }

            TT_I = TO_I_D + TV_I_D + BA_I_D + precio_ad;
            TT_I_IVA = TO_I_IVA_D + TV_I_IVA_D + BA_I_IVA_D + Precio_Ad_Tv_IVA;

            TT_I_TP_IVA = TO_I_IVA + TV_I_IVA + BA_I_IVA + Precio_Ad_Tv_IVA;
            TT_P = TO_P_D + TV_P_D + BA_P_D + precio_ad;

            TT_P_IVA = TO_P_IVA_D + TV_P_IVA_D + BA_P_IVA_D + Precio_Ad_Tv_IVA;

            TT_P_TP_IVA = TO_P_IVA + TV_P_IVA + BA_P_IVA + Precio_Ad_Tv_IVA;

        } else {
            // System.out.println("Error Cotizacion Invalida");
            limpiar();
        }

        registro_tarificador();
        // consultarDescuentos();
        // System.out.println(" descuentosAdicionales "+consultarDescuentos);
        // if (consultarDescuentos() != null) {
        if (consultarDescuentos2() != null) {
            // ArrayList<ArrayList<String>> descuentosAdicionales =
            // consultarDescuentos();
            ArrayList<ArrayList<String>> descuentosAdicionales = consultarDescuentos2();

            for (int i = 0; i < descuentosAdicionales.size(); i++) {

                String adicional = descuentosAdicionales.get(i).get(0);
                String descuento = descuentosAdicionales.get(i).get(1);
                String meses = descuentosAdicionales.get(i).get(2);
                System.out.println("descuentosAdicionales " + descuentosAdicionales.get(i).get(0) + " Descuento "
                        + descuentosAdicionales.get(i).get(1) + "Tiempo " + descuentosAdicionales.get(i).get(2));

                if (meses.equalsIgnoreCase("1")) {
                    meses = meses + " Mes";
                } else {
                    meses = meses + " Meses";
                }

                itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(adicional, descuento, "TV", meses));
            }

			/*
             * System.out.println("itemPromocionesAdicionales " +
			 * itemPromocionesAdicionales);
			 */

            for (int i = 0; i < itemPromocionesAdicionales.size(); i++) {
                /*
                 * System.out.println("adicional " +
				 * itemPromocionesAdicionales.get(i).getAdicional() +
				 * " descuento " +
				 * itemPromocionesAdicionales.get(i).getDescuento() + " meses "
				 * + itemPromocionesAdicionales.get(i).getMeses());
				 */
            }
        } else {
            // ArrayList<ArrayList<String>> descuentosAdicionales = new
            // ArrayList<ArrayList<String>>();
            itemPromocionesAdicionales.clear();
            itemPromocionesAdicionales.add(0, new ItemPromocionesAdicionales("Vacia"));
        }

    }

    public ArrayList<ArrayList<String>> consultarDescuentos() {

        ArrayList<ArrayList<String>> promociones = null;
        int isNuevo = 0;

        String depto = "";
        if (MainActivity.config.getDepartamento().equalsIgnoreCase("Distrito Capital de Bogota")) {
            depto = ciudadCliente;
        } else {
            depto = MainActivity.config.getDepartamento();
        }

        String adicionalesCadena = "";
        int cantidad = Descripcion_Ad.length;
        for (int i = 0; i < cantidad; i++) {
            if (i == 0 && cantidad > 1) {
                adicionalesCadena += "'" + Descripcion_Ad[i][0] + "',";
            } else if (i == (Descripcion_Ad.length - 1)) {
                adicionalesCadena += "'" + Descripcion_Ad[i][0] + "'";
            } else {
                adicionalesCadena += "'" + Descripcion_Ad[i][0] + "',";
            }
        }

        if (nuevo) {
            isNuevo = 1;
        } else {
            isNuevo = 0;
        }

        String query = "SELECT  adicional, SUM(Descuento) AS promocion,SUM(Meses) AS meses  "
                + "FROM( SELECT adicional,tarifa, tarifaiva,'' AS Descuento,'' AS Meses "
                + "FROM Adicionales WHERE (departamento LIKE '" + depto + "' OR departamento = ''"
                + "OR departamento IS NULL) AND tipoProducto = 'tv' " + "AND adicional IN (" + adicionalesCadena + ") "
                + "GROUP BY adicional " + "UNION " + "SELECT Plan,'','',Descuento,Meses FROM reglas "
                + "WHERE Tipo_Producto = 'adic_tv' " + "AND Plan IN (" + adicionalesCadena + ") "
                + "AND (Cantidad LIKE '%" + contadorFija + "%' OR Cantidad IS NULL OR Cantidad ='') " + "AND (Nuevo = '"
                + isNuevo + "' OR Nuevo = '' OR Nuevo IS NULL) " + "GROUP BY Plan) AS tbl GROUP BY adicional";
        System.out.println("query promociones adicionales " + query);
        promociones = MainActivity.basedatos.consultarDescuentos(query);

        return promociones;

    }

    public ArrayList<ArrayList<String>> consultarDescuentos2() {

        ArrayList<ArrayList<String>> promociones = null;
        int isNuevo = 0;

        String adicionalesCadena = "";
        int cantidad = Descripcion_Ad.length;
        for (int i = 0; i < cantidad; i++) {
            if (i == 0 && cantidad > 1) {
                adicionalesCadena += "'" + Descripcion_Ad[i][0] + "',";
            } else if (i == (Descripcion_Ad.length - 1)) {
                adicionalesCadena += "'" + Descripcion_Ad[i][0] + "'";
            } else {
                adicionalesCadena += "'" + Descripcion_Ad[i][0] + "',";
            }
        }

        if (nuevo) {
            isNuevo = 1;
        } else {
            isNuevo = 0;
        }

        String query = "SELECT DISTINCT (regla) FROM reglas " + "WHERE Tipo_Producto = 'adic_tv' " + "   AND Plan IN ("
                + adicionalesCadena + ")" + "   AND (Cantidad LIKE '%3%' OR Cantidad IS NULL OR Cantidad ='')"
                + "   AND (Nuevo = '1' OR Nuevo = '' OR Nuevo IS NULL)";

        query = Utilidades.queryExternoPromocionesAdicionales(query, jsonDatos);

        System.out.println("query Adicionales 2" + query);

        promociones = MainActivity.basedatos.consultarDescuentos(query);

        System.out.println("promociones 2 Adicionales " + promociones);

        return promociones;

    }

    // array list de Productos
    public ArrayList<ItemTarificador> Array_Cotizacion() {
        ArrayList<ItemTarificador> Productos = new ArrayList<ItemTarificador>();

        String validacion = "0";

        if (control) {
            validacion = "1";
        } else {
            validacion = "0";
        }

        Productos.add(0, new ItemTarificador("Error Cotizacion Invalida", validacion, "Validacion"));

        Productos.add(new ItemTarificador("" + telefonia, "Producto", "TO"));
        Productos.add(new ItemTarificador("" + TO_I_IVA_D, "Precio", "TO"));
        Productos.add(new ItemTarificador("" + TO_P_IVA_D, "Precio", "TO"));
        Productos.add(new ItemTarificador("" + television, "Producto", "TV"));
        Productos.add(new ItemTarificador("" + TV_I_IVA_D, "Precio", "TV"));
        Productos.add(new ItemTarificador("" + TV_P_IVA_D, "Precio", "TV"));
        Productos.add(new ItemTarificador("Adicionales", "Producto", "AD"));
        Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "Precio", "AD"));
        Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "Precio", "AD"));
        Productos.add(new ItemTarificador("-", "Descuento", "AD"));
        Productos.add(new ItemTarificador("" + internet, "Producto", "BA"));
        Productos.add(new ItemTarificador("" + BA_I_IVA_D, "Precio", "BA"));
        Productos.add(new ItemTarificador("" + BA_P_IVA_D, "Precio", "BA"));
        Productos.add(new ItemTarificador("TOTAL", "Total", "TOTAL"));
        Productos.add(new ItemTarificador("" + TT_I_IVA, "Precio", "TOTAL"));
        Productos.add(new ItemTarificador("" + TT_P_IVA, "Precio", "TOTAL"));
        Productos.add(new ItemTarificador("", "Total", "TOTAL"));

        return Productos;
    }

    public ArrayList<ItemTarificador> Array_Cotizacion_Limpio() {
        ArrayList<ItemTarificador> Productos = new ArrayList<ItemTarificador>();

        Productos.add(new ItemTarificador("" + "-", "Producto", "TO"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "TO"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "TO"));
        Productos.add(new ItemTarificador("" + "-", "Descuento", "TO"));
        Productos.add(new ItemTarificador("" + "-", "Producto", "TV"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "TV"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "TV"));
        Productos.add(new ItemTarificador("" + "-", "Descuento", "TV"));
        Productos.add(new ItemTarificador("-", "Producto", "AD"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "AD"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "AD"));
        Productos.add(new ItemTarificador("-", "Descuento", "AD"));
        Productos.add(new ItemTarificador("" + "-", "Producto", "BA"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "BA"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "BA"));
        Productos.add(new ItemTarificador("" + "-", "Descuento", "BA"));
        Productos.add(new ItemTarificador("" + "-", "Producto", "3G"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "3G"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "3G"));
        Productos.add(new ItemTarificador("" + "-", "Descuento", "3G"));
        Productos.add(new ItemTarificador("" + "-", "Producto", "4G"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "4G"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "4G"));
        Productos.add(new ItemTarificador("" + "-", "Descuento", "4G"));
        Productos.add(new ItemTarificador("TOTAL", "Total", "TOTAL"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "TOTAL"));
        Productos.add(new ItemTarificador("" + 0, "Precio", "TOTAL"));
        Productos.add(new ItemTarificador("", "Total", "TOTAL"));

        return Productos;
    }

    // array list de Productos
    public ArrayList<ItemTarificador> Array_Cotizacion_Venta() {
        ArrayList<ItemTarificador> Productos = new ArrayList<ItemTarificador>();

        Productos.add(new ItemTarificador("" + Oferta, "Validaciones", "Oferta"));

        String controlCotizacion = "01";
        if (control) {
            controlCotizacion = "00";
        }

        Productos.add(new ItemTarificador("" + controlCotizacion, "Validaciones", "Control"));

        Productos.add(new ItemTarificador("" + telefonia, "Producto", "TO"));
        Productos.add(new ItemTarificador("" + TO_I_IVA, "precio", "TO"));
        Productos.add(new ItemTarificador("" + TO_P_IVA, "precio", "TO"));
        Productos.add(new ItemTarificador("" + TO_I_IVA_D, "precio descuento", "TO"));
        Productos.add(new ItemTarificador("" + TO_P_IVA_D, "precio descuento", "TO"));

        Productos.add(new ItemTarificador("" + television, "Producto", "TV"));
        Productos.add(new ItemTarificador("" + TV_I_IVA, "precio", "TV"));
        Productos.add(new ItemTarificador("" + TV_P_IVA, "precio", "TV"));
        Productos.add(new ItemTarificador("" + TV_I_IVA_D, "precio descuento", "TV"));
        Productos.add(new ItemTarificador("" + TV_P_IVA_D, "precio descuento", "TV"));

        String adicionales = "";
        for (int i = 0; i < Descripcion_Ad.length; i++) {
            adicionales += Descripcion_Ad[i][0] + ",";
        }
        Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "precio", "AD"));
        Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "precio", "AD"));
        Productos.add(new ItemTarificador("" + internet, "Producto", "BA"));
        Productos.add(new ItemTarificador("" + BA_I_IVA, "precio", "BA"));
        Productos.add(new ItemTarificador("" + BA_P_IVA, "precio", "BA"));
        Productos.add(new ItemTarificador("" + BA_I_IVA_D, "precio descuento", "BA"));
        Productos.add(new ItemTarificador("" + BA_P_IVA_D, "precio descuento", "BA"));

        Productos.add(new ItemTarificador("" + planFacturacionTO_I, "planFacturacionTO_I", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionTV_I, "planFacturacionTV_I", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionBA_I, "planFacturacionBA_I", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionTO_P, "planFacturacionTO_P", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionTV_P, "planFacturacionTV_P", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionBA_P, "planFacturacionBA_P", "PlanesFacturacion"));

        Productos.add(new ItemTarificador("" + controlGota, "gota", "gota"));
        Productos.add(new ItemTarificador("" + valorGota, "valorGota", "gota"));
        Productos.add(new ItemTarificador("" + valorGotaSinIva, "valorGotaSinIva", "gota"));
        Productos.add(new ItemTarificador("" + velocidadInicial, "VelocidadIni", "gota"));
        Productos.add(new ItemTarificador("" + velocidadFinal, "VelocidadIni", "gota"));
        Productos.add(new ItemTarificador("" + TT_I_TP_IVA, "precio total", "OTROS"));
        Productos.add(new ItemTarificador("" + TT_P_TP_IVA, "precio total", "OTROS"));
        Productos.add(new ItemTarificador("" + Contador_Productos(), "Contador", "OTROS"));
        Productos.add(new ItemTarificador(estrato, "Estrato", "OTROS"));

        return Productos;

    }

    public ArrayList<ItemTarificador> Array_Cotizacion_Venta_Digital() {
        ArrayList<ItemTarificador> Productos = new ArrayList<ItemTarificador>();

        Productos.add(new ItemTarificador("" + Oferta, "Validaciones", "Oferta"));

        String controlCotizacion = "01";
        if (control) {
            controlCotizacion = "00";
        }

        Productos.add(new ItemTarificador("" + controlCotizacion, "Validaciones", "Control"));

        Productos.add(new ItemTarificador("" + telefonia, "Producto", "TO"));
        Productos.add(new ItemTarificador("" + TO_I, "precio", "TO"));
        Productos.add(new ItemTarificador("" + TO_I_IVA, "precio", "TO"));
        Productos.add(new ItemTarificador("" + TO_P, "precio", "TO"));
        Productos.add(new ItemTarificador("" + TO_P_IVA, "precio", "TO"));
        Productos.add(new ItemTarificador("" + TO_I_IVA_D, "precio descuento", "TO"));
        Productos.add(new ItemTarificador("" + TO_P_IVA_D, "precio descuento", "TO"));
        Productos.add(new ItemTarificador("" + television, "Producto", "TV"));
        Productos.add(new ItemTarificador("" + TV_I, "precio", "TV"));
        Productos.add(new ItemTarificador("" + TV_I_IVA, "precio", "TV"));
        Productos.add(new ItemTarificador("" + TV_P, "precio", "TV"));
        Productos.add(new ItemTarificador("" + TV_P_IVA, "precio", "TV"));
        Productos.add(new ItemTarificador("" + TV_I_IVA_D, "precio descuento", "TV"));
        Productos.add(new ItemTarificador("" + TV_P_IVA_D, "precio descuento", "TV"));
        String adicionales = "";
        for (int i = 0; i < Descripcion_Ad.length; i++) {
            adicionales += Descripcion_Ad[i][0] + ",";
        }
        Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "precio", "AD"));
        Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "precio", "AD"));
        Productos.add(new ItemTarificador("" + internet, "Producto", "BA"));
        Productos.add(new ItemTarificador("" + BA_I, "precio", "BA"));
        Productos.add(new ItemTarificador("" + BA_I_IVA, "precio", "BA"));
        Productos.add(new ItemTarificador("" + BA_P, "precio", "BA"));
        Productos.add(new ItemTarificador("" + BA_P_IVA, "precio", "BA"));
        Productos.add(new ItemTarificador("" + BA_I_IVA_D, "precio descuento", "BA"));
        Productos.add(new ItemTarificador("" + BA_P_IVA_D, "precio descuento", "BA"));

        Productos.add(new ItemTarificador("" + planFacturacionTO_I, "planFacturacionTO_I", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionTV_I, "planFacturacionTV_I", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionBA_I, "planFacturacionBA_I", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionTO_P, "planFacturacionTO_P", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionTV_P, "planFacturacionTV_P", "PlanesFacturacion"));
        Productos.add(new ItemTarificador("" + planFacturacionBA_P, "planFacturacionBA_P", "PlanesFacturacion"));

        Productos.add(new ItemTarificador("" + controlGota, "gota", "gota"));
        Productos.add(new ItemTarificador("" + valorGota, "valorGota", "gota"));
        Productos.add(new ItemTarificador("" + valorGotaSinIva, "valorGotaSinIva", "gota"));
        Productos.add(new ItemTarificador("" + velocidadInicial, "VelocidadIni", "gota"));
        Productos.add(new ItemTarificador("" + velocidadFinal, "VelocidadIni", "gota"));

        Productos.add(new ItemTarificador("" + TT_I_TP_IVA, "precio total", "OTROS"));
        Productos.add(new ItemTarificador("" + TT_P_TP_IVA, "precio total", "OTROS"));
        Productos.add(new ItemTarificador("" + Contador_Productos(), "Contador", "OTROS"));
        Productos.add(new ItemTarificador(estrato, "Estrato", "OTROS"));

        return Productos;

    }

    public CotizacionCliente cotizacionCliente() {

        ArrayList<ProductoCotizador> productos = new ArrayList<ProductoCotizador>();

        ArrayList<ArrayList<String>> descuentos = Descuentos2(planTo, telefonia, planTv, television, planBa, internet, jsonDatos);

//        System.out.println("impresion descuentos "+descuentos);
//        UtilidadesTarificadorNew.imprimirDescuentos(descuentos);

        boolean controlTo = false;
        boolean controlTv = false;
        boolean controlBa = false;

        if (descuentos != null) {
            for (int i = 0; i < descuentos.size(); i++) {
                if (descuentos.get(i).get(0).equalsIgnoreCase("to")) {
                    productos.add(new ProductoCotizador(planTo, ProductoCotizador.TELEFONIA, telefonia, TO_I_IVA, TO_P_IVA, Utilidades.convertirDouble(descuentos.get(i).get(1), "descuentos.get(i).get(1)"), Utilidades.convertirNumericos(descuentos.get(i).get(2), "descuentos.get(i).get(2)"), planFacturacionTO_I, planFacturacionTO_P, "0"));
                    controlTo = true;
                } else if (descuentos.get(i).get(0).equalsIgnoreCase("tv")) {
                    productos.add(new ProductoCotizador(planTv, ProductoCotizador.TELEVISION, television, TV_I_IVA, TV_P_IVA, Utilidades.convertirDouble(descuentos.get(i).get(1), "descuentos.get(i).get(1)"), Utilidades.convertirNumericos(descuentos.get(i).get(2), "descuentos.get(i).get(2)"), planFacturacionTV_I, planFacturacionTV_P, "0"));
                    controlTv = true;
                } else if (descuentos.get(i).get(0).equalsIgnoreCase("ba")) {
                    productos.add(new ProductoCotizador(planBa, ProductoCotizador.INTERNET, internet, BA_I_IVA, BA_P_IVA, Utilidades.convertirDouble(descuentos.get(i).get(1), "descuentos.get(i).get(1)"), Utilidades.convertirNumericos(descuentos.get(i).get(2), "descuentos.get(i).get(2)"), planFacturacionBA_I, planFacturacionBA_P, "0"));
                    controlBa = true;
                }
            }
        } /*else {

            productos.add(new ProductoCotizador(planTo, ProductoCotizador.TELEFONIA, telefonia,TO_I_IVA, TO_P_IVA, 0, 0,planFacturacionTO_I,planFacturacionTO_P,"0"));
            productos.add(new ProductoCotizador(planTv, ProductoCotizador.TELEVISION, television, TV_I_IVA, TV_P_IVA, 0, 0,planFacturacionTV_I,planFacturacionTV_P,"0"));
            productos.add(new ProductoCotizador(planBa, ProductoCotizador.INTERNET, internet,BA_I_IVA, BA_P_IVA, 0, 0,planFacturacionBA_I,planFacturacionBA_P,"0"));
            controlTo = true;
            controlTv = true;
            controlBa = true;
        }*/

        if (!controlTo) {
            productos.add(new ProductoCotizador(planTo, ProductoCotizador.TELEFONIA, telefonia, TO_I_IVA, TO_P_IVA, 0, 0, planFacturacionTO_I, planFacturacionTO_P, "0"));
        }

        if (!controlTv) {
            productos.add(new ProductoCotizador(planTv, ProductoCotizador.TELEVISION, television, TV_I_IVA, TV_P_IVA, 0, 0, planFacturacionTV_I, planFacturacionTV_P, "0"));
        }

        if (!controlBa) {
            productos.add(new ProductoCotizador(planBa, ProductoCotizador.INTERNET, internet, BA_I_IVA, BA_P_IVA, 0, 0, planFacturacionBA_I, planFacturacionBA_P, "0"));
        }

        String controlCotizacion = "01";
        if (control) {
            controlCotizacion = "00";
        }
        CotizacionCliente cotizacionCliente = new CotizacionCliente();
        cotizacionCliente.setControl(controlCotizacion);
        cotizacionCliente.setOferta(Oferta);
        cotizacionCliente.setGotaba(new GotaBa(controlGota, valorGota, valorGotaSinIva, velocidadInicial, velocidadFinal));
        cotizacionCliente.setTotalIndividual(TT_I_TP_IVA);
        cotizacionCliente.setTotalEmpaquetado(TT_P_TP_IVA);
        cotizacionCliente.setContadorProductos(Contador_Productos());
        cotizacionCliente.setEstrato(estrato);
        cotizacionCliente.setProductoCotizador(productos);

        System.out.println("TT_I_TP_IVA " + TT_I_TP_IVA);
        System.out.println("TT_P_TP_IVA " + TT_P_TP_IVA);

		/*Productos.add(new ItemTarificador("" + Oferta, "Validaciones", "Oferta"));

		String controlCotizacion = "01";
		if (control) {
			controlCotizacion = "00";
		}

		Productos.add(new ItemTarificador("" + controlCotizacion, "Validaciones", "Control"));

		Productos.add(new ItemTarificador("" + telefonia, "Producto", "TO"));
		Productos.add(new ItemTarificador("" + TO_I_IVA, "precio", "TO"));
		Productos.add(new ItemTarificador("" + TO_P_IVA, "precio", "TO"));
		Productos.add(new ItemTarificador("" + TO_I_IVA_D, "precio descuento", "TO"));
		Productos.add(new ItemTarificador("" + TO_P_IVA_D, "precio descuento", "TO"));
		Productos.add(new ItemTarificador("" + mostrarDesTelefonia, "Descuento", "TO"));
		Productos.add(new ItemTarificador("" + television, "Producto", "TV"));
		Productos.add(new ItemTarificador("" + TV_I_IVA, "precio", "TV"));
		Productos.add(new ItemTarificador("" + TV_P_IVA, "precio", "TV"));
		Productos.add(new ItemTarificador("" + TV_I_IVA_D, "precio descuento", "TV"));
		Productos.add(new ItemTarificador("" + TV_P_IVA_D, "precio descuento", "TV"));
		Productos.add(new ItemTarificador("" + mostrarDesTelevision, "Descuento", "TV"));
		String adicionales = "";
		for (int i = 0; i < Descripcion_Ad.length; i++) {
			adicionales += Descripcion_Ad[i][0] + ",";
		}
		Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "precio", "AD"));
		Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "precio", "AD"));
		Productos.add(new ItemTarificador("" + internet, "Producto", "BA"));
		Productos.add(new ItemTarificador("" + BA_I_IVA, "precio", "BA"));
		Productos.add(new ItemTarificador("" + BA_P_IVA, "precio", "BA"));
		Productos.add(new ItemTarificador("" + BA_I_IVA_D, "precio descuento", "BA"));
		Productos.add(new ItemTarificador("" + BA_P_IVA_D, "precio descuento", "BA"));
		Productos.add(new ItemTarificador("" + mostrarDesInternet, "Descuento", "BA"));

		Productos.add(new ItemTarificador("" + planFacturacionTO_I, "planFacturacionTO_I", "PlanesFacturacion"));
		Productos.add(new ItemTarificador("" + planFacturacionTV_I, "planFacturacionTV_I", "PlanesFacturacion"));
		Productos.add(new ItemTarificador("" + planFacturacionBA_I, "planFacturacionBA_I", "PlanesFacturacion"));
		Productos.add(new ItemTarificador("" + planFacturacionTO_P, "planFacturacionTO_P", "PlanesFacturacion"));
		Productos.add(new ItemTarificador("" + planFacturacionTV_P, "planFacturacionTV_P", "PlanesFacturacion"));
		Productos.add(new ItemTarificador("" + planFacturacionBA_P, "planFacturacionBA_P", "PlanesFacturacion"));

		Productos.add(new ItemTarificador("" + controlGota, "gota", "gota"));
		Productos.add(new ItemTarificador("" + valorGota, "valorGota", "gota"));
		Productos.add(new ItemTarificador("" + valorGotaSinIva, "valorGotaSinIva", "gota"));
		Productos.add(new ItemTarificador("" + velocidadInicial, "VelocidadIni", "gota"));
		Productos.add(new ItemTarificador("" + velocidadFinal, "VelocidadIni", "gota"));

		Productos.add(new ItemTarificador("" + internet_3G, "Producto", "3G"));
		Productos.add(new ItemTarificador("" + MOVIL_3G_I_IVA, "precio", "3G"));
		Productos.add(new ItemTarificador("" + MOVIL_3G_P_IVA, "precio", "3G"));
		Productos.add(new ItemTarificador("" + MOVIL_3G_I_IVA_D, "precio descuento", "3G"));
		Productos.add(new ItemTarificador("" + MOVIL_3G_P_IVA_D, "precio descuento", "3G"));
		Productos.add(new ItemTarificador("" + mostrarDesInternet_3G, "Descuento", "3G"));
		Productos.add(new ItemTarificador("" + internet_4G, "Producto", "4G"));
		Productos.add(new ItemTarificador("" + MOVIL_4G_I_IVA, "precio", "4G"));
		Productos.add(new ItemTarificador("" + MOVIL_4G_P_IVA, "precio", "4G"));
		Productos.add(new ItemTarificador("" + MOVIL_4G_I_IVA_D, "precio descuento", "4G"));
		Productos.add(new ItemTarificador("" + MOVIL_4G_P_IVA_D, "precio descuento", "4G"));
		Productos.add(new ItemTarificador("" + mostrarDesInternet_4G, "Descuento", "4G"));
		Productos.add(new ItemTarificador("" + TT_I_TP_IVA, "precio total", "OTROS"));
		Productos.add(new ItemTarificador("" + TT_P_TP_IVA, "precio total", "OTROS"));
		Productos.add(new ItemTarificador("" + Contador_Productos(), "Contador", "OTROS"));
		Productos.add(new ItemTarificador(estrato, "Estrato", "OTROS"));*/

        return cotizacionCliente;

    }

    public void registro_tarificador() {
        ContentValues cv = new ContentValues();
        cv.put("Codigo_Asesor", MainActivity.config.getCodigo());
        cv.put("Fecha", Calendario.getFecha());
        cv.put("Hora", Calendario.getHora());
        MainActivity.basedatos.insertar("Registro_Tarificador", cv);
    }

    public void limpiar() {
        TO_I = 0;
        TO_I_IVA = 0;
        TO_P = 0;
        TO_P_IVA = 0;
        BA_I = 0;
        BA_I_IVA = 0;
        BA_P = 0;
        BA_P_IVA = 0;
        TV_I = 0;
        TV_I_IVA = 0;
        TV_P = 0;
        TV_P_IVA = 0;
        TO_I_D = 0;
        TO_I_IVA_D = 0;
        TO_P_D = 0;
        TO_P_IVA_D = 0;
        BA_I_D = 0;
        BA_I_IVA_D = 0;
        BA_P_D = 0;
        BA_P_IVA_D = 0;
        TV_I_D = 0;
        TV_I_IVA_D = 0;
        TV_P_D = 0;
        TV_P_IVA_D = 0;
        TT_I = 0;
        TT_I_IVA = 0;
        TT_P = 0;
        TT_P_IVA = 0;
        TT_I_TP_IVA = 0;
        TT_P_TP_IVA = 0;
        Precio_Ad_Tv_IVA = 0;
        Contador = 0;
        Contador_productos = 0;
        contadorFija = 0;

    }

}
