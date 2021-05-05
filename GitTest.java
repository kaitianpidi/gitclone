
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.InputStream;

/**
 * 功能：实现 使用 git clone 一个项目。
 *
 * git clone --progress  https://gitee.com/mingxin400/aop.git  2>&1
 *      --progress:  把动态的进度信息，作为标准错误输出
 *      2>&1： 把标准错误 追加到标准输出，显示到终端
 */
public class GitTest{
    public static void main(String[] args) {
        String cmmd ="git clone --progress  https://gitee.com/mingxin400/aop.git  2>&1";
        String[] cmdarray = { "cmd", "/c",cmmd};
        try {
            Process p = Runtime.getRuntime().exec(cmdarray,null, new File("d:/tmp"));
            InputStream inputStream = p.getInputStream(); //获取终端显示的内容
            String out = IOUtils.toString(inputStream, "UTF-8");
            System.out.println(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != process) {
                process.destroy();  //注意process一定要释放
       }
    }
}

/*
 参考文档：
 1、linux重定向总结:如何将shell命令的输出信息自动输出到.
  https://my.oschina.net/u/4354993/blog/3309825

2、解释：--progress
 https://git-scm.com/docs/git-clone

3、java使用ssh连接Linux并执行命令
https://www.cnblogs.com/xiaoliu66007/p/11084208.html

4、Process#waitFor()阻塞问题
https://blog.csdn.net/zero__007/article/details/88979811

*/
