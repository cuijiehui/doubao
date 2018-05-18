package com.xinspace.csevent.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.login.model.HotQuestionBean;

import java.util.List;


/**
 * Created by Android on 2016/11/16.
 */
public class MoreQuesAdapter extends BaseAdapter{

    private List<HotQuestionBean> dataList;
    private Context context;

    public MoreQuesAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<HotQuestionBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_more_ques, null);
            holder.tv_hot_question = (TextView) convertView.findViewById(R.id.tv_hot_question);
            holder.tv_hot_answers = (TextView) convertView.findViewById(R.id.tv_hot_answers);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_hot_question.setText((position + 1) + "." + dataList.get(position).getQuestions());
        holder.tv_hot_answers.setText(dataList.get(position).getAnswers());

        return convertView;
    }


    class ViewHolder {
        TextView tv_hot_question;
        TextView tv_hot_answers;
    }

}
