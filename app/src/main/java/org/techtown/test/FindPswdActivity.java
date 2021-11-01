package org.techtown.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class FindPswdActivity extends AppCompatActivity {

    // sql 연동
    private TextInputEditText inputName, inputEmail, inputNum;
    private Button btn_py;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpswd);

        // 닉네임, 이메일, 핸드폰번호
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputNum = findViewById(R.id.inputNum);

        // 확인버튼 눌렀을 때 대화상자 출력
        btn_py = findViewById(R.id.btn_py);
        btn_py.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = inputName.getText().toString();
                String userEmail = inputEmail.getText().toString();
                String userNum = inputNum.getText().toString();

                Intent intent = new Intent(FindPswdActivity.this, FindPswdActivity2.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userNum", userNum);
                startActivity(intent);

            }
        });
    }

    // 대화상자(Dialog) 내용
//    public void OnClickHandler()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("가입한 비밀번호 확인").setMessage("aaaa");
//
//        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int id)
//            {
//                finish();
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }


}
