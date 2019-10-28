package tip.tdc.com.tip.activity.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.maps.android.clustering.ClusterManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.current_day.CurrentWeather;
import de.hdodenhof.circleimageview.CircleImageView;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.askNshare.AskShareActivity;
import tip.tdc.com.tip.activity.detail.TipView.TipFragment;
import tip.tdc.com.tip.activity.detail.map.MyItem;
import tip.tdc.com.tip.activity.search.SearchActivity;
import tip.tdc.com.tip.activity.weather.WeatherActivity;
import tip.tdc.com.tip.activity.weather.WeatherContract;
import tip.tdc.com.tip.activity.weather.WeatherPresenter;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppSetting;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.interfaces.DialogCallback;
import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.ForecastDay;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressHUD;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressInstance;
import tip.tdc.com.tip.utils.Utils;

import com.bpackingapp.vietnam.travel.R;

import com.google.android.gms.maps.GoogleMap.*;

public class DetailTabsActivity extends AppCompatActivity implements WeatherContract.View,
        OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener,
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnMarkerDragListener,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        OnCameraMoveListener,
        OnCameraMoveStartedListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, DetailTabsActivity.class);
        return in;
    }

    String TAG = DetailTabsActivity.class.getSimpleName();

    private HomeFragment homeFragment;

    /**
     * Demonstrates customizing the info window and/or its contents.
     */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        private FrameLayout frameLayout;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
//                // This means that getInfoContents will be called.
//                return null;
//            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return mContents;
        }

        private void render(Marker marker, View view) {
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.marker_4);
            ((ImageView) view.findViewById(R.id.badge)).setVisibility(View.GONE);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }
        }
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout toolbar_layout;
    CoordinatorLayout main_content;
    NestedScrollView nestedScrollView;
    String cityKey;
    public FCity mFCity;

    Context mContext;

    private WeatherPresenter presenter;

    private GoogleMap mMap;
    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    ImageView imvCityThumb, imvWeatherThumb, imvTopbarBack;
    TextView tvWeatherTemp, tvCityName, tvCityDescription, tvTitleActivity;
    View vOverlayMap;

    RelativeLayout rlBottomMenu;
    RelativeLayout rlToolBar;
    RelativeLayout rlBackToList;
    RelativeLayout rlbounderMap;
    FrameLayout frameLayoutMap;
    SupportMapFragment supportMapFragment;

    LinearLayout llViewPlaceInfo;
    AppCompatTextView placeName, placeLocation, placePrice;

    LinearLayout.LayoutParams layoutParamsMapSmall;
    LinearLayout.LayoutParams layoutParamsMapFullScreen;
    RelativeLayout.LayoutParams layoutParamsFrameMapFullScreen;
    RelativeLayout.LayoutParams layoutParamsFrameMapSmall;


    CircleImageView imvPlaceThumb;

    int maxHeightHeaderView = 0;
    int maxHeightMapView = 0;
    String currentCategoryKey;

    List<CategoryInfo> categoryInfoList;
    List<CategoryInfo> categoryInfoCleaned = new LinkedList<>();
    List<CategoryInfo> categoryInfoCleanedTipEnd = new LinkedList<>();

    List<Place> listAllPlace = new LinkedList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tabs);

        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        frameLayoutMap = findViewById(R.id.home_frame_layout);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        homeFragment = new HomeFragment();
        homeFragment.setDetailTabsActivity(DetailTabsActivity.this);
        replaceFragment(homeFragment);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentCategoryKey = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(mSectionsPagerAdapter.getCategoryInfo().get(position).getType());
                if (mMap != null)
                    mMap.clear();
                addMarkersToMap(currentCategoryKey);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        rlBottomMenu = (RelativeLayout) findViewById(R.id.rlBottomMenu);
        rlToolBar = (RelativeLayout) findViewById(R.id.rlToolBar);
        rlToolBar.setAlpha(0.0f);

        tvTitleActivity = (TextView) findViewById(R.id.tvTitleActivity);
        imvTopbarBack = (ImageView) findViewById(R.id.imvTopbarBack);
        imvTopbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (Math.abs(verticalOffset) > maxHeightHeaderView) {
                    maxHeightHeaderView = Math.abs(verticalOffset);
                }
                rlToolBar.setAlpha((Math.abs(verticalOffset) / (maxHeightHeaderView * 1.0f)));
                if (scrollRange + verticalOffset == 0) {
                    rlToolBar.setVisibility(View.VISIBLE);
                    rlToolBar.setAlpha(1.0f);
                    isShow = true;
                } else if (isShow) {
                    rlToolBar.setAlpha(0.0f);
                    isShow = false;
                }
            }
        });

        rlToolBar.setAlpha(0.0f);
        rlToolBar.setVisibility(View.GONE);

        maxHeightMapView = Utils.getScreenSize(DetailTabsActivity.this).y - appBarLayout.getLayoutParams().height - rlBottomMenu.getLayoutParams().height - (int) Utils.convertDpToPixel((float) 170, mContext);
        presenter = new WeatherPresenter(this, getResources().getConfiguration().locale);

        initView();
        getIntentData();
        loadData();

        initializeMapView();

        final SVProgressHUD mSVProgressHUD = SVProgressInstance.showWithStatus(mContext, "Loading...");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSVProgressHUD.dismiss();
            }
        }, 2000);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            AppState.currentFireUser = FirebaseAuth.getInstance().getCurrentUser();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void initView() {
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar_layout.setTitle("");


        tvCityName = (TextView) findViewById(R.id.tvCityName);
        FontUtils.setFont(tvCityName, FontUtils.TYPE_NORMAL);
        tvCityDescription = (TextView) findViewById(R.id.tvCityDescription);
        FontUtils.setFont(tvCityDescription, FontUtils.TYPE_LIGHT);
        tvWeatherTemp = (TextView) findViewById(R.id.tvWeatherTemp);
        FontUtils.setFont(tvWeatherTemp, FontUtils.TYPE_LIGHT);
        tvWeatherTemp.setOnClickListener(onClickWeatherListener);


        imvCityThumb = (ImageView) findViewById(R.id.imvCityThumb);
        imvWeatherThumb = (ImageView) findViewById(R.id.imvWeatherThumb);
        imvWeatherThumb.setOnClickListener(onClickWeatherListener);


        findViewById(R.id.llMenuExplore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.llMenuSaved).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SearchActivity.createIntent(mContext);
                intent.putExtra(SearchActivity.SEARCH_TYPE, SearchActivity.SEARCH_TYPE_SAVED);
                intent.putExtra(AppConstants.KEY_INTENT_CITY, mFCity);

                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        findViewById(R.id.llMenuAllActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.llMenuAskShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AskShareActivity.createIntent(mContext);
                intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, cityKey);

                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        findViewById(R.id.llMenuAddTip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogTip dialogTip = new DialogTip();
                dialogTip.buildDialog(mFCity.getCityKey(), mContext, new DialogCallback() {
                    @Override
                    public void OnOkSelected() {

                    }

                    @Override
                    public void OnCancelSelected() {

                    }
                });

                dialogTip.show();
            }
        });

        findViewById(R.id.imvTopbarSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SearchActivity.createIntent(mContext);
                intent.putExtra(SearchActivity.SEARCH_TYPE, SearchActivity.SEARCH_TYPE_PLACE);
                intent.putExtra(AppConstants.KEY_INTENT_CITY, mFCity);
                intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, cityKey);
//                intent.putExtra(AppConstants.KEY_INTENT_CITY, mFCity); // search activity get city in tipapplication

                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });


        llViewPlaceInfo = (LinearLayout) findViewById(R.id.llViewPlaceInfo);
        llViewPlaceInfo.setVisibility(View.GONE);
        placePrice = (AppCompatTextView) findViewById(R.id.placePrice);
        placeLocation = (AppCompatTextView) findViewById(R.id.placeLocation);
        placeName = (AppCompatTextView) findViewById(R.id.placeName);
        imvPlaceThumb = (CircleImageView) findViewById(R.id.imvPlaceThumb);

        llViewPlaceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedMarkerPlace != null) {
                    try {
                        Intent intent = DetailPlaceActivity.createIntent(mContext);
                        intent.putExtra(AppConstants.KEY_INTENT_PLACE, selectedMarkerPlace);
                        intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, mFCity.getCityKey());
                        intent.putExtra(AppConstants.KEY_INTENT_CATE_TYPE, selectedMarkerPlace.getCategoryType());
                        startActivity(intent);
                    } catch (Exception e) {
                        LoggerFactory.logStackTrace(e);
                    }
                }
            }
        });

        vOverlayMap = findViewById(R.id.vOverlayMap);
        rlBackToList = (RelativeLayout) findViewById(R.id.rlBackToList);
        rlbounderMap = (RelativeLayout) findViewById(R.id.rlbounderMap);
        frameLayoutMap = (FrameLayout) findViewById(R.id.frameLayoutMap);
        rlbounderMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        vOverlayMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vOverlayMap.setVisibility(View.GONE);
                rlbounderMap.setLayoutParams(layoutParamsMapFullScreen);
                frameLayoutMap.setLayoutParams(layoutParamsFrameMapFullScreen);
                rlBackToList.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                appBarLayout.setExpanded(false, true);
                appBarLayout.setEnabled(false);
                appBarLayout.setActivated(false);

//                nestedScrollView.setNestedScrollingEnabled(false);
//                nestedScrollView.setEnabled(false);

                placeName.setText("");
                placeLocation.setText("");
                placePrice.setText("");

            }
        });

        rlBackToList.setVisibility(View.GONE);
        rlBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlbounderMap.setLayoutParams(layoutParamsMapSmall);
                frameLayoutMap.setLayoutParams(layoutParamsFrameMapSmall);
                vOverlayMap.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                rlBackToList.setVisibility(View.GONE);
//                appBarLayout.setExpanded(true);
                appBarLayout.setEnabled(true);
                appBarLayout.setActivated(true);
                appBarLayout.setExpanded(false);
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                lp.height = (int) Utils.convertDpToPixel((float) 256, mContext);
                nestedScrollView.setNestedScrollingEnabled(true);
                llViewPlaceInfo.setVisibility(View.GONE);
            }
        });

        layoutParamsMapSmall = (LinearLayout.LayoutParams) rlbounderMap.getLayoutParams();
        layoutParamsFrameMapSmall = (RelativeLayout.LayoutParams) frameLayoutMap.getLayoutParams();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutParamsMapFullScreen = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsMapFullScreen.height = maxHeightMapView;//(int)Utils.convertDpToPixel((float)maxHeightMapView, mContext);
        layoutParamsFrameMapFullScreen = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParamsFrameMapFullScreen.height = maxHeightMapView;//(int)Utils.convertDpToPixel((float)maxHeightMapView, mContext);
    }

    View.OnClickListener onClickWeatherListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mFCity != null) {
                startActivity(WeatherActivity.createIntent(mContext).putExtra(AppConstants.KEY_INTENT_CITY, mFCity));
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        }
    };

    public PlaceService placeService;


    private void getIntentData() {

        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY)) {
                mFCity = (FCity) getIntent().getSerializableExtra(AppConstants.KEY_INTENT_CITY);
                tvTitleActivity.setText(mFCity.getName());
                tvCityName.setText(mFCity.getName());
                tvCityDescription.setText(mFCity.getIntro());
                if (mFCity.getPhotourl().contains("http")) {
                    ImageLoader.getInstance().displayImage(mFCity.getPhotourl(), imvCityThumb, TipApplication.defaultOptions);
                }

                if (!AppSetting.getInstant(mContext).isCityUnlocked(mFCity.getCityKey())) {
                    AppSetting.getInstant(mContext).setCityUnlocked(mFCity.getCityKey());
                }

                LoggerFactory.d("======================== OPEN PLACE DETAIL =======================");
                LoggerFactory.d("mFCity Detail: " + mFCity.toString());
                LoggerFactory.d("======================== END PLACE DETAIL =======================");
            }
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY_KEY)) {
                cityKey = getIntent().getStringExtra(AppConstants.KEY_INTENT_CITY_KEY);

                categoryInfoList = TipApplication.getTipApplication().categoryService.getListCategoryInfoOfCity(cityKey);


                categoryInfoCleanedTipEnd.clear();

                CategoryInfo cateHome = new CategoryInfo();
                cateHome.setType(AppConstants.CATEGORY_HOME_TYPE);
                cateHome.setName("Home");
                cateHome.setPriority("999999");

                categoryInfoCleanedTipEnd.add(cateHome);

                for (int i = 0; i < categoryInfoList.size(); i++) {
                    CategoryInfo categoryInfo = categoryInfoList.get(i);
                    categoryInfo.setPriority(TipApplication.getTipApplication().categoryInfoService.getCategoryByKey(categoryInfo.getKey()).getPriority());

                    if (categoryInfoList.get(i).getType() != AppConstants.CATEGORY_EMERGENCY_TYPE
                            && categoryInfoList.get(i).getType() != AppConstants.CATEGORY_TRANSPORT_TYPE
                            && categoryInfoList.get(i).getType() != AppConstants.CATEGORY_CITY_INFO_TYPE
//                            && categoryInfoList.get(i).getType() != AppConstants.CATEGORY_STORIES_TYPE
                            ) {

                        if (categoryInfoList.get(i).getType() != AppConstants.CATEGORY_TIP_TYPE) {
                            categoryInfoCleanedTipEnd.add(categoryInfo);
                            LoggerFactory.e("Add place to categoryInfoCleanedTipEnd >>>>>>>>>>>>>>> " + i);
                        }
                        categoryInfoCleaned.add(categoryInfo);
                        LoggerFactory.e("Add place to categoryInfoCleaned <<<<<<<<<<<< " + i);

                    }
                }

                Collections.sort(categoryInfoCleanedTipEnd, new CategoryInfo.ComparatorPriority());


                placeService = new PlaceService(cityKey);
                placeService.setOnChangedListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(EventType type, int index, int oldIndex) {

                    }

                    @Override
                    public void onDataChanged() {
                        LoggerFactory.d(TAG, "PlaceService onDataChange");
                        if (placeService.getCount() > 0) {

                            for (int i = 0; i < placeService.getCount(); i++) {
                                try {
                                    Place place = placeService.getItem(i).getValue(Place.class);
                                    if (place != null && !place.isDeactived()) {
//                                        LoggerFactory.d(placeService.getItem(i).getValue(Place.class).toString());
                                        place.setPlaceKey(placeService.getItem(i).getKey());
                                        listAllPlace.add(place);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            addMarkersToMap(currentCategoryKey);
                            updateMapBounder();
                        }
                        mSectionsPagerAdapter.setCategoryInfo(categoryInfoCleanedTipEnd);
                        mSectionsPagerAdapter.notifyDataSetChanged();
                        homeFragment.setCats(categoryInfoCleanedTipEnd);
                        homeFragment.setOnCatClickListener(new OnCatClickListener() {
                            @Override
                            public void onClick(CategoryInfo cat) {
                                if (cat.getType() == AppConstants.CATEGORY_TIP_TYPE) {
                                    TipFragment tipFragment = new TipFragment();
                                    tipFragment.setDetailTabsActivity(DetailTabsActivity.this);
                                    tipFragment.setCategoryInfo(cat);
                                    replaceFragment(tipFragment);
                                } else {
                                    PlaceFragment placeFragment = new PlaceFragment();
                                    placeFragment.setDetailTabsActivity(DetailTabsActivity.this);
                                    placeFragment.setCategoryInfo(cat);
                                    replaceFragment(placeFragment);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.home_frame_layout, fragment);
        Log.e("frame change", "cccccccccccccccccccchanged");
        transaction.commit();
    }


    private void loadData() {
        if (mFCity == null)
            return;
        Coord coordinate = new Coord();
        coordinate.setLat(Double.parseDouble(mFCity.getLatitude()));
        coordinate.setLon(Double.parseDouble(mFCity.getLongitude()));

        presenter.getFiveDayForecast(coordinate);
        presenter.getCurrentDayExtendedForecast();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        if(fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
        } else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadUserInfo();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            view.clearFocus();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private List<CategoryInfo> categoryInfo = new LinkedList<>();

        public List<CategoryInfo> getCategoryInfo() {
            return categoryInfo;
        }

        public void setCategoryInfo(List<CategoryInfo> categoryInfo) {
            this.categoryInfo = categoryInfo;
        }


        @Override
        public Fragment getItem(int position) {
            if (categoryInfo.get(position).getType() == AppConstants.CATEGORY_HOME_TYPE) {
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    homeFragment.setDetailTabsActivity(DetailTabsActivity.this);
                    replaceFragment(homeFragment);
                }
                return homeFragment;
            } else if (categoryInfo.get(position).getType() == AppConstants.CATEGORY_TIP_TYPE) {
                TipFragment tipFragment = new TipFragment();
                tipFragment.setDetailTabsActivity(DetailTabsActivity.this);
                tipFragment.setCategoryInfo(categoryInfo.get(position));
                return tipFragment;
            } else {
                PlaceFragment placeFragment = new PlaceFragment();
                placeFragment.setDetailTabsActivity(DetailTabsActivity.this);
                placeFragment.setCategoryInfo(categoryInfo.get(position));
                return placeFragment;
            }

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            LoggerFactory.d(TAG, "SectionsPagerAdapter notifyDataSetChanged >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        @Override
        public int getCount() {
            return categoryInfo.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryInfo.get(position).getName();
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
        if (currentWeather != null){
            int roundedTemp = (int) Math.round(currentWeather.getMain().getTemp());
            String tempWithDegrees = String.format(getString(R.string.degrees_placeholderC), roundedTemp);
            tvWeatherTemp.setText(tempWithDegrees);
            Utils.setupWeatherIconHome(imvWeatherThumb, currentWeather.getWeather().get(0).getIcon());
        }
    }


    @Override
    public void updateCurrentDayExtendedForecast(ArrayList<ForecastDay> currentWeather) {
        LoggerFactory.d("updateCurrentDayExtendedForecast");

        if (currentWeather == null || currentWeather.size() == 0)
            return;

        String degrees = String.format(getString(R.string.degrees_placeholderC), currentWeather.get(0).getTemperature());
        tvWeatherTemp.setText(degrees);
        Utils.setupWeatherIconHome(imvWeatherThumb, currentWeather.get(0).getIcon());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);


        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
//        mMap.setOnInfoWindowCloseListener(this);
//        mMap.setOnInfoWindowLongClickListener(this);
//        mMap.setOnCameraMoveListener(this);
//        mMap.setOnCameraMoveStartedListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");

        updateMapBounder();

    }

    private void updateMapBounder() {
        if (mMap == null)
            return;
        if (listAllPlace == null || listAllPlace.size() == 0)
            return;


        AsyncTask asyncTask = new AsyncTask<String, String, String>() {

            LatLngBounds bounds;

            /**
             * Before starting background do some work.
             * */
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... params) {
                // TODO fetch url data do bg process.

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                int numberPlaceInclude = 1;

                try {
                    builder.include(new LatLng(Double.parseDouble(mFCity.getLatitude()), Double.parseDouble(mFCity.getLongitude())));
                    numberPlaceInclude++;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < listAllPlace.size(); i++) {
                    if (listAllPlace.get(i).getLatitude() == null || listAllPlace.get(i).getLatitude().length() == 0 || listAllPlace.get(i).getLongitude() == null || listAllPlace.get(i).getLongitude().length() == 0) {
                        continue;
                    }
                    try {
                        if (listAllPlace.get(i).getLatLng() == null) {
                            listAllPlace.get(i).setLatLng(new LatLng(Double.parseDouble(listAllPlace.get(i).getLongitude()), Double.parseDouble(listAllPlace.get(i).getLatitude())));
                        }

                        builder.include(listAllPlace.get(i).getLatLng());
                        numberPlaceInclude++;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (numberPlaceInclude > 0) {
                    bounds = builder.build();

                }
                return null;
            }

            /**
             * Update list ui after process finished.
             */
            protected void onPostExecute(String file_url) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         */
                        if (bounds != null) {
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, Integer.parseInt(mFCity.getZoom())));
                        }
                    }
                });
            }

        };
        asyncTask.execute(new String[]{});



    }

    private void addMarkersToMap(final String category) {
        if (mMap == null)
            return;

        AsyncTask asyncTask = new AsyncTask<String, String, String>() {
            /**
             * Before starting background do some work.
             * */
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... params) {
                // TODO fetch url data do bg process.
                return null;
            }

            /**
             * Update list ui after process finished.
             */
            protected void onPostExecute(String file_url) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         */

                        for (int i = 0; i < listAllPlace.size(); i++) {
                            try {
//                                LoggerFactory.d("PARSE Location : " + listAllPlace.get(i).getName() + "-lat: " + listAllPlace.get(i).getLatitude() + " lon: " + listAllPlace.get(i).getLongitude());
                                if (!listAllPlace.get(i).isDeactived()) {
                                    boolean categoryAccept = false;
                                    if (category == null || category.length() == 0) {
                                        categoryAccept = true;
                                    } else if (listAllPlace.get(i).getCategories() != null && listAllPlace.get(i).getCategories().size() > 0 &&
                                            listAllPlace.get(i).getCategories().toString().contains(category)) {
                                        categoryAccept = true;
                                    }

                                    if (listAllPlace.get(i).getLatitude() != null && listAllPlace.get(i).getLatitude().length() > 0
                                            && listAllPlace.get(i).getLongitude() != null && listAllPlace.get(i).getLongitude().length() > 0
                                            && categoryAccept
                                            ) {
                                        if (listAllPlace.get(i).getLatLng() == null) {
                                            listAllPlace.get(i).setLatLng(new LatLng(
                                                    Double.parseDouble(listAllPlace.get(i).getLongitude()),
                                                    Double.parseDouble(listAllPlace.get(i).getLatitude())));
                                        }

                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(listAllPlace.get(i).getLatLng())
                                                .title(listAllPlace.get(i).getName())
                                                .icon(vectorToBitmap(Place.getPlaceMaker(listAllPlace.get(i))))
                                                .snippet(listAllPlace.get(i).getPlaceKey())
                                                .flat(false)
                                                .rotation(0));
                                        marker.setTag(listAllPlace.get(i));
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

        };
        asyncTask.execute(new String[]{});

    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     */
    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
//    MapView mapView;

    /**
     * Initialises the MapView by calling its lifecycle methods.
     */
    public void initializeMapView() {
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        MySupportMapFragment mapFragment;
        mapFragment = (MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true);
                }
            });
        new OnMapAndViewReadyListener(mapFragment, this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    Place selectedMarkerPlace;

    @Override
    public boolean onMarkerClick(final Marker marker) {
        LoggerFactory.d("onMarkerClick : " + marker.toString());


        Place place = null;
//        place = (Place) marker.getTag();

        if (place == null && marker.getSnippet() != null && marker.getSnippet().length() > 0) {
            place = placeService.getPlaceByKey(marker.getSnippet());
        }




        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(mFCity.getLatitude()), Double.parseDouble(mFCity.getLongitude())), 15));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (place != null) {

            selectedMarkerPlace = place;

            LoggerFactory.d("onMarkerClick : place=" + selectedMarkerPlace.toString());

            llViewPlaceInfo.setVisibility(View.VISIBLE);
            placeName.setText(marker.getTitle());

            if (place.getPhotos() != null && place.getPhotos().size() > 0 && place.getPhotos().get(0).contains("http")) {
                ImageLoader.getInstance().displayImage(place.getPhotos().get(0), imvPlaceThumb, TipApplication.defaultOptions);
            } else {
                imvPlaceThumb.setImageResource(R.drawable.img_default);
            }

            placeLocation.setText(place.getAddress());
            if (place.getFromprice() == null || place.getFromprice().length() == 0 || place.getFromprice().equalsIgnoreCase("free")) {
                placePrice.setText("Free");
            } else {
                placePrice.setText(place.getFromprice());
            }

            int categoryType = TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(selectedMarkerPlace.getCategories().get(0));
            if (selectedMarkerPlace.getFromprice() != null && selectedMarkerPlace.getFromprice().length() > 0) {
                if (categoryType == AppConstants.CATEGORY_SLEEP_TYPE) {
                    placePrice.setText(selectedMarkerPlace.getFromprice() + "$");
                } else if (categoryType == AppConstants.CATEGORY_SEE_DO_TYPE) {
                    placePrice.setText(selectedMarkerPlace.getFromprice());
                } else {
                    placePrice.setText(selectedMarkerPlace.getFromprice());
                }
            } else {
                placePrice.setText("Free");
            }

            if (categoryType == AppConstants.CATEGORY_SHOPPING_TYPE
                    || categoryType == AppConstants.CATEGORY_DRINK_TYPE
                    || categoryType == AppConstants.CATEGORY_EAT_TYPE
                    ) {
                placePrice.setVisibility(View.GONE);
            } else {
                placePrice.setVisibility(View.VISIBLE);
            }

        } else {

            selectedMarkerPlace = null;
            LoggerFactory.d("marker select place null");
        }

        // Markers have a z-index that is settable and gettable.
//        float zIndex = marker.getZIndex() + 1.0f;
//        marker.setZIndex(zIndex);

        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LoggerFactory.d("onMarkerDragStart");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LoggerFactory.d("onMarkerDragEnd");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LoggerFactory.d("onMarkerDrag.  Current Position: " + marker.getPosition());
    }

    @Override
    public void onCameraMove() {
        LoggerFactory.d("onCamera move");
    }

    @Override
    public void onCameraMoveStarted(int i) {
        LoggerFactory.d("OnCamera move started");
    }

    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }

    public int getCategoryPageIndex(int cateType){
        for (int i = 0; i < categoryInfoCleanedTipEnd.size(); i++) {
            if (cateType == categoryInfoCleanedTipEnd.get(i).getType()) {
                return i;
            }
        }

        return 1;
    }


}
