//
//  TSTripItem.h
//  Travel Safe
//
//  Created by James Power on 7/23/13.
//  Copyright (c) 2013 CDC & ORISE. All rights reserved.
//

#import <Foundation/Foundation.h>

@class TSTrip;

@protocol TSTripItem<NSObject>

@property(nonatomic, strong) NSString* name;
@property(nonatomic, strong) TSTrip* trip;

+ (NSArray*)itemsForTrip:(TSTrip*)trip;
+ (NSString*)defaultNewItemName;
+ (id<TSTripItem>)defaultNewItem;
+ (NSString*)editItemViewControllerIdentifier;
+ (NSString*)emptyHeaderText;

/**
 * If a specific trip is given
 */
- (void) deleteItemFromTrip:(TSTrip*)trip;

@end
