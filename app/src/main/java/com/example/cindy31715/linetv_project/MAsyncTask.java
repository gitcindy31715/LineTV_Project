package com.example.cindy31715.linetv_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cindy31715 on 2020/3/16.
 */

public class MAsyncTask extends AsyncTask {

    ImageView iv;
    public MAsyncTask(ImageView imageView){
        this.iv = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Bitmap doInBackground(Object[] objects) {
        Bitmap bitmap = null;
        try {
            //取的可變引數的第一個元素;是url;
            Log.d("**","String[0] : "+String.valueOf(objects[0]));
            URL url = new URL(String.valueOf(objects[0]));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //設定連結超時時間;
            urlConnection.setConnectTimeout(6000);
            //設定讀取返回資料的超時時間
            urlConnection.setReadTimeout(5000);
            int responseCode = urlConnection.getResponseCode();
            Log.d("**res","responseCode : "+responseCode);
            //響應碼200代表連結和返回資料成功
            if (responseCode == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                Log.d("wzq", "doInBackground: responseCode" + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
//        Bitmap bmp = (Bitmap)o;
//        int width = bmp.getWidth();
//        int height = bmp.getHeight();
//
//        //放大為1.2倍
//        float scaleWidth = (float) 0.3;
//        float scaleHeight = (float) 0.3;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        // 得到新的圖片
//        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
        iv.setImageBitmap((Bitmap)o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

}
