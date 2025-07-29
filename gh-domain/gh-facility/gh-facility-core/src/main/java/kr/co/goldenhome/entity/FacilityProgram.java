package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.dto.SqliteProgram;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "facility_programs",
        indexes = {
                @Index(name = "idx_facility_programs_institution_symbol", columnList = "institution_symbol")
        })
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FacilityProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String institutionSymbol;
    private String type;
    private String name;
    private String capacity;
    private String time;
    private String place;

    @Builder
    private FacilityProgram(Long id, String institutionSymbol, String type, String name, String capacity, String time, String place) {
        this.id = id;
        this.institutionSymbol = institutionSymbol;
        this.type = type;
        this.name = name;
        this.capacity = capacity;
        this.time = time;
        this.place = place;
    }

    public static FacilityProgram from(SqliteProgram sqliteProgram) {
        return FacilityProgram.builder()
                .id(sqliteProgram.getIndex())
                .institutionSymbol(sqliteProgram.getFacilityId())
                .type(sqliteProgram.getType())
                .name(sqliteProgram.getName())
                .capacity(sqliteProgram.getCapacity())
                .time(sqliteProgram.getTime())
                .place(sqliteProgram.getPlace())
                .build();
    }
}
