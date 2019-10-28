package tip.tdc.com.tip.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;
import tip.tdc.com.tip.utils.LoggerFactory;

public class TransportActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, TransportActivity.class);
        return in;
    }

    private SliderLayout mDemoSlider;
    Context mContext;
    RelativeLayout rlToolBar;
    TextView tvNumberLove, tvNumberComment;
    TextView tvTitleActivity, tvTransportContent, tvSliderName;
    ImageView imvTopbarBack;
    ImageView imvLoveStatus, imvCommentStatus;
    int maxHeightHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        mContext = this;

        Utils.setStatusBarColor(TransportActivity.this, 0);

        initView();
        loadData();

        getIntentData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        rlToolBar = (RelativeLayout) findViewById(R.id.rlToolBar);

        tvTitleActivity = (TextView) findViewById(R.id.tvTitleActivity);
        FontUtils.setFont(tvTitleActivity, FontUtils.TYPE_NORMAL);
        tvTransportContent = (TextView) findViewById(R.id.tvTransportContent);
        tvSliderName = (TextView) findViewById(R.id.tvSliderName);
        FontUtils.setFont(tvSliderName, FontUtils.TYPE_NORMAL);
        tvNumberLove = (TextView) findViewById(R.id.tvNumberLove);
        FontUtils.setFont(tvNumberLove, FontUtils.TYPE_LIGHT);
        tvNumberComment = (TextView) findViewById(R.id.tvNumberComment);
        FontUtils.setFont(tvNumberComment, FontUtils.TYPE_LIGHT);


        imvTopbarBack = (ImageView) findViewById(R.id.imvTopbarBack);
        imvLoveStatus = (ImageView) findViewById(R.id.imvLoveStatus);
        imvCommentStatus = (ImageView) findViewById(R.id.imvCommentStatus);
        imvTopbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
    }

    private void loadData() {
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.INTENT_CLASS)) {
                if (getIntent().getStringExtra(AppConstants.INTENT_CLASS).equalsIgnoreCase(AppConstants.INTENT_CLASS_CITY_INFO)) {
                    tvTitleActivity.setText("Get to Know");
                    tvSliderName.setText("Get to Know");
                    Place place = (Place) getIntent().getSerializableExtra(HomeFragment.CITY_INFO_DATA);
                    if (place != null) {
                        tvTransportContent.setText(place.getDescription());
                        updateLinkify();

                        tvNumberComment.setText(place.getCommentCount() + "");
                        if (place.getLoved() != null && place.getLoved().length() > 0) {
                            tvNumberLove.setText(place.getLoved().split("#").length + "");
                        }


                        for (String photo : place.getPhotos()) {
                            TextSliderView textSliderView = new TextSliderView(this);
                            textSliderView
                                    .description("")
                                    .image(photo)
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(this);
                            mDemoSlider.addSlider(textSliderView);
                        }
                    }
                } else if (getIntent().getStringExtra(AppConstants.INTENT_CLASS).equalsIgnoreCase(AppConstants.INTENT_CLASS_EMERGENCY)) {
                    tvTitleActivity.setText("Emergency");
                    tvSliderName.setText("Emergency");
                    Place place = (Place) getIntent().getSerializableExtra(HomeFragment.EMERGENCY_DATA);
                    if (place != null) {
                        tvTransportContent.setText(place.getDescription());
                        updateLinkify();

                        tvNumberComment.setText(place.getCommentCount() + "");
                        if (place.getLoved() != null && place.getLoved().length() > 0) {
                            tvNumberLove.setText(place.getLoved().split("#").length + "");
                        }


                        for (String photo : place.getPhotos()) {
                            TextSliderView textSliderView = new TextSliderView(this);
                            // initialize a SliderLayout
                            textSliderView
                                    .description("")
                                    .image(photo)
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(this);

//                            //add your extra information
//                            textSliderView.bundle(new Bundle());
//                            textSliderView.getBundle()
//                                    .putString("extra",name);

                            mDemoSlider.addSlider(textSliderView);
                        }

                    }
                } else if (getIntent().getStringExtra(AppConstants.INTENT_CLASS).equalsIgnoreCase(AppConstants.INTENT_CLASS_TRANSPORT)) {
                    tvTitleActivity.setText("Transport");
                    tvSliderName.setText("Transport");
                    String transportText = "";
                    long numberComment = 0;
                    long numberLove = 0;

                    for (int i = 0; i < HomeFragment.transportData.size(); i++) {
                        Place place = HomeFragment.transportData.get(i);
                        if (place != null) {
                            transportText+= place.getDescription();
                            numberComment+= place.getCommentCount();

                            if (place.getLoved() != null && place.getLoved().length() > 0) {
                                numberLove+=place.getLoved().split("#").length;
                            }

                            for (String photo : place.getPhotos()) {
                                TextSliderView textSliderView = new TextSliderView(this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description("")
                                        .image(photo)
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(this);

//                            //add your extra information
//                            textSliderView.bundle(new Bundle());
//                            textSliderView.getBundle()
//                                    .putString("extra",name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                        }
                    }

                    tvTransportContent.setText(transportText);
                    updateLinkify();

                    tvNumberComment.setText(numberComment + "");
                    tvNumberLove.setText(numberLove + "");
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_custom_indicator:
//                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
//                break;
//            case R.id.action_custom_child_animation:
//                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
//                break;
//            case R.id.action_restore_default:
//                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//                break;
//            case R.id.action_github:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
//                startActivity(browserIntent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
}
