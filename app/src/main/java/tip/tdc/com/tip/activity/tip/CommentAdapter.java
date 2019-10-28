/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.tip;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.detail.TipView.TipViewHolder;
import tip.tdc.com.tip.model.User;
import tip.tdc.com.tip.model.UserComment;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 8/08/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<TipViewHolder> {
    private ArrayList<UserComment> list;

    public CommentAdapter(ArrayList<UserComment> Data) {
        list = Data;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tip_simple, parent, false);
        TipViewHolder holder = new TipViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TipViewHolder holder, final int position) {
        LoggerFactory.d("CommentAdapter:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        LoggerFactory.d(list.get(position).toString());
        String userName = "";
        if(list.get(position).getSenderId() != null && list.get(position).getSenderId().length() > 0) {
            User user = TipApplication.getTipApplication().userService.getUserBySenderId(list.get(position).getSenderId());
            if(user != null) {
                userName = TipApplication.getTipApplication().userService.getUserBySenderId(list.get(position).getSenderId()).getName();
                if (user.getAvatar() != null && user.getAvatar().length() > 0 && user.getAvatar().contains("http")) {
                    ImageLoader.getInstance().displayImage(user.getAvatar(), holder.imvUserAvatar, TipApplication.defaultOptionsAvatar);
                }
            } else {
                LoggerFactory.e("Get user by id return null for sender id " + list.get(position).getSenderId() );
                userName = list.get(position).getSenderId();
            }
        } else {
            LoggerFactory.e(">>>>>>>>>>> Sender id null");
            userName = "---";
        }

        holder.tvUserName.setText(userName);
        FontUtils.setFont(holder.tvUserName, FontUtils.TYPE_NORMAL);
        holder.tvComment.setText(list.get(position).getText());
        FontUtils.setFont(holder.tvComment, FontUtils.TYPE_NORMAL);



        if(list.get(position).getUpdatedAt() > 0) {
            holder.tvCityDescription.setText(Utils.convertDateHistory(list.get(position).getUpdatedAt(), holder.tvCityDescription.getContext()));
        }else {
            if (list.get(position).getCreatedAt() > 0) {
                holder.tvCityDescription.setText(Utils.convertDateHistory(list.get(position).getCreatedAt(), holder.tvCityDescription.getContext()));
            } else {
                holder.tvCityDescription.setText("");
            }
        }

        FontUtils.setFont(holder.tvUserName, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(holder.tvCityDescription, FontUtils.TYPE_NORMAL);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}