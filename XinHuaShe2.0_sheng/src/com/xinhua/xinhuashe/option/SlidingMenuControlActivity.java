package com.xinhua.xinhuashe.option;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.view.slidingmenu.SlidingFragmentActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.domain.ViewPagerItemInfo;
import com.xinhua.xinhuashe.option.about.AboutFeedbackFragment;
import com.xinhua.xinhuashe.option.about.AboutFragment;
import com.xinhua.xinhuashe.option.develop.DevelopingFragment;
import com.xinhua.xinhuashe.option.homepage.HomepageFragment;
import com.xinhua.xinhuashe.option.loding.service.LoginService;
import com.xinhua.xinhuashe.option.login.LoginFragment;
import com.xinhua.xinhuashe.option.login.MydataFragment;
import com.xinhua.xinhuashe.option.news.MyAttentionFragment;
import com.xinhua.xinhuashe.option.news.NewsFragment;
import com.xinhua.xinhuashe.option.news.NewsItemFragment;
import com.xinhua.xinhuashe.option.news.PushNewsItemFragment;
import com.xinhua.xinhuashe.option.setting.SettingFragment;
import com.xinhua.xinhuashe.option.update.UpdateActivity;
import com.xinhua.xinhuashe.option.vote.VoteSubmitFragment;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.view.NewFontsTextView;
import com.xinhua.xinhuashe.view.dialog.ExitDialog;

/**
 * 侧滑控制类
 * 
 * 
 */
public class SlidingMenuControlActivity extends SlidingFragmentActivity
		implements LeftMenuFragment.Callbacks, RightMenuFragment.Callbacks {

	public static SlidingMenuControlActivity activity;
	public static String KEY = "ParamsKey";
	private SlidingMenu slidingMenu;
	private ParentFragment contentFragment;
	public static FrameLayout main_header_right_FrameLayout;
	private ImageView main_header_left_ImageView, main_header_right_ImageView;
	public static NewFontsTextView main_header_title_TextView, main_header_text;
	public static ImageView main_header_spinner_ImageView;
	public Bundle bundleAll;
	@SuppressLint("Recycle")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		bundleAll=savedInstanceState;
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.slidingmenu_control_framelayout);

		// -- 设置应用标题栏
		main_header_title_TextView=(NewFontsTextView) findViewById(R.id.main_header_title_TextView);
		main_header_left_ImageView = (ImageView) findViewById(R.id.main_header_left_ImageView);
		main_header_left_ImageView.setOnClickListener(clickListener);
		main_header_right_FrameLayout = (FrameLayout) findViewById(R.id.main_header_right_FrameLayout);
		main_header_right_ImageView = (ImageView) LayoutInflater.from(this)
				.inflate(R.layout.header_menu_right, null);
		main_header_right_ImageView.setOnClickListener(clickListener);
		main_header_right_FrameLayout.addView(main_header_right_ImageView);

		main_header_text = (NewFontsTextView) findViewById(R.id.main_header_text);
		main_header_spinner_ImageView = (ImageView) findViewById(R.id.main_header_spinner_ImageView);
		
		// -- 侧滑菜单
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.leftmenu_black_shadow_bg);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// -- 右侧菜单父面板
		slidingMenu.setSecondaryMenu(R.layout.rightmenu_framelayout);
		slidingMenu.setSecondaryShadowDrawable(R.drawable.rightmenu_black_shadow_bg);

		// -- 左侧菜单父面板
		setBehindContentView(R.layout.leftmenu_framelayout);
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		Fragment leftMenuFragment = new LeftMenuFragment();
		Fragment rightMenuFragment = new RightMenuFragment();
		t.replace(R.id.leftmenu_FrameLayout, leftMenuFragment);
		t.replace(R.id.rightmenu_FrameLayout, rightMenuFragment);
		t.commit();

		
		contentFragment = new HomepageFragment();
		switchContent(contentFragment, false,
				"测试SlidingMenuControlActivity传来的数据");
		
	}

	public void switchContent(ParentFragment fragment, boolean addToBackStack,
			Object... params) {
		if (fragment != null) {
			if (params != null) {
				Bundle args = new Bundle();
				args.putSerializable(SlidingMenuControlActivity.KEY, params);
				if (fragment.getArguments() == null) {
					fragment.setArguments(args);
				}
				
			} else {
				Log.i(this.getClass().getSimpleName(), "目标ParentFragment未传递参数");
			}
			FragmentTransaction t = getSupportFragmentManager()
					.beginTransaction();
			t.replace(R.id.slidingmenu_control_FrameLayout, fragment);
			if (addToBackStack) {
				t.addToBackStack(fragment.getClass().getSimpleName());
			} else {
				// 给内容Fragment赋值，并在onSaveInstanceState时保存这个Fragment
				contentFragment = fragment;
			}
			t.commit();
		} else {
			Log.e(this.getClass().getSimpleName(), "目标ParentFragment为空");
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.main_header_left_ImageView:
				slidingMenu.showMenu();
				break;
			case R.id.header_menu_right_ImageView:
				slidingMenu.showSecondaryMenu();
				break;
			
			default:
				break;
			}
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		System.out.println("---SlidingMenuControl-onSaveInstanceState---");
		getSupportFragmentManager().putFragment(outState, "contentFragment",
				contentFragment);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println("---SlidingMenuControl-onRestoreInstanceState---");
		if (savedInstanceState == null) {// == null的时候新建Fragment1
//			contentFragment = new HomepageFragment();
		} else {
			// 不等于null，找出之前保存的当前Activity显示的Fragment
			contentFragment = (ParentFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, "contentFragment");
		}
		switchContent(contentFragment, false);
	}

	/**
	 * 点击左侧菜单列表
	 */
	@Override
	public void onLeftMenuClick(AdapterView<?> adapterView, View view, int i,
			Object data) {
		slidingMenu.showContent();
		int backStackCount = getSupportFragmentManager()
				.getBackStackEntryCount();
		System.out.println("---onLeftMenuClick-backStackCount---"
				+ backStackCount);
		if (backStackCount > 0) {
			onBackKeyDown(backStackCount, null);
		}
		ParentFragment fragment = null;
		switch (i) {
		case 0:
			//我的收藏
			if ("".equals(UserInfo.userId)) {
				Toast.makeText(SlidingMenuControlActivity.activity,
						R.string.nologin, Toast.LENGTH_SHORT).show();
				return;
			}
			fragment = new MyAttentionFragment();
			switchContent(fragment, true);
			break;
		case 1:
			//消息记录
			fragment =  new PushNewsItemFragment();
			switchContent(fragment, true);
				
			
			break;
		case 2:
			//投票
			if(UserInfo.userId.equals("")){
				Toast.makeText(SlidingMenuControlActivity.activity, R.string.nologin, Toast.LENGTH_SHORT).show();
				return;
			}
			fragment = new VoteSubmitFragment();
			Bundle bundle=new Bundle();
			bundle.putBundle("bundle", bundleAll);
			fragment.setArguments(bundle);
			switchContent(fragment, true);
			break;
		case 3:
			//版本更新
			Intent intent = new Intent(SlidingMenuControlActivity.activity,
			UpdateActivity.class);
			startActivity(intent);
			break;
		case 4:
			//设置
			fragment = new SettingFragment();
			switchContent(fragment, true);
			break;
		case 5:
			//关于
			fragment = new AboutFragment();
			switchContent(fragment, true);
			break;
		case 6:
			//意见反馈
			if ("".equals(UserInfo.userId)) {
				Toast.makeText(SlidingMenuControlActivity.activity,
						R.string.nologin, Toast.LENGTH_SHORT).show();
				return;
			}
			fragment = new AboutFeedbackFragment();
			switchContent(fragment, true);
			break;
		default:
			//正在建设中
			fragment = new DevelopingFragment();
			switchContent(fragment, true);
			break;
		}
		
	}
	/**
	 * 点击左侧菜单用户登陆
	 */
	@Override
	public void onUserInfoClick(View view) {
		slidingMenu.showContent();
		int backStackCount = getSupportFragmentManager()
				.getBackStackEntryCount();
		System.out.println("---onLeftMenuClick-backStackCount---"
				+ backStackCount);
		if (backStackCount > 0) {
			onBackKeyDown(backStackCount, null);
		}
		ParentFragment fragment = null;
		switch (view.getId()) {
		case R.id.leftmenu_userinfo_LinearLayout:
			//点击左侧侧滑栏登录监听
			
			if("".equals(UserInfo.userName)){
				fragment = new LoginFragment();
			}else{
				fragment= new MydataFragment();
			}
			break;
		default:
			break;
		}
		switchContent(fragment, true);
	}

	@Override
	public void onRightMenuClick(View view, Object data) {

	}

	
	public void popBackStack(FragmentActivity fragmentActivity) {
		int count = fragmentActivity.getSupportFragmentManager()
				.getBackStackEntryCount();
		for (int j = 0; j < count; j++) {
			int backStackId = fragmentActivity.getSupportFragmentManager()
					.getBackStackEntryAt(j).getId();
			fragmentActivity.getSupportFragmentManager().popBackStack(
					backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}

	protected String getBackStackTopFragmentName() {
		String fragmentName = "";
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			fragmentName = getSupportFragmentManager().getBackStackEntryAt(0)
					.getName();
		}
		return fragmentName;
	}

	public boolean onBackKeyDown(int backStackCount, KeyEvent event) {
		return getSupportFragmentManager().popBackStackImmediate();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			int backStackCount = getSupportFragmentManager()
					.getBackStackEntryCount();
			System.out.println("------SlidingMenu控制组------backStackCount------"
					+ backStackCount);
			if (backStackCount > 0) {
				return onBackKeyDown(backStackCount, event);
			} else {
				if (slidingMenu.isMenuShowing()) {
					slidingMenu.showContent();
					return true;
				} else {
					if (event.getRepeatCount() == 0) {
						ExitDialog exitDialog = new ExitDialog(
								SlidingMenuControlActivity.this);
						exitDialog.show();
						return true;
					} else {
						return super.dispatchKeyEvent(event);
					}
				}
			}
		} else {
			return super.dispatchKeyEvent(event);
		}
	}


	public ImageView getMain_header_right_ImageView() {
		return main_header_right_ImageView;
	}

	public void setHeaderRightView(View view) {
		main_header_right_FrameLayout.removeAllViews();
		main_header_right_FrameLayout.addView(view);
	}
	
	@Override
	protected void onResume() {
		main_header_text.invalidate();
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		main_header_text.invalidate();
		super.onRestart();
	}

}
