package com.example.nitish.border_wait_times;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;

    //Navigation Drawer List
    private String[] menu;

    String colorName = "#33CCCC";

    SwipeRefreshLayout swipeLayout;

    int[] id;
    String[] customsOffice = new String[]{" "};
    String[] location = new String[]{" "};

    MyDBHandler myDB;

    CustomsOfficeClass office;
    LocationClass loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new MyDBHandler(this,null,null,1);

        office = new CustomsOfficeClass();
        office.customsDatabase();
        office.customsLayout();

        loc = new LocationClass();
        loc.locationDatabase();
        loc.locationLayout();

        navigationDrawer();
        tabs();

        readFile();
        listView.setBackgroundColor(Color.parseColor(colorName));
    }

    private void tabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("customsOffice");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Customs Office"); //name displayed
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("location");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Location"); //name displayed
        tabHost.addTab(tabSpec);

    }

    private void navigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);

        menu = getResources().getStringArray(R.array.menu);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,menu)); //old adapter

        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.navigation_drawer,R.string.drawer_open){
            @Override
            public void onDrawerOpened(View drawerView) {
                String color = "#" + "cccccc";
                View v = listView.getChildAt(0);
                v.setBackgroundColor(Color.parseColor(color));
                //Toast.makeText(MainActivity.this,"Drawer Opened", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Toast.makeText(MainActivity.this,"Drawer Closed", Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true); //action bar menu button clickable now
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
    }

    private void readFile() {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout_main);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayout_main);

        switch(item.getItemId()){
            case R.id.settingMenu:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                Intent myIntent = new Intent(this,Settings.class);
                startActivity(myIntent);
                this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }

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
        //Toast.makeText(this,menu[position] + "was selected", Toast.LENGTH_LONG).show();
        selectItem(position);

        String input = (String) parent.getItemAtPosition(position);

        if(menu[0].equals(input)){
            drawerLayout.closeDrawer(Gravity.LEFT);
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
    public class CustomsOfficeClass extends ActionBarActivity{

        public final static String ITEM_TITLE = "title";
        public final static String ITEM_CAPTION = "caption";

        // Title
        String[] title = {""};

        // SectionHeaders
        private final String[] customsTitle = new String[]{"Customs Office's"};

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

        int customsOfficerows = 0;

        public CustomsOfficeClass(){

        }

        public void customsLayout(){
            //ListView with subheaders
            // Interactive Tools
            final ArrayAdapter<String> journalEntryAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.menuitem_customs, title); //title
            // AddJournalEntryItem
            addJournalEntryItem = (ListView) MainActivity.this.findViewById(R.id.menuitem_customs);
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
            adapter = new SeparatedListAdapterCustoms(MainActivity.this);
            ArrayAdapter<String> listadapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item_customs, customsOffice); //list

            // Add Sections
            for (int i = 0; i < customsTitle.length; i++){

                adapter.addSection(customsTitle[i], listadapter); //header
            }

            // Get a reference to the ListView holder
            journalListView = (ListView) MainActivity.this.findViewById(R.id.list_customs);

            // Set the adapter on the ListView holder
            journalListView.setAdapter(adapter);

            // Listen for Click events
            journalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                    String name = (String) adapter.getItem(position);
                    //Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, customs_2.class);
                    intent.putExtra("location",name);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                }
            });
        }

        public void customsDatabase(){
            //Get all the customs office, no duplicates
            Cursor customsOfficeCursor = myDB.distinct("customsoffice");

            //getCount
            customsOfficerows = customsOfficeCursor.getCount();

            //Setting size
            setCustomsOfficeSize(customsOfficerows);

            //Fetching values
            distinctCustomsOffice(customsOfficeCursor);
        }

        private void setCustomsOfficeSize(int customsOfficeSize){
            customsOffice = new String[customsOfficeSize];
        }

        private void distinctCustomsOffice(Cursor cursor) {
            if (cursor.moveToFirst()) {
                for(int i = 0; i < customsOfficerows; i++){
                    customsOffice[i] = cursor.getString(0);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }
    public class LocationClass extends ActionBarActivity{

        public final static String ITEM_TITLE = "title";
        public final static String ITEM_CAPTION = "caption";

        // Title
        String[] title = {""};

        // SectionHeaders
        private final String[] subHeader = new String[]{"Locations"};

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

        int locationrows = 0;

        public LocationClass(){

        }

        public void locationLayout(){
            //ListView with subheaders
            // Interactive Tools
            final ArrayAdapter<String> journalEntryAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.menuitem_location, title); //title
            // AddJournalEntryItem
            addJournalEntryItem = (ListView) MainActivity.this.findViewById(R.id.menuitem_location);
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
            adapter = new SeparatedListAdapterCustoms(MainActivity.this);
            ArrayAdapter<String> listadapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item_location, location); //list

            // Add Sections
            for (int i = 0; i < subHeader.length; i++){

                adapter.addSection(subHeader[i], listadapter); //header
            }

            // Get a reference to the ListView holder
            journalListView = (ListView) MainActivity.this.findViewById(R.id.list_location);

            // Set the adapter on the ListView holder
            journalListView.setAdapter(adapter);

            // Listen for Click events
            journalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                    String name = (String) adapter.getItem(position);
                    //Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, customs_2.class);
                    intent.putExtra("customsOffice",name);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                }
            });
        }
        public void locationDatabase(){
            //Get all the customs office, no duplicates
            Cursor locationCursor = myDB.distinct("location");

            //getCount
            locationrows = locationCursor.getCount();

            //Setting size
            setCustomsOfficeSize(locationrows);

            //Fetching values
            distinctLocation(locationCursor);
        }

        private void setCustomsOfficeSize(int locationSize){
            location = new String[locationSize];
        }

        private void distinctLocation(Cursor cursor) {
            if (cursor.moveToFirst()) {
                for(int i = 0; i < locationrows; i++){
                    location[i] = cursor.getString(0);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
    }
}