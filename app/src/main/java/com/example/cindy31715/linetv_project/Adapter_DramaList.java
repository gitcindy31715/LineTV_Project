package com.example.cindy31715.linetv_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cindy31715 on 2020/3/11.
 */

public class Adapter_DramaList extends BaseAdapter implements Filterable {
//    ArrayList<Map<String, Object>> Dramalist;//ClientInfo為資料來源的內容格式
    private ArrayList<ClassDramaInfo> Dramalist;//ClientInfo為資料來源的內容格式
    private Context context;
    private Bitmap bitmap;
//    private ArrayList<Map<String, Object>> item;
    private ArrayList<ClassDramaInfo> originalitem;

    public Adapter_DramaList(ArrayList<ClassDramaInfo> Dramalist, Context context) {
        this.Dramalist = Dramalist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Dramalist.size();
    }

    @Override
    public ClassDramaInfo getItem(int i) {
        return Dramalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drama, viewGroup, false);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.created_at = convertView.findViewById(R.id.created_at);
            viewHolder.rating = convertView.findViewById(R.id.rating);
            viewHolder.thumb = convertView.findViewById(R.id.thumb);
            viewHolder.ratingBar = convertView.findViewById(R.id.ratingBar);

            //用setTag將convertView和viewHolder關聯
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //設定控制元件的資料
        viewHolder.name.setText("劇名："+Dramalist.get(position).getName());
        viewHolder.created_at.setText("出品時間："+Dramalist.get(position).getCreated_at().substring(0,10));
        viewHolder.rating.setText("評價："+String.valueOf(Dramalist.get(position).getRating()).substring(0,3));
        viewHolder.ratingBar.setRating((float)Dramalist.get(position).getRating());
        Log.d("***","thumb : "+Dramalist.get(position).getThumb());

        new MAsyncTask(viewHolder.thumb).execute(Dramalist.get(position).getThumb());
//        viewHolder.thumb.setImageBitmap(Dramalist.get(position).get("thumb").);



        return convertView;
    }

    @Override
    public Filter getFilter(){
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString();
                FilterResults result = new FilterResults();
                if(originalitem == null){
                    synchronized (this){
                        originalitem = new ArrayList<ClassDramaInfo>(Dramalist);
                        // 若originalitem 沒有資料，會複製一份item的過來.
                    }
                }
                if(constraint != null && constraint.toString().length()>0){
                    ArrayList<ClassDramaInfo> filteredItem = new ArrayList<ClassDramaInfo>();
                    for(int i=0;i<originalitem.size();i++){
                        String title = originalitem.get(i).getName();
                        if(title.contains(constraint)){
                            filteredItem.add(originalitem.get(i));
                        }
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                }else{
                    synchronized (this){
                        ArrayList<ClassDramaInfo> list = new ArrayList<ClassDramaInfo>(originalitem);
                        result.values = list;
                        result.count = list.size();

                    }
                }

                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d("***","dramalist.size : "+Dramalist.size());
                Dramalist = (ArrayList<ClassDramaInfo>)results.values;
                if(results.count>0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    class ViewHolder {
        TextView drama_id;
        TextView name;
        TextView total_views;
        TextView created_at;
        ImageView thumb;
        TextView rating;
        RatingBar ratingBar;
    }


}
