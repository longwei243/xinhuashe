package com.xinhua.xinhuashe.option.staggeredgridview.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhuanews.sheng.R;

/**
 * @author jiayou
 * @version 2014年8月14日 下午4:42:34
 */
public class ShowFullPicActivity extends ParentActivity {

	
	private ImageView fullPicImageView;
	private String picPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.sgv_showfullpic;
	}

	@Override
	protected void setupViews() {
		fullPicImageView=(ImageView)findViewById(R.id.fullPicImageView);
	}

	@Override
	protected void initialized() {
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		picPath=bundle.getString("picpath");
		Log.d(MobileApplication.TAG, picPath);
		BitmapUtils bitmapUtils=new BitmapUtils(this);
		bitmapUtils.display(fullPicImageView, picPath);
	}

	@Override
	protected void threadTask() {
		
	}

	
}
