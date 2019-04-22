package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Drug;
import gov.cdc.oid.ncezid.travwell.model.Profile;

public class GetProfileVaccinesTask extends AsyncTaskLoader<List<Drug>> {

    public GetProfileVaccinesTask(Context context) {
        super(context);
    }

    @Override
    public List<Drug> loadInBackground() {
        Profile profile = DatabaseQueries.getProfile(getContext());
        if (profile != null) {
            // reset the calls
            profile.setVaccineDrugs(null);
            List<Drug> drugs = profile.getVaccineDrugs();
            return drugs;
        }
        return null;
    }
}
