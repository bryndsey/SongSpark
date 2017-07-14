package com.bryndsey.songspark.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metova.slim.Slim;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = Slim.createLayout(getContext(), this, container);
		ButterKnife.bind(this, view);
		return view;
	}

}
