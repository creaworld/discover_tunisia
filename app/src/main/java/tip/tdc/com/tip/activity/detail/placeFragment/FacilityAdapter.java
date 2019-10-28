/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.detail.placeFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import tip.tdc.com.tip.model.Facility;
import tip.tdc.com.tip.utils.FontUtils;
import com.bpackingapp.vietnam.travel.R;

public class FacilityAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private List<Facility> facilities;

    public FacilityAdapter(final Context context) {
        mContext = context;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_facility, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvFacility = (TextView) view.findViewById(R.id.tvFacility);
            FontUtils.setFont(viewHolder.tvFacility, FontUtils.TYPE_NORMAL);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvFacility.setText(facilities.get(position).getName());
        return view;
    }



    @Override
    public int getCount() {
        if(facilities == null)
            return 0;
        return facilities.size();
    }

    private static class ViewHolder {
        TextView tvFacility;
    }
}