package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.TweetBean;
import com.example.fanyangsz.oschina.Beans.TweetBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.view.CircleView.CircleImageActivity;
import com.example.fanyangsz.oschina.view.CircleView.UserCenterActivity;
import com.example.fanyangsz.oschina.view.LoginView.LoginFragment;

import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by fanyang.sz on 2016/2/23.
 */
public class TweetAdapter extends BaseAdapter{

    private TweetBeans.TweetList datas;
    private Context mContext;

    public TweetAdapter(TweetBeans.TweetList datas,Context context) {
        this.datas = datas;
        this.mContext = context;
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.list_item_tweet, null);
            holder.author = (TextView) convertView.findViewById(R.id.tv_tweet_name);
            holder.face = (ImageView) convertView.findViewById(R.id.iv_tweet_face);
            holder.content = (TextView) convertView.findViewById(R.id.tweet_item);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_tweet_image);
//            convertView.findViewById(R.id.iv_tweet_image).setVisibility(View.VISIBLE);
            holder.likeUsers = (TextView) convertView.findViewById(R.id.tv_likeusers);
            holder.time = (TextView) convertView.findViewById(R.id.tv_tweet_time);
            holder.platform = (TextView) convertView.findViewById(R.id.tv_tweet_platform);
            holder.del = (TextView) convertView.findViewById(R.id.tv_del);
            holder.IvLikeState = (ImageView) convertView.findViewById(R.id.tv_like_state);
            holder.commentcount = (TextView) convertView.findViewById(R.id.tv_tweet_comment_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        final TweetBean bean = datas.getTweet().get(position);

        holder.author.setText(bean.getAuthor());
        if(!bean.getPortrait().equals(holder.face.getTag())){
            HttpSDK.newInstance().getTweetImage(bean.getPortrait(),holder.face,HttpSDK.IMAGE_TYPE_1);
            holder.face.setTag(bean.getPortrait());
        }
        holder.face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserCenterActivity.class);
                intent.putExtra("hisid",String.valueOf(bean.getAuthorid()));
                intent.putExtra("hisname",bean.getAuthor());
                mContext.startActivity(intent);
            }
        });

        holder.content.setText(bean.getBody());
        if(bean.getImgSmall()!=null&&!bean.getImgSmall().isEmpty()){
            holder.image.setVisibility(View.VISIBLE);
            if(!bean.getImgSmall().equals(holder.image.getTag())){
                HttpSDK.newInstance().getTweetImage(bean.getImgSmall(),holder.image, HttpSDK.IMAGE_TYPE_0);
                holder.image.setTag(bean.getImgSmall());
            }
            final String url = bean.getImgBig();
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new CirclePopupWindow(context, url).showAtLocation(View.inflate(context,R.layout.layout_bottom_bar,null), Gravity.CENTER,0,0);
                    Intent intent = new Intent(mContext, CircleImageActivity.class);
                    intent.putExtra("bigUrl", url);
                    mContext.startActivity(intent);
                }
            });
        }else {
            holder.image.setVisibility(View.GONE);
        }
//        holder.likeUsers.setText(bean.getLikeCount()+"");
        likeUserShow(holder.likeUsers, bean.getLikeCount(), position);
        holder.time.setText(StringUtils.friendlyTime(bean.getPubDate()));
        holder.platform.setText(bean.getAppclient()+"");
        holder.IvLikeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpSDK.newInstance().postAgreeTweet(onAgreeTweetCallBack, bean.getId(),bean.getAuthorid(), LoginFragment.getLoginUser(mContext).getUser().getId());
            }
        });
        holder.commentcount.setText(bean.getCommentCount());
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
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.tab_background));
            builder.setSpan(blueSpan, 0, datas.getTweet().get(position).getLikeUser().get(0).getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        }
    }

    public TweetBeans.TweetList getDatas() {
        return datas;
    }

    HttpSDK.onAgreeTweetCallBack onAgreeTweetCallBack = new HttpSDK.onAgreeTweetCallBack() {
        @Override
        public void onError() {

        }

        @Override
        public void onSuccess() {

        }
    };

    private static class ViewHolder{
        TextView author;
        TextView time;
        TextView content;
        TextView commentcount;
        TextView platform;
        ImageView face;
        ImageView image;
        ImageView IvLikeState;
        TextView del;
        TextView likeUsers;
    }
}
