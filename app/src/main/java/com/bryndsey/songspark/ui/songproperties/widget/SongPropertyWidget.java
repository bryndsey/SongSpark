package com.bryndsey.songspark.ui.songproperties.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bryndsey.songspark.R;
import com.github.zagum.switchicon.SwitchIconView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongPropertyWidget extends LinearLayout implements AdapterView.OnItemSelectedListener {

	private Integer maximumWidth;

	@BindView(R.id.property_image)
	ImageView propertyImage;

	@BindView(R.id.property_spinner)
	Spinner propertySpinner;

	@BindView(R.id.property_randomize_toggle)
	SwitchIconView randomizeToggle;

	private SongPropertyInteractionListener listener;

	public SongPropertyWidget(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		View view = inflate(context, R.layout.widget_song_property, this);
		ButterKnife.bind(view);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SongPropertyWidget);

		Drawable image = attributes.getDrawable(R.styleable.SongPropertyWidget_propertySrc);
		propertyImage.setImageDrawable(image);

		int maxWidth = attributes.getDimensionPixelSize(R.styleable.SongPropertyWidget_maxWidth, 0);
		if (maxWidth > 0) {
			maximumWidth = maxWidth;
		}

		attributes.recycle();

		propertySpinner.setOnItemSelectedListener(this);

		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (maximumWidth != null) {
			int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
			if(maximumWidth < measuredWidth) {
				int measureMode = MeasureSpec.getMode(widthMeasureSpec);
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(maximumWidth, measureMode);
			}
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setSongPropertyInteractionListener(SongPropertyInteractionListener listener) {
		this.listener = listener;
	}

	public void setPropertyItems(List items) {
		ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.song_property_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		propertySpinner.setAdapter(adapter);
	}

	public void setPropertySelection(int position) {
		propertySpinner.setSelection(position);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (listener != null) {
			listener.onSelectionChanged(position);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

	@OnClick(R.id.property_randomize_toggle)
	public void onRandomizeToggleClicked() {
		randomizeToggle.switchState();
		if (listener != null) {
			listener.onRandomizeToggleChanged(randomizeToggle.isIconEnabled());
		}
	}

	public interface SongPropertyInteractionListener {

		void onSelectionChanged(int position);

		void onRandomizeToggleChanged(boolean value);
	}
}
