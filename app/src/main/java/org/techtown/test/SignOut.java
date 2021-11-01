package org.techtown.test;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SignOut extends Fragment {

    private View view;
    private Button btn_y;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_out, container, false);

        // 확인버튼 눌렀을 때 대화상자 출력
        btn_y = view.findViewById(R.id.btn_y);
        btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OnClickHandler();
            }
        });

        return view;
    }

    // 대화상자(Dialog) 내용
//    public void OnClickHandler()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder();
//
//        builder.setTitle("정말 탈퇴하시겠습니까?");
//
//        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int id)
//            {
//                //finish();
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
}