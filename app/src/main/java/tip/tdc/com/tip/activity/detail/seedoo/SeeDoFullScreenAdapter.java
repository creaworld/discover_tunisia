package tip.tdc.com.tip.activity.detail.seedoo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import io.fabric.sdk.android.services.common.SafeToast;
import io.realm.Realm;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.DetailPlaceActivity;
import tip.tdc.com.tip.activity.introslide.SlideIntroductionActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.database.RealmDB;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.model.SavedItem;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;

import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/26/17.
 */

public class SeeDoFullScreenAdapter extends RecyclerView.Adapter<SeeDoViewHolder> {
    private ArrayList<Place> list;
    private int categoryType;
    FCity mFCity;

    public SeeDoFullScreenAdapter(ArrayList<Place> Data, int cateType, FCity fCity) {
        list = Data;
        this.categoryType = cateType;
        this.mFCity = fCity;
    }

    @Override
    public SeeDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_see_do_fullscreen_root, parent, false);
        SeeDoViewHolder holder = new SeeDoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SeeDoViewHolder holder, final int position) {

        if (list.get(position).isBanner()) {
            holder.imvBanner.setVisibility(View.VISIBLE);
            if (list.get(position).getPhotos().get(0).contains("http")) {
                ImageLoader.getInstance().displayImage(list.get(position).getPhotos().get(0), holder.imvBanner, TipApplication.defaultOptions);
            } else {
                holder.imvBanner.setImageResource(R.drawable.img_default);
            }
            holder.imvBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.openWebsite(holder.imvBanner.getContext(), list.get(position).getWebsite());
                }
            });
        } else {
            holder.imvBanner.setVisibility(View.GONE);
            holder.imvBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        holder.tvCityName.setText(list.get(position).getName());
        FontUtils.setFont(holder.tvCityName, FontUtils.TYPE_NORMAL);
        holder.tvCityDistance.setText(list.get(position).getAddress());
        FontUtils.setFont(holder.tvCityDistance, FontUtils.TYPE_NORMAL);
        holder.tvCityDistance.setVisibility(View.GONE);
        holder.tvNumberComment.setText(list.get(position).getCommentCount() + "");
        FontUtils.setFont(holder.tvNumberComment, FontUtils.TYPE_NORMAL);
        holder.tvNumberLove.setText(Utils.countLove(list.get(position).getLoved()) + "");
        FontUtils.setFont(holder.tvNumberLove, FontUtils.TYPE_NORMAL);

        if (list.get(position).getFromprice() != null && list.get(position).getFromprice().length() > 0) {
            if (categoryType == AppConstants.CATEGORY_SLEEP_TYPE
                    || categoryType == AppConstants.CATEGORY_TOUR_TYPE
                    || categoryType == AppConstants.CATEGORY_PLAY_TYPE) {
                holder.tvCityFee.setText(list.get(position).getFromprice() + "$");
            } else if (categoryType == AppConstants.CATEGORY_SEE_DO_TYPE) {
                holder.tvCityFee.setText(list.get(position).getFromprice());
            } else {
                holder.tvCityFee.setText(list.get(position).getFromprice());
            }

            if (list.get(position).getToprice() != null && list.get(position).getToprice().length() > 0) {
                if (categoryType == AppConstants.CATEGORY_SLEEP_TYPE
                        || categoryType == AppConstants.CATEGORY_TOUR_TYPE
                        || categoryType == AppConstants.CATEGORY_PLAY_TYPE) {
                    holder.tvCityFee.setText(holder.tvCityFee.getText() + " - " + list.get(position).getToprice() + "$");
                } else if (categoryType == AppConstants.CATEGORY_SEE_DO_TYPE) {
                    holder.tvCityFee.setText(holder.tvCityFee.getText() + " - " + list.get(position).getToprice());
                } else {
                    holder.tvCityFee.setText(holder.tvCityFee.getText() + " - " + list.get(position).getToprice());
                }
            }
        } else if (list.get(position).getToprice() != null && list.get(position).getToprice().length() > 0) {
            if (categoryType == AppConstants.CATEGORY_SLEEP_TYPE
                    || categoryType == AppConstants.CATEGORY_TOUR_TYPE
                    || categoryType == AppConstants.CATEGORY_PLAY_TYPE) {
                holder.tvCityFee.setText(list.get(position).getToprice() + "$");
            } else if (categoryType == AppConstants.CATEGORY_SEE_DO_TYPE) {
                holder.tvCityFee.setText(list.get(position).getToprice());
            } else {
                holder.tvCityFee.setText(list.get(position).getToprice());
            }
        } else {
            if (categoryType == AppConstants.CATEGORY_SLEEP_TYPE) {
                holder.tvCityFee.setText("");
            } else if (categoryType == AppConstants.CATEGORY_NIGHT_LIGHT_TYPE) {
                holder.tvCityFee.setText("");
            } else if (categoryType == AppConstants.CATEGORY_PLAY_TYPE) {
                holder.tvCityFee.setText("");
            } else {
                holder.tvCityFee.setText("Free");
            }
        }

        if (categoryType == AppConstants.CATEGORY_SHOPPING_TYPE
                || categoryType == AppConstants.CATEGORY_DRINK_TYPE
                || categoryType == AppConstants.CATEGORY_EAT_TYPE
                ) {
            holder.tvCityFee.setVisibility(View.GONE);
        } else {
            holder.tvCityFee.setVisibility(View.VISIBLE);
        }
        if (
                (categoryType == AppConstants.CATEGORY_SEE_DO_TYPE
                        || categoryType == AppConstants.CATEGORY_EAT_TYPE
                        || categoryType == AppConstants.CATEGORY_DRINK_TYPE
                        || categoryType == AppConstants.CATEGORY_SHOPPING_TYPE
                        || categoryType == AppConstants.CATEGORY_PLAY_TYPE
                        || categoryType == AppConstants.CATEGORY_NIGHT_LIGHT_TYPE
                )
                        && list.get(position).getOpeningtime() != null
                        && list.get(position).getOpeningtime().length() > 0
                ) {
            holder.llTimeOpening.setVisibility(View.VISIBLE);
            holder.tvTimeOpenning.setText(list.get(position).getOpeningtime());
        } else {
            holder.llTimeOpening.setVisibility(View.GONE);
        }

        if (categoryType == AppConstants.CATEGORY_TOUR_TYPE) {
            holder.llTourInfo.setVisibility(View.VISIBLE);
            if (list.get(position).getTourDuration() != null && list.get(position).getTourDuration().length() > 0) {
                holder.tvTourTime.setVisibility(View.VISIBLE);
                holder.tvTourTime.setText(list.get(position).getTourDuration());
            } else {
                holder.tvTourTime.setVisibility(View.GONE);
            }

            if (list.get(position).getTourGroupSize() != null && list.get(position).getTourGroupSize().length() > 0) {
                holder.tvTourSize.setVisibility(View.VISIBLE);
                holder.tvTourSize.setText(list.get(position).getTourGroupSize());
            } else {
                holder.tvTourSize.setVisibility(View.GONE);
            }
        } else {
            holder.llTourInfo.setVisibility(View.GONE);
        }


        if (list.get(position).getPhotos().get(0).contains("http")) {
            ImageLoader.getInstance().displayImage(list.get(position).getPhotos().get(0), holder.imvCityThumb, TipApplication.defaultOptions);
        } else {
            holder.imvCityThumb.setImageResource(R.drawable.img_default);
        }
        holder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailPlaceActivity.createIntent(holder.imvCityThumb.getContext());
                intent.putExtra(AppConstants.KEY_INTENT_PLACE, list.get(position));
                intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, mFCity.getCityKey());
                intent.putExtra(AppConstants.KEY_INTENT_CATE_TYPE, categoryType);
                holder.imvCityThumb.getContext().startActivity(intent);
                ((Activity) holder.imvCityThumb.getContext()).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });




        if (TipApplication.getTipApplication().realmDB.getSavedItem(list.get(position).getPlaceKey()) != null) {
            holder.imvSaveStatus.setImageResource(R.drawable.icon_favorited);
        } else {
            holder.imvSaveStatus.setImageResource(R.drawable.icon_favorite);
        }
        holder.imvSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TipApplication.getTipApplication().realmDB.getSavedItem(list.get(position).getPlaceKey()) != null) {
                    Toast.makeText(holder.imvSaveStatus.getContext(), "Removed", Toast.LENGTH_SHORT).show();
                    TipApplication.getTipApplication().realmDB.removeSavedItem(list.get(position).getPlaceKey());
                    holder.imvSaveStatus.setImageResource(R.drawable.icon_favorite);
                } else {
                    Toast.makeText(holder.imvSaveStatus.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    holder.imvSaveStatus.setImageResource(R.drawable.icon_favorited);

                    SavedItem savedItem = new SavedItem(list.get(position), mFCity.getCityKey());
                    savedItem.setSaveId(list.get(position).getPlaceKey());
                    savedItem.setCategoryType(categoryType);
                    TipApplication.getTipApplication().realmDB.updateSavedItem(savedItem);
                }
            }
        });

        if (AppState.currentFireUser != null && list.get(position).getLoved() != null && list.get(position).getLoved().contains(AppState.currentFireUser.getUid())) {
            holder.imvLoveStatus.setImageResource(R.drawable.icon_loved);
        } else {
            holder.imvLoveStatus.setImageResource(R.drawable.icon_love);
        }

        holder.imvLoveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppState.currentFireUser != null) {
                    final Place place = list.get(position);
                    if (place.getLoved() != null && place.getLoved().contains(AppState.currentFireUser.getUid())) {
                        place.setLoved(place.getLoved().replace("#" + AppState.currentFireUser.getUid(), ""));
                        holder.imvLoveStatus.setImageResource(R.drawable.icon_love);
                    } else {
                        if (place.getLoved() == null) {
                            place.setLoved("");
                        }
                        place.setLoved(place.getLoved() + "#" + AppState.currentFireUser.getUid());
                        holder.imvLoveStatus.setImageResource(R.drawable.icon_loved);
                    }

                    holder.tvNumberLove.setText(Utils.countLove(place.getLoved()) + "");

                    if (mFCity != null) {
                        PlaceService placeService = new PlaceService(mFCity.getCityKey());
                        placeService.updatePlace(place, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    LoggerFactory.d("Update love success");
                                    holder.tvNumberLove.setText(Utils.countLove(place.getLoved()) + "");
                                    if (place.getLoved().contains(AppState.currentFireUser.getUid())) {
                                        holder.imvLoveStatus.setImageResource(R.drawable.icon_loved);
                                    } else {
                                        holder.imvLoveStatus.setImageResource(R.drawable.icon_love);
                                    }
                                } else {
                                    LoggerFactory.d("Update love failure");
                                }
                            }
                        });
                    }
                } else {
                    SlideIntroductionActivity.gotoLoginWindow(view.getContext());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}