//
//  DHNotificationModel.m
//  DHNotifications
//
//  Created by James Power on 1/2/13.
//  Copyright (c) 2013 Duet Health. All rights reserved.
//

#import "DHNotificationGroup.h"
#import "DHNotifier.h"

@implementation DHNotificationGroup

@synthesize notifiers;
@synthesize groupId;
@synthesize name;

// TODO: re-write the API to take this shit and do the repetitions thusly:
// DHRepeatMonday | DHRepeatWednesday
#define KEY_GROUP_ID @"groupId"
#define KEY_NOTIFICATION_TEXT @"notification_text"
#define KEY_CANONICAL_DATE @"canonical_date"

- (NSMutableDictionary*)record {
    NSMutableDictionary* record = [NSMutableDictionary dictionary];
    // TODO: enum updates wont be compatible unless we always add enums to the end,
    // maybe enum isn't the best way to do this?
    [record setObject:[NSNumber numberWithInt:self.interval]
               forKey:@"interval"];
    if (self.notificationText) {
        [record setObject:self.notificationText
                   forKey:KEY_NOTIFICATION_TEXT];
    }

    if (self.canonicalDate) {
        [record setObject:self.canonicalDate
                   forKey:KEY_CANONICAL_DATE];
    }
    [record setObject:self.groupId
               forKey:KEY_GROUP_ID];
    NSMutableArray* notes = [NSMutableArray array];
    for (DHNotifier* n in notifiers) {
        [notes addObject:[n record]];
    }

    [record setObject:notes
               forKey:@"notifiers"];
    return record;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        self.groupId = [[NSUUID UUID] UUIDString];
        self.notifiers = [NSMutableArray array];
    }
    return self;
}

- (instancetype)initWithInterval:(DHNotificationRepeatInterval)i andCount:(int)count andGroupId:(NSString*)identifier {
    self = [self init];

    if (self) {
        self.interval = i;
        if (identifier) {
            self.groupId = identifier;
        }

        for (int i = 0; i < count; i++) {
            DHNotifier* n = [[DHNotifier alloc] init];
            n.interval = i;
            [notifiers addObject:n];
        }
    }
    [self save];
    return self;
}

- (instancetype)initWithRecord:(NSDictionary*)record {
    self = [super init];

    if (self) {
        self.notifiers = [NSMutableArray array];
        self.interval = [record[@"interval"] intValue];
        self.name = record[@"name"];
        self.groupId = record[KEY_GROUP_ID];
        self.notificationText = record[KEY_NOTIFICATION_TEXT];
        self.canonicalDate = record[KEY_CANONICAL_DATE];
        for (NSDictionary* rec in record[@"notifiers"]) {
            DHNotifier* n = [[DHNotifier alloc] initWithRecord:rec];
            [self.notifiers addObject:n];
        }
    }

    return self;
}

- (instancetype)initWithInterval:(DHNotificationRepeatInterval)interval andCount:(int)count {
    self = [self init];
    if (self) {
        self.interval = interval;

        for (int i = 0; i < count; i++) {
            DHNotifier* n = [[DHNotifier alloc] init];
            n.interval = interval;
            [notifiers addObject:n];
        }
    }

    [self save];
    return self;
}

- (instancetype)initWithDate:(NSDate*)date andNotificationText:(NSString*)text andInterval:(DHNotificationRepeatInterval)interval {
    self = [self init];
    // Have to set this up before setting up notifiers

    if (self) {
        self.notificationText = text;
        self.interval = interval;
        self.canonicalDate = date;
        [self addNotifierWithDate:date];
    }
    [self save];
    return self;
}

- (instancetype)initWithDates:(NSArray*)dates andNotificationText:(NSString*)text andInterval:(DHNotificationRepeatInterval)interval {
    self = [self init];
    if (self) {
        // Just pick the first date as the canonical date
        if (dates.count) {
            self.canonicalDate = dates[0];
        }
        self.notificationText = text;
        self.interval = interval;
        for (NSDate* date in dates) {
            [self addNotifierWithDate:date];
        }
    }
    [self save];
    return self;
}

- (void)setEnabled:(BOOL)enabled {
    for (DHNotifier* n in notifiers) {
        if (self.interval == DHNotificationRepeatIntervalNever) {
            n.enabled = enabled;
        } else {
            n.enabled = enabled && n.repeating;
        }
    }
    [self save];
}

- (void)isPartlyEnabledWithYes:(YesBlock)yes
                         andNo:(NoBlock)no {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        BOOL enabled = NO;
        for (DHNotifier* n in notifiers) {
            if (n.enabled) {
                enabled = YES;
                break;
            }
        }

        if (enabled) {
            dispatch_sync(dispatch_get_main_queue(), yes);
        } else {
            dispatch_sync(dispatch_get_main_queue(), no);
        }
    });
}



- (BOOL)isEnabled {
    for (DHNotifier* n in notifiers) {
        if (self.interval == DHNotificationRepeatIntervalNever) {
            if (!n.enabled) {
                return NO;
            }
        } else {
            // If the notification group is meant to repeat, it's only enabled if all of its
            // repeating (i.e., set by the user), notifiers are enabled
            if (!n.enabled && n.repeating) {
                return NO;
            }
        }
    }
    return YES;
}

- (void)setNotificationText:(NSString*)text {
    _notificationText = text;
    for (DHNotifier* n in notifiers) {
        n.notificationText = text;
    }
}

- (void)addNotifierWithDate:(NSDate*)date {
    DHNotifier* n = [[DHNotifier alloc] init];
    n.interval = self.interval;
    n.notificationFireDate = date;

    n.notificationText = self.notificationText;

    [notifiers addObject:n];
    [self save];
}

- (void)removeNotifier:(DHNotifier*)notifier {
    [DHNotifications cancelNotificationById:notifier.notificationId];
    for (int i = 0; i < self.notifiers.count; i++) {
        if ([[self.notifiers[i] notificationId] isEqualToString:notifier.notificationId]) {
            [self.notifiers removeObjectAtIndex:i];
            break;
        }
    }
    [self save];
}

- (void)save {
    NSMutableDictionary* notifications = [DHNotifications notificationsDictionary];

    if (!notifications) {
        notifications = [NSMutableDictionary dictionary];
    }

    [notifications setObject:[self record]
                      forKey:self.groupId];
    [DHNotifications registerNotificationGroup:self];
    [DHNotifications save];
}


@end
