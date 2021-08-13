package com.toolbox.controller;

import com.toolbox.domain.Note;
import com.toolbox.vo.RequestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/note")
public class NoteController {


    @RequestMapping("/list")
    public RequestBean<Note> noteList(){
        String a = new String();

        return RequestBean.Success("a");
    }
}
