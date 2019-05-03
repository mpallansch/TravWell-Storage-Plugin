//
//  TSObjectMapping.m
//  Travel Safe
//
//  Created by James Power on 3/4/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import "TSObjectMapping.h"

@implementation TSObjectMapping

+ (RKEntityMapping*)diseaseMappingWithStore:(RKManagedObjectStore*)store {
    static RKEntityMapping* diseaseMapping;

    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        diseaseMapping = [RKEntityMapping mappingForEntityForName:@"TSDisease"
                                                              inManagedObjectStore:store];
        [diseaseMapping addAttributeMappingsFromDictionary:@{
                                                             @"DiseaseListName" : @"diseaseListName",
                                                             @"FindOutWhyHtml" : @"findOutWhyHtml",
                                                             @"FriendlyName" : @"friendlyName",
                                                             @"GroupText" : @"groupText",
                                                             @"DiseasePageUrl" : @"diseasePageUrl",
                                                             }];
        
        RKEntityMapping* drugMapping = [RKEntityMapping mappingForEntityForName:@"TSDrug"
                                                           inManagedObjectStore:store];
        [drugMapping addAttributeMappingsFromDictionary:@{
                                                          @"AlertText" : @"alertText",
                                                          @"DisplayName" : @"displayName",
                                                          @"Duration" : @"duration",
                                                          @"FriendlyName" : @"friendlyName",
                                                          @"ReminderInstructions" : @"reminderInstructions",
                                                          @"SearchTerms" : @"searchTerms",
                                                          }];
        
        RKEntityMapping* intervalMapping = [RKEntityMapping mappingForEntityForName:@"TSReminderInterval"
                                                               inManagedObjectStore:store];
        [intervalMapping addAttributeMappingsFromDictionary:@{
                                                              @"DaysOut" : @"daysOut",
                                                              @"Type" : @"type",
                                                              }];
        
        [drugMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"Interval"
                                                                                    toKeyPath:@"interval"
                                                                                  withMapping:intervalMapping]];
        [diseaseMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"Drugs"
                                                                                       toKeyPath:@"drugs"
                                                                                     withMapping:drugMapping]];
        
    });

    return diseaseMapping;
}

+ (void)setupDestinationMappingWithObjectManager:(RKObjectManager*)objectManager andStore:(RKManagedObjectStore*)store {
    /////////////////////////
    // Setup TSDestination //
    /////////////////////////
    RKEntityMapping* destinationMapping = [RKEntityMapping mappingForEntityForName:@"TSDestination"
                                                              inManagedObjectStore:store];
    [destinationMapping addAttributeMappingsFromDictionary:@{
                                                             @"Letter" : @"letter",
                                                             @"NameFriendly" : @"nameFriendly",
                                                             @"NameList" : @"nameList",
                                                             @"NameOfficial" : @"nameOfficial",
                                                             @"NamePlugin" : @"namePlugin",
                                                             @"ISOA2" : @"isoA2",
                                                             @"IsAlias" : @"isAlias",
                                                             @"ParentName" : @"parentName",
                                                             @"FlagUrlSmall" : @"flagUrl",
                                                             @"WebLink" : @"webLink",
                                                             @"TravelNoticesAnchorName" : @"travelNoticesAnchorName",
                                                             @"NameYellowFeverMalariaTable" : @"nameYellowFeverMalariaTable",
                                                             @"EmergencyNumbers" : @"emergencyNumbers",
                                                             }];



    [destinationMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"Diseases"
                                                                                       toKeyPath:@"diseases"
                                                                                     withMapping:[self diseaseMappingWithStore:store]]];

    // Setup packing lists

    RKEntityMapping* packingListMapping = [RKEntityMapping mappingForEntityForName:@"TSPackingList"
                                                              inManagedObjectStore:store];
    [packingListMapping addAttributeMappingsFromDictionary:@{
                                                             @"PackingListId" : @"packingListId",
                                                             @"friendlyName" : @"friendlyName",
                                                             }];

    RKEntityMapping* packingListSuperGroupMapping = [RKEntityMapping mappingForEntityForName:@"TSPackingListSuperGroup"
                                                                        inManagedObjectStore:store];
    [packingListSuperGroupMapping addAttributeMappingsFromDictionary:@{
                                                                       @"SuperGroupId" : @"superGroupId",
                                                                       @"SortOrder" : @"sortOrder",
                                                                       @"SuperGroupText" : @"superGroupText",
                                                                       }];

    RKEntityMapping* packingListGroupMapping = [RKEntityMapping mappingForEntityForName:@"TSPackingListGroup"
                                                                   inManagedObjectStore:store];
    [packingListGroupMapping addAttributeMappingsFromDictionary:@{
                                                                  @"GroupId" : @"groupId",
                                                                  @"SortOrder" : @"sortOrder",
                                                                  @"GroupText" : @"groupText",
                                                                  }];

    RKEntityMapping* packingListItem = [RKEntityMapping mappingForEntityForName:@"TSPackingListItem"
                                                           inManagedObjectStore:store];
    [packingListItem addAttributeMappingsFromDictionary:@{
                                                          @"ItemId" : @"itemId",
                                                          @"DisplayName" : @"name",
                                                          @"DescriptionContent" : @"descriptionContent",
                                                          @"AppSpecificContent" : @"appSpecificContent",
                                                          @"SortOrder" : @"sortOrder",
                                                          }];

    [packingListGroupMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"Items"
                                                                                            toKeyPath:@"items"
                                                                                          withMapping:packingListItem]];
    [packingListSuperGroupMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"Groups"
                                                                                                 toKeyPath:@"groups"
                                                                                               withMapping:packingListGroupMapping]];
    [packingListMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"SuperGroups"
                                                                                       toKeyPath:@"superGroups"
                                                                                     withMapping:packingListSuperGroupMapping]];
    [destinationMapping addPropertyMapping:[RKRelationshipMapping relationshipMappingFromKeyPath:@"PackingList"
                                                                                       toKeyPath:@"packingList"
                                                                                     withMapping:packingListMapping]];

    RKResponseDescriptor* bigListDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:destinationMapping
                                                                                           method:RKRequestMethodGET
                                                                                      pathPattern:@"/travel/api/1/destinations/list/json"
                                                                                          keyPath:@"Destinations"
                                                                                      statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [objectManager addResponseDescriptor:bigListDescriptor];

    RKResponseDescriptor* individualDestinationDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:destinationMapping
                                                                                                         method:RKRequestMethodGET
                                                                                                    pathPattern:@"/travel/api/1/destination/:nameFriendly/json"
                                                                                                        keyPath:nil
                                                                                                    statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [objectManager addResponseDescriptor:individualDestinationDescriptor];
}

+ (void)setupDiseaseListMappingWithObjectManager:(RKObjectManager*)objectManager andStore:(RKManagedObjectStore*)store {
    RKResponseDescriptor* diseaseResponseDescriptor = [RKResponseDescriptor responseDescriptorWithMapping:[self diseaseMappingWithStore:store]
                                                                                                         method:RKRequestMethodGET
                                                                                                    pathPattern:@"/travel/api/1/meddiseasedrug/list/json"
                                                                                                        keyPath:nil
                                                                                                    statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    [objectManager addResponseDescriptor:diseaseResponseDescriptor];
}

+ (void)configureObjectManagerWithStore:(RKManagedObjectStore *)store {
    RKObjectManager* objectManager = [RKObjectManager managerWithBaseURL:[NSURL URLWithString:@"https://wwwnc.cdc.gov"]];
    objectManager.managedObjectStore = store;
    [RKObjectManager setSharedManager:objectManager];

    [self setupDestinationMappingWithObjectManager:objectManager andStore:store];
    [self setupDiseaseListMappingWithObjectManager:objectManager andStore:store];
}

@end
