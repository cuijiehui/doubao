package sdk_sample.sdk.db;

/**
 * Created by Android on 2017/3/23.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sdk_sample.sdk.bean.LockBean;
import sdk_sample.sdk.bean.NeiborIdBean;
import sdk_sample.sdk.utils.SharedPreferencesUtil;

public class DatabaseHelper extends SQLiteOpenHelper {
    final String NEIBOR_ID_LIST_TABLE = "neiborIdList";
    final String LOCK_LIST_TABLE = "lockList";

    public DatabaseHelper(Context context) {
        super(context, (TextUtils.isEmpty(SharedPreferencesUtil.getUserName(context))?"data":SharedPreferencesUtil.getUserName(context)) + ".db", (CursorFactory)null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        this.createTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS neiborIdList");
        db.execSQL("DROP TABLE IF EXISTS lockList");
        this.createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        db.beginTransaction();
        db.execSQL("CREATE TABLE IF NOT EXISTS neiborIdList(fip varchar ,fport varchar,fneibname varchar,faddress varchar,department_id varchar,fremark varchar,neighborhoods_id varchar primary key)");
        db.execSQL("CREATE TABLE IF NOT EXISTS lockList(url varchar,lock_name varchar,domain_sn varchar,sip_number varchar primary key,lock_parent_name varchar)");
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public LockBean selectLock(String url, String sip_number) {
        try {
            SQLiteDatabase e = this.getWritableDatabase();
            Cursor cur = e.rawQuery("SELECT * FROM lockList Where url=\'" + url + "\'  and sip_number =\'" + sip_number + "\'", (String[])null);
            LockBean bean = new LockBean();

            while(cur.moveToNext()) {
                bean.setLock_name(cur.getString(cur.getColumnIndex("lock_name")));
                bean.setDomain_sn(cur.getString(cur.getColumnIndex("domain_sn")));
                bean.setSip_number(cur.getString(cur.getColumnIndex("sip_number")));
                bean.setLock_parent_name(cur.getString(cur.getColumnIndex("lock_parent_name")));
            }

            cur.close();
            e.close();
            return bean;
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public String getDisplayName(String sip_number) {
        if(TextUtils.isEmpty(sip_number)) {
            return "";
        } else {
            String displayName = "";

            try {
                SQLiteDatabase e = this.getReadableDatabase();
                Cursor cur = e.rawQuery("SELECT * FROM lockList Where sip_number=\'" + sip_number + "\'", (String[])null);
                if(cur.moveToNext()) {
                    return cur.getString(cur.getColumnIndex("lock_parent_name"));
                }

                cur.close();
                e.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return displayName;
        }
    }

    public String getDomainSn(String sip_number) {
        if(TextUtils.isEmpty(sip_number)) {
            return "";
        } else {
            String domain_sn = "";

            try {
                SQLiteDatabase e = this.getReadableDatabase();
                Cursor cur = e.rawQuery("SELECT * FROM lockList Where sip_number=\'" + sip_number + "\'", (String[])null);
                if(cur.moveToNext()) {
                    return cur.getString(cur.getColumnIndex("domain_sn"));
                }

                cur.close();
                e.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return domain_sn;
        }
    }

    public List<LockBean> selectLockList(String url) {
        ArrayList list = new ArrayList();

        try {
            SQLiteDatabase e = this.getReadableDatabase();
            Cursor cur = e.rawQuery("SELECT * FROM lockList Where url=\'" + url + "\'", (String[])null);
            LockBean bean = null;

            while(cur.moveToNext()) {
                bean = new LockBean();
                bean.setLock_name(cur.getString(cur.getColumnIndex("lock_name")));
                bean.setDomain_sn(cur.getString(cur.getColumnIndex("domain_sn")));
                bean.setSip_number(cur.getString(cur.getColumnIndex("sip_number")));
                bean.setLock_parent_name(cur.getString(cur.getColumnIndex("lock_parent_name")));
                list.add(bean);
            }

            cur.close();
            e.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return list;
    }

    public void saveLockList(List<LockBean> lockList, String url) {
        if(lockList != null) {
            Iterator var4 = lockList.iterator();

            while(var4.hasNext()) {
                LockBean bean = (LockBean)var4.next();
                saveLock(bean, url);
            }
        }
    }

    public boolean saveLock(LockBean bean, String url) {
        if(bean == null) {
            return false;
        } else {
            boolean isExe = false;
            SQLiteDatabase db = null;

            try {
                db = this.getWritableDatabase();
                db.beginTransaction();
                db.execSQL("replace into lockList values (\'" + url + "\',\'" + bean.getLock_name() + "\',\'" + bean.getDomain_sn() + "\',\'" + bean.getSip_number() + "\',\'" + bean.getLock_parent_name() + "\')");
                db.setTransactionSuccessful();
                isExe = true;
            } catch (Exception var9) {
                var9.toString();
            } finally {
                if(db != null) {
                    db.endTransaction();
                    db.close();
                }

            }

            return isExe;
        }
    }

    public boolean addNeiboridBean(NeiborIdBean bean) {
        boolean isExe = false;
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("replace into neiborIdList values (\'" + bean.getFip() + "\',\'" + bean.getFport() + "\',\'" + bean.getFneibname() + "\',\'" + bean.getFaddress() + "\',\'" + bean.getDepartment_id() + "\',\'" + bean.getFremark() + "\',\'" + bean.getNeighborhoods_id() + "\')");
            db.setTransactionSuccessful();
            isExe = true;
        } catch (Exception var8) {
            var8.toString();
        } finally {
            if(db != null) {
                db.endTransaction();
                db.close();
            }

        }

        return isExe;
    }

    public boolean removeNeiboridBean(String neighborhoods_id) {
        try {
            SQLiteDatabase e = this.getWritableDatabase();
            e.execSQL("delete from neiborIdList Where neighborhoods_id=\'" + neighborhoods_id + "\'");
            e.close();
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public List<NeiborIdBean> selectNeiboridBeanBeanList() {
        ArrayList list = new ArrayList();

        try {
            SQLiteDatabase e = this.getWritableDatabase();
            Cursor cur = e.rawQuery("SELECT * FROM neiborIdList", (String[])null);
            NeiborIdBean bean = null;

            while(cur.moveToNext()) {
                bean = new NeiborIdBean();
                bean.setFip(cur.getString(cur.getColumnIndex("fip")));
                bean.setFport(cur.getString(cur.getColumnIndex("fport")));
                bean.setFneibname(cur.getString(cur.getColumnIndex("fneibname")));
                bean.setFaddress(cur.getString(cur.getColumnIndex("faddress")));
                bean.setDepartment_id(cur.getString(cur.getColumnIndex("department_id")));
                bean.setFremark(cur.getString(cur.getColumnIndex("fremark")));
                bean.setNeighborhoods_id(cur.getString(cur.getColumnIndex("neighborhoods_id")));
                list.add(bean);
            }

            cur.close();
            e.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return list;
    }
}

