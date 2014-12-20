package org.androidpn.client;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.threadpool.IActivity;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;

public class NewsDetailActivity extends Activity implements IActivity{
	private static WebView news_detail_WebView;
	private static ImageView praise_ImageView, attention_ImageView,
			share_ImageView;
	private static Button content_send_button;
	private static EditText content_EditText;
	private String articleId, articleTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		setContentView(R.layout.push_news_detail);
		Intent intent = getIntent();
		String message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
		articleId = intent.getStringExtra(Constants.NOTIFICATION_TITLEID);
//		articleId = 1955+"";
		System.out.println("推送详情页面id："+articleId);
		
		/*praise_ImageView = (ImageView) findViewById(R.id.push_news_detail_praise_ImageView);
		attention_ImageView = (ImageView) findViewById(R.id.push_news_detail_attention_ImageView);
		content_EditText = (EditText) findViewById(R.id.push_news_detail_content_EditText);
		share_ImageView = (ImageView) findViewById(R.id.push_news_detail_share_ImageView);
		content_send_button = (Button) findViewById(R.id.push_news_detail_send_button);
		content_send_button.setBackgroundDrawable(SelecterUtil.setSelector(
				SlidingMenuControlActivity.activity, R.color.main_header_blue,
				R.color.gray, -1, -1));
	//	content_EditText.setOnEditorActionListener(editorActionListener);
		praise_ImageView.setOnClickListener(clickListener);
		attention_ImageView.setOnClickListener(clickListener);
		share_ImageView.setOnClickListener(clickListener);
		content_send_button.setOnClickListener(clickListener);*/
		news_detail_WebView = (WebView) findViewById(R.id.push_news_detail_WebView);
		news_detail_WebView.getSettings().setBlockNetworkImage(false);
		// 设置自动加载图片
		news_detail_WebView.getSettings().setLoadsImagesAutomatically(true);
		//增大webview中字体
		news_detail_WebView.getSettings().setTextSize(TextSize.NORMAL);
		//设置自动手动改变字体大小
		news_detail_WebView.getSettings().setBuiltInZoomControls(true);
		
		initialized();
		
	}
	/*
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.news_detail_praise_ImageView:
				if ("".equals(UserInfo.userId)) {
					Toast.makeText(SlidingMenuControlActivity.activity,
							R.string.nologin, Toast.LENGTH_SHORT).show();
					return;
				}
				Task taskPraise = new Task(TaskID.TASK_NEWS_DETAIL_PRAISE,
						RequestURL.getPraise(articleId), this.getClass()
								.getName(), "-点赞-");
				MobileApplication.poolManager.addTask(taskPraise);
				break;
			case R.id.news_detail_attention_ImageView:
				if ("".equals(UserInfo.userId)) {
					Toast.makeText(SlidingMenuControlActivity.activity,
							R.string.nologin, Toast.LENGTH_SHORT).show();
					return;
				}
				Task task = new Task(TaskID.TASK_NEWS_DETAIL_ATTENTION,
						RequestURL.getAttentionAdd(articleId), this.getClass()
								.getName(), "-添加收藏-");
				MobileApplication.poolManager.addTask(task);
				break;
			case R.id.news_detail_share_ImageView:
				Toast.makeText(SlidingMenuControlActivity.activity,
						R.string.developing, Toast.LENGTH_SHORT).show();
				break;
			case R.id.news_detail_content_TextView:
				String url = ((TextView) view).getText().toString();
				url = "http://183.203.18.45:7001/jeesite/version/shijianzhutiqu.flv";
				System.out.println("---url---" + url);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String type = "video/* ";
				Uri uri = Uri.parse(url);
				intent.setDataAndType(uri, type);
				SlidingMenuControlActivity.activity.startActivity(intent);
				break;
//			case R.id.header_menu_right_TextView:
//				//share
//				showShare(false, null);
//				break;
			case R.id.news_detail_send_button:
				if("".equals(UserInfo.userId)){
					Toast.makeText(SlidingMenuControlActivity.activity,
							R.string.nologin, Toast.LENGTH_SHORT).show();
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(ParentHandlerService.URL, RequestURL.getComment());
				map.put("userId", UserInfo.userId);
				map.put("nickName", UserInfo.userName);
				map.put("content",content_EditText.getText().toString());
				map.put("title", articleTitle);
				map.put("contentId", articleId);
				Task task1 = new Task(TaskID.TASK_NEWS_DETAIL_COMMENT, map, this
						.getClass().getName(), "-发表评论-");
				loadingDialog.show();
				MobileApplication.poolManager.addTask(task1);
				break;
			
			default:
				break;
			}
		}
	};*/
	private String shareurl;
	
	protected void initialized() {
		shareurl = RequestURL.getNewsDetail(articleId);
		System.out.println("---url---" + shareurl);
		news_detail_WebView.loadUrl(shareurl);
		news_detail_WebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 在当前的webview中跳转到新的url
				return true;
			}
		});
	}
	
	@Override
	public void closeLoadingView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(int arg0, Object... params) {
		if (params != null) {
			if (params[0] != null) {
				try {
					JSONObject jsonObject = new JSONObject(params[0].toString());
					String result = jsonObject.getString("result");
					if ("success".equals(result)) {
						content_EditText.setText("");
					}
					String message = jsonObject.getString("message");
					Toast.makeText(SlidingMenuControlActivity.activity,
							message, Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
			}
		} else {
		}
	}
}
