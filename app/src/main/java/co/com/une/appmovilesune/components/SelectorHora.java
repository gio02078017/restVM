package co.com.une.appmovilesune.components;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import co.com.une.appmovilesune.R;

public class SelectorHora extends LinearLayout {

	private EditText txtTexto;
	public ImageButton btnSelector;

	public SelectorHora(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.selectorhora, this);
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
