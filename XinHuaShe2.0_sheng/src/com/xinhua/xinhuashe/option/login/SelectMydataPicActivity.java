package com.xinhua.xinhuashe.option.login;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.util.Data;

//选择文件操作类
public class SelectMydataPicActivity extends Activity implements OnClickListener {

	// 使用照相机拍照获取图片
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	// 使用相册中的图片
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	// 从Intent获取图片路径的KEY
	public static final String KEY_PHOTO_PATH = "photo_path";
	private static final String TAG = "SelectPicActivity";
	private LinearLayout select_pic_dialog_layout;
	private ImageView select_pic_left_iv,select_pic_right_iv;
	/** 获取到的图片路径 */
	private String picPath;
	private Intent lastIntent;
	private Uri photoUri;

	private TextView select_pic_title_textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pic_layout);
		select_pic_dialog_layout = (LinearLayout) findViewById(R.id.select_pic_dialog_layout);
		select_pic_dialog_layout.setOnClickListener(this);
		select_pic_left_iv = (ImageView) findViewById(R.id.select_pic_left_iv);
		select_pic_left_iv.setOnClickListener(this);
		select_pic_right_iv = (ImageView) findViewById(R.id.select_pic_right_iv);
		select_pic_right_iv.setOnClickListener(this);
		select_pic_title_textView=(TextView) findViewById(R.id.select_pic_title_textView);
		select_pic_title_textView.setText(Data.SelectPic_Title);
		lastIntent = getIntent();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_pic_dialog_layout:
			finish();
			break;
		case R.id.select_pic_left_iv:
			takePhoto();
			break;
		case R.id.select_pic_right_iv:
			pickPhoto();
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			doPhoto(requestCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_SHORT).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		Log.i(TAG, "imagePath = " + picPath);
		/*
		 * if(picPath != null && ( picPath.endsWith(".png") ||
		 * picPath.endsWith(".PNG") ||picPath.endsWith(".jpg")
		 * ||picPath.endsWith(".JPG") ))
		 */
		if (picPath != null) {
			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		} else {
			Toast.makeText(this, "选择文件不正确!", Toast.LENGTH_SHORT).show();
		}
	}
}
