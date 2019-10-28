package tip.tdc.com.tip.activity.detail.placeFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.utils.FontUtils;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/26/17.
 */

public class DescriptionFragment extends Fragment {
    public static final int DESCRIPTION_TYPE_DES = 1;
    public static final int DESCRIPTION_TYPE_FACILITY = 2;
    public static final int DESCRIPTION_TYPE_NOTE = 3;
    int DescriptionType;
    TextView  tvViewAll;
    Place place;

    public DescriptionFragment() {
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setDescriptionType(int descriptionType) {
        DescriptionType = descriptionType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_description, container, false);
        tvViewAll = (TextView) view.findViewById(R.id.tvViewAll);
        FontUtils.setFont(tvViewAll, FontUtils.TYPE_NORMAL);

        initData();
        return view;
    }


    private void initData() {
        if(place!= null) {
            if(DescriptionType == DESCRIPTION_TYPE_DES) {
                if(place.getDescription() != null && place.getDescription().length() > 0) {
                    tvViewAll.setText(place.getDescription());
                }
            } else if(DescriptionType == DESCRIPTION_TYPE_FACILITY) {
                if(place.getFacilities() != null && place.getFacilities().size() > 0) {
                    tvViewAll.setText(place.getDescription());
                }
            }  else if(DescriptionType == DESCRIPTION_TYPE_NOTE) {
                if(place.getThingstonote() != null && place.getThingstonote().length() > 0) {
                    tvViewAll.setText(place.getThingstonote());
                }
            }
        }

        FontUtils.setFont(tvViewAll, FontUtils.TYPE_LIGHT);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}