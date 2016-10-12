package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.ProductoAMG;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class CompBADigital extends RelativeLayout implements Subject, Observer {

	public Observer observer;

	public Spinner spnProducto;

	public TextView txtPlan, lblVelocidad, lblVelocidadValor, lblCargoBasicoContenido, lblIvaContenido,
			lblTotalContenido, txtDescuento;
	private String departamento, estrato, descuento, duracion, valorIndividual, valorIndividualIva, valorEmpaquetado,
			valorEmpaquetadoIva;
	private String estadoProducto;
	private ProductoAMG existente, producto;

	public CompBADigital(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompBADigital(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompBADigital(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.compbadigital, this, true);

		spnProducto = (Spinner) findViewById(R.id.spnProducto);

		txtPlan = (TextView) findViewById(R.id.txtPlan);
		lblVelocidad = (TextView) findViewById(R.id.lblVelocidad);
		lblVelocidadValor = (TextView) findViewById(R.id.lblVelocidadValor);

		lblCargoBasicoContenido = (TextView) findViewById(R.id.lblCargoBasicoContenido);
		lblIvaContenido = (TextView) findViewById(R.id.lblIvaContenido);
		lblTotalContenido = (TextView) findViewById(R.id.lblTotalContenido);
		txtDescuento = (TextView) findViewById(R.id.txtDescuento);

		spnProducto.setOnItemSelectedListener(mostrarVelicidad);

		estadoProducto = "N";

	}

	private void llenarPlanes(ArrayList<String> plan) {

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

		for (int i = 0; i < plan.size(); i++) {
			adaptador.add(plan.get(i));
		}

		spnProducto.setAdapter(adaptador);

	}

	public void setDeptoEstrato(String departamento, String estrato) {
		this.departamento = departamento;
		this.estrato = estrato;

		System.out.println("setDeptoEstrato BA");

		llenarPlanes("");
	}

	public void setDeptoEstratoPlan(String departamento, String estrato, ArrayList<String> plan) {
		this.departamento = departamento;
		this.estrato = estrato;

		System.out.println("plan " + plan);

		System.out.println("setDeptoEstratoPlan BA");

		llenarPlanes(plan);
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

	public String getEstadoProducto() {
		return estadoProducto;
	}

	public void setEstadoProducto(String estadoProducto) {
		this.estadoProducto = estadoProducto;
		// llenarPlanes();
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

	OnItemSelectedListener mostrarVelicidad = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			System.out.println("parent => " + parent.getSelectedItem().toString());
			if (parent.getSelectedItem().toString().contains("1M")
					|| parent.getSelectedItem().toString().contains("1@")) {
				lblVelocidad.setVisibility(View.VISIBLE);
				lblVelocidadValor.setText("1MB");
			} else if (parent.getSelectedItem().toString().contains("3M")
					|| parent.getSelectedItem().toString().contains("3@")) {
				lblVelocidad.setVisibility(View.VISIBLE);
				lblVelocidadValor.setText("3MB");
			} else if (parent.getSelectedItem().toString().contains("5M")
					|| parent.getSelectedItem().toString().contains("5@")) {
				lblVelocidad.setVisibility(View.VISIBLE);
				lblVelocidadValor.setText("5MB");
			} else if (parent.getSelectedItem().toString().contains("10M")
					|| parent.getSelectedItem().toString().contains("10@")) {
				lblVelocidad.setVisibility(View.VISIBLE);
				lblVelocidadValor.setText("10MB");
			} else if (parent.getSelectedItem().toString().contains("20M")
					|| parent.getSelectedItem().toString().contains("20@")) {
				lblVelocidad.setVisibility(View.VISIBLE);
				lblVelocidadValor.setText("20MB");
			} else if (parent.getSelectedItem().toString().contains("50M")
					|| parent.getSelectedItem().toString().contains("50@")) {
				lblVelocidad.setVisibility(View.VISIBLE);
				lblVelocidadValor.setText("50MB");
			} else if (parent.getSelectedItem().toString().contains("Existente")) {
				estadoProducto = "E";
				lblVelocidad.setVisibility(View.GONE);
				lblVelocidadValor.setText(existente.plan);
			}
			mostrarValores();
			notifyObserver();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	private void limpiar() {
		txtDescuento.setText("");
	}

	public void setPlan(String plan) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnProducto.getAdapter();
		spnProducto.setSelection(adaptador.getPosition(plan));
	}

	public String getPlan() {
		return (String) spnProducto.getSelectedItem();
	}

	private void mostrarValores() {

		if (!estadoProducto.equalsIgnoreCase("E")) {
			ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios",
					new String[] { "empaquetado", "empaquetado_iva", "individual", "individual_iva", "homoPrimeraLinea" },
					"departamento like ? and producto like ? and tipo_producto = ? and estrato like ?",
					new String[] { departamento, getPlan(), "ba", "%" + estrato + "%" }, null, null, null);

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
		} else {
			lblCargoBasicoContenido.setText(String.valueOf(existente.cargoBasico));
			lblTotalContenido.setText(String.valueOf(existente.totalProducto));
			double iva = existente.totalProducto - existente.cargoBasico;
			lblIvaContenido.setText(String.valueOf(Math.floor(iva)));
		}
	}

	public String getTotal() {
		return lblTotalContenido.getText().toString();
	}

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		observer = o;
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observer = null;
	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub
		ArrayList<Object> respuestas = new ArrayList<Object>();
		respuestas.add("oferta");
		respuestas.add("totales");
		observer.update(respuestas);
	}

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		String valor = (String) value;

		System.out.println("[Evento TV Digital] [ba] valor ba digital " + valor);

		String balin = ofertaBalin(valor);

		System.out.println("[Evento TV Digital] [ba] balin ba digital " + balin);

		if (!balin.equalsIgnoreCase("")) {
			llenarPlanesBalines(balin);
		} else {
			llenarPlanes("");
		}
		// if (value.toString().contains("Black")) {
		// llenarPlanes("Black");
		// } else if (value.toString().contains("Gold")) {
		// llenarPlanes("Gold");
		// } else if (value.toString().contains("Silver")) {
		// llenarPlanes("Silver");
		// } else if (value.toString().contains("Bronze")) {
		// llenarPlanes("Bronze");
		// } else {
		// llenarPlanes("");
		// }
	}

	private void llenarPlanes(String oferta) {

		String planBa = Utilidades.planNumerico(estadoProducto);

		System.out.println("oferta plan top " + oferta);

		System.out.println("llenarPlanes() departamento " + departamento + " estadoProducto " + estadoProducto
				+ " planBa " + planBa);
		if (planBa.equals("0")) {
			if(producto != null){
				if(producto.plan != null){
					if(producto.plan.toUpperCase().contains("NETFLIX")){
						planBa = "3";
					}
				}
			}
		}

		ArrayList<ArrayList<String>> respuesta = null;

		if (!oferta.equalsIgnoreCase("")) {
			respuesta = MainActivity.basedatos.consultar(false, "Productos",
					new String[] { "Producto", "ProductoHomologado, oferta" },
					"Departamento=? and Tipo_Producto=? and (Producto like ? or Producto like ?) and Producto like ? and Nuevo IN(?,?) and Estrato like ? ",
					new String[] { departamento, Utilidades.tipo_producto_ba, "%Ultra%", "%Existente%",
							"%" + oferta + "%", planBa, "2", "%" + estrato + "%" },
					null, null, null);
		} else {
			respuesta = MainActivity.basedatos.consultar(false, "Productos",
					new String[] { "Producto", "ProductoHomologado, oferta" },
					"Departamento=? and Tipo_Producto=? and (Producto like ?) and Producto like ? and Nuevo IN(?,?) and Estrato like ? ",
					new String[] { departamento, Utilidades.tipo_producto_ba, "%Existente%", "IS NULL", planBa, "2",
							"%" + estrato + "%" },
					null, null, null);
		}

		System.out.println("respuesta ba digital " + respuesta);
		System.out.println("respuesta planBa " + planBa);
		System.out.println("respuesta departamento " + departamento);
		System.out.println("respuesta estrato " + estrato);

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				// adaptador.add(arrayList.get(0));

				adaptador.add(arrayList.get(0));

			}
		}else{
			adaptador.add(Utilidades.inicial_guion);
		}

		spnProducto.setAdapter(adaptador);

	}

	private void llenarPlanesBalines(String oferta) {

		String planBa = Utilidades.planNumerico(estadoProducto);

		System.out.println("oferta plan top " + oferta);

		System.out.println("llenarPlanes() departamento " + departamento + " estadoProducto " + estadoProducto
				+ " planBa " + planBa);
		if (planBa.equals("0")) {
			if(producto != null){
				if(producto.plan != null){
					if(producto.plan.toUpperCase().contains("NETFLIX")){
						planBa = "3";
					}
				}
			}
		}

		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Productos",
				new String[] { "Producto", "ProductoHomologado" },
				"Departamento=? and Tipo_Producto=? and (Producto like ? or Producto like ?) and Oferta=? and Nuevo IN(?,?) and Estrato like ? ",
				new String[] { departamento, Utilidades.tipo_producto_ba, "%Ultra%", "%Existente%", oferta, planBa, "2",
						"%" + estrato + "%" },
				null, null, null);

		System.out.println("respuesta ba digital " + respuesta);
		System.out.println("respuesta planTo " + planBa);
		System.out.println("respuesta departamento " + departamento);
		System.out.println("respuesta estrato " + estrato);

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				// adaptador.add(arrayList.get(0));
				if(producto != null){
					if(producto.plan != null){
						if(producto.plan.toUpperCase().contains("NETFLIX")){
							if(arrayList.get(0).toUpperCase().contains("NETFLIX")){
								adaptador.add(arrayList.get(0));
							}
						}else{
							adaptador.add(arrayList.get(0));	
						}
					}else{
						adaptador.add(arrayList.get(0));
					}
				}else{
					adaptador.add(arrayList.get(0));
				}
				
				

			}
		}

		spnProducto.setAdapter(adaptador);
	}

	public String ofertaBalin(String planTV) {
		String oferta = "";

		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Productos",
				new String[] { "Oferta" }, "Producto=?", new String[] { planTV }, null, null, null);

		System.out.println("[Evento TV Digital] Respuesta planTv " + respuesta);

		if (respuesta != null) {
			oferta = respuesta.get(0).get(0);
		}

		return oferta;
	}
}
