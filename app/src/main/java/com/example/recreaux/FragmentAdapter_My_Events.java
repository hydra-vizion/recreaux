package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter_My_Events extends FragmentStateAdapter {
    public FragmentAdapter_My_Events(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0){
            return new Fragment_Events();
        }

        else{
            return new Fragment_History();
        }


    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
