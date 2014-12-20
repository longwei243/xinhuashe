package com.xinhua.xinhuashe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

public class FileUtils {

	/**
	 * author Soul time 2012-2-9 describe SD文件读写操作
	 * 
	 * uses-permission SDCard中创建与删除文件权限 <uses-permission
	 * android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
	 * SDCard写入数据权限 <uses-permission
	 * android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	 * 
	 */

	public static final File SDpath = Environment.getExternalStorageDirectory();
	public static final String TAG = "FileUtils_CreateDocuments";

	/**
	 * 在SD卡中创建文件夹
	 * 
	 * @param path
	 *            文件夹路径
	 * @return 是否创建成功
	 */
	public static boolean createDocument(String path) {
		File file = new File(SDpath + File.separator + path);
		return file.mkdir();
	}

	/**
	 * 在SD卡中创建多级文件夹
	 * 
	 * @param path
	 *            文件夹路径，例android/data/data
	 * @return 是否创建成功
	 */
	public static boolean createMoreDocuments(String path) {
		boolean bool = false;
		String[] newPath = path.split(File.separator);
		String createPath = SDpath.toString();
		for (int i = 0; i < newPath.length; i++) {
			createPath = createPath + File.separator + newPath[i];
			File file = new File(createPath);
			bool = file.mkdirs();
		}
		return bool;
	}

	/**
	 * 在SD中创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 该文件路径
	 */
	public static File createFile(String path) {
		File file = new File(path);
		try {
			if (file.createNewFile()) {
				return file;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param filepath
	 *            文件路径
	 * @return 是否成功
	 */
	public static boolean deleteFile(String filepath) {
		File file = new File(filepath);
		return file.delete();
	}

	/**
	 * 删除文件或文件夹
	 * @param file
	 */
	public static void deleteFiles(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFiles(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		}
	}

	/**
	 * 判断该文件夹或文件是否存在
	 * 
	 * @param path
	 *            路径
	 * @return
	 */
	public static boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 在SD卡中保存数据
	 * 
	 * @param path
	 *            文件夹路径
	 * @param fileName
	 *            文件名
	 * @param data
	 *            数据
	 * @return 是否保存
	 */
	public static boolean saveFile(String path, String fileName, byte[] data) {
		boolean bool = path.split(File.separator).length > 2 ? createDocument(path)
				: createMoreDocuments(path);
		Log.i(TAG, String.valueOf(bool));
		// 判断该文件是否存在
		if (!isFileExist(path + File.separator + fileName)) {
			File file = createFile(path + File.separator + fileName);
			try {
				if (file != null) {
					FileOutputStream out = new FileOutputStream(file);
					out.write(data);
					out.flush();
					out.close();
					System.out.println("保存文件true");
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			System.out.println("文件已经存在");
		}
		return true;
	}

	/**
	 * 读取SD卡中文件
	 * 
	 * @param path
	 *            文件路径
	 * @return byte数据
	 * @throws Exception
	 */
	public static byte[] readFile(String path) throws Exception {
		if (isFileExist(path)) {
			File file = new File(path);
			FileInputStream input = new FileInputStream(file);
			byte[] bt = new byte[(int) file.length()];
			input.read(bt);
			input.close();
			return bt;
		}
		return null;
	}

	/**
	 * 读取SD卡中文件
	 * 
	 * @param path
	 *            文件路径
	 * @return byte数据
	 * @throws Exception
	 */
	public static String readFileAsStr(String path) throws Exception {
		return new String(readFile(path));
	}

	/**
	 * 读取SD卡中所有文件夹
	 * 
	 * @param path
	 *            路径
	 * @return 文件信息
	 */
	public static List<String> readDocument(String path) {
		File file = null;
		if (path != null) {
			file = new File(SDpath + File.separator + path);
		} else {
			file = new File(SDpath.toString());
		}
		File[] str = file.listFiles();
		List<String> list = new ArrayList<String>();
		if (str == null) {
			return null;
		} else {
			for (int i = 0; i < str.length; i++) {
				if (!str[i].isFile()) {
					list.add(str[i].getName());
				}
			}
		}
		return list;
	}

	/**
	 * 读取SD卡中所有文件名称
	 * 
	 * @param path
	 *            文件路径，默认为SD卡根目录
	 * @return 反回文件列表
	 */
	public static List<String> readAllFileName(String path) {
		File file = null;
		if (path != null) {
			file = new File(SDpath + File.separator + path);
		} else {
			file = new File(SDpath.toString());
		}
		File[] str = file.listFiles();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < str.length; i++) {
			if (str[i].isFile()) {
				list.add(str[i].getName());
			}
		}
		return list;
	}

	/**
	 * 读取SD卡中所有文件
	 * 
	 * @param path
	 *            文件路径，默认为SD卡根目录
	 * @return 反回文件列表
	 */
	public static List<File> readAllFile(String path) {
		File file = null;
		if (path != null) {
			file = new File(SDpath + File.separator + path);
		} else {
			file = new File(SDpath.toString());
		}
		File[] str = file.listFiles();
		List<File> list = new ArrayList<File>();
		for (int i = 0; i < str.length; i++) {
			if (str[i].isFile()) {
				list.add(str[i]);
			}
		}
		return list;
	}

	/**
	 * 判断SD卡是否存在，并可执行读写
	 * 
	 * @return
	 */
	public static boolean isSDcardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * Java逐行读取文本文件
	 * 
	 * @param filePath
	 * @throws IOException
	 * 
	 */
	public static final void readF1(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			System.out.println(line);
		}
		br.close();
	}

	/**
	 * Java逐行读取文本文件
	 * 
	 * @param filePath
	 * @throws IOException
	 * 
	 */
	public static final void readF2(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader bufferedreader = new BufferedReader(fr);
		String instring;
		while ((instring = bufferedreader.readLine().trim()) != null) {
			if (0 != instring.length()) {
				System.out.println(instring);
			}
		}
		fr.close();
	}

}
