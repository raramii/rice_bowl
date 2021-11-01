// 회원가입값요청
package org.techtown.test;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://rarara14.ivyro.net/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userEmail, String userPassword, String userNum, String userName, String userAddress, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userEmail",userEmail);
        map.put("userPassword", userPassword);
        map.put("userNum",userNum);
        map.put("userName", userName);
        map.put("userAddress", userAddress);
        //map.put("",);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
