import com.melloware.jintellitype.JIntellitype;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
public class GoAutomation
{
    Robot robot;
    //CRobot cRobot;
    boolean black_confirmed=false,white_confirmed=false,end=false,black_or_white=true,first_black=true,first_white=true;
    int black_x[][]=new int[19][19];
    int black_y[][]=new int[19][19];
    int white_x[][]=new int[19][19];
    int white_y[][]=new int[19][19];
    int status[][]=new int[19][19];
    int times[][];
    int x_ind,y_ind,x_pos,y_pos;
    int inf=0x3f3f3f3f;
    final int dir[][]={{1,0},{-1,0},{0,1},{0,-1}};
    final int eps=10;
    BufferedImage store;
    Dimension dimension;
    Rectangle rectangle;
    int min_x_b,min_y_b,max_x_b,max_y_b,min_x_w,min_y_w,max_x_w,max_y_w,black_times=0,white_times=0;
    public void exchange()
    {
        if(!black_confirmed||!white_confirmed)
        {
            System.err.println("至少一个棋盘没有定义！");
            return;
        }
        int a[][];
        a=black_x;
        black_x=white_x;
        white_x=a;
        a=black_y;
        black_y=white_y;
        white_y=a;
        int b=max_x_w;
        max_x_w=max_x_b;
        max_x_b=b;
        b=max_y_w;
        max_y_w=max_y_b;
        max_y_b=b;
        b=min_x_w;
        min_x_w=min_x_b;
        min_x_b=b;
        b=min_y_w;
        min_y_w=min_y_b;
        min_y_b=b;
        System.out.println("黑白棋盘已成功互换。");
    }
    public void get_setting()
    {
        Scanner input=null;
        try
        {
            FileInputStream fileInputStream=new FileInputStream("setting.txt");
            BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);
            input=new Scanner(bufferedInputStream);
        }
        catch(IOException e)
        {
            System.err.println("找不到或无法读取配置文件setting.txt");
            return;
        }
        try
        {
            min_x_b=input.nextInt();
            min_y_b=input.nextInt();
            max_x_b=input.nextInt();
            max_y_b=input.nextInt();
            min_x_w=input.nextInt();
            min_y_w=input.nextInt();
            max_x_w=input.nextInt();
            max_y_w=input.nextInt();
        }
        catch(Exception e)
        {
            System.err.println("setting.txt文件格式错误！");
        }
        finally
        {
            input.close();
        }
        if(Math.abs((max_x_b-min_x_b)-(max_y_b-min_y_b))>eps||max_x_b-min_x_b+1<300||max_y_b-min_y_b+1<300)
        {
            System.err.println("The width is "+(max_x_b-min_x_b)+".");
            System.err.println("The height is "+(max_y_b-min_y_b)+".");
            System.err.println("Cannot find the chessboard, please shoot more precisely.");
            return;
        }
        System.out.println("Chessboard is bounded by ("+min_x_b+","+min_y_b+") and ("+max_x_b+","+max_y_b+").");
        for(int i=0;i<19;++i)
        {
            int x_pos=min_x_b+(int)((max_x_b-min_x_b)/18.0*i+0.5);
            for(int j=0;j<19;++j)
            {
                int y_pos=min_y_b+(int)((max_y_b-min_y_b)/18.0*j+0.5);
                black_x[i][j]=x_pos;
                black_y[i][j]=y_pos;
            }
        }
        black_confirmed=true;
        if(Math.abs((max_x_w-min_x_w)-(max_y_w-min_y_w))>eps||max_x_w-min_x_w+1<300||max_y_w-min_y_w+1<300)
        {
            System.err.println("The width is "+(max_x_w-min_x_w)+".");
            System.err.println("The height is "+(max_y_w-min_y_w)+".");
            System.err.println("Cannot find the chessboard, please shoot more precisely.");
            return;
        }
        System.out.println("Chessboard is bounded by ("+min_x_w+","+min_y_w+") and ("+max_x_w+","+max_y_w+").");
        for(int i=0;i<19;++i)
        {
            int x_pos=min_x_w+(int)((max_x_w-min_x_w)/18.0*i+0.5);
            for(int j=0;j<19;++j)
            {
                int y_pos=min_y_w+(int)((max_y_w-min_y_w)/18.0*j+0.5);
                white_x[i][j]=x_pos;
                white_y[i][j]=y_pos;
            }
        }
        white_confirmed=true;
    }
    public void store_setting()
    {
        if(!black_confirmed||!white_confirmed)
        {
            System.out.println("至少有一个棋盘没有定义！");
            return;
        }
        PrintWriter out=null;
        try
        {
            FileOutputStream fileOutputStream=new FileOutputStream("setting.txt");
            out=new PrintWriter(fileOutputStream);
        }
        catch(IOException e)
        {
            System.err.println("无法写入根目录下setting.txt！");
            return;
        }
        out.println(min_x_b+" "+min_y_b);
        out.println(max_x_b+" "+max_y_b);
        out.println(min_x_w+" "+min_y_w);
        out.println(max_x_w+" "+max_y_w);
        out.close();
        System.out.println("配置已成功写入根目录下setting.txt。");
    }
    public void give_pos_black()
    {
        ++black_times;
        PointerInfo pointerInfo=MouseInfo.getPointerInfo();
        Point point=pointerInfo.getLocation();
        if((black_times&1)==1)
        {
            first_black=true;
        }
        if(black_confirmed)
        {
            black_confirmed=false;
            first_black=true;
        }
        if(first_black)
        {
            first_black=false;
            min_x_b=(int)point.getX();
            min_y_b=(int)point.getY();
        }
        else
        {
            max_x_b=(int)point.getX();
            max_y_b=(int)point.getY();
            if(Math.abs((max_x_b-min_x_b)-(max_y_b-min_y_b))>eps||max_x_b-min_x_b+1<300||max_y_b-min_y_b+1<300)
            {
                System.err.println("The width is "+(max_x_b-min_x_b)+".");
                System.err.println("The height is "+(max_y_b-min_y_b)+".");
                System.err.println("Cannot find the chessboard, please shoot more precisely.");
                return;
            }
            System.out.println("Chessboard is bounded by ("+min_x_b+","+min_y_b+") and ("+max_x_b+","+max_y_b+").");
            for(int i=0;i<19;++i)
            {
                int x_pos=min_x_b+(int)((max_x_b-min_x_b)/18.0*i+0.5);
                for(int j=0;j<19;++j)
                {
                    int y_pos=min_y_b+(int)((max_y_b-min_y_b)/18.0*j+0.5);
                    black_x[i][j]=x_pos;
                    black_y[i][j]=y_pos;
                }
            }
            black_confirmed=true;
        }
    }
    public void give_pos_white()
    {
        ++white_times;
        PointerInfo pointerInfo=MouseInfo.getPointerInfo();
        Point point=pointerInfo.getLocation();
        if((white_times&1)==1)
        {
            first_white=true;
        }
        if(white_confirmed)
        {
            white_confirmed=false;
            first_white=true;
        }
        if(first_white)
        {
            first_white=false;
            min_x_w=(int)point.getX();
            min_y_w=(int)point.getY();
        }
        else
        {
            max_x_w=(int)point.getX();
            max_y_w=(int)point.getY();
            if(Math.abs((max_x_w-min_x_w)-(max_y_w-min_y_w))>eps||max_x_w-min_x_w+1<300||max_y_w-min_y_w+1<300)
            {
                System.err.println("The width is "+(max_x_w-min_x_w)+".");
                System.err.println("The height is "+(max_y_w-min_y_w)+".");
                System.err.println("Cannot find the chessboard, please shoot more precisely.");
                return;
            }
            System.out.println("Chessboard is bounded by ("+min_x_w+","+min_y_w+") and ("+max_x_w+","+max_y_w+").");
            for(int i=0;i<19;++i)
            {
                int x_pos=min_x_w+(int)((max_x_w-min_x_w)/18.0*i+0.5);
                for(int j=0;j<19;++j)
                {
                    int y_pos=min_y_w+(int)((max_y_w-min_y_w)/18.0*j+0.5);
                    white_x[i][j]=x_pos;
                    white_y[i][j]=y_pos;
                }
            }
            white_confirmed=true;
        }
    }
    public boolean distinguish(int des_x[][],int des_y[][])
    {
        store=robot.createScreenCapture(rectangle);
        int width=store.getWidth();
        int height=store.getHeight();
        PointerInfo pointerInfo=MouseInfo.getPointerInfo();
        Point point=pointerInfo.getLocation();
        int x_ind=(int)point.getX();
        int y_ind=(int)point.getY();
        System.out.println("Central position is settled at ("+x_ind+","+y_ind+").");
        int std=store.getRGB(x_ind,y_ind);
        Color std_color=new Color(std,true);
        Queue<Pair<Integer,Integer>> q=new ArrayDeque<>();
        HashMap<Pair<Integer,Integer>,Boolean> have=new HashMap<>();
        q.add(new Pair<>(x_ind,y_ind));
        have.put(new Pair<>(x_ind,y_ind),true);
        int min_x=inf,min_y=inf,max_x=0,max_y=0;
        while(!q.isEmpty())
        {
            int cx=q.peek().getKey();
            int cy=q.peek().getValue();
            min_x=Math.min(min_x,cx);
            min_y=Math.min(min_y,cy);
            max_x=Math.max(max_x,cx);
            max_y=Math.max(max_y,cy);
            q.poll();
            //store.setRGB(cx,cy,Color.red.getRGB());
            for(int i=0;i<4;++i)
            {
                int nx=cx+dir[i][0];
                int ny=cy+dir[i][1];
                Pair np=new Pair(nx,ny);
                if(!have.containsKey(np)&&is_valid(nx,ny,width,height))
                {
                    Color next_color=new Color(store.getRGB(nx,ny),true);
                    if(similar(std_color,next_color,false))
                    {
                        q.add(np);
                        have.put(np,true);
                    }
                }
            }
        }
        //output(store,"distinguish.png");
        if(Math.abs((max_x-min_x)-(max_y-min_y))>eps||max_x-min_x+1<300||max_y-min_y+1<300)
        {
            System.err.println("The width is "+(max_x-min_x)+".");
            System.err.println("The height is "+(max_y-min_y)+".");
            System.err.println("Cannot find the chessboard, please shoot more precisely.");
            return false;
        }
        System.out.println("Chessboard is bounded by ("+min_x+","+min_y+") and ("+max_x+","+max_y+").");
        for(int i=0;i<19;++i)
        {
            int x_pos=min_x+(int)((max_x-min_x)/18.0*i+0.5);
            for(int j=0;j<19;++j)
            {
                int y_pos=min_y+(int)((max_y-min_y)/18.0*j+0.5);
                des_x[i][j]=x_pos;
                des_y[i][j]=y_pos;
            }
        }
        return true;
    }
    public void output(BufferedImage bufferedImage,String name)
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
    public void chess_output(int status[][])
    {
        PrintWriter out=null;
        try
        {
            FileOutputStream fileOutputStream=new FileOutputStream("chessboard.txt");
            out=new PrintWriter(fileOutputStream);
        }
        catch(IOException e)
        {
            return;
        }
        for(int i=0;i<19;++i)
        {
            for(int j=0;j<19;++j)
            {
                out.print(status[j][i]==-1?"○ ":(status[j][i]==1?"● ":"+ "));
            }
            out.println();
        }
        out.close();
    }
    public boolean is_valid(int x,int y,int width,int height)
    {
        return x>=0&&x<width&&y>=0&&y<height;
    }
    public boolean similar(Color c1,Color c2,boolean tight)
    {
        //return Math.abs(c1.getRed()-c2.getRed())<100&&Math.abs(c1.getGreen()-c2.getGreen())<100&&Math.abs(c1.getBlue()-c2.getBlue())<100;
        int R_1=c1.getRed();
        int G_1=c1.getGreen();
        int B_1=c1.getBlue();
        int R_2=c2.getRed();
        int G_2=c2.getGreen();
        int B_2=c2.getBlue();
        int R=R_1-R_2;
        int G=G_1-G_2;
        int B=B_1-B_2;
        int rmean=(R_1+R_2)/2;
        if(tight)
        {
            return Math.sqrt((2+rmean/256)*(R*R)+4*(G*G)+(2+(255-rmean)/256)*(B*B))<179.0;
        }
        return Math.sqrt((2+rmean/256)*(R*R)+4*(G*G)+(2+(255-rmean)/256)*(B*B))<300.0;
    }
    public void click(int x,int y)
    {
        for(int i=0;i<10;++i)
        {
            robot.mouseMove(x,y);
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(500);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        /*
        cRobot.move(x,y);
        cRobot.click(0,false);
        */
    }
    public void play()
    {
        if(!black_confirmed||!white_confirmed)
        {
            System.err.println("Please shoot the chessboards of black and white first.");
            return;
        }
        PointerInfo pointerInfo=MouseInfo.getPointerInfo();
        Point point=pointerInfo.getLocation();
        int x=(int)point.getX();
        int y=(int)point.getY();
        int dis=(int)((black_x[1][0]-black_x[0][0])/2.0+0.5);
        times=new int[19][19];
        for(int i=0;i<19;++i)
        {
            for(int j=0;j<19;++j)
            {
                int dx=x-black_x[i][j];
                int dy=y-black_y[i][j];
                if(dx*dx+dy*dy<=dis*dis)
                {
                    ++times[i][j];
                }
            }
        }
        int max_value=0;
        for(int i=0;i<19;++i)
        {
            for(int j=0;j<19;++j)
            {
                if(times[i][j]>max_value)
                {
                    x_pos=i;
                    y_pos=j;
                    x_ind=white_x[i][j];
                    y_ind=white_y[i][j];
                    max_value=times[i][j];
                }
            }
        }
        System.out.println("The position of the first turn is settled at ("+x_pos+","+y_pos+").");
        status[x_pos][y_pos]=1;
        PrintWriter out=null;
        boolean can_write=true;
        try
        {
            FileOutputStream fileOutputStream=new FileOutputStream("Entries.txt");
            out=new PrintWriter(fileOutputStream);
        }
        catch(IOException e)
        {
            can_write=false;
            System.err.println("Cannot write the diary.");
        }
        while(!end)
        {
            if(can_write)
            {
                out.println("("+x_pos+","+y_pos+")");
                out.flush();
            }
            //store=robot.createScreenCapture(rectangle);
            //output(store,"capture.png");
            click(x_ind,y_ind);
            //status[x_pos][y_pos]=black_or_white?1:-1;
            //modify();
            //chess_output(status);
            black_or_white=!black_or_white;
//            robot.delay(200);
//            if(!black_or_white)
//            {
//                click(x_ind,y_ind);
//            }
            while(!end)
            {
                robot.delay(200);
                if(judge_change())
                {
                    break;
                }
            }
        }
        if(can_write)
        {
            out.close();
        }
        System.out.println("转接已停止。");
    }
    //ArrayList<Pair<Integer,Integer>> changes;
    //boolean avoid_test[][];
    public boolean judge_change()
    {
        //changes=new ArrayList<>();
        store=robot.createScreenCapture(rectangle);
        //output(change,"new.png");
        int low_i,low_j,high_i,high_j,move_x[][],move_y[][];
        if(black_or_white)
        {
            low_i=black_x[0][0];
            low_j=black_y[0][0];
            high_i=black_x[18][18];
            high_j=black_y[18][18];
            move_x=white_x;
            move_y=white_y;
        }
        else
        {
            low_i=white_x[0][0];
            low_j=white_y[0][0];
            high_i=white_x[18][18];
            high_j=white_y[18][18];
            move_x=black_x;
            move_y=black_y;
        }
        int new_status[][]=get_status(store,low_i,low_j,high_i,high_j);
        int times=0,store_status[][]=new int[19][19];
        for(int i=0;i<19;++i)
        {
            for(int j=0;j<19;++j)
            {
                store_status[i][j]=status[i][j];
                if(new_status[i][j]==(black_or_white?1:-1)&&status[i][j]==0)
                {
                    ++times;
                    x_pos=i;
                    y_pos=j;
                    x_ind=move_x[i][j];
                    y_ind=move_y[i][j];
                }
            }
        }
        if(times!=1)
        {
            return false;
        }
        status[x_pos][y_pos]=(black_or_white?1:-1);
        modify();
        boolean ok=true;
        for(int i=0;i<19&&ok;++i)
        {
            for(int j=0;j<19;++j)
            {
                if(status[i][j]!=new_status[i][j])
                {
                    ok=false;
                    break;
                }
            }
        }
        if(!ok)
        {
            status=store_status;
            return false;
        }
        return true;
        /*
        for(int i=low_i;i<=high_i;++i)
        {
            for(int j=low_j;j<=high_j;++j)
            {
                if(change.getRGB(i,j)!=store.getRGB(i,j))
                {
                    changes.add(new Pair<>(i,j));
                }
            }
        }
        int map_x[][],map_y[][],c_x[][],c_y[][];
        if(black_or_white)
        {
            map_x=black_x;
            map_y=black_y;
            c_x=white_x;
            c_y=white_y;
        }
        else
        {
            map_x=white_x;
            map_y=white_y;
            c_x=black_x;
            c_y=black_y;
        }
        if(changes.size()==0)
        {
            return false;
        }
        int dis=(int)((map_x[0][1]-map_x[0][0])/2.0+0.5);
        times=new int[19][19];
        for(Pair diff:changes)
        {
            for(int i=0;i<19;++i)
            {
                for(int j=0;j<19;++j)
                {
                    if(status[i][j]!=0||avoid_test[i][j])
                    {
                        continue;
                    }
                    int dx=(Integer)diff.getKey()-map_x[i][j];
                    int dy=(Integer)diff.getValue()-map_y[i][j];
                    if(dx*dx+dy*(long)dy<=dis*(long)dis)
                    {
                        ++times[i][j];
                    }
                }
            }
        }
        int max_value=0,max_num=0;
        for(int i=0;i<19;++i)
        {
            for(int j=0;j<19;++j)
            {
                if(times[i][j]>max_value)
                {
                    x_pos=i;
                    y_pos=j;
                    x_ind=c_x[i][j];
                    y_ind=c_y[i][j];
                    max_value=times[i][j];
                    max_num=1;
                }
                else if(times[i][j]==max_value)
                {
                    ++max_num;
                }
            }
        }
        if(max_num>1)
        {
            //.out.println(max_value+" "+max_num);
            return false;
        }
        return true;
        */
    }
    public int[][]get_status(BufferedImage image,int low_i,int low_j,int high_i,int high_j)
    {
        int status[][]=new int[19][19];
        int black_num[][]=new int[19][19];
        int white_num[][]=new int[19][19];
        double width=(high_i-low_i)/18.0;
        double length=(high_j-low_j)/18.0;
        low_i-=(int)(width/2.0+0.5);
        low_j-=(int)(length/2.0+0.5);
        high_i+=(int)(width/2.0+0.5);
        high_j+=(int)(length/2.0+0.5);
        for(int i=low_i;i<=high_i;++i)
        {
            for(int j=low_j;j<=high_j;++j)
            {
                Color color=new Color(image.getRGB(i,j),true);
                int x=(int)((i-low_i)/width);
                int y=(int)((j-low_j)/length);
                if(x==19)
                {
                    x=18;
                }
                if(y==19)
                {
                    y=18;
                }
                /*
                if(((x+y)&1)==0)
                {
                    image.setRGB(i,j,0xffffffff);
                }
                */
                if(similar(Color.white,color,true))
                {
                    //image.setRGB(i,j,Color.green.getRGB());
                    ++white_num[x][y];
                }
                else if(similar(Color.black,color,true))
                {
                    //image.setRGB(i,j,Color.blue.getRGB());
                    ++black_num[x][y];
                }
            }
        }
        //output(image,"new.png");
        int std=(int)(width*length/4+0.5);
        //System.out.println(std);
        for(int i=0;i<19;++i)
        {
            for(int j=0;j<19;++j)
            {
                //System.out.print(white_num[i][j]+" ");
                if(white_num[i][j]>=std)
                {
                    status[i][j]=-1;
                }
                else if(black_num[i][j]>=std)
                {
                    status[i][j]=1;
                }
            }
            //System.out.println();
        }
        chess_output(status);
        return status;
    }
    public void get_status_from_white()
    {
        if(!white_confirmed)
        {
            System.err.println("白棋盘尚未定义！");
            return;
        }
        store=robot.createScreenCapture(rectangle);
        status=get_status(store,min_x_w,min_y_w,max_x_w,max_y_w);
        chess_output(status);
        System.out.println("获取白棋盘状态成功。");
    }
    public void modify()
    {
        have=new boolean[19][19];
        //avoid_test=new boolean[19][19];
        for(int i=0;i<19;++i)
        {
            for(int j=0;j<19;++j)
            {
                if(!have[i][j]&&status[i][j]==(black_or_white?-1:1))
                {
                    if(need_to_remove(i,j))
                    {
                        remove(i,j);
                    }
                }
            }
        }
    }
    boolean have[][];
    public boolean need_to_remove(int x,int y)
    {
        Queue<Pair<Integer,Integer>> q=new ArrayDeque<>();
        q.add(new Pair<>(x,y));
        have[x][y]=true;
        boolean remove=true;
        while(!q.isEmpty())
        {
            int cx=q.peek().getKey();
            int cy=q.peek().getValue();
            q.poll();
            for(int i=0;i<4;++i)
            {
                int nx=cx+dir[i][0];
                int ny=cy+dir[i][1];
                if(nx>=0&&nx<19&&ny>=0&&ny<19&&!have[nx][ny])
                {
                    if(status[nx][ny]==0)
                    {
                        remove=false;
                    }
                    else if(status[nx][ny]==(black_or_white?-1:1))
                    {
                        q.add(new Pair<>(nx,ny));
                        have[nx][ny]=true;
                    }
                }
            }
        }
        return remove;
    }
    public void remove(int x,int y)
    {
        //avoid_test[x][y]=true;
        status[x][y]=0;
        for(int i=0;i<4;++i)
        {
            int nx=x+dir[i][0];
            int ny=y+dir[i][1];
            if(nx>=0&&nx<19&&ny>=0&&ny<19&&status[nx][ny]==(black_or_white?-1:1))
            {
                remove(nx,ny);
            }
        }
    }
    public static void main(String[]args)
    {
        System.out.println("欢迎来到蔡弈文的围棋转接器！\n" +
                "请不要最小化此对话框，可以将其放在对弈对话框之后。\n" +
                "请关闭所有杀毒软件和不必要的程序，否则转接器可能失灵。\n" +
                "对弈对话框尽量规避弹窗。\n" +
                "Ctrl+B - 鼠标放在黑棋盘网格上（推荐天元和星，可放到初始子的黑色部分）\n" +
                "Ctrl+W - 鼠标放在白棋盘网络上（推荐天元和星）\n" +
                "Ctrl+Alt+B - 第一次鼠标放到黑棋盘的左上角交叉点，第二次为右下角\n" +
                "Ctrl+Alt+W - 第一次鼠标放到白棋盘的左上角交叉点，第二次为右下角\n" +
                "Ctrl+S - 鼠标放到黑棋盘初始棋子上，开始转换\n" +
                "Ctrl+T - 暂停转换（按下时最好黑棋下完而白棋未动）\n" +
                "Ctrl+E - 结束转换\n" +
                "Ctrl+Alt+L - 读取根目录下的配置文件setting.txt\n" +
                "Ctrl+Alt+S - 将当前配置写入根目录下setting.txt\n" +
                "Ctrl+Alt+E - 互换黑白棋盘\n" +
                "Ctrl+Alt+G - 从白棋棋盘上获得当前状态\n" +
                "根目录下生成的文件：\n" +
                "chessboard.txt - 当前棋盘状况\n" +
                "Entries.txt - 当前局棋谱\n");
        GoAutomation automation=new GoAutomation();
        try
        {
            automation.robot=new Robot();
        }
        catch(AWTException e)
        {
            e.printStackTrace();
            return;
        }
        //automation.cRobot=new CRobot();
        automation.dimension=Toolkit.getDefaultToolkit().getScreenSize();
        automation.rectangle=new Rectangle(automation.dimension);
        JIntellitype.getInstance().registerHotKey(1,JIntellitype.MOD_CONTROL,66);
        JIntellitype.getInstance().registerHotKey(2,JIntellitype.MOD_CONTROL,87);
        JIntellitype.getInstance().registerHotKey(3,JIntellitype.MOD_CONTROL,83);
        JIntellitype.getInstance().registerHotKey(4,JIntellitype.MOD_CONTROL,69);
        JIntellitype.getInstance().registerHotKey(5,JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT,66);
        JIntellitype.getInstance().registerHotKey(6,JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT,87);
        JIntellitype.getInstance().registerHotKey(7,JIntellitype.MOD_CONTROL,84);
        JIntellitype.getInstance().registerHotKey(8,JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT,76);
        JIntellitype.getInstance().registerHotKey(9,JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT,83);
        JIntellitype.getInstance().registerHotKey(10,JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT,69);
        JIntellitype.getInstance().registerHotKey(11,JIntellitype.MOD_CONTROL+JIntellitype.MOD_ALT,71);
        JIntellitype.getInstance().addHotKeyListener(i->
        {
            switch(i)
            {
                case 1:
                    automation.black_confirmed=automation.distinguish(automation.black_x,automation.black_y);
                    automation.min_x_b=automation.black_x[0][0];
                    automation.min_y_b=automation.black_y[0][0];
                    automation.max_x_b=automation.black_x[18][18];
                    automation.max_y_b=automation.black_y[18][18];
                    break;
                case 2:
                    automation.white_confirmed=automation.distinguish(automation.white_x,automation.white_y);
                    automation.min_x_w=automation.white_x[0][0];
                    automation.min_y_w=automation.white_y[0][0];
                    automation.max_x_w=automation.white_x[18][18];
                    automation.max_y_w=automation.white_y[18][18];
                    break;
                case 3:
                    automation.end=false;
                    automation.black_or_white=true;
                    new Thread(()->automation.play()).start();
                    break;
                case 4:
                    automation.end=true;
                    new Thread(()->
                    {
                        for(int i1=0;i1<8;++i1)
                        {
                            JIntellitype.getInstance().unregisterHotKey(i1);
                        }
                        JIntellitype.getInstance().cleanUp();
                    }).start();
                    break;
                case 5:
                    automation.give_pos_black();
                    break;
                case 6:
                    automation.give_pos_white();
                    break;
                case 7:
                    automation.end=true;
                    break;
                case 8:
                    automation.get_setting();
                    break;
                case 9:
                    automation.store_setting();
                    break;
                case 10:
                    automation.exchange();
                    break;
                default:
                    automation.get_status_from_white();
            }
        });
    }
}