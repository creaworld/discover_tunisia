package tip.tdc.com.tip.activity.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.information.PrivacyActivity;
import tip.tdc.com.tip.activity.introslide.SlideIntroductionActivity;
import tip.tdc.com.tip.data.AppSetting;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.interfaces.DialogCallback;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.UserService;
import tip.tdc.com.tip.utils.ColorUtils;
import tip.tdc.com.tip.utils.FontUtils;
import com.bpackingapp.vietnam.travel.R;

import org.json.JSONException;
import org.json.JSONObject;

import tip.tdc.com.tip.activity.home.HomeActivity;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.MSharedPreferences;
import tip.tdc.com.tip.utils.Utils;

import static tip.tdc.com.tip.activity.introslide.SlideIntroductionActivity.gotoHomeWindow;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    String TAG = LoginActivity.class.getSimpleName();
//    /**
//     * Id to identity READ_CONTACTS permission request.
//     */
//    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    LoginButton loginButton;
    RelativeLayout rlMaskLogin;
    private Spinner langSpinner;

    UserService userService;

    private Button skipBtn, conditionsBtn;

    private boolean isLogin = true;

    boolean needGoHome = false;

    private MSharedPreferences mSharedPreferences;

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, LoginActivity.class);
        return in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mSharedPreferences = MSharedPreferences.getInstance(this);
        // Set up the login form.
        rlMaskLogin = (RelativeLayout) findViewById(R.id.rlMaskLogin);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
//        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btnLoginFb = (Button) findViewById(R.id.btnLoginFb);
        btnLoginFb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFBLoggedIn()){
                    handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
                } else {
                    loginButton.performClick();
                }
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        String termsAndConditions = getResources().getString(R.string.terms_and_conditions);
        //customTextView((TextView) findViewById(R.id.tvTermCondition));
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        bindView();

        parseParentIntent();

        Utils.setStatusBarColor(LoginActivity.this, 0);

        userService = new UserService();
        userService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                userService.isLoaded = true;
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        if (isFBLoggedIn()) {
            handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
        }
    }

    private void parseParentIntent() {
        if (getIntent() != null) {
            if (getIntent().hasExtra("WINDOW_TYPE") && getIntent().getStringExtra("WINDOW_TYPE").equalsIgnoreCase("REGISTER")) {
                openSignUpView();
            } else if (getIntent().hasExtra("WINDOW_TYPE") && getIntent().getStringExtra("WINDOW_TYPE").equalsIgnoreCase("LOGIN")) {
                openLoginView();
            } else {
                openLoginView();
            }

            if(getIntent().hasExtra("AUTO_LOGIN_FB") && getIntent().getBooleanExtra("AUTO_LOGIN_FB", false)){
                if (isFBLoggedIn()) {
                    handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
                } else {
                    rlMaskLogin.setVisibility(View.VISIBLE);
                    loginButton.performClick();
                }
            }

            if(getIntent().hasExtra("NEED_GO_HOME") && getIntent().getBooleanExtra("NEED_GO_HOME", false)){
                needGoHome = true;
            }
        } else {
            openLoginView();
        }
    }

    CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    Context mContext;


    LinearLayout llLogin, llSignUp;
    //View indicatorLogin, indicatorSignUp;
    AutoCompleteTextView actvEmailLogin;
    EditText actvPasswordLogin;
    EditText signupUsername, signupEmail, signupPassword, signupPasswordConfirm;

    Button btnLoginInput, btnSignupInput;

    Button btnForgotPassword, btnSignUp;

    private void bindView() {
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        //indicatorLogin = findViewById(R.id.indicatorLogin);
        llLogin.setVisibility(View.VISIBLE);

        langSpinner= findViewById(R.id.lang_spinner);
        List<String> langs = new ArrayList<>();

        langs.add("Languages");
        langs.add("English");
        langs.add("Français");
        langs.add("العربية");

        langSpinner.setPrompt("Languages");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_lang, langs);

        langSpinner.setAdapter(dataAdapter);

        dataAdapter.setDropDownViewResource(R.layout.spinner_item_lang_dropdown);

        skipBtn = findViewById(R.id.skip_btn);
        conditionsBtn = findViewById(R.id.terms_btn);

        conditionsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TermOfServiceActivity.createIntent(mContext));
            }
        });


        llSignUp = (LinearLayout) findViewById(R.id.llSignUp);
        //indicatorSignUp = findViewById(R.id.indicatorSignup);
        llSignUp.setVisibility(View.VISIBLE);

        actvEmailLogin = (AutoCompleteTextView) findViewById(R.id.email);
        actvPasswordLogin = (EditText) findViewById(R.id.password);

        signupUsername = (EditText) findViewById(R.id.signupUsername);
        signupEmail = (EditText) findViewById(R.id.signupEmail);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        signupPasswordConfirm = (EditText) findViewById(R.id.signupPasswordConfirm);


        btnLoginInput = findViewById(R.id.btnLoginInput);
        FontUtils.setFont(btnLoginInput, FontUtils.TYPE_NORMAL);
        btnSignupInput = findViewById(R.id.btnSignupInput);
        FontUtils.setFont(btnSignupInput, FontUtils.TYPE_NORMAL);


        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        FontUtils.setFont(btnForgotPassword, FontUtils.TYPE_NORMAL);
        btnForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForotPassword dialogForotPassword = new DialogForotPassword();
                dialogForotPassword.buildDialog(mContext, new DialogCallback() {
                    @Override
                    public void OnOkSelected() {

                    }

                    @Override
                    public void OnCancelSelected() {

                    }
                });
                dialogForotPassword.show();
            }
        });
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(signupUsername.getText().toString(), signupEmail.getText().toString(), signupPassword.getText().toString(), signupPasswordConfirm.getText().toString());
            }
        });

        btnLoginInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginView();
            }
        });

        btnSignupInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpView();
            }
        });


        skipBtn.setOnClickListener(new OnClickListener() {
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

        findViewById(R.id.imvCloseLogin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
                finish();
                startActivity(SlideIntroductionActivity.createIntent(mContext));
            }
        });


        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoggerFactory.d("!!!!!!!!!!!!!!!!!!!!! LOGIN FACEBOOK SUCCESS !!!!!!!!!!!!!!!!!!!!");
                LoggerFactory.d(TAG, "facebook:onSuccess:" + loginResult.toString());
                LoggerFactory.d(TAG, "facebook:getRecentlyDeniedPermissions:" + loginResult.getRecentlyDeniedPermissions().toString());
                LoggerFactory.d(TAG, "facebook:getToken:" + loginResult.getAccessToken().getToken());
                LoggerFactory.d(TAG, "facebook:getUserId:" + loginResult.getAccessToken().getUserId());
                LoggerFactory.d(TAG, "facebook:getExpires:" + loginResult.getAccessToken().getExpires());
                LoggerFactory.d("!!!!!!!!!!!!!!!!!!!!! LOGIN FACEBOOK SUCCESS !!!!!!!!!!!!!!!!!!!!");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                rlMaskLogin.setVisibility(View.GONE);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                rlMaskLogin.setVisibility(View.GONE);
            }
        });
    }

    private void openLoginView() {
        isLogin = true;
        rlMaskLogin.setVisibility(View.GONE);
        //btnLoginInput.setTextColor(Utils.getColor(mContext, R.color.colorBlue500));
        //indicatorLogin.setBackgroundColor(Utils.getColor(mContext, R.color.colorBlue500));
        llLogin.setVisibility(View.VISIBLE);

        //btnSignupInput.setTextColor(Utils.getColor(mContext, R.color.colorContent));
        //indicatorSignUp.setBackgroundColor(Utils.getColor(mContext, R.color.colorLine));
        llSignUp.setVisibility(View.GONE);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            view.clearFocus();
        }
    }

    private void openSignUpView() {
        isLogin = false;
        rlMaskLogin.setVisibility(View.GONE);
        //btnLoginInput.setTextColor(Utils.getColor(mContext, R.color.colorContent));
        //indicatorLogin.setBackgroundColor(ColorUtils.getColor(mContext, R.color.colorLine));
        llLogin.setVisibility(View.GONE);

        //btnSignupInput.setTextColor(Utils.getColor(mContext, R.color.colorBlue500));
        //indicatorSignUp.setBackgroundColor(Utils.getColor(mContext, R.color.colorBlue500));
        llSignUp.setVisibility(View.VISIBLE);


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            view.clearFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentFireUser = mAuth.getCurrentUser();
//        if (currentFireUser != null) {
//            findViewById(R.id.llLogin).setVisibility(View.VISIBLE);
//            LoginActivity.this.startActivity(HomeActivity.createIntent(LoginActivity.this));
//        }
//        updateUI(currentFireUser);
    }

    private void customTextView(TextView view) {

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "By signing up, you agree to the ");
        int length1 = spanTxt.length();
        spanTxt.append("Terms of service");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(TermOfServiceActivity.createIntent(mContext));
            }
        }, spanTxt.length() - "Term of services".length(), spanTxt.length(), 0);
        spanTxt.append(" and");

        spanTxt.setSpan(new ForegroundColorSpan(Utils.getColor(mContext, R.color.colorBlue500)), length1, spanTxt.length()-4, 0);
        spanTxt.setSpan(new ForegroundColorSpan(Utils.getColor(mContext, R.color.colorContent)), spanTxt.length() -4, spanTxt.length(), 0);
        length1 = spanTxt.length();
        spanTxt.append(" Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(PrivacyActivity.createIntent(mContext));
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Utils.getColor(mContext, R.color.colorBlue500)), length1, spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }

//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                AppState.currentFireUser = mAuth.getCurrentUser();
                                AppState.currentBpackUser = TipApplication.getTipApplication().userService.getUserById(AppState.currentFireUser.getUid());
                                showProgress(false);
                                Toast.makeText(LoginActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                                loginSuccess();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                showProgress(false);
//                                updateUI(null);
                            }

                            // ...
                        }
                    });

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void loginSuccess() {
        if(needGoHome) {
            startActivity(HomeActivity.createIntent(mContext).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
        }
        finish();
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        LoggerFactory.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            LoggerFactory.d(TAG, "signInWithCredential:success");
                            AppState.currentFireUser = mAuth.getCurrentUser();
                            AppState.currentBpackUser = TipApplication.getTipApplication().userService.getUserById(AppState.currentFireUser.getUid());
                            LoggerFactory.d(TAG, "signInWithCredential:success with " + AppState.currentFireUser.toString());

                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback(){

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {
                                        AppState.email_address = object.getString("email");
                                        AppState.first_name = object.getString("first_name");
                                        AppState.last_name = object.getString("last_name");
                                    } catch (JSONException e) {
                                        LoggerFactory.logStackTrace(e);
                                    }
                                    LoggerFactory.d("!!!!!!!!!!!!!!!!!!!!!!!!!! GRAPH REQUEST BY FACEBOOK !!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    LoggerFactory.d("###### AppState.email_address:"+AppState.email_address);
                                    LoggerFactory.d("###### AppState.first_name:"+AppState.first_name);
                                    LoggerFactory.d("###### AppState.last_name:"+AppState.last_name);

                                    LoggerFactory.d("!!!!!!!!!!!!!!!!!!!!!!!!!! GRAPH REQUEST BY FACEBOOK !!!!!!!!!!!!!!!!!!!!!!!!!!");

                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields","id,email,first_name,last_name");
                            request.setParameters(parameters);
                            request.executeAsync();

                            LoggerFactory.d("!!!!!!!!!!!!!!!!!!!!!!!!!! LOGIN FIREBASE BY FACEBOOK !!!!!!!!!!!!!!!!!!!!!!!!!!");

                            LoggerFactory.d("###### User email:"+AppState.currentFireUser.getEmail());


                            if (AppState.currentFireUser != null) {
                                for (UserInfo profile : AppState.currentFireUser.getProviderData()) {
                                    // Id of the provider (ex: google.com)
                                    String providerId = profile.getProviderId();

                                    // UID specific to the provider
                                    String uid = profile.getUid();

                                    // Name, email address, and profile photo Url
                                    String name = profile.getDisplayName();
                                    String email = profile.getEmail();
                                    Uri photoUrl = profile.getPhotoUrl();
                                    LoggerFactory.d("###### getPhotoUrl:" + photoUrl);
                                    LoggerFactory.d("###### email:"+email);
                                };
                            }

                            String uid=task.getResult().getUser().getUid();
                            String name=task.getResult().getUser().getDisplayName();
                            String email=task.getResult().getUser().getEmail();
                            String image=task.getResult().getUser().getPhotoUrl().toString();

                            LoggerFactory.d("$$$$$$ uid:"+uid);
                            LoggerFactory.d("$$$$$$ name:"+name);
                            LoggerFactory.d("$$$$$$ email:"+email);
                            LoggerFactory.d("$$$$$$ image:"+image);
                            LoggerFactory.d("!!!!!!!!!!!!!!!!!!!!!!!!!! LOGIN FIREBASE BY FACEBOOK !!!!!!!!!!!!!!!!!!!!!!!!!!");

                            if (image != null && image.length() > 0) {
                                AppState.photo_url = image;
                            }if (email != null && email.length() > 0) {
                                AppState.email_address = email;
                            }

                            // create user if not exit on firebase
                            if (AppState.currentBpackUser == null) {
                                createUser(AppState.currentFireUser);
                            } else {
                                if (AppState.currentFireUser.getEmail() != null && AppState.currentFireUser.getEmail().length() > 0) {
                                    loginSuccess();

                                    userService.updateUser(name, uid, image, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            Toast.makeText(mContext, "Update user info completed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(mContext, "Login failed, Please provide email", Toast.LENGTH_SHORT).show();
                                    TipApplication.getTipApplication().logout();
                                    rlMaskLogin.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            LoggerFactory.d(TAG, "signInWithCredential:failure:"+ task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            rlMaskLogin.setVisibility(View.GONE);
                            TipApplication.getTipApplication().logout();


                            try {
                                if (task != null && task.getResult() != null) {
                                    if (task.getResult().getUser() != null
                                            && TipApplication.getTipApplication().userService.getUserById(task.getResult().getUser().getUid()) != null) {
                                        Toast.makeText(LoginActivity.this, "The email address is already used by another account", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (task.getResult().getUser() != null
                                            && TipApplication.getTipApplication().userService.getUserByEmail(task.getResult().getUser().getEmail()) != null) {
                                        Toast.makeText(LoginActivity.this, "The email address is already used by another account", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                LoggerFactory.logStackTrace(e);
                            }

                            Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void signUp(final String accountName, final String email, final String password, final String passConfirm) {

        if (email == null || email.length() == 0) {
            Toast.makeText(mContext, "Email invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password == null || password.length() == 0) {
            Toast.makeText(mContext, "Email invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.contains(" ")) {
            Toast.makeText(mContext, "Password invalid", Toast.LENGTH_SHORT).show();
            return;
        }

       /* if (!password.equals(passConfirm)) {
            Toast.makeText(mContext, "Password not match", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if(userService.isLoaded) {
            if(userService.getUserByEmail(email) != null) {
                Toast.makeText(mContext, "Email registered, Please login", Toast.LENGTH_LONG).show();
            }
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            AppState.currentFireUser = mAuth.getCurrentUser();
                            AppState.currentBpackUser = TipApplication.getTipApplication().userService.getUserById(AppState.currentFireUser.getUid());

                            LoggerFactory.d("AppState.currentFireUser.getUid:"+AppState.currentFireUser.getUid());
                            LoggerFactory.d("AppState.currentFireUser.getProviderId:"+AppState.currentFireUser.getProviderId());
                            LoggerFactory.d("AppState.currentFireUser.getDisplayName:"+AppState.currentFireUser.getDisplayName());
                            if(AppState.currentBpackUser == null) {
                                createUser(email, accountName, AppState.currentFireUser.getUid());
                            } else {
                                loginSuccess();
                            }

                            signupEmail.setText("");
                            signupPassword.setText("");
                            signupPasswordConfirm.setText("");
                            signupUsername.setText("");
                            Toast.makeText(mContext, "Register successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void createUser(String email, String accountName, String userid){
        LoggerFactory.d("Create User: email:"+ email + "/accountName:" + accountName + "/userid:"+userid);
        userService.registerUser(email, accountName, userid, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    Toast.makeText(mContext, "Sigup success", Toast.LENGTH_SHORT).show();
                    loginSuccess();
                } else {
                    Toast.makeText(mContext, "Sigup unsuccess", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(FirebaseUser currentUser){
        LoggerFactory.d("Create User: "+ currentUser.toString());
//        https://graph.facebook.com/" + facebookUserId + "/picture?height=500"
        if (currentUser.getEmail() != null && currentUser.getEmail().length() > 0) {
            String userAvatarUrl = "https://scontent.xx.fbcdn.net" + currentUser.getPhotoUrl().getPath();
            if (AppState.photo_url != null) {
                userAvatarUrl = AppState.photo_url;
            }
            userService.registerUser(currentUser.getEmail(), currentUser.getDisplayName(), currentUser.getUid(), userAvatarUrl, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(mContext, "Sigup success", Toast.LENGTH_SHORT).show();
                        loginSuccess();
                    } else {
                        Toast.makeText(mContext, "Sigup failed - database error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(mContext, "Sigup failed, email empty. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isFBLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onBackPressed() {
        if (isLogin) super.onBackPressed();
        else openLoginView();
    }
}

