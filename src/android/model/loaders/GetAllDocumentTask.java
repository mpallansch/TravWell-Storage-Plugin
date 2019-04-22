package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import gov.cdc.oid.ncezid.travwell.R;
import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Document;
import gov.cdc.oid.ncezid.travwell.utils.PreferenceUtils;

/**
 * Loader to get all of the documents
 */
public class GetAllDocumentTask extends AsyncTaskLoader<ArrayList<List<Document>>> {

    private long tripId;

    public GetAllDocumentTask(Context context, long tripId) {
        super(context);
        this.tripId = tripId;
    }

    @Override
    public ArrayList<List<Document>> loadInBackground() {
        long profileId = PreferenceUtils.getProfile(getContext());
        List<Document> all = DatabaseQueries.getDocumentsCategory("All Documents", profileId);
        List<Document> trip = DatabaseQueries.getDocumentsByTripCategory(tripId, "Current Trip", profileId);
        ArrayList<List<Document>> combinedDocuments = new ArrayList<List<Document>>();
        combinedDocuments.add(trip);
        combinedDocuments.add(all);
        return combinedDocuments;
    }
}