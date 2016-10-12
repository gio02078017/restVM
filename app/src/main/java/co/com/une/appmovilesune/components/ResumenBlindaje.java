package co.com.une.appmovilesune.components;

import co.com.une.appmovilesune.model.Blindaje;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ResumenBlindaje extends LinearLayout {

	public Blindaje blindaje;

	public ResumenBlindaje(Context context, Blindaje blindaje) {
		super(context);
		// TODO Auto-generated constructor stub
		this.blindaje = blindaje;
	}

	public ResumenBlindaje(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

}
