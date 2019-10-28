package tip.tdc.com.tip.activity.bookticket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import tip.tdc.com.tip.activity.detail.TransportActivity;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class BookTicketActivity extends AppCompatActivity {
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, BookTicketActivity.class);
        return in;
    }
    WebView webviewBookTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        initView();
        Utils.setStatusBarColor(BookTicketActivity.this, 0);
    }

    private void initView(){
        webviewBookTicket = (WebView) findViewById(R.id.webviewBookTicket);
        webviewBookTicket.loadUrl(AppConstants.BOOKING_TICKET_URL);
        webviewBookTicket.clearCache(true);
        webviewBookTicket.clearHistory();
        webviewBookTicket.getSettings().setJavaScriptEnabled(true);
        webviewBookTicket.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((TextView) findViewById(R.id.topbarTitle)).setText("Book ticket");
    }
}
