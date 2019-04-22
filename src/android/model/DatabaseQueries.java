package gov.cdc.oid.ncezid.travwell.model;

import android.content.Context;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import gov.cdc.oid.ncezid.travwell.utils.PreferenceUtils;

/**
 * Created by parker on 11/19/13.
 */
public class DatabaseQueries {

    public static Profile getProfile(Context context) {
        return new Select().from(Profile.class).where("Id =?", PreferenceUtils.getProfile(context)).executeSingle();
    }

    public static List<Profile> getAllProfiles() {
        return new Select().from(Profile.class).execute();
    }


    /**
     * Returns all of the trips on the device
     *
     * @return List<Trip>
     */
    public static List<Trip> getTripsAll(Context context) {
        return new Select().from(Trip.class).where(Profile.TABLE + "=?", PreferenceUtils.getProfile(context)).execute();
    }

    /**
     * Gets all the Destination with no trips, these are the destinations that the user can add to
     * their trips. Also are the objects that the API.constructDestinations() updates
     *
     * @return List<Destination>
     */
    public static List<Destination> getAllDestinations() {
        return new Select().from(Destination.class).where(Trip.TABLE + " IS NULL").execute();
    }

    public static List<Destination> getAllTripDestinations(long tripId) {
        return new Select().from(Destination.class).where(Trip.TABLE + "=?", tripId).execute();
    }

    public static Trip getTrip(long id) {
        return new Select().from(Trip.class).where("Id =?", id).executeSingle();
    }

    public static List<Disease> getDiseasesByTrip(long tripId) {
        return new Select().from(Disease.class).where(Trip.TABLE + "=?", tripId).execute();
    }

    public static PackingItem getPackingItem(long packingItemId) {
        PackingItem item = new Select().from(PackingItem.class).where("Id =?", packingItemId).executeSingle();
        if (item != null) {
            item.setAlarms(item.getAlarms());
        }
        return item;
    }

    /**
     * Makes the call for the Disease along with all of the relating calls alarms, drugs, and selected
     * drug with its alarms
     *
     * @param diseaseId
     * @return
     */
    public static Disease getDisease(long diseaseId) {
        Disease item = new Select().from(Disease.class).where("Id =?", diseaseId).executeSingle();
        if (item != null) {
            item.setAlarm(item.getAlarms());
            item.setDrugs(item.getDrugs());
            Drug drug = item.getSelectedDrug();
            if (drug != null) {
                drug.getAlarms();
                item.setSelectedDrug(drug);
            }
        }
        return item;
    }

    /**
     * Makes the call to get all diseases for a selected destination.
     *
     * @param destinationId
     * @return
     */
    public static List<Disease> getDiseasesByDestination(long destinationId) {
        return new Select().from(Disease.class).where(Destination.TABLE + " =?", destinationId).execute();
    }


    public static Drug getDrug(long drugId) {
        return new Select().from(Drug.class).where("Id =?", drugId).executeSingle();
    }

    /**
     * Will only retrieve diseases that have a profile, so profile disease
     *
     * @param listName
     * @return
     */
    public static List<Disease> getDiseaseByListName(String listName) {
        return new Select().from(Disease.class).where(Disease.Keys.DISEASE_LIST_NAME + "='" + listName + "' AND " +
                Profile.TABLE + " IS NOT NULL").execute();
    }

    public static List<Disease> getProfileVaccinesDisease(long profileId) {
        List<Disease> diseases = new Select().from(Disease.class).where(Profile.TABLE + " = " + profileId + " AND " +
                Trip.TABLE + " IS NULL").execute();
        for (Disease disease : diseases) {
            disease.getDrugs();
            for (Drug drug : disease.getDrugs()) {
                drug.getAlarms();
            }
        }
        return diseases;
    }

    public static List<PackingSuperGroup> getPackingSupperGroups(long tripId, boolean isTodo) {
        int intTodo = 0;
        if (isTodo) {
            intTodo = 1;
        }
        return new Select().from(PackingSuperGroup.class).where(Trip.TABLE + "=" + tripId + " AND " +
                PackingSuperGroup.Keys.IS_TODO + "=" + intTodo).execute();
    }

    public static Alarm getAlarm(long alarmId) {
        return new Select().from(Alarm.class).where("Id =?", alarmId).executeSingle();
    }

    public static List<Document> getDocumentsByTripCategory(long tripId, String category, long profileId) {
        return new Select().from(Document.class).where(Trip.TABLE + "=" + tripId + " AND " + Document.Keys.CATEGORY +
                "='" + category + "'" + " AND " + Profile.TABLE + "=" + profileId).execute();
    }

    public static List<Document> getDocumentsCategory(String category, long profileId) {
        return new Select().from(Document.class).where(Document.Keys.CATEGORY + "= '" + category + "'" + " AND " +
                Profile.TABLE + "=" + profileId).execute();
    }

    public static Document getDocument(long documentId) {
        return new Select().from(Document.class).where("Id =?", documentId).executeSingle();
    }

    public static List<Destination> getDestinationsByTrip(long tripId) {
        return new Select().from(Destination.class).where(Trip.TABLE + "=?", tripId).execute();
    }

    public static Destination getDestination(long destinationId) {
        return new Select().from(Destination.class).where("Id =?", destinationId).executeSingle();
    }

    /**
     * Get all of the alarms that are turned on
     *
     * @return
     */
    public static ArrayList<Alarm> getAlarmsOn() {
        return new Select().from(Alarm.class).where(Alarm.Keys.IS_ACTIVE + " =?", 1).execute();
    }
}