//
//  TSToDoItem+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 7/18/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSToDoItem+TSManagedObject.h"
#import "DHNotificationGroup.h"
#import "EXTSynthesize.h"
#import "TSTrip+TSManagedObject.h"

@implementation TSToDoItem (EXTProperties)

@synthesizeAssociation(TSToDoItem, associatedTrip);

@end

@implementation TSToDoItem (TSManagedObject)

@dynamic completed;
@dynamic name;
@dynamic trip;

#define CATEGORY_MISC NSLocalizedString(@"Choose Category", @"Category name for miscellaneous to do items")
#define CATEGORY_BEFORE_TRIP NSLocalizedString(@"Before Trip", @"Category name for 'before trip' to do items")
#define CATEGORY_DURING_TRIP NSLocalizedString(@"During Trip", @"Category name for 'during trip' to do items")
#define CATEGORY_AFTER_TRIP NSLocalizedString(@"After Trip", @"Category name for 'after trip' to do items")

+ (NSArray*)itemsForTrip:(TSTrip*)trip {
    // Modify user medication todos for this particular trip. Wont carry over to other trips as they'll get modified when that
    // trip is loaded, here. None of the modified fields should affect the profile screen either.
    NSArray* profileTodos = kUser.medications;
    NSMutableArray* profileToDosInTrip = [NSMutableArray array];

    for (TSToDoItem* item in profileTodos) {
        if ([trip containsProfileToDo:item]) {
            item.associatedTrip = trip;
            [profileToDosInTrip addObject:item];
        }
    }

    return [trip.toDoItems.array arrayByAddingObjectsFromArray:profileToDosInTrip];
}

+ (NSString*)defaultNewItemName {
    return NSLocalizedString(@"New 'To Do' item",
                             @"Default name for a newly created 'To Do' item");
}

+ (void)addNewItemsWithNames:(NSArray*)names andCategory:(NSString*)category toArray:(NSMutableArray*)array {
    for (NSString* name in names) {
        TSToDoItem* item = [TSToDoItem MR_createEntity];
        item.name = name;
        item.category = category;
        [array addObject:item];
    }
}

+ (NSMutableArray*)initialToDoItems {
    NSMutableArray* items = [NSMutableArray arrayWithCapacity:12];

    NSArray* names = @[
        @"See health care provider before trip",
        @"Get travel health insurance",
        @"Register with US Department of State",
        @"Fill prescriptions",
        @"Prepare travel documents",
        @"Prepare travel health kit",
        @"Give itinerary to emergency contact"
    ];

    [TSToDoItem addNewItemsWithNames:names
                         andCategory:CATEGORY_BEFORE_TRIP
                             toArray:items];
    names = @[
        @"Use insect repellent",
        @"Apply sunscreen",
    ];
    [TSToDoItem addNewItemsWithNames:names
                         andCategory:CATEGORY_DURING_TRIP
                             toArray:items];

    return items;
}

+ (id<TSTripItem>)defaultNewItem {
    TSToDoItem* item = [TSToDoItem MR_createEntity];
    item.category = CATEGORY_MISC;
    item.name = [TSToDoItem defaultNewItemName];
    return item;
}

+ (NSString*)editItemViewControllerIdentifier {
    return @"travel_item_details";
}

+ (NSArray*)orderedCategoriesWithCategories:(NSMutableOrderedSet*)categories {
    NSArray* canonicalOrder = @[
        CATEGORY_MISC,
        CATEGORY_BEFORE_TRIP,
        CATEGORY_DURING_TRIP,
        CATEGORY_AFTER_TRIP
    ];

    NSMutableArray* orderedCategories = [NSMutableArray arrayWithCapacity:4];
    for (NSString* s in canonicalOrder) {
        if ([categories containsObject:s]) {
            [orderedCategories addObject:s];
            [categories removeObject:s];
        }
    }

    return [orderedCategories arrayByAddingObjectsFromArray:[categories array]];
}

+ (NSArray*)orderedCategoriesForTrip:(TSTrip*)trip {
    NSMutableOrderedSet* setOfCategories = [[NSMutableOrderedSet alloc] init];

    for (id item in trip.toDoItems) {
        [setOfCategories addObject:[item category]];
    }

    // TODO: this is super ugly, clean this up perhaps
    return [TSToDoItem orderedCategoriesWithCategories:setOfCategories];
}

+ (NSString*)emptyHeaderText {
    return @"Add medicines you regularly take here, and they will appear in the packing lists of any trips you create.";
}

#pragma mark - TSTripItem methods

- (void)deleteItemFromTrip:(TSTrip *)trip {

    // TODO: this is kind of a horrible way to handle this, think of something more sensible and _do that instead_
    // If this item has a specific trip it can be deleted normally, also if no trip is passed in this must
    // be a profile todo and should just be deleted entirely

    if (self.trip || !trip) {
        [self MR_deleteEntity];
        // Storing deletions shouldn't be handled in the background, so doing this persistently
        [[NSManagedObjectContext MR_defaultContext] MR_saveToPersistentStoreAndWait];
    } else {
        // If not it's a profile toDo and must be just removed from this trip
        DLog(@"Removing item with uniqueId: %@", self.uniqueId);
        [trip removeProfileToDo:self];
    }
}

#pragma mark - Instance methods

- (NSString*)uniqueId {
    // Lazily generate these as necessary
    NSString* uniqueId = [self primitiveValueForKey:@"uniqueId"];
    if (!uniqueId) { self.uniqueId = [[NSUUID UUID] UUIDString]; }
    return uniqueId;
}

- (NSNumber*)completed {
    if (self.associatedTrip) {
        return @([self.associatedTrip hasCompletedToDo:self]);
    }
    
    return [self primitiveValueForKey:@"completed"];
}

- (void)toggleCompleted {
    [self setCompleted:[NSNumber numberWithBool:!([self.completed boolValue])]];
    [self.associatedTrip setCompleted:![self.associatedTrip hasCompletedToDo:self] forToDo:self];
}

- (DHNotificationGroup*)notificationGroup {
    return [DHNotifications notificationWithId:self.reminderId];
}

- (NSString*)description {
    return [NSString stringWithFormat:@"ToDo: '%@'", self.name];
}

@end
