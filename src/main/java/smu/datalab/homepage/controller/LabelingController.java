package smu.datalab.homepage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import smu.datalab.homepage.aspect.LoggingAnnotation;
import smu.datalab.homepage.service.LabelingService;
import smu.datalab.homepage.vo.EmotionInfo;
import smu.datalab.homepage.vo.NextLabel;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/labeling")
@RequiredArgsConstructor
public class LabelingController {

    private final LabelingService labelingService;
    private final List<String> emotions;

    @GetMapping("/list")
    @LoggingAnnotation
    public String labelList(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        final Optional<NextLabel> labeling = labelingService.getOneLabel(principal.getName());
        if (labeling.isPresent()) {
            model.addAttribute("data", labeling.get());
            model.addAttribute("emotions", emotions);
            return "list";
        } else {
            redirectAttributes.addFlashAttribute("message", "라벨링할 데이터가 없습니다.");
            model.addAttribute("data", null);
            return "redirect:/";
        }
    }

    @GetMapping("/next")
    @LoggingAnnotation
    public ResponseEntity nextLabel(Principal principal,
                                    EmotionInfo emotionInfo) {
        if (principal.getName().equals(emotionInfo.getOwner())) {
            labelingService.saveEmotion(emotionInfo);
            final Optional<NextLabel> labeling = labelingService.getOneLabel(principal.getName());
            if (labeling.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(labeling.get());
            else return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
