package co.com.une.appmovilesune.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.interfaces.ObserverTotales;

public class CompTotalCotizador extends LinearLayout {

	public static final String TOTAL_PAQUETE = "Total Paquete";
	public static final String TOTAL_PAQUETE_ADICIONALES = "Total Paquete + Adicionales";

	private TextView txtvalortotalindividual;
	private TextView txtvalortotalempaquetado;
	private TextView txtvalortotalindividualadicionales;
	private TextView txtvalortotalempaquetadoadicionales;
	private TextView txtvalortotalpagoconexion;
	private TextView txtvalortotalpagoparcial;
	private TextView txtvalordescuentoconexion;

	public CompTotalCotizador(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompTotalCotizador(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	@SuppressLint("NewApi")
	public CompTotalCotizador(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.comptotalcotizador, this, true);

		txtvalortotalindividual = (TextView) findViewById(R.id.txtvalortotalindividual);
		txtvalortotalempaquetado = (TextView) findViewById(R.id.txtvalortotalempaquetado);
		txtvalortotalindividualadicionales = (TextView) findViewById(R.id.txtvalortotalindividualadicionales);
		txtvalortotalempaquetadoadicionales = (TextView) findViewById(R.id.txtvalortotalempaquetadoadicionales);
		txtvalortotalpagoconexion = (TextView) findViewById(R.id.txtvalortotalpagoconexion);
		txtvalortotalpagoparcial = (TextView) findViewById(R.id.txtvalortotalpagoparcial);
		txtvalordescuentoconexion = (TextView) findViewById(R.id.txtvalordescuentoconexion);
	}

	public void llenarTotales(double totalIndividual, double totalEmpaquetado, double totalAdicionalesTV, double totalDecodificadores, double totalAdicionalesTO, double totalConexion, double totalPagoParcial, double valorDescuentoConexion){

		System.out.println("TotalesCot ltll ind "+totalIndividual);
		System.out.println("TotalesCot ltll emp "+totalEmpaquetado);

		double totalAdicionales = totalAdicionalesTO+totalAdicionalesTV;
		setTotalIndividual("$" + totalIndividual);
		setTotalEmpaquetado("$" + totalEmpaquetado);

		totalIndividual += totalAdicionales + totalDecodificadores;
		totalEmpaquetado += totalAdicionales + totalDecodificadores;

		System.out.println("TotalesCot suma ind "+totalIndividual);
		System.out.println("TotalesCot suma emp "+totalEmpaquetado);

		setTotalIndividualAdicionales("$" + totalIndividual);
		setTotalEmpaquetadoAdicionales("$" + totalEmpaquetado);

		setTotalConexion("$" + totalConexion);
		setTotalPagoParcial("$" + totalPagoParcial);
		setValorDescuentoConexion("$" + valorDescuentoConexion);
	}

	public void limpiarTotales(){
		setTotalIndividual("$0.0");
		setTotalEmpaquetado("$0.0");
		setTotalIndividualAdicionales("$0.0");
		setTotalEmpaquetadoAdicionales("$0.0");
	}

	public void setTotalIndividual(String valor){
		txtvalortotalindividual.setText(valor);
	}

	public void setTotalEmpaquetado(String valor){
		txtvalortotalempaquetado.setText(valor);
	}

	public void setTotalIndividualAdicionales(String valor){
		txtvalortotalindividualadicionales.setText(valor);
	}

	public void setTotalEmpaquetadoAdicionales(String valor){
		txtvalortotalempaquetadoadicionales.setText(valor);
	}

	public double getTotalIndividualAdicionales(){
		String cadena =  txtvalortotalindividualadicionales.getText().toString();
		cadena = cadena.substring(1);
		double total = Double.parseDouble(cadena);

		return total;
	}

	public double getTotalEmpaquetadoAdicionales(){
		String cadena =  txtvalortotalempaquetadoadicionales.getText().toString();
		cadena = cadena.substring(1);
		double total = Double.parseDouble(cadena);

		return total;
	}

	public void setTotalConexion(String valor){
		txtvalortotalpagoconexion.setText(valor);
	}

	public void setTotalPagoParcial(String valor){
		txtvalortotalpagoparcial.setText(valor);
	}

	public void setValorDescuentoConexion(String valor){
		txtvalordescuentoconexion.setText(valor);
	}

	public double getTotalConexion(){
		String cadena =  txtvalortotalpagoconexion.getText().toString();
		cadena = cadena.substring(1);
		double total = Double.parseDouble(cadena);

		return total;
	}

	public double getTotalPagoParcial(){
		String cadena =  txtvalortotalpagoparcial.getText().toString();
		cadena = cadena.substring(1);
		double total = Double.parseDouble(cadena);

		return total;
	}

	public double getValorDescuentoConexion(){
		String cadena =  txtvalordescuentoconexion.getText().toString();
		cadena = cadena.substring(1);
		double total = Double.parseDouble(cadena);

		return total;
	}
}
