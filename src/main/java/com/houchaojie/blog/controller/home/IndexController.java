package com.houchaojie.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.houchaojie.blog.entity.*;
import com.houchaojie.blog.enums.ArticleStatus;
import com.houchaojie.blog.enums.LinkStatus;
import com.houchaojie.blog.enums.NoticeStatus;
import com.houchaojie.blog.service.*;
import com.houchaojie.blog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author 侯超杰
 */
@Controller
public class IndexController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = {"/", "article"})
    public String index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                        Model model) {
        HashMap<String, Object> criteria = new HashMap<>( 1 );
        criteria.put( "status", ArticleStatus.PUBLISH.getValue() );
        //文章列表
        PageInfo<Article> articleList = articleService.pageArticle( pageIndex, pageSize, criteria );
        model.addAttribute( "pageInfo", articleList );

        //公告
        List<Notice> noticeList = noticeService.listNotice( NoticeStatus.NORMAL.getValue() );
        model.addAttribute( "noticeList", noticeList );
        //友情链接
        List<Link> linkList = linkService.listLink( LinkStatus.NORMAL.getValue() );
        model.addAttribute( "linkList", linkList );

        //侧边栏显示
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute( "allTagList", allTagList );
        //最新评论
        List<Comment> recentCommentList = commentService.listRecentComment( 10 );
        model.addAttribute( "recentCommentList", recentCommentList );
        model.addAttribute( "pageUrlPrefix", "/article?pageIndex" );
        return "Home/index";
    }

    @RequestMapping(value = "/search")
    public String search(
            @RequestParam("keywords") String keywords,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model) {
        //文章列表
        HashMap<String, Object> criteria = new HashMap<>( 2 );
        criteria.put( "status", ArticleStatus.PUBLISH.getValue() );
        criteria.put( "keywords", keywords );
        PageInfo<Article> articlePageInfo = articleService.pageArticle( pageIndex, pageSize, criteria );
        model.addAttribute( "pageInfo", articlePageInfo );

        //侧边栏显示
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute( "allTagList", allTagList );
        //获得随机文章
        List<Article> randomArticleList = articleService.listRandomArticle( 8 );
        model.addAttribute( "randomArticleList", randomArticleList );
        //获得热评文章
        List<Article> mostCommentArticleList = articleService.listArticleByCommentCount( 8 );
        model.addAttribute( "mostCommentArticleList", mostCommentArticleList );
        //最新评论
        List<Comment> recentCommentList = commentService.listRecentComment( 10 );
        model.addAttribute( "recentCommentList", recentCommentList );
        model.addAttribute( "pageUrlPrefix", "/search?pageIndex" );
        return "Home/Page/search";
    }

    @RequestMapping("/404")
    public String notFound(@RequestParam(required = false) String message, Model model) {
        model.addAttribute( "message", message );
        return "Home/Error/404";
    }

    @RequestMapping("/500")
    public String serverError(@RequestParam(required = false) String message, Model model) {
        model.addAttribute( "message", message );
        return "Home/Error/500";
    }

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @RequestMapping("/testRedis")
    @ResponseBody
    public String testRedis(@RequestParam(name = "key") String key) {
        return (String) redisUtil.get( key );
    }

}
