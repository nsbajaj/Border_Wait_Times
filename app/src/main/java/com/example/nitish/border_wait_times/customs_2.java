package com.example.nitish.border_wait_times;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
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


public class customs_2 extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;

    private String[] menu;

    String colorName = "#33CCCC";

    //Handle's Layout
    Layout layout;

    //Handle's Location
    customTimes cstimes;

    String[] location = new String[]{" "};

    MyDBHandler myDB;

    String type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customs_2);

        Intent intent = getIntent();
        String cname = intent.getStringExtra("customsOffice");
        String lname= intent.getStringExtra("location");

        myDB = new MyDBHandler(this,null,null,1);

        cstimes = new customTimes();
        layout = new Layout();
        if(cname != null){
            cstimes.customTimesDB("customsoffice","location",cname);
            type = "0";
            layout.listview(cname);
        }
        if(lname != null){
            cstimes.customTimesDB("location","customsoffice",lname);
            type = "1";
            layout.listview(lname);
        }

        navigationDrawer();

        readFile();
        listView.setBackgroundColor(Color.parseColor(colorName));
    }

    private void navigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_customs2);
        listView = (ListView) findViewById(R.id.left_customs2);

        menu = getResources().getStringArray(R.array.menu);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,menu));

        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_customs2);
        drawerListener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.navigation_drawer,R.string.drawer_open){
            @Override
            public void onDrawerOpened(View drawerView) {
                String color = "#" + "cccccc";
                View v = listView.getChildAt(0);
                v.setBackgroundColor(Color.parseColor(color));
                //Toast.makeText(customs_2.this, "Drawer Opened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Toast.makeText(customs_2.this,"Drawer Closed", Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true); //action bar menu button clickable now
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
    }

    private void readFile() {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout_customs2);
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
        getMenuInflater().inflate(R.menu.menu_customs_2, menu);
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
        customs_2.this.overridePendingTransition(R.anim.animation2_lr, R.anim.animation2_rl);
    }
    public class Layout extends ActionBarActivity{
        public final static String ITEM_TITLE = "title";
        public final static String ITEM_CAPTION = "caption";

        // Title
        String[] title = {""};

        // SectionHeaders
        private String[] subTitle;

        // MENU - ListView
        private ListView addJournalEntryItem;

        // Adapter for ListView Contents
        private SeperatedListAdapterCustoms2 adapter;

        // ListView Contents
        private ListView journalListView;

        public Map<String, ?> createItem(String title, String caption)
        {
            Map<String, String> item = new HashMap<String, String>();
            item.put(ITEM_TITLE, title);
            item.put(ITEM_CAPTION, caption);
            return item;
        }
        public Layout(){}
        public void listview(String name){
            //ListView with subheaders
            // Interactive Tools
            String temp = null;
            if(type == "0"){ temp = "Custom's Office"; };
            if(type == "1"){ temp = "Locations"; };
            subTitle = new String[]{temp + " for: " + name};
            final ArrayAdapter<String> journalEntryAdapter = new ArrayAdapter<String>(customs_2.this, R.layout.menuitem_customs, title); //title
            // AddJournalEntryItem
            addJournalEntryItem = (ListView) customs_2.this.findViewById(R.id.menuitem_customs2);
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
            adapter = new SeperatedListAdapterCustoms2(customs_2.this);
            ArrayAdapter<String> listadapter = new ArrayAdapter<String>(customs_2.this, R.layout.list_item_customs2, location); //list

            // Add Sections
            for (int i = 0; i < subTitle.length; i++){

                adapter.addSection(subTitle[i], listadapter); //header
            }

            // Get a reference to the ListView holder
            journalListView = (ListView) customs_2.this.findViewById(R.id.list_customs2);

            // Set the adapter on the ListView holder
            journalListView.setAdapter(adapter);

            // Listen for Click events
            journalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {
                    String name = (String) adapter.getItem(position);
                    //Toast.makeText(customs_2.this, name, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(customs_2.this, data.class);
                    intent.putExtra("type",type);
                    intent.putExtra("name",name);
                    customs_2.this.startActivity(intent);
                    customs_2.this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                }
            });
        }

    }
    public class customTimes extends ActionBarActivity{

        int locationrows = 0;

        public customTimes(){}
        public void customTimesDB(String fromName, String toName, String item){
            //Get all the customs office, no duplicates
            Cursor locationCursor = myDB.distinctWhereLocation(fromName, toName, item);

            //getCount
            locationrows = locationCursor.getCount();

            //Setting size
            setSize1(locationrows);

            //Fetching values
            distinctLocation(locationCursor);
        }

        private void setSize1(int size){
            location = new String[size];
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
