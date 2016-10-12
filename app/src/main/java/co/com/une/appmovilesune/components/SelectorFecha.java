package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SelectorFecha extends LinearLayout {

	private EditText txtTexto;
	public ImageButton btnSelector;

	public SelectorFecha(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.selectorfecha, this);
		txtTexto = (EditText) findViewById(R.id.txtTexto);
		btnSelector = (ImageButton) findViewById(R.id.btnSeleccionador);
	}

	public String getTexto() {
		return txtTexto.getText().toString();
	}

	public void setTexto(String texto) {
		this.txtTexto.setText(texto);
	}

}
