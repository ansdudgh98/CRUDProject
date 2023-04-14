package hoy.project.service;

import hoy.project.api.controller.dto.request.form.CommentForm;
import hoy.project.api.controller.dto.response.comment.*;
import hoy.project.domain.Account;
import hoy.project.domain.Article;
import hoy.project.domain.Comment;
import hoy.project.repository.AccountRepository;
import hoy.project.repository.ArticleRepository;
import hoy.project.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;

    @Override
    public CommentPostResponse post(CommentForm commentForm, Long articleId, String userId) {

        Article article = articleRepository.findArticleById(articleId);
        Account account = accountRepository.findByUserId(userId);

        if(article == null){
            throw new IllegalArgumentException("존재하지 않는 게시물에 댓글을 작성하려고 합니다.");
        }

        if(account == null){
            throw new IllegalArgumentException("존재하지 않는 게시물에 댓글을 작성하려고 합니다.");
        }

        Comment comment = new Comment(commentForm.getContent(),account,article);

        commentRepository.save(comment);

        return new CommentPostResponse(comment.getId());
    }

    @Override
    public CommentEditResponse edit(CommentForm commentForm, Long commentId, String userId) {

        Account account = accountRepository.findByUserId(userId);
        Comment findComment = commentRepository.findCommentById(commentId);

        if(findComment == null){
            throw new IllegalArgumentException("잘못된 게시글 번호 입니다.");
        }

        if(!findComment.getAccount().equals(account)){
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }


        findComment.editComment(commentForm.getContent());

        return new CommentEditResponse(findComment.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentsReadResponse readCommentLatest10(Long articleId, int index) {

        Slice<Comment> sliceComments = commentRepository.findSliceComments(articleId, PageRequest.of(index, 10));

        List<CommentReadResponse> lists = sliceComments.getContent()
                .stream()
                .map(comment -> new CommentReadResponse(comment.getId(), comment.getAccount().getUserId(), comment.getContent(), comment.getCreatedDate(), comment.getLastModifiedDate()))
                .sorted(Comparator.comparing(CommentReadResponse::getModifiedDate).reversed())
                .collect(Collectors.toList());

        return new CommentsReadResponse(sliceComments.hasNext(), lists);
    }

    @Override
    public void delete(Long index, String userId) {

        Comment findComment = commentRepository.findCommentsById(index);

        if(!findComment.getAccount().getUserId().equals(userId)){
            throw new IllegalArgumentException("삭제할 권한이 없습니다!");
        }

        findComment.deActive();

    }
}
