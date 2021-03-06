package com.labeling.demo.controller;

import com.labeling.demo.entity.*;
import com.labeling.demo.entity.vo.TempoVO;
import com.labeling.demo.entity.vo.UserVO;
import com.labeling.demo.service.InstanceUserService;
import com.labeling.demo.service.TaskService;
import com.labeling.demo.service.TeamService;
import com.labeling.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private static final String root = "bg/";
    private UserService userService;
    private TaskService taskService;
    private TeamService teamService;
    private InstanceUserService instanceUserService;

    @Autowired
    public UserController(UserService userService, TaskService taskService, TeamService teamService, InstanceUserService instanceUserService) {
        this.userService = userService;
        this.taskService = taskService;
        this.teamService = teamService;
        this.instanceUserService = instanceUserService;
    }


    @GetMapping("/bg")
    @RequiresRoles("admin")
//    @RequiresPermissions("login:annotate:bg")
    public String background(Model model) {
        User curUser = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("username", curUser.getUsername());
        return root+"bg";
    }

    @GetMapping("/bg_index")
    @RequiresRoles("admin")
    public String bgIndex(Model model) {
        // 哪个任务，哪个团队中的哪个成员的工作进展
//        List<User> allUsers = userService.findAll();
//        List<TempoVO> tempos = new ArrayList<>();  //保存当前正在进行的用户进度信息
//        for (User user : allUsers) {
//            String username = user.getUsername();
//            String teamName = user.getTeamName();
//            if (StringUtils.isNotBlank(teamName)) {
//                Team team = teamService.findByName(teamName);
//                String taskName = team.getTaskName();
//                Task task = taskService.findByName(taskName);
//                Integer tagNum = instanceUserService.countByUsername(username);
//                Integer corpusSize = task.getCorpussize();
//                tempos.add(new TempoVO(username, teamName, taskName, tagNum, corpusSize));
//            }
//        }
//        model.addAttribute("tempos", tempos);
        return root+"bg_index";
    }

    @PostMapping("/tempoInfo")
    @ResponseBody
    @RequiresRoles("admin")
    public List<TempoVO> usersInfo() {
        List<User> allUsers = userService.findAll();
        List<TempoVO> tempos = new ArrayList<>();  //保存当前正在进行的用户进度信息
        for (User user : allUsers) {
            String username = user.getUsername();
            String teamName = user.getTeamName();
            if (StringUtils.isNotBlank(teamName)) {
                Team team = teamService.findByName(teamName);
                Integer taskId = team.getTaskId();
                Task task = taskService.findById(taskId);
                Integer tagNum = instanceUserService.countByTask(username, taskId);
                Integer corpusSize = task.getCorpussize();
                tempos.add(new TempoVO(username, teamName, task.getTaskname(), tagNum, corpusSize));
            }
        }
        return tempos;
    }

    @GetMapping("/account")
    public String account(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("userVo", new UserVO(user.getUsername(), user.getRole(), user.getTeamName(), 0));
        return "account";
    }

    @PostMapping("/register")
    @ResponseBody
    public RespEntity addUser(User user) {
        System.out.println(user);
        //将用户名作为盐值
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        //使用MD5算法对用户密码进行加密1024次
        String pwd = new SimpleHash("MD5", user.getPassword(), salt, 1024).toHex();
        user.setPassword(pwd);

        boolean isOk = this.userService.addUser(user);
        if (isOk) {
            return new RespEntity(RespStatus.SUCCESS);
        } else {
            return new RespEntity(RespStatus.Error);
        }
    }

    @PostMapping("/alter_pwd")
    @ResponseBody
    public RespEntity alterPwd(@RequestParam("oldPwd") String pwd,
                               @RequestParam("newPwd") String newPwd) {
        User curUser = (User) SecurityUtils.getSubject().getPrincipal();
        String username = curUser.getUsername();
        String password = curUser.getPassword();
        System.out.println(curUser);
        ByteSource salt = ByteSource.Util.bytes(username);
        String oldPwd = new SimpleHash("MD5", pwd, salt, 1024).toHex();
        if (!oldPwd.equals(password)) {
            return new RespEntity(RespStatus.UNAUTHEN);
        } else if (!pwd.equals(newPwd)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(new SimpleHash("MD5", newPwd, salt, 1024).toHex());
            userService.updateUser(user);
        }

        return new RespEntity(RespStatus.SUCCESS);
    }

}
