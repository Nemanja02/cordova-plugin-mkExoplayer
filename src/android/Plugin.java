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

import android.net.*;
import org.apache.cordova.*;
import org.json.*;
import android.view.ViewGroup;

public class Plugin extends CordovaPlugin {
    private mkPlayer player;

    @Override
    public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext) throws JSONException {
        try {
            final Plugin self = this;
            if (action.equals("show")) {
                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (self.player != null) {
                            self.player.close();
                        }
                        JSONObject params = data.optJSONObject(0);
                        self.player = new mkPlayer(new Configuration(params), cordova.getActivity(), callbackContext, webView);
                        self.player.createPlayer(webView);
                        
                        
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("setStream")) {
                if (self.player == null) {
                    return false;
                }
                final String url = data.optString(0, null);
                final JSONObject controller = data.optJSONObject(1);
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.setStream(Uri.parse(url), controller);
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, new JSONObject(), true);
                    }
                });
                return true;
            }
            else if (action.equals("playPause")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.playPause();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });

                return true;
            }
            else if (action.equals("playNext")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.play_next();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("playPrevious")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.play_prev();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("stop")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.stop();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });

                return true;
            }
            else if (action.equals("seekTo")) {
                if (self.player == null) {
                    return false;
                }
                final long seekTo = data.optLong(0, 0);
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject payload = self.player.seekTo(seekTo);
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, payload, true);
                    }
                });
                return true;
            }
            else if (action.equals("seekBy")) {
                if (self.player == null) {
                    return false;
                }
                final long seekBy = data.optLong(0, 0);
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject payload = self.player.seekBy(seekBy);
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, payload, true);
                    }
                });
                return true;
            }
            else if (action.equals("getState")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getThreadPool().execute(new Runnable() {
                    public void run() {
                        JSONObject response = self.player.getPlayerState();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, response, false);
                    }
                });
                return true;
            }
            else if (action.equals("showController")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.showController();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("hideController")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.hideController();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("getSubtitles")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.getSubtitles();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("getSubtitlesList")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject obj = self.player.getSubtitlesList();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, obj, false);
                    }
                });
                return true;
            }
            else if (action.equals("getAudiosList")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject obj = self.player.getAudiosList();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, obj, false);
                    }
                });
                return true;
            }
            else if (action.equals("getAudios")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.getAudios();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("showMenu")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.showMenu();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("getVideos")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.getVideos();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("getPlaylist")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.getPlaylist();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("showToast")) {
                if (self.player == null) {
                    return false;
                }
                final String msg = data.optString(0, null);
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.showToast(msg);
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("setController")) {
                if (self.player == null) {
                    return false;
                }
                final JSONObject controller = data.optJSONObject(0);
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.setController(controller);
                        new CallbackResponse(callbackContext).send(PluginResult.Status.NO_RESULT, true);
                    }
                });
                return true;
            }
            else if (action.equals("close")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.close();
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, false);
                    }
                });
                return true;
            }
            else if (action.equals("resize")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.resizeDialog(data.optDouble(0),data.optDouble(1),data.optDouble(2),data.optDouble(3));
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, false);
                    }
                });
                return true;
            }
            else if (action.equals("selectSubtitle")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.selectSubtitle(data.optInt(0));
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, false);
                    }
                });
                return true;
            }
            else if (action.equals("selectAudio")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.selectAudio(data.optInt(0));
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, false);
                    }
                });
                return true;
            }
            else if (action.equals("getDuration")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.putOpt("data", self.player.getDuration());
                        } catch(JSONException e) {
                            
                        }
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, obj, false);
                    }
                });
                return true;
            }
            else if (action.equals("getCurrentPosition")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.putOpt("data", self.player.getCurrentPosition());
                        } catch(JSONException e) {

                        }
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, obj, false);
                    }
                });
                return true;
            }
            else if (action.equals("hidePlayer")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.hidePlayer(webView);
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, false);
                    }
                });
                return true;
            }
            else if (action.equals("showPlayer")) {
                if (self.player == null) {
                    return false;
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        self.player.showPlayer(webView);
                        new CallbackResponse(callbackContext).send(PluginResult.Status.OK, false);
                    }
                });
                return true;
            }
            else {
                new CallbackResponse(callbackContext).send(PluginResult.Status.INVALID_ACTION, false);
                return false;
            }
        }
        catch (Exception e) {
            new CallbackResponse(callbackContext).send(PluginResult.Status.JSON_EXCEPTION, false);
            return false;
        }
    }
}
