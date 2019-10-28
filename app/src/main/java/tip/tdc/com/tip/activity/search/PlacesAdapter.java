/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.search;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import tip.tdc.com.tip.model.Place;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class PlacesAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  private ArrayList<Place> items = new ArrayList<Place>();

  public PlacesAdapter() {
    setHasStableIds(true);
  }

  public void add(Place object) {
    items.add(object);
    notifyDataSetChanged();
  }

  public void add(int index, Place object) {
    items.add(index, object);
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends Place> collection) {
    if (collection != null) {
      items.addAll(collection);
      notifyDataSetChanged();
    }
  }

  public void addAll(Place... items) {
    addAll(Arrays.asList(items));
  }

  public void clear() {
    items.clear();
    notifyDataSetChanged();
  }

  public void remove(String object) {
    items.remove(object);
    notifyDataSetChanged();
  }

  public Place getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
