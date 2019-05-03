//
//  TSPackingListItem+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 7/18/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSPackingListItem.h"
#import "TSTripItem.h"
#import "TSCategorizable.h"
#import "TSCheckable.h"

@class TSTrip;

@interface TSPackingListItem (TSManagedObject)<TSTripItem, TSCategorizable, TSCheckable>

+ (NSArray*)itemsForTrip:(TSTrip*)trip;
+ (NSString*)defaultNewItemName;
+ (id<TSTripItem>)defaultNewItem;
+ (NSArray*)orderedCategoriesForTrip:(TSTrip*)trip;

- (void)toggleCompleted;

@end

@interface TSPackingListItem (EXTProperties)

/** Maintain a reference to the todo that may have created this packing list item, this way if this item is deleted so is the todo. */
@property(nonatomic, strong) TSToDoItem* associatedToDo;
@property(nonatomic, strong) TSTrip* associatedTrip;

@end