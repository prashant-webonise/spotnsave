/*
 * Copyright (C) 2012 Capricorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.capricorn;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * A custom view that looks like the menu in <a href="https://path.com">Path
 * 2.0</a> (for iOS).
 *
 * @author Capricorn
 *
 */
public class ArcMenu extends RelativeLayout {
	private ArcLayout mArcLayout;
	private ArrayList<View> childItemList;

	//    private ImageView mHintView;

	public ArcMenu(Context context) {
		super(context);
		init(context);
	}

	public ArcMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		applyAttrs(attrs);
	}

	private void init(Context context) {
		Log.i(VIEW_LOG_TAG, "####### Arc Menu init");
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.arc_menu, this);

		mArcLayout = (ArcLayout) findViewById(R.id.item_layout);
		childItemList = new ArrayList<View>();	
		final ViewGroup controlLayout = (ViewGroup) findViewById(R.id.control_layout);
		controlLayout.setClickable(true);
		controlLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//                    mHintView.startAnimation(createHintSwitchAnimation(mArcLayout.isExpanded()));
					//                    mArcLayout.switchState(false);
				}

				return false;
			}
		});

		//        mHintView = (ImageView) findViewById(R.id.control_hint);
	}

	private void applyAttrs(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);

			float fromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees, ArcLayout.DEFAULT_FROM_DEGREES);
			float toDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees, ArcLayout.DEFAULT_TO_DEGREES);
			mArcLayout.setArc(fromDegrees, toDegrees);

			int defaultChildSize = mArcLayout.getChildSize();
			int newChildSize = a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, defaultChildSize);
			mArcLayout.setChildSize(newChildSize);

			a.recycle();
		}
	}

	public void addItem(View item, OnClickListener listener) {
		Log.i(VIEW_LOG_TAG, "#######Adding Item");
		mArcLayout.addView(item);
		childItemList.add(item);
		item.setOnClickListener(getItemClickListener(listener));
	}

	private OnClickListener getItemClickListener(final OnClickListener listener) {
		return new OnClickListener() {

			@Override
			public void onClick(final View viewClicked) {
				//                Animation animation = bindItemAnimation(viewClicked, true, 400);
				//                animation.setAnimationListener(new AnimationListener() {
				//
				//                    @Override
				//                    public void onAnimationStart(Animation animation) {
				//
				//                    }
				//
				//                    @Override
				//                    public void onAnimationRepeat(Animation animation) {
				//
				//                    }
				//
				//                    @Override
				//                    public void onAnimationEnd(Animation animation) {
				//                        postDelayed(new Runnable() {
				//
				//                            @Override
				//                            public void run() {
				//                                itemDidDisappear();
				//                            }
				//                        }, 0);
				//                    }
				//                });

				final int itemCount = mArcLayout.getChildCount();
				for (int i = 0; i < itemCount; i++) {
					View item = mArcLayout.getChildAt(i);
					if (viewClicked != item) {
						//                        bindItemAnimation(item, false, 300);
					}
				}

				mArcLayout.invalidate();
				//                mHintView.startAnimation(createHintSwitchAnimation(true));

				if (listener != null) {
					listener.onClick(viewClicked);
				}
			}
		};
	}

	//    private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
	//        Animation animation = createItemDisapperAnimation(duration, isClicked);
	//        child.setAnimation(animation);
	//
	//        return animation;
	//    }

	private void itemDidDisappear() {
		final int itemCount = mArcLayout.getChildCount();
		for (int i = 0; i < itemCount; i++) {
			View item = mArcLayout.getChildAt(i);
			item.clearAnimation();
		}

		mArcLayout.switchState(false);
	}

	public void removeAllChildItems(){
		mArcLayout.removeAllViews();
	}
}
