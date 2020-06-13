public class CRobot
{
    {
        System.load(System.getProperty("user.dir")+"\\cpp\\CRobot.dll");
    }
    public native boolean move(int x,int y);
    public native boolean move_with_radio(int x,int y,double ratio);
    public native boolean click(int type,boolean double_click);
    public static void main(String[]args)
    {
        CRobot cRobot=new CRobot();
        //cRobot.move_with_radio(960,540,1.25);
        cRobot.move(960,540);
        cRobot.click(0,false);
    }
}