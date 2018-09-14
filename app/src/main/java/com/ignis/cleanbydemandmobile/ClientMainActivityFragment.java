package com.ignis.cleanbydemandmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

;import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientMainActivityFragment extends AppCompatActivity {

    private DrawerLayout mdrawelayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mtoolbar;
    private NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String fragment_state;

    @BindView(R.id.action_title) TextView action_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main_fragment);
        ButterKnife.bind(this);

        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);

        mdrawelayout = (DrawerLayout) findViewById(R.id.clientdrawer);
        mToggle = new ActionBarDrawerToggle(this, mdrawelayout, R.string.open, R.string.close);

        mdrawelayout.addDrawerListener(mToggle);
        mToggle.syncState();

      //  fragment_state = getIntent().getStringExtra("fragment_state");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ClientScheduleFragment clientscheduleFragment = new ClientScheduleFragment();
        fragmentTransaction.add(R.id.fragment_container,clientscheduleFragment, null);
        fragmentTransaction.commit();
        action_title.setText("Schedules");

//--------------------------------------------------------------------------------------------------

        navigationView = (NavigationView) findViewById(R.id.clienavigation_view);
        View headerView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {

                    case R.id.d_cleanmyhouse:

                        ClientCleanMyHouseFragment clientCleanMyHouseFragment = new ClientCleanMyHouseFragment();
                        fragmentTransaction.replace(R.id.fragment_container,clientCleanMyHouseFragment, null);
                        fragmentTransaction.commit();
                        action_title.setText("Clean my House");

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_myinfo:

                        MyInfoFragment myInfoFragment = new MyInfoFragment();
                        fragmentTransaction.replace(R.id.fragment_container,myInfoFragment, null);
                        fragmentTransaction.commit();
                        action_title.setText("My Info");

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_schedules:

                        ClientScheduleFragment clientScheduleFragment = new ClientScheduleFragment();
                        fragmentTransaction.replace(R.id.fragment_container,clientScheduleFragment, null);
                        fragmentTransaction.commit();
                        action_title.setText("Schedules");

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_history:

                        HistoryFragment historyFragment = new HistoryFragment();
                        fragmentTransaction.replace(R.id.fragment_container,historyFragment, null);
                        fragmentTransaction.commit();
                        action_title.setText("History");

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                   /* case R.id.d_settings:

                        SettingsFragment settingsFragment = new SettingsFragment();
                        fragmentTransaction.replace(R.id.fragment_container,settingsFragment, null);
                        fragmentTransaction.commit();
                        action_title.setText("Settings");

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;*/


                    case R.id.d_aboutus:

                        AboutUsFragment aboutUsFragment = new AboutUsFragment();
                        fragmentTransaction.replace(R.id.fragment_container,aboutUsFragment, null);
                        fragmentTransaction.commit();
                        action_title.setText("About Us");

                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                    case R.id.d_logout:

                        Toast.makeText(ClientMainActivityFragment.this, "7", Toast.LENGTH_SHORT).show();
                        item.setChecked(true);
                        mdrawelayout.closeDrawers();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        hidenavbar();
    }

    private void hidenavbar() {

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


}
