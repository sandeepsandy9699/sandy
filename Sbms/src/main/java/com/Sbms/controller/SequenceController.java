package com.Sbms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Sbms.document.SequenceId;
import com.Sbms.service.SequenceService;

@RestController
@RequestMapping("/sequence")
public class SequenceController {

	@Autowired
	SequenceService sequenceService;

	@GetMapping("/getSequence/{key}")
	public long getSequence(@PathVariable String key) {
		return sequenceService.getNextSequenceId(key);
	}
	
	@PostMapping("/saveSequence")
	public String save(@RequestBody SequenceId seq) {
		sequenceService.saveSequence(seq);
		return "saved";
	}

}
