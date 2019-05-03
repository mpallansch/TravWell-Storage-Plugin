//
//  TSToDoItem+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 7/18/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSToDoItem.h"
#import "TSCheckable.h"
#import "TSTripItem.h"
#import "TSCategorizable.h"

@class DHNotificationGroup;

@interface TSToDoItem (TSManagedObject)<TSTripItem, TSCheckable, TSCategorizable>

@property(nonatomic, readonly) DHNotificationGroup* notificationGroup;

+ (NSArray*)itemsForTrip:(TSTrip*)trip;
+ (NSString*)defaultNewItemName;
+ (id<TSTripItem>)defaultNewItem;
+ (NSMutableArray*)initialToDoItems;
+ (NSArray*)orderedCategoriesForTrip:(TSTrip*)trip;

/*!
 * Toggle completed (completed = !completed).
 */
- (void)toggleCompleted;

@end

@interface TSToDoItem (EXTProperties)

/** For 'My Profile'/global todo items use the associatedTrip to make trip specific changes. */
@property(nonatomic, strong) TSTrip* associatedTrip;

@end