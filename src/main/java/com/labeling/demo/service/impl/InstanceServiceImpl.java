package com.labeling.demo.service.impl;

import com.labeling.demo.entity.Instance;
import com.labeling.demo.repository.InstanceRepository;
import com.labeling.demo.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstanceServiceImpl implements InstanceService {

    private InstanceRepository instanceRepository;

    @Autowired
    public InstanceServiceImpl(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Instance instance) {
        instanceRepository.save(instance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(Iterable<Instance> instances) {
        instanceRepository.saveAll(instances);
    }

    @Override
    public List<Instance> findAll() {
        return instanceRepository.findAll();
    }

    @Override
    public Page<Instance> findPageData(Pageable instPage) {
        return instanceRepository.findAll(instPage);
    }

    @Override
    public Instance findInstById(Long id) {
        return instanceRepository.findByInstanceId(id);
    }

    @Override
    public List<Instance> findByTaskName(String taskName) {
        return instanceRepository.findByTaskName(taskName);
    }

    @Override
    public long count() {
        return instanceRepository.count();
    }

    @Override
    public Instance findById(Long instanceId) {
        return instanceRepository.findByInstanceId(instanceId);
    }


}
