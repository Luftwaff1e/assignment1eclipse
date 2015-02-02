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
 * 
 * Hosts a datePickerDialog and allows the user to easily enter dates
 * 
 * Much of the interface code to pass data back was based off of an answer on stack overflow.
 * It can be found here: http://stackoverflow.com/questions/9343241/passing-data-between-a-fragment-and-its-container-activity 2015-01-31
 * 
 * Copyright 2015 Kyle Carlstrom kcarlstr@ualberta.ca Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
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

    // Creates a datePickerDialog from the current date
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

    // When the user sets the date this will pass that date back to the activity that is hosting
    // this fragment through the onDataPass interface
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        Date currentDate = calendar.getTime();

        passData(currentDate);
    }
}