package com.xinhua.xinhuashe.option.news;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.douban.Douban;
import cn.sharesdk.dropbox.Dropbox;
import cn.sharesdk.evernote.Evernote;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.flickr.Flickr;
import cn.sharesdk.foursquare.FourSquare;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;
import cn.sharesdk.instagram.Instagram;
import cn.sharesdk.kaixin.KaiXin;
import cn.sharesdk.linkedin.LinkedIn;
import cn.sharesdk.mingdao.Mingdao;
import cn.sharesdk.netease.microblog.NetEaseMicroBlog;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.pinterest.Pinterest;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sohu.microblog.SohuMicroBlog;
import cn.sharesdk.sohu.suishenkan.SohuSuishenkan;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.tumblr.Tumblr;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.vkontakte.VKontakte;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.yixin.friends.Yixin;
import cn.sharesdk.yixin.moments.YixinMoments;
import cn.sharesdk.youdao.YouDao;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.android.view.utils.SelecterUtil;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.util.UserBehaviorInfo;

/**
 * 新闻详细页
 * 
 * @author azuryleaves
 * @since 2014-3-13 上午10:27:06
 * @version 1.0
 * 
 */
public class NewsDetailFragment extends ParentFragment implements IActivity {

	private String articleId, articleTitle;
	private static WebView news_detail_WebView;
	private static ImageView praise_ImageView, attention_ImageView,
			share_ImageView;
	private static Button content_send_button;
	private static EditText content_EditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileApplication.allIActivity.add(this);
		articleId = params[0].toString();
		articleTitle = params[0].toString();
		System.out.println("---articleId---" + articleId);

		ShareSDK.initSDK(SlidingMenuControlActivity.activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = super.onCreateView(inflater, container,
				savedInstanceState);
		threadTask();
		TextView view = (TextView) inflater.inflate(
				R.layout.header_menu_right_textview, null);
		view.setOnClickListener(clickListener);
		view.setText("分享");
		SlidingMenuControlActivity.activity.setHeaderRightView(view);
		return contextView;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.news_detail;
	}

	@Override
	protected void setupViews(View parentView) {
		praise_ImageView = (ImageView) parentView
				.findViewById(R.id.news_detail_praise_ImageView);
		attention_ImageView = (ImageView) parentView
				.findViewById(R.id.news_detail_attention_ImageView);
		content_EditText = (EditText) parentView
				.findViewById(R.id.news_detail_content_EditText);
		share_ImageView = (ImageView) parentView
				.findViewById(R.id.news_detail_share_ImageView);
		content_send_button = (Button) parentView
				.findViewById(R.id.news_detail_send_button);
		content_send_button.setBackgroundDrawable(SelecterUtil.setSelector(
				SlidingMenuControlActivity.activity, R.color.main_header_blue,
				R.color.gray, -1, -1));
		// content_EditText.setOnEditorActionListener(editorActionListener);
		praise_ImageView.setOnClickListener(clickListener);
		attention_ImageView.setOnClickListener(clickListener);
		share_ImageView.setOnClickListener(clickListener);
		content_send_button.setOnClickListener(clickListener);
		news_detail_WebView = (WebView) parentView
				.findViewById(R.id.news_detail_WebView);
		news_detail_WebView.getSettings().setBlockNetworkImage(false);
		// 设置自动加载图片
		news_detail_WebView.getSettings().setLoadsImagesAutomatically(true);
		// 增大webview中字体
		// 设置自动手动改变字体大小
		news_detail_WebView.getSettings().setBuiltInZoomControls(true);
	}

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
				loadingDialog.show();
				MobileApplication.poolManager.addTask(taskPraise);
				Map<String, String> map1 = UserBehaviorInfo
						.sendUserOpenAppInfo();

				map1.put("operateType", "011");
				map1.put("operateObjID", articleId);
				map1.put(ParentHandlerService.URL,
						RequestURL.postUserbehaviorPhoneInfo());
				Task task1 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map1,
						this.getClass().getName(), "用户点赞行为");
				MobileApplication.poolManager.addTask(task1);
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
				loadingDialog.show();
				MobileApplication.poolManager.addTask(task);
				Map<String, String> map2 =UserBehaviorInfo.sendUserOpenAppInfo();
				map2.put("operateType", "006");
				map2.put("operateObjID", articleId);
				map2.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
				Task task2 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map2, this
						.getClass().getName(), "用户收藏行为");
				MobileApplication.poolManager.addTask(task2);
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
			case R.id.header_menu_right_TextView:
				// share
				showShare(false, null);
				Map<String, String> map3 =UserBehaviorInfo.sendUserOpenAppInfo();
				map3.put("operateType", "008");
				map3.put("operateObjID", articleId);
				map3.put(ParentHandlerService.URL, RequestURL.postUserbehaviorPhoneInfo());
				Task task3 = new Task(TaskID.TASK_POST_USERBEHAVIORINFO, map3, this
						.getClass().getName(), "用户分享行为");
				MobileApplication.poolManager.addTask(task3);
				break;
			case R.id.news_detail_send_button:
				/*
				 * if("".equals(UserInfo.userId)){
				 * Toast.makeText(SlidingMenuControlActivity.activity,
				 * R.string.nologin, Toast.LENGTH_SHORT).show(); } Map<String,
				 * Object> map = new HashMap<String, Object>();
				 * map.put(ParentHandlerService.URL, RequestURL.getComment());
				 * map.put("userId", UserInfo.userId); map.put("nickName",
				 * UserInfo.userName);
				 * map.put("content",content_EditText.getText().toString());
				 * map.put("title", articleTitle); map.put("contentId",
				 * articleId); Task task1 = new
				 * Task(TaskID.TASK_NEWS_DETAIL_COMMENT, map, this
				 * .getClass().getName(), "-发表评论-"); loadingDialog.show();
				 * MobileApplication.poolManager.addTask(task1);
				 */
				Toast.makeText(SlidingMenuControlActivity.activity, "目前还不能评论",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	private String shareurl;

	/*
	 * private OnEditorActionListener editorActionListener = new
	 * OnEditorActionListener() {
	 * 
	 * @Override public boolean onEditorAction(TextView v, int actionId,
	 * KeyEvent event) { if ("".equals(UserInfo.userId)) {
	 * Toast.makeText(SlidingMenuControlActivity.activity, R.string.nologin,
	 * Toast.LENGTH_SHORT).show(); return true; } switch (actionId) { case
	 * EditorInfo.IME_ACTION_SEND: // v.setClickable(false); Map<String, Object>
	 * map = new HashMap<String, Object>(); map.put(ParentHandlerService.URL,
	 * RequestURL.getComment()); map.put("userId", UserInfo.userId);
	 * map.put("nickName", UserInfo.userName); map.put("content",
	 * v.getText().toString()); map.put("title", articleTitle);
	 * map.put("contentId", articleId); Task task = new
	 * Task(TaskID.TASK_NEWS_DETAIL_COMMENT, map, this .getClass().getName(),
	 * "-发表评论-"); loadingDialog.show();
	 * MobileApplication.poolManager.addTask(task); break; default: break; }
	 * return true; } };
	 */

	@Override
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
	protected void threadTask() {

	}

	@Override
	public void closeLoadingView() {
		loadingDialog.cancel();
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(int taskId, Object... params) {
		loadingDialog.cancel();

		if (params != null) {
			if (params[0] != null) {
				try {

					switch (taskId) {
					case TaskID.TASK_NEWS_DETAIL_ATTENTION:
						JSONObject jsonObject = new JSONObject(
								params[0].toString());
						String result = jsonObject.getString("result");
						if ("success".equals(result)) {
							String message = jsonObject.getString("message");
							Toast.makeText(SlidingMenuControlActivity.activity,
									message, Toast.LENGTH_SHORT).show();
						}
						break;
					case TaskID.TASK_NEWS_DETAIL_PRAISE:
						JSONObject jsonObject1 = new JSONObject(
								params[0].toString());
						String result1 = jsonObject1.getString("result");
						if ("success".equals(result1)) {
							String message1 = jsonObject1.getString("message");
							Toast.makeText(SlidingMenuControlActivity.activity,
									message1, Toast.LENGTH_SHORT).show();
						}
					case TaskID.TASK_NEWS_DETAIL_COMMENT:
						JSONObject jsonObject2 = new JSONObject(
								params[0].toString());
						String result2 = jsonObject2.getString("result");
						if ("success".equals(result2)) {
							String message2 = jsonObject2.getString("message");
							Toast.makeText(SlidingMenuControlActivity.activity,
									message2, Toast.LENGTH_SHORT).show();
						}

					default:
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				loadingDialog.showTimeOut();
			}
		} else {
			loadingDialog.showTimeOut();
		}
	}

	/**
	 * 展示一键分享
	 * 
	 * @param silent
	 *            是否直接分享
	 * @param platform
	 *            平台
	 */
	private void showShare(boolean silent, String platform) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher,
				SlidingMenuControlActivity.activity
						.getString(R.string.app_name));
		// oks.setAddress("12345678901");
		// oks.setTitle(MainActivity.this.getString(R.string.evenote_title));
		// oks.setTitleUrl("http://sharesdk.cn");
		oks.setText("新华社：" + shareurl);
		// oks.setImagePath(MainActivity.TEST_IMAGE);
		// oks.setImageUrl(MainActivity.TEST_IMAGE_URL);
		// oks.setUrl(url);
		// oks.setFilePath(MainActivity.TEST_IMAGE);
		// oks.setComment(MainActivity.this.getString(R.string.share));
		// oks.setSite(MainActivity.this.getString(R.string.app_name));
		// oks.setSiteUrl(url);
		// oks.setVenueName("ShareSDK");
		// oks.setVenueDescription("This is a beautiful place!");
		// oks.setLatitude(23.056081f);
		// oks.setLongitude(113.385708f);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}

		// 取消注释，可以实现对具体的View进行截屏分享
		// oks.setViewToShare(getPage());

		// 去除注释，可令编辑页面显示为Dialog模式
		// oks.setDialogMode();

		// 去除注释，在自动授权时可以禁用SSO方式
		// oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标
		// Bitmap logo = BitmapFactory.decodeResource(menu.getResources(),
		// R.drawable.ic_launcher);
		// String label = menu.getResources().getString(R.string.app_name);
		// OnClickListener listener = new OnClickListener() {
		// public void onClick(View v) {
		// String text = "Customer Logo -- ShareSDK " +
		// ShareSDK.getSDKVersionName();
		// Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
		// oks.finish();
		// }
		// };
		// oks.setCustomerLogo(logo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);
		oks.addHiddenPlatform(QZone.NAME);
		oks.addHiddenPlatform(WechatFavorite.NAME);
		oks.addHiddenPlatform(Facebook.NAME);
		oks.addHiddenPlatform(Twitter.NAME);
		oks.addHiddenPlatform(Renren.NAME);
		oks.addHiddenPlatform(KaiXin.NAME);
		oks.addHiddenPlatform(Email.NAME);
		oks.addHiddenPlatform(ShortMessage.NAME);
		oks.addHiddenPlatform(SohuMicroBlog.NAME);
		oks.addHiddenPlatform(NetEaseMicroBlog.NAME);
		oks.addHiddenPlatform(Douban.NAME);
		oks.addHiddenPlatform(YouDao.NAME);
		oks.addHiddenPlatform(SohuSuishenkan.NAME);
		oks.addHiddenPlatform(Evernote.NAME);
		oks.addHiddenPlatform(LinkedIn.NAME);
		oks.addHiddenPlatform(GooglePlus.NAME);
		oks.addHiddenPlatform(FourSquare.NAME);
		oks.addHiddenPlatform(Pinterest.NAME);
		oks.addHiddenPlatform(Flickr.NAME);
		oks.addHiddenPlatform(Tumblr.NAME);
		oks.addHiddenPlatform(Dropbox.NAME);
		oks.addHiddenPlatform(VKontakte.NAME);
		oks.addHiddenPlatform(Instagram.NAME);
		oks.addHiddenPlatform(Yixin.NAME);
		oks.addHiddenPlatform(YixinMoments.NAME);
		oks.addHiddenPlatform(Mingdao.NAME);
		oks.addHiddenPlatform(TencentWeibo.NAME);
		oks.addHiddenPlatform(WechatMoments.NAME);

		oks.show(SlidingMenuControlActivity.activity);
	}

}
