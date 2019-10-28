/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.model.SubCategory;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressHUD;
import tip.tdc.com.tip.utils.SVprogressHUD.SVProgressInstance;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class FilterActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, FilterActivity.class);
        return in;
    }

    Context mContext;
    TextView tvPrice, tvPriceMin, tvPriceMax, tvSubcategory;
    ListView listviewFilter;
    String[] subCategoryPreSelected;
    FilterItemAdapter filterItemAdapter;
    RangeSeekBar progressbarPrice;

    public static String KEY_MAX_PRICE = "KEY_MAX_PRICE";
    public static String KEY_MIN_PRICE = "KEY_MIN_PRICE";
    public static String KEY_CATEGORY_TYPE = "KEY_CATEGORY_TYPE";
    public static String KEY_LIST_CATEGORY = "KEY_LIST_CATEGORY";
    double selectedPriceMin = -1, selectedPriceMax = 0, selectedPrice = 0;
    int categoryType;
    String listSubCatePrefix;
    String cityKey;
    PlaceService placeService;

    List<Place> placeList = new LinkedList<>();
    List<SubCategory> subCategoryList = new LinkedList<>();
    HashMap<String, SubCategory> subCategoryHashMap = new HashMap<>();
    String categoryKey;
    boolean isPriceFilter = false;
    boolean dataInited = false;
    Handler handler = new Handler();


    SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mContext = this;
        Utils.setStatusBarColor(FilterActivity.this, 0);
        bindView();
        svProgressHUD = SVProgressInstance.showWithStatus(mContext, "Loading...");
        parseIntent();


    }

    @Override
    protected void onPause() {
        super.onPause();
        handler = null;
        if (svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler();
    }

    private void parseIntent() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(KEY_MAX_PRICE)) {
                selectedPriceMax = getIntent().getDoubleExtra(KEY_MAX_PRICE, 0);
                progressbarPrice.setSelectedMaxValue(selectedPriceMax);
            }

            if (getIntent().hasExtra(KEY_MIN_PRICE)) {
                selectedPriceMin = getIntent().getDoubleExtra(KEY_MIN_PRICE, -1);
                progressbarPrice.setSelectedMinValue(selectedPriceMin);
            }

            if (getIntent().hasExtra(KEY_CATEGORY_TYPE)) {
                categoryType = getIntent().getIntExtra(KEY_CATEGORY_TYPE, -1);
                categoryKey = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(categoryType);
                TipApplication.getTipApplication().changeSubCategory(categoryKey);


//                if (categoryType == AppConstants.CATEGORY_EAT_TYPE || categoryType == AppConstants.CATEGORY_SLEEP_TYPE) {
//                    progressbarPrice.setVisibility(View.VISIBLE);
//                    tvPriceMin.setVisibility(View.VISIBLE);
//                    tvPriceMax.setVisibility(View.VISIBLE);
//                    findViewById(R.id.rlPriceTitle).setVisibility(View.VISIBLE);
//                    isPriceFilter = true;
//                } else {
                    progressbarPrice.setVisibility(View.GONE);
                    tvPriceMin.setVisibility(View.GONE);
                    tvPriceMax.setVisibility(View.GONE);
                    findViewById(R.id.rlPriceTitle).setVisibility(View.GONE);
                    isPriceFilter = false;
//                }
            }

            if (getIntent().hasExtra(KEY_LIST_CATEGORY)) {
                listSubCatePrefix = getIntent().getStringExtra(KEY_LIST_CATEGORY);
                subCategoryPreSelected = listSubCatePrefix.split("#");
            }

            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY_KEY)) {
                cityKey = getIntent().getStringExtra(AppConstants.KEY_INTENT_CITY_KEY);

                loadData();

            }

        }
    }

    private void returnResultFilter() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_MAX_PRICE, progressbarPrice.getSelectedMaxValue());
        returnIntent.putExtra(KEY_MIN_PRICE, progressbarPrice.getSelectedMinValue());

        String listSubCategory = "";
        for (int i = 0; i < subCategoryList.size(); i++) {
            if (subCategoryList.get(i).isSelected()) {
                listSubCategory = listSubCategory + "#" + subCategoryList.get(i).getObjectKey();
            }
        }

        if (listSubCategory.length() > 0) {
            listSubCategory = listSubCategory.substring(1);
        }
        returnIntent.putExtra(KEY_LIST_CATEGORY, listSubCategory);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void bindView() {
        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.topbarTitle)).setText("Category");
        FontUtils.setFont(findViewById(R.id.topbarTitle), FontUtils.TYPE_NORMAL);
        FontUtils.setFont(findViewById(R.id.topbarRightBtnReset), FontUtils.TYPE_LIGHT);
        findViewById(R.id.topbarRightBtnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbarPrice.setSelectedMinValue(progressbarPrice.getAbsoluteMinValue());
                progressbarPrice.setSelectedMaxValue(progressbarPrice.getAbsoluteMaxValue());
                for (int i = 0; i < subCategoryList.size(); i++) {
                    subCategoryList.get(i).setSelected(false);
                    subCategoryList.get(i).setSelected(false);
                }

                subCategoryPreSelected = null;
//                dataInited = false;

//                selectedPriceMin = fPriceMin;
//                selectedPriceMax = fPriceMax;

                filterPlace();

                filterItemAdapter.notifyDataSetChanged();
            }
        });

        tvSubcategory = (TextView) findViewById(R.id.tvSubcategory);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvPriceMin = (TextView) findViewById(R.id.tvPriceMin);
        tvPriceMax = (TextView) findViewById(R.id.tvPriceMax);


        progressbarPrice = (RangeSeekBar) findViewById(R.id.progressbarPrice);

        progressbarPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                LoggerFactory.d("onRangeSeekBarValuesChanged : minValue:" + minValue + "/maxValue:" + maxValue);
                filterPlace();
            }
        });


        listviewFilter = (ListView) findViewById(R.id.listviewFilter);
        listviewFilter.setDivider(null);
        listviewFilter.setDividerHeight(0);
        findViewById(R.id.btnApplyFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnResultFilter();
            }
        });

        FontUtils.setFont(tvSubcategory, FontUtils.TYPE_BOLD);
        FontUtils.setFont(findViewById(R.id.topbarRightBtnReset), FontUtils.TYPE_NORMAL);
        FontUtils.setFont(findViewById(R.id.btnApplyFilter), FontUtils.TYPE_NORMAL);
    }

    private void filterSubCategoryByPrice() {

    }

    private void loadData() {
        if (TipApplication.getTipApplication().changeSubCategoryComplete) {
            filterItemAdapter = new FilterItemAdapter(mContext);
            filterItemAdapter.setItems(subCategoryList);
            filterItemAdapter.setSpinnerItemSelectListener(new FilterItemAdapter.SpinnerItemSelectListener() {
                @Override
                public void onItemSelected(int position, boolean selected) {
                    subCategoryList.get(position).setSelected(selected);
                }
            });
            listviewFilter.setAdapter(filterItemAdapter);

            progressbarPrice.setSelectedMinValue(0);


            if (cityKey != null && cityKey.length() > 0) {
                placeService = new PlaceService(cityKey);
                placeService.setOnChangedListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(EventType type, int index, int oldIndex) {

                    }

                    @Override
                    public void onDataChanged() {
                        placeList = placeService.getAllPlacesByCategoryKey(categoryKey);
                        initFilterPlace();
                        filterPlace();
                        filterItemAdapter.notifyDataSetChanged();
                        if (svProgressHUD != null && svProgressHUD.isShowing()) {
                            svProgressHUD.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        } else {
            if (handler == null)
                return;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 1000);
        }
    }

    public boolean isCategorySelected(String subKey) {
        if (subCategoryPreSelected == null || subCategoryPreSelected.length == 0)
            return false;

        for (String subCate : subCategoryPreSelected) {
            if (subCate.length() > 0 && subCate.contains(subKey)) {
                return true;
            }
        }
        return false;
    }

    double fPriceMin = 0, fPriceMax = 0;

    private void initFilterPlace() {
        if (subCategoryHashMap != null && subCategoryHashMap.size() > 0) {
            subCategoryHashMap.clear();
        }

        for (int i = 0; i < placeList.size(); i++) {
            Place place = placeList.get(i);
            if (!place.isDeactived()) {

                // find max and min price
                if (isPriceFilter && !dataInited) {
                    try {
                        if (place.getFromprice() != null && place.getFromprice().length() > 0) {
                            String fromPrice = place.getFromprice().replace("đ", "").replace("$", "").replace("free", "").replace("Free", "").replace(",", "").replace(".", "");
                            if (fromPrice.length() > 0) {
                                double pMin = Double.parseDouble(fromPrice);
                                if (pMin < fPriceMin) {
                                    fPriceMin = pMin;
                                }
                            }
                        }

                        if (place.getToprice() != null && place.getToprice().length() > 0) {
                            String topPrice = place.getToprice().replace("đ", "").replace("$", "").replace("free", "").replace("Free", "").replace(",", "").replace(".", "");
                            if (topPrice.length() > 0) {
                                double pMax = Double.parseDouble(topPrice);
                                if (pMax > fPriceMax) {
                                    fPriceMax = pMax;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // after found min and max price, set to seekbar
        if (isPriceFilter && !dataInited) {
            progressbarPrice.setRangeValues(fPriceMin, fPriceMax);
            Toast.makeText(mContext, "Found max/min = " + fPriceMax +"/"+ fPriceMin, Toast.LENGTH_SHORT).show();

            if (selectedPriceMax <= 0) {
                selectedPriceMax = fPriceMax;

            }
            if (selectedPriceMin == -1) {
                selectedPriceMin = fPriceMin;

            }

            tvPriceMax.setText("đ" + (int)fPriceMax);
            progressbarPrice.setSelectedMaxValue(progressbarPrice.getAbsoluteMaxValue().floatValue());

            tvPriceMin.setText("đ" + (int)fPriceMin);
            progressbarPrice.setSelectedMinValue(selectedPriceMin);

            if (fPriceMax <= 0 && fPriceMax <= 0) {
                findViewById(R.id.rlPriceTitle).setVisibility(View.GONE);
                findViewById(R.id.llSeekBar).setVisibility(View.GONE);
                Toast.makeText(mContext, "Price empty, hide price filter", Toast.LENGTH_LONG).show();
            }
        } else if (isPriceFilter) {
            selectedPriceMax = progressbarPrice.getSelectedMaxValue().doubleValue();
            selectedPriceMin = progressbarPrice.getSelectedMinValue().doubleValue();
        }

        dataInited = true; // do not need update all place price to subcategory object
    }

    private void filterPlace() {
        subCategoryHashMap.clear();

        if (isPriceFilter) {
            selectedPriceMax = progressbarPrice.getSelectedMaxValue().doubleValue();
            selectedPriceMin = progressbarPrice.getSelectedMinValue().doubleValue();
        }

        if (subCategoryHashMap != null && subCategoryHashMap.size() > 0) {
            subCategoryHashMap.clear();
        }

        for (int i = 0; i < placeList.size(); i++) {
            Place place = placeList.get(i);
            if (!place.isDeactived()) {
                // collect all sub category of each place
                if (place.getSubcategories() != null) {
                    for (int j = 0; j < place.getSubcategories().size(); j++) {
                        if (place.getSubcategories() != null && place.getSubcategories().get(j).length() > 0) {
                            boolean isPriceValid = true;

                            // filter by price on
                            if (isPriceFilter) {
                                LoggerFactory.d("Place: " + place.getName() + " price min:" + place.getFromprice() + " price max:" + place.getToprice());
                                // filter : place from price must larger than selected min price
                                try {
                                    if (place.getFromprice() != null && place.getFromprice().length() > 0) {
                                        String fromPrice = place.getFromprice().replace("$", "").replace("vnđ", "");
                                        if (fromPrice.length() > 0) {
                                            double fromPricePlace = Double.parseDouble(fromPrice);

                                            if (fromPricePlace < selectedPriceMin) {
                                                isPriceValid = false;
                                            }

                                            if (fromPricePlace > selectedPriceMax) {
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
                                            if (topPricePlace > selectedPriceMax) {
                                                isPriceValid = false;
                                            }
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (isPriceValid) {
                                SubCategory subCategory = subCategoryHashMap.get(place.getSubcategories().get(j));
                                if (subCategory != null) {
                                    subCategory.setResultCount(subCategory.getResultCount() + 1);
                                    subCategoryHashMap.put(place.getSubcategories().get(j), subCategory);
                                } else {
                                    subCategory = new SubCategory();
                                    subCategory.setObjectKey(place.getSubcategories().get(j));
                                    SubCategory subCategory1 = TipApplication.getTipApplication().subCategoryService.getSubCategoryByKey(place.getSubcategories().get(j));
                                    if (subCategory1 != null && subCategory1.getName() != null && subCategory1.getName().trim().length() > 0) {
                                        subCategory.setName(subCategory1.getName());
                                        subCategory.setResultCount(1);
                                        if (isCategorySelected(place.getSubcategories().get(j))) {
                                            subCategory.setSelected(true);
                                        } else {
                                            subCategory.setSelected(false);
                                        }
                                        subCategoryHashMap.put(place.getSubcategories().get(j), subCategory);
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }

//        // after found min and max price, set to seekbar
//        if (isPriceFilter && !dataInited) {
//            progressbarPrice.setRangeValues(fPriceMin, fPriceMax);
//            Toast.makeText(mContext, "Found max/min = " + fPriceMax +"/"+ fPriceMin, Toast.LENGTH_SHORT).show();
//
//            if (selectedPriceMax <= 0) {
//                selectedPriceMax = fPriceMax;
//
//            }
//            if (selectedPriceMin == -1) {
//                selectedPriceMin = fPriceMin;
//
//            }
//
//            tvPriceMax.setText("đ" + (int)fPriceMax);
//            progressbarPrice.setSelectedMaxValue(progressbarPrice.getAbsoluteMaxValue().floatValue());
//
//            tvPriceMin.setText("đ" + (int)fPriceMin);
//            progressbarPrice.setSelectedMinValue(selectedPriceMin);
//
//            if (fPriceMax <= 0 && fPriceMax <= 0) {
//                findViewById(R.id.rlPriceTitle).setVisibility(View.GONE);
//                findViewById(R.id.llSeekBar).setVisibility(View.GONE);
//                Toast.makeText(mContext, "Price empty, hide price filter", Toast.LENGTH_LONG).show();
//            }
//        } else if (isPriceFilter) {
//            selectedPriceMax = progressbarPrice.getSelectedMaxValue().doubleValue();
//            selectedPriceMin = progressbarPrice.getSelectedMinValue().doubleValue();
//        }

        subCategoryList.clear();
        subCategoryList.addAll(subCategoryHashMap.values());
        filterItemAdapter.notifyDataSetChanged();
    }
}
