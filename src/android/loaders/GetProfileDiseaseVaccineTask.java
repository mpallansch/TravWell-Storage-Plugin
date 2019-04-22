package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Disease;
import gov.cdc.oid.ncezid.travwell.model.Profile;

/**
 * Does not show the diseases that have added
 */
public class GetProfileDiseaseVaccineTask extends AsyncTaskLoader<List<Disease>> {

    public GetProfileDiseaseVaccineTask(Context context) {
        super(context);
    }

    @Override
    public List<Disease> loadInBackground() {
        Profile profile = DatabaseQueries.getProfile(getContext().getApplicationContext());
        if (profile != null) {
            List<Disease> diseases = DatabaseQueries.getProfileVaccinesDisease(profile.getId());
            List<Disease> cleanedDiseases = new ArrayList<Disease>();
            for (Disease disease : diseases) {
                if (disease.getSelectedDrug() == null) {
                    cleanedDiseases.add(disease);
                }
            }
            return cleanedDiseases;
        }
        return null;
    }
}