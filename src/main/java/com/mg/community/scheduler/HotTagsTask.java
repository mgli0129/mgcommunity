package com.mg.community.scheduler;

import com.github.pagehelper.PageHelper;
import com.mg.community.cache.HotTopicsDataCache;
import com.mg.community.cache.PriorityCache;
import com.mg.community.dto.HotTopicDataDTO;
import com.mg.community.model.Question;
import com.mg.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName HotTagsTask
 * @Description 热门话题定时任务
 * @Author MGLi
 * @Date 2019/12/15 19:47
 * @Version 1.0
 */
@Component
@Slf4j
public class HotTagsTask {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PriorityCache priorityCache;

    @Autowired
    private HotTopicsDataCache hotTopicsDataCache;

    /**
     * 热门话题的权值计算公式：
     *    权值 = 5 + 标签 + 问题回复数
     * @每8小时刷新一次
     */
    @Scheduled(fixedRate = 1000*60*60*8)
    public void getHotTopics() {

        int offSet = 0;
        int limit = 5;
        List<Question> questions = new ArrayList<>();
        Map<String, Long> priorities = new HashMap<>();
        List<String> outPut = new ArrayList<>();

        log.info("getHotTopics start: {}", new Date());

        //得到标签和权值的无序的Map
        while(offSet == 0 || questions.size() == 5){
            PageHelper.startPage(offSet, limit);
            questions = questionService.findAllBySearch(null);
            for (Question question : questions) {
                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    Long priority = priorities.get(tag);
                    if(priority != null){
                        priorities.put(tag, priority + 5 + question.getCommentCount());
                    }else{
                        priorities.put(tag, 5 + question.getCommentCount());
                    }
                }
            }
            offSet ++;
        }

        //选出前X位排名
        priorityCache.sortPriorites(priorities);

        log.info("getHotTopics stop: {}", new Date());
    }

    /**
     * 获取热门话题的统计信息，每4个小时更新一次
     */
    @Scheduled(fixedRate = 1000*60*60*4)
    public void getHotTopicData() {
        hotTopicsDataCache.getDatas();
    }

}
