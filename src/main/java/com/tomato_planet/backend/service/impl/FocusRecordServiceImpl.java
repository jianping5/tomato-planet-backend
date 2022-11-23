package com.tomato_planet.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tomato_planet.backend.common.StatusCode;
import com.tomato_planet.backend.constant.TimeTypeConstant;
import com.tomato_planet.backend.exception.BusinessException;
import com.tomato_planet.backend.mapper.TodoItemMapper;
import com.tomato_planet.backend.model.entity.FocusRecord;
import com.tomato_planet.backend.model.entity.TodoItem;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.vo.FocusDataComplexVO;
import com.tomato_planet.backend.model.vo.FocusDataSimpleVO;
import com.tomato_planet.backend.service.FocusRecordService;
import com.tomato_planet.backend.mapper.FocusRecordMapper;
import com.tomato_planet.backend.service.TodoItemService;
import com.tomato_planet.backend.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
* @author jianping5
* @description 针对表【focus_record(专注记录表)】的数据库操作Service实现
* @createDate 2022-10-24 11:52:01
*/
@Service
public class FocusRecordServiceImpl extends ServiceImpl<FocusRecordMapper, FocusRecord>
    implements FocusRecordService{

    @Resource
    private TodoItemService todoItemService;

    @Resource
    private TodoItemMapper todoItemMapper;

    @Override
    public long endFocusState(FocusRecord focusRecord, User loginUser) {
        // 校验请求是否为空
        if (focusRecord == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        // 获取登录用户的id
        long loginUserId = loginUser.getId();

        // 校验对应待办项是否存在
        Long todoItemId = focusRecord.getTodoItemId();
        if (todoItemId == null || todoItemId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR);
        }
        TodoItem todoItem = todoItemService.getById(todoItemId);
        if (todoItem == null) {
            throw new BusinessException(StatusCode.NULL_ERROR, "对应待办不存在");
        }

        // 校验专注时长
        Integer focusTime = focusRecord.getFocusTime();
        if (focusTime == null || focusTime < 1) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "专注时长过短");
        }

        // 插入专注记录表中（设置对应userId）
        focusRecord.setUserId(loginUserId);
        boolean result = this.save(focusRecord);
        Long focusRecordId = focusRecord.getId();
        if (!result || focusRecordId == null || focusRecordId <= 0) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "添加专注记录失败");
        }

        return focusRecordId;
    }

    @Override
    public FocusDataSimpleVO getFocusDataSimple(User loginUser) {
        // 获取当前登录用户的id
        Long loginUserId = loginUser.getId();


        // 查询该用户的所有专注记录
        QueryWrapper<FocusRecord> focusRecordQueryWrapper = new QueryWrapper<>();
        focusRecordQueryWrapper.eq("user_id", loginUserId);
        List<FocusRecord> focusRecordList = this.list(focusRecordQueryWrapper);

        // 初始化
        Long totalCount = 0L;
        Long totalTimeLength = 0L;
        Integer dayCount = 0;
        Integer dayTimeLength = 0;
        LocalDateTime now = LocalDateTime.now();

        // 遍历专注记录，获取累积和当日次数及时长
        for (FocusRecord focusRecord : focusRecordList) {
            if (TimeUtils.isSameDay(focusRecord.getFocusEndTime(), now)) {
                dayCount ++;
                dayTimeLength += focusRecord.getFocusTime();
            }
            totalCount ++;
            totalTimeLength += focusRecord.getFocusTime();
        }

        // 获取专注数据简单视图体
        FocusDataSimpleVO focusDataSimpleVO = injectToFocusDataSimpleVO(totalCount, totalTimeLength, dayCount, dayTimeLength);

        return focusDataSimpleVO;
    }

    @Override
    public List<FocusDataComplexVO> getFocusDataComplex(User loginUser, int timeType) {
        Long loginUserId = loginUser.getId();

        // 查询该用户的所有专注记录
        QueryWrapper<FocusRecord> focusRecordQueryWrapper = new QueryWrapper<>();
        focusRecordQueryWrapper.eq("user_id", loginUserId);
        List<FocusRecord> focusRecordList = this.list(focusRecordQueryWrapper);

        // 遍历专注记录，获取待办项id（去重）
        Set<Long> todoItemIds = new HashSet<>();
        focusRecordList.forEach(focusRecord -> {
            todoItemIds.add(focusRecord.getTodoItemId());
        });

        // 根据所有待办项id查询所有待办项，并将待办项id和对应的待办项名称封装进map（注意逻辑删除的也需要查）
        List<TodoItem> todoItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(todoItemIds)) {
            todoItemList = todoItemMapper.listAllByIds(todoItemIds);
        }

        // List<TodoItem> todoItemList = todoItemService.listByIds(todoItemIds);
        Map<Long, String> focusNameMap = new HashMap<>();
        todoItemList.forEach(todoItem -> {
            Long todoItemId = todoItem.getId();
            focusNameMap.put(todoItemId, todoItem.getTodoName());
        });

        // 初始化
        LocalDateTime now = LocalDateTime.now();

        // 创建map，key 为待办项名称， value 为待办项总时长
        Map<String, Long> focusDataComplexMap = new HashMap<>();

        // 日
        if (timeType == TimeTypeConstant.DAY) {
            for (FocusRecord focusRecord : focusRecordList) {
                if (TimeUtils.isSameDay(focusRecord.getFocusEndTime(), now)) {
                    handleFocusDataComplexMap(focusRecord, focusNameMap, focusDataComplexMap);
                }
            }
        }

        // 周
        if (timeType == TimeTypeConstant.WEEK) {
            for (FocusRecord focusRecord : focusRecordList) {
                if (TimeUtils.isSameWeek(focusRecord.getFocusEndTime(), now)) {
                    handleFocusDataComplexMap(focusRecord, focusNameMap, focusDataComplexMap);
                }
            }
        }

        // 月
        if (timeType == TimeTypeConstant.MONTH) {
            for (FocusRecord focusRecord : focusRecordList) {
                if (TimeUtils.isSameMonth(focusRecord.getFocusEndTime(), now)) {
                    handleFocusDataComplexMap(focusRecord, focusNameMap, focusDataComplexMap);
                }
            }

        }


        // 年
        if (timeType == TimeTypeConstant.YEAR) {
            for (FocusRecord focusRecord : focusRecordList) {
                if (TimeUtils.isSameYear(focusRecord.getFocusEndTime(), now)) {
                    handleFocusDataComplexMap(focusRecord, focusNameMap, focusDataComplexMap);
                }
            }
        }

        // 注入属性
        List<FocusDataComplexVO> focusDataComplexVOList = injectToFocusDataComplexVO(focusDataComplexMap);

        // 返回数组
        return focusDataComplexVOList;
    }

    /**
     * 注入属性到专注数据简单视图体
     * @param totalCount
     * @param totalTimeLength
     * @param dayCount
     * @param dayTimeLength
     * @return
     */
    public FocusDataSimpleVO injectToFocusDataSimpleVO(Long totalCount, Long totalTimeLength, Integer dayCount, Integer dayTimeLength) {
        FocusDataSimpleVO focusDataSimpleVO = new FocusDataSimpleVO();

        // 拆分累积专注时长
        Long totalHours = totalTimeLength/60;
        Long totalMinutes = totalTimeLength - totalHours*60;

        // 拆分累积平均专注时长
        Long totalAveTimeLength = totalTimeLength/totalCount;
        Long totalAveHours = totalAveTimeLength/60;
        Long totalAveMinutes = totalAveTimeLength - totalAveHours*60;

        // 拆分当日专注时长
        Integer dayHours = dayTimeLength/60;
        Integer dayMinutes = dayTimeLength - dayHours*60;

        // 注入属性
        focusDataSimpleVO.setFocusCount(totalCount);
        focusDataSimpleVO.setFocusHours(totalHours);
        focusDataSimpleVO.setFocusMinutes(totalMinutes);
        focusDataSimpleVO.setFocusAveHours(totalAveHours);
        focusDataSimpleVO.setFocusAveMinutes(totalAveMinutes);
        focusDataSimpleVO.setFocusDayCount(dayCount);
        focusDataSimpleVO.setFocusDayHours(dayHours);
        focusDataSimpleVO.setFocusDayMinutes(dayMinutes);

        return focusDataSimpleVO;
    }


    /**
     * 注入属性到专注数据复杂视图体
     * @param focusDataComplexMap
     * @return
     */
    public List<FocusDataComplexVO> injectToFocusDataComplexVO(Map<String, Long> focusDataComplexMap) {
        // 创建专注数据复杂视图体数组

        List<FocusDataComplexVO> focusDataComplexVOList = new ArrayList<>();

        focusDataComplexMap.forEach((todoName, totalTimeLength) -> {

            // 拆分专注时长
            Long totalHours = totalTimeLength/60;
            Long totalMinutes = totalTimeLength - totalHours*60;

            // 创建专注数据复杂视图体并注入属性
            FocusDataComplexVO focusDataComplexVO = new FocusDataComplexVO();
            focusDataComplexVO.setTodoItemName(todoName);
            focusDataComplexVO.setFocusHours(totalHours);
            focusDataComplexVO.setFocusMinutes(totalMinutes);

            focusDataComplexVOList.add(focusDataComplexVO);

        });

        return focusDataComplexVOList;
    }

    /**
     * 处理专注数据复杂Map
     * @param focusRecord
     * @param focusNameMap
     * @param focusDataComplexMap
     */
    public void handleFocusDataComplexMap(FocusRecord focusRecord, Map<Long, String> focusNameMap, Map<String, Long> focusDataComplexMap) {
        String todoName = focusNameMap.get(focusRecord.getTodoItemId());
        Integer focusTime = focusRecord.getFocusTime();
        if (focusDataComplexMap.containsKey(todoName)) {
            focusDataComplexMap.put(todoName, focusDataComplexMap.get(todoName) + focusTime);
        } else {
            focusDataComplexMap.put(todoName, Long.valueOf(focusTime));
        }
    }

}