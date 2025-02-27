package com.khanh.expensemanagement.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.khanh.expensemanagement.R;

public class SemiCircularProgressBar extends View {

    private Paint progressPaint;
    private Paint backgroundPaint;
    private float progress = 0; // Tiến trình từ 0 đến 100
    private float maxProgress = 100;
    private float arcWidth = 140; // Độ dày của đường cong

    public SemiCircularProgressBar(Context context) {
        super(context);
        init(context);
    }

    public SemiCircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SemiCircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Khởi tạo Paint cho nền
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(arcWidth);

        // Khởi tạo Paint cho tiến trình
        progressPaint = new Paint();
        progressPaint.setColor(ContextCompat.getColor(context, R.color.pink)); // Màu tiến trình
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(arcWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Tính toán bán kính và vị trí của vòng tròn
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2 - arcWidth;

        // Vẽ nền (vòng tròn nền)
        canvas.drawArc(arcWidth, arcWidth, width - arcWidth, height - arcWidth,
                180, 180, false, backgroundPaint);

        // Vẽ tiến trình (vòng tròn tiến trình)
        float sweepAngle = (progress / maxProgress) * 180; // Tính góc quét tiến trình
        canvas.drawArc(arcWidth, arcWidth, width - arcWidth, height - arcWidth,
                180, sweepAngle, false, progressPaint);
    }

    // Phương thức để thiết lập tiến trình
    public void setProgress(float progress) {
        if (progress > maxProgress) {
            this.progress = maxProgress;
        } else {
            this.progress = progress;
        }
        invalidate(); // Vẽ lại view
    }

    // Phương thức để lấy tiến trình hiện tại
    public float getProgress() {
        return progress;
    }

    // Phương thức để thiết lập giá trị tối đa
    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }
}
