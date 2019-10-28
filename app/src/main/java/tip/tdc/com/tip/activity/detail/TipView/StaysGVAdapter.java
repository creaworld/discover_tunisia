package tip.tdc.com.tip.activity.detail.TipView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpackingapp.vietnam.travel.R;

import java.util.List;

import tip.tdc.com.tip.activity.detail.OnCatClickListener;
import tip.tdc.com.tip.model.CategoryInfo;

public class StaysGVAdapter extends BaseAdapter {

    private List<CategoryInfo> catsInfo;
    private Context context;
    private OnCatClickListener listener;

    public StaysGVAdapter(Context context, List<CategoryInfo> cats) {
        this.catsInfo = cats;
        this.context = context;
    }

    public void setOnCatClickListener(OnCatClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return catsInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.stays_grid_item, null);

        ImageView img = view.findViewById(R.id.stays_grid_img);
        TextView title = view.findViewById(R.id._stays_grid_title);

        final CategoryInfo info = catsInfo.get(position);

        title.setText(info.getName());
        img.setImageResource(R.drawable.abstract_bg);

        Log.e("buid isssssssssssss", info.getName());

        Log.e("is listener", (listener == null) + "");

        if (listener != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("clicked from", "adapter");
                    listener.onClick(info);
                }
            });

        return view;
    }
}


