package com.chms.churchmanageapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.ApplicationStatus}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusDto implements Serializable {
    private static final long serialVersionUID = -5533758224834880220L;
    private  long statusId;
    private  String statusName;
}