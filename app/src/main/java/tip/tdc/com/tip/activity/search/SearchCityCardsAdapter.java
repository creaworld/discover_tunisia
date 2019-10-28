/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.DetailTabsActivity;
import tip.tdc.com.tip.activity.iap.IAPActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppSetting;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.utils.FontUtils;
import com.bpackingapp.vietnam.travel.R;

public class SearchCityCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>

{

    private final Context mContext;

    public List<FCity> fCityList = new LinkedList<>();

    public static final int VIEW_HOLDER_TYPE_CITY = 1;


    public SearchCityCardsAdapter(final Context context) {
        mContext = context;
    }

    public void setfCityList(List<FCity> fCityList) {
        if(fCityList != null) {
            this.fCityList = fCityList;
        } else {
            this.fCityList.clear();
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderCity(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_city_home, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == VIEW_HOLDER_TYPE_CITY) {
            final ViewHolderCity viewHolder = (ViewHolderCity) holder;
            viewHolder.mItem = fCityList.get(position);
//            LoggerFactory.d("onBindViewHolder:position" + position + "|mItem:" + viewHolder.mItem.toString());

            viewHolder.tvCityName.setText(viewHolder.mItem.getName());
            viewHolder.tvCityDescription.setText(viewHolder.mItem.getIntro());

            FontUtils.setFont(viewHolder.tvCityName, FontUtils.TYPE_BOLD);
            FontUtils.setFont(viewHolder.tvCityDescription, FontUtils.TYPE_NORMAL);
            if(viewHolder.mItem.getPhotourl().contains("http")) {
                ImageLoader.getInstance().displayImage(viewHolder.mItem.getPhotourl(), viewHolder.imvCityThumb, TipApplication.defaultOptions);
            }

            if (AppSetting.getInstant(mContext).isCityUnlocked(viewHolder.mItem.getCityKey())) {
                viewHolder.imvCityUnlockStatus.setImageResource(R.drawable.icon_available_offline);
                viewHolder.imvCityUnlockStatus.setVisibility(View.VISIBLE);
                viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewCity(viewHolder.mItem);
                    }
                });
            } else {
//            viewHolder.imvCityUnlockStatus.setVisibility(View.GONE);
                if(AppSetting.getInstant(mContext).isUnlockAllCity()) {
                    viewHolder.imvCityUnlockStatus.setVisibility(View.GONE);
                    viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewCity(viewHolder.mItem);
                        }
                    });
                }
                else if (viewHolder.mItem.getApplePurchaseId() != null && viewHolder.mItem.getApplePurchaseId().length() > 0) {
                    viewHolder.imvCityUnlockStatus.setVisibility(View.VISIBLE);
                    viewHolder.imvCityUnlockStatus.setImageResource(R.drawable.icon_locked);
                    viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = IAPActivity.createIntent(mContext);
                            intent.putExtra(AppConstants.KEY_INTENT_CITY, viewHolder.mItem);
                            mContext.startActivity(intent);
                            ((Activity) mContext).overridePendingTransition(R.anim.svslide_in_bottom, R.anim.svslide_out_top);
                        }
                    });
                } else {
                    viewHolder.imvCityUnlockStatus.setVisibility(View.GONE);
                    viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewCity(viewHolder.mItem);
                        }
                    });
                }
            }

//            viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(mContext, DetailTabsActivity.class);
//                    intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, viewHolder.mItem.getCityKey());
//                    intent.putExtra(AppConstants.KEY_INTENT_CITY, viewHolder.mItem);
//                    mContext.startActivity(intent);
//                    ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
//                }
//            });

        }
    }

    private void viewCity(FCity fCity){
        Toast.makeText(mContext, "Loading ...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mContext, DetailTabsActivity.class);
        intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, fCity.getCityKey());
        intent.putExtra(AppConstants.KEY_INTENT_CITY, fCity);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return VIEW_HOLDER_TYPE_CITY;
    }

    @Override
    public int getItemCount() {
        return fCityList.size();
    }

    private class ViewHolderCity extends RecyclerView.ViewHolder {
        public final View mView;
        TextView tvCityName;
        TextView tvCityDescription;
        ImageView imvCityThumb;
        ImageView imvCityUnlockStatus;
        FCity mItem;

        public ViewHolderCity(View view) {
            super(view);
            mView = view;
            tvCityName = (TextView) mView.findViewById(R.id.tvCityName);
            tvCityDescription = (TextView) mView.findViewById(R.id.tvCityDescription);
            imvCityThumb = (ImageView) mView.findViewById(R.id.imvCityThumb);
            imvCityUnlockStatus = (ImageView) mView.findViewById(R.id.imvCityUnlockStatus);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvCityName.getText() + "'";
        }
    }
}