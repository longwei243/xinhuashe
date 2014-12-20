package com.xinhua.xinhuashe.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.service.MobileApplication;

/**
 * 请求地址
 * 
 * 
 */
public class RequestURL {

	public static String http = "http://"
			+ MobileApplication.preferences.getString("http", "");

	public static String xianarea = "寿阳";
	public static String xianCode = "140725";

	public static String area = "寿阳";
	public static String areaCode = "140725";

	public static String default_shi = "晋中";
	public static String default_shiCode = "140700";

	public static String shi = "晋中";
	public static String shiCode = "140700";

	public static String tingJuCode = "120100";
	public static String tingJu = "水利局";

	// -- 登陆
	public static String getLogin() {
		return http + "/xinhua/m/login.html?code=" + areaCode;
	}

	// -- 退出
	public static String getLogout() {
		return http + "/party/j_spring_security_logout";
	}

	// -- 默认注册
	public static String getDefaultRegister() {
		return http + "/xinhua/m/regDefault.html?code=" + areaCode;
	}

	// -- 实名注册
	public static String getAuthRegister() {
		return http + "/xinhua/m/regAuth.html?code=" + areaCode;
	}

	// -- 更新-检查
	public static String getUpdateVersionCheck() {
		return http + "/xinhua/m/version-" + areaCode + ".html";
	}

	// -- 首页新闻
	public static String getHomePageNews() {
		String url = http + "/xinhua/m/article-news.html";
		return url;
	}

	// -- 首页热图
	public static String getHomePageHotPic() {
		return http + "/xinhua/m/roll-area-" + areaCode + ".html";
	}

	/**
	 * 天气信息
	 * 
	 * @param cityId
	 *            城市编码
	 * @return
	 */
	public static String getWeatherInfo(String city, String day) {
		try {
			city = URLEncoder.encode(city, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "http://php.weather.sina.com.cn/xml.php?city=" + city
				+ "&password=DJOYnieT8234jlsK&day=" + day;
	}

	// -- 获取二维码
	public static String getQRCode() {
		String url = http + "/xinhua/m/qRcideimageSave.html";
		return url;
	}

	// -- 所有新闻栏目列表
	public static String getAllColumnsList() {
		String url = http + "/xinhua/m/AllCategory.html";
		return url;
	}

	// -- 某一模块下新闻栏目列表
	public static String getColumnsList(String parmas) {
		String url = http + "/xinhua/m/categoryListByParent-" + parmas
				+ "-3.html";
		return url;
	}
	// -- huimingshenghuo栏目列表
	public static String getHuiMingColumnsList() {
		String url = http + "/xinhua/m/xinwen-4-"+areaCode+".html";
		return url;
	}

	// -- 新闻列表
	public static String getNewsList(String... parmas) {
		String url = http + "/xinhua/m/articles-list-" + parmas[0] + "-"
				+ areaCode + ".html" + "?pageNo=" + parmas[1] + "&pageSize=10";
		return url;
	}

	// -- 山西新闻列表
	public static String getShanXiNewsList(String... parmas) {
		String url = http + "/xinhua/m/articles-list-" + parmas[0]
				+ "-140000.html" + "?pageNo=" + parmas[1] + "&pageSize=10";
		return url;
	}

	// -- 山西新闻列表
		public static String getQuanGuoNewsList(String... parmas) {
			String url = http + "/xinhua/m/articles-list-" + parmas[0]
					+ "-100000.html" + "?pageNo=" + parmas[1] + "&pageSize=10";
			return url;
		}
	
	// -- 县区新闻列表
	public static String getXianQuNewsList(String... parmas) {
		String url = http + "/xinhua/m/articles-list-" + parmas[0] + "-"
				+ xianCode + ".html" + "?pageNo=" + parmas[1] + "&pageSize=10";
		return url;
	}

	// -- 地市新闻列表
	public static String getDiShiNewsList(String... parmas) {
		String url = http + "/xinhua/m/articles-list-" + parmas[0] + "-"
				+ shiCode + ".html" + "?pageNo=" + parmas[1] + "&pageSize=10";
		return url;
	}

	// -- 厅局资讯新闻列表
	public static String getTingJuNewsList(String... parmas) {
		String url = http + "/xinhua/m/articles-list-" + parmas[0] + "-"
				+ tingJuCode + ".html" + "?pageNo=" + parmas[1]
				+ "&pageSize=10";
		return url;
	}

	// -- 新闻详情
	public static String getNewsDetail(String articleId) {
		return http + "/xinhua/f/viewOnMobile-" + articleId + ".html";
	}

	// -- 我的收藏
	public static String getMyAttention(String pageNo) {
		return http + "/xinhua/m/articleListfindByUserId-" + UserInfo.userId
				+ ".html?pageNo=" + pageNo + "&pageSize=10";
	}

	// -- 添加收藏
	public static String getAttentionAdd(String articleId) {
		return http + "/xinhua/m/articlesSave-" + UserInfo.userId + "-"
				+ articleId + ".html";
	}

	// -- 删除收藏
	public static String getAttentionDelete(String articleId) {
		return http + "/xinhua/m/articlesDelete-" + UserInfo.userId + "-"
				+ articleId + ".html";
	}

	// -- 查看评论
	public static String getSeeComment(String articleId) {
		return http + "/xinhua/m/commentListfindByCommentIdUserId-" + articleId
				+ "-" + UserInfo.userId + ".html";
	}

	// -- 发表评论
	public static String getComment() {
		return http + "/xinhua/m/comment";
	}

	// -- 点赞
	public static String getPraise(String articleId) {
		return http + "/xinhua/m/praiseSave-" + UserInfo.userId + "-"
				+ articleId + ".html";
	}

	// -- 有话要说-新增
	public static String getSayAdd() {
		return http + "/xinhua/m/suggestion.html";
	}

	/*
	 * // -- 有话要说-列表 public static String getSayList(String id, String pageNo) {
	 * return http + "/jeesite/m/suggestionfindByOfficeIdAndType-" + areaCode +
	 * "-" + id + ".html?pageNo=" + pageNo + "&pageSize=10"; }
	 */

	// -- 有话要说-列表全部
	public static String getSayList(String pageNo) {
		return http + "/xinhua/m/suggestionfindByOfficeId-" + areaCode
				+ ".html?pageNo=" + pageNo + "&pageSize=10";
	}

	// --有话要说-类型
	public static String getSayType() {
		return http + "/xinhua/m/dictFindType.html";
	}

	// -- 有话要说-选人
	public static String getSayTarget() {
		return http + "/xinhua/m/goalUserfindAll-" + areaCode + ".html";
	}

	/*// -- 有话要说-上传文件
	public static String getSayUpload() {
		return http + "/xinhua/m/suggestionFile.html";
	}*/

	// -- 有话要说-回复信息列表
	public static String getSayReplyList(String suggestionId, String pageNo) {
		return http + "/xinhua/m/replyByUserId-" + suggestionId
				+ ".html?pageNo=" + pageNo + "&pageSize=10";
	}

	// -- 有话要说-详情
	public static String getSayReplyDetail(String suggestionId) {
		return http + "/xinhua/f/suggestion-" + suggestionId + ".html";
	}

	// 本地新闻
	public static String getHomePageNews1() {
		String url = http + "/xinhua/m/articles-list-8-" + areaCode
				+ ".html?pageNo=1&pageSize=3";
		return url;
	}

	// 要闻推荐
	public static String getHomePageNews2() {
		String url = http + "/xinhua/m/article-news.html";
		return url;
	}

	// 要闻新闻列表
	public static String getYaoWenNewsList(String... parmas) {
		String url = http + "/xinhua/m/article-news.html" + "?pageNo="
				+ parmas[0] + "&pageSize=10";
		return url;
	}

	// 获得新闻动态栏目下的几条新闻
	public static String getXinWenColumnNews() {
		String url = http + "/xinhua/m/content-2-" + areaCode + ".html";
		return url;

	}
	// 获得新闻动态栏目全国的一条新闻
	public static String getXinWenColumnNews1() {
		String url = http + "/xinhua/m/content-54-100000.html";
		return url;
		
	}
	// 获得新闻动态栏目下山西的一条新闻
	public static String getXinWenColumnNews4() {
		String url = http + "/xinhua/m/content-6-140000.html";
		return url;
		
	}
	// 获得新闻动态栏目下地市的一条新闻
	public static String getXinWenColumnNews3() {
		String url = http + "/xinhua/m/content-7-" + default_shiCode + ".html";
		return url;
		
	}
	// 获得新闻动态栏目下县区的一条新闻
	public static String getXinWenColumnNews2() {
		String url = http + "/xinhua/m/content-8-" + areaCode + ".html";
		return url;
		
	}
	// 获得官方发布栏目全国的一条新闻
	public static String getGFColumnNews4() {
		String url = http + "/xinhua/m/content-34-100000.html";
		return url;
		
	}
	// 获得官方发布栏目下山西的一条新闻
	public static String getGFColumnNews3() {
		String url = http + "/xinhua/m/content-33-140000.html";
		return url;
		
	}
	// 获得官方发布栏目下地市的一条新闻
	public static String getGFColumnNews2() {
		String url = http + "/xinhua/m/content-32-" + default_shiCode + ".html";
		return url;
		
	}
	// 获得官方发布栏目下县区的一条新闻
	public static String getGFColumnNews1() {
		String url = http + "/xinhua/m/content-31-" + areaCode + ".html";
		return url;
		
	}

	// 获得惠民生活栏目下的几条新闻
	public static String getHuiMingColumnNews() {
		String url = http + "/xinhua/m/huiming-4-" + areaCode + ".html";
		return url;
	}

	// 获得掌上政务栏目下的几条新闻
	public static String getZhengWuColumnNews() {
		String url = http + "/xinhua/m/zhengwu-3-" + areaCode + ".html";
		return url;
	}

	// 厅局栏目下的子局
	public static String getTingJu() {
		String url = http + "/xinhua/m/officeListByCode-120000.html";
		return url;
	}

	// --用户个人资料
	public static String getMydataInfo() {
		return http + "/xinhua/m/update-" + UserInfo.userId + ".html";
	}

	public static String getOpition() {

		return http + "/xinhua/m/feedback";
	}

	// --修改密码
	public static String setPassword() {
		return http + "/xinhua/m/modifyPwd.html";
	}

	// 修改头像
	public static String setPersonalPic() {
		return http + "/xinhua/m/imageSave.html";
	}

	// 获取舆情快报下的新闻列表
	public static String getYuQingKuaiBaoNewsList(String... params) {
		return http + "/xinhua/m/articlelists-" + UserInfo.userId + "-"
				+ params[0] + "-" + areaCode + ".html" + "?pageNo=" + params[1]
				+ "&pageSize=10";
	}

	// 获取用户自然资料
	public static String getmodifyUser() {
		return http + "/xinhua/m/modifyUser.html";
	}

	// 获取用户申请资料修改
	public static String getmodifyUserApply() {
		return http + "/xinhua/m/modifyUserApply.html";
	}

	// 获取用户自然资料展示
	public static String getShowUser() {
		return http + "/xinhua/m/userShow.html";
	}

	// 获取投票
	public static String getVote() {
		return http + "/xinhua/m/getPostTitle.html";
	}

	// 获取随手拍的图片展示
	public static String getShowSGVPics() {
		return http + "/xinhua/m/getPhotosHand.html";
	}

	// 上传用户拍的随手拍照片及位置信息
	public static String getUploadSGVPics() {
		return http + "/xinhua/m/photosHand.html";
	}

	// 返回投票结果
	public static String postVote() {
		return http + "/xinhua/m/setPostChooseHits.html";
	}

	// 找回密码
	public static String findPassword() {
		return http + "/xinhua/m/findPassword.html";
	}

	// 消息记录
	public static String getNewsPush() {
		return "http://211.142.30.180:7001/jmoco_push/not/authority/xinhua";
	}

	// --手机信息
	public static String getPhoneInfo() {
		return http + "/xinhua/m/phoneInformation";

	}

	// --用户行为公用接口
	public static String postUserbehaviorPhoneInfo() {
		return http + "/xinhua/m/userBehavior";
	}
}
