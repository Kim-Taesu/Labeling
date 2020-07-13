package smu.datalab.homepage.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Labeling {
    @Id
    @GeneratedValue
    Long id;

    String date;
    @Lob
    String content;
    String owner;
    String emotion;
}
