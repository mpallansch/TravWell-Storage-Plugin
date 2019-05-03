//
//  TSPackingListItem.h
//  Travel Safe
//
//  Created by James Power on 3/4/14.
//  Copyright (c) 2014 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class TSPackingListGroup, TSTrip;

@interface TSPackingListItem : NSManagedObject

@property (nonatomic, retain) NSString * appSpecificContent;
@property (nonatomic, retain) NSString * category;
@property (nonatomic, retain) NSNumber * completed;
@property (nonatomic, retain) NSString * descriptionContent;
@property (nonatomic, retain) NSString * itemId;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * sortOrder;
@property (nonatomic, retain) NSNumber * userModifiedCategory;
@property (nonatomic, retain) TSPackingListGroup *group;
@property (nonatomic, retain) TSTrip *trip;

@end
