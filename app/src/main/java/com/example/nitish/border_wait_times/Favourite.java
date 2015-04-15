package com.example.nitish.border_wait_times;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Favourite extends ActionBarActivity implements AdapterView.OnItemClickListener{

    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CAPTION = "caption";

    // Title
    String[] title = {""};

    // SectionHeaders
    private final String[] subHeader = new String[]{"Favourite Custom's Office / Locations"};

    // Section Contents
    //private final static String[] notes = null;

    // MENU - ListView
    private ListView addJournalEntryItem;

    // Adapter for ListView Contents
    private SeparatedListAdapterCustoms adapter;

    // ListView Contents
    private ListView journalListView;

    public Map<String, ?> createItem(String title, String caption)
    {
        Map<String, String> item = new HashMap<String, String>();
        item.put(ITEM_TITLE, title);
        item.put(ITEM_CAPTION, caption);
        return item;
    }

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;

    private String[] menu;

    String colorName = "#33CCCC";

    MyDBHandler myDB;

    int favouriteRows = 0;

    String[] favourite = new String[]{" "};

    String url = "https://www.google.ca/?gws_rd=ssl#q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        database();
        layout();
        navigationDrawer();

        readFile();
        listView.setBackgroundColor(Color.parseColor(colorName));
    }

    private void database() {
        myDB = new MyDBHandler(this,null,null,1);

        //Get all the customs office, no duplicates
        Cursor favouriteCursor = myDB.favouriteList("location","favourite");

        //getCount
        favouriteRows = favouriteCursor.getCount();

        //Setting size
        setFavouriteSize(favouriteRows);

        //Fetching values
        distinctFavourite(favouriteCursor);
    }

    private void setFavouriteSize(int favouriteSize){
        favourite = new String[favouriteSize];
    }

    private void distinctFavourite(Cursor cursor) {
        if (cursor.moveToFirst()) {
            for(int i = 0; i < favouriteRows; i++){
                favourite[i] = cursor.getString(0);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    private void layout() {
        //ListView with subheaders
        // Interactive Tools
        final ArrayAdapter<String> journalEntryAdapter = new ArrayAdapter<String>(this, R.layout.menuitem_favourite, title); //title
        // AddJournalEntryItem
        addJournalEntryItem = (ListView) this.findViewById(R.id.menuitem_favourite);
        addJournalEntryItem.setAdapter(journalEntryAdapter);
        addJournalEntryItem.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long duration)
            {
                String item = journalEntryAdapter.getItem(position);
                //Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        // Create the ListView Adapter
        adapter = new SeparatedListAdapterCustoms(this);
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, R.layout.list_item_favourite, favourite); //list

        // Add Sections
        for (int i = 0; i < subHeader.length; i++){
            adapter.addSection(subHeader[i], listadapter); //header
        }

        // Get a reference to the ListView holder
        journalListView = (ListView) this.findViewById(R.id.list_favourite);

        // Set the adapter on the ListView holder
        journalListView.setAdapter(adapter);

        // Listen for Click events
        journalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                String name = (String) adapter.getItem(position);
                //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                String input = (String) parent.getItemAtPosition(position);
                for(int i = 0; i < favourite.length; i++){
                    if(input.equals(favourite[i])){
                        Intent intent = new Intent(Favourite.this, Browser.class);
                        String temp = url + favourite[i];
                        intent.putExtra("url",temp);
                        startActivity(intent);
                        Favourite.this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                        break;
                    }
                }
            }
        });
    }

    private void navigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_favourite);
        listView = (ListView) findViewById(R.id.left_drawer_favourite);

        menu = getResources().getStringArray(R.array.menu);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,menu));

        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_favourite);
        drawerListener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.navigation_drawer,R.string.drawer_open){
            @Override
            public void onDrawerOpened(View drawerView) {
                String color = "#" + "cccccc";
                View v = listView.getChildAt(1);
                v.setBackgroundColor(Color.parseColor(color));
                //Toast.makeText(Favourite.this,"Drawer Opened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Toast.makeText(Favourite.this,"Drawer Closed", Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true); //action bar menu button clickable now
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
    }

    private void readFile() {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout_favourite);
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
        getMenuInflater().inflate(R.menu.menu_favourite, menu);
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
}