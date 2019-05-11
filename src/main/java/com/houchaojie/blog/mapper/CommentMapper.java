package com.houchaojie.blog.mapper;

import com.houchaojie.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 侯超杰
 */
@Mapper
public interface CommentMapper {
    /**
     *根据文章id获取评论列表
     * @param limit
     * @return
     */
    List<Comment> listRecentComment(@Param( value = "limit") Integer limit);

    List<Comment> listChildComment(Integer id);

    void insert(Comment comment);

    List<Comment> listCommentByArticleId(Integer articleId);

    Comment getCommentById(Integer id);

    List<Comment> listComment();

    void deleteById(Integer id);

    void update(Comment comment);

    Integer countComment();
}
