//
//  TSDestination+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 6/14/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSDestination+TSManagedObject.h"

@implementation TSDestination (TSManagedObject)

- (NSString*)travelNoticesUrl {
    return [NSString stringWithFormat:@"%@#%@", self.webLink, self.travelNoticesAnchorName];
}

- (NSString*)description {
    return [NSString stringWithFormat:@"Name list: %@, isAlias: %d, Official: %@, friendly: %@", self.nameList, self.isAlias.boolValue, self.nameOfficial, self.nameFriendly];
}

@end
