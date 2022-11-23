package com.tomato_planet.backend.service;

import com.tomato_planet.backend.model.entity.FocusRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.FocusEndRequest;
import com.tomato_planet.backend.model.vo.FocusDataComplexVO;
import com.tomato_planet.backend.model.vo.FocusDataSimpleVO;

import java.util.List;

/**
* @author jianping5
* @description 针对表【focus_record(专注记录表)】的数据库操作Service
* @createDate 2022-10-24 11:52:01
*/
public interface FocusRecordService extends IService<FocusRecord> {

    /**
     * 结束专注状态
     * @param focusRecord
     * @param loginUser
     * @return
     */
    long endFocusState(FocusRecord focusRecord, User loginUser);

    /**
     * 获取专注记录简单视图
     * @param loginUser
     * @return
     */
    FocusDataSimpleVO getFocusDataSimple(User loginUser);


    /**
     * 获取专注记录复杂视图
     * @param loginUser
     * @param timeType
     * @return
     */
    List<FocusDataComplexVO> getFocusDataComplex(User loginUser, int timeType);
}
