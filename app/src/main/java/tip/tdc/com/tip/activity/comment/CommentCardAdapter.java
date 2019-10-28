package tip.tdc.com.tip.activity.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

import com.bpackingapp.vietnam.travel.R;
import tip.tdc.com.tip.model.UserComment;
import tip.tdc.com.tip.utils.DateUtils;

public class CommentCardAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private List<UserComment> userComments;

    public CommentCardAdapter(final Context context) {
        mContext = context;
    }

    public List<UserComment> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<UserComment> userComments) {
        this.userComments = userComments;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_comment_auto, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            viewHolder.tvCommentDate = (TextView) view.findViewById(R.id.tvCommentDate);
            viewHolder.tvComment = (TextView) view.findViewById(R.id.tvComment);
            view.setTag(viewHolder);

            viewHolder.imvUserAvatar = (ImageView) view.findViewById(R.id.imvUserAvatar);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        UserComment userComment = userComments.get(position);

        viewHolder.tvUserName.setText(userComment.getUserName());
        viewHolder.tvCommentDate.setText(DateUtils.convertDateToStringComment(userComment.getcDate()));
        viewHolder.tvComment.setText(userComment.getText());
        viewHolder.imvUserAvatar.setImageResource(R.drawable.ic_man_gray);

        return view;
    }

    @Override
    public int getCount() {
        return userComments.size();
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView tvUserName;
        TextView tvCommentDate;
        TextView tvComment;
        ImageView imvUserAvatar;
    }
}