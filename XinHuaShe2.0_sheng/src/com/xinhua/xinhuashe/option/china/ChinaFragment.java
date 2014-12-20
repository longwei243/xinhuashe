package com.xinhua.xinhuashe.option.china;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;

/**
 * 中国网事
 *
 * @author azuryleaves
 * @since 2014-4-21 上午10:43:15
 * @version 1.0
 *
 */
public class ChinaFragment extends ParentFragment {

	private static WebView news_detail_WebView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		threadTask();
		SlidingMenuControlActivity.main_header_title_TextView.setText("中国网事");
		return contextView;
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.china;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void setupViews(View parentView) {
		news_detail_WebView = (WebView) parentView
				.findViewById(R.id.news_detail_WebView);
		news_detail_WebView.getSettings().setJavaScriptEnabled(true);
		news_detail_WebView.getSettings().setAppCacheEnabled(true);
		// 设置是否显示网络图像---true,封锁网络图片，不显示 false----允许显示网络图片
		news_detail_WebView.getSettings().setBlockNetworkImage(false);
		// 设置自动加载图片
		news_detail_WebView.getSettings().setLoadsImagesAutomatically(true);
		//增大webview中字体
//		news_detail_WebView.getSettings().setTextSize(TextSize.LARGER);
		//设置自动手动改变字体大小
		news_detail_WebView.getSettings().setBuiltInZoomControls(true);
	}

	@Override
	protected void initialized() {
		String url = "http://zgws.xinhuanet.com/api/open/List.aspx?siteid=156&r=0.7723461736459285&wt=中国网事";
		news_detail_WebView.loadUrl(url);
		news_detail_WebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 在当前的webview中跳转到新的url
				return true;
			}
		});
	}

	@Override
	protected void threadTask() {
		
	}

}
