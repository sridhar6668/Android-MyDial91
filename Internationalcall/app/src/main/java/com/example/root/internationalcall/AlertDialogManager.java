package com.example.root.internationalcall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by root on 8/30/15.
 */
public class AlertDialogManager {

    public void displayAlert(final Context context, final String phoneNumber)
    {
        displayLog("Entering displayAlert");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+phoneNumber));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }

    public void showAlertDialog(final Context context,final String phoneNumber) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Do you want to call using dial91?");
        //alertDialog.setMessage(message);

            alertDialog.setButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    displayLog("Calling: " + phoneNumber);
                    makeCall(context, phoneNumber);
                }
            });

        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    void makeCall(Context context, String numberString){
        displayLog("Making call to: " + numberString);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:8572166745," + Uri.encode(numberString + "#")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void displayLog(String s){

        Log.d("dial91", "dialogmanager:  " + s);
    }


}