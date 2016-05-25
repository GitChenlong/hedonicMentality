package com.sage.hedonicmentality.fragment.Me;

import android.view.View;
import android.widget.RelativeLayout;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.HMRecord;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.TimeUtil;
import com.sage.hedonicmentality.view.AppdLineGraphicView;
import com.sage.hedonicmentality.view.LineGraphicView;
import com.sage.hedonicmentality.view.StolicLineGraphicView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by Administrator on 2015/11/23.
 */
public class FragmentMyHMCurve extends BaseFragment {
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.line_graphic)
    LineGraphicView line_graphic;
    @Bind(R.id.line_appdgraphic)
    AppdLineGraphicView line_appdgraphic;
    @Bind(R.id.line_stolicdgraphic)
    StolicLineGraphicView line_stolicdgraphic;
    private List<HMRecord> myhmdata = new ArrayList<HMRecord>();
    private ArrayList<Double> datasleep  = new ArrayList<Double>();
    private ArrayList<Double> dataappetitle  = new ArrayList<Double>();
    private ArrayList<Double> appetitedata = new ArrayList<Double>();
    private ArrayList<Double> systolicdata  = new ArrayList<Double>();

    @Override
    public int getLayout() {
        return R.layout.fragment_curve;
    }

    @Override
    public void initActionbar() {
        layout_actionbar.setBackgroundResource(R.color.bg_title);
        tv_title.setText(R.string.yuexixian);
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        btn_rigth.setImageResource(R.mipmap.share);
        btn_rigth.setVisibility(View.VISIBLE);
        myhmdata.addAll(FragmentMyHM.myhmdata);
        btn_rigth.setOnClickListener(new View.OnClickListener() {
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
//                SharedPreferencesHelper.getInstance().Builder(getActivity());
//                String name = SharedPreferencesHelper.getInstance().getString(Contact.SH_USERNAME);
                oks.setTitle(getString(R.string.share_content));
                        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                        oks.setTitleUrl("http://www.kuailexinli.com/static/app/HappyPsychology.apk");
                // text是分享文本，所有平台都需要这个字段
                oks.setText(getString(R.string.share));
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
        setdata();
    }

    private void setdata() {
        setAppetite();
        setSleep();
        setAppetitedata();
        setSystolicdata();
        line_graphic.setData(datasleep, 12, 2);
        line_appdgraphic.setData(dataappetitle, 12, 2);
        line_stolicdgraphic.setData(systolicdata, appetitedata, 300, 50);
    }
    private void setAppetitedata(){
        int min = Integer.parseInt(TimeUtil.getStringNowMin(myhmdata.get(myhmdata.size()-1).getCreatetime()));
        for(int j= 0;j< min;j++){
            if(j!=0){
                appetitedata.add(0.0);
            }
        }
        for (int i = myhmdata.size()-1;i > -1;i--){
            Double a = Double.parseDouble(myhmdata.get(i).getDiastolic());
            appetitedata.add(a);
        }
    }
    private void setSystolicdata(){
        int min = Integer.parseInt(TimeUtil.getStringNowMin(myhmdata.get(myhmdata.size()-1).getCreatetime()));
        for(int j= 0;j< min;j++){
            if(j!=0){
                systolicdata.add(0.0);
            }
        }
        for (int i = myhmdata.size()-1;i > -1;i--){
            Double a = Double.parseDouble(myhmdata.get(i).getSystolic());
            systolicdata.add(a);
        }
    }

    private void setAppetite(){
        int min = Integer.parseInt(TimeUtil.getStringNowMin(myhmdata.get(myhmdata.size()-1).getCreatetime()));
        for(int j= 0;j< min;j++){
            if(j!=0){
                dataappetitle.add(0.0);
            }
        }
        for (int i = myhmdata.size()-1;i > -1;i--){
            Double a = Double.parseDouble(myhmdata.get(i).getAppetite());
            dataappetitle.add(a*2);
        }
    }
    private void setSleep(){
        int min = Integer.parseInt(TimeUtil.getStringNowMin(myhmdata.get(myhmdata.size()-1).getCreatetime()));
        for(int j= 0;j< min;j++){
            if(j!=0){
                datasleep.add(0.0);
            }
        }
        for (int i = myhmdata.size()-1;i > -1;i--){
           Double a = Double.parseDouble(myhmdata.get(i).getSleep());
           datasleep.add(a*2);
        }
    }
}
