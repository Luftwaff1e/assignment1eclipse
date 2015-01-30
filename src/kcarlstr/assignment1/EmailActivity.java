package kcarlstr.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Simple activity that allows a user to enter in a recipient and a subject
 * and send a claim
 */

public class EmailActivity extends Activity {

	private EditText recipientEditText;
	private EditText subjectEditText;
	private EditText claimStringEditText;
	private Button sendEmailButton;
	
	private String recipient;
	private String subject;
	private String claimString;
	private Claim currentClaim;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_view_layout);
		
		// Gets the currently clicked claim from the bundle that was passed in
		Bundle extras = getIntent().getExtras();
        int position_clicked = extras.getInt(ClaimsListActivity.CLAIM_CLICKED_INTENT);
        currentClaim = ClaimsData.get(getApplicationContext()).getClaims().get(position_clicked);
        claimString = currentClaim.toString();
		
        // Get references to the different widgets
		recipientEditText = (EditText) findViewById(R.id.recipient_edit_text);
		subjectEditText = (EditText) findViewById(R.id.subject_edit_text);
		claimStringEditText = (EditText) findViewById(R.id.claims_string_edit_text);
		sendEmailButton = (Button) findViewById(R.id.send_email_button);
		claimStringEditText.setText(claimString);
		
		/*
		 * Recipient text field extraction --------------------------
		 */
		recipientEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				recipient = s.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*
		 * Subject text field extraction ------------------------------
		 */
		subjectEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				subject = s.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*
		 * Claim text field extraction ---------------------------------
		 */
		claimStringEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claimString = s.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*
		 * Send email button that will open up a
		 */
		sendEmailButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				// http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application 2015-01-30
				i.setType("message/rfc822"); // Only shows actual email clients
				i.putExtra(Intent.EXTRA_EMAIL, recipient);
				i.putExtra(Intent.EXTRA_SUBJECT, subject);
				i.putExtra(Intent.EXTRA_TEXT, claimString);
				try {
					startActivity(Intent.createChooser(i, "Send claim"));
				} catch (android.content.ActivityNotFoundException e) {
					Toast.makeText(EmailActivity.this, "Email client not found", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
		
		
	}

	
}
