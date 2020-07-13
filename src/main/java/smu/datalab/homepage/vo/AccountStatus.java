package smu.datalab.homepage.vo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountStatus {
    String id;
    Long total;
    Long todo;
}
