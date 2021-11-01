package org.techtown.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FindPswdActivity2 extends AppCompatActivity {

    private TextView tv_pswd;
    private Button btn_to_login;

    phpdo task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pswd2);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userEmail = intent.getStringExtra("userEmail");
        String userNum = intent.getStringExtra("userNum");

        task = new FindPswdActivity2.phpdo();
        tv_pswd = (TextView)findViewById(R.id.tv_id);
        task.execute(userName,userEmail,userNum);

        // btn_to_login 버튼의 click 이벤트
        btn_to_login = findViewById(R.id.btn_to_login);
        btn_to_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FindPswdActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class phpdo extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {

            try {

                String userName = arg0[0];
                String userEmail = arg0[1];
                String userNum = arg0[2];

                String link = "http://rarara14.ivyro.net/FindPswd.php?userName=" + userName + "&userEmail=" + userEmail + "&userNum=" + userNum; // 탐색하고싶은 url
                URL url = new URL(link); // url화 함
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // url을 연결한 객체 생성
                conn.setRequestMethod("GET"); // get방식 통신
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false);  // 캐싱데이터를 받을지 안받을 지
                conn.setDefaultUseCaches(false);  // 캐싱데이터 디폴트 값 설정

                String strCookie = conn.getHeaderField("Set-Cookie"); // 쿠키데이터 보관

                InputStream is = conn.getInputStream(); // input 스트림 개방

                StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //문자열 셋 세팅
                String result;

                while ((result = reader.readLine()) != null) {
                    builder.append(result);
                }

                result = builder.toString();
                tv_pswd.setText(result);

            } catch (MalformedURLException | ProtocolException exception) {
                exception.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
            return null;
        }
    }
}