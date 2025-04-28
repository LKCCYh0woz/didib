//
//  DoraemonMockDataViewController.m
//  AFNetworking
//
//  Created by didi on 2019/11/5.
//

#import "DoraemonMockAPIViewController.h"
#import "DoraemonDefine.h"
#import "DoraemonMockApiListView.h"


@interface DoraemonMockAPIViewController()

@property (nonatomic, strong) DoraemonMockApiListView *detailView;
@property (nonatomic, assign) CGFloat padding_left;

@end

@implementation DoraemonMockAPIViewController

- (void)viewDidLoad{
    [super viewDidLoad];
    
    _detailView = [[DoraemonMockApiListView alloc] initWithFrame:CGRectMake(0, self.sepeatorLine.doraemon_bottom, self.view.doraemon_width, self.view.doraemon_height - self.sepeatorLine.doraemon_bottom)];
    [self.view addSubview:_detailView];
    
    NSString *leftTitle = [DoraemonMockManager sharedInstance].mockGroup;
    if ([leftTitle isEqualToString:@"所有"]) {
        leftTitle = @"接口分组";
    }
    [self.leftButton renderUIWithTitle:leftTitle];
    
    NSString *rightTitle = [DoraemonMockManager sharedInstance].mockState;
    if ([rightTitle isEqualToString:@"所有"]) {
        rightTitle = @"开关状态";
    }
    [self.rightButton renderUIWithTitle:rightTitle];
}

#pragma mark --DoraemonMockFilterBgroundDelegate
- (void)selectedClick{
    if(self.rightButton.down){
        NSString *rightTitle = [DoraemonMockManager sharedInstance].states[self.listView.selectedIndex];
        [DoraemonMockManager sharedInstance].mockState = rightTitle;
        if ([rightTitle isEqualToString:@"所有"]) {
            rightTitle = @"开关状态";
        }
        [self.rightButton renderUIWithTitle:rightTitle];
    }else{
        NSString *leftTitle = [DoraemonMockManager sharedInstance].groups[self.listView.selectedIndex];
        [DoraemonMockManager sharedInstance].mockGroup = leftTitle;
        if ([leftTitle isEqualToString:@"所有"]) {
            leftTitle = @"接口分组";
        }
        [self.leftButton renderUIWithTitle:leftTitle];
    }
    
    [super selectedClick];

}


@end
