package com.sage.hedonicmentality.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.sage.hedonicmentality.R;

import java.util.ArrayList;

public class LineGraphicView extends View
{
    /**
     * 公共部分
     */
    private static final int CIRCLE_SIZE = 10;

    private static enum Linestyle
    {
        Line, Curve
    }

    private Context mContext;
    private Paint mPaint;
    private Resources res;
    private DisplayMetrics dm;

    /**
     * data
     */
    private Linestyle mStyle = Linestyle.Line;

    private int canvasHeight;
    private int canvasWidth;
    private int bheight = 0;
    private int blwidh;
    private boolean isMeasure = true;
    /**
     * Y轴最大值
     */
    private int maxValue;
    /**
     * Y轴间距值
     */
    private int averageValue;
    private int marginTop = 10;
    private int marginBottom = 90;
    /**
     * 横线Y坐标
     */
    private int juHeght;

    /**
     * 曲线上总点数
     */
    private Point[] mPoints;
    /**
     * 纵坐标值
     */
    private ArrayList<Double> yRawData;
    /**
     * 横坐标值
     */
    private ArrayList<String> xRawDatas;
    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
    private int spacingHeight;

    public LineGraphicView(Context context)
    {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView()
    {
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (isMeasure)
        {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            if (bheight == 0)
                bheight = (int) (canvasHeight - marginBottom);
            blwidh = dip2px(50);//距离左边  边距
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(res.getColor(R.color.color_f2f2f2));

        drawAllXLine(canvas);
        // 画直线（纵向）
        drawAllYLine(canvas);
        // 点的操作设置
        mPoints = getPoints();

        mPaint.setColor(res.getColor(R.color.color_ff4631));
        mPaint.setStrokeWidth(dip2px(1.0f));
        mPaint.setStyle(Style.STROKE);
        if (mStyle == Linestyle.Curve)
        {
            drawScrollLine(canvas);
        }
        else
        {
            drawLine(canvas);
        }

        mPaint.setStyle(Style.FILL);
        for (int i = 0; i < mPoints.length; i++)
        {
//            if(yRawData.get(i)!=0.0){
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, 12 / 2, mPaint);
//            }
        }
    }

    /**
     *  画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas)
    {
        for (int i = 0; i < spacingHeight + 1; i++)
        {
            String text = "";
            canvas.drawLine(blwidh, bheight - (bheight / spacingHeight) * i + marginTop, blwidh
                            + (canvasWidth - blwidh) / xRawDatas.size() * (xRawDatas.size()-1),
                    bheight - (bheight / spacingHeight) * i + marginTop, mPaint);// Y坐标
            if(i == spacingHeight){
                juHeght = bheight - (bheight / spacingHeight) * i + marginTop;
            }

            if(i == 1){
                text = getResources().getString(R.string.sleep_1);
            }
            if(i == 2){
                text = getResources().getString(R.string.sleep_2);
            }
            if(i == 3){
                text = getResources().getString(R.string.sleep_3);
            }
            if(i == 4){
                text = getResources().getString(R.string.sleep_4);
            }
            if(i == 5){
                text = getResources().getString(R.string.sleep_5);
            }
            drawText(text, 2, bheight - (bheight / spacingHeight) * i + marginTop,
                    canvas);
        }
    }

    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawAllYLine(Canvas canvas)
    {
        for (int i = 0; i < xRawDatas.size(); i++)
        {
            xList.add(blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i);
            canvas.drawLine(blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i, marginTop, blwidh
                    + (canvasWidth - blwidh) / xRawDatas.size() * i, bheight + marginTop, mPaint);
            drawText(xRawDatas.get(i), blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i, bheight + dip2px(26),
                    canvas);// X坐标
        }
    }

    private void drawScrollLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    private void drawLine(Canvas canvas)
    {
        //多变矩形阴影
        Paint framPanint = new Paint();
        framPanint.setAntiAlias(true);
        framPanint.setStrokeWidth(2f);

        Point startp = new Point();
        Point endp = new Point();
        // 创建LinearGradient并设置渐变颜色数组
        // 第一个,第二个参数表示渐变起点 可以设置起点终点在对角等任意位置
        // 第三个,第四个参数表示渐变终点
        // 第五个参数表示渐变颜色
        // 第六个参数可以为空,表示坐标,值为0-1 new float[] {0.25f, 0.5f, 0.75f, 1 }
        // 如果这是空的，颜色均匀分布，沿梯度线。
        // 第七个表示平铺方式
        // CLAMP重复最后一个颜色至最后
        // MIRROR重复着色的图像水平或垂直方向已镜像方式填充会有翻转效果
        // REPEAT重复着色的图像水平或垂直方向
//        Shader mShader = new LinearGradient(0, 0, 0, getHeight(), new int[] {
//                Color.argb(100, 0, 255, 255), Color.argb(45, 0, 255, 255),
//                Color.argb(10, 0, 255, 255) }, null, Shader.TileMode.CLAMP);
        Shader mShader = new LinearGradient(0, 0, 0, getHeight(), new int[] {
                Color.argb(100, 71, 115, 114), Color.argb(100, 71, 115, 114),Color.argb(90, 71, 115, 114),
                Color.argb(15,71, 115, 114) ,Color.argb(0,71, 115, 114)}, null, Shader.TileMode.CLAMP);
        framPanint.setShader(mShader);
        Path path = new Path();

        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            path.lineTo(startp.x, startp.y);
//            if(yRawData.get(i) != 0.0){
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
//            }
            if(i == mPoints.length - 2){
                path.lineTo(endp.x, endp.y);
                path.lineTo(endp.x, getHeight()-juHeght);
                path.lineTo(mPoints[0].x,getHeight()-juHeght);
                path.lineTo(mPoints[0].x,mPoints[0].y);
                path.close();
                canvas.drawPath(path, framPanint);
            }
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(res.getColor(R.color.color_999999));
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

    private Point[] getPoints()
    {
        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++)
        {
            int ph = bheight - (int) (bheight * (yRawData.get(i) / maxValue));

            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(ArrayList<Double> yRawData, int maxValue, int averageValue)
    {
        ArrayList<String> xRawDatas = new ArrayList<String>();
        xRawDatas.add("1");
        xRawDatas.add("2");
        xRawDatas.add("3");
        xRawDatas.add("4");
        xRawDatas.add("5");
        xRawDatas.add("6");
        xRawDatas.add("7");
        xRawDatas.add("8");
        xRawDatas.add("9");
        xRawDatas.add("10");
        xRawDatas.add("11");
        xRawDatas.add("12");
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.mPoints = new Point[yRawData.size()];
        this.xRawDatas = xRawDatas;
        this.yRawData = yRawData;
        this.spacingHeight = maxValue / averageValue;
    }

    public void setTotalvalue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public void setPjvalue(int averageValue)
    {
        this.averageValue = averageValue;
    }

    public void setMargint(int marginTop)
    {
        this.marginTop = marginTop;
    }

    public void setMarginb(int marginBottom)
    {
        this.marginBottom = marginBottom;
    }

    public void setMstyle(Linestyle mStyle)
    {
        this.mStyle = mStyle;
    }

    public void setBheight(int bheight)
    {
        this.bheight = bheight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }

}
