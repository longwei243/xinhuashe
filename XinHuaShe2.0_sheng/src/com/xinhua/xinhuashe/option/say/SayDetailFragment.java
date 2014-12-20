package com.xinhua.xinhuashe.option.say;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SayDetailFragment extends ParentFragment {

	private String suggestionId;
	private String url;
	private WebView say_raply_detail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		suggestionId = (String) params[0];
	}

	@Override
	protected int getLayoutId() {
		return R.layout.say_raply_detail;
	}

	@Override
	protected void setupViews(View parentView) {
		say_raply_detail = (WebView) parentView
				.findViewById(R.id.say_raply_detail_WebView);
	}

	@Override
	protected void initialized() {
		url = RequestURL.getSayReplyDetail(suggestionId);
		say_raply_detail.loadUrl(url);
		say_raply_detail.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 在当前的webview中跳转到新的url
				return true;
			}
		});
		say_raply_detail.getSettings().setDefaultFixedFontSize(22);
		say_raply_detail.getSettings().setBuiltInZoomControls(true);
	}

	@Override
	protected void threadTask() {

	}

}
