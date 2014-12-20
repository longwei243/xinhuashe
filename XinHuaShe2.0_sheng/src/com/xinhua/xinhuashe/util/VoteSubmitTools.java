package com.xinhua.xinhuashe.util;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.LinearLayout;

import com.xinhuanews.sheng.R;

/**
 * @author wisdomhu 自定义模块工具类
 */
public class VoteSubmitTools {

	Activity mContext;
	float scale;// 屏幕密度
	ArrayList<Integer> mPercents = new ArrayList<Integer>();
	int voteSum ;

	public VoteSubmitTools(Activity context) {
		mContext = context;
	}

	/**
	 * @param width
	 * @param height
	 * @return 返回dp转为 px(像素)之后的宽和高
	 */
	public LinearLayout.LayoutParams dip2px(int width, int height) {
		scale = mContext.getResources().getDisplayMetrics().density;
		return new LinearLayout.LayoutParams((int) (width * scale + 0.5f), (int) (height * scale + 0.5f));
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(int width) {

		return (int) (width * scale + 0.5f);
	}

	/**
	 * @param index
	 *            根据索引值设置绘制view的背景色
	 */
	public int setDrawColor(int index) {
		switch (index % 4) {
		case 0:
			return mContext.getResources().getColor(R.color.vote_blue);
		case 1:
			return mContext.getResources().getColor(R.color.vote_green);
		case 2:
			return mContext.getResources().getColor(R.color.vote_red);
		case 3:
			return mContext.getResources().getColor(R.color.vote_yellow);
		default:
			return mContext.getResources().getColor(R.color.vote_blue);
		}
	}

	/**
	 * @param voteIndex
	 * @param percents
	 * @param isNeedDrawing
	 * @return 返回百分比数组 ，数据计算待优化
	 */
	public ArrayList<Integer> setDrawPercents(int voteIndex, ArrayList<Integer> percents, boolean isNeedDrawing,boolean isAddNum) {
		if (isNeedDrawing) {
			if(isAddNum){
				voteSum=1;
			}else{
				voteSum=0;
			}
			for (int i = 0; i < percents.size(); i++) {
				voteSum += percents.get(i);
			}
			if(voteSum!=0){
				for (int i = 0; i < percents.size(); i++) {
					if (isAddNum) {
						if (i == voteIndex){
							mPercents.add((int) ((percents.get(i) + 1) * 100 / voteSum));
						}else{
							mPercents.add((int) ((percents.get(i) ) * 100 / voteSum));
						}
					}else{
						mPercents.add((int) ((percents.get(i) ) * 100 / voteSum));
					}
//					if (i == voteIndex){}
//					else
//						mPercents.add((int) (percents.get(i) * 100 / voteSum));
				}
			}else{
				mPercents.add(0);
			}
		} else {
			mPercents = percents;
		}
		return mPercents;
	}

}
