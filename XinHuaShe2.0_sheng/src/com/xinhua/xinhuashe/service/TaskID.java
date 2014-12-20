package com.xinhua.xinhuashe.service;

/**
 * 任务ID - 必须唯一
 *
 * @author azuryleaves
 * @since 2013-12-4 下午1:48:20
 * @version 1.0
 *
 */
public class TaskID {

	public static final int TASK_LOGIN = 101; // 用户登录
	public static final int TASK_LOGOUT = 102; // 用户退出
	public static final int TASK_REGISTER = 103; // 用户注册
	public static final int TASK_UPDATE_CHECK = 104; // 检查更新
	public static final int TASK_UPDATE_DOWNLOAD = 105; // 更新下载
	
	public static final int TASK_WEATHER_INFO = 198; // 天气
	public static final int TASK_COLUMN_INIT = 199; // 栏目信息初始化
	
	public static final int TASK_HOMEPAGE_GRIDVIEW = 201; // 首页-九宫格
	public static final int TASK_HOMEPAGE_NEWS = 202; // 首页-新闻
	public static final int TASK_HOMEPAGE_NEWS_DETAIL = 203; // 首页-新闻详情
	public static final int TASK_HOMEPAGE_HOT_PIC = 204; // 首页-热图
	
	public static final int TASK_USER_INFO = 210; // 个人信息

//	public static final int TASK_NEWS_FRAGMENT_INIT = 1001; // 栏目信息初始化，并刷新页面
	public static final int TASK_NEWS_LIST = 1002; // 新闻列表
	public static final int TASK_NEWS_DETAIL = 1003; // 新闻详情
	public static final int TASK_NEWS_DETAIL_ATTENTION = 1004; // 新闻详情-收藏
	public static final int TASK_NEWS_DETAIL_PRAISE = 1005; // 新闻详情-点赞
	public static final int TASK_NEWS_DETAIL_SHARE = 1006; // 新闻详情-分享
	public static final int TASK_NEWS_DETAIL_COMMENT = 1007; // 新闻详情-评论
	
	public static final int TASK_SAY_LIST = 2001; // 有话要说-列表
	public static final int TASK_SAY_DETAIL = 2002; // 有话要说-详情
	public static final int TASK_SAY_TARGET = 2003; // 有话要说-选人
	public static final int TASK_SAY_COMMIT = 2004; // 有话要说-提交
	public static final int TASK_SAY_TYPE = 2005; // 有话要说-类型
	public static final int TASK_SAY_COMMENT_NULL_FILE = 2006; // 有话要说-无图
	public static final int TASK_COLUMN_XINWENDONGTAI=3001;//新闻动态下的栏目
public static final int TASK_COLUMN_HUIMINGSHENGHUO = 3002;//惠民生活下的栏目
	public static final int TASK_COLUMN_ZHANGSHANGZHENGWU = 3003;//掌上政务下的栏目
	public static final int TASK_COLUMN_HUDONGFENXIANG = 3004;//互动分享下的栏目
	public static final int TASK_COLUMN_BIANMINXINXI=3005;//便民信息下的栏目
	//public static final int TASK_CHANGEPASSWORD_OLDPASSWORD=2009;//验证原始密码
	public static final int TASK_COLUNMN_NEWS = 4003;
	public static final int TASK_MYDATA_INFO= 6007; // 个人资料
	public static final int FEED_BACK_SEND= 6008; // 意见反馈
	public static final int TASK_CHANGEPASSWORD_OLDPASSWORD=2009;//验证原始密码
	public static final int TASK_CHANGEPASSWORD_NEWPASSWORD=2010;//修改新密码
	public static final int TASK_HOMEPAGE_NEWS1 = 4001;
	public static final int TASK_HOMEPAGE_NEWS2 = 4002;
	public static final int TASK_COLUMN_TINGJU = 4004;

	public static final int TASK_GETQRCODE = 5001;//获取二维码
	public static final int TASK_ZHONGGUOWANGSHI = 9001;
	public static final int TASK_COLUNMN_NEWS_HUIMING = 9002;
	public static final int TASK_COLUNMN_NEWS_ZHENGWU = 9003;
	
	public static final int TASK_SHOW_PERSON_DATA = 6009;//展示用户个人信息
	public static final int TASK_CHANGE_PERSON_DATA = 6010;//修改个人资料
	public static final int TASK_MODIFYUSERAPPLY_PERSON_DATA = 6011;//提交申请修改个人资料
	public static final int TASK_VOTE = 6012;//投票
	public static final int TASK_POST_VOTE = 6013;//返回投票结果
	public static final int TASK_FIND_PASSWORD = 6014;//返回投票结果
	
	public static final int TASK_SHOW_SGV_PICS=5002;//获取随手拍展示的图片
	public static final int TASK_UPLOAD_SGV_PICS=5003;//上传随手拍用户拍的图片
	public static final int TASK_COLUMN_YUQING = 3006;
	
	public static final int TASK_PHONE_INFO= 2008; // 手机信息
	
	public static final int TASK_POST_USERBEHAVIORINFO=2009;//用户行为
	public static final int TASK_COLUNMN_NEWS1 = 4005;
	public static final int TASK_COLUNMN_NEWS2 = 4006;
	public static final int TASK_COLUNMN_NEWS3 = 4007;
	public static final int TASK_COLUNMN_NEWS4 = 4008;
	public static final int TASK_COLUNMN_NEWS_GF1 = 4009;
	public static final int TASK_COLUNMN_NEWS_GF2 = 4010;
	public static final int TASK_COLUNMN_NEWS_GF3 = 4011;
	public static final int TASK_COLUNMN_NEWS_GF4 = 4012;
}
