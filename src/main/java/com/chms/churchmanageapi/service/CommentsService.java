package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.dto.RgstrnRqstCmntDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentsService {
    String saveComments(RgstrnRqstCmntDTO rgstrnRqstCmntDTO);

    List<RgstrnRqstCmntDTO> getComments(String memberID);
}
