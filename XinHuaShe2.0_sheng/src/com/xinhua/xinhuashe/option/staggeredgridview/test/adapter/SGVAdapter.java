package com.xinhua.xinhuashe.option.staggeredgridview.test.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xinhua.xinhuashe.option.staggeredgridview.test.ShowFullPicActivity;
import com.xinhua.xinhuashe.service.MobileApplication;
import com.xinhua.xinhuashe.view.staggeredgridview.DynamicHeightImageView;
import com.xinhuanews.sheng.R;

public class SGVAdapter extends BaseAdapter{
	
	private final Random mRandom = new Random();
	private List<String> urls = null;
	private List<String> district=null;
	private Context context ;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SGVAdapter() {
    	
    }
    
    public SGVAdapter(Context context,ArrayList<Map<String,String>> urlsList) {
    	this.context = context;
    	urls = new ArrayList<String>();
		district=new ArrayList<String>();
		for (int i = 0; i < urlsList.size(); i++) {
			urls.add((String)urlsList.get(i).get("SGVPicsUrl"));
			district.add((String)urlsList.get(i).get("SGVPicsDistrict"));
		}
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urls.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return urls.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sgv_item_new, null);
			
			vh = new ViewHolder();
			vh.imageView = (DynamicHeightImageView) convertView.findViewById(R.id.svg_imageview);
			vh.svg_picDistrict=(TextView) convertView.findViewById(R.id.svg_picdirect);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		double positionHeight = getPositionRatio(position);
		vh.imageView.setHeightRatio(positionHeight);
		if(position % 2 == 0) {
			vh.imageView.setBackgroundColor(R.color.red);
		}else {
			vh.imageView.setBackgroundColor(R.color.gray);
		}
		vh.imageView.setId(position);
		vh.imageView.setOnClickListener(onClickListener);
		vh.svg_picDistrict.setText(district.get(position));
		BitmapUtils bitmapUtils=new BitmapUtils(MobileApplication.mobileApplication);
		bitmapUtils.display(vh.imageView, urls.get(position));
		Log.i(MobileApplication.TAG, urls.get(position));
		return convertView;
	}
	
	private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
	
	public class ViewHolder {
		DynamicHeightImageView imageView;
		TextView svg_picDistrict;
	}
	
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent=new Intent(context,ShowFullPicActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("picpath", urls.get(v.getId()));
			intent.putExtras(bundle);
			context.startActivity(intent);
			Log.i(MobileApplication.TAG,"tupianlianjie--"+v.getId()+"----"+urls.get(v.getId()));
		}
	};
	
}
