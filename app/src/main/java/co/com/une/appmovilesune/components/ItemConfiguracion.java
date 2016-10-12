package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemConfiguracion extends RelativeLayout {

	public TypedArray attrs;

	public CheckBox chkSelector;
	public ProgressBar pgbProceso;
	public ImageView imgEstado;
	public TextView txtNombre;

	public ItemConfiguracion(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ItemConfiguracion(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		this.attrs = context.obtainStyledAttributes(attrs, R.styleable.ItemConfiguracion, 0, 0);

	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.itemconfiguracion, this);

		chkSelector = (CheckBox) findViewById(R.id.chkSelector);
		pgbProceso = (ProgressBar) findViewById(R.id.pgbProgreso);
		imgEstado = (ImageView) findViewById(R.id.imgEstado);
		txtNombre = (TextView) findViewById(R.id.txtNombre);

		txtNombre.setText(attrs.getString(R.styleable.ItemConfiguracion_text));
	}

	public void setInvisible() {
		pgbProceso.setVisibility(ProgressBar.INVISIBLE);
	}

	public void setVisible() {
		pgbProceso.setVisibility(ProgressBar.VISIBLE);
		imgEstado.setVisibility(ImageView.INVISIBLE);
	}

	public void setOK() {
		imgEstado.setImageResource(R.drawable.responsegood);
		imgEstado.setVisibility(ImageView.VISIBLE);
	}

	public void setWRONG() {
		imgEstado.setImageResource(R.drawable.responsebad);
		imgEstado.setVisibility(ImageView.VISIBLE);
	}

	public boolean isCheked() {
		return chkSelector.isChecked();
	}

}
