//
//  TSAdviceItem.h
//  Travel Safe
//
//  Created by James Power on 9/3/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSDisease, TSTrip, TSUser;

@interface TSAdviceItem : NSManagedObject

@property(nonatomic, retain) NSNumber* completed;
@property(nonatomic, retain) NSString* name;
@property(nonatomic, retain) NSString* notes;
@property(nonatomic, retain) NSString* reminderId;
@property(nonatomic, retain) TSDisease* disease;
@property(nonatomic, retain) NSSet* trips;
@property(nonatomic, retain) TSUser* user;
@end

@interface TSAdviceItem (CoreDataGeneratedAccessors)

- (void)addTripsObject:(TSTrip*)value;
- (void)removeTripsObject:(TSTrip*)value;
- (void)addTrips:(NSSet*)values;
- (void)removeTrips:(NSSet*)values;

@end
