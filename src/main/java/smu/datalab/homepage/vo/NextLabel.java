package smu.datalab.homepage.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import smu.datalab.homepage.dto.Labeling;

@Setter
@Getter
@ToString
public class NextLabel {
    Labeling labeling;
    Integer total;
    Integer todo;
}
