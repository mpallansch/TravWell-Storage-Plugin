package gov.cdc.oid.ncezid.travwell.model;

import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by parker on 11/19/13.
 */
@Table(name = Disease.TABLE)
public class Disease extends Model {

    public static final String TABLE = "Disease";

    public static final String TYPE_MEDICINE = "Medicine";
    public static final String TYPE_VACCINE = "Vaccine";
    public static final String NAME_MALARIA = "Malaria";

    public Disease(String diseaseListName, String friendlyName, String groupText, String findOutWhyHtml, String
            diseasePageUrl, Trip trip, String drugType, Profile profile) {
        this.diseaseListName = diseaseListName;
        this.friendlyName = friendlyName;
        this.groupText = groupText;
        this.findOutWhyHtml = findOutWhyHtml;
        this.diseasePageUrl = diseasePageUrl;
        this.trip = trip;
        this.drugType = drugType;
        this.profile = profile;
    }

    /**
     * Diseases are related to destinations
     *
     * @param disease
     * @param destination
     */
    public void updateDisease(Disease disease, Destination destination) {
        this.diseaseListName = disease.diseaseListName;
        this.friendlyName = disease.friendlyName;
        this.groupText = disease.groupText;
        this.findOutWhyHtml = disease.findOutWhyHtml;
        this.diseasePageUrl = disease.diseasePageUrl;
        this.trip = disease.trip;
        this.drugType = disease.drugType;
        this.profile = disease.profile;
        this.destination = destination;
        this.save();
    }

    public static Disease newInstanceDestinationHeader(String destinationName) {
        Disease disease = new Disease();
        disease.setDiseaseListName(destinationName);
        return disease;
    }

    /**
     * Checks to make sure the object is not a {@link Disease} made by the {@link
     * Disease#newInstanceDestinationHeader(String)}
     * <p/>
     * Checks the {@link #getId()}, {@link #diseasePageUrl}, and the {@link #friendlyName} to be null / empty since
     * only the {@link #diseaseListName} is set
     *
     * @return
     */
    public boolean isHeader() {
        return getId() == null && TextUtils.isEmpty(diseasePageUrl) && TextUtils.isEmpty(friendlyName);
    }

    public Disease() {
        super();
    }

    @Column(name = Keys.DISEASE_LIST_NAME)
    private String diseaseListName;

    @Column(name = Keys.FRIENDLY_NAME)
    private String friendlyName;

    @Column(name = Keys.GROUP_TEXT)
    private String groupText;

    @Column(name = Keys.FIND_OUT_WHY)
    private String findOutWhyHtml;

    @Column(name = Keys.DISEASE_PAGE_URL)
    private String diseasePageUrl;

    @Column(name = Trip.TABLE)
    private Trip trip;

    @Column(name = Keys.DRUG_SELECTED)
    private Drug selectedDrug;

    @Column(name = Keys.IS_ALARM_ON)
    private boolean isAlarmOn;

    @Column(name = Keys.DRUG_TYPE)
    private String drugType;

    @Column(name = Profile.TABLE)
    private Profile profile;

    @Column(name = Destination.TABLE)
    private Destination destination;

    private List<Alarm> alarms;

    private List<Drug> drugs;

    public static final class Keys {
        public static final String DISEASE_LIST_NAME = "DiseaseListName";
        public static final String FRIENDLY_NAME = "FriendlyName";
        public static final String GROUP_TEXT = "GroupText";
        public static final String FIND_OUT_WHY = "FindOutWhyHtml";
        public static final String DISEASE_PAGE_URL = "DiseasePageUrl";
        public static final String DRUG_SELECTED = "DrugSelected";
        public static final String IS_ALARM_ON = "IsAlarmOn";
        public static final String DRUG_TYPE = "DrugType";
    }


    public String getDiseaseListName() {
        return diseaseListName;
    }

    public void setDiseaseListName(String diseaseListName) {
        this.diseaseListName = diseaseListName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }

    public String getFindOutWhyHtml() {
        return findOutWhyHtml;
    }

    public void setFindOutWhyHtml(String findOutWhyHtml) {
        this.findOutWhyHtml = findOutWhyHtml;
    }

    public String getDiseasePageUrl() {
        return diseasePageUrl;
    }

    public void setDiseasePageUrl(String diseasePageUrl) {
        this.diseasePageUrl = diseasePageUrl;
    }

    public List<Drug> getDrugs() {
        if (drugs == null) {
            drugs = new Select().from(Drug.class).where(Disease.TABLE + " =?", this.getId()).execute();
        }
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    public Trip getTrip() {
        if (trip != null && trip.getName() != null) {
            trip = DatabaseQueries.getTrip(this.trip.getId());
        }
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    /**
     * Get alarms by the related profile drug NOT the disease
     *
     * @return
     */
    public List<Alarm> getAlarms() {
        if (alarms == null) {
            if (selectedDrug != null && selectedDrug.getId() != null)
                alarms = new Select().from(Alarm.class).where(Drug.TABLE + " =?", selectedDrug.getId()).execute();
        }
        return alarms;
    }

    public void setAlarm(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    public Drug getSelectedDrug() {
        if (selectedDrug != null && selectedDrug.getId() != null) {
            selectedDrug = new Select().from(Drug.class).where("Id =?", selectedDrug.getId()).executeSingle();
        }
        return selectedDrug;
    }

    public void setSelectedDrug(Drug selectedDrug) {
        this.selectedDrug = selectedDrug;
    }

    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean isAlarmOn) {
        this.isAlarmOn = isAlarmOn;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
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

    /**
     * Use this method to delete the diseases, this way for the profile drugs and medicines are deleted right
     */
    public void deleteDisease() {
        if (TextUtils.equals(drugType, TYPE_MEDICINE)) {
            delete();
        } else if (selectedDrug == null) {
            delete();
        }
    }

    public Destination getDestination() {
        if (destination == null) {
            destination = new Select().from(Destination.class).where("Id =?", destination.getId()).executeSingle();
        }
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "friendlyName='" + friendlyName + '\'' +
                " 'mId='" + getId() +
                " 'selectedDrug='" + (selectedDrug != null ? selectedDrug.getId() : "null") + '}';
    }
}