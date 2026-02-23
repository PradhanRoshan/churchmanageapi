package com.chms.churchmanageapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.RgstrnRqstCmnt}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RgstrnRqstCmntDTO implements Serializable {
    private static final long serialVersionUID = 951553197465538947L;
    private Long id;
    private String memberId;
    private String rgstrnRqstCmntRole;
    private String textRgstrnRqstCmnt;
    private String nameRgstrnRqstCmntUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}