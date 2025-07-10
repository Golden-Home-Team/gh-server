package kr.co.goldenhome.facility;

import kr.co.goldenhome.dto.Service;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.StringUtils;

public class ServiceFieldSetMapper implements FieldSetMapper<Service> {

    @Override
    public Service mapFieldSet(FieldSet fieldSet) {

        String phoneNumber = fieldSet.readString("phoneNumber");
        if (StringUtils.hasText(phoneNumber)) phoneNumber = phoneNumber.replace("\"", "");

        return Service.batch(
                fieldSet.readString("type"),
                fieldSet.readString("name"),
                fieldSet.readString("districtName"),
                fieldSet.readString("director"),
                fieldSet.readString("capacity"),
                fieldSet.readString("currentTotal"),
                fieldSet.readString("currentMale"),
                fieldSet.readString("currentFemale"),
                fieldSet.readString("staffTotal"),
                fieldSet.readString("staffMale"),
                fieldSet.readString("staffFemale"),
                fieldSet.readString("address"),
                phoneNumber,
                null,
                null
        );
    }
}
