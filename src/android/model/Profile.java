package gov.cdc.oid.ncezid.travwell.model;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import gov.cdc.oid.ncezid.travwell.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parker on 12/12/13.
 */
@Table(name = Profile.TABLE)
public class Profile extends Model {

    public static final String TABLE = "Profile";

    public static final int HOME_APP = 0;
    public static final int HOME_TRIP = 1;

    public Profile(String firstName, String lastName, long lastUpdatedDestinations, int homeScreen, boolean remindersToDo, boolean remindersVaccine, boolean remindersMedicine, long lastUpdatedDisease) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdatedDestinations = lastUpdatedDestinations;
        this.homeScreen = homeScreen;
        this.remindersToDo = remindersToDo;
        this.remindersVaccine = remindersVaccine;
        this.remindersMedicine = remindersMedicine;
        this.lastUpdatedDisease = lastUpdatedDisease;
    }

    public Profile() {
        super();
    }

    @Column(name = Keys.FIRST_NAME)
    private String firstName;
    @Column(name = Keys.LAST_NAME)
    private String lastName;
    @Column(name = Keys.LAST_UPDATED_DESTINATIONS)
    private long lastUpdatedDestinations;
    @Column(name = Keys.LAST_UPDATED_DISEASE)
    private long lastUpdatedDisease;
    @Column(name = Keys.HOME_SCREEN)
    private int homeScreen;
    @Column(name = Keys.REMINDERS_TO_DO)
    private boolean remindersToDo;
    @Column(name = Keys.REMINDERS_VACCINE)
    private boolean remindersVaccine;
    @Column(name = Keys.REMINDERS_MEDICINE)
    private boolean remindersMedicine;

    private List<Drug> vaccineDrugs;
    private List<Disease> malarialMedicineDiseases;
    private List<PackingItem> profileMedicines;

    public static class Keys {
        public static final String FIRST_NAME = "FirstName";
        public static final String LAST_NAME = "LastName";
        public static final String LAST_UPDATED_DESTINATIONS = "LastUpdatedDestinations";
        public static final String LAST_UPDATED_DISEASE = "LastUpdatedDisease";
        public static final String HOME_SCREEN = "HomeScreen";
        public static final String REMINDERS_TO_DO = "RemindersToDo";
        public static final String REMINDERS_VACCINE = "RemindersVaccine";
        public static final String REMINDERS_MEDICINE = "RemindersMedicine";
    }


    /**
     * This is needed for getting profile vaccine drugs in the profile screen because the profile
     * is not nulling out so it returns the same info eventhough it changed
     *
     * @param vaccineDrugs
     */
    public void setVaccineDrugs(List<Drug> vaccineDrugs) {
        this.vaccineDrugs = vaccineDrugs;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getLastUpdatedDestinations() {
        return lastUpdatedDestinations;
    }

    public void setLastUpdatedDestinations(long lastUpdatedDestinations) {
        this.lastUpdatedDestinations = lastUpdatedDestinations;
    }

    public int getHomeScreen() {
        return homeScreen;
    }

    public void setHomeScreen(int homeScreen) {
        this.homeScreen = homeScreen;
    }

    public boolean isRemindersToDo() {
        return remindersToDo;
    }

    public void setRemindersToDo(boolean remindersToDo) {
        this.remindersToDo = remindersToDo;
    }

    public boolean isRemindersVaccine() {
        return remindersVaccine;
    }

    public void setRemindersVaccine(boolean remindersVaccine) {
        this.remindersVaccine = remindersVaccine;
    }

    public boolean isRemindersMedicine() {
        return remindersMedicine;
    }

    public void setRemindersMedicine(boolean remindersMedicine) {
        this.remindersMedicine = remindersMedicine;
    }

    public long getLastUpdatedDisease() {
        return lastUpdatedDisease;
    }

    public void setLastUpdatedDisease(long lastUpdatedDisease) {
        this.lastUpdatedDisease = lastUpdatedDisease;
    }

    /**
     * These are the saved Drugs that will be auto populated into the disease fields as selected,
     * not related to trips or but related to profile Vaccine diseases
     * <p/>
     * Profile Vaccines == Trip IS NULL, Type = Vaccine
     *
     * @return
     */
    public List<Drug> getVaccineDrugs() {
        if (vaccineDrugs == null) {
            List<Disease> diseases = DatabaseQueries.getProfileVaccinesDisease(this.getId());
            List<Drug> drugDiseases = new ArrayList<Drug>();
            for (Disease disease : diseases) {
                if (disease.getSelectedDrug() != null) {
                    drugDiseases.add(disease.getSelectedDrug());
                    if (disease.getSelectedDrug() != null) {
                        disease.getSelectedDrug().getAlarms();
                    }
                }
            }
            vaccineDrugs = drugDiseases;
            //vaccineDrugs = new Select().from(Drug.class).where(Profile.TABLE + "=" + this.getId() + " AND " + Drug.Keys.DRUG_TYPE + "='" + Disease.TYPE_VACCINE + "'" + " AND " + Trip.TABLE + " IS NULL").execute();
        }
        return vaccineDrugs;
    }

    /**
     * Get all of the selected Medicines (Malarial) for each trip for this profile, and the trip object
     * <p/>
     * Type == Disease, SelectedDrug == IS NOT NULL
     * <p/>
     * DO NOT USE just keeping it around just in case, used to get all of the malarial
     * and put them into the profile medicine section
     *
     * @return
     */
    @Deprecated
    public List<Disease> getMalarialMedicineDiseases() {
        if (malarialMedicineDiseases == null) {
            malarialMedicineDiseases = new Select().from(Disease.class).where(Profile.TABLE + "=" + this.getId() + " AND " + Disease.Keys.DRUG_TYPE + "='" + Disease.TYPE_MEDICINE + "'" + " AND " + Disease.Keys.DRUG_SELECTED + " IS NOT NULL").execute();
            for (Disease disease : malarialMedicineDiseases) {
                // get the trip for the disease so we can display the name of the medicine and trip in the view
                disease.getTrip();
            }
        }
        return malarialMedicineDiseases;
    }

    public List<PackingItem> getProfileMedicines() {
        if (profileMedicines == null) {
            profileMedicines = new Select().from(PackingItem.class).where(Profile.TABLE + "=" + this.getId() + " AND " + PackingItem.Keys.ITEM_ID + '=' + PackingItem.PROFILE_MEDICINE).execute();
        }
        return profileMedicines;
    }

    /**
     * Save then sets in the shared preferences the fields,
     * profile timeLastUpdated, reminderSettings, homeScreenSettings
     *
     * @param context
     */
    public void saveSetPreferences(Context context) {
        this.save();
        PreferenceUtils.setProfile(context, this.getId());
        setPreferences(context);
    }

    public void setPreferences(Context context) {
        PreferenceUtils.setTimeLastUpdatedDestinations(context, this.lastUpdatedDestinations);
        PreferenceUtils.setTimeLastUpdatedDiseases(context, this.lastUpdatedDisease);
        PreferenceUtils.setHomeScreenSetting(context, this.homeScreen);
        PreferenceUtils.setReminderToDoSetting(context, this.remindersToDo);
        PreferenceUtils.setReminderVaccineSetting(context, this.remindersVaccine);
        PreferenceUtils.setReminderMedicineSetting(context, this.remindersMedicine);
    }
}