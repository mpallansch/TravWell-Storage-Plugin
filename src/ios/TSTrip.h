//
//  TSTrip.h
//  Travel Safe
//
//  Created by James Power on 3/3/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSAdviceItem, TSDestination, TSDoc, TSPackingListItem, TSToDoItem, TSUser;

@interface TSTrip : NSManagedObject

@property (nonatomic, retain) NSDate * endDate;
@property (nonatomic, retain) NSDate * startDate;
@property (nonatomic, retain) NSData * deletedProfilePackingListIds;
@property (nonatomic, retain) NSData * deletedProfileToDoIds;
@property (nonatomic, retain) NSData * completedProfileToDoIds;
@property (nonatomic, retain) NSData * completedProfilePackingListIds;
@property (nonatomic, retain) NSOrderedSet *adviceItems;
@property (nonatomic, retain) NSOrderedSet *destinations;
@property (nonatomic, retain) NSOrderedSet *docs;
@property (nonatomic, retain) NSOrderedSet *packingListItems;
@property (nonatomic, retain) NSOrderedSet *toDoItems;
@property (nonatomic, retain) TSUser *user;
@end

@interface TSTrip (CoreDataGeneratedAccessors)

- (void)insertObject:(TSAdviceItem *)value inAdviceItemsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromAdviceItemsAtIndex:(NSUInteger)idx;
- (void)insertAdviceItems:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removeAdviceItemsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInAdviceItemsAtIndex:(NSUInteger)idx withObject:(TSAdviceItem *)value;
- (void)replaceAdviceItemsAtIndexes:(NSIndexSet *)indexes withAdviceItems:(NSArray *)values;
- (void)addAdviceItemsObject:(TSAdviceItem *)value;
- (void)removeAdviceItemsObject:(TSAdviceItem *)value;
- (void)addAdviceItems:(NSOrderedSet *)values;
- (void)removeAdviceItems:(NSOrderedSet *)values;
- (void)insertObject:(TSDestination *)value inDestinationsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromDestinationsAtIndex:(NSUInteger)idx;
- (void)insertDestinations:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removeDestinationsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInDestinationsAtIndex:(NSUInteger)idx withObject:(TSDestination *)value;
- (void)replaceDestinationsAtIndexes:(NSIndexSet *)indexes withDestinations:(NSArray *)values;
- (void)addDestinationsObject:(TSDestination *)value;
- (void)removeDestinationsObject:(TSDestination *)value;
- (void)addDestinations:(NSOrderedSet *)values;
- (void)removeDestinations:(NSOrderedSet *)values;
- (void)insertObject:(TSDoc *)value inDocsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromDocsAtIndex:(NSUInteger)idx;
- (void)insertDocs:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removeDocsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInDocsAtIndex:(NSUInteger)idx withObject:(TSDoc *)value;
- (void)replaceDocsAtIndexes:(NSIndexSet *)indexes withDocs:(NSArray *)values;
- (void)addDocsObject:(TSDoc *)value;
- (void)removeDocsObject:(TSDoc *)value;
- (void)addDocs:(NSOrderedSet *)values;
- (void)removeDocs:(NSOrderedSet *)values;
- (void)insertObject:(TSPackingListItem *)value inPackingListItemsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromPackingListItemsAtIndex:(NSUInteger)idx;
- (void)insertPackingListItems:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removePackingListItemsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInPackingListItemsAtIndex:(NSUInteger)idx withObject:(TSPackingListItem *)value;
- (void)replacePackingListItemsAtIndexes:(NSIndexSet *)indexes withPackingListItems:(NSArray *)values;
- (void)addPackingListItemsObject:(TSPackingListItem *)value;
- (void)removePackingListItemsObject:(TSPackingListItem *)value;
- (void)addPackingListItems:(NSOrderedSet *)values;
- (void)removePackingListItems:(NSOrderedSet *)values;
- (void)insertObject:(TSToDoItem *)value inToDoItemsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromToDoItemsAtIndex:(NSUInteger)idx;
- (void)insertToDoItems:(NSArray *)value atIndexes:(NSIndexSet *)indexes;
- (void)removeToDoItemsAtIndexes:(NSIndexSet *)indexes;
- (void)replaceObjectInToDoItemsAtIndex:(NSUInteger)idx withObject:(TSToDoItem *)value;
- (void)replaceToDoItemsAtIndexes:(NSIndexSet *)indexes withToDoItems:(NSArray *)values;
- (void)addToDoItemsObject:(TSToDoItem *)value;
- (void)removeToDoItemsObject:(TSToDoItem *)value;
- (void)addToDoItems:(NSOrderedSet *)values;
- (void)removeToDoItems:(NSOrderedSet *)values;
@end
