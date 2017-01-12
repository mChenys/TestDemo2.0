package mchenys.net.csdn.blog.testmoden20.test2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mChenys on 2017/1/12.
 */
public class SingleTagView extends View {
    public SingleTagView(Context context) {
        super(context);
    }

    public SingleTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint_green = generatePaint(Color.GREEN, Paint.Style.FILL, 5);
        Paint paint_red = generatePaint(Color.RED, Paint.Style.STROKE, 5);

        Rect rect1 = new Rect(300, 10, 500, 100);
        canvas.drawRect(rect1, paint_red); //画出原轮廓

        canvas.rotate(30);//顺时针旋转画布
        canvas.drawRect(rect1, paint_green);//画出旋转后的矩形
    }

    private Paint generatePaint(int color, Paint.Style style, int width) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        return paint;
    }
}
