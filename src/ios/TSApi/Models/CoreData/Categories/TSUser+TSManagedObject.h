//
//  TSUser+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 6/13/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSUser.h"

#define kUser [TSUser sharedUser]

@interface TSUser (TSManagedObject)

extern NSString* const TSUserStartScreenAllTrips;
extern NSString* const TSUserStartScreenNextTrip;

/*!
 * Shared user model.
 */
+ (TSUser*)sharedUser;

/** Returns NSArray of TSAdviceItems that are vaccinations attached to the user. */
@property (nonatomic, readonly) NSArray* vaccinations;

/** Returns NSArray of TSAdviceItems that are medications attached to the user. */
@property (nonatomic, readonly) NSArray* medications;

/** Returns YES iff user has accepted Terms of Service. */
@property (nonatomic, getter = hasAcceptedTerms) BOOL acceptedTerms;

/** Given an array of diseases return only the diseases in the array which the user hasn't already vaccinated for. */
- (NSArray*)missingDiseasesFromDiseases:(NSArray*)diseases;

- (void)updateAdviceItemIfNecessary:(TSAdviceItem*)adviceItem;

/**
 * Update all the users advice items with the same name as the given adviceItem
 * to have the same selectedDrug, reminderId, and dateOfVaccination.
 */
- (void)updateSimilarAdviceItemsWithItem:(TSAdviceItem*)adviceItem;

@end
