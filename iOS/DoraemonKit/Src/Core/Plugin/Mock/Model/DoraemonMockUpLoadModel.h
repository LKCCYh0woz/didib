//
//  DoraemonMockUpLoadModel.h
//  AFNetworking
//
//  Created by didi on 2019/11/15.
//

#import "DoraemonMockBaseModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface DoraemonMockUpLoadModel : DoraemonMockBaseModel

@property (nonatomic, assign) BOOL existMock;//本地是否存在mock数据
@property (nonatomic, copy) NSString *result;//本地a保存的mock数据

@end

NS_ASSUME_NONNULL_END
