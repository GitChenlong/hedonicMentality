package com.sage.hedonicmentality.alipay;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sage.hedonicmentality.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/4.
 */
public class AlipayFragment extends Fragment {
    //商户PID
    private static final String PARTNER = "2088221477641150";
    //商户收款账号
    private static final String SELLER = "2088221477641150";
    //商户私钥，pkcs8格式
    private static final String RSA_PRIVATE ="MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAOXQB0fP9EPqaW0A5zKLMhWCHUXkKDWs6sDM0Lb1oQTajECY0dkBzbiMtuchS0McFJvHLtQumwA2ZdKFHSq1xrijlIB38VPCw9xLnta1WLB/Kau9f8LCQyxOxwbibFhCjQuXZ2aNsjz3xwr4wV3aucbFbi+nqDB5zEI1CUd5oP3lAgMBAAECgYEA0gAR1xZ5YGJhakEsA6zc3HikpUxDTiEv93RxuO6l8BiBlznOcJ3MTjsPnA95hd6wGlWePjhEXaJX01LefzvSVQWUmz1Kl1i0dGA3gStBTlZrcLuw7tRDVIYKZe76u9SyyVxjHvZBRfNtyKRgH4a2HZYZCsspw8gTH6M+Pp9ZgGECQQD2eVhb3ulCphK497szouhvLvopaNXlAhaackPTgHtAe0wnmyeSjB6VcDkY8+S9RvP527CPYC5JkZFwmNF3UWH5AkEA7rHVKvHEbBGbvrnBotQcw0masxqjRAArem4t9XQ5LGUawtLYPaqxovg//GoQohK7FAJI+d5ZwJzOFyArZlw2TQJBANmG3uS2J0gbKgr1GNLJxDncTAyf6abj4O8OSa4whpayOtP5q0M7RwK8uKe9GPOz2z95FKp1SGTFhIbLWJLl0IECQQDVulI/Y8AXbCTiwH2MCRXAH1Xv7XgnWWeU9rccQ3c2M0mlDRonDYKFhHf20hoWpWLx5aHmapw/BajbuChkPUk5AkEA8bKT9hWdyQX0hH+7Fiv345MibFxQuQ9wgMw+t/KowGoMVjQkttjpzwBPbdClztPFbnogFEifLC5hNsgaXsRFrw==";
    //支付宝公钥
    private static final String RSA_PUBLIC = "MIGfMA0GCqGSIb3DQEBAQUAA4GNADCBiQKBgQDl0AdHz/RD6mltAOcyizIVgh1F5Cg1rOrAzNC29aEE2oxAmNHZAc24jLbnIUtDHBSbxy7ULpsANmXShR0qtca4o5SAd/FTwsPcS57WtViwfymrvX/CwkMsTscG4mxYQo0Ll2dmjbI898cK+MFd2rnGxW4vp6gwecxCNQlHeaD95QIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.alipayfragment,null);
        ButterKnife.bind(this,view);

        return view;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    Log.e("resultInfo", resultInfo + " /resultStatus:" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getActivity(), "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getActivity(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            if (TextUtils.equals(resultStatus,"4000")) {

                            }else if (TextUtils.equals(resultStatus,"6001")) {

                            }else if (TextUtils.equals(resultStatus,"6002")) {

                            }
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getActivity(), "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(getActivity(), "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };
    public  void pay(String commodity,String description,String price){
        // 订单
        String orderInfo = getOrderInfo(commodity, commodity, price);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            Log.e("sign",sign.toString());
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(getActivity());
        String version = payTask.getVersion();
        Toast.makeText(getActivity(), version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"15m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        Log.e("sign", SignUtils.sign(content, RSA_PRIVATE));
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
