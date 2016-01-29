package tt.zhuangzai.com.zhuangzai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luozi on 2016/1/27.
 */
public class ZhuangZaiView extends View {

    private Paint bodyPaint, facePain, testSPaint, testFPatin, eyePaint;
    //===
    private float canvasW = 0, canvasH = 0;//body width and height
    private float startX = 0, endX = 0, startY = 0, endY = 0;//开始结束的xy坐标
    private float headW = 0, headH = 0;//head width and height
    private float mastW = 0, mastH = 0;//mast width and height
    private float faceW = 0, faceH = 0;//face width and height
    private float eyeDiaOut = 0, eyeDiaIn = 0, eyeball, eyeSpacing;//眼睛外圆，内圆，眼珠大小的半径和眼睛间距
    private float bodyW = 0, bodyH = 0;//body width and height
    private float bodyRound = 20;//body round angle
    private float textDia = 0;//桩字的半径
    private float handW = 0, handH = 0;//hand width and height
    private float footW = 0, footH = 0;//foot width and height

    private static final float HEAD_SCALE = 0.59f;//头部占整个身体的比重
    private static final float CANVAS_SCALE = 0.7f;//绘制的整个矩形横向和纵向的比重

    private Bitmap zhuangBitmap;

    //=============
    RectF canvasRect;

    Path headPath;
    RectF headUpRect;
    RectF headDownRect;

    Path mastPath;
    RectF mastRect;
    RectF mastUpRect;
    RectF mastDownRect;

    Path facePath;
    RectF faceUpRect;
    RectF faceDownRect;

    RectF bodyRect;
    RectF textRect;

    Path leftHandPath;
    RectF leftHandRect;

    Path rightHandPath;
    RectF rightHandRect;

    Path footPath;
    RectF leftFootRect;
    RectF rightFootRect;

    public ZhuangZaiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZhuangZaiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bodyPaint = new Paint();
        bodyPaint.setStyle(Paint.Style.FILL);
        bodyPaint.setAntiAlias(true);
        bodyPaint.setColor(Color.parseColor("#299dff"));

        facePain = new Paint();
        facePain.setStyle(Paint.Style.FILL);
        facePain.setAntiAlias(true);
        facePain.setColor(Color.WHITE);

        testSPaint = new Paint();
        testSPaint.setAntiAlias(true);
        testSPaint.setStyle(Paint.Style.STROKE);
        testSPaint.setColor(Color.RED);

        testFPatin = new Paint();
        testFPatin.setStyle(Paint.Style.FILL);
        testFPatin.setAntiAlias(true);
        testFPatin.setColor(Color.BLACK);

        eyePaint = new Paint();
        eyePaint.setStyle(Paint.Style.FILL);
        eyePaint.setAntiAlias(true);
        eyePaint.setColor(Color.parseColor("#716f6e"));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initParms(changed, left, top, right, bottom);
    }

    private void initParms(boolean changed, int left, int top, int right, int bottom) {
        int rCoe = Math.min(getWidth(), getHeight());
        canvasH = rCoe;
        canvasW = rCoe * CANVAS_SCALE;

        //整个人物的起始坐标
        startX = getWidth() / 2 - (canvasW / 2);
        endX = getWidth() / 2 + (canvasW / 2);
        startY = getHeight() / 2 - (canvasH / 2);
        endY = getHeight() / 2 + (canvasH / 2);

        //head
        headH = canvasH * HEAD_SCALE;
        headW = canvasW;

        //mast
        mastH = canvasH * 0.13f;
        mastW = canvasW * 0.13f;

        //face
        faceH = headH * 0.737f;
        faceW = headW * 0.744f;

        //eye
        eyeDiaOut = headW * 0.068f * 0.5f;
        eyeDiaIn = headW * 0.041f * 0.5f;
        eyeball = headW * 0.022f * 0.5f;
        eyeSpacing = headW * 0.351f * 0.5f;

        //body
        bodyW = canvasW * 0.434f;
        bodyH = canvasH * 0.22f;
        bodyRound = bodyW * 0.275f;

        //桩字的半径
        textDia = canvasW * 0.2f * 0.5f;

        //hand
        handW = canvasW * 0.291f;
        handH = canvasH * 0.108f;

        //foot
        footW = canvasW * 0.112f;
        footH = canvasH * 0.151f;

        zhuangBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zhunag_logo);

        canvasRect = new RectF(startX, startY, endX, endY);

        headPath = new Path();
        headUpRect = new RectF(canvasRect.left, startY + mastH, canvasRect.right, startY + mastH + (headH * 0.58f));
        headDownRect = new RectF(canvasRect.left, startY + mastH + (headH * 0.58f), canvasRect.right, mastH + headH + startY);

        mastPath = new Path();
        mastRect = new RectF(canvasRect.centerX() - (mastW / 2), startY, canvasRect.centerX() + (mastW / 2), startY + mastH);
        mastUpRect = new RectF(canvasRect.centerX() - (mastRect.width() * 0.33f), mastRect.top, canvasRect.centerX() + (mastRect.width() * 0.33f), mastRect.top + mastH * 0.7f);
        mastDownRect = new RectF(canvasRect.centerX() - (mastW * 0.11f), mastUpRect.bottom, canvasRect.centerX() + (mastW * 0.11f), startY + mastH);

        facePath = new Path();
        faceUpRect = new RectF(canvasRect.centerX() - (faceW / 2), headUpRect.top + (canvasH * 0.133f), canvasRect.centerX() + (faceW / 2), headUpRect.top + (canvasH * 0.35f));
        faceDownRect = new RectF(faceUpRect.left, faceUpRect.bottom, faceUpRect.right, faceUpRect.top + faceH);

        bodyRect = new RectF(canvasRect.centerX() - (bodyW / 2), faceDownRect.bottom, canvasRect.centerX() + (bodyW / 2), faceDownRect.bottom + bodyH);
        textRect = new RectF(bodyRect.centerX() - textDia, bodyRect.centerY() - textDia, bodyRect.centerX() + textDia, bodyRect.centerY() + textDia);

        leftHandPath = new Path();
        leftHandRect = new RectF(bodyRect.left - handW + (canvasW * 0.094f), faceDownRect.bottom,
                bodyRect.left + (canvasW * 0.094f), faceDownRect.bottom + handH);

        rightHandPath = new Path();
        rightHandRect = new RectF(bodyRect.right - (canvasW * 0.094f), faceDownRect.bottom,
                bodyRect.right + handW - (canvasW * 0.094f), faceDownRect.bottom + handH);

        footPath = new Path();
        leftFootRect = new RectF(canvasRect.centerX() - (canvasW * 0.137f), bodyRect.bottom - (footH * 0.1f),
                canvasRect.centerX() - (canvasW * 0.137f) + footW, canvasRect.bottom);
        rightFootRect = new RectF(canvasRect.centerX() + (canvasW * 0.137f) - footW, bodyRect.bottom - (footH * 0.1f),
                canvasRect.centerX() + (canvasW * 0.137f), canvasRect.bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画头部
         * */
        headPath.moveTo(headUpRect.left, headUpRect.bottom);//头部纵向左边的中间坐标,加上天线额高度
        //头部上半部分
        headPath.cubicTo((headUpRect.width() * 0.03f) + headUpRect.left, headUpRect.top - mastH,
                (headUpRect.width() * 0.97f) + headUpRect.left, headUpRect.top - mastH, headUpRect.right, headUpRect.bottom);
        headPath.moveTo(headUpRect.left, headUpRect.bottom);//头部纵向左边的中间坐标,加上天线额高度
        //头部下半部分
        headPath.cubicTo((headUpRect.width() * 0.03f) + headDownRect.left, headDownRect.bottom + mastH / 5 * 3,
                (headDownRect.width() * 0.97f) + headDownRect.left, headDownRect.bottom + mastH / 5 * 3, endX, headUpRect.bottom);
        headPath.close();
        canvas.drawPath(headPath, bodyPaint);

        /**
         * 画天线
         * */
        mastPath.moveTo(mastUpRect.left, mastRect.top);
        //画天线的左边弧度
        mastPath.quadTo(mastRect.left - (mastW * 0.13f), mastUpRect.top + (mastUpRect.height() * 0.41f / 2), mastUpRect.left, mastUpRect.top + (mastUpRect.height() * 0.41f));
        mastPath.lineTo(mastUpRect.left, mastUpRect.top + (mastUpRect.height() * 0.58f));
        mastPath.quadTo(mastRect.left - (mastW * 0.13f), mastUpRect.bottom - (mastUpRect.height() * 0.41f / 2), mastUpRect.left, mastUpRect.bottom);
        //画天线底部矩形
        mastPath.lineTo(mastDownRect.left, mastDownRect.top);
        mastPath.lineTo(mastDownRect.left, mastDownRect.bottom);
        mastPath.lineTo(mastDownRect.right, mastDownRect.bottom);
        mastPath.lineTo(mastDownRect.right, mastDownRect.top);
        //画天线右边弧度
        mastPath.lineTo(mastUpRect.right, mastUpRect.bottom);
        mastPath.quadTo(mastRect.right + (mastW * 0.13f), mastUpRect.bottom - (mastUpRect.height() * 0.41f / 2), mastUpRect.right, mastUpRect.bottom - (mastUpRect.height() * 0.41f));
        mastPath.lineTo(mastUpRect.right, mastUpRect.top + (mastUpRect.height() * 0.41f));
        mastPath.quadTo(mastRect.right + (mastW * 0.13f), mastUpRect.top + (mastUpRect.height() * 0.41f / 2), mastUpRect.right, mastUpRect.top);
        mastPath.close();
        canvas.drawPath(mastPath, bodyPaint);

        /**
         * 画脸
         * */
        facePath.moveTo(faceUpRect.left, faceUpRect.bottom);
        //脸上半部分
        facePath.cubicTo((faceUpRect.width() * 0.05f) + faceUpRect.left, (headUpRect.top + faceUpRect.top) * 0.5f,
                (faceUpRect.width() * 0.95f) + faceUpRect.left, (headUpRect.top + faceUpRect.top) * 0.5f, faceUpRect.right, faceUpRect.bottom);
        facePath.moveTo(faceUpRect.left, faceUpRect.bottom);//头部纵向左边的中间坐标,加上天线额高度
        //脸下半部分
        facePath.cubicTo(faceDownRect.left, headDownRect.bottom,
                faceDownRect.width() + faceDownRect.left, headDownRect.bottom, faceDownRect.right, faceDownRect.top);
        facePath.close();
        canvas.drawPath(facePath, facePain);

        /**
         * 画眼睛
         * */
        //左眼
        canvas.drawCircle(headUpRect.centerX() - eyeSpacing, faceUpRect.bottom + eyeDiaOut, eyeDiaOut, eyePaint);
        eyePaint.setColor(Color.parseColor("#3a3734"));
        canvas.drawCircle(headUpRect.centerX() - eyeSpacing, faceUpRect.bottom + eyeDiaOut, eyeDiaIn, eyePaint);
        eyePaint.setColor(Color.parseColor("#8a8887"));
        canvas.drawCircle(headUpRect.centerX() - eyeSpacing, faceUpRect.bottom + eyeDiaOut, eyeball, eyePaint);
        //右眼
        canvas.drawCircle(headUpRect.centerX() + eyeSpacing, faceUpRect.bottom + eyeDiaOut, eyeDiaOut, eyePaint);
        eyePaint.setColor(Color.parseColor("#3a3734"));
        canvas.drawCircle(headUpRect.centerX() + eyeSpacing, faceUpRect.bottom + eyeDiaOut, eyeDiaIn, eyePaint);
        eyePaint.setColor(Color.parseColor("#8a8887"));
        canvas.drawCircle(headUpRect.centerX() + eyeSpacing, faceUpRect.bottom + eyeDiaOut, eyeball, eyePaint);

        /**
         * 画身体
         * */
        canvas.drawRoundRect(bodyRect, bodyRound, bodyRound, bodyPaint);

        /**
         * 画桩字
         * */
        canvas.drawBitmap(zhuangBitmap, null, textRect, bodyPaint);

        /**
         * 画左手
         * */
        leftHandPath.moveTo(leftHandRect.right, leftHandRect.top - (leftHandRect.height() / 3));
        leftHandPath.cubicTo(leftHandRect.left - (leftHandRect.width() / 3), leftHandRect.bottom - (leftHandRect.height() / 3),
                leftHandRect.left - (leftHandRect.width() / 3), leftHandRect.bottom + (leftHandRect.height() / 3),
                leftHandRect.right, leftHandRect.bottom - (leftHandRect.height() / 3));
        leftHandPath.close();
        canvas.drawPath(leftHandPath, bodyPaint);

        /**
         * 画右手
         * */
        rightHandPath.moveTo(rightHandRect.left, rightHandRect.top - (rightHandRect.height() / 3));
        rightHandPath.cubicTo(rightHandRect.right + (rightHandRect.width() / 3), rightHandRect.bottom - (rightHandRect.height() / 3),
                rightHandRect.right + (rightHandRect.width() / 3), rightHandRect.bottom + (rightHandRect.height() / 3),
                rightHandRect.left, rightHandRect.bottom - (rightHandRect.height() / 3));
        rightHandPath.close();
        canvas.drawPath(rightHandPath, bodyPaint);

        /**
         * 画脚
         * */
        footPath.moveTo(leftFootRect.left, leftFootRect.top);
        footPath.cubicTo(leftFootRect.left + (leftFootRect.width() / 3), leftFootRect.bottom + (leftFootRect.height() * 0.2f),
                leftFootRect.right - (leftFootRect.width() / 3), leftFootRect.bottom + (leftFootRect.height() * 0.2f),
                leftFootRect.right, leftFootRect.top + (leftFootRect.height() * 0.4f));

        footPath.lineTo(leftFootRect.right, leftFootRect.top + leftFootRect.height() * 0.4f);
        footPath.lineTo(rightFootRect.left, rightFootRect.top + rightFootRect.height() * 0.4f);

        footPath.cubicTo(rightFootRect.left + (rightFootRect.width() / 3), rightFootRect.bottom + (rightFootRect.height() * 0.2f),
                rightFootRect.right - (rightFootRect.width() / 3), rightFootRect.bottom + (rightFootRect.height() * 0.2f),
                rightFootRect.right, rightFootRect.top);
        canvas.drawPath(footPath, bodyPaint);


//        canvas.drawRect(canvasRect, testSPaint);
//        canvas.drawRect(bodyRect, testSPaint);
//        canvas.drawRect(headUpRect, testSPaint);
//        canvas.drawRect(headDownRect, testSPaint);
//        canvas.drawRect(faceUpRect, testSPaint);
//        canvas.drawRect(faceDownRect, testSPaint);
//        canvas.drawRect(textRect, testSPaint);
//        canvas.drawRect(leftHandRect, testSPaint);
//        canvas.drawRect(rightHandRect, testSPaint);
//        canvas.drawRect(rightFootRect, testSPaint);
//        canvas.drawRect(leftFootRect, testSPaint);
//        canvas.drawRect(canvasRect, testSPaint);


//        canvas.drawCircle(leftFootRect.left + (leftFootRect.width() / 3), leftFootRect.bottom, 2, testFPatin);
//        canvas.drawCircle(leftFootRect.right - (leftFootRect.width() / 3), leftFootRect.bottom, 2, testFPatin);
//        canvas.drawCircle(leftFootRect.right, leftFootRect.top + (leftFootRect.height() * 0.4f), 2, testFPatin);
//        canvas.drawCircle((headDownRect.width() * 0.97f) + headDownRect.left, headDownRect.bottom * 1.15f, 2, testFPatin);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (zhuangBitmap != null)
            zhuangBitmap.recycle();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
