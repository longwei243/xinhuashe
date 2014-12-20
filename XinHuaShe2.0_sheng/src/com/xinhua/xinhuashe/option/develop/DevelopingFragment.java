package com.xinhua.xinhuashe.option.develop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.parentclass.ParentFragment;

/**
 * 正在建设中
 *
 * @author azuryleaves
 * @since 2014-3-15 上午12:47:13
 * @version 1.0
 *
 */
public class DevelopingFragment extends ParentFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container, savedInstanceState);
		return contextView;
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.building;
	}

	@Override
	protected void setupViews(View parentView) {
		
	}

	@Override
	protected void initialized() {
		
	}

	@Override
	protected void threadTask() {
		
	}
	
}
