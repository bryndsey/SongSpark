package com.bryndsey.songspark.ui.songproperties.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bryndsey.songspark.R;
import com.github.zagum.switchicon.SwitchIconView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongPropertyWidget extends LinearLayout implements AdapterView.OnItemSelectedListener {

	private Integer maximumWidth;

	@BindView(R.id.property_name)
	TextView propertyName;

	@BindView(R.id.property_spinner)
	Spinner propertySpinner;

	@BindView(R.id.property_randomize_toggle)
	SwitchIconView randomizeToggle;

	private SongPropertyInteractionListener listener;

	public SongPropertyWidget(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SongPropertyWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		View view = inflate(context, R.layout.widget_song_property, this);
		ButterKnife.bind(view);

		if (attrs != null) {
			TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SongPropertyWidget);

			if (attributes != null) {
				propertyName.setText(attributes.getText(R.styleable.SongPropertyWidget_propertyName));

				int maxWidth = attributes.getDimensionPixelSize(R.styleable.SongPropertyWidget_maxWidth, 0);
				if (maxWidth > 0) {
					maximumWidth = maxWidth;
				}

				attributes.recycle();
			}
		}

		propertySpinner.setOnItemSelectedListener(this);

		setOrientation(VERTICAL);
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
