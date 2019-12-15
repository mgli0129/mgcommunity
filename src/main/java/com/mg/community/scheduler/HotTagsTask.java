package com.mg.community.scheduler;

import com.github.pagehelper.PageHelper;
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

    @Scheduled(fixedRate = 5000)
    public void getHotTopics() {

        int offSet = 0;
        int limit = 5;
        List<Question> questions = new ArrayList<>();
        Map<String, Long> priorities = new HashMap<>();

        log.info("Get hot tags starting time: {}", new Date());

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
        for (String s : priorities.keySet()) {
            Long value = (Long) priorities.get(s);
            System.out.println(s + ": " + value);
        }
        log.info("Get hot tags end time: {}", new Date());
    }

}
