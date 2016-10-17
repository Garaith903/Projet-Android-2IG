package com.henallux.deal_it_version_1.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.henallux.deal_it_version_1.exception.IdException;
import com.henallux.deal_it_version_1.model.Consultation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

public class SearchActivityAnnouncement extends AppCompatActivity {

    private Context ctx;
    private Bundle bundle;
    private int idAnnouncemet;

    private String url;
    private String urlConsultation;

    private String createIdConsultation;
    private String currentUserID;
    private String idPosterFocussedAnno;
    private String dateFocussedAnnouncement;
    private String titleFocussedAnnouncement;
    private String categoryFocussedAnnouncement;
    private String typeFocussedAnnouncement;
    private String descrFocussedAnnouncement;
    private String priceFocussedAnnouncement;
    private Consultation consult;

    private TextView textViewID;
    private TextView textViewDate;
    private TextView textViewTitle;
    private TextView textViewCategory;
    private TextView textViewType;
    private TextView textViewDescription;
    private TextView textViewPrice;
    private TextView currency;
    private TextView textPrice;

    private GregorianCalendar gC;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity_announcement);

        bundle = this.getIntent().getExtras();
        currentUserID =  bundle.getString("CurrentUserID");
        idAnnouncemet = bundle.getInt("AnnouncementID");

        gC = new GregorianCalendar();

        textViewID = (TextView) findViewById(R.id.idPoster);
        textViewDate = (TextView) findViewById(R.id.datePostAnno);
        textViewTitle = (TextView) findViewById(R.id.titreAnnouncement);
        textViewCategory = (TextView) findViewById(R.id.categoryAnnouncement);
        textViewType = (TextView) findViewById(R.id.typeAnnouncement);
        textViewDescription = (TextView) findViewById(R.id.descriptionAnno);
        textViewPrice = (TextView) findViewById(R.id.PriceAnnouncement);
        currency = (TextView) findViewById(R.id.currencyUnit);
        textPrice = (TextView) findViewById(R.id.labelPriceAnno);

        url = "http://dealitv2.azurewebsites.net/api/annonces/GetAnnouncementById/?id="+idAnnouncemet;

        getInfoFocussedAnnouncemnt(url);

        // Ajout de la consultation
        dateString = gC.get(GregorianCalendar.YEAR)+"-"+((gC.get(GregorianCalendar.MONTH))+1)+"-"+gC.get(GregorianCalendar.DAY_OF_MONTH);

        String idAnnoString = String.valueOf(idAnnouncemet);
        createIdConsultation = currentUserID.concat(idAnnoString);

        try {
            consult = new Consultation(createIdConsultation, dateString, currentUserID, idAnnouncemet);
            urlConsultation = "http://dealitv2.azurewebsites.net/api/consultations/InsererConsultation";
            addConsultation(urlConsultation);
        } catch (IdException e) {
            e.printStackTrace();
        } catch (DateException e) {
            e.printStackTrace();
        }

        textViewID.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intentionProfil = new Intent(SearchActivityAnnouncement.this, ProfilActivity.class);

                        intentionProfil.putExtra("CurrentUserID", idPosterFocussedAnno);

                        startActivity(intentionProfil);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search_activity_announcement, menu);
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

    public void getInfoFocussedAnnouncemnt(String url)
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

                        categoryFocussedAnnouncement = separated[0];
                        typeFocussedAnnouncement = separated[1];
                        titleFocussedAnnouncement = separated[2];
                        dateFocussedAnnouncement = separated[3];
                        descrFocussedAnnouncement = separated[4];
                        priceFocussedAnnouncement = separated[5];
                        idPosterFocussedAnno = separated[6];

                        textViewID.setText(idPosterFocussedAnno);
                        textViewDate.setText(dateFocussedAnnouncement);
                        textViewTitle.setText(titleFocussedAnnouncement);
                        textViewCategory.setText(categoryFocussedAnnouncement);
                        textViewType.setText(typeFocussedAnnouncement);
                        textViewDescription.setText(descrFocussedAnnouncement);

                        if (priceFocussedAnnouncement.length() < 1) {
                            textPrice.setText(" ");
                            textViewPrice.setText(" ");
                            currency.setText(" ");

                        }else {
                            textViewPrice.setText(priceFocussedAnnouncement);
                        }
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
                Toast.makeText(SearchActivityAnnouncement.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void addConsultation(String urlConsultation)
    {
        JSONObject consultationJsonObject = new JSONObject();
        try
        {
            consultationJsonObject.put("idConsultation",consult.getIdConsultation());
            consultationJsonObject.put("dateConsultation",consult.getDateConstultationString());
            consultationJsonObject.put("idUtilisateur",consult.getIdViewer());
            consultationJsonObject.put("idRefAnnonce",consult.getIdAnnouncement());

        }catch(JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, urlConsultation, consultationJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                //Si on arrive ici c'est que la consultation a bien été rajoutée
                Toast.makeText(SearchActivityAnnouncement.this, R.string.AddConsultationWellDone, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {;
                volleyError.printStackTrace();
                volleyError.getCause();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectReq);
    }

}
