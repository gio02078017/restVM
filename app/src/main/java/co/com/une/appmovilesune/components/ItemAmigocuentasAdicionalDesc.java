package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ItemAmigocuentasAdicionalDesc extends RelativeLayout {

    public ItemAmigocuentasAdicionalDesc(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ItemAmigocuentasAdicionalDesc(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.itemamigocuentasaddesc, this);

    }

}
