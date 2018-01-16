package com.github.kirillf.materialdemo.tools;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Copyrigh Mail.ru Games (c) 2015
 * Created by y.bereza.
 */

public class HidingFABBehavior extends FloatingActionButton.Behavior {
    public HidingFABBehavior(Context context, AttributeSet attrs) {

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild, View target,
                                       int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout,
                               final FloatingActionButton child,
                               final View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.setVisibility(View.INVISIBLE);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.setVisibility(View.VISIBLE);
        }
    }
}
