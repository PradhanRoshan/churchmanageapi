package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.domain.RgstrnRqstCmnt;
import com.chms.churchmanageapi.dto.RgstrnRqstCmntDTO;
import com.chms.churchmanageapi.repository.RgstrnRqstCmntRepository;
import com.chms.churchmanageapi.service.CommentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl implements CommentsService {

    private static final Logger log = LoggerFactory.getLogger(CommentsServiceImpl.class);


    private final RgstrnRqstCmntRepository rgstrnRqstCmntRepository;

    // Constructor injection for the repository
    public CommentsServiceImpl(RgstrnRqstCmntRepository rgstrnRqstCmntRepository) {
        this.rgstrnRqstCmntRepository = rgstrnRqstCmntRepository;
    }

    @Override
    public String saveComments(RgstrnRqstCmntDTO rgstrnRqstCmntDTO) {
        if(rgstrnRqstCmntDTO.getTextRgstrnRqstCmnt() == null || rgstrnRqstCmntDTO.getTextRgstrnRqstCmnt().trim().isEmpty()){
            log.warn("Invalid comment data received: {}", rgstrnRqstCmntDTO);
            return "Invalid comment data";
        }
        RgstrnRqstCmnt rgstrnRqstCmnt = new RgstrnRqstCmnt();
        rgstrnRqstCmnt.setMemberId(rgstrnRqstCmntDTO.getMemberId());
        rgstrnRqstCmnt.setRgstrnRqstCmntRole(rgstrnRqstCmntDTO.getRgstrnRqstCmntRole());
        rgstrnRqstCmnt.setTextRgstrnRqstCmnt(rgstrnRqstCmntDTO.getTextRgstrnRqstCmnt());
        rgstrnRqstCmnt.setNameRgstrnRqstCmntUser(rgstrnRqstCmntDTO.getNameRgstrnRqstCmntUser());
        // Here you would typically save the comment to the database using the repository, e.g.:
        RgstrnRqstCmnt savedCmnt  = rgstrnRqstCmntRepository.save(rgstrnRqstCmnt);
        log.info("Comment saved successfully. ID={}", savedCmnt.getId());
        return "Comments saved successfully";
    }

    /**
     * Return all comments for a member ordered newest first.
     *
     * @param memberID the member identifier to fetch comments for
     * @return list of comment DTOs (empty list when none found)
     */
    @Override
    public List<RgstrnRqstCmntDTO> getComments(String memberID) {
        // Fetch comments for given memberID ordered by creation time (newest first)
        List<RgstrnRqstCmnt> comments = rgstrnRqstCmntRepository.findByMemberIdOrderByDttmCreateDesc(memberID);

        // Map entities to DTOs
        return comments.stream().map(c -> {
            RgstrnRqstCmntDTO dto = new RgstrnRqstCmntDTO();
            dto.setId(c.getId());
            dto.setMemberId(c.getMemberId());
            dto.setRgstrnRqstCmntRole(c.getRgstrnRqstCmntRole());
            dto.setTextRgstrnRqstCmnt(c.getTextRgstrnRqstCmnt());
            dto.setNameRgstrnRqstCmntUser(c.getNameRgstrnRqstCmntUser());

            // Auditable.dttmCreate is a java.sql.Timestamp; convert to LocalDateTime safely
            try {
                java.sql.Timestamp ts = c.getDttmCreate();
                if (ts != null) {
                    dto.setTimestamp(ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                }
            } catch (Exception ex) {
                log.debug("Failed to convert timestamp for comment id={}: {}", c.getId(), ex.getMessage());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
