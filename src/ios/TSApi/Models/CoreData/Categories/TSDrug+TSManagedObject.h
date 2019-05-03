//
//  TSDrug+TSManagedObject.h
//  Travel Safe
//
//  Created by James Power on 8/7/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSDrug.h"

@interface TSDrug (TSManagedObject)

/**
 * Return reminder instructions stripped of all HTML.
 */
- (NSString*)reminderInstructions;

@end
