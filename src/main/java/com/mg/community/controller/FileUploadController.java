package com.mg.community.controller;

import com.mg.community.dto.FileUploadDTO;
import com.mg.community.dto.ResultDTO;
import com.mg.community.exception.CustomizeErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName FileUploadController
 * @Description File upload through markdown editor.
 * 1. Markdown editor 前端的原始代码会有一些异常，可通过调试后修改，具体的代码见：
 * [image-dialog.js]:
 * // var json = (body.innerText) ? body.innerText : ( (body.textContent) ? body.textContent : null);
 * var jsonContainer = body.getElementsByTagName("pre")[0];
 * var json = (jsonContainer.innerText) ? jsonContainer.innerText : ( (jsonContainer.textContent) ? jsonContainer.textContent : null);
 * // --- end
 * // Edited by MG 20191218  --- begin
 * dialog.find("[data-link]").val(json.url);
 * // --- end
 * 2. Markdown editor会通过js调用后台的服务；
 * 3. 后台服务里，可以通过MultipartHttpServletRequest强转HttpServletRequest对象，再由MultipartHttpServletRequest对象获取上传的文件，返回MultipartFile类型；
 * 4. 后续的处理为正常的文件流方式；
 * @Author MGLi
 * @Date 2019/12/18 10:45
 * @Version 1.0
 */
@Controller
@Slf4j
public class FileUploadController {

    @Value("${upload.filePath}")
    private String defaultStoreFilePath;

    @RequestMapping("/upload")
    @ResponseBody
    public FileUploadDTO upload(HttpServletRequest request) {

        FileUploadDTO fileUploadDTO = new FileUploadDTO();

        //通过Request获取上传的文件信息
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
        String originalFilename = file.getOriginalFilename();
        log.info("file name is {}" + originalFilename);

        //修改文件名
        String fileName = UUID.randomUUID().toString() + "." + originalFilename.split("\\.")[1];

        //设置本地项目外的绝对路劲
        //注：生产环境里，需要单独把文件保存的文件服务器，这里配置的将是服务器的文件上传路劲
        String serverStoreFilePath = defaultStoreFilePath + "\\" + fileName;

        String serverRunFilePath = null;

        try {
            //设置本地项目内的上传路劲
            serverRunFilePath = ResourceUtils.getURL("classpath:").getPath() + "static/upload" + "/" + fileName;

            //通过流的方式保存文件到本地项目外的绝对路劲
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(serverStoreFilePath));
            //通过流的方式保存文件到本地项目内的绝对路劲
            //注：生产环境里，此步骤可省
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(serverRunFilePath));
        } catch (IOException e) {
            //上传失败
            log.debug("File upload failure: {}" + ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAILURE));
            fileUploadDTO.setSuccess(0);
            fileUploadDTO.setMessage(ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAILURE).getMessage());
            fileUploadDTO.setUrl("");
            return fileUploadDTO;
        }

        //上传成功
        //组装markdown editor所需要的返回信息
        fileUploadDTO.setSuccess(1);
        fileUploadDTO.setMessage("File has already uploaded successfully!");
        fileUploadDTO.setUrl("/upload/" + fileName);
        log.info("uploading file: {}" + fileUploadDTO);

        return fileUploadDTO;
    }
}
