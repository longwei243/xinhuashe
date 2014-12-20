package com.xinhua.xinhuashe.option;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.lidroid.xutils.BitmapUtils;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.view.slidingmenu.adapter.SlidingMenuGridViewAdapter;

/**
 * 右侧菜单控制类
 * 
 * 
 */
public class RightMenuFragment extends ParentFragment implements IActivity {

	private Callbacks callbacks = defaultCallbacks;
	private View weather_location_LinearLayout;
	private TextView weather_state_TextView, weather_temperature_TextView,
			weather_wind_TextView;
	private GridView rightmenu_GridView;
	private List<Map<String, Object>> data;
	private SlidingMenuGridViewAdapter adapter;
	private String result;
	private ImageView QRCode_show_ImageView;
	private BitmapUtils bitmapUtils;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		MobileApplication.allIActivity.add(this);
		threadTask();
		return contextView;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			callbacks.onRightMenuClick(v, "");
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		callbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callbacks = defaultCallbacks;
	}

	private static Callbacks defaultCallbacks = new Callbacks() {

		@Override
		public void onRightMenuClick(View view, Object data) {

		}

	};

	public interface Callbacks {

		/**
		 * CommonMenu列表项点击事件回掉接口
		 * 
		 * @param adapterView
		 * @param view
		 * @param i
		 */
		public void onRightMenuClick(View view, Object data);

	}

	@Override
	protected int getLayoutId() {
		return R.layout.rightmenu;
	}

	@Override
	protected void setupViews(View parentView) {
		weather_location_LinearLayout = parentView
				.findViewById(R.id.weather_location_LinearLayout);
		weather_state_TextView = (TextView) parentView
				.findViewById(R.id.weather_state_TextView);
		weather_temperature_TextView = (TextView) parentView
				.findViewById(R.id.weather_temperature_TextView);
		weather_wind_TextView = (TextView) parentView
				.findViewById(R.id.weather_wind_TextView);
		rightmenu_GridView = (GridView) parentView
				.findViewById(R.id.rightmenu_GridView);
		weather_location_LinearLayout.setOnClickListener(clickListener);

		QRCode_show_ImageView = (ImageView) parentView
				.findViewById(R.id.QRCode_show_ImageView);
		QRCode_show_ImageView.setOnClickListener(onClickListener);
		bitmapUtils = new BitmapUtils(MobileApplication.mobileApplication);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.QRCode_show_ImageView:
				if ("".equals(UserInfo.userId)) {
					Toast.makeText(SlidingMenuControlActivity.activity,
							R.string.nologin, Toast.LENGTH_SHORT).show();
					return;
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put(ParentHandlerService.URL, RequestURL.getQRCode());
				map.put("frontUserId", UserInfo.userId);
				Task task = new Task(TaskID.TASK_GETQRCODE, map, this
						.getClass().getName(), "-获取二维码-");
				MobileApplication.poolManager.addTask(task);
				break;
			}

		}
	};

	@SuppressLint("Recycle")
	@Override
	protected void initialized() {
		data = new ArrayList<Map<String, Object>>();
		String[] lifeTitles = getResources().getStringArray(
				R.array.weather_life_title_list);
		TypedArray icons = getResources().obtainTypedArray(
				R.array.weather_life_icon_list);
		for (int i = 0; i < lifeTitles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", icons.getDrawable(i));
			map.put("state", "");
			map.put("title1", lifeTitles[i]);
			map.put("title2", "");
			data.add(map);
		}
		adapter = new SlidingMenuGridViewAdapter(
				SlidingMenuControlActivity.activity, data,
				R.layout.weather_life_item, new String[] { "icon", "state",
						"title1", "title2" }, new int[] {
						R.id.weather_item_image_ImageView,
						R.id.weather_state_tv,
						R.id.weather_item_text_1_TextView,
						R.id.weather_item_text_2_TextView });
		rightmenu_GridView.setAdapter(adapter);
	}

	@Override
	protected void threadTask() {
		// MobileApplication.cacheUtils.getAsString("areaCode")
		Task task = new Task(TaskID.TASK_WEATHER_INFO,
				RequestURL.getWeatherInfo(RequestURL.default_shi, "0"),
				this.getClass().getName(), "-获取天气信息-");
		MobileApplication.poolManager.addTask(task);
	}

	private String readXMLString(String xmlStr, String node) {
		String result = "";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			InputStream inputStream = new ByteArrayInputStream(
					xmlStr.getBytes());
			Document doc = builder.parse(inputStream);
			// 下面开始读取
			Element root = doc.getDocumentElement(); // 获取根元素
			NodeList items = root.getElementsByTagName(node);
			Element element = (Element) items.item(0);
			Text text = (Text) element.getFirstChild();
			result = text.getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void closeLoadingView() {

	}

	@Override
	public void init() {
		weather_state_TextView.setText(readXMLString(result, "status1"));
		weather_temperature_TextView.setText(readXMLString(result,
				"temperature1")
				+ getResources().getString(R.string.weather_temperature)
				+ getResources().getString(R.string.weather_separator)
				+ readXMLString(result, "temperature2")
				+ getResources().getString(R.string.weather_temperature));
		weather_wind_TextView.setText(readXMLString(result, "direction1")
				+ readXMLString(result, "power1")
				+ getResources().getString(R.string.weather_power));
		String[] lifeInfo = { readXMLString(result, "chy_l"),
				readXMLString(result, "gm_l"), readXMLString(result, "yd_l"),
				readXMLString(result, "zwx_l") };
		for (int i = 0; i < lifeInfo.length; i++) {
			Map<String, Object> map = data.get(i);

			map.put("title2", lifeInfo[i]);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void refresh(int taskId, Object... params) {
		if (params != null) {
			if (params[0] != null) {
				switch (taskId) {
				case TaskID.TASK_WEATHER_INFO:
					result = (String) params[0];
					init();
					break;
				case TaskID.TASK_GETQRCODE:
					try {
						JSONObject jsonObject = (JSONObject) params[0];
						result = jsonObject.getString("message");
						bitmapUtils.display(QRCode_show_ImageView,
								RequestURL.http + result);
						Log.i(MobileApplication.TAG, result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}

			}
		}
	}

}
