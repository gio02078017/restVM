package co.com.une.appmovilesune.model;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

public class Carrusel extends AsyncTask<ArrayList<Object>, Integer, ArrayList<Object>> implements Subject {

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
    protected ArrayList<Object> doInBackground(ArrayList<Object>... params) {
        // TODO Auto-generated method stub

        ArrayList<String[]> param = new ArrayList<String[]>();

        param.add(new String[]{"parametros", (String) params[0].get(0)});
        param.add(new String[]{"debug", (String) params[0].get(1)});
        param.add(new String[]{"profundidad", (String) params[0].get(2)});

        ArrayList<Object> resultados = new ArrayList<Object>();
        resultados.add("consultarCarrusel");
        resultados.add(MainActivity.conexion.ejecutarSoap("consultarCarrusel", param));

        System.out.println("Conexion doInBackground simulador despues CambiarAccion"+MainActivity.conexion.isCambiarAccion());
        System.out.println("Conexion doInBackground simulador despues NuevaAccion"+MainActivity.conexion.getNuevaAccion());

        if(MainActivity.conexion.isCambiarAccion()) {
            System.out.println("Conexion doInBackground simulador validacion "+ Utilidades.validacionConfig(resultados));
            ArrayList<Object> validados = Utilidades.validacionConfig(resultados);
            if((Boolean) validados.get(0)){
                //resultados.add(1,validados.get(1));
                resultados.set(1,validados.get(1));
            }else{
                //resultados.add(0,MainActivity.conexion.getNuevaAccion());
                resultados.set(0,MainActivity.conexion.getNuevaAccion());
            }
        }

        System.out.println("Conexion doInBackground simulador resultados "+resultados);


        return resultados;
    }

    @Override
    protected void onPostExecute(ArrayList<Object> result) {
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
