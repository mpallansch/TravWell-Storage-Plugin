package gov.cdc.oid.ncezid.travwell.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by parker on 11/19/13.
 */
@Table(name = Drug.TABLE)
public class Drug extends Model {

    public static final String TABLE = "Drug";

    public Drug(String displayName, String friendlyName, String duration, String alertText, String
            reminderInstructions, String tsUpdated, Disease disease, Trip trip, Profile profile, String diseaseName,
                String drugType, String diseaseNameList) {
        this.displayName = displayName;
        this.friendlyName = friendlyName;
        this.duration = duration;
        this.alertText = alertText;
        this.reminderInstructions = reminderInstructions;
        this.tsUpdated = tsUpdated;
        this.disease = disease;
        this.trip = trip;
        this.profile = profile;
        this.diseaseFriendlyName = diseaseName;
        this.drugType = drugType;
        this.diseaseNameList = diseaseNameList;
    }

    /**
     * Do not update isAlarmOn, notes, isCompleted
     *
     * @param drug
     */
    public void updateDrug(Drug drug) {
        this.displayName = drug.displayName;
        this.friendlyName = drug.friendlyName;
        this.duration = drug.duration;
        this.alertText = drug.alertText;
        this.reminderInstructions = drug.reminderInstructions;
        this.tsUpdated = drug.tsUpdated;
        this.disease = drug.disease;
        this.trip = drug.trip;
        this.profile = drug.profile;
        this.diseaseFriendlyName = drug.diseaseFriendlyName;
        this.drugType = drug.drugType;
        this.diseaseNameList = drug.diseaseNameList;
        this.save();
    }

    public Drug() {
        super();
    }

    @Column(name = Keys.DISPLAY_NAME)
    private String displayName;

    @Column(name = Keys.FRIENDLY_NAME)
    private String friendlyName;

    @Column(name = Keys.DURATION)
    private String duration;

    @Column(name = Keys.ALERT_TEXT)
    private String alertText;

    @Column(name = Keys.REMINDER_INSTRUCTIONS)
    private String reminderInstructions;

    @Column(name = Keys.TS_UPDATED)
    private String tsUpdated;

    @Column(name = Keys.TIME_STARTED)
    private Long timeStarted;

    @Column(name = Disease.TABLE)
    private Disease disease;

    @Column(name = Trip.TABLE)
    private Trip trip;

    @Column(name = Keys.COMPLETED)
    private boolean isCompleted;

    @Column(name = Keys.NOTES)
    private String notes;

    @Column(name = Profile.TABLE)
    private Profile profile;

    @Column(name = Keys.DISEASE_NAME_FRIENDLY)
    private String diseaseFriendlyName;

    @Column(name = Keys.DISEASE_NAME_LIST)
    private String diseaseNameList;

    @Column(name = Keys.DRUG_TYPE)
    private String drugType;

    @Column(name = Keys.IS_ALARM_ON)
    private boolean isAlarmOn;

    private List<Alarm> alarms;

    public static final class Keys {
        public static final String DISPLAY_NAME = "DisplayName";
        public static final String FRIENDLY_NAME = "FriendlyName";
        public static final String DURATION = "Duration";
        public static final String ALERT_TEXT = "AlertText";
        public static final String REMINDER_INSTRUCTIONS = "ReminderInstructions";
        public static final String TS_UPDATED = "TsUpdated";
        public static final String TIME_STARTED = "TimeStarted";
        public static final String COMPLETED = "Completed";
        public static final String NOTES = "Notes";
        public static final String DISEASE_NAME_FRIENDLY = "DiseaseNameFriendly";
        public static final String DISEASE_NAME_LIST = "DiseaseNameList";
        public static final String DRUG_TYPE = "DrugType";
        public static final String IS_ALARM_ON = "IsAlarmOn";
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlertText() {
        return alertText;
    }

    public void setAlertText(String alertText) {
        this.alertText = alertText;
    }

    public String getReminderInstructions() {
        return reminderInstructions;
    }

    public void setReminderInstructions(String reminderInstructions) {
        this.reminderInstructions = reminderInstructions;
    }

    public String getTsUpdated() {
        return tsUpdated;
    }

    public void setTsUpdated(String tsUpdated) {
        this.tsUpdated = tsUpdated;
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

    public List<Alarm> getAlarms() {
        if (alarms == null) {
            alarms = new Select().from(Alarm.class).where(Drug.TABLE + " =?", this.getId()).orderBy(Alarm.Keys.TIME +
                    " ASC").execute();
        }
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    public Long getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Long timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Disease getDisease() {
        if (disease != null) {
            disease = new Select().from(Disease.class).where("Id=?", this.disease.getId()).executeSingle();
        }
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getDiseaseFriendlyName() {
        return diseaseFriendlyName;
    }

    public void setDiseaseFriendlyName(String diseaseFriendlyName) {
        this.diseaseFriendlyName = diseaseFriendlyName;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getDiseaseNameList() {
        return diseaseNameList;
    }

    public void setDiseaseNameList(String diseaseNameList) {
        this.diseaseNameList = diseaseNameList;
    }

    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean isAlarmOn) {
        this.isAlarmOn = isAlarmOn;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "friendlyName='" + friendlyName + '\'' +
                ", diseaseFriendlyName='" + diseaseFriendlyName + '\'' +
                "'mId=" + getId() + '}';
    }
}
