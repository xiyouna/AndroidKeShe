package cn.android.myapplication.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import cn.android.myapplication.NoteBean;


public class NoteDao {
    Context context;
    noteDBHelper dbHelper;

    public NoteDao(Context context) {
        this.context = context;
        dbHelper = new noteDBHelper(context, "note.db", null, 1);
    }

    public void insertNote(NoteBean bean) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note_tittle", bean.getTitle());
        cv.put("note_content", bean.getContent());
        cv.put("note_mark", bean.getMark());
        cv.put("createTime", bean.getCreateTime());
        cv.put("note_owner", bean.getOwner());
        cv.put("year", bean.getYear());
        cv.put("month", bean.getMonth());
        cv.put("image", bean.getImage());
        cv.put("day", bean.getDay());

        sqLiteDatabase.insert("note_data", null, cv);
    }

    public int DeleteNote(int id) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int ret = 0;
        ret = sqLiteDatabase.delete("note_data", "note_id=?", new String[]{id + ""});
        return ret;
    }

    public Cursor getAllData(String note_owner) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "select * from note_data where note_owner=?";
        return sqLiteDatabase.rawQuery(sql, new String[]{note_owner});
    }

    public void updateNote(NoteBean note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note_tittle", note.getTitle());
        cv.put("note_content", note.getContent());
        cv.put("note_mark", note.getMark());
        cv.put("year", note.getYear());
        cv.put("month", note.getMonth());
        cv.put("day", note.getDay());
        cv.put("image", note.getImage());

        db.update("note_data", cv, "note_id=?", new String[]{note.getId() + ""});
        db.close();
    }


    @SuppressLint("Range")
    public List<NoteBean> queryNotesAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<NoteBean> noteList = new ArrayList<>();
        NoteBean note;
        String sql = "select * from note_data where note_mark!=2   order by note_id desc";

        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            note = new NoteBean();
            note.setId(cursor.getInt(cursor.getColumnIndex("note_id")));
            note.setTitle(cursor.getString(cursor.getColumnIndex("note_tittle")));
            note.setContent(cursor.getString(cursor.getColumnIndex("note_content")));
            note.setMark(cursor.getInt(cursor.getColumnIndex("note_mark")));
            note.setYear(cursor.getString(cursor.getColumnIndex("year")));
            note.setMonth(cursor.getString(cursor.getColumnIndex("month")));
            note.setDay(cursor.getString(cursor.getColumnIndex("day")));
            note.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            note.setImage(cursor.getString(cursor.getColumnIndex("image")));
            noteList.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }

        return noteList;
    }

    public List<NoteBean> queryNotesAllByDate(String year, String month, String day) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<NoteBean> noteList = new ArrayList<>();
        NoteBean note;
        String sql;
        Cursor cursor = null;
        sql = "select * from note_data where  year='" + year + "' and  month='" + month + "' and  day='" + day + "' order by note_id desc";

        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            note = new NoteBean();
            note.setId(cursor.getInt(cursor.getColumnIndex("note_id")));
            note.setTitle(cursor.getString(cursor.getColumnIndex("note_tittle")));
            note.setContent(cursor.getString(cursor.getColumnIndex("note_content")));
            note.setImage(cursor.getString(cursor.getColumnIndex("image")));
            note.setMark(cursor.getInt(cursor.getColumnIndex("note_mark")));
            note.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            note.setYear(cursor.getString(cursor.getColumnIndex("year")));
            note.setMonth(cursor.getString(cursor.getColumnIndex("month")));
            note.setDay(cursor.getString(cursor.getColumnIndex("day")));
            noteList.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }

        return noteList;
    }

    public List<NoteBean> queryNotesAllByDate(String startTime, String endTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<NoteBean> noteList = new ArrayList<>();
        NoteBean note;
        String sql;
        Cursor cursor = null;
        sql = "select * from note_data where  createTime >'" + startTime + "' and  createTime<'" + endTime + "' order by note_id desc";

        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            note = new NoteBean();
            note.setId(cursor.getInt(cursor.getColumnIndex("note_id")));
            note.setTitle(cursor.getString(cursor.getColumnIndex("note_tittle")));
            note.setContent(cursor.getString(cursor.getColumnIndex("note_content")));
            note.setImage(cursor.getString(cursor.getColumnIndex("image")));
            note.setMark(cursor.getInt(cursor.getColumnIndex("note_mark")));
            note.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            note.setYear(cursor.getString(cursor.getColumnIndex("year")));
            note.setMonth(cursor.getString(cursor.getColumnIndex("month")));
            note.setDay(cursor.getString(cursor.getColumnIndex("day")));
            noteList.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }

        return noteList;
    }

    public List<NoteBean> queryNotesAllByYearAndMonth(String nowYear, String nowMonth) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<NoteBean> noteList = new ArrayList<>();
        NoteBean note;
        String sql;
        Cursor cursor = null;
        sql = "select * from note_data where year='" + nowYear + "' and month='" + nowMonth + "' order by note_id desc";

        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            note = new NoteBean();
            note.setId(cursor.getInt(cursor.getColumnIndex("note_id")));
            note.setTitle(cursor.getString(cursor.getColumnIndex("note_tittle")));
            note.setContent(cursor.getString(cursor.getColumnIndex("note_content")));
            note.setImage(cursor.getString(cursor.getColumnIndex("image")));
            note.setMark(cursor.getInt(cursor.getColumnIndex("note_mark")));
            note.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            note.setYear(cursor.getString(cursor.getColumnIndex("year")));
            note.setMonth(cursor.getString(cursor.getColumnIndex("month")));
            note.setDay(cursor.getString(cursor.getColumnIndex("day")));

            noteList.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }

        return noteList;
    }

    public int countType(String login_user, int mark) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select count(*) from note_data where note_owner=? and note_mark=?";
        Cursor cursor = db.rawQuery(sql, new String[]{login_user, mark + ""});
        int i = 0;
        while (cursor.moveToNext()) {
            i = cursor.getInt(0);
        }
        return i;
    }


}
