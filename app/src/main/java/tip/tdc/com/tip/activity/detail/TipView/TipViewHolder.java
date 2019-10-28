package tip.tdc.com.tip.activity.detail.TipView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tip.tdc.com.tip.utils.FontUtils;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/26/17.
 */

public class TipViewHolder extends RecyclerView.ViewHolder {
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

    public TipViewHolder(View v) {
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