//
//  TSReminderInterval+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 8/6/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSReminderInterval.h"

@interface TSReminderInterval (TSManagedObject)


typedef NS_ENUM(NSInteger, TSReminderIntervalType) {
    TSReminderIntervalTypeOnce,
    TSReminderIntervalTypeDaily,
    TSReminderIntervalTypeWeekly,
    TSReminderIntervalTypeStrict,
    TSReminderIntervalTypeUnknown,
};

@property (nonatomic, readonly) TSReminderIntervalType intervalType;

+ (instancetype)initWithIntervalType:(TSReminderIntervalType)intervalType
                          andDaysOut:(NSArray*)daysOut
                             andDrug:(TSDrug*)drug;

/**
 * Ignores dates prior to the fromDate even if they're indicated by the interval.
 */
- (NSArray*)datesRepresentedByIntervalFromDate:(NSDate*)date toDate:(NSDate*)endDate;

@end
