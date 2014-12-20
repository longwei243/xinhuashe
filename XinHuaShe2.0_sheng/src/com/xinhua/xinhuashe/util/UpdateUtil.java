package com.xinhua.xinhuashe.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版本更新工具
 * 
 * @author azuryleaves
 * @since 2014-1-16 下午4:06:52
 * @version 1.0
 * 
 */
public class UpdateUtil {
	
	public final static int HasUpdate = 0;
	public final static int LatestUpdate = 1;
	public final static int ProgessBar_Visible = 10;
	public final static int ProgessBar_Max = 11;
	public final static int ProgessBar_Progress = 12;
	public final static int DownLoad_Finish = 13;
	public final static int DownLoad_Error = -1;

	public static boolean hasUpdate(int serverCode, int localCode) {
		return serverCode > localCode ? true : false;
	}

	/**
	 * 解析服务器版本信息
	 * 
	 * @param result
	 * @param map
	 * @return
	 */
	public static void getServerAPKInfo(String result, Map<String, String> map) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			Iterator<Entry<String, String>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) iterator
						.next();
				map.put(entry.getKey(), jsonObject.getString(entry.getKey()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
