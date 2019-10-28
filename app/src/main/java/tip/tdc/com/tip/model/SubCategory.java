package tip.tdc.com.tip.model;

import com.google.firebase.database.Exclude;

/**
 * Created by phamngocthanh on 7/25/17.
 */

public class SubCategory {
    // HARD CODE
    static String TipUser = "-Kdjnh1gwHPXYRRTQuIg"; // Dev: "-KdjpEvGoGrCg0_ejxHc"
    static String TipAdmin = "-KdjniTnpvjaBGV3h9KR"; //

    // KEY -----------------------------------------------------
    String name;
    // KEY -----------------------------------------------------

    // variables
    // for filter
    int resultCount = 0;
    boolean selected = false;

    @Exclude
    double minPrice;
    @Exclude
    double maxPrice;

    @Exclude
    String objectKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
