package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.henallux.deal_it_version_1.R;

public class MainActivity extends AppCompatActivity {

    private Bundle bundle;
    private ImageButton profil;
    private ImageButton search;
    private ImageButton add_Announcement;
    private ImageButton manage_Announcement;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle = this.getIntent().getExtras();
        currentUserID =  bundle.getString("CurrentUserID");

        profil = (ImageButton)findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intention = new Intent(MainActivity.this, ProfilActivity.class);

                intention.putExtra("CurrentUserID", currentUserID);

                startActivity(intention);
            }
        });

        search = (ImageButton)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intentionSearch = new Intent(MainActivity.this, SearchActivity.class);

                intentionSearch.putExtra("CurrentUserID", currentUserID);

                startActivity(intentionSearch);
            }
        });
        add_Announcement = (ImageButton)findViewById(R.id.add_Announcement);
        add_Announcement.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intentionSearch = new Intent(MainActivity.this, AddAnnouncementActivity.class);

                intentionSearch.putExtra("CurrentUserID", currentUserID);

                startActivity(intentionSearch);
            }
        });

        manage_Announcement = (ImageButton)findViewById(R.id.manager_Announcement);
        manage_Announcement.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intention = new Intent(MainActivity.this, Manager_Announcement.class);

                intention.putExtra("CurrentUserID", currentUserID);

                startActivity(intention);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
