package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemAmigocuentasAdicional extends RelativeLayout {

    public ItemAmigocuentasAdicional(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ItemAmigocuentasAdicional(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.itemamigocuentasad, this);

    }

}
