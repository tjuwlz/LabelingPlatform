package com.labeling.demo.service.impl;

import com.labeling.demo.entity.TagType;
import com.labeling.demo.entity.Task;
import com.labeling.demo.repository.TaskMapper;
import com.labeling.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final static String root = "annotate/";
    private TaskMapper taskMapper;

    @Autowired
    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(Task task) {
        return taskMapper.insertSelective(task) >= 1;
    }

    @Override
    public List<Task> findAll() {
        return taskMapper.findAll();
    }

//    public Task findByName(String taskName) {
//        return taskMapper.selectByPrimaryKey(taskName);
//    }

    @Override
    public Task findById(Integer taskId) {
        return taskMapper.selectByPrimaryKey(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(Task task) {
        return taskMapper.updateByPrimaryKey(task);
    }

    @Override
    public List<Task> findByExpertName(String expertName) {
        return taskMapper.findByExpert(expertName);
    }

    @Override
    public String pageScheduler(Task task) {
        return root + TagType.getTypeByID(task.getDatatype()).getUrl();
    }

    @Override
    public Task findByName(String taskName) {
        return taskMapper.findByName(taskName);
    }

    @Override
    public Integer taskCount() {
        return taskMapper.count();
    }

}
