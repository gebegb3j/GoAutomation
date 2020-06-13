# GoAutomation
用模式识别实现围棋转接器的开源Java项目——一个类似GTool2.exe的工具

一个IDEA的Project，详细说明：http://www.caiyiwen.tech/article/GoAutomation.html

根目录下各文件：

GoAutomation.jar - 项目封装jar，可单独在任意装有JVM的系统上运行。

GoAutomation.exe - 项目exe，可以和jre1.8.0_231目录一起在任意Windows 7以上系统运行（或者自带Jre的系统）。

cpp目录 - 用于CRobot类的C++接口源码及dll。

jre1.8.0_231目录 - 完整的Jre，配合GoAutomation.exe运行。

src目录下各文件：

GoAutomation.java - 最终项目成果，也是最终程序运行的唯一需要代码。

Automation.java - 一个离成功只差一步的失败项目。

CRobot.java - 用JNI调用C代码实现功能的类。

ScreenTest.java - 调试截屏的工具。

AnalysisChessboard.java - 读取chessborad.txt的工具。

TestLogic.java - 测试围棋盘状态维护的工具。