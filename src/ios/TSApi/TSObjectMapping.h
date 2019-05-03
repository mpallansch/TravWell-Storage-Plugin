//
//  TSObjectMapping.h
//  Travel Safe
//
//  Created by James Power on 3/4/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TSObjectMapping : NSObject

+ (void)configureObjectManagerWithStore:(RKManagedObjectStore*)store;

@end
