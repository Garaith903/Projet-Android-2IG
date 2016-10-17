package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.henallux.deal_it_version_1.R;
import com.henallux.deal_it_version_1.dataBase.MySingleton;
import com.henallux.deal_it_version_1.exception.DateException;
import com.henallux.deal_it_version_1.exception.DescriptionException;
import com.henallux.deal_it_version_1.exception.IdException;
import com.henallux.deal_it_version_1.exception.PriceException;
import com.henallux.deal_it_version_1.exception.TitleException;
import com.henallux.deal_it_version_1.exception.TypeException;
import com.henallux.deal_it_version_1.model.Announcement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddAnnouncementActivity extends AppCompatActivity {

    private Bundle bundle;
    private Spinner categoryS;
    private String categorySelected;
    private RadioGroup rgType;
    private RadioButton rbType;
    private EditText titleEdit;
    private EditText descriptionEdit;
    private EditText priceEdit;
    private Button buttonAdd;

    private Announcement announcementAdd;
    private GregorianCalendar currentDate;
    private String currentUserID;
    private String typeSelected;
    private String titleAnnouncementt;
    private String descriptionAnnouncement;
    private String priceAnnouncement;
    private Double priceConvert;
    private String currentDateAdd;
    private int idAnnoncement;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__announcement);

        bundle = this.getIntent().getExtras();
        currentUserID =  bundle.getString("CurrentUserID");

        categoryS = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.CategoryArray_addAnnouncement, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryS.setAdapter(adapter);
        categorySelected = categoryS.getSelectedItem().toString();
        categoryS.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        categorySelected = getWriteCategoryName(parent.getSelectedItem().toString());
                    }

                    @Override
                    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                        categorySelected = parent.getSelectedItem().toString();
                    }
                }
        );

        //radio boutton
        rgType = (RadioGroup) findViewById(R.id.typeRadioGrp);

        rbType = (RadioButton) findViewById(rgType.getCheckedRadioButtonId());
        typeSelected = getWriteTypeName(rbType.getText().toString());

        rgType.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        rbType = (RadioButton) findViewById(checkedId);
                        typeSelected = getWriteTypeName(rbType.getText().toString());
                    }
                }
        );

        titleEdit = (EditText) findViewById(R.id.title_announcement);
        descriptionEdit = (EditText) findViewById(R.id.description_announcement);
        priceEdit = (EditText) findViewById(R.id.prize);

        buttonAdd = (Button) findViewById(R.id.button_add_announcement);

        buttonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        currentDateAdd = checkDate();
                        //categorySelected
                        //typeSelected
                        titleAnnouncementt = titleEdit.getText().toString();
                        descriptionAnnouncement = descriptionEdit.getText().toString();
                        priceAnnouncement = priceEdit.getText().toString();

                        if(checkCategory(categorySelected) && checkTitle(titleAnnouncementt) && checkDescription(descriptionAnnouncement) && checkPrice(priceAnnouncement))
                        {
                            url = "http://dealitv2.azurewebsites.net/api/annonces/RechercheIdMaxAnnouncement";
                            getMaxIdAnnouncement(url);

                            try
                            {
                                announcementAdd = new Announcement((idAnnoncement+1), categorySelected, typeSelected, titleAnnouncementt, descriptionAnnouncement, currentDateAdd, priceConvert, currentUserID);
                                String urlAdd = "http://dealitv2.azurewebsites.net/api/annonces/InsererAnnonce";

                                addAnnouncement(urlAdd);

                            } catch (PriceException e) {
                                e.printStackTrace();
                            } catch (DescriptionException e) {
                                e.printStackTrace();
                            } catch (TypeException e) {
                                e.printStackTrace();
                            } catch (TitleException e) {
                                e.printStackTrace();
                            } catch (DateException e) {
                                e.printStackTrace();
                            } catch (IdException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__announcement, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.profil :
                Intent intentProf = new Intent(AddAnnouncementActivity.this,ProfilActivity.class);
                intentProf.putExtra("CurrentUserID", currentUserID);
                startActivity(intentProf);
                return true;
            case R.id.search :
                Intent intentSearch = new Intent(AddAnnouncementActivity.this,SearchActivity.class);
                intentSearch.putExtra("CurrentUserID", currentUserID);
                startActivity(intentSearch);
                return true;
            case R.id.manager_Announcement :
                Intent intentManage = new Intent(AddAnnouncementActivity.this,Manager_Announcement.class);
                intentManage.putExtra("CurrentUserID", currentUserID);
                startActivity(intentManage);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public String checkDate()
    {
        currentDate = new GregorianCalendar();
        return currentDate.get(GregorianCalendar.YEAR)+ "-" + ((currentDate.get(GregorianCalendar.MONTH))+1) + "-" + currentDate.get(GregorianCalendar.DAY_OF_MONTH);
    }

    public boolean checkCategory(String cat)
    {
        if(cat.equals(getResources().getString(R.string.spinnerText0)))
        {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkPrice(String priceCheck)
    {
        if(priceCheck.length() > 0) {

                priceConvert = Double.parseDouble(priceCheck);
                return true;
        }
        else {
            priceConvert = null;
            return true;
        }

    }


    public boolean checkDescription(String descript)
    {
        Boolean noSquare = true;

        if(descript.length() < 1 && descript.length() > 500)
        {
            Toast.makeText(AddAnnouncementActivity.this, getString(R.string.ErrorDescriptionLength), Toast.LENGTH_SHORT ).show();
            return false;
        }
        else
        {
            for (int i = 0; i < descript.length(); i++)
            {
                if(descript.charAt(i) == '#')
                {
                    noSquare = false;
                    break;
                }
            }
            if(!noSquare)
            {
                Toast.makeText(AddAnnouncementActivity.this, getString(R.string.ErrorUsingSquare), Toast.LENGTH_SHORT ).show();
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    public boolean checkTitle(String titleCheck)
    {
        Boolean noSquare = true;

        if(titleCheck.length() < 1 && titleCheck.length() > 60)
        {
            Toast.makeText(AddAnnouncementActivity.this, getString(R.string.ErrorTitleLength), Toast.LENGTH_SHORT ).show();
            return false;
        }
        else
        {
            for (int i = 0; i < titleCheck.length(); i++)
            {
                if(titleCheck.charAt(i) == '#')
                {
                    noSquare = false;
                }
            }
            if(!noSquare)
            {
                Toast.makeText(AddAnnouncementActivity.this,  getString(R.string.ErrorUsingSquare), Toast.LENGTH_SHORT ).show();
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    public String getWriteTypeName(String item)
    {
        String typeCorr;
        if(item.equals(getResources().getString(R.string.SearchRadio1)))
        {
            typeCorr = getResources().getString(R.string.Type1);
        }
        else if (item.equals(getResources().getString(R.string.SearchRadio2)))
        {
            typeCorr = getResources().getString(R.string.Type2);
        }
        else
        {
            typeCorr = getResources().getString(R.string.Type3);
        }

        return typeCorr;
    }

    public String getWriteCategoryName(String item)
    {
        String cat;
        if(item.equals(getResources().getString(R.string.spinnerText0)))
        {
            cat = getResources().getString(R.string.Category0);
        }
        else if (item.equals(getResources().getString(R.string.spinnerText2)))
        {
            cat = getResources().getString(R.string.Category2);
        }
        else if (item.equals(getResources().getString(R.string.spinnerText3)))
        {
            cat = getResources().getString(R.string.Category3);
        }
        else if (item.equals(getResources().getString(R.string.spinnerText4)))
        {
            cat = getResources().getString(R.string.Category4);
        }
        else if (item.equals(getResources().getString(R.string.spinnerText5)))
        {
            cat = getResources().getString(R.string.Category5);
        }
        else if (item.equals(getResources().getString(R.string.spinnerText6)))
        {
            cat = getResources().getString(R.string.Category6);
        }
        else if (item.equals(getResources().getString(R.string.spinnerText7)))
        {
            cat = getResources().getString(R.string.Category7);
        }
        else
        {
            cat = getResources().getString(R.string.Category8);
        }

        return cat;
    }

    public void getMaxIdAnnouncement(String url)
    {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    idAnnoncement = (int) jsonArray.get(0);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(AddAnnouncementActivity.this, getString(R.string.ErrorInternet),Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    //Ajouter l'annonce
    public void addAnnouncement(String url1)
    {

        JSONObject announcementJsonObject = new JSONObject();
        try
        {
            announcementJsonObject.put("idAnnonce", announcementAdd.getIdAnnoncement());
            announcementJsonObject.put("categorie", announcementAdd.getCategory());
            announcementJsonObject.put("typeAnnonce", announcementAdd.getTypeAnnounce());
            announcementJsonObject.put("titreAnnonce", announcementAdd.getTitleAnnounce());
            announcementJsonObject.put("dateAnnonce", announcementAdd.getDateReleaseAnnounceString());
            announcementJsonObject.put("description", announcementAdd.getDescription());
            announcementJsonObject.put("prix", announcementAdd.getPrice());
            announcementJsonObject.put("idAnnonceur", announcementAdd.getIdAnnonceur());
        }catch(JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, announcementJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                //Si on arrive ici c'est que l annonce a bien été rajoutée
                Toast.makeText(AddAnnouncementActivity.this, R.string.AddAnnouncementWellDone, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                volleyError.getCause();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
