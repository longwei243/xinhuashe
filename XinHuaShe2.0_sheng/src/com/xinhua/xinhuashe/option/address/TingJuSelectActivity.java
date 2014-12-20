package com.xinhua.xinhuashe.option.address;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.threadpool.IActivity;
import com.android.threadpool.Task;
import com.google.gson.reflect.TypeToken;
import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.domain.Category;
import com.xinhua.xinhuashe.domain.TingJu;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.address.adapter.GridView3TingJuAdapter;
import com.xinhua.xinhuashe.option.address.service.TingJuService;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.service.ParentHandlerService;
import com.xinhua.xinhuashe.service.TaskID;
import com.xinhua.xinhuashe.view.GridViewInScrollView;

public class TingJuSelectActivity extends ParentActivity implements IActivity{
	private GridViewInScrollView tingju_gridview;
	private GridView3TingJuAdapter tingju_adapter;
	private String flag;
	private Button tingju_btn;
	List<String> dataList = new ArrayList<String>();
	private String code;
	private List<Category> categories = new LinkedList<Category>();
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_tingju_select;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MobileApplication.allIActivity.add(this);
		
		try{
			String result = MobileApplication.cacheUtils.getAsString(TingJuService.ColumnInfo_TingJu);
			categories = ParentHandlerService.gson
					.fromJson(result,
							new TypeToken<LinkedList<Category>>() {
					}.getType());
		}catch (Exception e) {
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	protected void setupViews() {

		tingju_gridview = (GridViewInScrollView) findViewById(R.id.tingju_gridview);
		tingju_adapter = new GridView3TingJuAdapter(TingJuSelectActivity.this, categories);
		tingju_gridview.setAdapter(tingju_adapter);
		tingju_adapter.notifyDataSetChanged();
		tingju_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				tingju_adapter.setSeclection(position);
				tingju_adapter.notifyDataSetChanged();
				TextView text = (TextView) view.findViewById(R.id.gridview3_item_text);
				flag = (String) text.getText();
				code = categories.get(position).getCode();
			}
		});
		
		tingju_btn = (Button) findViewById(R.id.tingju_btn);
		tingju_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RequestURL.tingJu = flag;
				RequestURL.tingJuCode = code;
				SlidingMenuControlActivity.main_header_text.setText(RequestURL.tingJu);

				finish();
			}
		});
	}

	@Override
	protected void initialized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void threadTask() {
		
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
	public void refresh(int arg0, Object... arg1) {
	}

}
