package co.com.une.appmovilesune.model;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

public class Carrusel extends AsyncTask<ArrayList<Object>, Integer, String> implements Subject {

	private Context context;
	private ProgressDialog pd;
	private Observer observer;
	private Object resultado;

	public Carrusel(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		System.out.println(context);
		pd = ProgressDialog.show(context, context.getResources().getString(R.string.consultando),
				context.getResources().getString(R.string.obteniendoinformacion));
	}

	@Override
	protected String doInBackground(ArrayList<Object>... params) {
		// TODO Auto-generated method stub

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "parametros", (String) params[0].get(0) });
		param.add(new String[] { "debug", (String) params[0].get(1) });
		param.add(new String[] { "profundidad", (String) params[0].get(2) });

		return MainActivity.conexion.ejecutarSoap("consultarCarrusel", param);
	}

	@Override
	protected void onPostExecute(String result) {
		pd.dismiss();

		System.out.println("result " + result);
		resultado = result;

		notifyObserver();
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
		observer.update(resultado);
	}

}
