//
//  TSAppDelegate.h
//  Travel Safe
//
//  Created by James Power on 5/22/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <UIKit/UIKit.h>

#define kManagedObjectContext ((TSAppDelegate*)[UIApplication sharedApplication].delegate).sharedManagedObjectContext
#define kAppDelegate ((TSAppDelegate*)[UIApplication sharedApplication].delegate)

@interface TSAppDelegate : UIResponder<UIApplicationDelegate>

- (void)saveMagicalRecordData;

@property(strong, nonatomic) UIWindow* window;

@end
