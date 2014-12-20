package com.xinhua.xinhuashe.option.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.net.update.Config;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.guide.GuideActivity;
import com.xinhua.xinhuashe.option.update.UpdateActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;

/**
 * 关于页面
 * 
 */
public class AboutFragment extends ParentFragment {

	private String versionName;
	private TextView about_version_TextView;
	private RelativeLayout version_update_layout, introduce_layout, guide_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		
		return contextView;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			
			switch (view.getId()) {
			//版本更新
			case R.id.about_version_update:
				Intent intent = new Intent(SlidingMenuControlActivity.activity,
						UpdateActivity.class);
				startActivity(intent);
				break;
			//功能介绍
			case R.id.about_introduce:
				
				break;
			//新手引导
			case R.id.about_guide:
				Intent intentguide = new Intent(SlidingMenuControlActivity.activity,
						GuideActivity.class);
				startActivity(intentguide);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.about;
	}

	@Override
	protected void setupViews(View parentView) {
		
		about_version_TextView = (TextView) parentView
				.findViewById(R.id.about_version_TextView);
		
		version_update_layout = (RelativeLayout) parentView.findViewById(R.id.about_version_update);
		version_update_layout.setOnClickListener(clickListener);
		
		introduce_layout = (RelativeLayout) parentView.findViewById(R.id.about_introduce);
		introduce_layout.setOnClickListener(clickListener);
		
		guide_layout = (RelativeLayout) parentView.findViewById(R.id.about_guide);
		guide_layout.setOnClickListener(clickListener);
	}

	@Override
	protected void initialized() {
			versionName = Config.getVerName(SlidingMenuControlActivity.activity, getResources().getString(
					R.string.package_name));
			about_version_TextView.setText("V"+versionName);
			SlidingMenuControlActivity.main_header_title_TextView.setText("关于");
	}

	@Override
	protected void threadTask() {

	}

}
