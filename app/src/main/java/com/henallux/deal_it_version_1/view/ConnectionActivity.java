package com.henallux.deal_it_version_1.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.henallux.deal_it_version_1.R;
import com.henallux.deal_it_version_1.controller.Controller;
import com.henallux.deal_it_version_1.dataBase.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

public class ConnectionActivity extends AppCompatActivity {

    private Button connectButton;
    private EditText loginET, pwdET;
    private String loginIn, pwdIn;
    private Controller controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        controller = new Controller();

        connectButton = (Button) findViewById(R.id.ConnectionButton);

        connectButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loginET = (EditText) findViewById(R.id.LoginEditText);
                        pwdET = (EditText) findViewById(R.id.PasswordEditText);

                        loginIn = loginET.getText().toString();

                        String password = pwdET.getText().toString();
                        //String password = "motdepasse09";

                        pwdIn = null;

                        try
                        {
                            pwdIn = controller.cryptPwd(password);
                        }
                        catch (NoSuchAlgorithmException e)
                        {
                            e.printStackTrace();
                        }
                        String url = "http://dealitv2.azurewebsites.net/api/utilisateurs/CheckID/?login="+ loginIn + "&password="+ pwdIn;

                        connection(url);

                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_connection, menu);
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


    public void connection(String url)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try
                {
                    JSONArray Json = new JSONArray(response);

                    if(Json.get(0).equals("0"))
                    {
                        //L'utilisateur est bien loggé
                        Intent intention = new Intent(ConnectionActivity.this, MainActivity.class);

                        intention.putExtra("CurrentUserID", loginIn);

                        startActivity(intention);
                        Toast.makeText(ConnectionActivity.this, getString(R.string.ConnectionEtablished) + " " + loginIn, Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        //Mauvaise connexion
                        Toast.makeText(ConnectionActivity.this, getString(R.string.ErrorConnection),Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ConnectionActivity.this, getString(R.string.ErrorInternet),Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
