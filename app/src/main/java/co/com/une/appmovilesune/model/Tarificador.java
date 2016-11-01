package co.com.une.appmovilesune.model;

import java.util.ArrayList;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemDependencias;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ItemTarificador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Calendario;

public class Tarificador {

	private String telefonia, television, internet, internet_3G, internet_4G, departamento, ciudad, estrato;

	private String desTelefonia, desTelevision, desInternet, desInternet_3G, desInternet_4G;

	private String mostrarDesTelefonia, mostrarDesTelevision, mostrarDesInternet, mostrarDesInternet_3G,
			mostrarDesInternet_4G;

	private double TO_I = 0, TO_I_IVA = 0, TO_P = 0, TO_P_IVA = 0, TO_I_D = 0, TO_I_IVA_D = 0, TO_P_D = 0,
			TO_P_IVA_D = 0;
	private double BA_I = 0, BA_I_IVA = 0, BA_P = 0, BA_P_IVA = 0, BA_I_D = 0, BA_I_IVA_D = 0, BA_P_D = 0,
			BA_P_IVA_D = 0;
	private double TV_I = 0, TV_I_IVA = 0, TV_P = 0, TV_P_IVA = 0, TV_I_D = 0, TV_I_IVA_D = 0, TV_P_D = 0,
			TV_P_IVA_D = 0;
	private double MOVIL_3G_I = 0, MOVIL_3G_I_IVA = 0, MOVIL_3G_P = 0, MOVIL_3G_P_IVA = 0, MOVIL_3G_I_D = 0,
			MOVIL_3G_I_IVA_D = 0, MOVIL_3G_P_D = 0, MOVIL_3G_P_IVA_D = 0;
	private double MOVIL_4G_I = 0, MOVIL_4G_I_IVA = 0, MOVIL_4G_P = 0, MOVIL_4G_P_IVA = 0, MOVIL_4G_I_D = 0,
			MOVIL_4G_I_IVA_D = 0, MOVIL_4G_P_D = 0, MOVIL_4G_P_IVA_D = 0;

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

	public Tarificador() {

	}

	public void Consulta_Tarifaz(String telefonia, String television, String internet, String internet_3G,
			String internet_4G, String desTelefonia, String desTelevision, String desInternet, String desInternet_3G,
			String desInternet_4G, String[][] descripcion_Ad, double precio_ad, String estrato, boolean nuevo,
			String ciudadCliente, String jsonDatos, Context context, double totalDecodificadores) {

		this.estrato = estrato;
		this.telefonia = telefonia;
		this.television = television;
		this.internet = internet;
		this.internet_3G = internet_3G;
		this.internet_4G = internet_4G;
		this.desTelefonia = desTelefonia;
		this.desTelevision = desTelevision;
		this.desInternet = desInternet;
		this.desInternet_3G = desInternet_3G;
		this.desInternet_4G = desInternet_4G;
		this.nuevo = nuevo;
		this.ciudadCliente = ciudadCliente;
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

		// System.out.println("desTelefonia " + desTelefonia);

		System.out.println("telefonia " + telefonia);
		System.out.println("television " + television);
		System.out.println("internet " + internet);

		limpiar();

		Tarifas_Telefonia(telefonia);
		Tarifas_Television(television);
		Tarifas_Internet(internet);
		Tarifas_Internet_Movil(internet_3G);
		Tarifas_Internet_4G(internet_4G);
		Descuentos_Telefonia(desTelefonia, estrato);
		Descuentos_Television(desTelevision, estrato);
		Descuentos_Internet(desInternet, estrato);
		Descuentos_Internet_3G(desInternet_3G, estrato);
		Descuentos_Internet_4G(desInternet_4G, estrato);
		Total();
	}

	// metodo encargado de la cotizacion de Telefonia
	public void Tarifas_Telefonia(String Telefonia) {
		if (!Telefonia.equalsIgnoreCase(Utilidades.inicial)) {
			Consulta(Telefonia, Utilidades.tipo_producto_to);
		} else {
			telefonia = "-";
		}
	}

	// metodo encargado de la cotizacion de Television
	public void Tarifas_Television(String Television) {
		if (!Television.equalsIgnoreCase(Utilidades.inicial)) {
			Consulta(Television, Utilidades.tipo_producto_tv);
		} else {
			television = "-";
		}
	}

	// metodo encargado de la cotizacion de Internet
	public void Tarifas_Internet(String Internet) {
		if (!Internet.equalsIgnoreCase(Utilidades.inicial)) {
			Consulta(Internet, Utilidades.tipo_producto_ba);
		} else {
			internet = "-";
		}
	}

	// metodo encargado de la cotizacion de Internet_Movil
	public void Tarifas_Internet_Movil(String Internet_Movil) {
		if (!Internet_Movil.equalsIgnoreCase(Utilidades.inicial)) {
			Consulta(Internet_Movil, Utilidades.tipo_producto_3g);
		} else {
			internet_3G = "-";
		}
	}

	// metodo encargado de la cotizacion de Internet_4G
	public void Tarifas_Internet_4G(String Internet_4G) {
		if (!Internet_4G.equalsIgnoreCase(Utilidades.inicial)) {
			Consulta(Internet_4G, Utilidades.tipo_producto_4g);
		} else {
			internet_4G = "-";
		}
	}

	// metodo encargado de los descuentos de Telefonia
	public void Descuentos_Telefonia(String Descuento, String Estrato) {
		// System.out.println("Descuento to" + Descuento);
		if (Descuento != null && !Descuento.equalsIgnoreCase(Utilidades.inicial_des)
				&& !Descuento.equalsIgnoreCase(Utilidades.inicial_sindes)) {
			Descuentos(telefonia, Descuento, TO_I, TO_I_IVA, TO_P, TO_P_IVA, Estrato, Utilidades.tipo_producto_to_d);
		} else {
			setAsignar_Descuentos(TO_I, TO_I_IVA, TO_P, TO_P_IVA, Utilidades.tipo_producto_to_d, "-");
			desTelefonia = "-";
		}
	}

	// metodo encargado de los descuentos de Television
	public void Descuentos_Television(String Descuento, String Estrato) {
		// System.out.println("Descuento tv" + Descuento);
		if (Descuento != null && !Descuento.equalsIgnoreCase(Utilidades.inicial_des)
				&& !Descuento.equalsIgnoreCase(Utilidades.inicial_sindes)) {
			Descuentos(television, Descuento, TV_I, TV_I_IVA, TV_P, TV_P_IVA, Estrato, Utilidades.tipo_producto_tv_d);
		} else {
			setAsignar_Descuentos(TV_I, TV_I_IVA, TV_P, TV_P_IVA, Utilidades.tipo_producto_tv_d, "-");
			desTelevision = "-";
		}
	}

	// metodo encargado de los descuentos de Internet
	public void Descuentos_Internet(String Descuento, String Estrato) {
		// System.out.println("Descuento ba" + Descuento);
		if (Descuento != null && !Descuento.equalsIgnoreCase(Utilidades.inicial_des)
				&& !Descuento.equalsIgnoreCase(Utilidades.inicial_sindes)) {
			Descuentos(internet, Descuento, BA_I, BA_I_IVA, BA_P, BA_P_IVA, Estrato, Utilidades.tipo_producto_ba_d);
			// System.out.println("Entro a descuentos ba " + Descuento);
		} else {
			setAsignar_Descuentos(BA_I, BA_I_IVA, BA_P, BA_P_IVA, Utilidades.tipo_producto_ba_d, "-");
			desInternet = "-";
		}
	}

	// metodo encargado de los descuentos de Internet_Movil
	public void Descuentos_Internet_3G(String Descuento, String Estrato) {
		// System.out.println("Descuento 3g" + Descuento);
		if (Descuento != null && !Descuento.equalsIgnoreCase(Utilidades.inicial_des)
				&& !Descuento.equalsIgnoreCase(Utilidades.inicial_sindes)) {
			Descuentos(internet_3G, Descuento, MOVIL_3G_I, MOVIL_3G_I_IVA, MOVIL_3G_P, MOVIL_3G_P_IVA, Estrato,
					Utilidades.tipo_producto_3g_d);
		} else {
			setAsignar_Descuentos(MOVIL_3G_I, MOVIL_3G_I_IVA, MOVIL_3G_P, MOVIL_3G_P_IVA, Utilidades.tipo_producto_3g_d,
					"-");
			desInternet_3G = "-";
		}
	}

	// metodo encargado de los descuentos de Internet_4G
	public void Descuentos_Internet_4G(String Descuento, String Estrato) {
		// System.out.println("Descuento 4g" + Descuento);
		if (Descuento != null && !Descuento.equalsIgnoreCase(Utilidades.inicial_des)
				&& !Descuento.equalsIgnoreCase(Utilidades.inicial_sindes)) {
			Descuentos(internet_4G, Descuento, MOVIL_4G_I, MOVIL_4G_I_IVA, MOVIL_4G_P, MOVIL_4G_P_IVA, Estrato,
					Utilidades.tipo_producto_4g_d);
		} else {
			desInternet_4G = "-";
			setAsignar_Descuentos(MOVIL_4G_I, MOVIL_4G_I_IVA, MOVIL_4G_P, MOVIL_4G_P_IVA, Utilidades.tipo_producto_4g_d,
					"-");
		}
	}

	// metodo que consulta las tarifas en la DB
	public void Consulta(String producto, String tipo_producto) {
		int Individual = 0, Individual_Iva = 0, Empaquetado = 0, Empaquetado_Iva = 0;
		int Contador_Paquete = 0;
		String paquete = "";

		Contador_Paquete = Contador_Productos();

		if (Contador_Paquete >= 1 && Contador_Paquete <= 2) {
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

				if (tipo_producto.equalsIgnoreCase("4G")) {
					// System.out.println("internet " + internet);
					/*
					 * if (internet.equalsIgnoreCase("-")) { paquete =
					 * "pq sin BA"; } else { paquete = "pq con BA"; }
					 */
					if (producto.contains("MIFI") || (producto.contains("Modem") && producto.contains("4G"))) {
						;
					} else {
						paquete = "pq sin BA";
					}
				}

				ArrayList<String> precios = MainActivity.basedatos.consultar(false, "Precios",
						new String[] { "individual", "individual_iva", "empaquetado", "empaquetado_iva", "Oferta",
								"cantidadproductos", "homoPrimeraLinea", "homoSegundaLinea", "valorGota",
								"valorGotaIva", "ProductoHomologado", "velocidadGota" },
						"departamento=? and tipo_producto=? and Producto=? and estrato like ? and tipo_paquete Like ?",
						new String[] { depto, tipo_producto, producto, "%" + estrato + "%", "%" + paquete + "%" }, null,
						null, null).get(0);

				System.out.println("precios " + precios);

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
		if (!internet_3G.equalsIgnoreCase(Utilidades.inicial)
				&& !internet_3G.equalsIgnoreCase(Utilidades.inicial_guion)) {
			Contador_productos++;
		}

		if (!internet_4G.equalsIgnoreCase(Utilidades.inicial)
				&& !internet_4G.equalsIgnoreCase(Utilidades.inicial_guion)) {
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
				System.out.println("velocidadInicial" + velocidadInicial);
				System.out.println("velocidadFinal" + velocidadFinal);

			}
		}
		if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_3g)) {
			MOVIL_3G_I = Individual;
			MOVIL_3G_I_IVA = Individual_Iva;
			MOVIL_3G_P = Empaquetado;
			MOVIL_3G_P_IVA = Empaquetado_Iva;
		}
		if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_4g)) {
			MOVIL_4G_I = Individual;
			MOVIL_4G_I_IVA = Individual_Iva;
			MOVIL_4G_P = Empaquetado;
			MOVIL_4G_P_IVA = Empaquetado_Iva;
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
						new String[] { "Individual", "Individual_Iva", "Empaquetado", "Empaquetado_Iva" },
						"Tipo_Producto=? and Estrato Like ? and Producto Like ? and Descuentos=? and Ciudad Like ? ",
						new String[] { tipo_producto, "%" + estrato + "%", "%" + producto + "%", descuento,
								"%" + ciudadCliente + "%" },
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
			mostrarDesTelefonia = control;

		} else if (tipo_producto.equalsIgnoreCase(Utilidades.tipo_producto_tv_d)) {
			TV_I_D = individual;
			TV_I_IVA_D = individual_iva;
			TV_P_D = empaquetado;
			TV_P_IVA_D = empaquetado_iva;
			mostrarDesTelevision = control;
		} else if (tipo_producto.equalsIgnoreCase(Utilidades.tipo_producto_ba_d)) {
			BA_I_D = individual;
			BA_I_IVA_D = individual_iva;
			BA_P_D = empaquetado;
			BA_P_IVA_D = empaquetado_iva;
			mostrarDesInternet = control;
		} else if (tipo_producto.equalsIgnoreCase(Utilidades.tipo_producto_3g_d)) {
			MOVIL_3G_I_D = individual;
			MOVIL_3G_I_IVA_D = individual_iva;
			MOVIL_3G_P_D = empaquetado;
			MOVIL_3G_P_IVA_D = empaquetado_iva;
			mostrarDesInternet_3G = control;

		} else if (tipo_producto.equalsIgnoreCase(Utilidades.tipo_producto_4g_d)) {
			MOVIL_4G_I_D = individual;
			MOVIL_4G_I_IVA_D = individual_iva;
			MOVIL_4G_P_D = empaquetado;
			MOVIL_4G_P_IVA_D = empaquetado_iva;
			mostrarDesInternet_4G = control;
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
		
		System.out.println("planTo "+planTo);
		System.out.println("telefonia "+telefonia);
		System.out.println("planTv "+planTv);
		System.out.println("television "+television);
		System.out.println("planTv "+planBa);
		System.out.println("television "+internet);
		System.out.println("jsonDatos "+jsonDatos);

		
		if (planTv != null && television != null) {
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
			} else {
				control = false;
				System.out.println("Salida por comparacion");
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

			TT_I = TO_I_D + TV_I_D + BA_I_D + MOVIL_3G_I_D + MOVIL_4G_I_D + precio_ad;
			TT_I_IVA = TO_I_IVA_D + TV_I_IVA_D + BA_I_IVA_D + MOVIL_3G_I_IVA_D + MOVIL_4G_I_IVA_D + Precio_Ad_Tv_IVA;

			TT_I_TP_IVA = TO_I_IVA + TV_I_IVA + BA_I_IVA + MOVIL_3G_I_IVA + MOVIL_4G_I_IVA + Precio_Ad_Tv_IVA;
			TT_P = TO_P_D + TV_P_D + BA_P_D + MOVIL_3G_P_D + MOVIL_4G_P_D + precio_ad;

			TT_P_IVA = TO_P_IVA_D + TV_P_IVA_D + BA_P_IVA_D + MOVIL_3G_P_IVA_D + MOVIL_4G_P_IVA_D + Precio_Ad_Tv_IVA;

			TT_P_TP_IVA = TO_P_IVA + TV_P_IVA + BA_P_IVA + MOVIL_3G_P_IVA + MOVIL_4G_P_IVA + Precio_Ad_Tv_IVA;

			// if (internet_3G.equalsIgnoreCase("Demanda - 1D") ||
			// internet_3G.equalsIgnoreCase("Demanda - 3D")
			// || internet_3G.equalsIgnoreCase("Demanda - 5D") ||
			// internet_3G.equalsIgnoreCase("Demanda - 10H")
			// || internet_3G.equalsIgnoreCase("Demanda - 20H") ||
			// internet_3G.equalsIgnoreCase("Demanda - 30H")) {
			//
			// TT_I_TP_IVA_Sin_Demanda = TO_I_IVA + TV_I_IVA + BA_I_IVA +
			// Precio_Ad_Tv_IVA;
			// TT_P_TP_IVA_Sin_Demanda = TO_P_IVA + TV_P_IVA + BA_P_IVA +
			// Precio_Ad_Tv_IVA;
			//
			// } else {
			// TT_I_TP_IVA_Sin_Demanda = TO_I_IVA + TV_I_IVA + BA_I_IVA +
			// MOVIL_3G_I_IVA + MOVIL_4G_I_IVA
			// + Precio_Ad_Tv_IVA;
			//
			// TT_P_TP_IVA_Sin_Demanda = TO_P_IVA + TV_P_IVA + BA_P_IVA +
			// MOVIL_3G_P_IVA + MOVIL_4G_P_IVA
			// + Precio_Ad_Tv_IVA;
			// }
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
		Productos.add(new ItemTarificador("" + mostrarDesTelefonia, "Descuento", "TO"));
		Productos.add(new ItemTarificador("" + television, "Producto", "TV"));
		Productos.add(new ItemTarificador("" + TV_I_IVA_D, "Precio", "TV"));
		Productos.add(new ItemTarificador("" + TV_P_IVA_D, "Precio", "TV"));
		Productos.add(new ItemTarificador("" + mostrarDesTelevision, "Descuento", "TV"));
		Productos.add(new ItemTarificador("Adicionales", "Producto", "AD"));
		Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "Precio", "AD"));
		Productos.add(new ItemTarificador("" + Precio_Ad_Tv_IVA, "Precio", "AD"));
		Productos.add(new ItemTarificador("-", "Descuento", "AD"));
		Productos.add(new ItemTarificador("" + internet, "Producto", "BA"));
		Productos.add(new ItemTarificador("" + BA_I_IVA_D, "Precio", "BA"));
		Productos.add(new ItemTarificador("" + BA_P_IVA_D, "Precio", "BA"));
		Productos.add(new ItemTarificador("" + mostrarDesInternet, "Descuento", "BA"));
		Productos.add(new ItemTarificador("" + internet_3G, "Producto", "3G"));
		Productos.add(new ItemTarificador("" + MOVIL_3G_I_IVA_D, "Precio", "3G"));
		Productos.add(new ItemTarificador("" + MOVIL_3G_P_IVA_D, "Precio", "3G"));
		Productos.add(new ItemTarificador("" + mostrarDesInternet_3G, "Descuento", "3G"));
		Productos.add(new ItemTarificador("" + internet_4G, "Producto", "4G"));
		Productos.add(new ItemTarificador("" + MOVIL_4G_I_IVA_D, "Precio", "4G"));
		Productos.add(new ItemTarificador("" + MOVIL_4G_P_IVA_D, "Precio", "4G"));
		Productos.add(new ItemTarificador("" + mostrarDesInternet_4G, "Descuento", "4G"));
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
		Productos.add(new ItemTarificador("" + mostrarDesTelefonia, "Descuento", "TO"));
		Productos.add(new ItemTarificador("" + television, "Producto", "TV"));
		Productos.add(new ItemTarificador("" + TV_I, "precio", "TV"));
		Productos.add(new ItemTarificador("" + TV_I_IVA, "precio", "TV"));
		Productos.add(new ItemTarificador("" + TV_P, "precio", "TV"));
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
		Productos.add(new ItemTarificador("" + BA_I, "precio", "BA"));
		Productos.add(new ItemTarificador("" + BA_I_IVA, "precio", "BA"));
		Productos.add(new ItemTarificador("" + BA_P, "precio", "BA"));
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
		Productos.add(new ItemTarificador(estrato, "Estrato", "OTROS"));

		return Productos;

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
		MOVIL_3G_I = 0;
		MOVIL_3G_I_IVA = 0;
		MOVIL_3G_P = 0;
		MOVIL_3G_P_IVA = 0;
		MOVIL_3G_I_IVA = 0;
		MOVIL_4G_I = 0;
		MOVIL_4G_I_IVA = 0;
		MOVIL_4G_P = 0;
		MOVIL_4G_P_IVA = 0;
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
		MOVIL_3G_I_D = 0;
		MOVIL_3G_I_IVA_D = 0;
		MOVIL_3G_P_D = 0;
		MOVIL_3G_P_IVA_D = 0;
		MOVIL_4G_I_D = 0;
		MOVIL_4G_I_IVA_D = 0;
		MOVIL_4G_P_D = 0;
		MOVIL_4G_P_IVA_D = 0;
		TT_I = 0;
		TT_I_IVA = 0;
		TT_P = 0;
		TT_P_IVA = 0;
		Precio_Ad_Tv_IVA = 0;
		Contador = 0;
		Contador_productos = 0;
		contadorFija = 0;

	}

}
