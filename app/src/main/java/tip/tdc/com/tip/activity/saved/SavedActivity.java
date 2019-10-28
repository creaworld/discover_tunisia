/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.saved;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.SavedItem;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import tip.tdc.com.tip.utils.view.stickylistheaders.StickyListHeadersListView;
import com.bpackingapp.vietnam.travel.R;

public class SavedActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, SavedActivity.class);
        return in;
    }

    StickyListHeadersListView lvSaved;
    TextView tvFilterKey;
//    SavedAdapter savedAdapter;
    List<SavedItem> savedItems;
    List<SavedItem> savedItemsFiltered = new LinkedList<>();
    Spinner dynamic_spinner;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        mContext = this;

        initView();
        Utils.setStatusBarColor(SavedActivity.this, 0);
    }

    private void initView() {
        tvFilterKey = (TextView) findViewById(R.id.tvFilterKey);
        lvSaved = (StickyListHeadersListView) findViewById(R.id.lvSaved);
        savedItems = TipApplication.getTipApplication().realmDB.getSavedItemList();
//        savedAdapter = new SavedAdapter(mContext, savedItemsFiltered);
//        lvSaved.setAdapter(savedAdapter);

        filterSaved(cate_all);

        ((TextView) findViewById(R.id.topbarTitle)).setText("Saved");
//        findViewById(R.id.topbarLeftBtn).setVisibility(View.GONE);
        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dynamic_spinner = (Spinner) findViewById(R.id.dynamic_spinner);
        final List<String> list = new LinkedList<>();
        list.add(cate_all);
        list.add(sleep);
        list.add(eat);
        list.add(shop);
        list.add(bar_drink);
        list.add(cate_tip);
        list.add(cate_see_do);

        SpinnerCategoryAdapter spinnerCategoryAdapter = new SpinnerCategoryAdapter(mContext);
//        spinnerCategoryAdapter.setItems(list);

        dynamic_spinner.setAdapter(spinnerCategoryAdapter);

        dynamic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LoggerFactory.d("item", (String) parent.getItemAtPosition(position));
                filterSaved(list.get(position));
                tvFilterKey.setText(list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    final String bar_drink = "Bar & Drink";
    final String shop = "Shop";
    final String eat = "Eat";
    final String sleep = "Sleep";
    final String cate_all = "All";
    final String cate_tip = "Tips";
    final String cate_see_do = "See & Do";

    private void filterSaved(String filterType) {
        savedItemsFiltered.clear();
        int cateforyType = AppConstants.CATEGORY_ALL_TYPE;
        if (filterType.equalsIgnoreCase(cate_all)) {
            cateforyType = AppConstants.CATEGORY_ALL_TYPE;
        } else if (filterType.equalsIgnoreCase(sleep)) {
            cateforyType = AppConstants.CATEGORY_SLEEP_TYPE;
        } else if (filterType.equalsIgnoreCase(eat)) {
            cateforyType = AppConstants.CATEGORY_EAT_TYPE;
        } else if (filterType.equalsIgnoreCase(shop)) {
            cateforyType = AppConstants.CATEGORY_SHOPPING_TYPE;
        } else if (filterType.equalsIgnoreCase(bar_drink)) {
            cateforyType = AppConstants.CATEGORY_DRINK_TYPE;
        } else if (filterType.equalsIgnoreCase(cate_tip)) {
            cateforyType = AppConstants.CATEGORY_TIP_TYPE;
        } else if (filterType.equalsIgnoreCase(cate_see_do)) {
            cateforyType = AppConstants.CATEGORY_SEE_DO_TYPE;
        }


        if (savedItems != null && savedItems.size() > 0) {
            for (int i = 0; i < savedItems.size(); i++) {
                if (cateforyType == AppConstants.CATEGORY_ALL_TYPE) {
                    savedItemsFiltered.add(savedItems.get(i));
                } else if (savedItems.get(i).getCategoryType() == cateforyType) {
                    savedItemsFiltered.add(savedItems.get(i));
                }
            }
        }

//        savedAdapter.notifyDataSetChanged();
    }
}
