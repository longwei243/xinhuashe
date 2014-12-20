package com.xinhua.xinhuashe.option.cop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.parentclass.ParentFragment;

/**
 * 便民工具详细页
 * 
 * @author azuryleaves
 * @since 2014-2-25 下午9:05:28
 * @version 1.0
 * 
 */
public class ConvenienceOfPeopleDetailFragment extends ParentFragment {

	private int position;
	public static WebView cop_detail_WebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = (Integer) params[0];
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		return contextView;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.cop_detail;
	}

	@Override
	protected void setupViews(View parentView) {
		cop_detail_WebView = (WebView) parentView
				.findViewById(R.id.cop_detail_WebView);
		cop_detail_WebView.getSettings().setJavaScriptEnabled(true);
		cop_detail_WebView.requestFocus();
		String url = COPData.urls[position];
		cop_detail_WebView.loadUrl(url);
		cop_detail_WebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	protected void initialized() {

	}

	@Override
	protected void threadTask() {

	}

}
