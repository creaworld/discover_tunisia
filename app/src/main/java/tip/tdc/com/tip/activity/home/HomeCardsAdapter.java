package tip.tdc.com.tip.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.nhaarman.listviewanimations.ArrayAdapter;
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

public class HomeCardsAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    List<FCity> cities;

    public HomeCardsAdapter(final Context context) {
        mContext = context;
        cities = new LinkedList<>();
    }

    public void setCities(List<FCity> cities) {
        this.cities = cities;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.card_city_home, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvCityName = (TextView) view.findViewById(R.id.tvCityName);
            viewHolder.tvCityDescription = (TextView) view.findViewById(R.id.tvCityDescription);
            view.setTag(viewHolder);

            viewHolder.imvCityThumb = (ImageView) view.findViewById(R.id.imvCityThumb);
            viewHolder.imvCityUnlockStatus = (ImageView) view.findViewById(R.id.imvCityUnlockStatus);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvCityName.setText(cities.get(position).getName());
        FontUtils.setFont(viewHolder.tvCityName, FontUtils.TYPE_NORMAL);
        viewHolder.tvCityDescription.setText(cities.get(position).getIntro());
        FontUtils.setFont(viewHolder.tvCityDescription, FontUtils.TYPE_NORMAL);
        if(cities.get(position).getPhotourl().contains("http")) {
            ImageLoader.getInstance().displayImage(cities.get(position).getPhotourl(), viewHolder.imvCityThumb, TipApplication.defaultOptions);
        }


        if (AppSetting.getInstant(mContext).isCityUnlocked(cities.get(position).getCityKey())) {
            viewHolder.imvCityUnlockStatus.setImageResource(R.drawable.icon_available_offline);
            viewHolder.imvCityUnlockStatus.setVisibility(View.VISIBLE);
            viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewCity(cities.get(position));
                }
            });
        } else {
//            viewHolder.imvCityUnlockStatus.setVisibility(View.GONE);
            if(AppSetting.getInstant(mContext).isUnlockAllCity()) {
                viewHolder.imvCityUnlockStatus.setVisibility(View.GONE);
                viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewCity(cities.get(position));
                    }
                });
            }
            else if (cities.get(position).getApplePurchaseId() != null && cities.get(position).getApplePurchaseId().length() > 0) {
                viewHolder.imvCityUnlockStatus.setVisibility(View.VISIBLE);
                viewHolder.imvCityUnlockStatus.setImageResource(R.drawable.icon_locked);
                viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = IAPActivity.createIntent(mContext);
                        intent.putExtra(AppConstants.KEY_INTENT_CITY, cities.get(position));
                        mContext.startActivity(intent);
                        ((Activity) mContext).overridePendingTransition(R.anim.svslide_in_bottom, R.anim.svslide_out_top);
                    }
                });
            } else {
                viewHolder.imvCityUnlockStatus.setVisibility(View.GONE);
                viewHolder.imvCityThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewCity(cities.get(position));
                    }
                });
            }
        }


        return view;
    }

    private void viewCity(final FCity fCity){
        Toast.makeText(mContext, "Loading ...", Toast.LENGTH_SHORT).show();


        if (((HomeActivity)mContext).mInterstitialAd.isLoaded()){
            ((HomeActivity)mContext).mInterstitialAd.show();

            ((HomeActivity)mContext).mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(mContext, DetailTabsActivity.class);
                    intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, fCity.getCityKey());
                    intent.putExtra(AppConstants.KEY_INTENT_CITY, fCity);
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
                }
            });

        }
        else {


            Intent intent = new Intent(mContext, DetailTabsActivity.class);
            intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, fCity.getCityKey());
            intent.putExtra(AppConstants.KEY_INTENT_CITY, fCity);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
        }







    }

    @Override
    public int getCount() {
        return cities.size();
    }

    public void addCity(FCity fCity) {
        cities.add(fCity);
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView tvCityName;
        TextView tvCityDescription;
        ImageView imvCityThumb;
        ImageView imvCityUnlockStatus;
    }
}