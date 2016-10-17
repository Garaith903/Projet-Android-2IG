package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.deal_it_version_1.R;

public class Manager_Announcement extends AppCompatActivity {

    private Bundle bundle;
    private ImageButton buttonHistory;
    private ImageButton buttonMyAnnouncemnt;

    private String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager__announcement);

        bundle = this.getIntent().getExtras();
        currentUser =  bundle.getString("CurrentUserID");

        buttonMyAnnouncemnt = (ImageButton) findViewById(R.id.MyAnnouncementsList);
        buttonMyAnnouncemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentionMyAnnouncement = new Intent(Manager_Announcement.this, UserAnnouncementListActivity.class);
                intentionMyAnnouncement.putExtra("CurrentUserID", currentUser);
                startActivity(intentionMyAnnouncement);
            }
        });

        buttonHistory = (ImageButton) findViewById(R.id.MyHistory);
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentionHistory = new Intent(Manager_Announcement.this, UserHistoryListActivity.class);
                intentionHistory.putExtra("CurrentUserID", currentUser);
                startActivity(intentionHistory);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manager__announcement, menu);
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
                Intent intentProf = new Intent(Manager_Announcement.this,ProfilActivity.class);
                intentProf.putExtra("CurrentUserID", currentUser);
                startActivity(intentProf);
                return true;
            case R.id.search :
                Intent intentSearch = new Intent(Manager_Announcement.this,SearchActivity.class);
                intentSearch.putExtra("CurrentUserID", currentUser);
                startActivity(intentSearch);
                return true;
            case R.id.add_Announcement :
                Intent intentAdd = new Intent(Manager_Announcement.this,AddAnnouncementActivity.class);
                intentAdd.putExtra("CurrentUserID", currentUser);
                startActivity(intentAdd);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
