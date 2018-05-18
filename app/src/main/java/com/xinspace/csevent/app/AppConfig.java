package com.xinspace.csevent.app;

/**
 * 此类用于存放接口url
 */
public class AppConfig {

    private static final String ShiDe = "shide";
    private static final String ShiDe_Test = "shide_test";

    public static final String COMMUNITY_AREA = "community_area";

    public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    //接口地址
    public static final String BaseUrl = "https://app.coresun.net/"+ ShiDe +"/v1/webapi_user/";
    public static final String BaseUrl2 = "https://app.coresun.net/"+ ShiDe +"/V2/Indiana_orther/";
    public static final String BaseUrl3 = "https://app.coresun.net/"+ ShiDe +"/v1/Webapi_user/";
    public static final String BaseShedeTest = "https://app.coresun.net/"+ ShiDe +"/V2/";
    public static final String WebBaseUrl = "https://app.coresun.net/shide/";

    public static final String BaseShiDeURl = "https://app.coresun.net/"+ ShiDe +"/";
    public static final String BaseShiDeURl2 = "http://app.coresun.net/"+ ShiDe +"/";

    public static final String BaseNewUrl = "http://wx.szshide.shop/";

    static String indexName="user_avatar";

    //ossKey获取地址
    public static final String OSS_KEY_URL=WebBaseUrl+"v1/webapi_public/get_token";

    //验证验证码是否正确
    public static final String VERIFY_CODE_URL=BaseUrl+"is_code";

    //获取商家icon
    public static final String GET_BRAND_ICON_URL=BaseUrl+"brand";

    //是否第一次抽取小奖品
    public static final String IS_FIRST_GET_SMALL_PRIZE=BaseUrl+"user_first";

    //获取品牌商家的轮播图
    public static final String GET_BRAND_PICTURE_URL=BaseUrl+"brand_select";

    //获取夺宝记录列表
    public static final String DUO_BAO_RECORD_URL=BaseUrl+"user_active_list";

    //独家页面的商品分类列表
    //public static final String PRODUCT_CLASS_LIST=BaseUrl+"get_active_class";
    public static final String PRODUCT_CLASS_LIST = BaseShiDeURl + "/V2/Activity_management/admin_class";

    //下载软件后给用户添加积分
    public static final String ADD_INTEGRAL_URL =BaseUrl+"found_record";

    //发现页面
    public static final String FOUND_DISCOVER_URL=BaseUrl+"found";

    //推荐软件的详细信息
    public static final String SOFTWARE_DETAIL_URL=BaseUrl+"found_select";

    //获取订单支付信息
    public static final String PAY_INFO="https://www.xinspace.com.cn/sz_web/v_master/webapi_public/unifiedorder";

    //马上开始抽奖
    public static final String START_AWARD_NOW=BaseUrl+"get_rand_active";

    //轮盘抽奖接口
    public static final String ATTEND_AWARD_FOR_POOL=BaseUrl+"mark_turntable_active";

    //即开抽奖第一次查询库存
    public static final String ACT_CHECK_STOCKET_GET = "http://120.76.25.154/" + ShiDe +"/Redis/Redis_goods/get";

    //即开抽奖第二次查询库存
    public static final String ACT_CHECK_STOCKET_SET = "http://120.76.25.154/" + ShiDe +"/Redis/Redis_goods/set";

    //签到
    public static final String QIAN_DAO_URL=BaseUrl+"user_sign_in";

    //判断用户是否已经签到
    public static final String IS_QIAN_DAO_URL=BaseUrl+"is_sign_in";

    //付款成功后调用的充值接口
    public static final String PAY_SUCCESS_CHARGE_URL=WebBaseUrl+"notify_url/integral_notify";

    //上传头像
    public static final String UPLOAD_PHOTO_URL=WebBaseUrl+"upload/?admin_commodity_image_index="+ CoresunApp.USER_ID+"&param_name="+indexName;

    //获取活动列表接口
    public static final String ACTIVITY_LIST_URL = BaseUrl+"get_active";

    //获取众筹活动列表接口
    //public static final String CROWD_LIST_URL = "http://app.coresun.net/" + ShiDe_Test + "/V2/Activity_management/get_active";
    public static final String CROWD_LIST_URL = BaseShiDeURl2  +  "V2/Activity_management/get_active";



    //广告获取接口
    public static final String ADVERTISEMENT_URL=BaseUrl+"get_ad_picture";

    //充值接口
    public static final String CHARGE_URL=BaseUrl+"user_integral";

    //抽奖接口
    public static final String ATTEND_AWARD_URL=BaseUrl+"mark_active";

    //获取个人信息
    public static final String PROFILE_URL=BaseUrl+"get_user_infos";

    public static final String PROFILE_URL2 = "http://shop.coresun.net/user/Usersapi/Getinfo";

    //获取即时抽奖活动信息详情接口
    public static final String ACTIVITY_INFORMATION = BaseUrl +"/activity_information";

    //获取众筹抽奖活动信息详情接口
    public static final String CROWD_ACT_INFORMATION = BaseShiDeURl +"V2/Activity_management/activity_information";

    //注册接口调用
    public static final String REGISTER_URL=BaseUrl+"registered";
    public static final String REGISTER_URL2 = "http://shop.coresun.net/user/Usersapi/Registered";
    public static final String REGISTER_URL3 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=account.apiRegister";

    //注册的验证码接口
    public static final String REGISTER_CODE_URL=BaseUrl+"sendcode";

    public static final String REGISTER_CODE_URL2 = "http://shop.coresun.net/user/Usersapi/sendcode";

    //重置密码
    public static final String RESET_CODE_URL=BaseUrl+"retrieve";

    //注册的验证码接口
    public static final String LOGIN_URL=BaseUrl+"login";

    public static final String LOGIN_URL2 = "http://shop.coresun.net/user/Usersapi/Login";

    public static final String LOGIN_URL3 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=account.apiLogin";

    //获取收货地址的接口
    //public static final String GET_ADDRESS_URL=BaseUrl+"get_addresslist";
    //public static final String GET_ADDRESS_URL="http://shop.coresun.net/user/Usersapi/user_address";
    public static final String GET_ADDRESS_URL=BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apiaddress";

    //index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.updateAddress
    public static final String ADD_TRY_ADDRESS_URL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.updateAddress";


    //添加收货地址的接口
    //public static final String ADD_ADDRESS_URL=BaseUrl+"add_address";
    public static final String ADD_ADDRESS_URL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apiaddress.submit";

    //删除收货地址的接口
    public static final String DELETE_ADDRESS_URL=BaseUrl+"del_address";

    public static final String DELETE_ADDRESS_URL2 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apiaddress.delete";

    //编辑收货地址的接口
    public static final String EDIT_ADDRESS_URL=BaseUrl+"re_address";

    //默认收货地址的接口
    public static final String DEFAULT_ADDRESS_URL=BaseUrl+"get_single_default";
    public static final String DEFAULT_ADDRESS_URL2 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apiaddress.setdefault";


    //第三方登录的接口
    public static final String THIRD_LOGIN_URL=BaseUrl+"third_party_login";

    //上传错误日志的接口
    public static final String ERR_LOG_URL=BaseUrl+"write_errlog";

    //获取第一次使用的小奖品的接口
    public static final String SMALL_PRIZES_URL=BaseUrl+"get_smallprizes_infos";

    //获取第一次使用的小奖品的接口
    public static final String SEND_RECORD_URL=BaseUrl+"mark_small_active";

    //获取用户奖品列表的接口
    public static final String PRIZES_LIST_URL=BaseUrl+"user_prize_list";

    //获取用户中奖记录详情的接口
    public static final String PRIZES_DETAIL_URL=BaseUrl+"user_prize";

    //中奖后没有地址时提交地址的接口
    public static final String COMMIT_ADDRESS_URL=BaseUrl+"delivery_up_address";

    //中奖后已经有地址时修改地址的接口
    public static final String MODIFY_ADDRESS_URL=BaseUrl3+"delivery_up";

    //中奖后为小奖品添加地址的接口
    public static final String ADD_ADDRESS_FOR_SMALL_PRIZE_URL=BaseUrl+"small_prizes_up";

    //补全个人信息的接口
    public static final String SUPPLY_USER_INFO_URL=BaseUrl+"user_complete";

    //金币兑换积分比例的接口
    public static final String GET_EXCHANGE_RATE_URL=BaseUrl+"get_exchange_proportion";

    //会员中心Q&A页面的接口
    public static final String GET_Q_AND_A_URL=BaseUrl+"questions";

    //会员中心意见反馈的接口
    public static final String FEEDBACK_URL=BaseUrl+"feedback";

    //会员中心抽奖规则的接口
    public static final String GET_RULES_URL=BaseUrl+"feedback_rule";

    //会员中心用户邮箱获取系统推送的接口
    public static final String USER_EMAIL_GET_SYS_MSG_URL=BaseUrl+"feedback_email";

    //获取启动应用时全屏广告的接口
    public static final String GET_FULLPAGE_ADS_URL=BaseUrl+"adlist_large";

    //添加完善资料的接口(写)
    public static final String SUPPLY_INFO_WRITE_URL=BaseUrl+"user_complete";

    //添加完善资料的接口(读)
    public static final String SUPPLY_INFO_READ_URL=BaseUrl+"user_complete_select";

    //奖品详情页面确认派送的接口
    public static final String SURE_DELIVERY_URL=BaseUrl+"confirmation_delivery";

    //版本更新的接口
    //public static final String UPDATE_SOFO_URL=BaseUrl+"android_edition";
    public static final String UPDATE_SOFO_URL= "http://community.coresun.net/api/Version/getVersionInfo";

    //奖品分类后获取分类内容的接口   1-54
    //public static final String GET_TYPE_CONTENT_URL=BaseUrl+"get_active_data";
    public static final String GET_TYPE_CONTENT_URL = BaseShiDeURl + "V2/Activity_management/product_class_list";


    //常见问题接口
    public static final String GET_TYPE_FAQ = BaseUrl2 + "get_QA";

    //抽奖规则
    public static final String GET_TYPE_rule = BaseUrl2 + "get_rule";

    //用户协议
    public static final String GET_TYPE_user_agree= BaseUrl2 + "get_rule";

    //众筹原价购买
    public static final String FULL_PRICE_BUY = BaseShiDeURl + "V2/Indiana_WebapiUser/Fullprice_purchase";

    //众筹购买
    public static final String CROWD_PRICE_BUY = BaseShiDeURl + "V2/Indiana_purchase/purchase";

    //获得微信支付宝统一下单单号
    public static final String WA_ORDER_NUM = BaseShiDeURl + "v1/webapi_user/user_integral";

    //用户购买记录
   // public static final String BUY_RECORD = BaseShiDeURl + "V2/Indiana_WebapiUser/Purchase_record";

    //金币购买接口
    public static final String GoldExIntegral = BaseShiDeURl + "V2/Indiana_WebapiUser/exchange";

    //众筹活动入队
    public static final String CrowdAddQueue = "http://redis.coresun.net/" + ShiDe + "/Redis/Redis_goods/getlock";

    //众筹活动出队
    public static final String CrowdDeQueue = "http://redis.coresun.net/" + ShiDe + "/Redis/Redis_goods/pop";

    //众筹活动抽奖记录
    public static final String CrowdRecord =  BaseShiDeURl + "V2/Indiana_WebapiUser/User_record";

    //众筹活动中奖奖记录
    public static final String CrowdWinRecord =  BaseShiDeURl + "V2/Indiana_WebapiUser/Purchase_win_record";

    //众筹活动揭晓列表
    public static final String CrowdPublish = BaseShiDeURl + "V2/Activity_management/get_winList";

    //众筹活动往期揭晓列表
    public static final String CrowdActivitiesPublish = BaseShiDeURl + "V2/Indiana_WebapiUser/Single_event_record";


    //众筹活动参与记录
    public static final String CrowdPartRecord = BaseShiDeURl + "V2/Indiana_WebapiUser/Participation_record";

    //众筹活动下一期
    public static final String CrowdNext = BaseShiDeURl + "V2/Activity_management/return_newId";

    //检测众筹支付是否成功
    public static final String checkCrowPayState = BaseShiDeURl2 + "V2/Indiana_WebapiUser/find_state";

    //获取晒点列表
    public static final String baskOrderList = BaseShiDeURl + "v1/webapi_user/showOrderList";

    //获取我的晒单列表
    public static final String myBaskOrderList = BaseShiDeURl + "v1/Webapi_user/showPersonal";

    //发表文字
    public static final String addTextContent = BaseShiDeURl + "v1/Webapi_user/addorder";

    //热点问题
    public static final String hotQuesList = BaseShiDeURl + "v1/webapi_user/questions";

    //统计接口
    public static final String statisticsApi = BaseShiDeURl + "V2/Indiana_Statistics/setRecord";
    //public static final String statisticsApi = "https://app.coresun.net/shide_test/V2/Indiana_Statistics/setRecord";

    public static final String longOpenDoorAPI = "https://www.doormaster.me:9099/doormaster/server/remote_control";

    //商品详情接口
    public static final String goodsDetail = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=goods.detail.api";

    //下订单接口
    //public static final String placeOrder = "http://shop.coresun.net/user/Order/setOrder";
    public static final String placeOrder = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.apicreate.submit";

    //团购下订单的接口
    public static final String groupOrder = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.createOrder";

    //积分下单接口
    //public static final String exChangeOrder = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog.payOrder";
    public static final String exChangeOrder = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog";

    //商品规格
    public static final String goodsSpec = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=goods.picker.api";

    //IntegralStandard
    public static final String IntegralStandard = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.api.option";

    //商品分类
    public static final String goodsClass = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=diypage.api.tabbar&id=22";

    //商品加入购物车
    public static final String addCart = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apicart.add";

    //获取购物车列表
    public static final String getShopCart = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apicart.get_list";

    //获取订单数据
    public static final String getOrderData = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.apipay";

    //获得微信支付宝统一下单单号2
    //public static final String WA_ORDER_NUM2 = BaseShiDeURl + "v1/webapi_public/unifiedorder2";
    public static final String WA_ORDER_NUM2 =   "http://wx.szshide.shop/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.apipay.toWechat";

    //获取验证码
    public static final String REGISTER_CODE_URL3 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=account.apiVerifycode";

    public static final String REGISTER_CODE_URL4 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=account.apiVerifycode";


    //忘记密码的注册
    public static final String FORGET_REGISTER_URL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=account.apiForget";

    //移除购物车
    public static final String REMOVE_SHOPCART_URL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apicart.remove";

    //用户购买记录
    public static final String BUY_RECORD = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.api.get_list";

    //用户积分兑换记录
    public static final String CONVERT_RECORD = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog.getCreditLog";

    //用户积分
    public static final String USER_INTEGRAL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.apiuser.integral";

    //用户个人信息
    public static final String USER_MESSAGE = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.api" ;

    //订单详情
    public static final String ORDER_DETAIL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.api.detail";

    //取消订单
    public static final String CANCLE_ORDER = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.api.cancel";

    //删除订单
    public static final String DEL_ORDER = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.api.delete";

    //确认收货confirmReceipt
    public static final String CONFIRM_RECEIPT = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.api.finish";

    //退款
    public static final String AFTER_SALE = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.apirefund.submit";

    //取消退款
    public static final String CANCLE_AFTER_SALE = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.apirefund.cancel";

    //查看物流
    public static final String LOOK_EXPRESS = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.api.express";
    public static final String LOOK_EXPRESS2 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.express";
    public static final String LOOK_EXPRESS3 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog.getlogistics";

    //积分商城收货
    public static final String JIFENSHOUHUO = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog.confirm";

    //微信首页
    public static final String WECHAT_GOODS = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=goods.get_list";

    //App首页商品
    public static final String FRIST_GOODS = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=diypage.api.recommend";

    // 九块九包邮
    public static final String JIU_GOODS = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=diypage.api.issendfree";

    //免费试用
    public static final String FRIST_TRY = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=diypage.api.apply";

    //申请免费试用
    public static final String APPLY_TRY = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.apply";

    //我的试用
    public static final String MY_APPLY= BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.detail";

    //我的试用列表
    public static final String MY_APPLY_LIST= BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.getMyApply";

    //秒杀时间
    public static final String SECKILL_TIME = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=seckill.api";

    //秒杀的列表
    public static final String SECKILL_LIST = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=seckill.api.seckillgoods";

    //拼团的列表
    public static final String GROUP_LIST = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api";

    //拼团分类列表
    public static final String GROUP_CATE_LIST = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.getList";

    //获取拼团商品详情
    public static final String GROUP_DETAIL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.getDetail";

    //获取拼团人员的详情
    public static final String GROUP_PERSON_DETAIL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.invite";

    //判断商品是否可以购买
    public static final String GROUP_IS_BUY = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.judgeOrder";

    // 团购商品统一下单
    public static final String PAY_GROUP_ORDER = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.pay";

    //积分商城统一下单
    public static final String PAY_EXCHANGE_ORDER = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog.payOrder";

    //秒杀商品详情
    public static final String SECKILL_DETAIL = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=seckill.api.secKillDetail";

    //获取拼团中
    public static final String GROUPING_DATA = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.getMyTeams";

    //试用分类
    public static final String TRIAL_TYPE = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.getCategory";

    //试用列表
    public static final String TRIAL_LIST = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=trial.api.getList";

    //积分兑换首页
    public static final String EXCHANGE_LIST = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.api";

    //签到
    public static final String SIGN_DATA = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog.sign";

    //积分搜索
    public static final String SERACH_DATA = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.api.getList";

    //获取商城分类
    public static final String SHOP_TYPE = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=shop.category.api";

    //修改个人信息
    public static final String Edit_Info = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.api.editInfo";

    //验证验证码
    public static final String VERIFY_CODE_URL2 = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=member.api.checkCode";

    //商城获取支付宝订单接口 by mqz
    public static final String SHOP_ALIPAY_INFO = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=order.apipay.toAlipay";

    //积分商城支付接口 by mqz
    public static final String SHOP_EXCHANGE_PAY_INFO = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=creditshop.apilog.payOrder";

    //团购支付宝接口
    public static final String GROUP_ALIPAY_INFO = BaseNewUrl + "app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=groups.api.alipay";

    /**
     *
     * --------------------------------------------------------------------------------------------------------------------
     *
     *
     */

    //社区接口头
    public static final String COMMUNITY_BASE_URL = "http://community.coresun.net";

    //社区接口头
    public static final String GZ_COMMUNITY_BASE_URL = "http://community.coresun.net:8080";

    //提交报修内容
    public static final String SUBMIT_REPAIRS = "/api/Repair/submitRepairInfo";

    //报修的记录列表
    public static final String REPAIRS_LIST = "/api/Repair/getRepairInfoList";

    //获取dai缴费列表
    public static final String PAYMENT = "/api/Charge/getChargeCate";

    //缴费统一下单
    public static final String PAY_ORDER = "/api/Wxpay/genChargeOrder";

    //获取最新公告数据
    public static final String COMMUNITY_NOTICE = "/api/Notice/getThreeList";

    //获取公告列表数据
    public static final String COMMUNITY_NOTICE_LIST = "/api/Notice/getList";

    //获取缴费列表数据
    public static final String COMMUNITY_PAYMENT_LIST = "/api/Charge/chargeRecord";

    //获取缴费类别
    public static final String PAY_TYPE = "/api/Charge/getCates";

    //获取社区列表
    public static final String COM_LIST = "/api/Area/getCommunityList";

    //获取社区房间号
    public static final String CODE_LIST =  "/api/Area/getPropertyList";

    //提交社区审核信息
    public static final String COM_SUBMIT = "/api/Area/setUserCommunity";

    //获取审核状态
    public static final String QUERY_AUDIT_STATUS =  "/api/Area/getUserStatus";

    //开门成功提交开开门记录
    public static final String ADD_LOCK_RECORD = "/api/Record/addEntranceRecord";

    //获取开门记录
    public static final String GET_LOCK_RECORD =  "/api/Record/getList";

    //获取通话界面的广告
    public static final String GET_INCALL_VD_DATA =  "/api/ad/getList";

    //租赁房租的展示信息
    public static final String GET_ROOM_DATA =  "/api/Rent/rentList";

    //获取城市接口
    public static final String GET_CITY_DATA =  "/api/City/cityList";

    //获取城市区域接口
    public static final String GET_CITY_AREA_DATA = "/api/City/getArea";

    //提交申请信息
    public static final String SUBMIY_APPLY_DATA =  "/api/Rent/apply";

    //获取申请的所有信息
    public static final String GET_APPLY_DATA = "/api/Rent/myApply";

    //获取社区列表   by mqz
    public static final String GET_COMMUNITY_LIST =  "/api/Area/getPropertyList";

    //支付宝物业缴费 by mqz
    public static final String COMMUNITY_ALIPAY = "/api/Alipay/genChargeOrder";

    //获取用户的社区信息 by mqz
    public static String APP_COM =  "/api/Area/getUserEntrance";

    public static String WEB_URL="NO";

}

