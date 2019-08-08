package com.labeling.demo.controller;

import com.labeling.demo.entity.Instance;
import com.labeling.demo.entity.RespEntity;
import com.labeling.demo.entity.RespStatus;
import com.labeling.demo.entity.Task;
import com.labeling.demo.service.InstanceService;
import com.labeling.demo.service.TaskService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Controller
public class DataController {
    private static final String tempDir = "src/main/resources/temp";
    private static final int BATCHSIZE  = 50;

    private InstanceService instanceService;
    private TaskService taskService;

    @Autowired
    public DataController(InstanceService instanceService, TaskService taskService) {
        this.instanceService = instanceService;
        this.taskService = taskService;
    }

    @GetMapping("/upload")
    @RequiresRoles("admin")
    public String toUpload(){
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    @RequiresRoles("admin")
    public RespEntity upload(@RequestParam("file") MultipartFile multiFile,
                             @RequestParam("taskName") String taskName,
                             @RequestParam("dataType") Short dataType,
                             @RequestParam("tags") String tags) throws IOException {
        if(multiFile==null || multiFile.isEmpty()){
            return new RespEntity(RespStatus.Error);
        }

        System.out.println(multiFile.getOriginalFilename());
        System.out.println(multiFile.getName());
        System.out.println(multiFile.getContentType());
        System.out.println(multiFile.getSize());

        Set<String> instSet = new HashSet<>(0);
        if (multiFile.getOriginalFilename().endsWith("zip")) {
            File tmpDir = new File(tempDir);
            if (!tmpDir.exists()){
                boolean isOk = tmpDir.mkdirs();
                if (!isOk){
                    return new RespEntity(RespStatus.Error);
                }
            }

            FileUtils.cleanDirectory(tmpDir);

            File tmpFile = new File(tmpDir.getAbsolutePath(), multiFile.getOriginalFilename());
            multiFile.transferTo(tmpFile);

            // 读取zip文件
            ZipFile zf = new ZipFile(tmpFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(tmpFile)));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().endsWith("txt") || ze.getName().endsWith("csv")) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze), Charset.forName("gbk")));
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = StringUtils.trimToEmpty(line);  //过滤两端空格
                        if (!StringUtils.isAllBlank(line)){
                            instSet.add(line);
                        }
                    }
                }
            }

        } else if(multiFile.getOriginalFilename().endsWith("txt") || multiFile.getOriginalFilename().endsWith("csv")){
            // 读取普通的txt和csv文件
//        BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
//          byte[] buffer = new byte[1024];
//        int len;
//        while((len=bis.read(buffer)) != -1){
//            String res = new String(buffer, 0, len, "GBK");
//            System.out.println(res);
//        }

            BufferedReader br = new BufferedReader(new InputStreamReader(multiFile.getInputStream(), Charset.forName("gbk")));
            String line;
            while ((line=br.readLine()) != null){
                line = StringUtils.trimToEmpty(line);
                if (!StringUtils.isAllBlank(line)){
                    instSet.add(line);
                }
            }
        }

        // 分批次将数据保存到MongoDB中
        if (!instSet.isEmpty()){
            ArrayList<Instance> insts = new ArrayList<>(BATCHSIZE);
            for(String item: instSet){
                insts.add(new Instance(taskName, item, "", 0));
                if (insts.size() == BATCHSIZE){
                    instanceService.saveAll(insts);
                    insts.clear();
                }
            }
            if (!insts.isEmpty())
                instanceService.saveAll(insts);

            taskService.save(new Task(taskName, dataType, instSet.size(), tags));
        }

        return new RespEntity(RespStatus.SUCCESS);
    }
}
