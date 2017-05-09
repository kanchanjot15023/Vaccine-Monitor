package com.example.pardeepkaur.vaccinemoniter1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Activity9 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_9);
        String email,pname;
        Intent tt1 = getIntent();
        Bundle extras = tt1.getExtras();
        email = extras.getString("email");
        pname=extras.getString("name");
        String zero="0";
        String temp =(String)extras.get("temppp");
        String light =(String)extras.get("lighttt");
        String insu =(String)extras.get("insuuu");
        TextView t1;
        t1 = (TextView) findViewById(R.id.temp);
        t1.setText(temp);
        TextView t2 = (TextView) findViewById(R.id.light);
        t2.setText(light);
        TextView t3 = (TextView) findViewById(R.id.insu);
        t3.setText(insu);
    }

    public void itemClicked(View view)
    {

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
