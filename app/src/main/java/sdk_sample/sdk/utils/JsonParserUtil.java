package sdk_sample.sdk.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Android on 2017/3/23.
 */

public class JsonParserUtil {
    private JsonParserUtil() {
    }

    public static <T> T parseJson2Object(Class<T> clazz, String json) throws ClassNotFoundException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return parseJson2Object(clazz, jsonObject);
    }

    public static <T> T parseJson2Object(Class<T> clazz, JSONObject jsonObject) throws ClassNotFoundException, SecurityException, IllegalAccessException, InvocationTargetException, InstantiationException, JSONException {
        Object obj = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        Field[] var7 = fields;
        int var6 = fields.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            Field field = var7[var5];
            field.setAccessible(true);
            Class typeClazz = field.getType();
            if(typeClazz.isPrimitive()) {
                setProperty(obj, field, jsonObject.opt(field.getName()));
            } else {
                String typeObj = typeClazz.getSimpleName();
                if(!"List".equals(typeObj)) {
                    if("String".equals(typeObj)) {
                        setProperty(obj, field, jsonObject.opt(field.getName()));
                    } else if("Boolean".equals(typeObj)) {
                        setProperty(obj, field, jsonObject.opt(field.getName()));
                    } else if("Long".equals(typeObj)) {
                        setProperty(obj, field, jsonObject.opt(field.getName()));
                    } else if("Integer".equals(typeObj)) {
                        setProperty(obj, field, jsonObject.opt(field.getName()));
                    } else if("Short".equals(typeObj)) {
                        setProperty(obj, field, jsonObject.opt(field.getName()));
                    } else if("Byte".equals(typeObj)) {
                        setProperty(obj, field, jsonObject.opt(field.getName()));
                    } else if("Double".equals(typeObj)) {
                        setProperty(obj, field, jsonObject.opt(field.getName()));
                    } else if(!TextUtils.isEmpty(typeObj)) {
                        setProperty(obj, field, parseJson2Object(typeClazz, jsonObject.getJSONObject(field.getName())));
                    }
                } else {
                    Type type = field.getGenericType();
                    ParameterizedType pt = (ParameterizedType)type;
                    Class dataClass = (Class)pt.getActualTypeArguments()[0];
                    JSONArray jArray = jsonObject.getJSONArray(field.getName());
                    ArrayList list = new ArrayList();

                    for(int i = 0; i < jArray.length(); ++i) {
                        list.add(parseJson2Object(dataClass, jsonObject.getJSONArray(field.getName()).getJSONObject(i)));
                    }

                    setProperty(obj, field, list);
                }
            }
        }
        return (T) obj;
    }

    private static void setProperty(Object obj, Field field, Object valueObj) throws SecurityException, IllegalAccessException, InvocationTargetException {
        try {
            if("serialVersionUID".equals(field.getName())) {
                return;
            }

            Class e = obj.getClass();
            Method method = e.getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase(Locale.getDefault()) + field.getName().substring(1), new Class[]{field.getType()});
            method.setAccessible(true);
            String fieldType = field.getType().toString();
            if(valueObj == null) {
                return;
            }

            if(!"Integer".equals(fieldType.toString()) && !"int".equals(fieldType.toString())) {
                if("Long".equalsIgnoreCase(fieldType)) {
                    if(valueObj != null) {
                        Long temp1 = Long.valueOf(Long.parseLong(valueObj.toString()));
                        method.invoke(obj, new Object[]{temp1});
                    }
                } else if("Short".equalsIgnoreCase(fieldType)) {
                    if(valueObj != null) {
                        Short temp2 = Short.valueOf(Short.parseShort(valueObj.toString()));
                        method.invoke(obj, new Object[]{temp2});
                    }
                } else if("Boolean".equalsIgnoreCase(fieldType)) {
                    if(valueObj != null) {
                        Boolean temp3 = Boolean.valueOf(Boolean.parseBoolean(valueObj.toString()));
                        method.invoke(obj, new Object[]{temp3});
                    }
                } else if("Double".equalsIgnoreCase(fieldType)) {
                    if(valueObj != null) {
                        Double temp4 = Double.valueOf(Double.parseDouble(valueObj.toString()));
                        method.invoke(obj, new Object[]{temp4});
                    }
                } else if("char".equalsIgnoreCase(fieldType)) {
                    if(valueObj != null) {
                        char temp5 = valueObj.toString().charAt(0);
                        method.invoke(obj, new Object[]{Character.valueOf(temp5)});
                    }
                } else {
                    if(valueObj != null && valueObj instanceof Integer) {
                        valueObj = String.valueOf(valueObj);
                    }

                    if(valueObj != null) {
                        method.invoke(obj, new Object[]{valueObj});
                    }
                }
            } else {
                Log.i("---------", "-----------   " + valueObj + "  ---- " + (valueObj instanceof String));
                if(valueObj != null && !valueObj.equals("") && !"null".equalsIgnoreCase(valueObj.toString())) {
                    Integer temp = Integer.valueOf(Integer.parseInt(String.valueOf(valueObj)));
                    method.invoke(obj, new Object[]{temp});
                } else {
                    method.invoke(obj, new Object[]{Integer.valueOf(0)});
                }
            }
        } catch (NoSuchMethodException var7) {
            var7.printStackTrace();
        } catch (IllegalArgumentException var8) {
            var8.printStackTrace();
        }

    }
}

