package com.example.root.internationalcall;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


public class NumberPadActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_pad);

        EditText et = (EditText) findViewById(R.id.numberPad);
        et.setSelection(et.getText().length());
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_number_pad, menu);
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

    public void makeCallView(View view) {
        EditText et = (EditText) findViewById(R.id.numberPad);

        String number = sanitizeNumber(String.valueOf(et.getText()));
        if(number.length() > 10){
            makeCall(number);
        }
        else{
            makeCall("91"+number);
        }
    }

    String sanitizeNumber(String number){
        char[] numberCharArray = number.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < numberCharArray.length; i++){
            if(Character.isDigit(numberCharArray[i])){
                sb.append(numberCharArray[i]);
            }
        }
        return sb.toString();

    }

    void makeCall(String numberString){
        displayLog("Making call to: " + numberString);
        displayToastPublished("Calling " + numberString);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:8572166745," + Uri.encode(numberString + "#")));
        startActivity(intent);
    }

    private void displayToastPublished(String s){

        Toast toast = Toast.makeText(this," "+ s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        //Toast.makeText(this, " "+ s , Toast.LENGTH_LONG).show();
    }
    
    private void displayLog(String s){

        Log.d("dial91", "NumberPadActivity:  " + s);
    }
}
