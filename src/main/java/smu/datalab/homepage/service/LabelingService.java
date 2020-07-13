package smu.datalab.homepage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import smu.datalab.homepage.dto.Labeling;
import smu.datalab.homepage.repository.LabelingRepository;
import smu.datalab.homepage.vo.AddInfo;
import smu.datalab.homepage.vo.EmotionInfo;
import smu.datalab.homepage.vo.NextLabel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabelingService {
    private final LabelingRepository labelingRepository;

    public Optional<NextLabel> getOneLabel(String id) {
        final Optional<Labeling> next = labelingRepository.findFirstByOwnerAndEmotionIsNull(id);
        if (!next.isPresent()) return Optional.empty();
        Labeling label = next.get();
        final Long todoById = getTodoById(id);
        final Long totalById = getTotalById(id);
        NextLabel nextLabel = new NextLabel();
        nextLabel.setLabeling(label);
        nextLabel.setTodo(Math.toIntExact(todoById));
        nextLabel.setTotal(Math.toIntExact(totalById));
        return Optional.of(nextLabel);
    }

    public Boolean saveEmotion(EmotionInfo emotionInfo) {
        final Optional<Labeling> byId = labelingRepository.findById(emotionInfo.getId());
        if (!byId.isPresent()) return false;
        final Labeling labeling = byId.get();
        labeling.setEmotion(emotionInfo.getEmotion());
        labelingRepository.save(labeling);
        return true;
    }

    public Long getTodoById(String name) {
        return labelingRepository.countByOwnerAndEmotionIsNull(name);
    }

    public Long getTotalById(String name) {
        return labelingRepository.countByOwner(name);
    }

    public Long getTodo() {
        return labelingRepository.countAllByEmotionIsNullAndOwnerIsNull();
    }

    public void addLabel(AddInfo addInfo) {
        final Long count = addInfo.getCount();
        final String id = addInfo.getId();
        final Page<Labeling> all = labelingRepository.findAllByOwnerIsNull(PageRequest.of(0, Math.toIntExact(count)).first());
        final List<Labeling> collect = all.get().collect(Collectors.toList());
        collect.forEach(labeling -> labeling.setOwner(id));
        labelingRepository.saveAll(collect);
        labelingRepository.flush();
    }
}
