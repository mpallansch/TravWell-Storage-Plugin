package gov.cdc.oid.ncezid.travwell.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by parker on 11/19/13.
 * Don't really use this class al all
 */
@Table(name = PackingGroup.TABLE)
public class PackingGroup extends Model {

    public PackingGroup() {
        super();
    }

    public static final String TABLE = "PackingGroup";

    public PackingGroup(int groupId, int sortOrder, String groupText, PackingSuperGroup packingSuperGroup, Trip trip, boolean isTodo) {
        this.groupId = groupId;
        this.sortOrder = sortOrder;
        this.groupText = groupText;
        this.packingSuperGroup = packingSuperGroup;
        this.trip = trip;
        this.isTodo = isTodo;
    }

    public void updatePackingGroup(PackingGroup packingGroup) {
        this.groupId = packingGroup.groupId;
        this.sortOrder = packingGroup.sortOrder;
        this.groupText = packingGroup.groupText;
        this.packingSuperGroup = packingGroup.packingSuperGroup;
        this.trip = packingGroup.trip;
        this.save();
    }

    @Column(name = Keys.GROUP_ID)
    public int groupId;

    @Column(name = Keys.SORT_ORDER)
    public int sortOrder;

    @Column(name = Keys.GROUP_TEXT)
    public String groupText;

    @Column(name = PackingSuperGroup.TABLE)
    public PackingSuperGroup packingSuperGroup;

    @Column(name = Trip.TABLE)
    public Trip trip;

    @Column(name = Keys.IS_TODO)
    public boolean isTodo;

    public List<PackingItem> packingItems;

    public static final class Keys {
        public static final String GROUP_ID = "GroupId";
        public static final String SORT_ORDER = "SortOrder";
        public static final String GROUP_TEXT = "GroupText";
        public static final String IS_TODO = "IsTodo";
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }

    public PackingSuperGroup getPackingSuperGroup() {
        if (packingSuperGroup != null) {
            packingSuperGroup = new Select().from(PackingSuperGroup.class).where("Id =?", this.packingSuperGroup.getId()).executeSingle();
        }
        return packingSuperGroup;
    }

    public void setPackingSuperGroup(PackingSuperGroup packingSuperGroup) {
        this.packingSuperGroup = packingSuperGroup;
    }

    public List<PackingItem> getPackingItems() {
        if (packingItems == null) {
            packingItems = new Select().from(PackingItem.class).where(PackingGroup.TABLE + " =?", this.getId()).execute();
        }
        return packingItems;
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
