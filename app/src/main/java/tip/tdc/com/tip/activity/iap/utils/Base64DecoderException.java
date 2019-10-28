/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.iap.utils;

/**
 * Exception thrown when encountering an invalid Base64 input character.
 *
 * @author nelson
 */
public class Base64DecoderException extends Exception {
    public Base64DecoderException() {
        super();
    }

    public Base64DecoderException(String s) {
        super(s);
    }

    private static final long serialVersionUID = 1L;
}
