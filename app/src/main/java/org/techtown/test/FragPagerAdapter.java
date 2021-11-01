package org.techtown.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragPagerAdapter extends FragmentStateAdapter { //뷰페이저2에서는 FragmentStateAdapter를 사용한다.
    // Real Fragment Total Count
    private final int mSetItemCount = 3; //프래그먼트 갯수 지정

    public FragPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int iViewIdx = getRealPosition(position);
        switch( iViewIdx ) {
            case 0    : { return new org.techtown.test.mFrag1(); }
            case 1    : { return new mFrag2(); }
            case 2    : { return new mFrag3(); }
            default   : { return new org.techtown.test.mFrag1(); } // 디폴트 페이지
        }

    }

    public int getRealPosition(int _iPosition){
        return _iPosition % mSetItemCount;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
