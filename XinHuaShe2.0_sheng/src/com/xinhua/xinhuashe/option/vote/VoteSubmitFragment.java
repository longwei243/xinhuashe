package com.xinhua.xinhuashe.option.vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.PostChoose;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.domain.VoteSubmitItem;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.vote.adapter.VoteSubmitAdapter;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.Data;

public class VoteSubmitFragment extends ParentFragment implements IActivity {

	private static ViewPager viewPager;
	private VoteSubmitAdapter pagerAdapter;
	public static View viewItem;
	private List<View> viewItems = new ArrayList<View>();
	private ArrayList<VoteSubmitItem> dataItems = new ArrayList<VoteSubmitItem>();
	private static LinearLayout dotContainer;
	private ImageView[] dotImages;
	private LinearLayout.LayoutParams lp;
	public static String name;
	
	private static long titleId;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MobileApplication.allIActivity.add(this);
		name = this.getClass().getName();
		
		threadTask();
		SlidingMenuControlActivity.main_header_title_TextView.setText("投票");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 页面初始化
	 */

	/**
	 * 设置页面低端标识当期页的小方点
	 */
	private void setDotImages() {
		dotContainer.removeAllViews();
		dotImages = new ImageView[viewItems.size()];
		if (lp == null) {
			lp = new LinearLayout.LayoutParams(11, 11);
			lp.setMargins(4, 3, 4, 3);
		}
		for (int i = 0; i < dotImages.length; i++) {
			dotImages[i] = new ImageView(SlidingMenuControlActivity.activity);
			dotImages[i].setScaleType(ScaleType.FIT_XY);
			dotImages[i].setLayoutParams(lp);
			dotImages[i].setBackgroundColor(i == 0 ? Color.BLUE : Color.GRAY);
			dotContainer.addView(dotImages[i], lp);
		}
	}

	@Override
	public void closeLoadingView() {

	}

	@Override
	public void init() {
		if (viewPager.getChildCount() != 0) {
			viewPager.removeAllViews();
		}
		if (!viewItems.isEmpty()) {
			viewItems.clear();
		}
		for (int i = 0; i < dataItems.size(); i++) {
			viewItem = inflater.inflate(R.layout.vote_submit_item, null);
			viewItems.add(viewItem);
		}
		pagerAdapter = new VoteSubmitAdapter(
				SlidingMenuControlActivity.activity, viewItems, dataItems);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (dotImages == null)
					return;
				for (int i = 0; i < dotImages.length; i++) {
					if (dotImages[i] == null)
						return;
					dotImages[i].setBackgroundColor(arg0 == i ? Color.BLUE
							: Color.GRAY);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		setDotImages();

	}

	@Override
	public void refresh(int taskId, Object... params) {
		if (params != null) {
			if (params[0] != null) {
				JSONArray jsonArray;
				switch (taskId) {
				case TaskID.TASK_VOTE:
					dataItems.clear();
					try {
						jsonArray = new JSONArray(params[0].toString());

						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject obj = (JSONObject) jsonArray.get(i);
							VoteSubmitItem vsi = new VoteSubmitItem();
							vsi.setId(obj.getLong("id"));
							vsi.setTitle(obj.getString("title"));
							vsi.setContent(obj.getString("content"));
							vsi.setCreateDate(obj.getString("createDate"));
							vsi.setHits(obj.getInt("hits"));
							JSONArray postChooseArray = obj
									.getJSONArray("postChooseList");
							List<PostChoose> postChooseList = new ArrayList<PostChoose>();
							for (int j = 0; j < postChooseArray.length(); j++) {
								PostChoose pc = new PostChoose();
								JSONObject jb = (JSONObject) postChooseArray
										.get(j);
								pc.setContent(jb.getString("content"));
								pc.setHits(jb.getInt("hits"));
								pc.setId(jb.getLong(("id")));
								pc.setChoose(jb.getString("choose"));
								postChooseList.add(pc);
							}

							vsi.setPostChooseList(postChooseList);
							dataItems.add(vsi);

						}
						init();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case TaskID.TASK_POST_VOTE:
					try {
						JSONObject jb = new JSONObject(params[0].toString());
						Toast.makeText(SlidingMenuControlActivity.activity,
								jb.getString("message"), Toast.LENGTH_SHORT)
								.show();
						if(jb.getString("result").equals("error")){
							MobileApplication.preferences.edit().putString("isVoteMessage"+titleId, UserInfo.userId+titleId).commit();
						}else{
							MobileApplication.preferences.edit().putString("isVoteMessage"+titleId, UserInfo.userId+titleId).commit();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				default:
					break;
				}

			}
		}

	}

	@Override
	protected int getLayoutId() {
		return R.layout.vote_submit_layout;
	}

	@Override
	protected void setupViews(View parentView) {
		dotContainer = (LinearLayout) parentView
				.findViewById(R.id.vote_submit_linear_dot);
		viewPager = (ViewPager) parentView
				.findViewById(R.id.vote_submit_viewpager);
	}

	@Override
	protected void initialized() {

	}

	@Override
	protected void threadTask() {
		Task task = new Task(TaskID.TASK_VOTE, RequestURL.getVote(), this
				.getClass().getName(), "投票线程");
		MobileApplication.poolManager.addTask(task);

	}

	public static void postChoose(long postChooseId, long postTitleId) {
		titleId=postTitleId;
		Map<String, String> map = new HashMap<String, String>();
		map.put("postChooseId", postChooseId + "");
		map.put("userId", UserInfo.userId);
		map.put("postTitleId", postTitleId + "");
		map.put(ParentHandlerService.URL, RequestURL.postVote());
		Task task = new Task(TaskID.TASK_POST_VOTE, map, name, "返回投票结果");
		MobileApplication.poolManager.addTask(task);
	}
}
