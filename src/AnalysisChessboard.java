import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
public class AnalysisChessboard
{
    public static void main(String[]args)
    {
        Scanner input=null;
        try
        {
            FileInputStream fileInputStream=new FileInputStream("chessboard.txt");
            BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);
            input=new Scanner(bufferedInputStream);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        String buffer;
        for(int i=0;i<19;++i)
        {
            System.out.print('{');
            for(int j=0;j<18;++j)
            {
                buffer=input.next();
                System.out.print((buffer.equals("○")?-1:(buffer.equals("●")?1:0))+",");
            }
            buffer=input.next();
            System.out.println((buffer.equals("○")?-1:(buffer.equals("●")?1:0))+"},");
        }
    }
}