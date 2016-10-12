package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.adapters.ItemTarificador;
import co.com.une.appmovilesune.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResumenTarificador extends LinearLayout {

	private TextView[] rt = new TextView[28];

	ArrayList<ItemTarificador> resumenTarificador = new ArrayList<ItemTarificador>();

	public ResumenTarificador(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ResumenTarificador(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.resumentarificador, this);

		// Array TO
		rt[0] = (TextView) findViewById(R.id.txt11);
		rt[1] = (TextView) findViewById(R.id.txt12);
		rt[2] = (TextView) findViewById(R.id.txt13);
		rt[3] = (TextView) findViewById(R.id.txt14);

		// Array TV
		rt[4] = (TextView) findViewById(R.id.txt21);
		rt[5] = (TextView) findViewById(R.id.txt22);
		rt[6] = (TextView) findViewById(R.id.txt23);
		rt[7] = (TextView) findViewById(R.id.txt24);

		// Array AD
		rt[8] = (TextView) findViewById(R.id.txt31);
		rt[9] = (TextView) findViewById(R.id.txt32);
		rt[10] = (TextView) findViewById(R.id.txt33);
		rt[11] = (TextView) findViewById(R.id.txt34);

		// Array BA
		rt[12] = (TextView) findViewById(R.id.txt41);
		rt[13] = (TextView) findViewById(R.id.txt42);
		rt[14] = (TextView) findViewById(R.id.txt43);
		rt[15] = (TextView) findViewById(R.id.txt44);

		// Array 3G
		rt[16] = (TextView) findViewById(R.id.txt51);
		rt[17] = (TextView) findViewById(R.id.txt52);
		rt[18] = (TextView) findViewById(R.id.txt53);
		rt[19] = (TextView) findViewById(R.id.txt54);

		// Array 4G
		rt[20] = (TextView) findViewById(R.id.txt61);
		rt[21] = (TextView) findViewById(R.id.txt62);
		rt[22] = (TextView) findViewById(R.id.txt63);
		rt[23] = (TextView) findViewById(R.id.txt64);

		// Array Total
		rt[24] = (TextView) findViewById(R.id.txt71);
		rt[25] = (TextView) findViewById(R.id.txt72);
		rt[26] = (TextView) findViewById(R.id.txt73);
		rt[27] = (TextView) findViewById(R.id.txt74);

	}

	public void mostrar() {

	}

	public void resumen(ArrayList<ItemTarificador> Resumen) {
		resumenTarificador.clear();
		resumenTarificador = Resumen;

		System.out.println("resumenTarificador " + resumenTarificador.size());

		System.out.println("resumenTarificador " + resumenTarificador);

		for (int i = 0; i < resumenTarificador.size(); i++) {
			rt[i].setText(resumenTarificador.get(i).getDato().toString());
		}
	}

}
