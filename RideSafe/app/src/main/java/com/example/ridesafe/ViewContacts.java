package com.example.ridesafe;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewContacts extends Fragment {
    @Nullable

    SQLiteconn db=null;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView t=view.findViewById(R.id.text);
        db=new SQLiteconn(view.getContext());

        Cursor res=db.viewAll();
        if(res.getCount()!=0){
            StringBuffer s=new StringBuffer();
            while(res.moveToNext()){
                Log.d("pta nhi kya",""+res.getColumnIndex("NAME"));
                s.append("Name: "+res.getString(0)+"\n");
                s.append("Mobile Number: "+res.getString(1).toString()+"\n");
                s.append("Priority: "+res.getString(2).toString()+"\n\n");
                Log.d("data kuch bhi",res.getString(0));
            }
            t.setText(s);
        }
        db.close();
    }
}
