package com.sage.hedonicmentality.view;


import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sage.hedonicmentality.R;


public class SubmittedSuccessfullyDiaLog extends Dialog{

	private Context mContext;
	private ImageView img_top;
	private Button btn_one;
	private Button btn_two;
	private Button btn_bottom;
	private CountDownTimer countDownTimer;

	public SubmittedSuccessfullyDiaLog(Context context) {
		super(context, R.style.iphone_progress_dialog);
		init(context);
	}
	public SubmittedSuccessfullyDiaLog(Context context,String hit){
		super(context, R.style.iphone_progress_dialog);
		init(context);
		((TextView)findViewById(R.id.txt_hit)).setText(hit);
	}

	private void init(Context context){
		this.mContext = context;
		setContentView(R.layout.submittedsuccessfully);
		timer();
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		window.setAttributes(params);
		window.setBackgroundDrawableResource(R.color.transparent);
	}
	private void timer(){
		countDownTimer=new CountDownTimer(1*1000,1*1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}
			@Override
			public void onFinish() {
				dismiss();
			}
		};
		countDownTimer.start();
	}
}
