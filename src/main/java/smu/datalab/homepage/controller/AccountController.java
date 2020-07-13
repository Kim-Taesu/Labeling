package smu.datalab.homepage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import smu.datalab.homepage.aspect.LoggingAnnotation;
import smu.datalab.homepage.dto.Account;
import smu.datalab.homepage.service.AccountService;
import smu.datalab.homepage.service.LabelingService;
import smu.datalab.homepage.vo.AddInfo;
import smu.datalab.homepage.vo.EditInfo;

import java.security.Principal;
import java.util.List;

import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final LabelingService labelingService;

    @GetMapping("/loginPage")
    public String loginPage(Principal principal) {
        if (nonNull(principal)) return "redirect:/";
        else return "login";
    }

    @GetMapping("/setUp")
    @LoggingAnnotation
    public String setupPage(Model model) {
        final List<String> users = accountService.getAllUsers();
        final Long todo = labelingService.getTodo();
        model.addAttribute("users", users);
        model.addAttribute("todo", todo);
        return "setUp";
    }

    @PostMapping("/setUp")
    @LoggingAnnotation
    public String addLabel(AddInfo addInfo) {
        labelingService.addLabel(addInfo);
        return "redirect:/account/setUp";
    }

    @GetMapping("/add")
    @LoggingAnnotation
    public String addAccount() {
        return "add-account";
    }

    @PostMapping("/signUp")
    @LoggingAnnotation
    public String signUp(Account account) {
        accountService.signUp(account);
        return "redirect:/";
    }

    @GetMapping("/status")
    public String status(Model model) {
        model.addAttribute("status", accountService.getAccountStatus());
        return "status";
    }

    @GetMapping("/edit")
    public String editPage(Model model) {
        model.addAttribute("users", accountService.getAllUsers());
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(EditInfo editInfo, RedirectAttributes attributes) {
        if (accountService.edit(editInfo)) {
            attributes.addFlashAttribute("message", "edit success");
        } else {
            attributes.addFlashAttribute("message", "edit fail");
        }
        return "redirect:/";
    }

    @GetMapping("/detail")
    public ResponseEntity detail(String name) {
        final Long detail = accountService.getDetail(name);
        return ResponseEntity.ok().body(detail);
    }
}
