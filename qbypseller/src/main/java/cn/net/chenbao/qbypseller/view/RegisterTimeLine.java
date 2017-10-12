package cn.net.chenbao.qbypseller.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.net.chenbao.qbypseller.R;

/**
 * 反正都是死的,就写死了 商家注册时间轴
 *
 * @author licheng
 */
public class RegisterTimeLine extends LinearLayout {

    private Paint grayPaint;
    private Paint yellowPaint;
    private Paint whitePaint;
    private Context context;
    /**
     * 屏幕宽
     */
    private int with;
    /**
     * 第几步
     */
    private int step;
    /**
     * 半径
     */
    private int r = 20;

    public RegisterTimeLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        this.context = context;
        with = context.getResources().getDisplayMetrics().widthPixels;
    }

    public RegisterTimeLine(Context context) {
        super(context);
        initPaint();
    }

    public void initPaint() {
        yellowPaint = new Paint();
        yellowPaint.setColor(getResources().getColor(R.color.text_yellow));//#FC4243
        yellowPaint.setAntiAlias(true);
        yellowPaint.setStrokeWidth(5);

        grayPaint = new Paint();
        grayPaint.setColor(getResources().getColor(R.color.text_b3));
        grayPaint.setAntiAlias(true);
        grayPaint.setStrokeWidth(5);

        whitePaint = new Paint();
        whitePaint.setColor(getResources().getColor(R.color.white));
        whitePaint.setAntiAlias(true);
        whitePaint.setStrokeWidth(5);
        whitePaint.setTextAlign(Align.CENTER);
        whitePaint.setTextSize(25);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        switch (step) {
            case 1:
                canvas.drawLine(with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        with - with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        grayPaint);
                canvas.drawCircle(with / 4 / 2, getHeight() / 2 + getPaddingTop(),// 一个黄圆
                        r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop(), r, grayPaint);

                canvas.drawCircle(with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop(), r, grayPaint);
                canvas.drawCircle(with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop(), r, grayPaint);

                canvas.drawText("√", with / 4 / 2, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);

                break;
            case 2:

                canvas.drawLine(with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        with / 4 / 2 * 3, getHeight() / 2 + getPaddingTop(),
                        yellowPaint);
                canvas.drawLine(with / 4 / 2 * 3,
                        getHeight() / 2 + getPaddingTop(), with / 4 / 2 * 7,
                        getHeight() / 2 + getPaddingTop(), grayPaint);


                canvas.drawCircle(with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop(), r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop(), r, grayPaint);
                canvas.drawCircle(with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop(), r, grayPaint);


                canvas.drawText("√", with / 4 / 2, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);

                break;
            case 3:
                canvas.drawLine(with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        with / 4 / 2 * 5, getHeight() / 2 + getPaddingTop(),
                        yellowPaint);
                canvas.drawLine(with / 4 / 2 * 5,
                        getHeight() / 2 + getPaddingTop(), with / 4 / 2 * 7,
                        getHeight() / 2 + getPaddingTop(), grayPaint);
                canvas.drawCircle(with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop(), r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop(), r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop(), r, grayPaint);

                canvas.drawText("√", with / 4 / 2, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);

                break;
            case 4:
                canvas.drawLine(with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        with / 4 / 2 * 7, getHeight() / 2 + getPaddingTop(),
                        yellowPaint);

                canvas.drawCircle(with / 4 / 2, getHeight() / 2 + getPaddingTop(),
                        r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop(), r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop(), r, yellowPaint);
                canvas.drawCircle(with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop(), r, yellowPaint);

                canvas.drawText("√", with / 4 / 2, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 3, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 5, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);
                canvas.drawText("√", with / 4 / 2 * 7, getHeight() / 2
                        + getPaddingTop() + r / 2, whitePaint);

                break;
        }

    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
        invalidate();
    }
}
