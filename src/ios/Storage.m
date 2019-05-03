#import "Storage.h"
#import "TSApi/Models/CoreData/TSDisease.h"
#import <MagicalRecord/MagicalRecord.h>
#import <Cordova/CDVPlugin.h>

@implementation Storage

- (void)getPreviousStorage:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* message = [command.arguments objectAtIndex:0];

    [MagicalRecord enableShorthandMethods];

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

    NSArray* diseases = [TSDisease MR_findAll];

    if (message != nil && [message length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end