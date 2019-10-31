package tip.tdc.com.tip.activity.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bpackingapp.vietnam.travel.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.model.Place;

public class GridFragment extends Fragment {

    private List<String> photos = new ArrayList<>();
    private GridView grid;
    private Activity act;
    private GridAdapter adapter;

    public void setPlaces(Activity act, List<Place> places) {
        this.act = act;
        adapter = new GridAdapter(act, photos);
        for (Place place : places){
            if (place.getPhotos() != null || place.getPhotos().size() > 0)
                photos.addAll(place.getPhotos());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        grid = view.findViewById(R.id.images_grid);
        grid.setAdapter(adapter);

        return view;
    }
}

class GridAdapter extends BaseAdapter {

    private List<String> photos;
    private Activity act;

    public GridAdapter(Activity act, List<String> photos) {
        this.photos = photos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return photos.size();
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
        View v = act.getLayoutInflater().inflate(R.layout.img_grid_item, parent, false);

        ImageView img = v.findViewById(R.id.img);

        String url = photos.get(position);

        if (url.contains("http")) {
            ImageLoader.getInstance().displayImage(url, img, TipApplication.defaultOptions);
        }

        return v;
    }
}


