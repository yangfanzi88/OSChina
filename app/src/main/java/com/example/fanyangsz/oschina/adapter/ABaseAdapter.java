package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanyang.sz on 2016/1/5.
 */
public abstract class ABaseAdapter<T extends Serializable> extends BaseAdapter {

    private ArrayList<T> datas;
    private Context context;
    private int selectedPosition = -1;

    public ABaseAdapter(ArrayList<T> datas, Context context){
        if(datas == null) datas = new ArrayList<T>();

        this.datas = datas;
        this.context = context;
    }

    abstract protected AbstractItemView<T> newItemView();

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView =  View.inflate(context,R.layout.list_item_news,null);

            holder.todayIcon = (ImageView) convertView.findViewById(R.id.iv_tip);
            holder.newsTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_description);
            holder.source = (TextView) convertView.findViewById(R.id.tv_source);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.comment = (TextView) convertView.findViewById(R.id.tv_comment_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.newsTitle.setText(datas.getNews().get(position).getTitle());
        holder.content.setText(datas.getNews().get(position).getBody());
        holder.source.setText(datas.getNews().get(position).getAuthor());
        holder.time.setText(datas.getNews().get(position).getPubDate());
        holder.comment.setText(datas.getNews().get(position).getCommentCount()+"");
        return convertView;*/
        AbstractItemView<T> itemViewProcessor;
        if (convertView == null) {
            itemViewProcessor = newItemView();

            convertView = View.inflate(context, itemViewProcessor.inflateViewId(), null);
            convertView.setTag(itemViewProcessor);

            itemViewProcessor.bindingView(convertView);
        } else {
            itemViewProcessor = (AbstractItemView<T>) convertView.getTag();
        }

        itemViewProcessor.datas = getDatas();
        itemViewProcessor.position = position;
        itemViewProcessor.size = datas.size();
        itemViewProcessor.bindingData(convertView, datas.get(position));

        convertView.setSelected(selectedPosition == position);

        return convertView;
    }
    public ArrayList<T> getDatas() {
        return datas;
    }

    /*static class ViewHolder{
        ImageView todayIcon;
        TextView newsTitle;
        TextView content;
        TextView source;
        TextView time;
        TextView comment;
    }*/

    public abstract static class AbstractItemView<T extends Serializable>{
        private List<T> datas;
        private int position;
        private int size;

        /**
         * ItemView的layoutId
         *
         * @return
         */
        abstract public int inflateViewId();

        /**
         * 绑定ViewHolder视图
         *
         * @param convertView
         */
        abstract public void bindingView(View convertView);

        /**
         * 绑定ViewHolder数据
         *
         * @param data
         */
        abstract public void bindingData(View convertView, T data);

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<T> getDatas() {
            return datas;
        }

        public void setDatas(List<T> datas) {
            this.datas = datas;
        }

    }

}
