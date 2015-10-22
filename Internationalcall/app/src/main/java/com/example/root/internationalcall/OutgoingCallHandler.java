package com.example.root.internationalcall;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by root on 8/30/15.
 */
public class OutgoingCallHandler extends BroadcastReceiver {

    public static final String PHONE_NUMBER_STRING = "phoneNumber";
    private final String PREFS_NAME = "MyPrefsFile";
    private final String BYPASS_CALL = "passCall";
    public static SharedPreferences settings;

    private boolean flag =  true;
    AlertDialogManager alert;
    @Override

    public void onReceive(Context context, Intent intent) {
        // Extract phone number reformatted by previous receivers
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        String phoneNumber = getResultData();
        //setResultData(null);
        if (phoneNumber == null) {
            // No reformatted number, use the original
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
        displayLog("Calling phone number: " + phoneNumber);

        if(getBypassCall() == 0) {
            displayLog("don't bypass dial91");
            if (isNumberCallable(phoneNumber)) {

                setResultData(null);
                displayLog("Callable");

                Intent newIntent = new Intent(context, OutgoingCallHandlerActivity.class);
                newIntent.putExtra(PHONE_NUMBER_STRING, phoneNumber);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent);

            }
        }
        setBypassCall(0);
            displayLog("Not callable");


    }


    private void setBypassCall(int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(BYPASS_CALL, value);
        editor.commit();
    }

    public int getBypassCall(){
        return settings.getInt(BYPASS_CALL, 0);
    }

    public void displayAlert(final Context context, final String phoneNumber)
    {
        displayLog("Entering displayAlert");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to call using dial91?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        displayLog("Calling: " + phoneNumber);
                        flag = false;
                        makeCall(context, phoneNumber);

                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        setResultData(phoneNumber);
                        flag = false;
                        /*
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        */

                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();


    }

    void makeCall(Context context, String numberString){
        displayLog("Making call to: " + numberString);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:8572166745," + Uri.encode(numberString + "#")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private boolean isNumberCallable(String number) {

        if(number.length() > 10){
            if(number.startsWith("+91") || number.startsWith("91")) {
                return true;
            }
        }
        return false;
    }

    private void displayToast(String s){
          //Toast.makeText(this, "Message: " + s, Toast.LENGTH_LONG).show();
    }
    private static void displayLog(String msg){

        Log.d("dial91", "Handler: " + msg);
    }

}