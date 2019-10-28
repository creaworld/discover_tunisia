/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.model;

/**
 * Created by phamngocthanh on 8/11/17.
 */

public class FCityCategory {
    String category_key;
    String order;

    public String getCategory_key() {
        return category_key;
    }

    public void setCategory_key(String category_key) {
        this.category_key = category_key;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "FCityCategory{" +
                "category_key='" + category_key + '\'' +
                ", order=" + order +
                '}';
    }
}
