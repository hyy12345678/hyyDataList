package com.example.hyydatalist.view;

import com.example.hyydatalist.constants.HyyConstants;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class AutoExtViewGroup extends ViewGroup {

	public AutoExtViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AutoExtViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AutoExtViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private final static int VIEW_MARGIN = 10;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		Log.d(HyyConstants.HYY_TAG, "widthMeasureSpec=" + widthMeasureSpec
				+ "heightMeasureSpec=" + heightMeasureSpec);

		int stages = 1;
		int stageHeight = 0;
		int stageWidth = 0;

		int wholeWidth = MeasureSpec.getSize(widthMeasureSpec);

		for (int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);
			// measure
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			stageWidth += child.getMeasuredWidth();
			stageHeight = child.getMeasuredHeight();
			if (stageWidth >= wholeWidth) {
				stages++;
			}
		}

		int wholeHeight = stages * (stageHeight + VIEW_MARGIN)/2;

		// report this final dimension
		setMeasuredDimension(resolveSize(wholeWidth, widthMeasureSpec),
				resolveSize(wholeHeight, heightMeasureSpec));

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		final int count = getChildCount();
		int row = 0; // which row lay your view relative to parent
		int lengthX = l; // right position of child relative to parent
		int lengthY = t; // bottom position of child relative to parent

		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();

			lengthX += width + VIEW_MARGIN;
			lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height + t;

			// if it cant't draw in a same line ,skip it to next line
			if (lengthX > r) {
				lengthX = width + VIEW_MARGIN + l;
				row++;
				lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
						+ t;

			}

			child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
		}

	}

}
