package com.zhousheng.tou_tiao.services;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Data
public class LanguageService {
    private List<String> lang1 = Lists.newArrayList(
       "以#为例子，",
            "#作为例子，",
            "以经典车型#为例，"
    );


}
