//
//  TSPackingList.h
//  Travel Safe
//
//  Created by James Power on 8/14/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSDestination, TSPackingListSuperGroup;

@interface TSPackingList : NSManagedObject

@property(nonatomic, retain) NSString* packingListId;
@property(nonatomic, retain) NSString* friendlyName;
@property(nonatomic, retain) NSOrderedSet* superGroups;
@property(nonatomic, retain) TSDestination* destination;
@end

@interface TSPackingList (CoreDataGeneratedAccessors)

- (void)insertObject:(TSPackingListSuperGroup*)value inSuperGroupsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromSuperGroupsAtIndex:(NSUInteger)idx;
- (void)insertSuperGroups:(NSArray*)value atIndexes:(NSIndexSet*)indexes;
- (void)removeSuperGroupsAtIndexes:(NSIndexSet*)indexes;
- (void)replaceObjectInSuperGroupsAtIndex:(NSUInteger)idx withObject:(TSPackingListSuperGroup*)value;
- (void)replaceSuperGroupsAtIndexes:(NSIndexSet*)indexes withSuperGroups:(NSArray*)values;
- (void)addSuperGroupsObject:(TSPackingListSuperGroup*)value;
- (void)removeSuperGroupsObject:(TSPackingListSuperGroup*)value;
- (void)addSuperGroups:(NSOrderedSet*)values;
- (void)removeSuperGroups:(NSOrderedSet*)values;
@end
