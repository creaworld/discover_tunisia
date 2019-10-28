/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class TermOfServiceActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, TermOfServiceActivity.class);
        return in;
    }
    WebView webviewBookTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        initView();
        Utils.setStatusBarColor(TermOfServiceActivity.this, 0);
    }

    private void initView(){
        webviewBookTicket = (WebView) findViewById(R.id.webviewBookTicket);
        webviewBookTicket.loadUrl(AppConstants.TERM_URL);
//        webviewBookTicket.setWebChromeClient(new MyCustomChromeClient(this));
//        webviewBookTicket.setWebViewClient(new WebViewClient(this));
//        webviewBookTicket.clearCache(true);
//        webviewBookTicket.clearHistory();
        webviewBookTicket.getSettings().setJavaScriptEnabled(true);
        webviewBookTicket.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((TextView) findViewById(R.id.topbarTitle)).setText("Term of Service");
    }
}
