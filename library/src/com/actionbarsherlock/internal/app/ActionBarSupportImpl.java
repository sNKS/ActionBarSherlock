/*
 * Copyright (C) 2011 Jake Wharton <jakewharton@gmail.com>
 * Copyright (C) 2010 Johan Nilsson <http://markupartist.com>
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

package com.actionbarsherlock.internal.app;

import java.util.ArrayList;
import java.util.List;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ActionMode;
import android.support.v4.view.Menu;
import android.view.View;
import android.widget.SpinnerAdapter;
import com.actionbarsherlock.R;
import com.actionbarsherlock.internal.widget.ActionBarView;

public final class ActionBarSupportImpl extends ActionBar {
    /** Action bar view. */
    private ActionBarView mActionBar;

    /** List of listeners to the menu visibility. */
    private final List<OnMenuVisibilityListener> mMenuListeners = new ArrayList<OnMenuVisibilityListener>();

    /** Whether display of the indeterminate progress is allowed. */
    private boolean mHasIndeterminateProgress = false;



    public ActionBarSupportImpl(FragmentActivity activity) {
        super(activity);
    }


    // ------------------------------------------------------------------------
    // ACTION BAR SHERLOCK SUPPORT
    // ------------------------------------------------------------------------

    @Override
    protected ActionBar getPublicInstance() {
        return (mActionBar != null) ? this : null;
    }

    public void init() {
        mActionBar = (ActionBarView)getActivity().findViewById(R.id.action_bar);

        if (mActionBar == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with a screen_*.xml layout");
        }

        final PackageManager pm = getActivity().getPackageManager();
        final ApplicationInfo appInfo = getActivity().getApplicationInfo();
        ActivityInfo actInfo = null;
        try {
            actInfo = pm.getActivityInfo(getActivity().getComponentName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {}


        if (mActionBar.getTitle() == null) {
            if ((actInfo != null) && (actInfo.labelRes != 0)) {
                //Load label string resource from the activity entry
                mActionBar.setTitle(actInfo.labelRes);
            } else {
                //No activity label string resource and none in theme
                mActionBar.setTitle(actInfo.loadLabel(pm));
            }
        }

        final ActionBarView.HomeItem homeItem = mActionBar.getHomeItem();
        if (homeItem.getIcon() == null) {
            if ((actInfo != null) && (actInfo.icon != 0)) {
                //Load the icon from the activity entry
                homeItem.setIcon(actInfo.icon);
            } else {
                //No activity icon and none in theme
                homeItem.setIcon(pm.getApplicationIcon(appInfo));
            }
        }

        //LOGO LOADING DOES NOT WORK
        //SEE: http://stackoverflow.com/questions/6105504/load-activity-and-or-application-logo-programmatically-from-manifest
        //SEE: https://groups.google.com/forum/#!topic/android-developers/UFR4l0ZwJWc

        if (mHasIndeterminateProgress) {
            mActionBar.setProgressBarIndeterminateVisibility(true);
        }
    }

    public void setMenu(Menu menu) {
        mActionBar.setMenu(menu);
    }

    public void onMenuVisibilityChanged(boolean isVisible) {
        //Marshal to all listeners
        for (OnMenuVisibilityListener listener : mMenuListeners) {
            listener.onMenuVisibilityChanged(isVisible);
        }
    }

    public void setWindowActionBarItemTextEnabled(boolean enabled) {
        //TODO pass to menu view;
    }

    public void setWindowIndeterminateProgressEnabled(boolean enabled) {
        if (mActionBar == null) {
            mHasIndeterminateProgress = enabled;
        }
    }

    public void setProgressBarIndeterminateVisibility(boolean visible) {
        if (mHasIndeterminateProgress && (mActionBar != null)) {
            mActionBar.setProgressBarIndeterminateVisibility(visible);
        }
    }

    // ------------------------------------------------------------------------
    // ACTION MODE METHODS
    // ------------------------------------------------------------------------

    @Override
    protected ActionMode startActionMode(ActionMode.Callback callback) {
        throw new RuntimeException("Not implemented.");
    }

    // ------------------------------------------------------------------------
    // ACTION BAR METHODS
    // ------------------------------------------------------------------------

    @Override
    public void addOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
        if (!mMenuListeners.contains(listener)) {
            mMenuListeners.add(listener);
        }
    }

    @Override
    public void addTab(Tab tab) {
        mActionBar.addTab(tab);
    }

    @Override
    public void addTab(Tab tab, boolean setSelected) {
        mActionBar.addTab(tab, setSelected);
    }

    @Override
    public void addTab(Tab tab, int position) {
        mActionBar.addTab(tab, position);
    }

    @Override
    public void addTab(ActionBar.Tab tab, int position, boolean setSelected) {
        mActionBar.addTab(tab, position, setSelected);
    }

    @Override
    public View getCustomView() {
        return mActionBar.getCustomView();
    }

    @Override
    public int getDisplayOptions() {
        return mActionBar.getDisplayOptions();
    }

    @Override
    public int getHeight() {
        return mActionBar.getHeight();
    }

    @Override
    public int getNavigationItemCount() {
        return mActionBar.getNavigationItemCount();
    }

    @Override
    public int getNavigationMode() {
        return mActionBar.getNavigationMode();
    }

    @Override
    public int getSelectedNavigationIndex() {
        return mActionBar.getSelectedNavigationIndex();
    }

    @Override
    public ActionBar.Tab getSelectedTab() {
        return mActionBar.getSelectedTab();
    }

    @Override
    public CharSequence getSubtitle() {
        return mActionBar.getSubtitle();
    }

    @Override
    public ActionBar.Tab getTabAt(int index) {
        return mActionBar.getTabAt(index);
    }

    @Override
    public int getTabCount() {
        return mActionBar.getTabCount();
    }

    @Override
    public CharSequence getTitle() {
        return mActionBar.getTitle();
    }

    @Override
    public void hide() {
        mActionBar.hide();
    }

    @Override
    public boolean isShowing() {
        return mActionBar.isShowing();
    }

    @Override
    public ActionBar.Tab newTab() {
        return mActionBar.newTab();
    }

    @Override
    public void removeAllTabs() {
        mActionBar.removeAllTabs();
    }

    @Override
    public void removeOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
        mMenuListeners.remove(listener);
    }

    @Override
    public void removeTab(ActionBar.Tab tab) {
        mActionBar.removeTab(tab);
    }

    @Override
    public void removeTabAt(int position) {
        mActionBar.removeTabAt(position);
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        mActionBar.setBackgroundDrawable(d);
    }

    @Override
    public void setCustomView(int resId) {
        mActionBar.setCustomView(resId);
    }

    @Override
    public void setCustomView(View view) {
        mActionBar.setCustomView(view);
    }

    @Override
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        mActionBar.setCustomView(view, layoutParams);
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    @Override
    public void setDisplayOptions(int options, int mask) {
        mActionBar.setDisplayOptions(options, mask);
    }

    @Override
    public void setDisplayOptions(int options) {
        mActionBar.setDisplayOptions(options);
    }

    @Override
    public void setDisplayShowCustomEnabled(boolean showCustom) {
        mActionBar.setDisplayShowCustomEnabled(showCustom);
    }

    @Override
    public void setDisplayShowHomeEnabled(boolean showHome) {
        mActionBar.setDisplayShowHomeEnabled(showHome);
    }

    @Override
    public void setDisplayShowTitleEnabled(boolean showTitle) {
        mActionBar.setDisplayShowTitleEnabled(showTitle);
    }

    @Override
    public void setDisplayUseLogoEnabled(boolean useLogo) {
        mActionBar.setDisplayUseLogoEnabled(useLogo);
    }

    @Override
    public void setListNavigationCallbacks(SpinnerAdapter adapter, ActionBar.OnNavigationListener callback) {
        mActionBar.setListNavigationCallbacks(adapter, callback);
    }

    @Override
    public void setNavigationMode(int mode) {
        mActionBar.setNavigationMode(mode);
    }

    @Override
    public void setSelectedNavigationItem(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void selectTab(ActionBar.Tab tab) {
        mActionBar.selectTab(tab);
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        mActionBar.setSubtitle(subtitle);
    }

    @Override
    public void setSubtitle(int resId) {
        mActionBar.setSubtitle(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mActionBar.setTitle(title);
    }
    @Override
    public void setTitle(int resId) {
        mActionBar.setTitle(resId);
    }

    @Override
    public void show() {
        mActionBar.show();
    }
}
