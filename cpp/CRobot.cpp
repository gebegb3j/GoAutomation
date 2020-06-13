#include"CRobot.h"
#include<stdio.h>
#include<windows.h>
using namespace std;
JNIEXPORT jboolean JNICALL Java_CRobot_move(JNIEnv*env,jobject _obj,jint x,jint y)
{
	HINSTANCE hDll;
	typedef bool(*Fun1)(int,int);
	hDll=LoadLibrary("user32.dll");
	if(NULL==hDll)
	{  
		fprintf(stderr,"Load dll 'user32.dll' fail.");
		return false;
	}
	Fun1 SetCursorPos=(Fun1)GetProcAddress(hDll,"SetCursorPos");
	if(NULL==SetCursorPos)
	{
		fprintf(stderr,"Call function 'SetCursorPos' fail.");
		FreeLibrary(hDll);
		return false;
	}
	SetCursorPos(x,y);
	FreeLibrary(hDll);
	return true;
}
JNIEXPORT jboolean JNICALL Java_CRobot_move_1with_1radio(JNIEnv*env,jobject _obj,jint x,jint y,jdouble ratio)
{
	x=(int)(x/ratio+0.5);
	y=(int)(y/ratio+0.5);
	return Java_CRobot_move(env,_obj,x,y);
}
/** mouse click
 * type          -- int, 0:left click;1:right click 
 * double_click  -- bool, true:double click; false: single click
 */
JNIEXPORT jboolean JNICALL Java_CRobot_click(JNIEnv*env,jobject _obj,jint type,jboolean double_click)
{
	int left_click=MOUSEEVENTF_LEFTDOWN|MOUSEEVENTF_LEFTUP;
	int right_click=MOUSEEVENTF_RIGHTDOWN|MOUSEEVENTF_RIGHTUP;
	int clicktype;
	HINSTANCE hDll;  
	typedef void(*Fun2)(
			DWORD dwFlags,        // motion and click options
			DWORD dx,             // horizontal position or change
			DWORD dy,             // vertical position or change
			DWORD dwData,         // wheel movement
			ULONG_PTR dwExtraInfo // application-defined information
	);
	hDll=LoadLibrary("user32.dll");
	if(NULL==hDll)
	{
		fprintf(stderr,"Load dll 'user32.dll' fail.");
		return false;
	}
	Fun2 mouse_event=(Fun2)GetProcAddress(hDll,"mouse_event");
	if(NULL==mouse_event)
	{  
		fprintf(stderr,"Call function 'mouse_event' fail.");
		FreeLibrary(hDll);
		return false;
	}
	if(type==0)
	{
		clicktype=left_click;
	}
	else
	{
		clicktype=right_click;
	}
	mouse_event(clicktype,0,0,0,0);
	FreeLibrary(hDll);
	if(double_click)
	{
		return Java_CRobot_click(env,_obj,type,false);
	}
	return true;
}
