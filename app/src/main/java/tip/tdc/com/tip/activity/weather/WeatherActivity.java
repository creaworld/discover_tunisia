package tip.tdc.com.tip.activity.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.current_day.CurrentWeather;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.TransportActivity;
import tip.tdc.com.tip.activity.login.LoginActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.ForecastDay;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.Utils;

import com.bpackingapp.vietnam.travel.R;

import tip.tdc.com.tip.data.SampleData;
import tip.tdc.com.tip.utils.LoggerFactory;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, WeatherActivity.class);
        return in;
    }


    int maxHeightHeaderView = 0;

    FCity mFCity;
    private WeatherPresenter presenter;


    RelativeLayout rlToolBar;

    ListView listviewWeather;
    WeatherDayAdapter weatherDayAdapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mContext = this;

        presenter = new WeatherPresenter(this, getResources().getConfiguration().locale);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        rlToolBar = (RelativeLayout) findViewById(R.id.rlToolBar);

        tvTitleActivity = (TextView) findViewById(R.id.tvTitleActivity);
        imvTopbarBack = (ImageView) findViewById(R.id.imvTopbarBack);
        imvTopbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (Math.abs(verticalOffset) > maxHeightHeaderView) {
                    maxHeightHeaderView = Math.abs(verticalOffset);
                }
//                LoggerFactory.d("verticalOffset=" + verticalOffset);
                rlToolBar.setAlpha((Math.abs(verticalOffset) / (maxHeightHeaderView * 1.0f)));
                if (scrollRange + verticalOffset == 0) {
                    rlToolBar.setAlpha(1.0f);
                    isShow = true;
                } else if (isShow) {
//                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    rlToolBar.setAlpha(0.0f);
                    isShow = false;
                }
            }
        });
//        checkLocationPermissions();

        initView();
        getIntentData();
        loadData();
        Utils.setStatusBarColor(WeatherActivity.this, 0);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY)) {
                mFCity = (FCity) getIntent().getSerializableExtra(AppConstants.KEY_INTENT_CITY);
                tvTitleActivity.setText(mFCity.getName());
                tvCityName.setText(mFCity.getName());
            }
        }
    }

    TextView tvTitleActivity, tvTemprature, tvDate, tvCityName, tvWeatherName, tvHumidity, tvWindSpeed, tvpop;
    ImageView imvCurrentWeather, imvTopbarBack;

    private void initView() {
        listviewWeather = (ListView) findViewById(R.id.listviewWeather);
        listviewWeather.setDivider(null);
        listviewWeather.setDividerHeight(0);

        tvTitleActivity = (TextView) findViewById(R.id.tvTitleActivity);
        tvTemprature = (TextView) findViewById(R.id.tvTemprature);
        FontUtils.setFont(tvTemprature, FontUtils.TYPE_LIGHT);
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvWeatherName = (TextView) findViewById(R.id.tvWeatherName);
        FontUtils.setFont(tvWeatherName, FontUtils.TYPE_LIGHT);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        FontUtils.setFont(tvHumidity, FontUtils.TYPE_LIGHT);
        tvpop = (TextView) findViewById(R.id.tvpop);
        FontUtils.setFont(tvpop, FontUtils.TYPE_LIGHT);
        tvWindSpeed = (TextView) findViewById(R.id.tvWindSpeed);
        FontUtils.setFont(tvWindSpeed, FontUtils.TYPE_LIGHT);
        imvCurrentWeather = (ImageView) findViewById(R.id.imvCurrentWeather);
    }

    private void loadData() {
        weatherDayAdapter = new WeatherDayAdapter(mContext);

        AnimationAdapter mAnimAdapter;
        mAnimAdapter = new ScaleInAnimationAdapter(weatherDayAdapter);
        mAnimAdapter.setAbsListView(listviewWeather);
        listviewWeather.setAdapter(mAnimAdapter);

        if (mFCity != null) {
            Coord coordinate = new Coord();
            coordinate.setLat(Double.parseDouble(mFCity.getLatitude()));
            coordinate.setLon(Double.parseDouble(mFCity.getLongitude()));

            presenter.getFiveDayForecast(coordinate);

            presenter.getCurrentDayExtendedForecast();
        }
    }

    //    private void checkLocationPermissions() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            retrieveLatestKnownLocationAndCheckFiveDayWeather();
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{
//                            Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.ACCESS_COARSE_LOCATION},
//                    LOCATION_REQUEST_ID);
//        }
//    }
//
//
//    private void retrieveLatestKnownLocationAndCheckFiveDayWeather() {
//        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
//
//        try {
//            locationProvider.getLastKnownLocation()
//                    .subscribe(new Action1<Location>() {
//                        @Override
//                        public void call(Location location) {
//                            Coord coordinate = new Coord();
//                            coordinate.setLat(location.getLatitude());
//                            coordinate.setLon(location.getLongitude());
//
//                            presenter.getFiveDayForecast(coordinate);
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.LOCATION_REQUEST_ID: {
//                if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                        && (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//                    retrieveLatestKnownLocationAndCheckFiveDayWeather();
//                } else {
//                    Toast.makeText(this, "Cannot retrieve current location\nwithout permission", Toast.LENGTH_SHORT).show();
//                }
                return;
            }
        }
    }

    @Override
    public void updateFiveDayForecast(ArrayList<ForecastDay> forecastDays) {
        LoggerFactory.d("updateFiveDayForecast");
        updateCurrentDayExtendedForecast(forecastDays);
    }


    @Override
    public void updateTodayForecast(CurrentWeather currentWeather) {
//        loading_weather_progress.setVisibility(View.INVISIBLE);
        LoggerFactory.d("updateTodayForecast");
        int roundedTemp = (int) Math.round(currentWeather.getMain().getTemp());
        String tempWithDegrees = String.format(getString(R.string.degrees_placeholderC), roundedTemp);
        tvTemprature.setText(tempWithDegrees);
        tvTitleActivity.setText(tempWithDegrees);
        tvWindSpeed.setText("" + (int) Math.floor(currentWeather.getWind().getSpeed()) + "m/s");
        tvHumidity.setText("" + currentWeather.getMain().getHumidity());
        tvpop.setText("" + (int) Math.floor(currentWeather.getMain().getPressure()));
        tvWeatherName.setText("" + currentWeather.getWeather().get(0).getDescription());


        LoggerFactory.d(currentWeather.toString());
        LoggerFactory.d("wind:" + currentWeather.getWind());
        LoggerFactory.d("getClouds:" + currentWeather.getClouds());
        LoggerFactory.d("currentWeather.getWeather():" + currentWeather.getWeather().get(0).getDescription());
        LoggerFactory.d("currentWeather.getIcon():" + currentWeather.getWeather().get(0).getIcon());
        LoggerFactory.d("currentWeather.getMain():" + currentWeather.getWeather().get(0).getMain());
        LoggerFactory.d("currentWeather.getId():" + currentWeather.getWeather().get(0).getId());
    }


    public void updateCurrentDayExtendedForecast(ArrayList<ForecastDay> currentWeather) {
        LoggerFactory.d("updateCurrentDayExtendedForecast");

        if (currentWeather == null || currentWeather.size() == 0)
            return;

        String degrees = String.format(getString(R.string.degrees_placeholderC), currentWeather.get(0).getTemperature());
        tvTemprature.setText(degrees);
        tvHumidity.setText("" + (int) currentWeather.get(0).getPressure());
        tvWindSpeed.setText("" + (int) currentWeather.get(0).getGrnd());

        Utils.setupWeatherIconHome(imvCurrentWeather, currentWeather.get(0).getIcon());

        weatherDayAdapter.setWeatherDays(currentWeather);
        weatherDayAdapter.notifyDataSetChanged();
    }

    private int getColor(Temperature colorTemp) {
        int color = 0;

        switch (colorTemp) {
            case SUPER_HOT:
                color = ContextCompat.getColor(this, R.color.super_hot);
                break;
            case MEDIUM_HOT:
                color = ContextCompat.getColor(this, R.color.medium_hot);
                break;
            case HOT:
                color = ContextCompat.getColor(this, R.color.hot);
                break;
            case OK:
                color = ContextCompat.getColor(this, R.color.ok);
                break;
            case OK_CHILL:
                color = ContextCompat.getColor(this, R.color.ok_chill);
                break;
            case COLD:
                color = ContextCompat.getColor(this, R.color.cold);
                break;
        }

        return color;
    }
}
