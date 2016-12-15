package com.dreamer.view.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by huangfei on 16/4/25.
 */
@Controller
public class UploadImageQueryController {
    @RequestMapping("/test")
    public String test(){
        return "upload/index";
    }

}
