//
//  TSDoc.h
//  Travel Safe
//
//  Created by James Power on 8/27/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSTrip, TSUser;

@interface TSDoc : NSManagedObject

@property(nonatomic, retain) NSString* name;
@property(nonatomic, retain) NSString* fileName;
@property(nonatomic, retain) TSUser* user;
@property(nonatomic, retain) TSTrip* trip;

@end
