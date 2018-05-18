package com.xinspace.csevent.shop.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinspace.csevent.R;
import com.xinspace.csevent.data.entity.GroupParticipants;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Android on 2017/8/22.
 */

public class RecommendAdapter extends BaseQuickAdapter<GroupParticipants.RecommendBean, RecommendAdapter.VHolder>{

    public RecommendAdapter(@LayoutRes int layoutResId, @Nullable List<GroupParticipants.RecommendBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(VHolder helper, GroupParticipants.RecommendBean item) {

        SpannableStringBuilder ssb = new SpannableStringBuilder(item.getGroupnum() + "人拼团 " + item.getTitle());
        ForegroundColorSpan soreSpan = new ForegroundColorSpan(Color.parseColor("#EA5202"));
        ssb.setSpan(soreSpan, 0, item.getGroupnum().length()+3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        helper.recommendTitle.setText(ssb);

        helper.recommendPrice.setText(item.getGroupsprice());
        helper.recommendAllPrice.setText(item.getPrice());
        helper.recommendAllPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//删除线
        helper.recommondParticipants.setText("已有" + item.getSales() + "人参与");
        Glide.with(mContext)
                .load(item.getThumb())
                .error(R.drawable.loading_icon)
                .crossFade()
                .into(helper.imgRecommend);
    }

    class VHolder extends BaseViewHolder{

        private ImageView imgRecommend;
        private TextView recommendTitle,recommendPrice,recommendSinglePrice,recommendAllPrice,recommondParticipants;

        public VHolder(View view) {
            super(view);
            imgRecommend = (ImageView) view.findViewById(R.id.spell_group_recommend_img);
            recommendTitle =(TextView) view.findViewById(R.id.spell_group_recommend_title);
            recommendPrice =(TextView) view.findViewById(R.id.recommend_price);
            recommendSinglePrice =(TextView) view.findViewById(R.id.recommend_single_price);
            recommendAllPrice =(TextView) view.findViewById(R.id.recommend_all_price);
            recommondParticipants =(TextView) view.findViewById(R.id.recommend_salesout);
        }
    }
}
