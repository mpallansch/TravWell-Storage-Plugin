//
//  TSUser+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 6/13/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSUser+TSManagedObject.h"
#import "TSAdviceItem+TSManagedObject.h"
#import "TSDrug+TSManagedObject.h"
#import "TSDisease.h"

@implementation TSUser (TSManagedObject)

NSString* const TSUserStartScreenAllTrips = @"user_start_all_trips";
NSString* const TSUserStartScreenNextTrip = @"user-start_next_trip";

static NSString* kAcceptedTermsKey = @"k_accepted_terms_key_v1";

/*!
 * Singleton user object. Should only ever be one user for the application. Basically a placeholder
 * for shared state.
 */
+ (TSUser*)sharedUser {
    static TSUser* user;

    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        user = [[TSUser MR_findAll] firstObject];
        if (!user) {
            user = [TSUser MR_createEntity];
            user.startScreen = TSUserStartScreenNextTrip;
        }
    });

    return user;
}

- (NSArray*)missingDiseasesFromDiseases:(NSArray*)diseases {
    NSMutableArray* missingDiseases = [NSMutableArray array];

    NSMutableSet* currentVaccinationDiseaseNames = [NSMutableSet set];
    for (TSAdviceItem* item in self.vaccinations) {
        [currentVaccinationDiseaseNames addObject:item.disease.diseaseListName];
    }

    for (TSDisease* disease in diseases) {
        if (![currentVaccinationDiseaseNames containsObject:disease.diseaseListName]) {
            [missingDiseases addObject:disease];
        }
    }

    return [NSArray arrayWithArray:missingDiseases];
}

- (NSArray*)vaccinations {
    NSMutableSet* adviceItemNames = [NSMutableSet set];
    return [[TSAdviceItem MR_findAll] bk_select:^BOOL(TSAdviceItem* adviceItem) {
        BOOL alreadyAdded = [adviceItemNames containsObject:adviceItem.name];
        [adviceItemNames addObject:adviceItem.name];

        return !adviceItem.drug && !alreadyAdded && adviceItem.disease.selectedDrug != nil;
    }];
}

- (NSArray*)medications {
    return self.medicineTodos.allObjects;
}

- (BOOL)hasAcceptedTerms {
    return [[NSUserDefaults standardUserDefaults] boolForKey:kAcceptedTermsKey];
}

- (void)setAcceptedTerms:(BOOL)acceptedTerms {
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:kAcceptedTermsKey];
}

- (void)updateAdviceItemIfNecessary:(TSAdviceItem*)adviceItem {
    // Attach this tripItem to the user, but only if it isn't an anti-malarial -
    // anti-malarials need done multiple times, potentially - also Malarials are our only drugs
    if (adviceItem.drug) { return; }

    for (TSAdviceItem* item in self.adviceItems) {
        if ([item.name isEqualToString:adviceItem.name]) {
            adviceItem.disease.selectedDrug = item.disease.selectedDrug;
            adviceItem.reminderId = item.reminderId;
            adviceItem.disease.dateOfVaccination = item.disease.dateOfVaccination;
        }
    }

    adviceItem.user = self;
}

- (void)updateSimilarAdviceItemsWithItem:(TSAdviceItem*)adviceItem {
    for (TSAdviceItem* item in self.adviceItems) {
        if ([item.name isEqualToString:adviceItem.name]) {
            item.disease.selectedDrug = adviceItem.disease.selectedDrug;
            item.reminderId = adviceItem.reminderId;
            item.disease.dateOfVaccination = adviceItem.disease.dateOfVaccination;
        }
    }
}

@end
