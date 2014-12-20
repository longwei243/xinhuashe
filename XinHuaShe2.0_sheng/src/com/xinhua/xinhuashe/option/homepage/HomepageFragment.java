package com.xinhua.xinhuashe.option.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.china.ChinaFragment;
import com.xinhua.xinhuashe.option.homepage.adapter.GalleryAdapter;
import com.xinhua.xinhuashe.option.huiminshenghuo.HuiMinShengHuoFragment;
import com.xinhua.xinhuashe.option.news.NewsDetailFragment;
import com.xinhua.xinhuashe.option.news.NewsItemFragment;
import com.xinhua.xinhuashe.option.news.YaoWenNewsItemFragment;
import com.xinhua.xinhuashe.option.say.SayFragment;
import com.xinhua.xinhuashe.option.xinwendongtai.XinWenDongTaiFragment;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.YuQingKuaiBaiFragment;
import com.xinhua.xinhuashe.option.zhangshangzhenwu.ZhangShangZhenWuFragment;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.Data;
import com.xinhua.xinhuashe.util.IPOSUtils;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;
import com.xinhua.xinhuashe.view.FlowIndicator;
import com.xinhua.xinhuashe.view.ListViewInScrollView;

/**
 * 首页
 * 
 * 
 */
public class HomepageFragment extends ParentFragment implements IActivity {

	private ScrollView homepage_ScrollView;
	@SuppressWarnings("deprecation")
	private Gallery homepage_header_Gallery;
	private FlowIndicator homepage_header_FlowIndicator;
	private GalleryAdapter galleryAdapter;
	private List<Map<String, String>> galleryData, listViewData1, listViewData2;
	private TextView homepage_header_TextView;
	
	private ViewPager viewPager;//页卡内容  
    private ImageView imageView;// 动画图片  
    private TextView textView1,textView2;  
    private List<View> views;// Tab页面列表  
    private int offset = 0;// 动画图片偏移量  
    private int currIndex = 0;// 当前页卡编号  
    private int bmpW;// 动画图片宽度  
    private View view1,view2;//各个页卡  
    
    private ListView listview1;
	private SimpleAdapter listViewAdapter1;
	private ListView listview2;
	private SimpleAdapter listViewAdapter2;
    
	RelativeLayout home_image_zhong;
    RelativeLayout home_layout_xinwendongtai, home_layout_zhangshangzhengwu,
    				home_layout_huimingshenghuo, home_layout_hudongfenxiang;
    
    RelativeLayout home_image_china;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		View view = SlidingMenuControlActivity.activity
				.getMain_header_right_ImageView();
		SlidingMenuControlActivity.activity.setHeaderRightView(view);
		RequestURL.xianarea = RequestURL.area;
		RequestURL.xianCode = RequestURL.areaCode;

		RequestURL.shi = RequestURL.default_shi;
		RequestURL.shiCode = RequestURL.default_shiCode;
		
		InitImageView(contextView);  
        InitTextView(contextView);  
        InitViewPager(contextView); 
        
        threadTask();
		return contextView;
	}
	
	private class MyOnClickListener implements OnClickListener{  
        private int index=0;  
        public MyOnClickListener(int i){  
            index=i;  
        }  
        public void onClick(View v) {  
            viewPager.setCurrentItem(index);              
        }  
          
    }  
	public class MyViewPagerAdapter extends PagerAdapter{  
        private List<View> mListViews;  
          
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;  
        }  
  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));  
        }  
  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {            
             container.addView(mListViews.get(position), 0);  
             return mListViews.get(position);  
        }  
  
        @Override  
        public int getCount() {           
            return  mListViews.size();  
        }  
          
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1;  
        }  
    }  
	public class MyOnPageChangeListener implements OnPageChangeListener{  
		  
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量  
        int two = one * 2;// 页卡1 -> 页卡3 偏移量  
        public void onPageScrollStateChanged(int arg0) {  
              
              
        }  
  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
              
              
        }  
  
        public void onPageSelected(int arg0) {  
           
            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//显然这个比较简洁，只有一行代码。  
            currIndex = arg0;  
            animation.setFillAfter(true);// True:图片停在动画结束位置  
            animation.setDuration(300);  
            imageView.startAnimation(animation);  
//            Toast.makeText(getActivity(), "您选择了"+ viewPager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();  
        }  
	}
        
    private void InitViewPager(View root) {  
        viewPager=(ViewPager) root.findViewById(R.id.vPager);  
        views=new ArrayList<View>();  
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        view1=inflater.inflate(R.layout.lay1, null);  
        view2=inflater.inflate(R.layout.lay2, null);  
        views.add(view1);  
        views.add(view2);  
        viewPager.setAdapter(new MyViewPagerAdapter(views));  
        viewPager.setCurrentItem(0);  
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener()); 
        
        
        
        listViewData1 = new ArrayList<Map<String, String>>();
		listview1 = (ListViewInScrollView)view1.findViewById(R.id.homepage_viewpager_ListView1);
        listViewAdapter1 = new SimpleAdapter(SlidingMenuControlActivity.activity, listViewData1, R.layout.homepage_listview_item, new String[] { "title" },
				new int[] { R.id.homepage_listview_item_TextView });
        listview1.setAdapter(listViewAdapter1);
        listViewAdapter1.notifyDataSetChanged();
        
        
        
        listViewData2 = new ArrayList<Map<String, String>>();
        listview2 = (ListViewInScrollView)view2.findViewById(R.id.homepage_viewpager_ListView2);
        listViewAdapter2 = new SimpleAdapter(SlidingMenuControlActivity.activity, listViewData2, R.layout.homepage_listview_item, new String[] { "title" },
        		new int[] { R.id.homepage_listview_item_TextView });
        listview2.setAdapter(listViewAdapter2);
        listViewAdapter2.notifyDataSetChanged();
        
//        lay1_more = (RelativeLayout) view1.findViewById(R.id.lay1_more);
//        lay1_more.setOnClickListener(clickListener);
//        lay2_more = (RelativeLayout) view2.findViewById(R.id.lay2_more);
//        lay2_more.setOnClickListener(clickListener);
    }  
     /** 
      *  初始化头标 
      */  
  
    private void InitTextView(View root) {  
        textView1 = (TextView) root.findViewById(R.id.text1);  
        textView2 = (TextView) root.findViewById(R.id.text2);  
  
        textView1.setOnClickListener(clickListener);  
        textView2.setOnClickListener(clickListener);  
    }  
  
    /** 
     2      * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据 
     3 */  
  
    private void InitImageView(View root) {  
        imageView= (ImageView) root.findViewById(R.id.cursor);  
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度  
        DisplayMetrics dm = new DisplayMetrics();  
        SlidingMenuControlActivity.activity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;// 获取分辨率宽度  
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量  
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
        imageView.setImageMatrix(matrix);// 设置动画初始位置  
    }  
	
    private void initListView1(JSONArray jsonArray) {
		int length = jsonArray.length();
		if (length > 3) {
			length = 3;
		}
		for (int i = 0; i < length; i++) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", Html.fromHtml(jsonObject.getString("title")).toString());
				map.put("id", jsonObject.getString("id"));
				listViewData1.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		listViewAdapter1.notifyDataSetChanged();
		listview1.setOnItemClickListener(itemClickListener);
	}
    private void initListView2(JSONArray jsonArray) {
    	int length = jsonArray.length();
    	if (length > 3) {
    		length = 3;
    	}
    	for (int i = 0; i < length; i++) {
    		try {
    			JSONObject jsonObject = jsonArray.getJSONObject(i);
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("title", Html.fromHtml(jsonObject.getString("title")).toString());
    			map.put("id", jsonObject.getString("id"));
    			listViewData2.add(map);
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	}
    	listViewAdapter2.notifyDataSetChanged();
    	listview2.setOnItemClickListener(itemClickListener);
    }
	

	@Override
	protected int getLayoutId() {
		return R.layout.homepage;
	}

	@Override
	protected void setupViews(View parentView) {
		homepage_ScrollView = (ScrollView) parentView
				.findViewById(R.id.homepage_ScrollView);
		homepage_header_Gallery = (Gallery) parentView
				.findViewById(R.id.homepage_header_Gallery);
		homepage_header_TextView = (TextView) parentView
				.findViewById(R.id.homepage_header_TextView);
		homepage_header_FlowIndicator = (FlowIndicator) parentView
				.findViewById(R.id.homepage_header_FlowIndicator);
		
		home_image_zhong = (RelativeLayout) parentView.findViewById(R.id.home_image_zhong);
		home_image_zhong.setOnClickListener(clickListener);
		
		home_layout_xinwendongtai = (RelativeLayout) parentView.findViewById(R.id.home_layout_xinwendongtai);
		home_layout_xinwendongtai.setOnClickListener(clickListener);
		home_layout_zhangshangzhengwu = (RelativeLayout) parentView.findViewById(R.id.home_layout_zhangshangzhengwu);
		home_layout_zhangshangzhengwu.setOnClickListener(clickListener);
		home_layout_huimingshenghuo = (RelativeLayout) parentView.findViewById(R.id.home_layout_huimingshenghuo);
		home_layout_huimingshenghuo.setOnClickListener(clickListener);
		home_layout_hudongfenxiang = (RelativeLayout) parentView.findViewById(R.id.home_layout_hudongfenxiang);
		home_layout_hudongfenxiang.setOnClickListener(clickListener);
		
		home_image_china = (RelativeLayout) parentView.findViewById(R.id.home_image_china);
		home_image_china.setOnClickListener(clickListener);
	}

	@SuppressLint("Recycle")
	@Override
	protected void initialized() {
		SlidingMenuControlActivity.main_header_title_TextView.setText(R.string.app_name);
		galleryData = new ArrayList<Map<String, String>>();
		Map<String, String> maptemp = new HashMap<String, String>();
		maptemp.put("image", "assets/img/default_big.png");
		maptemp.put("title", "");
		maptemp.put("id", "");
		galleryData.add(maptemp);
		galleryAdapter = new GalleryAdapter(SlidingMenuControlActivity.activity, galleryData);
		homepage_header_Gallery.setAdapter(galleryAdapter);
		homepage_header_FlowIndicator.setCount(1);	
	}

	@Override
	protected void threadTask() {
		if(listViewData1.isEmpty()){
			Task task = new Task(TaskID.TASK_HOMEPAGE_NEWS1,
					RequestURL.getHomePageNews1(), this.getClass().getName(),
					"-本地新闻-");
			MobileApplication.poolManager.addTask(task);
			
		}
		
		if(listViewData2.isEmpty()) {
			Task tasknews2 = new Task(TaskID.TASK_HOMEPAGE_NEWS2,
					RequestURL.getHomePageNews2(), this.getClass().getName(),
					"-要闻推荐-");
			MobileApplication.poolManager.addTask(tasknews2);
			
		}
		
		Task taskPic = new Task(TaskID.TASK_HOMEPAGE_HOT_PIC,
				RequestURL.getHomePageHotPic(), this.getClass().getName(),
				"-首页热图-");
		System.out.println("开始首页热图任务了。。。。。。");
		MobileApplication.poolManager.addTask(taskPic);
	}


	private OnClickListener clickListener = new OnClickListener() {

		

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.home_image_zhong:
				if (UserInfo.userId.equals("")) {
					Toast.makeText(SlidingMenuControlActivity.activity, R.string.nologin, Toast.LENGTH_SHORT).show();
					return;
				}
				ParentFragment fragment_yuqingkuaibao = new YuQingKuaiBaiFragment();
				switchFragment(fragment_yuqingkuaibao, fragment_yuqingkuaibao.getClass().getSimpleName(), params);

				break;
			case R.id.home_layout_xinwendongtai:
				ParentFragment fragment_xwdt = new XinWenDongTaiFragment();
				switchFragment(fragment_xwdt, fragment_xwdt.getClass().getSimpleName(), params);
				break;
			case R.id.home_layout_zhangshangzhengwu:
				ParentFragment fragment_zszw = new ZhangShangZhenWuFragment();
				switchFragment(fragment_zszw, fragment_zszw.getClass().getSimpleName(), params);
				break;
			case R.id.home_layout_huimingshenghuo:
				ParentFragment fragment_hmsh = new HuiMinShengHuoFragment();
				switchFragment(fragment_hmsh, fragment_hmsh.getClass().getSimpleName(), params);
				break;
			case R.id.home_layout_hudongfenxiang:
				ParentFragment fragment_say=new SayFragment();
				switchFragment(fragment_say, fragment_say.getClass().getSimpleName(), params);
				
				break;
			case R.id.text1:
				ParentFragment fragment_lay1=new NewsItemFragment();
				ViewPagerItemInfo pageInfo = new ViewPagerItemInfo(
						"本地要闻", "8", 0);
				pageInfo.setModule("article");
				switchFragment(fragment_lay1, fragment_lay1.getClass().getSimpleName(), pageInfo);
				SlidingMenuControlActivity.main_header_title_TextView.setText("本地要闻");
				break;
			case R.id.text2:
				ParentFragment fragment_lay2=new YaoWenNewsItemFragment();
				ViewPagerItemInfo pageInfo2 = new ViewPagerItemInfo(
						"要闻推荐", "25", 0);
				pageInfo2.setModule("article");
				switchFragment(fragment_lay2, fragment_lay2.getClass().getSimpleName(), pageInfo2);
				SlidingMenuControlActivity.main_header_title_TextView.setText("要闻推荐");
				break;
			case R.id.home_image_china:
				ParentFragment fragment = new ChinaFragment();
				switchFragment(fragment, fragment.getClass()
						.getSimpleName(), params);
				break;
			default:
				break;
			}
			//用户行为  查看新闻详情
			Map<String, String> map =UserBehaviorInfo.sendUserOpenAppInfo();
			map.put("operateType", "004");
			map.put("operateObjID", "25");
			map.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
			Task task = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map, this
					.getClass().getName(), "用户点击稿件行为");
			MobileApplication.poolManager.addTask(task);
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long l) {
			ParentFragment fragment = null;
			switch (adapterView.getId()) {
			case R.id.homepage_header_Gallery:
				
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) ((GalleryAdapter) adapterView
						.getAdapter()).getItem(position);
				fragment = new NewsDetailFragment();
				switchFragment(fragment, fragment.getClass()
						.getSimpleName(), map.get("id"), map.get("title"));
				
				break;
			case R.id.homepage_viewpager_ListView1:
				@SuppressWarnings("unchecked")
				Map<String, String> map1 = (Map<String, String>) ((SimpleAdapter) adapterView
						.getAdapter()).getItem(position);
				fragment = new NewsDetailFragment();
				switchFragment(fragment, fragment.getClass().getSimpleName(),
						map1.get("id"), map1.get("title"));
				break;
			case R.id.homepage_viewpager_ListView2:
				@SuppressWarnings("unchecked")
				Map<String, String> map2 = (Map<String, String>) ((SimpleAdapter) adapterView
						.getAdapter()).getItem(position);
				fragment = new NewsDetailFragment();
				switchFragment(fragment, fragment.getClass().getSimpleName(),
						map2.get("id"), map2.get("title"));
				break;
			
			default:
				break;
			}
		}
	};
	private RelativeLayout lay1_more;
	private RelativeLayout lay2_more;
	

	private void initGallery(JSONArray jsonArray) {
		if (jsonArray.length() > 0) {
			galleryData.clear();
		}
		homepage_header_FlowIndicator.setCount(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String image = "assets/img/defaultpic_big.png";
				String title = "";
				String id = "";
				if (jsonObject.has("image")) {
					image = jsonObject.getString("image");
				}
				if (jsonObject.has("title")) {
					title = jsonObject.getString("title");
				}
				if (jsonObject.has("id")) {
					id = jsonObject.getString("id");
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("image", image);
				map.put("title", title);
				map.put("id", id);
				galleryData.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		homepage_header_TextView.setText(Html.fromHtml(galleryData.get(0).get(
				"title")));
		galleryAdapter.notifyDataSetChanged();
		homepage_header_Gallery
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapterView,
							View view, int position, long l) {
						@SuppressWarnings("unchecked")
						Map<String, String> map = (Map<String, String>) galleryAdapter
								.getItem(position);
						homepage_header_TextView.setText(Html.fromHtml(map
								.get("title")));
						homepage_header_FlowIndicator.setSeletion(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> adapterView) {

					}
				});
		homepage_header_Gallery.setOnItemClickListener(itemClickListener);
		homepage_ScrollView.scrollTo(0, 0);
	}

	@Override
	public void closeLoadingView() {

	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(int taskId, Object... params) {
		
		
		if (params != null) {
			if (params[0] != null) {
				JSONArray jsonArray;
				
				switch (taskId) {
				
				case TaskID.TASK_HOMEPAGE_HOT_PIC:
					jsonArray = (JSONArray) params[0];
					initGallery(jsonArray);
					break;
				case TaskID.TASK_HOMEPAGE_NEWS1:
					listViewData1.clear();
					jsonArray = (JSONArray) params[0];
					initListView1(jsonArray);
					break;
				case TaskID.TASK_HOMEPAGE_NEWS2:
					listViewData2.clear();
					jsonArray = (JSONArray) params[0];
					initListView2(jsonArray);
					break;
				default:
					break;
				}
				
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			loadingDialog.showTimeOut();
		}
	}
	@Override
	public void onDestroy() {
//		iposUtils.onDestroy();
		super.onDestroy();
	}

}


