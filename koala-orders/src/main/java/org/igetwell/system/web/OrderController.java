package org.igetwell.system.web;

import org.igetwell.common.sequence.sequence.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {

    @Autowired
    private Sequence sequence;

    @PostMapping("/getNo")
    public String getNextNo(){
        System.err.println(sequence.nextValue());
        System.err.println(sequence.nextNo());
        return sequence.nextNo();
    }
}
