package com.xinhua.xinhuashe.parentclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.view.dialog.LoadingDialog;

/**
 * Fragment父类
 * 
 */
public abstract class ParentFragment extends Fragment {

	protected static String SaveFragmentKey = "ParentFragment", SaveDataKey = "SaveDataKey";
	protected static LayoutInflater inflater;
	protected Object[] params, saveData;
	public static LoadingDialog loadingDialog;
	protected ImageView loading_refresh_ImageView;
	private static FragmentManager manager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = SlidingMenuControlActivity.activity.getSupportFragmentManager();
		if (savedInstanceState != null) {
			saveData = (Object[]) savedInstanceState.getSerializable(SaveDataKey);
//			System.out.println("---ParentFragment---savedInstanceState---" + saveData[0]);
		}
		Bundle bundle = getArguments();
		if (bundle != null) {
			params = (Object[]) bundle
					.getSerializable(SlidingMenuControlActivity.KEY);
//			System.out.println("---ParentFragment---getArguments---" + params[0]);
			if (params == null) {
				Log.e(this.getClass().getSimpleName(),
						"传参Bundle对象包含的Object数组为空");
			}
		} else {
			Log.e(this.getClass().getSimpleName(), "传参Bundle对象为空");
		}
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(getActivity(), clickListener);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ParentFragment.inflater = inflater;
		int layoutId = getLayoutId();
		View parentView = null;
		if (layoutId == 0) {
			Log.e(MobileApplication.TAG, "请添加ContentView布局文件");
		} else {
			parentView = inflater.inflate(layoutId, container, false);
		}
		setupViews(parentView);
		initialized();
		return parentView;
	}
	
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.loadingdialog_cancel_ImageView:
				loadingDialog.setTag(getActivity().getClass().getSimpleName());
				loadingDialog.cancel();
				break;

			default:
				break;
			}

		}
	};
	
	protected void onSaveInstanceState(Bundle outState, ParentFragment fragment, Object... saveData) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SaveDataKey, saveData);
		getActivity().getSupportFragmentManager().putFragment(outState, SaveFragmentKey,
				fragment);
//		System.out.println("---ParentFragment---onSaveInstanceState---" + fragment.getClass().getSimpleName() + "---" + saveData[0].toString());
	};
	
	protected ListView getGeneralListView() {
		ListView listView = (ListView) inflater.inflate(
				R.layout.general_listview, null);
		return listView;
	}
	
	/**
	 * 布局文件ID
	 * 
	 * @return
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化组件
	 */
	protected abstract void setupViews(View parentView);
	
	/**
	 * 初始化数据
	 */
	protected abstract void initialized();
	
	/**
	 * 需要线程池操作的任务
	 * 
	 */
	protected abstract void threadTask();
	
	protected void setArguments(ParentFragment fragment, Object... params) {
		if (fragment != null) {
			if (params != null) {
				Bundle args = new Bundle();
				args.putSerializable(SlidingMenuControlActivity.KEY, params);
				fragment.setArguments(args);
			} else {
				Log.i(this.getClass().getSimpleName(), "目标ParentFragment要传递的参数为空");
			}
		} else {
			Log.e(this.getClass().getSimpleName(), "目标ParentFragment为空");
		}
	}
	
	protected void switchFragment(ParentFragment fragment,
			String fragmentName, Object... params) {
		switchFragment(R.id.slidingmenu_control_FrameLayout, fragment, fragmentName, params);
	}

	/**
	 * Fragment跳转，可返回
	 * 
	 * @param replaceLayout
	 * @param fragment
	 * @param fragmentName
	 * @param params
	 */
	protected void switchFragment(int replaceLayout, ParentFragment fragment,
			String fragmentName, Object... params) {
		if (fragment != null) {
			if (params != null) {
				Bundle args = new Bundle();
				args.putSerializable(SlidingMenuControlActivity.KEY, params);
				fragment.setArguments(args);
			} else {
				Log.i(this.getClass().getSimpleName(), "目标ParentFragment未传递参数");
			}
			manager.beginTransaction()
					.replace(replaceLayout, fragment).addToBackStack(fragmentName)
					.commit();
		} else {
			Log.e(this.getClass().getSimpleName(), "目标ParentFragment为空");
		}
	}

	protected void switchFragmentNoBack(ParentFragment fragment,
			Object... params) {
		switchFragmentNoBack(R.id.slidingmenu_control_FrameLayout, fragment, params);
	}

	/**
	 * Fragment跳转，无返回
	 * 
	 * @param replaceLayout
	 * @param fragment
	 * @param params
	 */
	protected void switchFragmentNoBack(int replaceLayout,
			ParentFragment fragment, Object... params) {
		if (fragment != null) {
			if (params != null) {
				Bundle args = new Bundle();
				args.putSerializable(SlidingMenuControlActivity.KEY, params);
				fragment.setArguments(args);
			} else {
				Log.i(this.getClass().getSimpleName(), "目标ParentFragment未传递参数");
			}
			FragmentTransaction t = getActivity().getSupportFragmentManager()
					.beginTransaction();
			t.replace(replaceLayout, fragment);
			t.commit();
		} else {
			Log.e(this.getClass().getSimpleName(), "目标ParentFragment为空");
		}
	}

	/**
	 * 清除Fragment返回栈
	 * 
	 * @param fragmentActivity
	 */
	public static void popFragmentBackStack(FragmentActivity fragmentActivity) {
		int count = fragmentActivity.getSupportFragmentManager()
				.getBackStackEntryCount();
		for (int j = 0; j < count; j++) {
			int backStackId = fragmentActivity.getSupportFragmentManager()
					.getBackStackEntryAt(j).getId();
			fragmentActivity.getSupportFragmentManager().popBackStack(
					backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}

}
