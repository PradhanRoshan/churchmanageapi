package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.domain.RgstrnRqstCmnt;
import com.chms.churchmanageapi.dto.RgstrnRqstCmntDTO;
import com.chms.churchmanageapi.repository.RgstrnRqstCmntRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentsServiceImplTest {

    @Autowired
    private CommentsServiceImpl commentsService;

    @MockBean
    private RgstrnRqstCmntRepository rgstrnRqstCmntRepository;

    @Test
    void getComments_nullMemberId_returnsEmptyListAndDoesNotQueryRepository() {
        List<RgstrnRqstCmntDTO> result = commentsService.getComments(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(rgstrnRqstCmntRepository);
    }

    @Test
    void getComments_blankMemberId_returnsEmptyListAndDoesNotQueryRepository() {
        List<RgstrnRqstCmntDTO> result = commentsService.getComments("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(rgstrnRqstCmntRepository);
    }

    @Test
    void getComments_repositoryReturnsEmpty_returnsEmptyList() {
        when(rgstrnRqstCmntRepository.findByMemberIdOrderByDttmCreateDesc("12345678"))
                .thenReturn(Collections.emptyList());

        List<RgstrnRqstCmntDTO> result = commentsService.getComments("12345678");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rgstrnRqstCmntRepository).findByMemberIdOrderByDttmCreateDesc("12345678");
    }

    @Test
    void getComments_mapsFieldsAndTimestampUsingToLocalDateTime() {
        LocalDateTime now = LocalDateTime.of(2026, 2, 28, 12, 30, 45);
        Timestamp ts = Timestamp.valueOf(now);

        RgstrnRqstCmnt entity = new RgstrnRqstCmnt();
        entity.setId(10L);
        entity.setMemberId("12345678");
        entity.setRgstrnRqstCmntRole("ADMIN");
        entity.setTextRgstrnRqstCmnt("Hello");
        entity.setNameRgstrnRqstCmntUser("Jane");
        entity.setDttmCreate(ts);

        when(rgstrnRqstCmntRepository.findByMemberIdOrderByDttmCreateDesc("12345678"))
                .thenReturn(List.of(entity));

        List<RgstrnRqstCmntDTO> result = commentsService.getComments("12345678");

        assertEquals(1, result.size());
        RgstrnRqstCmntDTO dto = result.get(0);
        assertEquals(10L, dto.getId());
        assertEquals("12345678", dto.getMemberId());
        assertEquals("ADMIN", dto.getRgstrnRqstCmntRole());
        assertEquals("Hello", dto.getTextRgstrnRqstCmnt());
        assertEquals("Jane", dto.getNameRgstrnRqstCmntUser());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void getComments_nullTimestamp_doesNotThrowAndTimestampIsNull() {
        RgstrnRqstCmnt entity = new RgstrnRqstCmnt();
        entity.setId(11L);
        entity.setMemberId("12345678");
        entity.setRgstrnRqstCmntRole("USER");
        entity.setTextRgstrnRqstCmnt("Hi");
        entity.setNameRgstrnRqstCmntUser("John");
        entity.setDttmCreate(null);

        when(rgstrnRqstCmntRepository.findByMemberIdOrderByDttmCreateDesc("12345678"))
                .thenReturn(List.of(entity));

        List<RgstrnRqstCmntDTO> result = assertDoesNotThrow(() -> commentsService.getComments("12345678"));

        assertEquals(1, result.size());
        assertNull(result.get(0).getTimestamp());
    }

    @Test
    void getComments_skipsNullEntitiesInsteadOfFailingWholeCall() {
        RgstrnRqstCmnt entity = new RgstrnRqstCmnt();
        entity.setId(12L);
        entity.setMemberId("12345678");
        entity.setRgstrnRqstCmntRole("USER");
        entity.setTextRgstrnRqstCmnt("A");
        entity.setNameRgstrnRqstCmntUser("B");

        when(rgstrnRqstCmntRepository.findByMemberIdOrderByDttmCreateDesc("12345678"))
                .thenReturn(Arrays.asList(null, entity, null));

        List<RgstrnRqstCmntDTO> result = commentsService.getComments("12345678");

        assertEquals(1, result.size());
        assertEquals(12L, result.get(0).getId());
    }

    @Test
    void saveComments_validComment_persistsAndReturnsSuccessMessage() {
        RgstrnRqstCmntDTO dto = new RgstrnRqstCmntDTO();
        dto.setMemberId("MEM-5");
        dto.setRgstrnRqstCmntRole("Admin");
        dto.setTextRgstrnRqstCmnt("Reminder to update profile. Thanks!");
        dto.setNameRgstrnRqstCmntUser("Admin User");

        RgstrnRqstCmnt saved = new RgstrnRqstCmnt();
        saved.setId(99L);

        when(rgstrnRqstCmntRepository.save(any(RgstrnRqstCmnt.class))).thenReturn(saved);

        String result = commentsService.saveComments(dto);

        assertEquals("Comments saved successfully", result);

        verify(rgstrnRqstCmntRepository).save(argThat(entity ->
                "MEM-5".equals(entity.getMemberId()) &&
                        "Admin".equals(entity.getRgstrnRqstCmntRole()) &&
                        "Reminder to update profile. Thanks!".equals(entity.getTextRgstrnRqstCmnt()) &&
                        "Admin User".equals(entity.getNameRgstrnRqstCmntUser())
        ));
    }

    @Test
    void saveComments_nullText_returnsInvalidMessageAndDoesNotPersist() {
        RgstrnRqstCmntDTO dto = new RgstrnRqstCmntDTO();
        dto.setMemberId("MEM-5");
        dto.setRgstrnRqstCmntRole("Admin");
        dto.setTextRgstrnRqstCmnt(null);
        dto.setNameRgstrnRqstCmntUser("Admin User");

        String result = commentsService.saveComments(dto);

        assertEquals("Invalid comment data", result);
        verifyNoInteractions(rgstrnRqstCmntRepository);
    }

    @Test
    void saveComments_blankText_returnsInvalidMessageAndDoesNotPersist() {
        RgstrnRqstCmntDTO dto = new RgstrnRqstCmntDTO();
        dto.setMemberId("MEM-5");
        dto.setRgstrnRqstCmntRole("Admin");
        dto.setTextRgstrnRqstCmnt("   ");
        dto.setNameRgstrnRqstCmntUser("Admin User");

        String result = commentsService.saveComments(dto);

        assertEquals("Invalid comment data", result);
        verifyNoInteractions(rgstrnRqstCmntRepository);
    }
}
