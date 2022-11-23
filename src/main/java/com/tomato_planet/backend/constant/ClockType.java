package com.tomato_planet.backend.constant;


/**
 * 打卡类型
 *
 * @author jianping5
 * @createDate 2022/11/6 12:12
 */
public enum ClockType {

    MORNING(0, "早起打卡", "我今天早起了！", "[\"打卡\"]"),
    FOCUS(1, "专注记录打卡", "今天过得很充实！", "[\"打卡\"]"),
    EVENING(2, "睡眠打卡", "我要睡觉了", "[\"打卡\"]");

    ClockType(int clockType, String clockTitle, String clockContent, String clockTag) {
        this.clockType = clockType;
        this.clockTitle = clockTitle;
        this.clockContent = clockContent;
        this.clockTag = clockTag;
    }

    /**
     * 打卡类型
     */
    private final int clockType;

    /**
     * 打卡标题
     */
    private final String clockTitle;

    /**
     * 打卡内容
     */
    private final String clockContent;

    /**
     * 打卡标签
     */
    private final String clockTag;


    public int getClockType() {
        return clockType;
    }

    public String getClockTitle() {
        return clockTitle;
    }

    public String getClockContent() {
        return clockContent;
    }

    public String getClockTag() {
        return clockTag;
    }
}
