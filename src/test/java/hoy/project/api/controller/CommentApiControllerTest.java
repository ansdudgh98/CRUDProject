package hoy.project.api.controller;

import hoy.project.api.controller.dto.request.form.CommentForm;
import hoy.project.api.controller.dto.response.comment.CommentDeleteResponse;
import hoy.project.api.controller.dto.response.comment.CommentEditResponse;
import hoy.project.api.controller.dto.response.comment.CommentPostResponse;
import hoy.project.api.controller.dto.response.comment.CommentsReadResponse;
import hoy.project.api.controller.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentApiControllerTest extends ControllerTest {

    String userId = "test1234";

    @BeforeEach
    public void initSession() {
        session.setAttribute(SessionConst.attributeName, userId);
    }


    @Test
    @DisplayName("[API][Comment]댓글 작성 성공 테스트")
    public void commentPostTest1() throws Exception {
        given(commentService.post(any(CommentForm.class), eq(1L), eq(userId))).willReturn(new CommentPostResponse(1L));

        mockMvc.perform(post("/api/comment/?article=" + 1L)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentForm("테스트 게시물 입니다."))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("[API][Comment]댓글 수정 성공 테스트")
    public void commentEditTest1() throws Exception {

        CommentForm commentForm = new CommentForm("테스트 수정 댓글 입니다.");

        given(commentService.edit(any(CommentForm.class), eq(1L), eq(userId))).willReturn(new CommentEditResponse(1L));

        mockMvc.perform(post("/api/comment/edit/" + 1L)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("[API][Comment]댓글 삭제 성공 테스트")
    public void commentDeleteTest1() throws Exception {

        given(commentService.delete(eq(1L), eq(userId))).willReturn(new CommentDeleteResponse());

        mockMvc.perform(get("/api/comment/delete/" + 1L)
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[API][Comment]댓글 10개 읽기 성공 테스트 - 다음 댓글이 존재할때")
    public void ListCommentReadTest1() throws Exception {
        given(commentService.readCommentLatest10(anyLong(), eq(0))).willReturn(new CommentsReadResponse(true, anyList()));

        mockMvc.perform(get("/api/comment/0?article=" + anyLong())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(print());
    }
}