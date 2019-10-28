/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.detail.TipView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.fabric.sdk.android.services.common.SafeToast;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.DetailTabsActivity;
import tip.tdc.com.tip.activity.detail.TipView.TipFullScreenAdapter;
import tip.tdc.com.tip.activity.detail.seedoo.SeeDoFullScreenAdapter;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.utils.ColorUtils;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 8/15/17.
 */

public class TipFragment extends Fragment {

    ArrayList<Place> listTip = new ArrayList<>();
    ArrayList<Place> listRecentTip = new ArrayList<>();
    ArrayList<Place> listTopTip = new ArrayList<>();
    RecyclerView recyclerViewCategory1;
    DetailTabsActivity detailTabsActivity;
    CategoryInfo categoryInfo;

    TextView tvRecentTip, tvTopTip;
    View rlRecentTip, viewRecentTip, rlTopTip, viewTopTip;

    TipFullScreenAdapter tipFullScreenAdapter;

    public void setDetailTabsActivity(DetailTabsActivity detailTabsActivity) {
        this.detailTabsActivity = detailTabsActivity;
    }

    public void setCategoryInfo(CategoryInfo categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_place_tip, container, false);
        setCategory1View(view);

        rlRecentTip = view.findViewById(R.id.rlRecentTip);
        tvRecentTip = (TextView) view.findViewById(R.id.tvRecentTip);
        viewRecentTip = view.findViewById(R.id.viewRecentTip);


        tvTopTip = (TextView) view.findViewById(R.id.tvTopTip);
        rlTopTip = view.findViewById(R.id.rlTopTip);
        viewTopTip = view.findViewById(R.id.viewTopTip);

        rlRecentTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRecentTip.setBackgroundColor(ColorUtils.getColor(getContext(), R.color.colorBlue500));
                tvRecentTip.setTextColor(ColorUtils.getColor(getContext(), R.color.colorBlue500));

                viewTopTip.setBackgroundColor(ColorUtils.getColor(getContext(), R.color.colorGrey300));
                tvTopTip.setTextColor(ColorUtils.getColor(getContext(), R.color.colorGrey600));

                listTip.clear();
                listTip.addAll(listRecentTip);
                tipFullScreenAdapter.notifyDataSetChanged();
            }
        });

        rlTopTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTopTip.setBackgroundColor(ColorUtils.getColor(getContext(), R.color.colorBlue500));
                tvTopTip.setTextColor(ColorUtils.getColor(getContext(), R.color.colorBlue500));

                viewRecentTip.setBackgroundColor(ColorUtils.getColor(getContext(), R.color.colorGrey300));
                tvRecentTip.setTextColor(ColorUtils.getColor(getContext(), R.color.colorGrey600));

                listTip.clear();
                listTip.addAll(listTopTip);
                tipFullScreenAdapter.notifyDataSetChanged();
            }
        });
        initData();
        return view;
    }

    public void setCategory1View(View view) {
        recyclerViewCategory1 = (RecyclerView) view.findViewById(R.id.cardView);
        recyclerViewCategory1.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCategory1.setLayoutManager(MyLayoutManager);
    }

    private void initData() {
        String category0Key = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(categoryInfo.getType());
        for (int i = 0; i < detailTabsActivity.placeService.getCount(); i++) {
            try {
                if (detailTabsActivity.placeService.getItem(i).getValue(Place.class).getCategories().toString().contains(category0Key)) {
                    Place place = detailTabsActivity.placeService.getItem(i).getValue(Place.class);
                    place.setPlaceKey(detailTabsActivity.placeService.getItem(i).getKey());
                    if(isAdminTip(place.getSubcategories())){
                        listTopTip.add(place);
                    } else {
                        listRecentTip.add(place);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listTip.clear();
        listTip.addAll(listRecentTip);

        tipFullScreenAdapter = new TipFullScreenAdapter(listTip, detailTabsActivity.mFCity);
        recyclerViewCategory1.setAdapter(tipFullScreenAdapter);

    }

    public static boolean isAdminTip(ArrayList<String> listSubCategory){
        if(listSubCategory == null || listSubCategory.size() == 0)
            return false;

        for (int i = 0; i < listSubCategory.size(); i++){
            if(listSubCategory.get(i).contains(AppConstants.TipAdmin)){
                return true;
            }
        }

        return false;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}