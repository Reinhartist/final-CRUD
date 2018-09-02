package com.qa.crud.services;

import com.qa.crud.domain.Project;
import com.qa.crud.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<Project> findByNameMatchesRegex(String name) {
        return projectRepository.findByNameMatchesRegex("(?i).*" + name + ".*");
    }

    @Transactional(readOnly = true)
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public String deleteProject(Long id) {
        projectRepository.deleteById(id);
        return "Deleted";
    }

    @Transactional(readOnly = true)
    public Project updateProject(String name, Boolean active, Long id) {
        projectRepository.updateProject(name, active, id);
        return projectRepository.findExactDetailsById(id);
    }

    @Transactional(readOnly = true)
    public Project assignWorker(Long projId, Long accNo) {
        projectRepository.assignWorker(projId, accNo);
        return projectRepository.findExactDetailsById(projId);
    }

    @Transactional(readOnly = true)
    public Project removeWorker(Long projId, Long accNo) {
        projectRepository.removeWorker(projId, accNo);
        return projectRepository.findExactDetailsById(projId);
    }
}
