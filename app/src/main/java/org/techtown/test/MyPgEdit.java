package org.techtown.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class MyPgEdit extends Fragment {

    private View view;
    private Button btn_signout;
    private Button btn_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_pg_edit, container, false);

        btn_signout = view.findViewById(R.id.btn_signout);
        btn_logout = view.findViewById(R.id.btn_logout);


        // 회원탈퇴페이지로 이동
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle bundle = new Bundle();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SignOut signOut = new SignOut();// 다른 프래그먼트 생성
                //myPgEdit.setArguments(bundle); // 번들에 만들었던 꾸러미를 프래그먼트에 넣어줌
                transaction.replace(R.id.main_frame, signOut); // INTENT에서 STARTACRIVITY같은 것
                // 첫번째 인자 : 교체할 화면 영역 ,두번째 인자 : 바꿀화면
                // "현재페이지를 두번째 인자 페이지로 교체 해라"
                transaction.commit(); //저장. 교체 완료
            }
        });



        return view;
    }
}