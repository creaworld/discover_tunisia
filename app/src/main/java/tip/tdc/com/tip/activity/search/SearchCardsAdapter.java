package tip.tdc.com.tip.activity.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.fabric.sdk.android.services.common.SafeToast;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.DetailPlaceActivity;
import tip.tdc.com.tip.activity.detail.TipView.TipFragment;
import tip.tdc.com.tip.activity.tip.TipActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.model.SavedItem;
import tip.tdc.com.tip.model.User;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class SearchCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public interface SearchAdapterCallBack{
        void onRemove(int position);
        void onRemovePlace(String placeId);
    }

    SearchAdapterCallBack searchAdapterCallBack;

    public void setSearchAdapterCallBack(SearchAdapterCallBack searchAdapterCallBack) {
        this.searchAdapterCallBack = searchAdapterCallBack;
    }

    private final Context mContext;

    public List<Place> placeList;
    public Place mPlace;
    public FCity mFCity;

    public static final int VIEW_HOLDER_TYPE_TIP = 2;
    public static final int VIEW_HOLDER_TYPE_SEE_DO = 3;

    String categoryTipKey;
    int searchType;

    public SearchCardsAdapter(final Context context, FCity _FCity, int searchType) {
        mContext = context;
        categoryTipKey = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_TIP_TYPE);
        this.mFCity = _FCity;
        this.searchType = searchType;
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
        Collections.sort(this.placeList, new PlaceComparator());
    }

    public static class PlaceComparator implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            String category1 = "";
            String category2 = "";
            if(o2 != null && o2.getCategories() != null && o2.getCategories().size() > 0) {
                category2 = o2.getCategories().get(0);
            }
            if(o1!=null && o1.getCategories() != null && o1.getCategories().size() > 0) {
                category1 = o1.getCategories().get(0);
            }
            return category2.compareTo(category1);
        }
    }

    @Override
    public long getHeaderId(int position) {
        if (position == 0) {
            return -1;
        } else if(placeList.get(position).getCategories() != null && placeList.get(position).getCategories().size() > 0){
            return TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(placeList.get(position).getCategories().get(0));
        } else {
            LoggerFactory.e("<<<<<<<<<<<<<<<<<<<<< getHeaderId : category null: "+placeList.get(position).getName());
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;

        if(placeList.get(position).getCategoryName() != null && placeList.get(position).getCategoryName().length() > 0) {
            textView.setText(placeList.get(position).getCategoryName());
        } else if(placeList.get(position).getCategories() != null && placeList.get(position).getCategories().size() > 0){
            String categoryName = TipApplication.getTipApplication().categoryInfoService.getCategoryNameByKey(String.valueOf(placeList.get(position).getCategories().get(0)));
            if (categoryName == null || categoryName.length() == 0) {
                categoryName = "-+-";
            }

            textView.setText(categoryName);
        } else {
            textView.setText("---");
        }
        holder.itemView.setBackgroundColor(Color.WHITE);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HOLDER_TYPE_TIP:
                return new ViewHolderTip(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_tip_fullscreen_root, parent, false));
            case VIEW_HOLDER_TYPE_SEE_DO:
                return new ViewHolderSeeDo(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_see_do_fullscreen_root, parent, false));
        }

        return new ViewHolderSeeDo(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
       if (holder.getItemViewType() == VIEW_HOLDER_TYPE_TIP) {
            final ViewHolderTip viewHolderTip = (ViewHolderTip) holder;
            viewHolderTip.mItem = placeList.get(position);
            LoggerFactory.d("onBindViewHolder:position" + position + "|mItem:" + viewHolderTip.mItem.toString());

            viewHolderTip.tvComment.setText(viewHolderTip.mItem.getDescription());
            viewHolderTip.tvNumberComment.setText(viewHolderTip.mItem.getCommentCount() + "");
            if (viewHolderTip.mItem.getLoved() != null && viewHolderTip.mItem.getLoved().length() > 0) {
                viewHolderTip.tvNumberLove.setText(Utils.countLove(viewHolderTip.mItem.getLoved()) +"");
            }


            if (TipApplication.getTipApplication().realmDB.getSavedItem(viewHolderTip.mItem.getPlaceKey()) != null) {
                viewHolderTip.imvSaveStatus.setImageResource(R.drawable.icon_favorited);
            } else {
                viewHolderTip.imvSaveStatus.setImageResource(R.drawable.icon_favorite_gray);
            }

            viewHolderTip.imvSaveStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TipApplication.getTipApplication().realmDB.getSavedItem(viewHolderTip.mItem.getPlaceKey()) != null) {
                        Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        TipApplication.getTipApplication().realmDB.removeSavedItem(viewHolderTip.mItem.getPlaceKey());
                        viewHolderTip.imvSaveStatus.setImageResource(R.drawable.icon_favorite_gray);

                        placeList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        SavedItem savedItem = new SavedItem(viewHolderTip.mItem, mFCity.getCityKey());
                        savedItem.setSaveId(viewHolderTip.mItem.getPlaceKey());
                        savedItem.setCategoryType(AppConstants.CATEGORY_TIP_TYPE);
                        TipApplication.getTipApplication().realmDB.updateSavedItem(savedItem);
                        Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                        viewHolderTip.imvSaveStatus.setImageResource(R.drawable.icon_favorited);
                    }
                }
            });

            viewHolderTip.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = TipActivity.createIntent(viewHolderTip.card_view.getContext());
                    intent.putExtra(AppConstants.KEY_INTENT_PLACE_KEY, viewHolderTip.mItem.getPlaceKey());
                    intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, mFCity.getCityKey());
                    intent.putExtra(AppConstants.KEY_INTENT_PLACE, viewHolderTip.mItem);
                    if(mFCity != null) {
                        intent.putExtra(AppConstants.KEY_INTENT_CITY_NAME, mFCity.getName());
                    }

                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
                }
            });

            if (TipFragment.isAdminTip(viewHolderTip.mItem.getSubcategories())) {
                viewHolderTip.imvUserAvatar.setImageResource(R.drawable.ic_man_gray_admin);
                viewHolderTip.tvUserName.setText("Btips");
            } else {
                User user = TipApplication.getTipApplication().userService.getUserByEmail(viewHolderTip.mItem.getEmail());
                if (user != null) {
                    viewHolderTip.tvUserName.setText(user.getName());
                } else {
                    viewHolderTip.tvUserName.setText("---");
                }

                if(user != null && user.getAvatar() != null && user.getAvatar().length() > 0 && user.getAvatar().contains("http")) {
                    ImageLoader.getInstance().displayImage(user.getAvatar(), viewHolderTip.imvUserAvatar, TipApplication.defaultOptionsAvatar);
                } else {
                    viewHolderTip.imvUserAvatar.setImageResource(R.drawable.ic_man_gray);
                }

                if (viewHolderTip.mItem.getPhotos() != null && viewHolderTip.mItem.getPhotos().size() > 0 && viewHolderTip.mItem.getPhotos().get(0) != null && viewHolderTip.mItem.getPhotos().get(0).contains("http")) {
                    ImageLoader.getInstance().displayImage(viewHolderTip.mItem.getPhotos().get(0), viewHolderTip.imvUserAvatar, TipApplication.defaultOptionsAvatar);
                } else {
                    viewHolderTip.imvUserAvatar.setImageResource(R.drawable.ic_man_gray);
                }
            }
        } else if (holder.getItemViewType() == VIEW_HOLDER_TYPE_SEE_DO) {
            final ViewHolderSeeDo viewHolderSeeDo = (ViewHolderSeeDo) holder;
            viewHolderSeeDo.mItem = placeList.get(position);
            LoggerFactory.d("onBindViewHolder:position" + position + "|mItem:" + viewHolderSeeDo.mItem.toString());

           viewHolderSeeDo.categoryType = AppConstants.CATEGORY_SEE_DO_TYPE;
           if(placeList.get(position).getCategories() != null && placeList.get(position).getCategories().size() > 0){
               viewHolderSeeDo.categoryType = TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(placeList.get(position).getCategories().get(0));
               placeList.get(position).setCategoryName(TipApplication.getTipApplication().categoryInfoService.getCategoryNameByType(viewHolderSeeDo.categoryType));
           }


            viewHolderSeeDo.tvCityName.setText(viewHolderSeeDo.mItem.getName());
            viewHolderSeeDo.tvCityDistance.setText(viewHolderSeeDo.mItem.getAddress());
            viewHolderSeeDo.tvNumberComment.setText(viewHolderSeeDo.mItem.getCommentCount() + "");
            if (viewHolderSeeDo.mItem.getLoved() != null && viewHolderSeeDo.mItem.getLoved().length() > 0) {
                viewHolderSeeDo.tvNumberLove.setText(Utils.countLove(viewHolderSeeDo.mItem.getLoved()) +"");
            }

            if (viewHolderSeeDo.mItem.getFromprice() != null && viewHolderSeeDo.mItem.getFromprice().length() > 0) {
                if (viewHolderSeeDo.categoryType == AppConstants.CATEGORY_SLEEP_TYPE) {
                    viewHolderSeeDo.tvCityFee.setText(viewHolderSeeDo.mItem.getFromprice() + "$");
                } else if (viewHolderSeeDo.categoryType == AppConstants.CATEGORY_SEE_DO_TYPE) {
                    viewHolderSeeDo.tvCityFee.setText(viewHolderSeeDo.mItem.getFromprice());
                } else {
                    viewHolderSeeDo.tvCityFee.setText(viewHolderSeeDo.mItem.getFromprice());
                }
            } else {
                viewHolderSeeDo.tvCityFee.setText("Free");
            }
            if (
                    (
                            viewHolderSeeDo.categoryType == AppConstants.CATEGORY_SEE_DO_TYPE
                                    || viewHolderSeeDo.categoryType == AppConstants.CATEGORY_EAT_TYPE
                                    || viewHolderSeeDo.categoryType == AppConstants.CATEGORY_DRINK_TYPE
                                    || viewHolderSeeDo.categoryType == AppConstants.CATEGORY_SHOPPING_TYPE
                    )
                            && viewHolderSeeDo.mItem.getOpeningtime() != null
                            && viewHolderSeeDo.mItem.getOpeningtime().length() > 0
                    ) {
                viewHolderSeeDo.llTimeOpening.setVisibility(View.VISIBLE);
                viewHolderSeeDo.tvTimeOpenning.setText(viewHolderSeeDo.mItem.getOpeningtime());
            } else {
                viewHolderSeeDo.llTimeOpening.setVisibility(View.GONE);
            }

            if (viewHolderSeeDo.categoryType == AppConstants.CATEGORY_TOUR_TYPE) {
                viewHolderSeeDo.llTourInfo.setVisibility(View.VISIBLE);
                if (viewHolderSeeDo.mItem.getTourDuration() != null && viewHolderSeeDo.mItem.getTourDuration().length() > 0) {
                    viewHolderSeeDo.tvTourTime.setVisibility(View.VISIBLE);
                    viewHolderSeeDo.tvTourTime.setText(viewHolderSeeDo.mItem.getTourDuration());
                } else {
                    viewHolderSeeDo.tvTourTime.setVisibility(View.GONE);
                }

                if (viewHolderSeeDo.mItem.getTourGroupSize() != null && viewHolderSeeDo.mItem.getTourGroupSize().length() > 0) {
                    viewHolderSeeDo.tvTourSize.setVisibility(View.VISIBLE);
                    viewHolderSeeDo.tvTourSize.setText(viewHolderSeeDo.mItem.getTourGroupSize());
                } else {
                    viewHolderSeeDo.tvTourSize.setVisibility(View.GONE);
                }
            } else {
                viewHolderSeeDo.llTourInfo.setVisibility(View.GONE);
            }

            if (viewHolderSeeDo.categoryType == AppConstants.CATEGORY_SHOPPING_TYPE
                    || viewHolderSeeDo.categoryType == AppConstants.CATEGORY_DRINK_TYPE
                    || viewHolderSeeDo.categoryType == AppConstants.CATEGORY_EAT_TYPE
                    ) {
                viewHolderSeeDo.tvCityFee.setVisibility(View.GONE);
            } else {
                viewHolderSeeDo.tvCityFee.setVisibility(View.VISIBLE);
            }

            if(viewHolderSeeDo.mItem.getPhotos().get(0).contains("http")) {
                ImageLoader.getInstance().displayImage(viewHolderSeeDo.mItem.getPhotos().get(0), viewHolderSeeDo.imvCityThumb, TipApplication.defaultOptions);
            } else {
                viewHolderSeeDo.imvCityThumb.setImageResource(R.drawable.img_default);
            }

            viewHolderSeeDo.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DetailPlaceActivity.createIntent(mContext);
                    intent.putExtra(AppConstants.KEY_INTENT_PLACE, viewHolderSeeDo.mItem);
                    intent.putExtra(AppConstants.KEY_INTENT_CATE_TYPE, viewHolderSeeDo.categoryType);
                    intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, mFCity.getCityKey());
                    viewHolderSeeDo.imvCityThumb.getContext().startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
                }
            });

            if (TipApplication.getTipApplication().realmDB.getSavedItem(viewHolderSeeDo.mItem.getPlaceKey()) != null) {
                viewHolderSeeDo.imvSaveStatus.setImageResource(R.drawable.icon_favorited);
            } else {
                viewHolderSeeDo.imvSaveStatus.setImageResource(R.drawable.icon_favorite);
            }
            viewHolderSeeDo.imvSaveStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TipApplication.getTipApplication().realmDB.getSavedItem(viewHolderSeeDo.mItem.getPlaceKey()) != null) {
                        Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                        TipApplication.getTipApplication().realmDB.removeSavedItem(viewHolderSeeDo.mItem.getPlaceKey());
                        viewHolderSeeDo.imvSaveStatus.setImageResource(R.drawable.icon_favorite);

                        placeList.remove(position);
                        notifyDataSetChanged();

                        if (searchAdapterCallBack != null){
//                            searchAdapterCallBack.onRemove(position);
                            searchAdapterCallBack.onRemovePlace(viewHolderSeeDo.mItem.getPlaceKey());
                        }
                    } else {
                        SavedItem savedItem = new SavedItem(viewHolderSeeDo.mItem, mFCity.getCityKey());
                        savedItem.setSaveId(viewHolderSeeDo.mItem.getPlaceKey());
                        savedItem.setCategoryType(viewHolderSeeDo.categoryType);
                        TipApplication.getTipApplication().realmDB.updateSavedItem(savedItem);
                        Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                        viewHolderSeeDo.imvSaveStatus.setImageResource(R.drawable.icon_favorited);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (placeList.get(position).getCategories() != null && placeList.get(position).getCategories().size() > 0 && placeList.get(position).getCategories().toString().contains(categoryTipKey)) {
            return VIEW_HOLDER_TYPE_TIP;
        } else {
            return VIEW_HOLDER_TYPE_SEE_DO;
        }
    }

    @Override
    public int getItemCount() {
        if (placeList != null)
            return placeList.size();
        return 0;
    }

    public class ViewHolderTip extends RecyclerView.ViewHolder {
        public Place mItem;
        CardView card_view;
        public TextView tvUserName;
        public TextView tvCityDescription;
        public TextView tvComment;
        public TextView tvNumberLove;
        public TextView tvNumberComment;
        public ImageView imvUserAvatar;
        public ImageView imvLoveStatus;
        public ImageView imvCommentStatus;
        public ImageView imvSaveStatus;

        public ViewHolderTip(View v) {
            super(v);
            card_view = (CardView) v.findViewById(R.id.card_view);
            tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvCityDescription = (TextView) v.findViewById(R.id.tvCityDescription);
            tvComment = (TextView) v.findViewById(R.id.tvComment);
            tvNumberLove = (TextView) v.findViewById(R.id.tvNumberLove);
            tvNumberComment = (TextView) v.findViewById(R.id.tvNumberComment);
            imvUserAvatar = (ImageView) v.findViewById(R.id.imvUserAvatar);
            imvLoveStatus = (ImageView) v.findViewById(R.id.imvLoveStatus);
            imvCommentStatus = (ImageView) v.findViewById(R.id.imvCommentStatus);
            imvSaveStatus = (ImageView) v.findViewById(R.id.imvSaveStatus);

            FontUtils.setFont(tvUserName, FontUtils.TYPE_NORMAL);
            FontUtils.setFont(tvCityDescription, FontUtils.TYPE_LIGHT);
            FontUtils.setFont(tvComment, FontUtils.TYPE_LIGHT);
            FontUtils.setFont(tvNumberLove, FontUtils.TYPE_LIGHT);
            FontUtils.setFont(tvNumberComment, FontUtils.TYPE_LIGHT);
        }
    }

    public class ViewHolderSeeDo extends RecyclerView.ViewHolder {
        public Place mItem;
        public int categoryType;
        public TextView tvCityDistance;
        public TextView tvCityName;
        public TextView tvCityFee;
        public TextView tvNumberLove;
        public TextView tvNumberComment;
        public TextView tvTourTime;
        public TextView tvTourSize;
        public TextView tvTimeOpenning;
        public ImageView imvCityThumb;
        public ImageView imvLoveStatus;
        public ImageView imvCommentStatus;
        public ImageView imvSaveStatus;
        public LinearLayout llTimeOpening;
        public LinearLayout llTourInfo;

        public ViewHolderSeeDo(View v) {
            super(v);
            llTimeOpening = (LinearLayout) v.findViewById(R.id.llTimeOpening);
            llTourInfo = (LinearLayout) v.findViewById(R.id.llTourInfo);


            tvCityDistance = (TextView) v.findViewById(R.id.tvCityDistance);
            FontUtils.setFont(tvCityDistance, FontUtils.TYPE_LIGHT);

            tvCityName = (TextView) v.findViewById(R.id.tvCityName);
            FontUtils.setFont(tvCityName, FontUtils.TYPE_NORMAL);

            tvCityFee = (TextView) v.findViewById(R.id.tvCityFee);
            FontUtils.setFont(tvCityFee, FontUtils.TYPE_LIGHT);

            tvNumberLove = (TextView) v.findViewById(R.id.tvNumberLove);
            FontUtils.setFont(tvNumberLove, FontUtils.TYPE_LIGHT);

            tvNumberComment = (TextView) v.findViewById(R.id.tvNumberComment);
            FontUtils.setFont(tvNumberComment, FontUtils.TYPE_LIGHT);

            tvTourTime = (TextView) v.findViewById(R.id.tvTourTime);
            FontUtils.setFont(tvTourTime, FontUtils.TYPE_LIGHT);

            tvTourSize = (TextView) v.findViewById(R.id.tvTourSize);
            FontUtils.setFont(tvTourSize, FontUtils.TYPE_LIGHT);

            tvTimeOpenning = (TextView) v.findViewById(R.id.tvTimeOpenning);
            FontUtils.setFont(tvTimeOpenning, FontUtils.TYPE_LIGHT);


            imvSaveStatus = (ImageView) v.findViewById(R.id.imvSaveStatus);
            imvCityThumb = (ImageView) v.findViewById(R.id.imvCityThumb);
            imvLoveStatus = (ImageView) v.findViewById(R.id.imvLoveStatus);
            imvCommentStatus = (ImageView) v.findViewById(R.id.imvCommentStatus);
        }
    }
}