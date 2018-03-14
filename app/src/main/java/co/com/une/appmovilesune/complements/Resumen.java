package co.com.une.appmovilesune.complements;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.model.AdicionalCotizador;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Venta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Resumen extends Activity {

    Venta venta;
    Cliente cliente;

    TextView lblTO, lblTV, lblAdicional, lblBA, txtPlanTO, txtDescuentoTO, txtPlanTV, txtDescuentoTV,
            txtAdicionales, txtPlanBA, txtDescuentoBA, txtTotal,
            txtAdicionalesTO, lblAdicionalTO,txtAdicionalesBA, lblAdicionalBA;

    TableRow tableimpuesto;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewconfirmacion);

        Bundle reicieveParams = getIntent().getExtras();
        venta = (Venta) reicieveParams.getSerializable("venta");
        cliente = (Cliente) reicieveParams.getSerializable("cliente");

        tableimpuesto = (TableRow) findViewById(R.id.tableImpuesto);
        tableimpuesto.setVisibility(View.GONE);

        lblTO = (TextView) findViewById(R.id.lblTelefonia);
        txtPlanTO = (TextView) findViewById(R.id.planTelefonia);
        txtDescuentoTO = (TextView) findViewById(R.id.dtoTelefonia);
        txtAdicionalesTO = (TextView) findViewById(R.id.strAdicionalesTO);
        lblAdicionalTO = (TextView) findViewById(R.id.lblAdicionalesTO);

        lblTV = (TextView) findViewById(R.id.lblTelevision);
        txtPlanTV = (TextView) findViewById(R.id.planTelevision);
        txtDescuentoTV = (TextView) findViewById(R.id.dtoTelevision);
        txtAdicionales = (TextView) findViewById(R.id.strAdicionales);
        lblAdicional = (TextView) findViewById(R.id.lblAdicionales);

        lblBA = (TextView) findViewById(R.id.lblInternet);
        txtPlanBA = (TextView) findViewById(R.id.planInternet);
        txtDescuentoBA = (TextView) findViewById(R.id.dtoInternet);

        txtAdicionalesBA = (TextView) findViewById(R.id.strAdicionalesBA);
        lblAdicionalBA = (TextView) findViewById(R.id.lblAdicionalesBA);

        txtTotal = (TextView) findViewById(R.id.totalVenta);

        for (int i = 0; i < venta.getCotizacionCliente().getProductoCotizador().size(); i++) {
                if (venta.getCotizacionCliente().getProductoCotizador().get(i).getTipo() == ProductoCotizador.getTELEFONIA()) {
                    pintarTelefonia(venta.getCotizacionCliente().getProductoCotizador().get(i));
                } else if (venta.getCotizacionCliente().getProductoCotizador().get(i).getTipo() == ProductoCotizador.getTELEVISION()) {
                    pintarTelevision(venta.getCotizacionCliente().getProductoCotizador().get(i));
                } else if (venta.getCotizacionCliente().getProductoCotizador().get(i).getTipo() == ProductoCotizador.getINTERNET()) {
                    pintarInternet(venta.getCotizacionCliente().getProductoCotizador().get(i));
                }
        }

       /* if (!venta.getTelefonia()[0].equals("-") && !venta.getTelefonia()[0].equals("")) {
            txtPlanTO.setText(venta.getTelefonia()[0]);
            if (!venta.getTelefonia()[6].equals("-")) {
                txtDescuentoTO.setText(venta.getTelefonia()[6]);
            } else {
                txtDescuentoTO.setVisibility(View.GONE);
            }

            if (!venta.getTelefonia()[13].equals("-") && !venta.getTelefonia()[13].trim().equals("")) {
                txtAdicionalesTO.setText(venta.getTelefonia()[13]);
            } else {
                lblAdicionalTO.setVisibility(View.GONE);
                txtAdicionalesTO.setVisibility(View.GONE);
                findViewById(R.id.TableRow16).setVisibility(View.GONE);
            }

            if (Utilidades.excluirMunicipal("mensajeImpuestoEstrato", "mensajeImpuestoEstrato", cliente.getCiudad())) {
                if (Utilidades.listaEstratos("mensajeImpuesto", cliente.getEstrato()).equalsIgnoreCase("true")) {
                    tableimpuesto.setVisibility(View.VISIBLE);
                }
            } else {
                tableimpuesto.setVisibility(View.VISIBLE);
            }

        } else {
            lblTO.setVisibility(View.GONE);
            txtPlanTO.setVisibility(View.GONE);
            txtDescuentoTO.setVisibility(View.GONE);
            findViewById(R.id.tableRow1).setVisibility(View.GONE);
            findViewById(R.id.tableRow2).setVisibility(View.GONE);
        }

        if (!venta.getTelevision()[0].equals("-") && !venta.getTelevision()[0].equals("")) {
            txtPlanTV.setText(venta.getTelevision()[0]);
            if (!venta.getTelevision()[3].equals("-")) {
                txtDescuentoTV.setText(venta.getTelevision()[3]);
            } else {
                txtDescuentoTV.setVisibility(View.GONE);
            }
            if (!venta.getTelevision()[6].equals("-") && !venta.getTelevision()[6].trim().equals("")) {
                txtAdicionales.setText(venta.getTelevision()[6]);
            } else {
                lblAdicional.setVisibility(View.GONE);
                txtAdicionales.setVisibility(View.GONE);
                findViewById(R.id.TableRow5).setVisibility(View.GONE);
            }
        } else {
            lblTV.setVisibility(View.GONE);
            txtPlanTV.setVisibility(View.GONE);
            txtDescuentoTV.setVisibility(View.GONE);
            lblAdicional.setVisibility(View.GONE);
            txtAdicionales.setVisibility(View.GONE);
            findViewById(R.id.tableRow3).setVisibility(View.GONE);
            findViewById(R.id.TableRow4).setVisibility(View.GONE);
            findViewById(R.id.TableRow5).setVisibility(View.GONE);
            findViewById(R.id.TableRow6).setVisibility(View.GONE);
        }

        if (!venta.getInternet()[0].equals("-") && !venta.getInternet()[0].equals("")) {
            txtPlanBA.setText(venta.getInternet()[0]);
            if (!venta.getInternet()[5].equals("-")) {
                txtDescuentoBA.setText(venta.getInternet()[5]);
            } else {
                txtDescuentoBA.setVisibility(View.GONE);
            }
        } else {
            lblBA.setVisibility(View.GONE);
            txtPlanBA.setVisibility(View.GONE);
            txtDescuentoBA.setVisibility(View.GONE);
            findViewById(R.id.TableRow7).setVisibility(View.GONE);
            findViewById(R.id.TableRow8).setVisibility(View.GONE);
        }*/

        txtTotal.setText(venta.getTotal());

    }

    public void pintarTelefonia(ProductoCotizador productoCotizador){
        if(!Utilidades.validarVacioProducto(productoCotizador.getPlan())) {
            txtPlanTO.setText(productoCotizador.getPlan());

            if(productoCotizador.getDescuentoCargobasico() > 0.0){
                txtDescuentoTO.setText(productoCotizador.descuentoConcatenado());
            }else{
                txtDescuentoTO.setVisibility(View.GONE);
            }
        }else{
            lblTO.setVisibility(View.GONE);
            txtPlanTO.setVisibility(View.GONE);
            txtDescuentoTO.setVisibility(View.GONE);
            findViewById(R.id.tableRow1).setVisibility(View.GONE);
            findViewById(R.id.tableRow2).setVisibility(View.GONE);
        }

        /*if (!venta.getTelefonia()[0].equals("-") && !venta.getTelefonia()[0].equals("")) {
            txtPlanTO.setText(venta.getTelefonia()[0]);
            if (!venta.getTelefonia()[6].equals("-")) {
                txtDescuentoTO.setText(venta.getTelefonia()[6]);
            } else {
                txtDescuentoTO.setVisibility(View.GONE);
            }

            if (!venta.getTelefonia()[13].equals("-") && !venta.getTelefonia()[13].trim().equals("")) {
                txtAdicionalesTO.setText(venta.getTelefonia()[13]);
            } else {
                lblAdicionalTO.setVisibility(View.GONE);
                txtAdicionalesTO.setVisibility(View.GONE);
                findViewById(R.id.TableRow16).setVisibility(View.GONE);
            }

            if (Utilidades.excluirMunicipal("mensajeImpuestoEstrato", "mensajeImpuestoEstrato", cliente.getCiudad())) {
                if (Utilidades.listaEstratos("mensajeImpuesto", cliente.getEstrato()).equalsIgnoreCase("true")) {
                    tableimpuesto.setVisibility(View.VISIBLE);
                }
            } else {
                tableimpuesto.setVisibility(View.VISIBLE);
            }

        } else {
            lblTO.setVisibility(View.GONE);
            txtPlanTO.setVisibility(View.GONE);
            txtDescuentoTO.setVisibility(View.GONE);
            findViewById(R.id.tableRow1).setVisibility(View.GONE);
            findViewById(R.id.tableRow2).setVisibility(View.GONE);
        }*/
    }

    public void pintarTelevision(ProductoCotizador productoCotizador){
        if(!Utilidades.validarVacioProducto(productoCotizador.getPlan())) {
            txtPlanTV.setText(productoCotizador.getPlan());

            if(productoCotizador.getDescuentoCargobasico() > 0.0){
                txtDescuentoTV.setText(productoCotizador.descuentoConcatenado());
            }else{
                txtDescuentoTV.setVisibility(View.GONE);
            }

            String adicionales = pintarAdicionales(productoCotizador.getAdicionalesCotizador());

           if(adicionales != null){
               txtAdicionales.setText(adicionales);
           }else{
               lblAdicional.setVisibility(View.GONE);
               txtAdicionales.setVisibility(View.GONE);
           }
        }else{
            lblTV.setVisibility(View.GONE);
            txtPlanTV.setVisibility(View.GONE);
            txtDescuentoTV.setVisibility(View.GONE);
            lblAdicional.setVisibility(View.GONE);
            txtAdicionales.setVisibility(View.GONE);
            findViewById(R.id.tableRow3).setVisibility(View.GONE);
            findViewById(R.id.TableRow4).setVisibility(View.GONE);
            findViewById(R.id.TableRow5).setVisibility(View.GONE);
            findViewById(R.id.TableRow6).setVisibility(View.GONE);
        }
    }

    public String pintarAdicionales(ArrayList<AdicionalCotizador> arrayAdicionales){
        String adicionales = null;

        if(arrayAdicionales.size() > 0){
            for (int i = 0; i < arrayAdicionales.size(); i++) {
                arrayAdicionales.get(i).imprimirAdicional();
                if(i==0){
                    adicionales = arrayAdicionales.get(i).getNombreAdicional();
                }else{
                    adicionales += " , "+arrayAdicionales.get(i).getNombreAdicional();
                }
            }
        }

        return adicionales;
    }

    public void pintarInternet(ProductoCotizador productoCotizador){
        if(!Utilidades.validarVacioProducto(productoCotizador.getPlan())) {
            txtPlanBA.setText(productoCotizador.getPlan());

            if(productoCotizador.getDescuentoCargobasico() > 0.0){
                txtDescuentoBA.setText(productoCotizador.descuentoConcatenado());
            }else{
                txtDescuentoBA.setVisibility(View.GONE);
            }
            String adicionales = pintarAdicionales(productoCotizador.getAdicionalesCotizador());

            if(adicionales != null){
                txtAdicionalesBA.setText(adicionales);
            }else{
                lblAdicionalBA.setVisibility(View.GONE);
                txtAdicionalesBA.setVisibility(View.GONE);
            }
        }else{
            lblBA.setVisibility(View.GONE);
            txtPlanBA.setVisibility(View.GONE);
            txtDescuentoBA.setVisibility(View.GONE);
            lblAdicionalBA.setVisibility(View.GONE);
            txtAdicionalesBA.setVisibility(View.GONE);
            findViewById(R.id.TableRow7).setVisibility(View.GONE);
            findViewById(R.id.TableRow8).setVisibility(View.GONE);
        }
    }

    public void aceptarResumen(View v) {
        Intent intent = new Intent();
        intent.putExtra("result", "Acepto");
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
    }

    public void cancelarResumen(View v) {
        Intent intent = new Intent();
        intent.putExtra("result", "Cancelo");
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
    }

}