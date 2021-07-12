package com.test.backendapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.test.backendapi.document.Group;

@Component
public class ModelListener extends AbstractMongoEventListener<Group> {

	private SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	public ModelListener(SequenceGeneratorService sequenceGeneratorService) {
		this.sequenceGeneratorService = sequenceGeneratorService;
	}

	@Override
	public void onBeforeConvert(BeforeConvertEvent<Group> event) {
		if (event.getSource().getId() < 1) {
			event.getSource().setId(sequenceGeneratorService.generateSequence(Group.SEQUENCE_NAME));
		}
	}
}
