package gov.cdc.oid.ncezid.travwell.model;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import gov.cdc.oid.ncezid.travwell.ui.trip.FragmentTripList;

import java.util.HashSet;
import java.util.List;

/**
 * Created by parker on 11/19/13.
 */
@Table(name = PackingItem.TABLE)
public class PackingItem extends Model {

    public static final int USER_PACKING_ITEM = 0;
    public static final int PROFILE_MEDICINE = -100;
    public static final int HEADER = -1;

    public PackingItem() {
        super();
    }

    public static final String TABLE = "PackingItem";

    public PackingItem(int itemId, String displayName, String descriptionContent, String appSpecificContent, int sortOrder, PackingGroup packingGroup, Trip trip, PackingSuperGroup packingSuperGroup, boolean isTodo, boolean isAlarmOn, int packingSupperGroupOrder) {
        this.itemId = itemId;
        this.displayName = displayName;
        this.descriptionContent = descriptionContent;
        this.appSpecificContent = appSpecificContent;
        this.sortOrder = sortOrder;
        this.packingGroup = packingGroup;
        this.trip = trip;
        this.packingSuperGroup = packingSuperGroup;
        this.isTodo = isTodo;
        this.isAlarmOn = isAlarmOn;
        this.packingSupperGroupOrder = packingSupperGroupOrder;
    }

    /**
     * Do not update isAlarmOn, isTodo, and notes
     *
     * @param packingItem
     */
    public void updatePackingItem(PackingItem packingItem) {
        this.itemId = packingItem.itemId;
        this.displayName = packingItem.displayName;
        this.descriptionContent = packingItem.descriptionContent;
        this.appSpecificContent = packingItem.appSpecificContent;
        this.sortOrder = packingItem.sortOrder;
        this.packingGroup = packingItem.packingGroup;
        this.trip = packingItem.trip;
        this.packingSuperGroup = packingItem.packingSuperGroup;
        this.packingSupperGroupOrder = packingItem.packingSupperGroupOrder;
        this.save();
    }

    @Column(name = Keys.ITEM_ID)
    private int itemId;

    @Column(name = Keys.DISPLAY_NAME)
    private String displayName;

    @Column(name = Keys.DESCRIPTION_CONTENT)
    private String descriptionContent;

    @Column(name = Keys.APP_SPECIFIC_CONTENT)
    private String appSpecificContent;

    @Column(name = Keys.SORT_ORDER)
    private int sortOrder;

    @Column(name = Keys.NOTES)
    private String notes;

    @Column(name = PackingGroup.TABLE)
    private PackingGroup packingGroup;

    @Column(name = Trip.TABLE)
    private Trip trip;

    @Column(name = Keys.IS_COMPLETED)
    private boolean isCompleted;

    @Column(name = PackingSuperGroup.TABLE)
    private PackingSuperGroup packingSuperGroup;

    @Column(name = Keys.IS_TODO)
    private boolean isTodo;

    @Column(name = Keys.IS_ALARM_ON)
    private boolean isAlarmOn;

    @Column(name = Keys.PACKING_SUPPER_GROUP_ORDER)
    int packingSupperGroupOrder;

    @Column(name = Profile.TABLE)
    private Profile profile;

    private List<Alarm> alarms;

    public static final class Keys {
        public static final String IS_COMPLETED = "IsCompleted";
        public static final String ITEM_ID = "ItemId";
        public static final String DISPLAY_NAME = "DisplayName";
        public static final String DESCRIPTION_CONTENT = "DescriptionContent";
        public static final String APP_SPECIFIC_CONTENT = "AppSpecificContent";
        public static final String SORT_ORDER = "SortOrder";
        public static final String NOTES = "Notes";
        public static final String IS_TODO = "IsTodo";
        public static final String IS_ALARM_ON = "IsAlarmOn";
        public static final String PACKING_SUPPER_GROUP_ORDER = "PackingSupperGroupOrder";
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescriptionContent() {
        return descriptionContent;
    }

    public void setDescriptionContent(String descriptionContent) {
        this.descriptionContent = descriptionContent;
    }

    public String getAppSpecificContent() {
        return appSpecificContent;
    }

    public void setAppSpecificContent(String appSpecificContent) {
        this.appSpecificContent = appSpecificContent;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public PackingGroup getPackingGroup() {
        return new Select().from(PackingGroup.class).where("Id =?", this.packingGroup.getId()).executeSingle();
    }

    public void setPackingGroup(PackingGroup packingGroup) {
        this.packingGroup = packingGroup;
    }

    public Trip getTrip() {
        return DatabaseQueries.getTrip(this.trip.getId());
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<Alarm> getAlarms() {
        if (alarms == null) {
            alarms = new Select().from(Alarm.class).where(PackingItem.TABLE + "=?", this.getId()).execute();
        }
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public PackingSuperGroup getPackingSuperGroup() {
        if (packingSuperGroup != null && packingSuperGroup.getSuperGroupText() != null) {
            packingSuperGroup = new Select().from(PackingSuperGroup.class).where("Id =?", this.packingSuperGroup.getId()).executeSingle();
        }
        return packingSuperGroup;
    }

    public void setPackingSuperGroup(PackingSuperGroup packingSuperGroup) {
        this.packingSuperGroup = packingSuperGroup;
    }

    public boolean isTodo() {
        return isTodo;
    }

    public void setTodo(boolean isTodo) {
        this.isTodo = isTodo;
    }

    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean isAlarmOn) {
        this.isAlarmOn = isAlarmOn;
    }

    public int getPackingSupperGroupOrder() {
        return packingSupperGroupOrder;
    }

    public void setPackingSupperGroupOrder(int packingSupperGroupOrder) {
        this.packingSupperGroupOrder = packingSupperGroupOrder;
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

    public static class SavePackingItem extends AsyncTask<Void, Void, Void> {
        private PackingItem packingItem;
        private List<Alarm> newAlarms;
        private Context context;
        private int type;

        public SavePackingItem(PackingItem packingItem, List<Alarm> newAlarms, Context context, int type) {
            this.packingItem = packingItem;
            this.newAlarms = newAlarms;
            this.context = context.getApplicationContext();
            this.type = type;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ActiveAndroid.beginTransaction();
            try {
                if (type == FragmentTripList.PROFILE_MEDICINES) {
                    packingItem.setItemId(PackingItem.PROFILE_MEDICINE);
                    Profile profile = DatabaseQueries.getProfile(context);
                    if (profile != null) {
                        packingItem.setProfile(profile);
                    }
                }
                // check to see if this is a new packingItem
                boolean newPackingItem = false;
                if (packingItem.getId() == null) {
                    newPackingItem = true;
                }
                packingItem.save();
                if (newAlarms != null) {
                    boolean alarmsOn = false;
                    for (Alarm alarm : newAlarms) {
                        // need to create a new alarm object to save and get a different id;
                        Alarm newAlarm = new Alarm(alarm.getTrip(), alarm.getPackingItem(), alarm.getTime(), alarm.getDay(), alarm.getHour(), alarm.getMinute(), alarm.isRepeating(), alarm.isActive(), alarm.getType(), alarm.getTitle(), alarm.getInterval());
                        newAlarm.setPackingItem(packingItem);
                        newAlarm.saveActivate(context);
                        if (!alarmsOn) {
                            alarmsOn = newAlarm.isActive();
                        }
                    }
                    // check remove old alarms
                    if (!newPackingItem) {
                        List<Alarm> oldAlarms = packingItem.getAlarms();
                        for (Alarm oldAlarm : oldAlarms) {
                            oldAlarm.deleteDeactivate(context);
                        }
                    }
                    // make sure the alarms boolean are toggle
                    packingItem.setAlarmOn(alarmsOn);
                    packingItem.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
            return null;
        }
    }

    public static class DeletePackingItems extends AsyncTask<Void, Void, Void> {
        private List<PackingItem> packingItems;
        private HashSet<PackingItem> packingItemsSet;
        private PackingItem packingItem;
        private Context context;
        private Trip trip;
        private long tripId;
        private HashSet<Long> deletedMedicineIds;
        boolean isTodo;


        public DeletePackingItems(List<PackingItem> packingItems, Context context, long tripId, boolean isTodo) {
            this.packingItems = packingItems;
            this.context = context.getApplicationContext();
            this.tripId = tripId;
            this.isTodo = isTodo;
        }

        public DeletePackingItems(HashSet<PackingItem> packingItemsSet, Context context, long tripId, boolean isTodo) {
            this.packingItemsSet = packingItemsSet;
            this.context = context.getApplicationContext();
            this.tripId = tripId;
            this.isTodo = isTodo;
        }


        public DeletePackingItems(PackingItem packingItem, Context context, long tripId, boolean isTodo) {
            this.packingItem = packingItem;
            this.context = context.getApplicationContext();
            this.tripId = tripId;
            this.isTodo = isTodo;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (packingItems != null && packingItems.size() > 0) {
                for (PackingItem item : packingItems) {
                    deletePackingItem(item, context);
                }
            } else if (packingItem != null) {
                deletePackingItem(packingItem, context);
            } else if (packingItemsSet != null && packingItemsSet.size() > 0) {
                for (PackingItem item : packingItems) {
                    deletePackingItem(item, context);
                }
            }
            // only save when there is a trip, there is only a trip for there is a profile medicine
            if (trip != null && deletedMedicineIds != null) {
                if (isTodo) {
                    trip.setDeletedToDoMedicines(deletedMedicineIds);
                } else {
                    trip.setDeletedPackingListMedicines(deletedMedicineIds);
                }
                trip.save();
            }
            return null;
        }

        private void deletePackingItem(PackingItem packingItem, Context context) {
            // check the trip id, if not valid we are in the profile and we are going to globally
            // delete the medicine item instead of fake deleting it
            if (packingItem.getItemId() == PackingItem.PROFILE_MEDICINE && tripId > 0) {
                if (deletedMedicineIds == null) {
                    if (trip == null) {
                        trip = DatabaseQueries.getTrip(tripId);
                    }
                    if (isTodo) {
                        deletedMedicineIds = trip.getDeletedToDoMedicines();
                    } else {
                        deletedMedicineIds = trip.getDeletedPackingListMedicines();
                    }
                }
                deletedMedicineIds.add(packingItem.getId());
            } else {
                List<Alarm> alarms = packingItem.getAlarms();
                if (alarms != null && alarms.size() > 0) {
                    for (Alarm alarm : alarms) {
                        alarm.deleteDeactivate(context);
                    }
                }
                packingItem.delete();
            }
        }
    }
}