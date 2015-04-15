package com.example.nitish.border_wait_times;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class ContactUs extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;

    private String[] menu;

    String colorName = "#33CCCC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        underline();
        navigationDrawer();

        readFile();
        listView.setBackgroundColor(Color.parseColor(colorName));

        TextView text = (TextView) findViewById(R.id.textView_sent);
        if(sendEmail() == true){
            text.setText("Your email was sent! Thank You.");
        }
        else{
            text.setText("An error had occurred. We apologize for inconvenience. Please try again later.");
        }
    }

    private void navigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_contact);
        listView = (ListView) findViewById(R.id.left_drawer_contact);

        menu = getResources().getStringArray(R.array.menu);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,menu));

        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_contact);
        drawerListener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.navigation_drawer,R.string.drawer_open){
            @Override
            public void onDrawerOpened(View drawerView) {
                String color = "#" + "cccccc";
                View v = listView.getChildAt(3);
                v.setBackgroundColor(Color.parseColor(color));
                //Toast.makeText(ContactUs.this,"Drawer Opened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Toast.makeText(ContactUs.this,"Drawer Closed", Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true); //action bar menu button clickable now
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
    }

    private boolean sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");

        i.putExtra(Intent.EXTRA_EMAIL, new String[] { "nsbajaj@myseneca.ca" });
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(i, "Choose an Email client :"));
            return true;
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(ContactUs.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void underline() {
        TextView contactUs = (TextView) findViewById(R.id.textView_contactUs);

        contactUs.setPaintFlags(contactUs.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // title
    }

    private void readFile() {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout_contactUs);
        File dir = getFilesDir();
        File file = new File(dir,"background.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner in;

        if (file.exists()) {
            try{
                in = new Scanner(file);
                while(in.hasNext()){
                    String input = in.next();
                    if(input != null){
                        colorName = input;
                        currentLayout.setBackgroundColor(Color.parseColor(colorName));
                    }
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
        if(drawerListener.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, menu[position] + "was selected", Toast.LENGTH_LONG).show();
        selectItem(position);

        String input = (String) parent.getItemAtPosition(position);

        if(menu[0].equals(input)){
            drawerLayout.closeDrawer(Gravity.LEFT);
            Intent myIntent = new Intent(this,MainActivity.class);
            startActivity(myIntent);
            this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }
        else if(menu[1].equals(input)){
            drawerLayout.closeDrawer(Gravity.LEFT);
            Intent myIntent = new Intent(this,Favourite.class);
            startActivity(myIntent);
            this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }
        else if(menu[2].equals(input)){
            drawerLayout.closeDrawer(Gravity.LEFT);
            Intent myIntent = new Intent(this,Settings.class);
            startActivity(myIntent);
            this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }
        else if(menu[3].equals(input)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    private void selectItem(int position) {
        listView.setItemChecked(position, true);
        setTitle(menu[position]);
    }
}
