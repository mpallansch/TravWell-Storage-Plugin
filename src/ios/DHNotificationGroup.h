//
//  DHNotificationModel.h
//  DHNotifications
//
//  Created by James Power on 1/2/13.
//  Copyright (c) 2013 Duet Health. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DHNotifications.h"

@class DHNotifier;

@interface DHNotificationGroup : NSObject

/* Date the user initially selects when setting up the group. So if they repeat
 * the reminder the time of this date is used. They can delete all the notifiers
 * but the original date will still be around.
 */
@property(nonatomic, assign) DHNotificationRepeatInterval interval;
@property(nonatomic, strong) NSMutableArray* notifiers;
@property(nonatomic, copy) NSString* groupId;
@property(nonatomic, copy) NSString* name;
@property(nonatomic, copy) NSString* notificationText;
@property(nonatomic, copy) NSDate* canonicalDate;

typedef void (^YesBlock)();
typedef void (^NoBlock)();

- (void)isPartlyEnabledWithYes:(YesBlock)yes andNo:(NoBlock)no;

- (void)save;
- (NSMutableDictionary*)record;

- (instancetype)initWithRecord:(NSMutableDictionary*)record;
- (instancetype)initWithInterval:(DHNotificationRepeatInterval)i andCount:(int)count andGroupId:(NSString*)identifier;
- (instancetype)initWithInterval:(DHNotificationRepeatInterval)interval andCount:(int)count;
- (instancetype)initWithDate:(NSDate*)date andNotificationText:(NSString*)text andInterval:(DHNotificationRepeatInterval)interval;
- (instancetype)initWithDates:(NSArray*)dates andNotificationText:(NSString*)text andInterval:(DHNotificationRepeatInterval)interval;
- (void)addNotifierWithDate:(NSDate*)date;
- (void)removeNotifier:(DHNotifier*)notifier;
- (void)setEnabled:(BOOL)enabled;
- (BOOL)isEnabled;
- (void)setNotificationText:(NSString*)text;


@end