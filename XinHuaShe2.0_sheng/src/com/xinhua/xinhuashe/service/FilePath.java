package com.xinhua.xinhuashe.service;

import android.os.Environment;

/**
 * 文件路径
 *
 * @author azuryleaves
 * @since 2014-3-24 下午3:17:15
 * @version 1.0
 *
 */
public class FilePath {
	
	public static String SDPath = Environment.getExternalStorageDirectory()
			.getPath();
	
	public static String RootPath = SDPath + MobileApplication.filepath_properties.getProperty("root");
	public static String LogPath = SDPath + MobileApplication.filepath_properties.getProperty("log");
	public static String UpdatePath = SDPath + MobileApplication.filepath_properties.getProperty("update");
	public static String ConfigPath = SDPath + MobileApplication.filepath_properties.getProperty("config");
	public static String CachePath = SDPath + MobileApplication.filepath_properties.getProperty("cache");
	public static String CachePicPath = SDPath + MobileApplication.filepath_properties.getProperty("cache_pic");
	public static String CacheDataPath = SDPath + MobileApplication.filepath_properties.getProperty("cache_data");
	
}
