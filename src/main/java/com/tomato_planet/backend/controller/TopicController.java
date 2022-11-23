package com.tomato_planet.backend.controller;

import com.tomato_planet.backend.common.BaseResponse;
import com.tomato_planet.backend.common.ResultUtils;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.model.dto.TopicDTO;
import com.tomato_planet.backend.model.entity.Collection;
import com.tomato_planet.backend.model.entity.Comment;
import com.tomato_planet.backend.model.entity.Topic;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.DeleteRequest;
import com.tomato_planet.backend.model.request.TopicAddRequest;
import com.tomato_planet.backend.model.request.TopicCommentRequest;
import com.tomato_planet.backend.model.request.TopicUpdateRequest;
import com.tomato_planet.backend.model.vo.CommentVO;
import com.tomato_planet.backend.model.vo.TopicVO;
import com.tomato_planet.backend.service.CollectionService;
import com.tomato_planet.backend.service.CommentService;
import com.tomato_planet.backend.service.TopicService;
import com.tomato_planet.backend.util.ImageUtils;
import com.tomato_planet.backend.util.UserHolder;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 主题控制器
 *
 * @author jianping5
 * @createDate 2022/10/29 13:44
 */
@RestController
@RequestMapping("/topic")
@ApiOperation("主题控制器")
public class TopicController {

    @Resource
    private TopicService topicService;

    @Resource
    private ImageUtils imageUtils;

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public BaseResponse<Long> addTopic(@RequestPart TopicAddRequest topicAddRequest,
                                          @RequestPart(required = false) MultipartFile[] files) {
        if (topicAddRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User loginUser = UserHolder.getUser();
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicAddRequest, topic);
        long topicId = topicService.addTopic(topic, loginUser, files);
        return ResultUtils.success(topicId);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTopic(@RequestPart TopicUpdateRequest topicUpdateRequest,
                                             @RequestPart(required = false) MultipartFile[] files) {
        if (topicUpdateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User loginUser = UserHolder.getUser();
        boolean result = topicService.updateTopic(topicUpdateRequest, loginUser, files);
        if (!result) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "主题更新失败");
        }
        return ResultUtils.success(true);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteTopic(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User loginUser = UserHolder.getUser();
        boolean result = topicService.deleteTopic(id, loginUser);
        if (!result) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "主题删除失败");
        }
        return ResultUtils.success(true);
    }


    @GetMapping("/search")
    public BaseResponse<List<TopicVO>> searchTopic(@RequestParam String keyWords) {
        if (StringUtils.isBlank(keyWords)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        List<TopicVO> topicVOList = topicService.searchTopic(keyWords);
        return ResultUtils.success(topicVOList);
    }

    @GetMapping("/list")
    public BaseResponse<List<TopicVO>> listTopicByTag(@RequestParam String tag) {
        if (StringUtils.isBlank(tag)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        List<TopicVO> topicVOList = topicService.listTopicByTag(tag);
        return ResultUtils.success(topicVOList);
    }

    @PostMapping("/view")
    public BaseResponse<TopicVO> viewTopicDetail(@RequestBody TopicDTO topicViewDTO) {
        if (topicViewDTO == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Long id = topicViewDTO.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        TopicVO topicVO = topicService.viewTopicDetail(id);
        return ResultUtils.success(topicVO);
    }


    @PostMapping("/like")
    public BaseResponse<Boolean> likeTopic(@RequestBody TopicDTO topicDTO) {
        if (topicDTO == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Long id = topicDTO.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User loginUser = UserHolder.getUser();
        boolean result = topicService.likeTopic(id, loginUser);
        return ResultUtils.success(result);
    }

    @PutMapping("/like/{topicId}")
    public BaseResponse<Boolean> likeTopic(@PathVariable Long topicId) {
        if (topicId == null || topicId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        boolean result = topicService.likeTopic(topicId);
        if (!result) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "点赞失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/collect")
    public BaseResponse<Boolean> collectTopic(@RequestBody TopicDTO topicDTO) {
        if (topicDTO == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Long id = topicDTO.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        User loginUser = UserHolder.getUser();
        boolean result = topicService.collectTopic(id, loginUser);
        return ResultUtils.success(result);
    }

    @GetMapping("/collect/list")
    public BaseResponse<List<TopicVO>> listCollectedTopic() {
        List<TopicVO> topicVOList = topicService.listCollectedTopic();
        return ResultUtils.success(topicVOList);
    }

    @GetMapping("/publish/list")
    public BaseResponse<List<TopicVO>> listPublishTopic() {
        List<TopicVO> topicVOList = topicService.listPublishTopic();
        return ResultUtils.success(topicVOList);
    }

    @PostMapping("/image/upload")
    public BaseResponse<Map<String, List<String>>> uploadImage(@RequestParam(value = "files",required = false) MultipartFile[] files){
        if(ObjectUtils.isEmpty(files)){
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Map<String, List<String>> uploadImagesUrl = imageUtils.uploadImages(files);
        return ResultUtils.success(uploadImagesUrl);
    }

    @PostMapping("/comment")
    public BaseResponse<Boolean> commentTopic(@RequestBody TopicCommentRequest topicCommentRequest) {
        if (topicCommentRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(topicCommentRequest, comment);
        boolean result = commentService.commentTopic(comment);
        if (!result) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "评论失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/comment/list/{topicId}")
    public BaseResponse<List<CommentVO>> listTopicComment(@PathVariable Long topicId) {
        if (topicId == null || topicId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        List<CommentVO> commentVOList = commentService.listTopicComment(topicId);
        return ResultUtils.success(commentVOList);
    }

}
