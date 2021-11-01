package org.techtown.test;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindEmailRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://rarara14.ivyro.net/FindEmail.php";
    private Map<String, String> map;

    public FindEmailRequest(String userName, Response.Listener<String> listener){
        super(Method.POST, URL, listener,null);

        map = new HashMap<>();
        map.put("userName", userName);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
