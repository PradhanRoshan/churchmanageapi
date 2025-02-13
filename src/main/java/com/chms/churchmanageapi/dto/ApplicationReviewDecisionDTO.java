package com.chms.churchmanageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.chms.churchmanageapi.domain.Member}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationReviewDecisionDTO implements Serializable {
    private static final long serialVersionUID = 1593983468869797826L;
    private  String memberId;
    private RoleDto role;
    private  ApplicationStatusDto applicationStatus;
}