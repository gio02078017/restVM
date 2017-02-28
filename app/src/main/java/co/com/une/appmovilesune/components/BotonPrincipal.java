package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class BotonPrincipal extends LinearLayout {

    private TextView txtNombre;
    public ProgressBar pgbProceso;
    private ImageView imgIcono, imgEstado;
    private TypedArray attrs;

    public BotonPrincipal(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public BotonPrincipal(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.attrs = context.obtainStyledAttributes(attrs, R.styleable.BotonPrincipal, 0, 0);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.botonprincipal, this);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        pgbProceso = (ProgressBar) findViewById(R.id.pgbProgreso);
        imgIcono = (ImageView) findViewById(R.id.imgIcono);
        imgEstado = (ImageView) findViewById(R.id.imgEstado);

        imgIcono.setImageResource(
                attrs.getResourceId(R.styleable.BotonPrincipal_icono, R.styleable.BotonPrincipal_icono));
        txtNombre.setText(attrs.getString(R.styleable.BotonPrincipal_text));
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

    public void restart() {
        pgbProceso.setVisibility(ImageView.INVISIBLE);
        imgEstado.setVisibility(ImageView.INVISIBLE);
    }

}
