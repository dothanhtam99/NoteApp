package com.tamdt.mynotes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tamdt.mynotes.fragment.HomeFragment
import com.tamdt.mynotes.fragment.TaskNoteFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            HomeFragment()
        else TaskNoteFragment()
    }
}