/**
 * 功能： 对mainActivity中的数据进行解析处理
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-3-7       下午05:39:59
 * Copyright (c) 2012, TNT All Rights Reserved.
*/

package nf.framework.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class JSONDataUtils {

	
	/**
	 * 解析json数据
	 * @param str
	 * @param param
	 * @return 返回JSONObject类型
	 */
	public static JSONObject JSONDataToJSONObject(String str,String param){
		JSONObject json=null;
		if(json==null){
			return null;
		}
		try {
			JSONObject jsonStr=new JSONObject(str);
			if(jsonStr.has(param)){
			json=new JSONObject(String.valueOf(jsonStr.get(param)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * 解析json数据
	 * @param str
	 * @param param
	 * @return 返回String类型
	 */
	public static String JSONDataToString(String str,String param){
		String json=null;
		if(str!=null&&str.length()!=0){
			try {
				JSONObject jsonStr=new JSONObject(str);
				if(jsonStr.has(param)){
					json=String.valueOf(jsonStr.get(param));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}
/**
 * 将String转成JSONArray
 * @param json
 * @return
 */
	public static JSONArray StringToJSONArray(String json){
		JSONArray jsonArray=null;
		if(json==null){
			return null;
		}
		try {
			 jsonArray=new JSONArray(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArray;
	}
	/**
     * 将map数据解析出来，并拼接成json字符串
     * 
     * @param map
     * @return
     */
    public static JSONObject setJosn(Map map) throws Exception {
            JSONObject json = null;
            StringBuffer temp = new StringBuffer();
            if (map!=null&&!map.isEmpty()) {
                    temp.append("{");
                    // 遍历map
                    Set set = map.entrySet();
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                            Map.Entry entry = (Map.Entry) i.next();
                            String key = (String) entry.getKey();
                            Object value = entry.getValue();
                            temp.append("\"" + key + "\":");
                            if (value instanceof Map<?, ?>) {
                                    temp.append(setJosn((Map<String, Object>) value) + ",");
                            } else if (value instanceof List<?>) {
                                    temp.append(setList((List<Map<String, Object>>) value)
                                                    + ",");
                            } else {
                    			 temp.append("\"" + value + "\"" + ",");
                            }
                    }
                    if (temp.length() > 1) {
                            temp = new StringBuffer(temp.substring(0, temp.length() - 1));
                    }
                    temp.append("}");
                    json = new JSONObject(temp.toString());
            }
            return json;
    }

    /**
     * 将单个list转成json字符串
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public static String setList(List<Map<String, Object>> list)
                    throws Exception {
            String jsonL = "";
            StringBuffer temp = new StringBuffer();
            temp.append("[");
            for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> m = list.get(i);
                    if (i == list.size() - 1) {
                            temp.append(setJosn(m));
                    } else {
                            temp.append(setJosn(m) + ",");
                    }
            }
            if (temp.length() > 1) {
                    temp = new StringBuffer(temp.substring(0, temp.length()));
            }
            temp.append("]");
            jsonL = temp.toString();
            return jsonL;
    }
	/** 
     * Json 转成 Map<> 
     * @param jsonStr 
     * @return 
     */  
    public static Map<String, String> getMapForJson(String jsonStr){  
        JSONObject jsonObject ;  
        try {  
            jsonObject = new JSONObject(jsonStr);  
              
            Iterator<String> keyIter= jsonObject.keys();  
            String key;  
            Object value ;  
            Map<String, String> valueMap = new HashMap<String, String>();  
            while (keyIter.hasNext()) {  
                key = keyIter.next();  
                value = jsonObject.get(key);  
                valueMap.put(key, String.valueOf(value));  
            }  
            return valueMap;  
        } catch (Exception e) {  
            // TODO: handle exception  
            e.printStackTrace();  
        }  
        return null;  
    }  
    /**
     * 将整个json字符串解析，并放置到map<String,Object>中
     * 
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getJosn(String jsonStr) throws Exception {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!TextUtils.isEmpty(jsonStr)) {
                    JSONObject json = new JSONObject(jsonStr);
                    Iterator i = json.keys();
                    while (i.hasNext()) {
                            String key = (String) i.next();
                            String value = json.getString(key);
                            if (value.indexOf("{") == 0) {
                                    map.put(key.trim(), getJosn(value));
                            } else if (value.indexOf("[") == 0) {
                                    map.put(key.trim(), getList(value));
                            } else {
                                    map.put(key.trim(), value.trim());
                            }
                    }
            }
            return map;
    }

    /**
     * 将单个json数组字符串解析放在list中
     * 
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getList(String jsonStr)
                    throws Exception {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            JSONArray ja = new JSONArray(jsonStr);
            for (int j = 0; j < ja.length(); j++) {
                    String jm = ja.get(j) + "";
                    if (jm.indexOf("{") == 0) {
                            Map<String, Object> m = getJosn(jm);
                            list.add(m);
                    }
            }
            return list;
    }

    public static Map ConvertObjToMap(Object obj){
    	  Map<String,Object> reMap = new HashMap<String,Object>();
    	  if (obj == null) 
    	   return null;
    	  Field[] fields = obj.getClass().getDeclaredFields();
    	  try {
    	   for(int i=0;i<fields.length;i++){
    	    try {
    	     Field f = obj.getClass().getDeclaredField(fields[i].getName());
    	     f.setAccessible(true);
    	           Object o = f.get(obj);
    	           if(o != null )
    	           reMap.put(fields[i].getName(), o);
    	    } catch (NoSuchFieldException e) {
    	     // TODO Auto-generated catch block
    	     e.printStackTrace();
    	    } catch (IllegalArgumentException e) {
    	     // TODO Auto-generated catch block
    	     e.printStackTrace();
    	    } catch (IllegalAccessException e) {
    	     // TODO Auto-generated catch block
    	     e.printStackTrace();
    	    }
    	   }
    	  } catch (SecurityException e) {
    	   // TODO Auto-generated catch block
    	   e.printStackTrace();
    	  } 
    	  return reMap;
    	 }
    	 
}
