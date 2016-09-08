package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.CommentBean;
import com.example.fanyangsz.oschina.Beans.CommentBeans;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.view.CircleView.UserCenterActivity;
import com.example.fanyangsz.oschina.widgets.CircleImageView;
import com.example.fanyangsz.oschina.widgets.TweetTextView;

import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by fanyang.sz on 2016/6/23.
 */
public class CommentAdapter extends BaseAdapter {

    private CommentBeans.CommentList datas;
    private Context mContext;

    public CommentAdapter(CommentBeans.CommentList datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return datas.getComments().size();
    }

    @Override
    public Object getItem(int position) {
        return datas.getComments().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.list_item_comment, null);
            holder.faceCircle = (CircleImageView) convertView.findViewById(R.id.iv_avatar);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.contentTv = (TweetTextView) convertView.findViewById(R.id.tv_content);
            holder.fromTv = (TextView) convertView.findViewById(R.id.tv_from);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CommentBean bean = datas.getComments().get(position);
//        bindData(holder, datas.getComments().get(position));
        HttpSDK.newInstance().getTweetImage(bean.getPortrait(),holder.faceCircle,HttpSDK.IMAGE_TYPE_1);
        holder.faceCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserCenterActivity.class);
                intent.putExtra("hisid",String.valueOf(bean.getAuthorId()));
                intent.putExtra("hisname",bean.getAuthor());
                mContext.startActivity(intent);
            }
        });
        holder.nameTv.setText(bean.getAuthor());
        holder.timeTv.setText(StringUtils.friendlyTime(bean.getPubDate()));
        holder.contentTv.setText(bean.getContent());
        holder.fromTv.setText(bean.getAppClient()+"");

        return convertView;
    }

    /*private void bindData(ViewHolder holder, CommentBean bean){
        HttpSDK.newInstance().getTweetImage(bean.getPortrait(),holder.faceCircle,HttpSDK.IMAGE_TYPE_1);
        holder.nameTv.setText(bean.getAuthor());
        holder.timeTv.setText(bean.getPubDate());
        holder.contentTv.setText(bean.getContent());
        holder.fromTv.setText(bean.getAppClient()+"");
    }*/

    private static class ViewHolder{
        CircleImageView faceCircle;
        TextView nameTv;
        TextView timeTv;
        TweetTextView contentTv;
        TextView fromTv;
    }
}
