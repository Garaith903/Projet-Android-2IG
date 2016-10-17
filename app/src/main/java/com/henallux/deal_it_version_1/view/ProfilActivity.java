package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.henallux.deal_it_version_1.R;
import com.henallux.deal_it_version_1.dataBase.MySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;

public class ProfilActivity extends AppCompatActivity {

    private Bundle bundle;
    private String currentUserID;

    private TextView currentID, name, firstname, adress, nationnalityUser, email1, email2, sexe, phone1, phone2;
    private ImageView photo;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        bundle = this.getIntent().getExtras();
        currentUserID =  bundle.getString("CurrentUserID");

        currentID = (TextView) findViewById(R.id.idEtud);
        currentID.setText(currentUserID);
        name = (TextView) findViewById(R.id.nameStud);
        firstname = (TextView) findViewById(R.id.firstname);
        adress = (TextView) findViewById(R.id.adress);
        nationnalityUser = (TextView) findViewById(R.id.nationStudent);
        email1 = (TextView) findViewById(R.id.email1);
        email2 = (TextView) findViewById(R.id.email2);
        sexe = (TextView) findViewById(R.id.sexeStudent);
        phone1 = (TextView) findViewById(R.id.phoneStudent1);
        phone2 = (TextView) findViewById(R.id.phoneStudent2);

        photo = (ImageView) findViewById(R.id.imageProfil);

        url = "http://dealitv2.azurewebsites.net/api/utilisateurs/GetAllUserDataById/?userID="+currentUserID;

        getUserData(url);

        url = "http://dealitv2.azurewebsites.net/api/photos/GetPhotoUrlByUserId/?userID="+currentUserID;

        getUserAvatar(url);


        getOverflowMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case R.id.search :
                Intent intentSearch = new Intent(ProfilActivity.this,SearchActivity.class);
                intentSearch.putExtra("CurrentUserID", currentUserID);
                startActivity(intentSearch);
                return true;
            case R.id.add_Announcement :
                Intent intentAdd = new Intent(ProfilActivity.this,AddAnnouncementActivity.class);
                intentAdd.putExtra("CurrentUserID", currentUserID);
                startActivity(intentAdd);
                return true;
            case R.id.manager_Announcement :
                Intent intentManage = new Intent(ProfilActivity.this,Manager_Announcement.class);
                intentManage.putExtra("CurrentUserID", currentUserID);
                startActivity(intentManage);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getOverflowMenu() {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null)
            {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getUserData(String urlDataUser){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlDataUser,new Response.Listener<JSONArray>(){
            @Override

            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String[] separated = (jsonArray.get(i).toString()).split("#");
                        name.setText(separated[0]);
                        firstname.setText(separated[1]);
                        adress.setText(separated[2]);
                        sexe.setText(separated[3]);
                        nationnalityUser.setText(separated[4]);

                    }
                    url = "http://dealitv2.azurewebsites.net/api/telephones/GetAllTelephoneNumberByUserId/?userID="+currentUserID;

                    getUserPhone(url);
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
                Toast.makeText(ProfilActivity.this, getString(R.string.ErrorInternet),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void getUserPhone(String urlDataPhone){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlDataPhone,new Response.Listener<JSONArray>(){
            @Override

            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (i == 0)
                            phone1.setText(jsonArray.get(i).toString());
                        else
                            phone2.setText(jsonArray.get(i).toString());

                    }

                    url = "http://dealitv2.azurewebsites.net/api/emails/GetAllEMailByUserId/?userID="+currentUserID;

                    getUserEmail(url);
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
                Toast.makeText(ProfilActivity.this, getString(R.string.ErrorInternet),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }
    public void getUserEmail(String urlDataEmail){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlDataEmail,new Response.Listener<JSONArray>(){
            @Override

            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (i == 0)
                            email1.setText(jsonArray.get(i).toString());
                        else
                            email2.setText(jsonArray.get(i).toString());
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
                Toast.makeText(ProfilActivity.this, getString(R.string.ErrorInternet),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    public void getUserAvatar(String urlPhotoUser){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlPhotoUser,new Response.Listener<JSONArray>(){
            @Override

            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    String urlPhoto = jsonArray.get(0).toString();
                    new MyAsyncTask(photo).execute(urlPhoto);
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
                Toast.makeText(ProfilActivity.this, getString(R.string.ErrorInternet),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView photoVue;

        public MyAsyncTask(ImageView img){
            photoVue = img;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap image = null;
            String url = urls[0];

            try {
                InputStream streamIn = new java.net.URL(url).openStream();
                image = BitmapFactory.decodeStream(streamIn);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }

        protected void onPostExecute(Bitmap image){
            if(image!= null){
                photoVue.setImageBitmap(image);
            }
            else
                Toast.makeText(ProfilActivity.this,R.string.ErrorImage,Toast.LENGTH_SHORT).show();
        }
    }
}
