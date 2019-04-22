package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.util.List;

import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.Disease;
import gov.cdc.oid.ncezid.travwell.model.Drug;
import gov.cdc.oid.ncezid.travwell.model.Profile;

/**
 * Created by parker on 7/21/15.
 */
public class GetDiseaseTask extends AsyncTaskLoader<Disease> {

    private long mDiseaseId;
    private long mDrugId;

    public static GetDiseaseTask newInstanceDisease(Context context, long diseaseId) {
        return new GetDiseaseTask(context, diseaseId, 0L);
    }

    public static GetDiseaseTask newInstanceDrug(Context context, long drugId) {
        return new GetDiseaseTask(context, 0L, drugId);
    }

    private GetDiseaseTask(Context context, long diseaseId, long drugId) {
        super(context);
        mDiseaseId = diseaseId;
        mDrugId = drugId;
    }

    @Override
    public Disease loadInBackground() {
        if (mDiseaseId > 0L) {
            return getByDiseaseId(getContext(), mDiseaseId);
        }
        if (mDrugId > 0L) {
            return getByDrugId(mDrugId);
        }
        return null;
    }

    private Disease getByDiseaseId(Context context, long id) {
        Disease disease = DatabaseQueries.getDisease(id);
        if (disease != null) {
            // need to check for selected drug, we need to check because of the vaccine drugs
            // can be added via the profile, only vaccines
            if (TextUtils.equals(disease.getDrugType(), Disease.TYPE_VACCINE)) {
                if (disease.getSelectedDrug() == null) {
                    Profile profile = DatabaseQueries.getProfile(context);
                    List<Drug> profileDrugs = profile.getVaccineDrugs();

                    for (Drug profileDrug : profileDrugs) {
                        if (TextUtils.equals(disease.getDiseaseListName(), profileDrug.getDiseaseNameList())) {
                            disease.setSelectedDrug(profileDrug);
                            disease.save();
                            return disease;
                        }
                    }
                }
            }
        }
        return disease;
    }

    private Disease getByDrugId(long id) {
        Drug drug = DatabaseQueries.getDrug(id);
        if (drug != null) {
            List<Disease> diseases = DatabaseQueries.getDiseaseByListName(drug.getDiseaseNameList());
            if (diseases != null) {
                int size = diseases.size();
                if (size > 0) {
                    return diseases.get(size - 1);
                }
            }
        }
        return null;
    }
}
