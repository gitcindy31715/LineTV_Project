package com.example.cindy31715.linetv_project;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    /*-------一般變數------------*/
    final String url = "https://static.linetv.tw/interview/dramas-sample.json";
    private Bitmap bitmap;
    /*-------list------------*/
    private ListView DramaListView;
    private Adapter_DramaList adapter_dramaList=null;
    private ClassDramaInfo dramaInfo;
    private ArrayList<ClassDramaInfo> Dramalist_ = new ArrayList<ClassDramaInfo>();
    private SearchView searchView;
//    private ArrayList<Map<String, Object>> Dramalist = new ArrayList<Map<String, Object>>();
    /*---network--*/
    private ConnectivityManager manager;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        searchView.setIconifiedByDefault(false);// 關閉icon切換
        searchView.setFocusable(false); // 不要進畫面就跳出輸入鍵盤

        /*檢查網路*/
        if (checkNetworkState()) {
            setSearch_function();
            Toast.makeText(this,"提示：下拉可更新",Toast.LENGTH_LONG).show();
            loadJson();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetworkState()) {
                    setSearch_function();
                    loadJson();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });

//        init();
    }

    private void init(){
        DramaListView = (ListView)findViewById(R.id.drama_listview);
        searchView = (SearchView) findViewById(R.id.search_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
    }
    private boolean checkNetworkState() {
        boolean flag = false;
//得到網路連線資訊
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//去進行判斷網路是否連線
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        } else {
//            isNetworkAvailable();
        }
        return flag;
    }

    private void setNetwork(){
        Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("網路提示資訊");
        builder.setMessage("網路不可用，如果繼續，請先設定網路！");
        builder.setPositiveButton("設定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
/**
 * 判斷手機系統的版本！如果API大於10 就是3.0
 * 因為3.0以上的版本的設定和3.0以下的設定不一樣，呼叫的方法不同
 */
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
    }

    private void isNetworkAvailable(){
        NetworkInfo.State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING){
            Toast.makeText(this, "wifi is open! gprs", Toast.LENGTH_SHORT).show();
        }
//判斷為wifi狀態下才載入廣告，如果是GPRS手機網路則不載入！
        if(wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){
            Toast.makeText(this, "wifi is open! wifi", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJson(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://static.linetv.tw/interview/dramas-sample.json");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream is = connection.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    String line = in.readLine();
                    StringBuffer json = new StringBuffer();
                    while (line != null) {
                        json.append(line);
                        line = in.readLine();
                        Log.d("---", "onCreate: "+line);
                    }
                    JSONObject jsonObject_out = new JSONObject(String.valueOf(json));
                    JSONArray jsonArray= jsonObject_out.getJSONArray("data");
                    Log.d("---", "jsonArray.length() = "+jsonArray.length());
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject_in = jsonArray.getJSONObject(i);
                        int drama_id = jsonObject_in.getInt("drama_id");
                        String name = jsonObject_in.getString("name");
                        long total_views = jsonObject_in.getLong("total_views");
                        String created_at = jsonObject_in.getString("created_at");
                        String thumb = jsonObject_in.getString("thumb");
                        double rating = jsonObject_in.getDouble("rating");


                        dramaInfo = new ClassDramaInfo(drama_id,name,total_views,created_at,thumb,rating);
//                        Map<String,Object> map = new HashMap<String,Object>();
//                        map.put("drama_id",drama_id);
//                        map.put("name",name);
//                        map.put("total_views",total_views);
//                        map.put("created_at",created_at);
//                        map.put("thumb",thumb);
//                        map.put("rating",rating);

                        Dramalist_.add(dramaInfo);
//                        Dramalist.add(map);

                        Log.d("---", "drama_id : "+drama_id+" name : "+name);
                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                }catch (Exception x){
                    Log.d("---", "onCreate: "+x);
                }
            }
        }).start();

    }

    private void setSearch_function() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_dramaList.getFilter().filter(newText);

                return true;
            }
        });

    }

    public Handler handler = new Handler() {
//Handler執行在主執行緒中(UI執行緒中)，  它與子執行緒可以通過Message物件來傳遞資料

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter_dramaList=new Adapter_DramaList(Dramalist_,MainActivity.this);
                    DramaListView.setAdapter(adapter_dramaList);
                    DramaListView.setTextFilterEnabled(true);
                    DramaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent it = new Intent(MainActivity.this,DramaDetailActivity.class);
                            it.putExtra("drama_detail",adapter_dramaList.getItem(i));
                            startActivity(it);

                        }
                    });
                    break;
            }


        }
    };

}

