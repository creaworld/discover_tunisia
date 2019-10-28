/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.model.FilterItem;
import tip.tdc.com.tip.model.SubCategory;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import com.bpackingapp.vietnam.travel.R;

public class FilterItemAdapter extends ArrayAdapter<Integer> {

    public interface SpinnerItemSelectListener {
        void onItemSelected(int position, boolean selected);
    }

    SpinnerItemSelectListener spinnerItemSelectListener;

    public void setSpinnerItemSelectListener(SpinnerItemSelectListener spinnerItemSelectListener) {
        this.spinnerItemSelectListener = spinnerItemSelectListener;
    }

    private final Context mContext;
    List<SubCategory> items;

    public FilterItemAdapter(final Context context) {
        mContext = context;
        items = new LinkedList<>();
    }

    public void setItems(List<SubCategory>  items) {
        this.items = items;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_filter, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.textView);
            viewHolder.textViewCount = (TextView) view.findViewById(R.id.textViewCount);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.filterItem = items.get(position);
        viewHolder.textView.setText(items.get(position).getName());
        viewHolder.textViewCount.setText( "(" + items.get(position).getResultCount() + ")");
        viewHolder.checkbox.setChecked(items.get(position).isSelected());
        FontUtils.setFont(viewHolder.textView, FontUtils.TYPE_NORMAL);
        FontUtils.setFont(viewHolder.textViewCount, FontUtils.TYPE_NORMAL);

        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.checkbox.setChecked(!viewHolder.checkbox.isChecked());

                if(spinnerItemSelectListener != null){
                    spinnerItemSelectListener.onItemSelected(position, viewHolder.checkbox.isChecked());
                }
                LoggerFactory.d("SubCategory: " + items.get(position).toString());

            }
        });

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(spinnerItemSelectListener != null){
                    spinnerItemSelectListener.onItemSelected(position, b);
                }

                LoggerFactory.d("SubCategory: " + items.get(position).toString());
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
        TextView textViewCount;
        CheckBox checkbox;
        SubCategory filterItem;
    }
}