package com.dashuai.oclick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

import static android.graphics.Color.rgb;

public class Digit extends View {

    private final static String TAG = Needle.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private float PANEL_RADIUS = 200.0f;// 表盘半径

    private float HOUR_POINTER_LENGTH;// 指针长度
    private float MINUTE_POINTER_LENGTH;
    private float SECOND_POINTER_LENGTH;
    private float UNIT_DEGREE = (float) (6 * Math.PI / 180);// 一个小格的度数

    private int mWidth, mCenterX, mCenterY, mRadius;

    private int degreesColor;

    private Paint mNeedlePaint;

    public Digit(Context context) {
        super(context);
        init(context, null);
    }

    public Digit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Digit(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;
        PANEL_RADIUS = mRadius;
        HOUR_POINTER_LENGTH = PANEL_RADIUS - 400;
        MINUTE_POINTER_LENGTH = PANEL_RADIUS - 250;
        SECOND_POINTER_LENGTH = PANEL_RADIUS - 150;

        //drawDegrees(canvas);
        drawHoursValues(canvas);
        drawDigit(canvas);

        postInvalidateDelayed(1000);
        // todo 每一秒刷新一次，让指针动起来

    }


    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor

        float circle[] = new float[2];
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int nowSeconds = now.getSeconds();
        float w = mRadius/4;
        RectF rect = new RectF(0, 0, 2 * mCenterX, 2 * mCenterY);
        RectF rectInner = new RectF(w, w, 2 * mCenterX - w, 2 * mCenterY - w);



        mNeedlePaint = new Paint();


        mNeedlePaint.setColor(Color.LTGRAY);

        canvas.drawCircle(mCenterX, mCenterY, mRadius, mNeedlePaint);

        mNeedlePaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, mRadius-w, mNeedlePaint);

        LinearGradient linearGradient = new LinearGradient(0, 0, 200, 0, rgb(228, 51, 255), rgb(109, 51, 255),
                Shader.TileMode.CLAMP);
        mNeedlePaint.setShader(linearGradient);
        //mNeedlePaint.setStyle(Paint.Style.STROKE);

        //mNeedlePaint.setStrokeWidth(90f);
        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);
        float nowDegree = nowSeconds * 6;
        circle[0] = (float) (mCenterX + (mRadius - w / 2) * Math.cos((nowDegree-90) * UNIT_DEGREE/6));
        circle[1] = (float) (mCenterY + (mRadius - w / 2) * Math.sin((nowDegree-90) * UNIT_DEGREE/6));
        canvas.drawArc(rect,nowDegree+90,180,true,mNeedlePaint);
        canvas.drawCircle(circle[0], circle[1], w / 2, mNeedlePaint);
        mNeedlePaint.setShader(null);
        mNeedlePaint.setColor(Color.WHITE);
        circle[0] = (float) (mCenterX + (mRadius - w / 2) * Math.cos((nowDegree+90) * UNIT_DEGREE/6));
        circle[1] = (float) (mCenterY + (mRadius - w / 2) * Math.sin((nowDegree+90) * UNIT_DEGREE/6));
        canvas.drawArc(rectInner, nowDegree + 90, 180, true, mNeedlePaint);
        mNeedlePaint.setColor(Color.LTGRAY);
        canvas.drawCircle(circle[0], circle[1], w / 2, mNeedlePaint);


    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawDigit(final Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int nowSeconds = now.getSeconds();


        float myTextSize = 234f;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.GRAY);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(myTextSize);
        canvas.drawText("" + nowHours + ":" + (nowMinutes < 10 ? "0" : "") + nowMinutes, mCenterX, mCenterY + myTextSize / 2, paint);

    }


    private void drawPointer(Canvas canvas, int pointerType, int value) {

        float degree;
        float[] pointerHeadXY = new float[2];

        mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        switch (pointerType) {
            case 0:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.WHITE);
                pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
                break;
            case 1:
                // todo 画分针，设置分针的颜色
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.RED);
                pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);

                break;
            case 2:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.GREEN);
                pointerHeadXY = getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
                break;
        }


        canvas.drawLine(mCenterX, mCenterY, pointerHeadXY[0], pointerHeadXY[1], mNeedlePaint);
    }

    private float[] getPointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (mCenterX + pointerLength * Math.sin(degree));
        xy[1] = (float) (mCenterY - pointerLength * Math.cos(degree));
        return xy;
    }


}