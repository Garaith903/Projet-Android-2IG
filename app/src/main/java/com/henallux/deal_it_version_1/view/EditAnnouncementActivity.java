package com.henallux.deal_it_version_1.view;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class EditAnnouncementActivity extends AppCompatActivity {

    private Bundle bundle;
    private String currentUserID;
    private String announcementToModifyID;
    private String dataAnnouncementToModify;
    private String urlGetAnnouncement;

    private Spinner categoryS;
    private RadioGroup rgType;
    private RadioButton rbType;
    private RadioButton rbType1;
    private RadioButton rbType2;
    private EditText titleEdit;
    private EditText descriptionEdit;
    private EditText priceEdit;
    private Button buttonModify;

    private String idPosterFocussedAnno;
    private String dateFocussedAnnouncement;
    private String titleFocussedAnnouncement;
    private String categoryFocussedAnnouncement;
    private String typeFocussedAnnouncement;
    private String descrFocussedAnnouncement;
    private String priceFocussedAnnouncement;
    private ArrayAdapter<CharSequence> adapter;

    private GregorianCalendar currentDate;
    private String currentDateModification;
    private String titleAnnouncement;
    private String descriptionAnnouncement;
    private String priceAnnouncement;
    private Double priceConvert;

    private String categorySelected;
    private String typeSelected;
    private String urlIdMax;

    private int idAnnoncement;
    private Announcement announcementModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_announcement);

        bundle = this.getIntent().getExtras();
        currentUserID = bundle.getString("CurrentUserID");
        announcementToModifyID = bundle.getString("AnnouncementID");

        //creation du spinner de catégorie
        categoryS = (Spinner) findViewById(R.id.spinnerCategory);

        //On Remplis le spinner à partir de stringArray
        adapter = ArrayAdapter.createFromResource(this, R.array.CategoryArray_addAnnouncement, android.R.layout.simple_spinner_item);
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

        //creation du radio group et des radio buttons
        rgType = (RadioGroup) findViewById(R.id.typeRadioGrp);
        rbType1 = (RadioButton)findViewById(R.id.typeRadio1);
        rbType2 = (RadioButton)findViewById(R.id.typeRadio2);

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

        urlGetAnnouncement = "http://dealitv2.azurewebsites.net/api/annonces/GetAnnouncementById/?id="+Integer.valueOf(announcementToModifyID);
        getDataAnnouncemntToModify(urlGetAnnouncement);

        buttonModify = (Button) findViewById(R.id.button_modify_announcement);
        buttonModify.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        currentDateModification = checkDate();
                        titleAnnouncement = titleEdit.getText().toString();
                        descriptionAnnouncement = descriptionEdit.getText().toString();
                        priceAnnouncement = priceEdit.getText().toString();

                        if(checkCategory(categorySelected) && checkTitle(titleAnnouncement) && checkDescription(descriptionAnnouncement) && checkPrice(priceAnnouncement))
                        {
                            urlIdMax = "http://dealitv2.azurewebsites.net/api/annonces/RechercheIdMaxAnnouncement";
                            getMaxIdAnnouncement(urlIdMax);
                            try
                            {
                                announcementModify = new Announcement(Integer.valueOf(announcementToModifyID), categorySelected, typeSelected, titleAnnouncement, descriptionAnnouncement, currentDateModification, priceConvert, currentUserID);

                                String urlConsult = "http://dealitv2.azurewebsites.net/api/consultations/GetAllConsultationByAnnouncementID/?annoID="+(Integer.valueOf(announcementToModifyID));
                                getConsultationOfSelectedAnnouncement(urlConsult);

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
        getMenuInflater().inflate(R.menu.menu_edit_announcement, menu);
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

    public void getDataAnnouncemntToModify(String urlGetData)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlGetData, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArray)
            {
                try
                {
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String[] separated = (jsonArray.get(i).toString()).split("#");

                        categoryFocussedAnnouncement = separated[0];
                        typeFocussedAnnouncement = separated[1];
                        titleFocussedAnnouncement = separated[2];
                        dateFocussedAnnouncement = separated[3];
                        descrFocussedAnnouncement = separated[4];
                        priceFocussedAnnouncement = separated[5];
                        idPosterFocussedAnno = separated[6];
                    }
                    ArrayAdapter myAdap = (ArrayAdapter) categoryS.getAdapter();
                    int spinnerPosition = myAdap.getPosition(categoryFocussedAnnouncement);
                    categoryS.setSelection(spinnerPosition);
                    adapter.notifyDataSetChanged();

                    int idButton1 = rbType1.getId();
                    int idButton2 = rbType2.getId();
                    if (typeFocussedAnnouncement.toString().equals(getResources().getString(R.string.SearchRadio2))) {
                        rgType.check(idButton1);
                    }
                    else {
                        rgType.check(idButton2);
                    }
                    titleEdit.setText(titleFocussedAnnouncement);
                    descriptionEdit.setText(descrFocussedAnnouncement);

                    if(priceFocussedAnnouncement.length()>0)
                    {
                        priceEdit.setText(priceFocussedAnnouncement);
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
                Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
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
            Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorDescriptionLength), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorUsingSquare), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorTitleLength), Toast.LENGTH_SHORT ).show();
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
                Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorUsingSquare), Toast.LENGTH_SHORT ).show();
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

                Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorInternet),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    public void modifyAnnouncement(String url1)
    {

        JSONObject announcementJsonObject = new JSONObject();
        try
        {
            announcementJsonObject.put("idAnnonce", announcementModify.getIdAnnoncement());
            announcementJsonObject.put("categorie", announcementModify.getCategory());
            announcementJsonObject.put("typeAnnonce", announcementModify.getTypeAnnounce());
            announcementJsonObject.put("titreAnnonce", announcementModify.getTitleAnnounce());
            announcementJsonObject.put("dateAnnonce", announcementModify.getDateReleaseAnnounceString());
            announcementJsonObject.put("description", announcementModify.getDescription());
            announcementJsonObject.put("prix", announcementModify.getPrice());
            announcementJsonObject.put("idAnnonceur", announcementModify.getIdAnnonceur());
        }catch(JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, announcementJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                //Si on arrive ici c'est que l annonce a bien été rajoutée
                Toast.makeText(EditAnnouncementActivity.this, R.string.ModifyAnnouncementWellDone, Toast.LENGTH_SHORT).show();
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

    public void deleteAnnouncement(String urlDelete)
    {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.DELETE, urlDelete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                String urlModification = "http://dealitv2.azurewebsites.net/api/annonces/InsererAnnonce";
                modifyAnnouncement(urlModification);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
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
                    String urlDelAnnouncement = "http://dealitv2.azurewebsites.net/api/annonces/DeleteAnnouncementById/?id="+Integer.valueOf(announcementToModifyID);
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
                Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
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
                //On ne fait rien
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(EditAnnouncementActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}
