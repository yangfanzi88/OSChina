package com.example.fanyangsz.oschina.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.Active;
import com.example.fanyangsz.oschina.Beans.UserInformation;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.util.UIHelper;
import com.example.fanyangsz.oschina.Support.util.Utils;
import com.example.fanyangsz.oschina.view.CircleView.CircleImageActivity;
import com.example.fanyangsz.oschina.widgets.CircleImageView;
import com.example.fanyangsz.oschina.widgets.MyLinkMovementMethod;
import com.example.fanyangsz.oschina.widgets.TweetTextView;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.bitmap.BitmapHelper;
import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.StringUtils;

import static com.example.fanyangsz.oschina.widgets.TweetTextView.modifyPath;

/**
 * Created by fanyang.sz on 2016/8/3.
 */

public class ActiveAdapter extends BaseAdapter {

    private Context mContext;
    private UserInformation data;
    private Bitmap recordBitmap;
    private final KJBitmap kjb = new KJBitmap();
    private int rectSize;

    public ActiveAdapter(Context mContext, UserInformation data) {
        this.mContext = mContext;
        this.data = data;
        initImageSize(mContext);
    }
    private void initImageSize(Context cxt) {
        if (cxt != null && rectSize == 0) {
            rectSize = (int) cxt.getResources().getDimension(R.dimen.space_100);
        } else {
            rectSize = 300;
        }
    }

    @Override
    public int getCount() {
        return data.getActiveList().size();
    }

    @Override
    public Object getItem(int position) {
        return data.getActiveList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Active bean = data.getActiveList().get(position);
        //绑定视图
        if(convertView == null){
            holder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.list_item_active, null);
            holder.face = (CircleImageView) convertView.findViewById(R.id.iv_avatar);
            holder.userName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.action = (TextView) convertView.findViewById(R.id.tv_action);
            holder.actionName = (TextView) convertView.findViewById(R.id.tv_action_name);
            holder.body = (TweetTextView) convertView.findViewById(R.id.tv_body);
            holder.lyReply = (LinearLayout) convertView.findViewById(R.id.ly_reply);
            holder.reply = (TweetTextView) convertView.findViewById(R.id.tv_reply);
            holder.picture = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.from = (TextView) convertView.findViewById(R.id.tv_from);
            holder.commentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        if(!bean.getPortrait().equals(holder.face.getTag())){
            HttpSDK.newInstance().getTweetImage(bean.getPortrait(), holder.face, HttpSDK.IMAGE_TYPE_1);
            holder.face.setTag(bean.getPortrait());
        }
        holder.userName.setText(bean.getAuthor());
        holder.time.setText(StringUtils.friendlyTime(bean.getPubDate()));
        holder.action.setText(UIHelper.parseActiveAction(bean.getObjectType(), bean.getObjectCatalog(), bean.getObjectTitle()));
        //绑定body
        if (TextUtils.isEmpty(bean.getMessage())) {
            holder.body.setVisibility(View.GONE);
        } else {
            holder.body.setMovementMethod(MyLinkMovementMethod.a());
            holder.body.setFocusable(false);
            holder.body.setDispatchToParent(true);
            holder.body.setLongClickable(false);

            Spanned span = Html.fromHtml(modifyPath(bean.getMessage()));

            if (!StringUtils.isEmpty(bean.getTweetattach())) {
                if (recordBitmap == null) {
                    initRecordImg(parent.getContext());
                }
                ImageSpan recordImg = new ImageSpan(parent.getContext(),
                        recordBitmap);
                SpannableString str = new SpannableString("c");
                str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.body.setText(str);
               /* span = InputHelper.displayEmoji(parent.getContext()
                        .getResources(), span);
                holder.body.append(span);*/
            } else {
                /*span = InputHelper.displayEmoji(parent.getContext()
                        .getResources(), span);*/
                holder.body.setText(span);
            }
//            MyURLSpan.parseLinkText(holder.body, span);
        }
        //绑定reply
        Active.ObjectReply reply = bean.getObjectReply();
        if (reply != null) {
            holder.reply.setMovementMethod(MyLinkMovementMethod.a());
            holder.reply.setFocusable(false);
            holder.reply.setDispatchToParent(true);
            holder.reply.setLongClickable(false);
            Spanned span = UIHelper.parseActiveReply(reply.objectName,
                    reply.objectBody);
            holder.reply.setText(span);//
//            MyURLSpan.parseLinkText(holder.reply, span);
            holder.lyReply.setVisibility(TextView.VISIBLE);
        } else {
            holder.reply.setText("");
            holder.lyReply.setVisibility(TextView.GONE);
        }
        //绑定图片
        if (!TextUtils.isEmpty(bean.getTweetimage())) {
            setTweetImage(parent, holder, bean);
        } else {
            holder.picture.setVisibility(View.GONE);
            holder.picture.setImageBitmap(null);
        }

        holder.from.setText(String.valueOf(bean.getAppClient()));
        holder.commentCount.setText(String.valueOf(bean.getCommentCount()));

        return convertView;
    }

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        /*recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                Utils.dip2px(cxt, 20), Utils.dip2px(cxt, 20));*/
    }

    /**
     * 动态设置图片显示样式
     *
     * @author kymjs
     */
    private void setTweetImage(final ViewGroup parent, final ViewHolder vh,
                               final Active item) {
        vh.picture.setVisibility(View.VISIBLE);

        /*kjb.display(vh.picture, item.getTweetimage(), R.drawable.pic_bg, rectSize,
                rectSize, new BitmapCallBack() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        super.onSuccess(bitmap);
                        if (bitmap != null) {
                            bitmap = BitmapHelper.scaleWithXY(bitmap, rectSize
                                    / bitmap.getHeight());
                            vh.picture.setImageBitmap(bitmap);
                        }
                    }
                });*/
        HttpSDK.newInstance().getTweetImage(item.getTweetimage(), vh.picture, HttpSDK.IMAGE_TYPE_0);

        vh.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ImagePreviewActivity.showImagePrivew(parent.getContext(), 0,
                        new String[] { getOriginalUrl(item.getTweetimage()) });*/
                Intent intent = new Intent(mContext, CircleImageActivity.class);
                intent.putExtra("bigUrl", item.getTweetimage().replaceAll("_thumb", ""));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    private static class ViewHolder{
        CircleImageView face;
        TextView userName;
        TextView time;
        TextView action;
        TextView actionName;
        TweetTextView body;
        LinearLayout lyReply;
        TweetTextView reply;
        ImageView picture;
        TextView from;
        TextView commentCount;
    }
}
