package com.example.lostandfoundbackend.controller;

import cn.hutool.core.io.FileUtil;
import com.example.lostandfoundbackend.common.AuthAccess;
import com.example.lostandfoundbackend.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 文件上传相关接口
 *
 * @Author Tao
 * @Date 2023/5/4 20:50
 * @Version 1.0
 */

@RestController
@RequestMapping("/file")
public class FileController {

    //从配置文件中获取ip，默认localhost
    @Value("${ip:localhost}")
    String ip;
    //从配置文件中获取port
    @Value("${server.port}")
    String port;
    //System.getProperty("user.dir")：E:\Project\separationProjects\campusLostAndFoundSystem\LostAndFoundBackend
    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "src/main/resources/static/files";

    /**
     * 文件上传接口
     *
     * @param file 前端发送来的文件
     * @return
     * @throws IOException
     */

    @AuthAccess
    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) throws IOException {
        //文件的原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件的主名称
        String mainName = FileUtil.mainName(originalFilename);
        //获取到文件类型
        String type = FileUtil.extName(originalFilename);
        //判断保存文件的目录是否存在
        if (!FileUtil.exist(ROOT_PATH)) {
            //创建父文件夹
            FileUtil.mkdir(ROOT_PATH);
        }
        //如果要上传的文件已经存在，
        if (FileUtil.exist(ROOT_PATH + File.separator + originalFilename)) {
            //重命名文件名
            originalFilename = System.currentTimeMillis() + "_" + mainName + "." + type;
        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        //存储文件到本地的磁盘
        file.transferTo(saveFile);
        String url = "http://" + ip + ":" + port + "/file/download/" + originalFilename;
        //返回文件的url
        return Result.success(url);
    }

    @AuthAccess
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        //预览
        response.addHeader("Content-Disposition","inline;filename="+
                URLEncoder.encode(fileName,"UTF-8"));

        //附件下载
        //response.addHeader("Content-Disposition","attachment;filename="+
        //        URLEncoder.encode(fileName,"UTF-8"));
        String filePath = ROOT_PATH + File.separator + fileName;
        if (!FileUtil.exist(filePath)){
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    //@GetMapping("/page")
    //public Result findPage(@RequestParam Integer pageNum,
    //                           @RequestParam Integer pageSize,
    //                           @RequestParam(required = false) String name
    //
    //){
    //    QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
    //    queryWrapper.like(Strings.isNotEmpty(name),"name",name);
    //    queryWrapper.orderByDesc("id");
    //    return Result.success(fileMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper));
    //}

    /**
     * 批量删除
     * @param ids
     * @return
     */
    //@PostMapping("/del/batch")
    //public Boolean batchDelete(@RequestBody List<Integer> ids){
    //    return fileService.removeBatchByIds(ids);
    //}
    //
    //@DeleteMapping("/{id}")
    //public Boolean delete(@PathVariable Integer id){
    //    return fileService.removeById(id);
    //}

    /**
     * 更新数据
     * @param files
     * @return
     */
    //@PostMapping("/update")
    //public Result saveOrUpdate(@RequestBody Files files){
    //    return Result.success(fileService.saveOrUpdate(files));
    //}


    /**
     * 获取文件的md5
     * @param md5
     * @return
     */
    //private Files getFileByMd5(String md5){
    //    //查询文件的md5是否存在
    //    QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
    //    queryWrapper.eq("md5",md5);
    //    Files one = fileMapper.selectOne(queryWrapper);
    //    return one;
    //}


}
