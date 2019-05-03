//
//  TSReminderInterval+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 8/6/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSReminderInterval+TSManagedObject.h"

@implementation TSReminderInterval (TSManagedObject)

+ (NSString*)stringFromType:(TSReminderIntervalType)type {
    switch (type) {
        case TSReminderIntervalTypeOnce:
            return @"once";
        case TSReminderIntervalTypeDaily:
            return @"daily";
        case TSReminderIntervalTypeWeekly:
            return @"weekly";
        case TSReminderIntervalTypeStrict:
            return @"strict";
        default:
            return @"";
    }
}

+ (instancetype)initWithIntervalType:(TSReminderIntervalType)intervalType
                          andDaysOut:(NSArray*)daysOut
                             andDrug:(TSDrug*)drug {
    TSReminderInterval* interval = [TSReminderInterval MR_createEntity];
    interval.daysOut = daysOut;
    interval.drug = drug;
    interval.type = [TSReminderInterval stringFromType:intervalType];
    
    return interval;
}

- (NSArray*)datesRepresentedByIntervalFromDate:(NSDate*)date toDate:(NSDate*)endDate {
    DLog(@"Setting up dates with start date: %@ (plus 1 day: %@)", date, [date dateByAddingDays:-1]);

    if (self.intervalType == TSReminderIntervalTypeOnce) {
        // Means literally once
        return @[
            date
        ];
    }

    NSDate* today = [NSDate date];

    NSMutableArray* dates = [[NSMutableArray alloc] init];

    if (self.intervalType == TSReminderIntervalTypeStrict) {
        // Strict means exactly the number of days given
        for (NSNumber* numberOfDaysFromDate in self.daysOut) {
            //dates addObject:[date dateby
            NSDate* dateToAdd = [date dateByAddingDays:[numberOfDaysFromDate integerValue]];
            [self addDate:dateToAdd toMutableArray:dates ifLaterThan:today];
        }
        return dates;
    }


    NSInteger daysBetween = [date daysAfterDate:endDate];

    // Daily means given an interval: [-2, 10] start reminders 2 days before the start
    // date of the trip and 10 days past the end
    if (self.intervalType == TSReminderIntervalTypeDaily) {
        NSArray* intervalBorders = (NSArray*)self.daysOut;
        DLog(@"interval borders: %@", intervalBorders);
        if (intervalBorders.count > 1) {
            NSInteger startDay = [intervalBorders[0] integerValue];
            NSInteger lastDay = [intervalBorders[1] integerValue] + daysBetween;
            for (NSInteger i = startDay; i <= lastDay; i++) {
                [self addDate:[date dateByAddingDays:i] toMutableArray:dates ifLaterThan:today];
            }
        }
        return dates;
    }

    // Same thing as daily but only once per week
    if (self.intervalType == TSReminderIntervalTypeWeekly) {
        NSArray* intervalBorders = (NSArray*)self.daysOut;
        if (intervalBorders.count > 1) {
            NSInteger startWeek = [intervalBorders[0] integerValue];
            NSInteger lastWeek = [intervalBorders[1] integerValue] + daysBetween;
            for (NSInteger i = startWeek; i <= lastWeek; i += 7) {
                [self addDate:[date dateByAddingDays:i] toMutableArray:dates ifLaterThan:today];
            }
        }
        return dates;
    }

    return dates;
}

- (void)addDate:(NSDate*)date toMutableArray:(NSMutableArray*)array ifLaterThan:(NSDate*)laterThanDate {
    [array addObject:date];
}

- (TSReminderIntervalType)intervalType {
    if ([self.type isEqualToString:@"once"]) {
        return TSReminderIntervalTypeOnce;
    } else if ([self.type isEqualToString:@"daily"]) {
        return TSReminderIntervalTypeDaily;
    } else if ([self.type isEqualToString:@"weekly"]) {
        return TSReminderIntervalTypeWeekly;
    } else if ([self.type isEqualToString:@"strict"]) {
        return TSReminderIntervalTypeStrict;
    }

    return TSReminderIntervalTypeUnknown;
}

@end
