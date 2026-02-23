package com.chms.churchmanageapi.controller;

import com.chms.churchmanageapi.dto.ApplicationReviewDecisionDTO;
import com.chms.churchmanageapi.dto.ApplicationStatusHistoryDto;
import com.chms.churchmanageapi.dto.RgstrnRqstCmntDTO;
import com.chms.churchmanageapi.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {


    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }


    @Operation(summary = "Get application progress history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved application progress history"),
            @ApiResponse(responseCode = "400", description = "Invalid member ID"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/getComments/{memberID}", produces = "application/json")
    public ResponseEntity<List<RgstrnRqstCmntDTO> > getComments(@PathVariable String memberID) {
        List<RgstrnRqstCmntDTO> comments = commentsService.getComments(memberID);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Save comments for application in progress status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed the review decision"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/saveComments", produces = "application/json")
    public ResponseEntity<String> saveComments(@RequestBody RgstrnRqstCmntDTO rgstrnRqstCmntDTO) {
        String response = commentsService.saveComments(rgstrnRqstCmntDTO);
        return ResponseEntity.ok(response);
    }

}
