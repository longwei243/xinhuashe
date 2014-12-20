package com.xinhua.xinhuashe.option.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.address.adapter.AddressToCode;
import com.xinhua.xinhuashe.option.address.adapter.GridView3Adapter;
import com.xinhua.xinhuashe.option.xinwendongtai.XinWenDongTaiNewsFragment;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.parentclass.ParentFragment;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.view.GridViewInScrollView;

public class AddressSelectActivity extends ParentActivity{
	private GridViewInScrollView sheng_gridview;
	private GridView3Adapter sheng_adapter;
	
	private GridViewInScrollView shi_gridview;
	private GridView3Adapter shi_adapter;
	
	private GridViewInScrollView xian_gridview;
	private GridView3Adapter xian_adapter;
	
	private Button xian_btn;
	private Button shi_btn;
	
	private String flag = "晋中";

	private String str = "寿阳";
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_address_select;
	}

	
	@Override
	protected void initialized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void threadTask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupViews() {
		
//		SlidingMenuControlActivity.main_header_title_TextView.setText("县区快报");
		
		sheng_gridview = (GridViewInScrollView) findViewById(R.id.sheng_gridview);
		sheng_adapter = new GridView3Adapter(this);
		sheng_gridview.setAdapter(sheng_adapter);
		sheng_adapter.setSeclection(0);
		sheng_adapter.notifyDataSetChanged();
		
		
		shi_gridview = (GridViewInScrollView) findViewById(R.id.shi_gridview);
		shi_adapter = new GridView3Adapter(this, "山西");
		shi_gridview.setAdapter(shi_adapter);
		shi_adapter.notifyDataSetChanged();
		shi_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				shi_adapter.setSeclection(position);
				shi_adapter.notifyDataSetChanged();
				TextView text = (TextView) view.findViewById(R.id.gridview3_item_text);
				flag = (String) text.getText();
				xian_adapter = new GridView3Adapter(AddressSelectActivity.this, flag);
				xian_gridview.setAdapter(xian_adapter);
				xian_adapter.notifyDataSetChanged();
			}
		});
		
		shi_btn = (Button) findViewById(R.id.shi_btn);
		shi_btn.setOnClickListener(new OnClickListener() {
			//点击市确定按钮
			@Override
			public void onClick(View v) {
				RequestURL.xianarea = flag;
				RequestURL.xianCode = AddressToCode.addressToCode(flag);
				SlidingMenuControlActivity.main_header_text.setText(RequestURL.xianarea);
				finish();
//				Toast.makeText(AddressSelectActivity.this, "请选择具体县", Toast.LENGTH_LONG).show();
			}
		});
		
		xian_gridview = (GridViewInScrollView) findViewById(R.id.xian_gridview);
		xian_adapter = new GridView3Adapter(this, flag);
		xian_gridview.setAdapter(xian_adapter);
		xian_gridview.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				xian_adapter.setSeclection(position);
				xian_adapter.notifyDataSetChanged();
				TextView text = (TextView) view.findViewById(R.id.gridview3_item_text);
				str = (String) text.getText();
			}
		});
		
		xian_btn = (Button) findViewById(R.id.xian_btn);
		xian_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RequestURL.xianarea = str;
				RequestURL.xianCode = AddressToCode.addressToCode(str);
				SlidingMenuControlActivity.main_header_text.setText(RequestURL.xianarea);
				finish();
			}
		});
		
	
	}

}
