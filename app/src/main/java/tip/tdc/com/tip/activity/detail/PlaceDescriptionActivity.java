package tip.tdc.com.tip.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.activity.bookticket.BookTicketActivity;
import tip.tdc.com.tip.activity.detail.placeFragment.DescriptionFragment;
import tip.tdc.com.tip.activity.detail.placeFragment.FacilitiesFragment;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class PlaceDescriptionActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, PlaceDescriptionActivity.class);
        return in;
    }

    Place place;
    TextView tvDescription, topbarTitle;
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
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        topbarTitle = (TextView) findViewById(R.id.topbarTitle);
        setTitle("Description");

        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((TextView) findViewById(R.id.topbarTitle)).setText("Description");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        FontUtils.setFont(tabLayout, FontUtils.TYPE_NORMAL);
        Utils.setStatusBarColor(PlaceDescriptionActivity.this, 0);
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_PLACE)) {
                place = (Place) getIntent().getSerializableExtra(AppConstants.KEY_INTENT_PLACE);
                if (place != null) {
                    tvDescription.setText(place.getDescription());
                    updateLinkify(tvDescription);

                    // primary sections of the activity.
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    topbarTitle.setText(place.getName());
                }
            }
        }
    }

    List<String> listInfo = new LinkedList<>();

    private int getNumberDetailInfo() {
        listInfo.clear();

        if (place.getDescription() != null && place.getDescription().length() > 0) {
            listInfo.add("Description,"+DescriptionFragment.DESCRIPTION_TYPE_DES);
        }

        if (place.getFacilities() != null && place.getFacilities().size() > 0) {
            listInfo.add("Facilities,"+DescriptionFragment.DESCRIPTION_TYPE_FACILITY);
        }

        if (place.getThingstonote() != null && place.getThingstonote().length() > 0) {
            listInfo.add("Note,"+DescriptionFragment.DESCRIPTION_TYPE_NOTE);
        }


        return listInfo.size();
    }

    private void updateLinkify(TextView textView) {
//        Linkify.addLinks(textView, "BlundellApps.com", "http://www.BlundellApps.com");
        Linkify.addLinks(textView, Linkify.WEB_URLS);
        Linkify.addLinks(textView, Linkify.ALL);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (DescriptionFragment.DESCRIPTION_TYPE_DES == Integer.parseInt(listInfo.get(position).split(",")[1])) {
                DescriptionFragment homeFragment = new DescriptionFragment();
                homeFragment.setDescriptionType(DescriptionFragment.DESCRIPTION_TYPE_DES);
                homeFragment.setPlace(place);
                return homeFragment;
            } else if (DescriptionFragment.DESCRIPTION_TYPE_FACILITY == Integer.parseInt(listInfo.get(position).split(",")[1])) {
                FacilitiesFragment homeFragment = new FacilitiesFragment();
                homeFragment.setPlace(place);
                return homeFragment;
            } else {
                DescriptionFragment homeFragment = new DescriptionFragment();
                homeFragment.setDescriptionType(DescriptionFragment.DESCRIPTION_TYPE_NOTE);
                homeFragment.setPlace(place);
                return homeFragment;
            }
        }

        @Override
        public int getCount() {
            return getNumberDetailInfo();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listInfo.get(position).split(",")[0];
        }
    }
}
