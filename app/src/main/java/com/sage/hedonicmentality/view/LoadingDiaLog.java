package com.sage.hedonicmentality.view;





import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sage.hedonicmentality.R;


public class LoadingDiaLog extends Dialog{

	private Context mContext;
	private ImageView img_top;
	private Button btn_one;
	private Button btn_two;
	private Button btn_bottom;

	public LoadingDiaLog(Context context) {
		super(context, R.style.iphone_progress_dialog);
		init(context);
	}
	public LoadingDiaLog(Context context,String prompt){
		super(context, R.style.iphone_progress_dialog);
		init(context);
		((TextView)findViewById(R.id.txt_loading)).setText(prompt);
	}

	private void init(Context context){
		this.mContext = context;
		setContentView(R.layout.dialog_loading);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;

		window.setAttributes(params);
		window.setBackgroundDrawableResource(R.color.transparent);
	}

}
