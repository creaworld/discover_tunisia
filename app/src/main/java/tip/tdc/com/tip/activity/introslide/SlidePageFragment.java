package tip.tdc.com.tip.activity.introslide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bpackingapp.vietnam.travel.R;

// use this for load image from server
//import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by phamngocthanh on 3/26/15.
 */
public class SlidePageFragment extends Fragment {
    private static final String PIC_URL = "tintinurl";

    public static SlidePageFragment newInstance(@NonNull final String picUrl) {
        Bundle arguments = new Bundle();
        arguments.putString(PIC_URL, picUrl);

        SlidePageFragment fragment = new SlidePageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    public static SlidePageFragment newInstance(@NonNull final Integer picUrl) {
        Bundle arguments = new Bundle();
        arguments.putInt(PIC_URL, picUrl.intValue());

        SlidePageFragment fragment = new SlidePageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slide_page, container, false);

//        SimpleDraweeView view = (SimpleDraweeView) rootView.findViewById(R.id.pic);
        ImageView view = (ImageView) rootView.findViewById(R.id.pic);

        Bundle arguments = getArguments();
        if (arguments != null) {
//            String url = arguments.getString(PIC_URL);
//            view.setImageURI(Uri.parse(url));
            int imageId = arguments.getInt(PIC_URL);
            (view).setScaleType(ImageView.ScaleType.FIT_XY);
            view.setImageResource(imageId);
        }

        return rootView;
    }
}
