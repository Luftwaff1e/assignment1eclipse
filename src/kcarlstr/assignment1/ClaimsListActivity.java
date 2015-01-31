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
    private int positionClicked;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getClaims(savedInstanceState);
        setUpListeners();
        setUpAdapter();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.claims_list_view_menu, menu);
        return true;
    }

	@Override
	protected void onResume() {
		super.onResume();
		sortClaims();
        adapter.notifyDataSetChanged();
	}
	
    @Override
    public void onPause() {
    	super.onPause();
    	ClaimsData.get(this).saveClaims();
    }

	private void getClaims(Bundle savedInstanceState) {
    	claims = ClaimsData.get(this).loadClaims();
        if (claims == null) {
        	claims = ClaimsData.get(this).getClaims();
        }
    }
    
    private void setUpListeners() {
    	ListView lv = getListView();
    	
    	lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getBaseContext(), ExpenseListActivity.class);
		        intent.putExtra(CLAIM_CLICKED_INTENT, position);
		        startActivity(intent);
			}
		});
    	
    	lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ConfirmDeleteFragment dialog = new ConfirmDeleteFragment();
                dialog.show(getFragmentManager(), "confirmDialog");
                positionClicked = position;
                return true;
            }
        });
    }
    
    private void setUpAdapter() {
        adapter = new ClaimsListArrayAdapter(this, claims);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    
    private void sortClaims() {
    	Collections.sort(claims, new ClaimComparator());
    }

    public void addClaim(MenuItem item) {
        Claim newClaim = new Claim();
        ArrayList<Claim> claimsList = ClaimsData.get(getApplicationContext()).getClaims();
        claimsList.add(newClaim);
        Intent intent = new Intent(getBaseContext(), ExpenseListActivity.class);
        intent.putExtra(ClaimsListActivity.CLAIM_CLICKED_INTENT, claimsList.size()-1);
        startActivity(intent);
    }

    @Override
    public void onDialogPass(boolean data) {
        if (data) {
            claims.remove(positionClicked);
        }
        adapter.notifyDataSetChanged();
    }
}