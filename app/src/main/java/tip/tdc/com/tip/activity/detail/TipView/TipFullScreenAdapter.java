package tip.tdc.com.tip.activity.detail.TipView;

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
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.introslide.SlideIntroductionActivity;
import tip.tdc.com.tip.activity.tip.TipActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.model.SavedItem;
import tip.tdc.com.tip.model.User;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/26/17.
 */

public class TipFullScreenAdapter extends RecyclerView.Adapter<TipViewHolder> {
    private ArrayList<Place> list;
    private FCity mCityInfo;
    public TipFullScreenAdapter(ArrayList<Place> Data, FCity cityInfo) {
        list = Data;
        mCityInfo = cityInfo;
    }
    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_tip_fullscreen_root, parent, false);
        TipViewHolder holder = new TipViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final TipViewHolder holder, final int position) {
        holder.tvComment.setText(list.get(position).getDescription());
        FontUtils.setFont(holder.tvComment, FontUtils.TYPE_NORMAL);

        holder.tvNumberComment.setText(list.get(position).getCommentCount()+"");
        FontUtils.setFont(holder.tvNumberComment, FontUtils.TYPE_NORMAL);
        holder.tvNumberLove.setText(Utils.countLove(list.get(position).getLoved()) +"");
        FontUtils.setFont(holder.tvNumberLove, FontUtils.TYPE_NORMAL);

        if(TipApplication.getTipApplication().realmDB.getSavedItem(list.get(position).getPlaceKey()) != null){
            holder.imvSaveStatus.setImageResource(R.drawable.icon_favorited);
        } else {
            holder.imvSaveStatus.setImageResource(R.drawable.icon_favorite_gray);
        }

        holder.imvSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TipApplication.getTipApplication().realmDB.getSavedItem(list.get(position).getPlaceKey()) != null){
                    Toast.makeText(holder.imvSaveStatus.getContext(), "Removed", Toast.LENGTH_SHORT).show();
                    TipApplication.getTipApplication().realmDB.removeSavedItem(list.get(position).getPlaceKey());
                    holder.imvSaveStatus.setImageResource(R.drawable.icon_favorite_gray);
                } else {
                    SavedItem savedItem = new SavedItem(list.get(position), mCityInfo.getCityKey());
                    savedItem.setSaveId(list.get(position).getPlaceKey());
                    savedItem.setCategoryType(AppConstants.CATEGORY_TIP_TYPE);
                    TipApplication.getTipApplication().realmDB.updateSavedItem(savedItem);
                    Toast.makeText(holder.imvSaveStatus.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    holder.imvSaveStatus.setImageResource(R.drawable.icon_favorited);
                }
            }
        });

        if (AppState.currentFireUser != null &&  list.get(position).getLoved() != null &&  list.get(position).getLoved().contains(AppState.currentFireUser.getUid())) {
            holder.imvLoveStatus.setImageResource(R.drawable.icon_loved);
        } else {
            holder.imvLoveStatus.setImageResource(R.drawable.icon_love);
        }

        holder.imvLoveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppState.currentFireUser != null) {
                    final Place place = list.get(position);
                    if (place.getLoved() != null && place.getLoved().contains(AppState.currentFireUser.getUid())) {
                        place.setLoved(place.getLoved().replace("#" + AppState.currentFireUser.getUid(), ""));
                        holder.imvLoveStatus.setImageResource(R.drawable.icon_love);
                    } else {
                        if(place.getLoved() == null){
                            place.setLoved("");
                        }
                        place.setLoved(place.getLoved() + "#" + AppState.currentFireUser.getUid());
                        holder.imvLoveStatus.setImageResource(R.drawable.icon_loved);
                    }
                    holder.tvNumberLove.setText(Utils.countLove(place.getLoved()) +"");

                    if (mCityInfo != null) {
                        PlaceService placeService = new PlaceService(mCityInfo.getCityKey());
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

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TipActivity.createIntent(holder.card_view.getContext());
                intent.putExtra(AppConstants.KEY_INTENT_PLACE_KEY, list.get(position).getPlaceKey());
                intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, mCityInfo.getCityKey());
                intent.putExtra(AppConstants.KEY_INTENT_PLACE, list.get(position));
                if(mCityInfo != null) {
                    intent.putExtra(AppConstants.KEY_INTENT_CITY_NAME, mCityInfo.getName());
                }

                holder.card_view.getContext().startActivity(intent);
                ((Activity) holder.card_view.getContext()).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
            }
        });

        if(TipFragment.isAdminTip(list.get(position).getSubcategories())){
            holder.imvUserAvatar.setImageResource(R.drawable.ic_man_gray_admin);
            holder.tvUserName.setText("Btips");
        } else {
            User user = TipApplication.getTipApplication().userService.getUserByEmail(list.get(position).getEmail());
            if (user != null) {
                holder.tvUserName.setText(user.getName());
            } else {
                holder.tvUserName.setText("---");
            }

            if(user != null && user.getAvatar() != null && user.getAvatar().length() > 0 && user.getAvatar().contains("http")) {
                ImageLoader.getInstance().displayImage(user.getAvatar(), holder.imvUserAvatar, TipApplication.defaultOptionsAvatar);
            } else {
                holder.imvUserAvatar.setImageResource(R.drawable.ic_man_gray);
            }
        }

        if(list.get(position).getUpdatedAt() > 0) {
            holder.tvCityDescription.setText(Utils.convertDateHistory(list.get(position).getUpdatedAt(), holder.tvCityDescription.getContext()));
        }else {
            if (list.get(position).getCreatedAt() > 0) {
                holder.tvCityDescription.setText(Utils.convertDateHistory(list.get(position).getCreatedAt(), holder.tvCityDescription.getContext()));
            } else {
                holder.tvCityDescription.setText("");
            }
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}