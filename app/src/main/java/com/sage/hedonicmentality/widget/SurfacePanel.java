package com.sage.hedonicmentality.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class SurfacePanel extends View {
    private int bottomY = 0;
    /**
     * x轴与底部的距离
     */
    Context scontaxt;
    //screen information
    private float textsize = 0;
    private static final float GESTURE_THRESHOLD_DIP = 12.0f;
    int textHeight = 0;
    int textWidth = 0;

    private int viewWidth = 0;
    private int viewHeight = 0;
    private int topHR = 100;
    private int bottomHR = 50;
    private int numberSection = 0;
    private int timeSpanSec = 0;

    private int endTime = 0;
    private int startTime = 0;
    private int hr = 0;


    private int backGroundColor = Color.BLACK;
    private float phyViewWidth = 0;
    private float phyViewHeight = 0;
    private int timeSpanInView = 0;

    private float pixelHeight = 0;

    private int windowArraySize = 0;
    float prewindowdataArray[][] = null;
    float premaxY = 0;
    float preminY = 0;
    float prelastDataIndex = 0;

    Paint curvePaint;
    Paint textPaint;
    Paint trianglePaint;
    Paint rectPaint;
    Paint cyclePaint;
    // Size of a DensityIndependentPixel
    private float mDips = 0;


    static final int DATABUFFERLEN = 300;
    float[][] databuffer = new float[DATABUFFERLEN][3];
    int lastDataIndex = 0;

    int endpos = 0, preendpos = 0, prepreendpos = 0;
    int score = 0;
    int up = 0, down = 0, peakindex = 0, prelowindex = 0, newlowindex = 0, notsmooth = 0;

    public int getScore() {
        return score;
    }

    public void clearBuffer() {
        for (int i = 0; i < DATABUFFERLEN; i++) {
            databuffer[i][0] = 0;
            databuffer[i][1] = 0;
            databuffer[i][2] = 0;
        }
        lastDataIndex = 0;

        endpos = preendpos = prepreendpos = 0;
        score = 0;
        up = down = peakindex = prelowindex = newlowindex = notsmooth = 0;
    }

    public void addBeat(float time, float beat) {
        if (lastDataIndex >= DATABUFFERLEN) {
            lastDataIndex = lastDataIndex % DATABUFFERLEN;
        }
        databuffer[lastDataIndex][0] = time;
        databuffer[lastDataIndex][1] = beat;
        databuffer[lastDataIndex][2] = 0;

        prepreendpos = preendpos;
        preendpos = endpos;
        endpos = lastDataIndex;

        evaluateHeartBeat();

        lastDataIndex++;
        return;
    }

    public SurfacePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < DATABUFFERLEN; i++) {
            databuffer[i][0] = 0;
            databuffer[i][1] = 0;
        }
        score = 0;
        mDips = getResources().getDisplayMetrics().density;

        curvePaint = new Paint();
        curvePaint.setColor(0xff00ff00);
        curvePaint.setStrokeWidth(2f * mDips);//change from 2 to 4
        curvePaint.setDither(true);
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setStrokeJoin(Paint.Join.ROUND);
        curvePaint.setStrokeCap(Paint.Cap.ROUND);
        curvePaint.setPathEffect(new CornerPathEffect(10));
        curvePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(43);
        //textPaint.setTextSize(textsize);
        textPaint.setAntiAlias(true);


        trianglePaint = new Paint();
        trianglePaint.setColor(Color.RED);
        trianglePaint.setStrokeWidth(2f * mDips);
        trianglePaint.setStyle(Paint.Style.FILL);


        rectPaint = new Paint();
        rectPaint.setColor(Color.YELLOW);
        rectPaint.setStrokeWidth(2f * mDips);
        rectPaint.setStyle(Paint.Style.FILL);

        cyclePaint = new Paint();
        cyclePaint.setColor(Color.GREEN);
        cyclePaint.setStrokeWidth(2f * mDips);
        cyclePaint.setStyle(Paint.Style.FILL);

        scontaxt = context;
//		SurfaceHolder holder=getHolder();
//		holder.addCallback(this);
    }


    public void doDraw(Canvas mcanvas, float[][] databuffer, int lastdataindex) {


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#40818077"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0);

        float left = textWidth;
        float top = 0;
        float right = textWidth + viewWidth;
        float bottom = viewHeight;

        //mcanvas.drawRect(left, top, right, bottom, paint);

        paint.setColor(Color.parseColor("#40008070"));
//	    mcanvas.drawRect(0, 0, textWidth, viewHeight+textWidth, paint);
//	    mcanvas.drawRect(0, viewHeight, viewWidth+textWidth, viewHeight+textWidth, paint);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f * mDips);
        //mcanvas.drawLine(textWidth, 0, textWidth, viewHeight, paint);

        //mcanvas.drawText("脉愽: "+String.valueOf(hr), (int)(textWidth+viewWidth/2), (int)(textHeight*1.5), textPaint);

        int step = (int) (topHR - bottomHR) / 4;
        int stepPixels = viewHeight / 4;
        for (int i = 0; i < 5; i++) {
//        	mcanvas.drawLine((int)(0.85*textWidth), stepPixels*i, textWidth+viewWidth, stepPixels*i, paint);
//        	mcanvas.drawText(String.valueOf(topHR-i*step), (int)(0.1*textWidth), stepPixels*i+textHeight, textPaint);

        }
        //draw X axis with time in MM:SS format.

        //mcanvas.drawLine(textWidth, viewHeight, viewWidth+textWidth, viewHeight, paint);
        int startY = viewHeight;
        int stopY = viewHeight + (int) (0.15 * textWidth);
        int stepX = (viewWidth / numberSection);
        String timeStamp = null;
        int minutes = 0;
        int seconds = 0;
        int ptime = 0;
        int timeStep = timeSpanInView / numberSection;
        for (int i = 0; i <= numberSection; i++) {
            //mcanvas.drawLine(textWidth+stepX*i, startY, textWidth+stepX*i, stopY, paint);
            ptime = endTime - (numberSection - i) * timeStep;
            if (ptime <= 0) timeStamp = "00:00";
            else {
                ptime = ptime / 1000;
                minutes = ptime / 60;
                seconds = ptime % 60;
                timeStamp = String.format("%02d:%02d", minutes, seconds);
            }
            //mcanvas.drawText(timeStamp, stepX*i, stopY + (int)(textHeight) + (int)(0.15*textWidth) , textPaint);
        }


        //mcanvas.drawCircle(50, 50, 40, cyclePaint);


        float windowdata[][] = prepareWindowData(databuffer, lastdataindex);

        if (windowdata == null) return;
        // Prepare the curve Path
        Path curve = new Path();
        // Move at the first point.
        curve.moveTo(windowdata[0][0], windowdata[0][1]);
        float cx = 0, cy = viewHeight - 2 * mDips, height = viewHeight / 32 < 10 ? 10 : viewHeight / 32;

        // Draw the remaining points of the curve
        for (int i = 1; i < windowArraySize; i++) {
            curve.lineTo(windowdata[i][0], windowdata[i][1]);
            cx = windowdata[i][0];
            cy = viewHeight - bottomY;

            switch ((int) windowdata[i][2]) {
                case (1): //least score triangle
                {
                    Path triPath = new Path();
                    triPath.moveTo(cx - height / 2, cy);
                    triPath.lineTo(cx, cy - height);
                    triPath.lineTo(cx + height / 2, cy);
                    mcanvas.drawPath(triPath, trianglePaint);
                    break;
                }
                case (2): //middle score rec
                {
                    mcanvas.drawRect(cx - height / 2, cy - height, cx + height / 2, cy, rectPaint);
                    break;
                }
                case (3): //high score circle
                {
                    mcanvas.drawCircle(cx, cy - height / 2, height / 2, cyclePaint);
                    break;
                }
                default:
                    break;
            }
        }

        mcanvas.drawPath(curve, curvePaint);


    }

    private float[][] prepareWindowData(float[][] databuffer, int lastdataindex) {
        //timeSpanInView find out the start point of data

        float endtime = databuffer[lastdataindex][0];
        //when time start before heart beat is detected, the time is going to set to
        //real time at 1 second interval and y value set to 60 as middle
        //as soon as beat was detected, all Y value set to beginner's value to show it
        //as flat in screen.
        int len = DATABUFFERLEN;
        //loop back to databauffer
        int lastlastindex = 0;


        lastlastindex = (lastdataindex + DATABUFFERLEN - 1) % (DATABUFFERLEN);

        endTime = (int) databuffer[lastlastindex][0] * 1000;

        if (databuffer[lastlastindex][0] == 0.0)
            return null;
        while (databuffer[lastlastindex][0] > 0 && (databuffer[lastdataindex][0] - databuffer[lastlastindex][0]) * 1000 < timeSpanInView) {
            lastlastindex = (lastlastindex + DATABUFFERLEN - 1) % (DATABUFFERLEN);
        }
        lastlastindex = (lastlastindex + 1) % (DATABUFFERLEN);

        startTime = (int) (databuffer[lastdataindex][0] * 1000);
        hr = (int) databuffer[lastdataindex][1];
        //now we get the first and last dataindex to fit in the window. try to get the point where where x=0
        //figure it out later
        //int pre= (lastlastindex+10-1)%(SimulateThread.DATABUFFERLEN);

        //first point x coord
        int x = (int) (timeSpanInView - (databuffer[lastdataindex][0] - databuffer[lastlastindex][0]) * 1000);

        float[][] xycoord = new float[(lastdataindex - lastlastindex + DATABUFFERLEN) % DATABUFFERLEN + 1][3];
        int xyindex = 0;
        int firstIndex = lastlastindex;
        //find x and y position for each point
        //first point with timespan x. second point is x+databuffer[lastlastindex][0]-databuffer[lastlastindex-1][0]
        float maxY = databuffer[firstIndex][1];
        float minY = databuffer[firstIndex][1];
        while (lastlastindex != lastdataindex) {
            xycoord[xyindex][0] = x + (databuffer[lastlastindex][0] - databuffer[firstIndex][0]) * 1000;
            xycoord[xyindex][0] = xycoord[xyindex][0] * viewWidth / timeSpanInView + +textWidth; //get pixel value for x coord
            xycoord[xyindex][1] = databuffer[lastlastindex][1];
            xycoord[xyindex][2] = databuffer[lastlastindex][2];

            maxY = xycoord[xyindex][1] > maxY ? xycoord[xyindex][1] : maxY;
            minY = xycoord[xyindex][1] < minY ? xycoord[xyindex][1] : minY;
            lastlastindex = (lastlastindex + 1) % (DATABUFFERLEN);
            xyindex++;
        }

        xycoord[xyindex][0] = x + (databuffer[lastdataindex][0] - databuffer[firstIndex][0]) * 1000;
        xycoord[xyindex][0] = xycoord[xyindex][0] * viewWidth / timeSpanInView;
        xycoord[xyindex][1] = databuffer[lastdataindex][1];
        xycoord[xyindex][2] = databuffer[lastdataindex][2];

        windowArraySize = xyindex;

        maxY = xycoord[xyindex][1] > maxY ? xycoord[xyindex][1] : maxY;
        minY = xycoord[xyindex][1] < minY ? xycoord[xyindex][1] : minY;

        float height = maxY - minY;
        bottomHR = (int) (minY - ((viewHeight - pixelHeight) / 2) * height / pixelHeight);
        topHR = (int) (maxY + ((viewHeight - pixelHeight) / 2) * height / pixelHeight);
        for (int j = 0; j < xyindex; j++) {
            xycoord[j][1] = xycoord[j][1] - minY;
//			xycoord[j][1]=(height-xycoord[j][1])*viewHeight/height;
            xycoord[j][1] = (height - xycoord[j][1]) * pixelHeight / height + (viewHeight - pixelHeight) / 2;
        }


        prelastDataIndex = lastdataindex;

        return xycoord;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int lastlastindex = (lastDataIndex + DATABUFFERLEN - 1) % (DATABUFFERLEN);
        if (databuffer[lastlastindex][0] == 0.0) {

        } else {
            doDraw(canvas, databuffer, lastlastindex);
        }

        postInvalidateDelayed(1500);
    }

    private int calculateWidthFromFontSize(String testString, int currentSize) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(currentSize);
        paint.getTextBounds(testString, 0, testString.length(), bounds);

        return (int) Math.ceil(bounds.width());
    }

    private int calculateHeightFromFontSize(String testString, int currentSize) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(currentSize);
        paint.getTextBounds(testString, 0, testString.length(), bounds);

        return (int) Math.ceil(bounds.height());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        final float scale = getContext().getResources().getDisplayMetrics().density;
        textsize = (GESTURE_THRESHOLD_DIP * scale + 0.5f);
        textPaint.setTextSize(textsize);

        // set height of each line (height of text + 20%)
//	    textHeight = (int) (calculateHeightFromFontSize("120", (int)textsize) * 1.2);
//	    textWidth =(int)(calculateWidthFromFontSize("58:88", (int)textsize)*1.2);

        viewWidth = w - textWidth;
        viewHeight = h - textWidth;
        //mDips = getResources().getDisplayMetrics().density;
        phyViewWidth = viewWidth / (mDips * 160);
        phyViewHeight = viewHeight / (mDips * 160);
        //2 inches hold 40 seconds
        // 40/2=20 seconds data
        //heart rate should range from 45 to 100. Anything out of range is wrong. there is no need to practice the breath
        timeSpanInView = (int) ((int) (40000 / 2) * phyViewWidth);

        //the width to heigh ratiao should be kept to 3.5
        //pixels per inch in height  mDips*160*2/3.5 = pixel in height  
        pixelHeight = mDips * 160 * 2 / 3.5f;
        pixelHeight = viewHeight > pixelHeight ? pixelHeight : viewHeight;

        numberSection = timeSpanInView / 5000; //every five seconds for the mark
        timeSpanSec = 5000;
        if (numberSection * (textWidth * 1.3) > viewWidth) {
            numberSection = timeSpanInView / 10000;
            timeSpanSec = 10000;
        }

        bottomY = h * 2 / 51;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void evaluateHeartBeat() {
        if (databuffer[endpos][1] > databuffer[preendpos][1])    //up now check if it is low point
        {
            up++;
            if (down > 0) //upward but previous is down.
            {
                if ((databuffer[endpos][1] < databuffer[prepreendpos][1] || (up > 1 && down >= 1))) //either
                {
                    //detected low point   //peak already detected.
                    if (peakindex == 0)
                        peakindex = prepreendpos;
                    down = 0;
                    prelowindex = newlowindex;
                    if (up == 1)
                        newlowindex = preendpos;
                    else if (up > 1)
                        newlowindex = prepreendpos;
                    if (notsmooth > 0) {
                        if ((databuffer[newlowindex][0] - databuffer[prelowindex][0]) > 7.5)//600/8
                        //paint two point
                        {
                            databuffer[peakindex][2] = 2;
                            score = score + 1;
                        } else {
                            databuffer[peakindex][2] = 1;
                        }
                    } else {
                        if ((databuffer[newlowindex][0] - databuffer[prelowindex][0]) > 7.5) {
                            databuffer[peakindex][2] = 3;
                            score = score + 2;
                            //goodtiming = hbtime[newlowindex]-hbtime[prelowindex];
                            //reset br
                            //br = 6000/goodtiming;
                        } else if ((databuffer[newlowindex][0] - databuffer[prelowindex][0]) > 6) {
                            databuffer[peakindex][2] = 2;
                            score = score + 2;
                        } else {
                            databuffer[peakindex][2] = 1;
                        }
                    }
                    peakindex = 0;
                    notsmooth = 0;
                } else {
                    if (peakindex == 0)//need decide if peakindex is passed or not.
                        notsmooth = 1;
                }
            } else if (down > 0) {
                notsmooth = 1;
                down = 0;
            }

        }
        if (databuffer[endpos][1] < databuffer[preendpos][1]) {
            down++;
            if (up > 0) //downward but previous is up
            {
                if (databuffer[endpos][1] < databuffer[prepreendpos][1] && (down > 1 && up >= 1)) {
                    //detected peak point
                    up = 0;
                    if (down == 1)
                        peakindex = preendpos;
                    else if (down == 2)
                        peakindex = prepreendpos;

                } else {
                    //posible low point;
                }
            } else if (up > 0) {
                notsmooth = 1;
                up = 0;
            }
        }

    }


}
