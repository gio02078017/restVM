package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemCarrusel;
import co.com.une.appmovilesune.adapters.ListaCarruselAdapter;
import co.com.une.appmovilesune.change.ControlSimulador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Carrusel;
import co.com.une.appmovilesune.model.Cliente;

public class ControlCarrusel extends Activity implements Observer {

	private TituloPrincipal tp;
	private ListView listacarrusel;
	private ImageButton consulta;
	private ProgressDialog dialogo;
	private Carrusel carrusel;
	private Cliente cliente;
	private EditText txtDocumento;
	private EditText txtDireccion;
	private TextView mensajecarrusel;
	private TableRow rowMensaje;
	private String crm;
	

	public static JSONObject mensajes;

	ArrayList<ItemCarrusel> arraycarrusel = new ArrayList<ItemCarrusel>();

	protected static final int REQUEST_CODE = 10;
	private static final int OK_RESULT_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewcarrusel);

		tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
		listacarrusel = (ListView) findViewById(R.id.listacarrusel);
		consulta = (ImageButton) findViewById(R.id.consulta);
		txtDocumento = (EditText) findViewById(R.id.txtDocumento);
		txtDireccion = (EditText) findViewById(R.id.txtDireccion);
		mensajecarrusel = (TextView) findViewById(R.id.mensajeCarrusel);
		rowMensaje = (TableRow) findViewById(R.id.rowMensaje);

		Bundle reicieveParams = getIntent().getExtras();

		if (reicieveParams != null) {
			cliente = (Cliente) reicieveParams.getSerializable("cliente");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		tp.setTitulo(getResources().getString(R.string.carrusel));

		txtDocumento.setText(cliente.getCedula());
		txtDireccion.setText(cliente.getDireccion());
		mensajecarrusel.setText("");
		rowMensaje.setVisibility(View.GONE);

		consulta.setOnClickListener(consultapresionado);

	}

	OnClickListener consultapresionado = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// mostrarCarrusel();
			consultarCarrusel();
		}
	};

	private void consultarCarrusel() {

		carrusel = new Carrusel(this);
		carrusel.addObserver(this);
		mensajecarrusel.setText("");
		rowMensaje.setVisibility(View.GONE);

		ArrayList<Object> parametros = new ArrayList<Object>();
		JSONObject dataCarrusel = new JSONObject();
		try {

			String ciudad;
			String departamento;
			String codigoDireccion = "";
			boolean controlDatos = true;
			mensajes = new JSONObject();

			crm = Utilidades.camposUnicosCiudad("CRMCarrusel", cliente.getCiudad());

			JSONArray ja = new JSONArray();

			if (cliente.getDireccion().equalsIgnoreCase("")) {
				controlDatos = false;
				ja.put(Utilidades.jsonMensajes(this.getResources().getString(R.string.direccion), "Sin Diligenciar"));

			}

			/*if (cliente.getCedula().equalsIgnoreCase("")) {
				controlDatos = false;
				ja.put(Utilidades.jsonMensajes("Documento", "Sin Diligenciar"));

			}*/

			if (crm.equalsIgnoreCase("FENIX_ATC")) {
				if (!cliente.isControlNormalizada()) {
					controlDatos = false;
					ja.put(Utilidades.jsonMensajes("Direccion",
							this.getResources().getString(R.string.normailizardireccion)));
				} else {
					codigoDireccion = cliente.getPaginacion();
				}
			} else if (crm.equalsIgnoreCase("ELITE")) {
				codigoDireccion = "0";
			} else {
				codigoDireccion = cliente.getPaginacion();
			}

			if (controlDatos) {
				if (cliente.getCiudad().equalsIgnoreCase("BogotaREDCO")) {
					ciudad = "BOGOTA";
					departamento = "Cundinamarca";
				} else if (cliente.getCiudad().equalsIgnoreCase("Buga")) {
					ciudad = "";
					departamento = "";
				} else {
					ciudad = cliente.getCiudad();
					departamento = cliente.getDepartamento();
				}

				crm = Utilidades.camposUnicosCiudad("CRMCarrusel", cliente.getCiudad());

				dataCarrusel.put("crm", crm);
				dataCarrusel.put("apporigen", "VM");
				if(!cliente.getCedula().equalsIgnoreCase("")){
					dataCarrusel.put("documento", cliente.getCedula());
				}else{
					dataCarrusel.put("documento", "1234567");
				}

				dataCarrusel.put("direccion", cliente.getDireccion());
				dataCarrusel.put("municipio", ciudad);
				dataCarrusel.put("departamento", departamento);
				dataCarrusel.put("barrio", cliente.getBarrio());
				dataCarrusel.put("codigoDireccion", codigoDireccion);
				
				parametros.add(dataCarrusel.toString());
				parametros.add("false");
				parametros.add("0");
				carrusel.execute(parametros);
			} else {
				mensajes.put("Titulo", "Campos Requeridos");
				mensajes.put("Mensajes", ja);
				mensajeValidaciones(mensajes.toString());
				// setMensajes(mensajes);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.w("Error", e.getMessage());
		}

	}

	public void mensajeValidaciones(String mensajes) {
		Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
		intent.putExtra("mensajes", mensajes);
		startActivityForResult(intent, MainActivity.REQUEST_CODE);
	}

	private void mostrarCarrusel() {
		// ArrayList<ItemCarrusel> carrusel = new ArrayList<ItemCarrusel>();
		arraycarrusel.clear();
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));
		arraycarrusel.add(new ItemCarrusel("2016-05-02", "TO", "CARRUSEL", "PQ-12341xxxx"));

		ListaCarruselAdapter adapter = new ListaCarruselAdapter(this, arraycarrusel, this);

		listacarrusel.setAdapter(adapter);
		ControlSimulador.setListViewHeightBasedOnChildren(listacarrusel);
	}

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		System.out.println(value);
		arraycarrusel.clear();
		try {
			JSONObject jop = new JSONObject(value.toString());
			cliente.setControlCarrusel(true);
			if (jop.has("data")) {
				String data = jop.get("data").toString();
				if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
					data = new String(Base64.decode(data));
					System.out.println("data => " + data);
					JSONObject respuesta = new JSONObject(data);

					System.out.println("respuesta " + respuesta);

					if (respuesta.has("codigo")) {

						cliente.setCodigoCarrusel(respuesta.getString("codigo"));

						if (respuesta.has("direccion") && respuesta.getJSONObject("direccion").has("natural")) {
							cliente.setDireccionCarrusel(respuesta.getJSONObject("direccion").getString("natural"));
						}

						if (respuesta.has("id_cliente")) {
							cliente.setDocumentoCarrusel(respuesta.getString("id_cliente"));
						}
						
						cliente.setCrmCarrusel(crm);

						if (respuesta.getString("codigo").equalsIgnoreCase("00")) {

							if (respuesta.has("productos")) {
								JSONArray productos = respuesta.getJSONArray("productos");

								System.out.println("productos " + productos);

								for (int i = 0; i < productos.length(); i++) {

									String producto = productos.getJSONObject(i).getString("producto");

									if (producto.equalsIgnoreCase("ba") || producto.equalsIgnoreCase("inter")) {
										producto = "BA";
									} else if (producto.equalsIgnoreCase("tv") || producto.equalsIgnoreCase("telev")) {
										producto = "TV";
									}

									arraycarrusel.add(new ItemCarrusel(
											productos.getJSONObject(i).getString("fecha_retiro"), producto,
											productos.getJSONObject(i).getString("motivo"), "PQ-12341xxxx"));

								}

								System.out.println("arraycarrusel " + arraycarrusel);

								ListaCarruselAdapter adapter = new ListaCarruselAdapter(this, arraycarrusel, this);

								listacarrusel.setAdapter(adapter);
								ControlSimulador.setListViewHeightBasedOnChildren(listacarrusel);

								cliente.setArraycarrusel(arraycarrusel);
							}
						} else {
							rowMensaje.setVisibility(View.VISIBLE);
							mensajecarrusel.setText(respuesta.getString("mensaje"));
						}
					}
				}

			}
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}
	}

	// metodo que permite regresar a la actividad anterior
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("keyCode " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("cliente", cliente);
			setResult(MainActivity.OK_RESULT_CODE, intent);
			super.onBackPressed();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("cliente", cliente);
		// intent.putExtra("Scooring", scooring);
		setResult(MainActivity.OK_RESULT_CODE, intent);
	}
}
