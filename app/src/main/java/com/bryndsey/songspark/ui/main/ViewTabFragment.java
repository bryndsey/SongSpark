package com.bryndsey.songspark.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.bryndsey.songspark.ui.chordview.ChordFragment;
import com.bryndsey.songspark.ui.songproperties.SongPropertiesFragment;
import com.metova.slim.annotation.Layout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;

@Layout(R.layout.fragment_view_tab)
public class ViewTabFragment extends BaseFragment {

	@BindView(R.id.tabs)
	SmartTabLayout tabView;

	@BindView(R.id.view_pager)
	ViewPager viewPager;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
				getActivity().getSupportFragmentManager(),
				FragmentPagerItems.with(getContext())
						.add(R.string.properties_tab_title, SongPropertiesFragment.class)
						.add(R.string.chords_tab_title, ChordFragment.class)
						.create()
		);

		viewPager.setAdapter(adapter);
		tabView.setViewPager(viewPager);
	}
}
