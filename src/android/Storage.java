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
                
                String response = "{";
                int i;
                
                response += "\"alarms\": [";
                for(i = 0; i < alarms.size(); i++){
                    response += convertAlarmToJSONObjectString(alarms.get(i));
                    if(i != alarms.size() - 1){
                        response += ",";
                    }
                }
                response += "]";
                
                callbackContext.success(response);
                
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
    
    public String convertAlarmToJSONObjectString(Alarm alarm) {
        return "{" +
            (alarm.drug != null ? ("\"drug\": " + convertDrugToJSONObjectString(alarm.drug) + ",") : "") + 
            (alarm.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(alarm.trip) + ",") : "")  +
            (alarm.disease != null ? ("\"disease\": " + convertDiseaseToJSONObjectString(alarm.disease) + ",") : "")  +
            (alarm.profile != null ? ("\"profile\": " + convertProfileToJSONObjectString(alarm.profile) + ",") : "")  +
            "\"isActive\": " + alarm.isActive + "," +
            "\"time\": " + alarm.time + "," +
            "\"title\": \"" + alarm.title + "\"," +
            "\"interval\": " + alarm.interval + "," +
            "\"type\": \"" + alarm.type + "\"," +
            "\"repeating\": " + alarm.repeating + "}";
    }
    
    public String convertDestinationToJSONObjectString(Destination destination) {
        return "{" +
            (destination.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(destination.trip) + ",") : "")  +
            "\"letter\": \"" + destination.letter + "\"," +
            "\"nameFriendly\": \"" + destination.nameFriendly + "\"," +
            "\"nameList\": \"" + destination.nameList + "\"," +
            "\"nameYellowFeverMalariaTable\": \"" + destination.nameYellowFeverMalariaTable + "\"," +
            "\"nameOfficial\": \"" + destination.nameOfficial + "\"," +
            "\"namePlugin\": \"" + destination.namePlugin + "\"," +
            "\"webLink\": \"" + destination.webLink + "\"," +
            "\"isoA2\": \"" + destination.isoA2 + "\"," +
            "\"isAlias\": " + destination.isAlias + "," +
            "\"parentName\": \"" + destination.parentName + "\"," +
            "\"flagUrlSmall\": \"" + destination.flagUrlSmall + "\"," +
            "\"emergencyNumbers\": \"" + destination.emergencyNumbers + "\"}";
    }
    
    public String convertDiseaseToJSONObjectString(Disease disease) {
        return "{" +
            (disease.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(disease.trip) + ",") : "")  +
            (disease.profile != null ? ("\"profile\": " + convertProfileToJSONObjectString(disease.profile) + ",") : "")  +
            "\"diseaseListName\": \"" + disease.diseaseListName + "\"," +
            "\"friendlyName\": \"" + disease.friendlyName + "\"," +
            "\"groupText\": \"" + disease.groupText + "\"," +
            "\"findOutWhyHtml\": \"" + disease.findOutWhyHtml + "\"," +
            "\"diseasePageUrl\": \"" + disease.diseasePageUrl + "\"," +
            "\"drugType\": \"" + disease.drugType + "\"}";
    }
    
    public String convertDocumentToJSONObjectString(Document document) {
        return "{" +
            (document.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(document.trip) + ",") : "")  +
            (document.profile != null ? ("\"profile\": " + convertProfileToJSONObjectString(document.profile) + ",") : "")  +
            "\"name\": \"" + document.name + "\"," +
            "\"category\": \"" + document.category + "\"," +
            "\"localImagePath\": \"" + document.localImagePath + "\"," +
            "\"remoteImagePath\": \"" + document.remoteImagePath + "\"," +
            "\"localThumbPath\": \"" + document.localThumbPath + "\"," +
            "\"remoteThumbPath\": \"" + document.remoteThumbPath + "\"}";
    }
    
    public String convertDrugToJSONObjectString(Drug drug) {
        return "{" +
            (drug.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(drug.trip) + ",") : "")  +
            (drug.disease != null ? ("\"disease\": " + convertDiseaseToJSONObjectString(drug.disease) + ",") : "")  +
            (drug.profile != null ? ("\"profile\": " + convertProfileToJSONObjectString(drug.profile) + ",") : "")  +
            "\"displayName\": \"" + drug.displayName + "\"," +
            "\"friendlyName\": \"" + drug.friendlyName + "\"," +
            "\"duration\": \"" + drug.duration + "\"," +
            "\"alertText\": \"" + drug.alertText + "\"," +
            "\"tsUpdated\": \"" + drug.tsUpdated + "\"," +
            "\"reminderInstructions\": \"" + drug.reminderInstructions + "\"," +
            "\"diseaseFriendlyName\": \"" + drug.diseaseFriendlyName + "\"," +
            "\"drugType\": \"" + drug.drugType + "\"," +
            "\"diseaseNameList\": \"" + drug.diseaseNameList + "\"}";
    }
    
    public String convertPackingGroupToJSONObjectString(PackingGroup packingGroup) {
        return "{" +
            (packingGroup.packingSuperGroup != null ? ("\"packingSuperGroup\": " + convertPackingSuperGroupToJSONObjectString(packingGroup.packingSuperGroup) + ",") : "")  +
            (packingGroup.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(packingGroup.trip) + ",") : "")  +
            "\"groupId\": " + packingGroup.groupId + "," +
            "\"sortOrder\": " + packingGroup.sortOrder + "," +
            "\"groupText\": \"" + packingGroup.groupText + "\"," +
            "\"isTodo\": " + packingGroup.isTodo + "}";
    }
    
    public String convertPackingItemToJSONObjectString(PackingItem packingItem) {
        return "{" +
            (packingItem.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(packingItem.trip) + ",") : "")  +
            (packingItem.packingGroup != null ? ("\"packingGroup\": " + convertPackingGroupToJSONObjectString(packingItem.packingGroup) + ",") : "")  +
            (packingItem.packingSuperGroup != null ? ("\"packingSuperGroup\": " + convertPackingSuperGroupToJSONObjectString(packingItem.packingSuperGroup) + ",") : "")  +
            "\"itemId\": " + packingItem.itemId + "," +
            "\"displayName\": \"" + packingItem.displayName + "\"," +
            "\"descriptionContent\": \"" + packingItem.descriptionContent + "\"," +
            "\"appSpecificContent\": \"" + packingItem.appSpecificContent + "\"," +
            "\"sortOrder\": " + packingItem.sortOrder + "," +
            "\"isTodo\": " + packingItem.isTodo + "," +
            "\"isAlarmOn\": " + packingItem.isAlarmOn + "," +
            "\"packingSupperGroupOrder\": " + packingItem.packingSupperGroupOrder + "}";
    }
    
    public String convertPackingSuperGroupToJSONObjectString(PackingSuperGroup packingSuperGroup) {
        return "{" +
            (packingSuperGroup.trip != null ? ("\"trip\": " + convertTripToJSONObjectString(packingSuperGroup.trip) + ",") : "")  +
            "\"superGroupId\": " + packingSuperGroup.superGroupId + "," +
            "\"sortOrder\": " + packingSuperGroup.sortOrder + "," +
            "\"superGroupText\": \"" + packingSuperGroup.superGroupText + "\"," +
            "\"isTodo\": " + packingSuperGroup.isTodo + "}";
    }
    
    public String convertProfileToJSONObjectString(Profile profile) {
        return "{" +
            "\"firstName\": \"" + profile.firstName + "\"," +
            "\"lastName\": \"" + profile.lastName + "\"," +
            "\"lastUpdatedDestinations\": " + profile.lastUpdatedDestinations + "," +
            "\"homeScreen\": " + profile.homeScreen + "," +
            "\"remindersToDo\": " + profile.remindersToDo + "," +
            "\"remindersVaccine\": " + profile.remindersVaccine + "," +
            "\"remindersMedicine\": " + profile.remindersMedicine + "," +
            "\"lastUpdatedDisease\": " + profile.lastUpdatedDisease + "}";
    }
    
    public String convertTripToJSONObjectString(Trip trip) {
        return "{" +
            (trip.profile != null ? ("\"profile\": " + convertProfileToJSONObjectString(trip.profile) + ",") : "")  +
            "\"name\": \"" + trip.name + "\"," +
            "\"endTime\": " + trip.endTime + "," +
            "\"startTime\": " + trip.startTime + "," +
            "\"flag\": \"" + trip.flag + "\"}";
    }
}