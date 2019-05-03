//
//  TSReminderInterval.h
//  Travel Safe
//
//  Created by James Power on 8/6/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSDrug;

@interface TSReminderInterval : NSManagedObject

@property(nonatomic, retain) id daysOut;
@property(nonatomic, retain) NSString* type;
@property(nonatomic, retain) TSDrug* drug;

@end
