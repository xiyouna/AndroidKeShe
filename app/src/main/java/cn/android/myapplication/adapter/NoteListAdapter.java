package cn.android.myapplication.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import cn.android.myapplication.NoteBean;
import cn.android.myapplication.R;


/**
 * Note
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private List<NoteBean> mNotes;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private OnAlarmClickListener onAlarmClickListener;
    private int position;

    public void setOnAlarmClickListener(OnAlarmClickListener onAlarmClickListener) {
        this.onAlarmClickListener = onAlarmClickListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public NoteListAdapter() {
        mNotes = new ArrayList<>();
    }

    public void setmNotes(List<NoteBean> notes) {
        this.mNotes = notes;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (NoteBean) v.getTag());
        }
    }

//    @Override
//    public boolean onLongClick(View v) {
//        if (mOnItemLongClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            mOnItemLongClickListener.onItemLongClick(v,(NoteBean)v.getTag());
//        }
//        return true;
//    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NoteBean note);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

//    public interface OnRecyclerViewItemLongClickListener {
//        void onItemLongClick(View view, NoteBean note);
//    }

//    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
//        this.mOnItemLongClickListener = listener;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.i(TAG, "###onCreateViewHolder: ");
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_note, parent, false);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        //view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Log.i(TAG, "###onBindViewHolder: ");
        final NoteBean note = mNotes.get(position);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(note);
        //Log.e("adapter", "###record="+record);
        holder.tv_list_title.setText(note.getTitle());
        holder.tv_list_summary.setText(note.getContent());
        holder.tv_list_remindtime.setText(note.getCreateTime());

//        holder.tv_list_mark.setText(note.getMark()==0?"未完成":"已完成");
//        holder.tv_list_mark.setTextColor(note.getMark()==0?0xFF666666:0xFF9E9E9E);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        //Log.i(TAG, "###getItemCount: ");
        if (mNotes != null && mNotes.size() > 0) {
            return mNotes.size();
        }
        return 0;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView tv_list_title;//笔记标题
        public TextView tv_list_summary;//笔记摘要
        public TextView tv_list_remindtime;//创建时间
        public TextView tv_list_mark;

        public ViewHolder(View view) {
            super(view);
            tv_list_title = (TextView) view.findViewById(R.id.tv_list_title);
            tv_list_summary = (TextView) view.findViewById(R.id.tv_list_summary);
            tv_list_remindtime = (TextView) view.findViewById(R.id.tv_list_time);


            view.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("选择你的操作");
//            contextMenu.add(0, Menu.FIRST + 1, 0, "查看该笔记");
//            contextMenu.add(0, Menu.FIRST + 2, 0, "编辑该笔记");
//            contextMenu.add(0, Menu.FIRST + 3, 0, "删除该笔记");
//            contextMenu.add(0, Menu.FIRST + 4, 0, "标记为已完成");
//            contextMenu.add(0, Menu.FIRST + 5, 0, "标记为未完成");
//            contextMenu.add(0, Menu.FIRST + 6, 0, "设置日期");
//            contextMenu.add(0, Menu.FIRST + 7, 0, "清除提醒");
        }
    }
    public interface OnAlarmClickListener{
        void onClick(int position);
    }
}
