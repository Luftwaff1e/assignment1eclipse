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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kylecarlstrom on 15-01-16.
 */
public class ExpenseListActivity extends ListActivity implements DatePickerFragment.OnDataPass,
        ConfirmDeleteFragment.OnDataPass {

    public static final String EXPENSE_CLICKED_INTENT = "expense_clicked_intent";
    public static final String CLAIM_STATUS_INTENT = "claim_status_intent";
    private ArrayList<Expense> expenses;
    private int position_clicked;
    private int expense_clicked;
    private boolean is_start_date = false;
    private SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy");

    ExpenseListAdapter adapter;
    TextView titleTextView;
    Claim current_claim;
    Button startDateButton;
    Button endDateButton;
    TextView claimStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_list_view_layout);
        titleTextView = (EditText) findViewById(R.id.claim_title);
        claimStatusTextView = (TextView) findViewById(R.id.claim_status);
        startDateButton = (Button) findViewById(R.id.start_date_button);
        endDateButton = (Button) findViewById(R.id.end_date_button);

        // Get the position that was clicked from the extras on the intent
        Bundle extras = getIntent().getExtras();
        position_clicked = extras.getInt(ClaimsListActivity.CLAIM_CLICKED_INTENT);
        current_claim = ClaimsData.get(getApplicationContext()).getClaims().get(position_clicked);
        expenses = current_claim.getExpenses();

        if (current_claim.getClaimDescription() == null) {
            titleTextView.setHint("Claim description");
        } else {
            titleTextView.setText(current_claim.getClaimDescription());
        }
        claimStatusTextView.setText(current_claim.getProgress());
        startDateButton.setText(sf.format(current_claim.getStartDate()));
        endDateButton.setText(sf.format(current_claim.getEndDate()));


        titleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                current_claim.setClaimDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date = true;
                Bundle bundle = new Bundle();
                bundle.putSerializable(DatePickerFragment.DATE_FRAGMENT_INTENT, current_claim.getStartDate());
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_start_date = false;
                Bundle bundle = new Bundle();
                bundle.putSerializable(DatePickerFragment.DATE_FRAGMENT_INTENT, current_claim.getEndDate());
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ExpenseEditActivity.class);
                intent.putExtra(ClaimsListActivity.CLAIM_CLICKED_INTENT, position_clicked);
                intent.putExtra(EXPENSE_CLICKED_INTENT, position);
                intent.putExtra(CLAIM_STATUS_INTENT, current_claim.getProgress());
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ConfirmDeleteFragment dialog = new ConfirmDeleteFragment();
                dialog.show(getFragmentManager(), "confirmDialog");
                expense_clicked = position;
                return true;
            }
        });


        adapter = new ExpenseListAdapter(this, expenses);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        String progress = current_claim.getProgress();
            if (progress.equals("Submitted")) {
            	titleTextView.setFocusable(false);
                startDateButton.setEnabled(false);
                endDateButton.setEnabled(false);
            } else if (progress.equals("Returned")) {
                titleTextView.setFocusable(true);
                startDateButton.setEnabled(true);
                endDateButton.setEnabled(true);
            } else if (progress.equals("Approved")) {
                titleTextView.setEnabled(false);
                startDateButton.setEnabled(false);
                endDateButton.setEnabled(false);
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expense_list_view_menu, menu);
        MenuItem addExpenseButton = menu.findItem(R.id.expense_add_action_bar_button);
        MenuItem deleteClaimButton = menu.findItem(R.id.claim_delete_action_bar_button);
        MenuItem submitClaimButton = menu.findItem(R.id.submit_claim_action_bar_button);
        MenuItem returnClaimButton = menu.findItem(R.id.return_claim_action_bar_button);
        MenuItem approveClaimButton = menu.findItem(R.id.approve_claim_action_bar_button);

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
                getActionBar().hide();
        }

        return true;
    }

    public void addExpense(MenuItem item) {
        Expense newExpense = new Expense();
        current_claim.getExpenses().add(newExpense);
        Intent intent = new Intent(getBaseContext(), ExpenseEditActivity.class);
        intent.putExtra(ClaimsListActivity.CLAIM_CLICKED_INTENT, position_clicked);
        intent.putExtra(EXPENSE_CLICKED_INTENT, current_claim.getExpenses().size()-1);
        intent.putExtra(CLAIM_STATUS_INTENT, current_claim.getProgress());
        startActivity(intent);
    }

    @Override
    public void onDataPass(Date data) {
        if (is_start_date) {
            current_claim.setStartDate(data);
            startDateButton.setText(sf.format(data));
        } else {
            current_claim.setEndDate(data);
            endDateButton.setText(sf.format(data));
        }
    }


    public void submitClaim(MenuItem item) {
        current_claim.setProgress("Submitted");
        claimStatusTextView.setText("Submitted");
        titleTextView.setFocusable(false);
        startDateButton.setEnabled(false);
        endDateButton.setEnabled(false);
        invalidateOptionsMenu();
    }

    public void returnClaim(MenuItem item) {
        current_claim.setProgress("Returned");
        claimStatusTextView.setText("Returned");
        titleTextView.setFocusableInTouchMode(true);
        startDateButton.setEnabled(true);
        endDateButton.setEnabled(true);
        invalidateOptionsMenu();
    }

    public void approveClaim(MenuItem item) {
        current_claim.setProgress("Approved");
        claimStatusTextView.setText("Approved");
        titleTextView.setFocusable(false);
        startDateButton.setEnabled(false);
        endDateButton.setEnabled(false);
        invalidateOptionsMenu();
    }

    public void deleteClaim(MenuItem item) {
        ClaimsData.get(getApplicationContext()).getClaims().remove(current_claim);
        finish();
    }

    @Override
    public void onDialogPass(boolean data) {
        if (data) {
            expenses.remove(expense_clicked);
        }
        adapter.notifyDataSetChanged();
    }
}