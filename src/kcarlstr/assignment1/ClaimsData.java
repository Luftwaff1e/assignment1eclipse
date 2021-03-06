package kcarlstr.assignment1;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by kylecarlstrom on 15-01-16.
 * 
 * ClaimsData is a singleton so you access the constructor by calling get()
 * If there was already an instance of the class it will return it, otherwise
 * it will create a new ArrayList of claims and return that.
 * 
 * Also implemented the saving to file and loading from file here because this class
 * acts as a bit of a controller and I thought this would be the best place to save/load.
 * 
 * Copyright 2015 Kyle Carlstrom kcarlstr@ualberta.ca Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
public class ClaimsData {

    private static ClaimsData sClaimsData;
    private Context appContext;
    private String FILENAME = "file.sav";
    private ArrayList<Claim> claims;

    // Private constructor only gets called if there is no instance of ClaimsData
    private ClaimsData(Context appContext) {
        this.appContext = appContext;
        claims = new ArrayList<Claim>();
    }

    // Returns the current instance of ClaimsData if it exists or it will call the constructor
    public static ClaimsData get(Context c) {
        if (sClaimsData == null) {
            sClaimsData = new ClaimsData(c.getApplicationContext());
        }
        return sClaimsData;
    }

    public ArrayList<Claim> getClaims() {
        return claims;
    }

    // Uses Gson to write the contents of all the claims and expenses to a file
    // Heavily influenced by lonelytwitter lab: https://github.com/kylecarlstrom/lonelyTwitter/commits/master 2015-01-31
    public void saveClaims() {
    	Gson gson = new Gson();
    	try {
			FileOutputStream fileOutputStream = appContext.openFileOutput(FILENAME, 0);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			gson.toJson(claims, outputStreamWriter);
			outputStreamWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    			
    }
    
    // Uses Gson to load the contents of the claims from the file.
    public ArrayList<Claim> loadClaims() {
    	Gson gson = new Gson();
    	try {
			FileInputStream fileInputStream = appContext.openFileInput(FILENAME);
			Type dataType = new TypeToken<ArrayList<Claim>>() {}.getType();
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			claims = gson.fromJson(inputStreamReader, dataType);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return claims;
    }
}