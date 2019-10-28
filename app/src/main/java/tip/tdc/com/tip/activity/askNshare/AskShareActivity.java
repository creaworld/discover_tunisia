/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.askNshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.Collections;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.DialogTip;
import tip.tdc.com.tip.activity.detail.TipView.TipFullScreenAdapter;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.interfaces.DialogCallback;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.ColorUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class AskShareActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, AskShareActivity.class);
        return in;
    }

    Context mContext;
    String TAG = AskShareActivity.class.getSimpleName();

    ArrayList<Place> listTip = new ArrayList<>();
    ArrayList<Place> listRecentTip = new ArrayList<>();
    ArrayList<Place> listTopTip = new ArrayList<>();
    RecyclerView recyclerViewCategory1;
    String cityKey;

    FCity mFCity;

    TextView tvRecentTip, tvTopTip;
    View rlRecentTip, viewRecentTip, rlTopTip, viewTopTip;
    static final int TAB_QUESTION = 0;
    static final int TAB_TIPS = 1;
    int tipTab = TAB_QUESTION;

    TipFullScreenAdapter tipFullScreenAdapter;

    PlaceService placeService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_place_tip);
        mContext = this;

        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY_KEY)) {
                cityKey = getIntent().getStringExtra(AppConstants.KEY_INTENT_CITY_KEY);
            }
        }

        bindView();
        loadData();
        Utils.setStatusBarColor(AskShareActivity.this, 0);
    }

    private void bindView() {
        rlRecentTip = findViewById(R.id.rlRecentTip);
        tvRecentTip = (TextView) findViewById(R.id.tvRecentTip);
        viewRecentTip = findViewById(R.id.viewRecentTip);


        tvTopTip = (TextView) findViewById(R.id.tvTopTip);
        rlTopTip = findViewById(R.id.rlTopTip);
        viewTopTip = findViewById(R.id.viewTopTip);

        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rlRecentTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRecentTip.setBackgroundColor(ColorUtils.getColor(mContext, R.color.colorBlue500));
                tvRecentTip.setTextColor(ColorUtils.getColor(mContext, R.color.colorBlue500));

                viewTopTip.setBackgroundColor(ColorUtils.getColor(mContext, R.color.colorGrey300));
                tvTopTip.setTextColor(ColorUtils.getColor(mContext, R.color.colorGrey600));

                listTip.clear();
                listTip.addAll(listRecentTip);
                Collections.sort(listTip, new Place.PlaceComparatorCreateDate());
                tipFullScreenAdapter.notifyDataSetChanged();
                tipTab =TAB_QUESTION;
            }
        });

        rlTopTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTopTip.setBackgroundColor(ColorUtils.getColor(mContext, R.color.colorBlue500));
                tvTopTip.setTextColor(ColorUtils.getColor(mContext, R.color.colorBlue500));

                viewRecentTip.setBackgroundColor(ColorUtils.getColor(mContext, R.color.colorGrey300));
                tvRecentTip.setTextColor(ColorUtils.getColor(mContext, R.color.colorGrey600));

                listTip.clear();
                listTip.addAll(listTopTip);
                Collections.sort(listTip, new Place.PlaceComparatorCreateDate());
                Collections.sort(listTip, new Place.PlaceComparatorCreateDate());
                tipFullScreenAdapter.notifyDataSetChanged();
                tipTab =TAB_TIPS;
            }
        });

        setCategory1View();

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
    }

    public void setCategory1View() {
        recyclerViewCategory1 = (RecyclerView)findViewById(R.id.cardView);
        recyclerViewCategory1.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(AskShareActivity.this);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCategory1.setLayoutManager(MyLayoutManager);
    }
    private void loadData() {
        if (cityKey != null) {
            LoggerFactory.d(TAG, " load citykey:"+cityKey);
            mFCity = TipApplication.getTipApplication().cityService.getCityByKey(cityKey);
            if (mFCity != null) {
                ((TextView) findViewById(R.id.topbarTitle)).setText(mFCity.getName());
            }
            final String category0Key = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_TIP_TYPE);
            LoggerFactory.d(TAG, "Tip category key:"+category0Key);


            listTip.clear();
            listTip.addAll(listRecentTip);
            Collections.sort(listTip, new Place.PlaceComparatorCreateDate());

            tipFullScreenAdapter = new TipFullScreenAdapter(listTip, mFCity);
            recyclerViewCategory1.setAdapter(tipFullScreenAdapter);

            placeService = new PlaceService(cityKey);
            placeService.setOnChangedListener(new ChangeEventListener() {
                @Override
                public void onChildChanged(EventType type, int index, int oldIndex) {

                }

                @Override
                public void onDataChanged() {
                    LoggerFactory.d(TAG, "PlaceService onDataChange");
                    if (placeService.getCount() == 0)
                        return;
                    listTopTip.clear();
                    listRecentTip.clear();
                    for (int i = 0; i < placeService.getCount(); i++) {
                        try {
                            Place place = placeService.getItem(i).getValue(Place.class);

                            if (place.getCategories() != null && place.getCategories().toString().contains(category0Key)) {
                                LoggerFactory.d(TAG, "parse place: " + place.toString());
                                place.setPlaceKey(placeService.getItem(i).getKey());
                                if (isAdminTip(place.getSubcategories())) {
                                    listTopTip.add(place);
                                } else {
                                    listRecentTip.add(place);
                                }
                                LoggerFactory.d(TAG, "listTopTip size:" + listTopTip.size());
                                LoggerFactory.d(TAG, "listRecentTip size:" + listRecentTip.size());
                            }
                        } catch (Exception e) {
                            LoggerFactory.logStackTrace(e);
                        }
                    }

                    listTip.clear();
                    if(tipTab == TAB_QUESTION) {
                        listTip.addAll(listRecentTip);
                    }else {
                        listTip.addAll(listTopTip);
                    }

                    Collections.sort(listTip, new Place.PlaceComparatorCreateDate());
                    tipFullScreenAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


        }
    }

    public static boolean isAdminTip(ArrayList<String> listSubCategory) {
        if (listSubCategory == null || listSubCategory.size() == 0)
            return false;

        for (int i = 0; i < listSubCategory.size(); i++) {
            if (listSubCategory.get(i).contains(AppConstants.TipAdmin)) {
                return true;
            }
        }

        return false;
    }
}
