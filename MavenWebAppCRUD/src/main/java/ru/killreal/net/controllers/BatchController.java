package ru.killreal.net.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.killreal.net.dao.PersonDAO;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController {

    public BatchController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    private final PersonDAO personDAO;

    @GetMapping()
    public String index() {
        return ("batch/index");
    }

    @GetMapping("/without")
    public String withoutBatch() {
        personDAO.testMultipleUpdate();
        return ("redirect:/people");
    }

    @GetMapping("/with")
    public String withBatch() {
        personDAO.testBatchUpdate();
        return ("redirect:/people");
    }

}
