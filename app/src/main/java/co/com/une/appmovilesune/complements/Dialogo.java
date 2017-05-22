package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.components.Autocomplete;
import co.com.une.appmovilesune.components.FormularioConfiguracion;
import co.com.une.appmovilesune.components.FormularioResumido;
import co.com.une.appmovilesune.components.ItemCompetenciaFormulario;

public class Dialogo extends Dialog {

	public static final int DIALOGO_FECHA = 0;
	public static final int DIALOGO_ALERTA = 1;
	public static final int DIALOGO_CONFIRMACION = 2;
	public static final int DIALOGO_SELECTOR_PRODUCTO = 3;
	public static final int DIALOGO_ASESORIA = 4;
	public static final int DIALOGO_CONFIGURACION = 5;
	public static final int DIALOGO_SELECTOR_FRANJA = 6;
	public static final int DIALOGO_SELECTOR_BARRIO = 7;
	public static final int DIALOGO_SELECTOR_ASESORIA = 8;
	public static final int DIALOGO_SELECTOR_RECUPERADOR = 9;
	public static final int DIALOGO_SELECTOR_TIPO_ASESORIA = 10;
	public static final int DIALOGO_SELECTOR_HOBBIES = 11;
	public static final int DIALOGO_FORMULARIO_COMPETENCIA = 12;
	public static final int DIALOGO_CUSTOM = 13;

    private String[] StHobbies = {"Cine", "Teatro", "Musica", "TV", "Lectura", "Deportes", "Otros"};
    private String[] StProductos = {"TO", "TV", "BA", "3G", "4G"};

    public Dialog dialogo;

    private ArrayList<String> productos;
    private ArrayList<String> hobbies;
    private String fechaSeleccion = "NoDefinida";
    private String[] asesoria = new String[4];
    private String[] configuracion = new String[2];
    private String[] competencia = new String[5];
    private String franja = "AM";
    private String idAsesoria = "";
    private String selectTipoAsesoria = "";
    private String barrio = "";
    private boolean seleccion = false;
    private String lanzador = "";

    private Context context;
    private int Tipo;
    public FormularioResumido fr;
    public FormularioConfiguracion fc;
    public ItemCompetenciaFormulario icf;
    public Autocomplete ac;

    public Dialogo(Context context, int Tipo, String mensaje) {
        super(context);
        this.context = context;
        this.Tipo = Tipo;
        switch (Tipo) {
            case DIALOGO_FECHA:
                dialogo = new DatePickerDialog(context, establecerFecha, Calendario.getAnio(), Calendario.getMes(),
                        Calendario.getDia());
                dialogo.setTitle("Fecha");
                break;
            case DIALOGO_ALERTA:
                AlertDialog al = new AlertDialog.Builder(context).create();
                al.setInverseBackgroundForced(true);
                al.setIcon(R.drawable.icon_dialogo);
                al.setCancelable(false);
                al.setTitle("Alerta");
                al.setMessage(mensaje);
                al.setButton("OK", botonAlerta);
                dialogo = al;
                break;
            case DIALOGO_CONFIRMACION:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setInverseBackgroundForced(true);
                builder.setIcon(R.drawable.icon_dialogo);
                builder.setCancelable(false);
                builder.setTitle("Confirmacion");
                builder.setMessage(mensaje);
                builder.setPositiveButton("Aceptar", aceptaConfirmacion);
                builder.setNegativeButton("Cancelar", cancelaConfirmacion);
                dialogo = builder.create();
                break;
            case DIALOGO_SELECTOR_PRODUCTO:
                productos = new ArrayList<String>();
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setInverseBackgroundForced(true);
                builder2.setIcon(R.drawable.icon_dialogo);
                builder2.setCancelable(false);
                builder2.setTitle("Selector de Productos");
                builder2.setMultiChoiceItems(StProductos, null, seleccionarProductos);
                builder2.setPositiveButton("Listo", aceptaConfirmacionProductos);
                builder2.setNegativeButton("Cancelar", cancelaConfirmacionProductos);
                dialogo = builder2.create();
                break;
            case DIALOGO_ASESORIA:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                builder3.setInverseBackgroundForced(true);
                // builder3.setIcon(R.drawable.icon_dialogo);
                builder3.setCancelable(false);
                // builder3.setTitle("Confirmacion");
                fr = new FormularioResumido(context);
                LayoutInflater li = LayoutInflater.from(fr.getContext());
                LinearLayout someLayout = (LinearLayout) li.inflate(R.layout.dialogoasesoria, null);
                builder3.setView(someLayout);
                builder3.setPositiveButton("Aceptar", aceptaConfirmacionAsesoria);
                builder3.setNegativeButton("Cancelar", cancelaConfirmacionAsesoria);
                dialogo = builder3.create();
                break;
            case DIALOGO_CONFIGURACION:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(context);
                builder4.setInverseBackgroundForced(true);
                // builder4.setIcon(R.drawable.icon_dialogo);
                builder4.setCancelable(false);
                // builder4.setTitle("Confirmacion");
                fc = new FormularioConfiguracion(context);
                LayoutInflater lic = LayoutInflater.from(fc.getContext());
                LinearLayout someLayoutc = (LinearLayout) lic.inflate(R.layout.dialogoconfiguracion, null);
                builder4.setView(someLayoutc);
                builder4.setPositiveButton("Aceptar", aceptaConfirmacionConfiguracion);
                builder4.setNegativeButton("Cancelar", cancelaConfirmacionConfiguracion);
                dialogo = builder4.create();
                break;
            case DIALOGO_SELECTOR_FRANJA:
                AlertDialog.Builder builder5 = new AlertDialog.Builder(context);
                builder5.setInverseBackgroundForced(true);
                builder5.setIcon(R.drawable.icon_dialogo);
                builder5.setCancelable(false);
                builder5.setTitle("Selector de Franja");
                builder5.setSingleChoiceItems(Utilidades.franjas, 0, seleccionFranja);
                builder5.setPositiveButton("Listo", aceptaConfirmacionFranja);
                builder5.setNegativeButton("Cancelar", cancelaConfirmacionFranja);
                dialogo = builder5.create();
                break;
            case DIALOGO_SELECTOR_BARRIO:
                AlertDialog.Builder builder6 = new AlertDialog.Builder(context);
                builder6.setInverseBackgroundForced(true);
                builder6.setIcon(R.drawable.icon_dialogo);
                builder6.setCancelable(false);
                builder6.setTitle("Selector de Barrio");
                ac = new Autocomplete(context);
                LayoutInflater lib = LayoutInflater.from(ac.getContext());
                LinearLayout someLayoutb = (LinearLayout) lib.inflate(R.layout.autocomplete, null);
                builder6.setView(someLayoutb);
                builder6.setPositiveButton("Listo", aceptaSelectorBarrio);
                builder6.setNegativeButton("Cancelar", cancelaSelectorBarrio);
                dialogo = builder6.create();
                break;
            case DIALOGO_SELECTOR_ASESORIA:
                AlertDialog.Builder builder7 = new AlertDialog.Builder(context);
                builder7.setInverseBackgroundForced(true);
                builder7.setIcon(R.drawable.icon_dialogo);
                builder7.setCancelable(false);
                builder7.setTitle("Selector de Asesoria");
                builder7.setSingleChoiceItems(Utilidades.asesorias, 0, seleccionAsesoriaExistente);
                builder7.setPositiveButton("Listo", aceptaConfirmacionAsesoriaExistente);
                builder7.setNegativeButton("Cancelar", cancelaConfirmacionAsesoriaExistente);
                dialogo = builder7.create();
                break;
            case DIALOGO_SELECTOR_RECUPERADOR:
                AlertDialog.Builder builder8 = new AlertDialog.Builder(context);
                builder8.setInverseBackgroundForced(true);
                builder8.setIcon(R.drawable.icon_dialogo);
                builder8.setCancelable(false);
                builder8.setTitle(context.getResources().getString(R.string.seleccion));
                builder8.setMessage(mensaje);
                builder8.setPositiveButton("Tarificador", confirmaTarificador);
                builder8.setNegativeButton("Venta", confirmaVenta);
                dialogo = builder8.create();
                break;
            case DIALOGO_SELECTOR_TIPO_ASESORIA:
                AlertDialog.Builder builder9 = new AlertDialog.Builder(context);
                builder9.setInverseBackgroundForced(true);
                builder9.setIcon(R.drawable.icon_dialogo);
                builder9.setCancelable(false);
                builder9.setTitle(context.getResources().getString(R.string.seleccioneopcion));
                builder9.setSingleChoiceItems(Utilidades.tipo_asesoria, 0, seleccionTipoAsesoria);
                builder9.setPositiveButton("Listo", aceptaConfirmacionTipoAsesoria);
                builder9.setNegativeButton("Cancelar", cancelaConfirmacionTipoAsesoria);
                dialogo = builder9.create();
                break;
            case DIALOGO_SELECTOR_HOBBIES:
                hobbies = new ArrayList<String>();
                AlertDialog.Builder builder10 = new AlertDialog.Builder(context);
                builder10.setInverseBackgroundForced(true);
                builder10.setIcon(R.drawable.icon_dialogo);
                builder10.setCancelable(false);
                builder10.setTitle("Selector de Aficiones");
                builder10.setMultiChoiceItems(StHobbies, null, seleccionarhobbies);
                builder10.setPositiveButton("Listo", aceptaConfirmacionHobbies);
                builder10.setNegativeButton("Cancelar", cancelaConfirmacionHobbies);
                dialogo = builder10.create();
                break;
            case DIALOGO_FORMULARIO_COMPETENCIA:
                AlertDialog.Builder builder11 = new AlertDialog.Builder(context);
                builder11.setInverseBackgroundForced(true);
                // builder4.setIcon(R.drawable.icon_dialogo);
                builder11.setCancelable(false);
                // builder4.setTitle("Confirmacion");
                icf = new ItemCompetenciaFormulario(context);
                LayoutInflater licf = LayoutInflater.from(icf.getContext());
                LinearLayout someLayoutcf = (LinearLayout) licf.inflate(R.layout.itemcompetenciaformulario, null);
                builder11.setView(someLayoutcf);
                builder11.setPositiveButton("Aceptar", aceptaConfirmacionCompetencia);
                builder11.setNegativeButton("Cancelar", cancelaConfirmacionCompetencia);
                dialogo = builder11.create();
                break;
            default:
                break;
        }
    }

	public Dialogo(Context context, int Tipo, String titulo, String mensaje, String tituloBotonOK, String tituloBotonCancel, OnClickListener accionBotonOK, OnClickListener accionBotonCancel) {
		super(context);
		this.context = context;
		this.Tipo = Tipo;

		AlertDialog.Builder dialogCustom = new AlertDialog.Builder(context);
		dialogCustom.setInverseBackgroundForced(true);
		dialogCustom.setIcon(R.drawable.icon_dialogo);
		dialogCustom.setCancelable(false);
		dialogCustom.setTitle(titulo);
		dialogCustom.setMessage(mensaje);
		dialogCustom.setPositiveButton(tituloBotonOK, accionBotonOK);
		dialogCustom.setNegativeButton(tituloBotonCancel, accionBotonCancel);
		dialogo = dialogCustom.create();

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

    }

    private OnClickListener cancelaConfirmacionCompetencia = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionCompetencia = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            Spinner spnCompetencia = (Spinner) dialogo.findViewById(R.id.spnCompetencia);
            Spinner spnProducto = (Spinner) dialogo.findViewById(R.id.spnCompetenciaProducto);
            EditText txtOtraCompetencia = (EditText) dialogo.findViewById(R.id.txtOtraCompetencia);
            EditText txtVigenciaContrato = (EditText) dialogo.findViewById(R.id.txtVigenciaContrato);
            EditText txtPagoMensual = (EditText) dialogo.findViewById(R.id.txtPagoMensual);

            competencia[0] = spnCompetencia.getSelectedItem().toString();
            competencia[1] = spnProducto.getSelectedItem().toString();
            competencia[2] = txtOtraCompetencia.getText().toString();
            competencia[3] = txtVigenciaContrato.getText().toString();
            competencia[4] = txtPagoMensual.getText().toString();

            lanzador = "Competencia";
        }
    };

    private OnClickListener confirmaTarificador = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "confirmacionTarificador";
        }
    };

    private OnClickListener confirmaVenta = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            lanzador = "confirmacionVenta";
        }
    };

    private OnClickListener cancelaSelectorBarrio = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaSelectorBarrio = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

            AutoCompleteTextView txtBarrio = (AutoCompleteTextView) dialogo.findViewById(R.id.txtBarrio);
            barrio = txtBarrio.getText().toString();

            seleccion = true;
            lanzador = "barrio";
        }
    };

    private OnClickListener seleccionAsesoriaExistente = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            idAsesoria = Utilidades.asesorias[which].split("-")[0];
        }
    };

    private OnClickListener cancelaConfirmacionAsesoriaExistente = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionAsesoriaExistente = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (idAsesoria.equals("")) {
                idAsesoria = Utilidades.asesorias[0].split("-")[0];
            }
            seleccion = true;
            lanzador = "Cargar";
        }
    };

    private OnMultiChoiceClickListener seleccionarhobbies = new OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int item, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                hobbies.add(StHobbies[item]);
            } else {
                hobbies.remove(StHobbies[item]);
            }
        }
    };

    private OnClickListener seleccionTipoAsesoria = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            // selectTipoAsesoria =
            // Utilidades.tipo_asesoria[which].split("-")[0];
            selectTipoAsesoria = Utilidades.tipo_asesoria[which];
            // System.out.println("selectTipoAsesoria " + selectTipoAsesoria);
            // System.out.println("Evento");
        }
    };

    private OnClickListener cancelaConfirmacionTipoAsesoria = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionTipoAsesoria = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (selectTipoAsesoria.equals("")) {
                selectTipoAsesoria = Utilidades.tipo_asesoria[0];
                // System.out.println("selectTipoAsesoria " +
                // selectTipoAsesoria);
            }
            seleccion = true;
            lanzador = "TipoAsesoria";
        }
    };

    private OnClickListener seleccionFranja = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            switch (which) {
                case 0:
                    franja = "AM";
                    break;
                case 1:
                    franja = "PM";
                    break;
                default:
                    franja = "NoDefinida";
                    break;
            }
        }
    };

    private OnClickListener cancelaConfirmacionFranja = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionFranja = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            lanzador = "franja";
        }
    };

    private OnMultiChoiceClickListener seleccionarProductos = new OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int item, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                productos.add(StProductos[item]);
            } else {
                productos.remove(StProductos[item]);
            }
        }
    };

    private OnClickListener cancelaConfirmacionConfiguracion = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionConfiguracion = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            Spinner sltDepartamento = (Spinner) dialogo.findViewById(R.id.sltDepartamento);
            Spinner sltCiudad = (Spinner) dialogo.findViewById(R.id.sltCiudad);

            configuracion[0] = sltDepartamento.getSelectedItem().toString();
            configuracion[1] = sltCiudad.getSelectedItem().toString();
            lanzador = "configuracion";
        }
    };

    private OnClickListener cancelaConfirmacionAsesoria = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionAsesoria = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            Spinner sltMunicipio = (Spinner) dialogo.findViewById(R.id.sltCiudad);
            EditText txtDocuemto = (EditText) dialogo.findViewById(R.id.txtDocumento);
            EditText txtTelefono = (EditText) dialogo.findViewById(R.id.txtTelefono);
            EditText txtDireccion = (EditText) dialogo.findViewById(R.id.txtDireccion);

            asesoria[0] = (String) sltMunicipio.getSelectedItem();
            asesoria[1] = txtDocuemto.getText().toString();
            asesoria[2] = txtTelefono.getText().toString();
            asesoria[3] = txtDireccion.getText().toString();

            lanzador = "asesoria";

        }
    };

    private OnClickListener cancelaConfirmacionProductos = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionProductos = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            lanzador = "productos";
        }
    };

    private OnClickListener cancelaConfirmacion = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacion = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            lanzador = "confirmacion";
        }
    };

    private DialogInterface.OnClickListener botonAlerta = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            return;
        }
    };

    private DatePickerDialog.OnDateSetListener establecerFecha = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fechaSeleccion = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            seleccion = true;
        }
    };

    private OnClickListener cancelaConfirmacionHobbies = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = false;
            lanzador = "";
        }
    };

    private OnClickListener aceptaConfirmacionHobbies = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            seleccion = true;
            lanzador = "hobbies";
        }
    };

    public String getFechaSeleccion() {
        return fechaSeleccion;
    }

    public boolean isSeleccion() {
        return seleccion;
    }

    public void setSeleccion(boolean seleccion) {
        this.seleccion = seleccion;
    }

    public ArrayList<String> getProductos() {
        return productos;
    }

    public ArrayList<String> getHobbies() {
        return hobbies;
    }

    public String[] getAsesoria() {
        return asesoria;
    }

    public String[] getConfiguracion() {
        return configuracion;
    }

    public String[] getCompetencia() {
        return competencia;
    }

    public String getLanzador() {
        return lanzador;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getFranja() {
        return franja;
    }

    public void setFranja(String franja) {
        this.franja = franja;
    }

    public String getIdAsesoria() {
        return idAsesoria;
    }

    public void setIdAsesoria(String idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public String getSelectTipoAsesoria() {
        return selectTipoAsesoria;
    }

    public void setSelectTipoAsesoria(String selectTipoAsesoria) {
        this.selectTipoAsesoria = selectTipoAsesoria;
    }
}
