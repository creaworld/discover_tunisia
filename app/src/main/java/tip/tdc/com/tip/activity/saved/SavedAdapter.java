///*
// * Copyright (c) 2017.
// *
// * Created by Pham Ngoc Thanh
// * Contact via Email: ngocthanh2207@gmail.com
// */
//
//package tip.tdc.com.tip.activity.saved;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import java.util.List;
//
//import io.fabric.sdk.android.services.common.SafeToast;
//import tip.tdc.com.tip.TipApplication;
//import tip.tdc.com.tip.activity.detail.DetailPlaceActivity;
//import tip.tdc.com.tip.activity.detail.seedoo.SeeDoViewHolder;
//import tip.tdc.com.tip.data.AppConstants;
//import tip.tdc.com.tip.model.Place;
//import tip.tdc.com.tip.model.SavedItem;
//import tip.tdc.com.tip.utils.view.stickylistheaders.StickyListHeadersAdapter;
//import com.bpackingapp.vietnam.travel.R;
//
//public class SavedAdapter extends BaseAdapter implements StickyListHeadersAdapter {
//    Context mContext;
//    List<SavedItem> mAppList;
//    private boolean show;
//
//    public static final int ID_LIST_BENEFICIARY = 1;
//    public static final int ID_LIST_ACCOUNT_HOLDER = 0;
//
//    private boolean loading;
//    private boolean loaded;
//
//    public boolean isLoading() {
//        return loading;
//    }
//
//    public void setLoading(boolean loading) {
//        this.loading = loading;
//    }
//
//    public boolean isLoaded() {
//        return loaded;
//    }
//
//    public void setLoaded(boolean loaded) {
//        this.loaded = loaded;
//    }
//
//
//    public SavedAdapter(Context context, List<SavedItem> mApps) {
//        this.mContext = context;
//        this.mAppList = mApps;
//        this.show = false;
//    }
//
//    public void setShow(boolean show) {
//        this.show = show;
//    }
//
//
//    // update content to view
//    public View getView(final int position, View convertView, final ViewGroup parent) {
//        if (position < mAppList.size()) {
//            final SavedItem itemData = mAppList.get(position);
//            final int index = position;
//
//            if (convertView == null || !(convertView.getTag() instanceof SeeDoViewHolder)) {
//                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//                convertView = inflater.inflate(R.layout.card_see_do_fullscreen_root, parent, false);
//
//                SeeDoViewHolder holder = new SeeDoViewHolder(convertView);
//                holder.tvCityName.setText(mAppList.get(position).getName());
//                holder.tvCityDistance.setText(mAppList.get(position).getAddress());
//                holder.tvNumberComment.setText(mAppList.get(position).getCommentCount() + "");
//                if (mAppList.get(position).getLoved() != null && mAppList.get(position).getLoved().length() > 0) {
//                    holder.tvNumberLove.setText(mAppList.get(position).getLoved().split("#").length + "");
//                }
//
//                if (mAppList.get(position).getFromprice() != null && mAppList.get(position).getFromprice().length() > 0) {
//                    holder.tvCityFee.setText(mAppList.get(position).getFromprice() + "$");
//                } else {
//                    holder.tvCityFee.setText("Free");
//                }
//                if (
////                        (
////                                categoryType == AppConstants.CATEGORY_SEE_DO_TYPE
////                                        || categoryType == AppConstants.CATEGORY_EAT_TYPE
////                                        || categoryType == AppConstants.CATEGORY_DRINK_TYPE
////                                        || categoryType == AppConstants.CATEGORY_SHOPPING_TYPE
////                        )
////                                &&
//                mAppList.get(position).getOpeningtime() != null
//                                && mAppList.get(position).getOpeningtime().length() > 0
//                        ) {
//                    holder.llTimeOpening.setVisibility(View.VISIBLE);
//                    holder.tvTimeOpenning.setText(mAppList.get(position).getOpeningtime());
//                } else {
//                    holder.llTimeOpening.setVisibility(View.GONE);
//                }
//
////                if (categoryType == AppConstants.CATEGORY_TOUR_TYPE) {
//                    holder.llTourInfo.setVisibility(View.VISIBLE);
//                    if(mAppList.get(position).getTourDuration() != null && mAppList.get(position).getTourDuration().length() > 0) {
//                        holder.tvTourTime.setVisibility(View.VISIBLE);
//                        holder.tvTourTime.setText(mAppList.get(position).getTourDuration());
//                    } else {
//                        holder.tvTourTime.setVisibility(View.GONE);
//                    }
//
//                    if(mAppList.get(position).getTourGroupSize() != null && mAppList.get(position).getTourGroupSize().length() > 0) {
//                        holder.tvTourSize.setVisibility(View.VISIBLE);
//                        holder.tvTourSize.setText(mAppList.get(position).getTourGroupSize());
//                    } else {
//                        holder.tvTourSize.setVisibility(View.GONE);
//                    }
////                } else {
////                    holder.llTourInfo.setVisibility(View.GONE);
////                }
//
////                if (categoryType == AppConstants.CATEGORY_SHOPPING_TYPE
////                        || categoryType == AppConstants.CATEGORY_DRINK_TYPE
////                        || categoryType == AppConstants.CATEGORY_EAT_TYPE
////                        ) {
////                    holder.tvCityFee.setVisibility(View.GONE);
////                } else {
////                    holder.tvCityFee.setVisibility(View.VISIBLE);
////                }
//
//                ImageLoader.getInstance().displayImage(mAppList.get(position).getPhotos().get(0).get_string(), holder.imvCityThumb, TipApplication.defaultOptions);
//                holder.imvCityThumb.setImageResource(R.drawable.img_default);
//                holder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = DetailPlaceActivity.createIntent(mContext);
//                        intent.putExtra(AppConstants.KEY_INTENT_CITY, new Place(mAppList.get(position)));
//                        intent.putExtra(AppConstants.KEY_INTENT_CATE_TYPE, mAppList.get(position).getCategoryType());
//                        mContext.startActivity(intent);
//                        ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
//                    }
//                });
//
//                if(TipApplication.getTipApplication().realmDB.getSavedItem(mAppList.get(position).getSaveId()) != null){
//                    holder.imvSaveStatus.setImageResource(R.drawable.icon_hotel_star);
//                } else {
//                    holder.imvSaveStatus.setImageResource(R.drawable.ic_save);
//                }
//
//                holder.imvSaveStatus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(TipApplication.getTipApplication().realmDB.getSavedItem(mAppList.get(position).getSaveId()) != null){
//                            Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
//                            ((ImageView) view).setImageResource(R.drawable.ic_save);
//                        } else {
//                            TipApplication.getTipApplication().realmDB.updateSavedItem(mAppList.get(position));
//                            Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
//                            ((ImageView) view).setImageResource(R.drawable.icon_hotel_star);
//                        }
//                    }
//                });
////                FontUtils.setFont(holder.tvBank);
////                FontUtils.setFont(holder.tvAccountName);
////                FontUtils.setFont(holder.tvAccountNumber);
//                convertView.setTag(holder);
//            } else {
////                holder = (SavedAdapter.ViewHolder) convertView.getTag();
//            }
//
//            return convertView;
//        } else {
//            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//            convertView = inflater.inflate(R.layout.item_loading, parent, false);
//            if (!loaded && loading) {
//                convertView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
//            } else {
//                convertView.findViewById(R.id.progressBar).setVisibility(View.GONE);
//            }
//            return convertView;
//        }
//    }
//
//
//    public int getCount() {
//        return mAppList.size() + 1;
//    }
//
//    public Object getItem(int position) {
//        return null;
//    }
//
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getHeaderView(int position, View convertView, ViewGroup parent) {
//        if (show && position < mAppList.size()) {
//            ContactHeaderViewHolder holder;
//            if (convertView == null || !(convertView.getTag() instanceof  ContactHeaderViewHolder)) {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_header_category, null);
//                holder = new ContactHeaderViewHolder(convertView);
//                convertView.setTag(holder);
//            } else {
//                holder = (ContactHeaderViewHolder) convertView.getTag();
//            }
//
//
//            if (position < mAppList.size() && mAppList.get(position).getCategories() != null && mAppList.get(position).getCategories().size() > 0
//                    && mAppList.get(position).getCategories().get(0).get_string().length() > 0) {
//                holder.tvHeaderName.setText(getCategoryNameByKey(mAppList.get(position).getCategories().get(0).get_string()));
//            } else {
//                holder.tvHeaderName.setText("header 2");
//            }
//
//
//            return convertView;
//        } else {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_space, null);
//            return convertView;
//        }
//    }
//
//    @Override
//    public long getHeaderId(int position) {
//        if (position < mAppList.size() && mAppList.get(position).getCategories().get(0).get_string().length() == 0) {
//            return ID_LIST_ACCOUNT_HOLDER;
//        } else {
//            return ID_LIST_BENEFICIARY;
//        }
//    }
//
//    public String getCategoryNameByKey(String categoryKey){
//        String categoryName = "";
//        categoryName = TipApplication.getTipApplication().categoryInfoService.getCategoryNameByKey(categoryKey);
//
//        return categoryName;
//    }
//}
