package com.sage.hedonicmentality.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sage.hedonicmentality.alipay.PayResult;
import com.sage.hedonicmentality.alipay.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/16.
 */
public class Alipay {
    //商户PID
    private static final String PARTNER = "2088221477641150";
    //商户收款账号
    private static final String SELLER = "2088221477641150";
    //商户私钥，pkcs8格式
    private static final String RSA_PRIVATE ="MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAOXQB0fP9EPqaW0A5zKLMhWCHUXkKDWs6sDM0Lb1oQTajECY0dkBzbiMtuchS0McFJvHLtQumwA2ZdKFHSq1xrijlIB38VPCw9xLnta1WLB/Kau9f8LCQyxOxwbibFhCjQuXZ2aNsjz3xwr4wV3aucbFbi+nqDB5zEI1CUd5oP3lAgMBAAECgYEA0gAR1xZ5YGJhakEsA6zc3HikpUxDTiEv93RxuO6l8BiBlznOcJ3MTjsPnA95hd6wGlWePjhEXaJX01LefzvSVQWUmz1Kl1i0dGA3gStBTlZrcLuw7tRDVIYKZe76u9SyyVxjHvZBRfNtyKRgH4a2HZYZCsspw8gTH6M+Pp9ZgGECQQD2eVhb3ulCphK497szouhvLvopaNXlAhaackPTgHtAe0wnmyeSjB6VcDkY8+S9RvP527CPYC5JkZFwmNF3UWH5AkEA7rHVKvHEbBGbvrnBotQcw0masxqjRAArem4t9XQ5LGUawtLYPaqxovg//GoQohK7FAJI+d5ZwJzOFyArZlw2TQJBANmG3uS2J0gbKgr1GNLJxDncTAyf6abj4O8OSa4whpayOtP5q0M7RwK8uKe9GPOz2z95FKp1SGTFhIbLWJLl0IECQQDVulI/Y8AXbCTiwH2MCRXAH1Xv7XgnWWeU9rccQ3c2M0mlDRonDYKFhHf20hoWpWLx5aHmapw/BajbuChkPUk5AkEA8bKT9hWdyQX0hH+7Fiv345MibFxQuQ9wgMw+t/KowGoMVjQkttjpzwBPbdClztPFbnogFEifLC5hNsgaXsRFrw==";
    //支付宝公钥
    private static final String RSA_PUBLIC = "MIGfMA0GCqGSIb3DQEBAQUAA4GNADCBiQKBgQDl0AdHz/RD6mltAOcyizIVgh1F5Cg1rOrAzNC29aEE2oxAmNHZAc24jLbnIUtDHBSbxy7ULpsANmXShR0qtca4o5SAd/FTwsPcS57WtViwfymrvX/CwkMsTscG4mxYQo0Ll2dmjbI898cK+MFd2rnGxW4vp6gwecxCNQlHeaD95QIDAQAB";


//    /**
//     * get the sdk version. 获取SDK版本号
//     *
//     */
//    public void getSDKVersion(Context context) {
//        PayTask payTask = new PayTask(context);
//        String version = payTask.getVersion();
//        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
//    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public static String getOrderInfo(String subject, String body, String price) {
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
    public static String getOutTradeNo() {
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
    public static String sign(String content) {
        Log.e("sign", SignUtils.sign(content, RSA_PRIVATE));
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
