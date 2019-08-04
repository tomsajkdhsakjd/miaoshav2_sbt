package com.shendehai.com.web;

import com.shendehai.com.service.ICreateHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/createHtml")
public class CreateHtmlController {
	@Autowired
	private ICreateHtmlService create;
	@ResponseBody
   @RequestMapping(value="/start")
	public String Start(long itemid) {
    	System.out.println(itemid);
		String result = create.CreateItemDeatilHtml(itemid);
		return result;
	}
	@RequestMapping(value="/showdetails")
	public String showdetail(String itemid) {
		return itemid;
	}
}
