package com.ewu.UKNOapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CampusLinksActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_links);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.campus_links, menu);
        return true;
    }
    
}
