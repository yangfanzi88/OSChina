package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Beans.NewsBeans;
import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/1/11.
 */
public class NewsAdapter extends BaseAdapter{

    private NewsBeans.NewsList datas;
    private Context context;
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public NewsAdapter(NewsBeans.NewsList datas, Context context) {
        this.datas = datas;
        this.context = context;
    }
    @Override
    public int getCount() {
        return datas.getNews().size();
    }

    @Override
    public Object getItem(int position) {
        return datas.getNews().get(position);
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
        //处理todayIcon的逻辑
//        datas.getNews().get(position).getPubDate();
//        try{
//            SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            long pubTime = today.parse(datas.getNews().get(position).getPubDate()).getTime();
//            if(pubTime < getStartTime() || pubTime > getEndTime())
//                holder.todayIcon.setVisibility(View.GONE);
//        }catch (Exception e){
//        }


        holder.newsTitle.setText(datas.getNews().get(position).getTitle());
        holder.content.setText(datas.getNews().get(position).getBody());
        holder.source.setText(datas.getNews().get(position).getAuthor());
        holder.time.setText(datas.getNews().get(position).getPubDate());
        holder.comment.setText(datas.getNews().get(position).getCommentCount()+"");
        return convertView;
    }
    static class ViewHolder{
        ImageView todayIcon;
        TextView newsTitle;
        TextView content;
        TextView source;
        TextView time;
        TextView comment;
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsBean bean = datas.getNews().get(position);
        String url = bean.getUrl();
        if(TextUtils.isEmpty(url)){
            return;
        }
        Intent intent = new Intent(context,NewsDetialsActivity.class);
        intent.putExtra("urlWebView",url);
        context.startActivity(intent);
    }*/

    public NewsBeans.NewsList getDatas() {
        return datas;
    }
   /* private Long getStartTime(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    private Long getEndTime(){
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }*/
}
