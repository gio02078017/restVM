package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonParseException;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemCarrusel;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

public class Cliente implements Serializable, Observer, Subject {

	private static final long serialVersionUID = 8257744085980317629L;
	private String id;
	private String id_asesoria;
	private String TipoDocumento;
	private String Cedula;
	private String Expedicion;
	private String LugarExpedicion;
	private String DepartamentoExpedicion;
	private String DaneLugarExpedicion;
	private String Nombre;
	private String Apellido;
	private String FechaNacimiento;
	private String Telefono;
	private String TelefonoDestino;
	private boolean bloquearTelefonoDestino = false;
	private String Telefono2;
	private String Celular;
	private String Celular2;
	private String Estrato;
	private int Estratifico;
	private boolean controlEstrato;
	private String Barrio;
	private String CodigoBarrio;
	private String Direccion;
	private String DireccionNormalizada;
	private String DireccionConsulta;
	private String IdDireccionGis;
	private String Latitud;
	private String longitud;
	private String IdDireccionGisEx;
	private String CodigoHogar;
	private String Paginacion;
	private String paginaAsignacion;
	private String tecnologia;
	private boolean bloqueoTecnologia;

	private String Contrato;
	private String PuntoReferencia;
	private String TipoServicio;
	private String Correo;
	private String dominioCorreo;
	private String usermail;
	private String Login;
	private String pinhp;
	private boolean validarLogin = false;
	private String observacion;

	private int tipoCliente = 2;

	private String departamento;
	private String ciudad;
	private String departamentoSiebel;
	private String ciudadSiebel;
	private String ciudadFenix;
	private String ciudadAmc;

	private String NivelEstudio;
	private String Profesion;
	private String Ocupacion;
	private String Cargo;
	private String NivelIngresos;
	private String EstadoCivil;
	private String Genero;
	private String PersonasCargo;
	private String TipoVivienda;
	private String TipoPredio;
	private String TipoPropiedad;
	private String TipoConstruccion;
	private String Migracion4G;
	private String NombrePredio;
	private String MunicipioSSC;

	private Contacto contacto1;
	private Contacto contacto2;
	private ScooringUne scooringune;

	private Facturacion facturacion;

	private String portafolio;
	private String cobertura;
	private String carteraUNE;
	private String consolidado;

	private String domiciliacion;
	private String bloqueoDomiciliacion;

	public Observer observador;

	public Context context;

	private String ciudadDane;

	private int idIVR = 0;

	private boolean controlFacturacion;

	private boolean validarCorreo = true;
	private boolean controlNormalizada = false;
	private boolean consultaNormalizada = false;
	private boolean controlClienteSiebel = false;
	private boolean controlEstadoCuenta = false;
	private boolean controlCerca = false;
	private String tipoCuenta = "";
	private String smartPromo = "0";
	private String hogarNuevo = "0";

	private int estandarizarSiebel = 2;

	public AmigoCuentas ac;

	private String logSmartPromoRes;
	private String logSmartPromoEnv;

	private ArrayList<ItemCarrusel> arraycarrusel;
	private boolean carrusel = false;
	private String codigoCarrusel;
	private String direccionCarrusel;
	private String documentoCarrusel;
	private boolean controlCarrusel;
	private String crmCarrusel;
	private String productosCarrusel;

	private boolean realizoConfronta;
	private boolean confronta;

	private String pagoAnticipado;

	public Cliente() {
		TipoDocumento = "";
		Cedula = "";
		Expedicion = "";
		LugarExpedicion = "";
		Nombre = "";
		Apellido = "";
		FechaNacimiento = "";
		Telefono = "";
		TelefonoDestino = "";
		Telefono2 = "";
		Celular = "";
		Celular2 = "";
		Estrato = "";
		Barrio = "";
		CodigoBarrio = "";
		Direccion = "";
		Paginacion = "";
		PuntoReferencia = "";
		departamento = "";
		ciudad = "";
		TipoServicio = "";
		Contrato = "";
		Correo = "";
		dominioCorreo = "";
		usermail = "";
		Login = "";
		pinhp = "";
		observacion = "";
		NivelEstudio = "";
		Profesion = "";
		Ocupacion = "";
		Cargo = "";
		NivelIngresos = "";
		EstadoCivil = "";
		Genero = "";
		PersonasCargo = "";
		TipoVivienda = "";
		TipoPredio = "";
		TipoPropiedad = "";
		NombrePredio = "";
		DaneLugarExpedicion = "";

		controlFacturacion = false;
		validarLogin = false;

		MunicipioSSC = "";

		validarCorreo = true;

		controlNormalizada = false;
		consultaNormalizada = false;

		tecnologia = "";
		bloqueoTecnologia = false;

		domiciliacion = "";
		bloqueoDomiciliacion = "";

		logSmartPromoEnv = "";
		logSmartPromoRes = "";

		pagoAnticipado = "";
	}

	public Cliente(String Municipio, String Cedula, String Telefono, String Direccion, String id_asesoria) {

		this.id_asesoria = id_asesoria;
		this.TipoDocumento = "";
		this.Cedula = Cedula;
		this.Expedicion = "";
		this.LugarExpedicion = "";
		this.Nombre = "";
		this.Apellido = "";
		this.FechaNacimiento = "";
		if (!MainActivity.config.getDepartamento().equals("Antioquia")) {
			this.Telefono = MainActivity.config.getIdentificador() + Telefono;
		} else {
			this.Telefono = Telefono;
		}
		this.TelefonoDestino = "";
		this.Telefono2 = "";
		this.Celular = "";
		this.Celular2 = "";
		this.Estrato = "";
		this.Barrio = "";
		this.Direccion = Direccion;
		this.Paginacion = "";
		this.Contrato = "";
		this.PuntoReferencia = "";
		this.departamento = MainActivity.config.getDepartamento();
		this.ciudad = Municipio;
		this.ciudadFenix = Utilidades.traducirCiudad(Municipio);
		this.ciudadDane = Utilidades.traducirCiudadDane(Municipio);
		this.departamentoSiebel = Utilidades.traducirCiudadDane(Municipio);
		this.ciudadSiebel = Utilidades.traducirCiudadGIIS(Municipio);
		this.ciudadAmc = Utilidades.traducirCiudadAmc(Municipio);
		this.TipoServicio = "";
		this.Correo = "";
		this.dominioCorreo = "";
		this.usermail = "";
		this.Login = "";
		this.pinhp = "";
		this.observacion = "";

		this.CodigoHogar = "";

		if (!MainActivity.servidor.equals(Conexion.SERVIDOR_PRUEBAS)) {
			this.tipoCliente = 2;
		} else {
			this.tipoCliente = 1;
		}

		// this.tipoCliente = 2;
		this.NivelEstudio = "";
		this.Profesion = "";
		this.Ocupacion = "";
		this.Cargo = "";
		this.NivelIngresos = "";
		this.EstadoCivil = "";
		this.Genero = "";
		this.PersonasCargo = "";
		this.TipoVivienda = "";
		this.TipoPredio = "";
		this.TipoPropiedad = "";
		this.NombrePredio = "";

		this.tecnologia = "";
		this.domiciliacion = "";
		this.bloqueoDomiciliacion = "";

		contacto1 = new Contacto();
		contacto2 = new Contacto();
		scooringune = new ScooringUne();

		facturacion = new Facturacion();

	}

	public Cliente(String tipoDocumento, String Cedula, String Expedicion, String LugarExpedicion, String Nombre,
			String Apellido, String fechaNacimiento, String Telefono, String TelefinoDestino, String Telefono2,
			String Celular, String Celular2, String Estrato, String Barrio, String Direccion, String Paginacion,
			String PuntoReferencia, String TipoServicio, String Correo, String Login, String pinhp, String obsevacion,
			String nivelEstudio, String profesion, String ocupacion, String cargo, String nivelIngresos,
			String estadoCivil, String genero, String personasCargo, String tipoVivienda, String tipoPredio,
			String tipoPropiedad, String nombrePredio) {
		this.TipoDocumento = tipoDocumento;
		this.Cedula = Cedula;
		this.Expedicion = Expedicion;
		this.LugarExpedicion = LugarExpedicion;
		this.Nombre = Nombre;
		this.Apellido = Apellido;
		this.FechaNacimiento = fechaNacimiento;
		if (!MainActivity.config.getDepartamento().equals("Antioquia")) {
			this.Telefono = MainActivity.config.getIdentificador() + Telefono;
			if (TelefinoDestino.length() == 7) {
				this.TelefonoDestino = MainActivity.config.getIdentificador() + TelefinoDestino;
			} else if (TelefinoDestino.length() == 10) {
				this.TelefonoDestino = TelefinoDestino;
			} else {
				this.TelefonoDestino = "";
			}

		} else {
			if (TelefinoDestino.length() == 7 || TelefinoDestino.length() == 10) {
				this.TelefonoDestino = TelefinoDestino;
			} else {
				this.TelefonoDestino = "";
			}
		}

		// System.out.println(this.TelefonoDestino);

		this.Telefono2 = Telefono2;
		this.Celular = Celular;
		this.Celular2 = Celular2;
		this.Estrato = Estrato;
		this.Barrio = Barrio;
		this.Direccion = Direccion;
		this.Paginacion = Paginacion;
		this.PuntoReferencia = PuntoReferencia;
		this.departamento = "";
		this.ciudad = "";
		this.TipoServicio = TipoServicio;
		this.Correo = Correo;
		this.Login = Login;
		this.pinhp = pinhp;
		this.observacion = obsevacion;

		this.NivelEstudio = nivelEstudio;
		this.Profesion = profesion;
		this.Ocupacion = ocupacion;
		this.Cargo = cargo;
		this.NivelIngresos = nivelIngresos;
		this.EstadoCivil = estadoCivil;
		this.Genero = genero;
		this.PersonasCargo = personasCargo;
		this.TipoVivienda = tipoVivienda;
		this.TipoPredio = tipoPredio;
		this.TipoPropiedad = tipoPropiedad;
		this.NombrePredio = nombrePredio;
	}

	public void cargarCliente(String id_asesoria) {
		this.id_asesoria = id_asesoria;

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "clientes", null,
				"id_asesoria=?", new String[] { this.id_asesoria }, null, "id_asesoria DESC", null);

		if (resultado != null) {
			this.id = resultado.get(0).get(0);
			this.TipoDocumento = resultado.get(0).get(1);
			this.Cedula = resultado.get(0).get(2);
			this.Expedicion = resultado.get(0).get(3);
			this.LugarExpedicion = resultado.get(0).get(4);
			this.Nombre = resultado.get(0).get(5);
			this.Apellido = resultado.get(0).get(6);
			this.FechaNacimiento = resultado.get(0).get(6);
			this.Telefono = resultado.get(0).get(7);
			this.TelefonoDestino = resultado.get(0).get(8);
			this.Telefono2 = resultado.get(0).get(9);
			this.Celular = resultado.get(0).get(10);
			this.Celular2 = resultado.get(0).get(11);
			this.Estrato = resultado.get(0).get(12);
			this.Barrio = resultado.get(0).get(13);
			this.Direccion = resultado.get(0).get(14);
			this.Paginacion = resultado.get(0).get(15);
			this.Contrato = resultado.get(0).get(16);
			this.PuntoReferencia = resultado.get(0).get(17);
			this.TipoServicio = resultado.get(0).get(18);
			this.departamento = resultado.get(0).get(19);
			this.ciudad = resultado.get(0).get(20);
			this.ciudadFenix = Utilidades.traducirCiudad(resultado.get(0).get(20));
			this.Correo = resultado.get(0).get(21);
			this.Login = resultado.get(0).get(22);
			this.observacion = resultado.get(0).get(23);
			if (resultado.get(0).get(25) != null) {
				if (!resultado.get(0).get(25).equals("")) {
					this.tipoCliente = Integer.parseInt(resultado.get(0).get(24));
				}
			}

			this.NivelEstudio = resultado.get(0).get(25);
			this.Profesion = resultado.get(0).get(26);
			this.Ocupacion = resultado.get(0).get(27);
			this.Cargo = resultado.get(0).get(28);
			this.NivelIngresos = resultado.get(0).get(29);
			this.EstadoCivil = resultado.get(0).get(30);
			this.Genero = resultado.get(0).get(31);
			this.PersonasCargo = resultado.get(0).get(32);
			this.TipoVivienda = resultado.get(0).get(33);
			this.TipoPredio = resultado.get(0).get(34);
			this.NombrePredio = resultado.get(0).get(35);

			contacto1 = new Contacto();
			contacto1.cargarContactos(this.Cedula);
			contacto2 = new Contacto();
			contacto2.cargarContactos(this.Cedula);
			facturacion = new Facturacion();
			facturacion.cargarFacturacion(this.Cedula);

			MainActivity.btnCliente.setOK();
		} else {
			// System.out.println("No se pudo cargar la asesoria");
		}

	}

	public void actualizarCliente() {
		ContentValues cv = new ContentValues();
		cv.put("id_asesoria", id_asesoria);
		cv.put("TipoDocumento", TipoDocumento);
		cv.put("Cedula", Cedula);
		cv.put("Expedicion", Expedicion);
		// System.out.println("Cliente 194 => " + LugarExpedicion);
		cv.put("LugarExpedicion", LugarExpedicion);
		cv.put("Nombre", Nombre);
		// cv.put("Apellido", Apellido);
		cv.put("FechaNacimiento", FechaNacimiento);
		cv.put("Telefono", Telefono);
		cv.put("TelefonoDestino", TelefonoDestino);
		cv.put("Telefono2", Telefono2);
		cv.put("Celular", Celular);
		cv.put("Celular2", Celular2);
		cv.put("Estrato", Estrato);
		cv.put("Barrio", Barrio);
		cv.put("Direccion", Direccion);
		cv.put("Paginacion", Paginacion);
		cv.put("Contrato", Contrato);
		cv.put("PuntoReferencia", PuntoReferencia);
		cv.put("TipoServicio", TipoServicio);
		cv.put("Departamento", departamento);
		cv.put("Ciudad", ciudad);
		cv.put("Correo", Correo);
		cv.put("Login", Login);
		cv.put("observaciones", observacion);
		cv.put("tipoCliente", tipoCliente);

		cv.put("NivelEstudio", NivelEstudio);
		cv.put("Profesion", Profesion);
		cv.put("Ocupacion", Ocupacion);
		cv.put("Cargo", Cargo);
		cv.put("NivelIngresos", NivelIngresos);
		cv.put("EstadoCivil", EstadoCivil);
		cv.put("Genero", Genero);
		cv.put("PersonasCargo", PersonasCargo);
		cv.put("TipoVivienda", TipoVivienda);
		cv.put("TipoPredio", TipoPredio);
		cv.put("NombrePredio", NombrePredio);

		if (id == null) {
			guardarCliente(id_asesoria);
		} else if (MainActivity.basedatos.actualizar("clientes", cv, "Cedula=?", new String[] { Cedula })) {
			// System.out.println("Cliente Actualizado");
		}
	}

	public void guardarCliente(String idAsesoria) {
		id_asesoria = idAsesoria;
		ContentValues cv = new ContentValues();
		cv.put("id_asesoria", id_asesoria);
		cv.put("TipoDocumento", TipoDocumento);
		cv.put("Cedula", Cedula);
		cv.put("Expedicion", Expedicion);
		cv.put("LugarExpedicion", LugarExpedicion);
		cv.put("Nombre", Nombre);
		// cv.put("Apellido", Apellido);
		cv.put("FechaNacimiento", FechaNacimiento);
		cv.put("Telefono", Telefono);
		cv.put("TelefonoDestino", TelefonoDestino);
		cv.put("Telefono2", Telefono2);
		cv.put("Celular", Celular);
		cv.put("Celular2", Celular2);
		cv.put("Estrato", Estrato);
		cv.put("Barrio", Barrio);
		cv.put("Direccion", Direccion);
		cv.put("Paginacion", Paginacion);
		cv.put("Contrato", Contrato);
		cv.put("PuntoReferencia", PuntoReferencia);
		cv.put("TipoServicio", TipoServicio);
		cv.put("Departamento", departamento);
		cv.put("Ciudad", ciudad);
		cv.put("Correo", Correo);
		cv.put("Login", Login);
		cv.put("observaciones", observacion);
		cv.put("tipoCliente", tipoCliente);

		cv.put("NivelEstudio", NivelEstudio);
		cv.put("Profesion", Profesion);
		cv.put("Ocupacion", Ocupacion);
		cv.put("Cargo", Cargo);
		cv.put("NivelIngresos", NivelIngresos);
		cv.put("EstadoCivil", EstadoCivil);
		cv.put("Genero", Genero);
		cv.put("PersonasCargo", PersonasCargo);
		cv.put("TipoVivienda", TipoVivienda);
		cv.put("TipoPredio", TipoPredio);
		cv.put("NombrePredio", NombrePredio);

		if (MainActivity.basedatos.insertar("clientes", cv)) {
			ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "clientes",
					new String[] { "Cedula" }, "id_asesoria=?", new String[] { idAsesoria }, null, null, null);
			if (resultado != null) {
				// id = resultado.get(0).get(2);
				contacto1.setId_cliente(Cedula);
				contacto1.guardarContacto(Cedula);
				contacto2.setId_cliente(Cedula);
				contacto2.guardarContacto(Cedula);
				facturacion.setId_cliente(Cedula);
				facturacion.guardarFacturacion(Cedula);
			} else {
				// System.out.println("No se pudo guardar cliente");
			}
		} else {
			id = null;
		}
	}

	public void llenarCliente() {

		// System.out.println("Llenar Cliente");

		ArrayList<String[]> parametros = new ArrayList<String[]>();
		parametros.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		parametros.add(new String[] { "Identificador", Telefono });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add("Cliente");
		params.add(parametros);

		MainActivity.crearConexion();
		MainActivity.conexion.addObserver(MainActivity.obsrMainActivity);
		try {

			MainActivity.conexion.execute(params);

		} catch (Exception e) {
			e.printStackTrace();
			// Log.w("Error", "Error Cliente Automatico "+e.getMessage());
			// MainActivity.conexion.cancel(true);
		}
	}

	public void llenarClienteManual(Context context, String Telefono) {
		ArrayList<String[]> parametros = new ArrayList<String[]>();
		parametros.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		parametros.add(new String[] { "Identificador", Telefono });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add("Cliente");
		params.add(parametros);

		MainActivity.crearConexion();
		MainActivity.conexion.setManual(context);
		MainActivity.conexion.addObserver(MainActivity.obsrMainActivity);
		MainActivity.conexion.execute(params);
	}

	public void llenarClienteREDCO(Context context, String dato, String tipo, String evento) {
		ArrayList<String[]> parametros = new ArrayList<String[]>();

		if (tipo.equalsIgnoreCase("identificador")) {
			// parametros.add(Utilidades.limpiarTelefono(dato));
			dato = Utilidades.limpiarTelefono(dato);
		}

		parametros.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		parametros.add(new String[] { "Tipo", tipo });
		parametros.add(new String[] { "Parametros", dato });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add("ClienteREDCO");
		params.add(parametros);

		MainActivity.crearConexion();
		if (evento.equalsIgnoreCase("Manual")) {
			MainActivity.conexion.setManual(context);
		}
		MainActivity.conexion.addObserver(MainActivity.obsrMainActivity);
		MainActivity.conexion.execute(params);

	}

	public void llenarConsolidadoSiebel(Context context, String dato, String tipo, String evento) {
		ArrayList<String[]> parametros = new ArrayList<String[]>();
		MainActivity.btnCobertura.setVisible();
		MainActivity.btnPortafolio.setVisible();
		JSONObject parametro = new JSONObject();
		if (tipo.equalsIgnoreCase("identificador")) {
			try {
				parametro.put("strIdentificador", Utilidades.limpiarTelefonoSiebel(dato));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		parametros.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		parametros.add(new String[] { "Tipo", tipo });
		parametros.add(new String[] { "Parametros", parametro.toString() });
		parametros.add(new String[] { "Salida", "" });
		ArrayList<Object> params = new ArrayList<Object>();
		params.add("ConsolidadoSiebel");
		params.add(parametros);
		MainActivity.crearConexion();
		if (evento.equalsIgnoreCase("Manual")) {
			MainActivity.conexion.setManual(context);
		}
		MainActivity.conexion.addObserver(MainActivity.obsrMainActivity);
		MainActivity.conexion.execute(params);
	}

	public void reorganizarCliente(String cliente) {
		if (cliente.equals("Ha superado el limite de consultas por dia")
				|| cliente.equals("Consulta Hecha fuera del horario establecido")) {
			Toast.makeText(MainActivity.context, cliente, Toast.LENGTH_SHORT).show();
		} else {
			try {
				JSONObject jo = new JSONObject(cliente);

				System.out.println("jo cliente " + jo);

				System.out.println("jo Municipio " + traducirMunicipio(jo.getString("Municipio")));

				if (!Utilidades.excluir("siebelMunicipios", traducirMunicipio(jo.getString("Municipio")))) {

					Direccion = jo.getString("Direccion");
					Estrato = jo.getString("Estrato");
					Paginacion = jo.getString("Paginacion");

					if (jo.getString("Nombres").equalsIgnoreCase("")
							&& jo.getString("Apellidos").equalsIgnoreCase("")) {
						Nombre = jo.getString("NombreCompleto");
						Apellido = "";
					} else {
						Nombre = jo.getString("Nombres");
						Apellido = jo.getString("Apellidos");
					}

					Cedula = jo.getString("Documento");
					// departamento =
					// traducirDepartamento(jo.getString("Departamento"));
					if (!departamento.equalsIgnoreCase(Utilidades.Bogota)) {
						ciudad = traducirMunicipio(jo.getString("Municipio"));
						ciudadFenix = Utilidades.traducirCiudadFenix(ciudad);
						ciudadDane = Utilidades.traducirCiudadDane(ciudad);
						this.departamentoSiebel = Utilidades.traducirCiudadDane(ciudad);
						this.ciudadSiebel = Utilidades.traducirCiudadGIIS(ciudad);
					}

					System.out.println("ciudad " + ciudad);

					if (!ciudad.equalsIgnoreCase("BogotaREDCO")) {
						lanzarSimulador("Cobertura");
					}

					lanzarSimulador("Portafolio");
					lanzarSimulador("Cartera_UNE");
					if (Utilidades.visible("AmigoCuentas", MainActivity.config.getCiudad())) {
						lanzarAmigoCuentas(Telefono, "identificador", jo.getString("Municipio"));
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void reorganizarClienteSiebel(String consolidado) {

		Utilidades.pintarBotones(consolidado);

		System.out.println("Control Cliente reorganizarClienteSiebel adentro");

		try {
			JSONObject jo = new JSONObject(consolidado);
			System.out.println("Control Cliente jo siebel " + jo);
			System.out.println("Telefono " + Telefono);
			if (jo.has("CodigoMensaje")) {
				if (jo.getString("CodigoMensaje").equalsIgnoreCase("00")) {
					setConsolidado(consolidado);
				}
				System.out.println("Control Cliente jo siebel" + jo);
				if (jo.has("ListaDatosClienteVent")) {
					JSONArray arrayJson = jo.getJSONArray("ListaDatosClienteVent");
					System.out.println("Control Cliente arrayJson siebel " + arrayJson);
					if (arrayJson.length() > 0) {
						JSONObject cliente = arrayJson.getJSONObject(0);
						System.out.println("cliente " + cliente);
						Direccion = cliente.getString("Direccion");

						Estrato = cliente.getString("Estrato");
						Cedula = cliente.getString("Cliente_id");
						Nombre = cliente.getString("Nombres");
						Apellido = cliente.getString("Apellidos");
						IdDireccionGis = cliente.getString("IdDireccionGis");
						TipoDocumento = cliente.getString("TipoDocumento");
						CodigoHogar = cliente.getString("Codigo_Hogar");
						System.out.println("Telefono " + Telefono);
						if (Telefono != null && !Telefono.equalsIgnoreCase("")) {
							Telefono = Telefono;
						}

						if (!ciudad.equalsIgnoreCase(cliente.getString("Municipio"))) {

							System.out.println(
									"Control Cliente cliente.getString(Municipio) " + cliente.getString("Municipio"));

							if (!cliente.getString("Municipio").equalsIgnoreCase("Bogota")) {
								ciudad = cliente.getString("Municipio");
							}
						}

						System.out.println("Control Cliente ciudad cliente " + ciudad);

						controlClienteSiebel = true;
						estandarizarSiebel = 1;
					}
				} else {
					estandarizarSiebel = 0;
				}
			} else {
				estandarizarSiebel = 0;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String traducirDepartamento(String departamento) {
		if (departamento.equals("BOG")) {
			departamento = "CUN";
		}
		ArrayList<ArrayList<String>> result = MainActivity.basedatos.consultar(true, "Departamentos",
				new String[] { "Departamento" }, "DepartamentoFenix=?", new String[] { departamento }, null, null,
				null);
		return result.get(0).get(0);
	}

	public String traducirMunicipio(String municipio) {
		ArrayList<ArrayList<String>> result = MainActivity.basedatos.consultar(true, "Departamentos",
				new String[] { "Ciudad" }, "CiudadFenix=?", new String[] { municipio }, null, null, null);
		return result.get(0).get(0);
	}

	public String obtenerDirecciones(Context context) {
		ProgressDialog pd = ProgressDialog.show(context, "Procesando", "Obteniendo Direcciones...");
		Simulador simulador = new Simulador();

		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(Cedula);

		String result = simulador.consultarDirecciones(parametros);
		pd.dismiss();
		return result;
	}

	private void lanzarSimulador(String tipo) {

		Simulador simulador = new Simulador();
		simulador.addObserver(this);

		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(Telefono);
		parametros.add(Direccion);
		parametros.add(ciudad);
		parametros.add(Cedula);

		if (tipo.equals("Portafolio") && ciudad.equalsIgnoreCase("BogotaREDCO")) {

			parametros = new ArrayList<String>();
			parametros.add(Utilidades.limpiarTelefono(Telefono));
			parametros.add("identificador");

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(MainActivity.config.getCodigo());
			params.add("PortafolioREDCO");
			params.add(parametros);
			simulador.execute(params);

		} else if (tipo.equals("Portafolio")) {

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(MainActivity.config.getCodigo());
			params.add("Portafolio");
			params.add("identificador");
			params.add(parametros);

			simulador.execute(params);

		} else if (tipo.equals("Cobertura")) {

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(MainActivity.config.getCodigo());
			params.add("CoberturaNew");
			params.add("identificador");
			params.add(parametros);

			simulador.execute(params);

		} else if (tipo.equals("Cartera_UNE")) {

			parametros = new ArrayList<String>();
			parametros.add(MainActivity.config.getCodigo());
			parametros.add(Cedula);

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(MainActivity.config.getCodigo());
			params.add("Cartera_UNE");
			params.add(parametros);

			simulador.execute(params);

		}

	}

	public void lanzarAmigoCuentas(String valor, String tipo, String municipio) {
		System.out.println("lanzarAmigoCuentas");
		ac = new AmigoCuentas();

		ArrayList<String> parametros = new ArrayList<String>();
		// parametros = new ArrayList<String>();
		parametros.add(valor);
		parametros.add(tipo);
		parametros.add(municipio);

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(parametros);
		params.add(this);
		ac.execute(params);

	}

	public boolean isControlFacturacion() {
		return controlFacturacion;
	}

	public void setControlFacturacion(boolean controlFacturacion) {
		this.controlFacturacion = controlFacturacion;
	}

	public JSONObject consolidarCliente() {
		JSONObject jo = new JSONObject();
		ArrayList<JSONObject> objectContactos = new ArrayList<JSONObject>();
		ArrayList<JSONObject> objectTelefonos = new ArrayList<JSONObject>();
		ArrayList<JSONObject> objectDirecciones = new ArrayList<JSONObject>();

		JSONArray arrayTelefonos = new JSONArray();
		JSONArray arrayDirecciones = new JSONArray();
		JSONArray arrayContactos = new JSONArray();

		try {
			jo.put("municipio", ciudad);
			jo.put("municipioFenix", Utilidades.ciudadFenix(ciudad, departamento));
			jo.put("municipioDane", ciudadDane);
			jo.put("departamento", departamento);
			jo.put("departamentoFenix", Utilidades.departamentoFenix(departamento));
			jo.put("nombres", Nombre);
			jo.put("apellidos", Apellido);
			jo.put("tipoDocumento", TipoDocumento);
			jo.put("documento", Cedula);
			jo.put("fechaexpedicion", Expedicion);

			jo.put("departamentoExpedicion", DepartamentoExpedicion);
			if (DepartamentoExpedicion != null && !DepartamentoExpedicion.equalsIgnoreCase("")) {
				String[] lugar = LugarExpedicion.split("-");
				jo.put("lugarexpedicion", lugar[0]);
			} else {
				jo.put("lugarexpedicion", LugarExpedicion);
			}

			jo.put("daneLugarExpedicion", "" + DaneLugarExpedicion + "");
			jo.put("fechanacimiento", FechaNacimiento);
			jo.put("estrato", Estrato);

			if (Estratifico == 1) {
				jo.put("estratifico", 1);
			} else {
				jo.put("estratifico", 0);
			}

			jo.put("tipoServicio", TipoServicio);
			jo.put("genero", Genero);

			if (Correo.equalsIgnoreCase("")) {
				if (!Login.equalsIgnoreCase("") && validarLogin) {
					String correoUne = Login.toLowerCase();
					jo.put("email", correoUne + "@une.net.co");
				} else {
					jo.put("email", "cliente@sincorreo.com");
				}
			} else {
				jo.put("email", Correo);
			}

			jo.put("login", Login.toLowerCase());
			jo.put("pinhotpack", pinhp);
			jo.put("tipoCliente", tipoCliente);
			jo.put("observacion", observacion.replace(";", ".,"));
			jo.put("estrato", Estrato);
			jo.put("tipoServicio", "Hogares");
			jo.put("validarCorreo", validarCorreo);
			jo.put("validarLogin", validarLogin);

			try {
				if (logSmartPromoEnv != null) {
					jo.put("logSmartPromoEnv", new JSONObject(logSmartPromoEnv));
				}
			} catch (JsonParseException e) {
				Log.w("Error", e.getMessage());

			}

			try {
				if (logSmartPromoRes != null) {
					jo.put("logSmartPromoRes", new JSONObject(logSmartPromoRes));
				}
			} catch (JsonParseException e) {
				Log.w("Error", e.getMessage());

			}

			jo.put("departamentoExpedicion", DepartamentoExpedicion);

			/*
			 * jo.put("Telefono", Telefono); jo.put("TelefonoDestino",
			 * TelefonoDestino); jo.put("Telefono2", Telefono2);
			 * jo.put("Celular", Celular); jo.put("Celular2", Celular2);
			 */

			/*
			 * jo.put("Barrio", Barrio); jo.put("Direccion", Direccion);
			 * jo.put("Paginacion", Paginacion); jo.put("Contrato", Contrato);
			 * jo.put("PuntoReferencia", PuntoReferencia);
			 */

			// jo.put("Departamento", departamento);
			// jo.put("Ciudad", ciudad);
			/*
			 * jo.put("Correo", Correo); jo.put("Login", Login);
			 * jo.put("tipoCliente", tipoCliente);
			 */

			if (NivelEstudio != null) {
				jo.put("nivelEstudio", Utilidades.limpiar_opcion(NivelEstudio));
			} else {
				jo.put("nivelEstudio", "N/A");
			}

			if (Profesion != null) {
				jo.put("profesion", Utilidades.limpiar_opcion(Profesion));
			} else {
				jo.put("profesion", "N/A");
			}

			if (TipoPredio != null) {
				jo.put("tipoPredio", Utilidades.limpiar_opcion(TipoPredio));
			} else {
				jo.put("tipoPredio", "N/A");
			}

			if (TipoPropiedad != null) {
				jo.put("tipoPropiedad", Utilidades.limpiar_opcion(TipoPropiedad));
			} else {
				jo.put("tipoPropiedad", "N/A");
			}

			if (NivelIngresos != null) {
				jo.put("nivelIngresos", Utilidades.limpiar_opcion(NivelIngresos));
			} else {
				jo.put("nivelIngresos", "N/A");
			}

			if (Ocupacion != null) {
				jo.put("ocupacion", Utilidades.limpiar_opcion(Ocupacion));
			} else {
				jo.put("ocupacion", "N/A");
			}

			if (TipoVivienda != null) {
				jo.put("tipoVivienda", Utilidades.limpiar_opcion(TipoVivienda));
			} else {
				jo.put("tipoVivienda", "N/A");
			}

			if (EstadoCivil != null) {
				jo.put("estadoCivil", Utilidades.limpiar_opcion(EstadoCivil));
			} else {
				jo.put("estadoCivil", "N/A");
			}

			if (PersonasCargo != null) {
				jo.put("personasCargo", Utilidades.limpiar_opcion(PersonasCargo));
			} else {
				jo.put("personasCargo", "N/A");
			}

			if (Cargo != null) {
				jo.put("cargo", Utilidades.limpiar_opcion(Cargo));
			} else {
				jo.put("cargo", "N/A");
			}

			if (NombrePredio != null) {
				jo.put("nombrePredio", Utilidades.limpiar_opcion(NombrePredio));
			} else {
				jo.put("nombrePredio", "N/A");
			}

			if (MunicipioSSC != null) {
				jo.put("municipioSSC", Utilidades.limpiar_opcion(MunicipioSSC));
			} else {
				jo.put("municipioSSC", "N/A");
			}

			if (TipoConstruccion != null) {
				jo.put("TipoConstruccion", Utilidades.limpiar_opcion(TipoConstruccion));
			} else {
				jo.put("TipoConstruccion", "N/A");
			}

			if (Migracion4G != null) {
				jo.put("Migracion4G", Utilidades.limpiar_opcion(Migracion4G));
			} else {
				jo.put("Migracion4G", "N/A");
			}

			if (crmCarrusel != null) {
				jo.put("crmCarrusel", crmCarrusel);
			} else {
				jo.put("crmCarrusel", "");
			}

			if (productosCarrusel != null) {
				jo.put("productosCarrusel", productosCarrusel);
			} else {
				jo.put("productosCarrusel", "");
			}

			Telefono = Utilidades.limpiarTelefono(Telefono);

			objectTelefonos.add(Utilidades.jsonTelefonos("Servicio", Telefono));
			objectTelefonos.add(Utilidades.jsonTelefonos("IVR", TelefonoDestino));
			objectTelefonos.add(Utilidades.jsonTelefonos("Telefono2", Telefono2));
			objectTelefonos.add(Utilidades.jsonTelefonos("Celular", Celular));

			for (int i = 0; i < objectTelefonos.size(); i++) {
				arrayTelefonos.put(objectTelefonos.get(i));
			}

			// System.out.println(Utilidades.jsonDirecciones(
			// facturacion.getMunicipio(), facturacion.getDepartamento(),
			// facturacion.getBarrio(), "", "Facturacion",
			// facturacion.getDireccion(), "N/A", "N/A", "N/A"));

			if (DireccionNormalizada == null) {
				DireccionNormalizada = "";
				DireccionConsulta = "";

			}

			System.out.println("DireccionNormalizada " + DireccionNormalizada);

			objectDirecciones.add(Utilidades.jsonDirecciones(ciudad, departamento, Barrio, CodigoBarrio, "Servicio",
					Direccion, DireccionNormalizada, DireccionConsulta, Contrato, Paginacion, paginaAsignacion,
					PuntoReferencia));

			objectDirecciones.add(Utilidades.jsonDirecciones(facturacion.getMunicipio(), facturacion.getDepartamento(),
					facturacion.getBarrio(), "", "Facturacion", facturacion.getDireccion(), "N/A", "N/A", "N/A", "N/A",
					"", "N/A"));

			for (int i = 0; i < objectDirecciones.size(); i++) {
				arrayDirecciones.put(objectDirecciones.get(i));
			}

			objectContactos.add(Utilidades.jsonContactos(contacto1.getNombres(), contacto1.getApellidos(),
					contacto1.getTelefono(), contacto1.getCelular(), contacto1.getParentesco(), 1));
			objectContactos.add(Utilidades.jsonContactos(contacto2.getNombres(), contacto2.getApellidos(),
					contacto2.getTelefono(), contacto2.getCelular(), contacto2.getParentesco(), 2));

			for (int i = 0; i < objectContactos.size(); i++) {
				arrayContactos.put(objectContactos.get(i));
			}

			/*
			 * jo.put("genero", Genero); jo.put("nombrePredio", NombrePredio);
			 * jo.put("NombreContacto1", contacto1.getNombres());
			 * jo.put("ApellidoContacto1", contacto1.getApellidos());
			 * jo.put("tcontacto1", contacto1.getTelefono());
			 * jo.put("ccontacto1", contacto1.getCelular());
			 * jo.put("pcontacto1", contacto1.getParentesco());
			 * jo.put("NombreContacto2", contacto2.getNombres());
			 * jo.put("ApellidoContacto2", contacto2.getApellidos());
			 * jo.put("tcontacto2", contacto2.getTelefono());
			 * jo.put("ccontacto2", contacto2.getCelular());
			 * jo.put("pcontacto2", contacto2.getParentesco());
			 *
			 * jo.put("departamentoFacturacion", facturacion.getDepartamento());
			 * jo.put("municipioFacturacion", facturacion.getMunicipio());
			 * jo.put("direccionFacturacion", facturacion.getDireccion());
			 * jo.put("barrioFacturacion", facturacion.getBarrio());
			 */
			jo.put("telefonos", arrayTelefonos);
			jo.put("direcciones", arrayDirecciones);
			jo.put("contactos", arrayContactos);

			if (tipoCuenta.equalsIgnoreCase("Scooring")) {
				// if (Utilidades.excluir("habilitarScooring", ciudad)) {

				System.out.println("scooringune.isPasaScooring() " + scooringune.isPasaScooring());

				if (!scooringune.isNoAplicaScooring()) {
					if (scooringune.isPasaScooring() || scooringune.isProspectoScooring()) {

						System.out.println("scooringune.getIdScooring() " + scooringune.getIdScooring());

						System.out.println("scooringune.getEstado() " + scooringune.getEstado());

						jo.put("idScooring", scooringune.getIdScooring());
						jo.put("estadoScooring", scooringune.getEstado());
						jo.put("origenScooring", scooringune.getOrigenScooring());
						jo.put("razonScooring", scooringune.getRazonScooring());
					} else if (scooringune.isPendienteRespuesta()) {
						jo.put("idScooring", scooringune.getIdScooring());
						jo.put("estadoScooring", scooringune.getEstado());
						jo.put("razonScooring", scooringune.getEstado());
					} else {
						jo.put("idScooring", "");
						jo.put("estadoScooring", "");
						jo.put("razonScooring", "");
					}
				} else {
					jo.put("idScooring", scooringune.getIdScooring());
					jo.put("estadoScooring", "N/A");
					jo.put("razonScooring", "N/A");
				}
			}

			jo.put("smartPromo", smartPromo);

			if (domiciliacion != null && !domiciliacion.equals(Utilidades.inicial_opcion)) {
				jo.put("domiciliacion", domiciliacion);
				jo.put("estadoDomiciliacion", bloqueoDomiciliacion);
			} else {
				jo.put("domiciliacion", "NO");
				jo.put("bloqueoDomiciliacion", "0");
			}

			System.out.println("BanderaPA " + pagoAnticipado);

			jo.put("pagoAnticipado",pagoAnticipado);

		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}
		return jo;

	}

	public String changeStringsDefault(String data) {
		String dataResult = "";
		if (data.equals("-- Seleccione Opción --")) {
			dataResult = "-";
		} else {
			dataResult = data;
		}
		return dataResult;
	}

	public int getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(int tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getCedula() {
		return Cedula;
	}

	public void setCedula(String cedula) {
		Cedula = cedula;
	}

	public String getExpedicion() {
		return Expedicion;
	}

	public void setExpedicion(String expedicion) {
		Expedicion = expedicion;
	}

	public String getNombre() {
		return Nombre;
	}

	public String getApellido() {
		return Apellido;
	}

	public String getLugarExpedicion() {
		return LugarExpedicion;
	}

	public String getDepartamentoExpedicion() {
		return DepartamentoExpedicion;
	}

	public void setDepartamentoExpedicion(String departamentoExpedicion) {
		DepartamentoExpedicion = departamentoExpedicion;
	}

	public void setLugarExpedicion(String lugarExpedicion) {
		LugarExpedicion = lugarExpedicion;
	}

	public String getDaneLugarExpedicion() {
		return DaneLugarExpedicion;
	}

	public void setDaneLugarExpedicion(String daneLugarExpedicion) {
		DaneLugarExpedicion = daneLugarExpedicion;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public void setApellido(String apellido) {
		Apellido = apellido;
	}

	public String getTelefono() {
		return Telefono;
	}

	public void setTelefono(String telefono) {
		if (!MainActivity.config.getDepartamento().equals("Antioquia")) {
			if (!this.Telefono.startsWith("(")) {
				this.Telefono = MainActivity.config.getIdentificador() + telefono;
			} else {
				this.Telefono = telefono;
			}

		} else {
			this.Telefono = telefono;
		}
	}

	public String getTelefonoDestino() {
		return TelefonoDestino;
	}

	public void setTelefonoDestino(String telefonoDestino) {
		TelefonoDestino = telefonoDestino;
	}

	public boolean isBloquearTelefonoDestino() {
		return bloquearTelefonoDestino;
	}

	public void setBloquearTelefonoDestino(boolean bloquearTelefonoDestino) {
		this.bloquearTelefonoDestino = bloquearTelefonoDestino;
	}

	public String getTelefono2() {
		return Telefono2;
	}

	public void setTelefono2(String telefono2) {
		Telefono2 = telefono2;
	}

	public String getCelular() {
		return Celular;
	}

	public void setCelular(String celular) {
		Celular = celular;
	}

	public String getCelular2() {
		return Celular2;
	}

	public void setCelular2(String celular2) {
		Celular2 = celular2;
	}

	public String getEstrato() {
		return Estrato;
	}

	public void setEstrato(String estrato) {
		Estrato = estrato;
	}

	public int getEstratifico() {
		return Estratifico;
	}

	public boolean isControlEstrato() {
		return controlEstrato;
	}

	public void setControlEstrato(boolean controlEstrato) {
		this.controlEstrato = controlEstrato;
	}

	public void setEstratifico(int estratifico) {
		Estratifico = estratifico;
	}

	public String getBarrio() {
		return Barrio;
	}

	public void setBarrio(String barrio) {
		Barrio = barrio;
	}

	public String getCodigoBarrio() {
		return CodigoBarrio;
	}

	public void setCodigoBarrio(String codigoBarrio) {
		CodigoBarrio = codigoBarrio;
	}

	public String getDireccion() {
		return Direccion;
	}

	public void setDireccion(String direccion) {
		Direccion = direccion;
	}

	public String getDireccionNormalizada() {
		return DireccionNormalizada;
	}

	public void setDireccionNormalizada(String direccionNormalizada) {
		DireccionNormalizada = direccionNormalizada;
	}

	public String getDireccionConsulta() {
		return DireccionConsulta;
	}

	public void setDireccionConsulta(String direccionConsulta) {
		DireccionConsulta = direccionConsulta;
	}

	public String getIdDireccionGis() {
		return IdDireccionGis;
	}

	public void setIdDireccionGis(String idDireccionGis) {
		IdDireccionGis = idDireccionGis;
	}

	public String getLatitud() {
		return Latitud;
	}

	public void setLatitud(String latitud) {
		Latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getIdDireccionGisEx() {
		return IdDireccionGisEx;
	}

	public void setIdDireccionGisEx(String idDireccionGisEx) {
		IdDireccionGisEx = idDireccionGisEx;
	}

	public String getPaginacion() {
		return Paginacion;
	}

	public void setPaginacion(String paginacion) {
		Paginacion = paginacion;
	}

	public String getPaginaAsignacion() {
		return paginaAsignacion;
	}

	public void setPaginaAsignacion(String paginaAsignacion) {
		this.paginaAsignacion = paginaAsignacion;
	}

	public String getContrato() {
		return Contrato;
	}

	public void setContrato(String contrato) {
		Contrato = contrato;
	}

	public String getPuntoReferencia() {
		return PuntoReferencia;
	}

	public void setPuntoReferencia(String puntoReferencia) {
		PuntoReferencia = puntoReferencia;
	}

	public String getTipoServicio() {
		return TipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		TipoServicio = tipoServicio;
	}

	public String getCorreo() {
		return Correo;
	}

	public void setCorreo(String correo) {
		Correo = correo;
	}

	public String getDominioCorreo() {
		return dominioCorreo;
	}

	public void setDominioCorreo(String dominioCorreo) {
		this.dominioCorreo = dominioCorreo;
	}

	public String getUsermail() {
		return usermail;
	}

	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}

	public String getLogin() {
		return Login;
	}

	public void setLogin(String login) {
		Login = login;
	}

	public boolean isValidarLogin() {
		return validarLogin;
	}

	public String getPinhp() {
		return pinhp;
	}

	public void setPinhp(String pinhp) {
		this.pinhp = pinhp;
	}

	public void setValidarLogin(boolean validarLogin) {
		this.validarLogin = validarLogin;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCiudadFenix() {
		return ciudadFenix;
	}

	public void setCiudadFenix(String ciudadFenix) {
		this.ciudadFenix = ciudadFenix;
	}

	public String getCiudadDane() {
		return ciudadDane;
	}

	public void setCiudadDane(String ciudadDane) {
		this.ciudadDane = ciudadDane;
	}

	public String getCiudadSiebel() {
		return ciudadSiebel;
	}

	public void setCiudadSiebel(String ciudadSiebel) {
		this.ciudadSiebel = ciudadSiebel;
	}

	public String getDepartamentoSiebel() {
		return departamentoSiebel;
	}

	public void setDepartamentoSiebel(String departamentoSiebel) {
		this.departamentoSiebel = departamentoSiebel;
	}

	public String getCiudadAmc() {
		return ciudadAmc;
	}

	public void setCiudadAmc(String ciudadAmc) {
		this.ciudadAmc = ciudadAmc;
	}

	public String getConsolidado() {
		return consolidado;
	}

	public void setConsolidado(String consolidado) {
		this.consolidado = consolidado;
	}

	public String getPortafolio() {
		return portafolio;
	}

	public void setPortafolio(String portafolio) {
		this.portafolio = portafolio;
	}

	public String getCobertura() {
		return cobertura;
	}

	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	public String getCarteraUNE() {
		return carteraUNE;
	}

	public void setCarteraUNE(String carteraUNE) {
		this.carteraUNE = carteraUNE;
	}

	public String getTipoDocumento() {
		return TipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		TipoDocumento = tipoDocumento;
	}

	public String getFechaNacimiento() {
		return FechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		FechaNacimiento = fechaNacimiento;
	}

	public String getNivelEstudio() {
		return NivelEstudio;
	}

	public void setNivelEstudio(String nivelEstudio) {
		NivelEstudio = nivelEstudio;
	}

	public String getProfesion() {
		return Profesion;
	}

	public void setProfesion(String profesion) {
		Profesion = profesion;
	}

	public String getOcupacion() {
		return Ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		Ocupacion = ocupacion;
	}

	public String getCargo() {
		return Cargo;
	}

	public void setCargo(String cargo) {
		Cargo = cargo;
	}

	public String getNivelIngresos() {
		return NivelIngresos;
	}

	public void setNivelIngresos(String nivelIngresos) {
		NivelIngresos = nivelIngresos;
	}

	public String getEstadoCivil() {
		return EstadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		EstadoCivil = estadoCivil;
	}

	public String getGenero() {
		return Genero;
	}

	public void setGenero(String genero) {
		Genero = genero;
	}

	public String getPersonasCargo() {
		return PersonasCargo;
	}

	public void setPersonasCargo(String personasCargo) {
		PersonasCargo = personasCargo;
	}

	public String getTipoVivienda() {
		return TipoVivienda;
	}

	public void setTipoVivienda(String tipoVivienda) {
		TipoVivienda = tipoVivienda;
	}

	public String getTipoPredio() {
		return TipoPredio;
	}

	public void setTipoPredio(String tipoPredio) {
		TipoPredio = tipoPredio;
	}

	public String getTipoPropiedad() {
		return TipoPropiedad;
	}

	public void setTipoPropiedad(String tipoPropiedad) {
		TipoPropiedad = tipoPropiedad;
	}

	public String getTipoConstruccion() {
		return TipoConstruccion;
	}

	public void setTipoConstruccion(String tipoConstruccion) {
		TipoConstruccion = tipoConstruccion;
	}

	public String getMigracion4G() {
		return Migracion4G;
	}

	public void setMigracion4G(String migracion4g) {
		Migracion4G = migracion4g;
	}

	public String getNombrePredio() {
		return NombrePredio;
	}

	public void setNombrePredio(String nombrePredio) {
		NombrePredio = nombrePredio;
	}

	public Contacto getContacto1() {
		return contacto1;
	}

	public void setContacto1(Contacto contacto1) {
		this.contacto1 = contacto1;
	}

	public Contacto getContacto2() {
		return contacto2;
	}

	public void setContacto2(Contacto contacto2) {
		this.contacto2 = contacto2;
	}

	public ScooringUne getScooringune() {
		return scooringune;
	}

	public void setScooringune(ScooringUne scooringune) {
		this.scooringune = scooringune;
	}

	public Facturacion getFacturacion() {
		return facturacion;
	}

	public void setFacturacion(Facturacion facturacion) {
		this.facturacion = facturacion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIdIVR() {
		return idIVR;
	}

	public void setIdIVR(int idIVR) {
		this.idIVR = idIVR;
	}

	public String getCodigoHogar() {
		return CodigoHogar;
	}

	public void setCodigoHogar(String codigoHogar) {
		CodigoHogar = codigoHogar;
	}

	public String getMunicipioSSC() {
		return MunicipioSSC;
	}

	public void setMunicipioSSC(String municipioSSC) {
		MunicipioSSC = municipioSSC;
	}

	public boolean isValidarCorreo() {
		return validarCorreo;
	}

	public void setValidarCorreo(boolean validarCorreo) {
		this.validarCorreo = validarCorreo;
	}

	public boolean isControlNormalizada() {
		return controlNormalizada;
	}

	public void setControlNormalizada(boolean controlNormalizada) {
		this.controlNormalizada = controlNormalizada;
	}

	public boolean isConsultaNormalizada() {
		return consultaNormalizada;
	}

	public void setConsultaNormalizada(boolean consultaNormalizada) {
		this.consultaNormalizada = consultaNormalizada;
	}

	public void generarAmigoCuentasNuevo() {
		ac = new AmigoCuentas();
	}

	public boolean isControlClienteSiebel() {
		return controlClienteSiebel;
	}

	public void setControlClienteSiebel(boolean controlClienteSiebel) {
		this.controlClienteSiebel = controlClienteSiebel;
	}

	public int getEstandarizarSiebel() {
		return estandarizarSiebel;
	}

	public void setEstandarizarSiebel(int estandarizarSiebel) {
		this.estandarizarSiebel = estandarizarSiebel;
	}

	public boolean isControlEstadoCuenta() {
		return controlEstadoCuenta;
	}

	public void setControlEstadoCuenta(boolean controlEstadoCuenta) {
		this.controlEstadoCuenta = controlEstadoCuenta;
	}

	public boolean isControlCerca() {
		return controlCerca;
	}

	public void setControlCerca(boolean controlCerca) {
		this.controlCerca = controlCerca;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public String getSmartPromo() {
		return smartPromo;
	}

	public void setSmartPromo(String smartPromo) {
		this.smartPromo = smartPromo;
	}

	public String getHogarNuevo() {
		return hogarNuevo;
	}

	public void setHogarNuevo(String hogarNuevo) {
		this.hogarNuevo = hogarNuevo;
	}

	public String getTecnologia() {
		return tecnologia;
	}

	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}

	public boolean isBloqueoTecnologia() {
		return bloqueoTecnologia;
	}

	public void setBloqueoTecnologia(boolean bloqueoTecnologia) {
		this.bloqueoTecnologia = bloqueoTecnologia;
	}

	public String getDomiciliacion() {
		return domiciliacion;
	}

	public void setDomiciliacion(String domiciliacion) {
		this.domiciliacion = domiciliacion;
	}

	public String getBloqueoDomiciliacion() {
		return bloqueoDomiciliacion;
	}

	public void setBloqueoDomiciliacion(String bloqueoDomiciliacion) {
		this.bloqueoDomiciliacion = bloqueoDomiciliacion;
	}

	public String getlogSmartPromoRes() {
		return logSmartPromoRes;
	}

	public void setlogSmartPromoRes(String logSmartPromoRes) {
		this.logSmartPromoRes = logSmartPromoRes;
	}

	public String getlogSmartPromoEnv() {
		return logSmartPromoEnv;
	}

	public void setlogSmartPromoEnv(String logSmartPromoEnv) {
		this.logSmartPromoEnv = logSmartPromoEnv;
	}

	public ArrayList<ItemCarrusel> getArraycarrusel() {
		return arraycarrusel;
	}

	public void setArraycarrusel(ArrayList<ItemCarrusel> arraycarrusel) {
		this.arraycarrusel = arraycarrusel;
	}

	public boolean isCarrusel() {
		return carrusel;
	}

	public void setCarrusel(boolean carrusel) {
		this.carrusel = carrusel;
	}

	public String getCodigoCarrusel() {
		return codigoCarrusel;
	}

	public void setCodigoCarrusel(String codigoCarrusel) {
		this.codigoCarrusel = codigoCarrusel;
	}

	public String getDireccionCarrusel() {
		return direccionCarrusel;
	}

	public void setDireccionCarrusel(String direccionCarrusel) {
		this.direccionCarrusel = direccionCarrusel;
	}

	public String getDocumentoCarrusel() {
		return documentoCarrusel;
	}

	public void setDocumentoCarrusel(String documentoCarrusel) {
		this.documentoCarrusel = documentoCarrusel;
	}

	public boolean isControlCarrusel() {
		return controlCarrusel;
	}

	public void setControlCarrusel(boolean controlCarrusel) {
		this.controlCarrusel = controlCarrusel;
	}

	public String getCrmCarrusel() {
		return crmCarrusel;
	}

	public void setCrmCarrusel(String crmCarrusel) {
		this.crmCarrusel = crmCarrusel;
	}

	public String getProductosCarrusel() {
		return productosCarrusel;
	}

	public void setProductosCarrusel(String productosCarrusel) {
		this.productosCarrusel = productosCarrusel;
	}

	public boolean isConfronta() {
		return confronta;
	}

	public void setConfronta(boolean confronta) {
		this.confronta = confronta;
	}

	public boolean isRealizoConfronta() {
		return realizoConfronta;
	}

	public void setRealizoConfronta(boolean realizoConfronta) {
		this.realizoConfronta = realizoConfronta;
	}

	public void setPagoAnticipado(String pagoAnticipado){
		this.pagoAnticipado = pagoAnticipado;
	}

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		ArrayList<Object> resultado = null;
		System.out.println("value update cliente " + value);
		if (value != null) {

			if (value.equals("Oferta")) {
				try {

					Log.e("AMC", "prueba " + this.ac.portafolio);
					if (this.ac != null) {
						if (this.ac.portafolio != null) {
							JSONObject portafolio = new JSONObject(this.ac.portafolio);
							JSONObject infoCliente = portafolio.getJSONObject("infoCliente");
							this.Direccion = infoCliente.getString("direccion");
							System.out.println("Direccion " + this.Direccion);
							this.ciudad = Utilidades.traducirCiudadFenix(infoCliente.getString("municipioId"));
							System.out.println("ciudad " + this.ciudad);

							if (infoCliente.getString("departamento").equalsIgnoreCase("Bogota D.C")) {
								this.departamento = "Distrito Capital De Bogota";
							} else if (infoCliente.getString("departamento").equalsIgnoreCase("Valle del Cauca")) {
								this.departamento = infoCliente.getString("departamentoProd");
							} else {
								this.departamento = infoCliente.getString("departamento");

							}

							System.out.println("departamento " + this.departamento);
							this.Estrato = infoCliente.getString("estrato");
							System.out.println("Estrato " + this.Estrato);
							this.Apellido = infoCliente.getString("apellidos");
							System.out.println("Apellido " + this.Apellido);
							this.Nombre = infoCliente.getString("nombres");
							System.out.println("Nombre " + this.Nombre);
							this.CodigoHogar = infoCliente.getString("codigoHogar");
							System.out.println("CodigoHogar " + this.CodigoHogar);
							this.Telefono = infoCliente.getString("identificador");
							System.out.println("Telefono " + this.Telefono);
							this.Paginacion = infoCliente.getString("paginaServicio");
							System.out.println("Paginacion " + this.Paginacion);
							this.TipoServicio = infoCliente.getString("usoServicio");
							System.out.println("TipoServicio " + this.TipoServicio);
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (value.equals("Selector")) {

			} else {
				try {
					resultado = (ArrayList<Object>) value;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		if (resultado != null) {
			if (resultado.get(0).equals("Portafolio")) {
				portafolio = resultado.get(1).toString();
				if (portafolio.equals("Ha superado el limite de consultas por dia")
						|| portafolio.equals("Consulta Hecha fuera del horario establecido")) {
					Toast.makeText(MainActivity.context, portafolio, Toast.LENGTH_SHORT).show();
				}

			} else if (resultado.get(0).equals("CoberturaNew")) {
				cobertura = resultado.get(1).toString();
				String tec = Utilidades.obtenerCobertura(cobertura, MainActivity.context);
				if (!tec.equalsIgnoreCase("N/D")) {
					tecnologia = tec;
					if (Utilidades.camposUnicosCiudad("bloqueoTecnologia", getCiudad()).equals("true")) {
						setBloqueoTecnologia(true);
					}
				} else {
					tecnologia = Utilidades.inicial_opcion;
					if (Utilidades.camposUnicosCiudad("bloqueoTecnologia", getCiudad()).equals("true")) {
						setBloqueoTecnologia(true);
					}
				}

				System.out.println("Tecnologia " + tecnologia);
				if (cobertura.equals("Ha superado el limite de consultas por dia")
						|| cobertura.equals("Consulta Hecha fuera del horario establecido")) {
					Toast.makeText(MainActivity.context, cobertura, Toast.LENGTH_SHORT).show();
				}
			} else if (resultado.get(0).equals("Cartera_UNE")) {
				carteraUNE = resultado.get(1).toString();
				if (carteraUNE.equals("Ha superado el limite de consultas por dia")
						|| carteraUNE.equals("Consulta Hecha fuera del horario establecido")) {
					Toast.makeText(MainActivity.context, carteraUNE, Toast.LENGTH_SHORT).show();
				}
			} else if (resultado.get(0).equals("Cliente")) {
				// System.out.println("Update Cliente => " + resultado);
			}
		}

	}

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		// System.out.println("addobserver");
		observador = o;
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observador = null;
	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub
		// System.out.println(observador);
		observador.update(Estrato);
	}

}