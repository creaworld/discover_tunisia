package tip.tdc.com.tip.model;

import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/25/17.
 */

public class Facility {
    String name;
    int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon(){
        switch (type) {
            case 1:
                return "breakfast";
            case 2:
                return "parking";
            case 3:
                return "citymaps";
            case 4:
                return "Towels";
            case 5:
                return "wifi";
            case 6:
                return "internet";
            case 7:
                return "city_tour";
            case 8:
                return "linen";
            default:
                return "";
        }
    }

    public String iconForType(int mType){
        switch (mType) {
            case 1:
                return "breakfast";
            case 2:
                return "parking";
            case 3:
                return "citymaps";
            case 4:
                return "Towels";
            case 5:
                return "wifi";
            case 6:
                return "internet";
            case 7:
                return "city_tour";
            case 8:
                return "linen";
            default:
                return "";
        }
    }

    public int iconResourceForType(int mType){
        switch (mType) {
            case 1:
                return R.drawable.breakfast;
            case 2:
                return R.drawable.parking;
            case 3:
                return R.drawable.citymaps;
            case 4:
                return R.drawable.towels;
            case 5:
                return R.drawable.wifi;
            case 6:
                return R.drawable.internet;
            case 7:
                return R.drawable.city_tour;
            case 8:
                return R.drawable.linen;
            default:
                return R.drawable.logo2;
        }
    }
}
