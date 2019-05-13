//
//  DHNotifier.m
//  DHNotifications
//
//  Created by James Power on 1/2/13.
//  Copyright (c) 2013 Duet Health. All rights reserved.
//

#import "DHNotifier.h"
#import "DHNotifications.h"

@implementation DHNotifier

@synthesize interval;
@synthesize notification;
@synthesize notificationId;
@synthesize notificationText;
@synthesize enabled;
@synthesize repeating;

- (instancetype)init {
    self = [super init];
    if (self) {
        notificationText = @"";
    }
    return self;
}

- (instancetype)initWithRecord:(NSDictionary*)record {
    self = [super init];
    notificationText = @"";
    enabled = [[record objectForKey:@"enabled"] boolValue];

    if (enabled) {
        notificationId = [record objectForKey:KEY_NOTIFICATION_ID];
        NSArray* notifications = [[UIApplication sharedApplication] scheduledLocalNotifications];
        for (UILocalNotification* n in notifications) {
            NSString* nid = [n.userInfo objectForKey:KEY_NOTIFICATION_ID];
            if (nid && [self.notificationId isEqualToString:nid]) {
                self.notification = n;
            }
        }
    }

    repeating = [[record objectForKey:KEY_REPEATING] boolValue];
    notificationText = [record objectForKey:KEY_NOTIFICATION_TEXT];
    interval = [[record objectForKey:@"interval"] intValue];
    _notificationFireDate = [record objectForKey:@"notification_fire_date"];

    return self;
}

#define NUM(x) [NSNumber numberWithInt:x]

- (NSDictionary*)record {
    NSMutableDictionary* record = [@{
                                       @"interval" : NUM(interval),
                                       KEY_NOTIFICATION_TEXT : notificationText,
                                       @"enabled" : [NSNumber numberWithBool:enabled],
                                       KEY_REPEATING : [NSNumber numberWithBool:repeating],
                                       @"notification_fire_date" : self.notificationFireDate,
                                   } mutableCopy];

    if (enabled) {
        [record setObject:notificationId
                   forKey:KEY_NOTIFICATION_ID];
    }

    return record;
}

#pragma mark - Properties

- (BOOL)isEnabled {
    return enabled;
}

- (void)setEnabled:(BOOL)e {

    // Check SDK as the CDC may build with legacy Xcode
#ifdef __IPHONE_8_0
    if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0") && e) {
        // in iOS 8 we gotta request local notification permissions
        UIUserNotificationSettings* alertSettings = [UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeAlert | UIUserNotificationTypeSound categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:alertSettings];
    }
#endif

    if (e && !enabled) {
        notification = [[UILocalNotification alloc] init];
        notification.soundName = UILocalNotificationDefaultSoundName;
        
        if (!self.notificationId) {
            self.notificationId = [[NSUUID UUID] UUIDString];
        }

        notification.fireDate = _notificationFireDate;
        notification.alertBody = notificationText;

        if (interval != DHNotificationRepeatIntervalNever) {
            notification.repeatCalendar = [NSCalendar currentCalendar];
        }

        if (interval == DHNotificationRepeatIntervalWeekly) {
            notification.repeatInterval = NSWeekCalendarUnit;
        } else if (interval == DHNotificationRepeatIntervalDaily) {
            notification.repeatInterval = NSDayCalendarUnit;
        }

        notification.timeZone = [NSTimeZone defaultTimeZone];
        notification.soundName = UILocalNotificationDefaultSoundName;

        NSMutableDictionary* notificationData = [NSMutableDictionary dictionary];
        notificationData[KEY_NOTIFICATION_TEXT] = notificationText;
        notificationData[KEY_NOTIFICATION_ID] = notificationId;
        // Used to not immediately fire notifications that were just set, or ones set in the past
        notificationData[KEY_NOTIFICATION_SET_DATE] = [NSDate date];

        notification.userInfo = notificationData;

        [[UIApplication sharedApplication] scheduleLocalNotification:notification];
    } else if (!e && enabled && notification) {
        [DHNotifications cancelNotificationById:notificationId];
    }
    enabled = e;
}

- (void)setNotificationFireDate:(NSDate*)notificationFireDate {
    _notificationFireDate = notificationFireDate;

    // Hack to toggle the UILocalNotification if it exists
    self.enabled = !self.enabled;
    self.enabled = !self.enabled;
}

@end
