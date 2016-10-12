package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.ProductoAMG;

public class CompTVDigital extends RelativeLayout implements Subject {

	private ArrayList<Observer> observer = new ArrayList<Observer>();

	private RadioButton radio_elitepvr, radio_elite, radio_full, radio_superior, radio_esencial, radio_superior2,
			radio_bronce, radio_bronce2, radio_superior_iptv, radio_esencial_iptv, radio_bronce_iptv;

	private TextView lblCargoBasicoContenido, lblIvaContenido, lblTotalContenido, txtDescuento, txtPlan;
	private String departamento, estrato, descuento, duracion;

	private double cargoBasico, iva, total;

	private String plan, valorIndividual, valorIndividualIva, valorEmpaquetado, valorEmpaquetadoIva;
	private String estadoProducto;
	private ProductoAMG existente, producto;

	public Spinner spnProducto;

	private Cliente cliente;

	private boolean isHFC = true;

	public CompTVDigital(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompTVDigital(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompTVDigital(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.comptvdigital, this, true);

		spnProducto = (Spinner) findViewById(R.id.spnProducto);

		spnProducto.setOnItemSelectedListener(mostrarPlan);
		txtPlan = (TextView) findViewById(R.id.txtPlan);

		lblCargoBasicoContenido = (TextView) findViewById(R.id.lblCargoBasicoContenido);
		lblIvaContenido = (TextView) findViewById(R.id.lblIvaContenido);
		lblTotalContenido = (TextView) findViewById(R.id.lblTotalContenido);
		txtDescuento = (TextView) findViewById(R.id.txtDescuento);

		estadoProducto = "N";

		mostrarOcultarPlanes();
		ponerNombrePlanes();
		// mostrarValores();

	}

	public void setDeptoEstrato(String departamento, String ciudad, String estrato) {
		this.departamento = departamento;
		this.estrato = estrato;

		System.out.println("Estrato -> " + estrato);

		// Utilidades.mostrarTVDigital("planesTVDigitalVisible2", ciudad);

		ArrayList<String> plan = Utilidades.mostrarTVDigital("planesTVDigitalVisible2", ciudad);

		if (!isHFC) {
			plan = Utilidades.mostrarTVDigital("planesTVDigitalVisibleIPTV", ciudad);
		}

		if (plan.size() > 0) {
			mostrarOcultarPlanes_AC(plan);
		}
	}

	public void setDeptoEstratoPlan(String departamento, String estrato, ArrayList<String> plan, Cliente cliente) {
		this.departamento = departamento;
		this.estrato = estrato;
		this.cliente = cliente;

		System.out.println("plan " + plan);

		System.out.println("setDeptoEstratoPlan TV");

		mostrarOcultarPlanes_AC(plan);
	}

	private void mostrarOcultarPlanes() {

	}

	private void mostrarOcultarPlanes_AC(ArrayList<String> plan) {

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

		for (int i = 0; i < plan.size(); i++) {

			System.out.println("mostrarOcultarPlanes_AC " + plan.get(i));

			if (Utilidades.validarNacionalValor("mostrarDigital", plan.get(i)) && UtilidadesTarificador.buscarPlanes(departamento, plan.get(i))) {
				adaptador.add(plan.get(i));
			}
		}


		spnProducto.setAdapter(adaptador);

		cargarOfertaDefault(plan);

	}

	private void ponerNombrePlanes() {
		// if (isHFC) {
		// radio_elitepvr.setText(Utilidades.obtenerPlanTVDigital("elite+pvr",
		// "HFC"));
		// radio_elite.setText(Utilidades.obtenerPlanTVDigital("elite", "HFC"));
		// radio_full.setText(Utilidades.obtenerPlanTVDigital("full", "HFC"));
		// radio_superior.setText(Utilidades.obtenerPlanTVDigital("superior",
		// "HFC"));
		// radio_esencial.setText(Utilidades.obtenerPlanTVDigital("escencial",
		// "HFC"));
		// } else {
		// radio_elitepvr.setText(Utilidades.obtenerPlanTVDigital("elite+pvr",
		// "IPTV"));
		// radio_elite.setText(Utilidades.obtenerPlanTVDigital("elite",
		// "IPTV"));
		// radio_full.setText(Utilidades.obtenerPlanTVDigital("full", "IPTV"));
		// radio_superior.setText(Utilidades.obtenerPlanTVDigital("superior",
		// "IPTV"));
		// radio_esencial.setText(Utilidades.obtenerPlanTVDigital("escencial",
		// "IPTV"));
		// }
	}

	public void setPlan(String plan) {

		System.out.println("plan " + plan);

		this.plan = plan;

		// if (plan.equalsIgnoreCase("elite+pvr")) {
		// radio_elitepvr.setChecked(true);
		// this.plan = radio_elitepvr.getText().toString();
		// } else if (plan.equalsIgnoreCase("elite")) {
		// radio_elite.setChecked(true);
		// this.plan = radio_elite.getText().toString();
		// } else if (plan.equalsIgnoreCase("full")) {
		// radio_full.setChecked(true);
		// this.plan = radio_full.getText().toString();
		// } else if (plan.equalsIgnoreCase("superior")) {
		// radio_superior.setChecked(true);
		// this.plan = radio_superior.getText().toString();
		// } else if (plan.equalsIgnoreCase("escencial")) {
		// radio_esencial.setChecked(true);
		// this.plan = radio_esencial.getText().toString();
		// }
		mostrarValores();
		notifyObserver();
	}

	public String getPlan() {
		return plan;
	}

	public double getCargobasico() {
		return cargoBasico;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public String getDuracion() {
		return duracion;
	}

	public String getValorIndividual() {
		return valorIndividual;
	}

	public String getValorIndividualIva() {
		return valorIndividualIva;
	}

	public String getValorEmpaquetado() {
		return valorEmpaquetado;
	}

	public String getValorEmpaquetadoIva() {
		return valorEmpaquetadoIva;
	}

	public ProductoAMG getExistente() {
		return existente;
	}

	public void setExistente(ProductoAMG existente) {
		this.existente = existente;
	}

	public ProductoAMG getProducto() {
		return producto;
	}

	public void setProducto(ProductoAMG producto) {
		this.producto = producto;
	}

	public void setIPTV() {
		isHFC = false;
	}

	public void mostrarDescuento() {
		System.out.println("Mostratr Descuento " + duracion + " Mes la " + descuento + "%");
		if (!duracion.equals("0") && !descuento.equals("0")) {
			if (!duracion.equals("1")) {
				txtDescuento.setText(duracion + " Mes al " + descuento + "%");
			} else {
				txtDescuento.setText(duracion + " Meses al " + descuento + "%");
			}
		} else {
			txtDescuento.setText("");
		}
	}

	public void mostrarValores() {

		System.out.println("plan mostrarValores " + plan);

		if (plan != null) {
			ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios",
					new String[] { "empaquetado", "empaquetado_iva", "individual", "individual_iva", "homoPrimeraLinea" },
					"departamento like ? and producto like ? and tipo_producto = ? and estrato like ?",
					new String[] { departamento, plan, "tv", "%" + estrato + "%" }, null, null, null);

			if (respuesta != null) {
				
				

				valorEmpaquetado = respuesta.get(0).get(0);
				valorEmpaquetadoIva = respuesta.get(0).get(1);
				valorIndividual = respuesta.get(0).get(2);
				valorIndividualIva = respuesta.get(0).get(3);

				txtPlan.setText(respuesta.get(0).get(4));
				if(producto != null){
					producto.planFacturacion = respuesta.get(0).get(4);
				}
				lblCargoBasicoContenido.setText(respuesta.get(0).get(0));
				lblTotalContenido.setText(respuesta.get(0).get(1));
				int iva = Integer.parseInt(respuesta.get(0).get(1)) - Integer.parseInt(respuesta.get(0).get(0));
				lblIvaContenido.setText(String.valueOf(iva));
			}
		}
	}

	public String getTotal() {
		return lblTotalContenido.getText().toString();
	}

	public String getEstadoProducto() {
		return estadoProducto;
	}

	public void setEstadoProducto(String estadoProducto) {
		this.estadoProducto = estadoProducto;

	}

	// RadioButton.OnClickListener eventoRadios = new
	// RadioButton.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// switch (v.getId()) {
	// case R.id.radio_elitepvr:
	// plan = radio_elitepvr.getText().toString();
	// break;
	// case R.id.radio_elite:
	// plan = radio_elite.getText().toString();
	// break;
	// case R.id.radio_full:
	// plan = radio_full.getText().toString();
	// break;
	// case R.id.radio_superior:
	// plan = radio_superior.getText().toString();
	// break;
	// case R.id.radio_superior2:
	// plan = radio_superior2.getText().toString();
	// break;
	// case R.id.radio_esencial:
	// plan = radio_esencial.getText().toString();
	// break;
	// case R.id.radio_superior_iptv:
	// plan = radio_superior_iptv.getText().toString();
	// break;
	// case R.id.radio_esencial_iptv:
	// plan = radio_esencial_iptv.getText().toString();
	// break;
	// case R.id.radio_bronce:
	// plan = radio_bronce.getText().toString();
	// break;
	// case R.id.radio_bronce2:
	// plan = radio_bronce2.getText().toString();
	// break;
	// case R.id.radio_bronce_iptv:
	// plan = radio_bronce_iptv.getText().toString();
	// break;
	// default:
	// break;
	// }
	//
	// System.out.println("plan evento radio " + plan);
	//
	// mostrarValores();
	// notifyObserver();
	// }
	//
	// };

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		observer.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observer.remove(o);
	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub

		ArrayList<Object> data = new ArrayList<Object>();
		data.add("plan");
		data.add(plan);
		for (int i = 0; i < observer.size(); i++) {
			if (observer.get(i).getClass().getSimpleName().equals("ControlAmigoCuentasDigital")) {
				ArrayList<Object> respuestas = new ArrayList<Object>();
				respuestas.add("oferta");
				respuestas.add("");

				observer.get(i).update(respuestas);
			} else {
				observer.get(i).update(data);
			}

		}
	}

	private void cargarOfertaDefault(ArrayList<String> plan) {
		String oferta = "";

		String inConsulta = "";

		for (int i = 0; i < plan.size(); i++) {
			if (i == 0) {
				inConsulta = "'" + plan.get(i) + "'";
			} else {
				inConsulta += ",'" + plan.get(i) + "'";
			}
		}

		System.out.println("inConsulta " + inConsulta);

		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos
				.consultar2("select lst_item from listasestratos where " + "lst_estrato = " + estrato + " "
						+ "and lst_nombre = 'planesTVDigitalDefault2' " + "and lst_item IN (" + inConsulta + ")");

		// ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos
		// .consultar(
		// true,
		// "listasestratos",
		// new String[] { "lst_item" },
		// "lst_nombre=? and lst_estrato=? and lst_item IN (?)",
		// new String[] { "planesTVDigitalDefault2",
		// cliente.getEstrato(), inConsulta },
		// null, null, null);

		System.out.println("respuesta 1" + respuesta);

		if (respuesta != null) {
			oferta = respuesta.get(0).get(0);
		}

		if (!oferta.equals("")) {
			respuesta = MainActivity.basedatos.consultar(true, "listasgenerales", new String[] { "lst_nombre" },
					"lst_item=?", new String[] { oferta }, null, null, null);

			System.out.println("respuesta 2" + respuesta);

			if (respuesta != null) {
				ofertaDefault(respuesta.get(0).get(0));
			}
		}
	}

	public void ofertaDefault(String plan) {

		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnProducto.getAdapter();

		spnProducto.setSelection(adaptador.getPosition(plan));

		mostrarValores();
		notifyObserver();
	}

	OnItemSelectedListener mostrarPlan = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub

			plan = parent.getSelectedItem().toString();
			
			System.out.println("[Evento TV Digital] CompTVDigital "+plan);

			mostrarValores();
			notifyObserver();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	/*
	 * public static String camposUnicos(String nombre) {
	 * ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos
	 * .consultar(true, "listasgenerales", new String[] { "lst_item" },
	 * "lst_nombre=?", new String[] { nombre }, null, null, null);
	 */

}
