package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import gov.cdc.oid.ncezid.travwell.R;
import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Trip;

/**
 * Loader Task to get all of the trips on the device then separates them out into current and
 * completed trips with headers
 */
public class GetAllTripsTask extends AsyncTaskLoader<List<Trip>> {

    public GetAllTripsTask(Context context) {
        super(context);
    }

    @Override
    public List<Trip> loadInBackground() {
        List<Trip> trips = DatabaseQueries.getTripsAll(getContext());
        if (trips.size() > 0) {
            List<Trip> currentTrips = new ArrayList<Trip>();
            List<Trip> completedTrips = new ArrayList<Trip>();
            long currentTime = System.currentTimeMillis();
            for (Trip trip : trips) {
                if (currentTime >= trip.getEndTime()) {
                    currentTrips.add(trip);
                } else {
                    completedTrips.add(trip);
                }
            }
            if (currentTrips.size() > 0) {
                Trip header = new Trip("Completed Trips", 0, 0, null, null);
                currentTrips.add(0, header);
            }
            if (completedTrips.size() > 0) {
                Trip header = new Trip("Current Trips", 0, 0, null, null);
                completedTrips.add(0, header);
            }
            trips.clear();
            trips.addAll(completedTrips);
            trips.addAll(currentTrips);
        }

        return trips;
    }

}
