package com.example.fanyangsz.oschina.view.QuickOptionView;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileOutputStream;

/**
 * Created by fanyang.sz on 2016/2/16.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {
    private SurfaceHolder holder;
    private Camera camera;
    private boolean af;

    public CameraView(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {//Surface生成事件的处理
        try{
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
        }catch (Exception e){

        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {//Surface改变事件的处理
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(width, width);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            camera.autoFocus(null);
            af = true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP && af ==true ){
            camera.takePicture(null,null,this);
            af = false;
        }
        return true;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {
            String path = Environment.getExternalStorageState() + "/test.jpg";
            data2file(data,path);
        }catch(Exception e){
        }
        camera.startPreview();
    }

    private void data2file(byte[] w, String fileName) throws Exception {//将二进制数据转换为文件的函数
        FileOutputStream out =null;
        try {
            out =new FileOutputStream(fileName);
            out.write(w);
            out.close();
        } catch (Exception e) {
            if (out !=null)
                out.close();
            throw e;
        }
    }
}
