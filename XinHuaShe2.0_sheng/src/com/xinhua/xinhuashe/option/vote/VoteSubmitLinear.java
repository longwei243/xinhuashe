package com.xinhua.xinhuashe.option.vote;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinhua.xinhuashe.option.vote.view.VoteSubmitPercentView;
import com.xinhua.xinhuashe.option.vote.view.VoteSutmitCanvasView;
import com.xinhua.xinhuashe.util.Data;
import com.xinhua.xinhuashe.util.VoteSubmitTools;

public class VoteSubmitLinear extends LinearLayout {

	Activity mContext;
	// 投票标题
	TextView titleView;
	// 投票结果布局控件;
	VoteSutmitCanvasView canvasView;
	TextView addVoteView;
	VoteSubmitPercentView percentView;
	LinearLayout linear;
	// 布局参数
	LinearLayout.LayoutParams tlp;
	LinearLayout.LayoutParams lp;
	LinearLayout.LayoutParams plp;
	LinearLayout.LayoutParams clp;
	// 自定义工具类
	VoteSubmitTools convertTools;
	// 动画效果参数
	int numPercent;
	TranslateAnimation animation;
	// 测试数据
	ArrayList<String> mAnswers;
	ArrayList<Integer> mPercents = new ArrayList<Integer>();
	boolean mIsNeedDrawing = false;
	int mVoteIndex;
	int voteSum = 1;
	private Boolean isAddNum;
	public VoteSubmitLinear(Activity context, int voteIndex, ArrayList<String> answers, ArrayList<Integer> percents, boolean isNeedDrawing,boolean isAddNum) {
		super(context);
		mContext = context;
		this.isAddNum=isAddNum;
		convertTools = new VoteSubmitTools(mContext);
		mVoteIndex = voteIndex;
		mAnswers = answers;
		mIsNeedDrawing = isNeedDrawing;
		mPercents = convertTools.setDrawPercents(voteIndex, percents, isNeedDrawing,isAddNum);
		init();
	}

	/**
	 * 初始化页面
	 */
	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < mAnswers.size(); i++) {
			titleView = new TextView(mContext);
			titleView.setText(mAnswers.get(i));
			titleView.setTextSize(15);
			titleView.setTextColor(Color.BLACK);
			tlp = convertTools.dip2px(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

			addView(titleView, tlp);

			setDrawLinear(i);

			addView(linear);
		}
	}

	/**
	 * @param index
	 *            动态生成包含投票结果的矩形和百分比数值的线性布局
	 */
	private void setDrawLinear(final int index) {
		// 自定义linear
		linear = new LinearLayout(mContext);
		lp = convertTools.dip2px(LayoutParams.MATCH_PARENT, 30);
		linear.setOrientation(LinearLayout.HORIZONTAL);
		linear.setLayoutParams(lp);

		// 设置百分数TextView
		percentView = new VoteSubmitPercentView(mContext);
		plp = convertTools.dip2px(100, 35);

		// 绘制矩形增长View
		canvasView = new VoteSutmitCanvasView(mContext);
		clp = convertTools.dip2px(mPercents.get(index) * 3, 35);
		canvasView.setBackgroundColor(convertTools.setDrawColor(index));

		linear.addView(canvasView, clp);
		linear.addView(percentView, plp);

		canvasView.setDrawUpdate(convertTools.dip2px((mPercents.get(index) * 3)), mIsNeedDrawing);
		percentView.setDrawUpdate(mPercents.get(index), mIsNeedDrawing, convertTools);

		// 设置+1投票动态效果View
		if (mIsNeedDrawing && mVoteIndex == index&&isAddNum) {
			addVoteView = new TextView(mContext);
			addVoteView.setText("+1");
			addVoteView.setTextColor(Color.RED);
			linear.addView(addVoteView, plp);
			animation = new TranslateAnimation(0, 0, 10, -30);
			animation.setDuration(2000);
			addVoteView.setAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
				

				@Override
				public void onAnimationEnd(Animation animation) {
					// 此处若改为View.GONE则会出现canvasView绘制结束时percentView往左回移一小段距离的问题
					addVoteView.setVisibility(View.INVISIBLE);
				}
			});
		}
	}

}
