package hoy.project.api.controller;

import hoy.project.api.controller.dto.request.form.ReplyForm;
import hoy.project.api.controller.dto.response.reply.ReplyDeleteResponse;
import hoy.project.api.controller.dto.response.reply.ReplyReadListResponse;
import hoy.project.api.controller.dto.response.reply.ReplyWriteAndEditResponse;
import hoy.project.api.controller.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReplyControllerTest extends ControllerTest {

    String userId = "test1234";

    @BeforeEach
    public void initSession() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        session.setAttribute(SessionConst.attributeName, userId);
    }

    @Test
    @DisplayName("[API][Reply]대댓글 작성 성공 테스트")
    public void writeCommentTest() throws Exception {

        ReplyForm replyForm = new ReplyForm("테스트");

        given(replyService.writeReply(any(ReplyForm.class), eq(1L), eq(userId))).willReturn(new ReplyWriteAndEditResponse(1L));

        mockMvc.perform(post("/api/reply?comment=" + 1L)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replyForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new ReplyWriteAndEditResponse(1L))));
    }


    @Test
    @DisplayName("[API][Reply]대댓글 수정 성공 테스트")
    public void editReplyTest() throws Exception {

        ReplyForm replyForm = new ReplyForm("테스트");

        given(replyService.editReply(any(ReplyForm.class), eq(1L), eq(userId))).willReturn(new ReplyWriteAndEditResponse(1L));

        mockMvc.perform(post("/api/reply/edit/" + 1L)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replyForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new ReplyWriteAndEditResponse(1L))));
    }

    @Test
    @DisplayName("[API][Reply]대댓글 삭제 성공 테스트")
    public void deleteReplyTest() throws Exception {

        given(replyService.deActive(eq(1L), eq(userId))).willReturn(new ReplyDeleteResponse());

        mockMvc.perform(get("/api/reply/delete/" + 1L)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new ReplyDeleteResponse())));

    }

    @Test
    @DisplayName("[API][Reply]대댓글 10개 읽기 성공 테스트")
    public void readReplyTest() throws Exception {
        given(replyService.readReplyLatest10(anyLong(), eq(0))).willReturn(new ReplyReadListResponse(true, anyList()));

        mockMvc.perform(get("/api/reply/read/" + anyLong() + "?index=" + 0)
                        .session(session))
                .andExpect(status().isOk());
    }


}
