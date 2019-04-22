package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Document;

/**
 * Loader Task to get a document
 */
public class GetDocumentTask extends AsyncTaskLoader<Document> {

    private long documentId;

    public GetDocumentTask(Context context, long documentId) {
        super(context);
        this.documentId = documentId;
    }

    @Override
    public Document loadInBackground() {
        return DatabaseQueries.getDocument(documentId);
    }
}