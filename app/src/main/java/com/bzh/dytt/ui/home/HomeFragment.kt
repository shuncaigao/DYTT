package com.bzh.dytt.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bzh.dytt.R
import com.bzh.dytt.base.BaseFragment
import com.bzh.dytt.di.Injectable
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var homePagerAdapter: HomePagerAdapter

    private var tabObserver: Observer<List<HomeViewModel.HomeMovieType>> = Observer { tabs ->
        if (tabs != null) {
            homePagerAdapter.data.clear()
            for (tab in tabs) {
                homePagerAdapter.data.add(tab)
            }
            homePagerAdapter.notifyDataSetChanged()
        }
    }

    override fun doCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun doViewCreated(view: View, savedInstanceState: Bundle?) {
        super.doViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        lifecycle.addObserver(homeViewModel)
        homeViewModel.tabLiveData.observe(this, tabObserver)
        home_tab_layout.setupWithViewPager(home_view_pager)
        homePagerAdapter = HomePagerAdapter(resources, fragmentManager)
        home_view_pager.adapter = homePagerAdapter
    }

    override fun doDestroyView() {
        homeViewModel.tabLiveData.removeObserver(tabObserver)
        lifecycle.removeObserver(homeViewModel)
        super.doDestroyView()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    class HomePagerAdapter(private val resources: Resources, private val fragmentManager: FragmentManager?) : FragmentPagerAdapter(fragmentManager) {

        val data = arrayListOf<HomeViewModel.HomeMovieType>()

        override fun getPageTitle(position: Int): String = when (data[position]) {
            HomeViewModel.HomeMovieType.MOVIE_ZUIXIN_FILM -> {
                resources.getString(R.string.home_tab_item_zuixin_film)
            }
            HomeViewModel.HomeMovieType.MOVIE_ZONGHE_FILM -> {
                resources.getString(R.string.home_tab_item_zonghe_film)
            }
            HomeViewModel.HomeMovieType.MOVIE_HUAYU_FILM -> {
                resources.getString(R.string.home_tab_item_huayu_film)
            }
            HomeViewModel.HomeMovieType.MOIVE_OUMEI_FILM -> {
                resources.getString(R.string.home_tab_item_oumei_film)
            }
            HomeViewModel.HomeMovieType.MOIVE_RIHAN_FILM -> {
                resources.getString(R.string.home_tab_item_rihan_film)
            }
            HomeViewModel.HomeMovieType.MOVIE_HUAYU_TV -> {
                resources.getString(R.string.home_tab_item_huayu_tv)
            }
            HomeViewModel.HomeMovieType.MOVIE_OUTMEI_TV -> {
                resources.getString(R.string.home_tab_item_oumei_tv)
            }
            HomeViewModel.HomeMovieType.MOVIE_RIHAN_TV -> {
                resources.getString(R.string.home_tab_item_rihan_tv)
            }
            HomeViewModel.HomeMovieType.MOVIE_ZENGYIV -> {
                resources.getString(R.string.home_tab_item_zongyi)
            }
            else -> {
                resources.getString(R.string.home_tab_item_zuixin_film)
            }
        }

        override fun getItem(position: Int): Fragment {
            return HomeListFragment.newInstnace(data[position])
        }

        override fun getCount() = data.size
    }
}