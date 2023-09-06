//
//  DoraemonCacheManager.h
//  DoraemonKit-DoraemonKit
//
//  Created by yixiang on 2017/12/12.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface DoraemonCacheManager : NSObject

+ (DoraemonCacheManager *)sharedInstance;

- (void)saveLoggerSwitch:(BOOL)on;

- (BOOL)loggerSwitch;

- (void)saveMockGPSSwitch:(BOOL)on;

- (BOOL)mockGPSSwitch;

- (void)saveMockCoordinate:(CLLocationCoordinate2D)coordinate;

- (CLLocationCoordinate2D)mockCoordinate;

- (void)saveFpsSwitch:(BOOL)on;

- (BOOL)fpsSwitch;

- (void)saveCpuSwitch:(BOOL)on;

- (BOOL)cpuSwitch;

- (void)saveMemorySwitch:(BOOL)on;

- (BOOL)memorySwitch;

- (void)saveNetFlowSwitch:(BOOL)on;

- (BOOL)netFlowSwitch;

- (void)saveLargeImageDetectionSwitch:(BOOL)on;

- (BOOL)largeImageDetection;

- (void)saveSubThreadUICheckSwitch:(BOOL)on;

- (BOOL)subThreadUICheckSwitch;

- (void)saveCrashSwitch:(BOOL)on;

- (BOOL)crashSwitch;

- (void)saveNSLogSwitch:(BOOL)on;

- (BOOL)nsLogSwitch;

- (void)saveMethodUseTimeSwitch:(BOOL)on;

- (BOOL)methodUseTimeSwitch;

@end
