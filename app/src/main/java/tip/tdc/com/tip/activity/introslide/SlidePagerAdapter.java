package tip.tdc.com.tip.activity.introslide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by phamngocthanh on 3/26/15.
 */
public class SlidePagerAdapter extends FragmentStatePagerAdapter {
//    private List<String> picList = new LinkedList<String>();
    private List<Integer> picList = new LinkedList<Integer>();

    public SlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return SlidePageFragment.newInstance(picList.get(i%SlideIntroductionActivity.IMAGES_SLIDER.length));
    }

    @Override
    public int getCount() {
        return SlideIntroductionActivity.MAX_PAGE_SLIDER;
    }

    public void addAll(List<Integer> picList) {
        this.picList = picList;
    }
}
