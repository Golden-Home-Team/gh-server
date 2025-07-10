package kr.co.goldenhome.facility;

import kr.co.goldenhome.dto.Facility;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.util.StringUtils;

    public class FacilityFieldSetMapper implements FieldSetMapper<Facility> {

        @Override
        public Facility mapFieldSet(FieldSet fieldSet) {

            String phoneNumberAndFaxNumber1 = fieldSet.readString("phoneNumberAndFaxNumber1");
            String phoneNumberAndFaxNumber2 = fieldSet.readString("phoneNumberAndFaxNumber2");
            String phoneNumberAndFaxNumber;
            if (StringUtils.hasText(phoneNumberAndFaxNumber1)) phoneNumberAndFaxNumber = phoneNumberAndFaxNumber1;
            else if (StringUtils.hasText(phoneNumberAndFaxNumber2))phoneNumberAndFaxNumber = phoneNumberAndFaxNumber2;
            else phoneNumberAndFaxNumber = null;
            String phoneNumber = null;
            if (StringUtils.hasText(phoneNumberAndFaxNumber)) {
                phoneNumber = phoneNumberAndFaxNumber.replace("\"", "");
            }

            String establishmentDate1 = fieldSet.readString("establishmentDate1");
            String establishmentDate2 = fieldSet.readString("establishmentDate2");
            String establishmentDate;
            if (StringUtils.hasText(establishmentDate1)) establishmentDate = establishmentDate1;
            else if (StringUtils.hasText(establishmentDate2)) establishmentDate = establishmentDate2;
            else establishmentDate = null;


            String operatingBody1 = fieldSet.readString("operatingBody1");
            String operatingBody2 = fieldSet.readString("operatingBody2");
            String operatingBody;
            if (StringUtils.hasText(operatingBody1)) operatingBody = operatingBody1;
            else if (StringUtils.hasText(operatingBody2)) operatingBody = operatingBody2;
            else operatingBody = null;

            return Facility.batch(
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
                    establishmentDate,
                    operatingBody
            );
        }
    }
