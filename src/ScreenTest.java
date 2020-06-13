import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class ScreenTest
{
    Robot robot;
    BufferedImage bufferedImage;
    Dimension dimension;
    Rectangle rectangle;
    public void output(BufferedImage bufferedImage, String name)
    {
        try
        {
            File file=new File(name);
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",fileOutputStream);
            fileOutputStream.close();
        }
        catch(IOException e)
        {
            System.err.println("Cannot output the screen capture.");
        }
    }
    public static void main(String[]args)
    {
        ScreenTest test=new ScreenTest();
        try
        {
            test.robot=new Robot();
        }
        catch(AWTException e)
        {
            e.printStackTrace();
            return;
        }
        test.dimension=Toolkit.getDefaultToolkit().getScreenSize();
        test.rectangle=new Rectangle(test.dimension);
        test.bufferedImage=test.robot.createScreenCapture(test.rectangle);
        test.output(test.bufferedImage,"test.png");
    }
}