package cn.android.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        noteDao = new NoteDao(this);
        login_user = getIntent().getStringExtra("login_user");
        initView();
        initData();
    }

    protected void initView() {
        setSupportActionBar(findViewById(R.id.toolbar));
        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
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

        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change:
                break;
            case R.id.menu_tj:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initData() {
        List<NoteBean> noteBeans = noteDao.queryNotesAll();
        Map<String, Calendar> map = new HashMap<>();

        for (int i = 0; i < noteBeans.size(); i++) {
            String day = noteBeans.get(i).getDay();
            String text = noteBeans.get(i).getTitle().substring(0, 1);
            int year = Integer.parseInt(noteBeans.get(i).getYear());
            int month = Integer.parseInt(noteBeans.get(i).getMonth());
            map.put(getSchemeCalendar(year, month, Integer.parseInt(day), 0xFF40db25, text).toString(),
                    getSchemeCalendar(year, month, Integer.parseInt(day), 0xFF40db25, text));
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.clearSchemeDate();
        mCalendarView.setSchemeDate(map);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    protected void onResume() {
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
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        notes.clear();
        notes.addAll(noteDao.queryNotesAllByDate( calendar.getYear() + "", calendar.getMonth() + "", calendar.getDay() + ""));
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
