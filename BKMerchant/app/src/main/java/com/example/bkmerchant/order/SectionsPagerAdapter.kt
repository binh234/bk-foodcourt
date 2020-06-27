package com.example.bkmerchant.order

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bkmerchant.R
import com.example.bkmerchant.order.orderList.HistoryOrderFragment
import com.example.bkmerchant.order.orderList.OngoingOrderFragment
import java.lang.IndexOutOfBoundsException

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fragment: Fragment, val storeId: String) : FragmentStateAdapter(fragment){

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        0 to { OngoingOrderFragment(storeId) },
        1 to { HistoryOrderFragment(storeId) }
    )

    override fun getItemCount(): Int {
        return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

//    override fun getItem(position: Int): Fragment {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1)
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return context.resources.getString(TAB_TITLES[position])
//    }
//
//    override fun getCount(): Int {
//        // Show 2 total pages.
//        return 2
//    }
}