package com.labeling.demo.controller;

import com.labeling.demo.entity.*;
import com.labeling.demo.service.InstanceService;
import com.labeling.demo.service.TaskService;
import com.labeling.demo.service.TeamService;
import com.labeling.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class AnnotateController {
    private List<Instance> instanceList = null;

    private InstanceService instanceService;
    private TaskService taskService;
    private TeamService teamService;

    @Autowired
    public AnnotateController(InstanceService instanceService, TaskService taskService, TeamService teamService) {
        this.instanceService = instanceService;
        this.taskService = taskService;
        this.teamService = teamService;
    }

    @GetMapping("/annotate")
    public String toAnnotate(Model model){
        Subject curSubj = SecurityUtils.getSubject();
        User curUser = (User) curSubj.getPrincipal();
        String username = curUser.getUsername();
        model.addAttribute("username", username);

        if (curSubj.hasRole("admin")){
            return "bg";
        } else {
            //判断当前用户是否加入小组（没有，则返回空页面）
            String teamName = curUser.getTeamName();
            if (StringUtils.isBlank(teamName)){
                return "no_anno";
            }
            //找到该小组分配的任务
            Team team = teamService.findByName(teamName);
            //获取该任务对应的标注数据
            String taskName = team.getTaskName();
            Task task = taskService.findByName(taskName);
            // 最好分页读取！
            instanceList = instanceService.findByTaskName(taskName);
//            Pageable instPage = PageRequest.of(0, 1);  //当前页 pageNum, 每页大小 pageSize
//            Page<Instance> pageData = instanceService.findPageData(instPage);
//            instanceList = pageData.getContent();

            model.addAttribute("taskName", taskName);
            model.addAttribute("dataType", DataType.getTypeByID(task.getDatatype()));
            model.addAttribute("corpusSize", task.getCorpussize());

            String[] tags = StringUtils.split(task.getTags(), ";");
            model.addAttribute("tags", tags);

            model.addAttribute("instance", instanceList.get(0));
            return "annotate";
        }
    }

    @PostMapping("/annotate")
    @ResponseBody
    public RespEntity annotate(@RequestParam("tag") String tag,
                               @RequestParam("cmd") String cmd){
        System.out.println(tag);

        Subject curSubj = SecurityUtils.getSubject();
        User curUser = (User) curSubj.getPrincipal();
        System.out.println(curUser);
        // 保存标签值

        // 更新用户的标注记录

        // 保存标注记录

        // 根据用户已标数据量取出下一条数据


        return new RespEntity<>(RespStatus.SUCCESS, instanceList.get(0));
    }
}
