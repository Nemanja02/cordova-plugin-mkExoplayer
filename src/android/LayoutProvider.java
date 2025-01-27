/*
 The MIT License (MIT)

 Copyright (c) 2020 mkalyon

 Original repository https://github.com/frontyard/cordova-plugin-exoplayer

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.mkalyon.cordova.plugins.mkEXO;

import android.app.*;
import android.graphics.*;
import android.text.format.DateFormat;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.google.android.exoplayer2.ui.*;
import java.lang.String;
import java.util.Calendar;
import org.json.*;
import com.squareup.picasso.*;
import android.widget.FrameLayout.*;

public class LayoutProvider {
    private enum BUTTON { exo_prev, exo_rew, exo_play, exo_pause, exo_ffwd, exo_next }
    public static final String TAG = "ExoPlayerPlugin";
    public static final int timeZone=0;
    public static FrameLayout getMainLayout(Activity activity) {
        FrameLayout view = new FrameLayout(activity);
        view.setLayoutParams(new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT));
        view.setKeepScreenOn(true);
        return view;
    }

    public static SimpleExoPlayerView getExoPlayerView(Activity activity, Configuration config) {
        SimpleExoPlayerView view = new SimpleExoPlayerView(activity);

        // VELICINA PLAYERA
        view.setLayoutParams(new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT));
        if (config.isAspectRatioFillScreen()) {
            view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        }
        view.setFastForwardIncrementMs(config.getForwardTimeMs());
        view.setRewindIncrementMs(config.getRewindTimeMs());
        view.setShowMultiWindowTimeBar(true);
        view.setControllerHideOnTouch(true);
        view.setControllerShowTimeoutMs(config.getHideTimeout());
        setupController(view, activity, config.getController());

        return view;
    }

    public static SimpleExoPlayerView getExoPlayerViewResize(Activity activity, Configuration config, double width, double height) {
        SimpleExoPlayerView view = new SimpleExoPlayerView(activity);

        // VELICINA PLAYERA
        view.setLayoutParams(new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT));
        if (config.isAspectRatioFillScreen()) {
            view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        }
        view.setFastForwardIncrementMs(config.getForwardTimeMs());
        view.setRewindIncrementMs(config.getRewindTimeMs());
        view.setShowMultiWindowTimeBar(true);
        view.setControllerHideOnTouch(true);
        view.setControllerShowTimeoutMs(config.getHideTimeout());
        setupController(view, activity, config.getController());

        return view;
    }

    public static void setupController(SimpleExoPlayerView parentView, Activity activity, JSONObject controller) {
        if (null != controller) {
            parentView.setUseController(true);
            setupButtons(parentView, activity, controller);
            setupBar(parentView, activity, controller);
            setupBuffering(parentView, activity, controller);
        }
        else {
            parentView.setUseController(false);
        }
    }

    private static void setupButtons(SimpleExoPlayerView parentView, Activity activity, JSONObject controller) {
        String packageName = activity.getPackageName();
        String buttonsColor = controller.optString("buttonsColor");

        JSONObject buttonsConfig = controller.optJSONObject("controlIcons");
        if (null != buttonsConfig) {
            for (BUTTON b : BUTTON.values()) {
                String buttonName = b.name();
                ImageButton imageButton = (ImageButton) findView(parentView, activity, buttonName);
                if (null != imageButton) {
                    if (buttonsConfig.has(buttonName)) {
                        String buttonUrl = buttonsConfig.optString(buttonName);
                        if (null == buttonUrl || buttonUrl.equals("null")) { // Again, why is this a String "null"?
                            // Image is set to null, remove it from view.
                            imageButton.setVisibility(View.GONE);
                            ((ViewGroup) imageButton.getParent()).removeView(imageButton);
                        }
                        else {
                            // Loading from external source.
                           // Picasso.with(imageButton.getContext()).load(buttonUrl).into(imageButton);
                            Picasso.get().load(buttonUrl).into(imageButton);
                        }
                    }
                    else if (null != buttonsColor) {
                        // Using default and tinting.
                        imageButton.setColorFilter(Color.parseColor(buttonsColor));
                    }
                }
            }
        }
        else {
            LinearLayout exoButtons = (LinearLayout) findView(parentView, activity, "exo_buttons");
            exoButtons.setVisibility(View.GONE);
        }
    }

    private static void setupBar(SimpleExoPlayerView parentView, Activity activity, JSONObject controller) {
        String streamTitle = controller.optString("streamTitle", null);
		String streamEpg = controller.optString("streamEpg", null);
        String streamDescription = controller.optString("streamDescription", null);
        String streamImage = controller.optString("streamImage", null);
        String textColor = controller.optString("textColor");

        ImageView imageView = (ImageView) findView(parentView, activity, "exo_image");
        TextView titleView = (TextView) findView(parentView, activity, "exo_title");
		TextView epgView = (TextView) findView(parentView, activity, "exo_epg");
        TextView subtitleView = (TextView) findView(parentView, activity, "exo_subtitle");
        View timebarView = findView(parentView, activity, "exo_timebar");
        TextView positionView = (TextView) findView(timebarView, activity, "exo_position");
        TextView durationView = (TextView) findView(timebarView, activity, "exo_duration");
        TextView saat = (TextView) findView(parentView , activity, "clock");

        if (null != textColor) {
            int intTextColor = Color.parseColor(textColor);
            if (null != titleView) {
                titleView.setTextColor(intTextColor);
            }
            if (null != saat) {
                saat.setTextColor(intTextColor);
            }
			if (null != epgView) {
                epgView.setTextColor(intTextColor);
            }
            if (null != subtitleView) {
                subtitleView.setTextColor(intTextColor);
            }
            if (null != positionView) {
                positionView.setTextColor(intTextColor);
            }
            if (null != durationView) {
                durationView.setTextColor(intTextColor);
                Calendar cal = Calendar.getInstance();
                int minutes = cal.get(Calendar.MINUTE);

                if (DateFormat.is24HourFormat(activity )) {
                    int hours = cal.get(Calendar.HOUR_OF_DAY);
                    hours=hours + timeZone ;
                    saat.setText((hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes));
                }
                else {
                    int hours = cal.get(Calendar.HOUR);
                    hours=hours + timeZone ;
                    //saat.setText(hours + ":" + (minutes < 10 ? "0" + minutes : minutes) + " " + new DateFormatSymbols().getAmPmStrings()[cal.get(Calendar.AM_PM)]);
                    saat.setText(hours + ":" + (minutes < 10 ? "0" + minutes : minutes));
                }
            }
        }
        if(null != streamImage && streamImage !="") {
            Picasso.get().load(streamImage).into(imageView);
        }
        if(null != streamTitle && streamTitle !="") {
            titleView.setText(streamTitle);
        }
		if(null != streamEpg && streamEpg !="") {
            epgView.setText(streamEpg);
        }
        if (null != streamDescription && !streamDescription.equals("")) {
            subtitleView.setText(streamDescription);
        }
        else {
            subtitleView.setVisibility(View.GONE);
        }
        if (controller.optBoolean("hideProgress")) {
            timebarView.setVisibility(View.GONE);
        }
        else {
            if (controller.optBoolean("hidePosition") && null != positionView) {
                positionView.setVisibility(View.GONE);
                ((ViewGroup) positionView.getParent()).removeView(positionView);
            }
            if (controller.optBoolean("hideDuration") && null != durationView) {
                durationView.setVisibility(View.GONE);
                ((ViewGroup) durationView.getParent()).removeView(durationView);
            }
        }
    }

    private static void setupBuffering(SimpleExoPlayerView parentView, Activity activity, JSONObject controller) {
        String bufferingColor = controller.optString("bufferingColor");
        ProgressBar bufferingBar = (ProgressBar)findView(parentView, activity, "exo_buffering");
        if (null != bufferingBar && null != bufferingColor) {
            bufferingBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(bufferingColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    public static void setBufferingVisibility(SimpleExoPlayerView parentView, Activity activity, boolean visibile) {
        ProgressBar progressBar = (ProgressBar)findView(parentView, activity, "exo_buffering");
        if (null != progressBar) {
            progressBar.setVisibility(visibile ? View.VISIBLE : View.GONE);
        }
    }

    public static void setPlayerVisibility(SimpleExoPlayerView parentView, Activity activity, boolean visibile) {
            parentView.setVisibility(visibile ? View.VISIBLE : View.GONE);
    }

    private static View findView(View view, Activity activity, String name) {
        int viewId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
        return view.findViewById(viewId);
    }

    public static WindowManager.LayoutParams getDialogLayoutParams(Activity activity, Configuration config, Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        return lp;
    }

    public static FrameLayout.LayoutParams resizePlayer(Activity activity, Configuration config, Dialog dialog,double top, double left, double width, double height) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) width, (int) height, Gravity.TOP | Gravity.LEFT);
        lp.setMargins((int) left,(int) top, 0,0);
        return lp;
    }
    public static FrameLayout.LayoutParams getRect() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1920, 1080, Gravity.TOP | Gravity.LEFT);
        return lp;
    }
}
