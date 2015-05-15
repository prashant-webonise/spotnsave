package com.epicelements.spotnsave;

 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;




public class ProfileEdit extends SherlockFragmentActivity implements OnClickListener
	{
		 
    final Activity activity = this;
    
    Dialog levelDialog;
   
    private EditText firstName, lastName, dateofBirth, Gender, Phonenumber, Country;
	

    
	 // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/register.php";

    //testing on Emulator:
   // private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/register.php";

  //testing from a real server:
    private static final String LOGIN_URL = "http://www.konnectc.com/spotnsave/read_profile.php";
    
    private static final String UPDATE_URL = "http://www.konnectc.com/spotnsave/edit_profile.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_DATEOFBIRTH = "dateofbirth";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_COUNTRY = "country";
    private static final String TAG_MESSAGE = "message";
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileedit);

// get extras (email address) from previous activity
        
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    //    Editor editor = pref.edit();
     // returns stored preference value
     // If value is not present return second param value - In this case null
        
//     String emailpref = pref.getString("email", null); // getting String
        
        
   
		firstName = (EditText)findViewById(R.id.editText1);
		lastName = (EditText)findViewById(R.id.editText2);
		Gender = (EditText)findViewById(R.id.editText8);
		Country = (EditText)findViewById(R.id.editText7);
		Phonenumber = (EditText)findViewById(R.id.editText6);
		dateofBirth = (EditText)findViewById(R.id.editText3);

		((ImageButton)this.findViewById(R.id.imageButton2)).setOnClickListener(mRegisterlistener);

        
      ((EditText)this.findViewById(R.id.editText3)).setOnClickListener(birthclicklistener);
      ((EditText)this.findViewById(R.id.editText8)).setOnClickListener(genderlistener);
      ((EditText)this.findViewById(R.id.editText7)).setOnClickListener(countrylistener);

	}


    private OnClickListener mRegisterlistener = new OnClickListener() {
    	public void onClick(View v) {
		// TODO Auto-generated method stub
    		
    		 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
             Editor editor = pref.edit();
             String firstname = firstName.getText().toString();
             String lastname = lastName.getText().toString();
             String firstlast = firstname + " " + lastname;
         	editor.putString("fullnameregistration", firstlast); // Storing string
         	editor.commit(); // commit changes	

		new UpdateProfile().execute();
		//finish();
			//	new CreateUser().execute();
		

	} };
    	
    private OnClickListener countrylistener = new OnClickListener() {
         	public void onClick(View v) {
         		// TODO Auto-generated method stub
         	
     
//         		final CountryPicker picker = CountryPicker.newInstance("Select Country");
// 				picker.setListener(new CountryPickerListener() {
//
// 					@Override
// 					public void onSelectCountry(String name, String code) {
// 						Toast.makeText(
// 								RegistrationView.this,
// 								"Country Name: " + name + " - Code: " + code
// 										+ " - Currency: "
// 										+ CountryPicker.getCurrencyCode(code),
// 								Toast.LENGTH_SHORT).show();
// 						Country.setText(name);
// 						Toast.makeText(
// 								ProfileEdit.this,
// 								name,
// 								Toast.LENGTH_SHORT).show();
// 						picker.dismiss();
// 					}
// 				});
 				
// 				picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
 				return;
     	
   
         	}};
         	
     private OnClickListener genderlistener = new OnClickListener() {
     	    	public void onClick(View v) {
     	    		// TODO Auto-generated method stub// Strings to Show In Dialog with Radio Buttons
     	    		final CharSequence[] items = {" Male "," Female "};
     	            
                     // Creating and Building the Dialog 
                     AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEdit.this);
                     builder.setTitle("Gender");
                     builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int item) {
                        
                         
                         switch(item)
                         {
                             case 0:
                                     // Your code when first option seletced
                             	Gender.setText("M");
                                      break;
                             case 1:
                                     // Your code when 2nd  option seletced
                             	Gender.setText("F");
                                     break;
                             
                         }
                         
 						levelDialog.dismiss();    
                         }
                     });
                     levelDialog = builder.create();
                     levelDialog.show();

     	    	}};	
     
     private OnClickListener birthclicklistener = new OnClickListener() {
         public void onClick(View v) {
             final Calendar dateandtime = Calendar.getInstance(Locale.US);

 			DatePickerDailog dp = new DatePickerDailog(ProfileEdit.this,
 					dateandtime, new DatePickerDailog.DatePickerListner() {

 						@Override
 						public void OnDoneButton(Dialog datedialog, Calendar c) {
 							datedialog.dismiss();
 							dateandtime.set(Calendar.YEAR, c.get(Calendar.YEAR));
 							dateandtime.set(Calendar.MONTH,
 									c.get(Calendar.MONTH));
 							dateandtime.set(Calendar.DAY_OF_MONTH,
 									c.get(Calendar.DAY_OF_MONTH));
 							(dateofBirth).setText(new SimpleDateFormat("yyyy-MM-dd")
 									.format(c.getTime()));
 						}

 						@Override
 						public void OnCancelButton(Dialog datedialog) {
 							// TODO Auto-generated method stub
 							datedialog.dismiss();
 						}
 					});
 			dp.show();
 			
 			
 			
 			
 		
 	
         	
         }
     };  
 	

	class CreateUser extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileEdit.this);
            pDialog.setMessage("Loading Profile...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String firstname = null,lastname,phone,gender,country,dateofbirth;
           
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            //    Editor editor = pref.edit();
             // returns stored preference value
             // If value is not present return second param value - In this case null
                
             String username = pref.getString("email", null); // getting String
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", username));


                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                firstname = json.getString(TAG_FIRSTNAME);
                lastname = json.getString(TAG_LASTNAME);
                gender = json.getString(TAG_GENDER);
                country = json.getString(TAG_COUNTRY);
                phone = json.getString(TAG_PHONE);
                dateofbirth = json.getString(TAG_DATEOFBIRTH);

                if (success == 1) {
                	Log.d("Profile loaded", json.toString());
//                	Intent i = new Intent(ProfileEdit.this, MapViewMainfromSDK.class);
//                	finish();
//    				startActivity(i);
                	
//                	firstName.setText(firstname);
                     Editor editor = pref.edit();
                 	editor.putString("firstname", firstname); // Storing string
                 	editor.putString("lastname", lastname); // Storing string
                 	editor.putString("dateofbirth", dateofbirth); // Storing string
                 	editor.putString("gender", gender); // Storing string
                 	editor.putString("country", country); // Storing string
                 	editor.putString("phone", phone); // Storing string
                 	
                 	editor.commit();
                	
                	return json.getString(TAG_MESSAGE);
                	
                	
                	
                }else{
                	Log.d("Profile Read Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
            

		}
		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
        	
        	//get form data
        	
        	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            Editor editor = pref.edit();
        	
        	String firstname = pref.getString("firstname", null); // getting String
        	String lastname = pref.getString("lastname", null); // getting String
        	String dateofbirth = pref.getString("dateofbirth", null); // getting String
        	String gender = pref.getString("gender", null); // getting String
        	String phone = pref.getString("phone", null); // getting String
        	String country = pref.getString("country", null); // getting String
        	
        	firstName.setText(firstname);
        	lastName.setText(lastname);
        	dateofBirth.setText(dateofbirth);
        	Gender.setText(gender);
        	Country.setText(country);
        	Phonenumber.setText(phone);
        	
        	editor.remove("firstname"); // will delete key name
        	editor.remove("lastname"); // will delete key 
        	editor.remove("dateofbirth"); // will delete key 
        	editor.remove("gender"); // will delete key 
        	editor.remove("phone"); // will delete key 
        	editor.remove("country"); // will delete key 
        	 
        	editor.commit(); // commit changes
        	
            // dismiss the dialog once product deleted
        	
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(ProfileEdit.this, file_url, Toast.LENGTH_LONG).show();

            }
            

        }

	}
	
	class UpdateProfile extends AsyncTask<String, String, String> {

		 /**
        * Before starting background thread Show Progress Dialog
        * */
		boolean failure = false;

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(ProfileEdit.this);
           pDialog.setMessage("Updating Profile...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
           int success;
           
           SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
           //    Editor editor = pref.edit();
            // returns stored preference value
            // If value is not present return second param value - In this case null
               
            String username = pref.getString("email", null); // getting String

            
           String firstname = firstName.getText().toString();
           String lastname = lastName.getText().toString();
           String dateofbirth = dateofBirth.getText().toString();
           String gender = Gender.getText().toString();
           String phone = Phonenumber.getText().toString();
           String country = Country.getText().toString();
//           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//           String currentdate = sdf.format(new Date());
//           String currentdate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

           
           try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("email", username));
               params.add(new BasicNameValuePair("firstname", firstname));
               params.add(new BasicNameValuePair("lastname", lastname));
               params.add(new BasicNameValuePair("gender", gender));
               params.add(new BasicNameValuePair("dateofbirth", dateofbirth));
               params.add(new BasicNameValuePair("country", country));
//               params.add(new BasicNameValuePair("currentdate", currentdate));
               params.add(new BasicNameValuePair("phone", phone));
               

               Log.d("request!", "starting");

               //Posting user data to script
               JSONObject json = jsonParser.makeHttpRequest(
                      UPDATE_URL, "POST", params);

               // full json response
               Log.d("Login attempt", json.toString());

               // json success element
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	Log.d("Profile Updated!", json.toString());
//               	Intent i = new Intent(ProfileEdit.this, MapViewMain.class);
               	finish();
//   				startActivity(i);
               	return json.getString(TAG_MESSAGE);
               }else{
               	Log.d("Failure!", json.getString(TAG_MESSAGE));
               	return json.getString(TAG_MESSAGE);

               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return null;

		}
		/**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
           // dismiss the dialog once product deleted
           pDialog.dismiss();
           if (file_url != null){
           	Toast.makeText(ProfileEdit.this, file_url, Toast.LENGTH_LONG).show();
           }

       }

	}
	
	@Override
    public void onResume()
    {
        super.onResume();
        new CreateUser().execute();
	
}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	}

    
