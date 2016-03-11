package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/2/23.
 */
public class TweetAdapter extends BaseAdapter {

    private TweetBeans.TweetList datas;
    private Context context;

    public TweetAdapter(TweetBeans.TweetList datas,Context context) {
        this.datas = datas;
        this.context = context;
    }

    private static class ViewHolder{
        TextView author;
        TextView time;
        TextView content;
        TextView commentcount;
        TextView platform;
        ImageView face;
        ImageView image;
        ImageView tvLikeState;
        TextView del;
        TextView likeUsers;
    }

    @Override
    public int getCount() {
        return datas.getTweet().size();
    }

    @Override
    public Object getItem(int position) {
        return datas.getTweet().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = View.inflate(context, R.layout.list_item_tweet, null);
            holder.author = (TextView) convertView.findViewById(R.id.tv_tweet_name);
            holder.face = (ImageView) convertView.findViewById(R.id.iv_tweet_face);
            holder.content = (TextView) convertView.findViewById(R.id.tweet_item);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_tweet_image);
            holder.likeUsers = (TextView) convertView.findViewById(R.id.tv_likeusers);
            holder.time = (TextView) convertView.findViewById(R.id.tv_tweet_time);
            holder.platform = (TextView) convertView.findViewById(R.id.tv_tweet_platform);
            holder.del = (TextView) convertView.findViewById(R.id.tv_del);
            holder.tvLikeState = (ImageView) convertView.findViewById(R.id.tv_like_state);
            holder.commentcount = (TextView) convertView.findViewById(R.id.tv_tweet_comment_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.author.setText(datas.getTweet().get(position).getAuthor());

        holder.content.setText(datas.getTweet().get(position).getBody());

//        holder.likeUsers.setText(datas.getTweet().get(position).getLikeCount()+"");
        likeUserShow(holder.likeUsers,datas.getTweet().get(position).getLikeCount(), position);
        holder.time.setText(datas.getTweet().get(position).getPubDate()+"");
        holder.platform.setText(datas.getTweet().get(position).getAppclient()+"");

//        holder.tvLikeState.setText(datas.getTweetslist().getTweet().get(position).get);
        holder.commentcount.setText(datas.getTweet().get(position).getCommentCount()+"");
        return convertView;
    }

    private void likeUserShow(TextView textView, int count, int position){
        if(count == 0){
            textView.setVisibility(View.GONE);
        }
        else{
            textView.setVisibility(View.VISIBLE);
            String s = datas.getTweet().get(position).getLikeUser().get(0).getName()
                    + " 等" + count +"人觉得很赞";

            SpannableStringBuilder builder = new SpannableStringBuilder(s);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.tab_background));
            builder.setSpan(blueSpan, 0, datas.getTweet().get(position).getLikeUser().get(0).getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        }
    }

}
