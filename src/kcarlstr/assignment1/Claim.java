package kcarlstr.assignment1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kylecarlstrom on 15-01-15.
 * 
 * This class holds the information about a claim and its related expenses
 * Most of the member variables are initialized for a new claim so that the user
 * could have defaults.
 * 
 * Copyright 2015 Kyle Carlstrom kcarlstr@ualberta.ca Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 */
public class Claim {
	
    private String progress;
    private Date startDate;
    private Date endDate;
    private String claimDescription;
    private ArrayList<Expense> expenses;
    private Map<String, Double> currencyAmounts = new HashMap<String, Double>();

    public Claim() {
        this.startDate = new Date();
        this.endDate = new Date(startDate.getTime() + (1000 * 60 * 60 * 24));
        this.claimDescription = null;
        this.expenses = new ArrayList<Expense>();
        this.progress = "In Progress";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getClaimDescription() {
        return claimDescription;
    }

    public void setClaimDescription(String claimDescription) {
        this.claimDescription = claimDescription;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }


    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    // Updates the amounts of each currency to display in the claims list view
    private void updateCurrencyAmounts() {
        currencyAmounts.clear();
        currencyAmounts.put("CAD", 0.0);
        currencyAmounts.put("USD", 0.0);
        currencyAmounts.put("EUR", 0.0);
        currencyAmounts.put("GBP", 0.0);
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            String currencyCode = expense.getCurrency().getCurrencyCode();
            if (currencyAmounts.containsKey(currencyCode)) {
                double amount = currencyAmounts.get(currencyCode);
                amount += expense.getAmount();
                currencyAmounts.put(currencyCode, amount);
            } else {
                currencyAmounts.put(currencyCode, expense.getAmount());
            }
        }
        
    }
    
    public Map<String, Double> getCurrencyAmounts() {
        updateCurrencyAmounts();
        return currencyAmounts;
    }

	@Override
	public String toString() {
		String claim_text = "";
		claim_text += claimDescription + "\n";
		claim_text += "Start date: " + startDate.toLocaleString() + "\n";
		claim_text += "End date: " + endDate.toLocaleString() + "\n";
		for (int i = 0; i < expenses.size(); i++) {
			claim_text += "Expense " + String.valueOf(i) + "\n";
			claim_text += expenses.get(i).toString() + "\n";
		}
		return claim_text;
	}
    
    
    
}


// http://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property 2015-01-30
class ClaimComparator implements Comparator<Claim> {
    public int compare(Claim claim1, Claim claim2) {
        return claim1.getStartDate().compareTo(claim2.getStartDate());
    }
}