//
//  TSDrug.h
//  Travel Safe
//
//  Created by James Power on 7/21/15.
//  Copyright (c) 2015 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSDisease, TSReminderInterval;

@interface TSDrug : NSManagedObject

@property (nonatomic, retain) NSString * alertText;
@property (nonatomic, retain) NSString * displayName;
@property (nonatomic, retain) NSString * duration;
@property (nonatomic, retain) NSString * friendlyName;
@property (nonatomic, retain) NSString * reminderInstructions;
@property (nonatomic, retain) id searchTerms;
@property (nonatomic, retain) NSSet *disease;
@property (nonatomic, retain) TSDisease *diseases;
@property (nonatomic, retain) TSReminderInterval *interval;
@end

@interface TSDrug (CoreDataGeneratedAccessors)

- (void)addDiseaseObject:(TSDisease *)value;
- (void)removeDiseaseObject:(TSDisease *)value;
- (void)addDisease:(NSSet *)values;
- (void)removeDisease:(NSSet *)values;

@end
