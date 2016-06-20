package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Beans.BlogBean;
import com.example.fanyangsz.oschina.Beans.BlogBeans;
import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/6/1.
 */

public class BlogsAdapter extends BaseAdapter {
    //成员变量
    private BlogBeans.Blogs blogs;
    private Context mContext;

    public BlogsAdapter(BlogBeans.Blogs blogs, Context mContext) {
        this.blogs = blogs;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return blogs.getBlog().size();
    }

    @Override
    public Object getItem(int position) {
        return blogs.getBlog().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView =  View.inflate(mContext, R.layout.list_item_news,null);

            holder.typeIcon = (ImageView) convertView.findViewById(R.id.iv_tip);
            holder.newsTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_description);
            holder.source = (TextView) convertView.findViewById(R.id.tv_source);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.comment = (TextView) convertView.findViewById(R.id.tv_comment_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        if(blogs.getBlog().get(position).getDocumenttype() == BlogBean.DOC_TYPE_ORIGINAL){
            holder.typeIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.widget_original_icon));
        }else {
            holder.typeIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.widget_repaste_icon));
        }
        if(blogs.getBlog().get(position).isHaveRead()){
            holder.newsTitle.setTextColor(mContext.getResources().getColor(R.color.news_content));
        }
        holder.newsTitle.setText(blogs.getBlog().get(position).getTitle());
        holder.content.setText(blogs.getBlog().get(position).getBody());
        holder.source.setText(blogs.getBlog().get(position).getAuthor());
        holder.time.setText(blogs.getBlog().get(position).getPubDate());
        holder.comment.setText(blogs.getBlog().get(position).getCommentCount()+"");
        return convertView;
    }

    static class ViewHolder{
        ImageView typeIcon;
        TextView newsTitle;
        TextView content;
        TextView source;
        TextView time;
        TextView comment;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public BlogBeans.Blogs getDatas() {
        return blogs;
    }
}
