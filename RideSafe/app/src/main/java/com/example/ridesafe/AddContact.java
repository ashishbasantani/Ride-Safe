package com.example.ridesafe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AddContact extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SQLiteconn db;
    Button submit;
    String n,m,p;
    TextInputLayout name,phone;
    private String mParam1;
    private String mParam2;

    public AddContact() {

    }


    public static AddContact newInstance(String param1, String param2) {
        AddContact fragment = new AddContact();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        db=new SQLiteconn(view.getContext().getApplicationContext());

        name=view.findViewById(R.id.name);
        phone=view.findViewById(R.id.phoneNumber);




        submit=view.findViewById(R.id.button);

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*if(n.trim().length()==0 || m.trim().length()==0 || p.trim().length()==0){
                            Toast.makeText(getContext().getApplicationContext(),"Please fill all details",Toast.LENGTH_LONG).show();
                        }
                        else {*/
                        //n=name.getText().toString();
                        //m=mobile.getText().toString();

                        Log.d("flow checking","kk");
                        Log.e("before call", "onClick: "+n+" "+m+" "+p);
                        boolean flag = db.insert(""+n, ""+m, ""+p);
                        String msg;
                        if (flag) {
                            msg = "Contact Added";
                        } else {
                            msg = "Record insertion failed";
                        }
                        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                       name.setText("");
//                        phone.setText("");

                        //name.requestFocus();
                    }
                    //
                    // }
                }
        );

    }
}
