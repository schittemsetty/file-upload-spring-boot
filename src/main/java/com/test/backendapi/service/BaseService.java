package com.test.backendapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.test.backendapi.document.Color;
import com.test.backendapi.document.Group;
import com.test.backendapi.document.User;
import com.test.backendapi.exception.handler.CustomException;
import com.test.backendapi.repository.ColorRepository;
import com.test.backendapi.repository.GroupRepository;
import com.test.backendapi.repository.UserRepository;
import com.test.backendapi.util.JWTUtil;

@Service
public class BaseService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ColorRepository colorRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private SequenceGeneratorService generatorService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTUtil jwtUtil;

	public String signIn(User user) {
		String token = "";
		User existingUser = userRepository.findByEmail(user.getEmail());
		
		if (existingUser == null) {
			throw new CustomException("Email not found", 404);
		}

		if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
			token = jwtUtil.generateToken(user.getEmail());
		} else {
			throw new CustomException("Incorrect Password", 404);
		}

		return token;
	}

	public User register(User user) {
		User existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser != null) {
			throw new CustomException("Email already exists", 401);
		}
		String pass = passwordEncoder.encode(user.getPassword());
		user.setPassword(pass);
		userRepository.save(user);
		return user;
	}

	public List<Group> getUsers(Map<String, String> users, String groupName, Group existingGroup) {
		List<Group> groups = new ArrayList<Group>();
		for (Map.Entry<String, String> entry : users.entrySet()) {
			Group userexists = groupRepository.findByGroupAndUser(groupName, entry.getKey());

			Color color = colorRepository.findByColor(entry.getValue());

			if (color == null) {
				color = createColor(entry.getValue());
			}

			if (userexists != null && existingGroup != null) {
				if (!existingGroup.getColor().equals(entry.getValue())) {
					existingGroup.setColor(entry.getValue());
					existingGroup = groupRepository.save(existingGroup);
				}

			} else {
				Group group = new Group();
				group.setColor(entry.getValue());
				group.setGroup(groupName);
				group.setUser(entry.getKey());
				group.setId(generatorService.generateSequence(Group.SEQUENCE_NAME));

				existingGroup = groupRepository.save(group);
			}
			groups.add(existingGroup);
		}
		return groups;
	}

	public Map<String, Object> getAll(String color, int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		Group group = new Group();
		group.setColor(color);
		List<Group> groups = groupRepository.findAllByColor(color, pageable);

		Map<String, Object> map = new HashMap<String, Object>();

		if (!CollectionUtils.isEmpty(groups)) {
			map.put("data", groups);
			map.put("totalCount", groups.size());
		}

		return map;

	}

	@Transactional
	public List<Group> createGroup(Map<String, Map<String, String>> groups) {

		List<Group> group = new ArrayList<Group>();
		for (Entry<String, Map<String, String>> entry : groups.entrySet()) {
			Group existingGroup = groupRepository.findByGroup(entry.getKey());
			List<Group> newGrops = getUsers(entry.getValue(), entry.getKey(), existingGroup);

			group.addAll(newGrops);
		}

		return group;
	}

	public Color createColor(String colorName) {
		Color color = new Color();
		color.setColor(colorName);
		colorRepository.save(color);
		return color;
	}

	public List<Color> getAllColors() {

		Sort sort = Sort.by(Order.asc("color"));
		return colorRepository.findAll(sort);
	}

}
