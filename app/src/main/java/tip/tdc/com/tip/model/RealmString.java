/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.model;

import io.realm.RealmObject;

/**
 * Created by phamngocthanh on 8/7/17.
 */

public class RealmString extends RealmObject{
    private String _string;

    public String get_string() {
        return _string;
    }

    public void set_string(String string) {
        this._string = string;
    }


}
