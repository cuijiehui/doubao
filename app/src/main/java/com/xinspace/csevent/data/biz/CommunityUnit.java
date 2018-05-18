package com.xinspace.csevent.data.biz;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

/**
 * author by Mqz
 * Created by Android on 2017/9/18.
 */

public class CommunityUnit implements Parcelable {

    /**
     * code : 200
     * message : 获取社区列表
     * data : [{"id":"91","name":"物业管理处东门"},{"id":"117","name":"0701"},{"id":"118","name":"0702"},{"id":"119","name":"0703"},{"id":"120","name":"0704"},{"id":"121","name":"0801"},{"id":"122","name":"0802"},{"id":"123","name":"0803"},{"id":"124","name":"0804"},{"id":"125","name":"0901"},{"id":"126","name":"0902"},{"id":"127","name":"0903"},{"id":"128","name":"0904"},{"id":"129","name":"1001"},{"id":"130","name":"1002"},{"id":"131","name":"1003"},{"id":"132","name":"1004"},{"id":"133","name":"1101"},{"id":"134","name":"1102"},{"id":"135","name":"1103"},{"id":"136","name":"1104"},{"id":"137","name":"1201"},{"id":"138","name":"1202"},{"id":"139","name":"1203"},{"id":"140","name":"1204"},{"id":"141","name":"1301"},{"id":"142","name":"1302"},{"id":"143","name":"1303"},{"id":"144","name":"1304"},{"id":"145","name":"1501"},{"id":"146","name":"1502"},{"id":"147","name":"1503"},{"id":"148","name":"1504"},{"id":"149","name":"1601"},{"id":"150","name":"1602"},{"id":"151","name":"1603"},{"id":"152","name":"1604"},{"id":"153","name":"1701"},{"id":"154","name":"1702"},{"id":"155","name":"1703"},{"id":"156","name":"1704"},{"id":"157","name":"1801"},{"id":"158","name":"1802"},{"id":"159","name":"1803"},{"id":"160","name":"1804"},{"id":"161","name":"1901"},{"id":"162","name":"1902"},{"id":"163","name":"1903"},{"id":"164","name":"1904"},{"id":"165","name":"2001"},{"id":"166","name":"2002"},{"id":"167","name":"2003"},{"id":"168","name":"2004"},{"id":"169","name":"2101"},{"id":"170","name":"2102"},{"id":"171","name":"2103"},{"id":"172","name":"2104"},{"id":"173","name":"2201"},{"id":"174","name":"2202"},{"id":"175","name":"2203"},{"id":"176","name":"2204"},{"id":"177","name":"2301"},{"id":"178","name":"2302"},{"id":"179","name":"2303"},{"id":"180","name":"2304"},{"id":"181","name":"2401"},{"id":"182","name":"2402"},{"id":"183","name":"2403"},{"id":"184","name":"2404"},{"id":"185","name":"2501"},{"id":"186","name":"2502"},{"id":"187","name":"2503"},{"id":"188","name":"2504"},{"id":"189","name":"2601"},{"id":"190","name":"2602"},{"id":"191","name":"2603"},{"id":"192","name":"2604"},{"id":"193","name":"2701"},{"id":"194","name":"2702"},{"id":"195","name":"2703"},{"id":"196","name":"2704"},{"id":"197","name":"2801"},{"id":"198","name":"2802"},{"id":"199","name":"2803"},{"id":"200","name":"2804"},{"id":"201","name":"2901"},{"id":"202","name":"2902"},{"id":"203","name":"2903"},{"id":"204","name":"2904"},{"id":"205","name":"3001"},{"id":"206","name":"3002"},{"id":"207","name":"3003"},{"id":"208","name":"3004"},{"id":"209","name":"3101"},{"id":"210","name":"3102"},{"id":"211","name":"3103"},{"id":"212","name":"3104"},{"id":"213","name":"3201"},{"id":"214","name":"3202"},{"id":"215","name":"3203"},{"id":"216","name":"3204"},{"id":"318","name":"物业管理处东门临时"},{"id":"655","name":"0601"},{"id":"656","name":"0602"},{"id":"657","name":"0603"},{"id":"658","name":"0604"},{"id":"666","name":"0102"}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

    protected CommunityUnit(Parcel in) {
        code = in.readInt();
        message = in.readString();
    }

    public static final Creator<CommunityUnit> CREATOR = new Creator<CommunityUnit>() {
        @Override
        public CommunityUnit createFromParcel(Parcel in) {
            return new CommunityUnit(in);
        }

        @Override
        public CommunityUnit[] newArray(int size) {
            return new CommunityUnit[size];
        }
    };

    public static CommunityUnit objectFromData(String str) {

        return new Gson().fromJson(str, CommunityUnit.class);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
    }

    public static class DataBean {
        /**
         * id : 91
         * name : 物业管理处东门
         */

        private String id;
        private String name;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
