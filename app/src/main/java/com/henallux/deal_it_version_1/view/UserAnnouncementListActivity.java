package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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

public class UserAnnouncementListActivity extends AppCompatActivity {

    private Bundle bundle;
    private ListView listUserAnnouncement;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> titleUserAnnouncement;
    private ArrayList<String> listIdAnnouncementt;

    private ArrayList<String> listIdConsultation;

    private String listItemName;
    private String currentUser;
    String infoUserSelectedAnno;
    private String categoryUserAnnounce;
    private String dateUserAnnounce;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_announcement_list);

        bundle = this.getIntent().getExtras();
        currentUser =  bundle.getString("CurrentUserID");

        //Initialisation liste des titres des annonces postée par l'utilisateur
        titleUserAnnouncement = new ArrayList<String>();
        //Initialisation liste des id des annoces postées par l'utilisateur
        listIdAnnouncementt = new ArrayList<String>();
        //Initialisation liste des id des consultations pour une annonce a supprimer
        listIdConsultation = new ArrayList<String>();

        listUserAnnouncement = (ListView) findViewById(R.id.listViewUserAnnouncement);

        url = "http://dealitv2.azurewebsites.net/api/annonces/GetAllTitleAnnouncementByPosterId/?posterID="+currentUser;

        getListUserAnnouncemnt(url);

        listUserAnnouncement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.interractWarning), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

        if (v.getId()==R.id.listViewUserAnnouncement)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(titleUserAnnouncement.get(info.position));
            String[] menuItems = getResources().getStringArray(R.array.contextMenuMyAnnouncement);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        String[] menuItems = getResources().getStringArray(R.array.contextMenuMyAnnouncement);

        String menuItemName = menuItems[menuItemIndex];

        //Obtenir Le titre de l'objet
        listItemName = titleUserAnnouncement.get(info.position);

        //récupère la chaine de caractère contenant les information nécessaire à l'identification de l'annonce sélectionnée
        infoUserSelectedAnno = listIdAnnouncementt.get(info.position).toString();

        switch (menuItemIndex) {

            case 0 :

                Intent myIntent = new Intent(UserAnnouncementListActivity.this, SearchActivityAnnouncement.class);
                myIntent.putExtra("CurrentUserID", currentUser);
                myIntent.putExtra("AnnouncementID", Integer.valueOf(infoUserSelectedAnno));
                startActivity(myIntent);

                break;

            case 1 :

                Intent intentEdit = new Intent(UserAnnouncementListActivity.this, EditAnnouncementActivity.class);
                intentEdit.putExtra("CurrentUserID", currentUser);
                intentEdit.putExtra("AnnouncementID", infoUserSelectedAnno);
                startActivity(intentEdit);

                break;

            case 2 :

                adapter.remove(adapter.getItem(info.position));
                String urlConsult = "http://dealitv2.azurewebsites.net/api/consultations/GetAllConsultationByAnnouncementID/?annoID="+(Integer.valueOf(infoUserSelectedAnno));
                getConsultationOfSelectedAnnouncement(urlConsult);

                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_announcement_list, menu);
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

    public void getListUserAnnouncemnt(String url)
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
                        String[] separated = (jsonArray.get(i).toString()).split("#");
                        //Récupere le titre de l'annonce => afficher dans le listView
                        titleUserAnnouncement.add(separated[0]);
                        //Récupèration reste des information afin d'identifier précisément l'annonce sélectionnée dans listView
                        listIdAnnouncementt.add(separated[1]);
                    }
                    //on donne le format a la listview
                    if(titleUserAnnouncement.size()!=0) {
                        adapter = new ArrayAdapter<String>(UserAnnouncementListActivity.this, android.R.layout.simple_list_item_1, titleUserAnnouncement);
                        listUserAnnouncement.setAdapter(adapter);
                        registerForContextMenu(listUserAnnouncement);
                    }else{
                        Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.ListEmpty), Toast.LENGTH_LONG).show();
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
                Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void getConsultationOfSelectedAnnouncement(String urlGCOSA)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlGCOSA, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String urlDeleteConsultation = "http://dealitv2.azurewebsites.net/api/consultations/DeleteConsultationById/?id="+jsonArray.get(i).toString();
                        deleteConsultation(urlDeleteConsultation);
                    }
                    String urlDelAnnouncement = "http://dealitv2.azurewebsites.net/api/annonces/DeleteAnnouncementById/?id="+Integer.valueOf(infoUserSelectedAnno);
                    deleteAnnouncement(urlDelAnnouncement);

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
                Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.DeleteConsultationConfirmation),Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void deleteAnnouncement(String urlDelete)
    {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.DELETE, urlDelete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                adapter.notifyDataSetChanged();

                if (titleUserAnnouncement.size() == 0) {
                    Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.ListEmpty), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.DeleteAnnouncementConfirmation),Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(UserAnnouncementListActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}
