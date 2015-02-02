package kcarlstr.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by kylecarlstrom on 15-01-16.
 * 
 * Adapter for the ExpenseListActiviy class, fairly standard
 * 
 * Copyright 2015 Kyle Carlstrom kcarlstr@ualberta.ca Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
public class ExpenseListAdapter extends ArrayAdapter<Expense> {
	
    private final Context context;
    private List<Expense> expenses;

    
    public ExpenseListAdapter(Context context, List<Expense> objects) {
        super(context, R.layout.expense_list_layout, objects);
        this.context = context;
        this.expenses = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for each row in the list
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.expense_list_layout, parent, false);

        // Get reference to the text views found in the layout
        TextView descriptionTextView = (TextView) v.findViewById(R.id.description_expense);
        TextView amountSpentTextView = (TextView) v.findViewById(R.id.amount_spent_expense);
        TextView dateTextView = (TextView) v.findViewById(R.id.date_text_expense);

        // Display the amount in the proper format for that currency
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setCurrency(expenses.get(position).getCurrency());
        amountSpentTextView.setText(nf.format(expenses.get(position).getAmount()));
        final SimpleDateFormat sf = new SimpleDateFormat("MMMM dd, yyyy");
        dateTextView.setText(sf.format(expenses.get(position).getDate()));


        // Set the text for the textview
        descriptionTextView.setText(expenses.get(position).getDescription());

        // Return the view that was generated
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}