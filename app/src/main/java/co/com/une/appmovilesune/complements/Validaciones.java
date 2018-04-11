package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.util.Util;

import com.google.gson.JsonArray;

import android.content.Context;
import android.media.MediaRecorder.VideoSource;
import android.util.Log;
import android.widget.Toast;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesDecos;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.model.Asesoria;
import co.com.une.appmovilesune.model.Blindaje;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Competencia;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Prospecto;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.UneMas;
import co.com.une.appmovilesune.model.Venta;
import co.com.une.appmovilesune.model.CotizacionCliente;

public class Validaciones {

    public static ArrayList<ListaDefault> mensaje = new ArrayList<ListaDefault>();

    public static JSONObject mensajes;

    public static ArrayList<String> tipoControlador = new ArrayList<String>();


    public static boolean validarClienteVenta(Cliente cliente, Venta venta, Context context) {

        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        int tamanoDocumento = 0;
        String telefonoServicio = "";

        try {
            mensajes = new JSONObject();

            JSONArray ja = new JSONArray();

            if (!cliente.getNombre().equals("")) {
                reg.add(true);
            } else {

                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Nombres", "Sin Diligenciar"));
            }

            if (!cliente.getApellido().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Apellidos", "Sin Diligenciar"));
            }

            if (!cliente.getTipoDocumento().equals(Utilidades.inicial_opcion)) {
                reg.add(true);

                try {
                    tamanoDocumento = Integer
                            .parseInt(Utilidades.claveValor("tamanoDocumento", cliente.getTipoDocumento()));
                } catch (NumberFormatException e) {
                    Log.w("Error ", e.getMessage());
                }

            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Tipo Documento", "Sin Seleccionar"));
            }

            if (!cliente.getCedula().equals("")) {
                if (cliente.getCedula().length() <= tamanoDocumento) {

                    if (Utilidades.tamanoCedula(cliente.getCedula(), cliente.getTipoDocumento())) {
                        if (!Utilidades.ListaNegraCedula(cliente.getCedula())) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Documento", context.getResources()
                                    .getString(co.com.une.appmovilesune.R.string.listaNegraDocumento)));
                        }
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Documento",
                                context.getResources().getString(co.com.une.appmovilesune.R.string.tamanoDocumento)));
                    }

                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Documento",
                            context.getResources().getString(R.string.documentosuperamaximocaracteres)));
                }

            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Documento", "Sin Diligenciar"));
            }

            if (!cliente.getExpedicion().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.fecha_exped),
                        "Sin Diligenciar"));
            }

            System.out.println("Fecha Nacimiento " + cliente.getFechaNacimiento());
            if (!cliente.getFechaNacimiento().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Fecha De Nacimiento", "Sin Diligenciar"));
            }

            System.out.println("cliente.getTipoDocumento() " + cliente.getTipoDocumento());
            System.out.println("cliente.getLugarExpedicion() " + cliente.getLugarExpedicion());

            if (cliente.getTipoDocumento().equals("CC")) {
                if (!cliente.getLugarExpedicion().equals("")) {
                    // reg.add(true);
                    System.out.println(" getDepartamentoExpedicion() " + cliente.getDepartamentoExpedicion());
                    if (!cliente.getDepartamentoExpedicion().equalsIgnoreCase("")) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.lugar_exped),
                                context.getResources().getString(R.string.lugarexpediciondebeseleccionarselista)));
                    }
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.lugar_exped),
                            "Sin Diligenciar"));
                }
            }

            if (!cliente.getDireccion().equals("") || !cliente.getPaginacion().equals("")
                    || !cliente.getContrato().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.direccion),
                        "Sin Diligenciar"));
            }

            if (!cliente.getBarrio().equals("")) {
                // reg.add(true);
                System.out.println("getCodigoBarrio() " + cliente.getCodigoBarrio());
                if (!cliente.getCodigoBarrio().equalsIgnoreCase("")
                        && !cliente.getCodigoBarrio().equalsIgnoreCase("Ninguna")) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Barrio", "El Barrio Debe Ser Seleccionado De La Lista"));
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Barrio", "Sin Diligenciar"));
            }

            if (!cliente.getEstrato().equals("--Seleccione Estrato--") || !cliente.getEstrato().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Estrato", "Sin Seleccionar"));
            }

            if (!cliente.getTipoServicio().equals(Utilidades.inicial_opcion)) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Tipo Servicio", "Sin Seleccionar"));
            }

            if (!cliente.getGenero().equals(Utilidades.inicial_opcion)) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Genero", "Sin Seleccionar"));
            }

            telefonoServicio = Utilidades.limpiarTelefono(cliente.getTelefono());

            System.out.println("telefonoServicio " + telefonoServicio);

            if (!telefonoServicio.equals("") || !cliente.getTelefono2().equals("")) {

                if (!telefonoServicio.equals("")) {
                    if (telefonoServicio.length() == 7) {
                        if (!Utilidades.ListaNegraTelefonos(telefonoServicio)) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Telefono De Servicio",
                                    context.getResources().getString(R.string.telefono)));
                        }
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Telefono De Servicio", "Debe Estar Compuesto Por 7 Digitos"));
                    }
                }
                // reg.add(true);
                if (!cliente.getTelefono2().equals("")) {
                    if (cliente.getTelefono2().length() == 7 || cliente.getTelefono2().length() == 10) {
                        if (!Utilidades.ListaNegraTelefonos(cliente.getTelefono2())) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Telefono Secundarios",
                                    context.getResources().getString(R.string.telefonoingresadoinvalido)));
                        }
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Telefono Secundarios",
                                "Debe Estar Compuesto Por 7 o 10 Digitos"));
                    }
                } else {
                    reg.add(true);
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Telefonos", "Sin Diligenciar"));
            }

            if (!cliente.getCelular().equals("") && cliente.getCelular().length() == 10) {
                if (!Utilidades.ListaNegraTelefonos(cliente.getCelular())) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Celular", "El Celular Ingresado No Es Valido"));
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Celular", "Sin Diligenciar, Debe Estar Compuesto Por 10 Digitos"));
            }

            System.out.println("Estado Civil " + cliente.getEstadoCivil());
            if (!Utilidades.excluir("excluirScooring", cliente.getCiudad())) {

                if (cliente.getEstadoCivil() != null && !cliente.getEstadoCivil().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Estado Civil", "Sin Seleccionar"));
                }

                System.out.println("Personas a Cargo " + cliente.getPersonasCargo());
                if (cliente.getPersonasCargo() != null
                        && !cliente.getPersonasCargo().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Personas a Cargo", "Sin Seleccionar"));
                }

                System.out.println("Ingresos " + cliente.getNivelIngresos());
                if (cliente.getNivelIngresos() != null
                        && !cliente.getNivelIngresos().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Nivel De Ingresos", "Sin Seleccionar"));
                }

                System.out.println("Estudios " + cliente.getNivelEstudio());
                if (cliente.getNivelEstudio() != null && !cliente.getNivelEstudio().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Nivel De Estudios", "Sin Seleccionar"));
                }

                System.out.println("Profesion " + cliente.getProfesion());
                if (cliente.getProfesion() != null && !cliente.getProfesion().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.sec_profesion_string),
                            "Sin Seleccionar"));

                }

                System.out.println("Ocupacion " + cliente.getOcupacion());
                if (cliente.getOcupacion() != null && !cliente.getOcupacion().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.sec_ocupacion_string),
                            "Sin Seleccionar"));
                }

                System.out.println("Tipo Inmueble " + cliente.getTipoVivienda());
                if (cliente.getTipoVivienda() != null && !cliente.getTipoVivienda().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Tipo Vivienda", "Sin Seleccionar"));
                }

                System.out.println("Tipo Predio " + cliente.getTipoPredio());
                if (cliente.getTipoPredio() != null && !cliente.getTipoPredio().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Tipo Predio", "Sin Seleccionar"));
                }

                System.out.println("Tipo getTipoPropiedad " + cliente.getTipoPropiedad());
                if (cliente.getTipoPropiedad() != null
                        && !cliente.getTipoPropiedad().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Tipo Propiedad", "Sin Seleccionar"));
                }

                System.out.println("Cargo " + cliente.getCargo());
                if (cliente.getCargo() != null && !cliente.getCargo().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Cargo", "Sin Seleccionar"));
                }

            }

            if (cliente.getTipoConstruccion() != null
                    && !cliente.getTipoConstruccion().equals(Utilidades.inicial_opcion)
                    && !cliente.getTipoConstruccion().equals("N/A")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.tipoconstruccion),
                        "Sin Seleccionar"));
            }

            System.out.println("migracion " + venta.getInternet()[3]);
            System.out.println("login  " + cliente.getLogin());
            System.out.println("validarLogin  " + cliente.isValidarLogin());

            /*if (!venta.getInternet()[0].equals("-") && venta.getInternet()[3].equals("Nueva")) {
                if (!Utilidades.excluirNacional("excluir", "login")
                        && Utilidades.excluirMunicipal("validarCampos", "login", cliente.getCiudad())) {
                    if (!cliente.getLogin().equalsIgnoreCase("") && cliente.isValidarLogin()) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Login", "Sin Seleccionar, Debe Ser Un Login Valido"));
                    }
                }
            } else {
                cliente.setValidarLogin(false);
            }

            if (!venta.getTelevision()[0].equals("-") && !venta.getTelevision()[6].equals("")) {
                String[] adicionales = venta.getTelevision()[6].split(",");
                for (int i = 0; i < adicionales.length; i++) {
                    if (adicionales[i].equalsIgnoreCase("Paquete Adultos HotPack")
                            && Utilidades.excluirMunicipal("validarCampos", "pinHotPack", cliente.getCiudad())) {
                        if (!cliente.getPinhp().equals("")) {
                            if (cliente.getPinhp().length() == 10) {
                                reg.add(true);
                            } else {
                                reg.add(false);
                                ja.put(Utilidades.jsonMensajes("Pin Hot Pack",
                                        "Pin incorrecto, Debe ingresar el numero de celular para el pin Hot Pack"));
                            }
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Pin Hot Pack",
                                    "Sin Seleccionar, Debe ingresar el pin Hot Pack"));
                        }
                    }
                }
            }*/

            System.out.println("validar correo " + cliente.isValidarCorreo());
            if (cliente.isValidarCorreo()) {
                System.out.println("validacion correo " + Utilidades.validateEmail(cliente.getCorreo()));
                if (Utilidades.validateEmail(cliente.getCorreo())) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Correo", "Sin Seleccionar, Debe Ser Un Correo Valido"));
                }
            } else {
                cliente.setCorreo("");
            }

            if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
                System.out.println("validar Direccion " + cliente.isControlNormalizada());
                if (cliente.isControlNormalizada() || venta.medioIngreso.equalsIgnoreCase("Cross") || cliente.isControlCerca() || Utilidades.CoberturaRural(cliente)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Direccion",
                            context.getResources().getString(R.string.normailizardireccion)));
                }
            }

            if (!Utilidades.excluir("excluirContacto1", cliente.getCiudad())) {

                if (!cliente.getContacto1().getNombres().equals("") && !cliente.getContacto1().getApellidos().equals("")
                        && (!cliente.getContacto1().getTelefono().equals(""))) {
                    if (cliente.getContacto1().getTelefono().length() == 7
                            || cliente.getContacto1().getTelefono().length() == 10) {
                        if (!Utilidades.ListaNegraTelefonos(cliente.getContacto1().getTelefono())) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Telefono Contacto Personal",
                                    context.getResources().getString(R.string.telefonoingresadoinvalido)));
                        }
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Telefono Contacto Personal",
                                "Debe Estar Compuesto Por 7 o 10 Digitos"));
                    }

                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Contacto Personal",
                            context.getResources().getString(R.string.datoscontactosindiligenciar)));
                }

            }
            if (!Utilidades.excluir("excluirContacto2", cliente.getCiudad())) {

                if (!cliente.getContacto2().getNombres().equals("") && !cliente.getContacto2().getApellidos().equals("")
                        && (!cliente.getContacto2().getTelefono().equals(""))
                        && (cliente.getContacto2().getParentesco() != null
                        && !cliente.getContacto2().getParentesco().equals(Utilidades.inicial_opcion))) {
                    if (cliente.getContacto2().getTelefono().length() == 7
                            || cliente.getContacto2().getTelefono().length() == 10) {
                        if (!Utilidades.ListaNegraTelefonos(cliente.getContacto2().getTelefono())) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Telefono Contacto Familiar",
                                    context.getResources().getString(R.string.telefonoingresadoinvalido)));
                        }
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Telefono Contacto Familiar",
                                "Debe Estar Compuesto Por 7 o 10 Digitos"));
                    }
                    // reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Contacto Familiar",
                            context.getResources().getString(R.string.datoscontactosindiligenciar)));
                }
            }

            if (Utilidades.excluirMunicipal("habilitarCampos", "domiciliacion", cliente.getCiudad())) {
                if (!cliente.getDomiciliacion().equals(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.domiciliacion),
                            context.getResources().getString(R.string.domiciliaciondebeserdiligenciada)));
                }
            }

            System.out.println("reg " + reg);

            System.out.println("mensajes " + mensajes);

            if (!reg.contains(false)) {
                return true;
            } else {
                mensajes.put("Titulo", "Campos Requeridos");
                mensajes.put("Mensajes", ja);

                setMensajes(mensajes);
                return false;

            }

        } catch (JSONException e) {
            Log.w("Error ", e.getMessage());
            return false;
        }
    }

    public static boolean validarClienteProspecto(Cliente cliente, Prospecto prospecto) {
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        mensaje.clear();
        mensaje.add(new ListaDefault(0, "Validaciones Prospecto", ""));
        if (!cliente.getNombre().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Nombre", "No diligenciado"));
        }

        if (!cliente.getCedula().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Documento", "No diligenciado"));
        }

        if (!cliente.getDireccion().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Dirección", "No diligenciado"));
        }

        if (!cliente.getBarrio().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Barrio", "No diligenciado"));
        }

        if (!cliente.getTelefono().equals("") || !cliente.getTelefono2().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Teléfono", "No diligenciado"));
        }

        if (!cliente.getCelular().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Celular", "No diligenciado"));
        }

        if (!prospecto.getProductos().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Productos", "No diligenciado"));
        }

        if (!prospecto.getMotivo().equals("-- Seleccione Motivo --") && !prospecto.getMotivo().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
            mensaje.add(new ListaDefault(1, "Motivo", "No diligenciado"));
        }

        if (cliente.isControlFacturacion()) {
            if (!cliente.getFacturacion().getDepartamento().equalsIgnoreCase(Utilidades.iniDepartamento) && !cliente
                    .getFacturacion().getDepartamento().equalsIgnoreCase(Utilidades.seleccioneDepartamento)) {
                reg.add(true);
            } else {
                reg.add(false);
            }

            if (!cliente.getFacturacion().getMunicipio().equalsIgnoreCase(Utilidades.iniMunicipio)
                    && !cliente.getFacturacion().getMunicipio().equalsIgnoreCase(Utilidades.seleccioneMunicipio)) {
                reg.add(true);
            } else {
                reg.add(false);
            }

            if (!cliente.getFacturacion().getDireccion().equalsIgnoreCase("")) {
                reg.add(true);
            } else {
                reg.add(false);
            }
        }

        if (!reg.contains(false)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validarCompetencia(Cliente cliente, Competencia competencia, Context context) {
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        boolean mensajeCartera = false;

        try {

            mensajes = new JSONObject();

            JSONArray ja = new JSONArray();

			/*
             * if (!competencia.getCompetencia().equals(
			 * "--Seleccione Competencia--" ) ||
			 * (competencia.getCompetencia().equals("OTRA") && !competencia
			 * .getOtrasCompetencias().equals(""))) { reg.add(true); } else {
			 * reg.add(false); }
			 *
			 * if (!competencia.getProductos().equals("")) { reg.add(true); }
			 * else { reg.add(false); }
			 *
			 * if (!competencia.getVigenciaContrato().equals("")) {
			 * reg.add(true); } else { reg.add(false); }
			 *
			 * if (!competencia.getPagoMensual().equals("")) { reg.add(true); }
			 * else { reg.add(false); }
			 */

            System.out.println("competencia.getAtencion() " + competencia.getAtencion());
            if (!competencia.getAtencion().equalsIgnoreCase("-- Seleccione Atencion --")
                    && !competencia.getAtencion().equalsIgnoreCase("N/A")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Atención", "Sin Seleccionar"));
            }

            if (competencia.getAtencion().equalsIgnoreCase("ATIENDE")) {
                if (!competencia.getNoUne().equals("-- Seleccione Motivo --")) {
                    reg.add(true);

                    if (competencia.getNoUne().equalsIgnoreCase("CREDITO Y CARTERA") || competencia.getNoUne().equalsIgnoreCase("MOTIVO DE NO PAGO")) {

                        mensajeCartera = true;

                        if (!cliente.getNombre().equals("")) {
                            reg.add(true);
                        } else {

                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Nombres", "Sin Diligenciar"));
                        }

                        if (!cliente.getApellido().equals("")) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Apellidos", "Sin Diligenciar"));
                        }

                        if (!cliente.getTipoDocumento().equalsIgnoreCase("")
                                && !cliente.getTipoDocumento().equalsIgnoreCase(Utilidades.inicial_opcion)
                                && !cliente.getTipoDocumento().equalsIgnoreCase("N/A")) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Tipo Documento", "Sin Seleccionar"));
                        }

                        if (!cliente.getCedula().equals("")) {
                            if (Utilidades.tamanoCedula(cliente.getCedula(), cliente.getTipoDocumento())) {
                                if (!Utilidades.ListaNegraCedula(cliente.getCedula())) {
                                    reg.add(true);
                                } else {
                                    reg.add(false);
                                    ja.put(Utilidades.jsonMensajes("Documento", context.getResources()
                                            .getString(co.com.une.appmovilesune.R.string.listaNegraDocumento)));
                                }
                            } else {
                                reg.add(false);
                                ja.put(Utilidades.jsonMensajes("Documento", context.getResources()
                                        .getString(co.com.une.appmovilesune.R.string.tamanoDocumento)));
                            }
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Documento", "Sin Diligenciar"));
                        }

                        if (!cliente.getExpedicion().equals("")) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Fecha Expedición", "Sin Diligenciar"));
                        }

                        System.out.println("cliente.getTelefono2() " + cliente.getTelefono2());

                        if (!cliente.getTelefono2().equals("")) {
                            if (cliente.getTelefono2().length() == 7 || cliente.getTelefono2().length() == 10) {
                                reg.add(true);
                            } else {
                                reg.add(false);
                                ja.put(Utilidades.jsonMensajes("El Teléfono",
                                        "Debe Estar Compuesto Por 7 o 10 Digitos"));
                            }
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("El Teléfono", "Sin Diligenciar"));
                        }

                        if (!cliente.getDireccion().equals("")) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Dirección", "Sin Diligenciar"));
                        }

                        System.out.println("competencia.getSubMotivo() " + competencia.getSubMotivo());

                        if (!competencia.getSubMotivo().equals(Utilidades.inicial_sub_motivo)) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("SubMotivo", "Sin Diligenciar"));
                            ja.put(Utilidades.jsonMensajes("CREDITO Y CARTERA",
                                    "Para realizar esta acción se debe haber consultado previamente Scoring."));
                        }

                        if (competencia.getNoUne().equalsIgnoreCase("MOTIVO DE NO PAGO")) {
                            if (cliente.isValidarCorreo()) {
                                System.out.println("validacion correo " + Utilidades.validateEmail(cliente.getCorreo()));
                                if (Utilidades.validateEmail(cliente.getCorreo())) {
                                    reg.add(true);
                                } else {
                                    reg.add(false);
                                    ja.put(Utilidades.jsonMensajes("Correo", "Sin Seleccionar, Debe Ser Un Correo Valido"));
                                }
                            } else {
                                cliente.setCorreo("");
                            }
                        }

                    } else if (competencia.getNoUne().equalsIgnoreCase("DECISION DEL CLIENTE")) {
                        if (!Utilidades.excluir("excluirCompetencia", cliente.getCiudad())) {
                            if (competencia.Competencia != null && competencia.Competencia.size() > 0) {
                                reg.add(true);
                            } else {
                                reg.add(false);
                                ja.put(Utilidades.jsonMensajes("Datos Competencia", "Sin Diligenciar"));
                            }
                        }
                    }

                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Motivo", "Sin Seleccionar"));
                }
            }

            if (!mensajeCartera) {
                if (!cliente.getTelefono2().equals("") || !cliente.getDireccion().equals("")) {

                    // reg.add(true);
                    if (!cliente.getTelefono2().equals("")) {
                        if (cliente.getTelefono2().length() == 7 || cliente.getTelefono2().length() == 10) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("El Teléfono", "Debe Estar Compuesto Por 7 o 10 Digitos"));
                        }
                    } else {
                        reg.add(true);
                    }
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Campos Necesario",
                            "Debe Diligenciar al menos el Teléfono o la Dirección"));
                }
            }

            if (!reg.contains(false)) {
                return true;
            } else {

                mensajes.put("Titulo", "Campos Requeridos");
                mensajes.put("Mensajes", ja);

                setMensajes(mensajes);

                return false;
            }

        } catch (JSONException e) {
            Log.w("Error ", e.getMessage());
            return false;
        }

    }

    public static boolean validarVenta(Venta venta) {
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        boolean agenda = false;

        try {
            mensajes = new JSONObject();

            JSONArray ja = new JSONArray();

            System.out.println("validar Agenda " + venta.isValidarAgenda());
            System.out.println("venta.getHorarioAtencion() " + venta.getHorarioAtencion());

            if (!venta.isValidarOfertaScooring()) {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Oferta", venta.getMensajeScore()));
            }

            if (venta.isValidarAgenda()) {
                if (!venta.getHorarioAtencion().equals("-- Seleccione Horario Atención --")
                        && !venta.getHorarioAtencion().equalsIgnoreCase("")) {
                    reg.add(true);
                    agenda = true;
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Agenda", "Sin Diligenciar"));
                }
            }

            for (int i = 0; i < venta.getCotizacionCliente().getProductoCotizador().size(); i++) {
                if(!Utilidades.validarVacioProducto(venta.getCotizacionCliente().getProductoCotizador().get(i).getPlan())) {
                    if (venta.getCotizacionCliente().getProductoCotizador().get(i).getTipo() == ProductoCotizador.getTELEFONIA()) {
                        venta.getCotizacionCliente().getProductoCotizador().get(i).imprimir("validarVenta");
                        if (!venta.getCotizacionCliente().getProductoCotizador().get(i).getLinea().equals("-- Seleccione Pago Linea --")) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Pago Linea", "Sin Seleccionar"));
                        }
                    } else if (venta.getCotizacionCliente().getProductoCotizador().get(i).getTipo() == ProductoCotizador.getTELEVISION()) {
                        //cotizacionCliente.getProductoCotizador().set(i, rtv.getProductoCotizador());
                        if (!Utilidades.excluirNacional("tipoTivo", venta.getCotizacionCliente().getProductoCotizador().get(i).getPlan())) {
                            //System.out.println("decos json" + venta.getTelevision()[12]);

                            UtilidadesDecos.imprimirDecos("Validar Decos ", venta.getCotizacionCliente().getProductoCotizador().get(i).getItemDecodificadores());

                            JSONObject decos;
                            int ext = 0;

                            try {

                               decos = new JSONObject(venta.getCotizacionCliente().getProductoCotizador().get(i).getJsonDecos());
                               if (decos.has("ext")) {
                                    ext = Utilidades.convertirNumericos(decos.getString("ext"), "decos.getString(ext)");
                                }
                            } catch (Exception e) {
                                Log.w("Error ", e.getMessage());
                            }


                            JSONObject datosDecos = UtilidadesDecos.datosValidarDecos(venta.getCotizacionCliente().getProductoCotizador().get(i).getObjectDecodificador().getItemDecodificadors(), ext);

                            //System.out.println("decos validacion "+datosDecos);

                            boolean valicionDecos = UtilidadesDecos.validarDecos(datosDecos, venta.getCotizacionCliente().getProductoCotizador().get(i).getPlan());

                            //System.out.println("valicionDecos " + valicionDecos);

                            if (venta.getCotizacionCliente().getProductoCotizador().get(i).getPlan().contains("Existente")) {
                                valicionDecos = true;
                            }

                            if (valicionDecos) {
                                reg.add(true);
                            } else {
                                reg.add(false);
                                ja.put(Utilidades.jsonMensajes("Config", "La configuracion de los decos es erronea"));
                            }
                        }
                    } else if (venta.getCotizacionCliente().getProductoCotizador().get(i).getTipo() == ProductoCotizador.getINTERNET()) {
                        //cotizacionCliente.getProductoCotizador().set(i, rba.getProductoCotizador());
                    }
                }
            }

            /*if (!venta.getTelefonia()[0].equals("-") && !venta.getTelefonia()[0].equals("")) {
                if (!venta.getTelefonia()[3].equals("-- Seleccione Pago Linea --")) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Pago Linea", "Sin Seleccionar"));
                }

                if (!venta.getTelefonia()[6].equals("-")) {
                    if (!venta.getTelefonia()[7].equals("-- Seleccione Duración --")) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                    }
                } else {
                    reg.add(true);
                }

                if (!venta.getTelefonia()[4].equals("Nueva")) {
                    if (!venta.getTelefonia()[5].equals("-- Seleccione Tipo Migración --")) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                    }
                } else {
                    reg.add(true);
                }

            } else {
                reg.add(true);
            }*/

            /*if (!venta.getTelevision()[0].equals("-")) {

                if (!venta.getTelevision()[13].equalsIgnoreCase(Utilidades.inicial_opcion)) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Tipo Tecnologia", "Debe Seleccionar El Tipo Tecnologia"));
                }

                System.out.println("venta.isValidarTelevisores() " + venta.isValidarTelevisores());
				/*
				 * if (venta.isValidarTelevisores() ||
				 * venta.getTelevision()[2].equalsIgnoreCase("N/A")) {
				 * reg.add(true); } else { reg.add(false);
				 * ja.put(Utilidades.jsonMensajes("Televisores",
				 * "Televisores vs Decos Erroneo")); }
				 */

                System.out.println("venta.isValidarDecosPago() " + venta.isValidarDecosPago());
				/*
				 * if (venta.isValidarDecosPago() ||
				 * Utilidades.excluirNacional("planesDigitales",
				 * venta.getTelevision()[0])) { reg.add(true); } else {
				 * reg.add(false); ja.put(Utilidades.jsonMensajes(
				 * "Decos Adicionales",
				 * "No Puede Seleccionar Decos Sin Canales Compatibles")); }
				 */

                /*if (Utilidades.excluir("validarAgendaGPON", venta.getMunicipio())) {
                    if (venta.getTelevision()[13].equalsIgnoreCase("GPON") && agenda) {
                        // reg.add(true);
                        if (venta.getHorarioAtencion().contains("AM")) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("GPON", "Solo Se Puede Agendar GPON En Horas De La Mañana"));
                        }
                    }
                }

                if (!venta.getTelevision()[3].equals("-")) {
                    if (!venta.getTelevision()[4].equals("-- Seleccione Duración --")) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                    }
                } else {
                    reg.add(true);
                }

                System.out.println("venta.getTelevision()[2] " + venta.getTelevision()[2]);

                if (!Utilidades.excluirNacional("tipoTivo", venta.getTelevision()[0])) {
                    System.out.println("decos json" + venta.getTelevision()[12]);

                    UtilidadesDecos.imprimirDecos("Validar Decos ", venta.getItemDecodificadors());

                    JSONObject decos;
                    int ext = 0;

                    try {

                        decos = new JSONObject(venta.getTelevision()[12]);
                        if (decos.has("ext")) {
                            ext = Utilidades.convertirNumericos(decos.getString("ext"), "decos.getString(ext)");
                        }
                    } catch (Exception e) {
                        Log.w("Error ", e.getMessage());
                    }

                    JSONObject datosDecos = UtilidadesDecos.datosValidarDecos(venta.getItemDecodificadors(), ext);

                    boolean valicionDecos = UtilidadesDecos.validarDecos(datosDecos, venta.getTelevision()[0]);

                    System.out.println("valicionDecos " + valicionDecos);

                    if (venta.getTelevision()[0].contains("Existente")) {
                        valicionDecos = true;
                    }

                    if (valicionDecos) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Config", "La configuracion de los decos es erronea"));
                    }
                }

            } else {
                reg.add(true);
            }*/

            /*if (!venta.getInternet()[0].equals("-")) {
                if (!venta.getInternet()[5].equals("-")) {
                    if (!venta.getInternet()[6].equals("-- Seleccione Duración --")) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                    }
                } else {
                    reg.add(true);
                }

                if (!venta.getInternet()[3].equals("Nueva")) {
                    if (!venta.getInternet()[4].equals("-- Seleccione Tipo Migración --")) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                    }
                } else {
                    reg.add(true);
                }
            }*/

            if (!reg.contains(false)) {
                return true;
            } else {
                mensajes.put("Titulo", "Campos Requeridos");
                mensajes.put("Mensajes", ja);

                setMensajes(mensajes);
                return false;
            }

        } catch (JSONException e) {
            Log.w("Error ", e.getMessage());
            return false;
        }
    }



    public static boolean validarProspecto(Prospecto prospecto) {
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        if (!prospecto.getProductos().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
        }

        if (!prospecto.getMotivo().equals("")) {
            reg.add(true);
        } else {
            reg.add(false);
        }

        if (!reg.contains(false)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validarBlindaje(Blindaje blindaje) {
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        if (blindaje.nombreCliente.equals("Otro")) {
            if (blindaje.formulario == null) {
                reg.add(false);
            } else {
                reg.add(true);
            }

        } else {
            reg.add(true);
        }

        if (!reg.contains(false)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validarAgendaSiebel(Cliente cliente) {
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        int tamanoDocumento = 0;
        String telefonoServicio = "";
        try {
            mensajes = new JSONObject();
            JSONArray ja = new JSONArray();
            if (!cliente.getNombre().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Nombres", "Sin Diligenciar"));
            }
            if (!cliente.getApellido().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Apellidos", "Sin Diligenciar"));
            }
            if (!cliente.getCedula().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Documento", "Sin Diligenciar"));
            }

            System.out.println("cliente.getDireccion() " + cliente.getDireccion());

            if (!cliente.getDireccion().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Dirección", "Sin Diligenciar"));
            }

            System.out.println("cliente.getIdDireccionGis() " + cliente.getIdDireccionGis());

            if (cliente.getIdDireccionGis() != null && !cliente.getIdDireccionGis().equals("")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Dirección", "Debe Estandarizar La Direccion"));
            }
            telefonoServicio = Utilidades.limpiarTelefono(cliente.getTelefono());
            if (!telefonoServicio.equals("") || !cliente.getTelefono2().equals("")) {
                if (!telefonoServicio.equals("")) {
                    if (telefonoServicio.length() == 7) {
                        if (!Utilidades.ListaNegraTelefonos(telefonoServicio)) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Telefono De Servicio",
                                    "El Teléfono Ingresado No Es Valido"));
                        }
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Telefono De Servicio", "Debe Estar Compuesto Por 7 Digitos"));
                    }
                }
                if (!cliente.getTelefono2().equals("")) {
                    if (cliente.getTelefono2().length() == 7 || cliente.getTelefono2().length() == 10) {
                        if (!Utilidades.ListaNegraTelefonos(cliente.getTelefono2())) {
                            reg.add(true);
                        } else {
                            reg.add(false);
                            ja.put(Utilidades.jsonMensajes("Telefono Secundarios",
                                    "El Teléfono Ingresado No Es Valido"));
                        }
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Telefono Secundarios",
                                "Debe Estar Compuesto Por 7 o 10 Digitos"));
                    }
                } else {
                    reg.add(true);
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Telefonos", "Sin Diligenciar"));
            }
            if (!cliente.getCelular().equals("") && cliente.getCelular().length() == 10) {
                if (!Utilidades.ListaNegraTelefonos(cliente.getCelular())) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Celular", "El Celular Ingresado No Es Valido"));
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Celular", "Sin Diligenciar, Debe Estar Compuesto Por 10 Digitos"));
            }
            if (!cliente.getContacto1().getNombres().equals("") && !cliente.getContacto1().getApellidos().equals("")
                    && (!cliente.getContacto1().getTelefono().equals(""))) {
                if (cliente.getContacto1().getTelefono().length() == 7
                        || cliente.getContacto1().getTelefono().length() == 10) {
                    if (!Utilidades.ListaNegraTelefonos(cliente.getContacto1().getTelefono())) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes("Telefono Contacto Personal",
                                "El Teléfono Ingresado No Es Valido"));
                    }
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes("Telefono Contacto Personal",
                            "Debe Estar Compuesto Por 7 o 10 Digitos"));
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes("Contacto Personal",
                        "Algunos Datos Del Contacto Están Sin Diligenciar"));
            }
            System.out.println("reg " + reg);
            System.out.println("mensajes " + mensajes);
            if (!reg.contains(false)) {
                return true;
            } else {
                mensajes.put("Titulo", "Campos Requeridos");
                mensajes.put("Mensajes", ja);
                setMensajes(mensajes);
                return false;
            }
        } catch (JSONException e) {
            Log.w("Error ", e.getMessage());
            return false;
        }
    }

    public static boolean validarDatosVenta(String consolidado) {

        ArrayList<Boolean> validar = new ArrayList<Boolean>();

        try {

            JSONObject asesoria = new JSONObject(consolidado);

            System.out.println("JSON asesoria " + asesoria);

            if (asesoria.has("venta")) {
                if (!asesoria.getString("venta").equalsIgnoreCase("")) {
                    System.out.println("es ventas");

                    if (asesoria.has("cliente")) {
                        JSONObject cliente = asesoria.getJSONObject("cliente");

                        // System.out.println("cliente antes " + cliente);
                        //
                        // cliente.remove("direcciones");
                        // cliente.remove("telefonos");
                        // cliente.remove("contactos");
                        //
                        // System.out.println("cliente eliminacion " + cliente);
                        //
                        // cliente.put("direcciones", "");
                        //
                        // cliente.put("telefonos", "");
                        //
                        // cliente.put("direcciones", new JSONObject());
                        //
                        // cliente.put("telefonos", new JSONArray());
                        //
                        // System.out.println("cliente despues " + cliente);

                        validar.add(Utilidades.validarEtiquetaJson(cliente, "direcciones", "direccion"));
                        validar.add(Utilidades.validarEtiquetaJson(cliente, "telefonos", "tipo"));
                        validar.add(Utilidades.validarEtiquetaJson(cliente, "contactos", "telefono"));

                    } else {
                        validar.add(false);
                    }
                } else {
                    System.out.println("Cadena Venta Vacia");
                }

            }

            if (asesoria.has("competencia")) {
                if (!asesoria.getString("competencia").equalsIgnoreCase("")) {
                    System.out.println("es competencia");
                } else {
                    System.out.println("Cadena Competencia Vacia");
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            validar.add(false);

        }

        System.out.println(validar);

        if (validar.contains(false)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validarCotizacion(Cliente cliente, CotizacionCliente cotizacionCliente, Scooring scooring, Context context) {

        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        tipoControlador.clear();

        try {
            mensajes = new JSONObject();

            JSONArray ja = new JSONArray();

            if (cotizacionCliente.getControl().equalsIgnoreCase("00")) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.cotizacion), context.getResources().getString(R.string.cotizacioninvalidanocontinua)));
                tipoControlador.add(new String("Cotizacion"));
            }

            if (UtilidadesTarificador.validarDependencias(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getTELEVISION()), context)) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.dependencias), context.getResources().getString(R.string.mensajedependencias)));
                tipoControlador.add(new String("Cotizacion"));
            }

            if (UtilidadesTarificador.validarDecos(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getTELEVISION()).getObjectDecodificador(), context)) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.decos), context.getResources().getString(R.string.mensajedecos)));
                tipoControlador.add(new String("Cotizacion"));
            }

            if (cliente.isControlEstadoCuenta()) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.estadocuenta), context.getResources().getString(R.string.mensajeestadocuenta)));
                tipoControlador.add(new String("Scoring"));
            }

            if (UtilidadesTarificadorNew.validarCarrusel(cotizacionCliente, cliente, context )) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.carruselTitulo), context.getResources().getString(R.string.carrusel)));
                tipoControlador.add(new String("Carrusel"));
            }

            if (UtilidadesTarificadorNew.validarCotizacionClienteNuevo(cliente, cotizacionCliente)) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.clientenuevo), context.getResources().getString(R.string.mensajeCotizacioninvalidaClienteNuevo)));
                tipoControlador.add(new String("Cotizacion"));
            }

            if (Utilidades.validarTelefonos(cliente, context)) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.telefonosinvalidos), context.getResources().getString(R.string.mensajetelefonosinvalidos)));
                tipoControlador.add(new String("Cliente"));
            }

            if (!cliente.getTelefonoDestino().equalsIgnoreCase("")
                    && cliente.getTelefonoDestino().length() >= 7) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.telefonodestino), context.getResources().getString(R.string.mensajetelefonodestino)));
                tipoControlador.add(new String("Cliente"));
            }

            if ((!cliente.getDireccion().equalsIgnoreCase("") || cliente.isControlCerca())
                    && !cliente.getCedula().equalsIgnoreCase("")
                    && !cliente.getTipoDocumento()
                    .equalsIgnoreCase(Utilidades.inicial_opcion)) {
                reg.add(true);
                if (UtilidadesTarificadorNew.validarEstandarizacion(cliente,context) || cliente.isControlCerca() || Utilidades.CoberturaRural(cliente)) {
                    reg.add(true);

                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.direccion), context.getResources().getString(R.string.estandarizardireccion2)));
                    tipoControlador.add(new String("Cliente"));
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.campossensibles), context.getResources().getString(R.string.mensajecampossensibles)));
                tipoControlador.add(new String("Cliente"));
            }

            if ((cliente.getTipoPropiedad() != null
                    && !cliente.getTipoPropiedad()
                    .equalsIgnoreCase(Utilidades.inicial_opcion)
                    && !cliente.getTipoPropiedad()
                    .equalsIgnoreCase("N/A"))
                    || Utilidades.excluir("excluirAgenda",
                    cliente.getCiudad())) {
                reg.add(true);
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.tipopropiedad), context.getResources().getString(R.string.mensajetipopropiedad)));
                tipoControlador.add(new String("Cliente"));
            }

            if (cliente.isRealizoConfronta()) {
                reg.add(true);
                if (cliente.isConfronta()) {
                    reg.add(true);
                } else {
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.confronta), context.getResources().getString(R.string.mensajeconfrontanoaprobado)));
                    tipoControlador.add(new String("Scoring"));
                }
            } else {
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.confronta), context.getResources().getString(R.string.mensajeconfrontanorealizado)));
                tipoControlador.add(new String("Scoring"));
            }

            if(UtilidadesTarificadorNew.validarCarteraUNE(cliente, scooring)) {
                reg.add(true);
            }else{
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.cuentasune), context.getResources().getString(R.string.mensajecuentasune)));
                tipoControlador.add(new String("Scoring"));
            }

            if (UtilidadesTarificadorNew.validarScooring(cliente,cotizacionCliente,scooring,context)) {
                reg.add(true);
            }else{
                reg.add(false);
                ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.scoring), context.getResources().getString(R.string.mensajescoring)));
                tipoControlador.add(new String("Scoring"));
            }

            if(cliente.getPortafolioUNE() != null && cliente.getPortafolioUNE().validarPortafolio){
                if(cliente.getPortafolioUNE().getPaqueteSeleccionado() != null  && !cliente.getPortafolioUNE().getPaqueteSeleccionado().equals("")){
                    if(UtilidadesTarificadorNew.validarCotizacionvsPortafolio(cliente,cotizacionCliente,context)){
                        reg.add(true);
                    }else{
                        reg.add(false);
                        ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.cotizacion), context.getResources().getString(R.string.cotizacionvsportafolio)));
                        tipoControlador.add(new String("Cotizacion"));
                    }
                }else{
                    reg.add(false);
                    ja.put(Utilidades.jsonMensajes(context.getResources().getString(R.string.cotizacion), context.getResources().getString(R.string.cotizacionvsportafolio)));
                    tipoControlador.add(new String("Cotizacion"));
                }
            }

            System.out.println("reg " + reg);

            System.out.println("mensajes " + mensajes);

            if (!reg.contains(false)) {
                return true;
            } else {
                mensajes.put("Titulo", "Campos Requeridos");
                mensajes.put("Mensajes", ja);

                setMensajes(mensajes);
                return false;

            }

        } catch (JSONException e) {
            Log.w("Error ", e.getMessage());
            return false;
        }
    }

    public static JSONObject getMensajes() {
        return mensajes;
    }

    public static void setMensajes(JSONObject mensajes) {
        Validaciones.mensajes = mensajes;
    }

    public static ArrayList<ListaDefault> getMensaje() {
        return mensaje;
    }

    public static void setMensaje(ArrayList<ListaDefault> mensaje) {
        Validaciones.mensaje = mensaje;
    }

    public static ArrayList<String> getTipoControlador() {
        return tipoControlador;
    }

    public static void setTipoControlador(ArrayList<String> tipoControlador) {
        Validaciones.tipoControlador = tipoControlador;
    }
}
