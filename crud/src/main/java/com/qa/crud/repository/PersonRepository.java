package com.qa.crud.repository;

import com.qa.crud.domain.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, Long> {
    List<Person> findByNameMatchesRegex(String regex);

    @Query("match(x: Person) return max(x.accountNumber)")
    Long generateAccountNumber();

    @Query("match (x: Person) where ID(x) = {id} set x.name = {firstName}," +
            "x.lastName = {lastName} return x, " +
            "[ [ (x)<-[r_w: works_on]-(p: Project) | [ r_w, p ] ], " +
            "[ (x)-[r_o: owns]->(r: Resource) | [ r_o, r ] ] ]")
    Person updatePerson(@Param("firstName") String firstName,
                        @Param("lastName") String lastName,
                        @Param("id") Long id);

}
