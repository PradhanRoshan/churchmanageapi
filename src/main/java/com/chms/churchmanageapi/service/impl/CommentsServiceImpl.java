package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.domain.RgstrnRqstCmnt;
import com.chms.churchmanageapi.dto.RgstrnRqstCmntDTO;
import com.chms.churchmanageapi.repository.RgstrnRqstCmntRepository;
import com.chms.churchmanageapi.service.CommentsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * Fetches all registration request comments for the given member id.
     *
     * <p>Ordering: results are returned newest-first based on the entity creation timestamp
     * ({@code dttmCreate}) as defined by the underlying repository query.</p>
     *
     * <p>Validation: if {@code memberID} is {@code null} or blank, this method returns an empty list
     * and does not call the repository.</p>
     *
     * <p>Transactional: executes in a read-only transaction to keep JPA read semantics consistent
     * and to allow provider optimizations.</p>
     *
     * @param memberID the member identifier to fetch comments for
     * @return an immutable empty list when the input is invalid or no comments exist; otherwise a
     * mutable list of mapped DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<RgstrnRqstCmntDTO> getComments(String memberID) {
        if (StringUtils.isBlank(memberID)) {
            return Collections.emptyList();
        }
        final List<RgstrnRqstCmnt> comments = rgstrnRqstCmntRepository.findByMemberIdOrderByDttmCreateDesc(memberID);
        if (comments == null || comments.isEmpty()) {
            return Collections.emptyList();
        }

        // Map entities to DTOs (avoid streams to reduce allocations under load)
        final List<RgstrnRqstCmntDTO> rqstCmntDTOS = new ArrayList<>(comments.size());
        for (RgstrnRqstCmnt comment : comments) {
            if (comment == null) {
                continue;
            }
            rqstCmntDTOS.add(commentMapDto(comment));
        }
        return rqstCmntDTOS;
    }

    private RgstrnRqstCmntDTO commentMapDto(RgstrnRqstCmnt comment) {
        final RgstrnRqstCmntDTO rqstCmntDTO = new RgstrnRqstCmntDTO();
        rqstCmntDTO.setId(comment.getId());
        rqstCmntDTO.setMemberId(comment.getMemberId());
        rqstCmntDTO.setRgstrnRqstCmntRole(comment.getRgstrnRqstCmntRole());
        rqstCmntDTO.setTextRgstrnRqstCmnt(comment.getTextRgstrnRqstCmnt());
        rqstCmntDTO.setNameRgstrnRqstCmntUser(comment.getNameRgstrnRqstCmntUser());

        // Auditable.dttmCreate is a java.sql.Timestamp.
        // Prefer Timestamp#toLocalDateTime (no implicit zone conversion).
        try {
            final Timestamp ts = comment.getDttmCreate();
            if (ts != null) {
                rqstCmntDTO.setTimestamp(ts.toLocalDateTime());
            }
        } catch (RuntimeException ex) {
            // Don't fail the whole request for a single bad timestamp.
            log.debug("Failed to convert timestamp for comment id={}", comment.getId(), ex);
        }

        return rqstCmntDTO;
    }
}
