package com.afa.fizicianu.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.afa.fizicianu.models.Question;

import java.util.ArrayList;


public class SQLController {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public SQLController(Context c){
        this.context = c;
    }

    public SQLController open() throws SQLException{
        dbHelper = dbHelper.getInstantce(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
        db.close();
    }

    public ArrayList<Question> getQuestions(/*String category*/) throws SQLException {
        open();
        String query;
        ArrayList<Question> questions = new ArrayList<Question>();
        query = "select * from Probleme";
        Cursor c = db.rawQuery(query,null);
        while(c.moveToNext()){
            Question question = new Question();
            question.setIntrebare(c.getString(c.getColumnIndex("Intrebare")));
            question.setR1(c.getString(c.getColumnIndex("R1")));
            question.setR2(c.getString(c.getColumnIndex("R2")));
            question.setR3(c.getString(c.getColumnIndex("R3")));
            question.setR4(c.getString(c.getColumnIndex("R4")));
            question.setRC(c.getInt(c.getColumnIndex("RC")));
            questions.add(question);
        }

        Log.v("DB", String.valueOf(questions.size()));
        close();
        return questions;

    }

}
