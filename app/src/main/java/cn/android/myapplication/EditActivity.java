package cn.android.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.Calendar;
import java.util.List;

import cn.android.myapplication.adapter.PhotoAdapter;
import cn.android.myapplication.db.NoteDao;
import cn.android.myapplication.utils.DateFormatUtils;

public class EditActivity extends AppCompatActivity {
    private String TAG = "Diary";
    List<Uri> mSelected;
    private EditText editText;
    private Button button;
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText etTitle;
    private TextView tvTime;
    private NoteBean note;
    private String imagePath;
    private String select_year;
    private String select_month;
    private String select_day;
    private String select_hour;
    private String select_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.photo_list);
        // 关联toolbar和menu，只需这一句代码菜单就可以正常显示了
        toolbar.inflateMenu(R.menu.toolbar_menu);
        editText = findViewById(R.id.edit_view);
        etTitle = findViewById(R.id.edit_title);
        tvTime = findViewById(R.id.tv_time);
        button =findViewById(R.id.edit_floating);
        note = (NoteBean) getIntent().getSerializableExtra("note");
        SharedPreferences prefs = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editText.setTextSize(prefs.getInt("edit_font_size", 16));
        editText.requestFocus();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (note != null) {
            imagePath = note.getImage();
            etTitle.setText(note.getTitle());
            editText.setText(note.getContent());
            tvTime.setText(note.getCreateTime());
            select_day = note.getDay();
            select_month = note.getMonth();
            select_year = note.getYear();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(EditActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etTitle.getText().toString())) {
                    Toast.makeText(EditActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (note != null) {
                    note.setTitle(etTitle.getText().toString());
                    note.setContent(editText.getText().toString());
                    note.setCreateTime(tvTime.getText().toString());
                    note.setImage(imagePath);
                    note.setYear(select_year);
                    note.setMonth(select_month);
                    note.setDay(select_day);
                    new NoteDao(EditActivity.this).updateNote(note);
                } else {
                    note = new NoteBean();
                    note.setYear(select_year);
                    note.setMonth(select_month);
                    note.setDay(select_day);
                    note.setTitle(etTitle.getText().toString());
                    note.setContent(editText.getText().toString());
                    note.setCreateTime(tvTime.getText().toString());
                    note.setImage(imagePath);
                    new NoteDao(EditActivity.this).insertNote(note);
                }
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.item2:
//                        Matisse.from(EditActivity.this)
//                                .choose(MimeType.ofAll())
//                                .countable(true)
//                                .maxSelectable(9)
//                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                                .thumbnailScale(0.85f)
//                                .imageEngine(new GlideEngine())
//                                .showPreview(false) // Default is `true`
//                                .forResult(REQUEST_CODE_CHOOSE);
//                        break;
                }
                return false;
            }
        });
        long currentTimeMillis2 = System.currentTimeMillis();
        String text = DateFormatUtils.long2Str(currentTimeMillis2, DateFormatUtils.DATE_FORMAT_PATTERN_YMD_HM);
        tvTime.setText(text);
        String[] s = text.split(" ")[0].split("-");
        select_year = s[0]+"";
        select_month = s[1]+"";
        select_day = s[2]+"";


        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到Calendar实例，可以通过这个实例得到当前年月日时分秒
                Calendar c = Calendar.getInstance();
                DatePickerDialog datagramPacket = new DatePickerDialog(EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            //在弹出DatePickerDialog中，点击确定后会触发onDateSet方法
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                //月份是从0开始，所以+1
                                select_month = String.valueOf(month + 1);
                                select_day = String.valueOf(day);
                                select_year = String.valueOf(year);
                                //月份、日小于10，前面加一个0保持2位数显示
                                if ((month + 1) < 10) {
                                    select_month = "0" + (month + 1);
                                }
                                if (day < 10) {
                                    select_day = "0" + day;
                                }
                                new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        select_hour = String.valueOf(hourOfDay);
                                        select_minute = String.valueOf(minute);
                                        if (hourOfDay < 10) {
                                            select_hour = "0" + hourOfDay;
                                        }
                                        if (minute < 10) {
                                            select_minute = "0" + minute;
                                        }
                                        tvTime.setText(year + "-" + select_month + "-" + select_day + " " + select_hour + ":" + select_minute);

                                    }
                                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
                                //把选择的日期填写到布局中EditText中
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datagramPacket.show();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("未保存就退出吗吗？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            }).show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            imagePath = mSelected.get(0).toString();
            PhotoAdapter photoAdapter = new PhotoAdapter(mSelected);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(EditActivity.this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(photoAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}