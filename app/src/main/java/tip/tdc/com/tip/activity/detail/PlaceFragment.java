package tip.tdc.com.tip.activity.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.TipView.TipFullScreenAdapter;
import tip.tdc.com.tip.activity.detail.seedoo.SeeDoFullScreenAdapter;
import tip.tdc.com.tip.activity.filter.FilterActivity;
import tip.tdc.com.tip.activity.search.SearchCardsAdapter;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.database.RealmDB;
import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/26/17.
 */

public class PlaceFragment extends Fragment {

    ArrayList<Place> listPlace = new ArrayList<>();
    ArrayList<Place> listPlaceFiltered = new ArrayList<>();
    RecyclerView recyclerViewCategory1;
    DetailTabsActivity detailTabsActivity;
    CategoryInfo categoryInfo;
    FCity mFCity;

    TextView tvCategoryName, tvViewAll;
    ImageView imvFilter;
    NestedScrollView nestedScrollView;

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

        View view = inflater.inflate(R.layout.fragment_detail_place_place_info, container, false);
        setCategory1View(view);

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
//        nestedScrollView.setNestedScrollingEnabled(false);
        nestedScrollView.setSmoothScrollingEnabled(true);

        tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
        tvViewAll = (TextView) view.findViewById(R.id.tvViewAll);
        tvViewAll.setVisibility(View.GONE);
        imvFilter = (ImageView) view.findViewById(R.id.imvFilter);
        imvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FilterActivity.createIntent(getContext());
                if(maxPrice > 0) {
                    intent.putExtra(FilterActivity.KEY_MAX_PRICE, maxPrice);
                }
                if(minPrice < 0) {
                    intent.putExtra(FilterActivity.KEY_MIN_PRICE, minPrice);
                }
                intent.putExtra(FilterActivity.KEY_CATEGORY_TYPE, categoryInfo.getType());
                intent.putExtra(FilterActivity.KEY_LIST_CATEGORY, listSubCategory);
                intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, detailTabsActivity.mFCity.getCityKey());

                startActivityForResult(intent, REQUEST_SET_FILTER);
            }
        });

        view.findViewById(R.id.llBookingVisa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebsite(getContext(), AppConstants.BOOKING_VISA_URL);
            }
        });
        initData();
        return view;
    }

    public void setCategory1View(View view) {
        recyclerViewCategory1 = (RecyclerView) view.findViewById(R.id.cardView);
        recyclerViewCategory1.setHasFixedSize(true);
//        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
//        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerViewCategory1.setLayoutManager(MyLayoutManager);

        CustomLinearLayoutManager MyLayoutManager = new CustomLinearLayoutManager(getActivity());
        MyLayoutManager.setSmoothScrollbarEnabled(false);

        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCategory1.setLayoutManager(MyLayoutManager);

        recyclerViewCategory1.setNestedScrollingEnabled(false);

        recyclerViewCategory1.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                LoggerFactory.d("recyclerViewCategory1 onFling....");
                return false;
            }
        });
        recyclerViewCategory1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

    }

    private void initData() {
        listPlace.clear();
        listPlaceFiltered.clear();

        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        String category0Key = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(categoryInfo.getType());
        for (int i = 0; i < detailTabsActivity.placeService.getCount(); i++) {
            try {
                Place place = detailTabsActivity.placeService.getItem(i).getValue(Place.class);
                if(!place.isDeactived()) {
                    if (place.getCategories() != null && place.getCategories().toString().contains(category0Key)) {
                        place.setPlaceKey(detailTabsActivity.placeService.getItem(i).getKey());
                        listPlace.add(place);
                        listPlaceFiltered.add(place);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(listPlace, new Place.PlaceComparatorPriority());
        Collections.sort(listPlaceFiltered, new Place.PlaceComparatorPriority());



        RealmDB realmDB = TipApplication.getTipApplication().realmDB;

        if (realmDB == null) {
            TipApplication.getTipApplication().realmDB = new RealmDB();
        }


        switch (categoryInfo.getType()) {
            case AppConstants.CATEGORY_TIP_TYPE:
                tipFullScreenAdapter = new TipFullScreenAdapter(listPlaceFiltered, detailTabsActivity.mFCity);
                recyclerViewCategory1.setAdapter(tipFullScreenAdapter);
                break;
            default:
                seeDoFullScreenAdapter = new SeeDoFullScreenAdapter(listPlaceFiltered, categoryInfo.getType(), detailTabsActivity.mFCity);
                recyclerViewCategory1.setAdapter(seeDoFullScreenAdapter);
        }

    }

    TipFullScreenAdapter tipFullScreenAdapter;
    SeeDoFullScreenAdapter seeDoFullScreenAdapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static final int REQUEST_SET_FILTER = 999;
    double maxPrice = 0, minPrice = -1;
    String listSubCategory = "";

    List<String> listSubCategories = new LinkedList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoggerFactory.e(">>>>>>>>>>>>>>>>>>>>onActivityResult:" + requestCode + "|" + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            // have filter config
            if (requestCode == REQUEST_SET_FILTER) {
                if (data.hasExtra(FilterActivity.KEY_MAX_PRICE)) {
                    maxPrice = data.getDoubleExtra(FilterActivity.KEY_MAX_PRICE, -1);
                    LoggerFactory.e(">>>>>>>>>>>>>>>>>>>>onActivityResult:maxPrice" + maxPrice);
                }

                if (data.hasExtra(FilterActivity.KEY_MIN_PRICE)) {
                    minPrice = data.getDoubleExtra(FilterActivity.KEY_MIN_PRICE, -1);
                    LoggerFactory.e(">>>>>>>>>>>>>>>>>>>>onActivityResult:minPrice" + minPrice);
                }

                if (data.hasExtra(FilterActivity.KEY_LIST_CATEGORY)) {
                    listSubCategory = data.getStringExtra(FilterActivity.KEY_LIST_CATEGORY);
                    LoggerFactory.e(">>>>>>>>>>>>>>>>>>>>onActivityResult:listSubCategory:" + listSubCategory);
                    String[] listSubCategoryList = listSubCategory.split("#");
                    listSubCategories.clear();
                    for (String subCate : listSubCategoryList) {
                        if (subCate.length() > 0){
                            listSubCategories.add(subCate);
                        }
                    }

                }

                filterPlaceABySubCate();

                imvFilter.setImageResource(R.drawable.btn_filter);
            }
        }
    }

    public void filterPlaceABySubCate(){
        listPlaceFiltered.clear();
        if(listSubCategories.size() > 0) {
            boolean isPriceValid = true;
            for (int i = 0; i < listPlace.size(); i++) {
                Place place = listPlace.get(i);
                isPriceValid = true;
                if (placeContaintSubCate(place)) {
                    try {
                        if (place.getFromprice() != null && place.getFromprice().length() > 0) {
                            String fromPrice = place.getFromprice().replace("$", "").replace("vnđ", "");
                            if (fromPrice.length() > 0) {
                                double fromPricePlace = Double.parseDouble(fromPrice);
                                if(fromPricePlace < minPrice || fromPricePlace > maxPrice){
                                    isPriceValid = false;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    // filter : place top price must larger than selected max price
                    try {
                        if (place.getToprice() != null && place.getToprice().length() > 0) {
                            String topPrice = place.getToprice().replace("$", "").replace("vnđ", "");
                            if (topPrice.length() > 0) {
                                double topPricePlace = Double.parseDouble(topPrice);
                                if(topPricePlace > maxPrice || topPricePlace < minPrice){
                                    isPriceValid = false;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if(isPriceValid) {
                        listPlaceFiltered.add(place);
                    }
                }
            }
        } else {
            listPlaceFiltered.addAll(listPlace);
        }

        Collections.sort(listPlaceFiltered, new Place.PlaceComparatorPriority());

        if(tipFullScreenAdapter != null) {
            tipFullScreenAdapter.notifyDataSetChanged();
        }else if(seeDoFullScreenAdapter != null) {
            seeDoFullScreenAdapter.notifyDataSetChanged();
        }
    }

    public boolean placeContaintSubCate(Place place) {
        if(place.getSubcategories() != null) {
            for (String subCate: listSubCategories) {
                if (place.getSubcategories().toString().contains(subCate)){
                    LoggerFactory.d("placeContaintSubCate " + subCate + "--" + place.getName());
                    return true;
                }
            }
        }

        return false;
    }
}