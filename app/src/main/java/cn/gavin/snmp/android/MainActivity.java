package cn.gavin.snmp.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.gavin.snmp.MainController;
import cn.gavin.snmp.core.model.DeviceImp;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler handler;
    private DeviceAdapter adapter;
    private Executor threadExecutor = Executors.newFixedThreadPool(5);
    private int finishedDeviceCount;
    private ProgressDialog discoveryProgress;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, AddDevice.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        handler.post(new Runnable() {
            @Override
            public void run() {
                MainController.init(MainActivity.this);
                ListView devices = (ListView) findViewById(R.id.device_list_view);
                adapter = new DeviceAdapter(MainController.getDeviceManager().getAllDevices(), false);
                devices.setAdapter(adapter);
            }
        });
        handler = new MyHandler(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            doingCollecting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_device) {
            // Handle the camera action
        } else if (id == R.id.manager_group) {

        } else if (id == R.id.manager_credentials) {

        } else if (id == R.id.send_trap) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void doingCollecting() {
        if (discoveryProgress == null) {
            discoveryProgress = new ProgressDialog(this);
            discoveryProgress.setMax(100);
        }
        discoveryProgress.show();
        discoveryProgress.setProgress(0);
        finishedDeviceCount = 0;

        for (final DeviceImp deviceImp : adapter.getChecks()) {
            threadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    deviceImp.doCollection();
                    handler.sendEmptyMessage(0);
                }
            });
        }
    }

    static class MyHandler extends Handler {
        WeakReference<MainActivity> mainActivity;

        public MyHandler(MainActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mainActivity.get().finishedDeviceCount++;
                    if (mainActivity.get().finishedDeviceCount >= mainActivity.get().adapter.getChecks().size()) {
                        mainActivity.get().discoveryProgress.setProgress(100);
                        mainActivity.get().discoveryProgress.dismiss();
                        mainActivity.get().adapter.notifyDataSetChanged();
                    } else {
                        mainActivity.get().discoveryProgress.setProgress(mainActivity.get().finishedDeviceCount * 100 / mainActivity.get().adapter.getChecks().size());
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


}
