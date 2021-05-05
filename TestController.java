package com.compass;

import com.compass.config.GitCloneConfiguration;
import com.compass.vo.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.InputStream;


/**
 * 同步代码  接口：
 * 功能：从github 上拉取项目。
 */
@RequestMapping("test")
@RestController
@Slf4j
public class TestController {
    @Autowired
    GitCloneConfiguration gitCloneConfiguration;
    @RequestMapping("clone")
    public ResponseMessage gitClone(){
//        String cmmd ="git clone --progress  ""+https://gitee.com/mingxin400/aop.git  2>&1";
        String cmmd ="git clone --progress  "+ gitCloneConfiguration.getUrl() +"  2>&1";
//        String[] cmdarray = { "cmd", "/c",cmmd};
        String[] cmdarray = { gitCloneConfiguration.getCmd(), gitCloneConfiguration.getParam(),cmmd};
        Process process = null;
        try {
            process  = Runtime.getRuntime().exec(cmdarray,null, new File(gitCloneConfiguration.getDir()));
            InputStream inputStream = process.getInputStream(); //获取终端显示的内容
            String out = IOUtils.toString(inputStream, "UTF-8");
            log.info(out);
            String result = getResult(out);
            if (isSuccess(result)) {
                return ResponseMessage.successResponse(getResult(out));
            }else {
                return ResponseMessage.errorResponse("拉取项目失败,请从新拉取");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.errorResponse("拉取项目失败,请从新拉取");
        } finally {
            if (null != process) {
                process.destroy();  //注意process一定要释放
            }
        }
    }

    /**
     * 比较 带有%的数据，是否是100%,  是 则代表拉取成功，否则拉取失败
     * @param result
     * @return
     */
    public static boolean isSuccess(String result){
        int length = result.length();
        for(int i=0;i<length;) {
            int index = result.indexOf("%",i);
            //不存在，返回true
            if (index <0 ){ return true;}
            //存在，比较是否 100%， 不存在返回 false，否则 继续下一个
            if (result.substring(index-3,index).equals("100")) {
                i = index +1 ;
            }else {
                return false;
            }
        }
        return true;
    }
    /**
     * 1、功能： 结果数据里， 中间百分比进度的内容过多。 进行过滤， 只保留最后结果。
     * 2、观察规律：最后结果数据 都是以 \n 结尾； 中间数据都是以 \r 结尾
     * 3、结论：以\n ，\r 作为标识 进行过滤。
     * 算法：
     *  从后往前查找，更简便高效。
     *  1. 从右边找到第1个 \n,  第1个\r
     *  2.  1)如果\n  \r 都存在，则 它们之间的 数据 时要保留
     *      2)如果\n 存在，  \r 不存在，则 都是 \n 的数据 ，进行保留
     *      3）如果\n 不存在， 则 执行完毕。
     *
     */
    public String getResult(String str){
        StringBuilder stringBuilder = new StringBuilder();
        int length = str.length();

        String result="";  //存放结果 是正序。如果不需要正序结果，则去掉
        for(int i=length;i>0;) {
            int n = str.lastIndexOf("\n", i);
            int r = str.lastIndexOf("\r", i);

            //如果 \n 不存在。则退出
            if(n<0){
                break;
            }else {
                // \r 不存在，则所有的内容，都是 保留的
                if (r<0){
//                    stringBuilder.append(str.substring(0,n));
                    result = StringUtils.join(str.substring(0,n+1),result);
                    break;
                }else {
                    if (r<n){
                        // \r 存在，并小于\n， 保留\r 与 \n 之间的数据
//                        stringBuilder.append(str.substring(r+1,n));
                        result= StringUtils.join(str.substring(r+1,n+1),result);
                        i= r-1;
                    }else {
                        i = n;
                    }
                }
            }
        }
        //如果不要返回值，只是用来判断成功与否的 ，用stringBuilder 性能更好。
//        String result2 = stringBuilder.toString();

        return  result;
    }

}
