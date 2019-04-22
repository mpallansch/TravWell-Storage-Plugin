package gov.cdc.oid.ncezid.travwell.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by parker on 11/19/13.
 */
@Table(name = PackingSuperGroup.TABLE)
public class PackingSuperGroup extends Model {

    public static final int USER_PACKING_SUPER_GROUP = -1;
    public static final int MIS_PACKING_SUPER_GROUP = -2;
    public static final int BEFORE_PACKING_SUPER_GROUP = 1;
    public static final int DURING_PACKING_SUPER_GROUP = 2;
    public static final int AFTER_PACKING_SUPER_GROUP = 3;

    public static final String TABLE = "PackingSuperGroup";

    public PackingSuperGroup(int superGroupId, int sortOrder, String superGroupText, Trip trip, boolean isTodo) {
        this.superGroupId = superGroupId;
        this.sortOrder = sortOrder;
        this.superGroupText = superGroupText;
        this.trip = trip;
        this.isTodo = isTodo;
    }

    public PackingSuperGroup() {
        super();
    }

    public void updatePackingSuperGroup(PackingSuperGroup packingSuperGroup) {
        this.superGroupId = packingSuperGroup.superGroupId;
        this.superGroupId = packingSuperGroup.superGroupId;
        this.sortOrder = packingSuperGroup.sortOrder;
        this.superGroupText = packingSuperGroup.superGroupText;
        this.trip = packingSuperGroup.trip;
        this.save();
    }

    @Column(name = Keys.SUPER_GROUP_ID)
    private int superGroupId;

    @Column(name = Keys.SORT_ORDER)
    private int sortOrder;

    @Column(name = Keys.SUPER_GROUP_TEXT)
    private String superGroupText;

    @Column(name = Trip.TABLE)
    private Trip trip;

    @Column(name = Keys.IS_TODO)
    boolean isTodo;

    private List<PackingGroup> packingGroups;

    private List<PackingItem> packingItems;


    public static final class Keys {
        public static final String SUPER_GROUP_ID = "SuperGroupId";
        public static final String SORT_ORDER = "SortOrder";
        public static final String SUPER_GROUP_TEXT = "SuperGroupText";
        public static final String IS_TODO = "IsTodo";
    }

    public int getSuperGroupId() {
        return superGroupId;
    }

    public void setSuperGroupId(int superGroupId) {
        this.superGroupId = superGroupId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSuperGroupText() {
        return superGroupText;
    }

    public void setSuperGroupText(String superGroupText) {
        this.superGroupText = superGroupText;
    }

    public List<PackingGroup> getPackingGroups() {
        if (packingGroups == null) {
            packingGroups = new Select().from(PackingGroup.class).where(PackingSuperGroup.TABLE + "=?", this.getId()).execute();
        }
        return packingGroups;
    }

    public List<PackingItem> getPackingItems() {
        if (packingItems == null) {
            packingItems = new Select().from(PackingItem.class).where(PackingSuperGroup.TABLE + "=?", this.getId()).execute();
        }
        return packingItems;
    }

    public void setPackingItems(List<PackingItem> packingItems) {
        this.packingItems = packingItems;
    }


    public Trip getTrip() {
        return DatabaseQueries.getTrip(this.trip.getId());
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public boolean isTodo() {
        return isTodo;
    }

    public void setTodo(boolean isTodo) {
        this.isTodo = isTodo;
    }
}
