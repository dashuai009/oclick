package com.dashuai.oclick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

import static android.graphics.Color.rgb;

public class Needle extends View {

    private final static String TAG = Needle.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;
    private static final float DEFAULT_NEEDLW_STROKE_WIDTH_PERCENT = 0.02f;

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

    public Needle(Context context) {
        super(context);
        init(context, null);
    }

    public Needle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Needle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        this.degreesColor = Color.LTGRAY;
        ;

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

        drawDegrees(canvas);
        drawHoursValues(canvas);
        drawNeedles(canvas);

        postInvalidateDelayed(1000);
        // todo 每一秒刷新一次，让指针动起来

    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));
            if (i % 90 != 0) {
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }

        }
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        Paint paint = new Paint();
        float textSz = 80f;
        paint.setTextSize(textSz);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        float radius = PANEL_RADIUS * 8 / 10;
        canvas.drawText("12", mCenterX, mCenterY - PANEL_RADIUS + textSz, paint);
        canvas.drawText("3", mCenterX + PANEL_RADIUS - textSz / 2, mCenterY + textSz / 2, paint);
        canvas.drawText("6", mCenterX, mCenterY + PANEL_RADIUS - textSz / 4, paint);
        canvas.drawText("9", mCenterX - PANEL_RADIUS + textSz / 2, mCenterY + textSz / 2, paint);

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int nowSeconds = now.getSeconds();
        // 画时针
        int part = nowMinutes / 12;
        drawPointer(canvas, 0, 5 * nowHours + part);
        // 画分针
        // todo 画分针
        drawPointer(canvas, 1, nowMinutes);
        // 画秒针
        drawPointer(canvas, 2, nowSeconds);


    }


    private void drawPointer(Canvas canvas, int pointerType, int value) {

        float degree;
        float[] pointerHeadXY = new float[2];
        float[] pointerInnerXY = new float[2];

        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);

        switch (pointerType) {
            case 0:
                //时针
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_NEEDLW_STROKE_WIDTH_PERCENT);
                mNeedlePaint.setColor(Color.LTGRAY);
                mNeedlePaint.setShader(null);
                pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
                pointerInnerXY[0] = mCenterX + (pointerHeadXY[0] - mCenterX) / 2;
                pointerInnerXY[1] = mCenterY + (pointerHeadXY[1] - mCenterY) / 2;
                break;
            case 1:
                // todo 画分针，设置分针的颜色
                mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_NEEDLW_STROKE_WIDTH_PERCENT);
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.LTGRAY);
                mNeedlePaint.setShader(null);

                pointerHeadXY = getPointerHeadXY(MINUTE_POINTER_LENGTH, degree);
                pointerInnerXY[0] = mCenterX + (pointerHeadXY[0] - mCenterX) / 2;
                pointerInnerXY[1] = mCenterY + (pointerHeadXY[1] - mCenterY) / 2;

                break;
            case 2:
                //秒针
                mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.GREEN);
                LinearGradient linearGradient = new LinearGradient(0, 0, 200, 0, rgb(228, 51, 255), rgb(109, 51, 255),
                        Shader.TileMode.MIRROR);
                mNeedlePaint.setShader(linearGradient);
                pointerHeadXY = getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
                pointerInnerXY[0] = mCenterX - (pointerHeadXY[0] - mCenterX) / 10;
                pointerInnerXY[1] = mCenterY - (pointerHeadXY[1] - mCenterY) / 10;
                break;
        }

        canvas.drawLine(mCenterX, mCenterY, pointerHeadXY[0], pointerHeadXY[1], mNeedlePaint);
        if(pointerType<=1){
            mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH / 2);
            mNeedlePaint.setColor(Color.WHITE);
        }else{
            canvas.drawCircle(mCenterX,mCenterY,mWidth * DEFAULT_DEGREE_STROKE_WIDTH,mNeedlePaint);
        }
        canvas.drawLine(mCenterX, mCenterY, pointerInnerXY[0], pointerInnerXY[1], mNeedlePaint);
    }

    private float[] getPointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (mCenterX + pointerLength * Math.sin(degree));
        xy[1] = (float) (mCenterY - pointerLength * Math.cos(degree));
        return xy;
    }


}