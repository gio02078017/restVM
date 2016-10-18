package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TituloPrincipal extends RelativeLayout {

	private TextView titulo;

	public TituloPrincipal(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TituloPrincipal(Context context, AttributeSet attrs, int defStyleAttr, TextView titulo) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public TituloPrincipal(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, TextView titulo) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init(){
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.tituloprincipal, this, true);

		titulo = (TextView) findViewById(R.id.txtTitulo);
	}

	public void setTitulo(String titulo) {
		this.titulo.setText(titulo);
	}

}
