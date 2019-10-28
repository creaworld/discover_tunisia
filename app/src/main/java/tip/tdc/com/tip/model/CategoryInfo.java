package tip.tdc.com.tip.model;

import java.util.Comparator;

/**
 * Created by phamngocthanh on 7/25/17.
 */

public class CategoryInfo {
    int count;
    String name;
    String priority;
    int type;
    String priceUnit;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    @Override
    public String toString() {
        return "CategoryInfo{" +
                "count=" + count +
                ", name='" + name + '\'' +
                ", priority='" + priority + '\'' +
                ", type=" + type +
                '}';
    }


    public static class ComparatorPriority implements Comparator<CategoryInfo> {
        @Override
        public int compare(CategoryInfo o1, CategoryInfo o2) {
            Long p1 = new Long(0), p2 = new Long(0);
            if (o1.getPriority() != null && o1.getPriority().length() > 0) {
                p1 = Long.valueOf(o1.getPriority());
            }
            if (o2.getPriority() != null && o2.getPriority().length() > 0) {
                p2 = Long.valueOf(o2.getPriority());
            }
            return p2.compareTo(p1);
        }
    }
}
