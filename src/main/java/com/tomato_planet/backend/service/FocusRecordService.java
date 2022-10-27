package com.tomato_planet.backend.service;

import com.tomato_planet.backend.model.entity.FocusRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tomato_planet.backend.model.entity.User;
import com.tomato_planet.backend.model.request.FocusEndRequest;

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
}
