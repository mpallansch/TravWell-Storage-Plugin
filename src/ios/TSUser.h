//
//  TSUser.h
//  Travel Safe
//
//  Created by James Power on 2/25/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSAdviceItem, TSDoc, TSToDoItem, TSTrip;

@interface TSUser : NSManagedObject

@property (nonatomic, retain) NSString * startScreen;
@property (nonatomic, retain) NSOrderedSet *adviceItems;
@property (nonatomic, retain) NSOrderedSet *docs;
@property (nonatomic, retain) TSTrip *trip;
@property (nonatomic, retain) NSSet *medicineTodos;
@end

@interface TSUser (CoreDataGeneratedAccessors)

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
- (void)addMedicineTodosObject:(TSToDoItem *)value;
- (void)removeMedicineTodosObject:(TSToDoItem *)value;
- (void)addMedicineTodos:(NSSet *)values;
- (void)removeMedicineTodos:(NSSet *)values;

@end
