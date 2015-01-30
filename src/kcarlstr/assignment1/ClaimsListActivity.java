package kcarlstr.assignment1;

/**
 * Created by kylecarlstrom on 15-01-15.
 * 
 * List activity that shows all of the different claims
 */

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClaimsListActivity extends ListActivity implements ConfirmDeleteFragment.OnDataPass {
	
    private ArrayList<Claim> claims;
    public final static String CLAIM_CLICKED_INTENT = "com.kylecarlstrom.claim_clicked_intent";
    private ClaimsListArrayAdapter adapter;
    private int position_clicked;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        claims = ClaimsData.get(this).loadClaims();
        if (claims == null) {
        	claims = ClaimsData.get(this).getClaims();
        }
        adapter = new ClaimsListArrayAdapter(this, claims);
        ListView lv = getListView();
        /*
         * Delete Claim Dialog on Long Click --------------------------------------
         */
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ConfirmDeleteFragment dialog = new ConfirmDeleteFragment();
                dialog.show(getFragmentManager(), "confirmDialog");
                position_clicked = position;
                return true;
            }
        });
        setListAdapter(adapter);
    }

    /*
     * Claim in list clicked, goes to claim view ----------------------------
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getBaseContext(), ExpenseListActivity.class);
        intent.putExtra(CLAIM_CLICKED_INTENT, position);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Collections.sort(claims, new ClaimComparator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.claims_list_view_menu, menu);
        return true;
    }
    
    // Saves the claim data to a file in on pause
    @Override
    public void onPause() {
    	super.onPause();
    	ClaimsData.get(this).saveClaims();
    }

    /*
     * Add claim button-------------------------------------------
     */
    public void addClaim(MenuItem item) {
        Claim newClaim = new Claim();
        ArrayList<Claim> claimsList = ClaimsData.get(getApplicationContext()).getClaims();
        claimsList.add(newClaim);
        Intent intent = new Intent(getBaseContext(), ExpenseListActivity.class);
        intent.putExtra(ClaimsListActivity.CLAIM_CLICKED_INTENT, claimsList.size()-1);
        startActivity(intent);
    }

    /*
     * Receives data from the Delete Confirmation Dialog -----------------
     */
    @Override
    public void onDialogPass(boolean data) {
        if (data) {
            claims.remove(position_clicked);
        }
        adapter.notifyDataSetChanged();
    }
}