package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.henallux.deal_it_version_1.R;
import com.henallux.deal_it_version_1.dataBase.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserHistoryListActivity extends AppCompatActivity {

    private Bundle bundle;
    private ListView listUserHistory;
    private ArrayList<String> listIdViewedAnnouncement;
    private ArrayList<String> titleAnnouncementHistory;
    private ArrayList<String> idAnnouncementHistory;
    private ArrayAdapter<String> adapter;

    private String url;
    private String currentUser;
    private String listItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_list);

        bundle = this.getIntent().getExtras();
        currentUser =  bundle.getString("CurrentUserID");

        //Initialisation liste des id des annonces consultées
        listIdViewedAnnouncement = new ArrayList<String>();
        //Initialisation liste des titres des annonces dans l'historique
        titleAnnouncementHistory = new ArrayList<String>();
        //les listes "listIdViewedAnnouncement" et "titleAnnouncementHistory" ne seront pas dans le meme ordre. Nous concervons donc une copie de l'id dans une autre liste remplie au meme momment
        idAnnouncementHistory = new ArrayList<String>();

        listUserHistory = (ListView) findViewById(R.id.listViewUserHistory);

        //Recupération de toutes les annonces consultée (id)
        url = "http://dealitv2.azurewebsites.net/api/consultations/RetournerIdAnnIdUtil/?userApp="+currentUser;
        getIdVieuwedAnnouncement(url);

        listUserHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UserHistoryListActivity.this, getString(R.string.interractWarning), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId()==R.id.listViewUserHistory)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(titleAnnouncementHistory.get(info.position));

            String[] menuItems = getResources().getStringArray(R.array.contextMenuHistory);

            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int menuItemIndex = item.getItemId();

        String[] menuItems = getResources().getStringArray(R.array.contextMenuHistory);

        String menuItemName = menuItems[menuItemIndex];

        //Obtenir Le titre de l'objet
        listItemName = titleAnnouncementHistory.get(info.position);

        //récupère la chaine de caractère contenant les information nécessaire à l'identification de l'annonce sélectionnée
        String infoUserSelectedAnno = idAnnouncementHistory.get(info.position).toString();

        switch (menuItemIndex) {

            case 0 :
                Intent myIntent = new Intent(UserHistoryListActivity.this, SearchActivityAnnouncement.class);
                myIntent.putExtra("CurrentUserID", currentUser);
                myIntent.putExtra("AnnouncementID", Integer.valueOf(infoUserSelectedAnno));

                startActivity(myIntent);

                break;

            case 1 :
                String id = currentUser.concat(infoUserSelectedAnno);
                String urlDelete = "http://dealitv2.azurewebsites.net/api/consultations/DeleteConsultationById/?id="+id;
                adapter.remove(adapter.getItem(info.position));
                deleteConsultation(urlDelete);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_history_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all_history) {


            for (int d = 0; d < titleAnnouncementHistory.size(); d++)
            {
                //On reconstitue l'id de la consultation a supprimer
                String idElementToDel = currentUser.concat(idAnnouncementHistory.get(d).toString());
                String urlDelete = "http://dealitv2.azurewebsites.net/api/consultations/DeleteConsultationById/?id="+idElementToDel;
                adapter.remove(adapter.getItem(d));
                deleteConsultation(urlDelete);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void getIdVieuwedAnnouncement(String url)
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
                        listIdViewedAnnouncement.add(jsonArray.get(i).toString());
                        int id = Integer.valueOf(listIdViewedAnnouncement.get(i));
                        String urlTitleVA = "http://dealitv2.azurewebsites.net/api/annonces/GetAllTitleAnnouncementById/?idAnn="+id;
                        getTitleVA(urlTitleVA);
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
                Toast.makeText(UserHistoryListActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void getTitleVA(String urlTitle)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlTitle, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArrayT)
            {
                try
                {
                    for (int b = 0; b < jsonArrayT.length(); b++)
                    {
                        String[] separated = (jsonArrayT.get(b).toString()).split("#");
                        titleAnnouncementHistory.add(separated[0]);
                        idAnnouncementHistory.add(separated[1]);
                    }
                    //on donne le format a la listview
                    if(titleAnnouncementHistory.size()!=0) {
                        adapter = new ArrayAdapter<String>(UserHistoryListActivity.this, android.R.layout.simple_list_item_1, titleAnnouncementHistory);
                        listUserHistory.setAdapter(adapter);
                        registerForContextMenu(listUserHistory);
                    }else{
                        Toast.makeText(UserHistoryListActivity.this, getString(R.string.ListEmpty), Toast.LENGTH_LONG).show();
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
                Toast.makeText(UserHistoryListActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void deleteConsultation(String urlDelete)
    {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.DELETE, urlDelete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                adapter.notifyDataSetChanged();

                if (titleAnnouncementHistory.size() == 0) {
                    Toast.makeText(UserHistoryListActivity.this, getString(R.string.ListEmpty), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(UserHistoryListActivity.this, getString(R.string.DeleteConsultationConfirmation),Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(UserHistoryListActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

}
