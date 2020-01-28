package com.Sbms.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Sbms.document.Sbms;
import com.Sbms.repository.SbmsRepository;

@RequestMapping("/sbms")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SbmsController {

	@Autowired
	SbmsRepository sbmsRepository;

	@PostMapping("/save")
	public void SaveData() throws InterruptedException {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
		final LocalDateTime now = LocalDateTime.now();
		Sbms sbms = sbmsRepository.findByDate(dtf.format(now));
		List<Long> data = new ArrayList<Long>();
		List<String> label = new ArrayList<String>();
		if (sbms == null) {
			sbms = new Sbms();
			sbms.setDate(dtf.format(now));
			int random = 60 - ((int) Math.round((Math.random()) * (60 - 40)));
			data.add((long) random);
			sbms.setData(data);
			label.add(dtf.format(now) + " " + dtfTime.format(now));
			sbms.setLabel(label);
			sbmsRepository.save(sbms);
			System.out.println("data saved");
		}
		Sbms sbms1 = sbmsRepository.findByDate(dtf.format(now));
		do {
			final LocalDateTime now1 = LocalDateTime.now();
			int random = 60 - ((int) Math.round((Math.random()) * (60 - 40)));
			data.add((long) random);
			label.add(dtf.format(now1) + " " + dtfTime.format(now1));
			sbms1.setData(data);
			sbms1.setLabel(label);
			sbmsRepository.save(sbms1);
			Thread.sleep(10000);

		} while (sbms1 != null);

	}

	@GetMapping("/getData/{date}")
	public Sbms getData(@PathVariable String date) {
		System.out.println("Date: " + date);
		return sbmsRepository.findByDate(date);
	}

}
