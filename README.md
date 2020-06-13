# GoAutomation
用模式识别实现围棋转接器的开源Java项目——一个类似GTool2.exe的工具

一个IDEA的Project，详细说明：http://www.caiyiwen.tech/article/GoAutomation.html

根目录下各文件：

GoAutomation.jar - 项目封装jar，可单独在任意装有JVM的系统上运行。

GoAutomation.zip - 项目完整Windows绿色版，可以在不改变目录相对结构的情况下在任意Windows 7以上系统运行（无需Jre）。

cpp目录 - 用于CRobot类的C++接口源码及dll。

src目录下各文件：

GoAutomation.java - 最终项目成果，也是最终程序运行的唯一需要代码。

Automation.java - 一个离成功只差一步的失败项目。

CRobot.java - 用JNI调用C代码实现功能的类。

ScreenTest.java - 调试截屏的工具。

AnalysisChessboard.java - 读取chessborad.txt的工具。

TestLogic.java - 测试围棋盘状态维护的工具。