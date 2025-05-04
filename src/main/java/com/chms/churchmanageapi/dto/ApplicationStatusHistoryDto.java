package com.chms.churchmanageapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.ApplicationStatusHistory}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationStatusHistoryDto implements Serializable {
    private static final long serialVersionUID = 3475367302150381605L;
    private long id;
    private String applicationStatus;
    private String applicationType;
    private String comment;
    private String idUserCreate;
    private String idUserLstUpdt;
    @JsonFormat(pattern="MM-dd-yyyy", timezone="America/New_York")
    private  Timestamp dttmCreate;
    @JsonFormat(pattern="MM-dd-yyyy", timezone="America/New_York")
    private  Timestamp dttmLstUpdt;
}