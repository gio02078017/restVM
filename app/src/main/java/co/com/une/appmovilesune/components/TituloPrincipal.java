package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TituloPrincipal extends RelativeLayout {

	private TextView titulo;

	public TituloPrincipal(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.tituloprincipal, this);
		titulo = (TextView) findViewById(R.id.txtTitulo);
	}

	public void setTitulo(String titulo) {
		this.titulo.setText(titulo);
	}

}
