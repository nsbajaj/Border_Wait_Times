package com.example.nitish.border_wait_times;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Formatter;
import java.util.Scanner;


public class Settings extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerListener;

    private String[] menu;

    //Background Color
    String[] colourNames;
    String colorName = "#33CCCC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        navigationDrawer();

        setBackgroundColor();
    }

    private void navigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_setting);
        listView = (ListView) findViewById(R.id.left_drawer_setting);

        menu = getResources().getStringArray(R.array.menu);
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,menu));

        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_setting);
        drawerListener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.navigation_drawer,R.string.drawer_open){
            @Override
            public void onDrawerOpened(View drawerView) {
                String color = "#" + "cccccc";
                View v = listView.getChildAt(2);
                v.setBackgroundColor(Color.parseColor(color));
                //Toast.makeText(Settings.this,"Drawer Opened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Toast.makeText(Settings.this,"Drawer Closed", Toast.LENGTH_LONG).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true); //action bar menu button clickable now
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
    }

    private void setBackgroundColor() {
        TextView text = (TextView) findViewById(R.id.textView_setting);
        text.setText("Hello! Here you can choose to set a background color of your choice for the app!");
        readFile();
        listView.setBackgroundColor(Color.parseColor(colorName));

        //tie your array to your xml array like this
        colourNames = getResources().getStringArray(R.array.listArray);

        //create a ListView object pointing to your list view like this
        ListView lv = (ListView) findViewById(R.id.listView);

        //create an array adapter and attach it to your list view like this
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        backgroundColor();
    }

    private void readFile() {
        RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout_setting);
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
                        currentLayout.setBackgroundColor(Color.parseColor(getName()));
                    }
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeFile() {
        File dir = getFilesDir();
        File file = new File(dir,"background.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            writer.println(getName());
            writer.close();
        }
    }

    private String getName(){
        return colorName;
    }

    private void backgroundColor(){
        ListView listview = (ListView)findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RelativeLayout currentLayout = (RelativeLayout) findViewById(R.id.mainLayout_setting);
                String[] name = getResources().getStringArray(R.array.listArray);
                String[] color = getResources().getStringArray(R.array.listValues);
                String input = (String) parent.getItemAtPosition(position);
                for(int i = 0; i < name.length; i++){
                    if(name[i].equals(input)){
                        String temp = color[i];
                        String hex = temp.substring(2);
                        String hash = "#" + hex;
                        currentLayout.setBackgroundColor(Color.parseColor(hash));
                        listView.setBackgroundColor(Color.parseColor(hash));
                        colorName = hash;
                        writeFile();
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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