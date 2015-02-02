package kcarlstr.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;
import java.util.Map;

/**
 * Created by kylecarlstrom on 15-01-15.
 * 
 * Adapter for CLaimsListActivity
 * 
 * Copyright 2015 Kyle Carlstrom kcarlstr@ualberta.ca Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
public class ClaimsListArrayAdapter extends ArrayAdapter<Claim> {
	
    private final Context context;
    private List<Claim> claims;
    private Claim claim;
    private Map<String, Double> currencyAmounts;

    private TextView destinationTextView;
    private TextView claimStatusTextView;
    private TextView claimDateTextView;
    private TextView cadTextView;
    private TextView usdTextView;
    private TextView eurTextView;
    private TextView gbpTextView;
    private ProgressBar claimProgress;


    public ClaimsListArrayAdapter(Context context, List<Claim> objects) {
        super(context, R.layout.claims_list_layout, objects);
        this.context = context;
        this.claims = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.claims_list_layout, parent, false);
        getViews(v);      
        setFields(position);

        // Return the view that was generated
        return v;
    }
    
    private void getViews(View v) {
    	destinationTextView = (TextView) v.findViewById(R.id.destination);
        claimStatusTextView = (TextView) v.findViewById(R.id.claim_status_text_view);
        claimDateTextView = (TextView) v.findViewById(R.id.claim_date_list);
        
        cadTextView = (TextView) v.findViewById(R.id.expense_amount_cad);
        usdTextView = (TextView) v.findViewById(R.id.expense_amount_usd);
        eurTextView = (TextView) v.findViewById(R.id.expense_amount_eur);
        gbpTextView = (TextView) v.findViewById(R.id.expense_amount_gbp);
        claimProgress = (ProgressBar) v.findViewById(R.id.claim_progress_bar_list);
    }
    
    private void setFields(int position) {
    	claim = claims.get(position);
        destinationTextView.setText(claim.getClaimDescription());
        claimStatusTextView.setText(claim.getProgress());
        final SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy");
        claimDateTextView.setText(sf.format(claim.getStartDate()));
        
        // Formats the amount by the currency instance
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        currencyAmounts = claim.getCurrencyAmounts();
        nf.setCurrency(Currency.getInstance("CAD"));
        cadTextView.setText(nf.format(currencyAmounts.get("CAD")));
        nf.setCurrency(Currency.getInstance("USD"));
        usdTextView.setText(nf.format(currencyAmounts.get("USD")));
        nf.setCurrency(Currency.getInstance("EUR"));
        eurTextView.setText(nf.format(currencyAmounts.get("EUR")));
        nf.setCurrency(Currency.getInstance("GBP"));
        gbpTextView.setText(nf.format(currencyAmounts.get("GBP")));
        
        // Adjusts the progress bar
        String progress = claim.getProgress();
        if (progress.equals("Submitted")) {
            claimProgress.setProgress(50);
        } else if (progress.equals("Returned")) {
            claimProgress.setProgress(0);
        } else if (progress.equals("Approved")) {
            claimProgress.setProgress(100);
        } else {
        	claimProgress.setProgress(0);
        }
    }
}