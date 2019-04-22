package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Destination;

/**
 * Loader Task to get destinations from trips
 */
public class GetDestinationTask extends AsyncTaskLoader<Destination> {

    private long destinationId;

    public GetDestinationTask(Context context, long destinationId) {
        super(context);
        this.destinationId = destinationId;
    }

    @Override
    public Destination loadInBackground() {
        return DatabaseQueries.getDestination(destinationId);
    }
}