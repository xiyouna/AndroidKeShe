package cn.android.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cn.android.myapplication.R;

public class LineChartMarkView extends MarkerView {
    private final int ARROW_HEIGHT = dp2px(10);
    private final float ARROW_OFFSET = ((float) dp2px(2));
    private final int ARROW_WIDTH = dp2px(15);
    private final float BG_CORNER = ((float) dp2px(4));
    private final int DEFAULT_INDICATOR_COLOR = -1;
    private Bitmap bitmapForDot;
    private int bitmapHeight;
    private int bitmapWidth;
    DecimalFormat df = new DecimalFormat("0.00");
    private TextView tvDate;
    private TextView tvValue0;
    private TextView tvValue1;
    private TextView tvValue2;

    private ValueFormatter valueFormatter;

    public LineChartMarkView(Context context, ValueFormatter valueFormatter2) {
        super(context, R.layout.layout_markview);
        this.valueFormatter = valueFormatter2;
        this.tvDate = (TextView) findViewById(R.id.tv_date);
        this.tvValue0 = (TextView) findViewById(R.id.tv_value0);
        this.tvValue1 = (TextView) findViewById(R.id.tv_value1);
        this.tvValue2 = (TextView) findViewById(R.id.tv_value2);

        Bitmap bitmap = getBitmap(context, R.drawable.ic_brightness_curve_point);
        this.bitmapForDot = bitmap;
        if (bitmap != null) {
            this.bitmapWidth = bitmap.getWidth();
            this.bitmapHeight = this.bitmapForDot.getHeight();
        }
    }

    private static Bitmap getBitmap(Context context, int i) {
        try {
            if (Build.VERSION.SDK_INT <= 21) {
                return BitmapFactory.decodeResource(context.getResources(), i);
            }
            Drawable drawable = context.getDrawable(i);
            Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return createBitmap;
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // com.github.mikephil.charting.components.MarkerView, com.github.mikephil.charting.components.IMarker
    public void refreshContent(Entry entry, Highlight highlight) {
        Chart chartView = getChartView();
        if (chartView instanceof LineChart) {
            List dataSets = ((LineChart) chartView).getLineData().getDataSets();
            for (int i = 0; i < dataSets.size(); i++) {
                LineDataSet lineDataSet = (LineDataSet) dataSets.get(i);
                ((Entry) lineDataSet.getValues().get((int) entry.getX())).getY();
                try {
                    JSONObject jSONObject = new JSONObject(((Entry) lineDataSet.getValues().get((int) entry.getX())).getData().toString());
                    this.tvDate.setText(jSONObject.getString("dayCount"));
                    this.tvValue0.setText(jSONObject.getString("monthCount"));
                    this.tvValue1.setText(jSONObject.getString("average"));
                    this.tvValue2.setText(jSONObject.getString("cancel"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        super.refreshContent(entry, highlight);
    }

    @Override // com.github.mikephil.charting.components.MarkerView, com.github.mikephil.charting.components.IMarker
    public MPPointF getOffset() {
        return new MPPointF((float) (-(getWidth() / 2)), (float) (-getHeight()));
    }

    @Override // com.github.mikephil.charting.components.MarkerView, com.github.mikephil.charting.components.IMarker
    public void draw(Canvas canvas, float f, float f2) {
        Chart chartView = getChartView();
        if (chartView == null) {
            super.draw(canvas, f, f2);
            return;
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(-1);
        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL);
        paint2.setAntiAlias(true);
        paint2.setColor(-1);
        float width = (float) getWidth();
        float height = (float) getHeight();
        int save = canvas.save();
        canvas.translate(f, f2);
        Bitmap bitmap = this.bitmapForDot;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, ((float) (-this.bitmapWidth)) / 2.0f, ((float) (-this.bitmapHeight)) / 2.0f, (Paint) null);
        }
        Path path = new Path();
        int i = this.ARROW_HEIGHT;
        int i2 = this.bitmapHeight;
        if (f2 < ((float) i) + height + (((float) i2) / 2.0f)) {
            canvas.translate(0.0f, ((float) i) + height + (((float) i2) / 2.0f));
            float f3 = width / 2.0f;
            if (f > ((float) chartView.getWidth()) - f3) {
                canvas.translate(-(f3 - (((float) chartView.getWidth()) - f)), 0.0f);
                float f4 = this.ARROW_OFFSET;
                path.moveTo((f3 - (((float) chartView.getWidth()) - f)) - f4, -(((float) this.ARROW_HEIGHT) + height + f4));
                path.lineTo(((float) this.ARROW_WIDTH) / 2.0f, -(height - this.BG_CORNER));
                path.lineTo(((float) (-this.ARROW_WIDTH)) / 2.0f, -(height - this.BG_CORNER));
                float width2 = f3 - (((float) chartView.getWidth()) - f);
                float f5 = this.ARROW_OFFSET;
                path.moveTo(width2 - f5, -(((float) this.ARROW_HEIGHT) + height + f5));
            } else if (f > f3) {
                path.moveTo(0.0f, -(((float) this.ARROW_HEIGHT) + height));
                path.lineTo(((float) this.ARROW_WIDTH) / 2.0f, -(height - this.BG_CORNER));
                path.lineTo(((float) (-this.ARROW_WIDTH)) / 2.0f, -(height - this.BG_CORNER));
                path.lineTo(0.0f, -(((float) this.ARROW_HEIGHT) + height));
            } else {
                float f6 = f3 - f;
                canvas.translate(f6, 0.0f);
                float f7 = -f6;
                float f8 = this.ARROW_OFFSET;
                path.moveTo(f7 - f8, -(((float) this.ARROW_HEIGHT) + height + f8));
                path.lineTo(((float) this.ARROW_WIDTH) / 2.0f, -(height - this.BG_CORNER));
                path.lineTo(((float) (-this.ARROW_WIDTH)) / 2.0f, -(height - this.BG_CORNER));
                float f9 = this.ARROW_OFFSET;
                path.moveTo(f7 - f9, -(((float) this.ARROW_HEIGHT) + height + f9));
            }
            float f10 = (-width) / 2.0f;
            float f11 = -height;
            RectF rectF = new RectF(f10, f11, f3, 0.0f);
            canvas.drawPath(path, paint2);
            float f12 = this.BG_CORNER;
            canvas.drawRoundRect(rectF, f12, f12, paint);
            canvas.translate(f10, f11);
        } else {
            canvas.translate(0.0f, ((-height) - ((float) i)) - (((float) i2) / 2.0f));
            float f13 = width / 2.0f;
            if (f < f13) {
                float f14 = f13 - f;
                canvas.translate(f14, 0.0f);
                float f15 = -f14;
                float f16 = this.ARROW_OFFSET;
                path.moveTo(f15 + f16, ((float) this.ARROW_HEIGHT) + height + f16);
                path.lineTo(((float) this.ARROW_WIDTH) / 2.0f, height - this.BG_CORNER);
                path.lineTo(((float) (-this.ARROW_WIDTH)) / 2.0f, height - this.BG_CORNER);
                float f17 = this.ARROW_OFFSET;
                path.moveTo(f15 + f17, ((float) this.ARROW_HEIGHT) + height + f17);
            } else if (f > ((float) chartView.getWidth()) - f13) {
                canvas.translate(-(f13 - (((float) chartView.getWidth()) - f)), 0.0f);
                float f18 = this.ARROW_OFFSET;
                path.moveTo((f13 - (((float) chartView.getWidth()) - f)) + f18, ((float) this.ARROW_HEIGHT) + height + f18);
                path.lineTo(((float) this.ARROW_WIDTH) / 2.0f, height - this.BG_CORNER);
                path.lineTo(((float) (-this.ARROW_WIDTH)) / 2.0f, height - this.BG_CORNER);
                float width3 = f13 - (((float) chartView.getWidth()) - f);
                float f19 = this.ARROW_OFFSET;
                path.moveTo(width3 + f19, ((float) this.ARROW_HEIGHT) + height + f19);
            } else {
                path.moveTo(0.0f, ((float) this.ARROW_HEIGHT) + height);
                path.lineTo(((float) this.ARROW_WIDTH) / 2.0f, height - this.BG_CORNER);
                path.lineTo(((float) (-this.ARROW_WIDTH)) / 2.0f, height - this.BG_CORNER);
                path.moveTo(0.0f, ((float) this.ARROW_HEIGHT) + height);
            }
            float f20 = (-width) / 2.0f;
            RectF rectF2 = new RectF(f20, 0.0f, f13, height);
            canvas.drawPath(path, paint2);
            float f21 = this.BG_CORNER;
            canvas.drawRoundRect(rectF2, f21, f21, paint);
            canvas.translate(f20, 0.0f);
        }
        draw(canvas);
        canvas.restoreToCount(save);
    }

    private int dp2px(int i) {
        return (int) TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics());
    }
}