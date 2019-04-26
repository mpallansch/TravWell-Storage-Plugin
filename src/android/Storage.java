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
                response += "],";
                
                response += "\"destinations\": [";
                for(i = 0; i < destinations.size(); i++){
                    response += convertDestinationToJSONObjectString(destinations.get(i));
                    if(i != destinations.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"diseases\": [";
                for(i = 0; i < diseases.size(); i++){
                    response += convertDiseaseToJSONObjectString(diseases.get(i));
                    if(i != diseases.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"documents\": [";
                for(i = 0; i < documents.size(); i++){
                    response += convertDocumentToJSONObjectString(documents.get(i));
                    if(i != documents.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"drugs\": [";
                for(i = 0; i < drugs.size(); i++){
                    response += convertDrugToJSONObjectString(drugs.get(i));
                    if(i != drugs.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"packingGroups\": [";
                for(i = 0; i < packingGroups.size(); i++){
                    response += convertPackingGroupToJSONObjectString(packingGroups.get(i));
                    if(i != packingGroups.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"packingItems\": [";
                for(i = 0; i < packingItems.size(); i++){
                    response += convertPackingItemToJSONObjectString(packingItems.get(i));
                    if(i != packingItems.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"packingSuperGroups\": [";
                for(i = 0; i < packingSuperGroups.size(); i++){
                    response += convertPackingSuperGroupToJSONObjectString(packingSuperGroups.get(i));
                    if(i != packingSuperGroups.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"profiles\": [";
                for(i = 0; i < profiles.size(); i++){
                    response += convertProfileToJSONObjectString(profiles.get(i));
                    if(i != profiles.size() - 1){
                        response += ",";
                    }
                }
                response += "],";
                
                response += "\"trips\": [";
                for(i = 0; i < trips.size(); i++){
                    response += convertTripToJSONObjectString(trips.get(i));
                    if(i != trips.size() - 1){
                        response += ",";
                    }
                }
                response += "]";
                
                response += "}";
                
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
            "\"id\": " + ((alarm != null) ? String.valueOf(alarm.getId()) : "null") + "," +
            "\"drug\": " + ((alarm.drug != null) ? String.valueOf(alarm.drug.getId()) : "null") + "," +
            "\"trip\": " + ((alarm.trip != null) ? String.valueOf(alarm.trip.getId()) : "null") + "," +
            "\"disease\": " + ((alarm.disease != null) ? String.valueOf(alarm.disease.getId()) : "null") + "," +
            "\"profile\": " + ((alarm.profile != null) ? String.valueOf(alarm.profile.getId()) : "null") + "," +
            "\"isActive\": " + alarm.isActive + "," +
            "\"time\": " + alarm.time + "," +
            "\"title\": \"" + alarm.title + "\"," +
            "\"interval\": " + alarm.interval + "," +
            "\"type\": \"" + alarm.type + "\"," +
            "\"repeating\": " + alarm.repeating + "}";
    }
    
    public String convertDestinationToJSONObjectString(Destination destination) {
        return "{" +
            "\"id\": " + ((destination != null) ? String.valueOf(destination.getId()) : "null") + "," +
            "\"trip\": " + ((destination.trip != null) ? String.valueOf(destination.trip.getId()) : "null") + "," +
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
            "\"emergencyNumbers\": \"" + ((destination.emergencyNumbers != null) ? destination.emergencyNumbers.replace("\n", "").replace("\r", "").replaceAll("\t", "").replaceAll("\"", "\\\\\"")  : "") + "\"}";
    }
    
    public String convertDiseaseToJSONObjectString(Disease disease) {
        return "{" +
            "\"id\": " + ((disease != null) ? String.valueOf(disease.getId()) : "null") + "," +
            "\"trip\": " + ((disease.trip != null) ? String.valueOf(disease.trip.getId()) : "null") + "," +
            "\"profile\": " + ((disease.profile != null) ? String.valueOf(disease.profile.getId()) : "null") + "," +
            "\"diseaseListName\": \"" + disease.diseaseListName + "\"," +
            "\"friendlyName\": \"" + disease.friendlyName + "\"," +
            "\"groupText\": \"" + disease.groupText + "\"," +
            "\"findOutWhyHtml\": \"" + disease.findOutWhyHtml.replaceAll("\t", "").replaceAll("\"", "\\\\\"") + "\"," +
            "\"diseasePageUrl\": \"" + disease.diseasePageUrl + "\"," +
            "\"drugType\": \"" + disease.drugType + "\"}";
    }
    
    public String convertDocumentToJSONObjectString(Document document) {
        return "{" +
            "\"id\": " + ((document != null) ? String.valueOf(document.getId()) : "null") + "," +
            "\"trip\": " + ((document.trip != null) ? String.valueOf(document.trip.getId()) : "null") + "," +
            "\"profile\": " + ((document.profile != null) ? String.valueOf(document.profile.getId()) : "null") + "," +
            "\"name\": \"" + document.name + "\"," +
            "\"category\": \"" + document.category + "\"," +
            "\"localImagePath\": \"" + document.localImagePath + "\"," +
            "\"remoteImagePath\": \"" + document.remoteImagePath + "\"," +
            "\"localThumbPath\": \"" + document.localThumbPath + "\"," +
            "\"remoteThumbPath\": \"" + document.remoteThumbPath + "\"}";
    }
    
    public String convertDrugToJSONObjectString(Drug drug) {
        return "{" +
            "\"id\": " + ((drug != null) ? String.valueOf(drug.getId()) : "null") + "," +
            "\"trip\": " + ((drug.trip != null) ? String.valueOf(drug.trip.getId()) : "null") + "," +
            "\"disease\": " + ((drug.disease != null) ? String.valueOf(drug.disease.getId()) : "null") + "," +
            "\"profile\": " + ((drug.profile != null) ? String.valueOf(drug.profile.getId()) : "null") + "," +
            "\"displayName\": \"" + drug.displayName + "\"," +
            "\"friendlyName\": \"" + drug.friendlyName + "\"," +
            "\"duration\": \"" + drug.duration + "\"," +
            "\"alertText\": \"" + drug.alertText + "\"," +
            "\"tsUpdated\": \"" + drug.tsUpdated + "\"," +
            "\"timeStarted\": " + drug.timeStarted + "," +
            "\"isCompleted\": " + drug.isCompleted + "," +
            "\"isAlarmOn\": " + drug.isAlarmOn + "," +
            "\"notes\": \"" + drug.notes + "\"," +
            "\"reminderInstructions\": \"" + drug.reminderInstructions + "\"," +
            "\"diseaseFriendlyName\": \"" + drug.diseaseFriendlyName + "\"," +
            "\"drugType\": \"" + drug.drugType + "\"," +
            "\"diseaseNameList\": \"" + drug.diseaseNameList + "\"}";
    }
    
    public String convertPackingGroupToJSONObjectString(PackingGroup packingGroup) {
        return "{" +
            "\"id\": " + ((packingGroup != null) ? String.valueOf(packingGroup.getId()) : "null") + "," +
            "\"packingSuperGroup\": " + ((packingGroup.packingSuperGroup != null) ? String.valueOf(packingGroup.packingSuperGroup.getId()) : "null") + "," +
            "\"trip\": " + ((packingGroup.trip != null) ? String.valueOf(packingGroup.trip.getId()) : "null") + "," +
            "\"groupId\": " + packingGroup.groupId + "," +
            "\"sortOrder\": " + packingGroup.sortOrder + "," +
            "\"groupText\": \"" + packingGroup.groupText + "\"," +
            "\"isTodo\": " + packingGroup.isTodo + "}";
    }
    
    public String convertPackingItemToJSONObjectString(PackingItem packingItem) {
        return "{" +
            "\"id\": " + ((packingItem != null) ? String.valueOf(packingItem.getId()) : "null") + "," +
            "\"trip\": " + ((packingItem.trip != null) ? String.valueOf(packingItem.trip.getId()) : "null") + "," +
            "\"packingGroup\": " + ((packingItem.packingGroup != null) ? String.valueOf(packingItem.packingGroup.getId()) : "null") + "," +
            "\"packingSuperGroup\": " + ((packingItem.packingSuperGroup != null) ? String.valueOf(packingItem.packingSuperGroup.getId()) : "null") + "," +
            "\"itemId\": " + packingItem.itemId + "," +
            "\"displayName\": \"" + packingItem.displayName + "\"," +
            "\"descriptionContent\": \"" + ((packingItem.descriptionContent != null) ? packingItem.descriptionContent.replace("\n", "").replace("\r", "").replaceAll("\t", "").replaceAll("\"", "\\\\\"") : "") + "\"," +
            "\"appSpecificContent\": \"" + packingItem.appSpecificContent + "\"," +
            "\"sortOrder\": " + packingItem.sortOrder + "," +
            "\"isTodo\": " + packingItem.isTodo + "," +
            "\"isAlarmOn\": " + packingItem.isAlarmOn + "," +
            "\"packingSupperGroupOrder\": " + packingItem.packingSupperGroupOrder + "}";
    }
    
    public String convertPackingSuperGroupToJSONObjectString(PackingSuperGroup packingSuperGroup) {
        return "{" +
            "\"id\": " + ((packingSuperGroup != null) ? String.valueOf(packingSuperGroup.getId()) : "null") + "," +
            "\"trip\": " + ((packingSuperGroup.trip != null) ? String.valueOf(packingSuperGroup.trip.getId()) : "null") + "," +
            "\"superGroupId\": " + packingSuperGroup.superGroupId + "," +
            "\"sortOrder\": " + packingSuperGroup.sortOrder + "," +
            "\"superGroupText\": \"" + packingSuperGroup.superGroupText + "\"," +
            "\"isTodo\": " + packingSuperGroup.isTodo + "}";
    }
    
    public String convertProfileToJSONObjectString(Profile profile) {
        return "{" +
            "\"id\": " + ((profile != null) ? String.valueOf(profile.getId()) : "null") + "," +
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
            "\"id\": " + ((trip != null) ? String.valueOf(trip.getId()) : "null") + "," +
            "\"profile\": " + ((trip.profile != null) ? String.valueOf(trip.profile.getId()) : "null") + "," +
            "\"name\": \"" + trip.name + "\"," +
            "\"endTime\": " + trip.endTime + "," +
            "\"startTime\": " + trip.startTime + "," +
            "\"flag\": \"" + trip.flag + "\"}";
    }
}
