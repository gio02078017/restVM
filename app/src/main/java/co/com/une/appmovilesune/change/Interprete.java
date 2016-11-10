package co.com.une.appmovilesune.change;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemDirecciones;
import co.com.une.appmovilesune.adapters.ListaDefault;
import android.app.Activity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Interprete {

	public static String Validacion_Cifin = "";
	public static String Datos_Cifin = "";

	private static String Validacion_Cartera = "", Mensaje_Cartera = "";

	private static ArrayList<String[]> datos_cliente = new ArrayList<String[]>();

	private static ArrayList<Ofertas> ofertas = new ArrayList<Ofertas>();

	private static ArrayList<ListaDefault> Portafolio = new ArrayList<ListaDefault>();
	String Direccion = "", Paginacion = "", Estrato = "", Telefono = "", Cedula = "";

	public Interprete() {

	}

	public static ArrayList<ListaDefault> Mensajes(String interpretar) {

		ArrayList<ListaDefault> mensajes = new ArrayList<ListaDefault>();

		try {

			System.out.println("Interprete Mensajes -" + interpretar);

			JSONObject jsonObject = new JSONObject(interpretar);

			String Titulo = jsonObject.getString("Titulo");

			mensajes.add(new ListaDefault(0, Titulo, "Titulo"));

			JSONArray array = jsonObject.getJSONArray("Mensajes");

			for (int i = 0; i < array.length(); i++) {
				String titulo = array.getJSONObject(i).getString("titulo");
				String datos = array.getJSONObject(i).getString("datos");
				mensajes.add(new ListaDefault(2, titulo, datos));
			}

		} catch (JSONException e) {
			Log.w("Error", "Mensaje " + e.getMessage());

		}

		return mensajes;

	}

	public static ArrayList<ListaDefault> MensajesVacio() {

		ArrayList<ListaDefault> mensajes = new ArrayList<ListaDefault>();

		mensajes.add(new ListaDefault(0, "Error", "Titulo"));
		mensajes.add(new ListaDefault(2, "Respuesta", "Vacia"));

		mensajes.add(new ListaDefault(2, "Recomendación",
				"Intente Nueva Mente y de continuar el inconveniente Informe al analista del sistema"));

		return mensajes;

	}

	public static ArrayList<ListaDefault> Estado_Oferta(String Resultado) {
		ArrayList<ListaDefault> estadopedido = new ArrayList<ListaDefault>();

		System.out.println("Resultado " + Resultado);

		try {
			JSONObject jsonObject = new JSONObject(Resultado);

			if (jsonObject.has("codigoRespuesta")) {

				if (jsonObject.getString("codigoRespuesta").equalsIgnoreCase("00")) {

					if (jsonObject.has("nombreCliente")) {
						estadopedido.add(new ListaDefault(0, "Cliente", "Titulo"));

						estadopedido.add(new ListaDefault(1, "Documento", jsonObject.getString("documento")));

						estadopedido.add(new ListaDefault(2, "Nombre", jsonObject.getString("nombreCliente")));
					}

					if (jsonObject.has("ofertaId")) {
						estadopedido.add(new ListaDefault(0, "Oferta", "Titulo"));

						estadopedido.add(new ListaDefault(1, "Id Oferta", jsonObject.getString("ofertaId")));

						estadopedido.add(new ListaDefault(2, "Estado", jsonObject.getString("estadoOferta")));
					}

					if (jsonObject.has("pedido")) {

						estadopedido.add(new ListaDefault(0, "Pedido", "Titulo"));

						estadopedido.add(new ListaDefault(1, "Id Pedido",
								jsonObject.getJSONObject("pedido").getString("pedidoId")));

						estadopedido.add(new ListaDefault(1, "Estado",
								jsonObject.getJSONObject("pedido").getString("pedidoEstado")));
					}

					if (jsonObject.has("observaciones")) {
						estadopedido.add(new ListaDefault(0, "Observaciones", "Titulo"));

						estadopedido.add(new ListaDefault(1, "Observacion", jsonObject.getString("observaciones")));
					}

					if (jsonObject.has("agenda")) {

						if (jsonObject.get("agenda").getClass().getSimpleName().equals("JSONObject")) {
							if (jsonObject.getJSONObject("agenda").has("fecha")) {
								estadopedido.add(new ListaDefault(0, "Agenda", "Titulo"));

								estadopedido.add(new ListaDefault(1, "Fecha",
										jsonObject.getJSONObject("agenda").getString("fecha")));

								estadopedido.add(new ListaDefault(2, "Franja",
										jsonObject.getJSONObject("agenda").getString("franja")));

								estadopedido.add(new ListaDefault(1, "Hora Inicial",
										jsonObject.getJSONObject("agenda").getString("horaInicial")));

								estadopedido.add(new ListaDefault(2, "Hora Final",
										jsonObject.getJSONObject("agenda").getString("horaFin")));

							}
						}
					}

				} else {
					estadopedido.add(new ListaDefault(0, "Mensaje", "Titulo"));

					estadopedido.add(new ListaDefault(1, "mensaje", jsonObject.getString("descripcionMensaje")));

				}

				estadopedido.add(0, new ListaDefault(1, "SI", "Datos"));

			} else {
				estadopedido.clear();
				estadopedido.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
			}
		} catch (JSONException e) {
			Log.w("Error ", e.getMessage());
			estadopedido.clear();
			estadopedido.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
		}

		return estadopedido;

	}

	public static ArrayList<ListaDefault> Estado_Pedido(String Resultado) {
		ArrayList<ListaDefault> estadopedido = new ArrayList<ListaDefault>();

		if (Resultado.equalsIgnoreCase("-1")) {
			estadopedido.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tarde"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			estadopedido.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			estadopedido.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else {
			try {
				Resultado = Resultado.replace("\"", "'");
				// System.out.println("Estado Pedido 1 -" + Resultado);
				JSONObject jsonObject = new JSONObject(Resultado);
				if (jsonObject.has("Pedido")) {
					estadopedido.add(new ListaDefault(0, "Pedido", "Titulo"));
					estadopedido.add(new ListaDefault(1, "Pedido", jsonObject.getString("Pedido")));
				}

				if (jsonObject.has("Municipio")) {
					estadopedido.add(new ListaDefault(1, "Municipio",
							Utilidades.traducirCiudadFenix(jsonObject.getString("Municipio"))));
				}

				if (jsonObject.has("Productos")) {
					if (jsonObject.get("Productos").getClass().getSimpleName().equals("JSONArray")) {
						JSONArray array = jsonObject.getJSONArray("Productos");
						for (int i = 0; i < array.length(); i++) {
							estadopedido.add(
									new ListaDefault(0, array.getJSONObject(i).getString("producto_id"), "Titulo"));
							String Estado = array.getJSONObject(i).getString("estado_id");
							if (!array.getJSONObject(i).getString("descripcion").equalsIgnoreCase("")) {
								estadopedido.add(new ListaDefault(1, "Descripción",
										array.getJSONObject(i).getString("descripcion")));
							} else {
								estadopedido.add(new ListaDefault(1, "Descripción",
										array.getJSONObject(i).getString("estado_id")));
							}
							if (!Estado.equalsIgnoreCase("CUMPL") && !Estado.equalsIgnoreCase("FACTU")) {
								if (array.getJSONObject(i).has("fecha_estado")) {
									// System.out.println("Fecha estado array");
									estadopedido.add(new ListaDefault(1, "Fecha Estado",
											Utilidades.Parser_Fecha(array.getJSONObject(i).getString("fecha_estado"))));
								}
							} else {
								if (array.getJSONObject(i).has("fecha_instalacion")) {
									estadopedido.add(new ListaDefault(1, "Fecha Instalación", Utilidades
											.Parser_Fecha(array.getJSONObject(i).getString("fecha_instalacion"))));
								}
							}

						}
					} else if (jsonObject.get("Productos").getClass().getSimpleName().equals("JSONObject")) {
						JSONObject array = jsonObject.getJSONObject("Productos");
						estadopedido.add(new ListaDefault(0, array.getString("producto_id"), "Titulo"));
						String Estado = array.getString("estado_id");
						if (!array.getString("descripcion").equalsIgnoreCase("")) {
							estadopedido.add(new ListaDefault(1, "Descripción", array.getString("descripcion")));
						} else {
							estadopedido.add(new ListaDefault(1, "Descripción", array.getString("estado_id")));
						}
						if (!Estado.equalsIgnoreCase("CUMPL") && !Estado.equalsIgnoreCase("FACTU")) {
							if (array.has("fecha_estado")) {
								// System.out.println("Fecha estado objec");
								estadopedido.add(new ListaDefault(1, "Fecha Estado",
										Utilidades.Parser_Fecha(array.getString("fecha_estado"))));
							}
						} else {
							if (array.has("fecha_instalacion")) {
								estadopedido.add(new ListaDefault(1, "Fecha Instalación",
										Utilidades.Parser_Fecha(array.getString("fecha_instalacion"))));
							}
						}

					}
					estadopedido.add(0, new ListaDefault(1, "SI", "Datos"));
				}

				if (jsonObject.has("observaciones")) {

					/*
					 * estadopedido.add(new ListaDefault(0, "Pedido",
					 * "Titulo")); estadopedido.add(new ListaDefault(1,
					 * "Pedido", jsonObject .getString("Pedido")));
					 */
					JSONObject observaciones = jsonObject.getJSONObject("observaciones");

					System.out.println("observaciones " + observaciones);
					if (observaciones.has("codigoMensaje")) {
						if (observaciones.getString("codigoMensaje").equalsIgnoreCase("00")) {
							if (observaciones.has("observacion")) {
								System.out.println("pasa observacion ");
								JSONArray obJsonArray = observaciones.getJSONArray("observacion");

								if (obJsonArray.length() > 0) {
									// System.out.println("obJsonArray 0
									// "+obJsonArray.getString(0));
									// JSONObject obser =
									// obJsonArray.getString(0);
									if (obJsonArray.getJSONObject(0).has("observacion")) {
										estadopedido.add(new ListaDefault(0, "Observación", "Titulo"));

										estadopedido.add(new ListaDefault(2, "Observación",
												obJsonArray.getJSONObject(0).getString("observacion")));
									}
								}

							}
						}
					}
				}

				if (jsonObject.has("fechaAgenda")) {

					/*
					 * estadopedido.add(new ListaDefault(0, "Pedido",
					 * "Titulo")); estadopedido.add(new ListaDefault(1,
					 * "Pedido", jsonObject .getString("Pedido")));
					 */
					JSONObject fechaAgenda = jsonObject.getJSONObject("fechaAgenda");

					String fecha = "", franja = "";

					if (fechaAgenda.has("fechacita")) {
						fecha = fechaAgenda.getString("fechacita");

						if (fecha.equalsIgnoreCase("null")) {
							fecha = "";
						}
					}

					if (fechaAgenda.has("jornadacita")) {
						franja = fechaAgenda.getString("jornadacita");
						if (franja.equalsIgnoreCase("null")) {
							franja = "";
						}
					}

					if (!fecha.equalsIgnoreCase("") && !franja.equalsIgnoreCase("")) {
						estadopedido.add(new ListaDefault(0, "Agenda", "Titulo"));

						estadopedido.add(new ListaDefault(2, "Agenda", fecha + " " + franja));
					}

				}

			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				estadopedido.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
			}
		}

		return estadopedido;

	}

	public static ArrayList<ListaDefault> Cifin(String Resultado) {

		ArrayList<ListaDefault> cifin = new ArrayList<ListaDefault>();

		boolean ControlNombre = false;
		String Datos_Personales = "";

		Validacion_Cifin = "";
		Datos_Cifin = "";

		if (Resultado.equalsIgnoreCase("-1")) {
			cifin.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			cifin.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			cifin.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else {
			try {

				// System.out.println("Estado Cifin -" + Resultado);

				JSONObject jsonObject = new JSONObject(Resultado);

				if (jsonObject.has("codigoRespuesta")) {
					ControlNombre = false;

					String CodigoRespuesta = jsonObject.getString("codigoRespuesta");
					String MensajeRespuesta = jsonObject.getString("mensajeRespuesta");

					cifin.add(new ListaDefault(0, "VALIDADOR", "Titulo"));

					if (CodigoRespuesta.equalsIgnoreCase("0")) {

						if (jsonObject.has("cuerpoRespuesta")) {

							JSONObject cuerpoRespuesta = jsonObject.getJSONObject("cuerpoRespuesta");

							if (cuerpoRespuesta.has("tercero")) {
								cifin.add(new ListaDefault(6, "Datos Personales", "Titulo"));
								JSONObject tercero = cuerpoRespuesta.getJSONObject("tercero");
								String Identificador = tercero.getString("identificacion");
								cifin.add(new ListaDefault(1, "Identificación", Identificador));
								String FechaExpedicion = tercero.getString("fechaExpedicion");
								cifin.add(new ListaDefault(2, "Fecha Expedicion", FechaExpedicion));
								String Nombre = tercero.getString("nombre");
								cifin.add(new ListaDefault(1, "Nombre", Nombre));

								Nombre = Utilidades.quitaEspacios(Nombre);

								String[] Datos = Nombre.split(" ");

								Datos_Personales = "Nombre Usuario: " + Nombre + ", Identificación: " + Identificador
										+ ", Fecha Expedición: " + FechaExpedicion;
							}

							cifin.add(new ListaDefault(0, "Resultado Consulta", "Titulo"));

							String CodigoCalificacion = cuerpoRespuesta.getString("codigoCalificacion");

							String DescripcionEstado = cuerpoRespuesta.getString("descripcionEstado");
							cifin.add(new ListaDefault(1, "Descripcion Estado", DescripcionEstado));

							cifin.add(new ListaDefault(2, "Resultado", Validacion_Cifin));

							Datos_Cifin = "Respuesta Validador (" + Datos_Personales + " , " + Validacion_Cifin + ")";

						}
					}

					cifin.add(0, new ListaDefault(1, "SI", "Datos"));

				} else {
					Log.w("Advertencia", "datos no validos para el sistema");
					cifin.add(new ListaDefault(1, "NO", "datos no validos para el sistema"));
				}

			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				cifin.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));

			}

		}

		return cifin;

	}

	public static ArrayList<ListaDefault> Cobertura(String Resultado, String ciudad) {

		String Nodo = "", Armario = "", Caja = "", Cobertura_Adsl = "", Disponibilidad_Adsl = "";
		String Cobertura_hfc = "", Disponibilidad_hfc = "", Direccionalidad = "", TOIP = "", HFC_Digital = "",
				Velocidad = "";

		ArrayList<ListaDefault> cobertura = new ArrayList<ListaDefault>();

		if (Resultado.equalsIgnoreCase("-1")) {
			cobertura.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			cobertura.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			cobertura.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else if (Resultado.equals("Consulta Hecha fuera del horario establecido")
				|| Resultado.equals("Ha superado el limite de consultas por dia")) {
			cobertura.add(new ListaDefault(1, "NO", Resultado));
		} else {
			// System.out.println("Resultado " + Resultado);
			try {
				JSONObject jsonObject = new JSONObject(Resultado);

				System.out.println("cober " + jsonObject);

				cobertura.add(0, new ListaDefault(1, "SI", "Datos"));
				cobertura.add(new ListaDefault(0, "Resultado Cobertura", ""));

				if (jsonObject.getJSONObject("Cobertura").getString("Mensaje").equalsIgnoreCase("")
						&& Utilidades.excluir("deshabilitarCobertura", ciudad)) {
					cobertura.add(new ListaDefault(1, "Mensaje",
							"No es posible validar cobertura, Si esta seguro que la vivienda tiene cobertura continue con su venta, de lo contrario esta sera anulada."));

				} else {
					cobertura.add(
							new ListaDefault(1, "Mensaje", jsonObject.getJSONObject("Cobertura").getString("Mensaje")));

					JSONObject datos = jsonObject.getJSONObject("Cobertura");

					if (datos.has("COBERTURA_HFC")) {
						cobertura.add(new ListaDefault(2, "COBERTURA HFC", datos.getString("COBERTURA_HFC")));

						if (datos.has("DIRECCIONALIDAD")) {
							if (!datos.getString("DIRECCIONALIDAD").equalsIgnoreCase("null")) {
								cobertura.add(
										new ListaDefault(1, "DIRECCIONALIDAD", datos.getString("DIRECCIONALIDAD")));
							}
						}

						if (datos.has("DISPONIBILIDAD_HFC")) {
							if (!datos.getString("DISPONIBILIDAD_HFC").equalsIgnoreCase("null")) {
								cobertura.add(new ListaDefault(1, "DISPONIBILIDAD HFC",
										datos.getString("DISPONIBILIDAD_HFC")));
							}
						}
					}

					if (datos.has("HFC_DIGITAL")) {
						cobertura.add(new ListaDefault(1, "HFC DIGITAL", datos.getString("HFC_DIGITAL")));
					}

					if (datos.has("COBERTURA_GPON")) {
						cobertura.add(new ListaDefault(2, "COBERTURA GPON", datos.getString("COBERTURA_GPON")));

						if (datos.has("DISPONIBILIDAD_GPON")) {
							if (!datos.getString("DISPONIBILIDAD_GPON").equalsIgnoreCase("null")) {
								cobertura.add(new ListaDefault(1, "DISPONIBILIDAD GPON",
										datos.getString("DISPONIBILIDAD_GPON")));
							}
						}
					}

					if (datos.has("COBERTURA_REDCO")) {
						cobertura.add(new ListaDefault(2, "COBERTURA REDCO", datos.getString("COBERTURA_REDCO")));

						if (datos.has("DISPONIBILIDAD_REDCO")) {
							if (!datos.getString("DISPONIBILIDAD_REDCO").equalsIgnoreCase("null")) {
								cobertura.add(new ListaDefault(1, "DISPONIBILIDAD REDCO",
										datos.getString("DISPONIBILIDAD_REDCO")));
							}
						}

					}

					if (datos.has("NODO")) {
						cobertura.add(new ListaDefault(1, "NODO", datos.getString("NODO")));
					}

				}

				// if()

			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				cobertura.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
			}

		}

		return cobertura;
	}

	public static ArrayList<ListaDefault> CoberturaNodos(String Resultado) {

		String Nodo = "", Armario = "", Caja = "", Cobertura_Adsl = "", Disponibilidad_Adsl = "";
		String Cobertura_hfc = "", Disponibilidad_hfc = "", Direccionalidad = "", TOIP = "", HFC_Digital = "",
				Velocidad = "";

		ArrayList<ListaDefault> cobertura = new ArrayList<ListaDefault>();

		// System.out.println("Resultado " + Resultado);
		try {
			JSONArray ja = new JSONArray(Resultado);
			JSONObject jsonObject = ja.getJSONObject(0);
			cobertura.add(0, new ListaDefault(1, "SI", "Datos"));
			cobertura.add(new ListaDefault(0, "Resultado Cobertura", ""));
			cobertura.add(new ListaDefault(1, "Cobertura de Nodo " + jsonObject.getString("codigo"),
					"El nodo " + jsonObject.getString("respuesta") + " tiene cobertura"));
			if (jsonObject.getString("respuesta").equals("SI")) {
				MainActivity.btnCobertura.setOK();
			} else if (jsonObject.getString("respuesta").equals("NO")) {
				MainActivity.btnCobertura.setWRONG();

			}
		} catch (JSONException e) {

			Log.w("Error", "Mensaje " + e.getMessage());
			cobertura.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
		}

		return cobertura;
	}

	public static ArrayList<ListaDefault> Cobertura_Giis(String Resultado) {

		ArrayList<ListaDefault> cobertura = new ArrayList<ListaDefault>();

		if (Resultado.equalsIgnoreCase("-1")) {
			cobertura.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			cobertura.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			cobertura.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else {
			// System.out.println("Resultado GIIS" + Resultado);
			try {

				Resultado = Resultado.replace("\"", "'");

				JSONObject jsonObject = new JSONObject(Resultado);
				if (jsonObject.has("Cobertura")) {
					if (jsonObject.get("Cobertura").getClass().getSimpleName().equals("JSONArray")) {

						JSONArray array = jsonObject.getJSONArray("Cobertura");

						for (int i = 0; i < array.length(); i++) {
							String Titulo = array.getJSONObject(i).getString("Titulo");

							cobertura.add(new ListaDefault(0, Titulo, "Tecnologia"));
							String Cobertura = array.getJSONObject(i).getString("Cobertura");

							if (Cobertura.equalsIgnoreCase("SI")) {
								String Codigo = array.getJSONObject(i).getString("Codigo");

								cobertura.add(new ListaDefault(1, "Codigo", Codigo));

								String Tipo = array.getJSONObject(i).getString("Tipo");

								cobertura.add(new ListaDefault(2, "Tipo", Tipo));

								String Nombre = array.getJSONObject(i).getString("Nombre");

								cobertura.add(new ListaDefault(1, "Nombre", Nombre));

							} else if (Cobertura.equalsIgnoreCase("NO")) {

								String Mensaje = array.getJSONObject(i).getString("Mensaje");

								cobertura.add(new ListaDefault(1, "Cobertura :", Mensaje));
							}
						}

					} else if (jsonObject.get("Cobertura").getClass().getSimpleName().equals("JSONObject")) {

						JSONObject array = jsonObject.getJSONObject("Cobertura");

						String Titulo = array.getString("Titulo");

						cobertura.add(new ListaDefault(0, Titulo, "Tecnologia"));
						String Cobertura = array.getString("Cobertura");

						if (Cobertura.equalsIgnoreCase("SI")) {
							String Codigo = array.getString("Codigo");

							cobertura.add(new ListaDefault(1, "Codigo :", Codigo));

							String Tipo = array.getString("Tipo");

							cobertura.add(new ListaDefault(2, "Tipo", Tipo));

							String Nombre = array.getString("Nombre");

							cobertura.add(new ListaDefault(1, "Nombre", Nombre));

						} else if (Cobertura.equalsIgnoreCase("NO")) {

							String Mensaje = array.getString("Mensaje");

							cobertura.add(new ListaDefault(1, "Cobertura", Mensaje));
						}
					}
				}
				cobertura.add(0, new ListaDefault(1, "SI", "Datos"));

			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				cobertura.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
			}
		}

		return cobertura;
	}

	public static ArrayList<ListaDefault> Cartera(String Resultado) {

		ArrayList<ListaDefault> Cartera = new ArrayList<ListaDefault>();

		if (Resultado.equalsIgnoreCase("-1")) {
			Cartera.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			Cartera.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			Cartera.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else {
			try {
				JSONObject jsonObject = new JSONObject(Resultado);

				String Nombre = jsonObject.getString("Nombre");
				String Estado = jsonObject.getString("Dato");
				String Respuesta = jsonObject.getString("Respuesta");
				String Origen = jsonObject.getString("Origen");
				String idCliente = jsonObject.getString("idCliente");

				Validacion_Cartera = Respuesta;

				if (!Estado.equalsIgnoreCase("No Existe")) {
					Mensaje_Cartera = "Respuesta Cartera UNE ( Nombre Usuario: " + Nombre + ", " + Estado + ", "
							+ Respuesta + ")";
				} else {
					Mensaje_Cartera = "Respuesta Cartera UNE ( Nombre Usuario: " + Nombre + ", " + Respuesta + ")";
				}

				Cartera.add(new ListaDefault(0, "Nombre", Nombre));
				Cartera.add(new ListaDefault(0, "Estado", Estado));
				Cartera.add(new ListaDefault(0, "Respuesta", Respuesta));
				Cartera.add(new ListaDefault(0, "Origen", Origen));
				Cartera.add(new ListaDefault(0, "Mensaje_Cartera", Mensaje_Cartera));
				Cartera.add(new ListaDefault(0, "idCliente", idCliente));
				Cartera.add(0, new ListaDefault(1, "SI", "Datos"));

			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());

				Cartera.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
			}
		}

		return Cartera;
	}

	public static ArrayList<ListaDefault> Factura(String Resultado) {

		ArrayList<ListaDefault> Factura = new ArrayList<ListaDefault>();

		if (Resultado.equalsIgnoreCase("-1")) {
			Factura.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			Factura.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			Factura.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else {
			try {

				Resultado = Resultado.replace("\"", "'");

				// System.out.println("Estado Pedido 1 -" + Resultado);

				JSONObject jsonObject = new JSONObject(Resultado);

				if (jsonObject.has("TO")) {

					JSONObject array = jsonObject.getJSONObject("TO");

					Factura.add(new ListaDefault(0, "TELEFONÍA", "Titulo"));
					Factura.add(new ListaDefault(1, "Cargo Básico", array.getString("CB")));

					if (array.has("CA")) {
						Factura.add(new ListaDefault(1, "Cargo Adicionales", array.getString("CA")));
					}

					if (array.has("CF")) {
						Factura.add(new ListaDefault(1, "Cobros Financieros", array.getString("CF")));
					}

					Factura.add(new ListaDefault(2, "TOTAL", array.getString("Total")));

				}

				if (jsonObject.has("TV")) {

					JSONObject array = jsonObject.getJSONObject("TV");

					Factura.add(new ListaDefault(0, "TELEVISIÓN", "Titulo"));

					Factura.add(new ListaDefault(1, "Cargo Básico", array.getString("CB")));

					if (array.has("CA")) {
						Factura.add(new ListaDefault(1, "Cargo Adicionales", array.getString("CA")));
					}

					if (array.has("CF")) {
						Factura.add(new ListaDefault(1, "Cobros Financieros", array.getString("CF")));
					}

					Factura.add(new ListaDefault(2, "TOTAL", array.getString("Total")));

				}

				if (jsonObject.has("BA")) {

					JSONObject array = jsonObject.getJSONObject("BA");

					Factura.add(new ListaDefault(0, "INTERNET", "Titulo"));

					Factura.add(new ListaDefault(1, "Cargo Básico", array.getString("CB")));

					Factura.add(new ListaDefault(1, "Cobros Financieros", array.getString("CF")));

					Factura.add(new ListaDefault(2, "TOTAL :", array.getString("Total")));

				}

				if (jsonObject.has("3G")) {

					JSONObject array = jsonObject.getJSONObject("3G");

					Factura.add(new ListaDefault(0, "3G", "Titulo"));

					Factura.add(new ListaDefault(1, "Cargo Básico", array.getString("CB")));

					Factura.add(new ListaDefault(1, "Cobros Financieros", array.getString("CF")));

					Factura.add(new ListaDefault(2, "TOTAL", array.getString("Total")));

				}

				if (jsonObject.has("AD")) {

					JSONObject array = jsonObject.getJSONObject("AD");

					Factura.add(new ListaDefault(0, "Adicionales de Televisión", "Titulo"));
					// System.out.println("array " + array + " " +
					// array.length());

					for (int i = 0; i < array.length(); i++) {

						String Name = array.names().getString(i);
						/*
						 * System.out .println("Dato " +
						 * array.names().getString(i));
						 */

						if (!Name.equalsIgnoreCase("Total")) {
							Factura.add(
									new ListaDefault(1, Utilidades.tocapitalCase(Name) + " :", array.getString(Name)));
						}
					}

					Factura.add(new ListaDefault(2, "TOTAL ADICIONALES", array.getString("Total")));

				}

				if (jsonObject.has("Valor Paquete")) {

					Factura.add(new ListaDefault(3, "Valor Paquete", jsonObject.getString("Valor Paquete")));

				}

				if (jsonObject.has("Valor Total")) {

					Factura.add(new ListaDefault(4, "Valor Total", jsonObject.getString("Valor Total")));

				}

				Factura.add(0, new ListaDefault(1, "SI", "Datos"));

			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				Factura.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
			}
		}

		return Factura;
	}

	public static ArrayList<ItemDirecciones> Direcciones(String Resultado) {
		// System.out.println("LLenar_Direcciones " + Resultado);
		ArrayList<ItemDirecciones> Direcciones = new ArrayList<ItemDirecciones>();

		if (Resultado.equalsIgnoreCase("-1")) {
			Direcciones.add(new ItemDirecciones("NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			Direcciones.add(new ItemDirecciones("NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			Direcciones.add(new ItemDirecciones("NO", "Problemas de Conectividad"));
		} else if (Resultado.equals("Consulta Hecha fuera del horario establecido")
				|| Resultado.equals("Ha superado el limite de consultas por dia")) {
			Direcciones.add(new ItemDirecciones("NO", Resultado));
		} else {
			Resultado = Resultado.replace("\"", "'");

			try {

				JSONObject jsonObject = new JSONObject(Resultado);

				Direcciones.clear();

				if (jsonObject.has("SERVICIOS_CLIENTE")) {

					if (jsonObject.get("SERVICIOS_CLIENTE").getClass().getSimpleName().equals("JSONArray")) {
						JSONArray array = jsonObject.getJSONArray("SERVICIOS_CLIENTE");

						for (int i = 0; i < array.length(); i++) {
							String Direccion = array.getJSONObject(i).getString("DIRECCION");
							String Ciudad = array.getJSONObject(i).getString("MUNICIPIO_ID");
							Direcciones.add(new ItemDirecciones(Direccion, Ciudad));
						}

					} else if (jsonObject.get("SERVICIOS_CLIENTE").getClass().getSimpleName().equals("JSONObject")) {
						JSONObject array = jsonObject.getJSONObject("SERVICIOS_CLIENTE");

						String Direccion = array.getString("DIRECCION");
						String Ciudad = array.getString("MUNICIPIO_ID");
						Direcciones.add(new ItemDirecciones(Direccion, Ciudad));
					}

				}

				Direcciones.add(0, new ItemDirecciones("SI", "Datos"));

			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				Direcciones.add(new ItemDirecciones("NO", "Problemas Procesando el Resultado"));
			}

		}

		return Direcciones;

	}

	public static ArrayList<ListaDefault> CoberturaNuevo(String Resultado) {
		ArrayList<ListaDefault> cobertura = new ArrayList<ListaDefault>();
		if (Resultado.equalsIgnoreCase("-1")) {
			cobertura.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			cobertura.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			cobertura.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else if (Resultado.equals("Consulta Hecha fuera del horario establecido")
				|| Resultado.equals("Ha superado el limite de consultas por dia")) {
			cobertura.add(new ListaDefault(1, "NO", Resultado));
		} else {
			try {
				JSONObject jsonObject = new JSONObject(Resultado);
				System.out.println("jsonObject Cobertura Siebel" + jsonObject);
				System.out.println(
						"ListaDatosInfraestructuraVent " + jsonObject.getString("ListaDatosInfraestructuraVent"));
				if (jsonObject.has("ListaDatosInfraestructuraVent")) {
					JSONArray arrayJson = jsonObject.getJSONArray("ListaDatosInfraestructuraVent");
					System.out.println("arrayJson " + arrayJson);
					if (arrayJson.length() > 0) {
						JSONObject cober = arrayJson.getJSONObject(0);
						cobertura.add(0, new ListaDefault(1, "SI", "Datos"));
						cobertura.add(new ListaDefault(0, "Resultado Cobertura", ""));
						cobertura.add(new ListaDefault(1, "Mensaje", cober.getString("Mensaje")));
					}
				}
			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				cobertura.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
			}
		}
		return cobertura;
	}

	public static ArrayList<ListaDefault> PortafolioNuevo(String Resultado) {
		Portafolio.clear();
		boolean to = false;
		boolean tv = false;
		boolean ba = false;
		try {
			JSONObject consolidado = new JSONObject(Resultado);
			if (consolidado.has("CodigoMensaje")) {
				if (consolidado.getString("CodigoMensaje").equalsIgnoreCase("00")) {
					if (consolidado.has("ListaLocalizacionClienteVent")) {
						portafolioVivienda(consolidado.getString("ListaLocalizacionClienteVent"));
					}
					if (consolidado.has("ListaDatosProductosVent")) {
						JSONArray productos = new JSONArray(consolidado.getString("ListaDatosProductosVent"));
						Portafolio.add(0, new ListaDefault(1, "SI", "Datos"));
						if (productos.length() > 0) {
							for (int i = 0; i < productos.length(); i++) {
								System.out.println("productos " + productos.get(i));
								JSONObject producto = productos.getJSONObject(i);
								if (producto.getString("Producto").equalsIgnoreCase("TO")) {
									to = true;
								} else if (producto.getString("Producto").equalsIgnoreCase("TELEV")) {
									tv = true;
								} else if (producto.getString("Producto").equalsIgnoreCase("INTER")) {
									ba = true;
								}
							}
						}
						if (to) {
							portafolioTO(productos);
						}
						if (tv) {
							portafolioTV(productos);
						}
						if (ba) {
							portafolioBA(productos);
						}
					}
					if (consolidado.has("ListaDatosPaquetesVent")) {
						JSONArray paquetes = new JSONArray(consolidado.getString("ListaDatosPaquetesVent"));
						portafolioPaquetes(paquetes);
					}
				}
			}
		} catch (JSONException e) {
			Portafolio.add(new ListaDefault(1, "NO", e.getMessage()));
		}
		return Portafolio;
	}

	public static void portafolioVivienda(String data) {
		try {
			JSONObject vivienda = new JSONObject(data);
			String Departamento = vivienda.getString("Departamento");
			String Direccion_Portafolio = "";
			String Estrato_Portafolio = "";
			String Paginacion_Portafolio = "";
			Portafolio.add(new ListaDefault(0, "VIVIENDA", "Titulo"));
			Portafolio.add(new ListaDefault(1, "Departamento", Departamento));
			if (vivienda.has("Municipio")) {
				String Ciudad = vivienda.getString("Municipio");
				Portafolio.add(new ListaDefault(2, "Municipio", Ciudad));
			}
			if (vivienda.has("Estrato")) {
				Estrato_Portafolio = vivienda.getString("Estrato");
				Portafolio.add(new ListaDefault(1, "Estrato", Estrato_Portafolio));
			}
			if (vivienda.has("Direccion")) {
				Direccion_Portafolio = vivienda.getString("Direccion");
				Portafolio.add(new ListaDefault(2, "Direccion", Direccion_Portafolio));
			}
			if (vivienda.has("Paginacion")) {
				Paginacion_Portafolio = vivienda.getString("Paginacion");
				Portafolio.add(new ListaDefault(1, "Paginacion", Paginacion_Portafolio));
			}
			Datos_Vivienda(Estrato_Portafolio, Direccion_Portafolio, Paginacion_Portafolio);
		} catch (JSONException e) {
			Portafolio.add(new ListaDefault(1, "NO", e.getMessage()));
		}
	}

	public static void portafolioTO(JSONArray productos) {
		boolean titulo = false;
		try {
			if (productos.length() > 0) {
				for (int i = 0; i < productos.length(); i++) {
					JSONObject producto = productos.getJSONObject(i);
					if (producto.getString("Producto").equalsIgnoreCase("TO")) {
						if (!titulo) {
							Portafolio.add(new ListaDefault(0, "TO", "Titulo"));
							titulo = true;
						}
						Portafolio.add(new ListaDefault(3, "Identificador", producto.getString("Identificador")));
						if (producto.has("Estado_Servicio")) {
							Portafolio.add(new ListaDefault(1, "Estado", producto.getString("Estado_Servicio")));
						}
						Portafolio.add(new ListaDefault(2, "Datos Cliente", producto.getString("cliente")));
						if (producto.has("PlanId")) {
							Portafolio.add(new ListaDefault(2, "Plan Factura",
									producto.getString("PlanId") + " " + producto.getString("NombrePlan")));
						}
						if (producto.has("Adicionales")) {
							JSONArray adicionales = producto.getJSONArray("Adicionales");
							System.out.println("Portafolio adicionales " + adicionales);
							String adicional = "";
							for (int j = 0; j < adicionales.length(); j++) {
								System.out.println("xxxx adicional xxx " + adicionales.getString(j));
								if (j == 0) {
									adicional = adicionales.getString(j);
								} else {
									adicional = adicional + " , " + adicionales.getString(j);
								}
							}
							if (!adicional.equalsIgnoreCase("")) {
								Portafolio.add(new ListaDefault(2, "Adicionales", adicional));
							}
						}
						if (producto.has("cliente")) {
							Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", producto.getString("PaqueteId")));
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void portafolioTV(JSONArray productos) {
		boolean titulo = false;
		try {
			if (productos.length() > 0) {
				for (int i = 0; i < productos.length(); i++) {
					JSONObject producto = productos.getJSONObject(i);
					if (producto.getString("Producto").equalsIgnoreCase("TELEV")) {
						if (!titulo) {
							Portafolio.add(new ListaDefault(0, "TV", "Titulo"));
							titulo = true;
						}
						Portafolio.add(new ListaDefault(3, "Identificador", producto.getString("Identificador")));
						if (producto.has("Estado_Servicio")) {
							Portafolio.add(new ListaDefault(1, "Estado", producto.getString("Estado_Servicio")));
						}
						Portafolio.add(new ListaDefault(2, "Datos Cliente", producto.getString("cliente")));
						if (producto.has("PlanId")) {
							Portafolio.add(new ListaDefault(2, "Plan Factura",
									producto.getString("PlanId") + " " + producto.getString("NombrePlan")));
						}
						if (producto.has("CanalesHD")) {
							JSONArray canalesHD = producto.getJSONArray("CanalesHD");
							String canales = "";
							for (int j = 0; j < canalesHD.length(); j++) {
								if (j == 0) {
									canales = canalesHD.getString(j);
								} else {
									canales = canales + " , " + canalesHD.getString(j);
								}
							}
							if (!canales.equalsIgnoreCase("")) {
								Portafolio.add(new ListaDefault(2, "CanalesHD", canales));
							}
						}
						if (producto.has("CanalesAdicionales")) {
							JSONArray adicionales = producto.getJSONArray("CanalesAdicionales");
							String adicional = "";
							for (int j = 0; j < adicionales.length(); j++) {
								System.out.println("xxxx adicional xxx " + adicionales.getString(j));
								if (j == 0) {
									adicional = adicionales.getString(j);
								} else {
									adicional = adicional + " , " + adicionales.getString(j);
								}
							}
							if (!adicional.equalsIgnoreCase("")) {
								Portafolio.add(new ListaDefault(2, "Adicionales", adicional));
							}
						}
						if (producto.has("CantidadHD")) {
							if (!producto.getString("CantidadHD").equalsIgnoreCase("0")) {
								Portafolio.add(new ListaDefault(2, "Equipos HD", producto.getString("CantidadHD")));
							}
						}
						if (producto.has("CantidadSD")) {
							if (!producto.getString("CantidadSD").equalsIgnoreCase("0")) {
								Portafolio.add(new ListaDefault(2, "Equipos SD", producto.getString("CantidadSD")));
							}
						}
						if (producto.has("cliente")) {
							Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", producto.getString("PaqueteId")));
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void portafolioBA(JSONArray productos) {
		boolean titulo = false;
		try {
			if (productos.length() > 0) {
				for (int i = 0; i < productos.length(); i++) {
					JSONObject producto = productos.getJSONObject(i);
					if (producto.getString("Producto").equalsIgnoreCase("INTER")) {
						if (!titulo) {
							Portafolio.add(new ListaDefault(0, "BA", "Titulo"));
							titulo = true;
						}
						Portafolio.add(new ListaDefault(3, "Identificador", producto.getString("Identificador")));
						if (producto.has("Estado_Servicio")) {
							Portafolio.add(new ListaDefault(1, "Estado", producto.getString("Estado_Servicio")));
						}
						Portafolio.add(new ListaDefault(2, "Datos Cliente", producto.getString("cliente")));
						if (producto.has("PlanId")) {
							Portafolio.add(new ListaDefault(2, "Plan Factura",
									producto.getString("PlanId") + " " + producto.getString("NombrePlan")));
						}
						if (producto.has("cliente")) {
							Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", producto.getString("PaqueteId")));
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void portafolioPaquetes(JSONArray paquete) {
		boolean titulo = false;
		try {
			for (int i = 0; i < paquete.length(); i++) {
				if (!titulo) {
					Portafolio.add(new ListaDefault(0, "PAQUETES", "Titulo"));
					titulo = true;
				}
				Portafolio.add(new ListaDefault(1, "Identificador", paquete.getJSONObject(i).getString("IdPaquete")));
				Portafolio.add(new ListaDefault(2, "Datos Cliente", paquete.getJSONObject(i).getString("cliente")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<ListaDefault> Portafolio(String Resultado) {

		datos_cliente.clear();
		ofertas.clear();

		ArrayList<ListaDefault> Portafolio = new ArrayList<ListaDefault>();

		String Direccion = "", Paginacion = "", Estrato = "", Telefono = "", Cedula = "";

		boolean control_portafolio = false;

		if (Resultado.equalsIgnoreCase("-1")) {
			Portafolio.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
		} else if (Resultado.equalsIgnoreCase("0")) {
			Portafolio.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
		} else if (Resultado.equalsIgnoreCase("2")) {
			Portafolio.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		} else if (Resultado.equals("Consulta Hecha fuera del horario establecido")
				|| Resultado.equals("Ha superado el limite de consultas por dia")) {
			Portafolio.add(new ListaDefault(1, "NO", Resultado));
		} else {
			try {

				Resultado = Resultado.replace("\"", "'");

				JSONObject jsonObject = new JSONObject(Resultado);

				if (jsonObject.has("VIVIENDA")) {
					JSONObject vivienda = jsonObject.getJSONObject("VIVIENDA");

					String Departamento = vivienda.getString("Departamento");
					String Direccion_Portafolio = "";
					String Estrato_Portafolio = "";
					String Paginacion_Portafolio = "";

					Portafolio.add(new ListaDefault(0, "VIVIENDA", "Titulo"));
					Portafolio.add(new ListaDefault(1, "Departamento", Departamento));
					if (vivienda.has("Ciudad")) {
						String Ciudad = vivienda.getString("Ciudad");
						Portafolio.add(new ListaDefault(2, "Ciudad", Ciudad));
					}
					if (vivienda.has("Estrato")) {
						Estrato_Portafolio = vivienda.getString("Estrato");
						Portafolio.add(new ListaDefault(1, "Estrato", Estrato_Portafolio));
					}

					if (vivienda.has("Direccion")) {
						Direccion_Portafolio = vivienda.getString("Direccion");
						Portafolio.add(new ListaDefault(2, "Direccion", Direccion_Portafolio));
					}

					if (vivienda.has("Paginacion")) {
						Paginacion_Portafolio = vivienda.getString("Paginacion");
						Portafolio.add(new ListaDefault(1, "Paginacion", Paginacion_Portafolio));
					}

					Datos_Vivienda(Estrato_Portafolio, Direccion_Portafolio, Paginacion_Portafolio);

				}

				if (jsonObject.has("TO")) {

					if (jsonObject.get("TO").getClass().getSimpleName().equals("JSONArray")) {
						JSONArray array = jsonObject.getJSONArray("TO");
						Portafolio.add(new ListaDefault(0, "TO", "Titulo"));
						for (int i = 0; i < array.length(); i++) {
							String Identificador = array.getJSONObject(i).getString("Identificador");

							String Datos_cliente = array.getJSONObject(i).getString("Datos_cliente");

							String Tipo_Tecnologia = array.getJSONObject(i).getString("Tipo_Tecnologia");

							String Estado_Servicio = array.getJSONObject(i).getString("Estado_Servicio");

							Portafolio.add(new ListaDefault(3, "Identificador", Identificador));
							Portafolio.add(new ListaDefault(2, "Datos_cliente", Datos_cliente));

							if (array.getJSONObject(i).has("Plan_Factura")) {
								String planFactura = array.getJSONObject(i).getString("Plan_Factura");
								Portafolio.add(new ListaDefault(2, "Plan Factura", planFactura));
							}

							if (array.getJSONObject(i).has("IdEmpaquetamiento")) {
								String idEmpaquetamiento = array.getJSONObject(i).getString("IdEmpaquetamiento");
								Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", idEmpaquetamiento));
							}

							if (!control_portafolio) {
								Datos_Usuario(Identificador, Datos_cliente, "TO");
								control_portafolio = true;
							}

						}

					} else if (jsonObject.get("TO").getClass().getSimpleName().equals("JSONObject")) {

						JSONObject array = jsonObject.getJSONObject("TO");

						String Identificador = array.getString("Identificador");

						String Datos_cliente = array.getString("Datos_cliente");

						String Tipo_Tecnologia = array.getString("Tipo_Tecnologia");

						String Estado_Servicio = array.getString("Estado_Servicio");

						Portafolio.add(new ListaDefault(0, "TO", "Titulo"));

						Portafolio.add(new ListaDefault(3, "Identificador", Identificador));

						Portafolio.add(new ListaDefault(2, "Datos_cliente", Datos_cliente));

						if (array.has("Plan_Factura")) {
							String planFactura = array.getString("Plan_Factura");
							Portafolio.add(new ListaDefault(2, "Plan Factura", planFactura));
						}

						if (array.has("IdEmpaquetamiento")) {
							String idEmpaquetamiento = array.getString("IdEmpaquetamiento");
							Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", idEmpaquetamiento));
						}

						if (!control_portafolio) {

							Datos_Usuario(Identificador, Datos_cliente, "TO");

							control_portafolio = true;
						}

					}

				}

				if (jsonObject.has("INTER")) {
					if (jsonObject.get("INTER").getClass().getSimpleName().equals("JSONArray")) {
						JSONArray array = jsonObject.getJSONArray("INTER");
						Portafolio.add(new ListaDefault(0, "INTER", "Titulo"));

						for (int i = 0; i < array.length(); i++) {

							if (array.getJSONObject(i).has("Identificador")) {
								String Identificador = array.getJSONObject(i).getString("Identificador");
								Portafolio.add(new ListaDefault(3, "Identificador", Identificador));
							}

							if (array.getJSONObject(i).has("Identificador")) {
								String Datos_cliente = array.getJSONObject(i).getString("Datos_cliente");
								Portafolio.add(new ListaDefault(2, "Datos_cliente", Datos_cliente));
							}

							if (array.getJSONObject(i).has("Velocidad")) {
								String Velocidad = array.getJSONObject(i).getString("Velocidad");
								Portafolio.add(new ListaDefault(2, "Velocidad", Velocidad));
							}

							if (array.getJSONObject(i).has("Plan_Factura")) {
								String planFactura = array.getJSONObject(i).getString("Plan_Factura");
								Portafolio.add(new ListaDefault(2, "Plan Factura", planFactura));
							}

							if (array.getJSONObject(i).has("IdEmpaquetamiento")) {
								String idEmpaquetamiento = array.getJSONObject(i).getString("IdEmpaquetamiento");
								Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", idEmpaquetamiento));
							}

							if (!control_portafolio) {
								/*
								 * Datos_Usuario(Identificador, Datos_cliente,
								 * "INTER");
								 */
								control_portafolio = true;
							}
						}
					} else if (jsonObject.get("INTER").getClass().getSimpleName().equals("JSONObject")) {

						Portafolio.add(new ListaDefault(0, "INTER", "Titulo"));

						JSONObject array = jsonObject.getJSONObject("INTER");

						if (array.has("Identificador")) {
							String Identificador = array.getString("Identificador");
							Portafolio.add(new ListaDefault(3, "Identificador", Identificador));
						}

						if (array.has("Datos_cliente")) {
							String Datos_cliente = array.getString("Datos_cliente");

							Portafolio.add(new ListaDefault(2, "Datos_cliente", Datos_cliente));
						}
						if (array.has("Velocidad")) {
							String Velocidad = array.getString("Velocidad");
							Portafolio.add(new ListaDefault(2, "Velocidad", Velocidad));
						}

						if (array.has("Plan_Factura")) {
							String planFactura = array.getString("Plan_Factura");
							Portafolio.add(new ListaDefault(2, "Plan Factura", planFactura));
						}

						if (array.has("IdEmpaquetamiento")) {
							String idEmpaquetamiento = array.getString("IdEmpaquetamiento");
							Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", idEmpaquetamiento));
						}

						if (!control_portafolio) {
							// Datos_Usuario(Identificador, Datos_cliente,
							// "INTER");
							control_portafolio = true;
						}

					}
				}

				if (jsonObject.has("TELEV")) {
					if (jsonObject.get("TELEV").getClass().getSimpleName().equals("JSONArray")) {
						JSONArray array = jsonObject.getJSONArray("TELEV");
						Portafolio.add(new ListaDefault(0, "TELEV", "Titulo"));

						for (int i = 0; i < array.length(); i++) {
							// System.out.println(array.getJSONObject(i));
							String Identificador = array.getJSONObject(i).getString("Identificador");

							String Uso = array.getJSONObject(i).getString("Uso");

							String Datos_cliente = array.getJSONObject(i).getString("Datos_cliente");

							String Estado_Servicio = array.getJSONObject(i).getString("Estado_Servicio");

							String Plan_Television = array.getJSONObject(i).getString("Plan_Television");

							Portafolio.add(new ListaDefault(3, "Identificador", Identificador));
							Portafolio.add(new ListaDefault(2, "Datos_cliente", Datos_cliente));

							Portafolio.add(new ListaDefault(2, "Plan_Television", Plan_Television));

							if (array.getJSONObject(i).has("Plan_Factura")) {
								String planFactura = array.getJSONObject(i).getString("Plan_Factura");
								Portafolio.add(new ListaDefault(2, "Plan Factura", planFactura));
							}

							if (array.getJSONObject(i).has("IdEmpaquetamiento")) {
								String idEmpaquetamiento = array.getJSONObject(i).getString("IdEmpaquetamiento");
								Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", idEmpaquetamiento));
							}

							if (!control_portafolio) {
								Datos_Usuario(Identificador, Datos_cliente, "TELEV");
								control_portafolio = true;
							}
						}
					} else if (jsonObject.get("TELEV").getClass().getSimpleName().equals("JSONObject")) {

						Portafolio.add(new ListaDefault(0, "TELEV", "Titulo"));

						JSONObject array = jsonObject.getJSONObject("TELEV");

						String Identificador = array.getString("Identificador");

						String Uso = array.getString("Uso");

						String Datos_cliente = array.getString("Datos_cliente");

						String Estado_Servicio = array.getString("Estado_Servicio");

						String Plan_Television = array.getString("Plan_Television");

						Portafolio.add(new ListaDefault(3, "Identificador", Identificador));
						Portafolio.add(new ListaDefault(2, "Datos_cliente", Datos_cliente));

						Portafolio.add(new ListaDefault(2, "Plan_Television", Plan_Television));

						if (array.has("Plan_Factura")) {
							String planFactura = array.getString("Plan_Factura");
							Portafolio.add(new ListaDefault(2, "Plan Factura", planFactura));
						}

						if (array.has("IdEmpaquetamiento")) {
							String idEmpaquetamiento = array.getString("IdEmpaquetamiento");
							Portafolio.add(new ListaDefault(2, "Id Empaquetamiento", idEmpaquetamiento));
						}

						if (!control_portafolio) {
							// Datos_Usuario(Identificador, Datos_cliente,
							// "TELEV");
							control_portafolio = true;
						}

					}
				}

				if (jsonObject.has("PAQUETE")) {
					if (jsonObject.get("PAQUETE").getClass().getSimpleName().equals("JSONArray")) {

						JSONArray array = jsonObject.getJSONArray("PAQUETE");

						Portafolio.add(new ListaDefault(0, "PAQUETES", "Titulo"));

						for (int i = 0; i < array.length(); i++) {
							// System.out.println(array.getJSONObject(i));

							String Identificador = array.getJSONObject(i).getString("Identificador");

							String Datos_cliente = array.getJSONObject(i).getString("Datos_cliente");

							String Tipo_Paquete = array.getJSONObject(i).getString("Tipo_Paquete");

							Portafolio.add(new ListaDefault(1, "Identificador", Identificador));
							Portafolio.add(new ListaDefault(2, "Datos cliente", Datos_cliente));
							Portafolio.add(new ListaDefault(3, "Tipo Paquete", Tipo_Paquete));

							if (!control_portafolio) {
								// Datos_Usuario(Identificador, Datos_cliente,
								// "TELEV");
								control_portafolio = true;
							}
						}
					} else {
						JSONObject vivienda = jsonObject.getJSONObject("PAQUETE");

						String Identificador = vivienda.getString("Identificador");

						String Datos_cliente = vivienda.getString("Datos_cliente");

						String Tipo_Paquete = vivienda.getString("Tipo_Paquete");

						Portafolio.add(new ListaDefault(0, "PAQUETE", "Titulo"));
						Portafolio.add(new ListaDefault(3, "Identificador", Identificador));
						Portafolio.add(new ListaDefault(2, "Datos cliente", Datos_cliente));
						Portafolio.add(new ListaDefault(2, "Tipo Paquete", Tipo_Paquete));

						if (!control_portafolio) {
							// Datos_Usuario(Identificador, Datos_cliente,
							// "TELEV");
							control_portafolio = true;
						}
					}

				}

				/*
				 * if (jsonObject.has("Ofertas")) { JSONObject Ofertas =
				 * jsonObject.getJSONObject("Ofertas"); String tarifaTotal = "";
				 * boolean control = false; if (Ofertas.has("DUO")) { JSONObject
				 * duo = Ofertas.getJSONObject("DUO");
				 * 
				 * Portafolio .add(new ListaDefault(0, "OFERTAS", "Titulo"));
				 * 
				 * for (int i = 0; i < duo.length(); i++) {
				 * 
				 * String nameOferta = duo.names().getString(i); JSONObject
				 * oferta = duo.getJSONObject(nameOferta); Portafolio.add(new
				 * ListaDefault(7, nameOferta, "Oferta"));
				 * 
				 * for (int j = 0; j < oferta.length(); j++) { String
				 * nameProducto = oferta.names().getString( j); JSONObject
				 * producto = oferta .getJSONObject(nameProducto); for (int h =
				 * 0; h < producto.length(); h++) { String nameDato =
				 * producto.names() .getString(h); String tarifa = producto
				 * .getString(nameDato); if
				 * (!nameDato.equalsIgnoreCase("Total")) { Portafolio.add(new
				 * ListaDefault(1, nameProducto + " " + nameDato, tarifa));
				 * ofertas.add(new Ofertas(nameProducto, nameDato, tarifa,
				 * nameOferta)); } else { tarifaTotal = tarifa; } }
				 * 
				 * } if (oferta.length() > 0) { Portafolio.add(new
				 * ListaDefault(2, "Total", tarifaTotal)); } }
				 * 
				 * } else if (Ofertas.has("SUPERPLAY")) { JSONObject duo =
				 * Ofertas.getJSONObject("SUPERPLAY");
				 * 
				 * Portafolio .add(new ListaDefault(0, "OFERTAS", "Titulo"));
				 * 
				 * for (int i = 0; i < duo.length(); i++) {
				 * 
				 * String nameOferta = duo.names().getString(i); JSONObject
				 * oferta = duo.getJSONObject(nameOferta); Portafolio.add(new
				 * ListaDefault(7, nameOferta, "Oferta"));
				 * 
				 * for (int j = 0; j < oferta.length(); j++) { String
				 * nameProducto = oferta.names().getString( j); JSONObject
				 * producto = oferta .getJSONObject(nameProducto); for (int h =
				 * 0; h < producto.length(); h++) { String nameDato =
				 * producto.names() .getString(h); String tarifa = producto
				 * .getString(nameDato); if
				 * (!nameDato.equalsIgnoreCase("Total")) { Portafolio.add(new
				 * ListaDefault(1, nameProducto + " " + nameDato, tarifa));
				 * ofertas.add(new Ofertas(nameProducto, nameDato, tarifa,
				 * nameOferta)); } else { tarifaTotal = tarifa; } }
				 * 
				 * } if (oferta.length() > 0) { Portafolio.add(new
				 * ListaDefault(2, "Total", tarifaTotal)); } } }
				 * 
				 * }
				 */

				Portafolio.add(0, new ListaDefault(1, "SI", "Datos"));
			} catch (JSONException e) {
				Log.w("Error", "Mensaje " + e.getMessage());
				Portafolio.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
			}
		}

		return Portafolio;
	}

	public static ArrayList<ListaDefault> defaulScooring(String Resultado, int tipo) {

		ArrayList<ListaDefault> scooring = new ArrayList<ListaDefault>();

		try {

			JSONObject json = new JSONObject(Resultado);

			System.out.println("json " + json);

			if (tipo == 0) {

				scooring.add(0, new ListaDefault(1, "SI", "Datos"));
				scooring.add(new ListaDefault(0, "Scoring", ""));

				if (json.has("codigoMensaje")) {

					System.out.println("json.has(codigoMensaje) " + json.has("codigoMensaje"));

					if (json.getString("codigoMensaje").equalsIgnoreCase("00")) {
						scooring.add(new ListaDefault(1, "Insercion ", "EXITOSA"));

						scooring.add(new ListaDefault(1, "Mensaje ",
								"Espere " + Utilidades.tiempoScooring() + " segundos y consulte nuevamente"));
					} else if (json.getString("codigoMensaje").equalsIgnoreCase("01")
							|| (json.getString("codigoMensaje").equalsIgnoreCase("-1"))) {
						scooring.add(new ListaDefault(1, "Insercion ", "ERRONEA"));

						scooring.add(new ListaDefault(1, "Mensaje ",
								"Por favor intente de nuevo, si el error persiste continúe con su venta"));

					}

				}
			} else if (tipo == 1) {
				scooring.add(0, new ListaDefault(1, "SI", "Datos"));
				scooring.add(new ListaDefault(0, "Scoring", ""));

				if (json.has("codigoMensaje")) {
					if (json.getString("codigoMensaje").equalsIgnoreCase("00")) {

						scooring.add(new ListaDefault(1, "Consulta", "EXITOSA"));

						if (json.getString("estadoFinal").equalsIgnoreCase("")
								|| json.getString("estadoFinal").equalsIgnoreCase("NULL")
								|| json.getString("estadoFinal").equalsIgnoreCase("(NULL)")) {

							scooring.add(new ListaDefault(1, "Resultado", "Scoring Pendiente"));

							scooring.add(new ListaDefault(2, "Mensaje",
									"Por favor intente de nuevo, si el error persiste continúe con su venta"));

						} else if (json.getString("estadoFinal").equalsIgnoreCase("APROBADO")) {
							/*
							 * apruebaScooring = true;
							 * cliente.getScooringune().setResultado(true, true,
							 * true, buscar.getBusqueda(), "270000",
							 * jop.getString("estadoFinal"), idScooring);
							 */
							scooring.add(new ListaDefault(1, "Resultado", "Scoring Aprobado"));

							scooring.add(new ListaDefault(2, "Estado", json.getString("estadoFinal")));

							scooring.add(new ListaDefault(1, "Razon", json.getString("razonEstadoFinal")));

							scooring.add(new ListaDefault(2, "Maxima Cotización", json.getString("maximaCotizacion")));

						} else {

							scooring.add(new ListaDefault(1, "Resultado", "Scoring Rechazado"));

							scooring.add(new ListaDefault(2, "Motivo", json.getString("estadoFinal")));

							scooring.add(new ListaDefault(1, "Razon", json.getString("razonEstadoFinal")));
							/*
							 * apruebaScooring = false;
							 * cliente.getScooringune().setResultado(true,
							 * false, true, buscar.getBusqueda(), "0",
							 * jop.getString("estadoFinal"), idScooring);
							 */
						}

						// scooring.add(new ListaDefault(1, "Mensaje ",
						// "Espere 2 minutos y conulte nuevamente"));
					}

				}
			}

		} catch (JSONException e) {
			Log.w("Error", "Mensaje " + e.getMessage());
			scooring.clear();
			scooring.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
		}

		// // System.out.println("Resultado " + Resultado);
		// try {
		// JSONArray ja = new JSONArray(Resultado);
		// JSONObject jsonObject = ja.getJSONObject(0);
		//
		// scooring.add(0, new ListaDefault(1, "SI", "Datos"));
		// scooring.add(new ListaDefault(0, "Scooring", ""));
		//

		// .getString("blnn_direccionservicio")));
		//
		// blindaje.add(new ListaDefault(1, "Oferta ", jsonObject
		// .getString("blnn_oferta")));
		//
		// blindaje.add(new ListaDefault(1, "Total Factura Con Oferta",
		// jsonObject.getString("blnn_totalnuevoscompconiva")));
		//
		// } catch (JSONException e) {
		//
		// Log.w("Error", "Mensaje " + e.getMessage());
		// blindaje.clear();
		// blindaje.add(new ListaDefault(1, "NO",
		// "Problemas Procesando el Resultado"));
		// }
		//
		// return blindaje;

		return scooring;
	}

	public static ArrayList<ListaDefault> BlindajeNacional(String Resultado) {

		ArrayList<ListaDefault> blindaje = new ArrayList<ListaDefault>();

		// System.out.println("Resultado " + Resultado);
		try {
			JSONArray ja = new JSONArray(Resultado);
			JSONObject jsonObject = ja.getJSONObject(0);

			blindaje.add(0, new ListaDefault(1, "SI", "Datos"));
			blindaje.add(new ListaDefault(0, "Blindaje Nacional", ""));

			blindaje.add(new ListaDefault(1, "Dirección ", jsonObject.getString("blnn_direccionservicio")));

			blindaje.add(new ListaDefault(1, "Oferta ", jsonObject.getString("blnn_oferta")));

			blindaje.add(new ListaDefault(1, "Total Factura Con Oferta",
					jsonObject.getString("blnn_totalnuevoscompconiva")));

		} catch (JSONException e) {

			Log.w("Error", "Mensaje " + e.getMessage());
			blindaje.clear();
			blindaje.add(new ListaDefault(1, "NO", "Problemas Procesando el Resultado"));
		}

		return blindaje;
	}

	public static ArrayList<ListaDefault> IVR(String Resultado) {

		ArrayList<ListaDefault> IVR = new ArrayList<ListaDefault>();

		try {
			JSONArray ja = new JSONArray(Resultado);
			JSONObject jsonObject = ja.getJSONObject(0);

			System.out.println("jsonObject " + jsonObject);

			IVR.add(new ListaDefault(0, "Documentación", "Datos"));

			if(jsonObject.getString("cnf_permanencia").equals("0")
					|| jsonObject.getString("cnf_permanencia").equals("null")){
				IVR.add(new ListaDefault(1, "Acepta Permanencia", "NO"));
			} else {
				IVR.add(new ListaDefault(1, "Acepta Permanencia", "SI"));
			}

			if (jsonObject.getString("cnf_aceptacontrato").equals("0")
					|| jsonObject.getString("cnf_aceptacontrato").equals("null")) {
				IVR.add(new ListaDefault(1, "Acepta Contrato", "NO"));
			} else {
				IVR.add(new ListaDefault(1, "Acepta Contrato", "SI"));
			}

			if (jsonObject.getString("lgl_cifin").equals("0") || jsonObject.getString("lgl_cifin").equals("null")) {
				IVR.add(new ListaDefault(2, "Autoriza Cifin", "NO"));
			} else {
				IVR.add(new ListaDefault(2, "Autoriza Cifin", "SI"));
			}

			if (jsonObject.getString("lgl_habeasune").equals("0")
					|| jsonObject.getString("lgl_habeasune").equals("null")) {
				IVR.add(new ListaDefault(1, "Autoriza Habeas Une y Aliados", "NO"));
			} else {
				IVR.add(new ListaDefault(1, "Autoriza Habeas Une y Aliados", "SI"));
			}

			if (jsonObject.getString("lgl_habeasunealiados").equals("0")
					|| jsonObject.getString("lgl_habeasunealiados").equals("null")) {
				IVR.add(new ListaDefault(2, "Autoriza Habeas Grupo EPM", "NO"));
			} else {
				IVR.add(new ListaDefault(2, "Autoriza Habeas Grupo EPM", "SI"));
			}

			if (jsonObject.getString("lgl_habeasemail").equals("0")
					|| jsonObject.getString("lgl_habeasemail").equals("null")) {
				IVR.add(new ListaDefault(1, "Autoriza Habeas Email", "NO"));
			} else {
				IVR.add(new ListaDefault(1, "Autoriza Habeas Email", "SI"));
			}

			if (jsonObject.getString("lgl_habeassms").equals("0")
					|| jsonObject.getString("lgl_habeassms").equals("null")) {
				IVR.add(new ListaDefault(2, "Autoriza Habeas SMS", "NO"));
			} else {
				IVR.add(new ListaDefault(2, "Autoriza Habeas SMS", "SI"));
			}

			if (jsonObject.getString("lgl_habeasmail").equals("0")
					|| jsonObject.getString("lgl_habeasmail").equals("null")) {
				IVR.add(new ListaDefault(1, "Autoriza Habeas Correo Fisico", "NO"));
			} else {
				IVR.add(new ListaDefault(1, "Autoriza Habeas Correo Fisico", "SI"));
			}

			if (jsonObject.getString("lgl_habeascalls").equals("0")
					|| jsonObject.getString("lgl_habeascalls").equals("null")) {
				IVR.add(new ListaDefault(2, "Autoriza Habeas Llamadas", "NO"));
			} else {
				IVR.add(new ListaDefault(2, "Autoriza Habeas Llamadas", "SI"));
			}

			if (jsonObject.getString("lgl_3066email").equals("0")
					|| jsonObject.getString("lgl_3066email").equals("null")) {
				IVR.add(new ListaDefault(1, "Autoriza Res. 3066 Email", "NO"));
			} else {
				IVR.add(new ListaDefault(1, "Autoriza Res. 3066 Email", "SI"));
			}

			if (jsonObject.getString("lgl_3066sms").equals("0") || jsonObject.getString("lgl_3066sms").equals("null")) {
				IVR.add(new ListaDefault(2, "Autoriza Res. 3066 SMS", "NO"));
			} else {
				IVR.add(new ListaDefault(2, "Autoriza Res. 3066 SMS", "SI"));
			}

			if (jsonObject.getString("lgl_habeasmail").equals("0")
					|| jsonObject.getString("lgl_habeasmail").equals("null")) {
				IVR.add(new ListaDefault(1, "Autoriza Res. 3066 Correo Fisico", "NO"));
			} else {
				IVR.add(new ListaDefault(1, "Autoriza Res. 3066 Correo Fisico", "SI"));
			}

			if (jsonObject.getString("lgl_3066calls").equals("0")
					|| jsonObject.getString("lgl_3066calls").equals("null")) {
				IVR.add(new ListaDefault(2, "Autoriza Res. 3066 LLamadas", "NO"));
			} else {
				IVR.add(new ListaDefault(2, "Autoriza Res. 3066 LLamadas", "SI"));
			}

			if (jsonObject.getString("lgl_3066contratoresumido").equals("0")
					|| jsonObject.getString("lgl_3066contratoresumido").equals("null")) {
				IVR.add(new ListaDefault(1, "Lectura Contrato", "COMPLETO"));
			} else {
				IVR.add(new ListaDefault(1, "Lectura Contrato", "RESUMIDO"));
			}

			if (jsonObject.getString("lgl_3066contratomailfisico").equals("0")
					|| jsonObject.getString("lgl_3066contratomailfisico").equals("null")) {
				IVR.add(new ListaDefault(2, "Envio Contrato", "FISICO"));
			} else {
				IVR.add(new ListaDefault(2, "Envio Contrato", "EMAIL"));
			}

			if (jsonObject.getString("lgl_3066facturamailfisico").equals("0")
					|| jsonObject.getString("lgl_3066facturamailfisico").equals("null")) {
				IVR.add(new ListaDefault(2, "Envio Factura", "FISICO"));
			} else {
				IVR.add(new ListaDefault(2, "Envio Factura", "EMAIL"));
			}

			IVR.add(0, new ListaDefault(1, "SI", "Datos"));

		} catch (JSONException e) {
			Log.w("Error", "Mensaje " + e.getMessage());

			IVR.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
		}

		return IVR;
	}

	public static ArrayList<Ofertas> getOfertas() {
		return ofertas;
	}

	public static void Datos_Vivienda(String Estrato_Portafolio, String Direccion_Portafolio,
			String Paginacion_Portafolio) {

		/*
		 * System.out.println("Estrato_Portafolio " + Estrato_Portafolio +
		 * " Direccion_Portafolio " + Direccion_Portafolio +
		 * " Paginacion_Portafolio " + Paginacion_Portafolio);
		 */

		datos_cliente.add(new String[] { "Direccion", Direccion_Portafolio });
		datos_cliente.add(new String[] { "Paginacion", Paginacion_Portafolio });
		datos_cliente.add(new String[] { "Estrato", Estrato_Portafolio });

	}

	public static void Datos_Usuario(String Identificador, String Datos_Cliente, String Tipo) {

		if (Tipo.equalsIgnoreCase("TO")) {
			datos_cliente.add(new String[] { "Telefono", Identificador });
			Datos_Cliente = Datos_Cliente.replaceFirst(" ", "-");
			String[] Cliente = Datos_Cliente.split("-");
			if (Cliente.length > 1) {
				datos_cliente.add(new String[] { "Cedula", Cliente[0] });
				datos_cliente.add(new String[] { "Nombre", Cliente[1] });
			}
		}

	}

	public static ArrayList<String[]> getDatos_Cliente() {
		return datos_cliente;
	}

}
