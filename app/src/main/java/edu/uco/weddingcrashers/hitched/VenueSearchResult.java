package edu.uco.weddingcrashers.hitched;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class VenueSearchResult extends ListActivity {


    private ProgressDialog pDialog;

    private static String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
    private static final String TAG_RESULTS = "results";
    private static final String TAG_ID = "place_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "formatted_address";

    JSONArray venues = null;

    ArrayList<HashMap<String, String>> venueList;
    private String whichList;
    private String thevalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_search_result);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String n1 = extras.getString("search");
            url = String.valueOf(Uri.parse(url + n1 + "&key=AIzaSyBdaXki8OZJsX79vweMiJMaH09bYjXvjWY"));
        }
        new GetVenues().execute();



        venueList = new ArrayList<HashMap<String, String>>();

        final ListView searchList = getListView();

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String address = ((TextView) view.findViewById(R.id.address))
                        .getText().toString();
                String place_id = ((TextView) view.findViewById(R.id.placeID)).getText().toString();


                Intent venueDetail = new Intent(VenueSearchResult.this, VenueDetail.class);


                String whichClass = "";

                venueDetail.putExtra("place_id", place_id);
                venueDetail.putExtra("name", name);
                venueDetail.putExtra("address", address);
                venueDetail.putExtra("done", whichClass);
                startActivity(venueDetail);


            }
        });

        searchList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String address = ((TextView) view.findViewById(R.id.address))
                        .getText().toString();

                Intent editVenue = new Intent(VenueSearchResult.this, VenueActivity.class);

                whichList = name + System.getProperty("line.separator") + System.getProperty("line.separator") + address;
                Log.i("WHAT", whichList);

                thevalue = "yes";
                editVenue.putExtra("vname", whichList);
                editVenue.putExtra("myvalue", thevalue);
                startActivity(editVenue);
                return true;
            }
        });

    }

    private class GetVenues extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing prog
            // ress dialog
            pDialog = new ProgressDialog(VenueSearchResult.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    venues = jsonObj.getJSONArray(TAG_RESULTS);
//                    picture = jsonObj.getJSONObject(TAG_PIC);
//                    String pic = picture.getString(TAG_PHOTO);

                    // looping through All Contacts
                    for (int i = 0; i < venues.length(); i++) {
                        JSONObject c = venues.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String address = c.getString(TAG_ADDRESS);

                        // tmp hashmap for single contact
                        HashMap<String, String> myVenues = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        myVenues.put(TAG_ID, id);
                        myVenues.put(TAG_NAME, name);
                        myVenues.put(TAG_ADDRESS, address);
                       // myVenues.put(TAG_PHOTO, pic);
                        // adding contact to contact list
                        venueList.add(myVenues);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    VenueSearchResult.this, venueList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_ADDRESS, TAG_ID, },
                    new int[] {R.id.name,
                            R.id.address,
                            R.id.placeID});

            setListAdapter(adapter);
        }
    }
}