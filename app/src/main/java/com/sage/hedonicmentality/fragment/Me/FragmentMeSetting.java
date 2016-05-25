package com.sage.hedonicmentality.fragment.Me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Common;
import com.sage.hedonicmentality.app.Http;
import com.sage.hedonicmentality.bean.Userinfo;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.hedonicmentality.fragment.account.FragmentDiaChoose;
import com.sage.hedonicmentality.ui.ActivityLogin;
import com.sage.hedonicmentality.ui.ActivityMe;
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
 * Created by Sage on 2015/7/29.
 */
public class FragmentMeSetting extends BaseFragment implements ImageChooserListener {
    @Bind(R.id.iv_header)
    ImageView iv_header;
    @Bind(R.id.tv_nickname)
    TextView tv_nickname;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.tv_psw)
    TextView tv_psw;
    @Bind(R.id.tv_sex)
    public TextView tv_sex;
    @Bind(R.id.tv_birthday)
    public TextView tv_birthday;
    @Bind(R.id.tv_height)
    public TextView tv_height;
    @Bind(R.id.tv_weight)
    public TextView tv_weight;
    @Bind(R.id.tv_address)
    public TextView tv_address;
    @Bind(R.id.tv_education)
    public TextView tv_education;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;

    @Bind(R.id.tv_right)
    TextView tv_right;
    private int checkedItem = 0;
    /**
     * 性别的选择itemID,1男2女
     */
    private String[] sexChoose;
    /**
     * 性别数组，男和女
     */
    public static String name = "";
    /**
     * address
     */
    public static String addressme = "";
    /**
     * address
     */
    public static String birthdayme = "";
    /**
     * sex
     */
    public static String sexme = "";
    /**
     * education
     */
    public static String education = "";
    private String birth = "2000-1-1";
    /**
     * 生日
     */
    public static String height = "170";
    /**
     * 身高
     */
    public static String weight = "60";
    /**
     * 体重
     */
    private int sexindex = 0;
    private Userinfo userinfo;

    @Override
    public int getLayout() {
        return R.layout.fragment_me_setting;
    }

    @Override
    public void initActionbar() {
        tv_title.setText(R.string.title_me_setting);
        tv_right.setText("编辑");
        tv_right.setTextColor(getResources().getColor(R.color.white_color));
        sexChoose = getResources().getStringArray(R.array.item_sex);
//        tv_birthday.setText(birth);
//        tv_height.setText(getString(R.string.height_unit, height));
//        tv_weight.setText(getString(R.string.weight_unit, weight));
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        layout_actionbar.setBackgroundResource(R.color.bg_title);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Getinfo();
        super.onActivityCreated(savedInstanceState);
    }

    private void Getinfo() {
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String phone = SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd = SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
        String name = SharedPreferencesHelper.getInstance().getString(Contact.SH_USERNAME);
        String Birthday = SharedPreferencesHelper.getInstance().getString(Contact.SH_Birthday);
        String sex = SharedPreferencesHelper.getInstance().getString(Contact.SH_Sex);
        String Height = SharedPreferencesHelper.getInstance().getString(Contact.SH_Height);
        String Weight = SharedPreferencesHelper.getInstance().getString(Contact.SH_Weight);
        String address = SharedPreferencesHelper.getInstance().getString(Contact.SH_Address);
        LogUtils.e("++++++++++Height:"+Height);
        String education = SharedPreferencesHelper.getInstance().getString(Contact.SH_Education);
        this.name  = name;
        this.addressme = address;
        this.birthdayme = Birthday;
        this.education = education;
        height = Height;
        FragmentBirthdayme.age = Birthday;
        this.weight = Weight;

        if(!address.equals(null)||!address.equals("null")){
            tv_address.setText(address);
        }

        tv_birthday.setText(Birthday);
        tv_nickname.setText(name);
        tv_education.setText(education);
        StringBuffer str = new StringBuffer(phone);
        String qin = str.substring(0, 3);
        String hou = str.substring(7, 11);
        String ph = qin + "****" + hou;
        tv_phone.setText(ph);
        if (sex.equals("")) {
            sex = "1";
        }
        if (sex.equals("1")) {
            tv_sex.setText("男");
        } else if (sex.equals("2")) {
            tv_sex.setText("女");
        }
        if (Height.equals("")) {
            Height = "0.0";
        }
        if (Weight.equals("")) {
            Weight = "0.0";
        }
        tv_height.setText(getString(R.string.height_unit, Height));
        tv_weight.setText(getString(R.string.weight_unit, Weight));
        String avatar = SharedPreferencesHelper.getInstance().getString(Contact.SH_Avatar);
        if (avatar.length() > 0) {
            Picasso.with(getActivity()).load(avatar).into(iv_header);
        }
        userinfo = new Userinfo(phone, name, Integer.parseInt(sex), Birthday, Double.parseDouble(Height), Double.parseDouble(Weight));

//        Http.GetInfo(phone, pwd, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                String v = responseInfo.result;
//                byte[] bytes = v.getBytes();
//                try {
//                    v = new String(bytes, "UTF-8");
//                    JSONObject object = new JSONObject(v);
//                    int info = object.getInt("info");
//                    if (info != 1) {
//                        return;
//                    }
//                    Log.e("获取个人设置", "请求成功~"+v);
//                    JSONObject obj = object.getJSONObject("data");
//                    userinfo = new Userinfo(obj.getString("mobile_phone"), obj.getString("nick_name"), Integer.parseInt(obj.getString("sex")), obj.getString("birthday"), Double.parseDouble(obj.getString("height")), Double.parseDouble(obj.getString("weight")));
//                    tv_birthday.setText(userinfo.getBirthday());
//                    tv_nickname.setText(userinfo.getNick_name());
//                    StringBuffer str = new StringBuffer(userinfo.getMobile_phone());
//                    String qin = str.substring(0, 3);
//                    String hou = str.substring(7, 11);
//                    String ph = qin + "****" + hou;
//                    tv_phone.setText(ph);
//                    if (userinfo.getSex() == 1) {
//                        tv_sex.setText("男");
//                    } else if (userinfo.getSex() == 2) {
//                        tv_sex.setText("女");
//                    }
//                    tv_height.setText(getString(R.string.height_unit, userinfo.getHeight()));
//                    tv_weight.setText(getString(R.string.weight_unit, userinfo.getWeight()));
//                    if (obj.getString("avatar").length() > 0) {
//                        Picasso.with(getActivity()).load(obj.getString("avatar")).into(iv_header);
//                    }
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 8:
                    birth = msg.getData().getString("birthday");
                    tv_birthday.setText(birth);
                    break;
                case 6:
                    height = msg.getData().getString("height");
                    tv_height.setText(getString(R.string.height_unit, height));
                    break;
                case 7:
                    weight = msg.getData().getString("weight");
                    tv_weight.setText(getString(R.string.weight_unit, weight));
                    break;
            }
        }
    };


    @OnClick({R.id.layout_header, R.id.layout_nickname, R.id.layout_sex, R.id.layout_birthday, R.id.layout_height, R.id.layout_weight
            , R.id.layout_phone, R.id.layout_psw, R.id.tv_right,R.id.layout_adress,R.id.layout_education})
    public void infoClick(View view) {
        switch (view.getId()) {

            case R.id.layout_sex:
                if (editable)
                    which(15);
                break;
            case R.id.layout_birthday:
                if (editable)
                which(14);
                break;
            case R.id.layout_height:
                if (editable)
                    which(17);
//                    chooseHeight();
                break;
            case R.id.layout_weight:
                if (editable)
                    which(20);
//                    chooseWeight();
                break;

            case R.id.layout_nickname:
                if (editable) {
                    FragmentMeSetting.name = tv_nickname.getText().toString();
                    which(8);
                }
                break;
            case R.id.layout_header:
                if (editable)
                showAlert();
                break;
            case R.id.layout_psw:
                which(7);
                break;
            case R.id.tv_right:
                if (editable) {//提交数据
                    if (userinfo != null) {
                        startHappy();
                    }
                } else {
                    tv_right.setText("保存");
                }
                editable = !editable;
                break;
            case R.id.layout_education:
                if (editable)
                which(12);
                break;
            case R.id.layout_adress:
                if (editable)
                which(13);
                break;
        }
    }

    private boolean editable = false;

    /**
     * 是否可编辑
     */


    private void which(int i) {
        if (getActivity() == null) {
            return;
        }
        ((ActivityMe) getActivity()).changePage(i);
    }


    private void chooseBirthday() {
        SelectBirthdayPopupWindow selectBirthdayPopupWindow = new SelectBirthdayPopupWindow(getActivity(), tv_birthday.getText().toString(), mHandler);
        selectBirthdayPopupWindow.showAtLocation(getView().findViewById(R.id.layout_root), Gravity.BOTTOM, 0, 0);
    }

    private void chooseHeight() {
        SelectHeightPopupWindow selectHeightPopupWindow = new SelectHeightPopupWindow(getActivity(), height, mHandler);

        selectHeightPopupWindow.showAtLocation(getView().findViewById(R.id.layout_root), Gravity.BOTTOM, 0, 0);
    }

    private void chooseWeight() {
        SelectWeightPopupWindow selectWeightPopupWindow = new SelectWeightPopupWindow(getActivity(), weight, mHandler);
        selectWeightPopupWindow.showAtLocation(getView().findViewById(R.id.layout_root), Gravity.BOTTOM, 0, 0);
    }

    FragmentDiaChoose fragmentChooseSex;

    private void chooseSex() {
        if (fragmentChooseSex != null) {
            fragmentChooseSex.show(getChildFragmentManager(), "choose sex");
            return;
        }
        fragmentChooseSex = FragmentDiaChoose.create(1);
        fragmentChooseSex.setmChooseListener(new FragmentDiaChoose.ChooseClickListener() {
            @Override
            public void click(int index) {
                checkedItem = index;
                tv_sex.setText(sexChoose[index]);
            }
        });

        fragmentChooseSex.show(getChildFragmentManager(), "choose sex");
    }


    private FragmentDiaChoose fragmentChoosePic;
    private ImageChooserManager manager;
    private File outCropFile;

    /**
     * 裁剪后的头像地址
     */
    public void showAlert() {
        if (fragmentChoosePic != null) {
            fragmentChoosePic.show(getChildFragmentManager(), "choose pics");
            return;
        }
        fragmentChoosePic = FragmentDiaChoose.create(0);
        fragmentChoosePic.setmChooseListener(mChooseListener);

        fragmentChoosePic.show(getChildFragmentManager(), "choose pics");
    }

    private FragmentDiaChoose.ChooseClickListener mChooseListener = new FragmentDiaChoose.ChooseClickListener() {
        @Override
        public void click(int index) {
            switch (index) {
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
                            String phone = SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
                            String password = SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
                            Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
                            Http.Avatar(outCropFile, phone, password, new RequestCallBack<String>() {
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
                                            UtilSnackbar.showSimple(tv_nickname, object.getString("tip"));
                                            return;
                                        }
                                        Toast.makeText(Http.getCotext(), getResources().getString(R.string.postphone), Toast.LENGTH_SHORT).show();
                                        SharedPreferencesHelper.getInstance().putString(Contact.SH_Avatar, object.getString("avatar"));
                                        Picasso.with(getActivity()).load(object.getString("avatar")).into(iv_header);
                                        updata();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Util.cancelProgressFor(getActivity());
                                    UtilSnackbar.showSimple(tv_nickname, "上传头像失败！");
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

    public void showToast(String msg) {
        Toast.makeText(Http.getCotext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setName() {
        tv_nickname.setText(name);
    }
    public void setEduca() {
        tv_education.setText(education);
    }
    public void setaddress() {
        tv_address.setText(addressme);
    }
    public void setbirth() {
        tv_birthday.setText(birthdayme);
    }
    public void setSex() {
        tv_sex.setText(sexme);
    }
    public void setHegit() {
        tv_height.setText(getString(R.string.height_unit, height));
    }
    public void setWeight() {
        tv_weight.setText(getString(R.string.weight_unit, weight));
    }
    @Override
    public void back() {
        super.back();
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    private void startHappy() {
        final String nickName = tv_nickname.getText().toString().trim();
        String sex = tv_sex.getText().toString().trim();
        final String birthday = tv_birthday.getText().toString().trim();
        final String address = tv_address.getText().toString().trim();
        final String education = tv_education.getText().toString().trim();
        if (sex.equals(getResources().getString(R.string.man))) {
            sexindex = 1;
        } else if (sex.equals(getResources().getString(R.string.woman))) {
            sexindex = 2;
        }
        String sexs = "";
        if (userinfo.getSex() == 1) {
            sexs = getResources().getString(R.string.man);
        } else if (userinfo.getSex() == 2) {
            sexs = getResources().getString(R.string.woman);
        }
        Double he = 0.0;
        Double wi = 0.0;
        if (!height.equals("")) {
            he = Double.parseDouble(height);
        }
        if (!weight.equals("")) {
            wi = Double.parseDouble(weight);
        }
        if (nickName.equals(userinfo.getNick_name()) && userinfo.getBirthday().equals(birthday) && sex.equals(sexs)
                && he == userinfo.getHeight() && wi == userinfo.getWeight()) {
            Log.e("```", "上传停止");
            return;
        }
        //上传个人信息
        SharedPreferencesHelper.getInstance().Builder(getActivity());
        String phone = SharedPreferencesHelper.getInstance().getString(Contact.SH_PHONE);
        String pwd = SharedPreferencesHelper.getInstance().getString(Contact.SH_PWD);
        SharedPreferencesHelper.getInstance().putString(Contact.SH_USERNAME, nickName);
        SharedPreferencesHelper.getInstance().putString(Contact.SH_Birthday, birthday);
        SharedPreferencesHelper.getInstance().putString(Contact.SH_Sex, sexindex + "");
        SharedPreferencesHelper.getInstance().putString(Contact.SH_Weight, weight);
        SharedPreferencesHelper.getInstance().putString(Contact.SH_Height, height);
        SharedPreferencesHelper.getInstance().putString(Contact.SH_Address,addressme);
        SharedPreferencesHelper.getInstance().putString(Contact.SH_Education,education);
        updata();
        Util.showProgressFor(getActivity(), getResources().getString(R.string.longdings));
        Http.SelfSetting(phone, pwd, nickName, sexindex + "", birthday, height, weight,address,education,new RequestCallBack<String>() {
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
                        return;
                    }
                    tv_right.setText("编辑");
                    Util.showToast(getActivity(), "个人信息修改成功!");
                    //接口调用成功后 添加本地数据上一界面
                    LogUtils.i("个人信息修改成功");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.cancelProgressFor(getActivity());
                LogUtils.i("个人信息修改失败");
            }
        });

    }

    public void updata() {
        ActivityMe ac = (ActivityMe) getActivity();
        if (ac != null)
            ac.setNamePhone();
    }

}
