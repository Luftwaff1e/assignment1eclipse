package kcarlstr.assignment1;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kylecarlstrom on 15-01-15.
 */
public class ClaimsListArrayAdapter extends ArrayAdapter<Claim> {
    private final Context context;
    private List<Claim> claims;
    private Claim claim;
    private Map<String, Double> currencyAmounts;
    NumberFormat nf = NumberFormat.getCurrencyInstance();

    private TextView destinationTextView;
    private TextView claimStatusTextView;
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
        // Get inflater object
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflate the xml file "claims_list_layout" which gives the view for the list items
        View v = inflater.inflate(R.layout.claims_list_layout, parent, false);

        destinationTextView = (TextView) v.findViewById(R.id.destination);
        claimStatusTextView = (TextView) v.findViewById(R.id.claim_status_text_view);
        cadTextView = (TextView) v.findViewById(R.id.expense_amount_cad);
        usdTextView = (TextView) v.findViewById(R.id.expense_amount_usd);
        eurTextView = (TextView) v.findViewById(R.id.expense_amount_eur);
        gbpTextView = (TextView) v.findViewById(R.id.expense_amount_gbp);
        claimProgress = (ProgressBar) v.findViewById(R.id.claim_progress_bar_list);

        // Set the text for the textview
        claim = claims.get(position);
        destinationTextView.setText(claim.getClaimDescription());
        claimStatusTextView.setText(claim.getProgress());

        // Set the different currency amounts
        currencyAmounts = claim.getCurrencyAmounts();
        nf.setCurrency(Currency.getInstance("CAD"));
        cadTextView.setText(nf.format(currencyAmounts.get("CAD")));
        nf.setCurrency(Currency.getInstance("USD"));
        usdTextView.setText(nf.format(currencyAmounts.get("USD")));
        nf.setCurrency(Currency.getInstance("EUR"));
        eurTextView.setText(nf.format(currencyAmounts.get("EUR")));
        nf.setCurrency(Currency.getInstance("GBP"));
        gbpTextView.setText(nf.format(currencyAmounts.get("GBP")));
        
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

        // Return the view that was generated
        return v;
    }
}