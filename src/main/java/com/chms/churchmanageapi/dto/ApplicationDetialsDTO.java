package com.chms.churchmanageapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDetialsDTO implements Serializable {
    private static final long serialVersionUID = -5975937507795382872L;
    private UserDetialsDto userDetails;
    private List<RgstrnRqstCmntDTO> comments;
}
