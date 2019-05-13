#import "DHNotifications.h"
#import "Storage.h"
#import "TSTrip.h"
#import "TSDisease.h"
#import "TSDrug.h"
#import "TSAdviceItem.h"
#import "TSToDoItem.h"
#import "TSObjectMapping.h"
#import "TSPackingListItem.h"
#import "TSPackingListGroup.h"
#import "TSPackingListSuperGroup.h"
#import "TSReminderInterval.h"
#import <Cordova/CDVPlugin.h>

// Use a class extension to expose access to MagicalRecord's private setter methods
@interface NSManagedObjectContext ()
+ (void)MR_setRootSavingContext:(NSManagedObjectContext*)context;
+ (void)MR_setDefaultContext:(NSManagedObjectContext*)moc;
@end

@interface Storage()
@property(strong, nonatomic) NSManagedObjectContext* sharedManagedObjectContext;
@end

@implementation Storage

- (void)getPreviousStorage:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* message = [command.arguments objectAtIndex:0];
    
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
    
    NSMutableArray* destinationObjects = [[NSMutableArray alloc] init];
    NSMutableArray* destinationTripIds = [[NSMutableArray alloc] init];
    
    NSMutableArray* docObjects = [[NSMutableArray alloc] init];
    NSMutableArray* docTripIds = [[NSMutableArray alloc] init];
    
    NSMutableArray* adviceObjects = [[NSMutableArray alloc] init];
    NSMutableArray* adviceTripIds = [[NSMutableArray alloc] init];
    
    NSMutableArray* packingObjects = [[NSMutableArray alloc] init];
    NSMutableArray* packingTripIds = [[NSMutableArray alloc] init];
    
    NSMutableArray* todoObjects = [[NSMutableArray alloc] init];
    NSMutableArray* todoTripIds = [[NSMutableArray alloc] init];
    
    NSMutableArray* groupNames = [[NSMutableArray alloc] init];
    NSMutableArray* groupIsTodos = [[NSMutableArray alloc] init];
    
    NSString *JSONString = @"{";
    
    JSONString = [JSONString stringByAppendingString:@"\"trips\": ["];
    NSArray* trips = [TSTrip MR_findAll];
    NSUInteger tripCount = 0;
    for(id trip in trips){
        JSONString = [JSONString stringByAppendingString:@"{\"id\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long)(tripCount + 1)]];
        JSONString = [JSONString stringByAppendingString:@","];
        JSONString = [JSONString stringByAppendingString:@"\"profile\": 1,"];
        JSONString = [JSONString stringByAppendingString:@"\"startTime\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long)[[trip valueForKey:@"startDate"] timeIntervalSince1970] * 1000]];
        JSONString = [JSONString stringByAppendingString:@","];
        JSONString = [JSONString stringByAppendingString:@"\"endTime\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long)[[trip valueForKey:@"endDate"] timeIntervalSince1970] * 1000]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"name\": \""];
        NSArray* destinations = [trip valueForKey:@"destinations"];
        NSUInteger destinationCount = 0;
        for(id destination in destinations){
            JSONString = [JSONString stringByAppendingString:[destination valueForKey:@"nameOfficial"]];
            if(destinationCount != (destinations.count - 1)) {
                JSONString = [JSONString stringByAppendingString:@" - "];
            }
            
            [destinationObjects addObject:destination];
            [destinationTripIds addObject:@(tripCount + 1)];
            
            destinationCount++;
        }
        JSONString = [JSONString stringByAppendingString:@"\"}"];
        
        NSArray* docs = [trip valueForKey:@"docs"];
        NSUInteger docsCount = 0;
        for(id doc in docs){
            [docObjects addObject:doc];
            [docTripIds addObject:@(tripCount + 1)];
            docsCount++;
        }
        
        NSArray* advices = [trip valueForKey:@"adviceItems"];
        NSUInteger advicesCount = 0;
        for(id advice in advices){
            [adviceObjects addObject:advice];
            [adviceTripIds addObject:@(tripCount + 1)];
            advicesCount++;
        }
        
        NSArray* packings = [trip valueForKey:@"packingListItems"];
        NSUInteger packingsCount = 0;
        for(id packing in packings){
            [packingObjects addObject:packing];
            [packingTripIds addObject:@(tripCount + 1)];
            packingsCount++;
        }
        
        NSArray* todos = [trip valueForKey:@"toDoItems"];
        NSUInteger todosCount = 0;
        for(id todo in todos){
            [todoObjects addObject:todo];
            [todoTripIds addObject:@(tripCount + 1)];
            todosCount++;
        }
        
        if(tripCount != (trips.count - 1)){
            JSONString = [JSONString stringByAppendingString:@","];
        }
        tripCount++;
    }
    JSONString = [JSONString stringByAppendingString:@"], \"destinations\": ["];
    
    NSUInteger destinationCount = 0;
    for(id destination in destinationObjects){
        JSONString = [JSONString stringByAppendingString:@"{\"trip\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long) [[destinationTripIds objectAtIndex:destinationCount] integerValue]]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"nameFriendly\": \""];
        JSONString = [JSONString stringByAppendingString:[destination valueForKey:@"nameFriendly"]];
        JSONString = [JSONString stringByAppendingString:@"\"}"];
        
        if(destinationCount != (destinationObjects.count - 1)){
            JSONString = [JSONString stringByAppendingString:@","];
        }
        destinationCount++;
    }
    
    JSONString = [JSONString stringByAppendingString:@"], \"documents\": ["];
    
    NSUInteger docsCount = 0;
    for(id doc in docObjects){
        JSONString = [JSONString stringByAppendingString:@"{\"trip\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long) [[docTripIds objectAtIndex:docsCount] integerValue]]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"name\": \""];
        JSONString = [JSONString stringByAppendingString:[doc valueForKey:@"name"]];
        JSONString = [JSONString stringByAppendingString:@"\","];
        
        JSONString = [JSONString stringByAppendingString:@"\"localImagePath\": \""];
        JSONString = [JSONString stringByAppendingString:[doc valueForKey:@"fileName"]];
        JSONString = [JSONString stringByAppendingString:@"\"}"];
        
        if(docsCount != (docObjects.count - 1)){
            JSONString = [JSONString stringByAppendingString:@","];
        }
        docsCount++;
    }
    
    JSONString = [JSONString stringByAppendingString:@"], \"drugs\": ["];
    
    NSUInteger adviceCount = 0;
    for(id advice in adviceObjects){
        
        TSDisease* disease = [advice valueForKey:@"disease"];
        NSOrderedSet* drugs = [disease valueForKey:@"drugs"];
        if(drugs.count > 0){
            TSDrug* drug = [drugs objectAtIndex:0];
            
            JSONString = [JSONString stringByAppendingString:@"{\"friendlyName\": \""];
            JSONString = [JSONString stringByAppendingString:[drug valueForKey:@"friendlyName"]];
            JSONString = [JSONString stringByAppendingString:@"\","];
            
            JSONString = [JSONString stringByAppendingString:@"\"trip\": null,"];
            
            JSONString = [JSONString stringByAppendingString:@"\"isCompleted\": "];
            if([advice valueForKey:@"completed"]){
                JSONString = [JSONString stringByAppendingString:@"true"];
            } else {
                JSONString = [JSONString stringByAppendingString:@"false"];
            }
            JSONString = [JSONString stringByAppendingString:@","];
            
            JSONString = [JSONString stringByAppendingString:@"\"timeStarted\": "];
            JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long)[[disease valueForKey:@"dateOfVaccination"] timeIntervalSince1970] * 1000]];
            JSONString = [JSONString stringByAppendingString:@","];
            
            JSONString = [JSONString stringByAppendingString:@"\"notes\": \""];
            if([advice valueForKey:@"notes"] != nil){
                JSONString = [JSONString stringByAppendingString:[advice valueForKey:@"notes"]];
            }
            JSONString = [JSONString stringByAppendingString:@"\"}"];
            
            
        } else {
            JSONString = [JSONString stringByAppendingString:@"{}"];
        }
        
        if(adviceCount != (adviceObjects.count - 1)){
            JSONString = [JSONString stringByAppendingString:@","];
        }
        adviceCount++;
    }
    
    JSONString = [JSONString stringByAppendingString:@"], \"packingItems\": ["];
    
    NSUInteger itemsCount = 0;
    for(id item in packingObjects){
        
        JSONString = [JSONString stringByAppendingString:@"{\"trip\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long) [[packingTripIds objectAtIndex:itemsCount] integerValue]]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"packingSuperGroup\": "];
        
        NSUInteger groupCount = 0;
        bool found = false;
        for(NSString* name in groupNames){
            if([name isEqualToString:[item valueForKey:@"category"]]){
                found = true;
                break;
            }
            groupCount++;
        }
        if(!found){
            [groupNames addObject:[item valueForKey:@"category"]];
            [groupIsTodos addObject:@(false)];
            groupCount = (groupNames.count - 1);
        }
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long) groupCount]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"displayName\": \""];
        JSONString = [JSONString stringByAppendingString:[item valueForKey:@"name"]];
        JSONString = [JSONString stringByAppendingString:@"\","];
        
        JSONString = [JSONString stringByAppendingString:@"\"isCompleted\": "];
        if([[item valueForKey:@"completed"] integerValue] == 1){
            JSONString = [JSONString stringByAppendingString:@"true"];
        } else {
            JSONString = [JSONString stringByAppendingString:@"false"];
        }
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"descriptionContent\": \""];
        if([item valueForKey:@"descriptionContent"]  != nil){
            JSONString = [JSONString stringByAppendingString:[[item valueForKey:@"descriptionContent"] stringByReplacingOccurrencesOfString:@"\"" withString:@""]];
        }
        JSONString = [JSONString stringByAppendingString:@"\","];
        
        JSONString = [JSONString stringByAppendingString:@"\"notes\": \"null\",\"isTodo\": false},"];
        
        itemsCount++;
    }
    
    NSUInteger todosCount = 0;
    for(id todo in todoObjects){
        
        JSONString = [JSONString stringByAppendingString:@"{\"trip\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long) [[todoTripIds objectAtIndex:todosCount] integerValue]]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        NSString* foo = [todo valueForKey:@"reminderId"];
        [DHNotifications notificationWithId:foo];
        
        JSONString = [JSONString stringByAppendingString:@"\"packingSuperGroup\": "];
        
        NSUInteger groupCount = 0;
        bool found = false;
        for(NSString* name in groupNames){
            if([name isEqualToString:[todo valueForKey:@"category"]]){
                found = true;
                break;
            }
            groupCount++;
        }
        if(!found){
            [groupNames addObject:[todo valueForKey:@"category"]];
            [groupIsTodos addObject:@(true)];
            groupCount = (groupNames.count - 1);
        }
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long) groupCount]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"displayName\": \""];
        JSONString = [JSONString stringByAppendingString:[todo valueForKey:@"name"]];
        JSONString = [JSONString stringByAppendingString:@"\","];
        
        JSONString = [JSONString stringByAppendingString:@"\"notes\": \""];
        if([todo valueForKey:@"notes"] != nil){
            JSONString = [JSONString stringByAppendingString:[todo valueForKey:@"notes"]];
        } else {
            JSONString = [JSONString stringByAppendingString:@"null"];
        }
        JSONString = [JSONString stringByAppendingString:@"\","];
        
        JSONString = [JSONString stringByAppendingString:@"\"isCompleted\": "];
        if([[todo valueForKey:@"completed"] integerValue] == 1){
            JSONString = [JSONString stringByAppendingString:@"true"];
        } else {
            JSONString = [JSONString stringByAppendingString:@"false"];
        }
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"descriptionContent\": \"\", \"isTodo\": true}"];
        
        if(todosCount != (todoObjects.count - 1)){
            JSONString = [JSONString stringByAppendingString:@","];
        }
        todosCount++;
    }
    
    JSONString = [JSONString stringByAppendingString:@"], \"packingSuperGroups\": ["];
    
    NSUInteger groupsCount = 0;
    for(NSString* groupName in groupNames){
        
        JSONString = [JSONString stringByAppendingString:@"{\"id\": "];
        JSONString = [JSONString stringByAppendingString:[NSString stringWithFormat:@"%lu", (unsigned long) groupsCount]];
        JSONString = [JSONString stringByAppendingString:@","];
        
        JSONString = [JSONString stringByAppendingString:@"\"superGroupText\": \""];
        JSONString = [JSONString stringByAppendingString:groupName];
        JSONString = [JSONString stringByAppendingString:@"\","];
        
        JSONString = [JSONString stringByAppendingString:@"\"isTodo\": "];
        
        if([[groupIsTodos objectAtIndex:groupsCount] boolValue] == true){
            JSONString = [JSONString stringByAppendingString:@"true}"];
        } else {
            JSONString = [JSONString stringByAppendingString:@"false}"];
        }
        
        if(groupsCount != (groupNames.count - 1)){
            JSONString = [JSONString stringByAppendingString:@","];
        }
        groupsCount++;
    }
    
    JSONString = [JSONString stringByAppendingString:@"]"];
    
    JSONString = [JSONString stringByAppendingString:@"}"];
    
    if (JSONString != nil && [JSONString length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:JSONString];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (NSString*)objectToJSONString:(id) object types:(NSArray*) types keys:(NSArray*) keys{
    NSUInteger count = 0;
    NSString *JSONString = @"{";
    for(NSString *key in keys){
        JSONString = [JSONString stringByAppendingString:@"\""];
        JSONString = [JSONString stringByAppendingString:key];
        JSONString = [JSONString stringByAppendingString:@"\": "];
        if([[types objectAtIndex:count] isEqualToString:@"string"]){
            JSONString = [JSONString stringByAppendingString:@"\""];
            JSONString = [JSONString stringByAppendingString:[object valueForKey:key]];
            JSONString = [JSONString stringByAppendingString:@"\""];
        }
        if(count != (keys.count - 1)){
            JSONString = [JSONString stringByAppendingString:@","];
        }
        count++;
    }
    JSONString = [JSONString stringByAppendingString:@"}"];
    return JSONString;
}

@end
