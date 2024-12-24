@echo off
cls
title L2Jcn快速编译: 如果无法正常使用,请安装Ant并配置系统环境变量
echo ====================================================
echo 正在编译........编译完成后文件自动存放在\build目录下
echo 编译完毕后窗口自动关闭,编译日志请查看compile.log文件
echo 如果不能正常编译,请至www.l2jcn.com提供错误信息
echo ====================================================
color 0F
ant -f build.xml -l compile.log