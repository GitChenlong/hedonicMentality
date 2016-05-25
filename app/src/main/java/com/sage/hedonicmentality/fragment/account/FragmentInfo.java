package com.sage.hedonicmentality.fragment.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.ui.simple.BreathSetting;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;
import com.sage.hedonicmentality.utils.Util;
import com.sage.hedonicmentality.utils.UtilSnackbar;
import com.sage.libimagechoose.api.ChooserType;
import com.sage.libimagechoose.api.ChosenImage;
import com.sage.libimagechoose.api.ImageChooserListener;
import com.sage.libimagechoose.api.ImageChooserManager;
import com.sage.libimagechoose.api.utils.UtilPicCut;
import com.sage.libwheelview.widget.SelectBirthdayPopupWindow;
import com.sage.libwheelview.widget.SelectHeightPopupWindow;
import com.sage.libwheelview.widget.SelectWeightPopupWindow;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/27.
 */
public class FragmentInfo extends BaseFragment implements ImageChooserListener{
    @Bind(R.id.tv_sex)
    public TextView tv_sex;
    @Bind(R.id.tv_birthday)
    public TextView tv_birthday;
    @Bind(R.id.tv_height)
    public TextView tv_height;
    @Bind(R.id.tv_weight)
    public TextView tv_weight;
    @Bind(R.id.et_nickName)
    public EditText et_nickName;
    @Bind(R.id.btn_header)
    public ImageView btn_header;

    private int checkedItem=0;/**性别的选择itemID,0男1女*/
    private String[] sexChoose;/**性别数组，男和女*/
    private ImageChooserManager manager;
    private File outCropFile;/**裁剪后的头像地址*/
    private String birth="2000-1-1";/**生日*/
    private String height="170";/**身高*/
    private String weight="60";/**体重*/
    private int sexindex = 0;
    private Handler mHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch(msg.what){
                        case 8:
                            birth = msg.getData().getString("birthday");
                            tv_birthday.setText(birth);
                            break;
                        case 6:
                            height = msg.getData().getString("height");
                            tv_height.setText(getString(R.string.height_unit,height));
                            break;
                        case 7:
                            weight = msg.getData().getString("weight");
                            tv_weight.setText(getString(R.string.weight_unit,weight));
                            break;
                    }
                }
            };
    @Override
    public int getLayout() {
        return R.layout.fragment_info;
    }

    @Override
    public void initActionbar() {
        btn_left.setVisibility(View.INVISIBLE);
        tv_title.setText(R.string.title_add_info);
        sexChoose=getResources().getStringArray(R.array.item_sex);
        tv_birthday.setText(birth);
        tv_height.setText(getString(R.string.height_unit,height));
        tv_weight.setText(getString(R.string.weight_unit,weight));
    }


    @OnClick({R.id.btn_go,R.id.tv_skip_go,R.id.layout_sex,R.id.layout_birthday,R.id.btn_header,R.id.layout_height,R.id.layout_weight})
    public void infoClick(View view){
        switch(view.getId()){
            case R.id.btn_go:
                startHappy();
                break;
            case R.id.tv_skip_go:
                nextPage();
                break;
            case R.id.layout_sex:
                chooseSex();
                break;
            case R.id.layout_birthday:
                chooseBirthday();
                break;
            case R.id.btn_header:
                showAlert();
                break;
            case R.id.layout_height:
                chooseHeight();
                break;
            case R.id.layout_weight:
                chooseWeight();
                break;
        }
    }

    private void chooseBirthday() {
        SelectBirthdayPopupWindow selectBirthdayPopupWindow=new SelectBirthdayPopupWindow(getActivity(),tv_birthday.getText().toString(),mHandler);
        selectBirthdayPopupWindow.showAtLocation(getView().findViewById(R.id.layout_root), Gravity.BOTTOM, 0, 0);
    }
    private void chooseHeight(){
        SelectHeightPopupWindow selectHeightPopupWindow=new SelectHeightPopupWindow(getActivity(),height,mHandler);

        selectHeightPopupWindow.showAtLocation(getView().findViewById(R.id.layout_root),Gravity.BOTTOM,0,0);
    }

    private void chooseWeight(){
        SelectWeightPopupWindow selectWeightPopupWindow=new SelectWeightPopupWindow(getActivity(),weight,mHandler);
        selectWeightPopupWindow.showAtLocation(getView().findViewById(R.id.layout_root),Gravity.BOTTOM,0,0);
    }
    FragmentDiaChoose fragmentChooseSex;
    private void chooseSex(){
        if(fragmentChooseSex!=null){
            fragmentChooseSex.show(getChildFragmentManager(),"choose sex");
            return;
        }
        fragmentChooseSex= FragmentDiaChoose.create(1);
        fragmentChooseSex.setmChooseListener(new FragmentDiaChoose.ChooseClickListener() {
            @Override
            public void click(int index) {
                checkedItem = index;
                tv_sex.setText(sexChoose[index]);
            }
        });

        fragmentChooseSex.show(getChildFragmentManager(),"choose sex");
    }
    private void startHappy(){
        final String nickName=et_nickName.getText().toString().trim();
        String sex=tv_sex.getText().toString().trim();
        final String birthday = tv_birthday.getText().toString().trim();
        if(sex.equals("男")){
            sexindex = 1;
        }else
        if(sex.equals("女")){
            sexindex = 2;
        }
        //上传个人信息
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
        Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
        Http.SelfSetting(phone,pwd,nickName,sexindex+"",birthday,height,weight,"","",new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Util.cancelProgressFor(getActivity());
                String v = responseInfo.result;
                byte[] bytes = v.getBytes();
                try {
                    v = new String(bytes, "UTF-8");
                    JSONObject object = new JSONObject(v);
                    int info = object.getInt("info");
                    if (info != 1) {
                        UtilSnackbar.showSimple(btn_header, object.getString("tip"));
                        return;
                    }
                    //接口调用成功后
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_USERNAME, nickName);
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Birthday, birthday);
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Sex, sexindex + "");
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Weight, weight);
                    SharedPreferencesHelper.getInstance().putString(Contact.SH_Height, height);
                    nextPage();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.cancelProgressFor(getActivity());
                UtilSnackbar.showSimple(btn_header, s);
            }
        });

    }
    private void nextPage(){
        getActivity().startActivity(new Intent(getActivity(), BreathSetting.class));
        getActivity().overridePendingTransition(R.anim.main_alpha, R.anim.login_scale_alpha);
        getActivity().finish();
    }

    private FragmentDiaChoose fragmentChoosePic;
    public void showAlert(){
        if(fragmentChoosePic!=null){
            fragmentChoosePic.show(getChildFragmentManager(),"choose pics");
            return;
        }
            fragmentChoosePic=FragmentDiaChoose.create(0);
            fragmentChoosePic.setmChooseListener(mChooseListener);
    
        fragmentChoosePic.show(getChildFragmentManager(),"choose pics");
    }

    private FragmentDiaChoose.ChooseClickListener mChooseListener=new FragmentDiaChoose.ChooseClickListener() {
        @Override
        public void click(int index) {
            switch (index){
                case 0:/**camera*/
                    getPicture(ChooserType.REQUEST_CAPTURE_PICTURE);
                    break;
                case 1:/**photo*/
                    getPicture(ChooserType.REQUEST_PICK_PICTURE);
                    break;
            }
        }
    };



    public void getPicture(int type) {
        manager = new ImageChooserManager(this, type);
        manager.setImageChooserListener(this);
        try {
            manager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageChosen(ChosenImage image) {
        if (image == null) {
            return;
        }
        outCropFile = UtilPicCut.doCropAction(this,
                new File(image.getFilePathOriginal()), true);
    }

    @Override
    public void onError(String reason) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ChooserType.REQUEST_CAPTURE_PICTURE:
                case ChooserType.REQUEST_PICK_PICTURE:
                    if (manager == null) {

                    } else {
                        manager.submit(requestCode, data);
                    }
                    break;
                case UtilPicCut.REQUEST_DO_CROP:
                    if (outCropFile != null) {
                        try {

                 //上传头像
                  SharedPreferencesHelper.getInstance().Builder(getActivity());
                  String phone =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
                  String pwd =  SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
                            LogUtils.i("==" + phone + " =" + pwd);
                            Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
                            Http.Avatar(outCropFile, phone, pwd,new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    Util.cancelProgressFor(getActivity());
                                    String v = responseInfo.result;
                                    byte[] bytes = v.getBytes();
                                    try {
                                        v = new String(bytes, "UTF-8");
                                        JSONObject object = new JSONObject(v);
                                        int info = object.getInt("info");
                                        if (info != 1) {
                                            UtilSnackbar.showSimple(btn_header, object.getString("tip"));
                                            return;
                                        }
                                        Picasso.with(getActivity()).load(outCropFile).into(btn_header);
                                        SharedPreferencesHelper.getInstance().putString(Contact.SH_Avatar, object.getString("avatar"));
                                        Toast.makeText(Http.getCotext(),getResources().getString(R.string.postphone), Toast.LENGTH_SHORT).show();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Util.cancelProgressFor(getActivity());
                                    UtilSnackbar.showSimple(btn_header,"上传头像失败！");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                    showToast("裁剪图片失败");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

}
