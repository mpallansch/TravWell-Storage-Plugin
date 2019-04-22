package gov.cdc.oid.ncezid.travwell.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import gov.cdc.oid.ncezid.travwell.TravelHealthApplication;
import gov.cdc.oid.ncezid.travwell.model.Profile;
import com.duethealth.lib.component.utils.DhUtils;

/**
 * Created by parker on 12/12/13.
 */
public class PreferenceUtils {

    public static SharedPreferences prefs;
    public static final String NAME = TravelHealthApplication.SHARED_PREF_NAME;
    public static boolean useDefault;

    public static final String TIME_LAST_UPDATED_DESTINATIONS = "TimeLastUpdatedDestinations";
    public static final String TIME_LAST_UPDATED_DISEASES = "TimeLastUpdatedDiseases";
    public static final String PROFILE = "Profile";
    public static final String HOME_SCREEN_SETTING = "HomeScreenSetting";
    public static final String REMINDER_SETTING_TO_DO = "ReminderSettingToDo";
    public static final String REMINDER_SETTING_VACCINE = "ReminderSettingVaccine";
    public static final String REMINDER_SETTING_MEDICINE = "ReminderSettingMedicine";
    public static final String TERMS_ACCEPTED = "TermsAccepted";

    public static final String ANALYTICS_APP_OPEN = "AppOpenCount";

    public static final void setTimeLastUpdatedDestinations(Context context, long timeLastSynced) {
        buildSharedPreferences(context);
        commit(prefs.edit().putLong(TIME_LAST_UPDATED_DESTINATIONS, timeLastSynced));
    }

    public static final Long getTimeLastUpdatedDestinations(Context context) {
        buildSharedPreferences(context);
        return prefs.getLong(TIME_LAST_UPDATED_DESTINATIONS, 0L);
    }

    public static final void setTimeLastUpdatedDiseases(Context context, long timeLastSynced) {
        buildSharedPreferences(context);
        commit(prefs.edit().putLong(TIME_LAST_UPDATED_DISEASES, timeLastSynced));
    }

    public static final Long getTimeLastUpdatedDiseases(Context context) {
        buildSharedPreferences(context);
        return prefs.getLong(TIME_LAST_UPDATED_DISEASES, 0L);
    }


    public static final void setProfile(Context context, long profileId) {
        buildSharedPreferences(context);
        commit(prefs.edit().putLong(PROFILE, profileId));
    }

    public static final Long getProfile(Context context) {
        buildSharedPreferences(context);
        return prefs.getLong(PROFILE, 0);
    }

    public static final void setHomeScreenSetting(Context context, int homeScreen) {
        buildSharedPreferences(context);
        commit(prefs.edit().putInt(HOME_SCREEN_SETTING, homeScreen));
    }


    public static final int getHomeScreenSetting(Context context) {
        buildSharedPreferences(context);
        return prefs.getInt(HOME_SCREEN_SETTING, Profile.HOME_APP);
    }

    public static final void setReminderToDoSetting(Context context, boolean isOn) {
        buildSharedPreferences(context);
        commit(prefs.edit().putBoolean(REMINDER_SETTING_TO_DO, isOn));
    }

    public static final boolean getReminderToDoSetting(Context context) {
        buildSharedPreferences(context);
        return prefs.getBoolean(REMINDER_SETTING_TO_DO, true);
    }

    public static final void setReminderVaccineSetting(Context context, boolean isOn) {
        buildSharedPreferences(context);
        commit(prefs.edit().putBoolean(REMINDER_SETTING_VACCINE, isOn));
    }

    public static final boolean getReminderVaccineSetting(Context context) {
        buildSharedPreferences(context);
        return prefs.getBoolean(REMINDER_SETTING_VACCINE, true);
    }


    public static final void setReminderMedicineSetting(Context context, boolean isOn) {
        buildSharedPreferences(context);
        commit(prefs.edit().putBoolean(REMINDER_SETTING_MEDICINE, isOn));
    }

    public static final boolean getReminderMedicineSetting(Context context) {
        buildSharedPreferences(context);
        return prefs.getBoolean(REMINDER_SETTING_MEDICINE, true);
    }

    public static final void setTermsAccepted(Context context, boolean isAccepted) {
        buildSharedPreferences(context);
        commit(prefs.edit().putBoolean(TERMS_ACCEPTED, isAccepted));
    }

    public static final boolean getTermsAccepted(Context context) {
        buildSharedPreferences(context);
        return prefs.getBoolean(TERMS_ACCEPTED, false);
    }

    public static final void setAppOpenCount(Context context, int count) {
        buildSharedPreferences(context);
        commit(prefs.edit().putInt(ANALYTICS_APP_OPEN, count));
    }

    public static final int getAppOpenCount(Context context) {
        buildSharedPreferences(context);
        return prefs.getInt(ANALYTICS_APP_OPEN, 0);
    }

    /**
     * Builds the {@link #prefs} object with the name {@link #NAME}.
     *
     * @param context
     * @return
     */
    public static SharedPreferences buildSharedPreferences(Context context) {
        if (useDefault) {
            return buildDefaultSharedPreferences(context);
        }
        if (prefs == null) {
            prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return prefs;
    }

    public static SharedPreferences buildDefaultSharedPreferences(Context context) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
        useDefault = true;
        return prefs;
    }

    /**
     * Commits the given SharedPreferences.Editor. If the device is greater than
     * API 11
     *
     * @param editor
     * @return Returns true if applied. It will only return false if
     */
    @SuppressLint("NewApi")
    public static boolean commit(SharedPreferences.Editor editor) {
        if (DhUtils.isGingerBreadOrGreater()) {
            editor.apply();
            return true;
        } else {
            return editor.commit();
        }
    }


    public static final void clear(Context context) {
        buildSharedPreferences(context);
        prefs.edit().clear();
    }
}
