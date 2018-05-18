package com.xinspace.csevent.baskorder.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.xinspace.csevent.R;
import com.xinspace.csevent.baskorder.view.ScaleTopHeaderView;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumAdapter2 extends BaseAdapter {
    private Context context;
    private List<String> imgs;
    private int width;
    private String imgDir;
    private Handler handler;
    private HashMap<String, Boolean> isSelected;
    private int selectSize;
    private int selectTotal = 0;
    public Map<String, String> map = new HashMap<String, String>();
    public Map<String, String> contentImgMap = new HashMap<String, String>();

    private int hasImgSize;
    private String flag;

    public AlbumAdapter2(Context context, List<String> imgs, String imgDir,
                         Handler handler, HashMap<String, Boolean> isSelected, int hasImgSize, String flag) {
        super();
        this.context = context;
        this.imgs = imgs;
        width = new DisplayMetrics().widthPixels / 4;
        this.imgDir = imgDir;
        this.handler = handler;
        this.isSelected = isSelected;
        this.hasImgSize = hasImgSize;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int position) {
        return imgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.act_album_grid_item2, null);
            holder = new ViewHolder();
            holder.imageView = (ScaleTopHeaderView) convertView.findViewById(R.id.id_item_image);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.ch_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setPosition(position);
        holder.init();

        return convertView;
    }

    class ViewHolder {
        private ScaleTopHeaderView imageView;
        private int position;
        CheckBox checkBox;
        String imagePath;

        public void setPosition(int position) {
            this.position = position;
        }

        public void init() {








            checkBox.setVisibility(View.VISIBLE);
            imagePath = imgs.get(position);
            ImagerLoaderUtil.displayImage("file://" + imagePath , imageView);


            final boolean isSelect = isSelected.get(imagePath) != null && isSelected.get(imagePath) ? true : false;
            checkBox.setChecked(isSelect);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelect2 = isSelected.get(imagePath) != null && isSelected.get(imagePath) ? true : false;
                    // LogManger.i("www" , "isSelect" + isSelect2 + "selectTotal" + selectTotal);

                    if (flag.equals("1")) {
                        if (selectTotal + hasImgSize < 9) {
                            boolean b = !isSelect2;
                            if (b) {
                                selectTotal++;
                                checkBox.setChecked(true);
                                isSelected.put(imagePath, true);
                                map.put(imagePath, imagePath);
                            } else {
                                selectTotal--;
                                checkBox.setChecked(false);
                                map.remove(imagePath);
                                isSelected.remove(imagePath);
                            }
                        } else if (selectTotal + hasImgSize >= 9) {
                            if (isSelect2) {
                                selectTotal--;
                                checkBox.setChecked(false);
                                map.remove(imagePath);
                                isSelected.remove(imagePath);
                            } else {
                                handler.obtainMessage(2).sendToTarget();
                                checkBox.setChecked(false);
                            }
                        }
                    }
                }
            });

        }
    }

    public void destory() {
        System.gc();
    }

}
