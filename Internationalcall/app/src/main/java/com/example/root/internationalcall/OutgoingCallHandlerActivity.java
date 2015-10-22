package com.example.root.internationalcall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


public class OutgoingCallHandlerActivity extends ActionBarActivity {

    private final String PREFS_NAME = "MyPrefsFile";
    private final String BYPASS_CALL = "passCall";
    public static SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_NAME, 0);
        String phoneNumber = getIntent().getStringExtra(OutgoingCallHandler.PHONE_NUMBER_STRING);
        displayAlert(phoneNumber);
        //setContentView(R.layout.activity_outgoing_call_handler);
    }



    public void displayAlert(final String phoneNumber)
    {
        final Context context = getApplicationContext();
        displayLog("Entering displayAlert");
        final AlertDialog.Builder builder = new AlertDialog.Builder(OutgoingCallHandlerActivity.this);
        builder.setMessage("Do you want to call using dial91?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        displayLog("Calling: " + phoneNumber);
                        makeCall(context, phoneNumber);

                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        setBypassCall(1);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        //alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }

    private void setBypassCall(int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(BYPASS_CALL, value);
        editor.commit();
    }

    public int getBypassCall(){
        return settings.getInt(BYPASS_CALL, 0);
    }


    void makeCall(Context context, String numberString){
        displayLog("Making call to: " + numberString);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:8572166745," + Uri.encode(numberString + "#")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        finish();
    }

    private void displayToastPublished(String s){

        Toast toast = Toast.makeText(this, " " + s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        //Toast.makeText(this, " "+ s , Toast.LENGTH_LONG).show();
    }

    private void displayLog(String s){

        Log.d("dial91", "handlerActivity:  " + s);
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outgoing_call_handler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */


}
