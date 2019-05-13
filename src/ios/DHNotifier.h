//
//  DHNotifier.h
//  DHNotifications
//
//  Created by James Power on 1/2/13.
//  Copyright (c) 2013 Duet Health. All rights reserved.
//
//  Usage:
//  Define DH_DEFAULT_NOTIFICATION_TEXT for default text for a notification that
//         hasn't had its notificationText set
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DHNotifications.h"

#define KEY_NOTIFICATION_ID @"notification_id"
#define KEY_NOTIFICATION_SET_DATE @"notification_set_date"
#define KEY_NOTIFICATION_TEXT @"notification_text"
#define KEY_REPEATING @"notification_repeating"

@interface DHNotifier : NSObject

// TODO: is it necessary to maintain this both here and at the group level?
@property(nonatomic, assign) DHNotificationRepeatInterval interval;
@property(nonatomic, retain) NSDate* notificationFireDate;
@property(nonatomic, retain) NSString* notificationId;
@property(nonatomic, retain) UILocalNotification* notification;

/* Must be set before notification is enabled is set or the string is ignored */
// TODO: just toggle enabled twice to get rid of this requirement?
@property(nonatomic, retain) NSString* notificationText;
@property(nonatomic, assign, getter=isEnabled) BOOL enabled;
@property(nonatomic, assign, getter=isRepeating) BOOL repeating;

- (NSDictionary*)record;
- (instancetype)initWithRecord:(NSDictionary*)record;

@end
