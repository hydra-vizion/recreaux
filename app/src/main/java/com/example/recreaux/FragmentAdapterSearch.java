package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapterSearch extends FragmentStateAdapter {
    public FragmentAdapterSearch(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1:
                return new PeopleSearch();
            case 2:
                return new EventSearch();
            case 3:
                return new PlaceSearch();

        }
        return new TagSearch();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
