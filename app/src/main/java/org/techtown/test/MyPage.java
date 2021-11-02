package org.techtown.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MyPage extends Fragment {

    private View view;
    private ImageButton btn_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mypage, container, false);

        btn_edit = view.findViewById(R.id.btn_edit); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의

        // MyPage에서 MyPgEdit로 화면 전환
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Bundle bundle = new Bundle();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MyPgEdit myPgEdit = new MyPgEdit(); // 다른 프래그먼트 생성
                //myPgEdit.setArguments(bundle); // 번들에 만들었던 꾸러미를 프래그먼트에 넣어줌
                transaction.replace(R.id.main_frame, myPgEdit); // INTENT에서 STARTACRIVITY같은 것
                                                                    // 첫번째 인자 : 교체할 화면 영역 ,두번째 인자 : 바꿀화면
                                                                    // "현재페이지를 두번째 인자 페이지로 교체 해라"
                transaction.commit(); //저장. 교체 완료
            }
        });


        // 데이터베이스에서 닉네임 정보 불러오기





        return view;
    }
}