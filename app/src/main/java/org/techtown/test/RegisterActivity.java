// 회원가입처리
package org.techtown.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity {


    // sql 연동
    private EditText inputEmail, inputPw, inputPwck, inputNum, inputName, et_address_detail;
    private Button btn_register, btn_check;
    private AlertDialog dialog;
    private boolean validate = false;

    // 다음 주소 api
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private EditText et_zipcode, et_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {// 액티비티 시작 시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        // ↓툴바에 홈버튼을 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // 아이디 값 찾아주기
        inputEmail = findViewById(R.id.inputEmail);
        inputPw = findViewById(R.id.inputPw);
        inputPwck = findViewById(R.id.inputPwck);
        inputNum = findViewById(R.id.inputNum);
        inputName = findViewById(R.id.inputName);
        et_zipcode = findViewById(R.id.et_zipcode);
        et_address = findViewById(R.id.et_address);
        et_address_detail = findViewById(R.id.et_address_detail);

        //아이디 중복 체크
        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = inputEmail.getText().toString();
                if (validate) {
                    return; //검증 완료
                }

                if (userEmail.equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("이메일을 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                // 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    // 서버로부터 여기서 데이터를 받음
                    @Override
                    public void onResponse(String response) {

                        try {

                            // 서버로부터 받는 데이터는 json타입의 객체
                            JSONObject jsonResponse = new JSONObject(response);
                            // 그중 key값이 'success'인것을 가져온다.
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                inputEmail.setEnabled(false); //아이디값 고정
                                validate = true; //검증 완료
                                btn_check.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("이미 존재하는 아이디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });


        // 가입하기 버튼 클릭 시 수행
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userEmail = inputEmail.getText().toString();
                String userPassword = inputPw.getText().toString();
                String userNum = inputNum.getText().toString();
                String userName = inputName.getText().toString();
                String userAddress = et_zipcode.getText().toString() + " " + et_address.getText().toString() + " " + et_address_detail.getText().toString();
                //final String PassCk = join_pwck.getText().toString();
                // int userAge = Integer.parseInt(et_age.getText().toString());

                //아이디 중복체크 했는지 확인
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복된 아이디가 있는지 확인하세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                //한 칸이라도 입력 안했을 경우
                if (userEmail.equals("") || userPassword.equals("") || userName.equals("") ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("필수사항을 모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 서버로 Volley를 이용해서 요청을 함.
                RegisterRequest registerRequest = new RegisterRequest(userEmail, userPassword, userNum, userName, userAddress, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });// end of sql처리

        // 다음 주소 api 사용
        et_zipcode = (EditText) findViewById(R.id.et_zipcode);
        et_address = (EditText) findViewById(R.id.et_address);
        Button btn_findAddress = (Button) findViewById(R.id.btn_findAddress);

        if (btn_findAddress != null) {
            btn_findAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, WebViewActivity.class);
                    startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
                }
            });
        } // end of 다음 주소 api
    } // end of onCreate

    // 다음 주소 api
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        et_zipcode.setText(data.substring(0, 5));
                        et_address.setText(data.substring(7));
                    }
                }
            break;
        }
    }// end of 다음 주소 api

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
