//
//  TSDisease.h
//  Travel Safe
//
//  Created by James Power on 9/5/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSAdviceItem, TSDestination, TSDrug;

@interface TSDisease : NSManagedObject

@property(nonatomic, retain) NSDate* dateOfVaccination;
@property(nonatomic, retain) NSString* diseaseListName;
@property(nonatomic, retain) NSString* findOutWhyHtml;
@property(nonatomic, retain) NSString* friendlyName;
@property(nonatomic, retain) NSString* groupText;
@property(nonatomic, retain) NSString* diseasePageUrl;
@property(nonatomic, retain) TSAdviceItem* adviceItem;
@property(nonatomic, retain) TSDestination* destination;
@property(nonatomic, retain) NSOrderedSet* drugs;
@property(nonatomic, retain) TSDrug* selectedDrug;
@end

@interface TSDisease (CoreDataGeneratedAccessors)

- (void)insertObject:(TSDrug*)value inDrugsAtIndex:(NSUInteger)idx;
- (void)removeObjectFromDrugsAtIndex:(NSUInteger)idx;
- (void)insertDrugs:(NSArray*)value atIndexes:(NSIndexSet*)indexes;
- (void)removeDrugsAtIndexes:(NSIndexSet*)indexes;
- (void)replaceObjectInDrugsAtIndex:(NSUInteger)idx withObject:(TSDrug*)value;
- (void)replaceDrugsAtIndexes:(NSIndexSet*)indexes withDrugs:(NSArray*)values;
- (void)addDrugsObject:(TSDrug*)value;
- (void)removeDrugsObject:(TSDrug*)value;
- (void)addDrugs:(NSOrderedSet*)values;
- (void)removeDrugs:(NSOrderedSet*)values;
@end
