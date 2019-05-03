//
//  TSPackingListItem+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 7/18/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSPackingListItem+TSManagedObject.h"
#import "TSPackingList.h"
#import "TSPackingListGroup.h"
#import "TSPackingListSuperGroup.h"
#import "TSTrip+TSManagedObject.h"
#import "EXTSynthesize.h"
#import "TSToDoItem+TSManagedObject.h"

@implementation TSPackingListItem (EXTProperties)

@synthesizeAssociation(TSPackingListItem, associatedToDo);
@synthesizeAssociation(TSPackingListItem, associatedTrip);

@end

@implementation TSPackingListItem (TSManagedObject)

@dynamic name;
@dynamic trip;
@dynamic completed;

+ (NSArray*)itemsForTrip:(TSTrip*)trip {
    NSMutableArray* thisTripsItems = [NSMutableArray arrayWithArray:trip.packingListItems.array];
    // Create some packing list items on the fly which do not get saved just to display and relate back to a user medication
    NSArray* userMedications = kUser.medications;
    [userMedications enumerateObjectsUsingBlock:^(TSToDoItem* todo, NSUInteger idx, BOOL *stop) {
        TSPackingListItem* packingListItem = [TSPackingListItem MR_createEntity];
        packingListItem.name = todo.name;
        packingListItem.category = @"Prescription medicines";
        packingListItem.userModifiedCategory = @YES;
        packingListItem.associatedToDo = todo;
        packingListItem.associatedTrip = trip;
        
        if ([trip containsProfilePackingListItem:packingListItem]) {
            [thisTripsItems addObject:packingListItem];
        }
    }];

    return [NSArray arrayWithArray:thisTripsItems];
}

+ (NSString*)defaultNewItemName {
    return NSLocalizedString(@"New 'Packing List' item",
                             @"Default name for a newly created 'Packing List' item");
}

+ (id<TSTripItem>)defaultNewItem {
    TSPackingListItem* item = [TSPackingListItem MR_createEntity];
    item.category = @"Misc";
    item.userModifiedCategory = @YES;
    item.name = [TSPackingListItem defaultNewItemName];
    return item;
}

+ (NSArray*)orderedCategoriesForTrip:(TSTrip*)trip {

    NSMutableSet* setOfPreDefinedGroups = [NSMutableSet set];
    NSMutableOrderedSet* setOfUserCategories = [NSMutableOrderedSet orderedSet];
    for (TSPackingListItem* item in [self itemsForTrip:trip]) {

        /* This is a bit strange, but I couldn't come up with a good way to handle the arbitrary
         * categories coming back from the server being modified by the user. 
         */
        if (item.userModifiedCategory) {
            [setOfUserCategories addObject:item.category];
        } else {
            [setOfPreDefinedGroups addObject:item.group.superGroup];
        }
    }

    NSMutableArray* superGroups = [setOfPreDefinedGroups.allObjects mutableCopy];
    [superGroups sortUsingComparator:^NSComparisonResult(TSPackingListSuperGroup* g1, TSPackingListSuperGroup* g2) {
        DLog(@"Sort order g1: %@, g2: %@", g1.sortOrder, g2.sortOrder);
        return [g1.sortOrder compare:g2.sortOrder];
    }];

    DLog(@"Ordered set of user categories: %@", setOfUserCategories);
    // Begin with the user categories
    for (TSPackingListSuperGroup* sgroup in superGroups) {
        DLog(@"adding sgroup: %@", sgroup.superGroupText);
        [setOfUserCategories addObject:sgroup.superGroupText];
    }
    DLog(@"Ordered set of user categories: %@", setOfUserCategories);
    DLog(@"Array of user categories from set: %@", setOfUserCategories.array);
    return setOfUserCategories.array;
}

+ (NSString*)editItemViewControllerIdentifier {
    return @"packing_list_item_details";
}

+ (NSString*)emptyHeaderText {
    return @"Add items here to reminder youself what you have packed and what you have left to pack for your trip.";
}

#pragma mark - TripItem Protocol

- (void)deleteItemFromTrip:(TSTrip *)trip {
    // TODO: this is kind of a horrible way to handle this, think of something more sensible and _do that instead_
    // If this item has a specific trip it can be deleted normally, also if no trip is passed in this must
    // be a profile todo and should just be deleted entirely

    if (self.trip || !trip) {
        [self MR_deleteEntity];
        [[NSManagedObjectContext MR_defaultContext] MR_saveToPersistentStoreAndWait];
    } else {
        // If not it's a profile toDo and must be just removed from this trip
        DLog(@"Removing item with uniqueId: %@", self.associatedToDo.uniqueId);
        [trip removeProfilePackingListItem:self];
    }
}

#pragma mark - Instance methods

- (NSNumber*)completed {
    if (self.associatedTrip) {
        return @([self.associatedTrip hasCompletedPackingListItem:self]);
    }

    return [self primitiveValueForKey:@"completed"];
}

- (void)toggleCompleted {
    [self setCompleted:[NSNumber numberWithBool:!([[self completed] boolValue])]];
    [self.associatedTrip setCompleted:![self.associatedTrip hasCompletedPackingListItem:self] forPackingListItem:self];
}

#pragma mark - NSObject

- (NSString*)description {
    return self.name;
}

@end
