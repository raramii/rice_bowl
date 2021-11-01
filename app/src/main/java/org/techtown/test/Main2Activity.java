package org.techtown.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Main2Activity<myPageFragment> extends AppCompatActivity {

    private TextView tv_id, tv_pass, tv_name;

    //String strNickname, strProfile, strEmail, strGender;

    BottomNavigationView bottomNavigationView; //하단 바텀네비게이션 뷰

    private static final String TAG = "Main_Activity"; // 로그를 위한 태그

    // 툴바와 메뉴
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Object HomeFragment;

    // 마이페이지 프래그먼트, 메이페이지 편집 프래그먼트
    MyPage myPageFragment;
    MyPgEdit myPgEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);
        //tv_name = findViewById(R.id.tv_name);

        bottomNavigationView = findViewById(R.id.navigationBottom);

        // activity로 데이터 전달하기
        // Intent intent = new Intent(현재 activity명.this, 전달해줄 activity명.class);
        // intent.putExtra("전달할 데이터명", 값);
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        String userPassword = intent.getStringExtra("userPassword");
        //String userName = intent.getStringExtra("userName");

        //tv_id.setText(userEmail);
        //tv_pass.setText(userPassword);
        //tv_name.setText(userName);

//
//        TextView tvNickname = findViewById(R.id.tvNickname);
//        ImageView ivProfile = findViewById(R.id.ivProfile);
//        TextView tvEmail = findViewById(R.id.tvEmail);
//        TextView tvGender = findViewById(R.id.tvGender);
        Button btnLogout = findViewById(R.id.btnLogout);
//        Intent intent = getIntent();
//        strNickname = intent.getStringExtra("userName");
//        strProfile = intent.getStringExtra("profile");
//        strEmail = intent.getStringExtra("userEmail");
//        strGender = intent.getStringExtra("gender");
//
//        tvNickname.setText(strNickname);
//        tvEmail.setText(strEmail);
//        tvGender.setText(strGender);
//
//        Glide.with(this).load(strProfile).into(ivProfile); //프로필 사진 url을 사진으로 보여줌

        // 로그아웃
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        // 첫 화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new HomeFragment()).commit(); // FrameLayout에 fragment.xml 띄우기

        // 하단바
        // 바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // 하단 메뉴 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.navigation_home: getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new HomeFragment()).commit(); break;
                    case R.id.navigation_map: getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment2()).commit(); break;
                    case R.id.navigation_mypage: getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MyPage()).commit(); break;
                }
                return true;
            }
        });

        // 햄버거 눌렀을 때
        ivMenu = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);

        // 액션바를 툴바로 변경하기
        setSupportActionBar(toolbar);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 클릭됨");
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


    }

    // 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return false;
    }

    // 마이페이지에서 편집페이지로 넘어감


}