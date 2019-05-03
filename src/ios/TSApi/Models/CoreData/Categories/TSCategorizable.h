//
//  TSCategorizable.h
//  Travel Safe
//
//  Created by James Power on 8/14/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol TSCategorizable<NSObject>

+ (NSArray*)orderedCategoriesForTrip:(TSTrip*)trip;

@end
