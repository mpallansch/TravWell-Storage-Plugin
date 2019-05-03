//
//  TSDestination.h
//  Travel Safe
//
//  Created by James Power on 9/9/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSDisease, TSPackingList, TSTrip;

@interface TSDestination : NSManagedObject

@property(nonatomic, retain) NSString* flagUrl;
@property(nonatomic, retain) NSNumber* isAlias;
@property(nonatomic, retain) NSString* isoA2;
@property(nonatomic, retain) NSString* letter;
@property(nonatomic, retain) NSString* nameFriendly;
@property(nonatomic, retain) NSString* nameList;
@property(nonatomic, retain) NSString* nameOfficial;
@property(nonatomic, retain) NSString* namePlugin;
@property(nonatomic, retain) NSString* nameYellowFeverMalariaTable;
@property(nonatomic, retain) NSString* parentName;
@property(nonatomic, retain) NSString* travelNoticesAnchorName;
@property(nonatomic, retain) NSString* webLink;
@property(nonatomic, retain) NSString* emergencyNumbers;
@property(nonatomic, retain) NSOrderedSet* diseases;
@property(nonatomic, retain) TSPackingList* packingList;
@property(nonatomic, retain) NSSet* trip;
@end

@interface TSDestination (CoreDataGeneratedAccessors)

- (void)insertObject:(TSDisease*)value inDiseasesAtIndex:(NSUInteger)idx;
- (void)removeObjectFromDiseasesAtIndex:(NSUInteger)idx;
- (void)insertDiseases:(NSArray*)value atIndexes:(NSIndexSet*)indexes;
- (void)removeDiseasesAtIndexes:(NSIndexSet*)indexes;
- (void)replaceObjectInDiseasesAtIndex:(NSUInteger)idx withObject:(TSDisease*)value;
- (void)replaceDiseasesAtIndexes:(NSIndexSet*)indexes withDiseases:(NSArray*)values;
- (void)addDiseasesObject:(TSDisease*)value;
- (void)removeDiseasesObject:(TSDisease*)value;
- (void)addDiseases:(NSOrderedSet*)values;
- (void)removeDiseases:(NSOrderedSet*)values;
- (void)addTripObject:(TSTrip*)value;
- (void)removeTripObject:(TSTrip*)value;
- (void)addTrip:(NSSet*)values;
- (void)removeTrip:(NSSet*)values;

@end
