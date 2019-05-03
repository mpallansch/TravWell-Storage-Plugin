//
//  TSAdviceItem+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 7/18/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSAdviceItem.h"
#import "TSTripItem.h"

@class DHNotificationGroup, TSDisease;

@interface TSAdviceItem (TSManagedObject)<TSTripItem>

+ (NSArray*)itemsForTrip:(TSTrip*)trip;
+ (NSString*)defaultNewItemName;
+ (id<TSTripItem>)defaultNewItem;

@property (nonatomic, copy, readonly) NSString* category;
@property (nonatomic, readonly, getter = isDrug) BOOL drug;

- (DHNotificationGroup*)notificationGroup;

@end
