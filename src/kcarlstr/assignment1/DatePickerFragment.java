package kcarlstr.assignment1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by kylecarlstrom on 15-01-20.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String DATE_FRAGMENT_INTENT = "com.kylecarlstrom.datepicker_fragment_intent";
    Date date;
    OnDataPass dataPasser;

    public interface OnDataPass {
        public void onDataPass(Date data);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataPasser = (OnDataPass) activity;
    }

    public void passData(Date data) {
        dataPasser.onDataPass(data);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        date = (Date) getArguments().getSerializable(DATE_FRAGMENT_INTENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        Date currentDate = calendar.getTime();

        passData(currentDate);
    }
}