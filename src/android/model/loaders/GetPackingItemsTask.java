package gov.cdc.oid.ncezid.travwell.model.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import gov.cdc.oid.ncezid.travwell.R;
import gov.cdc.oid.ncezid.travwell.model.DatabaseQueries;
import gov.cdc.oid.ncezid.travwell.model.PackingItem;
import gov.cdc.oid.ncezid.travwell.model.PackingSuperGroup;
import gov.cdc.oid.ncezid.travwell.model.Profile;
import gov.cdc.oid.ncezid.travwell.model.Trip;

/**
 * Loader Task to get all of the PackingItems (To Do and Packing List)
 */
public class GetPackingItemsTask extends AsyncTaskLoader<List<PackingItem>> {

    private long tripId;
    private int type;
    HashSet<Long> deletedMedicineIds = new HashSet<Long>();

    public GetPackingItemsTask(Context context, long tripId, int type) {
        super(context);
        this.tripId = tripId;
        this.type = type;
    }

    @Override
    public List<PackingItem> loadInBackground() {
        List<PackingItem> finalPackingItems = new ArrayList<PackingItem>();
        boolean isTodo = true;
        Trip trip = DatabaseQueries.getTrip(tripId);
        if (trip == null) {
            return finalPackingItems;
        }
        if (type == 3) {
            isTodo = false;
            deletedMedicineIds = trip.getDeletedPackingListMedicines();
        } else if (type == 1) {
            isTodo = true;
            deletedMedicineIds = trip.getDeletedToDoMedicines();
        }

        Profile profile = DatabaseQueries.getProfile(getContext());
        List<PackingItem> profileMedicines = null;
        if (profile != null) {
            profileMedicines = profile.getProfileMedicines();
        }
        List<PackingSuperGroup> packingSuperGroups = DatabaseQueries.getPackingSupperGroups(tripId, isTodo);
        // sort the packing items
        sortPackingSupperGroups(packingSuperGroups);
        for (PackingSuperGroup packingSuperGroup : packingSuperGroups) {
            // get all of the packing items for the packing super group
            // (removed using the packing group, has no use right now)
            //packingSuperGroup.setPackingItems(null);
            List<PackingItem> packingItems = packingSuperGroup.getPackingItems();
            // only get items with  packing items
            if (isTodo) {
                if (TextUtils.equals(packingSuperGroup.getSuperGroupText(), "During Trip")) {
                    for (PackingItem medicineItems : profileMedicines) {
                        // set the right sort orders (-1 are Headers)
                        medicineItems.setSortOrder(0);
                        medicineItems.setTodo(isTodo);
                        // set the display name to Take MEDICINE
                        medicineItems.setDisplayName("Take " +
                                medicineItems.getDisplayName());
                        medicineItems.setPackingSupperGroupOrder(packingSuperGroup.getSortOrder());
                        if (!deletedMedicineIds.contains(medicineItems.getId())) {
                            packingItems.add(medicineItems);
                        }
                    }
                }
            } else {
                if (TextUtils.equals(packingSuperGroup.getSuperGroupText(), "Prescription medicines")) {
                    for (PackingItem medicineItems : profileMedicines) {
                        // set the right sort orders (-1 are Headers)
                        medicineItems.setSortOrder(0);
                        medicineItems.setTodo(isTodo);
                        medicineItems.setPackingSupperGroupOrder(packingSuperGroup.getSortOrder());
                        if (!deletedMedicineIds.contains(medicineItems.getId())) {
                            packingItems.add(medicineItems);
                        }
                    }
                }
            }

            if (packingItems.size() > 0) {
                // sort the items
                sortPackingItems(packingItems);
                // add the header to the top of the list
                // create the header (header == -1)
                PackingItem header = new PackingItem(PackingItem.HEADER, packingSuperGroup.getSuperGroupText(),
                        null, null, -1, null, null, null, false, false, packingSuperGroup.getSortOrder());
                // There is only one packing group for each super group
                packingItems.add(0, header);
                // add to final packing item list
                finalPackingItems.addAll(packingItems);
            }
        }

        return finalPackingItems;
    }

    /**
     * Needed for re-added mass deleted items by multi select
     *
     * @param items
     */

    public static void sortPackingItems(List<PackingItem> items) {
        Collections.sort(items, new Comparator<PackingItem>() {
            @Override
            public int compare(PackingItem g1, PackingItem g2) {
                return g1.getSortOrder() - g2.getSortOrder();
            }
        });
    }

    /**
     * Need for re-adding mass deleted items by multi select
     *
     * @param items
     */
    public static void sortPackingItemsBySupperGroupSortOrder(List<PackingItem> items) {
        Collections.sort(items, new Comparator<PackingItem>() {
            @Override
            public int compare(PackingItem g1, PackingItem g2) {
                return g1.getPackingSupperGroupOrder() - g2.getPackingSupperGroupOrder();
            }
        });
    }

    private void sortPackingSupperGroups(List<PackingSuperGroup> items) {
        Collections.sort(items, new Comparator<PackingSuperGroup>() {
            @Override
            public int compare(PackingSuperGroup g1, PackingSuperGroup g2) {
                return g1.getSortOrder() - g2.getSortOrder();
            }
        });
    }
}