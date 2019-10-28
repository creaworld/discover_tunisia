/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.visa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import tip.tdc.com.tip.activity.detail.TransportActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class VisaActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, VisaActivity.class);
        return in;
    }
    WebView webviewBookTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        initView();
        Utils.setStatusBarColor(VisaActivity.this, 0);
    }

    private void initView(){
        webviewBookTicket = (WebView) findViewById(R.id.webviewBookTicket);
        webviewBookTicket.loadUrl(AppConstants.BOOKING_VISA_URL);
        webviewBookTicket.getSettings().setUseWideViewPort(true);
        webviewBookTicket.getSettings().setLoadWithOverviewMode(true);

        webviewBookTicket.getSettings().setSupportZoom(true);
        webviewBookTicket.getSettings().setBuiltInZoomControls(true);
        webviewBookTicket.getSettings().setDisplayZoomControls(false);

        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((TextView) findViewById(R.id.topbarTitle)).setText("Visa");
    }
}
