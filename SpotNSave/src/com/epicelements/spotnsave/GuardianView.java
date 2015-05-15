package com.epicelements.spotnsave;

 
import com.actionbarsherlock.app.SherlockActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;




public class GuardianView extends SherlockActivity
	{
		 
    final Activity activity = this;
    
    private static final int CONTACT_PICKER_RESULT1 = 1001; 
    private static final int CONTACT_PICKER_RESULT2 = 1002;
    private static final int CONTACT_PICKER_RESULT3 = 1003;
    private static final int CONTACT_PICKER_RESULT4 = 1004;
    private static final int CONTACT_PICKER_RESULT5 = 1005;
    
    //---sends an SMS message to another device---

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
     }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardians);
        


        ((ImageButton)this.findViewById(R.id.imageButton1)).setOnClickListener(contacts1listener);      
        ((ImageButton)this.findViewById(R.id.imageButton3)).setOnClickListener(contacts2listener);
        ((ImageButton)this.findViewById(R.id.imageButton4)).setOnClickListener(contacts3listener);
        ((ImageButton)this.findViewById(R.id.imageButton5)).setOnClickListener(contacts4listener);
        ((ImageButton)this.findViewById(R.id.imageButton7)).setOnClickListener(contacts5listener);
        ((ImageButton)this.findViewById(R.id.imageButton2)).setOnClickListener(submitlistener);
        ((ImageButton)this.findViewById(R.id.imageButton6)).setOnClickListener(clearlistener);
        
        
	}
    
    
    private OnClickListener contacts1listener = new OnClickListener() {
        public void onClick(View v) {
        	Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
                    Contacts.CONTENT_URI);  
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT1); 		
        }
    };  
    
    private OnClickListener contacts5listener = new OnClickListener() {
        public void onClick(View v) {
        	Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
                    Contacts.CONTENT_URI);  
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT5); 		
        }
    };  
    
    private OnClickListener clearlistener = new OnClickListener() {
        public void onClick(View v) {
        
        	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            Editor editor = pref.edit();
        	editor.remove("gname1"); // will delete key       
        	editor.remove("gnumber1"); // will delete key   
        	editor.remove("gname2"); // will delete key       
        	editor.remove("gnumber2"); // will delete key   
        	editor.remove("gname3"); // will delete key       
        	editor.remove("gnumber3"); // will delete key   
        	editor.remove("gname4"); // will delete key       
        	editor.remove("gnumber4"); // will delete key   
        	editor.remove("gname5"); // will delete key       
        	editor.remove("gnumber5"); // will delete key 
        	editor.commit(); // commit changes
        	
        	EditText clear1 = (EditText)findViewById(R.id.editText1);
        	EditText clear2 = (EditText)findViewById(R.id.editText2);
        	EditText clear3 = (EditText)findViewById(R.id.editText3);
        	EditText clear4 = (EditText)findViewById(R.id.editText4);
        	EditText clear5 = (EditText)findViewById(R.id.editText5);
        	EditText clear6 = (EditText)findViewById(R.id.editText6);
        	EditText clear7 = (EditText)findViewById(R.id.editText7);
        	EditText clear8 = (EditText)findViewById(R.id.editText8);
        	EditText clear9 = (EditText)findViewById(R.id.editText9);
        	EditText clear10 = (EditText)findViewById(R.id.editText10);
        	
            clear1.setText("");
            clear2.setText("");
            clear3.setText("");
            clear4.setText("");
            clear5.setText("");
            clear6.setText("");
            clear7.setText("");
            clear8.setText("");
            clear9.setText("");
            clear10.setText("");
            
        	
        	
        	
        }
    };  
    
    
    private OnClickListener contacts2listener = new OnClickListener() {
        public void onClick(View v) {
        	Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
                    Contacts.CONTENT_URI);  
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT2); 		
        }
    }; 
    
    
    private OnClickListener contacts3listener = new OnClickListener() {
        public void onClick(View v) {
        	Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
                    Contacts.CONTENT_URI);  
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT3); 		
        }
    }; 
    
    private OnClickListener contacts4listener = new OnClickListener() {
        public void onClick(View v) {
        	Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
                    Contacts.CONTENT_URI);  
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT4); 		
        }
    }; 
    
    private OnClickListener submitlistener = new OnClickListener() {
        public void onClick(View v) {
           
        	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

            String number1 = pref.getString("gnumber1", null); // getting String
            String number2 = pref.getString("gnumber2", null); // getting String
            String number3 = pref.getString("gnumber3", null); // getting String
            String number4 = pref.getString("gnumber4", null); // getting String
            String number5 = pref.getString("gnumber5", null); // getting String
            String fullname = pref.getString("fullnameregistration", null); // getting String 
            
              if(number1 != null && !number1.equals("") && fullname == null){
              	sendSMS(number1, "I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
              }
              if(number1 != null && !number1.equals("") && fullname != null){
              	sendSMS(number1, fullname + " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
              }
              if(number2 != null && !number2.equals("") && fullname == null){
              	sendSMS(number2, "I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");       
              	}   
              if(number2 != null && !number2.equals("") && fullname != null){
              	sendSMS(number2, fullname + " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
              }
              if(number3 != null && !number3.equals("") && fullname == null){
              	sendSMS(number3, "I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");      
              	}
              if(number3 != null && !number3.equals("") && fullname != null){
              	sendSMS(number3, fullname + " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
              }
              if(number4 != null && !number4.equals("") && fullname == null){
              	sendSMS(number4, "I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");   
              	}
              if(number4 != null && !number4.equals("") && fullname != null){
              	sendSMS(number4, fullname + " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
              }
              if(number5 != null && !number5.equals("") && fullname == null){
              	sendSMS(number5, "I am using the spotNsave app and have chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
              }
              if(number5 != null && !number5.equals("") && fullname != null){
              	sendSMS(number5, fullname + " is using the spotNsave app and has chosen you to be a guardian. Get spotNsave today at www.spotnsave.com");
              }

         
            
        	finish();
        }
    }; 
    
    
    @Override 
    public void onActivityResult(int reqCode, int resultCode, Intent data){ 
        super.onActivityResult(reqCode, resultCode, data);

        switch(reqCode)
        {
        
        case (CONTACT_PICKER_RESULT5):
            if (resultCode == Activity.RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                 if (c.moveToFirst())
                 {
                     String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                     String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                     if (hasPhone.equalsIgnoreCase("1")) 
                     {
                         Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                         phones.moveToFirst();
                         String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //      Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                         String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                         EditText nameEntry5 = (EditText)findViewById(R.id.editText9);
                         nameEntry5.setText(nameContact);
             
                         
                         EditText numberEntry5 = (EditText)findViewById(R.id.editText10);
                         numberEntry5.setText(cNumber);
                         if (cNumber.length() == 0) {
                             Toast.makeText(this, "No email found for contact.", Toast.LENGTH_LONG).show();
                             
                         }
                         
                         SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                         Editor editor = pref.edit();
                     	editor.putString("gname5", nameContact); // Storing string
                     	editor.putString("gnumber5", cNumber); // Storing string
                     	editor.commit(); // commit changes 


                     }
                }
          }
        
        break;
        
        case (CONTACT_PICKER_RESULT4):
            if (resultCode == Activity.RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                 if (c.moveToFirst())
                 {
                     String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                     String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                     if (hasPhone.equalsIgnoreCase("1")) 
                     {
                         Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                         phones.moveToFirst();
                         String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //      Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                         String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                         EditText nameEntry4 = (EditText)findViewById(R.id.editText7);
                         nameEntry4.setText(nameContact);
             
                         
                         EditText numberEntry4 = (EditText)findViewById(R.id.editText6);
                         numberEntry4.setText(cNumber);
                         if (cNumber.length() == 0) {
                             Toast.makeText(this, "No email found for contact.", Toast.LENGTH_LONG).show();
                             
                         }
                         
                         SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                         Editor editor = pref.edit();
                     	editor.putString("gname4", nameContact); // Storing string
                     	editor.putString("gnumber4", cNumber); // Storing string
                     	editor.commit(); // commit changes 


                     }
                }
          }
        
        break;
        
        case (CONTACT_PICKER_RESULT3):
            if (resultCode == Activity.RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                 if (c.moveToFirst())
                 {
                     String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                     String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                     if (hasPhone.equalsIgnoreCase("1")) 
                     {
                         Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                         phones.moveToFirst();
                         String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //      Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                         String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                         EditText nameEntry3 = (EditText)findViewById(R.id.editText4);
                         nameEntry3.setText(nameContact);
                         
                         EditText numberEntry3 = (EditText)findViewById(R.id.editText5);
                         numberEntry3.setText(cNumber);
                         if (cNumber.length() == 0) {
                             Toast.makeText(this, "No number found for contact.", Toast.LENGTH_LONG).show();
                             break;
                         }
                         SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                         Editor editor = pref.edit();
                     	editor.putString("gname3", nameContact); // Storing string
                     	editor.putString("gnumber3", cNumber); // Storing string
                     	editor.commit(); // commit changes 


                     }
                }
          }
        
       
        
        
        break;
        case (CONTACT_PICKER_RESULT2):
            if (resultCode == Activity.RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                 if (c.moveToFirst())
                 {
                     String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                     String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                     if (hasPhone.equalsIgnoreCase("1")) 
                     {
                         Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                         phones.moveToFirst();
                         String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                  //        Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                         String nameContact2 = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                         EditText nameEntry = (EditText)findViewById(R.id.editText3);
                         nameEntry.setText(nameContact2);
                         
                         EditText numberEntry2 = (EditText)findViewById(R.id.editText8);
                         numberEntry2.setText(cNumber);
                         if (cNumber.length() == 0) {
                             Toast.makeText(this, "No number found for contact.", Toast.LENGTH_LONG).show();
                             break;
                         }
                         SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                         Editor editor = pref.edit();
                     	editor.putString("gname2", nameContact2); // Storing string
                     	editor.putString("gnumber2", cNumber); // Storing string
                     	editor.commit(); // commit changes 


                     }
                }
          }
        
        
        break;
           case (CONTACT_PICKER_RESULT1):
             if (resultCode == Activity.RESULT_OK)
             {
                 Uri contactData = data.getData();
                 Cursor c = managedQuery(contactData, null, null, null, null);
                  if (c.moveToFirst())
                  {
                      String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                      String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                      if (hasPhone.equalsIgnoreCase("1")) 
                      {
                          Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
                                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                          phones.moveToFirst();
                          String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                   //        Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                          String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                          EditText nameEntry = (EditText)findViewById(R.id.editText1);
                          nameEntry.setText(nameContact);
                          
                          EditText numberEntry = (EditText)findViewById(R.id.editText2);
                          numberEntry.setText(cNumber);
                          if (cNumber.length() == 0) {
                              Toast.makeText(this, "No Number found for contact.", Toast.LENGTH_LONG).show();
                              break;
                          }
                          SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                          Editor editor = pref.edit();
                      	editor.putString("gname1", nameContact); // Storing string
                      	editor.putString("gnumber1", cNumber); // Storing string
                      	editor.commit(); // commit changes 


                      }
                 }
                  
           }
           

           break;
           
           
           
           
           
           
           
           
        }
    }
    
    

    
 public void onResume(){
	 
	 super.onResume();
	 
	 
     SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
     //Editor editor = pref.edit();
     
     
     String emailpref1 = pref.getString("email", null); // getting String
     String emailpref2 = pref.getString("emailGuardianCompare", null); // getting String

  //   String emails = emailpref1+emailpref2;
     
 //	Toast.makeText(getApplicationContext(), emails, Toast.LENGTH_LONG).show();

 	String name1 = pref.getString("gname1", null); // getting String
    String number1 = pref.getString("gnumber1", null); // getting String
    String name2 = pref.getString("gname2", null); // getting String
    String number2 = pref.getString("gnumber2", null); // getting String
    String name3 = pref.getString("gname3", null); // getting String
    String number3 = pref.getString("gnumber3", null); // getting String
    String name4 = pref.getString("gname4", null); // getting String
    String number4 = pref.getString("gnumber4", null); // getting String
    
 //   Toast.makeText(getApplicationContext(), name4, Toast.LENGTH_LONG).show();
    if( emailpref1 != null && emailpref2 != null) {
    if( emailpref1.length() == emailpref2.length() )
    {
    
        if(number1 != null){
     	   EditText nameEntry1 = (EditText)findViewById(R.id.editText1);
            nameEntry1.setText(name1);
            
            EditText numberEntry1 = (EditText)findViewById(R.id.editText2);
            numberEntry1.setText(number1);
        }
        if(number2 != null){
     	   EditText nameEntry2 = (EditText)findViewById(R.id.editText3);
            nameEntry2.setText(name2);
            
            EditText numberEntry2 = (EditText)findViewById(R.id.editText8);
            numberEntry2.setText(number2);
        }
        if(number3 != null){
     	   EditText nameEntry3 = (EditText)findViewById(R.id.editText4);
            nameEntry3.setText(name3);
            
            EditText numberEntry3 = (EditText)findViewById(R.id.editText5);
            numberEntry3.setText(number3);
        }
        if(number4 != null){
     	   EditText nameEntry4 = (EditText)findViewById(R.id.editText7);
            nameEntry4.setText(name4);
            
            EditText numberEntry4 = (EditText)findViewById(R.id.editText6);
            numberEntry4.setText(number4);
        }
 	   
    }
 }}
    


	}

    
