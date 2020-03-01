package com.example.ridesafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteconn extends SQLiteOpenHelper {

        public static String name="Contacts.db";
        public static String table_name="contacts";
        public SQLiteconn(Context context) {
            super(context,name, null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "+table_name+"(NAME TEXT, MNUMBER TEXT, PRIORITY TEXT)");
            db.execSQL("create table userMaster(NAME TEXT,MNUMBER TEXT,PASSWORD TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+table_name);
            onCreate(db);
        }
        public boolean insert(String name,String number,String priority)
        {
            Log.e("Name form before","hjhjhh");
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues c=new ContentValues();
            Log.d("Name form",""+name);
            c.put("NAME",""+name);
            c.put("MNUMBER",""+number);
            c.put("PRIORITY",""+priority);
            long res=db.insert(table_name,null,c);
            db.close();
            if(res == -1)
                return false;
            else
                return true;

        }

        public boolean createUser(String name,String phone,String password){
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues c=new ContentValues();
           // Log.d("Name form",""+name);
            c.put("NAME",""+name);
            c.put("MNUMBER",""+phone);
            c.put("PASSWORD",""+password);
            long res=db.insert("userMaster",null,c);
            db.close();
            if(res == -1)
                return false;
            else
                return true;
        }

        public Cursor getUser(String phone){
            SQLiteDatabase db=this.getReadableDatabase();

            Cursor c=db.rawQuery("select * from userMaster where MNUMBER='"+phone+"'",null);
            Cursor res=c;
            return c;
        }
        public Cursor viewAll()
        {
            SQLiteDatabase db=this.getReadableDatabase();

            Cursor c=db.rawQuery("select * from "+table_name+" order by priority",null);
            Cursor res=c;
            /*Log.d("inside viewall","jjj"+res.getCount());
            while(res.moveToNext()){
                Log.d("msg",res.getString(0));
//                s.append("Name: "+res.getString(0)+"hfklhklfhklh\n");
//                s.append("Mobile Number: "+res.getString(1).toString()+"\n");
//                s.append("Priority: "+res.getString(2).toString()+"\n\n");
//                Log.d("data kuch bhi",res.getString(0));
            }*/
            return c;
        }
    }
