package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ListaAdicionales;
import co.com.une.appmovilesune.adapters.ListaAdicionalesAdapter;
import co.com.une.appmovilesune.change.ControlSimulador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.ProductoAMG;
import co.com.une.appmovilesune.model.Tarificador;

public class CompADDigital extends RelativeLayout implements Observer, Subject {

	public static final String OFERTA = "oferta";
	public static final String CONFIGURABLE = "configurable";
	public static final String BALIN = "balin";

	private Observer observer;

	public Spinner spnProducto;
	public ListView lstAdicionales;
	public TextView txtPlanTO, lblVelocidadValor, lblCargoBasicoContenido, lblIvaContenido, lblTotalContenido,
			txtTotalAdicionalesContenido;
	private String departamento, estrato, tipo;
	private String totalDescuento = "0";
	private Cliente cliente;
	private String planTV;
	private boolean isHFC = true;

	private ArrayList<ListaAdicionales> adicionales = new ArrayList<ListaAdicionales>();

	public CompADDigital(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompADDigital(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompADDigital(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.compaddigital, this, true);

		spnProducto = (Spinner) findViewById(R.id.spnProducto);
		lstAdicionales = (ListView) findViewById(R.id.lstAdicionales);

		txtPlanTO = (TextView) findViewById(R.id.txtPlanTO);
		lblVelocidadValor = (TextView) findViewById(R.id.lblVelocidadValor);

		lblCargoBasicoContenido = (TextView) findViewById(R.id.lblCargoBasicoContenido);
		lblIvaContenido = (TextView) findViewById(R.id.lblIvaContenido);
		lblTotalContenido = (TextView) findViewById(R.id.lblTotalContenido);
		txtTotalAdicionalesContenido = (TextView) findViewById(R.id.txtTotalAdicionalesContenido);

		spnProducto.setOnItemSelectedListener(mostrarVelicidad);

		// llenarAdicionales("");

	}

	private void llenarAdicionales(String oferta) {

		System.out.println("oferta llenarAdicionales" + oferta);

		if (tipo.equals(BALIN)) {
			ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Adicionales",
					new String[] { "adicional", "tarifa", "tarifaIva" },
					"departamento like ? and producto like ? and tipoProducto = ? and estrato like ? and adicional not like ?",
					new String[] { "%" + departamento + "%", "%HFC%", "tv", "%" + estrato + "%", "Decodificador%" },
					null, "producto,adicional ASC", null);

			ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(),
					android.R.layout.simple_spinner_item);
			adaptador.add("-- Seleccione Adicional --");

			if (respuesta != null) {
				for (ArrayList<String> arrayList : respuesta) {
					// adaptador.add(arrayList.get(0));
					System.out.println("adicional name " + arrayList.get(0));
					adaptador.add(arrayList.get(0));

				}
			}

			spnProducto.setAdapter(adaptador);

		} else {
			ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
					new String[] { "lst_valor" }, "lst_nombre=? and lst_clave=?",
					new String[] { "adicionalesTVDigital", oferta }, null, null, null);

			ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(),
					android.R.layout.simple_spinner_item);
			adaptador.add("-- Seleccione Adicional --");
			if (respuesta != null) {
				for (ArrayList<String> arrayList : respuesta) {
					// adaptador.add(arrayList.get(0));
					System.out.println("adicional name " + arrayList.get(0));
					adaptador.add(arrayList.get(0));

				}
			}

			ArrayList<ArrayList<String>> decos = null;

			/*
			 * if(isHFC){ decos = UtilidadesTarificador .llenarAdicionales(
			 * "TV DIGITAL", departamento, estrato); }else{ decos =
			 * UtilidadesTarificador .llenarAdicionales("TV DIGITAL 1 IPTV",
			 * departamento, estrato); }
			 * 
			 * System.out.println("decos -> " + decos);
			 * 
			 * if (decos != null) { for (int i = 0; i < decos.size(); i++) {
			 * System.out.println("decos name -> " + decos.get(i).get(0));
			 * adaptador.add(decos.get(i).get(0)); } }
			 */

			spnProducto.setAdapter(adaptador);
		}

	}

	public void setDeptoEstrato(String departamento, String estrato, Cliente cliente) {
		this.departamento = departamento;
		this.estrato = estrato;
		this.cliente = cliente;
		llenarAdicionales("bd");
	}

	public void setDeptoEstratoPlan(String departamento, String estrato, ArrayList<String> plan, String planTV,
			Cliente cliente) {
		this.departamento = departamento;
		this.estrato = estrato;
		this.cliente = cliente;
		this.planTV = planTV;

		System.out.println("plan " + plan);
		System.out.println("planTV  " + planTV);

		llenarPlanes(plan);
	}

	private void llenarPlanes(ArrayList<String> plan) {

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

		adaptador.add(Utilidades.inicial_opcion);
		System.out.println("adicionales size -> " + plan.size());
		for (int i = 0; i < plan.size(); i++) {
			System.out.println("nombre adicional -> " + plan.get(i));
			adaptador.add(plan.get(i));
		}

		// ArrayList<ArrayList<String>> decos =
		// UtilidadesTarificador.llenarAdicionales(Utilidades.obtenerPlanTVDigital(oferta),
		// departamento, estrato);
		/*
		 * ArrayList<ArrayList<String>> decos = UtilidadesTarificador
		 * .llenarAdicionales(planTV, departamento, estrato);
		 * 
		 * System.out.println("decos -> " + decos);
		 * 
		 * if (decos != null) { for (int i = 0; i < decos.size(); i++) {
		 * System.out.println("decos name -> " + decos.get(i).get(0));
		 * adaptador.add(decos.get(i).get(0)); } }
		 */

		for (int i = 0; i < adaptador.getCount(); i++) {
			System.out.println("Adaptador item -> " + adaptador.getItem(i));
		}

		spnProducto.setAdapter(adaptador);
		spnProducto.invalidate();

	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	private void consultarAdicional(String adicional) {
		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Adicionales",
				new String[] { "adicional", "tarifa", "tarifaIva" },
				"departamento like ? and (producto like ? or producto like ?) and tipoProducto = ? and estrato like ? and adicional = ?",
				new String[] { "%" + departamento + "%", "%HFC%", "%IPTV%", "tv", "%" + estrato + "%", adicional },
				null, "producto,adicional ASC", null);

		if (respuesta != null) {
			lblCargoBasicoContenido.setText(respuesta.get(0).get(1));
			lblTotalContenido.setText(respuesta.get(0).get(2));
			int iva = Integer.parseInt(respuesta.get(0).get(2)) - Integer.parseInt(respuesta.get(0).get(1));
			lblIvaContenido.setText(String.valueOf(iva));

			String[][] data = new String[1][2];

			data[0][0] = respuesta.get(0).get(0);
			data[0][1] = respuesta.get(0).get(2);

			/*
			 * ArrayList<ArrayList<String>> result = Tarificador
			 * .consultarDescuentos(data, departamento, true, 1);
			 */

			ArrayList<ArrayList<String>> result = Tarificador.consultarDescuentos2(data, departamento, true, 1,
					UtilidadesTarificador.jsonDatos(cliente, "1", "N/A", "ingresar 0 o 1"));

			ListaAdicionales adicionalItem = null;
			if (result != null) {
				adicionalItem = new ListaAdicionales(adicional, respuesta.get(0).get(2), result.get(0).get(1),
						result.get(0).get(2));
				if (tipo.equals(BALIN)) {
					adicionalItem.setBalin(true);
				}
			} else {
				adicionalItem = new ListaAdicionales(adicional, respuesta.get(0).get(2), "", "");
			}

			ArrayList<Boolean> agregar = new ArrayList<Boolean>();
			for (int i = 0; i < adicionales.size(); i++) {
				if (!adicionales.get(i).getAdicional().equals(adicional)) {
					agregar.add(true);
				} else {
					agregar.add(false);
				}
			}

			if (!agregar.contains(false)) {
				adicionales.add(adicionalItem);
			}

			actualizarLista();
		}
	}

	private void actualizarLista() {

		ListaAdicionalesAdapter adapter = new ListaAdicionalesAdapter((Activity) getContext(), adicionales,
				getContext(), "");
		adapter.addObserver(this);
		lstAdicionales.setAdapter(adapter);
		ControlSimulador.setListViewHeightBasedOnChildren(lstAdicionales);

		actualizarTotal();

	}

	private void actualizarTotal() {
		int total = 0;
		double totalDescuento = 0;
		for (int i = 0; i < adicionales.size(); i++) {
			total = total + Integer.parseInt(adicionales.get(i).getPrecio());
			if (tipo.equals(BALIN)) {
				totalDescuento = totalDescuento + Double.parseDouble(adicionales.get(i).getValorDescuento());
			}
		}

		setTotalDescuento(String.valueOf(totalDescuento));

		if (tipo.equals(BALIN)) {
			if (totalDescuento > 0) {
				txtTotalAdicionalesContenido
						.setText(String.valueOf(total) + " (" + String.valueOf(totalDescuento) + ")");
			}
		} else {
			txtTotalAdicionalesContenido.setText(String.valueOf(total));
		}

	}

	public String[][] getAdicionales() {
		String[][] adicioanles = new String[this.adicionales.size()][2];
		for (int i = 0; i < this.adicionales.size(); i++) {
			adicioanles[i][0] = this.adicionales.get(i).getAdicional();
			adicioanles[i][1] = this.adicionales.get(i).getPrecio();
		}

		return adicioanles;
	}

	public double getTotal() {
		double total = 0;
		for (int i = 0; i < adicionales.size(); i++) {
			total = total + Double.parseDouble(adicionales.get(i).getPrecio());
		}
		return total;
	}

	public void setIPTV() {
		isHFC = false;
	}

	public ArrayList<ItemPromocionesAdicionales> getPromociones() {
		ArrayList<ItemPromocionesAdicionales> promociones = new ArrayList<ItemPromocionesAdicionales>();

		for (int i = 0; i < adicionales.size(); i++) {
			promociones.add(new ItemPromocionesAdicionales(adicionales.get(i).getAdicional(),
					adicionales.get(i).getDescuento(), "TV", adicionales.get(i).getDuracion()));

		}

		return promociones;
	}

	OnItemSelectedListener mostrarVelicidad = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			System.out.println("parent => " + parent.getSelectedItem().toString());
			if (!parent.getSelectedItem().toString().equalsIgnoreCase("-- Seleccione Adicional --")) {
				consultarAdicional(parent.getSelectedItem().toString());
				notifyObserver();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		System.out.println("value update adicionales -> " + value);

		ArrayList<Object> data = (ArrayList<Object>) value;
		String lugar = data.get(0).toString();
		if (lugar.equalsIgnoreCase("plan")) {
			if (planTV != null && !planTV.equalsIgnoreCase("")) {
				;
			} else {
				String plan = (String) data.get(1);
				llenarAdicionales(Utilidades.traducirPlanOfertaDigital((String) data.get(1)));

				if (tipo.equals(BALIN)) {
					String[][] adicionalDependiente = UtilidadesTarificador.adicionalDependiente(plan);
					if (adicionalDependiente != null) {
						for (int i = 0; i < adicionalDependiente.length; i++) {
							consultarAdicional(adicionalDependiente[i][0]);
						}

					}
				}
			}
		} else if (lugar.equalsIgnoreCase("eliminar")) {
			int position = Integer.parseInt(data.get(1).toString());
			adicionales.remove(position);
			actualizarLista();
			notifyObserver();
		}

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
		respuestas.add(tipo);
		respuestas.add("totales");
		observer.update(respuestas);
	}

	public String getTotalDescuento() {
		return totalDescuento;
	}

	public void setTotalDescuento(String totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
}
