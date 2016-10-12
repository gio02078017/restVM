package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.ProductoAMG;

public class CompTODigital extends RelativeLayout implements Subject, Observer {

	private Observer observer;

	private CheckBox chkHabilitarTOdigital;
	private TextView  txtPlanTO, txtPlan, lblCargoBasicoContenido, lblIvaContenido, lblTotalContenido, txtDescuento;
	private LinearLayout llyContent;
	private String departamento, estrato, plan, descuento, duracion, valorIndividual, valorIndividualIva,
			valorEmpaquetado, valorEmpaquetadoIva;
	private String estadoProducto;
	private ProductoAMG existente, producto;

	public CompTODigital(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompTODigital(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompTODigital(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.comptodigital, this, true);

		llyContent = (LinearLayout) findViewById(R.id.llyContent);

		chkHabilitarTOdigital = (CheckBox) findViewById(R.id.chkHabilitarTOdigital);

		txtPlanTO = (TextView) findViewById(R.id.txtPlanTO);

		txtPlan = (TextView) findViewById(R.id.txtPlan);
		lblCargoBasicoContenido = (TextView) findViewById(R.id.lblCargoBasicoContenido);
		lblIvaContenido = (TextView) findViewById(R.id.lblIvaContenido);
		lblTotalContenido = (TextView) findViewById(R.id.lblTotalContenido);
		txtDescuento = (TextView) findViewById(R.id.txtDescuento);

		chkHabilitarTOdigital.setOnClickListener(alternarContenido);
		estadoProducto = "N";

		obtenerPlan();
		// mostrarValores();

	}

	public void setDeptoEstrato(String departamento, String estrato) {
		this.departamento = departamento;
		this.estrato = estrato;
	}

	private void obtenerPlan() {
		plan = Utilidades.obtenerPlanTODigital();
		txtPlanTO.setText(plan);
	}

	public String getPlan() {
		return txtPlanTO.getText().toString();
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

	private void limpiar() {
		txtDescuento.setText("");
	}

	
	public void mostrarValores(){	
		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos
				.consultar(true, "Precios", new String[] { "empaquetado", "empaquetado_iva", "individual", "individual_iva", "homoPrimeraLinea" },
						"departamento like ? and producto like ? and tipo_producto = ? and estrato like ?", new String[] { departamento, plan, "to", "%"+estrato+"%" }, null, null, null);	
		
		if(respuesta != null){
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

	public boolean isActive() {
		return chkHabilitarTOdigital.isChecked();
	}

	public String getTotal() {
		return lblTotalContenido.getText().toString();
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

		if (!this.estadoProducto.equalsIgnoreCase("N")) {
			deshabilitarCheck();
		}
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

	private void deshabilitarCheck() {
		chkHabilitarTOdigital.setChecked(true);
		chkHabilitarTOdigital.setEnabled(false);
		llyContent.setVisibility(View.VISIBLE);
		slide_down(getContext(), llyContent);
	}

	public void slide_down(Context ctx, View v) {

		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}

	public void slide_up(Context ctx, View v) {

		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}

	OnClickListener alternarContenido = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (llyContent.isShown()) {
				slide_up(getContext(), llyContent);
				llyContent.setVisibility(View.GONE);
				notifyObserver();
				limpiar();
			} else {
				llyContent.setVisibility(View.VISIBLE);
				slide_down(getContext(), llyContent);
				notifyObserver();
			}
		}
	};

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
		respuestas.add("");
		observer.update(respuestas);
	}

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		String valor = (String) value;
		System.out.println("valor to digital " + valor);

		String balin = ofertaBalin(valor);

		System.out.println("balin to digital " + balin);

		if (!balin.equalsIgnoreCase("")) {
			llenarPlanesBalines(balin);
		} else {
			llenarPlanes("");
		}

		// if(value.toString().contains("Black")){
		// llenarPlanes("Black");
		// }else if(value.toString().contains("Gold")){
		// llenarPlanes("Gold");
		// }else if(value.toString().contains("Silver")){
		// llenarPlanes("Silver");
		// }else if(value.toString().contains("Bronze")){
		// llenarPlanes("Bronze");
		// }else {
		// llenarPlanes("");
		// }
	}

	private void llenarPlanes(String oferta) {

		System.out.println("Oferta TO " + oferta);
		String planTo = Utilidades.planNumerico(estadoProducto);

		System.out.println("llenarPlanes() departamento " + departamento + " estadoProducto " + estadoProducto
				+ " planBa " + planTo);
		if (planTo.equals("0")) {
			planTo = "3";
		}
		
		ArrayList<ArrayList<String>> respuesta;
		
		if(!oferta.equalsIgnoreCase("")){
		respuesta = MainActivity.basedatos
				.consultar(
						false,
						"Productos",
						new String[] { "Producto", "ProductoHomologado" },
						"Departamento=? and Tipo_Producto=? and (Producto like ? or Producto like ?) and Producto like ? and Nuevo IN(?,?) and Estrato like ? ",
						new String[] { departamento,
								Utilidades.tipo_producto_to, "%Ultra%", "%Existente%", "%" + oferta + "%", planTo, "2",
								"%" + estrato + "%" }, null, null, null);
		}else{
			respuesta = MainActivity.basedatos
					.consultar(
							false,
							"Productos",
							new String[] { "Producto", "ProductoHomologado" },
							"Departamento=? and Tipo_Producto=? and (Producto like ?) and Producto like ? and Nuevo IN(?,?) and Estrato like ? ",
							new String[] { departamento,
									Utilidades.tipo_producto_to, "%Existente%", "IS NULL", planTo, "2",
									"%" + estrato + "%" }, null, null, null);	
		}
		
		System.out.println("respuesta to digital "+respuesta);
		System.out.println("respuesta planTo "+planTo);
		System.out.println("respuesta departamento "+departamento);
		System.out.println("respuesta estrato "+estrato);
		
		if (respuesta != null) {
			plan = respuesta.get(0).get(0);
			txtPlanTO.setText(respuesta.get(0).get(0));
			mostrarValores();

		}else{
			txtPlanTO.setText("-");
			mostrarValores();
		}
	}

	private void llenarPlanesBalines(String oferta) {

		System.out.println("Oferta TO " + oferta);
		String planTo = Utilidades.planNumerico(estadoProducto);

		System.out.println("llenarPlanes() departamento " + departamento + " estadoProducto " + estadoProducto
				+ " planBa " + planTo);
		if (planTo.equals("0")) {
			planTo = "3";
		}

		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Productos",
				new String[] { "Producto", "ProductoHomologado" },
				"Departamento=? and Tipo_Producto=? and (Producto like ? or Producto like ?) and Oferta=? and Nuevo IN(?,?) and Estrato like ? ",
				new String[] { departamento, Utilidades.tipo_producto_to, "%Ultra%", "%Existente%", oferta, planTo, "2",
						"%" + estrato + "%" },
				null, null, null);

		System.out.println("respuesta to digital " + respuesta);
		System.out.println("respuesta planTo " + planTo);
		System.out.println("respuesta departamento " + departamento);
		System.out.println("respuesta estrato " + estrato);

		if (respuesta != null) {
			plan = respuesta.get(0).get(0);
			txtPlanTO.setText(respuesta.get(0).get(0));
			mostrarValores();
		}
	}

	public String ofertaBalin(String planTV) {
		String oferta = "";

		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Productos",
				new String[] { "Oferta" }, "Producto=?", new String[] { planTV }, null, null, null);

		System.out.println("Respuesta planTv " + respuesta);

		if (respuesta != null) {
			oferta = respuesta.get(0).get(0);
		}

		return oferta;
	}

}
