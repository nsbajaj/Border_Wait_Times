package com.example.nitish.border_wait_times;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class data extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;

    private String[] menu;

    String colorName = "#33CCCC";

    MyDBHandler myDB;

    int type;
    String name = null;

    int count = 0;

    Button b1;

    boolean check = false;

    TextView text;

    String[] lastupdated;
    String[] commercialflow;
    String[] travellersflow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Intent intent = getIntent();
        type = Integer.parseInt(intent.getStringExtra("type"));
        name = intent.getStringExtra("name");

        navigationDrawer();
        readFile();
        listView.setBackgroundColor(Color.parseColor(colorName));

        myDB = new MyDBHandler(this,null,null,1);

        b1 = (Button) findViewById(R.id.button_favourite);
        text = (TextView) findViewById(R.id.textview_data);
        text.setTypeface(null, Typeface.BOLD);
        text.setPaintFlags(text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        text.setTextColor(Color.BLACK);

        text.setText("Timings For " + name);

        checkFavourite();

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(check == false) {
                    addToFavourite();
                }
                if(check == true){
                    removeFromFavourite();
                }
                check = true;
            }
        });

        ArrayList<SearchResults> searchResults = GetSearchResults();

        final ListView lv1 = (ListView) findViewById(R.id.ListView01);
        lv1.setAdapter(new MyCustomBaseAdapter(this, searchResults));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                SearchResults fullObject = (SearchResults)o;
                //Toast.makeText(data.this, "You have chosen: " + " " + fullObject.getLastUpdated(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<SearchResults> GetSearchResults(){
        ArrayList<SearchResults> results = new ArrayList<SearchResults>();

        Cursor getAllRows = null;
        if(type == 0){
            getAllRows = myDB.getData("customsoffice", name);
        }
        if(type == 1) {
            getAllRows = myDB.getData("location", name);
        }

        //get Count
        count = getAllRows.getCount();

        Log.i("count", String.valueOf(count));

        //Set Size
        lastupdated = new String[count];
        commercialflow = new String[count];
        travellersflow = new String[count];

        if (getAllRows.moveToFirst()) {
            for(int i = 0; i < count; i++){
                lastupdated[i] = getAllRows.getString(0);
                commercialflow[i] = getAllRows.getString(1);
                travellersflow[i] = getAllRows.getString(2);
                getAllRows.moveToNext();
            }
        }
        getAllRows.close();

        for(int i = 0; i < count; i++){
            //lastupdated[i] = lastupdated[i].substring(0,)
        }

        for(int i = 0; i < count; i++) {
            SearchResults sr1 = new SearchResults();
            sr1.setLastUpdated("Last Updated: " + lastupdated[i]);
            sr1.setCommercialFlow("Commercial Flow: " + commercialflow[i]);
            sr1.setTravellersFlow("Travellers Flow: " + travellersflow[i]);
            results.add(sr1);
        }
        return results;
    }

private void removeFromFavourite(){
        Cursor favouriteCursor = null;
        if(type == 0){
            favouriteCursor = myDB.removeFavourite("customsoffice", name);
        }
        if(type == 1) {
            favouriteCursor = myDB.removeFavourite("location", name);
        }
        count = favouriteCursor.getCount();
        b1.setText("Removed! Click here to add this Location!");
        check = false;
    }

    private void addToFavourite(){
        Cursor favouriteCursor = null;
        if(type == 0){
            favouriteCursor = myDB.addFavourite("customsoffice", name);
        }
        if(type == 1) {
            favouriteCursor = myDB.addFavourite("location", name);
        }
        count = favouriteCursor.getCount();
        b1.setText("Added! Click here to remove this Location!");
    }

    private void checkFavourite() {
        String tempString = null;
        if(type == 0){
            tempString = "customsoffice";
        }
        if(type == 1){
            tempString = "location";
        }

        Cursor checkCursor = myDB.checkFavourite("favourite",tempString,name);

        int[] temp = new int[checkCursor.getCount()];

        if (checkCursor.moveToFirst()) {
            for(int i = 0; i < checkCursor.getCount(); i++){
                temp[i] = Integer.parseInt(checkCursor.getString(0));
                checkCursor.moveToNext();
            }
        }
        checkCursor.close();

        if(temp[0] == 1){
            b1.setText("This Location is already in your Favourite's, Click here to remove it!");
            check = true;
        }
        if(temp[0] == 0){
            b1.setText("Add this location to Favourite!");
            check = false;
        }

    }

    private void navigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_data);
        listView = (ListView) findViewById(R.id.left_drawer_data);

        menu = getResources().getStringArray(R.array.menu);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,menu));

        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_data);
        drawerListener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.navigation_drawer,R.string.drawer_open){
            @Override
            public void onDrawerOpened(View drawerView) {
                String color = "#" + "cccccc";
                View v = listView.getChildAt(0);
                v.setBackgroundColor(Color.parseColor(color));
                //Toast.makeText(data.this,"Drawer Opened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Toast.makeText(data.this,"Drawer Closed", Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true); //action bar menu button clickable now
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
    }

    private void readFile() {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout_data);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, menu[position] + "was selected", Toast.LENGTH_LONG).show();
        selectItem(position);

        String input = (String) parent.getItemAtPosition(position);

        if(menu[0].equals(input)){
            drawerLayout.closeDrawer(Gravity.LEFT);
            Intent myIntent = new Intent(this,MainActivity.class);
            startActivity(myIntent);
            this.overridePendingTransition(R.anim.animation2_lr, R.anim.animation2_rl);
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
            Intent myIntent = new Intent(this,ContactUs.class);
            startActivity(myIntent);
            this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }
    }
    private void selectItem(int position) {
        listView.setItemChecked(position, true);
        setTitle(menu[position]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        data.this.overridePendingTransition(R.anim.animation2_lr, R.anim.animation2_rl);
    }
}
