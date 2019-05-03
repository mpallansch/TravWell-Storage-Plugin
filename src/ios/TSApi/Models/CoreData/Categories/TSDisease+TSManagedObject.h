//
//  TSDisease+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 8/13/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSDisease.h"

@interface TSDisease (TSManagedObject)

@property (nonatomic, readonly, getter = isDrug) BOOL drug;
@property (nonatomic, readonly) NSString* name;
@property (nonatomic, readonly) NSString* dateCompletedString;

@end
