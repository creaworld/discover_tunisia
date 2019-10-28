package tip.tdc.com.tip.activity.detail.seedoo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.fabric.sdk.android.services.common.SafeToast;
import tip.tdc.com.tip.utils.FontUtils;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/26/17.
 */

public class SeeDoViewHolder extends RecyclerView.ViewHolder {

    public static interface SeeDoSelectListener {
        void OnSeeDoPlaceSelected(int position);
    }
    public void setSeeDoSelectListener(SeeDoSelectListener listener) {
        seeDoSelectListener = listener;
    }

    SeeDoSelectListener seeDoSelectListener;

    public TextView tvCityDistance;
    public TextView tvCityName;
    public TextView tvCityFee;
    public TextView tvNumberLove;
    public TextView tvNumberComment;
    public TextView tvTourTime;
    public TextView tvTourSize;
    public TextView tvTimeOpenning;
    public ImageView imvBanner;
    public ImageView imvCityThumb;
    public ImageView imvLoveStatus;
    public ImageView imvCommentStatus;
    public ImageView imvSaveStatus;
    public LinearLayout llTimeOpening;
    public LinearLayout llTourInfo;
    public RelativeLayout rlPlaceInfo;

    public SeeDoViewHolder(View v) {
        super(v);
        llTimeOpening = (LinearLayout) v.findViewById(R.id.llTimeOpening);
        llTourInfo = (LinearLayout) v.findViewById(R.id.llTourInfo);
        rlPlaceInfo = (RelativeLayout) v.findViewById(R.id.rlPlaceInfo);


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


        imvBanner = (ImageView) v.findViewById(R.id.imvBanner);
        imvSaveStatus = (ImageView) v.findViewById(R.id.imvSaveStatus);
        imvCityThumb = (ImageView) v.findViewById(R.id.imvCityThumb);
        imvLoveStatus = (ImageView) v.findViewById(R.id.imvLoveStatus);
        imvCommentStatus = (ImageView) v.findViewById(R.id.imvCommentStatus);
        imvLoveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int id = (int)likeImageView.getTag();
//                if( id == R.drawable.ic_like){
//
//                    likeImageView.setTag(R.drawable.ic_liked);
//                    likeImageView.setImageResource(R.drawable.ic_liked);
//
//                    Toast.makeText(getActivity(),titleTextView.getText()+"
//                            added to favourites",Toast.LENGTH_SHORT).show();
//
//                }else{
//                    likeImageView.setTag(R.drawable.ic_like);
//                    likeImageView.setImageResource(R.drawable.ic_like);
//                    Toast.makeText(getActivity(),titleTextView.getText()+"
//                            removed from favourites",Toast.LENGTH_SHORT).show();
//                }
            }
        });

        imvCityThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(imvCityThumb.getContext(), "selected", Toast.LENGTH_LONG).show();
//                if (seeDoSelectListener != null) {
//                    seeDoSelectListener.OnSeeDoPlaceSelected(mPosition);
//                }
            }
        });
    }
}