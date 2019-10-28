/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.tip;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import io.fabric.sdk.android.services.common.SafeToast;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.introslide.SlideIntroductionActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.model.SavedItem;
import tip.tdc.com.tip.model.User;
import tip.tdc.com.tip.model.UserComment;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.CommentService;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class TipActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, TipActivity.class);
        return in;
    }

    RecyclerView recyclerViewCategory1;
    ArrayList<UserComment> userCommentArrayList = new ArrayList<>();
    Context mContext;
    String placeKey; // key of object : tip. sleep. travel, city ....
    String cityKey; // key of object : tip. sleep. travel, city .... for update place
    String placeName; // name of place if it is place
    Place place; // maybe is: tip. place
    String categoryKey;

    RelativeLayout rlPlaceInfo;
    ImageView imvUserAvatar, imvLoveStatus, imvSaveStatus;
    ImageView imvSendComment;
    EditText edittextComment;
    TextView tvUserName, tvCityDescription, tvComment, tvNumberLove, tvNumberComment, topbarTitle;
    View viewSpace;

    CommentService commentService;
    PlaceService placeService;
    CommentAdapter commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);
        mContext = this;

        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_PLACE_KEY)) {
                placeKey = getIntent().getStringExtra(AppConstants.KEY_INTENT_PLACE_KEY);
            }
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY_KEY)) {
                cityKey = getIntent().getStringExtra(AppConstants.KEY_INTENT_CITY_KEY);
            }
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY_NAME)) {
                placeName = getIntent().getStringExtra(AppConstants.KEY_INTENT_CITY_NAME);
            }
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_PLACE)) {
                place = (Place) getIntent().getSerializableExtra(AppConstants.KEY_INTENT_PLACE);
                LoggerFactory.d("Show Tip activity for data:"+place.toString());
            }
        }

        bindView();
        loadData();
        Utils.setStatusBarColor(TipActivity.this, 0);
    }

    private void bindView() {
        recyclerViewCategory1 = (RecyclerView) findViewById(R.id.cardView);
        recyclerViewCategory1.setHasFixedSize(true);

        viewSpace = findViewById(R.id.viewSpace);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(mContext);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCategory1.setLayoutManager(MyLayoutManager);

        rlPlaceInfo = (RelativeLayout) findViewById(R.id.rlPlaceInfo);
        imvSaveStatus = (ImageView) findViewById(R.id.imvSaveStatus);
        imvUserAvatar = (ImageView) findViewById(R.id.imvUserAvatar);
        imvLoveStatus = (ImageView) findViewById(R.id.imvLoveStatus);
        imvSendComment = (ImageView) findViewById(R.id.imvSendComment);
        edittextComment = (EditText) findViewById(R.id.edittextComment);


        tvUserName = (TextView) findViewById(R.id.tvUserName);
        FontUtils.setFont(tvUserName, FontUtils.TYPE_NORMAL);
        tvCityDescription = (TextView) findViewById(R.id.tvCityDescription);
        FontUtils.setFont(tvCityDescription, FontUtils.TYPE_NORMAL);

        tvComment = (TextView) findViewById(R.id.tvComment);
        tvNumberLove = (TextView) findViewById(R.id.tvNumberLove);
        tvNumberComment = (TextView) findViewById(R.id.tvNumberComment);

        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        topbarTitle = (TextView) findViewById(R.id.topbarTitle);

        imvSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppState.currentFireUser != null && AppState.currentFireUser.getUid() != null && commentService != null) {
                    if (edittextComment.getText().toString().length() > 0) {
                        commentService.addComment(edittextComment.getText().toString(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(mContext, "Success add comment", Toast.LENGTH_LONG).show();
                                    edittextComment.setText("");

                                    placeService = new PlaceService(cityKey);
                                    place.setCommentCount(place.getCommentCount() + 1);
                                    tvNumberComment.setText(""+place.getCommentCount());
                                    loadData();
                                    placeService.updatePlace(place, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                Toast.makeText(mContext, "Success update place", Toast.LENGTH_LONG).show();
                                                LoggerFactory.d("Success Update Place: " + place.toString());
                                                loadData();
                                            } else {
                                                Toast.makeText(mContext, "Unsuccess update place", Toast.LENGTH_LONG).show();
                                                LoggerFactory.d("failed Update Place: " + place.toString());
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(mContext, "Unsuccess", Toast.LENGTH_LONG).show();
                                    LoggerFactory.d("failed Update Place: " + edittextComment.getText().toString());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(mContext, "Comment empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SlideIntroductionActivity.gotoLoginWindow(mContext);
                }
            }
        });

        imvLoveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceService placeService = new PlaceService(cityKey);
                if(place == null)
                    return;
                if (AppState.currentFireUser != null) {
                    if (place.getLoved() != null && AppState.currentFireUser != null && place.getLoved().contains(AppState.currentFireUser.getUid())) {
                        place.setLoved(place.getLoved().replace("#" + AppState.currentFireUser.getUid(), ""));
                        imvLoveStatus.setImageResource(R.drawable.icon_love);
                    } else {
                        if(place.getLoved() == null){
                            place.setLoved("");
                        }

                        place.setLoved(place.getLoved() + "#" + AppState.currentFireUser.getUid());
                        imvLoveStatus.setImageResource(R.drawable.icon_loved);
                    }
                    final String numberOfLove = Utils.countLove(place.getLoved()) + "";
                    placeService.updatePlace(place, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                LoggerFactory.d("Update love success");
                                tvNumberLove.setText(numberOfLove);
                                imvLoveStatus.setImageResource(R.drawable.icon_loved);
                                if (place.getLoved().contains(AppState.currentFireUser.getUid())) {
                                    imvLoveStatus.setImageResource(R.drawable.icon_loved);
                                } else {
                                    imvLoveStatus.setImageResource(R.drawable.icon_love);
                                }
                            } else {
                                LoggerFactory.d("Update love failure");
                            }
                        }
                    });
                } else {
                    SlideIntroductionActivity.gotoLoginWindow(view.getContext());
                }
            }
        });

    }

    private void loadData() {
        if(place != null) {

            if(TipApplication.getTipApplication().categoryInfoService.getCategoryByKey(place.getCategories().get(0)).getType() == AppConstants.CATEGORY_TIP_TYPE) {
                findViewById(R.id.llTipInfo).setVisibility(View.VISIBLE);
                findViewById(R.id.viewSpace).setVisibility(View.VISIBLE);
                User user = TipApplication.getTipApplication().userService.getUserByEmail(place.getEmail());
                if (user != null) {
                    LoggerFactory.d("=============== " + user.toString());
                    tvUserName.setText(user.getName());
                } else {
                    tvUserName.setText("---");
                }

//            tvCityDescription.setText();
                tvComment.setText(place.getDescription());
                if(place.getLoved() != null && place.getLoved().length() > 0) {
                    tvNumberLove.setText(Utils.countLove(place.getLoved())+"");
                } else {
                    tvNumberLove.setText("0");
                }

                tvNumberComment.setText(place.getCommentCount()+"");

                if(place.getLoved() != null && place.getLoved().length() > 0 && AppState.currentFireUser != null && AppState.currentFireUser.getUid() != null && place.getLoved().contains(AppState.currentFireUser.getUid())) {
                    imvLoveStatus.setImageResource(R.drawable.icon_loved);
                } else {
                    imvLoveStatus.setImageResource(R.drawable.icon_love);
                }

                if(user != null && user.getAvatar() != null && user.getAvatar().length() > 0 && user.getAvatar().contains("http")) {
                    ImageLoader.getInstance().displayImage(user.getAvatar(), imvUserAvatar, TipApplication.defaultOptionsAvatar);
                }

                if(TipApplication.getTipApplication().realmDB.getSavedItem(place.getPlaceKey()) != null){
                    imvSaveStatus.setImageResource(R.drawable.icon_favorited);
                } else {
                    imvSaveStatus.setImageResource(R.drawable.icon_favorite);
                }

                imvSaveStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TipApplication.getTipApplication().realmDB.getSavedItem(place.getPlaceKey()) != null){
                            Toast.makeText(imvSaveStatus.getContext(), "Removed", Toast.LENGTH_SHORT).show();
                            TipApplication.getTipApplication().realmDB.removeSavedItem(place.getPlaceKey());
                            imvSaveStatus.setImageResource(R.drawable.icon_favorite);
                        } else {
                            SavedItem savedItem = new SavedItem(place, cityKey);
                            savedItem.setSaveId(place.getPlaceKey());
                            savedItem.setCategoryType(AppConstants.CATEGORY_TIP_TYPE);
                            TipApplication.getTipApplication().realmDB.updateSavedItem(savedItem);
                            Toast.makeText(imvSaveStatus.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            imvSaveStatus.setImageResource(R.drawable.icon_favorited);
                        }
                    }
                });
                tvCityDescription.setText("---");
                if(place.getUpdatedAt() > 0) {
                    tvCityDescription.setText(Utils.convertDateHistory(place.getUpdatedAt(), mContext));
                }else {
                    if(place.getCreatedAt() > 0) {
                        tvCityDescription.setText(Utils.convertDateHistory(place.getCreatedAt(), mContext));
                    }
                }


            } else {
                findViewById(R.id.llTipInfo).setVisibility(View.GONE);
                findViewById(R.id.viewSpace).setVisibility(View.GONE);
            }
        }

        if (placeKey != null && placeKey.length() > 0) {
            commentService = new CommentService(placeKey);
            commentService.setOnChangedListener(new ChangeEventListener() {
                @Override
                public void onChildChanged(EventType type, int index, int oldIndex) {
                }
                @Override
                public void onDataChanged() {
                    userCommentArrayList.clear();

                    if (commentService.getCount() > 0) {
                        for (int i = 0; i < commentService.getCount(); i++) {
                            UserComment userComment = commentService.getItem(i).getValue(UserComment.class);
                            if(TipApplication.getTipApplication().userService.getUserBySenderId(userComment.getSenderId()) != null) {
                                userComment.setUserName(TipApplication.getTipApplication().userService.getUserBySenderId(userComment.getSenderId()).name);
                            }
                            userCommentArrayList.add(userComment);
                        }
                    }
                    if (commentAdapter != null)
                        commentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
            commentAdapter = new CommentAdapter(userCommentArrayList);
            recyclerViewCategory1.setAdapter(commentAdapter);
        }

        if(placeName != null && placeName.length() > 0){
            topbarTitle.setText(placeName);
        } else if(place != null && place.getName() != null) {
            topbarTitle.setText(place.getName());
        }
    }
}
