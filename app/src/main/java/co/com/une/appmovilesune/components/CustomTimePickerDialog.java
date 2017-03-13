package co.com.une.appmovilesune.components;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by davidzuluaga on 22/02/17.
 */

public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 15;
    private final static int HOUR_MIN = 8;
    private final static int HOUR_MAX = 22;
    private TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;

    private NumberPicker minuteSpinner;
    private NumberPicker hourSpinner;

    int hour;
    int minute;

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        mTimeSetListener = listener;
    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");
            Field field1 = classForid.getField("hour");

            minuteSpinner = (NumberPicker) mTimePicker
                    .findViewById(field.getInt(null));
            minuteSpinner.setOnValueChangedListener(cambioMinuto);

            hourSpinner  = (NumberPicker) mTimePicker
                    .findViewById(field1.getInt(null));
            hourSpinner.setOnValueChangedListener(cambioHora);

            hour = Calendar.getInstance().getTime().getHours()+1;
            minute = Calendar.getInstance().getTime().getMinutes();

            calcularHorarios();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void habilitarFranjas(int minimo){
        Log.d("Minimo ",""+minimo+"");
        minuteSpinner.setMinValue(0);
        minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
        List<String> displayedValues = new ArrayList<>();
        for (int i = minimo*15; i < 60; i += TIME_PICKER_INTERVAL) {
            displayedValues.add(String.format("%02d", i));
        }
        minuteSpinner.setDisplayedValues(displayedValues
                .toArray(new String[displayedValues.size()]));
        minuteSpinner.setWrapSelectorWheel(true);
    }

    private void deshabilitarHorario(){
        hourSpinner.setMinValue(0);
        hourSpinner.setMaxValue(0);

        minuteSpinner.setMinValue(0);
        minuteSpinner.setMaxValue(0);
    }

    private void deshabilitarFranjas(){
        minuteSpinner.setMinValue(0);
        minuteSpinner.setMaxValue(0);
    }

    private void calcularHorarios(){

        if(hour >= HOUR_MIN && hour <= HOUR_MAX){
            hourSpinner.setMinValue(hour);
            hourSpinner.setMaxValue(HOUR_MAX);

            if(hour == hourSpinner.getValue()){
                habilitarFranjas(2);
            }

            habilitarFranjas(0);

        }else{
            deshabilitarHorario();
        }

    }

    NumberPicker.OnValueChangeListener cambioHora = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Log.d("hourChange",oldVal+" to "+newVal);
            calcularHorarios();
        }
    };

    NumberPicker.OnValueChangeListener cambioMinuto = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Log.d("minuteChange",oldVal+" to "+newVal);
        }
    };
}
