package kcarlstr.assignment1;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kylecarlstrom on 15-01-16.
 * 
 * Basically an overview of the claim, will list all of the different expenses and some info about them
 * 
 * Copyright 2015 Kyle Carlstrom kcarlstr@ualberta.ca Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
public class ExpenseListActivity extends ListActivity implements DatePickerFragment.OnDataPass,
        ConfirmDeleteFragment.OnDataPass {

    public static final String EXPENSE_CLICKED_INTENT = "com.kylecarlstrom.expense_clicked_intent";
    public static final String CLAIM_STATUS_INTENT = "com.kylecarlstrom.claim_status_intent";
    private ArrayList<Expense> expenses;
    private int positionClicked;
    private int expenseClicked;
    private boolean isStartDate = false;
    private final SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy");

    private ExpenseListAdapter adapter;
    private TextView titleTextView;
    private Claim current_claim;
    private Button startDateButton;
    private Button endDateButton;
    private TextView claimStatusTextView;
    private ProgressBar claimProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_list_view_layout);

        getClaimAndExpenses();
        
        getViews();
        
        setDefaultView();
        
        setListeners();
        
        adapter = new ExpenseListAdapter(this, expenses);
        setListAdapter(adapter);
    }
    
    private void getViews() {
    	// Get references to the widgets
        titleTextView = (EditText) findViewById(R.id.claim_title);
        claimStatusTextView = (TextView) findViewById(R.id.claim_status);
        startDateButton = (Button) findViewById(R.id.start_date_button);
        endDateButton = (Button) findViewById(R.id.end_date_button);
        claimProgress = (ProgressBar) findViewById(R.id.claim_progress_bar);
    }
    
    private void getClaimAndExpenses() {
    	// Get the position that was clicked from the extras on the intent
        Bundle extras = getIntent().getExtras();
        positionClicked = extras.getInt(ClaimsListActivity.CLAIM_CLICKED_INTENT);
        current_claim = ClaimsData.get(getApplicationContext()).getClaims().get(positionClicked);
        expenses = current_claim.getExpenses();
    }
    
    private void setDefaultView() {
    	// Set the default claim info
        if (current_claim.getClaimDescription() == null) {
            titleTextView.setHint("Claim description");
        } else {
            titleTextView.setText(current_claim.getClaimDescription());
        }
        claimStatusTextView.setText(current_claim.getProgress());
        startDateButton.setText(sf.format(current_claim.getStartDate()));
        endDateButton.setText(sf.format(current_claim.getEndDate()));
        claimProgress.setProgress(0);
    }
    
    private void setListeners() {
    	// Set up the listeners
        titleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            	// Intentionally blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                current_claim.setClaimDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            	// Intentionally blank
            }
        });
        
        // Start a date picker dialog on-click
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = true;
                Bundle bundle = new Bundle();
                bundle.putSerializable(DatePickerFragment.DATE_FRAGMENT_INTENT, current_claim.getStartDate());
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });
        
        // Start a date picker dialog on-click
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                Bundle bundle = new Bundle();
                bundle.putSerializable(DatePickerFragment.DATE_FRAGMENT_INTENT, current_claim.getEndDate());
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        ListView lv = getListView();
        // Tapping the expense in the list will go to the expense
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	// On item click start a new activity that shows the corresponding expense
                Intent intent = new Intent(getBaseContext(), ExpenseEditActivity.class);
                intent.putExtra(ClaimsListActivity.CLAIM_CLICKED_INTENT, positionClicked);
                intent.putExtra(EXPENSE_CLICKED_INTENT, position);
                intent.putExtra(CLAIM_STATUS_INTENT, current_claim.getProgress());
                startActivity(intent);
            }
        });
        // Long press on item will bring up a dialog to allow you to delete the item
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	if (current_claim.getProgress().equals("Submitted") || current_claim.getProgress().equals("Approved")) {
            		Toast.makeText(getBaseContext(), "Cannot currently delete expense items", Toast.LENGTH_SHORT).show();
            		return true;
            	}
                ConfirmDeleteFragment dialog = new ConfirmDeleteFragment();
                dialog.show(getFragmentManager(), "confirmDialog");
                expenseClicked = position;
                return true;
            }
        });
    }
    
    private void setEnabledOrNot() {
    	// Change what is editable based on the claims progress
        String progress = current_claim.getProgress();
            if (progress.equals("Submitted")) {
            	titleTextView.setFocusable(false);
                startDateButton.setEnabled(false);
                endDateButton.setEnabled(false);
                claimProgress.setProgress(50);
            } else if (progress.equals("Returned")) {
                titleTextView.setFocusable(true);
                startDateButton.setEnabled(true);
                endDateButton.setEnabled(true);
                claimProgress.setProgress(0);
            } else if (progress.equals("Approved")) {
                titleTextView.setEnabled(false);
                startDateButton.setEnabled(false);
                endDateButton.setEnabled(false);
                claimProgress.setProgress(100);
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        
        setEnabledOrNot();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expense_list_view_menu, menu);
        
        // Get references to each of the buttons in the menu bar
        MenuItem addExpenseButton = menu.findItem(R.id.expense_add_action_bar_button);
        MenuItem deleteClaimButton = menu.findItem(R.id.claim_delete_action_bar_button);
        MenuItem submitClaimButton = menu.findItem(R.id.submit_claim_action_bar_button);
        MenuItem returnClaimButton = menu.findItem(R.id.return_claim_action_bar_button);
        MenuItem approveClaimButton = menu.findItem(R.id.approve_claim_action_bar_button);

        // Choose which buttons show up depending on the current status of the claim
        String progress = current_claim.getProgress();
        if (progress.equals("In Progress")) {
                returnClaimButton.setVisible(false);
                approveClaimButton.setVisible(false);
        } else if (progress.equals("Submitted")) {
                returnClaimButton.setVisible(true);
                approveClaimButton.setVisible(true);
                addExpenseButton.setVisible(false);
                deleteClaimButton.setVisible(false);
                submitClaimButton.setVisible(false);
        } else if (progress.equals("Returned")) {
                addExpenseButton.setVisible(true);
                deleteClaimButton.setVisible(true);
                returnClaimButton.setVisible(false);
                approveClaimButton.setVisible(false);
                submitClaimButton.setVisible(true);
        } else {
        	returnClaimButton.setVisible(false);
            approveClaimButton.setVisible(false);
            addExpenseButton.setVisible(false);
            deleteClaimButton.setVisible(false);
            submitClaimButton.setVisible(false);
        }

        return true;
    }
    
    // Used to save data to a file
    @Override
	protected void onPause() {
		super.onPause();
		ClaimsData.get(this).saveClaims();
	}

	// Called when the user presses the "Add Expense" menu button
    // Creates a new expense and then passes it to a new activity so the user can edit it
    public void addExpense(MenuItem item) {
        Expense newExpense = new Expense();
        current_claim.getExpenses().add(newExpense);
        Intent intent = new Intent(getBaseContext(), ExpenseEditActivity.class);
        intent.putExtra(ClaimsListActivity.CLAIM_CLICKED_INTENT, positionClicked);
        intent.putExtra(EXPENSE_CLICKED_INTENT, current_claim.getExpenses().size()-1);
        intent.putExtra(CLAIM_STATUS_INTENT, current_claim.getProgress());
        startActivity(intent);
    }


    // Menu button used to submit the claim, once submitted no further edits are allowed
    public void submitClaim(MenuItem item) {
        current_claim.setProgress("Submitted");
        claimStatusTextView.setText("Submitted");
        titleTextView.setFocusable(false);
        startDateButton.setEnabled(false);
        endDateButton.setEnabled(false);
        invalidateOptionsMenu();
        claimProgress.incrementProgressBy(50);;// Used to redraw the action bar
    }

    // Menu button used to return the claim, now the claim is editable again.
    public void returnClaim(MenuItem item) {
        current_claim.setProgress("Returned");
        claimStatusTextView.setText("Returned");
        titleTextView.setFocusableInTouchMode(true);
        startDateButton.setEnabled(true);
        endDateButton.setEnabled(true);
        invalidateOptionsMenu();
        claimProgress.incrementProgressBy(-50);;
    }

    // Menu button used to approve the claim, one approved the claim is no longer editable
    public void approveClaim(MenuItem item) {
        current_claim.setProgress("Approved");
        claimStatusTextView.setText("Approved");
        titleTextView.setFocusable(false);
        startDateButton.setEnabled(false);
        endDateButton.setEnabled(false);
        invalidateOptionsMenu();
        claimProgress.incrementProgressBy(50);;
    }

    // Deletes the claim
    public void deleteClaim(MenuItem item) {
        ClaimsData.get(getApplicationContext()).getClaims().remove(current_claim);
        finish();
    }
    
    public void emailClaim(MenuItem item) {
    	Intent i = new Intent(getApplicationContext(), EmailActivity.class);
    	i.putExtra(ClaimsListActivity.CLAIM_CLICKED_INTENT, positionClicked);
    	startActivity(i);
    }
    
    // Interface to pass back the data from the DatePickerDialog
    @Override
    public void onDataPass(Date data) {
        if (isStartDate) {
            current_claim.setStartDate(data);
            startDateButton.setText(sf.format(data));
            if (current_claim.getStartDate().after(current_claim.getEndDate())) {
            	current_claim.setEndDate(current_claim.getStartDate());
            	endDateButton.setText(sf.format(data));
            }
        } else {
            current_claim.setEndDate(data);
            endDateButton.setText(sf.format(data));
            if (current_claim.getEndDate().before(current_claim.getStartDate())) {
            	current_claim.setStartDate(current_claim.getEndDate());
            	startDateButton.setText(sf.format(data));
            }
        }
    }

    // Used to pass back data from the deletion confirmation dialog
    @Override
    public void onDialogPass(boolean data) {
        if (data) {
            expenses.remove(expenseClicked);
        }
        adapter.notifyDataSetChanged();
    }
}