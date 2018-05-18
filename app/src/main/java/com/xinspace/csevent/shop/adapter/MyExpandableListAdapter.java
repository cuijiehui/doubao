package com.xinspace.csevent.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.data.biz.GetActivityListBiz;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.BuyGoodsAct;
import com.xinspace.csevent.shop.activity.GoodsDetailAct;
import com.xinspace.csevent.shop.modle.GoodsDetailBean;
import com.xinspace.csevent.shop.modle.OrderGoodsBean;
import com.xinspace.csevent.shop.modle.ShopBean;
import com.xinspace.csevent.shop.modle.ShopCartBean;
import com.xinspace.csevent.shop.weiget.OnShoppingCartChangeListener;
import com.xinspace.csevent.shop.weiget.ShoppingCartBiz;
import com.xinspace.csevent.shop.weiget.UIAlertView;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ShopBean> mListGoods = new ArrayList<>();
    private OnShoppingCartChangeListener mChangeListener;
    private boolean isSelectAll = false;
    private String openid;

    public MyExpandableListAdapter(Context context , String openid) {
        mContext = context;
        this.openid = openid;
    }

    public void setList(List<ShopBean> mListGoods) {
        this.mListGoods = mListGoods;
        setSettleInfo();
    }

    public void setOnShoppingCartChangeListener(OnShoppingCartChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public View.OnClickListener getAdapterListener() {
        return listener;
    }

    @Override
    public int getGroupCount() {
        return mListGoods.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListGoods.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListGoods.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListGoods.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_group_test, parent, false);
            holder.tvGroup = (TextView) convertView.findViewById(R.id.tvShopNameGroup);
            holder.tvEdit = (TextView) convertView.findViewById(R.id.tvEdit);
            holder.ivCheckGroup = (ImageView) convertView.findViewById(R.id.ivCheckGroup);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        holder.tvGroup.setText(mListGoods.get(groupPosition).getShopName());

        ShoppingCartBiz.checkItem(mListGoods.get(groupPosition).isGroupSelected(), holder.ivCheckGroup);
        boolean isEditing = mListGoods.get(groupPosition).isEditing();
        if (isEditing) {
            holder.tvEdit.setText("完成");
        } else {
            holder.tvEdit.setText("编辑");
        }

        holder.ivCheckGroup.setTag(groupPosition);
        holder.ivCheckGroup.setOnClickListener(listener);
        holder.tvEdit.setTag(groupPosition);
        holder.tvEdit.setOnClickListener(listener);
        //holder.tvGroup.setOnClickListener(listener);
        return convertView;
    }

    /**
     * child view
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_child_test, parent, false);
            holder.tvChild = (TextView) convertView.findViewById(R.id.tvItemChild);
            holder.tvDel = (TextView) convertView.findViewById(R.id.tvDel);
            holder.ivCheckGood = (ImageView) convertView.findViewById(R.id.ivCheckGood);
            holder.rlEditStatus = (RelativeLayout) convertView.findViewById(R.id.rlEditStatus);
            holder.llGoodInfo = (LinearLayout) convertView.findViewById(R.id.llGoodInfo);
            holder.ivAdd = (ImageView) convertView.findViewById(R.id.ivAdd);
            holder.ivReduce = (ImageView) convertView.findViewById(R.id.ivReduce);
            holder.tvGoodsParam = (TextView) convertView.findViewById(R.id.tvGoodsParam);
            holder.tvPriceNew = (TextView) convertView.findViewById(R.id.tvPriceNew);
            holder.tvPriceOld = (TextView) convertView.findViewById(R.id.tvPriceOld);
            holder.tvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//数字被划掉效果
            holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
            holder.tvNum2 = (TextView) convertView.findViewById(R.id.tvNum2);
            holder.ivGoods = (ImageView) convertView.findViewById(R.id.ivGoods);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        ShopCartBean goods = mListGoods.get(groupPosition).getList().get(childPosition);

        boolean isChildSelected = mListGoods.get(groupPosition).getList().get(childPosition).isChildSelected();
        boolean isEditing = goods.isEditing();

        String priceNew = "¥" + goods.getMarketprice();
        String priceOld = "¥" + goods.getProductprice();

        String num = goods.getTotal();
        String pdtDesc = goods.getOptiontitle();
        String goodName = mListGoods.get(groupPosition).getList().get(childPosition).getTitle();
        String imgUrl = mListGoods.get(groupPosition).getList().get(childPosition).getThumb();

        //ImagerLoaderUtil.displayRoundedImage(imgUrl , holder.ivGoods);
        ImagerLoaderUtil.displayImageWithLoadingIcon(imgUrl , holder.ivGoods , R.drawable.icon_detail_load);

        holder.ivCheckGood.setTag(groupPosition + "," + childPosition);
        holder.tvChild.setText(goodName);
        holder.tvPriceNew.setText(priceNew);
        holder.tvPriceOld.setText(priceOld);
        holder.tvNum.setText("X " + num);
        holder.tvNum2.setText(num);

        if(pdtDesc != null &&  !pdtDesc.equals("")){
            holder.tvGoodsParam.setText("商品规格:" + pdtDesc);
        }else{
            holder.tvGoodsParam.setText("商品规格:无");
        }

        holder.ivAdd.setTag(goods);
        holder.ivReduce.setTag(goods);
        holder.tvDel.setTag(groupPosition + "," + childPosition);
        holder.llGoodInfo.setTag(groupPosition + "," + childPosition);

        //holder.tvDel.setTag(groupPosition + "," + childPosition);

        ShoppingCartBiz.checkItem(isChildSelected, holder.ivCheckGood);

        if (isEditing) {
            holder.llGoodInfo.setVisibility(View.GONE);
            holder.rlEditStatus.setVisibility(View.VISIBLE);
        } else {
            holder.llGoodInfo.setVisibility(View.VISIBLE);
            holder.rlEditStatus.setVisibility(View.GONE);
        }

        holder.ivCheckGood.setOnClickListener(listener);
        holder.tvDel.setOnClickListener(listener);
        holder.ivAdd.setOnClickListener(listener);
        holder.ivReduce.setOnClickListener(listener);
        holder.llGoodInfo.setOnClickListener(listener);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //main
                case R.id.ivSelectAll:   //  购物车全选
                    isSelectAll = ShoppingCartBiz.selectAll(mListGoods, isSelectAll, (ImageView) v);
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
//                case R.id.tvEditAll:
//
//
//                    break;
                case R.id.tvEdit:     //切换界面，属于特殊处理，假如没打算切换界面，则不需要这块代码
                    int groupPosition2 = Integer.parseInt(String.valueOf(v.getTag()));
                    boolean isEditing = !(mListGoods.get(groupPosition2).isEditing());
                    mListGoods.get(groupPosition2).setEditing(isEditing);
                    for (int i = 0; i < mListGoods.get(groupPosition2).getList().size(); i++) {
                        mListGoods.get(groupPosition2).getList().get(i).setEditing(isEditing);
                    }
                    notifyDataSetChanged();
                    break;
//                case R.id.ivCheckGroup:     //商铺的全选
//                    int groupPosition3 = Integer.parseInt(String.valueOf(v.getTag()));
//                    isSelectAll = ShoppingCartBiz.selectGroup(mListGoods, groupPosition3);
//                    selectAll();
//                    setSettleInfo();
//                    notifyDataSetChanged();
//                    break;
                //child
                case R.id.ivCheckGood:    //单个商品的选择
                    String tag = String.valueOf(v.getTag());
                    if (tag.contains(",")) {
                        String s[] = tag.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        isSelectAll = ShoppingCartBiz.selectOne(mListGoods, groupPosition, childPosition);
                        selectAll();
                        setSettleInfo();
                        notifyDataSetChanged();
                    }
                    break;
                case R.id.tvDel:   // 删除商品
                    String tagPos = String.valueOf(v.getTag());
                    if (tagPos.contains(",")) {
                        String s[] = tagPos.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        showDelDialog(groupPosition, childPosition);
                    }
                    break;
                case R.id.ivAdd:   // 加
                    ShoppingCartBiz.addOrReduceGoodsNum(true, (ShopCartBean) v.getTag(), ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                    setSettleInfo();
                    break;
                case R.id.ivReduce:    // 减
                    ShoppingCartBiz.addOrReduceGoodsNum(false, (ShopCartBean) v.getTag(), ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                    setSettleInfo();
                    break;
                case R.id.llGoodInfo:
                    //ToastUtil.makeToast("商品详情，暂未实现");

                    String tag1 = String.valueOf(v.getTag());
                    if (tag1.contains(",")) {
                        String s[] = tag1.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        String goodId = mListGoods.get(groupPosition).getList().get(childPosition).getGoodsid();
                        Intent intent2 = new Intent(mContext , GoodsDetailAct.class);
                        intent2.putExtra("goodId" , goodId);
                        mContext.startActivity(intent2);
                    }
                    break;
//                case R.id.tvShopNameGroup:
//                    ToastUtil.makeToast("商铺详情，暂未实现");
//                    break;
                case R.id.btnSettle:   // 结算按钮

                    if (ShoppingCartBiz.hasSelectedGoods(mListGoods)) {
                        ToastUtil.makeToast("结算跳转");

                        String[] infos = ShoppingCartBiz.getShoppingCount(mListGoods);
                        List<GoodsDetailBean> goodsList = ShoppingCartBiz.getShopGoods(mListGoods);
                        List<OrderGoodsBean> orderList = ShoppingCartBiz.getShopOrder(mListGoods);

                        LogUtil.i("goodsList" +  goodsList.size() + "orderList" + orderList.size());
                        Intent intent = new Intent(mContext, BuyGoodsAct.class);
                        intent.putExtra("data" , (Serializable) goodsList);
                        intent.putExtra("dataOrder" , (Serializable) orderList);
                        intent.putExtra("allPrice" , infos[1]);
                        intent.putExtra("flag" , "0");
                        mContext.startActivity(intent);

                    } else {
                        ToastUtil.makeToast("请先选择商品！");
                    }
                    break;
            }
        }
    };

    private void selectAll() {
        if (mChangeListener != null) {
            mChangeListener.onSelectItem(isSelectAll);
        }
    }

    private void setSettleInfo() {
        String[] infos = ShoppingCartBiz.getShoppingCount(mListGoods);
        //删除或者选择商品之后，需要通知结算按钮，更新自己的数据；
        if (mChangeListener != null && infos != null) {
            mChangeListener.onDataChange(infos[0], infos[1]);
        }
    }

    private void showDelDialog(final int groupPosition, final int childPosition) {
        final UIAlertView delDialog = new UIAlertView(mContext, "温馨提示", "确认删除该商品吗?",
                "取消", "确定");
        delDialog.show();

        delDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {

                                       @Override
                                       public void doLeft() {
                                           delDialog.dismiss();
                                       }

                                       @Override
                                       public void doRight() {
                                           removeShopCart(openid , groupPosition , childPosition);
                                           delDialog.dismiss();
                                       }
                                   }
        );
    }

    private void delGoods(int groupPosition, int childPosition) {
        mListGoods.get(groupPosition).getList().remove(childPosition);
        if (mListGoods.get(groupPosition).getList().size() == 0) {
            mListGoods.remove(groupPosition);
        }
        notifyDataSetChanged();
    }

    private void removeShopCart(String openId , final int groupPosition , final int childPosition){

        String goodsId = mListGoods.get(groupPosition).getList().get(childPosition).getId();

        GetActivityListBiz.removeShopCartData(openId, goodsId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){

                    ToastUtil.makeToast("移除购物车成功");
                    delGoods(groupPosition, childPosition);
                    setSettleInfo();
                    notifyDataSetChanged();

                }else{
                    ToastUtil.makeToast("移除购物车失败");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }



    class GroupViewHolder {
        TextView tvGroup;
        TextView tvEdit;
        ImageView ivCheckGroup;
    }

    class ChildViewHolder {
        /**
         * 商品名称
         */
        TextView tvChild;
        /**
         * 商品规格
         */
        TextView tvGoodsParam;
        /**
         * 选中
         */
        ImageView ivCheckGood;
        /**
         * 非编辑状态
         */
        LinearLayout llGoodInfo;
        /**
         * 编辑状态
         */
        RelativeLayout rlEditStatus;
        /**
         * +1
         */
        ImageView ivAdd;
        /**
         * -1
         */
        ImageView ivReduce;
        /**
         * 删除
         */
        TextView tvDel;
        /**
         * 新价格
         */
        TextView tvPriceNew;
        /**
         * 旧价格
         */
        TextView tvPriceOld;
        /**
         * 商品状态的数量
         */
        TextView tvNum;
        /**
         * 编辑状态的数量
         */
        TextView tvNum2;

        ImageView ivGoods;
    }
}
