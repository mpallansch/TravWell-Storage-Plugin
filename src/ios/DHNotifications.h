//
//  DHNotifications.h
//  DHNotifications
//
//  Created by James Power on 1/2/13.
//  Copyright (c) 2013 Duet Health. All rights reserved.
//

#import <Foundation/Foundation.h>
@class DHNotificationGroup;

/**
 * Duet Health Notifications are designed to be a simple way to create and maintain
 * local reminders. Reminder information is stored in the NSUserDefaults under the
 * DHNotifications object.
 *
 * Usage: import into project however you see fit.
 *
 * Default notification text: define DH_DEFAULT_NOTIFICATION_TEXT for default text
 * for a given notification.
 *
 */
@interface DHNotifications : NSObject

typedef enum {
    DHNotificationRepeatIntervalNever,
    DHNotificationRepeatIntervalHourly,
    DHNotificationRepeatIntervalDaily,
    DHNotificationRepeatIntervalWeekly,
} DHNotificationRepeatInterval;

+ (NSMutableDictionary*)notificationsDictionary;
+ (void)save;
/** Hack to add notifications to a cache to help app speed issues with large sets of notifications. */
+ (void)registerNotificationGroup:(DHNotificationGroup*)notificationGroup;
/*!
 * Cancel all notifications in the given group and remove the group from the stored notifications.
 */
+ (void)removeNotifiersByGroupId:(NSString*)groupId;
+ (NSSet*)allInUseNotificationIds;
+ (NSSet*)allLocalNotificationIds;
+ (BOOL)cancelNotificationById:(NSString*)notId;

/**
 * Cancels all scheduled notifications that don't have an identifier within the
 * the pool of DHNotifications. 
 */
+ (void)cancelUnusedNotifications;

+ (DHNotificationGroup*)blankNotificationWithId:(NSString*)identifier andInterval:(DHNotificationRepeatInterval)interval andCount:(int)count;

/**
 * This method will return whatever STORED notification exists with the given identifier. Therefore if you want to modify
 * the returned notification group it is recommended to create the initial group using this method, and then modify that
 * group - don't repeatedly use this method to access the group.
 */
+ (DHNotificationGroup*)notificationWithId:(NSString*)identifier;

+ (void)removeAllNotifications;

#ifdef DEBUG
+ (void)outputDebugInfo;
#endif

@end
