package gov.cdc.oid.ncezid.travwell.model;

import android.content.Context;
import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import gov.cdc.oid.ncezid.travwell.utils.DhAlarmManagerHelper;

/**
 * Created by parker on 11/22/13.
 */
@Table(name = Alarm.TABLE)
public class Alarm extends Model {

    public static final String TABLE = "Alarm";

    public static final long DAY_IN_MILLISECONDS = DateUtils.DAY_IN_MILLIS;
    public static final long WEEK_IN_MILLISECONDS = DateUtils.WEEK_IN_MILLIS;
    public static final String PRIMARY = "primary";
    public static final String SECONDARY = "secondary";

    /**
     * Constructor for the Drug alarms. This is used in the Trip Setup Service. These will be single shot alarms.
     * So repeating is set to false, and all that is put in is the time in milliseconds.
     *
     * @param drug
     * @param trip
     * @param disease
     * @param title
     * @param isActive
     */
    public Alarm(Drug drug, Trip trip, Disease disease, String title, long time, boolean isActive, String type, long
            interval, Profile profile) {
        this.drug = drug;
        this.trip = trip;
        this.disease = disease;
        this.isActive = isActive;
        this.time = time;
        this.title = title;
        this.interval = interval;
        this.type = type;
        this.repeating = false;
        this.profile = profile;
    }

    /**
     * Constructor for the packing items / list items alarms. These will be repeating and depending
     * on the days selected multiple per packing item, for single shot alarms set with time.
     * With repeat use the day, hour and minutes
     *
     * @param trip
     * @param packingItem
     * @param day
     * @param hour
     * @param minute
     * @param isActive
     */
    public Alarm(Trip trip, PackingItem packingItem, long time, int day, int hour, int minute, boolean repeating,
                 boolean isActive, String type, String title, long interval) {
        this.trip = trip;
        this.packingItem = packingItem;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.isActive = isActive;
        this.repeating = repeating;
        this.time = time;
        this.type = type;
        this.title = title;
        this.interval = interval;
    }

    public Alarm() {
        super();
    }

    /**
     * Use only in the Trip updating service, saves then activates the right things
     *
     * @param alarm
     * @param context
     */
    public void updateDrugAlarm(Alarm alarm, Context context) {
        this.drug = alarm.drug;
        this.trip = alarm.trip;
        this.disease = alarm.disease;
        this.time = alarm.time;
        this.title = alarm.title;
        this.interval = alarm.interval;
        this.type = alarm.type;
        this.saveActivate(context);
    }

    /**
     * Uses to fully copy an alarm and to remove the database id.
     *
     * @param alarm
     */
    public static Alarm copyAlarm(Alarm alarm) {
        Alarm newAlarm = new Alarm();
        newAlarm.setDrug(alarm.getDrug());
        newAlarm.setTrip(alarm.getTrip());
        newAlarm.setDisease(alarm.getDisease());
        newAlarm.setDay(alarm.getDay());
        newAlarm.setHour(alarm.getHour());
        newAlarm.setMinute(alarm.getMinute());
        newAlarm.setActive(alarm.isActive());
        newAlarm.setTitle(alarm.getTitle());
        newAlarm.setType(alarm.getType());
        newAlarm.setInterval(alarm.getInterval());
        newAlarm.setRepeating(alarm.isRepeating());
        newAlarm.setPackingItem(alarm.getPackingItem());
        newAlarm.setProfile(alarm.getProfile());
        return newAlarm;
    }

    @Column(name = Drug.TABLE)
    private Drug drug;

    @Column(name = Trip.TABLE)
    private Trip trip;

    @Column(name = Disease.TABLE)
    private Disease disease;

    @Column(name = Keys.DAY)
    private int day;

    @Column(name = Keys.HOUR)
    private int hour;

    @Column(name = Keys.MINUTE)
    private int minute;

    @Column(name = Keys.IS_ACTIVE)
    private boolean isActive;

    @Column(name = Keys.TIME)
    private long time;

    @Column(name = Keys.TITLE)
    private String title;

    @Column(name = Keys.TYPE)
    private String type;

    @Column(name = Keys.INTERVAL)
    private long interval;

    @Column(name = Keys.REPEATING)
    private boolean repeating;

    @Column(name = PackingItem.TABLE)
    private PackingItem packingItem;

    @Column(name = Profile.TABLE)
    private Profile profile;

    public static final class Keys {
        public static final String DAY = "Day";
        public static final String HOUR = "Hour";
        public static final String MINUTE = "Minute";
        public static final String IS_ACTIVE = "IsActive";
        public static final String TIME = "Time";
        public static final String TITLE = "Title";
        public static final String TYPE = "Type";
        public static final String INTERVAL = "Interval";
        public static final String REPEATING = "Repeating";
    }

    public Drug getDrug() {
        if (drug != null) {
            drug = new Select().from(Drug.class).where("Id =?", this.drug.getId()).executeSingle();
        }
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Disease getDisease() {
        if (disease != null) {
            disease = new Select().from(Disease.class).where("Id =?", this.disease.getId()).executeSingle();
        }
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Trip getTrip() {
        if (trip != null) {
            trip = DatabaseQueries.getTrip(this.trip.getId());
        }
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isActive() {
        return isActive;
    }

    // TODO make this method control if the alarm is on or off
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }


    public PackingItem getPackingItem() {
        if (packingItem != null && packingItem.getId() != null) {
            packingItem = new Select().from(PackingItem.class).where("Id =?", this.packingItem.getId()).executeSingle();
        }
        return packingItem;
    }

    public void setPackingItem(PackingItem packingItem) {
        this.packingItem = packingItem;
    }

    /**
     * First de-activates the alarm then deletes it
     */
    public void deleteDeactivate(Context context) {
        setActive(false);
        activateDeactivateAlarm(context);
        delete();
    }

    /**
     * First saves the alarm then activates it depending on the isActive boolean
     */
    public void saveActivate(Context context) {
        save();
        activateDeactivateAlarm(context);
    }

    /**
     * Depending on if the alarm is active or not set it up
     */
    public void activateDeactivateAlarm(Context context) {
        DhAlarmManagerHelper alarmManagerHelper = new DhAlarmManagerHelper(context);
        if (isActive()) {
            alarmManagerHelper.cancelRepeatingAlarm(this);
            alarmManagerHelper.setRepeatingAlarm(this);
        } else {
            alarmManagerHelper.cancelRepeatingAlarm(this);
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
}