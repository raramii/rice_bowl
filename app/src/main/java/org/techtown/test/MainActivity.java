package org.techtown.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_login = findViewById(R.id.btn_login);

        // btn_login 버튼의 Click 이벤트
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에서 MenuActivity로 화면 전환
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        Button btn_findEmail = findViewById(R.id.btn_findEmail);

        // btn_findEmail 버튼의 click 이벤트
        btn_findEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FindEmailActivity.class);
                startActivity(intent);
            }
        });

        Button btn_findPswd = findViewById(R.id.btn_findPswd);

        // btn_findPswd 버튼의 click 이벤트
        btn_findPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FindPswdActivity.class);
                startActivity(intent);
            }
        });

        Button btn_join = findViewById(R.id.btn_join);

        // btn_join 버튼의 click 이벤트
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,JoinActivity.class);
                startActivity(intent);
            }
        });

    }
}