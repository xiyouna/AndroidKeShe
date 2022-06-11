package cn.android.myapplication.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LineChartManager {
    private YAxis leftYAxis;
    private Legend legend;
    private LimitLine limitLine;
    private LineChart lineChart;
    private YAxis rightYAxis;
    private XAxis xAxis;

    public LineChartManager(LineChart lineChart2, int i) {
        this.lineChart = lineChart2;
        this.leftYAxis = lineChart2.getAxisLeft();
        this.rightYAxis = lineChart2.getAxisRight();
        this.xAxis = lineChart2.getXAxis();
        initChart(lineChart2, i);
    }

    private void initChart(LineChart lineChart2, int i) {
        lineChart2.setDrawGridBackground(false);
        lineChart2.setBackgroundColor(-16777216);
        lineChart2.setDrawBorders(false);
        lineChart2.setDoubleTapToZoomEnabled(false);
        lineChart2.animateY(1000);
        this.xAxis = lineChart2.getXAxis();
        this.leftYAxis = lineChart2.getAxisLeft();
        YAxis axisRight = lineChart2.getAxisRight();
        this.rightYAxis = axisRight;
        axisRight.setDrawGridLines(false);
        this.leftYAxis.setDrawGridLines(false);
        this.rightYAxis.setEnabled(false);
        this.leftYAxis.setEnabled(false);
        this.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        this.xAxis.setGranularity(1.0f);
        this.xAxis.setGridColor(Color.parseColor("#666666"));
        this.xAxis.setDrawGridLines(true);
        this.xAxis.setGridLineWidth(1.0f);
        this.xAxis.setTextColor(-1);
        this.leftYAxis.setAxisMinimum((float) ((-i) / 2));
        this.leftYAxis.setAxisMaximum((float) i);
        Legend legend2 = lineChart2.getLegend();
        this.legend = legend2;
        legend2.setForm(Legend.LegendForm.LINE);
        this.legend.setTextSize(12.0f);
        this.legend.setTextColor(Color.parseColor("#FC4E5A"));
        this.legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        this.legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        this.legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        this.legend.setDrawInside(false);
        this.legend.setEnabled(false);
    }

    private void initLineDataSet(LineDataSet lineDataSet, int i, LineDataSet.Mode mode) {
        lineDataSet.setColor(i);
        lineDataSet.setCircleColor(i);
        lineDataSet.setLineWidth(1.0f);
        lineDataSet.setCircleRadius(3.0f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10.0f);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1.0f);
        lineDataSet.setFormSize(15.0f);
        if (mode == null) {
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    public void showOneLineChart(List<Float> list, String str, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            arrayList.add(new Entry(list.get(i2).floatValue(), list.get(i2).floatValue()));
        }
        LineDataSet lineDataSet = new LineDataSet(arrayList, str);
        initLineDataSet(lineDataSet, i, LineDataSet.Mode.CUBIC_BEZIER);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        this.lineChart.setData(lineData);
    }

    public void showMultiNormalLineChart(List<List<Float>> list, List<String> list2, List<Integer> list3) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < list.get(i).size(); i2++) {
                arrayList2.add(new Entry(list.get(i).get(i2).floatValue(), list.get(i).get(i2).floatValue()));
            }
            LineDataSet lineDataSet = new LineDataSet(arrayList2, list2.get(i));
            initLineDataSet(lineDataSet, list3.get(i).intValue(), LineDataSet.Mode.CUBIC_BEZIER);
            arrayList.add(lineDataSet);
        }
        this.lineChart.setData(new LineData(arrayList));
    }

    public void setXAxisData(float f, float f2, int i) {
        this.xAxis.setAxisMinimum(f);
        this.xAxis.setAxisMaximum(f2);
        this.xAxis.setLabelCount(i, false);
        this.lineChart.invalidate();
    }

    public void setXAxisData(final List<String> list, int i) {
        this.xAxis.setLabelCount(i, false);
        this.xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float f) {
                return (String) list.get(((int) f) % list.size());
            }
        });
        this.lineChart.invalidate();
    }

    public void setYAxisData(float f, float f2, int i) {
        this.leftYAxis.setAxisMaximum(f);
        this.leftYAxis.setAxisMinimum(f2);
        this.leftYAxis.setLabelCount(i, false);
        this.rightYAxis.setAxisMaximum(f);
        this.rightYAxis.setAxisMinimum(f2);
        this.rightYAxis.setLabelCount(i, false);
        this.lineChart.invalidate();
    }

    public void setYAxisData(final List<String> list, int i) {
        this.xAxis.setLabelCount(i, false);
        this.xAxis.setValueFormatter(new ValueFormatter() {
            /* class com.qiyin.wheelsurf.view.LineChartManager.AnonymousClass2 */

            @Override // com.github.mikephil.charting.formatter.ValueFormatter
            public String getFormattedValue(float f) {
                return (String) list.get(((int) f) % list.size());
            }
        });
        this.lineChart.invalidate();
    }

    public void setHighLimitLine(float f, String str, int i) {
        if (str == null) {
            str = "高限制线";
        }
        LimitLine limitLine2 = new LimitLine(f, str);
        limitLine2.setLineWidth(2.0f);
        limitLine2.setTextSize(10.0f);
        limitLine2.setLineColor(i);
        limitLine2.setTextColor(i);
        this.leftYAxis.addLimitLine(limitLine2);
        this.lineChart.invalidate();
    }

    public void setLowLimitLine(float f, String str, int i) {
        if (str == null) {
            str = "高限制线";
        }
        LimitLine limitLine2 = new LimitLine(f, str);
        limitLine2.setLineWidth(2.0f);
        limitLine2.setTextSize(10.0f);
        limitLine2.setLineColor(i);
        limitLine2.setTextColor(i);
        this.leftYAxis.addLimitLine(limitLine2);
        this.lineChart.invalidate();
    }

    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        this.lineChart.setDescription(description);
        this.lineChart.invalidate();
    }

    public void setChartFillDrawable(Drawable drawable) {
        if (this.lineChart.getData() != null && ((LineData) this.lineChart.getData()).getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) ((LineData) this.lineChart.getData()).getDataSetByIndex(0);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            this.lineChart.invalidate();
        }
    }

    public void showLineChart(final List<Entry> list, String str, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < list.size(); i3++) {
            Entry entry = new Entry((float) i3, list.get(i3).getY());
            entry.setData(list.get(i3).getData());
            arrayList.add(entry);
        }
        this.lineChart.zoom(((float) (list.size() / 7)) / 1.2f, 1.0f, 0.0f, 0.0f);
        this.xAxis.setLabelCount(list.size(), false);
        this.xAxis.setAxisMinimum(0.0f);
        this.xAxis.setDrawAxisLine(false);
        this.xAxis.setValueFormatter(new ValueFormatter() {
            /* class com.qiyin.wheelsurf.view.LineChartManager.AnonymousClass3 */

            @Override // com.github.mikephil.charting.formatter.ValueFormatter
            public String getFormattedValue(float f) {
                return new DecimalFormat("#").format((double) (f + 1.0f));
            }
        });
        LineDataSet lineDataSet = new LineDataSet(arrayList, str);
        initLineDataSet(lineDataSet, i2, LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            /* class com.qiyin.wheelsurf.view.LineChartManager.AnonymousClass4 */

            @Override // com.github.mikephil.charting.formatter.ValueFormatter
            public String getFormattedValue(float f) {
                return ((int) ((Entry) list.get((int) f)).getX()) + "";
            }
        });
        this.lineChart.setData(new LineData(lineDataSet));
        lineDataSet.setHighlightEnabled(true);
        this.lineChart.highlightValue((float) i, 0);
    }

    public void setMarkerView(Context context) {
        LineChartMarkView lineChartMarkView = new LineChartMarkView(context, this.xAxis.getValueFormatter());
        lineChartMarkView.setChartView(this.lineChart);
        this.lineChart.setMarker(lineChartMarkView);
        this.lineChart.invalidate();
    }
}