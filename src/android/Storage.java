package org.apache.cordova.storage;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import android.util.Log;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Arrays;

import com.activeandroid.ActiveAndroid;

public class Storage extends CordovaPlugin {

    @Override public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            ActiveAndroid.beginTransaction();
            if (action.equals("getPreviousStorage")) { 
                String message = args.getString(0);
                callbackContext.success("Hello Plugin! Here's your message champ: " + message);
                return true;
            } else { 
                callbackContext.error("Action not recognized");
                return false; 
            }
        } catch(Exception e) {
            callbackContext.error("Exception thrown");
            return false;
        }
    } 
}