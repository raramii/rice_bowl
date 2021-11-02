package org.techtown.test;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static private String URL = "http://rarara14.ivyro.net/.php";
    private Map<String, String> map;

    public ProfileRequest(int id, String userName, String userNum, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userName", userName);
        map.get("id");
        map.put("userNum", userNum);
    }
}