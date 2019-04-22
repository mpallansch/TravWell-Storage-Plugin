package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Destination;

/**
 * Loader Task to get destinations from trips
 */
public class GetDestinationsTask extends AsyncTaskLoader<List<Destination>> {

    private long tripId;

    public GetDestinationsTask(Context context, long tripId) {
        super(context);
        this.tripId = tripId;
    }

    @Override
    public List<Destination> loadInBackground() {
        return DatabaseQueries.getDestinationsByTrip(tripId);
    }
}