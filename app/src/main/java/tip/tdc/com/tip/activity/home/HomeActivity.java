package tip.tdc.com.tip.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

import io.fabric.sdk.android.services.common.SafeToast;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.bookticket.BookTicketActivity;
import tip.tdc.com.tip.activity.introslide.SlideIntroductionActivity;
import tip.tdc.com.tip.activity.search.SearchActivity;
import tip.tdc.com.tip.activity.visa.VisaActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppSetting;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.database.RealmDB;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.CityService;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.utils.DialogRate;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressHUD;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressInstance;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;
import tip.tdc.com.tip.activity.detail.DetailTabsActivity;
import tip.tdc.com.tip.activity.information.AboutActivity;
import tip.tdc.com.tip.activity.information.PrivacyActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String TAG = HomeActivity.class.getSimpleName();

    private HomeCardsAdapter mCityCardsAdapter;
    ListView listviewCities;
    List<FCity> fCities = new LinkedList<FCity>();


    Context mContext;
    Handler mHandler = new Handler();
    private AdView mAdView;
    public static InterstitialAd mInterstitialAd;


    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, HomeActivity.class);
        return in;
    }

    //    Menu
//    Info
    View menuInfo1, menuInfo2, menuInfo3, menuInfo4, menuInfo5;
    View menuVisaOnline, menuBookTicket, menuAllCity;

    // User info in menu
    ImageView imvUserAvatar;
    TextView tvMenuUserName;
    View llHeaderUserProfile, llHeaderLogin;

    //    private DatabaseReference mCityReference;
    SVProgressHUD mSVProgressHUD;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = HomeActivity.this;
        AppState.currentFireUser = FirebaseAuth.getInstance().getCurrentUser();

        mSVProgressHUD = SVProgressInstance.showWithStatus(mContext, "Loading...");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        initController();
        loadDataView();
        loadUserInfo();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCities();
            }
        }, 2000);

        if (AppSetting.getInstant(mContext).getOpenCount() %5 == 0 && !AppSetting.getInstant(mContext).isAppRated()) {
            DialogRate dialogRate = new DialogRate();
            dialogRate.buildDialog(DialogRate.TYPE_LIKE, mContext, null);
            dialogRate.show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSVProgressHUD != null && mSVProgressHUD.isShowing()) {
            mSVProgressHUD.dismissImmediately();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mCityCardsAdapter != null) {
            mCityCardsAdapter.notifyDataSetChanged();
        }
    }

    CityService cityService;

    private void loadCities() {
        if (TipApplication.getTipApplication().countryService.getCount() > 0) {
            cityService = new CityService(TipApplication.getTipApplication().countryService.getItem(0).getKey());
            cityService.setOnChangedListener(new ChangeEventListener() {
                @Override
                public void onChildChanged(EventType type, int index, int oldIndex) {

                }

                @Override
                public void onDataChanged() {
                    LoggerFactory.d("cityService count:" + cityService.getCount());
                    fCities.clear();

                    fCities.addAll(cityService.getListCities());

                    mCityCardsAdapter.setCities(fCities);
                    mCityCardsAdapter.notifyDataSetChanged();

                    if (mSVProgressHUD != null && mSVProgressHUD.isShowing()) {
                        mSVProgressHUD.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
    }


    private void initView() {
        llHeaderUserProfile = findViewById(R.id.llHeaderUserProfile);
        imvUserAvatar = (ImageView) findViewById(R.id.imvUserAvatar);
        tvMenuUserName = (TextView) findViewById(R.id.tvMenuUserName);
        FontUtils.setFont(tvMenuUserName, FontUtils.TYPE_NORMAL);


        llHeaderLogin = findViewById(R.id.llHeaderLogin);
        FontUtils.setFont(findViewById(R.id.btnLogin), FontUtils.TYPE_NORMAL);
        FontUtils.setFont(findViewById(R.id.btnSignUp), FontUtils.TYPE_NORMAL);

        listviewCities = (ListView) findViewById(R.id.listviewCities);

        listviewCities.setDivider(null);
        listviewCities.setDividerHeight(0);

        // All city
        menuAllCity = findViewById(R.id.menuAllCity);
        menuAllCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        menuBookTicket = findViewById(R.id.menuBookTicket);
        menuBookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BookTicketActivity.createIntent(mContext));
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        menuVisaOnline = findViewById(R.id.menuVisaOnline);
        menuVisaOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(VisaActivity.createIntent(mContext));
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        FontUtils.setFont(findViewById(R.id.tvMenuInfomation), FontUtils.TYPE_NORMAL);
        // About
        menuInfo1 = findViewById(R.id.menuInfo1);
        FontUtils.setFont(findViewById(R.id.tvMenu1), FontUtils.TYPE_NORMAL);
        menuInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent1);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });
        //privacy
        menuInfo2 = findViewById(R.id.menuInfo2);
        FontUtils.setFont(findViewById(R.id.tvMenu2), FontUtils.TYPE_NORMAL);
        menuInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeActivity.this, PrivacyActivity.class);
                startActivity(intent1);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });
        //feedback
        menuInfo3 = findViewById(R.id.menuInfo3);
        FontUtils.setFont(findViewById(R.id.tvMenu3), FontUtils.TYPE_NORMAL);
        menuInfo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sendEmail(mContext, AppConstants.EMAIL_FEEDBACK);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });
        //rate
        menuInfo4 = findViewById(R.id.menuInfo4);
        FontUtils.setFont(findViewById(R.id.tvMenu4), FontUtils.TYPE_NORMAL);
        menuInfo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openAppInStore(mContext, mContext.getPackageName());
            }
        });
        //logout
        menuInfo5 = findViewById(R.id.menuInfo5);
        FontUtils.setFont(findViewById(R.id.tvMenu5), FontUtils.TYPE_NORMAL);
        menuInfo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    AppState.currentFireUser = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menuInfo5.setVisibility(View.GONE);
                llHeaderLogin.setVisibility(View.VISIBLE);
                llHeaderUserProfile.setVisibility(View.GONE);
                tvMenuUserName.setText("");
//                Intent intent1 = new Intent(HomeActivity.this, LoginActivity.class);
//                startActivity(intent1);
//                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlideIntroductionActivity.gotoLoginWindow(mContext);
            }
        });

        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlideIntroductionActivity.gotoRegisterWindow(mContext);
            }
        });

        findViewById(R.id.imbSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SearchActivity.createIntent(mContext);
                intent.putExtra(SearchActivity.SEARCH_TYPE, SearchActivity.SEARCH_TYPE_CITY);
                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });





        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("69BCA36FEFDE7BEF821BF6DBAEFC7952").build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("error-->>" ,"error code"+i);
            }
        });





        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.fullScreen));
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("69BCA36FEFDE7BEF821BF6DBAEFC7952").build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("error-->>" ,"error code"+i);

            }

            @Override
            public void onAdLoaded() {
                Log.e("error-->>" ,"onload");
            }
        });



    }

    private void initController() {
        mCityCardsAdapter = new HomeCardsAdapter(this);
//        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mCityCardsAdapter, this));
//        swingBottomInAnimationAdapter.setAbsListView(listView);
//
//        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
//        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        AnimationAdapter mAnimAdapter;
        mAnimAdapter = new ScaleInAnimationAdapter(mCityCardsAdapter, 0.9f);
        mAnimAdapter.setAbsListView(listviewCities);
        listviewCities.setAdapter(mAnimAdapter);

//        for (int i = 0; i < 100; i++) {
//            mCityCardsAdapter.add(i);
//        }

        listviewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Toast.makeText(mContext, "Loading ...", Toast.LENGTH_SHORT).show();

                    FCity fCity = fCities.get(i);
                    Intent intent = new Intent(HomeActivity.this, DetailTabsActivity.class);
                    intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, fCity.getCityKey());
                    intent.putExtra(AppConstants.KEY_INTENT_CITY, fCity);
                    startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);

            }
        });
    }

    private void loadDataView() {
//        mCityReference = FirebaseDatabase.getInstance().getReference().child("city");
//        mCityReference.keepSynced(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        StorageReference storageRef = storage.getReferenceFromUrl(Util.URL_STORAGE_REFERENCE).child(Util.FOLDER_STORAGE_IMG);
//
//        if (requestCode == IMAGE_GALLERY_REQUEST){
//            if (resultCode == RESULT_OK){
//                Uri selectedImageUri = data.getData();
//                if (selectedImageUri != null){
//                    sendFileFirebase(storageRef,selectedImageUri);
//                }else{
//                    //URI IS NULL
//                }
//            }
//        }else if (requestCode == IMAGE_CAMERA_REQUEST){
//            if (resultCode == RESULT_OK){
//                if (filePathImageCamera != null && filePathImageCamera.exists()){
//                    StorageReference imageCameraRef = storageRef.child(filePathImageCamera.getName()+"_camera");
//                    sendFileFirebase(imageCameraRef,filePathImageCamera);
//                }else{
//                    //IS NULL
//                }
//            }
//        }else if (requestCode == PLACE_PICKER_REQUEST){
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(this, data);
//                if (place!=null){
//                    LatLng latLng = place.getLatLng();
//                    MapModel mapModel = new MapModel(latLng.latitude+"",latLng.longitude+"");
//                    ChatModel chatModel = new ChatModel(userModel,Calendar.getInstance().getTime().getTime()+"",mapModel);
//                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(chatModel);
//                }else{
//                    //PLACE IS NULL
//                }
//            }
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
        mCityCardsAdapter.notifyDataSetChanged();
    }


    private void loadUserInfo() {
        try {
            if (AppState.currentFireUser != null) {
                menuInfo5.setVisibility(View.VISIBLE);
                llHeaderUserProfile.setVisibility(View.VISIBLE);
                llHeaderLogin.setVisibility(View.GONE);

                if (AppState.currentBpackUser == null) {
                    AppState.currentBpackUser = TipApplication.getTipApplication().userService.getUserById(AppState.currentFireUser.getUid());
                }

                if (AppState.currentBpackUser != null) {
                    tvMenuUserName.setText(AppState.currentBpackUser.getName());
                } else if (AppState.currentFireUser.getDisplayName() != null && AppState.currentFireUser.getDisplayName().length() > 0) {
                    tvMenuUserName.setText(AppState.currentFireUser.getDisplayName());
                } else if (AppState.currentFireUser.getEmail() != null) {
                    tvMenuUserName.setText(AppState.currentFireUser.getEmail());
                }

                if (AppState.photo_url != null) {
                    ImageLoader.getInstance().displayImage(AppState.photo_url, imvUserAvatar, TipApplication.defaultOptions);
                } else if (AppState.currentBpackUser != null && AppState.currentBpackUser.getAvatar() != null && AppState.currentBpackUser.getAvatar() != null && AppState.currentBpackUser.getAvatar().contains("http")) {
                    ImageLoader.getInstance().displayImage(AppState.currentBpackUser.getAvatar().replace("http:", "https:"), imvUserAvatar, TipApplication.defaultOptions);
                } else if (AppState.currentFireUser.getPhotoUrl() != null && AppState.currentFireUser.getPhotoUrl().getPath() != null && AppState.currentFireUser.getPhotoUrl().getPath().contains("http")) {
                    ImageLoader.getInstance().displayImage(AppState.currentFireUser.getPhotoUrl().getPath(), imvUserAvatar, TipApplication.defaultOptions);
                } else {
                    imvUserAvatar.setImageResource(R.drawable.avatar_default);
                }
            } else {
                LoggerFactory.d("HOME", "USER NULL");
                menuInfo5.setVisibility(View.GONE);
                llHeaderUserProfile.setVisibility(View.GONE);
                llHeaderLogin.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                }
                break;
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // Add value event listener to the post
//        // [START post_value_event_listener]
//        ValueEventListener cityListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
////                Post post = dataSnapshot.getValue(Post.class);
////                // [START_EXCLUDE]
////                mAuthorView.setText(post.author);
////                mTitleView.setText(post.title);
////                mBodyView.setText(post.body);
//                // [END_EXCLUDE]
//
////                GenericTypeIndicator<List<FCity>> t = new GenericTypeIndicator<List<FCity>>() {};
////                List messages = dataSnapshot.getValue(t);
////                if( messages == null ) {
////                    LoggerFactory.d("no messages");
////                }
////                else {
////                    System.out.println("The first message is: " + messages.get(0) );
////
////                }
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    //Getting the data from snapshot
//                    FCity person = postSnapshot.getValue(FCity.class);
//
//                    //Adding it to a string
//                    String string = "City Name: "+person.getName()+"\nAddress: "+person.getPriority()+"\n\n";
//
//                    //Displaying it on textview
//                    LoggerFactory.d(string);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                LoggerFactory.d(TAG, "loadPost:onCancelled", databaseError.toException());
//                // [START_EXCLUDE]
//                Toast.makeText(HomeActivity.this, "Failed to load post.",
//                        Toast.LENGTH_SHORT).show();
//                // [END_EXCLUDE]
//            }
//        };
//        mCityReference.addValueEventListener(cityListener);
//        // [END post_value_event_listener]
//
//        // Keep copy of post listener so we can remove it when app stops
////        mPostListener = postListener;
//
//        // Listen for comments
////        mAdapter = new CommentAdapter(this, mCommentsReference);
////        mCommentsRecycler.setAdapter(mAdapter);
//    }

}
