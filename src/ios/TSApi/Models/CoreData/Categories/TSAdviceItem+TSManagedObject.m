//
//  TSAdviceItem+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 7/18/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSAdviceItem+TSManagedObject.h"
#import "TSDisease+TSManagedObject.h"
#import "DHNotifications.h"
#import "DHNotificationGroup.h"

@implementation TSAdviceItem (TSManagedObject)

NSString* const kTSAdviceKey = @"kTSAdviceKey";

@dynamic name;
@dynamic trip;

#pragma mark - Class methods

+ (NSArray*)itemsForTrip:(TSTrip*)trip {
    return trip.adviceItems.array;
}

+ (NSString*)defaultNewItemName {
    return NSLocalizedString(@"New 'Advice' item",
                             @"Default name for a newly created 'Advice' item");
}

+ (id<TSTripItem>)defaultNewItem {
    TSAdviceItem* item = [TSAdviceItem MR_createEntity];
    item.name = [TSAdviceItem defaultNewItemName];
    return item;
}

+ (NSString*)editItemViewControllerIdentifier {
    return @"travel_item_details";
}

+ (NSString*)emptyHeaderText {
    return @"As vaccinations are added during your trips they will be displayed here.";
}

+ (NSArray*)orderedCategoriesForTrip:(TSTrip*)trip {
    NSMutableOrderedSet* setOfCategories = [[NSMutableOrderedSet alloc] init];

    for (TSAdviceItem* item in trip.adviceItems) {
        [setOfCategories addObject:[item category]];
    }

    return [setOfCategories array];
}

#pragma mark - TSTripItem methods

- (void)deleteItemFromTrip:(TSTrip *)trip {
    [DHNotifications removeNotifiersByGroupId:self.reminderId];

    if (!self.drug) {
        self.user = nil;
        for (TSAdviceItem* item in [TSAdviceItem MR_findAll]) {
            if ([item.name isEqualToString:self.name]) {
                item.disease.selectedDrug = nil;
                item.reminderId = nil;
                item.disease.dateOfVaccination = nil;
                item.user = nil;
            }
        }
    }

    [kAppDelegate saveMagicalRecordData];
}

#pragma mark - Instance methods

- (NSString*)category {
    return self.disease.destination.nameList;
}

- (void)setReminderId:(NSString *)reminderId {
    [self willChangeValueForKey:@"reminderId"];
    [self setPrimitiveValue:reminderId forKey:@"reminderId"];
    [self didChangeValueForKey:@"reminderId"];
    // Very important to save this as soon as the reminderId is set
    [kAppDelegate saveMagicalRecordData];
}

- (DHNotificationGroup*)notificationGroup {
    return [DHNotifications notificationWithId:self.reminderId];
}

- (BOOL)isDrug {
    return self.disease.drug;
}

@end