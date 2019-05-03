//
//  TSPackingListSuperGroup.h
//  Travel Safe
//
//  Created by James Power on 8/14/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSPackingListGroup;

@interface TSPackingListSuperGroup : NSManagedObject

@property(nonatomic, retain) NSString* superGroupText;
@property(nonatomic, retain) NSString* superGroupId;
@property(nonatomic, retain) NSNumber* sortOrder;
@property(nonatomic, retain) NSManagedObject* packingList;
@property(nonatomic, retain) NSOrderedSet* groups;
@end

@interface TSPackingListSuperGroup (CoreDataGeneratedAccessors)

- (void)insertObject:(TSPackingListGroup*)value inGroupsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromGroupsAtIndex:(NSUInteger)idx;
- (void)insertGroups:(NSArray*)value atIndexes:(NSIndexSet*)indexes;
- (void)removeGroupsAtIndexes:(NSIndexSet*)indexes;
- (void)replaceObjectInGroupsAtIndex:(NSUInteger)idx withObject:(TSPackingListGroup*)value;
- (void)replaceGroupsAtIndexes:(NSIndexSet*)indexes withGroups:(NSArray*)values;
- (void)addGroupsObject:(TSPackingListGroup*)value;
- (void)removeGroupsObject:(TSPackingListGroup*)value;
- (void)addGroups:(NSOrderedSet*)values;
- (void)removeGroups:(NSOrderedSet*)values;
@end
