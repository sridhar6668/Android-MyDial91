package com.example.root.internationalcall;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SelectContactsActivity extends ActionBarActivity {


    private SimpleAdapter sa;

    ArrayList<HashMap<String,String>> list;
    ArrayList<ArrayList<String>> contactList = new ArrayList<ArrayList<String>>();
    //ArrayList<String> contactNumberList = new ArrayList<String>();

    private String[][] contactsArray;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        displayLog("Inside selectContactsActivity");

        fetchContacts();

        setUpListView((ListView) findViewById(R.id.contactsListView),"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_contacts, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        addSearchViewOptions(searchView);

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


    private void addSearchViewOptions(SearchView searchView){
        searchView.setQueryHint("Search for contacts");
        //searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);

        // Listens for enter key
        searchView.setOnQueryTextListener(searchQueryListener);

        // display suggestions
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        //search.setSuggestionsAdapter(new ExampleAdapter(this, cursor, items));
        // searchView.setOnSuggestionListener(suggestionListener);

    }




    private SearchView.OnSuggestionListener suggestionListener = new SearchView.OnSuggestionListener() {
        @Override
        public boolean onSuggestionSelect(int position) {
            return false;
        }

        @Override
        public boolean onSuggestionClick(int position) {
            return false;
        }
    };

    private SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            search(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            search(query);
            return false;
        }


        public void search(String query) {
            displayLog(query);
            setUpListView((ListView) findViewById(R.id.contactsListView), query);
        }

    };


    /**
     * This function will fetch the contacts and store it in an array "contactsArray"
     */
    private void fetchContacts(){
        Cursor cursor = null;
        try {
            cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            //cursor = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
              //      ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME + "ASC");
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                String idContact = cursor.getString(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                ArrayList<String> tempList = new ArrayList<String>();
                tempList.add(name);
                tempList.add(phoneNumber);
                contactList.add(tempList);

            } while (cursor.moveToNext());

            contactsArray = new String[contactList.size()][];
            for (int i = 0; i < contactList.size(); i++) {
                ArrayList<String> row = contactList.get(i);
                contactsArray[i] = row.toArray(new String[row.size()]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    /**
     * This function populates the list view using the contents of "contactsArray"
     * @param listView
     */
    private void setUpListView(final ListView listView, String filterString){
        list = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> item;
        for(int i=0;i< contactsArray.length;i++){

                if (contactsArray[i][0].toLowerCase().indexOf(filterString) != -1) {
                    item = new HashMap<String,String>();
                    item.put( "line1", contactsArray[i][0]);
                    item.put( "line2", contactsArray[i][1]);
                    list.add( item );
                }

        }
        sa = new SimpleAdapter(this, list,
                android.R.layout.two_line_list_item ,
                new String[] { "line1","line2" },
                new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(sa);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {


                displayLog("Before adapter");
                SimpleAdapter adapter = (SimpleAdapter) adapterView.getAdapter();

                displayLog("After adapter");

                Object item =  adapter.getItem(position);

                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
                String name = (String) obj.get("line1");
                String number = (String) obj.get("line2");
                displayLog("number: " + number);
                number = sanitizeNumber(number);
                if(number.length() > 10){
                    makeCall(number);
                }
                else{
                    makeCall("91"+number);
                }


            }

        });

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


    public void openNumberPad(View view) {

            Intent intent = new Intent(this, NumberPadActivity.class);
            startActivity(intent);
    }

    private void displayToastPublished(String s){

        Toast toast = Toast.makeText(this," "+ s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        //Toast.makeText(this, " "+ s , Toast.LENGTH_LONG).show();
    }

    private void displayLog(String s){

        Log.d("dial91", "contactsArray:  " + s);
    }

}
