package tip.tdc.com.tip.activity.comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

import tip.tdc.com.tip.activity.detail.TransportActivity;
import tip.tdc.com.tip.activity.login.LoginActivity;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;
import tip.tdc.com.tip.activity.home.HomeCardsAdapter;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.SampleData;

public class CommentActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, CommentActivity.class);
        return in;
    }

    Context mContext;
    ListView listviewComments;
    private CommentCardAdapter commentCardAdapter;

    TextView topbarTitle;

    String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mContext = this;

        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.KEY_ACT_TITLE)) {
                activityTitle = getIntent().getStringExtra(AppConstants.KEY_ACT_TITLE);
            }
        }

        initView();
        loadData();

        Utils.setStatusBarColor(CommentActivity.this, 0);
    }

    private void initView() {
        listviewComments = (ListView) findViewById(R.id.listviewComments);
        listviewComments.setDivider(null);
        listviewComments.setDividerHeight(0);
        topbarTitle = (TextView) findViewById(R.id.topbarTitle);
        findViewById(R.id.topbarLeftBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadData() {
        commentCardAdapter = new CommentCardAdapter(mContext);
        commentCardAdapter.setUserComments(SampleData.getListComment(10));
        AnimationAdapter mAnimAdapter;
        mAnimAdapter = new ScaleInAnimationAdapter(commentCardAdapter);
        mAnimAdapter.setAbsListView(listviewComments);
        listviewComments.setAdapter(mAnimAdapter);

        if (activityTitle != null && activityTitle.length() > 0) {
            topbarTitle.setText(activityTitle);
        } else {
            topbarTitle.setText("Comment");
        }
    }
}
