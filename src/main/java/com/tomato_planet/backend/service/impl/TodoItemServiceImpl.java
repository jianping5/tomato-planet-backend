package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.constant.ClockType;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.mapper.TodoItemMapper;
import com.tomato_planet.backend.model.entity.FocusRecord;
import com.tomato_planet.backend.model.entity.TodoItem;
import com.tomato_planet.backend.model.entity.Topic;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.TodoItemUpdateRequest;
import com.tomato_planet.backend.model.vo.TodoItemVO;
import com.tomato_planet.backend.service.FocusRecordService;
import com.tomato_planet.backend.service.TodoItemService;
import com.tomato_planet.backend.service.TopicService;
import com.tomato_planet.backend.util.ImageUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.tomato_planet.backend.constant.TodoItemConstant.*;

/**
* @author jianping5
* @description 针对表【todo_item(待办项表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class TodoItemServiceImpl extends ServiceImpl<TodoItemMapper, TodoItem>
    implements TodoItemService{

    @Resource
    private FocusRecordService focusRecordService;

    @Resource
    private ImageUtils imageUtils;

    @Resource
    private TopicService topicService;

    @Override
    public long addTodoItem(TodoItem todoItem, User loginUser) {
        // 请求参数是否为空
        if (todoItem == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 是否登录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        // 校验待办名称长度 <= 30
        String todoName = todoItem.getTodoName();
        if (StringUtils.isNotBlank(todoName) && todoName.length() > TODO_NAME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办名称过长");
        }
        // 校验待办总时长（分钟） [1,180] 整数
        Integer todoTotalTime = todoItem.getTodoTotalTime();
        if (todoTotalTime == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办时长不能为空");
        }
        if (todoTotalTime <= TODO_TOTAL_SHORTEST_TIME_LENGTH && todoTotalTime > TODO_TOTAL_LONGEST_TIME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办时长不满足要求");
        }

        // TODO 背景图片

        // 插入待办项,并设置待办项对应用户id
        todoItem.setUserId(userId);
        boolean result = this.save(todoItem);
        Long todoItemId = todoItem.getId();
        if (!result || todoItemId == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "添加待办失败");
        }
        return todoItemId;

    }

    @Override
    public boolean deleteTodoItem(long id, User loginUser) {
        TodoItem todoItem = getTodoItemById(id);
        Long userId = todoItem.getUserId();
        // 校验是否有权限
        if (!userId.equals(loginUser.getId())) {
            throw new BusinessException(StatusCode.NO_AUTH, "无权限");
        }
        return this.removeById(id);
    }

    @Override
    public boolean updateTodoItem(TodoItemUpdateRequest todoItemUpdateRequest, User loginUser) {
        // 是否登录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN);
        }
        final long loginUserId = loginUser.getId();
        // 校验待办名称长度 <= 30
        String todoName = todoItemUpdateRequest.getTodoName();
        if (StringUtils.isNotBlank(todoName) && todoName.length() > TODO_NAME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办名称过长");
        }
        // 校验待办总时长（分钟） [1,180] 整数
        Integer todoTotalTime = todoItemUpdateRequest.getTodoTotalTime();
        if (todoTotalTime == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办时长不能为空");
        }
        if (todoTotalTime <= TODO_TOTAL_SHORTEST_TIME_LENGTH && todoTotalTime >= TODO_TOTAL_LONGEST_TIME_LENGTH) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "待办时长不满足要求");
        }

        // 校验旧的待办是否存在
        Long id = todoItemUpdateRequest.getId();
        if (id == null || id < 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        TodoItem oldTodoItem = this.getById(id);
        if (oldTodoItem == null) {
            throw new BusinessException(StatusCode.NULL_ERROR, "待办不存在");
        }

        // 校验是否有权限
        long userId = oldTodoItem.getUserId();
        if (loginUserId != userId) {
            throw new BusinessException(StatusCode.NO_AUTH, "无权限");
        }

        TodoItem todoItem = new TodoItem();
        BeanUtils.copyProperties(todoItemUpdateRequest, todoItem);

        return this.updateById(todoItem);
    }

    @Override
    public List<TodoItemVO> listTodoItems(User loginUser) {

        // 获取当前登录用户的id，并查询与之相关的所有待办项
        long userId = loginUser.getId();
        QueryWrapper<TodoItem> todoItemQueryWrapper = new QueryWrapper<>();
        todoItemQueryWrapper.eq("user_id", userId);
        List<TodoItem> todoItemList = this.list(todoItemQueryWrapper);

        // 判空
        if (CollectionUtils.isEmpty(todoItemList)) {
            return new ArrayList<>();
        }
        List<TodoItemVO> todoItemVOList = new ArrayList<>();

        // 遍历所有待办项
        for (TodoItem todoItem : todoItemList) {
            TodoItemVO todoItemVO = new TodoItemVO();
            BeanUtils.copyProperties(todoItem, todoItemVO);
            // 查询该待办项对应的专注记录
            QueryWrapper<FocusRecord> focusRecordQueryWrapper = new QueryWrapper<>();
            focusRecordQueryWrapper.eq("user_id", userId).eq("todo_item_id", todoItem.getId());
            List<FocusRecord> focusRecordList = focusRecordService.list(focusRecordQueryWrapper);
            // 专注记录为空，则该待办项专注时长为0
            if (CollectionUtils.isEmpty(focusRecordList)) {
                todoItemVO.setFocusTotalTime(0);
                todoItemVOList.add(todoItemVO);
                continue;
            }
            // 遍历其所有专注记录，设置专注总时长
            int focusTotalTime = 0;
            for (FocusRecord focusRecord : focusRecordList) {
                int focusTime = focusRecord.getFocusTime();
                focusTotalTime += focusTime;
            }
            todoItemVO.setFocusTotalTime(focusTotalTime);
            todoItemVOList.add(todoItemVO);
        }
        return todoItemVOList;
    }

    @Override
    public long clockShare(User loginUser, MultipartFile file, Integer clockType) {
        // 判断文件是否为空
        if (file == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        // 判断请求类型是否为空以及格式
        if (clockType == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        // 声明 topic
        Topic topic;

        // 根据不同类型区分
        switch (clockType) {
            case 0:
                topic = getInjectedTopic(ClockType.MORNING, loginUser, file);
                break;
            case 1:
                topic = getInjectedTopic(ClockType.FOCUS, loginUser, file);
                break;
            case 2:
                topic = getInjectedTopic(ClockType.EVENING, loginUser, file);
                break;
            default:
                throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        // 插入topic表中
        boolean result = topicService.save(topic);
        if (!result) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "打卡分享失败");
        }

        // 获取主题id，并返回
        Long topicId = topic.getId();
        if (topicId == null || topicId <= 0) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "打卡分享失败");
        }

        return topicId;
    }

    /**
     * 根据id查询对应待办
     * @param id
     * @return
     */
    private TodoItem getTodoItemById(Long id) {
        // 判断id是否合格
        if (id == null || id < 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }

        // 从数据库中查找该待办
        TodoItem todoItem = this.getById(id);
        if (todoItem == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "该待办不存在");
        }
        return todoItem;
    }


    /**
     * 获取注入属性后的主题
     * @param clockType
     * @param loginUser
     * @param file
     * @return
     */
    private Topic getInjectedTopic(ClockType clockType, User loginUser, MultipartFile file) {
        Topic topic = new Topic();

        // 注入打卡基本属性
        topic.setTopicTitle(clockType.getClockTitle());
        topic.setTopicContent(clockType.getClockContent());
        topic.setTag(clockType.getClockTag());

        // 注入当前登录用户的id
        Long loginUserId = loginUser.getId();
        topic.setUserId(loginUserId);

        // 注入图片url
        if (file != null && !"".equals(file.getOriginalFilename())) {
            String imageUrl = imageUtils.uploadImageQiniu(file);
            topic.setImageUrl(imageUrl);
        }

        return topic;
    }

}




