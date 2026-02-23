package com.chms.churchmanageapi.repository;

import com.chms.churchmanageapi.domain.RgstrnRqstCmnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RgstrnRqstCmntRepository extends JpaRepository<RgstrnRqstCmnt, Long> {

    // Find all comments for a given memberId ordered by creation timestamp (newest first)
    List<RgstrnRqstCmnt> findByMemberIdOrderByDttmCreateDesc(String memberId);

}
