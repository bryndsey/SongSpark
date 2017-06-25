package com.bryndsey.songspark.ui.songproperties.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bryndsey.songspark.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class SongPropertyWidget extends LinearLayout implements AdapterView.OnItemSelectedListener {

	@BindView(R.id.property_image)
	ImageView propertyImage;

	@BindView(R.id.property_spinner)
	Spinner propertySpinner;

	private Consumer<Integer> selectedAction;

	public SongPropertyWidget(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		View view = inflate(context, R.layout.widget_song_property, this);
		ButterKnife.bind(view);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SongPropertyWidget);

		Drawable image = attributes.getDrawable(R.styleable.SongPropertyWidget_propertySrc);
		propertyImage.setImageDrawable(image);

		attributes.recycle();

		propertySpinner.setOnItemSelectedListener(this);

		setOrientation(HORIZONTAL);
	}

	public void setSongPropertySelectedAction(Consumer<Integer> action) {
		this.selectedAction = action;
	}

	public void setPropertyItems(List items) {
		ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		propertySpinner.setAdapter(adapter);
	}

	public void setPropertySelection(int position) {
		propertySpinner.setSelection(position);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (selectedAction != null) {
			try {
				selectedAction.accept(position);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
