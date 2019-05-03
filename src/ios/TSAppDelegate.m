//
//  TSAppDelegate.m
//  Travel Safe
//
//  Created by James Power on 5/22/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSAppDelegate.h"
#import "DHNotifier.h"
#import "TSObjectMapping.h"
#import "MetricsHandler.h"

// Use a class extension to expose access to MagicalRecord's private setter methods
@interface NSManagedObjectContext ()
+ (void)MR_setRootSavingContext:(NSManagedObjectContext*)context;
+ (void)MR_setDefaultContext:(NSManagedObjectContext*)moc;
@end

@interface TSAppDelegate()
@property(strong, nonatomic) NSManagedObjectContext* sharedManagedObjectContext;
@end

@implementation TSAppDelegate

- (BOOL)application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions {
    // The decision making progress for which UIViewController to start with is handled in TSOverallHomeDashboardViewController

#ifdef DEBUG
//RKLogConfigureByName("RestKit/Network", RKLogLevelTrace);
//RKLogConfigureByName("RestKit/ObjectMapping", RKLogLevelTrace);
#endif
    [NUIAppearance init];
    
    // TODO:
    // Since notifications aren't handled by CoreData there is a chance notifications
    // can be left dangling in the chance of a crash where CoreData hasn't updated reminderIds,
    // in this case I think it's safe to just cancel the dangling notification, maybe something
    //smarter should be done though.
    [DHNotifications cancelUnusedNotifications];

    // Setup CoreData with RestKit+ MagicalRecord
    NSURL* modelURL = [NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"TSCoreDataModels"
                                                                             ofType:@"momd"]];
    NSManagedObjectModel* managedObjectModel = [[[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL] mutableCopy];
    RKManagedObjectStore* managedObjectStore = [[RKManagedObjectStore alloc] initWithManagedObjectModel:managedObjectModel];
    NSString* storePath = [RKApplicationDataDirectory() stringByAppendingPathComponent:@"RKMagicalRecord.sqlite"];
    NSError* error = nil;

    [managedObjectStore addSQLitePersistentStoreAtPath:storePath
                                fromSeedDatabaseAtPath:nil
                                     withConfiguration:nil
                                               options:nil
                                                 error:&error];
    [managedObjectStore createManagedObjectContexts];

    // (hopefully?) Configure MagicalRecord to use RestKit's Core Data stack
    [NSPersistentStoreCoordinator MR_setDefaultStoreCoordinator:managedObjectStore.persistentStoreCoordinator];
    [NSManagedObjectContext MR_setRootSavingContext:managedObjectStore.persistentStoreManagedObjectContext];
    [NSManagedObjectContext MR_setDefaultContext:managedObjectStore.mainQueueManagedObjectContext];

    [RKManagedObjectStore setDefaultStore:managedObjectStore];
    self.sharedManagedObjectContext = managedObjectStore.mainQueueManagedObjectContext;
    [TSObjectMapping configureObjectManagerWithStore:managedObjectStore];
    [MagicalRecord setupAutoMigratingCoreDataStack];

    if (SYSTEM_VERSION_LESS_THAN(@"7.0")) {
        UIImage* img = [[UIImage imageNamed:@"bttn_back.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 8)];
        [[UIBarButtonItem appearance] setBackButtonBackgroundImage:img
                                                          forState:UIControlStateNormal
                                                        barMetrics:UIBarMetricsDefault];
    } else {
        [[UINavigationBar appearance] setTintColor:TSDarkGreenColor];
        [UINavigationBar appearance].titleTextAttributes = @{
            NSForegroundColorAttributeName : [UIColor colorWithRed:102.0 / 255.0
                                                       green:102.0 / 255.0
                                                        blue:102.0 / 255.0
                                                       alpha:1.0],
            NSFontAttributeName : [UIFont fontWithName:@"Arial-BoldMT"
                                                  size:20]
        };
    }

    return YES;
}

- (void)saveMagicalRecordData {
    [[NSManagedObjectContext MR_defaultContext] MR_saveToPersistentStoreWithCompletion:^(BOOL success, NSError *error) {
        if (error) {
            DLog(@"Error: %@: %@", error.localizedDescription, error.localizedFailureReason);
        }
    }];
}

- (void)applicationDidEnterBackground:(UIApplication*)application {
    [self saveMagicalRecordData];
}

- (void)applicationWillTerminate:(UIApplication*)application {
    [MagicalRecord cleanUp];
}

- (void)application:(UIApplication*)application didReceiveLocalNotification:(UILocalNotification*)notification {
    NSDictionary* dict = [notification userInfo];
    NSDate* setDate = dict[KEY_NOTIFICATION_SET_DATE];
    // Verify set date isn't after fire date
    if ([notification.fireDate isLessDate:setDate])
        return;

    DLog(@"Notification %@\nfired with dict: %@", notification, dict);

    if ([dict[KEY_NOTIFICATION_TEXT] length] > 0) {
        [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Reminder", @"Reminder alert title")
                                                        message:dict[KEY_NOTIFICATION_TEXT]
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                          otherButtonTitles:nil] show];
    }
}

@end
