package com.sage.hedonicmentality.fragment.breath;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.ui.simple.ActivityBuyDevice;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by Sage on 2015/7/27.
 */
public class FragmentDiaFinish extends DialogFragment {

    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_duration)
    TextView tv_duration;
    @Bind(R.id.tv_hr)
    TextView tv_hr;
    @Bind(R.id.tv_percent)
    TextView tv_percent;
    @Bind(R.id.tv_bpm)
    TextView tv_bpm;
    @Bind(R.id.tv_score)
    TextView tv_score;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.tv_share)
    TextView tv_share;
    @Bind(R.id.tv_endtxt)
    TextView tv_endtxt;
    @Bind(R.id.tv_continue)
    TextView tv_continue;
    @Bind(R.id.tv_exit)
    TextView tv_exit;
    @Bind(R.id.tv_one_hr)//第一界面hrv
    TextView tv_one_hr;
    @Bind(R.id.tv_one_hp)//第一界面生命值
    TextView tv_one_hp;
    @Bind(R.id.tv_one_time)//第一界面时长
    TextView tv_one_time;
    @Bind(R.id.tv_weitime)//未完成时长
    TextView tv_weitime;
    @Bind(R.id.tv_close)
    TextView tv_close;
   @Bind(R.id.layout_exit)
    LinearLayout layout_exit;
    @Bind(R.id.layout_finish)
    LinearLayout layout_finish;
    @Bind(R.id.iv_one_level)
    ImageView iv_one_level;
    @Bind(R.id.iv_wawa)
    ImageView iv_wawa;
    @Bind(R.id.iv_wa)
    ImageView iv_wa;
    @Bind(R.id.layout_show_back)
    LinearLayout layout_show_back;//第一视图
    @Bind(R.id.layout_show_one)
    LinearLayout layout_show_one;//第二视图
    private int tag,duration,hr,percent,bpm,score,time,ratio,level;

    public int isshowtwo = 0;
    /**tag=1 半路退出，tag=0,正常结束*/
    public static FragmentDiaFinish create(int tag,int duration,int hr,int percent,int bpm,int score,int time,int ratio,int level){
        FragmentDiaFinish fragment=new FragmentDiaFinish();
        Bundle bundle=new Bundle();
        bundle.putInt("tag",tag);
        bundle.putInt("duration",duration);
        bundle.putInt("hr",hr);
        bundle.putInt("percent",percent);
        bundle.putInt("bpm",bpm);
        bundle.putInt("score",score);
        bundle.putInt("time",time);
        bundle.putInt("ratio",ratio);
        bundle.putInt("level",level);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentDiaFinish(){
        setStyle(STYLE_NO_TITLE, R.style.DiaScaleAnimationTheme);//取消标题
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dia_finish, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void show_hidden(boolean show){
        layout_show_back.setVisibility(!show ? View.VISIBLE : View.GONE);
        layout_show_one.setVisibility(show ? View.VISIBLE : View.GONE);
        if(tag == 1) {
            tv_close.setVisibility(View.GONE);
        }
//        layout_finish.setVisibility(show ? View.VISIBLE : View.GONE);
//        layout_exit.setVisibility(!show ? View.VISIBLE : View.GONE);
    }
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if(getArguments()!=null){
            tag=getArguments().getInt("tag");
            show_hidden(tag==0);
            duration=getArguments().getInt("duration");
            hr=getArguments().getInt("hr");
            percent=getArguments().getInt("percent");
            bpm=getArguments().getInt("bpm");
            score=getArguments().getInt("score");
            time=getArguments().getInt("time");
            ratio=getArguments().getInt("ratio");
            level=getArguments().getInt("level");
            if(tag == 0){
                isshowtwo = 1;
            }
        }
        switch (level){
            case 0:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level0);
                break;
            case 1:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level1);
                break;
            case 2:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level2);
                break;
            case 3:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level3);
                break;
            case 4:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level4);
                break;
            case 5:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level5);
                break;
            case 6:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level6);
                break;
            case 7:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level7);
                break;
            case 8:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level8);
                break;
            case 9:
                iv_one_level.setBackgroundResource(R.mipmap.bc_level9);
                break;
        }
//        tv_endtxt.setText(Html.fromHtml(getString(R.string.end_fen, duration)));
//        tv_duration.setText(Html.fromHtml(getString(R.string.end_fen, duration)));
        tv_time.setText(getString(R.string.end_time,duration));
        tv_hr.setText(hr+"");
        tv_percent.setText(percent+"%");
        tv_bpm.setText(bpm+"/min");
        tv_score.setText(score+"");
        tv_one_hr.setText(hr+"");
        tv_one_hp.setText("+"+score);
        tv_one_time.setText(duration+"分钟");
        tv_weitime.setText(Html.fromHtml(getString(R.string.end_fen, time)));
        if(ratio<41){
            tv_endtxt.setText(getString(R.string.dilg_ku));
            iv_wawa.setImageResource(R.mipmap.ku);
            iv_wa.setImageResource(R.mipmap.ku);
        }else
        if(ratio>40&&ratio<80){
            tv_endtxt.setText(getString(R.string.dilg_xyiban));
            iv_wawa.setImageResource(R.mipmap.endhead);
            iv_wa.setImageResource(R.mipmap.endhead);
        }else
        if(ratio>=80){
            tv_endtxt.setText(getString(R.string.dilg_xiao));
            iv_wawa.setImageResource(R.mipmap.xiao);
            iv_wa.setImageResource(R.mipmap.xiao);
        }
//        // 将bitmap转换成drawable
//        BitmapDrawable bd=new BitmapDrawable(bits);
//        iv_one_level.setBackgroundDrawable(bd);
        Window window=getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;//这里的坐标是相对默认重心 center而言的。
//        wl.y = getResources().getDisplayMetrics().heightPixels;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = getResources().getDisplayMetrics().widthPixels*9/10;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.dimAmount=0.5f;
        wl.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // 设置显示位置
        getDialog().onWindowAttributesChanged(wl);
        //window.setAttributes(wl);
        // 设置点击外围取消
        getDialog().setCanceledOnTouchOutside(tag!=0);
        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_show_back.setVisibility(View.GONE);
                layout_show_one.setVisibility( View.VISIBLE);
                tv_close.setVisibility(View.VISIBLE);
                isshowtwo = 1;
            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(getActivity());
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                Platform platform = ShareSDK.getPlatform(getActivity(), SinaWeibo.NAME);
                platform.SSOSetting(true);
                // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(getString(R.string.share_content));
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl("http://www.kuailexinli.com/static/app/HappyPsychology.apk");
                // text是分享文本，所有平台都需要这个字段
//                SharedPreferencesHelper.getInstance().Builder(getActivity());
//                String name = SharedPreferencesHelper.getInstance().getString(Contact.SH_USERNAME);
                oks.setText(getString(R.string.share_fen,(100-ratio)+"%"));
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//                oks.setImageUrl("http://www.kuailexinli.com/static/images/indeximg1.png");
                oks.setImageUrl("http://api.kuailexinli.com/static/image/108.png");
//                oks.setUrl();
                // url仅在微信（包括好友和朋友圈）中使用
//                oks.setUrl("http://www.xrong.cn");
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                oks.setComment(getString(R.string.share_content));
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite(getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://www.kuailexinli.com/static/app/HappyPsychology.apk");
                // 启动分享GUI
                oks.show(getActivity());
            }
        });
    }

    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isshowtwo == 1){
//            (ActivityBreath)getActivity().distroyWebview();
            getActivity().finish();
        }
    }

    @OnClick({R.id.tv_close,R.id.btn_again,R.id.btn_goon,R.id.btn_exit,R.id.btn_share,R.id.iv_pic})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                dismiss();
//                if(tag==0)
                    getActivity().finish();
                break;
            case R.id.btn_again:
                dismiss();
                ((ActivityBreath)getActivity()).resetBreath();
                break;
            case R.id.btn_goon:
                dismiss();
                break;
            case R.id.btn_exit:
                dismiss();
                getActivity().finish();
                break;
            case R.id.btn_share:
                dismiss();
                break;
            case R.id.iv_pic:
                //dismiss();
                FragmentDiaBuyDevice.create().show(getChildFragmentManager(), "buyDevice");
                break;
            case R.id.tv_share://分享

                break;
//            case R.id.tv_continue://继续
//                dismiss();
//                break;
//            case R.id.tv_endtxt://退出
//                layout_show_back.setVisibility(View.GONE);
//                layout_show_one.setVisibility( View.VISIBLE);
//                tv_close.setVisibility(View.VISIBLE);
//                break;
            default:
                break;
        }

    }


}
