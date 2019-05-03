//
//  TSToDoItem.h
//  Travel Safe
//
//  Created by James Power on 3/3/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSTrip, TSUser;

@interface TSToDoItem : NSManagedObject

@property (nonatomic, retain) NSString * category;
@property (nonatomic, retain) NSNumber * completed;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * notes;
@property (nonatomic, retain) NSString * reminderId;
@property (nonatomic, retain) NSString * uniqueId;
@property (nonatomic, retain) TSTrip *trip;
@property (nonatomic, retain) TSUser *user;

@end
