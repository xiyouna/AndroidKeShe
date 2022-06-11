package cn.android.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.haibin.calendarview.BaseView;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.android.myapplication.adapter.NoteListAdapter;
import cn.android.myapplication.db.NoteDao;

public class NoteListFragment extends Fragment implements
        View.OnClickListener {

    RecyclerView mRecyclerView;
    private BaseQuickAdapter adapter;
    private List<NoteBean> notes = new ArrayList<>();
    private NoteDao noteDao;
    private View inflate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_note_list, null, false);
        noteDao = new NoteDao(getContext());
        initData();

        return inflate;
    }


    protected void initData() {

        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.rv_note);
        inflate.findViewById(R.id.fb_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditActivity.class));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BaseQuickAdapter<NoteBean, BaseViewHolder>(R.layout.item_list_note) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseView, NoteBean noteBean) {
                baseView.setText(R.id.tv_list_title, noteBean.getTitle());
                baseView.setText(R.id.tv_time, noteBean.getCreateTime().substring(10, noteBean.getCreateTime().length()));
                baseView.setText(R.id.tv_date, noteBean.getCreateTime().substring(0, 10));
                baseView.setText(R.id.tv_content, noteBean.getContent());
                if (!TextUtils.isEmpty(noteBean.getImage())) {
                    Glide.with(getContext()).load(Uri.parse(noteBean.getImage())).into((ImageView) baseView.getView(R.id.iv_content));
                }
                View view = baseView.getView(R.id.ll_content);
                baseView.getView(R.id.card_view_note).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.setVisibility(view.isShown() ? View.GONE : View.VISIBLE);
                    }
                });
                CheckBox checkBox = baseView.getView(R.id.cb_complete);
                if (noteBean.getMark() == 1) {
                    checkBox.setChecked(true);
                }
                baseView.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteBean.setMark(2);
                        new NoteDao(NoteListFragment.this.getContext()).updateNote(noteBean);
                        adapter.setNewInstance(noteDao.queryNotesAll());
                    }
                });
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            noteBean.setMark(1);
                            new NoteDao(NoteListFragment.this.getContext()).updateNote(noteBean);
                        } else {
                            noteBean.setMark(0);
                            new NoteDao(NoteListFragment.this.getContext()).updateNote(noteBean);
                        }
                    }
                });
                if (TextUtils.isEmpty(noteBean.getImage())) {
                    baseView.setGone(R.id.iv_content, true);
                }

            }
        };
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                NoteBean noteBean = (NoteBean) adapter.getData().get(position);
                intent.putExtra("note", noteBean);
                startActivity(intent);
                return false;
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.setNewInstance(noteDao.queryNotesAll());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


}
