package com.mg.community.cache;

import com.mg.community.dto.PriorityDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @ClassName PriorityCache
 * @Description 小顶堆排序
 * @Author MGLi
 * @Date 2019/12/16 13:57
 * @Version 1.0
 */

@Component
@Data
public class PriorityCache {

    private List<String> hots = new ArrayList<>();

    public void sortPriorites(Map<String, Long> sources) {
        int max = 5;
        PriorityQueue<PriorityDTO> priorityDTOS = new PriorityQueue<>(max);

        for (String s : sources.keySet()) {

            if (s != null) {
                PriorityDTO priorityDTO = new PriorityDTO();
                priorityDTO.setTag(s);
                priorityDTO.setPriority((Long) sources.get(s));
                if (priorityDTOS.size() < max) {
                    priorityDTOS.add(priorityDTO);
                } else {
                    PriorityDTO minPriority = priorityDTOS.peek();
                    if (priorityDTO.compareTo(minPriority) > 0) {
                        priorityDTOS.poll();
                        priorityDTOS.add(priorityDTO);
                    }
                }

            }
        }
        List<String> sortedPros = new ArrayList<>();
        PriorityDTO p = priorityDTOS.poll();
        while (p != null) {
            //使用List的index为0，可以不断插入进去，可以做到先进后出，也就是将小顶堆的转化为大顶List
            sortedPros.add(0, p.getTag());
            p = priorityDTOS.poll();
        }
        hots = sortedPros;
    }
}
