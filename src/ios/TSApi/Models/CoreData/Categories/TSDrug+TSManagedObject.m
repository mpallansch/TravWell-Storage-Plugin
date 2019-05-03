//
//  TSDrug+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 8/7/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSDrug+TSManagedObject.h"
#import "NSString+HTML.h"

@implementation TSDrug (TSManagedObject)

- (NSString*)reminderInstructions {
    // Necessary for handling overriding methods for CoreData model properties
    [self willAccessValueForKey:@"reminderInstructions"];
    NSString* reminderInstructions = [self primitiveValueForKey:@"reminderInstructions"];
    [self didAccessValueForKey:@"reminderInstructions"];
    return [reminderInstructions stringByDecodingHTMLEntities];
}

@end