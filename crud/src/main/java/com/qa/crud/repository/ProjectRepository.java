package com.qa.crud.repository;

import com.qa.crud.domain.Project;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends Neo4jRepository<Project, Long> {

    @Query("match (proj: Project) where proj.name =~ {regex} return proj, " +
            "[ [ (proj)-[proj_to_per: works_on]->(per: Person) | [ proj_to_per, per ] ], " +
            "[ (per)-[per_to_res: owns]->(res: Resource) | [ per_to_res, res ] ] ], ID(proj)")
    List<Project> findByNameMatchesRegex(@Param("regex") String regex);

    @Query("match (p: Project) where ID(p) = {id} set p.name = {name}, p.active = {active}")
    Project updateProject(@Param("name") String name, @Param("active") Boolean active,
                          @Param("id") Long id);

    @Query("match (proj: Project) where ID(proj) = {id} return proj, " +
            "[ [ (proj)-[proj_to_per: works_on]->(per: Person) | [ proj_to_per, per ] ], " +
            "[ (per)-[per_to_res: owns]->(res: Resource) | [ per_to_res, res ] ] ], ID(proj)")
    Project findExactDetailsById(@Param("id") Long id);

    @Query("match (proj: Project), (y: Person) where ID(proj) = {id} and y.accountNumber = {accNo} " +
            "create (proj)-[:works_on]->(y)")
    void assignWorker(@Param("id") Long projId, @Param("accNo") Long accNo);

    @Query("match (proj: Project), (y: Person), (proj)-[r: works_on]->(y) " +
            "where ID(proj) = {id} and y.accountNumber = {accNo} " +
            "delete r")
    void removeWorker(@Param("id") Long id, @Param("accNo") Long accNo);
}
