package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import gov.cdc.oid.ncezid.travwell.model.Alarm;
import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Destination;
import gov.cdc.oid.ncezid.travwell.model.Disease;
import gov.cdc.oid.ncezid.travwell.model.Drug;
import gov.cdc.oid.ncezid.travwell.utils.PreferenceUtils;

/**
 * Loader Task to get all of the diseases for a selected trip. Adds the Destinations as a 'header' diease object.
 */
public class GetAllDiseasesTask extends AsyncTaskLoader<List<Disease>> {

    private long tripId;

    public GetAllDiseasesTask(Context context, long tripId) {
        super(context);
        this.tripId = tripId;
    }

    @Override
    public List<Disease> loadInBackground() {
        List<Disease> diseases = new ArrayList<>();
        List<Disease> profileDiseases = DatabaseQueries.getProfileVaccinesDisease(PreferenceUtils.getProfile
                (getContext().getApplicationContext()));
        // First get all of the destinations for the trip
        List<Destination> destinations = DatabaseQueries.getAllTripDestinations(tripId);
        for (int i = 0, size = destinations.size(); i < size; i++) {
            Destination destination = destinations.get(i);
            List<Disease> destinationDiseases = destination.getDiseases();
            setProfileDiseases(profileDiseases, destinationDiseases);
            // Add the Destination Headers
            if (destinationDiseases != null && destinationDiseases.size() > 0) {
                Disease header = Disease.newInstanceDestinationHeader(destination.getNameList());
                diseases.add(header);
                diseases.addAll(destinationDiseases);
            }
        }
        if (diseases.size() == 0) {
            // Old trips do not associate the diseases by the destination, but by the trip
            diseases = DatabaseQueries.getDiseasesByTrip(tripId);
        }
        return diseases;
    }

    /**
     * Sets the profile disease alarms and alarm state on to the destination disease.
     *
     * @param profileDiseases
     * @param destinationDisease
     * @return
     */
    public static Disease setProfileDisease(List<Disease> profileDiseases, Disease destinationDisease) {
        for (int x = 0, sizeX = profileDiseases.size(); x < sizeX; x++) {
            Disease profileDisease = profileDiseases.get(x);
            if (hasSameName(destinationDisease, profileDisease)) {
                Drug profileDrug = profileDisease.getSelectedDrug();
                if (profileDrug != null) {
                    List<Alarm> profileAlarms = profileDrug.getAlarms();
                    if (profileAlarms != null) {
                        destinationDisease.setAlarmOn(isAlarmOn(profileAlarms));
                        destinationDisease.setAlarm(profileAlarms);
                    }
                }
            }
        }
        return destinationDisease;
    }

    /**
     * Sets the profile diseases alarms and alarms states onto the destination Diseases
     *
     * @param profileDiseases
     * @param destinationDiseases
     * @return
     */
    public static List<Disease> setProfileDiseases(List<Disease> profileDiseases, List<Disease> destinationDiseases) {
        for (int i = 0, sizeI = destinationDiseases.size(); i < sizeI; i++) {
            Disease destinationDisease = destinationDiseases.get(i);
            setProfileDisease(profileDiseases, destinationDisease);
        }
        return destinationDiseases;
    }

    public static boolean isAlarmOn(List<Alarm> profileAlarms) {
        for (int a = 0, sizeA = profileAlarms.size(); a < sizeA; a++) {
            Alarm alarm = profileAlarms.get(a);
            if (alarm.isActive()) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSameName(Disease d1, Disease d2) {
        return TextUtils.equals(d1.getFriendlyName(), d2.getFriendlyName());
    }

}