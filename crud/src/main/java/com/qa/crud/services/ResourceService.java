package com.qa.crud.services;

import com.qa.crud.domain.Resource;
import com.qa.crud.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Transactional(readOnly = true)
    public List<Resource> findByNameMatchesRegex(String name) {
        return resourceRepository.findByNameMatchesRegex("(?i).*" + name + "(?i).*");
    }

    @Transactional(readOnly = true)
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Transactional(readOnly = true)
    public String delete(Long id) {
        resourceRepository.deleteById(id);
        return "Deleted";
    }

    @Transactional(readOnly = true)
    public Resource updateResource(String name, Long id) {
        return resourceRepository.updateResource(name, id);
    }

    @Transactional(readOnly = true)
    public Resource assignResource(Long resId, Long accNo) {
        if(accNo == -1) return resourceRepository.deassignResource(resId);
        return resourceRepository.assignResource(resId, accNo);
    }
}
