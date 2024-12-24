package com.babidzhonio.api.controllers;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectController {

}
