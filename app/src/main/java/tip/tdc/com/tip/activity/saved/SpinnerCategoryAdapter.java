/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.saved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.utils.FontUtils;
import com.bpackingapp.vietnam.travel.R;

public class SpinnerCategoryAdapter extends ArrayAdapter<Integer> {

    public interface SpinnerItemSelectListener {
        void onItemSelected(int position);
    }

    SpinnerItemSelectListener spinnerItemSelectListener;

    public void setSpinnerItemSelectListener(SpinnerItemSelectListener spinnerItemSelectListener) {
        this.spinnerItemSelectListener = spinnerItemSelectListener;
    }

    private final Context mContext;
    List<CategoryInfo> items;

    public SpinnerCategoryAdapter(final Context context) {
        mContext = context;
        items = new LinkedList<>();
    }

    public void setItems(List<CategoryInfo>  items) {
        this.items = items;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_spinner, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.textView);
            FontUtils.setFont(viewHolder.textView, FontUtils.TYPE_NORMAL);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(items.get(position).getName());
        FontUtils.setFont(viewHolder.textView, FontUtils.TYPE_BOLD);

        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerItemSelectListener != null){
                    spinnerItemSelectListener.onItemSelected(position);
                }
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    private static class ViewHolder {
        TextView textView;
    }
}