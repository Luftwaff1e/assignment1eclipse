package kcarlstr.assignment1;

import java.util.Currency;
import java.util.Date;

/*
 * Created by kylecarlstrom on 15-01-15.
 * 
 * Stores an expense and all information that is encompassed within an expense
 * 
 * Copyright 2015 Kyle Carlstrom kcarlstr@ualberta.ca Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */

public class Expense {
	private boolean is_new;
    private Date date;
    private String category;
    private String description;
    private double amount;
    private Currency currency;

    // Constructor has default values in case the user doesn't enter any, also prevents lots of crashes
    public Expense() {
        date = new Date();
        category = "Other";
        description = "";
        amount = 0;
        currency = Currency.getInstance("CAD");
        is_new = true;

    }

    // Copy constructor used to make an identical object that is not just a reference
    public Expense(Expense copy) {
        this.date = copy.getDate();
        this.category = new String(copy.getCategory());
        this.description = new String(copy.getDescription());
        this.amount = copy.getAmount();
        this.currency = copy.getCurrency();
        this.is_new = copy.getIs_new();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

	public boolean getIs_new() {
		return is_new;
	}

	public void setIs_new(boolean is_new) {
		this.is_new = is_new;
	}

	// Gives a representation that makes sense, useful for emailing
	@Override
	public String toString() {
		String expense_text = "";
		expense_text += description + "\n";
		expense_text += String.valueOf(amount) + "\n";
		expense_text += "Currency: " + currency.getCurrencyCode() + "\n";
		expense_text += date.toLocaleString() + "\n";
		expense_text += "Category: " + category + "\n";
		return expense_text;
 	}
	
	
}
