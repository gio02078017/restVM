package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.model.ProductoAMG;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompPortafolio extends RelativeLayout {

    ImageView imgProducto, imgSuspendido;
    TextView lblTipoProducto, lblPlan, lblPlanFacturacion, lblIdentificador, lblVelocidadValor, lblClienteId, lblCargoBasicoContenido, lblIvaContenido, lblTotalContenido, txtDescuento;
    RelativeLayout rlyVelocidad;

    private ProductoAMG producto;

    public CompPortafolio(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public CompPortafolio(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();

    }

    public CompPortafolio(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();

    }

    private void init() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

        li.inflate(R.layout.compportafolio, this, true);

        imgProducto = (ImageView) findViewById(R.id.imgProducto);
        imgSuspendido = (ImageView) findViewById(R.id.imgSuspendido);

        lblTipoProducto = (TextView) findViewById(R.id.lblTipoProducto);
        lblPlan = (TextView) findViewById(R.id.lblPlan);
        lblPlanFacturacion = (TextView) findViewById(R.id.lblPlanFacturacion);
        lblIdentificador = (TextView) findViewById(R.id.lblIdentificador);
        lblVelocidadValor = (TextView) findViewById(R.id.lblVelocidadValor);
        lblClienteId = (TextView) findViewById(R.id.lblClienteId);
        lblCargoBasicoContenido = (TextView) findViewById(R.id.lblCargoBasicoContenido);
        lblIvaContenido = (TextView) findViewById(R.id.lblIvaContenido);
        lblTotalContenido = (TextView) findViewById(R.id.lblTotalContenido);
        txtDescuento = (TextView) findViewById(R.id.txtDescuento);

        rlyVelocidad = (RelativeLayout) findViewById(R.id.rlyVelocidad);
    }

    public ProductoAMG getProducto() {
        return producto;
    }

    public void setProducto(ProductoAMG producto) {
        this.producto = producto;
    }

    public void mostrarDatos() {

        System.out.println("producto.producto " + producto.producto);

        if (producto.producto.equalsIgnoreCase("TO")) {
            lblTipoProducto.setText(getResources().getText(R.string.telefonia));
            imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tel));
        } else if (producto.producto.equalsIgnoreCase("TV") || producto.producto.contains("TELEV")) {
            lblTipoProducto.setText(getResources().getText(R.string.television));
            imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tv));
        } else if (producto.producto.equalsIgnoreCase("BA")) {
            lblTipoProducto.setText(getResources().getText(R.string.internet));
            imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_ba));
            rlyVelocidad.setVisibility(View.VISIBLE);
            lblVelocidadValor.setText(producto.velocidad);
        }

        lblPlan.setText(producto.plan);
        lblPlanFacturacion.setText(producto.planFacturacion);
        lblIdentificador.setText(producto.identificador);

        lblClienteId.setText(producto.cliente);
        lblCargoBasicoContenido.setText(String.valueOf(producto.cargoBasico));
        double total = producto.cargoBasico;
        if (producto.iva != null) {
            for (int i = 0; i < producto.iva.size(); i++) {
                if (producto.iva.get(i)[0].equalsIgnoreCase("IVA CARGO BASICO")
                        || producto.iva.get(i)[0].equalsIgnoreCase("IVA CARGO FIJO")) {
                    total += Double.parseDouble(producto.iva.get(i)[1]);
                    lblIvaContenido.setText(producto.iva.get(i)[1]);
                }
            }
        }

        lblTotalContenido.setText(String.valueOf(total));
    }

    public double getTotal() {
        return Double.parseDouble(lblTotalContenido.getText().toString());
    }

}
