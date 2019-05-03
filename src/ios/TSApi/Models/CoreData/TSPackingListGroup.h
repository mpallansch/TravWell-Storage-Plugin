//
//  TSPackingListGroup.h
//  Travel Safe
//
//  Created by James Power on 8/14/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSPackingListItem;

@interface TSPackingListGroup : NSManagedObject

@property(nonatomic, retain) NSString* groupId;
@property(nonatomic, retain) NSString* groupText;
@property(nonatomic, retain) NSNumber* sortOrder;
@property(nonatomic, retain) NSManagedObject* superGroup;
@property(nonatomic, retain) NSOrderedSet* items;
@end

@interface TSPackingListGroup (CoreDataGeneratedAccessors)

- (void)insertObject:(TSPackingListItem*)value inItemsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromItemsAtIndex:(NSUInteger)idx;
- (void)insertItems:(NSArray*)value atIndexes:(NSIndexSet*)indexes;
- (void)removeItemsAtIndexes:(NSIndexSet*)indexes;
- (void)replaceObjectInItemsAtIndex:(NSUInteger)idx withObject:(TSPackingListItem*)value;
- (void)replaceItemsAtIndexes:(NSIndexSet*)indexes withItems:(NSArray*)values;
- (void)addItemsObject:(TSPackingListItem*)value;
- (void)removeItemsObject:(TSPackingListItem*)value;
- (void)addItems:(NSOrderedSet*)values;
- (void)removeItems:(NSOrderedSet*)values;
@end
