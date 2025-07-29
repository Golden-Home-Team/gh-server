package kr.co.goldenhome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlitePhoto {
    private Long index;
    private String facilityId;
    private String type;
    private String name;
    private String imageUrl;
    private String description;

}
