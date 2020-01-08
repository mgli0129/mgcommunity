package com.mg.community.cache;

import com.mg.community.dto.HotTopicDataDTO;
import com.mg.community.model.Question;
import com.mg.community.service.QuestionService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName HotTopicsData
 * @Description 统计热门话题的相关信息
 * @Author MGLi
 * @Date 2019/12/23 11:29
 * @Version 1.0
 */
@Component
@Data
public class HotTopicsDataCache {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PriorityCache priorityCache;

    private List<HotTopicDataDTO> hotTopicDataDTOS = new ArrayList<>();

    /**
     * 获取所有热门话题的统计信息
     */
    public void getDatas(){
        if(priorityCache.getHots() == null || priorityCache.getHots().size() == 0){
            return;
        }

        Question question = new Question();
        hotTopicDataDTOS = priorityCache.getHots().stream().map(h -> {
            question.setTag(h);
            return questionService.getHotTopicDatas(question);
        }).collect(Collectors.toList());
    }

    /**
     * 获取当前访问的热门话题的统计信息
     * @param tag
     * @return
     */
    public HotTopicDataDTO getCurrentData(String tag){
        if(StringUtils.isBlank(tag)){
            return null;
        }
        HotTopicDataDTO hotTopicDataDTO = new HotTopicDataDTO();
        if(hotTopicDataDTOS != null) {
            for (HotTopicDataDTO data : hotTopicDataDTOS) {
                if (data.getTag().equals(tag)) {
                    BeanUtils.copyProperties(data, hotTopicDataDTO);
                    return hotTopicDataDTO;
                }
            }
        }
        return null;
    }

}
