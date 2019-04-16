package org.apache.cordova;

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
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Arrays;

public class Storage extends CordovaPlugin {
    
   @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getPreviousStorage")) {
            String message = args.getString(0);
            callbackContext.success("Hello Plugin! Here's your message champ: " + message);
            return true;
        }
        return false;
    }
}
