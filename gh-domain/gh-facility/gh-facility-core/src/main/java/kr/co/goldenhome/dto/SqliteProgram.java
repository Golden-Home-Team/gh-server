package kr.co.goldenhome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqliteProgram {
    private Long index;
    private String facilityId;
    private String type;
    private String name;
    private String capacity;
    private String time;
    private String place;
}
