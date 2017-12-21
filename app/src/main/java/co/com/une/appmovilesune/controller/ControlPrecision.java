package co.com.une.appmovilesune.controller;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.components.TituloPrincipal;

public class ControlPrecision extends Activity {

    private WebView myWebView;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewprecision);

        TituloPrincipal tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("CIFIN");

        // viewEstadoPedido =(ListView)findViewById(R.id.viewEstadoPedido);
        /*
		 * tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal); tp.setTitulo(
		 * "Validador Cifin");
		 */
        myWebView = (WebView) this.findViewById(R.id.webPrecision);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.requestFocus();
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        // myWebView.getSettings().setPluginsEnabled(true);

        myWebView.loadUrl("http://cifin.asobancaria.com/cifin/index.jsp");

        myWebView.setWebViewClient(new WebViewClient() {
            // evita que los enlaces se abran fuera nuestra app en el navegador
            // de android
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }
                try {
                    webView.clearView();
                } catch (Exception e) {
                }
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                webView.loadUrl("about:blank");
                AlertDialog alertDialog = new AlertDialog.Builder(ControlPrecision.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Hay un error cargando CIFIN, intenta luego...");
                alertDialog.setButton("Intentar de nuevo?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });
                alertDialog.show();
                super.onReceivedError(webView, errorCode, description, failingUrl);
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (MainActivity.seguimiento) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (MainActivity.seguimiento) {
            EasyTracker.getInstance(this).activityStop(this);
        }
    }

    public void anterior(View view) {
        myWebView.goBack();
    }

    public void siguiente(View view) {
        myWebView.goForward();
    }

    public void detener(View view) {
        myWebView.stopLoading();
    }

    @Override
    protected void onDestroy() {
        if (myWebView != null) {
            super.onDestroy();
            myWebView.destroy();
        }

    }

}
