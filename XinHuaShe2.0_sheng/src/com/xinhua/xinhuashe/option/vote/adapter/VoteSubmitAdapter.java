package com.xinhua.xinhuashe.option.vote.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.UserInfo;
import com.xinhua.xinhuashe.domain.VoteSubmitItem;
import com.xinhua.xinhuashe.option.vote.VoteSubmitFragment;
import com.xinhua.xinhuashe.option.vote.VoteSubmitLinear;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.util.Data;

public class VoteSubmitAdapter extends PagerAdapter {

	Activity mContext;
	// 传递过来的页面view的集合
	public List<View> viewItems;
	// 每个item的页面view
	public View convertView;
	// 传递过来的所有数据
	int position;
	ArrayList<VoteSubmitItem> dataItems;
	private String isVote;
	ViewHolder holder = null;
	private String isVoteMessage;
	ArrayList<String> answerList;
	ArrayList<Integer> percentList;
	public VoteSubmitAdapter(Activity context, List<View> viewItems,
			ArrayList<VoteSubmitItem> dataItems) {
		mContext = context;
		this.viewItems = viewItems;
		this.dataItems = dataItems;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewItems.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		this.position = position;
		Log.i(MobileApplication.TAG, "caonima----" + position + "");
		holder = new ViewHolder();
		convertView = viewItems.get(position);
		answerList = new ArrayList<String>();
		percentList = new ArrayList<Integer>();

		for (int i = 0; i < dataItems.get(position)
				.getPostChooseList().size(); i++) {

			String answerStr = dataItems.get(position)
					.getPostChooseList().get(i).getContent();
			answerList.add(answerStr);
			percentList.add(dataItems.get(position)
					.getPostChooseList().get(i).getHits());
			Log.i(MobileApplication.TAG, answerList.get(i));
		}
		// 获取每个item页面的所有子控件
		holder.relative = (RelativeLayout) convertView
				.findViewById(R.id.vote_submit_item_relative);
		holder.image = (ImageView) convertView
				.findViewById(R.id.vote_submit_item_image);
		holder.title = (TextView) convertView
				.findViewById(R.id.vote_submit_item_title);
		holder.question = (TextView) convertView
				.findViewById(R.id.vote_submit_item_question);
		holder.date = (TextView) convertView
				.findViewById(R.id.vote_submit_item_date);
		holder.people = (TextView) convertView
				.findViewById(R.id.vote_submit_item_people);
		holder.linear = (LinearLayout) convertView
				.findViewById(R.id.vote_submit_item_linear);
		long questionId = dataItems.get(position).getId();
		isVoteMessage = MobileApplication.preferences.getString("isVoteMessage"
				+ questionId, "未投票");
		if (!isVoteMessage.equals(UserInfo.userId + questionId)) {
			// 第一个item页面动态添加投票选择按钮，点击后显示投票结果position == 0 &&
			if (holder.linear.getChildCount() == 0) {
				for (int i = 0; i < dataItems.get(position).getPostChooseList()
						.size(); i++) {

					Button submit = new Button(mContext);
					int ipad = submit.getWidth();
					submit.setId(position);
					System.out.println("----submit------"
							+ dataItems.get(position).getPostChooseList()
									.get(i).getChoose());
					submit.setText(dataItems.get(position).getPostChooseList()
							.get(i).getChoose()
							+ "."
							+ dataItems.get(position).getPostChooseList()
									.get(i).getContent());

					submit.setTextSize(15);
					submit.setBackgroundResource(R.drawable.vote_item_selector);
					submit.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
					submit.setPadding(10, 0, 10, 0);
					LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					holder.linear.addView(submit, blp);
					View view = new View(mContext);
					holder.linear.addView(view, 0, 30);
					submit.setOnClickListener(new SelectOnClickListener(holder,
							i));
				}
			}
		} else {
			// 移除投票按钮选项
			holder.linear.removeAllViews();
			// 绘制投票结果
			holder.linear.addView(new VoteSubmitLinear(mContext, 1,
					answerList, percentList, true,false));
		}
		/*
		 * else if (holder.linear.getChildCount() == 0) {
		 * holder.linear.addView(new VoteSubmitLinear(mContext, -1, dataItems
		 * .get(position).voteAnswers, dataItems.get(position).votePercents,
		 * false)); }
		 */
		// 设置每个item页面子控件的参数
		holder.relativeId = dataItems.get(position).getId();
		// 每个item设置监听
		/*
		 * holder.relative .setOnClickListener(new
		 * RelativeOnClickListener(position));
		 */
		holder.title.setText(dataItems.get(position).getTitle());
		// 因为是测试demo,不方便用公司缓存图片包，此处网络图片加载没找到什么好的方法
		holder.image.setImageResource(R.drawable.ic_launcher);
		// ImageLoader imageLoader = new ImageLoader();
		// imageLoader.setImageView(holder.image);
		// imageLoader.execute(dataItems.get(position).itemImage);
		holder.question.setText(dataItems.get(position).getContent());
		holder.date.setText(dataItems.get(position).getCreateDate());
		// 总人数
		holder.people.setText(dataItems.get(position).getHits() + "人参与");
		container.addView(viewItems.get(position));
		return viewItems.get(position);
	}

	@Override
	public int getCount() {
		return viewItems.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * @author wisdomhu 设置标题新闻跳转点击事件
	 */
	/*
	 * class RelativeOnClickListener implements OnClickListener {
	 * 
	 * private int rIndex;
	 * 
	 * public RelativeOnClickListener(int index) { rIndex = index; }
	 * 
	 * @Override public void onClick(View v) { Toast.makeText(mContext,
	 * "跳转到详情页面  itemId = " + dataItems.get(rIndex).itemId,
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * }
	 */

	/**
	 * @author wisdomhu 设置投票点击事件
	 */
	class SelectOnClickListener implements OnClickListener {
		private ViewHolder sHolder;
		private int voteIndex;

		public SelectOnClickListener(ViewHolder holder, int index) {
			sHolder = holder;
			voteIndex = index;
		}

		@Override
		public void onClick(View v) {

			int resultPosition = v.getId();
			/*ArrayList<String> answerList = new ArrayList<String>();
			ArrayList<Integer> percentList = new ArrayList<Integer>();*/
			answerList.clear();
			percentList.clear();
			for (int i = 0; i < dataItems.get(resultPosition)
					.getPostChooseList().size(); i++) {

				String answerStr = dataItems.get(resultPosition)
						.getPostChooseList().get(i).getContent();
				answerList.add(answerStr);
				percentList.add(dataItems.get(resultPosition)
						.getPostChooseList().get(i).getHits());
				Log.i(MobileApplication.TAG, answerList.get(i));
			}
//			isVoteMessage = MobileApplication.preferences.getString(
//					"isVoteMessage" + questionId, "未投票");
//			if (isVoteMessage.equals(UserInfo.userId + questionId)) {
//				// 发送请求
//			}
			long answerId = dataItems.get(resultPosition).getPostChooseList()
					.get(voteIndex).getId();
			long questionId = dataItems.get(resultPosition).getId();
			VoteSubmitFragment.postChoose(answerId, questionId);
			
			// 人数增加
			holder.people
					.setText(dataItems.get(resultPosition).getHits() + 1 + "人参与");
			// 移除投票按钮选项
			sHolder.linear.removeAllViews();
			// 绘制投票结果
			sHolder.linear.addView(new VoteSubmitLinear(mContext, voteIndex,
					answerList, percentList, true,true));

		}
	}

	/**
	 * @author wisdomhu 自定义类
	 */
	class ViewHolder {
		long relativeId;
		LinearLayout linear;
		RelativeLayout relative;
		TextView title;
		ImageView image;
		TextView question, date, people;
		TextView answer;
	}

}
