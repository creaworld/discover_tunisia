/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.model;

/**
 * Created by phamngocthanh on 8/21/17.
 */

public class FilterItem {
    public String name;
    public int subContent;
    boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubContent() {
        return subContent;
    }

    public void setSubContent(int subContent) {
        this.subContent = subContent;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
