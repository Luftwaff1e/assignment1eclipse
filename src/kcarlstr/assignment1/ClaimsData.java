package kcarlstr.assignment1;

import android.content.Context;
import android.renderscript.Element.DataType;

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
 */
public class ClaimsData {
    // Singleton class, general outline of the code comes from "Android Programming: The Big Nerd Ranch Guide"
    private static ClaimsData sClaimsData;
    private Context appContext;
    private String FILENAME = "file.sav";

    private ArrayList<Claim> claims;

    private ClaimsData(Context appContext) {
        this.appContext = appContext;
        claims = new ArrayList<Claim>();
    }

    public static ClaimsData get(Context c) {
        if (sClaimsData == null) {
            sClaimsData = new ClaimsData(c.getApplicationContext());
        }
        return sClaimsData;
    }

    public ArrayList<Claim> getClaims() {
        return claims;
    }

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