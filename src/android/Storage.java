package gov.cdc.oid.ncezid.travwell;

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
import java.util.ArrayList;
import java.util.List;

import com.activeandroid.query.Select;
import com.activeandroid.ActiveAndroid;

import gov.cdc.oid.ncezid.travwell.model.*;

public class Storage extends CordovaPlugin {

    @Override public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getPreviousStorage")) { 
            try {
                ActiveAndroid.initialize(this.cordova.getActivity().getApplicationContext());
                
                List<Alarm> alarms = new Select().from(Alarm.class).execute();
                List<Destination> destinations = new Select().from(Destination.class).execute();
                List<Disease> diseases = new Select().from(Disease.class).execute();
                List<Document> documents = new Select().from(Document.class).execute();
                List<Drug> drugs = new Select().from(Drug.class).execute();
                List<PackingGroup> packingGroups = new Select().from(PackingGroup.class).execute();
                List<PackingItem> packingItems = new Select().from(PackingItem.class).execute();
                List<PackingSuperGroup> packingSuperGroups = new Select().from(PackingSuperGroup.class).execute();
                List<Profile> profiles = new Select().from(Profile.class).execute();
                List<Trip> trips = new Select().from(Trip.class).execute();
                
                callbackContext.success(
                    alarms.toString() + "\n" + 
                    destinations.toString() + "\n" + 
                    diseases.toString() + "\n" + 
                    documents.toString() + "\n" + 
                    drugs.toString() + "\n" + 
                    packingGroups.toString() + "\n" + 
                    packingItems.toString() + "\n" + 
                    packingSuperGroups.toString() + "\n" + 
                    profiles.toString() + "\n" + 
                    trips.toString() + "\n"
                );
                
                ActiveAndroid.dispose();
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                callbackContext.error("Exception thrown " + e.getMessage());
                return false;
            }
        } else { 
            callbackContext.error("Action not recognized");
            return false; 
        }
    } 
}