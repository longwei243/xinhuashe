package com.xinhua.xinhuashe.option.address;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.option.SlidingMenuControlActivity;
import com.xinhua.xinhuashe.option.address.adapter.AddressToCode;
import com.xinhua.xinhuashe.option.address.adapter.GridView3Adapter;
import com.xinhua.xinhuashe.parentclass.ParentActivity;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.view.GridViewInScrollView;

public class ShengShiAddressSelectActivity extends ParentActivity{
	private GridViewInScrollView sheng_gridview;
	private GridView3Adapter sheng_adapter;
	
	private GridViewInScrollView shi_gridview;
	private GridView3Adapter shi_adapter;
	
	
	private Button shengshi_btn;
	
	private String flag = "晋中";
	
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_shengshi_address_select;
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

		SlidingMenuControlActivity.main_header_title_TextView.setText("地市热点");
		
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
				
			}
		});
			
		shengshi_btn = (Button) findViewById(R.id.shi_btn);
		shengshi_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RequestURL.shi = flag;
				RequestURL.shiCode = AddressToCode.addressToCode(flag);
				System.out.println("选择后的市编码为："+RequestURL.shiCode);
				SlidingMenuControlActivity.main_header_text.setText(RequestURL.shi);

				finish();
			}
		});
		
	
	
	}

}
