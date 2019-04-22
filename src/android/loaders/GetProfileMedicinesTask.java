package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.PackingItem;
import gov.cdc.oid.ncezid.travwell.model.Profile;

public class GetProfileMedicinesTask extends AsyncTaskLoader<List<PackingItem>> {
    public GetProfileMedicinesTask(Context context) {
        super(context);
    }

    @Override
    public List<PackingItem> loadInBackground() {
        Profile profile = DatabaseQueries.getProfile(getContext());
        if (profile != null) {
            return profile.getProfileMedicines();
        }
        return null;
    }
}