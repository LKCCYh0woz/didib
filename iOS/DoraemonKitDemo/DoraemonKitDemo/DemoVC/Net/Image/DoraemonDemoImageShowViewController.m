//
//  DoraemonDemoImageShowViewController.m
//  DoraemonKitDemo
//
//  Created by yixiang on 2019/6/18.
//  Copyright © 2019年 yixiang. All rights reserved.
//

#import "DoraemonDemoImageShowViewController.h"
#import "DoraemonDefine.h"

@interface DoraemonDemoImageShowViewController ()

@property (nonatomic, strong) UIImageView *imageView;

@end

@implementation DoraemonDemoImageShowViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setOriginalImage:_image];

}

- (void)setOriginalImage:(UIImage *)originalImage {
    CGFloat viewWidth = self.view.doraemon_width;
    CGFloat viewHeight = self.view.doraemon_height;
    CGFloat imageWidth = originalImage.size.width;
    CGFloat imageHeight = originalImage.size.height;
    BOOL isPortrait = imageHeight / viewHeight > imageWidth / viewWidth;
    CGFloat scaledImageWidth, scaledImageHeight;
    CGFloat x,y;
    CGFloat imageScale;
    if (isPortrait) {//图片竖屏分量比较大
        imageScale = imageHeight / viewHeight;
        scaledImageHeight = viewHeight;
        scaledImageWidth = imageWidth / imageScale;
        x = (viewWidth - scaledImageWidth) / 2;
        y = 0;
    } else {//图片横屏分量比较大
        imageScale = imageWidth / viewWidth;
        scaledImageWidth = viewWidth;
        scaledImageHeight = imageHeight / imageScale;
        x = 0;
        y = (viewHeight - scaledImageHeight) / 2;
    }
    _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(x, y, scaledImageWidth, scaledImageHeight)];
    _imageView.image = originalImage;
    _imageView.userInteractionEnabled = YES;
    [self.view addSubview:_imageView];
}


@end
