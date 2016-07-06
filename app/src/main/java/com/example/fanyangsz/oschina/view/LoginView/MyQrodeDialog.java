package com.example.fanyangsz.oschina.view.LoginView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.Support.util.QrCodeUtils;
import com.google.zxing.WriterException;
import org.kymjs.kjframe.utils.FileUtils;

/**
 * Created by fanyang.sz on 2016/6/29.
 */
public class MyQrodeDialog extends Dialog {

    private ImageView mIvCode;
    private Bitmap bitmap;

    private MyQrodeDialog(Context context, boolean flag,
                          OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private MyQrodeDialog(final Context context, int id, int defStyle) {
        super(context, defStyle);
        View contentView = getLayoutInflater().inflate(
                R.layout.dialog_my_qr_code, null);
        mIvCode = (ImageView) contentView.findViewById(R.id.iv_qr_code);
        try {
            bitmap = QrCodeUtils.Create2DCode(String.format(
                    "http://my.oschina.net/u/%s", id));
            mIvCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mIvCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dismiss();
                if (FileUtils.bitmapToFile(bitmap,
                        FileUtils.getSavePath("OSChina") + "/myqrcode.png")) {
                    Toast.makeText(context,"二维码已保存到oschina文件夹下", Toast.LENGTH_LONG).show();
//                    AppContext.showToast("二维码已保存到oschina文件夹下");
                } else {
                    Toast.makeText(context,"SD卡不可写，二维码保存失败", Toast.LENGTH_LONG).show();
//                    AppContext.showToast("SD卡不可写，二维码保存失败");
                }
                return false;
            }
        });

        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MyQrodeDialog.this.dismiss();
                return false;
            }
        });
        super.setContentView(contentView);
    }

    public MyQrodeDialog(Context context, int id) {
        this(context, id, R.style.quick_option_dialog);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(p);
    }
}
