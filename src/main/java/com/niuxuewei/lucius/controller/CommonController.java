package com.niuxuewei.lucius.controller;

import com.niuxuewei.lucius.core.exception.NotFoundException;
import com.niuxuewei.lucius.core.result.Result;
import com.niuxuewei.lucius.core.result.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@Slf4j
@PropertySource("classpath:lucius-config.properties")
public class CommonController {

    @Value("${soft-tracker.project-application.upload-folder}")
    private String UPLOAD_FOLDER;

    @GetMapping("/download/{fileName}")
    public Result download(@PathVariable String fileName,
                           HttpServletResponse response) throws UnsupportedEncodingException {
        String fullPath = UPLOAD_FOLDER + fileName;
        File file = new File(fullPath);

        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return null;
            } catch (IOException e) {
                log.error("下载发生错误，错误原因: {}", e.getMessage());
                ResultBuilder.InternalServerErrorResult();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new NotFoundException("文件名非法");
    }

}
