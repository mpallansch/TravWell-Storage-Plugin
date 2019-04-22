package gov.cdc.oid.ncezid.travwell.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by parker on 11/19/13.
 */
@Table(name = Destination.TABLE)
public class Destination extends Model implements Parcelable {

    public Destination(String letter, String nameFriendly, String nameList, String nameYellowFeverMalariaTable,
                       String nameOfficial, String namePlugin, String webLink, String isoA2, boolean isAlias, String
                               parentName, String flagUrlSmall, String emergencyNumbers, Trip trip) {
        this.letter = letter;
        this.nameFriendly = nameFriendly;
        this.nameList = nameList;
        this.nameYellowFeverMalariaTable = nameYellowFeverMalariaTable;
        this.nameOfficial = nameOfficial;
        this.namePlugin = namePlugin;
        this.webLink = webLink;
        this.isoA2 = isoA2;
        this.isAlias = isAlias;
        this.parentName = parentName;
        this.flagUrlSmall = flagUrlSmall;
        this.emergencyNumbers = emergencyNumbers;
        this.trip = trip;
    }

    public Destination() {
        super();
    }

    public void updateDestination(Destination updatedDestination) {
        this.letter = updatedDestination.letter;
        this.nameFriendly = updatedDestination.nameFriendly;
        this.nameList = updatedDestination.nameList;
        this.nameYellowFeverMalariaTable = updatedDestination.nameYellowFeverMalariaTable;
        this.nameOfficial = updatedDestination.nameOfficial;
        this.namePlugin = updatedDestination.namePlugin;
        this.webLink = updatedDestination.webLink;
        this.isoA2 = updatedDestination.isoA2;
        this.isAlias = updatedDestination.isAlias;
        this.parentName = updatedDestination.parentName;
        this.flagUrlSmall = updatedDestination.flagUrlSmall;
        this.emergencyNumbers = updatedDestination.emergencyNumbers;
        this.trip = updatedDestination.trip;
        this.save();
    }

    public static final String TABLE = "Destination";

    @Column(name = Keys.LETTER)
    private String letter;

    @Column(name = Keys.NAME_FRIENDLY)
    private String nameFriendly;

    @Column(name = Keys.NAME_LIST)
    private String nameList;

    @Column(name = Keys.NAME_YELLOW_FEVER)
    private String nameYellowFeverMalariaTable;

    @Column(name = Keys.NAME_OFFICIAL)
    private String nameOfficial;

    @Column(name = Keys.NAME_PLUGIN)
    private String namePlugin;

    @Column(name = Keys.WEB_LINK)
    private String webLink;

    @Column(name = Keys.ISOA2)
    private String isoA2;

    @Column(name = Keys.IS_ALIAS)
    private boolean isAlias;

    @Column(name = Keys.PARENT_NAME)
    private String parentName;

    @Column(name = Keys.FLAG_URL)
    private String flagUrlSmall;

    @Column(name = Keys.EMERGENCY_NUMBERS)
    private String emergencyNumbers;

    @Column(name = Trip.TABLE)
    private Trip trip;

    private List<Disease> diseases;

    public static final class Keys {
        public static final String LETTER = "Letter";
        public static final String NAME_FRIENDLY = "NameFriendly";
        public static final String NAME_LIST = "NameList";
        public static final String NAME_YELLOW_FEVER = "NameYellowFeverMalariaTable";
        public static final String NAME_OFFICIAL = "NameOfficial";
        public static final String NAME_PLUGIN = "NamePlugin";
        public static final String WEB_LINK = "WebLink";
        public static final String ISOA2 = "ISOA2";
        public static final String IS_ALIAS = "IsAlias";
        public static final String PARENT_NAME = "ParentName";
        public static final String FLAG_URL = "FlagUrlSmall";
        public static final String EMERGENCY_NUMBERS = "EmergencyNumbers";
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getNameFriendly() {
        return nameFriendly;
    }

    public void setNameFriendly(String nameFriendly) {
        this.nameFriendly = nameFriendly;
    }

    public String getNameList() {
        return nameList;
    }

    public void setNameList(String nameList) {
        this.nameList = nameList;
    }

    public String getNameYellowFeverMalariaTable() {
        return nameYellowFeverMalariaTable;
    }

    public void setNameYellowFeverMalariaTable(String nameYellowFeverMalariaTable) {
        this.nameYellowFeverMalariaTable = nameYellowFeverMalariaTable;
    }

    public String getNameOfficial() {
        return nameOfficial;
    }

    public void setNameOfficial(String nameOfficial) {
        this.nameOfficial = nameOfficial;
    }

    public String getNamePlugin() {
        return namePlugin;
    }

    public void setNamePlugin(String namePlugin) {
        this.namePlugin = namePlugin;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getIsoA2() {
        return isoA2;
    }

    public void setIsoA2(String isoA2) {
        this.isoA2 = isoA2;
    }

    public boolean isAlias() {
        return isAlias;
    }

    public void setAlias(boolean isAlias) {
        this.isAlias = isAlias;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getFlagUrlSmall() {
        return flagUrlSmall;
    }

    public void setFlagUrlSmall(String flagUrlSmall) {
        this.flagUrlSmall = flagUrlSmall;
    }

    public String getEmergencyNumbers() {
        return emergencyNumbers;
    }

    public void setEmergencyNumbers(String emergencyNumbers) {
        this.emergencyNumbers = emergencyNumbers;
    }

    public Trip getTrip() {
        return new Select().from(Trip.class).where("Id =?", trip.getId()).executeSingle();
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<Disease> getDiseases() {
        if (diseases == null) {
            diseases = DatabaseQueries.getDiseasesByDestination(getId());
        }
        return diseases;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameFriendly);
        dest.writeString(this.nameList);
        dest.writeByte(isAlias ? (byte) 1 : (byte) 0);
        dest.writeString(this.flagUrlSmall);
        dest.writeString(this.parentName);
    }

    private Destination(Parcel in) {
        this.nameFriendly = in.readString();
        this.nameList = in.readString();
        this.isAlias = in.readByte() != 0;
        this.flagUrlSmall = in.readString();
        this.parentName = in.readString();
    }

    public static Parcelable.Creator<Destination> CREATOR = new Parcelable.Creator<Destination>() {
        public Destination createFromParcel(Parcel source) {
            return new Destination(source);
        }

        public Destination[] newArray(int size) {
            return new Destination[size];
        }
    };
}