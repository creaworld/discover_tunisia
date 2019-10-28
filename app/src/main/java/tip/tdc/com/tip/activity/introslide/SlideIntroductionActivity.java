package tip.tdc.com.tip.activity.introslide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.home.HomeActivity;
import tip.tdc.com.tip.activity.login.LoginActivity;
import tip.tdc.com.tip.data.AppSetting;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.MSharedPreferences;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressHUD;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressInstance;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class SlideIntroductionActivity extends AppCompatActivity {
    String TAG = SlideIntroductionActivity.class.getSimpleName();
    public static int MAX_PAGE_SLIDER = 1000000;
    Context mContext;
    MSharedPreferences mSharedPreferences;

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, SlideIntroductionActivity.class);
        return in;
    }

    //TextView tvAppName, tvAppDes;
    //Button btnLogin, btnRegist, btnSkip, btnLoginFb;

    private ViewPager pager;
    private PageIndicator mPageIndicator;

    public static final Integer[] IMAGES_SLIDER = new Integer[]{
            /*R.drawable.app_bg_1,
            R.drawable.app_bg_2,
            R.drawable.app_bg_3,*/
            R.drawable.p0,
            R.drawable.p1,
            R.drawable.p2,
    };

    boolean isAutoScroll = false;
    SVProgressHUD mSVProgressHUD;

    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        boolean isDebuggable = (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
        if (!isDebuggable) {
            Fabric.with(this, new Crashlytics());
        }
        mContext = this;
        mSharedPreferences = MSharedPreferences.getInstance(mContext);

        if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getUid() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
            LoggerFactory.d("########################## User logined #########################");
            LoggerFactory.d("User name " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            LoggerFactory.d("User UID " + FirebaseAuth.getInstance().getCurrentUser().getUid());
            LoggerFactory.d("getPhoneNumber " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            LoggerFactory.d("getProviderData " + FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(0).getDisplayName());
            LoggerFactory.d("########################## User logined #########################");


            AppState.currentFireUser = FirebaseAuth.getInstance().getCurrentUser();



            if (TipApplication.getTipApplication().userService.isLoaded) {
                AppState.currentBpackUser = TipApplication.getTipApplication().userService.getUserById(AppState.currentFireUser.getUid());
                if (AppState.currentBpackUser != null) {
                    LoggerFactory.d("########################## LOAD User info firebase #########################");
                    LoggerFactory.d("AppState.currentBpackUser email" + AppState.currentBpackUser.getEmail());
                    LoggerFactory.d("AppState.currentBpackUser avatar" + AppState.currentBpackUser.getAvatar());
                    LoggerFactory.d("AppState.currentBpackUser name" + AppState.currentBpackUser.getName());
                    LoggerFactory.d("AppState.currentBpackUser user id" + AppState.currentBpackUser.getUserId());

                    LoggerFactory.d("########################## LOAD User info firebase #########################");
                } else {
                    LoggerFactory.d("WelComeScreen", "user info empty");
                }
            } else {
                LoggerFactory.d("WelComeScreen", "user service not load completed");
            }

            startActivity(HomeActivity.createIntent(mContext));
            this.finish();
        }
        initView();
        initController();
        mSVProgressHUD = SVProgressInstance.showWithStatus(mContext, "Loading...");

    }

    private void watcherInitAppData(){
        if(TipApplication.getTipApplication().languageService.getCount() == 0 || !TipApplication.getTipApplication().userService.isLoaded) {
            if(mSVProgressHUD != null && !mSVProgressHUD.isShowing()) {
                if (Utils.isInternetConnected(mContext) && !mSVProgressHUD.isShowing()) {
                    mSVProgressHUD.show();
                } else {

                }
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    watcherInitAppData();
                }
            },1000);
        } else {
            mSVProgressHUD.dismiss();
        }
    }

    Handler mHandler = new Handler();
    private void autoSlide(){
        if(!isAutoScroll)
            return;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(pager.getCurrentItem()+1, true);
                autoSlide();
            }
        }, 2000);
    }
    CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;

    private void initView() {
        /*btnSkip = (Button) findViewById(R.id.btnSkip);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLoginFb = (Button) findViewById(R.id.btnLoginFb);
        btnRegist = (Button) findViewById(R.id.btnRegist);

        tvAppName = (TextView) findViewById(R.id.tvAppName);
        FontUtils.setFont(tvAppName, FontUtils.TYPE_LIGHT);
        tvAppDes = (TextView) findViewById(R.id.tvAppDes);
        FontUtils.setFont(tvAppDes, FontUtils.TYPE_LIGHT);*/


        pager = (ViewPager) findViewById(R.id.pager);
        mPageIndicator = (PageIndicator) findViewById(R.id.indicator);

        loginBtn = findViewById(R.id.goto_login);

        mCallbackManager = CallbackManager.Factory.create();

        /*LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoggerFactory.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent intent = HomeActivity.createIntent(mContext);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                LoggerFactory.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                LoggerFactory.d(TAG, "facebook:onError", error);
            }
        });

        FontUtils.setFont(btnSkip, FontUtils.TYPE_NORMAL);
        /*FontUtils.setFont(btnLogin, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(btnRegist, FontUtils.TYPE_NORMAL);*/
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SlideIntroductionActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void initController() {
        /*btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TipApplication.getTipApplication().languageService.getCount() > 0) {
                    mSharedPreferences.putBoolean(AppSetting.KEY_IS_FIRST_LAUNCH, false);
                    gotoHomeWindow(mContext);    
                } else {
                    Toast.makeText(mContext, "Please try again. App is loading ...", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences.putBoolean(AppSetting.KEY_IS_FIRST_LAUNCH, false);
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("WINDOW_TYPE", "LOGIN");
                intent.putExtra("NEED_GO_HOME", true);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_up, R.anim.activity_exit_up);
                SlideIntroductionActivity.this.finish();
            }
        });

        btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences.putBoolean(AppSetting.KEY_IS_FIRST_LAUNCH, false);
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("WINDOW_TYPE", "LOGIN");
                intent.putExtra("AUTO_LOGIN_FB", true);
                intent.putExtra("NEED_GO_HOME", true);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_up, R.anim.activity_exit_up);
                SlideIntroductionActivity.this.finish();
            }
        });
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences.putBoolean(AppSetting.KEY_IS_FIRST_LAUNCH, false);
                gotoRegisterWindow(mContext);
                SlideIntroductionActivity.this.finish();
            }
        });*/


     loginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(mContext, LoginActivity.class);
             intent.putExtra("WINDOW_TYPE", "LOGIN");
             intent.putExtra("AUTO_LOGIN_FB", false);
             intent.putExtra("NEED_GO_HOME", true);
             mContext.startActivity(intent);
             ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_up, R.anim.activity_exit_up);
             SlideIntroductionActivity.this.finish();
         }
     });

        SlidePagerAdapter pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addAll(Arrays.asList(IMAGES_SLIDER));
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(300);
        /*pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position%3 == 0) {
                    tvAppDes.setText(R.string.msg_welcome_1);
                } else if (position%3 == 1) {
                    tvAppDes.setText(R.string.msg_welcome_2);
                } else if (position%3 == 2) {
                    tvAppDes.setText(R.string.msg_welcome_3);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        mPageIndicator.setViewPager(pager);
        mPageIndicator.setIndicatorType(PageIndicator.IndicatorType.CIRCLE);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();

        isAutoScroll = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        isAutoScroll = true;
        autoSlide();
        watcherInitAppData();

        if(AccessToken.getCurrentAccessToken() != null){
            mSharedPreferences.putBoolean(AppSetting.KEY_IS_FIRST_LAUNCH, false);
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("WINDOW_TYPE", "LOGIN");
            intent.putExtra("AUTO_LOGIN_FB", true);
            intent.putExtra("NEED_GO_HOME", true);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_up, R.anim.activity_exit_up);
            SlideIntroductionActivity.this.finish();
        }
    }

    public static void gotoLoginWindow(Context mContext) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("WINDOW_TYPE", "LOGIN");
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_up, R.anim.activity_exit_up);
    }

    public static void gotoHomeWindow(Context mContext) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
    }

    public static void gotoRegisterWindow(Context mContext) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("NEED_GO_HOME", true);
        intent.putExtra("WINDOW_TYPE", "REGISTER");
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_up, R.anim.activity_exit_up);
    }
}
