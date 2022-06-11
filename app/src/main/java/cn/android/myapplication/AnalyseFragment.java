package cn.android.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.android.myapplication.db.NoteDao;
import cn.android.myapplication.utils.DateFormatUtils;
import cn.android.myapplication.view.LineChartManager;

/**
 * 统计
 */
public class AnalyseFragment extends Fragment implements View.OnClickListener {
    private Calendar cal;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");
    private FrameLayout fl_content1;
    private int month;
    private TextView tv_title;
    private int year;
    private View inflate;
    private Button btnQuery;
    private String startTime;
    private String endTime;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_analyse, container, false);
        initView();
        return inflate;
    }

    public static AnalyseFragment newInstance() {

        Bundle args = new Bundle();

        AnalyseFragment fragment = new AnalyseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initView() {
        fl_content1 = (FrameLayout) inflate.findViewById(R.id.fl_content1);
        inflate.findViewById(R.id.iv_left).setOnClickListener(this);
        inflate.findViewById(R.id.iv_right).setOnClickListener(this);
        tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        tvEndTime = inflate.findViewById(R.id.tv_end_time);
        tvStartTime = inflate.findViewById(R.id.tv_start_time);
        btnQuery = inflate.findViewById(R.id.btn_query);
        tvResult = inflate.findViewById(R.id.tv_result);
        Calendar instance = Calendar.getInstance();
        cal = instance;
        year = instance.get(1);
        month = cal.get(2) + 1;
        TextView textView = tv_title;
        textView.setText(year + "年" + month + "月");
        getData(year, month);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(startTime)) {
                    Toast.makeText(getContext(), "请选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(endTime)) {
                    Toast.makeText(getContext(), "请选择结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                NoteDao noteDao = new NoteDao(getContext());
                List<NoteBean> noteBeans = noteDao.queryNotesAllByDate(startTime, endTime);
                int complete = 0;
                int cancel = 0;
                int timeout = 0;
                for (int j = 0; j < noteBeans.size(); j++) {
                    NoteBean noteBean = noteBeans.get(j);
                    String createTime = noteBean.getCreateTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try {
                        long time = simpleDateFormat.parse(createTime).getTime();
                        if (time < System.currentTimeMillis()&&noteBean.getMark()!=1&&noteBean.getMark()!=2) {
                            timeout++;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (noteBean.getMark() == 1) {
                        complete++;
                    }
                    if (noteBean.getMark() == 2) {
                        cancel++;
                    }
                }
                tvResult.setText("总共事项：" + noteBeans.size() + ",已完成" + complete + ",已取消" + cancel + ",已逾期" + timeout);


            }
        });
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到Calendar实例，可以通过这个实例得到当前年月日时分秒
                Calendar c = Calendar.getInstance();
                DatePickerDialog datagramPacket = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            //在弹出DatePickerDialog中，点击确定后会触发onDateSet方法
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                //月份是从0开始，所以+1
                                String select_month = String.valueOf(month + 1);
                                String select_day = String.valueOf(day);
                                String select_year = String.valueOf(year);
                                //月份、日小于10，前面加一个0保持2位数显示
                                if ((month + 1) < 10) {
                                    select_month = "0" + (month + 1);
                                }
                                if (day < 10) {
                                    select_day = "0" + day;
                                }
                                String finalSelect_month = select_month;
                                String finalSelect_day = select_day;
                                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String select_hour = String.valueOf(hourOfDay);
                                        String select_minute = String.valueOf(minute);
                                        if (hourOfDay < 10) {
                                            select_hour = "0" + hourOfDay;
                                        }
                                        if (minute < 10) {
                                            select_minute = "0" + minute;
                                        }
                                        tvStartTime.setText(year + "-" + finalSelect_month + "-" + finalSelect_day + " " + select_hour + ":" + select_minute);

                                        startTime = tvStartTime.getText().toString();
                                    }
                                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
                                //把选择的日期填写到布局中EditText中
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datagramPacket.show();
            }
        });
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到Calendar实例，可以通过这个实例得到当前年月日时分秒
                Calendar c = Calendar.getInstance();
                DatePickerDialog datagramPacket = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            //在弹出DatePickerDialog中，点击确定后会触发onDateSet方法
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                //月份是从0开始，所以+1
                                String select_month = String.valueOf(month + 1);
                                String select_day = String.valueOf(day);
                                String select_year = String.valueOf(year);
                                //月份、日小于10，前面加一个0保持2位数显示
                                if ((month + 1) < 10) {
                                    select_month = "0" + (month + 1);
                                }
                                if (day < 10) {
                                    select_day = "0" + day;
                                }
                                String finalSelect_month = select_month;
                                String finalSelect_day = select_day;
                                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String select_hour = String.valueOf(hourOfDay);
                                        String select_minute = String.valueOf(minute);
                                        if (hourOfDay < 10) {
                                            select_hour = "0" + hourOfDay;
                                        }
                                        if (minute < 10) {
                                            select_minute = "0" + minute;
                                        }
                                        tvEndTime.setText(year + "-" + finalSelect_month + "-" + finalSelect_day + " " + select_hour + ":" + select_minute);

                                        endTime = tvEndTime.getText().toString();
                                    }
                                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
                                //把选择的日期填写到布局中EditText中
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datagramPacket.show();
            }
        });

    }

    private int days(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;

        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;

        } else {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }

    }

    private void getData(int year, int month) {


        List<Entry> arrayList4 = new ArrayList<>();

        for (int i = 1; i < days(year, month); i++) {
            NoteDao noteDao = new NoteDao(getContext());
            List<NoteBean> noteBeans = noteDao.queryNotesAllByDate(year + "", (month < 10 ? "0" + month : month + "") + "", i < 10 ? "0" + i : i + "");
            int complete = 0;
            int cancel = 0;
            int timeout = 0;
            for (int j = 0; j < noteBeans.size(); j++) {
                NoteBean noteBean = noteBeans.get(j);
                String createTime = noteBean.getCreateTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    long time = simpleDateFormat.parse(createTime).getTime();
                    if (time < System.currentTimeMillis()&&noteBean.getMark()!=1&&noteBean.getMark()!=2) {
                        timeout++;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (noteBean.getMark() == 1) {
                    complete++;
                }
                if (noteBean.getMark() == 2) {
                    cancel++;
                }
            }
            dataModel datamodel2 = new dataModel();
            datamodel2.setType(1);
            datamodel2.setDayCount("今日事项：" + noteBeans.size());
            datamodel2.setMonthCount("已完成：" + complete);
            datamodel2.setAverage("延期：" + timeout);
            datamodel2.setCancel("取消：" + cancel);

            arrayList4.add(new Entry((float) i, noteBeans.size(), new Gson().toJson(datamodel2)));
        }
        LineChart lineChart = new LineChart(getContext());
        lineChart.moveViewToX((float) (Integer.parseInt(DateFormatUtils.long2Str(System.currentTimeMillis(), "dd")) - 8));
        fl_content1.removeAllViews();
        fl_content1.addView(lineChart);
        LineChartManager lineChartManager = new LineChartManager(lineChart, days(year, month));
        lineChartManager.setMarkerView(requireActivity());

        lineChartManager.showLineChart(arrayList4, "", 5, getResources().getColor(R.color.white));

    }

    @Override
    public void onResume() {
        super.onResume();
        getData(year, month);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                int i = month;
                if (i == 1) {
                    month = 12;
                    year--;
                } else {
                    month = i - 1;
                }
                tv_title.setText(year + "年" + month + "月");
                getData(year, month);
                return;
            case R.id.iv_right:
                int i2 = month;
                if (i2 == 12) {
                    month = 1;
                    year++;
                } else {
                    month = i2 + 1;
                }
                tv_title.setText(year + "年" + month + "月");
                getData(year, month);
                return;
            default:
                return;
        }
    }

    public class dataModel {
        String average;
        String dayCount;
        String monthCount;
        String cancel;

        public String getCancel() {
            return cancel;
        }

        public void setCancel(String cancel) {
            this.cancel = cancel;
        }

        int type;

        public dataModel() {
        }

        public String getDayCount() {
            return dayCount;
        }

        public void setDayCount(String str) {
            dayCount = str;
        }

        public int getType() {
            return type;
        }

        public void setType(int i) {
            type = i;
        }

        public String getMonthCount() {
            return monthCount;
        }

        public void setMonthCount(String str) {
            monthCount = str;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String str) {
            average = str;
        }
    }
}