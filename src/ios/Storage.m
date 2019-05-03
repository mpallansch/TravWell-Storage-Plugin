#import "Storage.h"
#import "TSApi/Models/CoreData/TSDisease.h"
#import <Cordova/CDVPlugin.h>

@implementation Storage

- (void)getPreviousStorage:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* message = [command.arguments objectAtIndex:0];

    NSArray* diseases = [TSDisease MR_findAll];

    if (message != nil && [message length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end