package vutnq.cookpadprogrammingtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vutnq.cookpadprogrammingtest.models.WeeklyResponseData;
import vutnq.cookpadprogrammingtest.rest.CookpadProgrammingTestClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionPageAdapter;

    private ViewPager mViewPager;

    private RelativeLayout layoutChooseLocation;

    private Toolbar toolbarMain;

    private TextView toolbarTitle;

    private TextView mainInformation;

    private final int PLACE_PICKER_REQUEST = 1;

    private final int SETTING_REQUEST = 2;

    double lat, lng;

    SectionsPageAdapter adapter;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarTitle = (TextView) findViewById(R.id.textViewToolbarTitle);

        if(!isNetworkConnected()){
            mainInformation = (TextView) findViewById(R.id.textViewMainInformation);
            mainInformation.setText(R.string.connect_internet);
            toolbarTitle.setText("");
        }else{

            mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

            mViewPager = (ViewPager) findViewById(R.id.viewPagerContainer);

            toolbarMain = (Toolbar) findViewById(R.id.toolbar);



            layoutChooseLocation = (RelativeLayout) findViewById(R.id.layoutChooseLocation);

            toolbarMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        Intent intent = builder.build(MainActivity.this);
                        startActivityForResult(intent,PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            });

            if(savedInstanceState != null){
                Fragment currentFragment = getSupportFragmentManager().getFragment(savedInstanceState,"fragmentInstanceSaved1");
                Fragment weeklyFragment = getSupportFragmentManager().getFragment(savedInstanceState,"fragmentInstanceSaved2");
                recoverViewPager(mViewPager,currentFragment,weeklyFragment);
            }else{
                setUpViewPager(mViewPager);
            }

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            dialog = new ProgressDialog(MainActivity.this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                dialog.show();
                Place place = PlacePicker.getPlace(getApplicationContext(),data);
                lng = place.getLatLng().longitude;
                lat = place.getLatLng().latitude;
                Geocoder geocoder = new Geocoder(this);

                try {
                    List<android.location.Address> addresses  = geocoder.getFromLocation(lat,lng,1);
                    StringBuffer buffer = new StringBuffer();

                    for(android.location.Address a : addresses){
                        for(int i = 1; i < a.getMaxAddressLineIndex();i++){
                            buffer.append(a.getAddressLine(i) + " ");
                        }
                    }

                    toolbarTitle.setText(buffer.toString());
                    Call<WeeklyResponseData> call = CookpadProgrammingTestClient.getApiService().getWeeklyWeatherWithLatLon(lat,lng,CookpadProgrammingTestClient.APP_ID,"metric");
                    call.enqueue(new Callback<WeeklyResponseData>() {
                        @Override
                        public void onResponse(Call<WeeklyResponseData> call, Response<WeeklyResponseData> response) {
                            CurrentFragment currentFragment = (CurrentFragment) adapter.getItem(0);
                            currentFragment.updateViewFragmentWhenUserSelectLocation(response.body());
                            WeeklyFragment weeklyFragment = (WeeklyFragment) adapter.getItem(1);
                            weeklyFragment.setAdapterForRecyclerView(response.body());
                            dialog.dismiss();
                            layoutChooseLocation.setVisibility(View.GONE);
                            mViewPager.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<WeeklyResponseData> call, Throwable t) {
                            Log.e(TAG,t.getMessage());
                            dialog.dismiss();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CurrentFragment currentFragment = (CurrentFragment) adapter.getItem(0);
        WeeklyFragment weeklyFragment = (WeeklyFragment) adapter.getItem(1);
        getSupportFragmentManager().putFragment(outState,"fragmentInstanceSaved1",currentFragment);
        getSupportFragmentManager().putFragment(outState,"fragmentInstanceSaved2",weeklyFragment);
    }


    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpViewPager(ViewPager viewPager){
        adapter  = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrentFragment(),"Current");
        adapter.addFragment(new WeeklyFragment(),"Weekly");
        viewPager.setAdapter(adapter);
    }

    private void recoverViewPager(ViewPager viewPager, Fragment current , Fragment weekly){
        adapter  = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(current,"Current");
        adapter.addFragment(weekly,"Weekly");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_setting:
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),SETTING_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
