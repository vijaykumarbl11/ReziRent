package com.resieasy.rezirent.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.resieasy.rezirent.Fragment.ShowHostelFragment;
import com.resieasy.rezirent.Fragment.ShowRentFragment;
import com.resieasy.rezirent.Fragment.ShowSellFragment;
import com.resieasy.rezirent.Fragment.ShowlikedFragment;

import java.util.ArrayList;

public class AdapterViewPager extends FragmentStateAdapter {
    public AdapterViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //ArrayList<Fragment> arr;

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:return new ShowRentFragment();
            case 1:return new ShowSellFragment();
            case 2:return new ShowHostelFragment();
            case 3:return new ShowlikedFragment();
            default: return new ShowRentFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
