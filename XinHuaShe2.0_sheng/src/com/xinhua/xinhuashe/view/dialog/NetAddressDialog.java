package com.xinhua.xinhuashe.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.xinhuanews.sheng.R;
import com.xinhua.xinhuashe.request.RequestURL;
import com.xinhua.xinhuashe.service.MobileApplication;

/**
 * 网络地址设置对话框
 *
 */
public class NetAddressDialog extends ParentDialog {

	private Context context;
	private static EditText netaddress_ip, netaddress_port;
	private static NetAddressDialog dialog;

	public NetAddressDialog(Context context) {
		super(context, onComfirmClickListener);
		this.context = context;
		setContentView();
		setTitle(R.string.setting_netaddress_title);
		dialog = this;
	}

	public void setContentView() {
		View setting_netaddress = LayoutInflater.from(context).inflate(
				R.layout.setting_netaddress, null);
		String netaddress = MobileApplication.preferences.getString("http", "");
		netaddress_ip = (EditText) setting_netaddress.findViewById(R.id.setting_netaddress_ip_EditText);
		netaddress_port = (EditText) setting_netaddress.findViewById(R.id.setting_netaddress_port_EditText);
		netaddress_ip.setText(netaddress.split(":")[0]);
		netaddress_port.setText(netaddress.split(":")[1]);
		CheckBox checkBox = (CheckBox) setting_netaddress.findViewById(R.id.setting_netaddress_default_CheckBox);
		checkBox.setOnCheckedChangeListener(checkedChangeListener);
		super.setContentView(setting_netaddress);
	}


	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
			if (checked) {
				String default_http = MobileApplication.preferences.getString("default_http", "");
				netaddress_ip.setEnabled(false);
				netaddress_ip.setText(default_http.split(":")[0]);
				netaddress_port.setEnabled(false);
				netaddress_port.setText(default_http.split(":")[1]);
			} else {
				netaddress_ip.setEnabled(true);
				netaddress_ip.setText("");
				netaddress_port.setEnabled(true);
				netaddress_port.setText("");
			}
		}
	};

	private static android.view.View.OnClickListener onComfirmClickListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View view) {
			String ip = netaddress_ip.getText().toString().trim();
			String http = ip + ":" + netaddress_port.getText().toString().trim();
			MobileApplication.preferences.edit().putString("http", http).putString("xmppHost", ip).commit();
			RequestURL.http = "http://" + MobileApplication.preferences.getString("http", "");
			dialog.cancel();
		}
	};

}
