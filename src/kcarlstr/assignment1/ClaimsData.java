package kcarlstr.assignment1;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kylecarlstrom on 15-01-16.
 */
public class ClaimsData {
    // Singleton class, general outline of the code comes from "Android Programming: The Big Nerd Ranch Guide"
    private static ClaimsData sClaimsData;
    private Context appContext;

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

    private void saveClaims() {

    }
}