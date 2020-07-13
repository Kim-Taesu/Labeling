package smu.datalab.homepage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import smu.datalab.homepage.aspect.LoggingAnnotation;
import smu.datalab.homepage.service.LabelingService;

import java.security.Principal;

import static java.util.Objects.nonNull;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LabelingService labelingService;

    @GetMapping("/")
    @LoggingAnnotation
    public String index(Model model, Principal principal) {
        if (nonNull(principal)) {
            final String name = principal.getName();
            final Long totalById = labelingService.getTotalById(name);
            final Long todoById = labelingService.getTodoById(name);
            model.addAttribute("total", totalById);
            model.addAttribute("todo", todoById);
            if (todoById == 0 && totalById != 0) model.addAttribute("message", "All Task Clear");
        }
        System.out.println(model.asMap().keySet());
        return "index";
    }
}
