package com.qa.crud.controller;

import com.qa.crud.domain.Person;
import com.qa.crud.services.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/findBy")
    public List<Person> findByNameMatchesRegex(@RequestParam(value = "name") String name) {
        String firstName = name.split(" ")[0];
        return personService.findByNameMatchesRegex(firstName);
    }

    @PostMapping("/saveperson")
    public Person savePerson(@RequestParam(value = "firstName") String firstName,
                           @RequestParam(value = "lastName") String lastName) {
        return personService.save(new Person(firstName, lastName));
    }

    @PostMapping("/updateperson")
    public Person updatePerson(@RequestParam(value = "firstName") String firstName,
                               @RequestParam(value = "lastName") String lastName,
                               @RequestParam(value = "id") Long id) {

        return personService.updatePerson(firstName, lastName, id);
    }

    @DeleteMapping("/delete")
    public String deletePerson(@RequestParam(value = "id") Long id) {
        return personService.deletePerson(id);
    }
}
