//
//  TSTrip+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 2/10/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import "TSTrip.h"

@class TSToDoItem, TSPackingListItem;

@interface TSTrip (TSManagedObject)

@property(nonatomic, readonly) NSString* destinationNameList;
/** Delete a trip but do some house keeping to ensure vaccinations aren't deleted that are still in use.
    Use this instead of deleting the TSTrip directly! */
- (void)deleteTrip;

- (BOOL)containsProfileToDo:(TSToDoItem*)todo;
- (BOOL)containsProfilePackingListItem:(TSPackingListItem*)packingListItem;
- (void)removeProfileToDo:(TSToDoItem*)todo;
- (void)removeProfilePackingListItem:(TSPackingListItem*)packingListItem;

- (BOOL)hasCompletedToDo:(TSToDoItem*)todo;
- (BOOL)hasCompletedPackingListItem:(TSPackingListItem*)packingListItem;
- (void)setCompleted:(BOOL)completed forToDo:(TSToDoItem*)todo;
- (void)setCompleted:(BOOL)completed forPackingListItem:(TSPackingListItem*)packingListItem;

+ (instancetype)tripWithDestinations:(NSArray*)destinations andStartDate:(NSDate*)startDate andEndDate:(NSDate*)endDate;

@end
