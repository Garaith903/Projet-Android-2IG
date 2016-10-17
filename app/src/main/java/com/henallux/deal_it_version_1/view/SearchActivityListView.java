package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.henallux.deal_it_version_1.R;
import com.henallux.deal_it_version_1.dataBase.MySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;


public class SearchActivityListView extends AppCompatActivity {

    private Bundle bundle;
    private String currentUserID;
    private String categoryP;
    private String typeP;
    private String url;
    private String startDateP;
    private String endDateP;
    private ArrayList<String> listAnnouncement;
    private ArrayList<Integer> idReceived;
    private ArrayAdapter<String> adapter;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity_list_view);

        bundle = this.getIntent().getExtras();
        currentUserID =  bundle.getString("CurrentUserID");
        categoryP = bundle.getString("Category");
        typeP = bundle.getString("Type");

        if (bundle.getBoolean("SearchByDate")) {
            startDateP = bundle.getString("StartDate");
            endDateP = bundle.getString("EndDate");
            Toast.makeText(SearchActivityListView.this, startDateP, Toast.LENGTH_LONG).show();
            Toast.makeText(SearchActivityListView.this, endDateP, Toast.LENGTH_LONG).show();
        }

        //initialisation liste des titres des annonces pour affichage ListView
        listAnnouncement = new ArrayList<String>();

        //initialisation liste des id reçu
        idReceived = new ArrayList<Integer>();

        listview = (ListView) findViewById(R.id.listViewAnnouncement);

        url = createURL();

        getListAnnouncemnt(url);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String elementChoisi = listAnnouncement.get(position);

                int idAnnoSearched = idReceived.get(position);

                Intent myIntent = new Intent(SearchActivityListView.this, SearchActivityAnnouncement.class);
                myIntent.putExtra("CurrentUserID", currentUserID);
                myIntent.putExtra("AnnouncementID", idAnnoSearched);

                startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search_activity_list_view, menu);
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


    public String createURL() {
        String urlTemp = "http://dealitv2.azurewebsites.net/api/annonces/";

        //3 parametres
        if ((!categoryP.equals(getResources().getString(R.string.Category1))) && (!typeP.equals(getResources().getString(R.string.Type1))) && ((bundle.getBoolean("SearchByDate")) == true)) {
            urlTemp += "GetAllAnnoByCategoryTypeDates/?categ=" + categoryP + "&typeA=" + typeP + "&start=" + startDateP + "&end=" + endDateP;
        }
        // 2 parametres : Categorie + Type
        else if ((!categoryP.equals(getResources().getString(R.string.Category1))) && (!typeP.equals(getResources().getString(R.string.Type1))) && ((bundle.getBoolean("SearchByDate")) == false)) {
            urlTemp += "GetAllAnnoByCategoryType/?categoryA=" + categoryP + "&typeA=" + typeP;
        }
        // 2 parametres : Categorie + Date
        else if ((!categoryP.equals(getResources().getString(R.string.Category1))) && (typeP.equals(getResources().getString(R.string.Type1))) && ((bundle.getBoolean("SearchByDate")) == true)) {
            urlTemp += "GetAllAnnoByCategoryDates/?categoryA=" + categoryP + "&start=" + startDateP + "&end=" + endDateP;
        }
        // 2 parametres : Type + Date
        else if ((categoryP.equals(getResources().getString(R.string.Category1))) && (!typeP.equals(getResources().getString(R.string.Type1))) && ((bundle.getBoolean("SearchByDate")) == true)) {
            urlTemp += "GetAllAnnoByTypeDates/?typeA=" + typeP + "&start=" + startDateP + "&end=" + endDateP;
        }
        // 1 parametre Categorie
        else if ((!categoryP.equals(getResources().getString(R.string.Category1))) && (typeP.equals(getResources().getString(R.string.Type1))) && ((bundle.getBoolean("SearchByDate")) == false)) {
            urlTemp += "GetAllAnnoByCategory/?categoryA=" + categoryP;
        }
        // 1 parametre type
        else if ((categoryP.equals(getResources().getString(R.string.Category1))) && (!typeP.equals(getResources().getString(R.string.Type1))) && ((bundle.getBoolean("SearchByDate")) == false)) {
            urlTemp += "GetAllAnnoByType/?typeA=" + typeP;
        }
        // 1 parametre Date
        else if ((categoryP.equals(getResources().getString(R.string.Category1))) && (typeP.equals(getResources().getString(R.string.Type1))) && ((bundle.getBoolean("SearchByDate")) == true)) {
            urlTemp += "GetAllAnnoDates/?start=" + startDateP + "&end=" + endDateP;
        }
        else {
            urlTemp += "GetAllAnnoByTitle";
        }
        return urlTemp;
    }

    public void getListAnnouncemnt(String url)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        int idtemp;
                        String[] separated = (jsonArray.get(i).toString()).split("#");
                        listAnnouncement.add(separated[0]);
                        try {
                            idtemp = Integer.parseInt(separated[1]);
                            idReceived.add(idtemp);

                        } catch(NumberFormatException nfe) {
                            Toast.makeText(SearchActivityListView.this, getString(R.string.ParseError) + separated[1], Toast.LENGTH_SHORT).show();
                        }
                    }
                    //on donne le format a la listview
                    if(listAnnouncement.size()!=0) {
                        adapter = new ArrayAdapter<String>(SearchActivityListView.this, android.R.layout.simple_list_item_1, listAnnouncement);
                        listview.setAdapter(adapter);
                    }else{
                        Toast.makeText(SearchActivityListView.this, getString(R.string.ListEmpty), Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(SearchActivityListView.this, getString(R.string.ErrorInternet),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}

