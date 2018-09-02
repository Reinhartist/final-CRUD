package com.qa.crud.services;

import com.qa.crud.domain.Person;
import com.qa.crud.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> findByNameMatchesRegex(String name) {
        return personRepository.findByNameMatchesRegex("(?i).*" + name + ".*");
    }

    @Transactional(readOnly = true)
    public Person save(Person person) {
        Long accNo = personRepository.generateAccountNumber();
        person.setAccountNumber(accNo == null ? 1 : accNo + 1);
        return personRepository.save(person);
    }

    @Transactional(readOnly = true)
    public Person updatePerson(String firstName, String lastName, Long id) {
        return personRepository.updatePerson(firstName, lastName, id);
    }

    @Transactional(readOnly = true)
    public String deletePerson(Long id) {
        personRepository.deleteById(id);
        return "Deleted";
    }
}