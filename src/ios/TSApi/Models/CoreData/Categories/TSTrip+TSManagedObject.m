//
//  TSTrip+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 2/10/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import "TSTrip+TSManagedObject.h"
#import "TSDisease+TSManagedObject.h"
#import "TSDestination+TSManagedObject.h"
#import "TSAdviceItem+TSManagedObject.h"
#import "TSPackingList.h"
#import "TSPackingListSuperGroup.h"
#import "TSPackingListGroup.h"
#import "TSPackingListItem+TSManagedObject.h"
#import "TSUser+TSManagedObject.h"
#import "TSToDoItem+TSManagedObject.h"
#import "EXTSynthesize.h"

@interface TSTrip(EXTProperties)

@property(nonatomic, strong) NSMutableSet* completedToDoIds;
@property(nonatomic, strong) NSMutableSet* completedPackingListItemIds;
@property(nonatomic, strong) NSMutableSet* deletedToDoIds;
@property(nonatomic, strong) NSMutableSet* deletedPackingListIds;

@end

@implementation TSTrip(EXTProperties)

@synthesizeAssociation(TSTrip, completedToDoIds);
@synthesizeAssociation(TSTrip, completedPackingListItemIds);
@synthesizeAssociation(TSTrip, deletedToDoIds);
@synthesizeAssociation(TSTrip, deletedPackingListIds);

@end

@implementation TSTrip (TSManagedObject)

- (NSString*)destinationNameList {
    NSMutableString* names = [NSMutableString string];
    // Extra space on the end should be okay here
    for (TSDestination* d in self.destinations.array) {
        [names appendFormat:@"%@ ", d.nameList];
    }
    return names;
}

- (NSString*)description {
    return [self destinationNameList];
}

- (void)deleteTrip {
    NSMutableArray* itemsToRemove = [NSMutableArray array];
    for (TSAdviceItem* item in self.adviceItems) {
        if (item.user) {
            [itemsToRemove addObject:item];
        }
    }

    for (TSAdviceItem* item in itemsToRemove) {
        [item removeTripsObject:self];
    }

    [self MR_deleteEntity];
    [[NSManagedObjectContext MR_defaultContext] MR_saveToPersistentStoreAndWait];
}

#pragma mark - Handling global to custom packing list and to do items

#define INIT_SET_IF_NIL(theset) if (!theset) { theset = [NSMutableSet set]; }

- (void)p_setupGlobalHandling {
    self.completedToDoIds = [NSKeyedUnarchiver unarchiveObjectWithData:self.completedProfileToDoIds];
    self.completedPackingListItemIds = [NSKeyedUnarchiver unarchiveObjectWithData:self.completedProfilePackingListIds];
    self.deletedToDoIds = [NSKeyedUnarchiver unarchiveObjectWithData:self.deletedProfileToDoIds];
    self.deletedPackingListIds = [NSKeyedUnarchiver unarchiveObjectWithData:self.deletedProfilePackingListIds];

    INIT_SET_IF_NIL(self.completedToDoIds);
    INIT_SET_IF_NIL(self.completedPackingListItemIds);
    INIT_SET_IF_NIL(self.deletedToDoIds);
    INIT_SET_IF_NIL(self.deletedPackingListIds);
    
    DLog(@"Done setting up global handling:\nCompletedToDoIds: %@\nCompleted Packing List Ids: %@\n: Deleted ToDos: %@\nDeleted Packing List: %@", self.completedToDoIds,
         self.completedPackingListItemIds,
         self.deletedToDoIds,
         self.deletedPackingListIds);
}

- (void)p_saveChanges {
    self.completedProfileToDoIds = [NSKeyedArchiver archivedDataWithRootObject:self.completedToDoIds];
    self.completedProfilePackingListIds = [NSKeyedArchiver archivedDataWithRootObject:self.completedPackingListItemIds];
    self.deletedProfileToDoIds = [NSKeyedArchiver archivedDataWithRootObject:self.deletedToDoIds];
    self.deletedProfilePackingListIds = [NSKeyedArchiver archivedDataWithRootObject:self.deletedPackingListIds];
}

- (BOOL)containsProfileToDo:(TSToDoItem*)todo {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    return ![self.deletedToDoIds containsObject:todo.uniqueId];
}

- (BOOL)containsProfilePackingListItem:(TSPackingListItem*)packingListItem {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    return ![self.deletedPackingListIds containsObject:packingListItem.associatedToDo.uniqueId];
}

- (void)removeProfileToDo:(TSToDoItem*)todo {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    [self.deletedToDoIds addObject:todo.uniqueId];
    [self p_saveChanges];
}

- (void)removeProfilePackingListItem:(TSPackingListItem*)packingListItem {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    [self.deletedPackingListIds addObject:packingListItem.associatedToDo.uniqueId];
    [self p_saveChanges];
}

- (BOOL)hasCompletedToDo:(TSToDoItem*)todo {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    return [self.completedToDoIds containsObject:todo.uniqueId];
}

- (BOOL)hasCompletedPackingListItem:(TSPackingListItem*)packingListItem {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    return [self.completedPackingListItemIds containsObject:packingListItem.associatedToDo.uniqueId];
}

- (void)setCompleted:(BOOL)completed forToDo:(TSToDoItem*)todo {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    if (completed) {
        [self.completedToDoIds addObject:todo.uniqueId];
    } else {
        [self.completedToDoIds removeObject:todo.uniqueId];
    }
    [self p_saveChanges];
}

- (void)setCompleted:(BOOL)completed forPackingListItem:(TSPackingListItem*)packingListItem {
    if (!self.completedToDoIds) { [self p_setupGlobalHandling]; }
    if (completed) {
        [self.completedPackingListItemIds addObject:packingListItem.associatedToDo.uniqueId];
    } else {
        [self.completedPackingListItemIds removeObject:packingListItem.associatedToDo.uniqueId];
    }
    [self p_saveChanges];
}

+ (instancetype)tripWithDestinations:(NSArray*)destinations andStartDate:(NSDate*)startDate andEndDate:(NSDate*)endDate {
    TSTrip* trip = [TSTrip MR_createEntity];
    trip.startDate = startDate;
    trip.endDate = endDate;
    trip.destinations = [NSOrderedSet orderedSetWithArray:destinations];
    trip.toDoItems = [NSOrderedSet orderedSetWithArray:[TSToDoItem initialToDoItems]];

    NSMutableArray* diseases = [NSMutableArray array];
    // Pull out all diseases
    for (TSDestination* destination in trip.destinations) {
        for (TSDisease* disease in destination.diseases) {
            [diseases addObject:disease];
        }
    }
    // Create all TSAdviceItems for the trip
    for (TSDisease* disease in diseases) {
        TSAdviceItem* adviceItem = [TSAdviceItem MR_createEntity];
        adviceItem.name = disease.diseaseListName;
        adviceItem.disease = disease;
        [adviceItem addTripsObject:trip];
        [kUser updateAdviceItemIfNecessary:adviceItem];
    }

    // Cleanup duplicate packing list items.
    NSMutableDictionary* packingList = [NSMutableDictionary dictionary];
    NSMutableDictionary* packingListItems = [NSMutableDictionary dictionary];
    for (TSDestination* destination in destinations) {
        for (TSPackingListSuperGroup* sgroup in destination.packingList.superGroups) {
            if (!packingList[sgroup.superGroupText]) {
                packingList[sgroup.superGroupText] = [NSMutableSet set];
            }
            for (TSPackingListGroup* group in sgroup.groups) {
                for (TSPackingListItem* item in group.items) {
                    item.category = sgroup.superGroupText;
                    [packingList[sgroup.superGroupText] addObject:item.name];
                    packingListItems[item.name] = item;
                    DLog(@"Adding item: %@ to category %@", item.name, item.category);
                }
            }
        }
    }

    // Phew, that was terrible - OK, now all the packing list items are organized into this single dictionary.
    // The points of that was to get rid of the duplicate super groups we might see in various destinations. So,
    // now we just need to point all the items that survived at this particular trip.
    DLog(@"Packing ist is organized into this: %@", packingList);
    for (NSString* key in packingList.allKeys) {
        for (NSString* packingListItemName in packingList[key]) {
            [packingListItems[packingListItemName] setTrip:trip];
        }
    }

    return trip;
}

@end
