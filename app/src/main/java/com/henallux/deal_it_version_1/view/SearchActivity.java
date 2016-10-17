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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import com.henallux.deal_it_version_1.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SearchActivity extends AppCompatActivity {

    private Bundle bundle;

    private Spinner categoryS;
    private Switch activateDate;
    private RadioGroup rgType;
    private RadioButton rbType;
    private RadioButton rbAll;
    private RadioButton rbOffer;
    private RadioButton rbDemand;
    private DatePicker endDate;
    private DatePicker startDate;
    private Button bSearch;

    private String currentUserID;
    private String categorySelected;
    private String typeSelected;
    private String startSearchDate;
    private String endSearchDate;

    private int dayStartSearch, monthStartSearch, yearStartSearch, dayEndSearch, monthEndSearch, yearEndSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bundle = this.getIntent().getExtras();
        currentUserID =  bundle.getString("CurrentUserID");

        //Creation et initialisation du spinner depuis un stringarray
        categoryS = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.CategoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryS.setAdapter(adapter);

        categorySelected = categoryS.getSelectedItem().toString();

        categoryS.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
                        categorySelected = getWriteCategoryName(parent.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView parent) {
                    }
                }
        );
        //radio boutton
        rgType = (RadioGroup) findViewById(R.id.groupType);

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

        //Switch
        activateDate = (Switch) findViewById(R.id.activateResearchDate);
        activateDate.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            startDate.setEnabled(true);
                            endDate.setEnabled(true);
                        } else {
                            startDate.setEnabled(false);
                            endDate.setEnabled(false);

                        }
                    }
                }
        );

        //Dates pickers
        startDate = (DatePicker) findViewById(R.id.StartDatePicker);
        startDate.setMaxDate(new Date().getTime());
        startDate.setEnabled(false);

        endDate = (DatePicker) findViewById(R.id.EndDatePicker);
        endDate.setMaxDate(new Date().getTime());
        endDate.setEnabled(false);

        //bouton recherche
        bSearch = (Button) findViewById(R.id.validateResearch);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkDateArg = true;
                Intent intention = new Intent(SearchActivity.this, SearchActivityListView.class);

                if (activateDate.isChecked()) {
                    dayStartSearch= startDate.getDayOfMonth();
                    monthStartSearch = (startDate.getMonth() + 1);
                    yearStartSearch = startDate.getYear();

                    dayEndSearch= endDate.getDayOfMonth();
                    monthEndSearch = (endDate.getMonth() + 1);
                    yearEndSearch = endDate.getYear();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    Date strEndDate = null;
                    Date strStartDate = null;
                    try {
                        strEndDate = sdf.parse(dayEndSearch+"/"+monthEndSearch+"/"+yearEndSearch);
                        strStartDate = sdf.parse(dayStartSearch+"/"+monthStartSearch+"/"+yearStartSearch);

                        if (strEndDate.before(strStartDate)) {
                            Toast.makeText(SearchActivity.this, R.string.ErrorDateCompare, Toast.LENGTH_LONG).show();
                            checkDateArg = false;
                        }
                        else
                        {
                            //on reconstruit les dates sous forme de chaine yyyy-MM-dd
                            startSearchDate = yearStartSearch+"-"+((monthStartSearch<10)?"0":"")+monthStartSearch+"-"+((dayStartSearch<10)?"0":"")+dayStartSearch;
                            endSearchDate = yearEndSearch+"-"+((monthEndSearch<10)?"0":"")+monthEndSearch+"-"+((dayEndSearch<10)?"0":"")+dayEndSearch;

                            checkDateArg = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    //Obtention date systeme
                    final Calendar cal = Calendar.getInstance();

                    //on reconstruit les dates sous forme de chaine yyyy-MM-dd
                    startSearchDate = cal.get(Calendar.YEAR)+"-"+(((cal.get(Calendar.MONTH)+1)<10)?"0":"")+(cal.get(Calendar.MONTH)+1)+"-"+((cal.get(Calendar.DAY_OF_MONTH)<10)?"0":"")+cal.get(Calendar.DAY_OF_MONTH);
                    endSearchDate = cal.get(Calendar.YEAR)+"-"+(((cal.get(Calendar.MONTH)+1)<10)?"0":"")+(cal.get(Calendar.MONTH)+1)+"-"+((cal.get(Calendar.DAY_OF_MONTH)<10)?"0":"")+cal.get(Calendar.DAY_OF_MONTH);
                }

                if(checkDateArg) {

                    intention.putExtra("CurrentUserID", currentUserID);
                    intention.putExtra("Category", categorySelected);
                    intention.putExtra("Type", typeSelected);
                    intention.putExtra("SearchByDate", activateDate.isChecked());
                    intention.putExtra("StartDate", startSearchDate);
                    intention.putExtra("EndDate", endSearchDate);

                    startActivity(intention);
                }
            }
        });
    }
    //Permet de récupérer les bonne chaines de caractère peut importe la langue du téléphone
    public String getWriteCategoryName(String item)
    {
        String cat;
        if(item.equals(getResources().getString(R.string.spinnerText1)))
        {
            cat = getResources().getString(R.string.Category1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
                Intent intentProf = new Intent(SearchActivity.this,ProfilActivity.class);
                intentProf.putExtra("CurrentUserID", currentUserID);
                startActivity(intentProf);
                return true;
            case R.id.add_Announcement :
                Intent intentAdd = new Intent(SearchActivity.this,AddAnnouncementActivity.class);
                intentAdd.putExtra("CurrentUserID", currentUserID);
                startActivity(intentAdd);
                return true;
            case R.id.manager_Announcement :
                Intent intentManage = new Intent(SearchActivity.this,Manager_Announcement.class);
                intentManage.putExtra("CurrentUserID", currentUserID);
                startActivity(intentManage);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
