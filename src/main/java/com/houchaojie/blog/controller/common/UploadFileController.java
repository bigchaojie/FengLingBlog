package com.houchaojie.blog.controller.common;

import com.alibaba.fastjson.JSON;
import com.houchaojie.blog.dto.ResultVO;
import com.houchaojie.blog.dto.UploadFileVO;
import com.houchaojie.blog.util.Functions;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Calendar;

/**
 * @author 侯超杰
 */
@Controller
@Slf4j
public class UploadFileController {
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@Param("file") MultipartFile file) {
        //本地使用,上传位置
//        String rootPath ="E:/IDEAWorkingSet/ForestBlog/uploads";
        String rootPath = "E:" + File.separator + "IDEAWorkingSet" + File.separator + "ForestBlog" + File.separator + "uploads";
        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();
        //文件名,如spring
        String name = filename.substring(0,filename.indexOf("."));
//        重命名文件
        String randomName = Functions.generateRandonFileName(filename );
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));

        String newFileName = randomName+"."+suffix;

        //创建年月文件夹
        Calendar date = Calendar.getInstance();
        File dateDirs = new File(date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH)+1));
//      文件父路径
        String fileParentPath = rootPath + File.separator + dateDirs + File.separator;
        //目标文件
        File descFile = new File(fileParentPath+filename);
        //重命名目标文件
        File newFileFullName = new File( fileParentPath +randomName);
        log.info(descFile.getPath());
        log.info( newFileFullName.getPath());
        boolean flag = descFile.renameTo( newFileFullName );
        if (flag) {
            log.info( "重命名成功" );
        }else{
            log.error("重命名失败,cause:{}");
        }

        //若文件存在重命名
        int i = 1;
        while (newFileFullName.exists()) {
            newFileName = String.format( "%s(%d)%s", randomName, i, suffix );
            String parentPath = newFileFullName.getParent();
            newFileFullName = new File( parentPath + File.separator + newFileName );
            i++;
        }
        //判断目标文件所在的目录是否存在
        if(!newFileFullName.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFileFullName.getParentFile().mkdirs();
        }

        //将内存中的数据写入磁盘
        try {
            file.transferTo(newFileFullName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败，cause:{}",e);
            System.out.println("上传失败");
        }
        //完整的url
        String fileUrl = new StringBuilder().append( File.separator ).append( "uploads" ).append( File.separator ).append( dateDirs ).append( File.separator ).append( randomName ).toString();
        log.info( fileUrl );

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");

        UploadFileVO uploadFileVO = new UploadFileVO();
        uploadFileVO.setTitle(newFileName);
        uploadFileVO.setSrc(fileUrl);
        resultVO.setData(uploadFileVO);
        return JSON.toJSONString(resultVO);
    }

}
