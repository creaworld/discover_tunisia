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

import tip.tdc.com.tip.activity.login.TermOfServiceActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class AboutActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, AboutActivity.class);
        return in;
    }
    WebView webviewAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindView();
        Utils.setStatusBarColor(AboutActivity.this, 0);
    }

    private void bindView(){
        webviewAbout = (WebView) findViewById(R.id.webviewAbout);
        webviewAbout.loadUrl(AppConstants.ABOUT_URL);
//        webviewAbout.clearCache(true);
//        webviewAbout.clearHistory();
        webviewAbout.getSettings().setJavaScriptEnabled(true);
        webviewAbout.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webviewAbout.getcl

        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.topbarTitle)).setText(R.string.title_activity_about);
    }
}
