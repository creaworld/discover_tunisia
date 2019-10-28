package tip.tdc.com.tip.activity.information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import tip.tdc.com.tip.activity.detail.TransportActivity;
import tip.tdc.com.tip.activity.login.TermOfServiceActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class PrivacyActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, PrivacyActivity.class);
        return in;
    }
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        bindView();
        Utils.setStatusBarColor(PrivacyActivity.this, 0);
    }

    private void bindView(){
        webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl(AppConstants.PRIVACY_URL);
        webview.clearCache(true);
        webview.clearHistory();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.topbarTitle)).setText(R.string.title_activity_privacy);
    }
}
