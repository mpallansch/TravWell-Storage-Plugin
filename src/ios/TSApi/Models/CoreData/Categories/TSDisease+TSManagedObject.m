//
//  TSDisease+TSManagedObject.m
//  Travel Safe
//
//  Created by James Power on 8/13/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import "TSDisease+TSManagedObject.h"
#import "NSString+HTML.h"

@implementation TSDisease (TSManagedObject)

- (NSString*)findOutWhyHtml {
    // Necessary for handling overriding methods for CoreData model properties
    [self willAccessValueForKey:@"findOutWhyHtml"];
    NSString* findOutWhyHtml = [self primitiveValueForKey:@"findOutWhyHtml"];
    [self didAccessValueForKey:@"findOutWhyHtml"];
    return [findOutWhyHtml stringByDecodingHTMLEntities];
}

- (NSString*)name {
    return self.diseaseListName;
}

- (BOOL)isDrug {
    return [self.diseaseListName isEqualToString:@"Malaria"];
}

- (NSString*)dateCompletedString {
    static NSDateFormatter* formatter;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        formatter = [[NSDateFormatter alloc] init];
        [formatter setDateStyle:NSDateFormatterShortStyle];
    });

    return [formatter stringFromDate:self.dateOfVaccination];
}

@end
