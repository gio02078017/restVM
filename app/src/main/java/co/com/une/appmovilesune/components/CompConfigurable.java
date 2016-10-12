package co.com.une.appmovilesune.components;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.R.array;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ListaAdicionales;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.ProductoAMG;
import co.com.une.appmovilesune.model.Tarificador;

public class CompConfigurable extends RelativeLayout implements Subject {

	public static final int TELEFONIA = 0;
	public static final int TELEVISION = 1;
	public static final int INTERNET = 2;
	public static final int ADICHD = 3;

	private ArrayList<Observer> observers = new ArrayList<Observer>();

	private Spinner spnProducto;
	private CheckBox chkHabilitar;
	private LinearLayout llyContent;
	private RelativeLayout rlyGotica;
	private ImageView imgProducto;
	private TextView lblTipoProducto, lblPlan, lblCargoBasicoContenido, lblIvaContenido, lblTotalContenido,
			txtDescuento, txtGotica, txtGoticaValor, lblTotalContenidoDescuento;

	private ArrayList<ArrayList<ProductoAMG>> planes;

	private int tipo;
	private String departamento, estrato, tipoProducto, descuento, duracion, valorIndividual, valorIndividualIva,
			valorEmpaquetado, valorEmpaquetadoIva, valorGota = "0";
	private String totalDescuento = "0";
	private String estadoProducto;
	private ProductoAMG existente, producto;
	private boolean isConfigurable = true;
	private boolean isHFC = true;
	private String ciudad;
	private boolean isBalin = false;
	private boolean isIndividual = false;

	public CompConfigurable(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompConfigurable(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compconfigurable);

		tipo = Integer.parseInt(a.getString(0));

	}

	public CompConfigurable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compconfigurable);

		tipo = Integer.parseInt(a.getString(0));

	}

	private void init() {

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.compconfigurable, this, true);

		spnProducto = (Spinner) findViewById(R.id.spnProducto);
		chkHabilitar = (CheckBox) findViewById(R.id.chkHabilitar);
		imgProducto = (ImageView) findViewById(R.id.imgProducto);
		lblTipoProducto = (TextView) findViewById(R.id.lblTipoProducto);
		llyContent = (LinearLayout) findViewById(R.id.llyContent);
		lblPlan = (TextView) findViewById(R.id.lblPlan);
		lblCargoBasicoContenido = (TextView) findViewById(R.id.lblCargoBasicoContenido);
		lblIvaContenido = (TextView) findViewById(R.id.lblIvaContenido);
		lblTotalContenido = (TextView) findViewById(R.id.lblTotalContenido);
		lblTotalContenidoDescuento = (TextView) findViewById(R.id.lblTotalContenidoDescuento);
		txtDescuento = (TextView) findViewById(R.id.txtDescuento);

		rlyGotica = (RelativeLayout) findViewById(R.id.rlyGotica);
		txtGotica = (TextView) findViewById(R.id.txtGotica);
		txtGoticaValor = (TextView) findViewById(R.id.txtGoticaValor);

		chkHabilitar.setOnClickListener(alternarContenido);
		spnProducto.setOnItemSelectedListener(mostrarVelicidad);

		estadoProducto = "N";

	}

	public void setDeptoEstrato(String departamento, String estrato) {

		System.out.println("setDeptoEstrato");

		this.departamento = departamento;

		this.estrato = estrato;

		switch (tipo) {
		case TELEFONIA:
			setTelefonia();
			break;
		case TELEVISION:
			setTelevision();
			break;
		case INTERNET:
			setInternet();
			break;
		case ADICHD:
			chkHabilitar.setChecked(false);
			llyContent.setVisibility(GONE);
			txtDescuento.setVisibility(GONE);
			setAdicHD();
			break;
		default:
			break;
		}
	}

	public void setDeptoEstratoPlan(String departamento, String estrato, ArrayList<String> plan) {

		System.out.println("setDeptoEstratoPlan");

		this.departamento = departamento;

		this.estrato = estrato;

		switch (tipo) {
		case TELEFONIA:
			setTelefonia(plan);
			break;
		case TELEVISION:
			setTelevision(plan);
			break;
		case INTERNET:
			setInternet(plan);
			break;
		case ADICHD:
			chkHabilitar.setChecked(false);
			llyContent.setVisibility(GONE);
			txtDescuento.setVisibility(GONE);
			setAdicHD(plan);
		default:
			break;
		}
	}

	public void setPlanes(ArrayList<ArrayList<ProductoAMG>> planes) {
		this.planes = planes;
	}

	private String traducirTipoProducto() {
		switch (tipo) {
		case TELEFONIA:
			return "TO";
		case TELEVISION:
			return "TV";
		case INTERNET:
			return "BA";
		case ADICHD:
			chkHabilitar.setChecked(false);
			llyContent.setVisibility(GONE);
			txtDescuento.setVisibility(GONE);
		default:
			return "";
		}
	}

	private void llenarPlanes(String[] campos, String clausulas, String[] valores) {

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

		System.out.println("planes " + planes);

		if (planes == null) {
			ArrayList<ArrayList<String>> respuesta = null;

			if (tipo == ADICHD) {
				respuesta = MainActivity.basedatos.consultar(false, "Adicionales", campos, clausulas, valores, null,
						null, null);
			} else {
				respuesta = MainActivity.basedatos.consultar(false, "Productos", campos, clausulas, valores, null, null,
						null);
			}

			System.out.println("respuesta planes " + respuesta);

			if (respuesta != null) {
				for (ArrayList<String> arrayList : respuesta) {
					// adaptador.add(arrayList.get(0));
					adaptador.add(arrayList.get(0));
				}
			}
		} else {
			for (int i = 0; i < planes.size(); i++) {
				for (int j = 0; j < planes.get(i).size(); j++) {
					System.out.println("TTP " + traducirTipoProducto());
					if (traducirTipoProducto() != null) {
						if (traducirTipoProducto().equalsIgnoreCase(planes.get(i).get(j).producto)) {
							adaptador.add(planes.get(i).get(j).plan);
						}
					}

				}
			}
		}

		spnProducto.setAdapter(adaptador);

	}

	private void llenarPlanes(ArrayList<String> plan) {

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

		ArrayList<String> planesBronze = Utilidades.obtenerPlanTVDigitalBronze();

		for (int i = 0; i < plan.size(); i++) {
			if (planesBronze != null) {
				for (int j = 0; j < planesBronze.size(); j++) {
					System.out.println("planes Bronze" + planesBronze.get(j));
					if (plan.get(i).equalsIgnoreCase(planesBronze.get(j))) {
						System.out.println(planesBronze.get(j));
						if (Utilidades.validarEstratoBronze(estrato, ciudad)) {
							adaptador.add(plan.get(i));
						}
					} else {
						adaptador.add(plan.get(i));
					}
				}
			} else {
				System.out.println("Sin planes Bronze");
				adaptador.add(plan.get(i));
			}

			System.out.println("plan.get(i) bloqueoBronze " + plan.get(i));
		}

		String existente = "";

		if (!isBalin) {
			existente = Existente(plan);
		}

		// System.out.println("existente "+existente);

		if (!existente.equalsIgnoreCase("")) {
			adaptador.add(existente);
		}

		// System.out.println("adaptador "+adaptador);

		spnProducto.setAdapter(adaptador);

	}

	private void mostrarValores() {
		System.out.println("getPlan " + getPlan());
		ArrayList<ArrayList<String>> respuesta = null;
		if (getPlan() != null) {
			if (getPlan().contains("Existente")) {
				estadoProducto = "E";
			}

			
			if(this.tipo != ADICHD){
				respuesta = MainActivity.basedatos
						.consultar(
								true,
								"Precios",
								new String[] { "empaquetado", "empaquetado_iva",
										"individual", "individual_iva", "ProductoHomologado", "velocidadGota", "valorGotaIva", "homoPrimeraLinea" },
								"departamento like ? and producto like ? and tipo_producto = ? and estrato like ?",
								new String[] { departamento, getPlan(),
										tipoProducto, "%" + estrato + "%" }, null,
								null, null);
			}else{
				respuesta = MainActivity.basedatos
						.consultar(
								false,
								"Adicionales",
								new String[] { "tarifa", "tarifaIva", "tarifa", "tarifaIva", "adicional" },
								"departamento like ? and (producto like ? or producto like ?) and tipoProducto = ? and estrato like ? and adicional = ?",
								new String[] { "%" + departamento + "%",
										"%HFC%", "%IPTV%", "tv",
										"%" + estrato + "%", getPlan() }, null,
								"producto,adicional ASC", null);
				
				
				
				if(respuesta != null){
					String[][] data = new String[1][2];

					data[0][0] = respuesta.get(0).get(4);
					data[0][1] = respuesta.get(0).get(2);

					System.out.println("data -> [" + data[0][0] + "][" + data[0][1] + "]");

					ArrayList<ArrayList<String>> result = Tarificador.consultarDescuentos(data, departamento, true, 1);

					System.out.println("result -> " + result);

					if (result != null) {
						setDescuento(result.get(0).get(1));
						setDuracion(result.get(0).get(2));
						mostrarDescuento();
					}
				}

			}

		}

		if (respuesta != null) {
			valorEmpaquetado = respuesta.get(0).get(0);
			valorEmpaquetadoIva = respuesta.get(0).get(1);
			valorIndividual = respuesta.get(0).get(2);
			valorIndividualIva = respuesta.get(0).get(3);
			if(this.tipo != ADICHD){
				lblPlan.setText(respuesta.get(0).get(7));
				if(producto != null){
					producto.planFacturacion = respuesta.get(0).get(4);	
				}
			}
			
			if(this.tipo == INTERNET){
				System.out.println("INTERNET");
				mostrarGota(respuesta.get(0).get(4), respuesta.get(0).get(5), respuesta.get(0).get(6));
			}

			if (isIndividual) {
				lblCargoBasicoContenido.setText(valorIndividual);
				lblTotalContenido.setText(valorIndividualIva);
				int iva = Integer.parseInt(valorIndividualIva) - Integer.parseInt(valorIndividual);
				lblIvaContenido.setText(String.valueOf(iva));
			} else {
				lblCargoBasicoContenido.setText(valorEmpaquetado);
				lblTotalContenido.setText(valorEmpaquetadoIva);
				int iva = Integer.parseInt(valorEmpaquetadoIva) - Integer.parseInt(valorEmpaquetado);
				lblIvaContenido.setText(String.valueOf(iva));
			}

		}
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

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public boolean isBalin() {
		return isBalin;
	}

	public void setBalin(boolean isBalin) {
		this.isBalin = isBalin;
	}

	public String getTotalDescuento() {
		return totalDescuento;
	}

	public void setTotalDescuento(String totalDescuento) {
		this.totalDescuento = totalDescuento;
	}

	public String getValorGota() {
		return valorGota;
	}

	public void setValorGota(String valorGota) {
		this.valorGota = valorGota;
	}

	public boolean isIndividual() {
		return isIndividual;
	}

	public void setIndividual(boolean isIndividual) {
		this.isIndividual = isIndividual;
		mostrarValores();
	}

	public void setEstadoProducto(String estadoProducto) {
		this.estadoProducto = estadoProducto;
		if (!this.estadoProducto.equalsIgnoreCase("N")) {
			deshabilitarCheck();
		}
		// switch (tipo) {
		// case TELEFONIA:
		// setTelefonia();
		// break;
		// case TELEVISION:
		// setTelevision();
		// break;
		// case INTERNET:
		// setInternet();
		// break;
		// default:
		// break;
		// }
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
		chkHabilitar.setChecked(true);
		chkHabilitar.setEnabled(false);
	}

	public void deshabilitarComponente() {
		chkHabilitar.setChecked(false);
		chkHabilitar.setEnabled(false);
		llyContent.setVisibility(GONE);
		txtDescuento.setVisibility(GONE);
		llyContent.setVisibility(GONE);
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
			totalDescuento = "0";
			lblTotalContenidoDescuento.setText("0");
			txtDescuento.setText("");
		}

		calcularTotalDescuento();
	}

	public void mostrarGota(String velIni, String velFin, String valor) {
		System.out.println("Mostrar Gotica " + velIni + " a " + velFin + " por " + valor);
		if ((!velFin.equalsIgnoreCase("N/A") && !velFin.equalsIgnoreCase(""))
				&& (!valor.equalsIgnoreCase("N/A") && !valor.equalsIgnoreCase(""))) {
			rlyGotica.setVisibility(VISIBLE);
			txtGotica.setText("Gotica de " + velIni + " a " + velFin);
			txtGoticaValor.setText(valor);
			valorGota = valor;
		} else {
			rlyGotica.setVisibility(GONE);
			txtGotica.setText("");
			txtGoticaValor.setText("");
		}
	}

	private void setTelefonia() {
		lblTipoProducto.setText(getResources().getText(R.string.telefonia));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tel));
		tipoProducto = Utilidades.tipo_producto_to;
		String planNumerico = Utilidades.planNumerico(estadoProducto);
		if (planNumerico.equals("0")) {
			planNumerico = "3";
		}

		String[] campos = new String[] { "Producto", "ProductoHomologado" };
		String clausulas = "";
		String[] valores = null;
		if (isBalin) {
			clausulas = "Departamento=? and Tipo_Producto=?  and Nuevo IN(?,?) and Estrato like ? and Oferta like ?  and Producto not like ?";
			valores = new String[] { departamento, tipoProducto, planNumerico, "2", "%" + estrato + "%", "PROMO12%",
					Utilidades.obtenerPlanTODigital() };
		} else {
			clausulas = "Departamento=? and Tipo_Producto=?  and Nuevo IN(?,?) and Estrato like ? and (Oferta = ? or Oferta = ?) and Producto not like ?";
			valores = new String[] { departamento, tipoProducto, planNumerico, "2", "%" + estrato + "%", "", "null",
					Utilidades.obtenerPlanTODigital() };
		}

		llenarPlanes(campos, clausulas, valores);
		mostrarValores();
	}

	private void setTelefonia(ArrayList<String> plan) {
		lblTipoProducto.setText(getResources().getText(R.string.telefonia));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tel));
		tipoProducto = Utilidades.tipo_producto_to;
		// String planNumerico = Utilidades.planNumerico(estadoProducto);
		// if (planNumerico.equals("0")) {
		// planNumerico = "3";
		// }
		//
		// String[] campos = new String[] { "Producto", "ProductoHomologado" };
		// String clausulas = "Departamento=? and Tipo_Producto=? and Nuevo
		// IN(?,?) and Estrato like ? and (Oferta = ? or Oferta = ?) and
		// Producto not like ?";
		// String[] valores = new String[] { departamento, tipoProducto,
		// planNumerico, "2", "%" + estrato + "%", "", "null",
		// Utilidades.obtenerPlanTODigital() };

		llenarPlanes(plan);
		mostrarValores();
	}

	private void setTelevision() {
		System.out.println("resources tv " + getResources().getText(R.string.television));
		lblTipoProducto.setText(getResources().getText(R.string.television));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tv));
		tipoProducto = Utilidades.tipo_producto_tv;

		String planNumerico = Utilidades.planNumerico(estadoProducto);
		if (planNumerico.equals("0")) {
			planNumerico = "3";
		}

		if (isBalin) {
			String[] campos = new String[] { "Producto", "ProductoHomologado" };
			String clausulas = "Departamento=? and Tipo_Producto=?  and Nuevo IN(?,?) and Estrato like ? and Oferta like ?";
			String[] valores = new String[] { departamento, tipoProducto, planNumerico, "2", "%" + estrato + "%",
					"PROMO12%" };

			llenarPlanes(campos, clausulas, valores);
		} else {
			if (isHFC) {
				String[] campos = new String[] { "Producto", "ProductoHomologado" };
				String clausulas = "Departamento=? and Tipo_Producto=?  and Nuevo IN(?,?) and Estrato like ? and Oferta = ? and Producto not like ? and Producto not like ?";
				String[] valores = new String[] { departamento, tipoProducto, planNumerico, "2", "%" + estrato + "%",
						"", "%IPTV%", "%HFC%" };

				llenarPlanes(campos, clausulas, valores);
			} else {
				ArrayList<String> plan = Utilidades.mostrarTVDigital("planesTVDigitalVisibleIPTV", ciudad);

				if (plan.size() > 0) {
					llenarPlanes(plan);
				}

			}
		}
		mostrarValores();
	}

	private void setTelevision(ArrayList<String> plan) {
		System.out.println("resources tv " + getResources().getText(R.string.television));
		lblTipoProducto.setText(getResources().getText(R.string.television));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tv));
		tipoProducto = Utilidades.tipo_producto_tv;

		// String planNumerico = Utilidades.planNumerico(estadoProducto);
		// if (planNumerico.equals("0")) {
		// planNumerico = "3";
		// }
		//
		// String[] campos = new String[] { "Producto", "ProductoHomologado" };
		// String clausulas = "Departamento=? and Tipo_Producto=? and Nuevo
		// IN(?,?) and Estrato like ? and Oferta = ? and Producto not like ? and
		// Producto not like ?";
		// String[] valores = new String[] { departamento, tipoProducto,
		// planNumerico, "2", "%" + estrato + "%", "", "%IPTV%", "%HFC%" };

		llenarPlanes(plan);
		mostrarValores();
	}

	private void setInternet() {
		lblTipoProducto.setText(getResources().getText(R.string.internet));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_ba));
		tipoProducto = Utilidades.tipo_producto_ba;

		String planNumerico = Utilidades.planNumerico(estadoProducto);
		if (planNumerico.equals("0")) {
			planNumerico = "3";
		}

		String[] campos = new String[] { "Producto", "ProductoHomologado" };
		String clausulas = "";
		String[] valores = null;

		if (isBalin) {
			clausulas = "Departamento=? and Tipo_Producto=? and Nuevo IN(?,?) and Estrato like ? and Oferta like ? and producto like ? ";
			valores = new String[] { departamento, Utilidades.tipo_producto_ba, planNumerico, "2", "%" + estrato + "%",
					"PROMO12%", "Mas%" };
		} else {
			clausulas = "Departamento=? and Tipo_Producto=? and (Producto like ? or Producto like ?) and Nuevo IN(?,?) and Estrato like ? ";
			valores = new String[] { departamento, Utilidades.tipo_producto_ba, "%Hog%", "%Existente%", planNumerico,
					"2", "%" + estrato + "%" };
		}

		llenarPlanes(campos, clausulas, valores);
		mostrarValores();
	}

	private void setInternet(ArrayList<String> plan) {
		lblTipoProducto.setText(getResources().getText(R.string.internet));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_ba));
		tipoProducto = Utilidades.tipo_producto_ba;

		// String planNumerico = Utilidades.planNumerico(estadoProducto);
		// if (planNumerico.equals("0")) {
		// planNumerico = "3";
		// }
		//
		// String[] campos = new String[] { "Producto", "ProductoHomologado" };
		// String clausulas = "Departamento=? and Tipo_Producto=? and (Producto
		// like ? or Producto like ?) and Nuevo IN(?,?) and Estrato like ? ";
		// String[] valores = new String[] { departamento,
		// Utilidades.tipo_producto_ba, "%Hog%", "%Existente%",
		// planNumerico, "2", "%" + estrato + "%" };

		llenarPlanes(plan);
		mostrarValores();
	}

	private void setAdicHD() {
		lblTipoProducto.setText(getResources().getText(R.string.adichd));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tv));

		String[] campos = new String[] { "adicional", "adicional" };
		String clausulas = "departamento=? and tipoProducto=? and Estrato like ? and producto like ? and adicional like ?";
		String[] valores = new String[] { departamento, Utilidades.tipo_producto_tv, "%" + estrato + "%", "%HFC%",
				"%Alta Definicion%" };
		if (!isHFC) {
			valores = new String[] { departamento, Utilidades.tipo_producto_tv, "%" + estrato + "%", "%IPTV%",
					"%Alta Definicion%" };
		}

		llenarPlanes(campos, clausulas, valores);
		mostrarValores();
	}

	private void setAdicHD(ArrayList<String> plan) {
		lblTipoProducto.setText(getResources().getText(R.string.adichd));
		imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tv));

		// String planNumerico = Utilidades.planNumerico(estadoProducto);
		// if (planNumerico.equals("0")) {
		// planNumerico = "3";
		// }
		//
		// String[] campos = new String[] { "Producto", "ProductoHomologado" };
		// String clausulas = "Departamento=? and Tipo_Producto=? and (Producto
		// like ? or Producto like ?) and Nuevo IN(?,?) and Estrato like ? ";
		// String[] valores = new String[] { departamento,
		// Utilidades.tipo_producto_ba, "%Hog%", "%Existente%",
		// planNumerico, "2", "%" + estrato + "%" };

		if (plan != null) {
			llenarPlanes(plan);
			mostrarValores();
		} else {
			chkHabilitar.setChecked(false);
			llyContent.setVisibility(GONE);
			txtDescuento.setVisibility(GONE);
			llyContent.setVisibility(GONE);
		}

	}

	public void unsetConfigurable() {
		isConfigurable = false;
	}

	public void setIPTV() {
		isHFC = false;
	}

	public String getPlan() {
		return (String) spnProducto.getSelectedItem();
	}

	public String getTotal() {
		return lblTotalContenido.getText().toString();
	}

	public boolean isActive() {
		return chkHabilitar.isChecked();
	}

	private void limpiar() {
		txtDescuento.setText("");
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
				txtDescuento.setVisibility(VISIBLE);
				slide_down(getContext(), llyContent);
				notifyObserver();
			}
		}
	};

	OnItemSelectedListener mostrarVelicidad = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			mostrarValores();
			notifyObserver();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub
		ArrayList<Object> data = new ArrayList<Object>();
		data.add("plan");
		data.add(getPlan());
		for (int i = 0; i < observers.size(); i++) {
			if (observers.get(i).getClass().getSimpleName().equals("ControlAmigoCuentasDigital")) {
				ArrayList<Object> respuestas = new ArrayList<Object>();
				if (isConfigurable) {

					if (isBalin) {
						respuestas.add("balin");
						observers.get(i).update(respuestas);
					} else {
						respuestas.add("configurable");
						observers.get(i).update(respuestas);
					}

				} else {
					respuestas.add("oferta");
					observers.get(i).update(respuestas);
				}

			} else {
				observers.get(i).update(data);
			}

		}
	}

	public String Existente(ArrayList<String> plan) {

		String planExistente = "";

		if (tipo == 0) {
			planExistente = "Telefonia Existente";
		} else if (tipo == 1) {
			for (int i = 0; i < plan.size(); i++) {
				String existente = Utilidades.TVExistente(plan.get(i));
				if (!existente.equalsIgnoreCase("")) {
					planExistente = existente;
					break;
				}
			}
		} else if (tipo == 2) {
			planExistente = "Internet Existente";
		}

		return planExistente;

		// public static final int TELEFONIA = 0;
		// public static final int TELEVISION = 1;
		// public static final int INTERNET = 2;

	}

	public void calcularTotalDescuento() {

		double totalDesc = 0;

		if (!descuento.equals("0")) {

			DecimalFormat decimales = new DecimalFormat("0.0000");
			double valor = 0;
			if (!isIndividual) {
				double desc = Double.parseDouble(descuento);
				if (valorEmpaquetadoIva != null) {
					desc = Double.parseDouble(valorEmpaquetadoIva);
				}

				totalDesc = valor - ((valor * desc) / 100);

				totalDescuento = String.valueOf(decimales.format(totalDesc));

				totalDescuento = totalDescuento.replace(",", ".");

				lblTotalContenidoDescuento.setText("(" + totalDescuento + ")");
			} else {
				double desc = Double.parseDouble(descuento);
				if (valorEmpaquetadoIva != null) {
					valor = Double.parseDouble(valorIndividualIva);
				}

				totalDesc = valor - ((valor * desc) / 100);

				totalDescuento = String.valueOf(decimales.format(totalDesc));

				lblTotalContenidoDescuento.setText("(" + totalDescuento + ")");
			}

		}

	}

}
