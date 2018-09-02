package com.qa.crud.controller;

import com.qa.crud.domain.Resource;
import com.qa.crud.services.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/findBy")
    public List<Resource> findByNameMatchesRegex(@RequestParam(value = "name") String name) {
        return resourceService.findByNameMatchesRegex(name);
    }

    @PostMapping("/saveresource")
    public Resource save(@RequestParam(value = "name") String name) {
        return resourceService.save(new Resource(name));
    }

    @PostMapping("/updateresource")
    public Resource updateResource(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "id") Long id) {
        return resourceService.updateResource(name, id);
    }

    @PostMapping("assignresource")
    public Resource assignResource(@RequestParam(value = "resId") Long resId,
                                   @RequestParam(value = "accNo") Long accNo) {
        return resourceService.assignResource(resId, accNo);
    }

    @DeleteMapping("/delete")
    public String deleteResource(@RequestParam(value = "id") Long id) {
        return resourceService.delete(id);
    }
}
