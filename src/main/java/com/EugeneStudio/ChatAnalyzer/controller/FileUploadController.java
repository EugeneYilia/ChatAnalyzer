package com.EugeneStudio.ChatAnalyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Controller("fileUploadController")
@RequestMapping("/uploadFile")
public class FileUploadController {

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (!file.isEmpty()) {
            String saveFileName = file.getOriginalFilename();
            File saveFile = new File(request.getSession().getServletContext().getRealPath("/upload/") + saveFileName);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                return "上传成功";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败";
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败";
            }
        } else {
            return "上传失败，因为文件为空";
        }
    }
}
