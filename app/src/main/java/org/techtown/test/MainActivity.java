package org.techtown.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.function.Function;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {

    // sql 연동
    private TextInputEditText et_id, et_pass;
    private Button btn_login, btn_join;
    private boolean validate = false;

    // 카카오 로그인
//    private SessionCallback sessionCallback;
    // 카카오 로그인2
    private View loginButton, logoutButton;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 카카오 로그인2
        updateKakoLoginUi();
        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) { // 로그인 성공
                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                }
                if (throwable != null) { // 로그인 오류 났을 경우
                }
                updateKakoLoginUi();
                return null;
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기기에 카카오톡이 설치되어있는 경우와 되어있지않은 경우
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) { // 기기에 카톡 설치o
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
                } else { // 기기에 카톡 설치 x
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);

                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakoLoginUi();
                        return null;
                    }
                });
            }
        });

        // 카카오 로그인
//        sessionCallback = new SessionCallback();
//        Session.getCurrentSession().addCallback(sessionCallback);
//        Session.getCurrentSession().checkAndImplicitOpen();
//        getAppKeyHash();

        // btn_findEmail 버튼의 click 이벤트
        Button btn_findEmail = findViewById(R.id.btn_findEmail);
        btn_findEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindEmailActivity.class);
                startActivity(intent);
            }
        });

        // btn_findPswd 버튼의 click 이벤트
        Button btn_findPswd = findViewById(R.id.btn_findPw);
        btn_findPswd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindPswdActivity.class);
                startActivity(intent);
            }
        });

        // btn_join 버튼의 click 이벤트
        Button btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        // 툴바 설정
        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // sql 연동
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);

        // 로그인 버튼을 클릭 시 수행
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get해옴
                String userEmail = et_id.getText().toString();
                String userPassword = et_pass.getText().toString();

//                if (validate) {
//                    return;
//                }
//
//                if (userEmail) {
//
//                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                String userEmail = jsonObject.getString("userEmail");
                                String userPassword = jsonObject.getString("userPassword");

                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                intent.putExtra("userEmail", userEmail); // 데이터 담아줌
                                intent.putExtra("userPassword", userPassword); // 데이터 담아줌
                                startActivity(intent);
                            } else { // 로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userEmail, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);

            }
        });

    } // end of sql

    // 카카오 로그인2
    private void updateKakoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>(){
            @Override
            public Unit invoke(User user, Throwable throwable) {
                // 로그인 여부 확인 ( 로그인 되어있다면 로그아웃 버튼 보이게, 안되어있다면 로그인 버튼 보이게)
                if (user != null) {

                    Log.i(TAG, "invoke: id=" + user.getId());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());

                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);
                } else {
                    // 닉네임, 프로필이미지 널로 설정
                    loginButton.setVisibility(View.VISIBLE);
                   logoutButton.setVisibility(View.GONE);
                }
                return null;
            }
        });
    }

//    // 카카오 로그인 설정
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//            return;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Session.getCurrentSession().removeCallback(sessionCallback);
//    }
//
//    private class SessionCallback implements ISessionCallback {
//        @Override
//        public void onSessionOpened() {
//            UserManagement.getInstance().me(new MeV2ResponseCallback() {
//                @Override
//                public void onFailure(ErrorResult errorResult) {
//                    int result = errorResult.getErrorCode();
//
//                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
//                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onSessionClosed(ErrorResult errorResult) {
//                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onSuccess(MeV2Response result) {
//                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
//                    intent.putExtra("name", result.getNickname());
//                    intent.putExtra("profile", result.getProfileImagePath());
//                    if(result.getKakaoAccount().hasEmail() == OptionalBoolean.TRUE)
//                        intent.putExtra("email", result.getKakaoAccount().getEmail());
//                    else
//                        intent.putExtra("email", "none");
//                    if(result.getKakaoAccount().hasGender() == OptionalBoolean.TRUE)
//                        intent.putExtra("gender", result.getKakaoAccount().getGender().getValue());
//                    else
//                        intent.putExtra("gender", "none");
//                    startActivity(intent);
//                    finish();
//                }
//            });
//        }
//
//        @Override
//        public void onSessionOpenFailed(KakaoException e) {
//            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void getAppKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                Log.e("Hash key", something);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            Log.e("name not found", e.toString());
//        }
//    } // end of 카카오 로그인

}