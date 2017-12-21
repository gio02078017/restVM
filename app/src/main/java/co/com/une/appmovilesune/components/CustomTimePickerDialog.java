package co.com.une.appmovilesune.components;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;

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

    private int hourMin;
    private int hourMax;
    private int interval;
    private int tiempoLlamada;

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);

        hourMin = Integer.parseInt(MainActivity.basedatos.consultar(false,"listasvalores",new String[] {"lst_valor"}, "lst_nombre = ? and lst_clave = ?",new String[] {"configuracionSelectorFranja", "horaMinima"}, null, null, null).get(0).get(0));
        hourMax = Integer.parseInt(MainActivity.basedatos.consultar(false,"listasvalores",new String[] {"lst_valor"}, "lst_nombre = ? and lst_clave = ?",new String[] {"configuracionSelectorFranja", "horaMaxima"}, null, null, null).get(0).get(0));
        interval = Integer.parseInt(MainActivity.basedatos.consultar(false,"listasvalores",new String[] {"lst_valor"}, "lst_nombre = ? and lst_clave = ?",new String[] {"configuracionSelectorFranja", "intervalo"}, null, null, null).get(0).get(0));
        tiempoLlamada = Integer.parseInt(MainActivity.basedatos.consultar(false,"listasvalores",new String[] {"lst_valor"}, "lst_nombre = ? and lst_clave = ?",new String[] {"configuracionSelectorFranja", "tiempoLlamada"}, null, null, null).get(0).get(0));

        mTimeSetListener = listener;

    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour / interval);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    Log.d("Tiempo", String.valueOf(mTimePicker.getCurrentHour()));
                    Log.d("Tiempo", String.valueOf(mTimePicker.getCurrentMinute() * interval));

                    if(esTiempoTranscurridoCorrecto(mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute()*interval)){
                        mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                                mTimePicker.getCurrentMinute() * interval);
                    }else{
                        Toast.makeText(getContext(),getContext().getResources().getString(R.string.mensajefranjainvalidaa)+" "+tiempoLlamada+" "+getContext().getResources().getString(R.string.mensajefranjainvalidab),Toast.LENGTH_LONG).show();
                        cancel();
                    }

                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    private boolean esTiempoTranscurridoCorrecto(int horaSeleccionada, int minutoSeleccionado){
        boolean valido = true;

        Date horaActual= new Date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        Date horaProgramada = new Date(new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime()) + String.format("%02d", horaSeleccionada)+":"+String.format("%02d", minutoSeleccionado)+":00");

        Log.d("horaActual",horaActual.toString()+" "+String.valueOf(horaActual.getTime()));
        Log.d("horaProgramada",horaProgramada.toString()+" "+String.valueOf(horaProgramada.getTime()));

        long diferencia = ((horaProgramada.getTime() - horaActual.getTime())/1000)/60;

        if(diferencia < tiempoLlamada){
            valido = false;
        }

        return valido;
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
            minuteSpinner.setWrapSelectorWheel(false);

            hourSpinner  = (NumberPicker) mTimePicker
                    .findViewById(field1.getInt(null));
            hourSpinner.setOnValueChangedListener(cambioHora);
            hourSpinner.setWrapSelectorWheel(false);

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
        minuteSpinner.setMaxValue((60 / interval) - 1);
        List<String> displayedValues = new ArrayList<>();
        for (int i = minimo; i < 60; i += interval) {
            displayedValues.add(String.format("%02d", i));
        }
        Log.d("displayedValues.size()",String.valueOf(displayedValues.size()));
        minuteSpinner.setDisplayedValues(displayedValues
                .toArray(new String[displayedValues.size()]));
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

        if(hour >= hourMin && hour <= hourMax){
            hourSpinner.setMinValue(hour);
            hourSpinner.setMaxValue(hourMax);

            habilitarFranjas(0);

        }else{
            deshabilitarHorario();
        }

    }

    NumberPicker.OnValueChangeListener cambioHora = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Log.d("hourChange", oldVal + " to " + newVal);
            //calcularHorarios();
            if (hourSpinner.getValue() == hourMax) {
                deshabilitarFranjas();
            } else if (hourSpinner.getValue() == hour){
                habilitarFranjas(0);
            }else{
                habilitarFranjas(0);
            }
        }
    };

    NumberPicker.OnValueChangeListener cambioMinuto = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Log.d("minuteChange",oldVal+" to "+newVal);

        }
    };
}
