package org.techtown.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class FindEmailActivity extends AppCompatActivity {

    // sql 연동
    private TextInputEditText inputName, inputNum;
    private Button btn_y;
    private AlertDialog dialog;
    private boolean validate = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findemail);

        // 닉네임, 핸드폰번호
        inputName = findViewById(R.id.inputName);
        inputNum = findViewById(R.id.inputNumber);

        // 확인버튼 눌렀을 때 대화상자 출력
        btn_y = findViewById(R.id.btn_y);
        btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = inputName.getText().toString();
                String userNum = inputNum.getText().toString();

                Intent intent = new Intent(FindEmailActivity.this, FindEmailActivity2.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userNum", userNum);
                startActivity(intent);
            }

        });

        // 대화상자(Dialog) 내용
//    public void OnClickHandler()
//    {
//        Intent intent = getIntent();
//        String userEmail = intent.getStringExtra("userEmail");
//
//        //String userEmail = inputName.getText().toString();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("가입한 이메일 확인");
//        builder.setMessage(intent.getStringExtra("userEmail"));
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
}
