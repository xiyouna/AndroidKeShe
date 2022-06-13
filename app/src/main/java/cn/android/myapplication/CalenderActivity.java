package cn.android.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.android.myapplication.db.NoteDao;

public class CalenderActivity extends AppCompatActivity {


    private FragmentTransaction fragmentTransaction;
    private CalenderFragment fragment;
    private boolean isShowCalender = true;
    private NoteListFragment fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        initView();
    }

    protected void initView() {
        setSupportActionBar(findViewById(R.id.toolbar));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = new CalenderFragment();
        fragmentTransaction.add(R.id.container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change://视图切换
                fragmentTransaction = getSupportFragmentManager().beginTransaction();//把fragment添加到activity,打包事务
                if (isShowCalender) {
                    fragment1 = new NoteListFragment();
                    fragmentTransaction.replace(R.id.container, fragment1).commit();//切换到列表
                    isShowCalender = false;
                } else {
                    fragmentTransaction.replace(R.id.container, fragment).commit();//切换到日历
                    isShowCalender = true;
                }
                break;
            case R.id.menu_tj://统计
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, AnalyseFragment.newInstance()).commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initData() {

    }


}
