package gov.cdc.oid.ncezid.travwell.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import gov.cdc.oid.ncezid.travwell.R;
import gov.cdc.oid.ncezid.travwell.utils.PreferenceUtils;

/**
 * Created by parker on 11/19/13.
 */
@Table(name = Trip.TABLE)
public class Trip extends Model {

    public static final String TABLE = "Trip";

    public Trip(String name, long endTime, long startTime, String flag, Profile profile) {
        this.name = name;
        this.endTime = endTime;
        this.startTime = startTime;
        this.flag = flag;
        this.profile = profile;
    }

    /**
     * updates Trip and saves it
     *
     * @param trip
     */
    public void updateTrip(Trip trip) {
        this.name = trip.name;
        this.endTime = trip.getEndTime();
        this.startTime = trip.getStartTime();
        this.flag = trip.flag;
        this.profile = trip.profile;
        this.save();
    }

    public Trip() {
        super();
    }

    @Column(name = Keys.NAME)
    private String name;

    @Column(name = Keys.END_TIME)
    private long endTime;

    @Column(name = Keys.START_TIME)
    private long startTime;

    @Column(name = Keys.FLAG)
    private String flag;

    @Column(name = Profile.TABLE)
    private Profile profile;

    /**
     * Comma separated list of Longs (Db ids)
     */
    @Column(name = Keys.CHECKED_PACKING_LIST_MEDICINES)
    private String checkedPackingListItems;
    private HashSet<Long> checkedPackingListMedicines;

    /**
     * Comma separated list of Longs (Db ids)
     */
    @Column(name = Keys.CHECKED_TO_DO_MEDICINES)
    private String checkedToDoMedicinesItems;
    private HashSet<Long> checkedToDoMedicines;

    @Column(name = Keys.DELETED_PACKING_LIST_MEDICINES)
    private String deletedPackingListItems;
    private HashSet<Long> deletedPackingListMedicines;

    @Column(name = Keys.DELETED_TO_DO_MEDICINES)
    private String deletedToDoMedicinesItems;
    private HashSet<Long> deletedToDoMedicines;

    public static final class Keys {
        public static final String NAME = "Name";
        public static final String END_TIME = "EndTime";
        public static final String START_TIME = "StartTime";
        public static final String FLAG = "Flag";
        public static final String CHECKED_PACKING_LIST_MEDICINES = "CheckedPackedListMedicines";
        public static final String CHECKED_TO_DO_MEDICINES = "CheckedToDoMedicines";
        public static final String DELETED_PACKING_LIST_MEDICINES = "DeletedPackingListMedicines";
        public static final String DELETED_TO_DO_MEDICINES = "DeletedToDoMedicines";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public HashSet<Long> setCheckedPackingListMedicines() {
        return this.checkedPackingListMedicines = getSetFromString(checkedPackingListItems);
    }

    public void setCheckedPackingListMedicines(HashSet<Long> checkedPackingListMedicines) {
        this.checkedPackingListMedicines = checkedPackingListMedicines;
        this.checkedPackingListItems = getCommaSeparatedValueFromSet(checkedPackingListMedicines);
    }

    public HashSet<Long> getCheckedToDoMedicines() {
        return this.checkedToDoMedicines = getSetFromString(checkedToDoMedicinesItems);
    }

    public void setCheckedToDoMedicines(HashSet<Long> checkedToDoMedicines) {
        this.checkedToDoMedicines = checkedToDoMedicines;
        this.checkedToDoMedicinesItems = getCommaSeparatedValueFromSet(checkedToDoMedicines);
    }

    public HashSet<Long> getDeletedToDoMedicines() {
        return this.deletedToDoMedicines = getSetFromString(deletedToDoMedicinesItems);
    }

    public void setDeletedToDoMedicines(HashSet<Long> deletedToDoMedicines) {
        this.deletedToDoMedicines = deletedToDoMedicines;
        this.deletedToDoMedicinesItems = getCommaSeparatedValueFromSet(deletedToDoMedicines);

    }

    public HashSet<Long> getDeletedPackingListMedicines() {
        return this.deletedPackingListMedicines = getSetFromString(deletedPackingListItems);
    }

    public void setDeletedPackingListMedicines(HashSet<Long> deletedPackingListMedicines) {
        this.deletedPackingListMedicines = deletedPackingListMedicines;
        this.deletedPackingListItems = getCommaSeparatedValueFromSet(deletedPackingListMedicines);
    }

    private String getCommaSeparatedValueFromSet(HashSet<Long> set) {
        if (set != null) {
            String csv = set.toString().replace("[", "").replace("]", "").replace(", ", ",");
            return csv;
        }
        return "";
    }

    private HashSet<Long> getSetFromString(String commaSeparatedList) {
        HashSet<Long> set = null;
        if (!TextUtils.isEmpty(commaSeparatedList)) {
            List<String> items = Arrays.asList(commaSeparatedList.split("\\s*,\\s*"));
            set = new HashSet<Long>();
            for (String item : items) {
                try {
                    Long itemLong = Long.parseLong(item);
                    set.add(itemLong);
                } catch (NumberFormatException e) {
                }
            }
        }
        if (set == null) {
            set = new HashSet<Long>();
        }
        return set;
    }

    private List<Destination> destinations;
    private List<PackingSuperGroup> packingSuperGroups;
    private List<PackingItem> allPackingItems;
    private List<PackingItem> todoItems;
    private List<PackingItem> packingItems;
    private List<PackingGroup> packingGroups;
    private List<Drug> drugs;
    private List<Disease> diseases;
    private List<Alarm> alarms;

    public List<Destination> getDestinations() {
        if (destinations == null) {
            destinations = new Select().from(Destination.class).where(Trip.TABLE + "=?", this.getId()).execute();
        }
        return destinations;
    }

    public List<PackingSuperGroup> getPackingSuperGroups() {
        if (packingSuperGroups == null) {
            packingSuperGroups = new Select().from(PackingSuperGroup.class).where(Trip.TABLE + "=?", this.getId()).execute();
        }
        return packingSuperGroups;
    }

    public List<PackingGroup> getPackingGroups() {
        if (packingGroups == null) {
            packingGroups = new Select().from(PackingGroup.class).where(Trip.TABLE + "=?", this.getId()).execute();

        }
        return packingGroups;
    }

    /**
     * Returns all of the PackingItems and To Do Items
     *
     * @return
     */
    public List<PackingItem> getAllPackingItems() {
        if (allPackingItems == null) {
            allPackingItems = new Select().from(PackingItem.class).where(Trip.TABLE + "=?", this.getId()).execute();
        }
        return allPackingItems;
    }

    /**
     * Just returns the To Do items
     *
     * @return
     */
    public List<PackingItem> getTodoItems() {
        if (todoItems == null) {
            todoItems = new Select().from(PackingItem.class).where(Trip.TABLE + "=" + this.getId() + " AND " + PackingItem.Keys.IS_TODO + "=" + 1).execute();
        }
        return todoItems;
    }

    /**
     * Just returns the Packing items
     *
     * @return
     */
    public List<PackingItem> getPackingItems() {
        if (packingItems == null) {
            packingItems = new Select().from(PackingItem.class).where(Trip.TABLE + "=" + this.getId() + " AND " + PackingItem.Keys.IS_TODO + "=" + 0).execute();

        }
        return packingItems;
    }

    /**
     * Returns all the Drugs (Vaccines and Medicines)
     *
     * @return
     */
    public List<Drug> getDrugs() {
        if (drugs == null) {
            drugs = new Select().from(Drug.class).where(Trip.TABLE + "=?", this.getId()).execute();
        }
        return drugs;
    }

    public List<Disease> getDiseases() {
        if (diseases == null) {
            diseases = new Select().from(Disease.class).where(Trip.TABLE + "=?", this.getId()).execute();
        }
        return diseases;
    }

    public List<Alarm> getAlarms() {
        if (alarms == null) {
            alarms = new Select().from(Alarm.class).where(Trip.TABLE + "=?", this.getId()).execute();
        }
        return alarms;
    }

    public static void deepDelete(Trip trip, Context context) {
        ActiveAndroid.beginTransaction();
        try {

            for (Alarm alarm : trip.getAlarms()) {
                // deactivate the alarms then delete them
                alarm.deleteDeactivate(context);
            }

            // Only want the documents for that trip
            List<Document> documents = DatabaseQueries.getDocumentsByTripCategory(trip.getId(), context.getString(R.string.doc_category_current), PreferenceUtils.getProfile(context));
            for (Document document : documents) {
                // delete the files too
                document.deepDelete();
            }

            for (Drug drug : trip.getDrugs()) {
                drug.delete();
            }

            for (Disease disease : trip.getDiseases()) {
                // special delete so the profile vaccines will have something to point to
                // set the selected drug to null so no sql constraint errors
                disease.setSelectedDrug(null);
                disease.deleteDisease();
            }
            for (PackingItem packingItem : trip.getAllPackingItems()) {
                packingItem.delete();
            }
            for (PackingGroup packingGroup : trip.getPackingGroups()) {
                packingGroup.delete();
            }
            for (PackingSuperGroup packingSuperGroup : trip.getPackingSuperGroups()) {
                packingSuperGroup.delete();
            }
            for (Destination destination : trip.getDestinations()) {
                destination.delete();
            }

            trip.delete();


            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public Profile getProfile() {
        if (profile != null) {
            profile = new Select().from(Profile.class).where("Id =?", profile.getId()).executeSingle();
        }
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProfileMedicineChecked(HashSet<Long> checkedMedicineIds, boolean isTodo) {
        new CheckTaskProfileMedicineTask(checkedMedicineIds, isTodo).execute();
    }

    private class CheckTaskProfileMedicineTask extends AsyncTask<Void, Void, Void> {

        private HashSet<Long> checkedMedicineIds;
        private boolean isToDo;

        public CheckTaskProfileMedicineTask(HashSet<Long> checkedMedicineIds, boolean isToDo) {
            this.checkedMedicineIds = checkedMedicineIds;
            this.isToDo = isToDo;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (checkedMedicineIds == null) {
                return null;
            }
            if (isToDo) {
                setCheckedToDoMedicines(checkedMedicineIds);
            } else {
                setCheckedPackingListMedicines(checkedMedicineIds);
            }
            save();
            return null;
        }
    }

    public static class DeepDeleteTaskTrip extends AsyncTask<Void, Void, Void> {
        private Trip trip;
        private Context context;
        private List<Trip> trips;

        public DeepDeleteTaskTrip(Context context, Trip trip) {
            this.trip = trip;
            this.context = context;
        }

        public DeepDeleteTaskTrip(Context context, List<Trip> trips) {

            this.context = context;
            this.trips = trips;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (trip != null) {
                deepDelete(trip, context);
            } else if (trips != null) {
                for (Trip trip : trips) {
                    deepDelete(trip, context);
                }
            }
            return null;
        }
    }
}