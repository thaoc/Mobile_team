package edu.csp.csc315.damascusway30;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //Variables for RecyclerView Adapter
    private RecyclerViewAdapter adapter;
    //DatabaseIO to get residents from Database
    private DatabaseIO databaseIO;
    //List to hold the residents
    private List<Resident> mResidents = new ArrayList<>();
    //Create Toolbar
    Toolbar toolbar;

    //variables for JSON
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> residentsList;
    private static String url_all_products = "http://localhost/Web-App/android-connect/get-resident.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RESIDENTS = "residents";
    private static final String TAG_RID = "rid";
    private static final String TAG_FNAME = "fname";
    private static final String TAG_MNAME = "mname";
    private static final String TAG_LNAME = "lname";
    JSONArray residents = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");
        new LoadAllResidents().execute();
        //initImageBitMaps();
        //initRecyclerView();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initImageBitMaps(){

/*
        //Create some residents to work with for testing
        Resident joel = new Resident("Joel", "Schuessler", "Clifton", "https://i.imgur.com/Ha3MFuv.jpg", false, "High", "Green", "Brown");
        Resident cheng = new Resident("Cheng", "Thao", "", "https://i.imgur.com/y19ovIo.jpg", false, "High", "Brown", "Black");
        Resident jeff = new Resident("El", "Jefe", "", "https://i.imgur.com/NF7LAhw.jpg", true, "Low", "Blue", "Red");
        Resident robert = new Resident("Robert", "Krueger", "", "https://i.imgur.com/Kw7Ua01.jpg", true, "Medium", "Brown", "Brown");
        Resident jerry = new Resident("Sailor", "Jerry", "", "https://i.imgur.com/5q4G9P9.jpg", false, "Medium", "Brown", "Brown");
        Resident tyrion = new Resident("Tyrion", "Lannister", "", "https://i.imgur.com/S1KQZwN.jpg", false, "Medium", "Brown", "Brown");
        Resident sarah = new Resident("Sarah", "Severson", "", "https://i.imgur.com/FKpCI8Y.jpg", true, "Medium", "Brown", "Brown");
        Resident vadar = new Resident("Mike", "Vader", "", "https://i.imgur.com/coRRgCY.jpg", true, "Medium", "Brown", "Brown");
        Resident mike = new Resident("Mike", "Davidovich", "", "https://i.imgur.com/CT8ae03.jpg", false, "Medium", "Brown", "Brown");
        Resident heather = new Resident("Heather", "Kooiker", "", "https://i.imgur.com/sHAYnvR.jpg", true, "Medium", "Brown", "Brown");
        //Add residents to List
        mResidents.add(joel);
        mResidents.add(cheng);
        mResidents.add(jeff);
        mResidents.add(robert);
        mResidents.add(jerry);
        mResidents.add(tyrion);
        mResidents.add(sarah);
        mResidents.add(vadar);
        mResidents.add(mike);
        mResidents.add(heather);
        //Create RecyclerView from List
*/
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(this, mResidents);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        Log.d(TAG, "OnCreateOptionsMenu: create menu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    //https://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
    //https://developer.android.com/reference/android/os/AsyncTask

    @SuppressLint("StaticFieldLeak")
    class LoadAllResidents extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading residents. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<>();
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            Log.d("All Residents: ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    residents = json.getJSONArray(TAG_RESIDENTS);

                    for (int i = 0; i < residents.length(); i++) {
                        JSONObject c = residents.getJSONObject(i);

                        String id = c.getString(TAG_RID);
                        String fname = c.getString(TAG_FNAME);
                        String lname = c.getString(TAG_LNAME);
                        String mname = c.getString(TAG_MNAME);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_RID, id);
                        map.put(TAG_FNAME, fname);
                        map.put(TAG_LNAME, lname);
                        map.put(TAG_MNAME, mname);

                        residentsList.add(map);

                        //need to parse through residentList and create ArrayList<Resident> for
                        //for mREsidents
                        // https://stackoverflow.com/questions/11258969/how-to-get-value-stored-in-arraylisthashmapkey-value
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into recycle view
                     * */
                    initRecyclerView();
                }
            });

        }

    }
}
