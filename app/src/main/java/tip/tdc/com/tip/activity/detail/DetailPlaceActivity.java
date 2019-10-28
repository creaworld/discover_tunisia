package tip.tdc.com.tip.activity.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.*;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedList;
import java.util.List;

import az.openweatherapi.model.gson.common.Coord;
import io.fabric.sdk.android.services.common.SafeToast;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.placeFragment.FacilityAdapter;
import tip.tdc.com.tip.activity.introslide.SlideIntroductionActivity;
import tip.tdc.com.tip.activity.tip.TipActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.model.Facility;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.model.SavedItem;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.CustomScrollView;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressInstance;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class DetailPlaceActivity extends AppCompatActivity implements OnMapReadyCallback,
        BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener,
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
        in.setClass(context, DetailPlaceActivity.class);
        return in;
    }

    private SliderLayout mDemoSlider;
    Context mContext;
    RelativeLayout rlToolBar;
    CustomScrollView scrollPlaceDetail;
    NestedScrollView nestedScrollPlaceDetail;
    TextView tvNumberLove, tvNumberComment;
    TextView tvTitleActivity, tvTransportContent, tvSliderName, tvOpenningDayTitle, tvOpenningDayValue, tvOpenningTimeTitle, tvOpenningTimeValue, tvPlaceDescription, tvPlaceDescriptionReadmore, tvAddressTitle, tvAddressDirection, tvAddress, tvPlacePrice, tvPhoneTitle, tvPhoneCall, tvPhoneNumber, tvEmailTitle, tvEmailSend, tvEmailAddress, tvWebsiteTitle, tvWebsiteVisit, tvWebsite, tvFacebookTitle, tvFacebookView, tvFacebook;
    ImageView imvTopbarBack;
    ImageView imvLoveStatus, imvCommentStatus, tabbarSave;
    int maxHeightHeaderView;
    private GoogleMap mMap;


    Place place;
    String cityKey;

    Button btnBookNow;

    LinearLayout llPlaceTourInfo;
    TextView tvTourLocationTitle, tvTourLocationValue, tvTourDurationTitle ,tvTourDurationValue, tvTourLanguageTitle
            ,tvTourLanguageValue, tvTourTransportTitle, tvTourTransportValue, tvTourGroupSizeTitle, tvTourGroupSizeValue;


    View llPhone, llAddress, llEmail, llWebsite, llFacebook;
    View layoutFacility, llFacilityMap, llFacilityTowels, llFacilityWifi, llFacilityInternet, llFacilityParking, llFacilityBreakfast, llFacilityLinen;


    View llFacilityAll;
    TextView tvPlaceFacilitiesViewAll, tvPlaceFacilities;

    View llOpenTime;
    View llCheckOutInTime;
    TextView tvCheckinTime, tvCheckoutTime;

    View llNote;
    TextView tvPlaceNote, tvPlaceNoteViewAll, tvNoteContent;

    GridView gridView;
    // Data
    List<Facility> facilities = new LinkedList<>();

    LinearLayout llBooking, llCheckBookingCom, llCheckHostelWorld, llCheckAirBnB;
    Button btnCheckBookingCom, btnCheckHostelWorld, btnCheckAirBnB;

    boolean isScrollEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mContext = this;
        initView();
        loadData();
        getIntentData();

        Utils.setStatusBarColor(DetailPlaceActivity.this, 0);
        initializeMapView();

        SVProgressInstance.showSuccessWithStatus(mContext, "Loading...");
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        rlToolBar = (RelativeLayout) findViewById(R.id.rlToolBar);

        nestedScrollPlaceDetail = (NestedScrollView) findViewById(R.id.nestedScrollPlaceDetail);
        scrollPlaceDetail = (CustomScrollView) findViewById(R.id.scrollPlaceDetail);



//        nestedScrollPlaceDetail.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                switch (motionEvent.getAction()) {
////                    case MotionEvent.ACTION_DOWN:
//                        // if we can scroll pass the event to the superclass
//                        if (isScrollEnable) return nestedScrollPlaceDetail.onTouchEvent(motionEvent);
//                        // only continue to handle the touch event if scrolling enabled
//                        return isScrollEnable; // scrollable is always false at this point
////                    default:
////                        return nestedScrollPlaceDetail.onTouchEvent(motionEvent);
////                }
//            }
//        });


        scrollPlaceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                isScrollEnable = true;
            }
        });

        tvTitleActivity = (TextView) findViewById(R.id.tvTitleActivity);
        FontUtils.setFont(tvTitleActivity, FontUtils.TYPE_NORMAL);
        tvTransportContent = (TextView) findViewById(R.id.tvTransportContent);
        FontUtils.setFont(tvTransportContent, FontUtils.TYPE_LIGHT);
        tvSliderName = (TextView) findViewById(R.id.tvSliderName);
        FontUtils.setFont(tvSliderName, FontUtils.TYPE_NORMAL);
        tvPlacePrice = (TextView) findViewById(R.id.tvPlacePrice);
        FontUtils.setFont(tvPlacePrice, FontUtils.TYPE_NORMAL);
        tvNumberLove = (TextView) findViewById(R.id.tvNumberLove);
        FontUtils.setFont(tvNumberLove, FontUtils.TYPE_LIGHT);
        tvNumberComment = (TextView) findViewById(R.id.tvNumberComment);
        FontUtils.setFont(tvNumberComment, FontUtils.TYPE_LIGHT);


        imvTopbarBack = (ImageView) findViewById(R.id.imvTopbarBack);
        imvLoveStatus = (ImageView) findViewById(R.id.imvLoveStatus);
        imvCommentStatus = (ImageView) findViewById(R.id.imvCommentStatus);
        tabbarSave = (ImageView) findViewById(R.id.tabbarSave);
        imvTopbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        imvLoveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceService placeService = new PlaceService(place.getPlaceKey());
                if (place == null)
                    return;
                if (AppState.currentFireUser != null) {
                    if (place.getLoved() != null && AppState.currentFireUser != null && place.getLoved().contains(AppState.currentFireUser.getUid())) {
                        place.setLoved(place.getLoved().replace("#" + AppState.currentFireUser.getUid(), ""));
                        imvLoveStatus.setImageResource(R.drawable.icon_love);
                    } else {
                        if (place.getLoved() == null) {
                            place.setLoved("");
                        }

                        place.setLoved(place.getLoved() + "#" + AppState.currentFireUser.getUid());
                        imvLoveStatus.setImageResource(R.drawable.icon_loved);
                    }

                    placeService.updatePlace(place, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                LoggerFactory.d("Update love success");
                                tvNumberLove.setText(Utils.countLove(place.getLoved()) + "");
                                imvLoveStatus.setImageResource(R.drawable.icon_loved);
                                if (place.getLoved().contains(AppState.currentFireUser.getUid())) {
                                    imvLoveStatus.setImageResource(R.drawable.icon_loved);
                                } else {
                                    imvLoveStatus.setImageResource(R.drawable.icon_love);
                                }
                            } else {
                                LoggerFactory.d("Update love failure");
                            }
                        }
                    });
                } else {
                    SlideIntroductionActivity.gotoLoginWindow(view.getContext());
                }
            }
        });

        imvCommentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (place != null) {
                    Intent intent = TipActivity.createIntent(mContext);
                    intent.putExtra(AppConstants.KEY_INTENT_PLACE, place);
                    intent.putExtra(AppConstants.KEY_INTENT_PLACE_KEY, place.getPlaceKey());
                    intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, cityKey);

                    ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
                    startActivity(intent);
                }
            }
        });

        tvNumberComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (place != null) {
                    Intent intent = TipActivity.createIntent(mContext);
                    intent.putExtra(AppConstants.KEY_INTENT_PLACE, place);
                    intent.putExtra(AppConstants.KEY_INTENT_PLACE_KEY, place.getPlaceKey());
                    intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, cityKey);
                    ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
                    startActivity(intent);
                }
            }
        });


        findViewById(R.id.rlShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TipActivity.createIntent(mContext);
                intent.putExtra(AppConstants.KEY_INTENT_PLACE, place);
                intent.putExtra(AppConstants.KEY_INTENT_PLACE_KEY, place.getPlaceKey());
                intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, cityKey);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
                startActivity(intent);
            }
        });

        tabbarSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TipApplication.getTipApplication().realmDB.getSavedItem(place.getPlaceKey()) != null) {
                    Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                    ((ImageView) view).setImageResource(R.drawable.icon_favorite);
                    TipApplication.getTipApplication().realmDB.removeSavedItem(place.getPlaceKey());
                } else {
                    SavedItem savedItem = new SavedItem(place, cityKey);
                    savedItem.setSaveId(place.getPlaceKey());
                    savedItem.setCategoryType(TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(place.getCategories().get(0)));
                    TipApplication.getTipApplication().realmDB.updateSavedItem(savedItem);
                    Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                    ((ImageView) view).setImageResource(R.drawable.icon_favorited);
                }
            }
        });
        rlToolBar.setAlpha(0.0f);
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
                LoggerFactory.d("verticalOffset=" + verticalOffset);
                LoggerFactory.d("scrollRange=" + scrollRange);
                if (verticalOffset == 0) {
                    rlToolBar.setAlpha(0.0f);
                    return;
                }
                rlToolBar.setAlpha((Math.abs(verticalOffset) / (maxHeightHeaderView * 1.0f)));
                if (scrollRange + verticalOffset == 0) {
                    rlToolBar.setAlpha(1.0f);
                    isShow = true;
                } else if (isShow) {
                    rlToolBar.setAlpha(0.0f);
                    isShow = false;
                }
            }
        });

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        tvOpenningDayTitle = (TextView) findViewById(R.id.tvOpenningDayTitle);
        FontUtils.setFont(tvOpenningDayTitle, FontUtils.TYPE_NORMAL);
        tvOpenningDayValue = (TextView) findViewById(R.id.tvOpenningDayValue);
        FontUtils.setFont(tvOpenningDayValue, FontUtils.TYPE_LIGHT);
        tvOpenningTimeTitle = (TextView) findViewById(R.id.tvOpenningTimeTitle);
        FontUtils.setFont(tvOpenningTimeTitle, FontUtils.TYPE_NORMAL);
        tvOpenningTimeValue = (TextView) findViewById(R.id.tvOpenningTimeValue);
        FontUtils.setFont(tvOpenningTimeValue, FontUtils.TYPE_LIGHT);
        tvPlaceDescription = (TextView) findViewById(R.id.tvPlaceDescription);
        FontUtils.setFont(tvPlaceDescription, FontUtils.TYPE_NORMAL);
        tvPlaceDescriptionReadmore = (TextView) findViewById(R.id.tvPlaceDescriptionReadmore);
        FontUtils.setFont(tvPlaceDescriptionReadmore, FontUtils.TYPE_NORMAL);


        btnBookNow = (Button) findViewById(R.id.btnBookNow);
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebsite(mContext, place.getTourBookingUrl());
            }
        });


        layoutFacility = findViewById(R.id.layoutFacility);
        llFacilityMap = findViewById(R.id.llFacilityMap);
        llFacilityTowels = findViewById(R.id.llFacilityTowels);
        llFacilityWifi = findViewById(R.id.llFacilityWifi);
        llFacilityInternet = findViewById(R.id.llFacilityInternet);
        llFacilityLinen = findViewById(R.id.llFacilityLinen);
        llFacilityBreakfast = findViewById(R.id.llFacilityBreakfast);
        llFacilityParking = findViewById(R.id.llFacilityParking);

        llCheckOutInTime = findViewById(R.id.llCheckOutInTime);
        tvCheckoutTime = (TextView) findViewById(R.id.tvCheckoutTime);
        FontUtils.setFont(tvCheckoutTime, FontUtils.TYPE_LIGHT);
        tvCheckinTime = (TextView) findViewById(R.id.tvCheckinTime);
        FontUtils.setFont(tvCheckinTime, FontUtils.TYPE_LIGHT);

        llFacilityAll = findViewById(R.id.llFacilityAll);
        gridView = (GridView) findViewById(R.id.gridView);
        tvPlaceFacilities = (TextView) findViewById(R.id.tvPlaceFacilities);
        FontUtils.setFont(tvPlaceFacilities, FontUtils.TYPE_NORMAL);
        tvPlaceFacilitiesViewAll = (TextView) findViewById(R.id.tvPlaceFacilitiesViewAll);
        FontUtils.setFont(tvPlaceFacilitiesViewAll, FontUtils.TYPE_NORMAL);
        tvPlaceFacilitiesViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        llOpenTime = findViewById(R.id.llOpenTime);


        llNote = findViewById(R.id.llNote);
        tvPlaceNote = (TextView) findViewById(R.id.tvPlaceNote);
        FontUtils.setFont(tvPlaceNote, FontUtils.TYPE_NORMAL);
        tvPlaceNoteViewAll = (TextView) findViewById(R.id.tvPlaceNoteViewAll);
        FontUtils.setFont(tvPlaceNoteViewAll, FontUtils.TYPE_NORMAL);
        tvNoteContent = (TextView) findViewById(R.id.tvNoteContent);
        FontUtils.setFont(tvNoteContent, FontUtils.TYPE_LIGHT);

        llAddress = findViewById(R.id.llAddress);
        tvAddressTitle = (TextView) findViewById(R.id.tvAddressTitle);
        FontUtils.setFont(tvAddressTitle, FontUtils.TYPE_NORMAL);
        tvAddressDirection = (TextView) findViewById(R.id.tvAddressDirection);
        FontUtils.setFont(tvAddressDirection, FontUtils.TYPE_NORMAL);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        FontUtils.setFont(tvAddress, FontUtils.TYPE_LIGHT);
        tvAddressDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermissions();
            }
        });

        llPhone = findViewById(R.id.llPhone);
        tvPhoneTitle = (TextView) findViewById(R.id.tvPhoneTitle);
        FontUtils.setFont(tvPhoneTitle, FontUtils.TYPE_NORMAL);
        tvPhoneCall = (TextView) findViewById(R.id.tvPhoneCall);
        FontUtils.setFont(tvPhoneCall, FontUtils.TYPE_NORMAL);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        FontUtils.setFont(tvPhoneNumber, FontUtils.TYPE_LIGHT);
        tvPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.callPhoneNumber(mContext, place.getPhonenumber());
            }
        });

        llEmail = findViewById(R.id.llEmail);
        tvEmailTitle = (TextView) findViewById(R.id.tvEmailTitle);
        FontUtils.setFont(tvEmailTitle, FontUtils.TYPE_NORMAL);
        tvEmailSend = (TextView) findViewById(R.id.tvEmailSend);
        FontUtils.setFont(tvEmailSend, FontUtils.TYPE_NORMAL);
        tvEmailAddress = (TextView) findViewById(R.id.tvEmailAddress);
        FontUtils.setFont(tvEmailAddress, FontUtils.TYPE_LIGHT);
        tvEmailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sendEmail(mContext, place.getEmail());
            }
        });


        llWebsite = findViewById(R.id.llWebsite);
        tvWebsiteTitle = (TextView) findViewById(R.id.tvWebsiteTitle);
        FontUtils.setFont(tvWebsiteTitle, FontUtils.TYPE_NORMAL);
        tvWebsiteVisit = (TextView) findViewById(R.id.tvWebsiteVisit);
        FontUtils.setFont(tvWebsiteVisit, FontUtils.TYPE_NORMAL);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);
        FontUtils.setFont(tvWebsite, FontUtils.TYPE_LIGHT);
        tvWebsiteVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebsite(mContext, place.getWebsite());
            }
        });


        llFacebook = findViewById(R.id.llFacebook);
        tvFacebookTitle = (TextView) findViewById(R.id.tvFacebookTitle);
        FontUtils.setFont(tvFacebookTitle, FontUtils.TYPE_NORMAL);
        tvFacebookView = (TextView) findViewById(R.id.tvFacebookView);
        FontUtils.setFont(tvFacebookView, FontUtils.TYPE_NORMAL);
        tvFacebook = (TextView) findViewById(R.id.tvFacebook);
        FontUtils.setFont(tvFacebook, FontUtils.TYPE_LIGHT);
        tvFacebookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebsite(mContext, place.getFacebook());
            }
        });


        tvPlaceDescriptionReadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PlaceDescriptionActivity.createIntent(mContext);
                intent.putExtra(AppConstants.KEY_INTENT_PLACE, place);
                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });
        tvPlaceNoteViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PlaceDescriptionActivity.createIntent(mContext);
                intent.putExtra(AppConstants.KEY_INTENT_PLACE, place);
                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        tvPlaceFacilitiesViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PlaceDescriptionActivity.createIntent(mContext);
                intent.putExtra(AppConstants.KEY_INTENT_PLACE, place);
                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        llBooking = (LinearLayout) findViewById(R.id.llBooking);
        llCheckBookingCom = (LinearLayout) findViewById(R.id.llCheckBookingCom);
        llCheckHostelWorld = (LinearLayout) findViewById(R.id.llCheckHostelWorld);
        llCheckAirBnB = (LinearLayout) findViewById(R.id.llCheckAirBnB);


        btnCheckBookingCom = (Button) findViewById(R.id.btnCheckBookingCom);
        btnCheckHostelWorld = (Button) findViewById(R.id.btnCheckHostelWorld);
        btnCheckAirBnB = (Button) findViewById(R.id.btnCheckAirBnB);

        btnCheckBookingCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebsite(mContext, place.getSleepBookingUrl()+ "?"+ AppConstants.BOOKING_COM_ID);
            }
        });

        btnCheckHostelWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebsite(mContext, place.getSleepHostelWorldUrl() +"?"+ AppConstants.HOSTEL_WORL_ID);
            }
        });

        btnCheckAirBnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebsite(mContext, place.getSleepAirbnbUrl() +"?"+ AppConstants.AIR_BNB_ID);
            }
        });

        llPlaceTourInfo = (LinearLayout) findViewById(R.id.llPlaceTourInfo);
        tvTourLocationTitle = (TextView) findViewById(R.id.tvTourLocationTitle);
        tvTourLocationValue = (TextView) findViewById(R.id.tvTourLocationValue);
        tvTourDurationTitle = (TextView) findViewById(R.id.tvTourDurationTitle);
        tvTourDurationValue = (TextView) findViewById(R.id.tvTourDurationValue);
        tvTourLanguageTitle = (TextView) findViewById(R.id.tvTourLanguageTitle);
        tvTourLanguageValue = (TextView) findViewById(R.id.tvTourLanguageValue);

        tvTourTransportTitle = (TextView) findViewById(R.id.tvTourTransportTitle);
        tvTourTransportValue = (TextView) findViewById(R.id.tvTourTransportValue);
        tvTourGroupSizeTitle = (TextView) findViewById(R.id.tvTourGroupSizeTitle);
        tvTourGroupSizeValue = (TextView) findViewById(R.id.tvTourGroupSizeValue);

        FontUtils.setFont(tvTourLocationTitle, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourLocationValue, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourDurationTitle, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourDurationValue, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourLanguageTitle, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourLanguageValue, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourTransportTitle, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourTransportValue, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourGroupSizeTitle, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(tvTourGroupSizeValue, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(findViewById(R.id.tvWriteComment), FontUtils.TYPE_NORMAL);

    }

    FacilityAdapter facilityAdapter;

    private void loadData() {
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);


        facilityAdapter = new FacilityAdapter(mContext);
        facilityAdapter.setFacilities(facilities);


        gridView.setAdapter(facilityAdapter);


    }


    private void getIntentData() {
        if (getIntent() != null) {
            if(getIntent().hasExtra(AppConstants.KEY_INTENT_CITY_KEY)){
                cityKey = getIntent().getStringExtra(AppConstants.KEY_INTENT_CITY_KEY);
            }

            if (getIntent().hasExtra(AppConstants.KEY_INTENT_PLACE)) {
                place = (Place) getIntent().getSerializableExtra(AppConstants.KEY_INTENT_PLACE);
                if (place != null) {

                    LoggerFactory.d("Open detail activity: " + place.toString());

                    tvTransportContent.setText(place.getDescription());
                    updateLinkify();

                    if (place.getPlaceKey() != null && TipApplication.getTipApplication().realmDB.getSavedItem(place.getPlaceKey()) != null) {
                        tabbarSave.setImageResource(R.drawable.icon_favorited);
                    } else {
                        tabbarSave.setImageResource(R.drawable.icon_favorite);
                    }

                    if (place.getLoved() != null && AppState.currentFireUser != null && place.getLoved().contains(AppState.currentFireUser.getUid())) {
                        imvLoveStatus.setImageResource(R.drawable.icon_loved);
                    } else {
                        imvLoveStatus.setImageResource(R.drawable.icon_love);
                    }

                    tvNumberComment.setText(place.getCommentCount() + "");
                    tvNumberLove.setText(Utils.countLove(place.getLoved()) + "");


                    for (String photo : place.getPhotos()) {
                        DefaultSliderView textSliderView = new DefaultSliderView(this);
                        // initialize a SliderLayout
                        textSliderView
                                .image(photo)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(this);
                        mDemoSlider.addSlider(textSliderView);

                    }

                    tvTitleActivity.setText(place.getName());
                    tvSliderName.setText(place.getName());

                    String priceUnit = "";
                    String currencyUnit = "$";
                    if (place.getCategories() != null && place.getCategories().size() > 0) {
                        CategoryInfo categoryInfo = TipApplication.getTipApplication().categoryInfoService.getCategoryByKey(place.getCategories().get(0));
                        if (categoryInfo != null && categoryInfo.getPriceUnit() != null) {
                            priceUnit = categoryInfo.getPriceUnit();
                        }
                        if(categoryInfo != null && categoryInfo.getType() == AppConstants.CATEGORY_SEE_DO_TYPE) {
                            currencyUnit = "";
                        }
                    }

                    if (place.getFromprice() != null && place.getFromprice().length() > 0) {
                        if (place.getToprice() != null && place.getToprice().length() > 0) {
                            tvPlacePrice.setText(place.getFromprice() + "-" + place.getToprice()+ currencyUnit);
                        } else {
                            if(place.getFromprice() != null && !place.getFromprice().contains("null")) {
                                if(place.getFromprice().toLowerCase().contains("free")){
                                    tvPlacePrice.setText(place.getFromprice());
                                } else {
                                    tvPlacePrice.setText(place.getFromprice() + currencyUnit);
                                }
                            } else {
                                tvPlacePrice.setText("");
                            }
                        }
                        if (priceUnit != null && priceUnit.length() > 0 && !tvPlacePrice.getText().toString().toLowerCase().contains("free")) {
                            tvPlacePrice.setText(tvPlacePrice.getText().toString() +"/"+priceUnit);
                        }

                    } else {
                        if(place.getCategories() != null && place.getCategories().size() > 0 &&
                                (TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(place.getCategories().get(0)) == AppConstants.CATEGORY_EAT_TYPE
                                 || TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(place.getCategories().get(0)) == AppConstants.CATEGORY_DRINK_TYPE
                                || TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(place.getCategories().get(0)) == AppConstants.CATEGORY_NIGHT_LIGHT_TYPE
                                || TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(place.getCategories().get(0)) == AppConstants.CATEGORY_PLAY_TYPE)){
                            tvPlacePrice.setText("");
                        } else {
                            tvPlacePrice.setText("Free");
                        }
                    }

                    if (place.getFacilities() != null && place.getFacilities().size() > 0) {

                        for (int i = 0; i < place.getFacilities().size(); i++) {
                            if (TipApplication.getTipApplication().facilityService.getFacilityByKey(place.getFacilities().get(i)) != null) {
                                facilities.add(TipApplication.getTipApplication().facilityService.getFacilityByKey(place.getFacilities().get(i)));
                            }
                        }

                        if (facilities.size() > 0) {
                            facilityAdapter.notifyDataSetChanged();
                        }

                        layoutFacility.setVisibility(View.VISIBLE);
                        llFacilityMap.setVisibility(View.GONE);
                        llFacilityInternet.setVisibility(View.GONE);
                        llFacilityTowels.setVisibility(View.GONE);
                        llFacilityWifi.setVisibility(View.GONE);
                        llFacilityParking.setVisibility(View.GONE);
                        llFacilityLinen.setVisibility(View.GONE);
                        llFacilityBreakfast.setVisibility(View.GONE);
                        int count = 0;

                        for (int i = 0; i < place.getFacilities().size(); i++) {
                            switch (TipApplication.getTipApplication().facilityService.getFacilityTypeByKey(place.getFacilities().get(i))) {
                                case AppConstants.FACILITY_CITY_MAP_TYPE:
                                    llFacilityMap.setVisibility(View.VISIBLE);
                                    break;

                                case AppConstants.FACILITY_INTERNET_TYPE:
                                    llFacilityInternet.setVisibility(View.VISIBLE);
                                    break;

                                case AppConstants.FACILITY_TOWEL_TYPE:
                                    llFacilityTowels.setVisibility(View.VISIBLE);
                                    break;

                                case AppConstants.FACILITY_WIFI_TYPE:
                                    llFacilityWifi.setVisibility(View.VISIBLE);
                                    break;

                                case AppConstants.FACILITY_PARKING_TYPE:
                                    llFacilityParking.setVisibility(View.VISIBLE);
                                    break;

                                case AppConstants.FACILITY_LINEN_TYPE:
                                    llFacilityLinen.setVisibility(View.VISIBLE);
                                    break;

                                case AppConstants.FACILITY_BREAKFAST_TYPE:
                                    llFacilityBreakfast.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    break;
                            }


                        }

                        llFacilityAll.setVisibility(View.VISIBLE);
                    } else {
                        layoutFacility.setVisibility(View.GONE);
                        llFacilityAll.setVisibility(View.GONE);
                    }


                    if (place.getOpeningday() != null && place.getOpeningday().length() > 0) {
                        llOpenTime.setVisibility(View.VISIBLE);
                        tvOpenningDayValue.setText(place.getOpeningday());
                        tvOpenningTimeValue.setText(place.getOpeningtime());
                    } else {
                        llOpenTime.setVisibility(View.GONE);
                    }


                    if (place.getThingstonote() != null && place.getThingstonote().length() > 0) {
                        llNote.setVisibility(View.VISIBLE);
                        tvNoteContent.setText(place.getThingstonote());
                    } else {
                        llNote.setVisibility(View.GONE);
                    }

                    if (place.getCheckin() != null && place.getCheckin().length() > 0) {
                        llCheckOutInTime.setVisibility(View.VISIBLE);
                        tvCheckinTime.setText("Check in\n" + place.getCheckin());
                        tvCheckoutTime.setText("Check out\n" + place.getCheckout());
                    } else {
                        llCheckOutInTime.setVisibility(View.GONE);
                    }


                    // For place contact
                    if (place.getAddress() != null && place.getAddress().length() > 0) {
                        llAddress.setVisibility(View.VISIBLE);
                        tvAddress.setText(place.getAddress());
                    } else {
                        llAddress.setVisibility(View.GONE);
                    }

                    if (place.getPhonenumber() != null && place.getPhonenumber().length() > 0) {
                        llPhone.setVisibility(View.VISIBLE);
                        tvPhoneNumber.setText(place.getPhonenumber());
                    } else {
                        llPhone.setVisibility(View.GONE);
                    }

                    if (place.getEmail() != null && place.getEmail().length() > 0) {
                        llEmail.setVisibility(View.VISIBLE);
                        tvEmailAddress.setText(place.getEmail());
                    } else {
                        llEmail.setVisibility(View.GONE);
                    }

                    if (place.getWebsite() != null && place.getWebsite().length() > 0) {
                        llWebsite.setVisibility(View.VISIBLE);
                        tvWebsite.setText(place.getWebsite());
                    } else {
                        llWebsite.setVisibility(View.GONE);
                    }

                    if (place.getFacebook() != null && place.getFacebook().length() > 0) {
                        llFacebook.setVisibility(View.VISIBLE);
                        tvFacebook.setText(place.getFacebook());
                    } else {
                        llFacebook.setVisibility(View.GONE);
                    }


                    // For Booking info
                    if ((place.getCategoryType() != 0 && place.getCategoryType() == AppConstants.CATEGORY_SLEEP_TYPE)
                            || (place.getCategories() != null && place.getCategories().size() > 0 && place.getCategories().toString().contains(TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_SLEEP_TYPE)))

                            ) {
                        llBooking.setVisibility(View.VISIBLE);
                    } else {
                        llBooking.setVisibility(View.GONE);
                    }
                    if (place.getSleepBookingUrl() != null && place.getSleepBookingUrl().length() > 0) {
                        llCheckBookingCom.setVisibility(View.VISIBLE);
                    } else {
                        llCheckBookingCom.setVisibility(View.GONE);
                    }

                    if (place.getSleepHostelWorldUrl() != null && place.getSleepHostelWorldUrl().length() > 0) {
                        llCheckHostelWorld.setVisibility(View.VISIBLE);
                    } else {
                        llCheckHostelWorld.setVisibility(View.GONE);
                    }

                    if (place.getSleepAirbnbUrl() != null && place.getSleepAirbnbUrl().length() > 0) {
                        llCheckAirBnB.setVisibility(View.VISIBLE);
                    } else {
                        llCheckAirBnB.setVisibility(View.GONE);
                    }

                    updateMapBounder();
                    addMarkersToMap();


                    // For Booking info
                    if ((place.getCategoryType() != 0 && place.getCategoryType() == AppConstants.CATEGORY_TOUR_TYPE)
                            || (place.getCategories() != null && place.getCategories().size() > 0 && place.getCategories().toString().contains(TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_TOUR_TYPE)))
                            ) {
                        llPlaceTourInfo.setVisibility(View.VISIBLE);

                        tvTourLocationValue.setText(place.getTourLocation());
                        tvTourDurationValue.setText(place.getTourDuration());
                        tvTourLanguageValue.setText(place.getTourLanguage());
                        tvTourGroupSizeValue.setText(place.getTourGroupSize());
                        tvTourTransportValue.setText(place.getTourTranspotation());

                        btnBookNow.setVisibility(View.VISIBLE);


                    } else {
                        llPlaceTourInfo.setVisibility(View.GONE);
                        btnBookNow.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void updateLinkify() {
//        Linkify.addLinks(tvTransportContent, "BlundellApps.com", "http://www.BlundellApps.com");
        Linkify.addLinks(tvTransportContent, Linkify.WEB_URLS);
        Linkify.addLinks(tvTransportContent, Linkify.ALL);
        tvTransportContent.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
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

        // Add lots of markers to the map.
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
//        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveStartedListener(this);
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
//        mMap.setMaxZoomPreference();


        updateMapBounder();

    }

    private void updateMapBounder() {
        if (mMap == null)
            return;
        if (place == null)
            return;

        if (place.getLongitude() == null || place.getLatitude() == null)
            return;

        try {
            LatLng latLng = new LatLng(Double.parseDouble(place.getLongitude()),Double.parseDouble(place.getLatitude()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            LoggerFactory.d("Animate Map Camera to place location : " + place.getLatitude() + ","+place.getLongitude());
            LoggerFactory.d("Animate Map Camera to place location : " + latLng.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void  addMarkersToMap() {
        if (mMap == null)
            return;
        if (place == null)
            return;

        if (place.getLongitude() == null || place.getLatitude() == null)
            return;
        try {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            Double.parseDouble(place.getLongitude()),
                            Double.parseDouble(place.getLatitude())))
                    .icon(vectorToBitmap(Place.getPlaceMaker(place)))
                    .title(place.getName())
                    .rotation(0));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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

        MySupportMapFragment mapFragment;
        mapFragment = (MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    nestedScrollPlaceDetail.requestDisallowInterceptTouchEvent(true);
                }
            });
        }
        new OnMapAndViewReadyListener(mapFragment, this);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        new OnMapAndViewReadyListener(mapFragment, this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex,
                Toast.LENGTH_SHORT).show();


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
//        LoggerFactory.d("onCamera move");
        isScrollEnable = false;
        scrollPlaceDetail.setEnabled(false);
        nestedScrollPlaceDetail.setEnabled(false);
    }

    @Override
    public void onCameraMoveStarted(int i) {
        LoggerFactory.d("OnCamera move started");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.LOCATION_REQUEST_ID: {
                if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    retrieveLatestKnownLocationAndCheckFiveDayWeather();
                } else {
                    Toast.makeText(this, "Cannot retrieve current location\nwithout permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void checkLocationPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            retrieveLatestKnownLocationAndCheckFiveDayWeather();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST_ID);
        }
    }


    private void retrieveLatestKnownLocationAndCheckFiveDayWeather() {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);

        try {
            locationProvider.getLastKnownLocation()
                    .subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            Coord coordinate = new Coord();
                            coordinate.setLat(location.getLatitude());
                            coordinate.setLon(location.getLongitude());
                            Utils.getDirection(mContext, String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()), place.getLongitude(), place.getLatitude());
                        }
                    });
        }
        catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Please accept location permission", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
