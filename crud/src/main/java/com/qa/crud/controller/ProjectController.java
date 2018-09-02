package com.qa.crud.controller;

import com.qa.crud.domain.Project;
import com.qa.crud.services.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/findBy")
    public List<Project> findByNameLike(@RequestParam(value = "name") String name) {
        return projectService.findByNameMatchesRegex(name);
    }

    @PostMapping("/saveproject")
    public Project saveProject(@RequestParam(value = "name") String name,
                                @RequestParam(value = "active") Boolean active) {
        return projectService.saveProject(new Project(name, active));
    }

    @PostMapping("/changeworker")
    public Project changeWorkers(@RequestParam(value = "id") Long projId,
                                 @RequestParam(value = "accNo") Long accNo,
                                 @RequestParam(value = "assign") Boolean assign) {
        if(assign) return projectService.assignWorker(projId, accNo);
        else return projectService.removeWorker(projId, accNo);
    }

    @PostMapping("/updateproject")
    public Project updateProject(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "active") Boolean active,
                                 @RequestParam(value = "id") Long id) {
        return projectService.updateProject(name, active, id);
    }

    @DeleteMapping("/delete")
    public String deleteProject(@RequestParam(value = "id") Long id) {
        return projectService.deleteProject(id);
    }
}
