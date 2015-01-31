package kcarlstr.assignment1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

/**
 * Created by kylecarlstrom on 15-01-15.
 */
public class ExpenseEditActivity extends Activity implements DatePickerFragment.OnDataPass {

    private int expenseClicked;
    private int claimClicked;

    private double amountInDollars;
    private String claimProgress;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy");
    private Expense expense;
    private Expense previous_expense;
    private Claim claim;


    private EditText amountSpentEditText;
    private Button dateButtonExpense;
    private Spinner currencySpinner;
    private EditText descriptionEditText;
    private Spinner categoriesSpinner;
    
    ArrayAdapter<CharSequence> currencyAdapter;
    ArrayAdapter<CharSequence> categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_edit_layout);

        getViews();

        getClaimAndExpense();
        
        inflateActionBar();
        
        setEditable();
        
        setFieldValues();
        
        setListeners();

    }
    
    private void getViews() {
    	// Get references to the widgets
        amountSpentEditText = (EditText) findViewById(R.id.amount_spent_edit_text);
        dateButtonExpense = (Button) findViewById(R.id.date_button_expense);
        currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
        descriptionEditText = (EditText) findViewById(R.id.description_edit_text_expense);
        categoriesSpinner = (Spinner) findViewById(R.id.category_spinner);
    }
    
    private void setEditable() {
    	if (claimProgress.equals("Submitted") || claimProgress.equals("Approved")) {
            getActionBar().hide();
            amountSpentEditText.setFocusable(false);
            dateButtonExpense.setEnabled(false);
            currencySpinner.setEnabled(false);
            descriptionEditText.setFocusable(false);
            categoriesSpinner.setEnabled(false);
        }
    }
    
    private void getClaimAndExpense() {
    	// Get the expense from the bundle
        Bundle extras = getIntent().getExtras();
        expenseClicked = extras.getInt(ExpenseListActivity.EXPENSE_CLICKED_INTENT);
        claimClicked = extras.getInt(ClaimsListActivity.CLAIM_CLICKED_INTENT);
        claimProgress = extras.getString(ExpenseListActivity.CLAIM_STATUS_INTENT);

        claim = ClaimsData.get(getApplicationContext()).getClaims().get(claimClicked);
        expense = claim.getExpenses().get(expenseClicked);
        
        // Invoke the copy constructor to make a clone of the original expense
        // This is done in case the user hits cancel so the changes can be reverted
        previous_expense = new Expense(expense);
    }
    
    private void setFieldValues() {
    	amountInDollars = expense.getAmount();
        amountSpentEditText.setHint(df.format(amountInDollars));
        dateButtonExpense.setText(sf.format(expense.getDate()));
        descriptionEditText.setText(expense.getDescription());

        currencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.currency_string,
                android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        currencySpinner.setSelection(currencyAdapter.getPosition(expense.getCurrency().toString()));

        categoriesAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_string,
                android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setSelection(categoriesAdapter.getPosition(expense.getCategory()));
    }
    
    private void setListeners() {
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
                try {
                	amountInDollars = Double.parseDouble(amountSpentString);
                } catch (NumberFormatException nfe) {
                	amountInDollars = 0.0;
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
        if (expense.getDescription().equals("")) {
        	claim.getExpenses().remove(expense);
        }
        super.onBackPressed();
    }
    
    

    @Override
	protected void onPause() {
		super.onPause();
		ClaimsData.get(this).saveClaims();
	}

	private void inflateActionBar() {
    /*
     * https://android.googlesource.com/platform/developers/samples/android/+/master/ui/actionbar/DoneBar/Application/src/main/java/com/example/android/donebar/DoneBarActivity.java
     * 2015-01-25
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
                    if (expense.getDescription().equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter a description",
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