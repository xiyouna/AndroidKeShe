package cn.android.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.android.myapplication.adapter.NoteListAdapter;
import cn.android.myapplication.db.NoteDao;

public class CalenderFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener,
        View.OnClickListener {
    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;


    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    RecyclerView mRecyclerView;
    private NoteListAdapter adapter;
    private List<NoteBean> notes = new ArrayList<>();
    private NoteDao noteDao;
    private String login_user = "123";
    private View inflate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_calender, null, false);
        noteDao = new NoteDao(getContext());
        login_user = getActivity().getIntent().getStringExtra("login_user");
        initView();
        initData();

        return inflate;
    }


    protected void initView() {
        mTextMonthDay = (TextView) inflate.findViewById(R.id.tv_month_day);
        mTextYear = (TextView) inflate.findViewById(R.id.tv_year);
        mTextLunar = (TextView) inflate.findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) inflate.findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) inflate.findViewById(R.id.calendarView);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });

        mCalendarLayout = (CalendarLayout) inflate.findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
    }


    protected void initData() {//遍历日历标记事项的小圆点
        List<NoteBean> noteBeans = noteDao.queryNotesAll();
        Map<String, Calendar> map = new HashMap<>();

        for (int i = 0; i < noteBeans.size(); i++) {
            String day = noteBeans.get(i).getDay();
            String text = noteBeans.get(i).getTitle().substring(0, 1);
            int year = Integer.parseInt(noteBeans.get(i).getYear());
            int month = Integer.parseInt(noteBeans.get(i).getMonth());
            map.put(getSchemeCalendar(year, month, Integer.parseInt(day), 0xEC810D,"事").toString(),
                    getSchemeCalendar(year, month, Integer.parseInt(day), 0xEC810D,"事"));
        }

        //Integer.parseInt函数是什么？？？？？？？？

        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.clearSchemeDate();
        mCalendarView.setSchemeDate(map);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteListAdapter();
        adapter.setOnItemClickListener(new NoteListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NoteBean note) {
              /*  Intent intent = new Intent(CalenderActivity.this, NoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtra("data", bundle);
                startActivity(intent);*/
            }
        });
        notes.clear();
        notes.addAll(noteDao.queryNotesAllByDate(mCalendarView.getCurYear() + "", mCalendarView.getCurMonth() + "", mCalendarView.getCurDay() + ""));
        adapter.setmNotes(notes);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onResume() {//回调
        super.onResume();
        initData();
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {//点击选择看哪天
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        notes.clear();
        String month = calendar.getMonth() < 10 ? "0" + calendar.getMonth() : calendar.getMonth() + "";
        String day =calendar.getDay() < 10 ? "0" + calendar.getDay() : calendar.getDay() + "";
        notes.addAll(noteDao.queryNotesAllByDate(calendar.getYear() + "", month + "", day + ""));//今日事项
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", year + "///" + month);
    }
}
