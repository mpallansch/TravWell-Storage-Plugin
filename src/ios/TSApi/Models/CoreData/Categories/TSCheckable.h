//
//  TSCheckable.h
//  Travel Safe
//
//  Created by James Power on 7/23/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol TSCheckable<NSObject>

@property(nonatomic, retain) NSNumber* completed;
- (void)toggleCompleted;

@end
