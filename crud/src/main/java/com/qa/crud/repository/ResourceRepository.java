package com.qa.crud.repository;

import com.qa.crud.domain.Resource;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends Neo4jRepository<Resource, Long> {
    List<Resource> findByNameMatchesRegex(String regex);

    @Query("match (x: Resource) where ID(x) = {id} set x.name = {name} return x, " +
            "[ [ (x)<-[r: owns]-(y: Person) | [ r, y ] ] ]")
    Resource updateResource(@Param("name") String name, @Param("id") Long id);

    @Query("match (x: Resource), (y: Person) where ID(x) = {resourceId} and " +
            "y.accountNumber = {accNo} " +
            "create (y) - [:owns] -> (x) return x, " +
            "[ [ (x)<-[r: owns]-(y: Person) | [ r, y ] ] ]")
    Resource assignResource(@Param("resourceId") Long resId, @Param("accNo") Long accNo);

    @Query("match (x: Resource), (y)-[r: owns]->(x) where ID(x) = {resourceId} " +
            "delete r return x")
    Resource deassignResource(@Param("resourceId") Long resId);
}