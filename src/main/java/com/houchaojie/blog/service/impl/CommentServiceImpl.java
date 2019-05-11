package com.houchaojie.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.houchaojie.blog.entity.Article;
import com.houchaojie.blog.entity.Comment;
import com.houchaojie.blog.enums.ArticleStatus;
import com.houchaojie.blog.mapper.ArticleMapper;
import com.houchaojie.blog.mapper.CommentMapper;
import com.houchaojie.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 侯超杰
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Override
    @CachePut(value = "default", key = "'comment:'+#comment.commentId")
    public void insertComment(Comment comment) {
        try {
            commentMapper.insert(comment);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("创建评论失败：comment:{}, cause:{}", comment, e);
        }
    }

    @Override
    public List<Comment> listCommentByArticleId(Integer articleId) {
        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listCommentByArticleId(articleId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据文章ID获得评论列表失败，articleId:{},cause:{}", articleId, e);
        }
        return commentList;
    }

    @Override
    @Cacheable(value = "default", key = "'comment:'+#id")
    public Comment getCommentById(Integer id) {
        Comment comment = null;
        try {
            comment = commentMapper.getCommentById(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据评论ID获得评论，id:{}, cause:{}", id, e);
        }
        return comment;
    }

    @Override
    public PageInfo<Comment> listCommentByPage(Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listComment();
            for (Comment comment : commentList) {
                Article article = articleMapper.getArticleByStatusAndId( ArticleStatus.PUBLISH.getValue(), comment.getCommentArticleId() );
                comment.setArticle( article );
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分页获得评论失败,pageIndex:{}, pageSize:{}, cause:{}", pageIndex, pageSize, e);
        }
        return new PageInfo<>(commentList);
    }

    @Override
    public List<Comment> listComment() {
        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listComment();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得评论列表失败：cause:{}", e);
        }
        return commentList;
    }

    @Override
    public void deleteComment(Integer id) {
        try {
            commentMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除评论失败, id:{}, cause:{}", id, e);
        }
    }

    @Override
    @CacheEvict(value = "default", key = "'comment:'+#comment.commentId")
    public void updateComment(Comment comment) {
        try {
            commentMapper.update(comment);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新评论，comment:{}, cause:{}", comment, e);
        }
    }

    @Override
    public Integer countComment() {
        Integer commentCount = null;
        try {
            commentCount = commentMapper.countComment();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统计评论数量失败, cause:{}", e);
        }
        return commentCount;
    }

    @Override
    public List<Comment> listRecentComment(Integer limit) {
        List<Comment> commentList = null;
        try {
            commentList = commentMapper.listRecentComment(limit);
            for (Comment comment : commentList) {
                Article article = articleMapper.getArticleByStatusAndId( ArticleStatus.PUBLISH.getValue(), comment.getCommentArticleId() );
                comment.setArticle( article );
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得最新评论失败, limit:{}, cause:{}", limit, e);
        }
        return commentList;
    }

    @Override
    public List<Comment> listChildComment(Integer id) {
        List<Comment> childCommentList = null;
        try {
            childCommentList = commentMapper.listChildComment(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获得子评论失败, id:{}, cause:{}", id, e);
        }
        return childCommentList;
    }
}
