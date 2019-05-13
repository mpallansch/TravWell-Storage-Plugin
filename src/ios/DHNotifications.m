//
//  DHNotifications.m
//  DHNotifications
//
//  Created by James Power on 1/2/13.
//  Copyright (c) 2013 Duet Health. All rights reserved.
//

#import "DHNotifications.h"
#import "DHNotificationGroup.h"
#import "DHNotifier.h"

#define KEY_NOTIFICATION @"notification"

@implementation DHNotifications

static NSMutableDictionary* notificationsDictionary;
static NSMutableDictionary* notificationCache;
static NSString* const kNotificationsFileName = @"DHNotifications";
/// Whenever this subject emits any value a save operation is kicked off. The save is throttled
/// every 1 seconds so if multiple requests come in rapidly they're ignored until at least 1 second
/// has elapsed since the last request, at which point the filesystem is hit and a save occurs.
static RACSubject* saveSubject;

+ (NSMutableDictionary*)notificationsDictionary {
    static RACScheduler* ioScheduler;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        ioScheduler = [RACScheduler scheduler];
        saveSubject = [RACSubject subject];
        [[[saveSubject throttle:1]
          deliverOn:ioScheduler ]
          subscribeNext:^(id x) {
              NSString* filePath = [DHNotifications pathToNotificationsFile];
              [[DHNotifications notificationsDictionary] writeToFile:filePath atomically:YES];
          }];

        notificationsDictionary = [NSMutableDictionary dictionaryWithContentsOfFile:[DHNotifications pathToNotificationsFile]];
        // Need to check for the way notifications were previously stored in shared preferences.
        if (!notificationsDictionary) {
            NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
            notificationsDictionary = [[defaults objectForKey:kNotificationsFileName] mutableCopy];
            [self save];
            [defaults removeObjectForKey:kNotificationsFileName];
        }

        if (!notificationsDictionary) {
            notificationsDictionary = [NSMutableDictionary dictionary];
        }
    });

    return notificationsDictionary;
}

+ (void)removeNotifiersByGroupId:(NSString*)groupId {
    DLog(@"Removing notifiers with group id: %@", groupId);
    NSMutableDictionary* notifications = [DHNotifications notificationsDictionary];
    NSMutableDictionary* group = [notificationsDictionary objectForKey:groupId];
    for (NSDictionary* notifier in [group objectForKey:@"notifiers"]) {
        [DHNotifications cancelNotificationById:[notifier objectForKey:KEY_NOTIFICATION_ID]];
    }

    if ([notifications objectForKey:groupId]) {
        [notifications removeObjectForKey:groupId];
    }

    double delayInSeconds = 1.0;
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delayInSeconds * NSEC_PER_SEC));
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void) {
        if ([notifications objectForKey:groupId]) {
            [notifications removeObjectForKey:groupId];
        }
    });

    [DHNotifications save];
}

+ (NSSet*)allLocalNotificationIds {
    NSMutableSet* ids = [NSMutableSet set];
    NSArray* notifications = [[UIApplication sharedApplication] scheduledLocalNotifications];

    for (UILocalNotification* notification in notifications) {
        if ([notification.userInfo objectForKey:KEY_NOTIFICATION_ID])
            [ids addObject:[notification.userInfo objectForKey:KEY_NOTIFICATION_ID]];
    }

    return ids;
}

+ (NSSet*)allInUseNotificationIds {
    NSMutableDictionary* notifications = [DHNotifications notificationsDictionary];
    NSMutableSet* ids = [NSMutableSet set];
    for (NSString* key in notifications) {
        NSDictionary* group = [notifications objectForKey:key];
        if ([[group objectForKey:KEY_NOTIFICATION] objectForKey:KEY_NOTIFICATION_ID]) {
            [ids addObject:[[group objectForKey:KEY_NOTIFICATION] objectForKey:KEY_NOTIFICATION_ID]];
        }
    }

    return ids;
}

+ (BOOL)cancelNotificationById:(NSString*)notId {
    BOOL found = NO;
    for (UILocalNotification* not in [[UIApplication sharedApplication] scheduledLocalNotifications]) {
        if ([[[not userInfo] objectForKey:KEY_NOTIFICATION_ID] isEqualToString:notId]) {
            [[UIApplication sharedApplication] cancelLocalNotification:not];
            found = YES;
            break;
        }
    }
    return found;
}

+ (void)cancelUnusedNotifications {
    NSSet* inUse = [DHNotifications allInUseNotificationIds];
    NSSet* allNotifications = [DHNotifications allLocalNotificationIds];

    for (NSString* notId in allNotifications) {
        if (![inUse containsObject:notId]) {
            [DHNotifications cancelNotificationById:notId];
        }
    }
}

+ (DHNotificationGroup*)blankNotificationWithId:(NSString*)identifier andInterval:(DHNotificationRepeatInterval)interval andCount:(int)count {
    // Remove all current reminders by the group reminder id
    [DHNotifications removeNotifiersByGroupId:identifier];
    DHNotificationGroup* group = [[DHNotificationGroup alloc] initWithInterval:interval
                                                                      andCount:count
                                                                    andGroupId:identifier];
    [group save];
    return group;
}

+ (NSMutableDictionary*)notificationCache {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        notificationCache = [NSMutableDictionary dictionary];
    });
    return notificationCache;
}

+ (void)registerNotificationGroup:(DHNotificationGroup*)notificationGroup {
    NSMutableDictionary* cache = [DHNotifications notificationCache];
    cache[notificationGroup.groupId] = notificationGroup;
}

+ (DHNotificationGroup*)notificationWithId:(NSString*)identifier {
    NSMutableDictionary* cache = [DHNotifications notificationCache];

    if (!cache[identifier]) {
        NSMutableDictionary* record = [DHNotifications notificationsDictionary][identifier];
        if (record) {
            cache[identifier] = [[DHNotificationGroup alloc] initWithRecord:record];
        }
    }

    return cache[identifier];
}

+ (void)removeAllNotifications {
    for (NSString* identifier in [DHNotifications allInUseNotificationIds]) {
        [DHNotifications cancelNotificationById:identifier];
    }

    notificationsDictionary = [NSMutableDictionary dictionary];
    [DHNotifications save];
}

#ifdef DEBUG
+ (void)outputDebugInfo {
    DLog(@"All notifications: %@", [[DHNotifications notificationsDictionary] allKeys]);
}
#endif

+ (void)save {
    [saveSubject sendNext:nil];
}

+ (NSString*)pathToNotificationsFile {
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    return [NSString stringWithFormat:@"%@/%@", [paths firstObject], kNotificationsFileName];
}

@end
