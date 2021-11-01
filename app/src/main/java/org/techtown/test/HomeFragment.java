package org.techtown.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class HomeFragment extends Fragment {

    private ViewGroup viewGroup; // 뷰그룹 객체
        int i=0;
        ViewPager viewPager;
        private org.techtown.test.Fragment2 fragment2;
        private MyPage myPage;

    public void Frag1(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup conatainer,
                             @Nullable Bundle savedInstanceState) {
        // 뷰그룹 인플레이션 한 후 다시 viewGroup에 리턴
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, conatainer, false);

        setInit();


        return viewGroup;
    }

    private void setInit() {
        /* setup infinity scroll viewpager */
        // 여기서 뷰페이저를 참조
        ViewPager2 viewPageSetUp = viewGroup.findViewById(R.id.viewPager2);
        // 프래그먼트에서는 getActivity로 참조하고, 액티비티에서는 this를 사용
        org.techtown.test.FragPagerAdapter SetupPagerAdapter = new org.techtown.test.FragPagerAdapter(getActivity());
        // FragPagerAdapter를 파라머티로 받고 ViewPager2에 전달 받는다.
        viewPageSetUp.setAdapter(SetupPagerAdapter);
        //방향은 가로로
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //페이지 한계 지정 갯수
        viewPageSetUp.setOffscreenPageLimit(3);
        // 무제한 스크롤 처럼 보이기 위해서는 0페이지 부터가 아니라 1000페이지 부터 시작해서 좌측으로 이동할 경우 999페이지로 이동하여 무제한 처럼 스크롤 되는 것 처럼 표현하기 위함.
        viewPageSetUp.setCurrentItem(1000);

        //페이지끼리 간격
        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        // 페이지 보이는 정도
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset);

        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
            }
        });
        viewPageSetUp.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float offset = position * - (2 * pageOffset + pageMargin);
                if(-1 > position) {
                    page.setTranslationX(-offset);
                } else if(1 >= position) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(offset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0f);
                    page.setTranslationX(offset);
                }
            }
        });
    }



        @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
