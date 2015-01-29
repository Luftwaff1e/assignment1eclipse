package kcarlstr.assignment1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

/**
 * Created by kylecarlstrom on 15-01-15.
 */
public class ExpenseEditActivity extends Activity implements DatePickerFragment.OnDataPass {

    private int expense_clicked;
    private int claim_clicked;
    private Claim claim;

    private double amountInDollars;
    private String claim_progress;
    private SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy");
    private DecimalFormat df = new DecimalFormat("#0.00");
    private Expense expense;
    private Expense previous_expense;


    EditText amountSpentEditText;
    Button dateButtonExpense;
    Spinner currencySpinner;
    EditText descriptionEditText;
    Spinner categoriesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_edit_layout);

        // Get references to the widgets
        amountSpentEditText = (EditText) findViewById(R.id.amount_spent_edit_text);
        dateButtonExpense = (Button) findViewById(R.id.date_button_expense);
        currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
        descriptionEditText = (EditText) findViewById(R.id.description_edit_text_expense);
        categoriesSpinner = (Spinner) findViewById(R.id.category_spinner);

        // Get the expense from the bundle
        Bundle extras = getIntent().getExtras();
        expense_clicked = extras.getInt(ExpenseListActivity.EXPENSE_CLICKED_INTENT);
        claim_clicked = extras.getInt(ClaimsListActivity.CLAIM_CLICKED_INTENT);
        claim_progress = extras.getString(ExpenseListActivity.CLAIM_STATUS_INTENT);
        
        // Inflates the "cancel/done" action bar
        inflateActionBar();

        // Get the current expense
        claim = ClaimsData.get(getApplicationContext()).getClaims().get(claim_clicked);
        expense = claim.getExpenses().get(expense_clicked);
        // Invoke the copy constructor to make a clone of the original expense
        // This is done in case the user hits cancel so the changes can be reverted
        previous_expense = new Expense(expense);


        // Sets the expense to be editable or not based on the claims status
        if (claim_progress.equals("Submitted") || claim_progress.equals("Approved")) {
            getActionBar().hide();
            amountSpentEditText.setFocusable(false);
            dateButtonExpense.setEnabled(false);
            currencySpinner.setEnabled(false);
            descriptionEditText.setFocusable(false);
            categoriesSpinner.setEnabled(false);
        }

        // Set default value of fields
        amountInDollars = expense.getAmount();
        amountSpentEditText.setHint(df.format(amountInDollars));
        dateButtonExpense.setText(sf.format(expense.getDate()));
        descriptionEditText.setText(expense.getDescription());

        // Set the adapter for the currency and categories spinners
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.currency_string,
                android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        currencySpinner.setSelection(currencyAdapter.getPosition(expense.getCurrency().toString()));

        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_string,
                android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setSelection(categoriesAdapter.getPosition(expense.getCategory()));

        
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense.setCurrency(Currency.getInstance(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense.setCategory(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                expense.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        amountSpentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amountSpentString = amountSpentEditText.getText().toString();
                if (amountSpentString.length() == 0) {
                    amountInDollars = 0.0;
                } else {
                    amountInDollars = Double.parseDouble(amountSpentString);
                }
                expense.setAmount(amountInDollars);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // On clicking the button a date picker dialog appears
        dateButtonExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(DatePickerFragment.DATE_FRAGMENT_INTENT, expense.getDate());
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

    }
    
    // Method to receive the date from the date picker dialog
    @Override
    public void onDataPass(Date data) {
        expense.setDate(data);
        dateButtonExpense.setText(sf.format(data)); // Since onResume() is not called, must update text manually
    }

    // If the expense is new and back is pressed then it doesn't save it
    @Override
    public void onBackPressed() {
        if (expense.getIs_new()) {
        	claim.getExpenses().remove(expense);
        }
        super.onBackPressed();
    }

    private void inflateActionBar() {
    // https://android.googlesource.com/platform/developers/samples/android/+/master/ui/actionbar/DoneBar/Application/src/main/java/com/example/android/donebar/DoneBarActivity.java
    // 2015-01-25
    /*
*       Copyright 2013 The Android Open Source Project
*
*       Licensed under the Apache License, Version 2.0 (the "License");
*       you may not use this file except in compliance with the License.
*        You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*        Unless required by applicable law or agreed to in writing, software
*        distributed under the License is distributed on an "AS IS" BASIS,
*        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*        See the License for the specific language governing permissions and
*        limitations under the License.
*/

    // BEGIN_INCLUDE (inflate_set_custom_view)
    // Inflate a "Done/Cancel" custom action bar view.
    final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
            .getSystemService(LAYOUT_INFLATER_SERVICE);
    final View customActionBarView = inflater.inflate(
            R.layout.actionbar_custom_view_done_cancel, null);
    customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expense.getDescription().equals("") || expense.getAmount() == 0) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter a description and amount",
                                Toast.LENGTH_SHORT).show();
                    } else {
                    	expense.setIs_new(false);
                        finish();
                    }
                }
            });
    customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expense.getIs_new()) {
                    	claim.getExpenses().remove(expense);
                    } else {
                    	claim.getExpenses().remove(expense);
                    	claim.getExpenses().add(previous_expense);
                    }
                    finish();
                }
            });
    // Show the custom action bar view and hide the normal Home icon and title.
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayOptions(
            ActionBar.DISPLAY_SHOW_CUSTOM,
            ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_SHOW_TITLE);
    actionBar.setCustomView(customActionBarView,
            new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
                    // END_INCLUDE (inflate_set_custom_view)
	}
}