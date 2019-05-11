package com.houchaojie.blog.controller.home;

import com.houchaojie.blog.entity.Article;
import com.houchaojie.blog.entity.Comment;
import com.houchaojie.blog.enums.ArticleStatus;
import com.houchaojie.blog.enums.Role;
import com.houchaojie.blog.service.ArticleService;
import com.houchaojie.blog.service.CommentService;
import com.houchaojie.blog.util.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 文章评论
 * @author 侯超杰
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    /**
     * 添加评论
     * @param request
     * @param comment
     */
    @RequestMapping(value = "/comment", method = {RequestMethod.POST})
    @ResponseBody
    public void insertComment(HttpServletRequest request, Comment comment) {
        //添加评论
        comment.setCommentCreateTime(new Date());
        comment.setCommentIp( Functions.getIpAddr(request));
        if (request.getSession().getAttribute("user") != null) {
            comment.setCommentRole( Role.ADMIN.getValue());
        } else {
            comment.setCommentRole(Role.VISITOR.getValue());
        }
        comment.setCommentAuthorAvatar(Functions.getGravatar(comment.getCommentAuthorEmail()));
        commentService.insertComment(comment);

        //更新文章的评论数
        Article article = articleService.getArticleByStatusAndId(ArticleStatus.PUBLISH.getValue(), comment.getCommentArticleId());
        articleService.updateCommentCount(article.getArticleId());
    }

}
